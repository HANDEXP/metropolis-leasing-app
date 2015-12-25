package com.mas.customview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

import com.littlemvc.R;



import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageViewPager extends LinearLayout{
	
	private ViewPager viewPager;
	private View rootView;
	private LinearLayout l;
	private List<ImageView> imageViews; // 滑动的图片集合 
	
	//当前图片的位置
	private int currentPosition;
	
	//滚动时间 单位秒
	public int  scrollSecond;
	
	private ScheduledExecutorService scheduledExecutorService;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(currentPosition);// 切换当前显示的图片
		};
	};
	
	
	
    public ImageViewPager(Context context) {
        super(context);
    }

    public ImageViewPager(Context context, AttributeSet attrs) 
    {
    	super(context,attrs);
        

    	
    	rootView = LayoutInflater.from(context).inflate(R.layout.customview_imageviewpager, this, true);
    	 	
    	
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        
    	viewPager = (ViewPager) rootView.findViewById(R.id.vp);
    	
    	l =  (LinearLayout) rootView.findViewById(R.id.dot_layout);
    	
    	imageViews = new ArrayList<ImageView>();
    	
    	//默认滚动时间为2秒
    	scrollSecond = 1;
    }

/**
 * 
 * @param 设置展示用的图片
 */
    
    public void setDrawables(int [] drawables)
    {
    	for(int i =0;i<drawables.length;i++){
    		ImageView img = new ImageView(this.getContext());
    		img.setImageResource(drawables[i]);
    		img.setScaleType(ScaleType.FIT_XY);
    		
    		
    		imageViews.add(img);

    	}
  
    	viewPager.setAdapter(new ViewPagerAdapter() );
    	viewPager.setOnPageChangeListener(new MyPageChangeListener());
    	
    	imageDisplayStart();
    	//设置完后自动滚动
    	
    }
    
 
/**
 * 开始滚动图片
 */
    public void imageDisplayStart()
    {
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 2, scrollSecond, TimeUnit.SECONDS);
    	
    }
 
/**
 * 停止滚动图片    
 */
    
    public void imageDisplayShutdown()
    {
    	scheduledExecutorService.shutdown();
    	
    }
    
/**
 * 
 * @author yun
 * 设置时间
 */
  public void setScrollSecond( int _scrollSecond)
  {
	  scrollSecond  = _scrollSecond;
	  
  }
    
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				currentPosition = (currentPosition + 1);
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}

	}

    private class ViewPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
    	
		@Override
		public Object instantiateItem(View arg0, int arg1) {

			try{
				((ViewPager) arg0).addView(imageViews.get(arg1 % imageViews.size()));
			}catch (Exception e){
				
			}
			

			return imageViews.get(arg1 % imageViews.size());
		}
    	
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
//			((ViewPager) arg0).removeView((View) arg2);
		}
    }
    
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
		
			currentPosition =  position;
			l.getChildAt(oldPosition  % imageViews.size()).setBackgroundResource(R.drawable.dot_normal);
			l.getChildAt(position  % imageViews.size()).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

}
