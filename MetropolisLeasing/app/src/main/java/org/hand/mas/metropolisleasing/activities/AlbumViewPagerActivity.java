package org.hand.mas.metropolisleasing.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.adapters.AlbumViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by gonglixuan on 15/3/27.
 */
public class AlbumViewPagerActivity extends Activity {

    private ViewPager mViewPager;
    private RelativeLayout mTopBarRL;
    private RelativeLayout mBottomBarRL;
    private ImageButton mIsSelectedImageButton;
    private ImageView mExitImageView;
    private TextView mFinishTextView;
    private TextView mCountTextView;

    private AlbumViewPagerAdapter mAdapter;

    /**
     * 当前文件夹
     */
    private String mImgDirPath;
    /**
     * 文件夹下的图片
     */
    private List<String> mImgs;
    private List<Object> mSelectedList;
    private int mCurrencyPosition;

    private final int RESULT_OK = 0;
    private final int RESULT_CANCEL = -1;

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
        finishViewPagerWithResultCode(RESULT_CANCEL);
    }

    private void bindAllViews(){
        mViewPager = (ViewPager) findViewById(R.id.viewPager_for_album_item);
        mTopBarRL = (RelativeLayout) findViewById(R.id.top_bar_for_album_pager);
        mBottomBarRL = (RelativeLayout) findViewById(R.id.bottom_bar_for_album_pager);
        mIsSelectedImageButton = (ImageButton) findViewById(R.id.is_selected_imagebutton);
        mExitImageView = (ImageView) findViewById(R.id.exit_album_view_pager_imageview);
        mFinishTextView = (TextView) findViewById(R.id.finish_for_viewpager_textview);
        mCountTextView = (TextView) findViewById(R.id.count_for_list_in_viewpager);

        generateParam();
        mAdapter = new AlbumViewPagerAdapter(getApplicationContext(),mImgDirPath,mImgs,new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (y < 0.2 || y > 0.85){
                    return;
                }
                if (mTopBarRL.getVisibility() == View.VISIBLE){
                    mTopBarRL.setVisibility(View.INVISIBLE);
                    mBottomBarRL.setVisibility(View.INVISIBLE);
                }else{
                    mTopBarRL.setVisibility(View.VISIBLE);
                    mBottomBarRL.setVisibility(View.VISIBLE);
                }
            }
        });


        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrencyPosition, true);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrencyPosition = position;
                updateUI(mCurrencyPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mIsSelectedImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected(mSelectedList, mCurrencyPosition)) {
                    mSelectedList.remove((Object) mCurrencyPosition);
                } else {
                    mSelectedList.add((Object) mCurrencyPosition);
                }
                updateUI(mCurrencyPosition);
            }
        });
        mExitImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishViewPagerWithResultCode(RESULT_CANCEL);
            }
        });
        mFinishTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedList.isEmpty()){
                    mSelectedList.add((Object) mCurrencyPosition);
                }

                finishViewPagerWithResultCode(RESULT_OK);
            }
        });
        updateUI(mCurrencyPosition);

    }

    private void generateParam(){
        Intent intent = getIntent();
        mCurrencyPosition = intent.getIntExtra("position",0);
        mImgDirPath = intent.getStringExtra("mImgDirPath");
        mImgs = (List<String>) intent.getSerializableExtra("mImgs");
        mSelectedList = (List<Object>) intent.getSerializableExtra("mSelectedList");
    }

    /**
     * 更新UI
     * @param position
     */
    private void updateUI(int position){
        if (isSelected(mSelectedList,position)){
            mIsSelectedImageButton.setImageResource(R.drawable.icon_for_pic_selected);
        }else{
            mIsSelectedImageButton.setImageResource(R.drawable.icon_for_pic_unselected);
        }
        mCountTextView.setText(String.valueOf(mSelectedList.size()));
        if (mSelectedList.isEmpty()){
            mCountTextView.setVisibility(View.INVISIBLE);
        }else {
            mCountTextView.setVisibility(View.VISIBLE);
        }
    }

    private boolean isSelected(List<Object> list,int position){
        if (list.contains((Object)position)){
            return true;
        }
        return false;
    }

    private void finishViewPagerWithResultCode(int resultCode){
        Intent intent = new Intent();
        /* View_Pager_All 传回 mSelectedList */
        /* 若是View_pager_Selected 则传回 selectedList */
        /* 需在AlbumViewActivity 中讨论 */
        intent.putExtra("mSelectedList", (java.io.Serializable) mSelectedList);
        if (resultCode == RESULT_CANCEL){
            List<String> uploadList = generateUploadList();
            intent.putExtra("mUploadList", (java.io.Serializable) uploadList);
        }
        setResult(resultCode, intent);
        finish();
    }

    private List<String> generateUploadList(){
        List<String> uploadList = new ArrayList<String>();
        String filePath;
        int length = mSelectedList.size();
        for (int i = 0;i < length;i++){
            filePath = mImgDirPath+"/"+mImgs.get((Integer)mSelectedList.get(i));
            uploadList.add(filePath);
        }
        return uploadList;
    }


}
