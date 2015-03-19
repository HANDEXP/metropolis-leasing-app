package org.hand.mas.metropolisleasing.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.adapters.OrderListAdapter;
import org.hand.mas.metropolisleasing.models.OrderListModel;
import org.hand.mas.metropolisleasing.models.OrderListSvcModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by gonglixuan on 15/3/10.
 */
public class OrderListActivity extends Activity implements LMModelDelegate{

    private PullToRefreshListView mPullRefreshListView;
    private ListView orderListView;
    private List<OrderListModel> mOrderList;
    private OrderListSvcModel mModel;
    private OrderListAdapter adapter;
    private HashMap<String,String> param;
    private static int pageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_orders_list);

        param = new HashMap<>();
        bindAllViews();
        pageNum = 1;
        mModel.load();
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
    public void modelDidFinishLoad(LMModel model) {

        AsHttpRequestModel reponseModel = (AsHttpRequestModel) model;
        if(model instanceof OrderListSvcModel){
            String json = new String(reponseModel.mresponseBody);
            try {
                JSONObject jsonObj = new JSONObject(json);
                String code = ((JSONObject)jsonObj.get("head")).get("code").toString();
                if(code.equals("success")){

                    JSONArray bodyArr = (JSONArray) ((JSONObject)jsonObj.get("body")).get("lists");
                    try {
                        initializeData(bodyArr);
                        if (adapter == null){
                            adapter = new OrderListAdapter(mOrderList,getApplicationContext());
                            mPullRefreshListView.setAdapter(adapter);
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
                if (mPullRefreshListView.isRefreshing()) {
                    mPullRefreshListView.onRefreshComplete();
                }
            }

        }
    }

    @Override
    public void modelDidStartLoad(LMModel model) {
        Log.d("StartLoad","modelDidStartLoad");
    }

    @Override
    public void modelDidFailedLoadWithError(LMModel model) {
        Log.d("LoadWithError","modelDidFailedLoadWithError");
    }

    /*私有方法*/
    private void bindAllViews(){
        pageNum = 1;
        mModel = new OrderListSvcModel(this);

        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.order_list);

        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                /* 重置适配器和数据 */
                mOrderList = null;
                adapter = null;
                pageNum = 1;
                if (param == null){
                    param = new HashMap<String, String>();
                }

                mModel.load();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (param == null){
                    param = new HashMap<String, String>();
                }
                param.put("page_num",String.valueOf(++pageNum));
                mModel.load(param);

            }
        });
        orderListView = mPullRefreshListView.getRefreshableView();
//        orderList.add(new OrderListModel());
//        orderListView.setAdapter(new OrderListAdapter(mOrderList,getApplicationContext()));
        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String project_number = (String) ((TextView)view.findViewById(R.id.project_number_for_order)).getText();
                String project_source = (String) ((TextView)view.findViewById(R.id.project_source_for_order)).getText();
                Intent intent = new Intent(getApplicationContext(),DetailListActivity.class);
                intent.putExtra("project_number",project_number);
                intent.putExtra("project_source",project_source);
                startActivity(intent);
                overridePendingTransition(R.anim.move_in_right,R.anim.move_out_left);
            }
        });
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
        for (int i = 0; i < 20; i++){
            JSONObject data = (JSONObject)jsonArray.get(i);
            try {
                OrderListModel item = new OrderListModel(
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
 }
