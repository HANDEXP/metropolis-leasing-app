package org.hand.mas.metropolisleasing.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.hand.mas.custom_view.RoundImageView;
import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.models.CddGridModel;
import org.hand.mas.utl.CommonAdapter;
import org.hand.mas.utl.ConstantUrl;
import org.hand.mas.utl.ViewHolder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gonglixuan on 15/3/25.
 */
public class CustomCddGridAdapter<T> extends CommonAdapter<T> {

    private DisplayImageOptions mOptions;
    private View.OnClickListener mIconClickListener;
    private View.OnClickListener mImgClickListener;

    public CustomCddGridAdapter(List mList, Context mContext,int mItemLayoutId, DisplayImageOptions mOptions) {
        super(mContext, mList,mItemLayoutId);
        this.mOptions = mOptions;
    }
    public CustomCddGridAdapter(List mList, Context mContext,int mItemLayoutId, DisplayImageOptions mOptions, View.OnClickListener mIconClickListener,View.OnClickListener mImgClickListener){
        super(mContext, mList,mItemLayoutId);
        this.mOptions = mOptions;
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

        roundImageView.setTag(position);
        deleteImageButton.setTag(position);

        Pattern pattern = Pattern.compile("png|jpeg|jpg|bmp|gif");
        Matcher matcher = pattern.matcher(fileSuffix);
        if(!matcher.find()){
            ImageLoader.getInstance().displayImage(
                    "https://avatars0.githubusercontent.com/u/3929205?v=3&u=578eeae7eb975f8de9b1facdeef5bac5225c258c&s=140",
                    roundImageView,
                    mOptions,
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            initEvent(deleteImageButton, roundImageView);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });

        }else{
            if(!remote){
                ImageLoader.getInstance().displayImage(filePath,
                        roundImageView,
                        mOptions,
                        new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                ((ImageView)view).setImageResource(R.drawable.friends_sends_pictures_no);
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                initEvent(deleteImageButton, roundImageView);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {

                            }
                        });
            }else{
                ImageLoader.getInstance().displayImage(ConstantUrl.basicUrl+filePath,
                        roundImageView,
                        mOptions,
                        new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                initEvent(deleteImageButton, roundImageView);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {

                            }
                        });
            }
        }
    }

    /* 显示删除按钮 */
    private void initEvent(View iconView,View imgView){
        /* anim */


        iconView.setVisibility(View.VISIBLE);
        iconView.setOnClickListener(mIconClickListener);

        imgView.setOnClickListener(mImgClickListener);

    }
}
