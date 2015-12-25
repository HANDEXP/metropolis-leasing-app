package com.mas.album.adapter;

import java.util.List;

import com.mas.album.R;
import com.mas.album.items.ImageItem;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageGridAdapter extends BaseAdapter{

	private List<ImageItem> imageList;
	
	private Context context;
	
	public ImageGridAdapter(List<ImageItem> imageList,Context context)
	{
		this.imageList = imageList;
		this.context = context;
		
	}
	
	@Override
	public int getCount() {

		return imageList.size();
	}

	@Override
	public Object getItem(int position) {

		return imageList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{

		if(convertView == null)
		{
			convertView = (View) LayoutInflater.from(context).inflate(
					R.layout.image_gird_item, null);
			
		}
		
		ImageView mImageView = 	(ImageView) convertView.findViewById(R.id.mImageView);		
		
		mImageView.setImageBitmap(imageList.get(position).getBm());
		
		
		return convertView;
	}

	
	
}
