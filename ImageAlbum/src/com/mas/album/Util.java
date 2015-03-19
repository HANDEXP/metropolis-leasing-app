package com.mas.album;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Util {
	public static Bitmap CompressBytes(byte[] content)
	{
		int length = content.length;
		
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inJustDecodeBounds = false;
		
		if(length < 300000){
			option.inSampleSize = 4;
		}else{
			option.inSampleSize = 8;
		}
		return BitmapFactory
				.decodeByteArray(content, 0, content.length, option);
	}

	/**
	 * 
	 * 通过指定最大大小，将byte[]压缩
	 */
	public static byte[] CompressBytes(byte[] content, int max_size) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		Bitmap bm;
		byte[] compressBytes = null;

		int rate = 100;

		int scale = (int) Math.floor(content.length / max_size);

		BitmapFactory.Options option = new BitmapFactory.Options();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		option.inJustDecodeBounds = false;
		option.inSampleSize = 1;

		if (scale < 2) {
			option.inSampleSize = 1;
		} else if (scale >= 2 && scale < 8) {
			option.inSampleSize = 2;
		} else if (scale >= 8 && scale < 32) {
			option.inSampleSize = 4;
		} else if (scale >= 32 && scale < 128) {
			option.inSampleSize = 8;
		} else {
			option.inSampleSize = 16;
		}

		try {
			bm = BitmapFactory.decodeByteArray(content, 0, content.length,
					option);
		} catch (OutOfMemoryError e) {
			// 增加一个压缩级别
			option.inSampleSize *= 2;
			bm = BitmapFactory.decodeByteArray(content, 0, content.length,
					option);
		}

		do {
			if (compressBytes == null) {

				bm.compress(Bitmap.CompressFormat.JPEG, rate, baos);

				compressBytes = baos.toByteArray();
			} else {
				rate = rate - 3;
				baos.reset();
				compressBytes = null;
				System.gc();
				bm.compress(Bitmap.CompressFormat.JPEG, rate, baos);
				compressBytes = baos.toByteArray();

			}

		} while (compressBytes.length > max_size);

		bm = null;

		System.gc();

		return compressBytes;
	}

	public static byte[] BitmapTobytes(Bitmap bm, int rate) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, rate, baos);
		return baos.toByteArray();
	}

	/**
	 * stream 转换 byte数组
	 */

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}

}
