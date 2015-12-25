package org.hand.mas.customviewpagerindicator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import java.util.ArrayList;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * Created by gonglixuan on 15/4/13.
 */
public class CustomCirclePageIndicator extends View implements PageIndicator,ViewPager.OnPageChangeListener {
    private static final int INVALID_POINTER = -1;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;

    /**
     * 画笔
     */
    private final Paint mPaintPageFill = new Paint(ANTI_ALIAS_FLAG);
    private final Paint mPaintStroke = new Paint(ANTI_ALIAS_FLAG);
    private final Paint mPaintFill = new Paint(ANTI_ALIAS_FLAG);
    /**
     * 属性
     */
    private int mPageColor;
    private int mFillColor;
    private int mStrokeColor;
    private int mOrientation;
    private boolean mCentered;
    private boolean mSnap;
    private float mRadius;
    /**
     * 判断滑动事件的边界
     */
    private int mTouchSlop;
    /**
     * 非Snap模式下ViewPager当前位置
     */
    private int mCurrentPage;
    /**
     * Snap模式下ViewPager当前位置
     */
    private int mSnapPage;
    /**
     * ViewPager偏移值
     */
    private float mPageOffset;
    /**
     * ViewPager状态
     */
    private float mPageState;

    /**
     * 每一次onTouch事件产生时水平位置的最后偏移量
     */
    private float mLastMotionX = -1;
    /**
     * 当前活动的PointerId
     */
    private int mActivePointerId = INVALID_POINTER;
    /**
     * 是否主动拖动
     */
    private boolean mIsDragging;


    private static class SavedState extends BaseSavedState{

        int currentPage;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel source) {
            super(source);
            currentPage = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPage);
        }

        @SuppressWarnings("UnusedDeclaration")
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public CustomCirclePageIndicator(Context context) {
        this(context, null);
    }

    public CustomCirclePageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCirclePageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获得默认值
        final Resources res = getResources();
        final int defaultPageColor = res.getColor(R.color.default_circle_indicator_page_color);
        final int defaultFillColor = res.getColor(R.color.default_circle_indicator_fill_color);
        final int defaultOrientation = res.getInteger(R.integer.default_circle_indicator_orientation);
        final int defaultStrokeColor = res.getColor(R.color.default_circle_indicator_stroke_color);
        final boolean defaultCentered = res.getBoolean(R.bool.default_circle_indicator_centered);
        final boolean defaultSnap = res.getBoolean(R.bool.default_circle_indicator_snap);
        final float defaultStrokeWidth = res.getDimension(R.dimen.default_circle_indicator_stroke_width);
        final float defaultRadius = res.getDimension(R.dimen.default_circle_indicator_radius);

        //获得attr
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclePageIndicator, defStyleAttr, 0);
        mPaintPageFill.setColor(typedArray.getColor(R.styleable.CirclePageIndicator_pageColor,defaultPageColor));
        mPaintPageFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(typedArray.getColor(R.styleable.CirclePageIndicator_fillColor,defaultFillColor));
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintStroke.setColor(typedArray.getColor(R.styleable.CirclePageIndicator_strokeColor,defaultStrokeColor));
        mPaintStroke.setStyle(Paint.Style.STROKE);
        mPaintStroke.setStrokeWidth(typedArray.getDimension(R.styleable.CirclePageIndicator_strokeWidth,defaultStrokeWidth));

        mCentered = typedArray.getBoolean(R.styleable.CirclePageIndicator_centered,defaultCentered);
        mOrientation = typedArray.getInteger(R.styleable.CirclePageIndicator_android_orientation,defaultOrientation);
        mSnap = typedArray.getBoolean(R.styleable.CirclePageIndicator_snap,defaultSnap);
        mRadius = typedArray.getDimension(R.styleable.CirclePageIndicator_radius4indicator,defaultRadius);

        Drawable background = typedArray.getDrawable(R.styleable.CirclePageIndicator_android_background);
        if (background != null) {
            setBackgroundDrawable(background);
        }

        typedArray.recycle();

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

    }

    /* Getter and Setter */
    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float mRadius) {
        this.mRadius = mRadius;
        invalidate();
    }

    public boolean isSnap() {
        return mSnap;
    }

    public void setSnap(boolean mSnap) {
        this.mSnap = mSnap;
        invalidate();
    }

    public boolean isCentered() {
        return mCentered;
    }

    public void setCentered(boolean mCentered) {
        this.mCentered = mCentered;
        invalidate();
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setOrientation(int mOrientation) {
        this.mOrientation = mOrientation;
        invalidate();
    }

    public int getFillColor() {
        return mPaintFill.getColor();
    }

    public void setFillColor(int mFillColor) {
        mPaintFill.setColor(mFillColor);
        invalidate();
    }

    public int getPageColor() {
        return mPaintPageFill.getColor();
    }

    public void setPageColor(int mPageColor) {
        mPaintPageFill.setColor(mPageColor);
        invalidate();
    }

    public int getStrokeColor() {
        return mPaintStroke.getColor();
    }

    public void setStrokeColor(int mStrokeColor) {
        mPaintStroke.setStrokeWidth(mStrokeColor);
    }

    public void getStrokeWidth(){
        mPaintStroke.getStrokeWidth();
    }

    /* Override Methods */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mViewPager == null){
            return;
        }
        final int count = mViewPager.getAdapter().getCount();
        if (count == 0){
            return;
        }
        if (mCurrentPage >= count){
            setCurrentItem(count - 1);
            return;
        }

        int longSize;
        int longPaddingBefore;
        int longPaddingAfter;
        int shortPaddingBefore;
        if (mOrientation == LinearLayout.HORIZONTAL){
            longSize = getWidth();
            longPaddingBefore = getPaddingLeft();
            longPaddingAfter = getPaddingRight();
            shortPaddingBefore = getPaddingTop();
        }else {
            longSize = getHeight();
            longPaddingBefore = getPaddingTop();
            longPaddingAfter = getPaddingBottom();
            shortPaddingBefore = getPaddingLeft();
        }

        /* 园间距 */
        float space = 3 * mRadius;
        /* 短偏移值 */
        float shortOffset = shortPaddingBefore + mRadius;
        /* 长偏移值 */
        float longOffset = longPaddingBefore + mRadius;
        if (mCentered){
            longOffset += ((longSize - longPaddingBefore - longPaddingAfter) / 2.0f) - ((count * space) / 2.0f);
        }
        float dx;
        float dy;

        float pageFillRadius = mRadius;
        if (mPaintStroke.getStrokeWidth() > 0){
            pageFillRadius -= mPaintStroke.getStrokeWidth() / 2.0f;
        }

        //Draw stroked circles
        for (int i = 0; i < count;i++){
            /* 圆心位置 */
            float drawLong = longOffset + (i * space);
            if (mOrientation == LinearLayout.HORIZONTAL){
                dx = drawLong;
                dy = shortOffset;
            }else {
                dx = shortOffset;
                dy = drawLong;
            }
            if (mPaintPageFill.getAlpha() >0){
                canvas.drawCircle(dx,dy,pageFillRadius,mPaintPageFill);
            }
            if (pageFillRadius != mRadius){
                canvas.drawCircle(dx,dy,mRadius,mPaintStroke);
            }
            /* about snap */
            float cx = (mSnap ? mSnapPage : mCurrentPage) * space;
            if (!mSnap){
                cx += mPageOffset * space;
            }
            if (mOrientation == LinearLayout.HORIZONTAL){
                dx = longOffset + cx;
                dy = shortOffset;
            }else {
                dx = shortOffset;
                dy = longOffset + cx;
            }
            canvas.drawCircle(dx,dy,mRadius,mPaintFill);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mOrientation == LinearLayout.HORIZONTAL){
            setMeasuredDimension(measureLong(widthMeasureSpec),measureShort(heightMeasureSpec));
        }else {
            setMeasuredDimension(measureShort(widthMeasureSpec),measureLong(heightMeasureSpec));
        }
    }

    /**
     * 测量长边
     * @param measureSpec
     * @return
     */
    private int measureLong(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY || mViewPager == null){
            result = specSize;
        }else{
            int count = mViewPager.getAdapter().getCount();
            /* 左右padding加直径加间距,为处理vertical +1 */
            result = (int) (getPaddingLeft() + getPaddingRight() + (count * 2 * mRadius) + (count-1) * mRadius + 1);
            if (specMode == MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return result;
    }

    /**
     * 测量短边
     * @param measureSpec
     * @return
     */
    private int measureShort(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else {
            result = (int) (getPaddingTop() + getPaddingBottom() + (2 * mRadius * 1));
            if (specMode == MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /* 冒泡 */
        if (super.onTouchEvent(event)){
            return true;
        }
        if (mViewPager == null || mViewPager.getAdapter().getCount() == 0){
            return false;
        }
        int action = event.getAction() & MotionEventCompat.ACTION_MASK;
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(event,0);
                mLastMotionX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = MotionEventCompat.findPointerIndex(event, mActivePointerId);
                final float x = MotionEventCompat.getX(event, activePointerIndex);
                /*偏移值*/
                final float deltaX = x - mLastMotionX;
                if (mIsDragging){
                    mLastMotionX = x;
                    if (mViewPager.isFakeDragging() || mViewPager.beginFakeDrag()){
                        mViewPager.fakeDragBy(deltaX);
                    }

                }else if (!mIsDragging){
                    if (Math.abs(deltaX) > mTouchSlop){
                        mIsDragging = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                /* 触碰（非滑动）改变位置 */
                if (!mIsDragging){
                    int count = mViewPager.getAdapter().getCount();
                    int width = getWidth();
                    float halfWidth = width / 2f;
                    float sixthWidth = width / 6f;
                    if (mCurrentPage > 0 && event.getX() < halfWidth - sixthWidth){
                        if (event.getAction() == MotionEvent.ACTION_UP)
                            mViewPager.setCurrentItem(mCurrentPage - 1);
                    }else if (mCurrentPage < count - 1 && event.getX() > halfWidth + sixthWidth){
                        if (event.getAction() == MotionEvent.ACTION_UP){
                            mViewPager.setCurrentItem(mCurrentPage + 1);
                        }
                    }
                    return true;
                }
                mIsDragging = false;
                mActivePointerId = INVALID_POINTER;
                if (mViewPager.isFakeDragging()) mViewPager.endFakeDrag();
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                int pointIndex = MotionEventCompat.getActionIndex(event);
                int pointId = MotionEventCompat.getPointerId(event,pointIndex);
                if (pointId == mActivePointerId){
                    int newPointIndex = pointIndex == 0 ? 1 : 0;
                    mActivePointerId = MotionEventCompat.getPointerId(event,newPointIndex);
                }
                mLastMotionX = MotionEventCompat.getX(event,MotionEventCompat.findPointerIndex(event,mActivePointerId));
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                int index = MotionEventCompat.getActionIndex(event);
                mLastMotionX = MotionEventCompat.getX(event, index);
                mActivePointerId = MotionEventCompat.getPointerId(event, index);
                break;
        }
        return true;

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPage = mCurrentPage;
        return savedState;

    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPage = savedState.currentPage;
        mSnapPage = savedState.currentPage;
        requestLayout();
    }

    @Override
    public void setViewPager(ViewPager viewPager) {
        if (mViewPager == viewPager){
            return;
        }
        if (viewPager.getAdapter() == null){
            throw new IllegalStateException("Adapter is null");
        }
        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(this);
        invalidate();
    }

    @Override
    public void setViewPager(ViewPager viewPager, int initialPosition) {
        setViewPager(viewPager);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int position) {
        if (mViewPager == null){
            throw new IllegalStateException("ViewPager is null");
        }
        mViewPager.setCurrentItem(position);
        mCurrentPage = position;
        invalidate();
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mListener = onPageChangeListener;
    }

    @Override
    public void notifyDataSetChanged() {
        invalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mCurrentPage = position;
        /* postionOffset */
        mPageOffset = positionOffset;
        invalidate();
        if (mListener != null){
            mListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {

        if (mListener != null){
            mListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mPageState = state;
        if (mListener != null){
            mListener.onPageScrollStateChanged(state);
        }
    }
}
