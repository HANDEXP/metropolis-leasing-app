package org.hand.mas.metropolisleasing.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.utl.LocalImageLoader;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by gonglixuan on 15/3/27.
 */
public class AlbumViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private String mImgDir;
    private List<String> mList;
    private Bitmap bitmap;
    private PhotoViewAttacher.OnPhotoTapListener mOnPhotoTapListener;

    public AlbumViewPagerAdapter(Context mContext, String mImgDir, List<String> mImgs, PhotoViewAttacher.OnPhotoTapListener mOnPhotoTapListener) {
        this.mContext = mContext;
        this.mImgDir = mImgDir;
        this.mList = mImgs;
        this.mOnPhotoTapListener = mOnPhotoTapListener;

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        photoView.setTag(R.id.className,"PhotoView");
        initEvent(photoView);
        String filePath = mImgDir + "/" + mList.get(position);
        bitmap = LocalImageLoader.getInstance().decodeSizedBitmapFromResource(filePath);
        photoView.setImageBitmap(bitmap);
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;
    }

    /**
     * 绑定事件
     * @param photoView
     */
    private void initEvent(PhotoView photoView) {
        photoView.setOnPhotoTapListener(mOnPhotoTapListener);
    }
}
