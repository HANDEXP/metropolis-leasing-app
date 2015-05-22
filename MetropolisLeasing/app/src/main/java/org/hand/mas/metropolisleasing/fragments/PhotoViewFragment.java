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
import android.widget.Toast;

import org.hand.mas.custom_view.CustomProgressBar;
import org.hand.mas.custom_view.OnProgressingListener;
import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.application.MSApplication;
import org.hand.mas.utl.ConstantUrl;
import org.hand.mas.utl.LocalImageLoader;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by gonglixuan on 15/4/28.
 */
public class PhotoViewFragment extends Fragment {

    private String mPath = "foo";
    private PhotoView photoView;
    private CustomProgressBar customProgressBar;
    private boolean isViewShown = false;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (photoView != null){
                LocalImageLoader.getInstance().isSampleForViewPager = false;
                LocalImageLoader.getInstance().setRatios(0.5f, 0.5f);
                LocalImageLoader.getInstance().loadImage(ConstantUrl.basicUrl + mPath, photoView, false);
            }
        }
    };

    private Runnable mThreadStart = new Runnable() {
        @Override
        public void run() {
            customProgressBar.startThread();
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            if (getView() != null){
                isViewShown = true;
//                customProgressBar.startThread();
//                mHandler.post(mThreadStart);
                mHandler.postDelayed(mRunnable, 500);
            }else{
                isViewShown = false;

            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            mPath = getArguments().getString("path");
        }
        View fragment =  inflater.inflate(R.layout.fragment_photoview, container, false);
        photoView = (PhotoView) fragment.findViewById(R.id.photo_view);
        photoView.setImageResource(R.drawable.friends_sends_pictures_no);
        customProgressBar = (CustomProgressBar) fragment.findViewById(R.id.progress_bar);
        customProgressBar.setProgressingListener(new OnProgressingListener() {
            @Override
            public void onStart(View view) {

            }

            @Override
            public void onProgress(View view) {
                ((CustomProgressBar) view).increasedProgress(5);
            }

            @Override
            public void onComplete(View view) {
                LocalImageLoader.getInstance().isSampleForViewPager = false;
                LocalImageLoader.getInstance().setRatios(1.0f, 1.0f);
                LocalImageLoader.getInstance().loadImage(ConstantUrl.basicUrl + mPath, photoView, true);
            }
        });
        if (!isViewShown){
//            customProgressBar.startThread();
//            mHandler.post(mThreadStart);
            mHandler.postDelayed(mRunnable, 500);
        }
        return fragment;
    }

    public void setImageWithDelay(long delayMillis){
        mHandler.post(mThreadStart);

    }

    public void resetImage(){
        if (photoView !=null)
            photoView.setImageResource(R.drawable.friends_sends_pictures_no);
        mHandler.removeCallbacks(mRunnable);
        mHandler.removeCallbacks(mThreadStart);
    }
}
