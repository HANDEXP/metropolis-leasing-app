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

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by gonglixuan on 15/3/17.
 */
public class ViewPagerActivity extends Activity {

    private ViewPager mViewPager;
    private List<CddGridModel> mCddGridList;
    private ViewPagerAdapter mViewPagerAdapter;
    private TextView mCurrencyPositionTextView;

    private ImageView mCrossImageView;
    private ImageView mTrashImageView;


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
    private void bindAllViews() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager_for_cdd_item);
        mCurrencyPositionTextView = (TextView) findViewById(R.id.currencyPosition_for_cdd_item);
        mCrossImageView = (ImageView) findViewById(R.id.cross_for_cdd_item);
        mTrashImageView = (ImageView) findViewById(R.id.trash_for_cdd_item);

        Intent intent = getIntent();
        mCddGridList = (List<CddGridModel>) intent.getSerializableExtra("cddGridList");
        mCurrencyPosition = intent.getIntExtra("position", 0);

        mCurrencyPositionTextView.setText(String.valueOf(mCurrencyPosition + 1).concat("/").concat(String.valueOf(mCddGridList.size())));
        mViewPagerAdapter = new ViewPagerAdapter(getApplicationContext(), mCddGridList);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(mCurrencyPosition);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrencyPosition = position;
                mCurrencyPositionTextView.setText(String.valueOf(position + 1).concat("/").concat(String.valueOf(mCddGridList.size())));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mCrossImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTrashImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(ViewPagerActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定要删除影像资料？")
                        .setConfirmText("确定")
                        .setCancelText("取消")
                        .setContentText("删除之后无法恢复")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
                                sDialog.setTitleText("已删除!")
                                        .setContentText("该影像资料已经删除")
                                        .setConfirmText("确定")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                Intent intent = new Intent();
                                                intent.putExtra("currencyPosition",mCurrencyPosition);
                                                ViewPagerActivity.this.setResult(RESULT_OK,intent);
                                                sDialog.dismiss();
                                                finish();
                                            }
                                        })
                                        .showCancelButton(false)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        }).show();
            }
        });
    }

}
