package org.hand.mas.custom_view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.utl.OnScrollToRefreshListener;

/**
 * Created by gonglixuan on 15/4/29.
 */
public class CustomPullToRefreshListView extends ListView implements AdapterView.OnItemClickListener,AbsListView.OnScrollListener,OnScrollToRefreshListener {

    private int type = 2;
    /* 无刷新、下拉、仅上拉、Both 4种类型 */
    public static final int NORMAL = -1;
    public static final int ONLY_HEADER = 0;
    public static final int ONLY_FOOTER = 1;
    public static final int BOTH = 2;

    private int scrollState;
    private int state;// 当前的状态；
    private final int NONE = 0;// 正常状态；
    private final int PULL = 1;// 提示下拉状态；
    private final int READY = 2;// 刷新状态；
    private final int RELEASE = 3;// 提示释放状态；
    private final int REFRESH = 4;// 刷新状态；


    public static final int HEADER_VIEW = 0;
    public static final int FOOTER_VIEW = 1;
    /* 首个显示的位置 */
    private int firstVisibleItem;
    /* 最后显示的位置 */
    private int visibleLastIndex;

    private int startY;

    private boolean isFooterEnable = true;
    private boolean isRefresh = false;


    /* 顶部View */
    private View mHeaderView;
    /* 顶部View高度 */
    private int headerHeight;
    /* 底部View */
    private View mFooterView;
    /* 头部Tip */
    private TextView mHeaderTipTextView;
    /* 头部progressbar */
    private ProgressBar mProgressBar;

    public static String mTipContent4StatusPull = "下拉刷新";
    public static String mTipContent4StatusReady = "还没拉够？";
    public static String mTipContent4StatusRelease = "";
    public static String mTipContent4StatusRefresh = "正在请求数据";

    /* 滚动事件监听 */
    private OnScrollListener mScrollListener;
    /* 点击事件监听 */
    private OnItemClickListener mItemClickListener;
    /* 滚动到底部事件监听 */
    private OnScrollToRefreshListener mScrollToBottomListener;

    /* 处理状态改变 */
    private Handler mHandler = new Handler();


    public CustomPullToRefreshListView(Context context) {
        this(context,null);
    }

    public CustomPullToRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomPullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomPullToRefreshListView);
        type = typedArray.getInt(R.styleable.CustomPullToRefreshListView_pullType,BOTH);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        switch (type){
            case NORMAL:
                setFooterEnable(false);
                break;
            case ONLY_HEADER:
                setFooterEnable(false);
                this.setHeaderView(layoutInflater.inflate(R.layout.listview_header,null));
                break;
            case ONLY_FOOTER:
                this.setFooterView(layoutInflater.inflate(R.layout.listview_footer,null));
                break;
            case BOTH:
                this.setHeaderView(layoutInflater.inflate(R.layout.listview_header,null));
                this.setFooterView(layoutInflater.inflate(R.layout.listview_footer,null));
                break;
        }


        measureView(mHeaderView);
        mHeaderTipTextView = (TextView) mHeaderView.findViewById(R.id.tip);
        mProgressBar = (ProgressBar) mHeaderView.findViewById(R.id.progress);

        headerHeight = mHeaderView.getMeasuredHeight();
        /* 初始状态隐藏header */
        topPadding(-headerHeight);
        setOnItemClickListener(this);
        setOnScrollListener(this);
        typedArray.recycle();
    }


    /* custom method */

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setHeaderView(View headerView){
        this.mHeaderView = headerView;
        addHeaderView(this.mHeaderView);
        invalidate();
    }

    public void setFooterView(View footerView){
        this.mFooterView = footerView;
        addFooterView(this.mFooterView);
        this.mFooterView.setVisibility(INVISIBLE);
        invalidate();
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setScrollListener(OnScrollListener mScrollListener) {
        this.mScrollListener = mScrollListener;
    }

    public void setScrollToBottomListener(OnScrollToRefreshListener mScrollToBottomListener){
        this.mScrollToBottomListener = mScrollToBottomListener;
    }

    public boolean isFooterEnable() {
        return isFooterEnable;
    }

    public void setFooterEnable(boolean isFooterEnable) {
        this.isFooterEnable = isFooterEnable;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public int getState() {
        return state;
    }

    private void measureView(View view){
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams== null){
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = getChildMeasureSpec(0,0,layoutParams.width);
        int tempHeight = layoutParams.height;
        int height;
        if (tempHeight > 0){
            height = MeasureSpec.makeMeasureSpec(tempHeight,MeasureSpec.EXACTLY);
        }else {
            height = MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
        }
        view.measure(width,height);
    }

    private void topPadding(float topPadding){
        mHeaderView.setPadding(mHeaderView.getPaddingLeft(),(int)topPadding,mHeaderView.getPaddingRight(),mHeaderView.getPaddingBottom());
        mHeaderView.invalidate();
    }

    public void setTipContent(String tipContent){
        mHeaderTipTextView.setText(tipContent);
    }

    /**
     * 增加头、尾view
     * @param viewFlag
     */
    public void addExtraView(int viewFlag){
        if (viewFlag == HEADER_VIEW){
            if (getHeaderViewsCount() == 0 && mHeaderView != null){
                addHeaderView(mHeaderView);
            }
        }else if (viewFlag == FOOTER_VIEW){
            if (getFooterViewsCount() == 0 && mFooterView != null){
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
            if (getHeaderViewsCount() != 0 && mHeaderView != null){
                removeHeaderView(mHeaderView);
            }
        }else if (viewFlag == FOOTER_VIEW){
            if (getFooterViewsCount() != 0 && mFooterView != null){
                removeFooterView(mFooterView);
            }

        }
    }

    /**
     * 手指拖动动画
     * @param ev
     */
    private void onMove(MotionEvent ev){
        if (!isRefresh() || type == NORMAL || type == ONLY_FOOTER){
            return;
        }
        int tempY = (int) ev.getY();
        /* 拖动距离 */
        int space = tempY - startY;
        /* 露出部分 */
        int visibleHeight = space - headerHeight;
        switch (state){
            case NONE:
                if (space > 0){
                    state = PULL;
                    refreshViewByState();
                }
                break;
            case PULL:
                topPadding(visibleHeight);
                refreshViewByState();
                if (space > headerHeight + 30 && scrollState == SCROLL_STATE_TOUCH_SCROLL){
                    state = READY;
                    refreshViewByState();

                }

                break;
            case READY:
                topPadding(visibleHeight);
                refreshViewByState();
                break;
            case RELEASE:
                topPadding(visibleHeight);
                if (space < headerHeight + 30){
                    state = PULL;
                    refreshViewByState();
                }else if (space >=0){
                    state = NONE;
                    isRefresh = false;
                    refreshViewByState();
                }
                break;
            case REFRESH:

                break;
        }
    }
    private void refreshViewByState(){
        switch (state){
            case NONE:
                mProgressBar.setVisibility(INVISIBLE);
                setTipContent("");
                topPadding(-headerHeight);
                break;
            case PULL:
                mProgressBar.setVisibility(INVISIBLE);
                setTipContent(mTipContent4StatusPull);
                break;
            case READY:
                mProgressBar.setVisibility(INVISIBLE);
                setTipContent(mTipContent4StatusReady);
                break;
            case RELEASE:
                mProgressBar.setVisibility(VISIBLE);
                setTipContent(mTipContent4StatusRelease);
                topPadding(0);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        state = REFRESH;
                        refreshViewByState();
                    }
                },1000);
                break;
            case REFRESH:
                mProgressBar.setVisibility(INVISIBLE);
                setTipContent(mTipContent4StatusRefresh);
                onRefreshListener();

                break;
        }
    }

    public void completeTheRefreshing(){
        state = NONE;
        disappearHeaderView();
        setRefresh(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (type == NORMAL)
            return super.onTouchEvent(ev);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0){
                    setRefresh(true);
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (state == READY){
                    state = RELEASE;
                    refreshViewByState();
                } else if (state == PULL){
                    state = NONE;
                    setRefresh(false);
                    refreshViewByState();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /* anime */

    private void disappearHeaderView(){
        ObjectAnimator anim = ObjectAnimator.ofFloat(mHeaderView,"iamyourfather",0.0f,1.0f).setDuration(1000);
        anim.start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue() * headerHeight;
                topPadding(-1 * val);
            }
        });
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
                this.mFooterView.setVisibility(INVISIBLE);
                removeExtraView(CustomPullToRefreshListView.FOOTER_VIEW);
            }
            if (getFooterViewsCount() == 0){
                this.mFooterView.setVisibility(VISIBLE);
                addExtraView(CustomPullToRefreshListView.FOOTER_VIEW);
                setSelection(getAdapter().getCount() - 1);
            }
        }
        if (mScrollListener != null){
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
        this.scrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mScrollListener != null){
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        this.firstVisibleItem = firstVisibleItem;
        this.visibleLastIndex = firstVisibleItem + visibleItemCount;
        Log.d("REIN","firstVisibleItem: " + String.valueOf(firstVisibleItem));
    }

    @Override
    public void onBottomListener(AbsListView view, int scrollState) {
        if (mScrollToBottomListener != null){
            mScrollToBottomListener.onBottomListener(view, scrollState);
        }
    }

    @Override
    public void onRefreshListener() {
        if (mScrollToBottomListener != null){
            mScrollToBottomListener.onRefreshListener();
        }
        setFooterEnable(true);
    }

    public void setRELEASE() {
        state = RELEASE;
        refreshViewByState();
    }
}
