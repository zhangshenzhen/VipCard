package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class MerchantFragmentAdapter extends BaseAdapter {
    private Context context;
    private List<MerchantListBean> list;

    public MerchantFragmentAdapter(Context context, List<MerchantListBean> list) {
        this.context = context;
        this.list = list;
        Wethod.configImageLoader(context);

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
        ViewHolder holder;
//        if (null==convertView){
        holder = new ViewHolder();
        convertView = View.inflate(context, R.layout.merchant_category_listview_item, null);
        holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        //holder.tv_vip = (TextView)convertView.findViewById(R.id.tv_vip);
        holder.tv_youhui = (TextView) convertView.findViewById(R.id.tv_youhui);
        holder.tv_jianjie = (TextView) convertView.findViewById(R.id.tv_jianjie);

        holder.tv_zhekou = (TextView) convertView.findViewById(R.id.tv_zhekou);
        holder.tv_yuanjia = (TextView) convertView.findViewById(R.id.tv_yuanjia);
        holder.tv_juli = (TextView) convertView.findViewById(R.id.tv_juli);
//        holder.tv_pingjia = (TextView) convertView.findViewById(R.id.tv_pingjia);
//        holder.star_1 = (ImageView) convertView.findViewById(R.id.stars_1);
//        holder.star_2 = (ImageView) convertView.findViewById(R.id.stars_2);
//        holder.star_3 = (ImageView) convertView.findViewById(R.id.stars_3);
//        holder.star_4 = (ImageView) convertView.findViewById(R.id.stars_4);
//        holder.star_5 = (ImageView) convertView.findViewById(R.id.stars_5);
        holder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_merchant_photo);
        holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        holder.tv_discount = (TextView) convertView.findViewById(R.id.tv_discount);
        holder.tv_number_zhekou = (TextView) convertView.findViewById(R.id.tv_number_zhekou);
        holder.ll_dikou = (LinearLayout) convertView.findViewById(R.id.ll_dikou);
//            convertView.setTag(holder);
//        }else{
//            holder = (ViewHolder) convertView.getTag();
//        }


        Log.e("TYZ", "list.get(position).getRechargeWelfare():" + list.get(position).getCouponWelfare());
        if (list.get(position).getCouponWelfare().equals("") || list.get(position).getCouponWelfare() == null) {
            holder.tv_youhui.setVisibility(View.GONE);
        } else {
            holder.tv_youhui.setText(list.get(position).getCouponWelfare());
        }

        if (list.get(position).getMaxDiscount().equals("") || list.get(position).getMaxDiscount() == null) {
            holder.tv_discount.setVisibility(View.GONE);
        } else {
            holder.tv_discount.setText(list.get(position).getMaxDiscount());
        }


        if (list.get(position).getRechargeWelfare().equals("") || list.get(position).getRechargeWelfare() == null) {
            // holder.tv_vip.setVisibility(View.GONE);
        } else {
            // holder.tv_vip.setText(list.get(position).getRechargeWelfare());
        }


        holder.tv_name.setText(list.get(position).getMuname());
//        holder.tv_pingjia.setText(list.get(position).getJudgeAllNum() + "");

        holder.tv_jianjie.setText(list.get(position).getMerdesc());
        holder.tv_juli.setText(list.get(position).getDistance());

        holder.tv_zhekou.setText(list.get(position).getDiscount());
        holder.tv_number_zhekou.setText(list.get(position).getIntegral());
        if (list.get(position).equals("")) {
            holder.ll_dikou.setVisibility(View.GONE);
        } else {
            holder.ll_dikou.setVisibility(View.VISIBLE);
        }
        holder.tv_yuanjia.setText(list.get(position).getMemberCount());
        if ("".equals(list.get(position).getLogo())) {
            holder.iv_photo.setImageResource(R.drawable.ic_launcher);
        } else {
            ImageLoader.getInstance().displayImage(Config.web.picUrl + list.get(position).getLogo(), holder.iv_photo, AppConfig.DEFAULT_IMG_MERCHANT_BG);
        }
//        if (Integer.parseInt(list.get(position).getStartLevel()) >= 5) {
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_true);
//            holder.star_3.setImageResource(R.drawable.stars_true);
//            holder.star_4.setImageResource(R.drawable.stars_true);
//            holder.star_5.setImageResource(R.drawable.stars_true);
//        } else if (Integer.parseInt(list.get(position).getStartLevel()) >= 4 && Integer.parseInt(list.get(position).getStartLevel()) < 5) {
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_true);
//            holder.star_3.setImageResource(R.drawable.stars_true);
//            holder.star_4.setImageResource(R.drawable.stars_true);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        } else if (Integer.parseInt(list.get(position).getStartLevel()) >= 3 && Integer.parseInt(list.get(position).getStartLevel()) < 4) {
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_true);
//            holder.star_3.setImageResource(R.drawable.stars_true);
//            holder.star_4.setImageResource(R.drawable.stars_flase);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        } else if (Integer.parseInt(list.get(position).getStartLevel()) >= 2 && Integer.parseInt(list.get(position).getStartLevel()) < 3) {
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_true);
//            holder.star_3.setImageResource(R.drawable.stars_flase);
//            holder.star_4.setImageResource(R.drawable.stars_flase);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        } else if (Integer.parseInt(list.get(position).getStartLevel()) >= 1 && Integer.parseInt(list.get(position).getStartLevel()) < 2) {
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_flase);
//            holder.star_3.setImageResource(R.drawable.stars_flase);
//            holder.star_4.setImageResource(R.drawable.stars_flase);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        } else if (Integer.parseInt(list.get(position).getStartLevel()) < 1) {
//            holder.star_1.setImageResource(R.drawable.stars_flase);
//            holder.star_2.setImageResource(R.drawable.stars_flase);
//            holder.star_3.setImageResource(R.drawable.stars_flase);
//            holder.star_4.setImageResource(R.drawable.stars_flase);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_name;//商家名称
        TextView tv_jianjie;//商家简介
        //TextView tv_vip;//会员级别
        TextView tv_youhui;//优惠
        ImageView star_1, star_2, star_3, star_4, star_5, iv_photo;
        TextView tv_pingjia;//有多少评价
        TextView tv_yuanjia;//原价
        TextView tv_zhekou;//打折
        TextView tv_juli;//距离
        TextView tv_discount;
        TextView tv_number_zhekou;
        LinearLayout ll_dikou;


    }

    public void add(List<MerchantListBean> l) {
        if (list == null) {
            list = l;
        } else {
            list.addAll(l);
        }

    }
    /*public static int change(String str){
        return Integer.parseInt(str);
    }*/
}
