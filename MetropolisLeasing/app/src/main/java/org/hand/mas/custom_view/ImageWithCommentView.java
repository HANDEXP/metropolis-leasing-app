package org.hand.mas.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by gonglixuan on 15/3/16.
 */
public class ImageWithCommentView extends View {

    /* 圆角 */
    private int mBorderRadius;

    private Bitmap mSrcBitmap;
    private Bitmap mThumbBitmap;

    /* 画笔 */
    private Paint mBitmapPaint;
    private Paint mStrokePaint;
    private Paint mTextPaint;

    private int mStrokeWidth;

    private Matrix mMatrix;

    private RectF mRect;

    /* Construct Methods */
    public ImageWithCommentView(Context context) {
        this(context,null);
    }

    public ImageWithCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initPaint();
    }

    /* Private Methods */
    private void initPaint(){

        mBitmapPaint = new Paint();

        mStrokePaint = new Paint();

        mTextPaint = new Paint();

    }

    private void initRes(Context context){
        mMatrix = new Matrix();

        if (mSrcBitmap == null){

        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
