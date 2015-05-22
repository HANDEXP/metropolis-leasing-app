package org.hand.mas.utl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import org.hand.mas.metropolisleasing.application.MSApplication;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by gonglixuan on 15/3/26.
 */
public class LocalImageLoader {
    /**
     * 图片缓存的核心类
     */
    private LruCache<String, Bitmap> mLruCache;
    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    /**
     * 线程池的线程数量，默认为1
     */
    private int mThreadCount = 1;
    /**
     * 队列的调度方式
     */
    private Type mType = Type.LIFO;
    /**
     * 任务队列
     */
    private LinkedList<Runnable> mTasks;
    /**
     * 轮询的线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;

    /**
     * 运行在UI线程的handler，用于给ImageView设置图片
     */
    private Handler mHandler;

    /**
     * 引入一个值为1的信号量，防止mPoolThreadHander未初始化完成
     */
    private volatile Semaphore mSemaphore = new Semaphore(0);

    /**
     * 引入一个值为1的信号量，由于线程池内部也有一个阻塞线程，防止加入任务的速度过快
     */
    private volatile Semaphore mPoolSemaphore;

    private static LocalImageLoader mInstance;

    private class ImgBeanHolder
    {
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }

    private static class ImageSize
    {
        int width;
        int height;
    }

    /**
     * 队列的调度方式
     *
     * @author zhy
     *
     */
    public enum Type
    {
        FIFO, LIFO
    }

    private double percentage;
    private boolean isShowProgressBar = false;

    /* 是否再ViewPager中显示缩略图 */
    public boolean isSampleForViewPager = true;

    private double widthRatio = 1.0f;
    private double heightRatio = 1.0f;

    public static LocalImageLoader getInstance(){
        if (mInstance == null){
            synchronized(LocalImageLoader.class){
                if (mInstance == null){
                    mInstance = new LocalImageLoader(1,Type.LIFO);
                }
            }
        }

        return mInstance;
    }

    public static LocalImageLoader getInstance(int mThreadCount,Type mType){
        if (mInstance == null){
            synchronized(LocalImageLoader.class){
                if (mInstance == null){
                    mInstance = new LocalImageLoader(mThreadCount,mType);
                }
            }
        }

        return mInstance;
    }

    public LocalImageLoader(int threadCount,Type type){
        init(threadCount,type);
    }

    private void init(int threadCount,Type type){
        mPoolThread = new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        mThreadPool.execute(getTask());
                        try {
                            mPoolSemaphore.acquire();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                mSemaphore.release();
                Looper.loop();
            }
        };
        mPoolThread.start();
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mLruCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            };
        };

        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mPoolSemaphore = new Semaphore(threadCount);
        mTasks = new LinkedList<Runnable>();
        mType = type == null ? Type.LIFO : type;
    }

    /**
     *
     * 加载图片
     * @param path
     * @param imageView
     */
    public void loadImage(final String path, final ImageView imageView, final boolean isLocal) {
        imageView.setTag(path);
        /* 回UI线程 */
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    ImgBeanHolder imgBeanHolder = (ImgBeanHolder) msg.obj;
                    ImageView imageView = imgBeanHolder.imageView;
                    Bitmap bitmap = imgBeanHolder.bitmap;
                    String path = imgBeanHolder.path;
                    if (imageView.getTag().toString().equals(path)) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            };
        }
        /*从缓存中获取*/
        Bitmap bm = null;
        if (isSampleForViewPager) {
            bm = getBitmapFromLruCache(path);
        } else {
            File file = getDiskCacheDir(imageView.getContext(), md5(path));
            bm = loadImageFromLocal(file.getAbsolutePath(), imageView);
        }
        if (bm != null) {
            ImgBeanHolder holder = new ImgBeanHolder();
            holder.bitmap = bm;
            holder.imageView = imageView;
            holder.path = path;
            Message message = Message.obtain();
            message.obj = holder;
            mHandler.sendMessage(message);
        } else {
            addTask(new Runnable() {
                @Override
                public void run() {
                    Bitmap bm = null;
                    /*网络获取*/
                    if (!isLocal) {
                        File file = getDiskCacheDir(imageView.getContext(), md5(path));
                        if (file.exists()) {
                            isShowProgressBar =false;
                            bm = loadImageFromLocal(file.getAbsolutePath(), imageView);
                        } else {
                            isShowProgressBar =true;
                            boolean downloadState = downloadImgByUrl(path, file);
                            if (downloadState) {
                                Log.d("downFileCachePath", file.getAbsolutePath());
                                bm = loadImageFromLocal(file.getAbsolutePath(), imageView);
                            }
                        }
                    } else {
                    /*本地获取*/
                        isShowProgressBar =false;
                        bm = loadImageFromLocal(path, imageView);

                    }
                    addBitmapToLruCache(path, bm);
                    ImgBeanHolder holder = new ImgBeanHolder();
                    if (isSampleForViewPager) {
                        holder.bitmap = getBitmapFromLruCache(path);
                    } else {
                        holder.bitmap = bm;
                    }
                    holder.path = path;
                    holder.imageView = imageView;
                    Message message = Message.obtain();
                    message.obj = holder;
                    mHandler.sendMessage(message);
                    mPoolSemaphore.release();
                }
            });
        }


    }
    /**
     * 添加一个任务
     *
     * @param runnable
     */
    private synchronized void addTask(Runnable runnable)
    {
        try
        {
            // 请求信号量，防止mPoolThreadHander为null
            if (mPoolThreadHandler == null)
                mSemaphore.acquire();
        } catch (InterruptedException e)
        {
        }
        mTasks.add(runnable);

        mPoolThreadHandler.sendEmptyMessage(0x110);
    }

    /**
     * 取出一个任务
     *
     * @return
     */
    private synchronized Runnable getTask()
    {
        if (mType == Type.FIFO)
        {
            return mTasks.removeFirst();
        } else if (mType == Type.LIFO)
        {
            return mTasks.removeLast();
        }
        return null;
    }



    /**
     * 根据ImageView获得适当的压缩的宽和高
     *
     * @param imageView
     * @return
     */
    private static ImageSize getImageViewWidth(ImageView imageView)
    {
        ImageSize imageSize = new ImageSize();
        final DisplayMetrics displayMetrics = imageView.getContext()
                .getResources().getDisplayMetrics();
        final ViewGroup.LayoutParams params = imageView.getLayoutParams();

        int width = params.width == ViewGroup.LayoutParams.WRAP_CONTENT ? 0 : imageView
                .getWidth(); // Get actual image width
        if (width <= 0)
            width = params.width; // Get layout width parameter
        if (width <= 0)
            width = getImageViewFieldValue(imageView, "mMaxWidth"); // Check
        // maxWidth
        // parameter
        if (width <= 0)
            width = displayMetrics.widthPixels;
        int height = params.height == ViewGroup.LayoutParams.WRAP_CONTENT ? 0 : imageView
                .getHeight(); // Get actual image height
        if (height <= 0)
            height = params.height; // Get layout height parameter
        if (height <= 0)
            height = getImageViewFieldValue(imageView, "mMaxHeight"); // Check
        // maxHeight
        // parameter
        if (height <= 0)
            height = displayMetrics.heightPixels;
        imageSize.width = width;
        imageSize.height = height;
        return imageSize;

    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     */
    private Bitmap getBitmapFromLruCache(String key)
    {
        return mLruCache.get(key);
    }

    /**
     * 往LruCache中添加一张图片
     *
     * @param key
     * @param bitmap
     */
    private void addBitmapToLruCache(String key, Bitmap bitmap)
    {
        if (getBitmapFromLruCache(key) == null)
        {
            if (bitmap != null)
                mLruCache.put(key, bitmap);
        }
    }

    /**
     * 计算inSampleSize，用于压缩图片
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight)
    {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;

        if (width > reqWidth && height > reqHeight){
            int widthRatio = Math.round((float) width / (float) reqWidth);
            int heightRatio = Math.round((float) height / (float) reqHeight);
            inSampleSize= Math.max(widthRatio,heightRatio);
        }
        return inSampleSize;
    }

    /**
     * 根据计算的inSampleSize，得到压缩后图片
     *
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap decodeSampledBitmapFromResource(String pathName,
                                                   int reqWidth, int reqHeight)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        //第一次渲染仅获取图片大小
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName,options);
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        //第二次渲染出图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(pathName,options);


        return bitmap;
    }

    /**
     * 获得ViewPager用图片
     * @param pathName
     * @return
     */
    public Bitmap decodeSizedBitmapFromResource(String pathName){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName,options);
        ImageSize imageSize = getViewPagerImageSize(widthRatio,heightRatio);
        options.inSampleSize = calculateInSampleSize(options,imageSize.width,imageSize.height);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(pathName,options);
        return bitmap;
    }

    /**
     * 根据url下载图片在指定的文件
     *
     * @param urlStr
     * @param file
     * @return
     */
    public static boolean downloadImgByUrl(String urlStr, File file)
    {
        FileOutputStream fos = null;
        InputStream is = null;
        try
        {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int contentLength = conn.getContentLength();

            is = conn.getInputStream();
            fos = new FileOutputStream(file);
            byte[] buf = new byte[512];
            int len = 0;
            int writedLength = 0;
            mInstance.percentage = 0;
            while ((len = is.read(buf)) != -1)
            {
                writedLength += len;
                mInstance.percentage = 1.0 * writedLength / contentLength;
                Log.d("percentage", String.valueOf(mInstance.percentage));

                fos.write(buf, 0, len);
            }
            fos.flush();
            return true;

        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (is != null)
                    is.close();
            } catch (IOException e)
            {
            }

            try
            {
                if (fos != null)
                    fos.close();
            } catch (IOException e)
            {
            }
        }

        return false;

    }

    public static Bitmap downloadImgByUrl(String urlStr, ImageView imageView){
        FileOutputStream fos = null;
        InputStream is =null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(conn.getInputStream());
            is.mark(is.available());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeStream(is,null,options);

            //Obtain width and height
            ImageSize imageSize = getImageViewWidth(imageView);
            options.inSampleSize = calculateInSampleSize(options,imageSize.width,imageSize.height);

            options.inJustDecodeBounds = false;
            is.reset();
            bitmap = BitmapFactory.decodeStream(is,null,options);
            conn.disconnect();
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (is != null){
                    is.close();
                }
                if (fos != null){
                    fos.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 反射获得ImageView设置的最大宽度和高度
     *
     * @param object
     * @param fieldName
     * @return
     */
    private static int getImageViewFieldValue(Object object, String fieldName)
    {
        int value = 0;
        try
        {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = (Integer) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
            {
                value = fieldValue;

                Log.e("TAG", value + "");
            }
        } catch (Exception e)
        {
        }
        return value;
    }
    /**
     * 获得缓存图片的地址
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public File getDiskCacheDir(Context context, String uniqueName)
    {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState()))
        {
            cachePath = context.getExternalCacheDir().getPath();
        } else
        {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 利用签名辅助类，将字符串字节数组
     *
     * @param str
     * @return
     */
    public String md5(String str)
    {
        byte[] digest = null;
        try
        {
            MessageDigest md = MessageDigest.getInstance("md5");
            digest = md.digest(str.getBytes());
            return bytes2hex02(digest);

        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 方式二
     *
     * @param bytes
     * @return
     */
    public String bytes2hex02(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        for (byte b : bytes)
        {
            // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
            tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() == 1)// 每个字节8为，转为16进制标志，2个16进制位
            {
                tmp = "0" + tmp;
            }
            sb.append(tmp);
        }

        return sb.toString();
    }
    private Bitmap loadImageFromLocal(final String path,
                                      final ImageView imageView) {
        Bitmap bm;
        // 加载图片
        // 图片的压缩
        // 1、获得图片需要显示的大小
        ImageSize imageSize = null;
        if (!isSampleForViewPager) {
            imageSize = getViewPagerImageSize(widthRatio,heightRatio);
        } else {
            imageSize = getImageViewWidth(imageView);
        }
        // 2、压缩图片
        bm = decodeSampledBitmapFromResource(path, imageSize.width,
                imageSize.height);
        return bm;
    }

    /**
     * 获得ViewPager中显示的图片尺寸
     * @param widthRatio
     * @param heightRatio
     * @return
     */
    private ImageSize getViewPagerImageSize(double widthRatio,double heightRatio){
        ImageSize imageSize = new ImageSize();
        WindowManager wm = (WindowManager) MSApplication.getApplication().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        int reqWidth = point.x;
        int reqHeight = point.y;
        imageSize = new ImageSize();
        imageSize.width = (int)(reqWidth * widthRatio);
        imageSize.height = (int)(reqHeight * heightRatio);
        return imageSize;
    }

    public void setRatios(double widthRatio,double heightRatio){
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
    }

    public double getPercentage(){
        return percentage;
    }

    public boolean isShowProgressBar(){
        return isShowProgressBar;
    }
}
