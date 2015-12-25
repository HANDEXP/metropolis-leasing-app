package org.hand.mas.metropolisleasing.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hand.hrms4android.exception.ParseExpressionException;
import com.hand.hrms4android.parser.Expression;
import com.hand.hrms4android.parser.xml.XmlConfigReader;


import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.hand.mas.custom_view.ClearEditText;
import org.hand.mas.custom_view.CustomPullToRefreshListView;
import org.hand.mas.custom_view.SlidingMenu;
import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.application.MSApplication;
import org.hand.mas.metropolisleasing.models.FunctionListSvcModel;
import org.hand.mas.metropolisleasing.models.OrderListModel;
import org.hand.mas.metropolisleasing.models.OrderListSvcModel;
import org.hand.mas.utl.CommonAdapter;
import org.hand.mas.utl.ConstantAnim;
import org.hand.mas.utl.OnScrollToRefreshListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by gonglixuan on 15/3/10.
 */
public class OrderListActivity extends Activity implements LMModelDelegate {


    private ListView mOrderListView;

    private TextView mTitleTextView;
    private ImageView mFilterImageView;
    private ImageView mSlideMenuImageView;
    private SlidingMenu mSlidingMenu;
    private DialogPlus dialog;
    private LinearLayout TakePhotoAndUploadLL;
    private LinearLayout SettingLL;
    private LinearLayout CalculatorLL;
    private ClearEditText mFilterEditText;

    private List mOrderList;
    private FunctionListSvcModel mModel;
    private FunctionListSvcModel mFunctionListModel;
    private CommonAdapter adapter;
    private HashMap<String, String> param;
    private static int pageNum;
    private boolean mFlag;

    private boolean mOrderListFlag = false;

    private int visibleLastIndex;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (param == null) {
                param = new HashMap<String, String>();
            }
            if (mOrderListFlag == true)
                return;
            param.put("page_num", String.valueOf(++pageNum));
            mModel.load();
            mOrderListFlag = true;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MSApplication.getApplication().addActivity(this);
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
        resetClickable(CalculatorLL);
        if (mFunctionListModel == null) {
            mFunctionListModel = new FunctionListSvcModel(this);
        }
        mFunctionListModel.load();
//        if (mOrderListView != null){
//            mOrderListView.setRELEASE();
//        }

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
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        } else {
            exitWithTwiceBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void modelDidFinishLoad(LMModel model) {

        AsHttpRequestModel reponseModel = (AsHttpRequestModel) model;
        if (model == mModel) {
            String json = new String(reponseModel.mresponseBody);
            try {
                JSONObject jsonObj = new JSONObject(json);
                String code = ((JSONObject) jsonObj.get("head")).get("code").toString();
                if (code.equals("ok")) {

                    JSONArray bodyArr = (JSONArray) ((JSONObject) jsonObj.get("body")).get("list");
                    JSONArray itemsArr = (JSONArray) bodyArr.getJSONObject(0).get("items");
                    try {
                        initializeData(itemsArr);
                        if (adapter == null) {
                            adapter = new CommonAdapter<JSONObject>(getApplicationContext(), mOrderList, R.layout.cell_home) {
                                @Override
                                public void convert(org.hand.mas.utl.ViewHolder helper, JSONObject obj, int position) {
                                    TextView textView = helper.getView(R.id.tv_user_name);

                                    try {
                                        textView.setText((String)obj.get("title"));
                                        final String url = (String)obj.get("url");
                                        final String title = (String)obj.get("title");
                                        textView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startWebViewActivity(url, title);
                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


//                                    TextView projectNumberTextView = helper.getView(R.id.project_number_for_order);
//                                    TextView projectStatusDesc = helper.getView(R.id.project_status_desc_for_order);
//                                    TextView bpName = helper.getView(R.id.bp_name_for_order);
//                                    TextView projectSource = helper.getView(R.id.project_source_for_order);
//                                    TextView bpClass = helper.getView(R.id.bp_class_for_order);
//                                    TextView identifierCode = helper.getView(R.id.identifier_code_for_order);
//
//                                    OrderListModel item = (OrderListModel) obj;
//
//                                    projectNumberTextView.setText(item.getProjectNumber());
//                                    projectStatusDesc.setText(item.getProjectStatusDesc());
//                                    bpName.setText(item.getBpName());
//                                    projectSource.setText(item.getProjectSource());
//                                    String bpStr = item.getBpClass().equals("NP") ? "个人" : "法人";
//                                    bpClass.setText(bpStr.concat("识别号:"));
//                                    if (bpClass.equals("ORG")) {
//                                        identifierCode.setText(item.getOrganizationCode());
//                                    } else if (bpClass.equals("NP")) {
//                                        identifierCode.setText(item.getIdCardNo());
//                                    }

                                }
                            };
                            mOrderListView.setAdapter(adapter);
                        } else {
                            int count4newData = bodyArr.length();

                            if (count4newData > 0) {
                                Toast.makeText(OrderListActivity.this, "加载了" + String.valueOf(count4newData + "条新数据"), Toast.LENGTH_SHORT).show();
//                                mOrderListView.setFooterEnable(true);
                            } else {
                                Toast.makeText(OrderListActivity.this, "没有更多的数据了", Toast.LENGTH_SHORT).show();
//                                mOrderListView.setFooterEnable(false);
                            }
//                            mOrderListView.removeExtraView(CustomPullToRefreshListView.FOOTER_VIEW);
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (mOrderListFlag == true) {
                    mOrderListFlag = false;
                }

            }

        } else if (model.equals(this.mFunctionListModel)) {
            String json = new String(reponseModel.mresponseBody);
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(json);
                String code = ((JSONObject) jsonObj.get("head")).get("code").toString();
                if (code.equals("ok")) {
                    JSONArray bodyArr = (JSONArray) ((JSONObject) jsonObj.get("body")).get("list");
                    JSONArray itemsArr = (JSONArray) bodyArr.getJSONObject(0).get("items");
                    addMenuItems(itemsArr);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        /* 考虑并发 */
//        if (mOrderListView.getState() != 0 && model instanceof OrderListSvcModel){
//            mOrderListView.setTipContent("请求数据成功");
//            mOrderListView.completeTheRefreshing();
//        }
    }

    @Override
    public void modelDidStartLoad(LMModel model) {

        Log.d("StartLoad", "modelDidStartLoad");
//        fadeAnim(mSlideMenuImageView,0.0f,90.0f,1000);

    }

    @Override
    public void modelDidFailedLoadWithError(LMModel model) {
        Log.d("LoadWithError", "modelDidFailedLoadWithError");
//        mOrderListView.removeExtraView(CustomPullToRefreshListView.FOOTER_VIEW);
//        if (mOrderListView.isRefresh()){
//            mOrderListView.setTipContent("请求数据失败");
//            mOrderListView.completeTheRefreshing();
//        }
    }

    /*私有方法*/
    private void bindAllViews() {
        pageNum = 1;
        mModel = new FunctionListSvcModel(this);

        LayoutInflater layoutInflater =
                (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mOrderListView = (ListView) findViewById(R.id.order_list);

        mTitleTextView = (TextView) findViewById(R.id.titleTextView);
        mFilterImageView = (ImageView) findViewById(R.id.filter_for_orderList);
        mSlideMenuImageView = (ImageView) findViewById(R.id.slide_menu);
        mSlidingMenu = (SlidingMenu) findViewById(R.id.sliding_menu_and_content);
        /* Sliding Menu */

        SettingLL = (LinearLayout) mSlidingMenu.findViewById(R.id.settingLL);

        mSlideMenuImageView.setVisibility(View.VISIBLE);
        mSlideMenuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlidingMenu.toggle();
            }
        });

        OnScrollToRefreshListener onScrollToRefreshListener = new OnScrollToRefreshListener() {
            @Override
            public void onBottomListener(AbsListView view, int scrollState) {
//                mOrderListView.setFooterEnable(false);
                mHandler.postDelayed(mRunnable, 2000);
            }

            @Override
            public void onRefreshListener() {
                /* 重置适配器和数据 */
                resetAdapter();
                mModel.load();
            }
        };
//        mOrderListView.setScrollToBottomListener(onScrollToRefreshListener);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSlidingMenu.getIsOpen() == false) {
                    String project_number = (String) ((TextView) view.findViewById(R.id.project_number_for_order)).getText();
                    String project_source = (String) ((TextView) view.findViewById(R.id.project_source_for_order)).getText();

                    String project_id = ((OrderListModel) parent.getAdapter().getItem(position)).getProjectId();

                    Intent intent = new Intent(getApplicationContext(), DetailListActivity.class);
                    intent.putExtra("project_id", project_id);
                    intent.putExtra("project_number", project_number);
                    intent.putExtra("project_source", project_source);
                    startActivity(intent);
                    overridePendingTransition(R.anim.move_in_right, R.anim.alpha_out);
                }
            }
        };
//        mOrderListView.setItemClickListener(onItemClickListener);
//        mOrderListView.addFooterView(mFooterView);
        mTitleTextView.setText("首页");
        mTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mOrderListView.setRELEASE();
            }
        });

        SettingLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSettingActivity();
            }
        });

        setDefaultUserData();
    }

    /*
    * 初始化数据
    *
    * */
    private void initializeData(JSONArray jsonArray) throws JSONException {
        if (mOrderList == null) {
            mOrderList = new ArrayList<OrderListModel>();
        }
        int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            JSONObject data = (JSONObject) jsonArray.get(i);
            try {

                mOrderList.add(data);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            } finally {

            }
        }
    }

    /* 重置适配器和数据 */
    private void resetAdapter() {
        mOrderList = null;
        adapter = null;
        pageNum = 1;
    }

    /* 两次后退退出 */
    private void exitWithTwiceBackPressed() {
        if (mFlag == false) {
            mFlag = true;
            Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_LONG).show();
            new CountDownTimer(5 * 1000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    mFlag = false;
                }
            }.start();
        } else {
            finish();
            return;
        }
    }

    /**
     * 打开SettingActivity
     */
    private void startSettingActivity() {
        Intent intent = new Intent(OrderListActivity.this, SettingActivity.class);
        if (mSlidingMenu.getIsOpen()) {
            mSlideMenuImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_for_slide_menu));
            mSlidingMenu.closeMenu();
        }
        while (mSlidingMenu.getIsOpen()) {
            continue;
        }
        startActivity(intent);
        overridePendingTransition(R.anim.move_in_bottm, R.anim.alpha_out);
    }

    /**
     * 打开FilterActivity
     *
     * @param v
     */
    private void startFilterActivity(View v) {
        String filter_param = ((EditText) v).getText().toString();
        dialog.dismiss();
        if (filter_param.isEmpty()) {

        } else {
            Intent intentForFilter = new Intent(OrderListActivity.this, FilteredOrderListActivity.class);
            intentForFilter.putExtra("filter_param", filter_param);
            startActivity(intentForFilter);
            overridePendingTransition(R.anim.move_in_right, R.anim.move_out_left);
        }
    }

    /**
     * 打开HtmlBaseActivity
     *
     * @param url
     */
    private void startWebViewActivity(String url, String title) {
        Intent intent = new Intent(OrderListActivity.this, HtmlBaseActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        startActivity(intent);
        overridePendingTransition(R.anim.move_in_bottm, R.anim.alpha_out);
    }

    private void addMenuItems(JSONArray itemsArr) {
        LinearLayout functionListLL = (LinearLayout) findViewById(R.id.function_list_ll);
        functionListLL.removeAllViews();
        int count = itemsArr.length();
        for (int i = 0; i < count; i++) {
            try {
                TextView ItemTextView = new TextView(getApplicationContext());
                ItemTextView.setTextColor(Color.WHITE);
                ItemTextView.setPadding(0, 5, 0, 5);
                ItemTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                JSONObject item = (JSONObject) itemsArr.get(i);
                final String url = (String) item.get("url");
                final String title = (String) item.get("title");
                ItemTextView.setText(title);
                ItemTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startWebViewActivity(url, title);
                    }
                });
                functionListLL.addView(ItemTextView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 重置clickable
     */
    private void resetClickable(View view) {
        if (view != null && !view.isClickable()) {
            view.setClickable(true);
        }
    }
    /*
* 取出缓存的用户资料
*/
    private void setDefaultUserData(){
        TextView usernameTextView = (TextView) findViewById(R.id.username_textview_in_menu);
        SharedPreferences preferences = getSharedPreferences("userInfo",MODE_APPEND);
        String userName = preferences.getString("userName","");
        usernameTextView.setText(userName);
    }

}
