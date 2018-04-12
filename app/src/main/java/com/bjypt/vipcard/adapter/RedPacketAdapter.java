package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.CoupnBean;
import com.bjypt.vipcard.model.CouponBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/4/1.
 */
public class RedPacketAdapter extends BaseAdapter{
    private Context context;
    private List<CoupnBean> list;

    public RedPacketAdapter(Context context, List<CoupnBean> list) {
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
            convertView = View.inflate(context, R.layout.layout_lv_redpacket_item,null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_merchantName);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_redPacketPrice);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_limitDate);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.tv_name.setText(list.get(position).getMuname());
        holder.tv_price.setText("红包"+list.get(position).getWealmoney()+"元");
        holder.tv_time.setText(getUserDate(list.get(position).getEndtime()));
        return convertView;
    }
    class ViewHolder{
        TextView tv_name,tv_price,tv_time;
    }
    public static String getUserDate(String sformat) {
        Date currentTime = new Date();
        currentTime.setTime(Long.parseLong(sformat));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    public void add(List<CoupnBean> l){
        if (list == null){
            list = l;
        }else {
            list.addAll(l);
        }
    }
//layout_lv_redpacket_item

}
