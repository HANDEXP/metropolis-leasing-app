package org.hand.mas.metropolisleasing.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.utl.ConstantUrl;
import org.hand.mas.utl.LocalImageLoader;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by gonglixuan on 15/4/28.
 */
public class PhotoViewFragment extends Fragment {

    private String mPath = "foo";
    private PhotoView photoView;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (photoView != null){
                LocalImageLoader.getInstance().isSampleForViewPager = false;
                LocalImageLoader.getInstance().setRatios(0.7f, 0.7f);
                LocalImageLoader.getInstance().loadImage(ConstantUrl.basicUrl + mPath, photoView, false);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            mPath = getArguments().getString("path");
        }

        photoView = new PhotoView(container.getContext());
        photoView.setImageResource(R.drawable.friends_sends_pictures_no);


        return photoView;
    }

    public void setImageWithDelay(long delayMillis){
        mHandler.postDelayed(mRunnable,delayMillis);
    }

    public void resetImage(){
        if (photoView !=null)
            photoView.setImageResource(R.drawable.friends_sends_pictures_no);
        mHandler.removeCallbacks(mRunnable);
    }
}
