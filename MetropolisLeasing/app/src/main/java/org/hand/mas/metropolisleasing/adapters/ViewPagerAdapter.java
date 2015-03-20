package org.hand.mas.metropolisleasing.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.models.CddGridModel;
import org.hand.mas.utl.ConstantUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by gonglixuan on 15/3/17.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<CddGridModel> mList;
    private List<String> stringList;

    public ViewPagerAdapter(Context mContext, List<CddGridModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.stringList = new ArrayList<String>();

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override

    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());

        String description = mList.get(position).getDescription();
        String filePath = mList.get(position).getFilePath();
        String fileName = mList.get(position).getFileName();
        String fileSuffix = mList.get(position).getFileSuffix().toLowerCase();
        boolean remote = mList.get(position).getRemote();

        Pattern pattern = Pattern.compile("png|jpeg|jpg|bmp");
        Matcher matcher = pattern.matcher(fileSuffix);
        if (!matcher.find()){
            ImageLoader.getInstance().displayImage("https://avatars0.githubusercontent.com/u/3929205?v=3&u=578eeae7eb975f8de9b1facdeef5bac5225c258c&s=140",
                    photoView);
        }else {
            if (!remote){
                ImageLoader.getInstance().displayImage(filePath,photoView);
            }else {
                ImageLoader.getInstance().displayImage(ConstantUrl.basicUrl+filePath,photoView);
            }

        }


        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
