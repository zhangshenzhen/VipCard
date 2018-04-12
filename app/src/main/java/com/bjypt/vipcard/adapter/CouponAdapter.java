package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.NewSubscribeDishesActivity;
import com.bjypt.vipcard.model.CouponBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/20.
 */
public class CouponAdapter extends BaseAdapter {
    private Context context;
    private List<CouponBean> list;
    private Handler handler;
    //gai  dong
    public Map<String, String> selectList = new HashMap<>();

    public CouponAdapter(Context context, List<CouponBean> list, Handler handler) {
        this.context = context;
        this.list = list;
        this.handler = handler;
    }

    @Override
    public void notifyDataSetChanged() {
        initSelect();
        super.notifyDataSetChanged();
    }

    private void initSelect() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).ischeck()) {
                selectList.put(i + "", list.get(i).getPkorderid());
            }
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.layout_coupon_item, null);
            holder.tv_quan_name = (TextView) convertView.findViewById(R.id.tv_quan_name);
            holder.tv_quan_merhcant_name = (TextView) convertView.findViewById(R.id.tv_quan_merhcant_name);
            holder.tv_quan_relley = (TextView) convertView.findViewById(R.id.tv_quan_relley);
            holder.tv_quan_ruller = (TextView) convertView.findViewById(R.id.tv_quan_ruller);
            holder.tv_quan_buy_date = (TextView) convertView.findViewById(R.id.tv_quan_buy_date);
            holder.tv_quan_date = (TextView) convertView.findViewById(R.id.tv_quan_date);
            holder.iv_coupon_photo = (ImageView) convertView.findViewById(R.id.iv_coupon_photo);
            holder.ly_quan_bg = (RelativeLayout) convertView.findViewById(R.id.ly_quan_bg);
            holder.rela_merchant_detail = (RelativeLayout) convertView.findViewById(R.id.rela_merchant_detail);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();

        if (list.get(position).getFlag() != null && list.get(position).getFlag().equals("1")) {
            holder.ly_quan_bg.setBackgroundResource(R.mipmap.quan_outdata_bg);
            holder.iv_coupon_photo.setVisibility(View.GONE);
            holder.tv_quan_relley.setVisibility(View.VISIBLE);
            holder.tv_quan_relley.setTextColor(Color.parseColor("#B0B0B0"));
            holder.tv_quan_name.setTextColor(Color.parseColor("#B0B0B0"));
            holder.tv_quan_merhcant_name.setTextColor(Color.parseColor("#B0B0B0"));
            holder.tv_quan_ruller.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_quan_ruller.setBackgroundResource(R.drawable.quan_out_data);
            holder.tv_quan_date.setTextColor(Color.parseColor("#B0B0B0"));
            holder.tv_quan_buy_date.setTextColor(Color.parseColor("#B0B0B0"));
            if (list.get(position).getUsestatus().equals("0")) {
                holder.tv_quan_relley.setText("已过期");
            } else {
                holder.tv_quan_relley.setText("已使用");
            }
        } else {
            if (list.get(position).getFlag() != null && list.get(position).getFlag().equals("2")) {
                holder.ly_quan_bg.setBackgroundResource(R.mipmap.quan_on_bg_2);
                holder.rela_merchant_detail.setVisibility(View.VISIBLE);
                holder.rela_merchant_detail.setClickable(true);
                holder.rela_merchant_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, NewSubscribeDishesActivity.class);
                        intent.putExtra("pkmuser", list.get(position).getPkmuser());
                        intent.putExtra("distance", "*");
                        context.startActivity(intent);
                    }
                });
            } else {
                holder.ly_quan_bg.setBackgroundResource(R.mipmap.quan_on_bg);
                holder.rela_merchant_detail.setVisibility(View.GONE);
                holder.rela_merchant_detail.setClickable(false);
            }

            holder.tv_quan_relley.setTextColor(Color.parseColor("#FCBC63"));
            holder.tv_quan_name.setTextColor(Color.parseColor("#FCBC63"));
            holder.tv_quan_merhcant_name.setTextColor(Color.parseColor("#FCBC63"));
            //            holder.tv_quan_ruller.setTextColor(Color.parseColor("#FCBC63"));
            //            holder.tv_quan_ruller.setBackgroundResource(R.drawable.quan_on);
            holder.tv_quan_date.setTextColor(Color.parseColor("#FCBC63"));
            holder.tv_quan_buy_date.setTextColor(Color.parseColor("#FCBC63"));
            holder.tv_quan_relley.setVisibility(View.GONE);
            if (list.get(position).getIsSelect() != null && list.get(position).getIsSelect().equals("Y")) {
                holder.iv_coupon_photo.setVisibility(View.VISIBLE);
                if (!selectList.containsKey(position + "")) {
                    holder.iv_coupon_photo.setImageResource(R.mipmap.quan_on_select_default_bg);
                } else {
                    holder.iv_coupon_photo.setImageResource(R.mipmap.quan_on_select_relley_bg);
                }

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!selectList.containsKey(position + "")) {
                            list.get(position).setIscheck(true);
                            selectList.put(position + "", list.get(position).getPkorderid());
                            Log.e("liyunte", "issect-------" + list.get(position).getPkorderid());
                        } else {
                            selectList.remove(position + "");
                            list.get(position).setIscheck(false);
                            Log.e("liyunte", "isremove------");
                        }

                        Log.e("liyunte", "name==" + selectList.get("" + position));
                        Message message = handler.obtainMessage(2);
                        message.obj = selectList;
                        handler.sendMessage(message);
                        notifyDataSetChanged();

                    }
                });
            } else {
                holder.iv_coupon_photo.setVisibility(View.GONE);
            }
        }
        holder.tv_quan_name.setText(list.get(position).getPayamount() + "抵扣" + list.get(position).getValueamount() + "元");
        holder.tv_quan_merhcant_name.setText(list.get(position).getMuname());
        holder.tv_quan_ruller.setText("仅用于" + list.get(position).getMuname());
        holder.tv_quan_date.setText("有效期:  " + list.get(position).getStartdate() + " 至 " + list.get(position).getEnddate());
        holder.tv_quan_buy_date.setText("购买时间:  " + list.get(position).getPaytime());

        return convertView;
    }

    class ViewHolder {
        TextView tv_quan_name, tv_quan_merhcant_name, tv_quan_ruller, tv_quan_buy_date, tv_quan_date, tv_quan_relley;
        ImageView iv_coupon_photo;
        RelativeLayout ly_quan_bg;
        RelativeLayout rela_merchant_detail;
    }

    public void add(List<CouponBean> l) {
        if (list == null) {
            list = l;
        } else {
            list.addAll(l);

        }
    }

}
