package org.hand.mas.metropolisleasing.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.adapters.CustomAlbumAdapter;
import org.hand.mas.metropolisleasing.bean.ImageFolder;
import org.hand.mas.metropolisleasing.models.UploadAttachmentSvcModel;
import org.hand.mas.utl.ConstantUtl;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by gonglixuan on 15/3/27.
 */
public class AlbumGridActivity extends Activity implements LMModelDelegate{

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
    private List<Object> mSelectedList = null;
    private View.OnClickListener mOnClickListener;

    private GridView mGirdView;
    private TextView mPreviewTextView;
    private TextView mFinishTextView;
    private TextView mCountTextView;
    private ImageView mExitImageView;
    private ImageView mDirectImageView;
    private TextView mTitleTextView;

    private SweetAlertDialog sweetAlertDialog;

    private CustomAlbumAdapter mAdapter;

    private UploadAttachmentSvcModel mUploadModel;

    private HashMap<String,String> param;

    private boolean isUploadSuccess = true;

    private int countForUploadSuccess = 0;
    private int countForFailure = 0;

    private final int RESULT_OK = 0;
    private final int RESULT_CANCEL = -1;

    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();

    private final int View_Pager_All = 0;
    private final int View_Pager_Selected = 1;
    private final int Direct_Changed = 2;

    private List<ImageFolder> imageFolderList;


    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            if (mProgressDialog.isShowing())
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
            String dirPath = mImgDir.getAbsolutePath();
            mAdapter = new CustomAlbumAdapter(getApplicationContext(),mImgs,null,dirPath,mOnClickListener);
            mGirdView.setAdapter(mAdapter);
            mTitleTextView.setText(dirPath.split("/")[dirPath.split("/").length - 1]);
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
    public void modelDidFinishLoad(LMModel model) {
        countForUploadSuccess++;
        if (countForFailure+countForUploadSuccess == mSelectedList.size()){
            if (sweetAlertDialog.isShowing()){
                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("上传结束")
                                .setConfirmText("确定")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        finishWithResultCode(RESULT_OK);
                                    }
                                });
            }

        }

    }

    @Override
    public void modelDidStartLoad(LMModel model) {

    }

    @Override
    public void modelDidFailedLoadWithError(LMModel model) {
        isUploadSuccess = false;
        Toast.makeText(getApplicationContext(),"有文件上传失败，请检查网络后再试",Toast.LENGTH_SHORT).show();
        countForFailure++;
    }

    @Override
    public void onBackPressed() {
        finishWithResultCode(RESULT_CANCEL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null){
            return;
        }
        switch (requestCode){
            case View_Pager_All:
                mSelectedList = (List<Object>) data.getSerializableExtra("mSelectedList");
                updateUIs();
                break;
            case View_Pager_Selected:
                List<Object> selectedList = (List<Object>) data.getSerializableExtra("mSelectedList");
                List<Object> tmpList = new ArrayList<Object>();
                for (int i = 0; i < selectedList.size();i++){
                    int position = (int) selectedList.get(i);
                    tmpList.add(mSelectedList.get(position));
                }
                mSelectedList = tmpList;
                updateUIs();
                break;
            case Direct_Changed:
                mImgDir = new File(data.getStringExtra("mImgDir"));
                mHandler.sendEmptyMessage(0x110);
                break;
        }
        if (resultCode == RESULT_OK){
            List<String> uploadList = (List<String>) data.getSerializableExtra("mUploadList");
            uploadAttachment(uploadList);
        }


    }

    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(getApplicationContext(),"NO STORAGE AVAILABLE",Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog = ProgressDialog.show(this,null,"正在加载...");
        imageFolderList = new ArrayList<ImageFolder>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = AlbumGridActivity.this.getContentResolver();

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
                    ImageFolder imageFolder = new ImageFolder(dirPath,path,dirPath.split("/")[dirPath.split("/").length - 1],picSize);
                    imageFolderList.add(imageFolder);
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

        mUploadModel = new UploadAttachmentSvcModel(this);
        param = new HashMap<String,String>();

        mGirdView = (GridView) findViewById(R.id.gridView_for_album_grid);
        mPreviewTextView = (TextView) findViewById(R.id.preview_textview);
        mFinishTextView = (TextView) findViewById(R.id.finish_textview);
        mCountTextView = (TextView) findViewById(R.id.count_for_list);
        mExitImageView = (ImageView) findViewById(R.id.exit_album);
        mDirectImageView = (ImageView) findViewById(R.id.to_direct_list_imageview);
        mTitleTextView = (TextView) findViewById(R.id.title_textView);

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.getId() == R.id.album_item_select_button){
                    Object position = v.getTag(R.id.position);
                    List<Object> List = mAdapter.mSelectedList;
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
        };
        mPreviewTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedList == null){
                    mSelectedList = getAdapterList();
                }
                if (mSelectedList.isEmpty()){
                    return;
                }
                startViewpagerActivity();
            }
        });
        mFinishTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAttachment(null);
            }
        });
        mExitImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishWithResultCode(RESULT_CANCEL);
            }
        });
        mDirectImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlbumGridActivity.this,ChangeDirectActivity.class);
                intent.putExtra("imageFolderList", (java.io.Serializable) imageFolderList);
                startActivityForResult(intent,Direct_Changed);
                overridePendingTransition(R.anim.move_in_left,R.anim.move_out_right);
            }
        });

    }

    /**
     * 打开AlbumViewPagerActivity,浏览所有图片
     * @param position
     */
    private void startViewpagerActivity(int position) {


        Intent intent = new Intent(getApplicationContext(), AlbumViewPagerActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("mImgDirPath", mImgDir.getAbsolutePath());
        intent.putExtra("mImgs", (java.io.Serializable) mImgs);
        intent.putExtra("mSelectedList", (java.io.Serializable) getAdapterList());


        startActivityForResult(intent, View_Pager_All);
    }

    /**
     * 打开AlbumViewPagerActivity,浏览被选择图片
     *
     */
    private void startViewpagerActivity(){
        Intent intent = new Intent(getApplicationContext(), AlbumViewPagerActivity.class);
        List<String> mSelectedImgs = new ArrayList<String>();
        mSelectedList = mAdapter.mSelectedList;
        /* 传入的都是被选择的图片 */
        List<Object> selectedList = new ArrayList<Object>();
        int length = mSelectedList.size();
        for (int i = 0;i < length;i++){
            mSelectedImgs.add(mImgs.get((Integer)mSelectedList.get(i)));
            selectedList.add((Object)i);
        }
        intent.putExtra("mImgDirPath", mImgDir.getAbsolutePath());
        intent.putExtra("mImgs", (java.io.Serializable) mSelectedImgs);
        intent.putExtra("mSelectedList", (java.io.Serializable) selectedList);
        startActivityForResult(intent,View_Pager_Selected);
    }

    public List<Object> getAdapterList(){
        return mAdapter.mSelectedList;
    }

    /**
     * 更新UI；
     */
    private void updateUIs(){
        mAdapter.mSelectedList = mSelectedList;
//                mAdapter = new CustomAlbumAdapter(getApplicationContext(),mImgs,mSelectedList,mImgDir.getAbsolutePath(),mOnClickListener);
        mGirdView.setAdapter(mAdapter);
        mCountTextView.setText(String.valueOf(mSelectedList.size()));
        if (mSelectedList.isEmpty()){
            mPreviewTextView.setAlpha(0.3f);
            mFinishTextView.setAlpha(0.3f);
            mCountTextView.setVisibility(View.INVISIBLE);
        }else {
            mPreviewTextView.setAlpha(1.0f);
            mFinishTextView.setAlpha(1.0f);
            mCountTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     *
     * 创建上传文件的列表
     * @return 上传文件的绝对路径
     */
    private List<String> generateUploadList(){
        List<String> uploadList = new ArrayList<String>();
        String filePath;
        int length = mSelectedList.size();
        for (int i = 0;i < length;i++){
            filePath = mImgDir.getAbsolutePath()+"/"+mImgs.get((Integer)mSelectedList.get(i));
            uploadList.add(filePath);
        }
        return uploadList;
    }

    /**
     *
     * finish并返回result_code
     * @param resultCode
     */
    private void finishWithResultCode(int resultCode){
        setResult(resultCode);
        finish();
    }

    private void uploadAttachment(List<String> mUploadList){
        if (mSelectedList == null){
            mSelectedList = getAdapterList();
        }
        if (mSelectedList.isEmpty()){
            return;
        }
        if (param == null){
            param = new HashMap<String,String>();
        }
        if (mUploadModel == null){
            mUploadModel = new UploadAttachmentSvcModel(AlbumGridActivity.this);
        }
        Intent intent = getIntent();
        String projectNumber = intent.getStringExtra("project_number");
        String cddItemId = intent.getStringExtra("cdd_item_id");
        String checkId = intent.getStringExtra("check_id");
        param.put("project_number",projectNumber);
        param.put("cdd_item_id",cddItemId);
        param.put("check_id",checkId);
        String filePath = null;
        byte[] bytes = null;
        String fileName = null;
        List<String> uploadList;
        if (mUploadList == null){
            uploadList = generateUploadList();
        }else {
            uploadList = mUploadList;
        }
        isUploadSuccess = true;
        countForUploadSuccess = 0;
        countForFailure = 0;
        sweetAlertDialog = new SweetAlertDialog(AlbumGridActivity.this,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("附件上传中")
                .showCancelButton(false);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.theme_color));
        for (int i = 0;i<uploadList.size();i++){
            filePath = uploadList.get(i);
            bytes = ConstantUtl.getBytes(filePath);
            fileName = filePath.split("/")[filePath.split("/").length - 1];
            mUploadModel.upload(param,bytes,fileName);
        }
        if (!sweetAlertDialog.isShowing()){
            sweetAlertDialog.show();
        }
    }

}
