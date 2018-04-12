package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.bjypt.vipcard.model.CitizenCardMerchantData;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinia.orderlang.utils.StringUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/8/11.
 */

public class OffineBusinessItemAdapter extends BaseAdapter {

    private Context context;
    private List<CitizenCardMerchantData> list;

    public OffineBusinessItemAdapter(Context context, List<CitizenCardMerchantData> list) {
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
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_mercahntlist, viewGroup, false);

            holder.photo_merchant_item = (ImageView) view.findViewById(R.id.photo_merchant_item);
            holder.name_merchant_item = (TextView) view.findViewById(R.id.name_merchant_item);
            holder.address_merchant_item = (TextView) view.findViewById(R.id.address_merchant_item);
            holder.distance_merchant_item = (TextView) view.findViewById(R.id.distance_merchant_item);
            holder.address_merchant_item = (TextView) view.findViewById(R.id.address_merchant_item);
            holder.jian_merchant_item = (TextView) view.findViewById(R.id.jian_merchant_item);
            holder.fan_merchant_item = (TextView) view.findViewById(R.id.fan_merchant_item);
            holder.linear_fan = (LinearLayout) view.findViewById(R.id.linear_fan);
            holder.linear_jian = (LinearLayout) view.findViewById(R.id.linear_jian);
            holder.imageView5 = (ImageView) view.findViewById(R.id.imageView5);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //商家图片
        ImageLoader.getInstance().displayImage(Config.web.picUrl + list.get(i).getLogo(), holder.photo_merchant_item, AppConfig.DEFAULT_IMG_MERCHANT_BG);

        //商家名称
        holder.name_merchant_item.setText(list.get(i).getMuname());

        //商家地址
        holder.address_merchant_item.setText(list.get(i).getAddress());

        //距离
        holder.distance_merchant_item.setText(list.get(i).getDistance());

        //减字一行，包括两个字段：firstConsume：首单立减 ，  consumeReduction 消费立减
        if ( (null == list.get(i).getFirstConsume() || ("").equals(list.get(i).getFirstConsume())) &&(null ==list.get(i).getConsumeReduction()||("").equals(list.get(i).getConsumeReduction()))) {
            holder.linear_jian.setVisibility(View.GONE);
        } else if ((null == list.get(i).getFirstConsume() || ("").equals(list.get(i).getFirstConsume())) &&(null !=list.get(i).getConsumeReduction()||!("").equals(list.get(i).getConsumeReduction()))){
            holder.linear_jian.setVisibility(View.VISIBLE);
            String string =list.get(i).getConsumeReduction();
            String regEx = "\\d{1,}[.]?\\d{0,}\\%";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(string);
            SpannableString spannableString = new SpannableString(string);
            if (m.find()) {
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#f1706b")),
                        m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            holder.jian_merchant_item.setText( spannableString);
        } else if ((null != list.get(i).getFirstConsume() || !("").equals(list.get(i).getFirstConsume())) &&(null ==list.get(i).getConsumeReduction()||("").equals(list.get(i).getConsumeReduction()))){
            holder.linear_jian.setVisibility(View.VISIBLE);
            holder.jian_merchant_item.setText(list.get(i).getFirstConsume());
        }else {
            holder.linear_jian.setVisibility(View.VISIBLE);
            String string = list.get(i).getFirstConsume() + "," + list.get(i).getConsumeReduction();
            String regEx = "\\d[.]?\\d{0,}\\%";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(string);
            SpannableString spannableString = new SpannableString(string);
            if (m.find()) {
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#ef4234")),
                        m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            holder.jian_merchant_item.setText( spannableString);
        }

        //消费立返积分
        if (null == list.get(i).getVirtualActivity() || ("").equals(list.get(i).getVirtualActivity())) {
            holder.linear_fan.setVisibility(View.GONE);
        } else {
            holder.linear_fan.setVisibility(View.VISIBLE);
            holder.fan_merchant_item.setText(list.get(i).getVirtualActivity());
        }

        return view;
    }

    class ViewHolder {
        ImageView photo_merchant_item;     //店铺图片
        TextView name_merchant_item;      //店铺名字
        TextView address_merchant_item;   //地址
        TextView distance_merchant_item;   //距离
        TextView jian_merchant_item;         //减
        LinearLayout linear_jian;             //减
        TextView fan_merchant_item;          //返
        LinearLayout linear_fan;              //返
        ImageView imageView5;
    }
}
