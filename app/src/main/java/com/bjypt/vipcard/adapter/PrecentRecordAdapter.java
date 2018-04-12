package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.PrecentRecordBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class PrecentRecordAdapter extends BaseAdapter {
    private List<PrecentRecordBean> lists;
    private Context context;

    public PrecentRecordAdapter(List<PrecentRecordBean> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.layout_precent_record_item,null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_precent_name);
            holder.tv_zhuangtai = (TextView) convertView.findViewById(R.id.tv_zhuangtai);
            holder.tv_rate = (TextView) convertView.findViewById(R.id.tv_rate);
            holder.tv_transcformount = (TextView) convertView.findViewById(R.id.tv_transcformount);
            holder.price = (TextView) convertView.findViewById(R.id.tv_precent_price);
            holder.time = (TextView) convertView.findViewById(R.id.tv_precent_time);
            holder.phone = (TextView) convertView.findViewById(R.id.tv_precent_phone);
            holder.bankname = (TextView) convertView.findViewById(R.id.tv_precent_bankName);
            holder.bankno = (TextView) convertView.findViewById(R.id.tv_precent_bankNo);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.name.setText(lists.get(position).getBankusername());
        holder.price.setText("提现金额￥"+lists.get(position).getAmount());
        if (lists.get(position).getApply_time()!=null&&!"".equals(lists.get(position).getApply_time())){
            holder.time.setText(lists.get(position).getApply_time());
        }
        holder.tv_rate.setText("费率："+lists.get(position).getRate());
        holder.tv_transcformount.setText("到账金额￥"+lists.get(position).getResultamount());
        holder.bankname.setText(lists.get(position).getBankname());
        holder.tv_zhuangtai.setText(lists.get(position).getStatus_desc());
        holder.bankno.setText(lists.get(position).getBankcardno());
        holder.phone.setText(lists.get(position).getBankuserphone());
        return convertView;
    }
    class ViewHolder{
        TextView name;
        TextView phone;
        TextView bankname;
        TextView bankno;
        TextView price;
        TextView time;
        TextView tv_zhuangtai;
        TextView tv_rate;
        TextView tv_transcformount;

    }
    public static String getUserTime(String value){
        if (value==null||"".equals(value)){
            return "";
        }else {
            Date date = new Date();
            date.setTime(Long.getLong(value));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            String dateString = formatter.format(date);
            return dateString;
        }
    }
    public void add(List<PrecentRecordBean> l){
        if (lists==null){
            lists = l;
        }else {
            lists.addAll(l);
        }

    }
}
