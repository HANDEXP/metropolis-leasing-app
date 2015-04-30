package org.hand.mas.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.utl.OnScrollToBottomListener;

/**
 * Created by gonglixuan on 15/4/29.
 */
public class CustomPullToRefreshListView extends ListView implements AdapterView.OnItemClickListener,AbsListView.OnScrollListener,OnScrollToBottomListener{

    public static final int HEADER_VIEW = 0;
    public static final int FOOTER_VIEW = 1;
    /* 最后显示的位置 */
    private int visibleLastIndex;

    private boolean isFooterEnable = true;


    /* 顶部View */
    private View mHeaderView;
    /* 底部View */
    private View mFooterView;

    /* 滚动事件监听 */
    private OnScrollListener mScrollListener;
    /* 点击事件监听 */
    private OnItemClickListener mItemClickListener;
    /* 滚动到底部事件监听 */
    private OnScrollToBottomListener mScrollToBottomListener;


    public CustomPullToRefreshListView(Context context) {
        this(context,null);
    }

    public CustomPullToRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomPullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        this.setFooterView(layoutInflater.inflate(R.layout.listview_footer,null));
        setOnItemClickListener(this);
        setOnScrollListener(this);
    }

    public void setFooterView(View footerView){
        this.mFooterView = footerView;
        addFooterView(this.mFooterView);
        invalidate();
    }

    public void setItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setScrollListener(OnScrollListener mScrollListener) {
        this.mScrollListener = mScrollListener;
    }

    public void setScrollToBottomListener(OnScrollToBottomListener mScrollToBottomListener){
        this.mScrollToBottomListener = mScrollToBottomListener;
    }

    public boolean isFooterEnable() {
        return isFooterEnable;
    }

    public void setFooterEnable(boolean isFooterEnable) {
        this.isFooterEnable = isFooterEnable;
    }

    /**
     * 增加头、尾view
     * @param viewFlag
     */
    public void addExtraView(int viewFlag){
        if (viewFlag == HEADER_VIEW){

        }else if (viewFlag == FOOTER_VIEW){
            if (getFooterViewsCount() == 0){
                addFooterView(mFooterView);
            }
        }
    }

    /**
     * 移除头、尾View
     * @param viewFlag
     */
    public void removeExtraView(int viewFlag){
        if (viewFlag == HEADER_VIEW){

        }else if (viewFlag == FOOTER_VIEW){
            removeFooterView(mFooterView);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mItemClickListener != null){
            mItemClickListener.onItemClick(parent,view,position,id);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int itemsLastIndex;
        itemsLastIndex = getAdapter().getCount();
        Log.d("HARU", "itemsLastIndex: " + String.valueOf(itemsLastIndex) + " visibleLastIndex: " + String.valueOf(visibleLastIndex) + " FooterViewsCount: " + String.valueOf(getFooterViewsCount()));
        if (itemsLastIndex == visibleLastIndex  && isFooterEnable() == true){

            onBottomListener(view,scrollState);

            if (getFooterViewsCount() != 0){
                removeExtraView(CustomPullToRefreshListView.FOOTER_VIEW);
            }
            if (getFooterViewsCount() == 0){
                addExtraView(CustomPullToRefreshListView.FOOTER_VIEW);
                setSelection(getAdapter().getCount() - 1);
            }
        }
        if (mScrollListener != null){
            mScrollListener.onScrollStateChanged(view,scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mScrollListener != null){
            mScrollListener.onScroll(view,firstVisibleItem,visibleItemCount,totalItemCount);
        }
        visibleLastIndex = firstVisibleItem + visibleItemCount;
    }

    @Override
    public void onBottomListener(AbsListView view, int scrollState) {
        if (mScrollToBottomListener != null){
            mScrollToBottomListener.onBottomListener(view,scrollState);
        }
    }
}
