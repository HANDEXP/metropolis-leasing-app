package org.hand.mas.utl;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * Created by gonglixuan on 15/5/26.
 */
public class ConstantAnim {

    public static void fadeInView(View v, int duration){
        AlphaAnimation animation = new AlphaAnimation(0.0f,1.0f);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        v.setAnimation(animation);
        v.startAnimation(animation);
    }

    public static void fadeInAndTranslateView(final View view){
        ObjectAnimator animator = ObjectAnimator
                .ofFloat(view,"foobar",1.0f,0.0f)
                .setDuration(1500);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currencyValue = (float) animation.getAnimatedValue();
                view.setAlpha(1.0f - currencyValue);
                view.setTranslationY(currencyValue * 300.0f);

            }
        });
    }

    public static void rotateAddItem(View v, float start, float end, int i) {
        RotateAnimation anim = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(i);
        anim.setFillAfter(true);
        v.startAnimation(anim);
    }
}
