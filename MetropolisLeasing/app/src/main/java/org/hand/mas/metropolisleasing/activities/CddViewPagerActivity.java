package org.hand.mas.metropolisleasing.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;

import org.hand.mas.custom_view.CustomCirclePageIndicator;
import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.adapters.CddViewPagerAdapter;
import org.hand.mas.metropolisleasing.models.CddGridModel;
import org.hand.mas.metropolisleasing.models.DeleteAttachmentSvcModel;
import org.hand.mas.utl.DepthPageTransformer;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by gonglixuan on 15/3/17.
 */
public class CddViewPagerActivity extends Activity implements LMModelDelegate{

    private ViewPager mViewPager;
    private List<CddGridModel> mCddGridList;
    private TextView mCurrencyPositionTextView;
    private ImageView mCrossImageView;
    private ImageView mTrashImageView;
    private SweetAlertDialog mDialogPlus;

    private CddViewPagerAdapter mViewPagerAdapter;

    private DeleteAttachmentSvcModel mDeleteModel;

    private HashMap<String,String> param;

    private int mCurrencyPosition;
    private String mCurrencyAttachmentId;
    private boolean mCurrencyIsRemote;

    private CustomCirclePageIndicator mIndicator;

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

    @Override
    public void modelDidFinishLoad(LMModel model) {
        AsHttpRequestModel reponseModel = (AsHttpRequestModel) model;
        String json = new String(reponseModel.mresponseBody);
        try {
            JSONObject jsonObj = new JSONObject(json);
            String code = ((JSONObject)jsonObj.get("head")).get("code").toString();
            if (code.equals("success")){
                if (mDialogPlus != null){
                    mDialogPlus.setTitleText("已删除!")
                            .setContentText("该影像资料已经删除")
                            .setConfirmText("确定")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    finish();
                                }
                            })
                            .showCancelButton(false)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                }
            }
        }catch (Exception e){
            if (mDialogPlus.isShowing()){
                mDialogPlus.dismiss();
            }
        }
    }

    @Override
    public void modelDidStartLoad(LMModel model) {

    }

    @Override
    public void modelDidFailedLoadWithError(LMModel model) {
        if (mDialogPlus.isShowing()){
            mDialogPlus.dismiss();
        }
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
        mCurrencyAttachmentId = intent.getStringExtra("attachmentId");
        mCurrencyIsRemote = intent.getBooleanExtra("isRemote",false);

        mCurrencyPositionTextView.setText(String.valueOf(mCurrencyPosition + 1).concat("/").concat(String.valueOf(mCddGridList.size())));
        mViewPagerAdapter = new CddViewPagerAdapter(getApplicationContext(), mCddGridList);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(mCurrencyPosition);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        mIndicator = (CustomCirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                CddGridModel item = mCddGridList.get(position);
                mCurrencyPosition = position;
                mCurrencyAttachmentId = item.getAttachmentId();
                mCurrencyIsRemote = item.getRemote();

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
                mDialogPlus = new SweetAlertDialog(CddViewPagerActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定要删除影像资料？")
                        .setConfirmText("确定")
                        .setCancelText("取消")
                        .setContentText("删除之后无法恢复")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
                                Intent intent = new Intent();
                                intent.putExtra("currencyPosition",mCurrencyPosition);
                                CddViewPagerActivity.this.setResult(RESULT_OK,intent);

                                if (!mCurrencyIsRemote){
                                    sDialog.setTitleText("已删除!")
                                            .setContentText("该影像资料已经删除")
                                            .setConfirmText("确定")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sDialog.dismiss();
                                                    finish();
                                                }
                                            })
                                            .showCancelButton(false)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                }else{
                                    sDialog.setTitleText("请稍后")
                                            .setContentText("正在删除服务器端附件")
                                            .showCancelButton(false)
                                            .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                                    deleteFileFromServer();

                                }
                            }
                        });
                mDialogPlus.show();
            }
        });
    }

    /* 删除远程附件 */
    private void deleteFileFromServer() {
        if (mDeleteModel == null){
            mDeleteModel = new DeleteAttachmentSvcModel(this);
        }

        param = new HashMap<String,String>();

        param.put("attachment_id",mCurrencyAttachmentId);
        mDeleteModel.load(param);

    }


}
