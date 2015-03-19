package org.hand.mas.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import org.hand.mas.metropolisleasing.R;

/**
 * Created by gonglixuan on 15/3/5.
 */
public class RoundImageView extends ImageView {


    /*
    * 定义图片类型，圆形或圆角
    * 设置默认圆角大小
    * */
    private int type;
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;
    private static final int BODER_RADIUS_DEFAULT = 5;

    /*
    *
    * 圆角的大小
    * */
    private int mBorderRadius;

    /**
     * 绘图
     */
    private Paint mBitmapPaint;
    private Paint mStrokePaint;

    private  int mRadius;
    private  int mStrokeWidth;

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

    private RectF mRoundRect;



    public RoundImageView(Context context) {
        this(context, null);
    }


    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);

        mStrokeWidth = 5;
        mStrokePaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mStrokePaint.setStrokeWidth(mStrokeWidth);
        mStrokePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mStrokePaint.setColor(Color.parseColor("#ffffff"));

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        /*
        * default BorderRadius is 10dp
        * default type is circle
        * */
        mBorderRadius = a.getDimensionPixelSize(R.styleable.RoundImageView_borderRadius,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,BODER_RADIUS_DEFAULT,getResources().getDisplayMetrics()));
        type = a.getInt(R.styleable.RoundImageView_type,TYPE_CIRCLE);

        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (type == TYPE_CIRCLE){
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight()) + mStrokeWidth;
            mRadius = mWidth / 2 - mStrokeWidth;
            setMeasuredDimension(mWidth,mWidth);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (type == TYPE_ROUND){
            mRoundRect = new RectF(0,0,getWidth(),getHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null){
            return;
        }
        setUpShader();
        if (type == TYPE_ROUND){
            canvas.drawRoundRect(mRoundRect,mBorderRadius,mBorderRadius,mBitmapPaint);

        }else{

            canvas.drawCircle(mRadius+mStrokeWidth,mRadius+mStrokeWidth,mRadius+mStrokeWidth/2,mStrokePaint);
            canvas.drawCircle(mRadius+mStrokeWidth,mRadius+mStrokeWidth,mRadius,mBitmapPaint);
//            measure(0, 0);

        }
    }



    /*
         * initialize BitmapShader
         *
         */
    private void setUpShader(){
        Drawable drawable = getDrawable();
        if (drawable == null){
            return;
        }
        Bitmap bmp = drawableToBitmap(drawable);
        mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if(type == TYPE_CIRCLE){
            int bSize = Math.min(bmp.getWidth(),bmp.getHeight());
            scale = mWidth * 1.0f / (bSize);
        }else if(type == TYPE_ROUND){
            scale = Math.max(getWidth() * 1.0f / bmp.getWidth(), getHeight() * 1.0f / bmp.getHeight());
        }
        mMatrix.setScale(scale,scale);
        mBitmapShader.setLocalMatrix(mMatrix);
        mBitmapPaint.setShader(mBitmapShader);

    }

    /**
     *
     * convert drawable to bitmap
     * @param drawable
     * return Bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable){
        if(drawable instanceof BitmapDrawable){
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,w,h);
        drawable.draw(canvas);
        return bitmap;
    }

    private int dp2px(int dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,getResources().getDisplayMetrics());
    }

    /* public methods */
    public RoundImageView setBorderRadius(int borderRadius){
        int pxVal = dp2px(borderRadius);
        if (this.mBorderRadius != pxVal){
            this.mBorderRadius = pxVal;
            invalidate();
        }
        return this;
    }
    public RoundImageView setType(int type){
        switch (this.type){
            case TYPE_CIRCLE:
                this.type = type;
                break;
            case TYPE_ROUND:
                this.type = type;
                break;
            default:
                this.type = TYPE_CIRCLE;
                break;
        }
        requestLayout();
        return this;
    }

    public RoundImageView setStrokeWidth(int strokeWidth){
        int pxVal = dp2px(strokeWidth);
        if (this.mStrokeWidth != pxVal){
            this.mStrokeWidth = pxVal;
            invalidate();
        }


        return this;
    }
}
