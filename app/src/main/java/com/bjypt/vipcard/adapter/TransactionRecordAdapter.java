package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.TransactionOrRechangeBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class TransactionRecordAdapter extends BaseAdapter {
    private List<TransactionOrRechangeBean> lists;
    private Context context;
    public TransactionRecordAdapter(List<TransactionOrRechangeBean> lists, Context context) {
        this.lists = lists;
        this.context = context;
        Wethod.configImageLoader(context);
    }
    @Override
    public int getCount() {
        return lists.size();
    }
    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.layout_transaction_record_item,null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_tri_name);
            holder.iv_url_log = (ImageView) convertView.findViewById(R.id.iv_url_log);
            holder.price = (TextView) convertView.findViewById(R.id.tv_tri_price);
            holder.tv_transaction_zhuantai = (TextView) convertView.findViewById(R.id.tv_transaction_zhuantai);
            holder.time = (TextView) convertView.findViewById(R.id.tv_tri_time);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        /*if (!"".equals(lists.get(position).getLogo_url())&&lists.get(position).getLogo_url()!=null){
            ImageLoader.getInstance().displayImage(lists.get(position).getLogo_url(),holder.iv_url_log);
        }*/
        if (!"".equals(lists.get(position).getTrade_desc())&&lists.get(position).getTrade_desc()!=null){
            holder.name.setText(lists.get(position).getTrade_desc());
        }
        holder.price.setText("金额￥" + lists.get(position).getTrade_money());
        if (!"".equals(lists.get(position).getStatus_desc())&&lists.get(position).getStatus_desc()!=null){
            holder.tv_transaction_zhuantai.setText(lists.get(position).getStatus_desc());
        }
        holder.time.setText(lists.get(position).getTrade_time());
        return convertView;
    }
    class ViewHolder{
        ImageView iv_url_log;
        TextView name;
        TextView price;
        TextView tv_transaction_zhuantai;
        TextView time;
    }
    public void add(List<TransactionOrRechangeBean> l){
        if (lists==null){
            lists = l;
        }else {
            lists.addAll(l);
        }
    }
}
