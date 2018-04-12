package com.bjypt.vipcard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.YuEBaoBuyActivity;
import com.bjypt.vipcard.model.FinancingListBean;

import java.util.ArrayList;

/**
 * Created by 崔龙 on 2018/1/3.
 * <p>
 * 理财首页列表adapter
 */

public class FinancingAdapter extends BaseAdapter {

    private ArrayList<FinancingListBean.ResultDataBean> lists;
    private Context context;
    private String pkmuser;

    public FinancingAdapter(ArrayList<FinancingListBean.ResultDataBean> lists, Context context, String pkmuser) {
        this.lists = lists;
        this.context = context;
        this.pkmuser = pkmuser;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view;
        /*  缓存子布局文件中的控件对象*/
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_financing, null, false);
            holder = new ViewHolder();
            holder.tvCountryGridCode = (TextView) view.findViewById(R.id.tv_city);
            holder.tv_profit = (TextView) view.findViewById(R.id.tv_profit);
            holder.tv_flag = (TextView) view.findViewById(R.id.tv_flag);
            holder.tv_day = (TextView) view.findViewById(R.id.tv_day);
            holder.iv_flag = (ImageView) view.findViewById(R.id.iv_flag);
            view.setTag(holder);
        }
        //缓存已滑入ListView中的item view
        else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.tvCountryGridCode.setText(lists.get(position).getProductname());
        if (lists.get(position).getProductrate().contains(".")) {
            holder.tv_profit.setText(lists.get(position).getProductrate().substring(0, lists.get(position).getProductrate().indexOf(".") + 2) + "%");
        } else {
            holder.tv_profit.setText(lists.get(position).getProductrate() + "%");
        }
        holder.tv_day.setText(lists.get(position).getProductday());

        if (lists.get(position).getProductstatus() == 1) {
            holder.iv_flag.setImageResource(R.mipmap.financing_gm);
            holder.tv_flag.setVisibility(View.GONE);
        } else {
            holder.iv_flag.setImageResource(R.mipmap.financing_done1);
            holder.tv_flag.setVisibility(View.GONE);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lists.get(position).getProductstatus() == 1) {
                    Intent intent = new Intent(context, YuEBaoBuyActivity.class);
                    intent.putExtra("productName", lists.get(position).getProductname());
                    intent.putExtra("time", lists.get(position).getDiffdate());
                    intent.putExtra("productrate", lists.get(position).getProductrate());
                    intent.putExtra("investMoney", lists.get(position).getProducttotalmoney());
                    intent.putExtra("pkProduct", lists.get(position).getPkproduct());
                    intent.putExtra("pkmuser", pkmuser);
                    context.startActivity(intent);
                }

            }
        });
        return view;
    }

    class ViewHolder {
        TextView tv_day;
        TextView tv_flag;
        TextView tv_profit;
        TextView tvCountryGridCode;
        ImageView iv_flag;
    }
}
