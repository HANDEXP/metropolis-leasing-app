package org.hand.mas.custom_view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by gonglixuan on 15/3/4.
 */
public class RoundImageDrawable extends Drawable {

    private Paint mPaint;
    private Bitmap mBitmap;

    private RectF mRectF;
    private float mRadiusX;
    private float mRadiusY;


    public RoundImageDrawable(Bitmap bitmap, float rx, float ry) {
        mBitmap = bitmap;
        mRadiusX = rx;
        mRadiusY = ry;
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(mRectF,mRadiusX,mRadiusX,mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmap.getHeight();
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmap.getWidth();
    }


    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);

        mRectF = new RectF(left, top, right, bottom);

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
