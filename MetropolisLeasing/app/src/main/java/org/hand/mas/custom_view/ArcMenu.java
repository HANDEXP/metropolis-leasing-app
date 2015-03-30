package org.hand.mas.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

import org.hand.mas.metropolisleasing.R;

/**
 * Created by gonglixuan on 15/3/22.
 */
public class ArcMenu extends ViewGroup implements View.OnClickListener{

    private static final int POS_LEFT_TOP = 0;
    private static final int POS_LEFT_BOTTOM = 1;
    private static final int POS_RIGHT_TOP = 2;
    private static final int POS_RIGHT_BOTTOM = 3;

    private Position mPosition = Position.LEFT_BOTTOM;
    private int mRadius;
    private Status mCurrentStatus = Status.CLOSE;
    /**
     * 主按钮
     */
    private View mMainButton;

    private OnMenuItemClickListener mMenuItemClickListener;


    private enum Position{
        LEFT_TOP,LEFT_BOTTOM,RIGHT_TOP,RIGHT_BOTTOM;
    }

    private enum Status{
        CLOSE,OPEN;
    }

    public interface OnMenuItemClickListener{
        void onClick(View view,int pos);
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener mMenuItemClickListener){

        this.mMenuItemClickListener = mMenuItemClickListener;

    }

    public ArcMenu(Context context) {
        this(context,null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        // 取自定义属性
        TypedArray typedValue = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenu, defStyleAttr, 0);

        int pos = typedValue.getInt(R.styleable.ArcMenu_position,POS_LEFT_BOTTOM);
        switch (pos){
            case POS_LEFT_TOP:
                mPosition = Position.LEFT_TOP;
                break;
            case POS_LEFT_BOTTOM:
                mPosition = Position.LEFT_BOTTOM;
                break;
            case POS_RIGHT_TOP:
                mPosition = Position.RIGHT_TOP;
                break;
            case POS_RIGHT_BOTTOM:
                mPosition = Position.RIGHT_BOTTOM;
                break;
        }

        mRadius = (int) typedValue.getDimension(R.styleable.ArcMenu_radius,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,getResources().getDisplayMetrics()));
        Log.d("TAG", "position = " + mPosition + " , radius =  " + mRadius);

        typedValue.recycle();
    }


    @Override
    public void onClick(View v) {

        rotateMainButton(v,0.0f,360.0f,300);
        toggleMenu(300);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int count = getChildCount();
        for(int i = 0; i < count; i++){
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed){
            layoutMainButton();
            int count = getChildCount();
            Log.d("TAG", "count = " + count);
            for(int i = 1; i < count;i++){
                View childView = getChildAt(i);
                childView.setVisibility(View.GONE);
                int left4child = (int) (mRadius*Math.sin(Math.PI/2/(count-2)*(i-1)));
                int top4child = (int) (mRadius*Math.cos(Math.PI / 2 / (count - 2) * (i - 1)));

                int width4child = childView.getMeasuredWidth();
                int height4child = childView.getMeasuredHeight();

                layoutChildView(childView, left4child, top4child, width4child, height4child);

            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    private void layoutMainButton() {
        mMainButton = getChildAt(0);
        mMainButton.setOnClickListener(this);

        int left = 0,top = 0;
        int width = mMainButton.getMeasuredWidth();
        int height = mMainButton.getMeasuredHeight();

        switch (mPosition){
            case LEFT_TOP:
                left = 0;
                top = 0;
                break;
            case LEFT_BOTTOM:
                left = 0;
                top = getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                left = getMeasuredWidth() - width;
                top = 0;
                break;
            case RIGHT_BOTTOM:
                left = getMeasuredWidth() - width;
                top = getMeasuredHeight() - height;
                break;
        }
        mMainButton.layout(left,top,left+width,top+height);
    }

    private void layoutChildView(View v,int left4child,int top4child,int width4child,int height4child) {
        if (mPosition == Position.LEFT_BOTTOM || mPosition == Position.RIGHT_BOTTOM){
            top4child = getMeasuredHeight() - height4child - top4child;
        }else if(mPosition == Position.LEFT_BOTTOM || mPosition == Position.RIGHT_TOP){
            left4child = getMeasuredWidth() - width4child - left4child;
        }
        v.layout(left4child,top4child,left4child+width4child,top4child+height4child);
    }

    private void rotateMainButton(View v, float start, float end, int i) {
        RotateAnimation anim = new RotateAnimation(start,end, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        anim.setDuration(i);
        anim.setFillAfter(true);
        v.startAnimation(anim);

    }


    /**
     * 打开／关闭菜单
     * @param interval
     */
    private void toggleMenu(int interval) {

        int count = getChildCount();

        for (int i = 1; i < count; i++){

            final View childView = getChildAt(i);
            childView.setVisibility(VISIBLE);

            int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * (i-1)));
            int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * (i - 1)));
            int xflag = 1;
            int yflag = 1;

            if (mPosition == Position.LEFT_TOP
                    || mPosition == Position.LEFT_BOTTOM)
            {
                xflag = -1;
            }

            if (mPosition == Position.LEFT_TOP
                    || mPosition == Position.RIGHT_TOP)
            {
                yflag = -1;
            }

            AnimationSet animationSet = new AnimationSet(true);
            Animation tranAnimation = null;

            if (mCurrentStatus == Status.CLOSE){
                tranAnimation = new TranslateAnimation(xflag * cl,0,yflag * ct,0);
                childView.setClickable(true);
                childView.setFocusable(true);
            }else {
                tranAnimation = new TranslateAnimation(0,xflag * cl,0,yflag * ct);
                childView.setClickable(false);
                childView.setFocusable(false);
            }

            //子View延迟展开
            tranAnimation.setFillAfter(true);
            tranAnimation.setDuration(interval);
            tranAnimation.setStartOffset(((i-1) * 100) / count);
            tranAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mCurrentStatus == Status.CLOSE){
                        childView.setVisibility(GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            // 旋转动画
            RotateAnimation rotateAnimation = new RotateAnimation(0,720,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            rotateAnimation.setDuration(interval);
            rotateAnimation.setFillAfter(true);

            animationSet.addAnimation(rotateAnimation);
            animationSet.addAnimation(tranAnimation);

            childView.startAnimation(animationSet);

            final int pos = i;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
        changeStatus();
    }

    /**
     * 改变状态
     */
    private void changeStatus() {
        mCurrentStatus = mCurrentStatus == Status.CLOSE ? Status.OPEN : Status.CLOSE;
    }

    public boolean isOpen()
    {
        return mCurrentStatus == Status.OPEN;
    }

}
