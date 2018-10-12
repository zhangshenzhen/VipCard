package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.bjypt.vipcard.R;

import com.bjypt.vipcard.activity.crowdfunding.projectdetail.CrowdfundingDetailActivity;
import com.bjypt.vipcard.fragment.crowdfunding.entity.CfProjectItem;
import com.bjypt.vipcard.utils.AmountDisplayUtil;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.FomartToolUtils;
import com.bumptech.glide.Glide;
import com.sinia.orderlang.utils.AppInfoUtil;

import java.math.BigDecimal;
import java.util.List;

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

         if (sellBean.getOptimalMoney()!= null) {
            // sellerViewHoldr.tv_youhui_num.setText("起投金额：" + sellBean.getOptimalMoney().stripTrailingZeros().toPlainString() + "");
         }
       /* ClipDrawable d = new ClipDrawable(new ColorDrawable(Color.parseColor("#00FF99")), Gravity.LEFT,ClipDrawable.HORIZONTAL);
        sellerViewHoldr.progressBar.setBackgroundColor(Color.parseColor("#BBFFFF"));
        sellerViewHoldr.progressBar.setProgressDrawable(d);*/

        sellerViewHoldr.tv_target.setText(AmountDisplayUtil.displayChineseWan2(sellBean.getCfAmount()));
        sellerViewHoldr.tv_max_rate.setText(sellBean.getMaxInterestRate()+"%");
        sellerViewHoldr.tv_end_data.setText(sellBean.getBuyEndAt()>0? FomartToolUtils.fomartDate(sellBean.getBuyEndAt()+""):"2018-08-03");//截止
        sellerViewHoldr.tv_come_out.setText(sellBean.getSettleEndAt()>0? FomartToolUtils.fomartDate(sellBean.getBuyEndAt()+""):"2018-08-04");//结算
        sellerViewHoldr.tv_merchant_name.setText((sellBean.getMerchantName()).isEmpty()? "**.***国际旅游有限公司" :sellBean.getMerchantName());

        if(sellBean.getCfAmount().compareTo(new BigDecimal(0))>0){
            BigDecimal progress = sellBean.getProgressCfAmount().divide(sellBean.getCfAmount(),2, BigDecimal.ROUND_HALF_UP);

            BigDecimal b=new BigDecimal(String.valueOf(progress));
            double rate = b.doubleValue()*100;
           // sellerViewHoldr.progressBar.setProgress((int)rate);
          //  sellerViewHoldr.tv_precent.setText(progress.multiply(new BigDecimal(100)).intValue() +"%");
            sellerViewHoldr.progressBar2.setProgress((int)rate);
            sellerViewHoldr.tv_precent2.setText(progress.multiply(new BigDecimal(100)).intValue() +"%");

        }else{
          //  sellerViewHoldr.progressBar.setProgress(100);
          //  sellerViewHoldr.tv_precent.setText(sellBean.getProgressCfAmount().stripTrailingZeros().toPlainString()+"%");
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
            progressBar2 = itemView.findViewById(R.id.pb_project_progress2);
            tv_precent2 = itemView.findViewById(R.id.tev_progress_data2);
            tv_target = itemView.findViewById(R.id.tv_target);
            tv_max_rate = itemView.findViewById(R.id.tv_max_rate);
            tv_end_data = itemView.findViewById(R.id.tv_end_data);
            tv_come_out = itemView.findViewById(R.id.tv_come_out);
            tv_merchant_name = itemView.findViewById(R.id.tev_mcher_name);

        }
    }

}