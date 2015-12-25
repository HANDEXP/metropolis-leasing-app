package org.hand.mas.metropolisleasing.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hand.hrms4android.exception.ParseExpressionException;
import com.hand.hrms4android.parser.Expression;
import com.hand.hrms4android.parser.xml.XmlConfigReader;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.application.MSApplication;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by gonglixuan on 15/3/30.
 */
public class SettingActivity extends Activity implements View.OnClickListener{

    private XmlConfigReader configReader;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    LinearLayout userInfoLL;
    LinearLayout changeCharacterLL;
    LinearLayout changePasswordLL;
    LinearLayout questionLL;
    LinearLayout discussLL;
    LinearLayout versionUpdateLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MSApplication.getApplication().addActivity(this);
        setContentView(R.layout.activity_setting);
        configReader = XmlConfigReader.getInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        bindAllViews();


    }

    @Override
    protected void onResume() {
        super.onResume();
        resetClickable(userInfoLL);
        resetClickable(changeCharacterLL);
        resetClickable(changePasswordLL);
        resetClickable(questionLL);
        resetClickable(discussLL);
        resetClickable(versionUpdateLL);
    }

    private void bindAllViews() {
        Button quitButton = (Button) findViewById(R.id.quitBtn);



        setDefaultUserData();

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(SettingActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定要退出吗？")
                        .setConfirmText("确定")
                        .setCancelText("取消")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                MSApplication.getApplication().exit();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.alpha_in,R.anim.move_out_bottm);

    }

    @Override
    public void onClick(View v) {
        v.setClickable(false);
        Intent intent = new Intent(SettingActivity.this,HtmlBaseActivity.class);
        String url = null;
        switch (v.getId()){


        }
    }
    /*
     * 取出缓存的用户资料
     */
    private void setDefaultUserData(){
        TextView usernameTextView = (TextView) findViewById(R.id.username_textview_in_setting);
        SharedPreferences preferences = getSharedPreferences("userInfo",MODE_APPEND);
        String userName = preferences.getString("userName","");
        usernameTextView.setText(userName);
    }

    /**
     * 重置clickable
     */
    private void resetClickable(View view){
        if (view != null && !view.isClickable()){
            view.setClickable(true);
        }
    }

    private void showErrorMsg() {
        Toast.makeText(SettingActivity.this,"Url没有配置",Toast.LENGTH_SHORT).show();
    }
}
