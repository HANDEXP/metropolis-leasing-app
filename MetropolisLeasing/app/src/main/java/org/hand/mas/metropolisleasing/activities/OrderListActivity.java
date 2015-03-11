package org.hand.mas.metropolisleasing.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.adapters.OrderListAdapter;
import org.hand.mas.metropolisleasing.models.OrderModel;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;

/**
 * Created by gonglixuan on 15/3/10.
 */
public class OrderListActivity extends Activity {

    private PullToRefreshLayout mPullToRefreshLayout;
    private ListView orderListView;
    private List<OrderModel> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.order_list_layout);
        ActionBarPullToRefresh.from(this).allChildrenArePullable().setup(mPullToRefreshLayout);
        orderListView = (ListView) findViewById(R.id.order_list);
        orderList = new ArrayList<OrderModel>();
        orderList.add(new OrderModel());
        orderListView.setAdapter(new OrderListAdapter(orderList,getApplicationContext()));
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
}
