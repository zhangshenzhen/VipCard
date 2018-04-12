package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.DiscountCouponsActivity;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.MerchantPullListBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/3/27.
 */

public class MerchantPullListAdapter extends BaseAdapter {

    private Context context;
    private List<MerchantPullListBean.ResultDataBean> list;

    public MerchantPullListAdapter(Context context, List<MerchantPullListBean.ResultDataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        MyViewHolder holder = null;
        if (view == null){
            holder = new MyViewHolder();
            view = View.inflate(context, R.layout.item_merchant_list, null);
            holder.address = (TextView) view.findViewById(R.id.merchantList_address);
            holder.distance = (TextView) view.findViewById(R.id.merchantList_distance);
            holder.name = (TextView) view.findViewById(R.id.merchantList_name);
            holder.pic = (ImageView) view.findViewById(R.id.merchantList_pic);
            holder.phone = (ImageView) view.findViewById(R.id.merchantList_phone);
            view.setTag(holder);
        }else {
            holder = (MyViewHolder) view.getTag();
        }

        holder.name.setText(list.get(i).getMuname());
        holder.address.setText(list.get(i).getAddress());
        holder.distance.setText("距离："+list.get(i).getDistance());
        ImageLoader.getInstance().displayImage(Config.web.picUrl +list.get(i).getLogo(), holder.pic, AppConfig.DEFAULT_IMG_MERCHANT_BG);
        return view;
    }

    class MyViewHolder{
        private TextView name;
        private TextView address;
        private TextView distance;
        private ImageView pic;
        private ImageView phone;
    }
}
