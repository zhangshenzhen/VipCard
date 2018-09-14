package com.bjypt.vipcard.adapter.cf;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.BaseRecycleViewAdapter;
import com.bjypt.vipcard.adapter.cf.holder.HomeCrowdfundingGridViewHolder;
import com.bjypt.vipcard.adapter.cf.holder.HomeCrowdfundingListViewHolder;
import com.bjypt.vipcard.fragment.crowdfunding.entity.CfProjectItem;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.LogUtil;
import com.sinia.orderlang.utils.AppInfoUtil;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;

public class HomeCrowdfundingListProjectAdapter extends BaseRecycleViewAdapter<CfProjectItem, HomeCrowdfundingListViewHolder> {
    Context context;
    private int icon_width;
    private int icon_height;

    public HomeCrowdfundingListProjectAdapter(Context context) {
        this.context = context;
        int width = AppInfoUtil.getScreenWidth(context);
        icon_width = width - DensityUtil.dip2px(context, 20);
        icon_height = (int) (icon_width / 1.82);
    }

    @Override
    public HomeCrowdfundingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_crowdfuning_project_list_item, null);
        HomeCrowdfundingListViewHolder mViewHolder = new HomeCrowdfundingListViewHolder(view);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(icon_width, icon_height);
        mViewHolder.icon.setLayoutParams(params);
        return mViewHolder;
    }


    @Override
    public void onBindViewHolder(HomeCrowdfundingListViewHolder holder, int position) {
        CfProjectItem  cfProjectItem = datas.get(position);
        holder.tvName.setText(cfProjectItem.getProjectName());
        if(cfProjectItem.getStatus() ==3){
            holder.igv_zhongchou.setImageDrawable(context.getResources().getDrawable(R.mipmap.cf_project_status_end));
        }else{
            if(cfProjectItem.getCfAmount().compareTo(cfProjectItem.getProgressCfAmount()) <=0){
                holder.igv_zhongchou.setImageDrawable(context.getResources().getDrawable(R.mipmap.cf_project_status_build));
            }else{
                holder.igv_zhongchou.setImageDrawable(context.getResources().getDrawable(R.mipmap.cf_project_status_start));
            }
        }
        if(cfProjectItem.getCfAmount().compareTo(new BigDecimal(0))>0){
            BigDecimal progress = cfProjectItem.getProgressCfAmount().divide(cfProjectItem.getCfAmount(),2, BigDecimal.ROUND_HALF_UP);
            holder.pb_project_progress.setProgress(progress.intValue() > 100 ? 100 : progress.intValue());
            holder.tvProgress_data.setText(progress.multiply(new BigDecimal(100)).intValue() +"%");
        }else{
            holder.pb_project_progress.setProgress(100);
            holder.tvProgress_data.setText("100%");
        }

        Picasso.with(context)
                .load( cfProjectItem.getHeadImg())
                .error(R.mipmap.more)
                .into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


}
