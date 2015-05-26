package org.hand.mas.metropolisleasing.activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;

import org.hand.mas.custom_view.RoundImageView;
import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.application.MSApplication;
import org.hand.mas.metropolisleasing.models.LoginSvcModel;
import org.hand.mas.utl.ConstantAnim;
import org.hand.mas.utl.ConstantUtl;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static org.hand.mas.utl.ConstantAnim.fadeInView;

/**
 * Created by gonglixuan on 15/3/19.
 */
public class LoginActivity extends Activity implements LMModelDelegate{

    private EditText mLoginEditText;
    private EditText mPasswordEditText;
    private LinearLayout LLForEditText;
    private RoundImageView mImage;
    private Button mButton;
    private SweetAlertDialog pDialog;
    private TextView mChangeBasicUrlTextView;

    private HashMap<String,String> param;
    private LoginSvcModel mModel;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static int NO = 0;
    private static int YES = 1;
    private int i = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MSApplication.getApplication().addActivity(this);
        bindAllViews();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInfoReady();
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
    public void onBackPressed() {

        MSApplication.getApplication().exit();
    }

    @Override
    public void modelDidFinishLoad(LMModel model) {

        AsHttpRequestModel reponseModel = (AsHttpRequestModel) model;
        String json = new String(reponseModel.mresponseBody);
        try {
            JSONObject jsonObj = new JSONObject(json);
            String code =  ((JSONObject)jsonObj.get("head")).get("code").toString();
            if (code.equals("ok")){
                saveUserData();
                new CountDownTimer(500*4,500){
                    @Override
                    public void onTick(long millisUntilFinished) {
                        i++;
                        switch (i){
                            case 1:
                                break;
                            case 2:
                                pDialog.setTitleText("登录成功")
                                        .changeAlertType(SweetAlertDialog.SUCCESS_AFTER_PROGRESS_TYPE);
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onFinish() {
                        i = -1;
                        pDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(),OrderListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }.start();
            }else {
                pDialog.setTitleText("登录失败")
                        .setConfirmText("确定")
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mButton.setClickable(true);
        }
    }

    @Override
    public void modelDidStartLoad(LMModel model) {
        pDialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("登录中");
        pDialog.setCancelable(false);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.theme_color));
        pDialog.show();
    }

    @Override
    public void modelDidFailedLoadWithError(LMModel model) {
        i = -1;
        pDialog.setTitleText("网络错误，请稍后再试")
               .setConfirmText("OK")
               .changeAlertType(SweetAlertDialog.ERROR_TYPE);
        mButton.setClickable(true);
    }

    /* Private Methods */
    private void bindAllViews(){
        mLoginEditText = (EditText) findViewById(R.id.login_editText);
        mPasswordEditText = (EditText) findViewById(R.id.password_editText);
        mImage = (RoundImageView) findViewById(R.id.img_for_login);
        mButton = (Button) findViewById(R.id.loginBtn);
        LLForEditText = (LinearLayout) findViewById(R.id.linearLayout_for_edittext);
        mChangeBasicUrlTextView = (TextView) findViewById(R.id.change_basic_url_textview);


        mLoginEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                isInfoReady();
            }
        });
        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isInfoReady();
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButton.setClickable(false);
                generateParam();
                mModel.load(param);
            }
        });
        mChangeBasicUrlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,LoadingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mModel = new LoginSvcModel(this);
        setDefaultUserData();
        fadeInView(mImage,1500);
//        translateYView(LLForEditText,800);
        ConstantAnim.fadeInAndTranslateView(LLForEditText);


    }
    /*
    *
    * 检查用户名密码是否齐全
    *
    * */
    private void isInfoReady(){
        int isUserNameReady,isUserPasswordReady;
        if(mLoginEditText.getText().length() > 0){
            isUserNameReady = YES;
        }else {
            isUserNameReady = NO;
        }
        if(mPasswordEditText.getText().length() > 0){
            isUserPasswordReady = YES;
        }else{
            isUserPasswordReady = NO;
        }
        switch (isUserNameReady + isUserPasswordReady){
            case 2:
                mButton.setTextColor(Color.parseColor("#ffffffff"));
                mButton.setEnabled(true);
                break;
            case 1:
                mButton.setTextColor(Color.parseColor("#bbffffff"));
                mButton.setEnabled(false);
                break;
            case 0:
                mButton.setTextColor(Color.parseColor("#50ffffff"));
                mButton.setEnabled(false);
                break;
            default:
                mButton.setTextColor(Color.parseColor("#50ffffff"));
                mButton.setEnabled(false);
                break;
        }
    }

    /*
    *
    * 创建登录参数
    *
    * */
    private void generateParam(){

        param = new HashMap<String,String>();
        param.put("user_name",mLoginEditText.getText().toString());
        param.put("user_password",mPasswordEditText.getText().toString());
        param.put("device_type","Android");
        try{
//            editor.putString(ConstantUtl.SYS_PREFRENCES_PUSH_TOKEN,)
        }catch (Exception e){
            e.printStackTrace();
        }finally {
//            editor.commit();
        }
        String token = preferences.getString(ConstantUtl.SYS_PREFRENCES_PUSH_TOKEN, "");
        if(token.length() != 0){
            param.put("push_token",token);
        }else{
            param.put("push_token","-1");
        }
        param.put("device_id", ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId());
    }

    /*
    * 存储用户资料
    * */
    private void saveUserData(){
        SharedPreferences preferences = getSharedPreferences("userInfo", MODE_APPEND);
        String userName = param.get("user_name");
        String userPassword = param.get("user_password");
        preferences.edit().putString("userName",userName).commit();
        preferences.edit().putString("userPassword",userPassword).commit();

    }

    /*
     * 取出缓存的用户资料
     */
    private void setDefaultUserData(){
        SharedPreferences preferences = getSharedPreferences("userInfo",MODE_APPEND);
        String userName = preferences.getString("userName","");
        String userPassword = preferences.getString("userPassword","");
        mLoginEditText.setText(userName);
        mPasswordEditText.setText(userPassword);
    }





  }
