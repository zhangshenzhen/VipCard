package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.MyHomePageData;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class MyHomePageRecyclerViewAdapter extends RecyclerView.Adapter<MyHomePageRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<MyHomePageData> mList;

    public MyHomePageRecyclerViewAdapter(Context mContext, List<MyHomePageData> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_homepage_adapter, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        int type = mList.get(i).getType();
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
                    holder.id_image1.setVisibility(View.GONE);
                } else {
                    if (("").equals(mList.get(i).getAttachmentList().get(0).getAttachment())) {
                        holder.id_image2.setVisibility(View.GONE);
                    } else {
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
                    holder.id_image1.setVisibility(View.GONE);
                } else {
                    if (("").equals(mList.get(i).getAttachmentList().get(0).getAttachment())) {
                        holder.id_image3.setVisibility(View.GONE);
                    } else {
                        ImageLoader.getInstance().displayImage(mList.get(i).getAttachmentList().get(0).getAttachment(), holder.id_image3, AppConfig.CIRCLE_IMAGE);
                    }
                }
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

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


        public ViewHolder(View view) {
            super(view);
            linear_item_1 = (LinearLayout) view.findViewById(R.id.linear_item_1);
            linear_item_2 = (LinearLayout) view.findViewById(R.id.linear_item_2);
            linear_item_3 = (LinearLayout) view.findViewById(R.id.linear_item_3);

            id_year_text1 = (TextView) view.findViewById(R.id.id_year_text1);
            id_month_text1 = (TextView) view.findViewById(R.id.id_month_text1);
            id_title_text1 = (TextView) view.findViewById(R.id.id_title_text1);
            id_time1 = (TextView) view.findViewById(R.id.id_time1);
            id_image1 = (ImageView) view.findViewById(R.id.id_image1);

            id_month_text2 = (TextView) view.findViewById(R.id.id_month_text2);
            id_title_text2 = (TextView) view.findViewById(R.id.id_title_text2);
            id_time2 = (TextView) view.findViewById(R.id.id_time2);
            id_image2 = (ImageView) view.findViewById(R.id.id_image2);

            id_title_text3 = (TextView) view.findViewById(R.id.id_title_text3);
            id_time3 = (TextView) view.findViewById(R.id.id_time3);
            id_image3 = (ImageView) view.findViewById(R.id.id_image3);

        }
    }
}
