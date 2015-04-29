package org.hand.mas.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by gonglixuan on 15/4/29.
 */
public class CustomPullToRefreshListView extends ListView implements AdapterView.OnItemClickListener,AbsListView.OnScrollListener{

    private View mFooterView;

    /* 滚动事件监听 */
    private OnScrollListener mScrollListener;
    /* 点击事件监听 */
    private OnItemClickListener mItemClickListener;


    public CustomPullToRefreshListView(Context context) {
        this(context,null);
    }

    public CustomPullToRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomPullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setFooterView(View footerView){
        this.mFooterView = footerView;
    }

    public void setItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setScrollListener(OnScrollListener mScrollListener) {
        this.mScrollListener = mScrollListener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
