package com.mas.album.adapter;

import java.util.List;

import com.mas.album.items.ImageItem;

import uk.co.senab.photoview.PhotoView;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class ViewPagerAdapter extends PagerAdapter{
	
	
	private List<ImageItem> imageList;
	
	
	
	public ViewPagerAdapter(List<ImageItem> imageList,Context context)
	{
		this.imageList = imageList;
		
	}

	@Override
	public int getCount() {
		return imageList.size();
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		PhotoView photoView = new PhotoView(container.getContext());
		photoView.setImageBitmap(imageList.get(position).getBm());

		// Now just add PhotoView to ViewPager and return it
		container.addView(photoView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		return photoView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
//		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	

}
