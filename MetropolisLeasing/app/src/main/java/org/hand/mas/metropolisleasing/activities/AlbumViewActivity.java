package org.hand.mas.metropolisleasing.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.adapters.AlbumViewPagerAdapter;
import org.hand.mas.metropolisleasing.adapters.CustomAlbumAdapter;
import org.hand.mas.metropolisleasing.models.CddGridModel;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Created by gonglixuan on 15/3/27.
 */
public class AlbumViewActivity extends Activity {

    private ProgressDialog mProgressDialog;
    private ImageView mImageView;

    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;
    /**
     * 所有的图片
     */
    private List<String> mImgs;

    private GridView mGirdView;
    private TextView mPreviewTextView;
    private TextView mFinishTextView;
    private TextView mCountTextView;

    private CustomAlbumAdapter mAdapter;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();

    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            mProgressDialog.dismiss();
            mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png") || filename.endsWith(".gif"))
                        return true;
                    return false;
                }
            }));
            /**
             * 文件夹的路径和图片的路径分开保存,减少内存的消耗；
             */
            mAdapter = new CustomAlbumAdapter(getApplicationContext(),mImgs,mImgDir.getAbsolutePath(),new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.album_item_select_button){
                        Object position = v.getTag(R.id.position);
                        List<Object> List = mAdapter.mList;
                        if (!List.contains((Object)position)){
                            List.add((Object) position);
                            ((ImageButton)v).setImageResource(R.drawable.icon_for_pic_selected);
                        }
                        else{
                            ((ImageButton)v).setImageResource(R.drawable.icon_for_pic_unselected);
                            List.remove((Object) position);
                        }
                        if (List.isEmpty()){
                            mPreviewTextView.setAlpha(0.3f);
                            mFinishTextView.setAlpha(0.3f);
                            mCountTextView.setVisibility(View.INVISIBLE);
                        }else {
                            mPreviewTextView.setAlpha(1.0f);
                            mFinishTextView.setAlpha(1.0f);
                            mCountTextView.setText(String.valueOf(List.size()));
                            mCountTextView.setVisibility(View.VISIBLE);
                        }
                    }else if (v.getId() == R.id.album_item_image){
                        int position = Integer.valueOf(v.getTag(R.id.position).toString());
                        startViewpagerActivity(position);
                    }
                }
            });

            mGirdView.setAdapter(mAdapter);
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_grid);

        bindAllViews();
        getImages();
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(getApplicationContext(),"NO STORAGE AVAILABLE",Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog = ProgressDialog.show(this,null,"正在加载...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = AlbumViewActivity.this.getContentResolver();

                //查询jpeg，png，jpg，gif
                Cursor mCursor = mContentResolver.query(mImageUri,null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[] {"image/jpeg","image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                while (mCursor.moveToNext()){
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile = new File(path).getParentFile();
                    String dirPath = parentFile.getAbsolutePath();

                    if (mDirPaths.contains(dirPath)){
                        continue;
                    }else {
                        mDirPaths.add(dirPath);
                    }

                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")||
                                filename.endsWith(".jpeg")||
                                filename.endsWith(".gif")||
                                filename.endsWith(".png")){
                                return true;
                            }
                            return false;
                        }
                    }).length;
                    if (picSize > mPicsSize){
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }
                }
                mCursor.close();
                //扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null ;
                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0x110);
            }
        }).start();
    }

    private void bindAllViews() {
        mGirdView = (GridView) findViewById(R.id.gridView_for_album_grid);
        mPreviewTextView = (TextView) findViewById(R.id.preview_textview);
        mFinishTextView = (TextView) findViewById(R.id.finish_textview);
        mCountTextView = (TextView) findViewById(R.id.count_for_list);
    }

    /**
     * 打开AlbumViewPagerActivity
     * @param position
     */
    private void startViewpagerActivity(int position) {


        Intent intent = new Intent(getApplicationContext(), AlbumViewPagerActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("mImgDirPath", mImgDir.getAbsolutePath());
        intent.putExtra("mImgs", (java.io.Serializable) mImgs);


        startActivityForResult(intent, 0);
    }
}
