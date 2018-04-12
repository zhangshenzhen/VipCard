package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.BankCtityBean;

import java.util.List;

/**
 * Created by 崔龙 on 2017/12/29.
 */

public class GridCodePopWindowAdapter extends BaseAdapter {
    private List<BankCtityBean.ResultDataBean> BankName;
    private Context context;

    public GridCodePopWindowAdapter(List<BankCtityBean.ResultDataBean> BankName, Context context) {
        this.BankName = BankName;
        this.context = context;
    }

    @Override
    public int getCount() {
        return BankName.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view;
        /*  缓存子布局文件中的控件对象*/
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_bank_city_select1, null, false);
            holder = new ViewHolder();
            holder.tvCountryGridCode = (TextView) view.findViewById(R.id.tv_city);
            view.setTag(holder);
        }
        //缓存已滑入ListView中的item view
        else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.tvCountryGridCode.setText(BankName.get(position).getlName());
        return view;
    }

    class ViewHolder {
        TextView tvCountryGridCode;
    }
}
