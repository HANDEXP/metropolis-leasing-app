package org.hand.mas.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nineoldandroids.view.ViewHelper;

import org.hand.mas.metropolisleasing.R;

import java.util.List;

/**
 * Created by gonglixuan on 15/3/23.
 */
public class SlidingMenu extends HorizontalScrollView{

    private LinearLayout mWrapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;

    private ImageView mIcon;

    private int mScreenWidth;
    private int mMenuWidth;
    private int mMenuRightPadding = 50;

    private boolean isOpen = false;
    private boolean once;
    /**
     * 触点坐标
     */
    private float x1,x2,y1,y2;

    private GestureDetector mGestureDetector;

    public SlidingMenu(Context context) {
        this(context,null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingMenu,defStyleAttr,0);
        int count = typedArray.getIndexCount();
        for (int i =0;i < count;i++){
            int attr = typedArray.getIndex(i);
            switch (attr){
                case R.styleable.SlidingMenu_rightPadding:
                    mMenuRightPadding = typedArray.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,50,context
                                        .getResources().getDisplayMetrics()
                            ));
                    break;
            }
        }
        typedArray.recycle();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed){
            this.scrollTo(mMenuWidth,0);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!once){
            mWrapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWrapper.getChildAt(0);
            mContent = (ViewGroup) mWrapper.getChildAt(1);
            mIcon = (ImageView) mContent.findViewById(R.id.slide_menu);

            mMenuWidth = mScreenWidth - mMenuRightPadding;
            mMenu.getLayoutParams().width = mMenuWidth;
            mContent.getLayoutParams().width = mScreenWidth;
            once = true;

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
//        return false;
        if (ev.getAction() == MotionEvent.ACTION_UP){
            x1 = -1;
            x2 = -1;
            y1 = -1;
            y2 = -1;
            int scrollX = getScrollX();
            Log.d("Tag","scrollX:"+String.valueOf(scrollX)+" mMenuWidth:"+String.valueOf(mMenuWidth));
            if (scrollX >= mMenuWidth / 2){
                this.smoothScrollTo(mMenuWidth, 0);
                isOpen = false;
            }else {
                this.smoothScrollTo(0, 0);
                isOpen = true;
            }
            getIsOpen();
        }else if (ev.getAction() == MotionEvent.ACTION_MOVE){
            if (x1 == -1){
                x1 = ev.getX();
            }else{
                x2 = ev.getX();
            }
            if (y1 == -1){
                y1 = ev.getY();
            }else{
                y2 = ev.getY();
            }

        }
        if (x1 != -1 && x2 != -1 && y1 != -1 && y2 != -1){
            float deltaX = Math.abs(x2 - x1);
            float deltaY = Math.abs(y2 - y1);
//            Log.d("delta","deltaX:"+String.valueOf(deltaX)+" deltaY:"+String.valueOf(deltaY));
            if (deltaX > deltaY){
                return super.onTouchEvent(ev);
            }
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        float scale = l * 1.0f / mMenuWidth;
        float rightScale = 0.7f + 0.3f * scale;
        float leftScale = 1.0f -scale * 0.3f;
        float leftAlpha = 0.6f + 0.4f * (1 - scale);

        // 调用属性动画，设置TranslationX
        ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.8f);

        ViewHelper.setScaleX(mMenu, leftScale);
        ViewHelper.setScaleY(mMenu, leftScale);
        ViewHelper.setAlpha(mMenu, leftAlpha);


    }
    /**
     * 打开菜单
     */
    public void openMenu()
    {
        if (isOpen)
            return;
        this.smoothScrollTo(0, 0);
        isOpen = true;

    }

    public void closeMenu()
    {
        if (!isOpen)
            return;
        this.smoothScrollTo(mMenuWidth, 0);
        isOpen = false;
    }

    /**
     * 切换菜单
     */
    public void toggle()
    {
        if (isOpen)
        {
            closeMenu();
        } else
        {
            openMenu();
        }
        getIsOpen();
    }

    /**
     * 返回菜单状态，顺便改图标
     * @return
     */
    public boolean getIsOpen(){
        if (this.isOpen){
            mIcon.setImageDrawable(getResources().getDrawable(R.drawable.cross));
        }else {
            mIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_for_slide_menu));
        }
        return this.isOpen;
    }

}
