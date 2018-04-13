package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2017/12/12.
 */

public class VersionInfoAdapter extends BaseAdapter {

    private String[] versionInfo;
    private Context mContext;

    public VersionInfoAdapter(String[] versionInfo, Context mContext) {
        this.versionInfo = versionInfo;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return versionInfo.length;
    }

    @Override
    public Object getItem(int i) {
        return versionInfo[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_version_data, parent, false);
            holder.version_info = (TextView) view.findViewById(R.id.version_info);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.version_info.setText(versionInfo[i]);
        return view;
    }

    public class ViewHolder {
        TextView version_info;
    }
}
