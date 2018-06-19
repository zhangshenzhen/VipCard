package com.bjypt.vipcard.activity.shangfeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.data.bean.BannerBean;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2018/4/26.
 */

public class LifeServiceGVAdapter extends BaseAdapter {
    private Context mContext;

    private List<BannerBean> listData;

    public LifeServiceGVAdapter(Context mContext, List<BannerBean> listData) {
        this.mContext = mContext;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gv_life_service, null);
            holder.tv_lifeservice_name = convertView.findViewById(R.id.tv_lifeservice_name);
            holder.iv_lifeservice_pic = convertView.findViewById(R.id.iv_lifeservice_pic);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_lifeservice_name.setText(listData.get(position).getApp_name());
        Glide.with(mContext).load(listData.get(position).getApp_icon()).error(R.mipmap.merchant_item_error).into(holder.iv_lifeservice_pic);

        return convertView;
    }

    class ViewHolder {
        ImageView iv_lifeservice_pic;
        TextView tv_lifeservice_name;
    }
}
