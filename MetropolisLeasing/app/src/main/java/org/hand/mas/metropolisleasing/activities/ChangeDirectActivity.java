package org.hand.mas.metropolisleasing.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.bean.ImageFolder;
import org.hand.mas.utl.CommonAdapter;
import org.hand.mas.utl.LocalImageLoader;
import org.hand.mas.utl.ViewHolder;

import java.util.List;

/**
 * Created by gonglixuan on 15/3/29.
 */
public class ChangeDirectActivity extends Activity {

    private List<ImageFolder> mImageFolderList;
    private ListView mListView;
    private ImageView mReturnImageView;
    private String mCurrencyImageFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_direct_list);

        generateParam();
        bindAllViews();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("mImgDir",mCurrencyImageFolder);
        setResult(0,intent);
        finishWithAnim();

    }

    private void bindAllViews() {
        mListView = (ListView) findViewById(R.id.direct_list);
        mReturnImageView = (ImageView) findViewById(R.id.to_album_grid);

        mListView.setAdapter(new CommonAdapter<ImageFolder>(ChangeDirectActivity.this,mImageFolderList,R.layout.activity_change_direct_list_child) {
            @Override
            public void convert(ViewHolder helper, ImageFolder obj, int position) {
                ImageView firstImg = helper.getView(R.id.imageview_for_direct);
                TextView nameTextView = helper.getView(R.id.file_direct_textview);
                TextView countTextView = helper.getView(R.id.count_for_images);

                LocalImageLoader.getInstance().isSampleForViewPager = true;
                LocalImageLoader.getInstance().loadImage(obj.getFirstImagePath(),firstImg,true);
                nameTextView.setText(obj.getName());
                countTextView.setText("("+String.valueOf(obj.getCount())+")");
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("mImgDir",mImageFolderList.get(position).getDir());
                setResult(0,intent);
                finishWithAnim();
            }
        });
        mReturnImageView.setVisibility(View.GONE);

    }

    private void generateParam(){
        Intent intent = getIntent();
        mImageFolderList = (List<ImageFolder>) intent.getSerializableExtra("imageFolderList");
        mCurrencyImageFolder = intent.getStringExtra("currencyImageFolder");
    }

    private void finishWithAnim(){
        finish();
        overridePendingTransition(R.anim.alpha_in,R.anim.move_out_left);
    }
}
