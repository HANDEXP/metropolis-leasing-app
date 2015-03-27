package org.hand.mas.metropolisleasing.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.adapters.AlbumViewPagerAdapter;

import java.io.File;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by gonglixuan on 15/3/27.
 */
public class AlbumViewPagerActivity extends Activity {

    private ViewPager mViewPager;

    private AlbumViewPagerAdapter mAdapter;

    /**
     * 当前文件夹
     */
    private String mImgDirPath;
    /**
     * 文件夹下的图片
     */
    private List<String> mImgs;
    private int mCurrencyPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_view_pager);
        bindAllViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void bindAllViews(){
        mViewPager = (ViewPager) findViewById(R.id.viewPager_for_album_item);
        generateParam();
        mAdapter = new AlbumViewPagerAdapter(getApplicationContext(),mImgDirPath,mImgs,new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                Toast.makeText(getApplicationContext(),"TEST",Toast.LENGTH_SHORT).show();
            }
        });


        mViewPager.setAdapter(mAdapter);

    }

    private void generateParam(){
        Intent intent = getIntent();
        mCurrencyPosition = intent.getIntExtra("position",0);
        mImgDirPath = intent.getStringExtra("mImgDirPath");
        mImgs = (List<String>) intent.getSerializableExtra("mImgs");
    }
}
