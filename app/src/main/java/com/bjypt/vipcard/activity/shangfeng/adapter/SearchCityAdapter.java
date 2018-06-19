package com.bjypt.vipcard.activity.shangfeng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.data.bean.CityBean;

import java.util.List;

/**
 * 搜索城市
 */
public class SearchCityAdapter extends BaseAdapter {
    private Context mContext;
    private List<CityBean> mCities;

    public SearchCityAdapter(Context mContext, List<CityBean> mCities) {
        this.mCities = mCities;
        this.mContext = mContext;
    }

    public void updateData(List<CityBean> list) {
        mCities = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCities == null ? 0 : mCities.size();
    }

    @Override
    public CityBean getItem(int position) {
        return mCities == null ? null : mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ResultViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.select_search_city_item, parent, false);
            holder = new ResultViewHolder();
            holder.name = (TextView) view.findViewById(R.id.tv_item_result_listview_name);
            view.setTag(holder);
        } else {
            holder = (ResultViewHolder) view.getTag();
        }
        holder.name.setText(mCities.get(position).getCityname());
        return view;
    }

    public static class ResultViewHolder {
        TextView name;
    }
}
