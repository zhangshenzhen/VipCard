package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;

import com.bjypt.vipcard.activity.crowdfunding.projectdetail.CrowdfundingDetailActivity;
import com.bjypt.vipcard.fragment.crowdfunding.entity.CfProjectItem;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bumptech.glide.Glide;
import com.sinia.orderlang.utils.AppInfoUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        icon_width = width - DensityUtil.dip2px(context, 10);
        icon_height = (int) (icon_width / 1.82);

    }

    public void reFresh(List<CfProjectItem> sellerBeans){
        this.sellerBeans= sellerBeans;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_crowdfuning_project_list_item,null);
        SellerViewHoldr mviewHoldr = new SellerViewHoldr(view);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(icon_width, icon_height);
        mviewHoldr.imageView.setLayoutParams(params);



        return mviewHoldr;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SellerViewHoldr sellerViewHoldr = (SellerViewHoldr) holder;
        //   SellerProjectBean.SellBean sellBean = sellerBeans.get(position);
        CfProjectItem sellBean = sellerBeans.get(position);

      if(sellBean.getStatus() != null){
       if(sellBean.getStatus() ==3){
            sellerViewHoldr.igv.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.cf_project_status_end));
        }else{
               if(sellBean.getCfAmount().compareTo(sellBean.getProgressCfAmount()) <=0){
                sellerViewHoldr.igv.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.cf_project_status_build));
            }else{
                sellerViewHoldr.igv.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.cf_project_status_start));
            }
        }
       }



        Glide.with(mcontext).load(sellBean.getHeadImg()).error(R.mipmap.more).into(sellerViewHoldr.imageView);
        sellerViewHoldr.tv_project_Name.setText(sellBean.getProjectName());//项目名称
        sellerViewHoldr.tv_youhui_num.setText("起投金额："+sellBean.getOptimalMoney().stripTrailingZeros().toPlainString()+"");

       /* ClipDrawable d = new ClipDrawable(new ColorDrawable(Color.parseColor("#00FF99")), Gravity.LEFT,ClipDrawable.HORIZONTAL);
        sellerViewHoldr.progressBar.setBackgroundColor(Color.parseColor("#BBFFFF"));
        sellerViewHoldr.progressBar.setProgressDrawable(d);*/


        if(sellBean.getCfAmount().compareTo(new BigDecimal(0))>0){
            BigDecimal progress = sellBean.getProgressCfAmount().divide(sellBean.getCfAmount(),2, BigDecimal.ROUND_HALF_UP);

            BigDecimal b=new BigDecimal(String.valueOf(progress));
            double rate = b.doubleValue()*100;
            sellerViewHoldr.progressBar.setProgress((int)rate);
            sellerViewHoldr.tv_precent.setText(progress.multiply(new BigDecimal(100)).intValue() +"%");

        }else{
            sellerViewHoldr.progressBar.setProgress(100);
            sellerViewHoldr.tv_precent.setText(sellBean.getProgressCfAmount().stripTrailingZeros().toPlainString()+"%");
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
        public final TextView tv_precent;
        public final TextView tv_youhui_num;//最优惠金额
        public final RelativeLayout re_item ;

        public SellerViewHoldr(View itemView) {
            super(itemView);
           // tv_project_des = itemView.findViewById(R.id.tv_project_des);
           /* imageView = itemView.findViewById(R.id.igv_seller);
            tv_project_Name = itemView.findViewById(R.id.tv_project_des);
            progressBar = itemView.findViewById(R.id.progress_bar);
            tv_precent = itemView.findViewById(R.id.tv_precent);
            tv_youhui_num = itemView.findViewById(R.id.tv_youhui_num);
            igv = itemView.findViewById(R.id.igv);*/
            re_item = itemView.findViewById(R.id.re_item);
            imageView = itemView.findViewById(R.id.igv_icon);
            igv = itemView.findViewById(R.id.igv_zhongchou);
            tv_youhui_num = itemView.findViewById(R.id.tv_start_amount);
            tv_project_Name = itemView.findViewById(R.id.tev_name);
            progressBar = itemView.findViewById(R.id.pb_project_progress);
            tv_precent = itemView.findViewById(R.id.tev_progress_data);

        }
    }

}