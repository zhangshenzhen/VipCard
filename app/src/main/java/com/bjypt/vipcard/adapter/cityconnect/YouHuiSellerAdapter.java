package com.bjypt.vipcard.adapter.cityconnect;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.NewMerchantListBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinia.orderlang.utils.StringUtil;

import java.util.List;

public class YouHuiSellerAdapter extends RecyclerView.Adapter {

    public YouHuiSellerAdapter(Context mcontext, List<NewMerchantListBean> sellerList) {
        this.mcontext = mcontext;
        this.sellerList = sellerList;
    }

    private Context mcontext;
    private List<NewMerchantListBean> sellerList;

    public void reFresh(List<NewMerchantListBean> sellerList){
        this.sellerList = sellerList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mercahntlist,null);
        YouHuiSellerHolder    youHuiSellerHolder = new YouHuiSellerHolder(view);
        return youHuiSellerHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        YouHuiSellerHolder  sellHolder = (YouHuiSellerHolder) holder;
        NewMerchantListBean   listBean =  sellerList.get(position);
        //商家图像
        if (!StringUtil.isNotEmpty(listBean.getLogo())) {
            sellHolder.photo_merchant_item.setImageResource(R.drawable.default_dianpu);
        } else {
            ImageLoader.getInstance().displayImage(Config.web.picUrl + listBean.getLogo(), sellHolder.photo_merchant_item, AppConfig.DEFAULT_IMG_MERCHANT_LIST_BG);
        }
        //店铺名
        sellHolder.name_merchant_item.setText(listBean.getMuname());
        //地址
        sellHolder.address_merchant_item.setText(listBean.getAddress());
        //距离
        sellHolder.distance_merchant_item.setText(listBean.getDistance());
        //减
         if(StringUtil.isNotEmpty(listBean.getIntegral())){
         sellHolder.linear_jian.setVisibility(View.GONE);
         }else {
          sellHolder.jian_merchant_item.setText(listBean.getIntegral());
         }
        //折
        if(StringUtil.isNotEmpty(listBean.getVirtualActivity())){
         sellHolder.linear_fan.setVisibility(View.GONE);
        }else {
         sellHolder.fan_merchant_item.setText(listBean.getVirtualActivity());
        }

    }

    @Override
    public int getItemCount() {
        return sellerList.size();
    }

    static class YouHuiSellerHolder extends RecyclerView.ViewHolder{
        ImageView photo_merchant_item;      //店铺图片
        TextView name_merchant_item;        //店铺名字
        TextView address_merchant_item;    //地址
        TextView distance_merchant_item;   //距离
        TextView jian_merchant_item;         //减
        LinearLayout linear_jian;             //减
        TextView fan_merchant_item;          //返
        LinearLayout linear_fan;              //返
        ImageView imageView5;
        public YouHuiSellerHolder(View itemView) {
            super(itemView);
            photo_merchant_item = (ImageView) itemView.findViewById(R.id.photo_merchant_item);
            name_merchant_item = (TextView) itemView.findViewById(R.id.name_merchant_item);
            address_merchant_item = (TextView) itemView.findViewById(R.id.address_merchant_item);
            distance_merchant_item = (TextView) itemView.findViewById(R.id.distance_merchant_item);
            jian_merchant_item = (TextView) itemView.findViewById(R.id.jian_merchant_item);
            fan_merchant_item = (TextView) itemView.findViewById(R.id.fan_merchant_item);
            linear_fan = (LinearLayout) itemView.findViewById(R.id.linear_fan);
            linear_jian = (LinearLayout) itemView.findViewById(R.id.linear_jian);
            imageView5 = (ImageView) itemView.findViewById(R.id.imageView5);

        }


    }


}
