package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.PayOneBean;

import java.util.List;

/**
 * Created by Administrator on 2016/3/28.
 */
public class PayOneAdapter extends BaseAdapter{
    private Context context;
    private List<PayOneBean> list;
    public PayOneAdapter(Context context,List<PayOneBean> list) {
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
        if (convertView == null){
           holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.layout_pay_one_item,null);
            holder.name = (TextView)convertView.findViewById(R.id.tv_caiming);
            holder.fen = (TextView)convertView.findViewById(R.id.tv_fen);
            holder.price = (TextView)convertView.findViewById(R.id.tv_price);
            convertView.setTag(holder);
        }
        holder = (ViewHolder)convertView.getTag();
        holder.name.setText(list.get(position).getName());
        holder.fen.setText(list.get(position).getFen()+"份");
        holder.price.setText(list.get(position).getPrice()+"元");
        return convertView;
    }
    class ViewHolder{
        TextView name;
        TextView fen;
        TextView price;
    }
}
