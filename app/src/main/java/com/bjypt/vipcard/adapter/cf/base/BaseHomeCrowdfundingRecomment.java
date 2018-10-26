package com.bjypt.vipcard.adapter.cf.base;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.crowdfunding.projectdetail.CrowdfundingDetailActivity;
import com.bjypt.vipcard.adapter.BaseRecycleViewAdapter;
import com.bjypt.vipcard.adapter.cf.holder.HomeCrowdfundingGridViewHolder;
import com.bjypt.vipcard.adapter.cf.holder.HomeCrowdfundingRecommentViewHolder;
import com.bjypt.vipcard.fragment.crowdfunding.entity.CfProjectItem;
import com.bjypt.vipcard.fragment.crowdfunding.entity.CfProjectItemNew;
import com.bjypt.vipcard.utils.AmountDisplayUtil;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.FomartToolUtils;
import com.bjypt.vipcard.utils.LogUtil;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;

public abstract class BaseHomeCrowdfundingRecomment extends BaseRecycleViewAdapter<CfProjectItemNew, HomeCrowdfundingRecommentViewHolder> {
    protected int icon_width = 0;
    protected int icon_height = 0;
    Context context;

    public BaseHomeCrowdfundingRecomment(Context context) {
        this.context = context;
        initSize(context);
    }

    public abstract void initSize(Context context);

    @Override
    public HomeCrowdfundingRecommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getItemView(parent.getContext());
        HomeCrowdfundingRecommentViewHolder mViewHolder = new HomeCrowdfundingRecommentViewHolder(view);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(icon_width, icon_height);
        mViewHolder.icon.setLayoutParams(params);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(icon_width, LinearLayout.LayoutParams.WRAP_CONTENT/*DensityUtil.dip2px(context, 90)*/);
        mViewHolder.linear_title.setLayoutParams(params2);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(HomeCrowdfundingRecommentViewHolder holder, int position) {
        CfProjectItemNew cfProjectItem = datas.get(position);
        holder.tvName.setText(cfProjectItem.getProjectName());
        int typImg = cfProjectItem.getTypeImg();
        if (typImg ==0){
            holder.igv_zhongchou.setImageDrawable(context.getResources().getDrawable(R.mipmap.cf_project_status_start));
        }else if (typImg ==1){
            holder.igv_zhongchou.setImageDrawable(context.getResources().getDrawable(R.mipmap.cf_project_status_build));
        }else {
            holder.igv_zhongchou.setImageDrawable(context.getResources().getDrawable(R.mipmap.cf_project_status_end));
        }

        LogUtil.debugPrint("maximumIncomecfProjectItem. : "+cfProjectItem.toString());
        BigDecimal progress = cfProjectItem.getProgressCfAmount().divide(cfProjectItem.getCfAmount(), 2, BigDecimal.ROUND_HALF_UP);
        holder.tvProgress_data.setText(progress.multiply(new BigDecimal(100)).intValue() + "%");
        holder.pb_project_progress.setProgress(progress.multiply(new BigDecimal(100)).intValue());//进度条
        holder.tv_target_money.setText(AmountDisplayUtil.displayChineseWan2(cfProjectItem.getCfAmount()));
        holder.tv_max_income.setText(cfProjectItem.getMaximumIncome()!=null?FomartToolUtils.fomartMoney(cfProjectItem.getMaximumIncome()) : cfProjectItem.getMaximumIncome()+"元");
       // holder.tv_max_income.setText(AmountDisplayUtil.displayChineseWan2(cfProjectItem.getMaximunIncome()));//最高收益
        Picasso.with(context)
                .load(cfProjectItem.getGridUrl())
                .error(R.mipmap.more)
                .into(holder.icon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CrowdfundingDetailActivity.class);
                intent.putExtra("pkprojectid", cfProjectItem.getPkprojectid());
                context.startActivity(intent);
            }
        });
    }

    public abstract View getItemView(Context context);

    @Override
    public int getItemCount() {
        return datas.size();
    }

}
