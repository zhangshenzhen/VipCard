package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;

/**
 * Created by Dell on 2018/3/27.
 */

public class MyServeGridViewAdapter extends BaseAdapter {

    private int[] images = {R.mipmap.transaction_record, R.mipmap.withdraw_deposit_record,
            R.mipmap.recharge_record, R.mipmap.shape, R.mipmap.attract_investment, R.mipmap.integral_shop, R.mipmap.generalize, R.mipmap.generalize};
    private String[] images_info = {"交易记录", "提现记录", "充值记录", "推荐给朋友", "客服热线","积分商城", "众筹", "卡推广收益"};
    private final LayoutInflater inflater;
    private Context context;

    public MyServeGridViewAdapter(Context context) {
        this.context = context;
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
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_my_serve, null);
            holder.iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
       //隐藏这里的众筹按钮
     if(position==6){ holder.iv_photo.setVisibility(View.GONE); holder.tv_name.setVisibility(View.GONE);}
        if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
            if (position == getCount() - 1 && "0".equals(getFromSharePreference(Config.userConfig.is_card_sales))) {
                holder.iv_photo.setVisibility(View.GONE);
                holder.tv_name.setVisibility(View.GONE);
            }else{
                holder.iv_photo.setImageResource(images[position]);
                holder.tv_name.setText(images_info[position]);
            }
        } else {
            if (position != getCount() - 1){
                holder.iv_photo.setImageResource(images[position]);
                holder.tv_name.setText(images_info[position]);
            }else{
                holder.iv_photo.setVisibility(View.GONE);
                holder.tv_name.setVisibility(View.GONE);
            }
        }

        return view;
    }

    class ViewHolder {
        ImageView iv_photo;
        TextView tv_name;
    }

    public String getFromSharePreference(String key) {
        return SharedPreferenceUtils.getFromSharedPreference(context, key);
    }
}