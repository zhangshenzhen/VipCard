package com.bjypt.vipcard.adapter.cf.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;

public class HomeCrowdfundingGridViewHolder extends RecyclerView.ViewHolder {

    public ImageView icon;
    public ImageView igv_zhongchou;
    public TextView tvName;
    public TextView tvProgress_data;
    public LinearLayout linear_title;

    public HomeCrowdfundingGridViewHolder(View itemView) {
        super(itemView);
        icon = itemView.findViewById(R.id.igv_icon);
        igv_zhongchou = itemView.findViewById(R.id.igv_zhongchou);
        tvName = itemView.findViewById(R.id.tev_name);
        tvProgress_data = itemView.findViewById(R.id.tev_progress_data);
        linear_title = itemView.findViewById(R.id.linear_title);
    }
}
