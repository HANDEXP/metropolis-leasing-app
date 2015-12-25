package org.hand.mas.utl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by gonglixuan on 15/3/25.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mList;
    protected int mItemLayoutId;

    protected CommonAdapter(Context mContext, List<T> mList,int itemLayoutId) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mList = mList;
        this.mItemLayoutId = itemLayoutId;
    }


    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public T getItem(int position) {
        return this.mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position,convertView,parent);
        convert(viewHolder, getItem(position),position);
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder helper,T obj,int position);

    public ViewHolder getViewHolder(int position,View convertView,ViewGroup parent){

        return ViewHolder.get(mContext,convertView,parent,mItemLayoutId,position);

    }
}
