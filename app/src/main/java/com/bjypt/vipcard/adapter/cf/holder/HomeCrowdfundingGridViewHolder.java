package com.bjypt.vipcard.adapter.cf.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bjypt.vipcard.R;

public class HomeCrowdfundingGridViewHolder extends RecyclerView.ViewHolder {

    public ImageView icon;//图像
    public ImageView igv_zhongchou;//众筹图标
    public TextView tvName;//项目名称
    public TextView tvProgress_data;//进度数据
    public TextView tv_target;//目标金额 cfAmount
    public TextView tv_rate;//年化收益率
    public ProgressBar pb_project_progress;//进度条
    public LinearLayout linear_title;
    public Button btn_look;//查看
    public TextView tv_crowdfun_end;//众筹截止
    public TextView tv_crowdfun_comeout;//众筹结算
    public TextView tv_enterprise_name;//企业名称
    public HomeCrowdfundingGridViewHolder(View itemView) {
        super(itemView);
        icon = itemView.findViewById(R.id.igv_icon);
        igv_zhongchou = itemView.findViewById(R.id.igv_zhongchou);
        tvName = itemView.findViewById(R.id.tev_name);
        tvProgress_data = itemView.findViewById(R.id.tev_progress_data);
        linear_title = itemView.findViewById(R.id.linear_title);
        tv_target = itemView.findViewById(R.id.tv_target);
        tv_rate = itemView.findViewById(R.id.tv_rate);
        pb_project_progress = itemView.findViewById(R.id.pb_project_progress);
        btn_look = itemView.findViewById(R.id.btn_look);
        tv_crowdfun_end = itemView.findViewById(R.id.tv_crowdfun_end);
        tv_crowdfun_comeout = itemView.findViewById(R.id.tv_crowdfun_comeout);
        tv_enterprise_name = itemView.findViewById(R.id.tv_enterprise_name);

    }
}
