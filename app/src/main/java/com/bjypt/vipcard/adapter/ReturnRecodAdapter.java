package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.ReturnRecodBean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/7.
 */
public class ReturnRecodAdapter extends BaseAdapter {
    private Context context;
    private List<ReturnRecodBean> list;

    public ReturnRecodAdapter(Context context, List<ReturnRecodBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView ==null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.layout_return_recod_item,null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_return_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_return_time);
            holder.tvmoney = (TextView) convertView.findViewById(R.id.tv_return_price);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_time.setText(list.get(position).getTime());
        holder.tvmoney.setText("充值"+list.get(position).getChognzhi()+"返现"+list.get(position).getReturn_money());
        return convertView;
    }
    class ViewHolder{
        TextView tv_name;
        TextView tv_time;
        TextView tvmoney;
    }
}
