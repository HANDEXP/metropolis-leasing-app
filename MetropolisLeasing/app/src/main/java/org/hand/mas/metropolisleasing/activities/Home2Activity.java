package org.hand.mas.metropolisleasing.activities;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;

import org.hand.mas.metropolisleasing.R;

public class Home2Activity extends Activity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
    }


    private void initView()
    {
        mListView = (ListView)findViewById(R.id.home_list);
    }



}
