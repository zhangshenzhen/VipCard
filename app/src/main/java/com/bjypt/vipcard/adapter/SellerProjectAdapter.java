package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.bean.SellerProjectBean;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class SellerProjectAdapter extends RecyclerView.Adapter{

    private Context mcontext;
    private ArrayList<SellerProjectBean.SellBean> sellerBeans;
    public int count;

    public SellerProjectAdapter(Context context, ArrayList<SellerProjectBean.SellBean> sellerBeans) {
        this.mcontext = context;
        this.sellerBeans= sellerBeans;

    }

    public void reFresh(ArrayList<SellerProjectBean.SellBean> sellerBeans){
        this.sellerBeans= sellerBeans;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seller,null);
         SellerViewHoldr mviewHoldr = new SellerViewHoldr(view);
         //图片条目的点击事件
          mviewHoldr.imageView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  //获取点击的条目位置;
                  int posion = mviewHoldr.getAdapterPosition();
                  //点击传递参数
                  //Intent intent = new Intent(mcontext,FruiteActivity.class);
                 // intent.putExtra("FRUIT_NAME",fruit.getName());
                 // intent.putExtra("FRUIT_IMAGE_ID",fruit.getImageid());
                 // mcontext.startActivity(intent);
              }
          });

        return mviewHoldr;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            SellerViewHoldr sellerViewHoldr = (SellerViewHoldr) holder;
            SellerProjectBean.SellBean sellBean = sellerBeans.get(position);
            Glide.with(mcontext).load(sellBean.getHeadImg()).into(sellerViewHoldr.imageView);
            sellerViewHoldr.tv_project_Name.setText(sellBean.getProjectName());//项目名称

             //假数据 进度条百分比
             Random r = new Random();
             int progress = r.nextInt(100);
             sellerViewHoldr.progressBar.setProgress(progress);
             sellerViewHoldr.tv_precent.setText(progress+"%");

    }

    @Override
    public int getItemCount() {
        return sellerBeans.size()>0? sellerBeans.size():0;
    }
   static class SellerViewHoldr extends RecyclerView.ViewHolder{

       private final ImageView imageView;
       private final TextView tv_project_Name;
       private final ProgressBar progressBar;
       private final TextView tv_precent;

       public SellerViewHoldr(View itemView) {
           super(itemView);

           imageView = itemView.findViewById(R.id.igv_seller);
           tv_project_Name = itemView.findViewById(R.id.tv_project_des);
           progressBar = itemView.findViewById(R.id.progress_bar);
           tv_precent = itemView.findViewById(R.id.tv_precent);
       }
   }
}
