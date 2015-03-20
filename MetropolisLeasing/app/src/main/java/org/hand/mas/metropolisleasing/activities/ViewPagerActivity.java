package org.hand.mas.metropolisleasing.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.adapters.ViewPagerAdapter;
import org.hand.mas.metropolisleasing.models.CddGridModel;

import java.util.List;

/**
 * Created by gonglixuan on 15/3/17.
 */
public class ViewPagerActivity extends Activity {

    private ViewPager mViewPager;
    private List<CddGridModel> mCddGridList;
    private ViewPagerAdapter mViewPagerAdapter;
    private TextView mCurrencyPositionTextView;

    private ImageView mCross;


    private int mCurrencyPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_pager);
        bindAllViews();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /* Private Methods */
    private void bindAllViews(){
        mViewPager = (ViewPager) findViewById(R.id.viewPager_for_cdd_item);
        mCurrencyPositionTextView = (TextView) findViewById(R.id.currencyPosition_for_cdd_item);
        mCross = (ImageView) findViewById(R.id.cross_for_cdd_item);

        Intent intent = getIntent();
        mCddGridList = (List<CddGridModel>) intent.getSerializableExtra("cddGridList");
        mCurrencyPosition = intent.getIntExtra("position",0);

        mCurrencyPositionTextView.setText(String.valueOf(mCurrencyPosition+1).concat("/").concat(String.valueOf(mCddGridList.size())));
        mViewPagerAdapter = new ViewPagerAdapter(getApplicationContext(),mCddGridList);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(mCurrencyPosition);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrencyPositionTextView.setText(String.valueOf(position+1).concat("/").concat(String.valueOf(mCddGridList.size())));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
