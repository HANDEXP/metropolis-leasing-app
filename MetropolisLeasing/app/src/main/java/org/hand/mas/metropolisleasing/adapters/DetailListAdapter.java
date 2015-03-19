package org.hand.mas.metropolisleasing.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.models.DetailListModel;

import java.util.List;

/**
 * Created by gonglixuan on 15/3/11.
 */
public class DetailListAdapter extends BaseAdapter {

    private List<DetailListModel> mList;
    private Context mContext;


    public DetailListAdapter(List<DetailListModel> mList, Context context) {
        this.mList = mList;
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
        if (convertView == null){
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.activity_order_details_list_child,null);
        }


        TextView bpNameTextView = (TextView) convertView.findViewById(R.id.bp_name_for_detail);
        TextView descriptionTextView = (TextView) convertView.findViewById(R.id.description_for_detail);
        TextView ccdCountTextView = (TextView) convertView.findViewById(R.id.ccd_count_for_detail);

        DetailListModel item = mList.get(position);

        bpNameTextView.setText(item.getBpName());
        descriptionTextView.setText(item.getDescription());
        ccdCountTextView.setText(item.getCddCount());

        return convertView;
    }
}
