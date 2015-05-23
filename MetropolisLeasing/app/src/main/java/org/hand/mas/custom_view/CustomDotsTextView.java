package org.hand.mas.custom_view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

import org.hand.mas.metropolisleasing.R;

/**
 * Created by gonglixuan on 15/5/22.
 */
public class CustomDotsTextView extends TextView {

    private JumpingSpan dotOne;
    private JumpingSpan dotTwo;
    private JumpingSpan dotThree;

    private int mDotsColor;
    private int mJumpHeight;
    private int mPeriod;
    private boolean mAutoPlay;

    /* 总长度 */
    private float textWidth;
    private AnimatorSet mAnimatorSet = new AnimatorSet();

    private boolean isPlaying;

    public CustomDotsTextView(Context context) {
        this(context, null);
    }

    public CustomDotsTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomDotsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomDotsTextView,defStyleAttr,0);
        int count = typedArray.getIndexCount();
        for(int i = 0; i < count; i++){
            int attr = typedArray.getIndex(i);
            switch (attr){
                case R.styleable.CustomDotsTextView_dotsColor:
                    break;
                case R.styleable.CustomDotsTextView_jumpHeight:
                    mJumpHeight = typedArray.getInt(R.styleable.CustomDotsTextView_jumpHeight, (int) (getTextSize() / 4));
                    break;
                case R.styleable.CustomDotsTextView_period:
                    mPeriod = typedArray.getInt(R.styleable.CustomDotsTextView_period,6000);
                    break;
                case R.styleable.CustomDotsTextView_autoPlay:
                    mAutoPlay = typedArray.getBoolean(R.styleable.CustomDotsTextView_autoPlay,true);
                    break;
            }
        }
        dotOne = new JumpingSpan();
        dotTwo = new JumpingSpan();
        dotThree = new JumpingSpan();

        SpannableString spannableString = new SpannableString("...");
        spannableString.setSpan(dotOne,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(dotTwo,1,2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(dotThree, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(spannableString, BufferType.SPANNABLE);

        textWidth = getPaint().measureText(".",0,1);

        ObjectAnimator dotOneJumpAnimator = createDotJumpAnimator(dotOne,0);
        dotOneJumpAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        mAnimatorSet.playTogether(dotOneJumpAnimator, createDotJumpAnimator(dotTwo, mPeriod / 6), createDotJumpAnimator(dotThree, mPeriod / 3));
        start();
    }

    private void setAllAnimationsRepeatCount(int repeatCount){
        for (Animator animator : mAnimatorSet.getChildAnimations()){
            if (animator instanceof ObjectAnimator){
                ((ObjectAnimator)animator).setRepeatCount(repeatCount);
            }
        }
    }

    private ObjectAnimator createDotJumpAnimator(JumpingSpan jumpingSpan,long delay){

        ObjectAnimator jumpAnimator = ObjectAnimator.ofFloat(jumpingSpan, "translationY", 0 ,-mJumpHeight);
        jumpAnimator.setEvaluator(new TypeEvaluator<Number>() {
            @Override
            public Number evaluate(float fraction, Number startValue, Number endValue) {
                return Math.max(0,Math.sin(fraction * Math.PI * 2)) * ( endValue.floatValue() - startValue.floatValue());
            }
        });
        jumpAnimator.setDuration(mPeriod);
        jumpAnimator.setStartDelay(delay);
        jumpAnimator.setRepeatCount(ValueAnimator.INFINITE);
        jumpAnimator.setRepeatMode(ValueAnimator.RESTART);
        return jumpAnimator;
    }

    public void start() {
        isPlaying = true;
        setAllAnimationsRepeatCount(ValueAnimator.INFINITE);
        mAnimatorSet.start();
    }

    public void stop() {
        isPlaying = false;
        setAllAnimationsRepeatCount(0);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPeriod(int mPeriod) {
        this.mPeriod = mPeriod;
    }

    public void setJumpHeight(int mJumpHeight) {
        this.mJumpHeight = mJumpHeight;
    }
}
