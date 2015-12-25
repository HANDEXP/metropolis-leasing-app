package org.hand.mas.utl;

import android.widget.AbsListView;

/**
 * Created by gonglixuan on 15/4/30.
 */
public interface OnScrollToRefreshListener {
    /**
     * 滚动到底部时触发事件
     * @param view
     * @param scrollState
     */
    void onBottomListener(AbsListView view, int scrollState);

    /**
     * 头部刷新事件
     */
    void onRefreshListener();
}
