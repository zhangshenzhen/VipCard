package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;

import com.bjypt.vipcard.activity.crowdfunding.projectdetail.CrowdfundingDetailActivity;

import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.fragment.crowdfunding.entity.CfProjectItem;
import com.bjypt.vipcard.utils.AmountDisplayUtil;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.FomartToolUtils;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bumptech.glide.Glide;
import com.sinia.orderlang.utils.AppInfoUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionProjectAdapter extends RecyclerView.Adapter {


    private Context mcontext;
    private List<CfProjectItem> sellerBeans;
    public int count;
    private final int icon_width;
    private final int icon_height;

    public CollectionProjectAdapter(Context context, List<CfProjectItem> sellerBeans) {
        this.mcontext = context;
        this.sellerBeans= sellerBeans;
        int width = AppInfoUtil.getScreenWidth(context);
        icon_width = width - DensityUtil.dip2px(context, 20);
        icon_height = (int) (icon_width / 1.82);

    }

    public void reFresh(List<CfProjectItem> sellerBeans){
        this.sellerBeans= sellerBeans;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crowdfuning_collection_list_item_new,null);
        SellerViewHoldr mviewHoldr = new SellerViewHoldr(view);
        //设置图片适配屏幕
        int w = (icon_width*2)/5;
        int h = (w*8)/9 ;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
        mviewHoldr.imageView.setLayoutParams(params);


        return mviewHoldr;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SellerViewHoldr sellerViewHoldr = (SellerViewHoldr) holder;
        CfProjectItem sellBean = sellerBeans.get(position);
        sellerViewHoldr.btn_look.setVisibility(View.GONE);
        sellerViewHoldr.tv_project_Name.setText(sellBean.getProjectName());//项目名称
        int typImg = sellBean.getTypeImg();
        if (typImg ==0){
            sellerViewHoldr.igv.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.cf_project_status_start));
        }else if (typImg ==1){
            sellerViewHoldr.igv.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.cf_project_status_build));
        }else {
            sellerViewHoldr.igv.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.cf_project_status_end));

        }
        Glide.with(mcontext).load(sellBean.getHeadImg()).error(R.mipmap.more).into(sellerViewHoldr.imageView);
         if (sellBean.getOptimalMoney()!= null) {
            // sellerViewHoldr.tv_youhui_num.setText("起投金额：" + sellBean.getOptimalMoney().stripTrailingZeros().toPlainString() + "");
         }
        sellerViewHoldr.tv_target.setText(AmountDisplayUtil.displayChineseWan2(sellBean.getCfAmount()));
        sellerViewHoldr.tv_max_rate.setText(FomartToolUtils.fomartNum(sellBean.getMaxInterestRate()+"")+"%");
        sellerViewHoldr.tv_end_data.setText(sellBean.getBuyEndAt()>0? FomartToolUtils.fomartDate(sellBean.getBuyEndAt()+""):"0000-00-00");//截止
        sellerViewHoldr.tv_come_out.setText(sellBean.getSettleEndAt()>0? FomartToolUtils.fomartDate(sellBean.getSettleEndAt()+""):"0000-00-00");//结算
        sellerViewHoldr.tv_merchant_name.setText(sellBean.getMerchantName());
        if(sellBean.getCfAmount().compareTo(new BigDecimal(0))>0){
            BigDecimal progress = sellBean.getProgressCfAmount().divide(sellBean.getCfAmount(),2, BigDecimal.ROUND_HALF_UP);
            BigDecimal b=new BigDecimal(String.valueOf(progress));
            double rate = b.doubleValue()*100;
            sellerViewHoldr.progressBar2.setProgress((int)rate);
            sellerViewHoldr.tv_precent2.setText(progress.multiply(new BigDecimal(100)).intValue() +"%");
        }else{
            sellerViewHoldr.progressBar2.setProgress(100);
            sellerViewHoldr.tv_precent2.setText(sellBean.getProgressCfAmount().stripTrailingZeros().toPlainString()+"%");
       }

        //图片条目的点击事件
        sellerViewHoldr.re_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取点击的条目位置;
                //int posion = mviewHoldr.getAdapterPosition();
                Intent intent = new Intent(mcontext, CrowdfundingDetailActivity.class);
                intent.putExtra("pkprojectid", sellBean.getPkprojectid());
                mcontext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return sellerBeans.size()>0? sellerBeans.size():0;
    }

    static class SellerViewHoldr extends RecyclerView.ViewHolder{

        public final ImageView imageView ;
        public final ImageView igv;
        public final TextView tv_project_Name;//项目名称
        public final ProgressBar progressBar;
        public final ProgressBar progressBar2;
        public final TextView tv_precent;
        public final TextView tv_precent2;
        public final TextView tv_youhui_num;//最优惠金额
        public final RelativeLayout re_item ;
        public final TextView tv_target;//目标金额
        public final TextView tv_max_rate;//收益率
        public final TextView tv_end_data;//截止日
        public final TextView tv_come_out;//截止日
        public final TextView tv_merchant_name;
        public final Button btn_look;

        public SellerViewHoldr(View itemView) {

            super(itemView);

            re_item = itemView.findViewById(R.id.re_item);
            imageView = itemView.findViewById(R.id.igv_icon);
            igv = itemView.findViewById(R.id.igv_zhongchou);
            tv_youhui_num = itemView.findViewById(R.id.tv_start_amount);
            tv_project_Name = itemView.findViewById(R.id.tev_name);
            progressBar = itemView.findViewById(R.id.pb_project_progress);
            tv_precent = itemView.findViewById(R.id.tev_progress_data);
            progressBar2 = itemView.findViewById(R.id.pb_project_progress2);
            tv_precent2 = itemView.findViewById(R.id.tev_progress_data2);
            tv_target = itemView.findViewById(R.id.tv_target);
            tv_max_rate = itemView.findViewById(R.id.tv_max_rate);
            tv_end_data = itemView.findViewById(R.id.tv_end_data);
            tv_come_out = itemView.findViewById(R.id.tv_come_out);
            tv_merchant_name = itemView.findViewById(R.id.tev_mcher_name);
            btn_look = itemView.findViewById(R.id.btn_look);
        }
    }
    public void setVisibility(boolean isVisible, SellerViewHoldr sellerViewHoldr) {
        RecyclerView.LayoutParams param = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        if (isVisible) {
            param.height = RelativeLayout.LayoutParams.WRAP_CONTENT;// 这里注意使用自己布局的根布局类型
            param.width = RelativeLayout.LayoutParams.MATCH_PARENT;// 这里注意使用自己布局的根布局类型
            sellerViewHoldr.itemView.setVisibility(View.VISIBLE);
        } else {
            param.height = 0;
            param.width = 0;
            sellerViewHoldr.itemView.setLayoutParams(param);
            sellerViewHoldr.itemView.setVisibility(View.GONE);
        }
    }

}