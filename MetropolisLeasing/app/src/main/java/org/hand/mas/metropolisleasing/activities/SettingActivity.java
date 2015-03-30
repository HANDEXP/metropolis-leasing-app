package org.hand.mas.metropolisleasing.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.application.MSApplication;

/**
 * Created by gonglixuan on 15/3/30.
 */
public class SettingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MSApplication.getApplication().addActivity(this);
        setContentView(R.layout.activity_setting);
        bindAllViews();
    }

    private void bindAllViews() {
        Button quitButton = (Button) findViewById(R.id.quitBtn);
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
}
