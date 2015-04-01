package org.hand.mas.metropolisleasing.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.adapters.CustomCddGridAdapter;
import org.hand.mas.metropolisleasing.models.CddGridModel;
import org.hand.mas.metropolisleasing.models.CddGridSvcModel;
import org.hand.mas.metropolisleasing.models.DeleteAttachmentSvcModel;
import org.hand.mas.metropolisleasing.models.UploadAttachmentSvcModel;
import org.hand.mas.utl.ConstantUtl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by gonglixuan on 15/3/16.
 */
public class CddGridActivity extends Activity implements LMModelDelegate {

    private GridView mGridView;
    private TextView mTitleTextView;
    private ImageView mAddItemImageView;
    private ImageView mReturnImageView;
    private SweetAlertDialog mSwal;

    private CustomCddGridAdapter mCddAdapter;


    private HashMap<String, String> param;
    private CddGridSvcModel mModel;
    private UploadAttachmentSvcModel mUploadModel;
    private DeleteAttachmentSvcModel mDeleteModel;

    private List<CddGridModel> mCddGridList;
    private String mTitle;
    private String mDescription;
    private String mProjectNumber;
    private String mCddItemId;
    private String mCheckId;
    private String mDeletedAttachmentId;
    private int mDeletedCurrencyPosition;

    int i = 0;
    DialogPlus  dialog;

    // 拍照
    public static final int IMAGE_CAPTURE = 0;
    private Uri photoUri;

    // 相册
    public static final int ACTION_GET_CONTENT = 1;

    // 详细
    public static final int VIEW_PAGER = 2;

    private final int RESULT_OK = 0;
    private final int RESULT_CANCEL = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cdd_grid);

        bindAllViews();

    }

    @Override
    public void onBackPressed() {
        finishWithAnim();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intentFromDetail = getIntent();
        mProjectNumber = intentFromDetail.getStringExtra("project_number");
        mCddItemId = intentFromDetail.getStringExtra("cdd_item_id");
        mCheckId = intentFromDetail.getStringExtra("check_id");
        mTitle = intentFromDetail.getStringExtra("bp_name");
        mDescription = intentFromDetail.getStringExtra("description");
        mTitleTextView.setText(mTitle);
        mAddItemImageView.setVisibility(View.VISIBLE);
        if (mModel == null) {
            resetParam();
//            param = new HashMap<String, String>();
//            param.put("project_number", mProjectNumber);
//            param.put("cdd_item_id", mCddItemId);
//            param.put("check_id", mCheckId);
            mModel = new CddGridSvcModel(this);
            mModel.load(param);
        }

    }

    @Override
    public void modelDidFinishLoad(LMModel model) {
        if (model instanceof UploadAttachmentSvcModel){
            mCddGridList = null;
            mCddAdapter = null;
            mModel.load(param);
        }else if(model instanceof DeleteAttachmentSvcModel){
            mModel.load(param);
        }
        AsHttpRequestModel reponseModel = (AsHttpRequestModel) model;
        String json = new String(reponseModel.mresponseBody);
        try {
            JSONObject jsonObj = new JSONObject(json);
            String code = ((JSONObject) jsonObj.get("head")).get("code").toString();
            if (code.equals("success")) {

                JSONArray bodyArr = (JSONArray) ((JSONObject) jsonObj.get("body")).get("grid");
                try {
                    initializeData(bodyArr);
                    mCddAdapter = new CustomCddGridAdapter(mCddGridList, getApplicationContext(), R.layout.cdd_grid_item, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDeletedCurrencyPosition = (int) v.getTag(R.id.position);
                            CddGridModel item = mCddGridList.get(mDeletedCurrencyPosition);
                            mDeletedAttachmentId = item.getAttachmentId();
                                    /* Swal */
                            mSwal = new SweetAlertDialog(CddGridActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("确定要删除影像资料？")
                                    .setConfirmText("确定")
                                    .setCancelText("取消")
                                    .setContentText("删除之后无法恢复")
                                    .showCancelButton(true)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(final SweetAlertDialog sDialog) {
                                            mCddGridList.remove(mDeletedCurrencyPosition);
                                            mCddAdapter.notifyDataSetChanged();
                                            sDialog.setTitleText("请稍后")
                                                    .setContentText("正在删除服务器端附件")
                                                    .showCancelButton(false)
                                                    .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                                            deleteFileFromServer(mDeletedAttachmentId);
                                        }
                                    });
                            mSwal.show();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = (int) v.getTag(R.id.position);
                            startViewpagerActivity(position);
                        }
                    });
                    mGridView.setAdapter(mCddAdapter);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mSwal != null&&mSwal.isShowing()) {
                mSwal.dismiss();
            }
        }
    }

    @Override
    public void modelDidStartLoad(LMModel model) {
        if (!(model instanceof UploadAttachmentSvcModel)){
            return;
        }
        if (mSwal == null) {
            mSwal = new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("请稍后")
                    .setContentText("正在更新影像数据")
                    .showCancelButton(false);
        }
    }

    @Override
    public void modelDidFailedLoadWithError(LMModel model) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            if (requestCode == VIEW_PAGER){
                return;
            }

        }
        Uri originalUri;
        String filePath;
        String fileName;
        String fileSuffix;
        switch (requestCode) {
            case IMAGE_CAPTURE:
                originalUri = photoUri;
                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor cursor = this.getContentResolver().query(originalUri, proj, null,
                        null, null);
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                filePath = cursor.getString(column_index);
                fileName = filePath.split("/")[filePath.split("/").length - 1];
                fileSuffix = filePath.split("\\.")[filePath.split("\\.").length - 1];
                try {
                    byte[] bytes = ConstantUtl.getBytes(filePath);
                    if (mUploadModel == null){
                        mUploadModel = new UploadAttachmentSvcModel(CddGridActivity.this);
                    }
                    resetParam();
                    mUploadModel.upload(param,bytes,fileName);
                    mModel.load(param);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "IMAGE_CAPTURE FAILED!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } finally {


                }

                break;
            case ACTION_GET_CONTENT:
                if (resultCode == RESULT_OK){
                    if (dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                    resetParam();
                    mModel.load(param);
                }
                else if (resultCode == RESULT_CANCEL){

                }
                break;
            case VIEW_PAGER:
                int positionForDeletedItem = data.getExtras().getInt("currencyPosition");
                /* 删除本地影像资料 */
                mCddGridList.remove(positionForDeletedItem);
                mCddAdapter.notifyDataSetChanged();


                System.gc();
                break;
            default:
                break;
        }
    }

    /* Private Methods */
    private void initializeData(JSONArray jsonArray) throws JSONException {

        if (mCddGridList == null) {
            mCddGridList = new ArrayList<CddGridModel>();
        }
        int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            JSONObject data = (JSONObject) jsonArray.get(i);
            try {
                CddGridModel item = new CddGridModel(
                        data.getString("attachment_id"),
                        data.getString("cdd_item_id"),
                        data.getString("file_path"),
                        data.getString("file_name"),
                        data.getString("description"),
                        data.getString("file_suffix"),
                        true
                );
                mCddGridList.add(item);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            } finally {
            }
        }

    }

    private void bindAllViews() {

        mTitleTextView = (TextView) findViewById(R.id.titleTextView);
        mGridView = (GridView) findViewById(R.id.gridView_for_cdd_grid);
        mAddItemImageView = (ImageView) findViewById(R.id.addItem_for_cdd_item);
        mReturnImageView = (ImageView) findViewById(R.id.return_to_detailList);


        /* 下方dialog */
        mAddItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateAddItem(v, 0.0f, 90.0f, 300);
                dialog = new DialogPlus.Builder(CddGridActivity.this)
                        .setContentHolder(new ViewHolder(R.layout.view_add_item_dialog))
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialogPlus, View view) {

                                switch (view.getId()) {
                                    case R.id.mCamera:
                                        Toast.makeText(getApplicationContext(), "Camera", Toast.LENGTH_LONG).show();
                                        Intent getImageByCamera = new Intent(
                                                "android.media.action.IMAGE_CAPTURE");
                                        SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                                                "yyyy_MM_dd_HH_mm_ss");
                                        String filename = timeStampFormat
                                                .format(new Date());
                                        ContentValues values = new ContentValues();
                                        values.put(MediaStore.Images.Media.TITLE, filename);
                                        photoUri = getContentResolver()
                                                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                        values);
                                        getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                                                photoUri);
                                        startActivityForResult(getImageByCamera,
                                                IMAGE_CAPTURE);
                                        break;
                                    case R.id.mPhoto:
                                        Intent getImageIntent = new Intent(getApplicationContext(), AlbumGridActivity.class);
                                        getImageIntent.putExtra("project_number", mProjectNumber);
                                        getImageIntent.putExtra("cdd_item_id",mCddItemId);
                                        getImageIntent.putExtra("check_id",mCheckId);
                                        startActivityForResult(getImageIntent,
                                                ACTION_GET_CONTENT);
                                        break;
                                    case R.id.mCancel:
                                        dialogPlus.dismiss();
                                        break;
                                }
                            }
                        })
                        .create();
                dialog.show();
            }
        });

        mReturnImageView.setVisibility(View.VISIBLE);
        mReturnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishWithAnim();
            }
        });

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 10;
    }

    /*
     * convert uri to path
     * @{param} String
     */
    private String uri2path(String uriString) {
        Uri uri = Uri.parse(uriString);

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor actualImageCursor = this.managedQuery(uri, projection, null, null, null);
        int actualImageColumnIndex = actualImageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualImageCursor.moveToFirst();
        String imgPath = actualImageCursor.getString(actualImageColumnIndex);
        return imgPath;
    }

    /*
    * finish activity
    * */
    private void finishWithAnim() {
        finish();
        overridePendingTransition(R.anim.alpha_in, R.anim.move_out_right);
    }

    private void rotateAddItem(View v, float start, float end, int i) {
        RotateAnimation anim = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(i);
        anim.setFillAfter(true);
        v.startAnimation(anim);
    }

    /*
    *
    * startViewPagerActivity
    *
    * */
    private void startViewpagerActivity(int position) {
        if (mCddGridList == null) {
            mCddGridList = new ArrayList<CddGridModel>();
        }

        CddGridModel item = mCddGridList.get(position);
        boolean isRemote = item.getRemote();
        String attachmentId = item.getAttachmentId();

        Intent intent = new Intent(getApplicationContext(), CddViewPagerActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("cddGridList", (java.io.Serializable) mCddGridList);
        intent.putExtra("isRemote", isRemote);
        intent.putExtra("attachmentId", attachmentId);

        startActivityForResult(intent, VIEW_PAGER);
    }

    /* 删除远程附件 */
    private void deleteFileFromServer(String attachmentId) {
        if (mDeleteModel == null) {
            mDeleteModel = new DeleteAttachmentSvcModel(this);
        }
        mCddGridList = null;
        HashMap<String, String> paramFordelete = new HashMap<String, String>();
        paramFordelete.put("attachment_id", attachmentId);
        mDeleteModel.load(paramFordelete);
    }

    private void resetParam(){
        mCddAdapter = null;
        mCddGridList = null;
        param = new HashMap<String, String>();
        param.put("project_number", mProjectNumber);
        param.put("cdd_item_id", mCddItemId);
        param.put("check_id", mCheckId);
    }
}
