package org.hand.mas.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import org.hand.mas.metropolisleasing.R;

/**
 * Created by gonglixuan on 15/5/20.
 */
public class CustomProgressBar extends View implements OnProgressingListener{

    private View mView;
    private int mBackgroundColor;
    private int mThickColor;
    private int mCircleWidth;
    private int mSpeed;
    private float mProgress = 0;
    private Paint mPaint;

    /* listeners */
    private OnProgressingListener mProgressingListener;
    /* thread */

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            startProgress();
            while (execute){

                mProgress =  isDebug ? mProgress+1 : mProgress;
                onProgress(mView);
                if (mProgress > 360){
                    stopProgress();
                    return;
                }
                postInvalidate();
                try {
                    Thread.sleep(mSpeed);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };
    private volatile boolean execute = false;

    private boolean isDebug = false;


    public CustomProgressBar(Context context) {

        this(context, null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,R.styleable.CustomProgressBar,defStyleAttr,0);
        int count = typedArray.getIndexCount();
        for (int i = 0;i < count; i++){
            int attr = typedArray.getIndex(i);
            switch (attr){
                case R.styleable.CustomProgressBar_backgroundColor:
                    mBackgroundColor = typedArray.getColor(attr, Color.BLUE);
                    break;
                case R.styleable.CustomProgressBar_thickColor:
                    mThickColor = typedArray.getColor(attr,Color.RED);
                    break;
                case R.styleable.CustomProgressBar_circleWidth:
                    mCircleWidth = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,20,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomProgressBar_speed:
                    mSpeed = typedArray.getInteger(attr,20);
                    break;
            }
        }
        typedArray.recycle();
        mPaint = new Paint();
        mView = this;



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mWidth = Math.min(getMeasuredWidth(),getMeasuredHeight());
        setMeasuredDimension(mWidth,mWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = getWidth() / 2;
        int radius = center - mCircleWidth / 2;
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        float start = (float) (center - radius * 0.9);
        float end = (float) (center + radius * 0.9);
        RectF oval = new RectF(start, start, end, end);
        mPaint.setColor(mBackgroundColor);
        canvas.drawCircle(center, center, radius, mPaint);

        mPaint.setColor(mThickColor);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval, -90, mProgress, false, mPaint);
        if (mProgress >= 360){
            onComplete(mView);
        }
    }

    public void setProgress(float mProgress) {
        this.mProgress = mProgress;
    }

    public void increasedProgress(int increment){
        this.mProgress = this.mProgress + increment;
    }

    public void setProgressingListener(OnProgressingListener mProgressingListener) {
        this.mProgressingListener = mProgressingListener;
    }

    public void startProgress(){
        execute = true;
        mProgress = 0;
        this.setVisibility(View.VISIBLE);
    }

    public void stopProgress(){
        execute = false;
//        this.setVisibility(View.GONE);
    }

    public void startThread(){
        startProgress();
        new Thread(runnable).start();

    }

    @Override
    public void onStart(View view) {
        if (mProgressingListener != null){
            mProgressingListener.onStart(view);
        }
        Log.d("CPB","OnStart");
    }

    @Override
    public void onProgress(View view) {
        if (mProgressingListener != null){
            mProgressingListener.onProgress(view);
        }

    }

    @Override
    public void onComplete(View view) {
        if (mProgressingListener != null){
            mProgressingListener.onComplete(view);
        }
        stopProgress();
        mView.setVisibility(GONE);
        Log.d("CPB","OnComplete");
    }

}