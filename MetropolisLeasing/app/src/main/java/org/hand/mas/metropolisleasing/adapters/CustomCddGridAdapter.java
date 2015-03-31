package org.hand.mas.metropolisleasing.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.hand.mas.custom_view.RoundImageView;
import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.models.CddGridModel;
import org.hand.mas.utl.CommonAdapter;
import org.hand.mas.utl.ConstantUrl;
import org.hand.mas.utl.LocalImageLoader;
import org.hand.mas.utl.ViewHolder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gonglixuan on 15/3/25.
 */
public class CustomCddGridAdapter<T> extends CommonAdapter<T> {

    private View.OnClickListener mIconClickListener;
    private View.OnClickListener mImgClickListener;

    public CustomCddGridAdapter(List mList, Context mContext,int mItemLayoutId) {
        super(mContext, mList,mItemLayoutId);
    }
    public CustomCddGridAdapter(List mList, Context mContext,int mItemLayoutId, View.OnClickListener mIconClickListener,View.OnClickListener mImgClickListener){
        super(mContext, mList,mItemLayoutId);
        this.mIconClickListener = mIconClickListener;
        this.mImgClickListener = mImgClickListener;

    }



    @Override
    public void convert(ViewHolder helper, T obj,int position) {
        final RoundImageView roundImageView = helper.getView(R.id.roundImageView_for_cdd_item);
        final ImageButton deleteImageButton = helper.getView(R.id.deleteImageButton);


        CddGridModel item = (CddGridModel) obj;
        String cddItemId = item.getCddItemId();
        String description = item.getDescription();
        String filePath = item.getFilePath();
        String fileName = item.getFileName();
        String fileSuffix = item.getFileSuffix().toLowerCase();
        boolean remote = item.getRemote();

        roundImageView.setTag(R.id.position,position);
        deleteImageButton.setTag(R.id.position,position);

        Pattern pattern = Pattern.compile("png|jpeg|jpg|bmp|gif");
        Matcher matcher = pattern.matcher(fileSuffix);
        roundImageView.setImageResource(R.drawable.friends_sends_pictures_no);
        if(!matcher.find()){

        }else {
            LocalImageLoader.getInstance().isSampleForViewPager = true;
            LocalImageLoader.getInstance().loadImage(ConstantUrl.basicUrl+filePath,roundImageView,false);

        }
        initEvent(deleteImageButton,roundImageView);
    }

    /* 显示删除按钮 */
    private void initEvent(View iconView,View imgView){
        /* anim */


        iconView.setVisibility(View.VISIBLE);
        iconView.setOnClickListener(mIconClickListener);

        imgView.setOnClickListener(mImgClickListener);

    }
}
