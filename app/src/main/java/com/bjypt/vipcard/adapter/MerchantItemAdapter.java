package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.MerchantListBean;
import com.bjypt.vipcard.model.NewMerchantListBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinia.orderlang.utils.StringUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/26.
 */

public class MerchantItemAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<NewMerchantListBean> myList;
    private LayoutInflater inflater;
    private Handler handler;


    public MerchantItemAdapter(List<NewMerchantListBean> myList, Context context, Handler handler) {
        this.myList = myList;
        this.context = context;
        this.handler = handler;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return myList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return myList.get(i).getContentBeans().size();
    }

    @Override
    public Object getGroup(int i) {
        return myList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return myList.get(i).getContentBeans().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_mercahntlist, null);
            viewHolder = new ViewHolder();
            viewHolder.photo_merchant_item = (ImageView) view.findViewById(R.id.photo_merchant_item);
            viewHolder.name_merchant_item = (TextView) view.findViewById(R.id.name_merchant_item);
            viewHolder.address_merchant_item = (TextView) view.findViewById(R.id.address_merchant_item);
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

        return view;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewTwo holder;

        holder = new ViewTwo();
        convertView = View.inflate(context, R.layout.layout_content_item, null);
        holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content_expand);
        holder.iv_content_pic = (ImageView) convertView.findViewById(R.id.iv_content_pic);
        holder.iv_red_packager_merchant = (ImageView) convertView.findViewById(R.id.iv_red_packager_merchant);
        holder.ll_conent_item = (LinearLayout) convertView.findViewById(R.id.ll_conent_item);
        holder.view_content_item = convertView.findViewById(R.id.view_content_item);
        // convertView.setTag(holder);
        if (myList.get(groupPosition).getContentBeans().get(childPosition).getContent() == null || "".equals(myList.get(groupPosition).getContentBeans().get(childPosition).getContent())) {
            holder.iv_content_pic.setVisibility(View.GONE);
            holder.tv_content.setVisibility(View.GONE);
            holder.ll_conent_item.setVisibility(View.GONE);
            holder.iv_red_packager_merchant.setVisibility(View.GONE);
            holder.tv_content.setText("");
        } else {
            holder.ll_conent_item.setVisibility(View.VISIBLE);
            if (myList.get(groupPosition).getContentBeans().get(childPosition).getContent().
                    contains("红包")) {
                holder.tv_content.setText(myList.get(groupPosition).getContentBeans().get(childPosition).getContent().substring(0, myList.get(groupPosition).getContentBeans().get(childPosition).getContent().lastIndexOf("红包")));
                holder.iv_red_packager_merchant.setVisibility(View.VISIBLE);
            } else {
                holder.iv_red_packager_merchant.setVisibility(View.GONE);
                holder.tv_content.setText(myList.get(groupPosition).getContentBeans().get(childPosition).getContent());
            }
            holder.iv_content_pic.setVisibility(View.VISIBLE);
            holder.tv_content.setVisibility(View.VISIBLE);
            if (childPosition == 0) {
                holder.iv_content_pic.setImageResource(R.mipmap.jian_merchant);
            } else if (childPosition == 1) {
                holder.iv_content_pic.setImageResource(R.mipmap.chong_merchant);
            } else if (childPosition == 2) {
                holder.iv_content_pic.setImageResource(R.mipmap.chong_merchant);
            } else if (childPosition == 3) {
                holder.iv_content_pic.setImageResource(R.mipmap.shou_merchant);
            } else if (childPosition == 4) {
                holder.iv_content_pic.setImageResource(R.mipmap.jian_merchant);
            } else {
                holder.iv_content_pic.setImageResource(R.mipmap.jifen_merchant);
            }

        }
        if (myList.get(groupPosition).getFlag() == childPosition) {
            holder.ll_conent_item.setVisibility(View.GONE);
            holder.iv_content_pic.setVisibility(View.GONE);
            holder.tv_content.setVisibility(View.GONE);
            holder.iv_red_packager_merchant.setVisibility(View.GONE);
        }

        if (myList.get(groupPosition).getContentBeans().size() - 1 == childPosition) {
            holder.view_content_item.setVisibility(View.VISIBLE);
        } else {
            holder.view_content_item.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    class ViewTwo {
        TextView tv_content;
        View view_content_item;
        ImageView iv_content_pic;
        ImageView iv_red_packager_merchant;
        LinearLayout ll_conent_item;
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

    public void add(List<NewMerchantListBean> l) {
        if (myList == null) {
            myList = l;
        } else {
            myList.addAll(l);
        }

    }
}


