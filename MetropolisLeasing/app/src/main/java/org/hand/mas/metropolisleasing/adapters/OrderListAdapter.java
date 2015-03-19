package org.hand.mas.metropolisleasing.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.hand.mas.metropolisleasing.R;
import org.hand.mas.metropolisleasing.models.OrderListModel;

import java.util.List;

/**
 * Created by gonglixuan on 15/3/10.
 */
public class OrderListAdapter extends BaseAdapter {

    private List<OrderListModel> mList;
    private Context mContext;

    public OrderListAdapter(List<OrderListModel> list, Context context){
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
        TextView projectNumberTextView = (TextView) convertView.findViewById(R.id.project_number_for_order);
        TextView projectStatusDesc = (TextView) convertView.findViewById(R.id.project_status_desc_for_order);
        TextView bpName = (TextView) convertView.findViewById(R.id.bp_name_for_order);
        TextView projectSource = (TextView) convertView.findViewById(R.id.project_source_for_order);
        TextView bpClass = (TextView) convertView.findViewById(R.id.bp_class_for_order);
        TextView identifierCode = (TextView) convertView.findViewById(R.id.identifier_code_for_order);

        OrderListModel item = mList.get(position);

        projectNumberTextView.setText(item.getProjectNumber());
        projectStatusDesc.setText(item.getProjectStatusDesc());
        bpName.setText(item.getBpName());
        projectSource.setText(item.getProjectSource());
        bpClass.setText(item.getBpClass().concat("识别号:"));
        if (bpClass.equals("ORG")){
            identifierCode.setText(item.getOrganizationCode());
        }else if (bpClass.equals("NP")){
            identifierCode.setText(item.getIdCardNo());
        }



        return convertView;
    }
}
