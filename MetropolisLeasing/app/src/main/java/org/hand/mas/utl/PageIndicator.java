package org.hand.mas.utl;
import android.support.v4.view.ViewPager;
/**
 * Created by gonglixuan on 15/4/14.
 */


public interface PageIndicator {

    /**
     * 指定viewpager绑定上indicator
     * @param viewPager
     */
    void setViewPager(ViewPager viewPager);

    /**
     * 指定viewpager绑定上indicator,并指定初始位置
     * @param viewPager
     */
    void setViewPager(ViewPager viewPager,int initialPosition);

    /**
     * 设置当前位置
     * @param position
     */
    void setCurrentItem(int position);

    /**
     * 设置OnPageChangeListener
     * @param onPageChangeListener
     */
    void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener);

    void notifyDataSetChanged();
}