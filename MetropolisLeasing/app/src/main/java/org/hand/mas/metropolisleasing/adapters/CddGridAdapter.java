package org.hand.mas.metropolisleasing.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.hand.mas.custom_view.RoundImageView;
import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.models.CddGridModel;
import org.hand.mas.utl.ConstantUrl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gonglixuan on 15/3/16.
 */
public class CddGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<CddGridModel> mList;
    private DisplayImageOptions mOptions;

    public CddGridAdapter(Context mContext, List<CddGridModel> mList) {
        this(mContext,mList,new DisplayImageOptions.Builder().build());
    }

    public CddGridAdapter(Context mContext, List<CddGridModel> mList, DisplayImageOptions options) {
        this.mContext = mContext;
        this.mList = mList;
        this.mOptions = options;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cdd_grid_item,parent,false);

        }
        RoundImageView roundImageView = (RoundImageView) convertView.findViewById(R.id.roundImageView_for_cdd_item);
        TextView descriptionTextView = (TextView) convertView.findViewById(R.id.description_for_cdd_item);

        String cddItemId = mList.get(position).getCddItemId();
        String description = mList.get(position).getDescription();
        String filePath = mList.get(position).getFilePath();
        String fileName = mList.get(position).getFileName();
        String fileSuffix = mList.get(position).getFileSuffix().toLowerCase();
        boolean remote = mList.get(position).getRemote();

        descriptionTextView.setText(description);

        Pattern pattern = Pattern.compile("png|jpeg|jpg|bmp");
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

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

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

                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {

                            }
                        });
            }
        }

        return convertView;
    }


}
