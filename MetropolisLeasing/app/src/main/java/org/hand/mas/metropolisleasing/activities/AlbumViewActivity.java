package org.hand.mas.metropolisleasing.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.adapters.CddGridAdapter;
import org.hand.mas.metropolisleasing.adapters.DetailListAdapter;
import org.hand.mas.metropolisleasing.adapters.ViewPagerAdapter;
import org.hand.mas.metropolisleasing.models.CddGridModel;
import org.hand.mas.metropolisleasing.models.CddGridSvcModel;
import org.hand.mas.metropolisleasing.models.DetailListModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gonglixuan on 15/3/16.
 */
public class AlbumViewActivity extends Activity implements LMModelDelegate{

    private GridView mGridView;


    private CddGridAdapter mCddAdapter;


    private ImageView mBackImageView;
    private ImageView mDelImageView;

    private FrameLayout mFrameLayout;


    private TextView returnTextView;
    private TextView mTitleTextView;
    private ImageView mAddItemImageView;

    private DisplayImageOptions mOptions;

    private HashMap<String,String> param;
    private CddGridSvcModel mModel;
    private List<CddGridModel> mCddGridList;
    private String mTitle;

    // 拍照
    public static final int IMAGE_CAPTURE = 0;

    // 相册
    public static final int ACTION_GET_CONTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cdd_grid);

        bindAllViews();

    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intentFromDetail = getIntent();
        String project_number = intentFromDetail.getStringExtra("project_number");
        String cdd_item_id = intentFromDetail.getStringExtra("cdd_item_id");
        mTitle = intentFromDetail.getStringExtra("bp_name");
        mTitleTextView.setText(mTitle);
        if(mModel == null){
            param = new HashMap<String,String>();
            param.put("project_number",project_number);
            param.put("cdd_item_id",cdd_item_id);
            mModel = new CddGridSvcModel(this);
            mModel.load(param);
        }

    }

    @Override
    public void modelDidFinishLoad(LMModel model) {
        AsHttpRequestModel reponseModel = (AsHttpRequestModel) model;
        String json = new String(reponseModel.mresponseBody);
        try {
            JSONObject jsonObj = new JSONObject(json);
            String code = ((JSONObject)jsonObj.get("head")).get("code").toString();
            if(code.equals("success")){

                JSONArray bodyArr = (JSONArray) ((JSONObject)jsonObj.get("body")).get("grid");
                try {
                    initializeData(bodyArr);
                    mCddAdapter = new CddGridAdapter(getApplicationContext(),mCddGridList,mOptions);
                    mGridView.setAdapter(mCddAdapter);


                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void modelDidStartLoad(LMModel model) {

    }

    @Override
    public void modelDidFailedLoadWithError(LMModel model) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;
        Uri originalUri;
        ContentResolver cr;
        switch (requestCode){
            case IMAGE_CAPTURE:
                break;
            case ACTION_GET_CONTENT:
                originalUri = data.getData();
                cr = this.getContentResolver();
                
                break;
            default:
                break;
        }
    }

    /* Private Methods */
    private void initializeData(JSONArray jsonArray) throws JSONException{

        if (mCddGridList == null){
            mCddGridList = new ArrayList<CddGridModel>();
        }
        int length = jsonArray.length();
        for (int i = 0; i < length; i++){
            JSONObject data = (JSONObject)jsonArray.get(i);
            try {
                CddGridModel item = new CddGridModel(
                        data.getString("cdd_item_id"),
                        data.getString("file_path"),
                        data.getString("file_name"),
                        data.getString("description"),
                        data.getString("file_suffix")
                );
                mCddGridList.add(item);
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }finally {
            }
        }

    }

    private void bindAllViews(){

        mTitleTextView = (TextView) findViewById(R.id.titleTextView_for_cdd_item);
        mGridView = (GridView) findViewById(R.id.gridView_for_cdd_grid);
        mAddItemImageView = (ImageView) findViewById(R.id.addItem_for_cdd_item);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mCddGridList == null){
                    mCddGridList = new ArrayList<CddGridModel>();
                }

                Intent intent = new Intent(getApplicationContext(),ViewPagerActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("cddGridList", (java.io.Serializable) mCddGridList);
                startActivityForResult(intent,1);
            }
        });
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        /* 下方dialog */
        mAddItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialog = new DialogPlus.Builder(AlbumViewActivity.this)
                        .setContentHolder(new ViewHolder(R.layout.view_add_item_dialog))
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialogPlus, View view) {

                                switch (view.getId()){
                                    case R.id.mCamera:
                                        Toast.makeText(getApplicationContext(),"Camera",Toast.LENGTH_LONG).show();
                                        Intent getImageByCamera = new Intent(
                                                "android.media.action.IMAGE_CAPTURE");
                                        startActivityForResult(getImageByCamera,
                                                IMAGE_CAPTURE);
                                        break;
                                    case R.id.mPhoto:
                                        Toast.makeText(getApplicationContext(),"Photo",Toast.LENGTH_LONG).show();
                                        Intent getImage = new Intent(
                                                Intent.ACTION_GET_CONTENT);
                                        getImage.addCategory(Intent.CATEGORY_OPENABLE);
                                        getImage.setType("image/jpeg");
                                        startActivityForResult(getImage,
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

        mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .build();

    }

}
