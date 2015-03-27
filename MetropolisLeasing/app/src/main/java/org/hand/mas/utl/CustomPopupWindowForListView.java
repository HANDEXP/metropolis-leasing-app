package org.hand.mas.utl;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

import java.util.List;

/**
 * Created by gonglixuan on 15/3/26.
 */
public abstract class CustomPopupWindowForListView<T> extends PopupWindow {
    /**
     * 布局文件的最外层View
     *
     */
    protected View mContentView;
    protected Context mContext;

    protected List<T> mList;

    public CustomPopupWindowForListView(View contentView, int width, int height, boolean focusable, List<T> mList) {
        this(contentView,width,height,focusable,mList,new Object[0]);

    }

    public CustomPopupWindowForListView(View contentView, int width, int height, boolean focusable, List<T> mList, Object... params) {
        super(contentView,width,height,focusable);
        this.mContentView = contentView;
        this.mContext = contentView.getContext();
        if (mList != null){
            this.mList = mList;
        }
        if (params != null && params.length > 0){
            beforeInitWeNeedSomeParams(params);
        }

        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        setOutsideTouchable(true);
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE){
                    dismiss();
                    return true;
                }

                return false;
            }
        });
        initViews();
        initEvents();
        init();
    }



    protected abstract void beforeInitWeNeedSomeParams(Object... params);

    public abstract void initViews();

    public abstract void initEvents();

    public abstract void init();

    public View findViewById(int id)
    {
        return mContentView.findViewById(id);
    }

    protected static int dpToPx(Context context, int dp)
    {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }
}
