package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.MerchantListBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/3/28.
 */
public class MerchantListAdapter extends MyBaseAdapter<MerchantListBean> {
    public MerchantListAdapter(Context mContext) {
        super(mContext);
        Wethod.configImageLoader(mContext);
    }

    public MerchantListAdapter(List<MerchantListBean> list, Context mContext) {
        super(list, mContext);
        Wethod.configImageLoader(mContext);
    }

    @Override
    public View initView(int position, View convertView, ViewGroup parent) {

        Log.i("aaa","welcome="+position);
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.merchant_category_listview_item, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_youhui = (TextView) convertView.findViewById(R.id.tv_youhui);
            holder.tv_jianjie = (TextView) convertView.findViewById(R.id.tv_jianjie);
            holder.tv_zhekou = (TextView) convertView.findViewById(R.id.tv_zhekou);
            holder.tv_yuanjia = (TextView) convertView.findViewById(R.id.tv_yuanjia);
            holder.tv_juli = (TextView) convertView.findViewById(R.id.tv_juli);
//            holder.tv_pingjia = (TextView) convertView.findViewById(R.id.tv_pingjia);
//            holder.star_1 = (ImageView) convertView.findViewById(R.id.stars_1);
//            holder.star_2 = (ImageView) convertView.findViewById(R.id.stars_2);
//            holder.star_3 = (ImageView) convertView.findViewById(R.id.stars_3);
//            holder.star_4 = (ImageView) convertView.findViewById(R.id.stars_4);
//            holder.star_5 = (ImageView) convertView.findViewById(R.id.stars_5);
            holder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_merchant_photo);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder = (ViewHolder) convertView.getTag();
        holder.tv_name.setText(list.get(position).getMuname());
//        holder.tv_pingjia.setText(list.get(position).getPingjia()+"");
//        holder.tv_vip.setText(list.get(position).getVipzuan());
        holder.tv_jianjie.setText(list.get(position).getMerdesc());
        holder.tv_juli.setText(list.get(position).getDistance());
//        holder.tv_youhui.setText(list.get(position).getYouhui());
//        holder.tv_zhekou.setText(list.get(position).getZhekou());
//        holder.tv_yuanjia.setText(list.get(position).getYuanjai() + "");
        if ("".equals(list.get(position).getLogo())) {
            holder.iv_photo.setImageResource(R.drawable.ic_launcher);
        } else {
            ImageLoader.getInstance().displayImage(Config.web.picUrl + list.get(position).getLogo(), holder.iv_photo, AppConfig.DEFAULT_IMG_OPTIONS_AD);
        }
//        if (list.get(position).getPingjia()>=100){
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_true);
//            holder.star_3.setImageResource(R.drawable.stars_true);
//            holder.star_4.setImageResource(R.drawable.stars_true);
//            holder.star_5.setImageResource(R.drawable.stars_true);
//        }else if (list.get(position).getPingjia()>=80&&list.get(position).getPingjia()<100){
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_true);
//            holder.star_3.setImageResource(R.drawable.stars_true);
//            holder.star_4.setImageResource(R.drawable.stars_true);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        }else if (list.get(position).getPingjia()>=60&&list.get(position).getPingjia()<80){
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_true);
//            holder.star_3.setImageResource(R.drawable.stars_true);
//            holder.star_4.setImageResource(R.drawable.stars_flase);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        }else if (list.get(position).getPingjia()>=40&&list.get(position).getPingjia()<60){
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_true);
//            holder.star_3.setImageResource(R.drawable.stars_flase);
//            holder.star_4.setImageResource(R.drawable.stars_flase);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        }else if (list.get(position).getPingjia()>=20&&list.get(position).getPingjia()<40){
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_flase);
//            holder.star_3.setImageResource(R.drawable.stars_flase);
//            holder.star_4.setImageResource(R.drawable.stars_flase);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        }else if (list.get(position).getPingjia()<20){
//        holder.star_1.setImageResource(R.drawable.stars_flase);
//        holder.star_2.setImageResource(R.drawable.stars_flase);
//        holder.star_3.setImageResource(R.drawable.stars_flase);
//        holder.star_4.setImageResource(R.drawable.stars_flase);
//        holder.star_5.setImageResource(R.drawable.stars_flase);
//    }
        return convertView;
    }

    class ViewHolder {
        TextView tv_name;//商家名称
        TextView tv_jianjie;//商家简介
        TextView tv_youhui;//优惠
        ImageView star_1, star_2, star_3, star_4, star_5, iv_photo;
        TextView tv_pingjia;//有多少评价
        TextView tv_yuanjia;//原价
        TextView tv_zhekou;//打折
        TextView tv_juli;//距离


    }

    public void add(List<MerchantListBean> l) {
        if (list == null) {
            list = l;
        } else {
            list.addAll(l);
        }

    }
}
