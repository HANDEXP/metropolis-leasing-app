package com.mas.album;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mas.album.items.ImageItem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;

public class main extends Activity  {

	private ArrayList<ImageItem> imageList = new ArrayList<ImageItem>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.hello).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(main.this,AlbumView.class);

				startActivityForResult(intent, 1);
			}
		});
		
		prepare();
		
		Intent intent = new Intent(this,AlbumView.class);
		
		ImageItem.mMemoryCache.put("imageList", imageList);
		
//		intent.putParcelableArrayListExtra("imageList", imageList);
//		intent.putExtra("my",(Serializable)imageList );
//		Bundle bundle = new Bundle();
//		bundle("imageList",(Serializable)imageList );
//		intent.putExtras(bundle);
//		intent.putExtra("imageList", (Parcelable)imageList);		
//		startActivity(intent);
		startActivityForResult(intent, 1);
		
		
	}
	
	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data == null){
			
			return ;			
		}
		
		switch (requestCode) {
		case 1:

			
			break;

		default:
			break;
		}
		
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	
	private void prepare()
	{
//		Bitmap bit1 = BitmapFactory.decodeResource(getResources(),
//				R.drawable.wallpaper);
//		
//		ImageItem item1 = new ImageItem(Util.BitmapTobytes(bit1, 10));
//				
//		Bitmap bit2 = BitmapFactory.decodeResource(getResources(),
//				R.drawable.ic_launcher);
//		
//		ImageItem item2 = new ImageItem(Util.BitmapTobytes(bit2, 10));
		

		//写错了导致程序直接闪退，且不报错
//		imageList.add(item1);
//		imageList.add(item2);
		
//		
	}



	
}
