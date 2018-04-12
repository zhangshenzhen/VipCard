package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.YuEBaoActivity;

import com.bjypt.vipcard.activity.YuEBaoBuyActivity;
import com.bjypt.vipcard.model.YuEBean;

import java.util.List;

/**
 * Created by liyunte on 2017/1/6.
 */

public class YuEBaoAdapter extends BaseAdapter {
private Context context;
    private List<YuEBean> list;

    public YuEBaoAdapter(Context context, List<YuEBean> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView==null){
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_yu_e_item,parent,false);
            holder.tv_days_yu_e_bao = (TextView) convertView.findViewById(R.id.tv_days_yu_e_bao);
            holder.tv_shouyi_yu_e_bao = (TextView) convertView.findViewById(R.id.tv_shouyi_yu_e_bao);
            holder.tv_yu_e_bao_name = (TextView) convertView.findViewById(R.id.tv_yu_e_bao_name);
            holder.iv_yu_e_bao = (ImageView) convertView.findViewById(R.id.iv_yu_e_bao);
            convertView.setTag(holder);
        }else {
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.tv_days_yu_e_bao.setText(list.get(position).getProductday());
        holder.tv_yu_e_bao_name.setText(list.get(position).getProductname());
        if (list.get(position).getProductrate().contains(".")){
            holder.tv_shouyi_yu_e_bao.setText(list.get(position).getProductrate().substring(0,list.get(position).getProductrate().indexOf(".")+2)+"%");
        }else {
            holder.tv_shouyi_yu_e_bao.setText(list.get(position).getProductrate()+"%");
        }

        if (list.get(position).getProductstatus()!=null&&list.get(position).getProductstatus().equals("1")){
            holder.iv_yu_e_bao.setImageResource(R.mipmap.yu_e_bao_at_buy);
        }else {
            holder.iv_yu_e_bao.setImageResource(R.mipmap.yu_e_bao_end);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getProductstatus()!=null&&list.get(position).getProductstatus().equals("1")){
                    Intent intent = new Intent(context,YuEBaoBuyActivity.class);
                    intent.putExtra("productName",list.get(position).getProductname());
                    intent.putExtra("time",list.get(position).getDiffdate());
                    intent.putExtra("productrate",list.get(position).getProductrate());
                    intent.putExtra("investMoney",list.get(position).getProducttotalmoney());
                    intent.putExtra("pkProduct",list.get(position).getPkproduct());
                    context.startActivity(intent);
                }

            }
        });
        return convertView;
    }
    class MyViewHolder{
        TextView tv_shouyi_yu_e_bao;
        TextView tv_days_yu_e_bao;
        TextView tv_yu_e_bao_name;
        ImageView iv_yu_e_bao;

    }

}
