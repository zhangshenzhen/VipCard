package com.bjypt.vipcard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjypt.vipcard.R;

import java.util.List;

/**
 * Created by Administrator on 2018/3/27.
 */

public class ShopStreetGridviewAdapter extends BaseAdapter {

    private int clickTemp = -1;
    private Context mContext;
    private List<String> listName;

    public ShopStreetGridviewAdapter(Context mContext, List<String> listName) {
        this.mContext = mContext;
        this.listName = listName;
    }

    public void setSelection(int position) {
        clickTemp = position;
    }

    @Override
    public int getCount() {
        return listName.size();
    }

    @Override
    public Object getItem(int i) {
        return listName.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MyViewHolder holder = null;
        if (convertView == null) {
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gridview_shopstreet, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(listName.get(i));

        if (clickTemp == i) {
            holder.tv_name.setBackgroundResource(R.mipmap.bg_grid_mian);
            holder.tv_name.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.tv_name.setBackgroundResource(R.mipmap.bg_grid_white);
            holder.tv_name.setTextColor(Color.parseColor("#999999"));
        }

        return convertView;
    }

    class MyViewHolder {
        TextView tv_name;
    }
}
