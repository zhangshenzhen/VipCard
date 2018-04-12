package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.LunTan;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by liyunte on 2016/12/30.
 */

public class NewsAdaper extends BaseAdapter {
    private Context context;
    private List<LunTan> list;

    public NewsAdaper(Context context, List<LunTan> list) {
        this.context = context;
        this.list = list;
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(configuration);
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
        MyViewHolder holder= null;
        if (convertView==null){
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_lun_item,parent,false);
            holder. tv = (ImageView) convertView.findViewById(R.id.iv_lun_pic);
            holder .tv_name = (TextView) convertView.findViewById(R.id.tv_lun_name);
            holder. tv_liear = (LinearLayout) convertView.findViewById(R.id.iv_liao_linear);
            convertView.setTag(holder);
        }else {
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(list.get(position).getProjectName());
        ImageLoader.getInstance().displayImage(Config.web.picUrl+list.get(position).getProjectIocn(),holder.tv, AppConfig.DEFAULT_IMG_LIFE_TYPE);


        return convertView;
    }
    class MyViewHolder {
        ImageView tv;
        TextView tv_name;
        LinearLayout tv_liear;

    }
}
