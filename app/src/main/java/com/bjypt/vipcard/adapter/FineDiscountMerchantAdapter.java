package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.FineMerchantBean;
import com.bjypt.vipcard.model.MerchantListBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinia.orderlang.utils.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/3/3.
 */

public class FineDiscountMerchantAdapter extends  MyBaseAdapter<FineMerchantBean>  {
    private Context mContext;
    private List<FineMerchantBean> merchantListBeen;

    public FineDiscountMerchantAdapter(List<FineMerchantBean> list, Context mContext) {
        super(list, mContext);
        this.mContext = mContext;
        this.merchantListBeen = list;
    }

    @Override
    public View initView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_fine_discount_listview_item, parent, false);
            viewHolder.iv_merchant_photo = (ImageView)convertView.findViewById(R.id.iv_merchant_photo);
            viewHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            viewHolder.tv_jianjie = (TextView)convertView.findViewById(R.id.tv_jianjie);
            viewHolder.tv_address = (TextView)convertView.findViewById(R.id.tv_address);
            viewHolder.tv_half_desc = (TextView)convertView.findViewById(R.id.tv_half_desc);
            viewHolder.linear_half_desc = (LinearLayout) convertView.findViewById(R.id.linear_half_desc);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(list.get(position).getMuname());
        viewHolder.tv_jianjie.setText(list.get(position).getFiveDiscount());
        viewHolder.tv_address.setText(list.get(position).getAddress());
        if(StringUtil.isNotEmpty(list.get(position).getSpecialRemark())){
            viewHolder.linear_half_desc.setVisibility(View.VISIBLE);
            viewHolder.tv_half_desc.setText(list.get(position).getSpecialRemark());
        }else{
            viewHolder.linear_half_desc.setVisibility(View.GONE);
        }
        ImageLoader.getInstance().displayImage(Config.web.picUrl + list.get(position).getLogo(), viewHolder.iv_merchant_photo, AppConfig.DEFAULT_IMG_MERCHANT_BG);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_merchant_photo;
        TextView tv_name;
        TextView tv_jianjie;
        TextView tv_address;
        TextView tv_half_desc;
        LinearLayout linear_half_desc;
    }
}
