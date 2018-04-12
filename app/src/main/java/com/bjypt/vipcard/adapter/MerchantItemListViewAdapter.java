package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.NewMerchantListBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinia.orderlang.utils.StringUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wanglei on 2017/8/30.
 */

public class MerchantItemListViewAdapter extends BaseAdapter {
    private Context context;
    private List<NewMerchantListBean> myList;
    private LayoutInflater inflater;


    public MerchantItemListViewAdapter(List<NewMerchantListBean> myList, Context context) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int i) {
        return myList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_merchant_listview, null);
            viewHolder = new ViewHolder();
            viewHolder.photo_merchant_item = (ImageView) view.findViewById(R.id.photo_merchant_item);
            viewHolder.name_merchant_item = (TextView) view.findViewById(R.id.name_merchant_item);
            viewHolder.quan_merchant_item = (TextView) view.findViewById(R.id.quan_merchant_item);
            viewHolder.discount_merchant_item = (TextView) view.findViewById(R.id.discount_merchant_item);
            viewHolder.zhe_img_merchant = (ImageView) view.findViewById(R.id.zhe_img_merchant);
            viewHolder.quan_img_merchant = (ImageView) view.findViewById(R.id.quan_img_merchant);
            viewHolder.lian_img_merchant = (ImageView) view.findViewById(R.id.lian_img_merchant);
            viewHolder.address_merchant_item = (TextView) view.findViewById(R.id.address_merchant_item);
            viewHolder.linear_zhe = (LinearLayout) view.findViewById(R.id.linear_zhe);
            viewHolder.linear_dikou = (LinearLayout) view.findViewById(R.id.linear_dikou);
            viewHolder.tv_jianjie = (TextView) view.findViewById(R.id.tv_jianjie);
            viewHolder.distance_merchant_item = (TextView) view.findViewById(R.id.distance_merchant_item);
            viewHolder.address_merchant_item = (TextView) view.findViewById(R.id.address_merchant_item);
            viewHolder.jian_merchant_item = (TextView) view.findViewById(R.id.jian_merchant_item);
            viewHolder.fan_merchant_item = (TextView) view.findViewById(R.id.fan_merchant_item);
            viewHolder.linear_fan = (LinearLayout) view.findViewById(R.id.linear_fan);
            viewHolder.linear_jian = (LinearLayout) view.findViewById(R.id.linear_jian);
            viewHolder.imageView5 = (ImageView) view.findViewById(R.id.imageView5);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //减字一行，包括两个字段：firstConsume：首单立减 ，  consumeReduction 消费立减
        if ( (null == myList.get(i).getFirstConsume() || ("").equals(myList.get(i).getFirstConsume())) &&(null ==myList.get(i).getConsumeReduction()||("").equals(myList.get(i).getConsumeReduction()))) {
            viewHolder.linear_jian.setVisibility(View.GONE);
        } else if ((null == myList.get(i).getFirstConsume() || ("").equals(myList.get(i).getFirstConsume())) &&(null !=myList.get(i).getConsumeReduction()||!("").equals(myList.get(i).getConsumeReduction()))){
            viewHolder.linear_jian.setVisibility(View.VISIBLE);
            String string =myList.get(i).getConsumeReduction();
            String regEx = "\\d{1,}[.]?\\d{0,}\\%";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(string);
            SpannableString spannableString = new SpannableString(string);
            if (m.find()) {
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#f1706b")),
                        m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            viewHolder.jian_merchant_item.setText( spannableString);
        } else if ((null != myList.get(i).getFirstConsume() || !("").equals(myList.get(i).getFirstConsume())) &&(null ==myList.get(i).getConsumeReduction()||("").equals(myList.get(i).getConsumeReduction()))){
            viewHolder.linear_jian.setVisibility(View.VISIBLE);
            viewHolder.jian_merchant_item.setText(myList.get(i).getFirstConsume());
        }else {
            viewHolder.linear_jian.setVisibility(View.VISIBLE);
            String string = myList.get(i).getFirstConsume() + "," + myList.get(i).getConsumeReduction();
            String regEx = "\\d[.]?\\d{0,}\\%";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(string);
            SpannableString spannableString = new SpannableString(string);
            if (m.find()) {
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#f1706b")),
                        m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            viewHolder.jian_merchant_item.setText( spannableString);
        }

        //消费立返积分
        if (null == myList.get(i).getVirtualActivity() || ("").equals(myList.get(i).getVirtualActivity())) {
            viewHolder.linear_fan.setVisibility(View.GONE);
        } else {
            viewHolder.linear_fan.setVisibility(View.VISIBLE);
            viewHolder.fan_merchant_item.setText(myList.get(i).getVirtualActivity());
        }

        if ("".equals(myList.get(i).getLogo())) {
            viewHolder.photo_merchant_item.setImageResource(R.drawable.default_dianpu);
        } else {
            ImageLoader.getInstance().displayImage(Config.web.picUrl + myList.get(i).getLogo(), viewHolder.photo_merchant_item, AppConfig.DEFAULT_IMG_MERCHANT_BG);
        }

        viewHolder.distance_merchant_item.setText(myList.get(i).getDistance());
        viewHolder.name_merchant_item.setText(myList.get(i).getMuname());
        viewHolder.address_merchant_item.setText(myList.get(i).getAddress());

        float discount = StringUtil.stringToFloat(myList.get(i).getDiscount());

        if (discount > 0 && discount < 100) {
            viewHolder.linear_zhe.setVisibility(View.VISIBLE);
            viewHolder.zhe_img_merchant.setVisibility(View.VISIBLE);
            viewHolder.discount_merchant_item.setText("买单立享" + (discount / 10) + "折优惠");
        } else {
            viewHolder.linear_zhe.setVisibility(View.GONE);
            viewHolder.zhe_img_merchant.setVisibility(View.GONE);
        }

        if (!(discount > 0) && (null == myList.get(i).getIntegral() || ("").equals(myList.get(i).getIntegral()))) {
            viewHolder.tv_jianjie.setVisibility(View.VISIBLE);
            viewHolder.tv_jianjie.setText(myList.get(i).getMerdesc());
        } else {
            viewHolder.tv_jianjie.setVisibility(View.GONE);
        }

        if (null == myList.get(i).getIntegral() || ("").equals(myList.get(i).getIntegral())) {
            viewHolder.linear_dikou.setVisibility(View.GONE);
            viewHolder.quan_img_merchant.setVisibility(View.GONE);
        } else {
            viewHolder.linear_dikou.setVisibility(View.VISIBLE);
            viewHolder.quan_img_merchant.setVisibility(View.VISIBLE);
            viewHolder.quan_merchant_item.setText(myList.get(i).getIntegral());
        }

        if (myList.get(i).getLinkage_pkdealer() == null) {
            viewHolder.lian_img_merchant.setVisibility(View.GONE);
        } else {
            viewHolder.lian_img_merchant.setVisibility(View.VISIBLE);
        }
        return view;
    }

    class ViewHolder {
        ImageView photo_merchant_item;     //店铺图片
        TextView name_merchant_item;      //店铺名字

        TextView quan_merchant_item;       //优惠券内容
        TextView discount_merchant_item;  //折扣内容

        ImageView zhe_img_merchant;       //折字图标
        ImageView quan_img_merchant;      //券字图标
        ImageView lian_img_merchant;      //连字图标
        TextView address_merchant_item;   //地址
        TextView distance_merchant_item;   //距离

        TextView tv_jianjie;                    //描述

        LinearLayout linear_zhe;               //折扣券
        LinearLayout linear_dikou;            //抵扣券

        TextView jian_merchant_item;         //减
        LinearLayout linear_jian;             //减
        TextView fan_merchant_item;          //返
        LinearLayout linear_fan;              //返
        ImageView imageView5;

    }
}
