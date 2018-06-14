package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;

/**
 * Created by Dell on 2018/3/27.
 */

public class AccountGridViewAdapter extends BaseAdapter{

    private int[] images = {R.mipmap.remaining_sum_record, R.mipmap.account_card,
            R.mipmap.integral_card, R.mipmap.withdraw_deposit, R.mipmap.recharge};
    private String[] images_info = {"余额记录","银行卡","积分记录","我提现","我充值"};
    private final LayoutInflater inflater;

    public AccountGridViewAdapter(Context context){
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_account,null);
            holder.iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        if(4 == position || 3 == position) {
            holder.iv_photo.setVisibility(View.GONE);
            holder.tv_name.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }else {
            holder.iv_photo.setImageResource(images[position]);
            holder.tv_name.setText(images_info[position]);
        }
        return view;
    }

    class ViewHolder{
        ImageView iv_photo;
        TextView tv_name;
    }
}