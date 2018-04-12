package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.MyHomePageData;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/11/23.
 */

public class MyHomePageAdapter extends BaseAdapter {

    private Context mContext;
    private List<MyHomePageData> mList;

    public MyHomePageAdapter(Context mContext, List<MyHomePageData> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        Log.i("wanglei", i + "  getView");
        int type = mList.get(i).getType();
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.item_homepage_adapter, null);
            holder.linear_item_1 = (LinearLayout) view.findViewById(R.id.linear_item_1);
            holder.linear_item_2 = (LinearLayout) view.findViewById(R.id.linear_item_2);
            holder.linear_item_3 = (LinearLayout) view.findViewById(R.id.linear_item_3);

            holder.id_year_text1 = (TextView) view.findViewById(R.id.id_year_text1);
            holder.id_month_text1 = (TextView) view.findViewById(R.id.id_month_text1);
            holder.id_title_text1 = (TextView) view.findViewById(R.id.id_title_text1);
            holder.id_time1 = (TextView) view.findViewById(R.id.id_time1);
            holder.id_image1 = (ImageView) view.findViewById(R.id.id_image1);

            holder.id_month_text2 = (TextView) view.findViewById(R.id.id_month_text2);
            holder.id_title_text2 = (TextView) view.findViewById(R.id.id_title_text2);
            holder.id_time2 = (TextView) view.findViewById(R.id.id_time2);
            holder.id_image2 = (ImageView) view.findViewById(R.id.id_image2);

            holder.id_title_text3 = (TextView) view.findViewById(R.id.id_title_text3);
            holder.id_time3 = (TextView) view.findViewById(R.id.id_time3);
            holder.id_image3 = (ImageView) view.findViewById(R.id.id_image3);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        switch (type) {
            case 1:                 //type=1 显示年月内容
                holder.linear_item_1.setVisibility(View.VISIBLE);
                holder.linear_item_2.setVisibility(View.GONE);
                holder.linear_item_3.setVisibility(View.GONE);

                holder.id_year_text1.setText(String.valueOf(mList.get(i).getYear()));
                holder.id_month_text1.setText(String.valueOf(mList.get(i).getMonth()));
                holder.id_title_text1.setText(String.valueOf(mList.get(i).getTitle()));
                holder.id_time1.setText(mList.get(i).getTime());
                if (mList.get(i).getAttachmentList() == null || mList.get(i).getAttachmentList().size() == 0) {
                    holder.id_image1.setVisibility(View.GONE);
                } else {
                    if (("").equals(mList.get(i).getAttachmentList().get(0).getAttachment())) {
                        holder.id_image1.setVisibility(View.GONE);
                    } else {
                        holder.id_image1.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(mList.get(i).getAttachmentList().get(0).getAttachment(), holder.id_image1, AppConfig.CIRCLE_IMAGE);
                    }
                }

                break;
            case 2:                  //type=2 显示月内容
                holder.linear_item_1.setVisibility(View.GONE);
                holder.linear_item_2.setVisibility(View.VISIBLE);
                holder.linear_item_3.setVisibility(View.GONE);

                holder.id_month_text2.setText(String.valueOf(mList.get(i).getMonth()));
                holder.id_title_text2.setText(String.valueOf(mList.get(i).getTitle()));
                holder.id_time2.setText(mList.get(i).getTime());
                if (mList.get(i).getAttachmentList() == null || mList.get(i).getAttachmentList().size() == 0) {
                    holder.id_image2.setVisibility(View.GONE);
                } else {
                    if (("").equals(mList.get(i).getAttachmentList().get(0).getAttachment())) {
                        holder.id_image2.setVisibility(View.GONE);
                    } else {
                        holder.id_image2.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(mList.get(i).getAttachmentList().get(0).getAttachment(), holder.id_image2, AppConfig.CIRCLE_IMAGE);
                    }
                }
                break;
            case 3:                 //type=3 显示内容
                holder.linear_item_1.setVisibility(View.GONE);
                holder.linear_item_2.setVisibility(View.GONE);
                holder.linear_item_3.setVisibility(View.VISIBLE);

                holder.id_title_text3.setText(String.valueOf(mList.get(i).getTitle()));
                holder.id_time3.setText(mList.get(i).getTime());
                if (mList.get(i).getAttachmentList() == null || mList.get(i).getAttachmentList().size() == 0) {
                    holder.id_image3.setVisibility(View.GONE);
                } else {
                    if (("").equals(mList.get(i).getAttachmentList().get(0).getAttachment())) {
                        holder.id_image3.setVisibility(View.GONE);
                    } else {
                        holder.id_image3.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(mList.get(i).getAttachmentList().get(0).getAttachment(), holder.id_image3, AppConfig.CIRCLE_IMAGE);
                    }
                }
                break;
        }

        return view;
    }

    class ViewHolder {

        LinearLayout linear_item_1;
        LinearLayout linear_item_2;
        LinearLayout linear_item_3;

        TextView id_year_text1;
        TextView id_month_text1;
        ImageView id_image1;
        TextView id_title_text1;
        TextView id_time1;

        TextView id_month_text2;
        ImageView id_image2;
        TextView id_title_text2;
        TextView id_time2;

        ImageView id_image3;
        TextView id_title_text3;
        TextView id_time3;

    }

}
