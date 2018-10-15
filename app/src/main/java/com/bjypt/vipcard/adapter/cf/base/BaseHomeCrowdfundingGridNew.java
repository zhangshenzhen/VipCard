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
import com.bjypt.vipcard.fragment.crowdfunding.entity.CfProjectItem;
import com.bjypt.vipcard.utils.AmountDisplayUtil;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.FomartToolUtils;
import com.sinia.orderlang.utils.AppInfoUtil;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;

public abstract class BaseHomeCrowdfundingGridNew extends BaseRecycleViewAdapter<CfProjectItem, HomeCrowdfundingGridViewHolder> {
    protected int icon_width = 0;
    protected int icon_height = 0;
    protected  int title_width;
    Context context;
    protected final int width;

    public BaseHomeCrowdfundingGridNew(Context context) {
        this.context = context;
        initSize(context);
        this.width = AppInfoUtil.getScreenWidth(context);
        this.title_width = (width - DensityUtil.dip2px(context, 20));
    }

    public abstract void initSize(Context context);

    @Override
    public HomeCrowdfundingGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getItemView(parent.getContext());
        HomeCrowdfundingGridViewHolder mViewHolder = new HomeCrowdfundingGridViewHolder(view);

        //设置图片适配屏幕比例
        int w = (width*2)/5;
        int h = (w*8)/9 ;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
        mViewHolder.icon.setLayoutParams(params);

        //设置下方标题布局的宽高
       // LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT/*DensityUtil.dip2px(context, 50)*/);
      //  mViewHolder.linear_title.setLayoutParams(params2);
        return mViewHolder;
    }


    @Override
    public void onBindViewHolder(HomeCrowdfundingGridViewHolder holder, int position) {
        CfProjectItem cfProjectItem = datas.get(position);
        holder.tvName.setText(cfProjectItem.getProjectName());
        if (cfProjectItem.getStatus() == 3) {
            holder.igv_zhongchou.setImageDrawable(context.getResources().getDrawable(R.mipmap.cf_project_status_end));
        } else {
            if (cfProjectItem.getCfAmount().compareTo(cfProjectItem.getProgressCfAmount()) <= 0) {
                holder.igv_zhongchou.setImageDrawable(context.getResources().getDrawable(R.mipmap.cf_project_status_build));
            } else {
                holder.igv_zhongchou.setImageDrawable(context.getResources().getDrawable(R.mipmap.cf_project_status_start));
            }
        }
        BigDecimal progress = cfProjectItem.getProgressCfAmount().divide(cfProjectItem.getCfAmount(), 2, BigDecimal.ROUND_HALF_UP);
        holder.tvProgress_data.setText(progress.multiply(new BigDecimal(100)).intValue() + "%");
        holder.pb_project_progress.setProgress(progress.multiply(new BigDecimal(100)).intValue());//进度条
        holder.tv_target.setText(AmountDisplayUtil.displayChineseWan2(cfProjectItem.getCfAmount()));//目标金额
        holder.tv_crowdfun_end.setText(cfProjectItem.getBuyEndAt()>0? FomartToolUtils.fomartDate(cfProjectItem.getBuyEndAt()+""):"2018-08-03");//截止日期
        holder.tv_crowdfun_comeout.setText(cfProjectItem.getSettleEndAt()>0? FomartToolUtils.fomartDate(cfProjectItem.getBuyEndAt()+""):"2018-08-04");
        holder.tv_enterprise_name.setText((""+cfProjectItem.getMerchantName()).isEmpty()? "*****国际旅游有限公司" :cfProjectItem.getMerchantName());
        holder.tv_rate.setText(FomartToolUtils.fomartNum(cfProjectItem.getMaxInterestRate()+"")+"%" );//年化率



       if(cfProjectItem.isLook()==false){
           holder.btn_look.setVisibility(View.GONE);
        }else {

       }

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
