package org.hand.mas.custom_view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.hand.mas.metropolisleasing.R;

/**
 * Created by gonglixuan on 15/4/2.
 */
public class ClearEditText extends EditText implements View.OnFocusChangeListener,OnClearIconClickListener {

    private Drawable mDeleteDrawable;
    private Context mContext;
    private OnClickListener mListener;


    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP){
            if (getCompoundDrawables()[2] != null){
                int x = (int) event.getX();
                int y = (int) event.getY();
                Rect rect = getCompoundDrawables()[2].getBounds();
                int TopBound = (getHeight() - rect.height()) / 2;
                int BottomBound = TopBound + rect.height();
                boolean isInnerWidth = x > (getWidth() - getTotalPaddingRight()) && x < (getWidth() - getPaddingRight()) ? true : false;
//                boolean isInnerHeight = y > TopBound && y < BottomBound ? true : false;
                if (isInnerWidth){

                    onClearIconClick();
                }

            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 初始化
     *
     */
    private void init() {
        mDeleteDrawable = mContext.getResources().getDrawable(R.drawable.delete);
        mDeleteDrawable.setBounds(0,0,mDeleteDrawable.getIntrinsicWidth(),mDeleteDrawable.getIntrinsicHeight());

        setClearDrawable(false);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setClearDrawable(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setClearDrawable(boolean isVisible) {
        if (isVisible)
            setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1],mDeleteDrawable,getCompoundDrawables()[3]);
        else
            setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1],null,getCompoundDrawables()[3]);
    }




    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            setClearDrawable(getText().length() > 0);
        }else {
            setClearDrawable(false);
        }
    }

    @Override
    public void onClearIconClick() {
        Toast.makeText(mContext,"Oh! You Touch me!",Toast.LENGTH_SHORT).show();
        if (mListener == null){
            setText("");
        }else {
            mListener.onClick(this);
        }

    }

    @Override
    public void setCustomClearIconClickListener(OnClickListener onClickListener) {
        mListener = onClickListener;
    }


}
