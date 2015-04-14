package org.hand.mas.metropolisleasing.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;
import com.mas.album.items.ImageItem;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.models.DetailListModel;
import org.hand.mas.metropolisleasing.models.DetailListSvcModel;
import org.hand.mas.utl.CommonAdapter;
import org.hand.mas.utl.ViewHolder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by gonglixuan on 15/3/11.
 */
public class DetailListActivity extends Activity implements LMModelDelegate{


    private ListView detailListView;
    private TextView projectNumberTextView;
    private TextView projectSourceTextView;
    private TextView mTitleTextView;
    private ImageView mReturnImageView;
    private TextView mHintTextView;

    private List<DetailListModel> mDetailList;
    private DetailListSvcModel mModel;
    private HashMap<String, String> param;

    private String project_id;
    private String project_number;
    // 照片列表数据
    private ArrayList<ImageItem> imageList = new ArrayList<ImageItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        param = new HashMap<String, String>();
        setContentView(R.layout.activity_order_details_list);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindAllViews();
        Intent intent = getIntent();
        project_id = intent.getStringExtra("project_id");
        project_number = intent.getStringExtra("project_number");
        String project_source = intent.getStringExtra("project_source");

        projectNumberTextView.setText(project_number);
        projectSourceTextView.setText(project_source);

        param.put("project_number",project_number);
        param.put("project_id",project_id);

        mModel.load(param);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finishWithAnim();
    }

    @Override
    public void modelDidFinishLoad(LMModel model) {
        Log.d("FinishLoad","modelDidFinishLoad");
        AsHttpRequestModel reponseModel = (AsHttpRequestModel) model;
        if (model instanceof DetailListSvcModel){
            String json = new String(reponseModel.mresponseBody);
            try {
                JSONObject jsonObj = new JSONObject(json);
                String code = ((JSONObject)jsonObj.get("head")).get("code").toString();
                if(code.equals("success")){

                    JSONArray bodyArr = (JSONArray) ((JSONObject)jsonObj.get("body")).get("details");
                    try {
                        initializeData(bodyArr);
                        detailListView.setAdapter(new CommonAdapter<DetailListModel>(getApplicationContext(),mDetailList,R.layout.activity_order_details_list_child) {
                            @Override
                            public void convert(ViewHolder helper, DetailListModel obj,int position) {
                                TextView bpNameTextView = helper.getView(R.id.bp_name_for_detail);
                                TextView descriptionTextView = helper.getView(R.id.description_for_detail);
                                TextView ccdCountTextView = helper.getView(R.id.ccd_count_for_detail);

                                DetailListModel item = obj;

                                bpNameTextView.setText(item.getBpName());
                                descriptionTextView.setText(item.getDescription());
                                ccdCountTextView.setText(item.getCddCount());
                            }
                        });
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                mHintTextView.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void modelDidStartLoad(LMModel model) {
        Log.d("StartLoad", "modelDidStartLoad");
        mHintTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void modelDidFailedLoadWithError(LMModel model) {
        Log.d("FailedLoadWithError","modelDidFailedLoadWithError");
    }

    /* 私有方法 */
    private void bindAllViews(){
        mModel = new DetailListSvcModel(this);

        projectNumberTextView = (TextView) findViewById(R.id.project_number_for_detail);
        projectSourceTextView = (TextView) findViewById(R.id.project_source_for_detail);
        detailListView = (ListView) findViewById(R.id.detail_list);
        mTitleTextView = (TextView) findViewById(R.id.titleTextView);
        mReturnImageView = (ImageView) findViewById(R.id.return_to_detailList);
        mHintTextView = (TextView) findViewById(R.id.hint_for_details_loading);

        detailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showAlbum(position);
            }
        });
        mTitleTextView.setText("资料清单");
        mReturnImageView.setVisibility(View.VISIBLE);
        mReturnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishWithAnim();
            }
        });
    }

    private void showAlbum(int position){
        Intent intent4ViewPager = new Intent(this, CddGridActivity.class);

        intent4ViewPager.putExtra("project_id",project_id);
        intent4ViewPager.putExtra("project_number",project_number);
        intent4ViewPager.putExtra("cdd_item_id",mDetailList.get(position).getCddItemId());
        intent4ViewPager.putExtra("check_id",mDetailList.get(position).getCheckId());
        intent4ViewPager.putExtra("bp_name",mDetailList.get(position).getBpName());
        intent4ViewPager.putExtra("description",mDetailList.get(position).getDescription());
        startActivityForResult(intent4ViewPager, 0);
        overridePendingTransition(R.anim.move_in_right,R.anim.alpha_out);
    }

    /**
     * 增加mDetailList数据
     * @param jsonArray
     * @throws JSONException
     */
    private void initializeData(JSONArray jsonArray) throws JSONException {
        mDetailList = new ArrayList<DetailListModel>();
        int length = jsonArray.length();
        for (int i = 0; i < length; i++){
            JSONObject data = (JSONObject)jsonArray.get(i);
            try {
                DetailListModel item = new DetailListModel(
                        data.getString("project_id"),
                        data.getString("project_number"),
                        data.getString("bp_name"),
                        data.getString("check_id"),
                        data.getString("description"),
                        data.getString("cdd_count"),
                        data.getString("cdd_item_id")
                );
                mDetailList.add(item);
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
        }

    }
    /*
    * finish activity
    * */
    private void finishWithAnim(){
        finish();
        overridePendingTransition(R.anim.alpha_in,R.anim.move_out_right);
    }
}
