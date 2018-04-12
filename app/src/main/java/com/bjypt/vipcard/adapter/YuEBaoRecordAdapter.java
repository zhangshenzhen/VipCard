package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.CouponBean;
import com.bjypt.vipcard.model.YuEBaoRecordAdapterBean;
import com.bjypt.vipcard.model.YuEBaoRecordBean;
import com.bjypt.vipcard.view.HorizontalProgressBarWithNumber;

import java.util.List;

/**
 * Created by liyunte on 2017/1/9.
 */

public class YuEBaoRecordAdapter extends BaseAdapter {
    private Context context;
    private List<YuEBaoRecordAdapterBean> list;

    public YuEBaoRecordAdapter(Context context, List<YuEBaoRecordAdapterBean> list) {
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
        MyViewHolder holder = null;
        if (convertView==null){
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_yu_e_bao_record_item,parent,false);
            holder.hprogress_yu_e_bao_record_item = (HorizontalProgressBarWithNumber) convertView.findViewById(R.id.hprogress_yu_e_bao_record_item);
      holder.tv_nh_shouyi_yu_e_bao_record_item = (TextView) convertView.findViewById(R.id.tv_nh_shouyi_yu_e_bao_record_item);
      holder.tv_days_yu_e_bao_record_item = (TextView) convertView.findViewById(R.id.tv_days_yu_e_bao_record_item);
      holder.tv_time_yu_e_bao_record_item = (TextView) convertView.findViewById(R.id.tv_time_yu_e_bao_record_item);
      holder.tv_by_money_yu_e_bao_record_item = (TextView) convertView.findViewById(R.id.tv_by_money_yu_e_bao_record_item);
      holder.tv_shouyi_price_yu_e_bao_record_item = (TextView) convertView.findViewById(R.id.tv_shouyi_price_yu_e_bao_record_item);
      holder.tv_zt_yu_e_bao_record = (TextView) convertView.findViewById(R.id.tv_zt_yu_e_bao_record);
      holder.tv_yu_e_bao_record_name = (TextView) convertView.findViewById(R.id.tv_yu_e_bao_record_name);
      convertView.setTag(holder);
        }else {
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.hprogress_yu_e_bao_record_item.setProgress(list.get(position).getProgress());
        holder.tv_yu_e_bao_record_name.setText(list.get(position).getName());
        holder.tv_nh_shouyi_yu_e_bao_record_item.setText(list.get(position).getYearLv()+"%");
        holder.tv_days_yu_e_bao_record_item.setText(list.get(position).getDays()+"天");
        holder.tv_time_yu_e_bao_record_item.setText("开始时间："+list.get(position).getStartTime()+"   "+"结束时间："+list.get(position).getEndTime());
        holder.tv_by_money_yu_e_bao_record_item.setText(list.get(position).getBuyprice());
        holder.tv_shouyi_price_yu_e_bao_record_item.setText(list.get(position).getShouyi());
        if (list.get(position).getIsEnd()!=null&&list.get(position).getIsEnd().equals("1")){
holder.tv_zt_yu_e_bao_record.setText("进行中");
            holder.tv_zt_yu_e_bao_record.setBackgroundResource(R.mipmap.yu_e_bao_buy_record_yellow_bg);
        }else {
            holder.tv_zt_yu_e_bao_record.setText("已结算");
            holder.tv_zt_yu_e_bao_record.setBackgroundResource(R.mipmap.yu_e_bao_buy_record_red_bg);
        }
        return convertView;
    }
    class MyViewHolder{
TextView tv_nh_shouyi_yu_e_bao_record_item;//年化收益
TextView tv_days_yu_e_bao_record_item;//天数
TextView tv_time_yu_e_bao_record_item;//开始时间和结束时间
TextView tv_by_money_yu_e_bao_record_item;//购买金额
TextView tv_shouyi_price_yu_e_bao_record_item;//收益
TextView tv_zt_yu_e_bao_record;//结算状态
TextView tv_yu_e_bao_record_name;//migncheng
        HorizontalProgressBarWithNumber hprogress_yu_e_bao_record_item;//进度
    }
    public void add(List<YuEBaoRecordAdapterBean> l){
        if (list == null){
            list = l;
        }else {
            list.addAll(l);
        }
    }
}
