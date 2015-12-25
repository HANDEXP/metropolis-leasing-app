package com.mas.album.items;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.util.LruCache;

public class ImageItem {

	static {

//		mMemoryCache = new LruCache<String, ArrayList<String>>(10000000);
		mMemoryCache = new HashMap<String, ArrayList<ImageItem>>();
	}

//	static public LruCache mMemoryCache;
	
	static public HashMap<String, ArrayList<ImageItem>> mMemoryCache;

	public byte[] content;

	public Bitmap bm;
	


	public ImageItem(byte[] content, Bitmap bm) {
		this.content = content;
		this.bm = bm;
	}
	

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public Bitmap getBm() {
		return bm;
	}

	public void setBm(Bitmap bm) {
		this.bm = bm;
	}

}
