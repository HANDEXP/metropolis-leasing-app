package org.hand.mas.metropolisleasing.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;

import org.hand.mas.custom_view.CustomPullToRefreshListView;
import org.hand.mas.custom_view.SlidingMenu;
import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.models.OrderListModel;
import org.hand.mas.metropolisleasing.models.OrderListSvcModel;
import org.hand.mas.utl.CommonAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gonglixuan on 15/3/22.
 */
public class FilteredOrderListActivity extends Activity implements LMModelDelegate{

    private CustomPullToRefreshListView mOrderListView;
    private TextView mTitleTextView;
    private ImageView mReturnImageView;

    private List<OrderListModel> mOrderList;
    private OrderListSvcModel mModel;
    private CommonAdapter adapter;
    private HashMap<String,String> param;
    private static int pageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        param = new HashMap<>();
        setContentView(R.layout.activity_orders_list);
        bindAllViews();
        mModel.load(param);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishWithAnim();
    }

    @Override
    public void modelDidFinishLoad(LMModel model) {

        AsHttpRequestModel reponseModel = (AsHttpRequestModel) model;
        if (model instanceof OrderListSvcModel){
            String json = new String(reponseModel.mresponseBody);
            try {
                JSONObject jsonObj = new JSONObject(json);
                String code = ((JSONObject)jsonObj.get("head")).get("code").toString();
                if(code.equals("success")){
                    JSONArray bodyArr = (JSONArray) ((JSONObject)jsonObj.get("body")).get("lists");
                    try {
                        initializeData(bodyArr);
                        if (adapter == null){
                            adapter = new CommonAdapter<OrderListModel>(getApplicationContext(), mOrderList, R.layout.activity_orders_list_child) {
                                @Override
                                public void convert(org.hand.mas.utl.ViewHolder helper, OrderListModel obj,int position) {
                                    TextView projectNumberTextView = helper.getView(R.id.project_number_for_order);
                                    TextView projectStatusDesc = helper.getView(R.id.project_status_desc_for_order);
                                    TextView bpName = helper.getView(R.id.bp_name_for_order);
                                    TextView projectSource = helper.getView(R.id.project_source_for_order);
                                    TextView bpClass = helper.getView(R.id.bp_class_for_order);
                                    TextView identifierCode = helper.getView(R.id.identifier_code_for_order);

                                    OrderListModel item = (OrderListModel)obj;

                                    projectNumberTextView.setText(item.getProjectNumber());
                                    projectStatusDesc.setText(item.getProjectStatusDesc());
                                    bpName.setText(item.getBpName());
                                    projectSource.setText(item.getProjectSource());
                                    bpClass.setText(item.getBpClass().concat("识别号:"));
                                    if (bpClass.equals("ORG")){
                                        identifierCode.setText(item.getOrganizationCode());
                                    }else if (bpClass.equals("NP")){
                                        identifierCode.setText(item.getIdCardNo());
                                    }
                                }
                            };
                            mOrderListView.setAdapter(adapter);
                        }else{
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
//                if (mPullRefreshListView.isRefreshing()) {
//                    mPullRefreshListView.onRefreshComplete();
//                }
            }
        }
    }

    @Override
    public void modelDidStartLoad(LMModel model) {

    }

    @Override
    public void modelDidFailedLoadWithError(LMModel model) {

    }

    /* Private Methods */
    private void bindAllViews(){
        pageNum = 1;
        mModel = new OrderListSvcModel(this);

        mOrderListView = (CustomPullToRefreshListView) findViewById(R.id.order_list);
        mTitleTextView = (TextView) findViewById(R.id.titleTextView);
        mReturnImageView = (ImageView) findViewById(R.id.return_to_detailList);
        SlidingMenu slidingMenu = (SlidingMenu) findViewById(R.id.sliding_menu_and_content);
        RelativeLayout slidingMenuContent = (RelativeLayout) findViewById(R.id.slide_menu_content);

        mOrderListView.setType(-1);
        mOrderListView.setFooterEnable(false);
        slidingMenu.setBackgroundColor(Color.WHITE);
        slidingMenuContent.setVisibility(View.GONE);
        mTitleTextView.setText("筛选结果");
        mReturnImageView.setVisibility(View.VISIBLE);
        mReturnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishWithAnim();
            }
        });
//        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
//        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                /* 重置适配器和数据 */
//                resetAdapter();
//                if (param == null){
//                    param = new HashMap<String, String>();
//                }
//                resetFilterParam();
//                mModel.load(param);
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                if (param == null){
//                    param = new HashMap<String, String>();
//                }
//                param.put("page_num",String.valueOf(++pageNum));
//                resetFilterParam();
//                mModel.load(param);
//            }
//        });
//        mOrderListView = mPullRefreshListView.getRefreshableView();
        mOrderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String project_number = (String) ((TextView)view.findViewById(R.id.project_number_for_order)).getText();
                String project_source = (String) ((TextView)view.findViewById(R.id.project_source_for_order)).getText();
                String project_id = ((OrderListModel) parent.getAdapter().getItem(position)).getProjectId();
                Intent intent = new Intent(getApplicationContext(),DetailListActivity.class);
                intent.putExtra("project_id",project_id);
                intent.putExtra("project_number",project_number);
                intent.putExtra("project_source",project_source);
                startActivity(intent);
                overridePendingTransition(R.anim.move_in_right,R.anim.move_out_left);
            }
        });
        resetFilterParam();
    }

    /*
    * 初始化数据
    *
    * */
    private void initializeData(JSONArray jsonArray) throws JSONException {
        if(mOrderList == null){
            mOrderList = new ArrayList<OrderListModel>();
        }
        int length = jsonArray.length();
        for (int i = 0; i < length; i++){
            JSONObject data = (JSONObject)jsonArray.get(i);
            try {
                OrderListModel item = new OrderListModel(
                        data.getString("project_id"),
                        data.getString("project_number"),
                        data.getString("project_status_desc"),
                        data.getString("bp_class"),
                        data.getString("organization_code"),
                        data.getString("bp_name"),
                        data.getString("id_card_no"),
                        data.getString("project_source")
                );
                mOrderList.add(item);
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }finally {

            }
        }
    }

    /* 重置适配器和数据 */
    private void resetAdapter(){
        mOrderList = null;
        adapter = null;
        pageNum = 1;
    }

    /* 重置筛选参数 */
    private void resetFilterParam(){
        Intent intent = getIntent();
        String filter_param = intent.getStringExtra("filter_param");
        param.put("filter_param",filter_param);
    }

    /*
    * finish activity
    * */
    private void finishWithAnim(){
        finish();
        overridePendingTransition(R.anim.move_in_left,R.anim.move_out_right);
    }
}
