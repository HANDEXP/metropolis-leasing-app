package org.hand.mas.metropolisleasing.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hand.hrms4android.parser.Expression;
import com.hand.hrms4android.parser.xml.XmlConfigReader;
import com.littlemvc.model.LMModel;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.AsHttpRequestModel;
import com.littlemvc.utl.AsNetWorkUtl;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.application.MSApplication;
import org.hand.mas.metropolisleasing.models.LoadingModel;
import org.hand.mas.metropolisleasing.models.RemoteModel;
import org.hand.mas.utl.ConstantUrl;
import org.hand.mas.utl.ConstantUtl;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gonglixuan on 15/4/1.
 */
public class LoadingActivity extends ActionBarActivity implements LMModelDelegate {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private String baseUrl;

    private LoadingModel model;
    private RemoteModel remoteModel;

    private DialogPlus dialogPlus;
    private TextView mBasicUrlTextView;
    private EditText mBasicUrlEditText;
    private Button mConfirmButton;
    private Button mReloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        MSApplication.getApplication().addActivity(this);

        setOverflowButtonAlways();
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();


        model = new LoadingModel(this);
        baseUrl = mPreferences.getString("sys_basic_url","");

        bindAllViews();


    }

    @Override
    protected void onResume() {
        super.onResume();

        /* 非Login页面跳转回来 */
        if (checkBaseUrl(baseUrl) && getIntent().getAction() != null){
            doReload();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_set:
                if (dialogPlus == null || ( dialogPlus != null || !dialogPlus.isShowing())){
                    buildBasicUrlDialog();
                }
                break;
            case R.id.action_exit:
                MSApplication.getApplication().exit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void modelDidFinishLoad(LMModel model) {
        // TODO Auto-generated method stub

        //
        // startLoginActivity();
        if (model.equals(this.model)) {

            File dir = MSApplication.getApplication().getDir(
                    ConstantUtl.SYS_PREFRENCES_CONFIG_FILE_DIR_NAME,
                    Context.MODE_PRIVATE);

            File configFile = new File(dir, ConstantUtl.configFile);

            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = new FileOutputStream(configFile);

                fileOutputStream.write(this.model.mresponseBody);

                MSApplication.getApplication().reader = XmlConfigReader.getInstance();
                MSApplication.getApplication().reader.getAttr(new Expression("/backend-config", ""));



                fileOutputStream.close();

            } catch (Exception ex) {

                Toast.makeText(this, "读写配置文件出现错误", Toast.LENGTH_SHORT).show();

                return;

            }finally {
                ConstantUrl.basicUrl = baseUrl;
                mEditor.putString("sys_basic_url",baseUrl);
                mEditor.commit();
                Intent intent = new Intent(LoadingActivity.this,LoginActivity.class);
                finish();
                startActivity(intent);
                overridePendingTransition(0,R.anim.alpha_out);

            }
        }
    }

    @Override
    public void modelDidStartLoad(LMModel model) {

    }

    @Override
    public void modelDidFailedLoadWithError(LMModel model) {
        if (model.equals(this.model)) {
            Toast.makeText(this, "未找到配置文件", Toast.LENGTH_SHORT).show();
        }
        ConstantUrl.basicUrl = baseUrl;
        mEditor.putString("sys_basic_url",baseUrl);
        mEditor.commit();
        Intent intent = new Intent(LoadingActivity.this,LoginActivity.class);
        finish();
        startActivity(intent);
        overridePendingTransition(0,R.anim.alpha_out);
    }

    /**
     *
     * 检查url是否正确
     */
    private boolean checkBaseUrl(String url) {
        if (url == null) {
            return false;
        }

        if (url.length() == 0) {
            return false;
        }

        if (url.equals("http://")) {
            return false;
        }

        return true;
    }

    /**
     * 重新读取配置文件
     */
    public void doReload() {
        AsHttpRequestModel.utl = AsNetWorkUtl.getAsNetWorkUtl(baseUrl);
        String regex = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]" ;
        Pattern pattern = Pattern. compile(regex);
        Matcher matcher = pattern.matcher(baseUrl);
        boolean isMatch = matcher.matches();
        if (!isMatch){
            Toast.makeText(this,"地址不合法",Toast.LENGTH_SHORT).show();
        }else {
            this.model.load();
        }


    }

    private void setOverflowButtonAlways()
    {
        try
        {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKey = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKey.setAccessible(true);
            menuKey.setBoolean(config, false);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void buildBasicUrlDialog(){
        dialogPlus = new DialogPlus.Builder(LoadingActivity.this)
                .setContentHolder(new ViewHolder(R.layout.view_set_basic_url_dialog))
                .setGravity(DialogPlus.Gravity.TOP)
                .setMargins(30,200,30,0)
                .create();
        dialogPlus.show();

        mBasicUrlEditText = (EditText) findViewById(R.id.edittext_for_basic);
        mConfirmButton = (Button) findViewById(R.id.confirm_btn_for_basic);


        mBasicUrlEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBasicUrlTextView.setText("http://".concat(s.toString()));
            }
        });
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasicUrlFormation();
                dialogPlus.dismiss();

            }
        });
    }

    private void bindAllViews(){
        mBasicUrlTextView = (TextView) findViewById(R.id.basic_url_text_view);
        mReloadButton = (Button) findViewById(R.id.reload_button);

        if (!checkBaseUrl(baseUrl)){
            buildBasicUrlDialog();
        }
        mBasicUrlTextView.setText(baseUrl);
        mReloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseUrl = (String) mBasicUrlTextView.getText();

                doReload();
            }
        });

    }

    /**
     * 检查basicUrl是否符合格式
     *
     */
    private void BasicUrlFormation(){
        String basicUrlStr = (String) mBasicUrlTextView.getText();
        if (!basicUrlStr.endsWith("/")){
            basicUrlStr = basicUrlStr.concat("/");

        }
        mBasicUrlTextView.setText(basicUrlStr);
    }


}
