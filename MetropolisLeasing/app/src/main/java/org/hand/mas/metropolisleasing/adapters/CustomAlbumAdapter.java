package org.hand.mas.metropolisleasing.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.utl.CommonAdapter;
import org.hand.mas.utl.LocalImageLoader;
import org.hand.mas.utl.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gonglixuan on 15/3/27.
 */
public class CustomAlbumAdapter<T> extends CommonAdapter<T> {

    private String mDirPath;
    private LocalImageLoader mImageLoader;
    public List<Object> mSelectedList;
    private View.OnClickListener mOnClickListener;

    public CustomAlbumAdapter(Context mContext, List<T> mImgs, List<Object>mSelectedList, String mDirPath,View.OnClickListener mOnClickListener) {
        this(mContext, mImgs,mSelectedList, R.layout.album_grid_item,mDirPath,mOnClickListener);

    }

    public CustomAlbumAdapter(Context mContext, List<T> mImgs, List<Object>mSelectedList, int itemLayoutId,String mDirPath,View.OnClickListener mOnClickListener) {
        super(mContext, mImgs, itemLayoutId);

        this.mDirPath = mDirPath;
        this.mImageLoader = LocalImageLoader.getInstance(3, LocalImageLoader.Type.LIFO);
        if (mSelectedList == null){
            this.mSelectedList = new ArrayList<>();
        }else {
            this.mSelectedList = mSelectedList;
        }

        this.mOnClickListener = mOnClickListener;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return super.getView(position, convertView, parent);

    }

    @Override
    public void convert(ViewHolder helper, T obj, int position) {
        ImageView imageView = (ImageView) helper.getConvertView().findViewById(R.id.album_item_image);
        ImageButton imageButton = (ImageButton) helper.getConvertView().findViewById(R.id.album_item_select_button);
        imageButton.setVisibility(View.VISIBLE);
        imageView.setTag(R.id.position,position);
        imageButton.setTag(R.id.position,position);

        if (!mSelectedList.contains((Object)position)){
            imageButton.setImageResource(R.drawable.icon_for_pic_unselected);
        }else {
            imageButton.setImageResource(R.drawable.icon_for_pic_selected);
        }


        initEvent(imageView, imageButton,position);
        imageView.setImageResource(R.drawable.friends_sends_pictures_no);
        String filePath = mDirPath.concat("/").concat(obj.toString());
        mImageLoader.loadImage(filePath,imageView);
    }


    /**
     * GridItem绑定事件
     * @param imageView
     * @param imageButton
     * @param position
     */
    private void initEvent(ImageView imageView,ImageButton imageButton, final int position){
        imageView.setOnClickListener(mOnClickListener);
        imageButton.setOnClickListener(mOnClickListener);

    }

}
