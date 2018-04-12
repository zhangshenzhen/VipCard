package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.MerchantListBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinia.orderlang.utils.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class MyExpandListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<MerchantListBean> list;
    private int sum = 0;
    private int falg = 10;
    Handler handler;

    public MyExpandListViewAdapter(Context context, List<MerchantListBean> list) {
        this.context = context;
        this.list = list;
        Wethod.configImageLoader(context);
    }

    public MyExpandListViewAdapter(Context context, List<MerchantListBean> list, Handler handler) {
        this.context = context;
        this.list = list;
        Wethod.configImageLoader(context);
        this.handler = handler;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getContentBeans().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getContentBeans().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        Log.e("lifetest", "3");
        holder = new ViewHolder();
//            convertView = View.inflate(context, R.layout.merchant_category_listview_item,false);
        convertView = LayoutInflater.from(context).inflate(R.layout.merchant_category_listview_item, parent, false);
        holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        holder.tv_adress_adapter = (TextView) convertView.findViewById(R.id.tv_adress_adapter);
        holder.tv_vip = (ImageView) convertView.findViewById(R.id.tv_vip);
        holder.tv_youhui = (TextView) convertView.findViewById(R.id.tv_youhui);
        holder.tv_jianjie = (TextView) convertView.findViewById(R.id.tv_jianjie);

        holder.tv_zhekou = (TextView) convertView.findViewById(R.id.tv_zhekou);
        holder.tv_yuanjia = (TextView) convertView.findViewById(R.id.tv_yuanjia);
        holder.tv_redpacket = (TextView) convertView.findViewById(R.id.tv_redpacket);
        holder.merchant_tehui = (ImageView) convertView.findViewById(R.id.merchant_tehui);
        holder.tv_juli = (TextView) convertView.findViewById(R.id.tv_juli);
//        holder.tv_pingjia = (TextView) convertView.findViewById(R.id.tv_pingjia);
//        holder.star_1 = (ImageView) convertView.findViewById(R.id.stars_1);
//        holder.star_2 = (ImageView) convertView.findViewById(R.id.stars_2);
//        holder.star_3 = (ImageView) convertView.findViewById(R.id.stars_3);
//        holder.star_4 = (ImageView) convertView.findViewById(R.id.stars_4);
//        holder.star_5 = (ImageView) convertView.findViewById(R.id.stars_5);
        holder.iv_red_package_item = (ImageView) convertView.findViewById(R.id.iv_red_package_item);
        holder.iv_item_pic = (ImageView) convertView.findViewById(R.id.iv_item_pic);
        holder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_merchant_photo);
        holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        holder.tv_discount = (TextView) convertView.findViewById(R.id.tv_discount);
        holder.tv_sum_activitys = (TextView) convertView.findViewById(R.id.tv_sum_activitys);
        holder.ll_show_child = (LinearLayout) convertView.findViewById(R.id.ll_show_child);
        holder.view_item_one = convertView.findViewById(R.id.view_item_one);
        holder.view_item_two = convertView.findViewById(R.id.view_item_two);
        convertView.setTag(holder);

        holder.tv_name.setText(list.get(groupPosition).getMuname());
        if (list.get(groupPosition).getAddress() != null && !list.get(groupPosition).getAddress().equals("")) {
            holder.tv_adress_adapter.setText(list.get(groupPosition).getAddress());
        }

//        holder.tv_pingjia.setText(list.get(groupPosition).getJudgeAllNum() + "");
        holder.tv_jianjie.setText(list.get(groupPosition).getMerdesc());
        holder.tv_juli.setText(list.get(groupPosition).getDistance());

        Log.e("llll", "list.get(groupPosition).getRechargeActivity():" + list.get(groupPosition).getRechargeActivity());
//        if (list.get(groupPosition).getRechargeActivity() != null && list.get(groupPosition).getRechargeActivity().equals("1")) {
//            holder.merchant_tehui.setVisibility(View.VISIBLE);
//        } else {
//            holder.merchant_tehui.setVisibility(View.GONE);
//        }

//        if (list.get(groupPosition).getSpecialPrice() != null && !list.get(groupPosition).getSpecialPrice().equals("") && list.get(groupPosition).getSpecialPrice().equals("1")) {
//            holder.merchant_tehui.setVisibility(View.VISIBLE);
//        } else {
//            holder.merchant_tehui.setVisibility(View.GONE);
//        }

        if (list.get(groupPosition).getIsfirst() != null && !list.get(groupPosition).getIsfirst().equals("") && list.get(groupPosition).getIsfirst().equals("1")) {
            holder.tv_vip.setVisibility(View.VISIBLE);
        }
        if (isExpanded) {
            holder.view_item_one.setVisibility(View.GONE);
            holder.view_item_two.setVisibility(View.VISIBLE);
        } else {
            holder.view_item_one.setVisibility(View.VISIBLE);
            holder.view_item_two.setVisibility(View.GONE);
        }
        if ("".equals(list.get(groupPosition).getConsumeRedPackage())) {
            holder.iv_item_pic.setVisibility(View.GONE);
            holder.tv_redpacket.setVisibility(View.GONE);
            holder.tv_redpacket.setText("");

        } else {
            holder.iv_item_pic.setVisibility(View.VISIBLE);
            holder.iv_item_pic.setImageResource(R.mipmap.sui_merchant);
            holder.tv_redpacket.setVisibility(View.VISIBLE);
            if (list.get(groupPosition).getConsumeRedPackage().contains("红包")) {
                holder.tv_redpacket.setText(list.get(groupPosition).getConsumeRedPackage().substring(0,
                        list.get(groupPosition).getConsumeRedPackage().lastIndexOf("红包")));
                holder.iv_red_package_item.setVisibility(View.VISIBLE);
            } else {
                holder.iv_red_package_item.setVisibility(View.GONE);
                holder.tv_redpacket.setText(list.get(groupPosition).getConsumeRedPackage());
            }

        }

//        holder.tv_zhekou.setText(list.get(groupPosition).getMemberCount());
        if ("".equals(list.get(groupPosition).getLogo())) {
            holder.iv_photo.setImageResource(R.drawable.ic_launcher);
        } else {
            ImageLoader.getInstance().displayImage(Config.web.picUrl + list.get(groupPosition).getLogo(), holder.iv_photo, AppConfig.DEFAULT_IMG_MERCHANT_BG);
        }
        float startLevel = StringUtil.getFloat(list.get(groupPosition).getStartLevel());
//        if (startLevel >= 5) {
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_true);
//            holder.star_3.setImageResource(R.drawable.stars_true);
//            holder.star_4.setImageResource(R.drawable.stars_true);
//            holder.star_5.setImageResource(R.drawable.stars_true);
//        } else if (startLevel >= 4 && startLevel < 5) {
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_true);
//            holder.star_3.setImageResource(R.drawable.stars_true);
//            holder.star_4.setImageResource(R.drawable.stars_true);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        } else if (startLevel >= 3 && startLevel < 4) {
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_true);
//            holder.star_3.setImageResource(R.drawable.stars_true);
//            holder.star_4.setImageResource(R.drawable.stars_flase);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        } else if (startLevel >= 2 && startLevel < 3) {
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_true);
//            holder.star_3.setImageResource(R.drawable.stars_flase);
//            holder.star_4.setImageResource(R.drawable.stars_flase);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        } else if (startLevel >= 1 && startLevel < 2) {
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_flase);
//            holder.star_3.setImageResource(R.drawable.stars_flase);
//            holder.star_4.setImageResource(R.drawable.stars_flase);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        } else if (startLevel < 1) {
//            holder.star_1.setImageResource(R.drawable.stars_flase);
//            holder.star_2.setImageResource(R.drawable.stars_flase);
//            holder.star_3.setImageResource(R.drawable.stars_flase);
//            holder.star_4.setImageResource(R.drawable.stars_flase);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        }
        holder.tv_sum_activitys.setText(list.get(groupPosition).getActivitysSum() + "个活动");
        holder.ll_show_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sum++;
                String value = sum + "a" + groupPosition;
                Message msg = handler.obtainMessage(110, value);
                handler.sendMessage(msg);
            }
        });
        return convertView;
    }

    @Override
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
        if (list.get(groupPosition).getContentBeans().get(childPosition).getContent() == null || "".equals(list.get(groupPosition).getContentBeans().get(childPosition).getContent())) {
            holder.iv_content_pic.setVisibility(View.GONE);
            holder.tv_content.setVisibility(View.GONE);
            holder.ll_conent_item.setVisibility(View.GONE);
            holder.iv_red_packager_merchant.setVisibility(View.GONE);
            holder.tv_content.setText("");
        } else {
            holder.ll_conent_item.setVisibility(View.VISIBLE);
            if (list.get(groupPosition).getContentBeans().get(childPosition).getContent().
                    contains("红包")) {
                holder.tv_content.setText(list.get(groupPosition).getContentBeans().get(childPosition).getContent().substring(0, list.get(groupPosition).getContentBeans().get(childPosition).getContent().lastIndexOf("红包")));
                holder.iv_red_packager_merchant.setVisibility(View.VISIBLE);
            } else {
                holder.iv_red_packager_merchant.setVisibility(View.GONE);
                holder.tv_content.setText(list.get(groupPosition).getContentBeans().get(childPosition).getContent());
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
        if (list.get(groupPosition).getFlag() == childPosition) {
            holder.ll_conent_item.setVisibility(View.GONE);
            holder.iv_content_pic.setVisibility(View.GONE);
            holder.tv_content.setVisibility(View.GONE);
            holder.iv_red_packager_merchant.setVisibility(View.GONE);
        }

        if (list.get(groupPosition).getContentBeans().size() - 1 == childPosition) {
            holder.view_content_item.setVisibility(View.VISIBLE);
        } else {
            holder.view_content_item.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
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
        TextView tv_name;//商家名称
        TextView tv_adress_adapter;//商家名称
        TextView tv_jianjie;//商家简介
        ImageView tv_vip;//第一商家
        TextView tv_youhui;//优惠
        ImageView star_1, star_2, star_3, star_4, star_5, iv_photo, iv_item_pic, iv_red_package_item;
        TextView tv_pingjia;//有多少评价
        TextView tv_yuanjia;//原价
        TextView tv_zhekou;//打折
        TextView tv_juli;//距离
        TextView tv_discount;
        TextView tv_redpacket;
        TextView tv_sum_activitys;
        LinearLayout ll_show_child;
        View view_item_one;
        View view_item_two;
        ImageView merchant_tehui;


    }

    public void add(List<MerchantListBean> l) {
        if (list == null) {
            list = l;
        } else {
            list.addAll(l);
        }

    }

}
