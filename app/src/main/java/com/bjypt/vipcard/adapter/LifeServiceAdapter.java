package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.LifeServiceBean;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/6/14
 * Use by
 */
public class LifeServiceAdapter extends MyBaseAdapter<LifeServiceBean>{


    public LifeServiceAdapter( Context mContext,List<LifeServiceBean> list) {
        super(list, mContext);
    }

    @Override
    public View initView(int position, View convertView, ViewGroup parent) {

        LifeViewHolder holder;
        if (convertView==null){
            holder = new LifeViewHolder();
            convertView = inflater.inflate(R.layout.life_service_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_tri_name);
            holder.iv_url_log = (ImageView) convertView.findViewById(R.id.iv_url_log);
            holder.price = (TextView) convertView.findViewById(R.id.tv_tri_price);
            holder.tv_transaction_zhuantai = (TextView) convertView.findViewById(R.id.tv_transaction_zhuantai);
            holder.tel = (TextView) convertView.findViewById(R.id.life_tel);
            holder.time = (TextView) convertView.findViewById(R.id.tv_tri_time);
            convertView.setTag(holder);
        }
        holder = (LifeViewHolder) convertView.getTag();

        if(list.get(position).getConsumeType().equals("1")){
            holder.name.setText("手机充值");
        }else if(list.get(position).getConsumeType().equals("2")){
            holder.name.setText("油卡充值");
        }else if(list.get(position).getConsumeType().equals("3")){
            holder.name.setText("固话");
        }else if(list.get(position).getConsumeType().equals("4")){
            holder.name.setText("QQ服务");
        }else if(list.get(position).getConsumeType().equals("5")){
            holder.name.setText("流量充值");
        }

        if(list.get(position).getStatus().equals("0")){
            holder.tv_transaction_zhuantai.setText("处理中");
        }else if(list.get(position).getStatus().equals("1")){
            holder.tv_transaction_zhuantai.setText("消费成功");
        }
        holder.tel.setText(list.get(position).getMobile());

        holder.price.setText("金额¥" + list.get(position).getTotalMoney()+"("+list.get(position).getRebateMoney()+"元)");

        holder.time.setText(list.get(position).getOrderTime());
        return convertView;
    }

    class LifeViewHolder{
        ImageView iv_url_log;
        TextView name;
        TextView price;
        TextView tv_transaction_zhuantai;
        TextView time;
        TextView tel;
    }
}
