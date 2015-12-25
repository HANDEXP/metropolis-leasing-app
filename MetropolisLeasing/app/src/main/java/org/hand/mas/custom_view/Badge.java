package org.hand.mas.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationSet;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.utl.OptAnimationLoader;

/**
 * Created by gonglixuan on 15/3/30.
 */
public class Badge extends View {

    private Paint mCirclePaint;
    private Paint mCountPaint;

    /* 圆大小及颜色 */
    private int mRadius;
    private int mColor;

    /* 字体大小 */
    private int mFontSize;
    private String mCount;
    float textOffsetY;

    /*
     * 3 x 3 Matrix,用于缩放
     */
    private Matrix mMatrix;

    /*
     *渲染图像，使用图像为绘制图形着色
     *
     */
    private BitmapShader mBitmapShader;

    /*
     * view 宽度
     */
    private int mWidth;



    public Badge(Context context) {
        this(context, null);
    }

    public Badge(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Badge);
        mColor = typedArray.getColor(R.styleable.Badge_solidColor,R.color.theme_color);
        mRadius = typedArray.getDimensionPixelSize(R.styleable.Badge_innerRadius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()));
        mFontSize = typedArray.getDimensionPixelSize(R.styleable.Badge_innerFontSize,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,getResources().getDisplayMetrics()));
        mCount = String.valueOf(0);
        initMatrix();
        initPaint();

        typedArray.recycle();
    }

    private void initMatrix() {
        mMatrix = new Matrix();
    }

    private void initPaint() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCirclePaint.setColor(mColor);


        mCountPaint = new Paint();
        mCountPaint.setAntiAlias(true);
        mCountPaint.setColor(Color.WHITE);
        mCountPaint.setTextAlign(Paint.Align.CENTER);
        mCountPaint.setTextSize(mFontSize);

        textOffsetY = (mCountPaint.descent() + mCountPaint.ascent()) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRadius = Math.min(mRadius,mWidth/2);
        canvas.drawCircle(mWidth / 2, mWidth / 2, mRadius, mCirclePaint);
        canvas.drawText(mCount,mWidth/2,mWidth/2-textOffsetY,mCountPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(mWidth,mWidth);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setCount(String count){
        this.mCount = count;
        invalidate();
        AnimationSet anim =  (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        if (!count.equals("0")){
            startAnimation(anim);
        }

    }

    public String setCount(){
        return this.mCount;
    }
}
