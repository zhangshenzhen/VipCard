package com.bjypt.vipcard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.FinancingTakeNotesListBean;

import java.util.ArrayList;

/**
 * Created by 崔龙 on 2018/1/4.
 */

public class FinancingTakeNotesAdapter extends BaseAdapter {
    private ArrayList<FinancingTakeNotesListBean.ResultDataBean> lists;
    private Context context;

    public FinancingTakeNotesAdapter(ArrayList<FinancingTakeNotesListBean.ResultDataBean> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view;
        /*  缓存子布局文件中的控件对象*/
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_financing_take_notes, null, false);
            holder = new ViewHolder();
            holder.tv_investdate = (TextView) view.findViewById(R.id.tv_investdate);
            holder.tv_investstatus = (TextView) view.findViewById(R.id.tv_investstatus);
            holder.tv_productrate = (TextView) view.findViewById(R.id.tv_productrate);
            holder.tv_productname = (TextView) view.findViewById(R.id.tv_productname);
            holder.tv_productday = (TextView) view.findViewById(R.id.tv_productday);
            holder.tv_investmoney = (TextView) view.findViewById(R.id.tv_investmoney);
            holder.tv_incomemoney = (TextView) view.findViewById(R.id.tv_incomemoney);
            holder.tv_progress = (TextView) view.findViewById(R.id.tv_progress);
            view.setTag(holder);
        }
        //缓存已滑入ListView中的item view
        else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.tv_investdate.setText("（始" + lists.get(position).getInvestdate() + "至" + lists.get(position).getExpiredate() + "）");
        if (lists.get(position).getInveststatus() == 1) {
            holder.tv_investstatus.setText("进行中");
        } else {
            holder.tv_investstatus.setText("已结束");
        }
        holder.tv_productrate.setText(lists.get(position).getProductrate() + "%");
        holder.tv_productname.setText(lists.get(position).getProductname());
        holder.tv_productday.setText(lists.get(position).getProductday());
        holder.tv_investmoney.setText(lists.get(position).getInvestmoney() + "元");
        holder.tv_incomemoney.setText(lists.get(position).getIncomemoney());
        holder.tv_progress.setText(lists.get(position).getProgress() + "%");

        return view;
    }

    class ViewHolder {
        TextView tv_productname;
        TextView tv_productrate;
        TextView tv_investstatus;
        TextView tv_investdate;
        TextView tv_productday;
        TextView tv_investmoney;
        TextView tv_incomemoney;
        TextView tv_progress;
    }
}
