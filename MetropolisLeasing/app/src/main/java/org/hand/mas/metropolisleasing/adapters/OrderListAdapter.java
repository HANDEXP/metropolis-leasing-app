package org.hand.mas.metropolisleasing.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.models.OrderModel;

import java.util.List;

/**
 * Created by gonglixuan on 15/3/10.
 */
public class OrderListAdapter extends BaseAdapter {

    private List<OrderModel> mList;
    private Context mContext;

    public OrderListAdapter(List<OrderModel> list, Context context){
        this.mList = list;
        this.mContext = context;

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
        if(convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.activity_orders_list_child,null);

        }
        return convertView;
    }
}
