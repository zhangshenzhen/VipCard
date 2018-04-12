package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.CoupnBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by liyunte on 2017/2/10.
 */

public class CoupnAdapter extends BaseAdapter {
    private Context context;
    private List<CoupnBean> list;

    public CoupnAdapter(Context context, List<CoupnBean> list) {
        this.context = context;
        this.list = list;
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
        ViewHolder holder = null;
        if (convertView==null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.layout_coupn_item,null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_coupon_price = (TextView) convertView.findViewById(R.id.tv_coupon_price);
            holder.tv_coupon_endtion = (TextView) convertView.findViewById(R.id.tv_coupon_endtion);
            holder.tv_coupon_xianzhi = (TextView) convertView.findViewById(R.id.tv_coupon_xianzhi);
            // holder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_coupon_photo);
            holder.iv_out_time = (ImageView) convertView.findViewById(R.id.iv_out_time);
            holder.rl_coupon = (RelativeLayout) convertView.findViewById(R.id.rl_coupon);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        if ("2".equals(list.get(position).getExpirestatus())){
            holder.rl_coupon.setBackgroundResource(R.mipmap.coupon_gray);
            holder.iv_out_time.setVisibility(View.VISIBLE);
        }

        holder.tv_name.setText(list.get(position).getMuname());
        holder.tv_coupon_endtion.setText("结束时间：" + getUserDate(list.get(position).getEndtime()));
        holder.tv_time.setText("开始时间：" + getUserDate(list.get(position).getStarttime()));
        holder.tv_coupon_price.setText("￥"+list.get(position).getWealmoney());
        holder.tv_coupon_xianzhi.setText("满"+list.get(position).getMzmoney()+"可用");

        return convertView;
    }
    class ViewHolder{
        TextView tv_name,tv_time,tv_coupon_price,tv_coupon_xianzhi,tv_coupon_endtion;
        ImageView iv_out_time;
        RelativeLayout rl_coupon;

    }
    public static String getUserDate(String sformat) {
        Date currentTime = new Date();
        currentTime.setTime(Long.parseLong(sformat));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    public void add(List<CoupnBean> l){
        if (list==null){
            list = l;
        }else {
            list.addAll(l);

        }
    }

}
