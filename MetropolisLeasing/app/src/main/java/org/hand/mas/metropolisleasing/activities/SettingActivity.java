package org.hand.mas.metropolisleasing.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hand.hrms4android.exception.ParseExpressionException;
import com.hand.hrms4android.parser.Expression;
import com.hand.hrms4android.parser.xml.XmlConfigReader;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.application.MSApplication;

/**
 * Created by gonglixuan on 15/3/30.
 */
public class SettingActivity extends Activity implements View.OnClickListener{

    private XmlConfigReader configReader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MSApplication.getApplication().addActivity(this);
        setContentView(R.layout.activity_setting);
        configReader = XmlConfigReader.getInstance();
        bindAllViews();

    }

    private void bindAllViews() {
        Button quitButton = (Button) findViewById(R.id.quitBtn);
        LinearLayout userInfoLL = (LinearLayout) findViewById(R.id.user_info_LL);
        LinearLayout changeCharacterLL = (LinearLayout) findViewById(R.id.change_character_LL);
        LinearLayout changePasswordLL = (LinearLayout) findViewById(R.id.change_password_LL);
        LinearLayout questionLL = (LinearLayout) findViewById(R.id.question_LL);
        LinearLayout discussLL = (LinearLayout) findViewById(R.id.discuss_LL);
        LinearLayout versionUpdateLL = (LinearLayout) findViewById(R.id.version_update_LL);


        userInfoLL.setOnClickListener(this);
        changeCharacterLL.setOnClickListener(this);
        changePasswordLL.setOnClickListener(this);
        questionLL.setOnClickListener(this);
        discussLL.setOnClickListener(this);
        versionUpdateLL.setOnClickListener(this);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MSApplication.getApplication().exit();
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
        Intent intent = new Intent(SettingActivity.this,HtmlBaseActivity.class);
        String url = null;
        switch (v.getId()){
            case R.id.user_info_LL:

                try {
                     url = configReader
                            .getAttr(new Expression(
                                    "/backend-config/url[@name='user_info_url']",
                                    "value"));
                } catch (ParseExpressionException e) {
                    e.printStackTrace();
                }
                intent.putExtra("url",url);
                startActivity(intent);
                break;
            case R.id.change_password_LL:
                try {
                    url = configReader
                            .getAttr(new Expression(
                                    "/backend-config/url[@name='change_password_url']",
                                    "value"));
                } catch (ParseExpressionException e) {
                    e.printStackTrace();
                }
                intent.putExtra("url",url);
                startActivity(intent);
                break;
            case R.id.question_LL:
                try {
                    url = configReader
                            .getAttr(new Expression(
                                    "/backend-config/url[@name='question_url']",
                                    "value"));
                } catch (ParseExpressionException e) {
                    e.printStackTrace();
                }
                intent.putExtra("url",url);
                startActivity(intent);
                break;
            case R.id.discuss_LL:
                try {
                    url = configReader
                            .getAttr(new Expression(
                                    "/backend-config/url[@name='discuss_url']",
                                    "value"));
                } catch (ParseExpressionException e) {
                    e.printStackTrace();
                }
                intent.putExtra("url",url);
                startActivity(intent);
                break;
            case R.id.version_update_LL:
                try {
                    url = configReader
                            .getAttr(new Expression(
                                    "/backend-config/url[@name='version_update_url']",
                                    "value"));
                } catch (ParseExpressionException e) {
                    e.printStackTrace();
                }
                intent.putExtra("url",url);
                startActivity(intent);
                break;
            case R.id.change_character_LL:
                try {
                    url = configReader
                            .getAttr(new Expression(
                                    "/backend-config/url[@name='change_character_url']",
                                    "value"));
                } catch (ParseExpressionException e) {
                    e.printStackTrace();
                }
                intent.putExtra("url",url);
                startActivity(intent);
                break;

        }
    }
}
