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
import com.bjypt.vipcard.model.FundListBean;
import com.bjypt.vipcard.model.MerchantListBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinia.orderlang.utils.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/3/1.
 */

public class LifeHuiAdapter extends MyBaseAdapter<MerchantListBean> {

    private Context mContext;
    private List<MerchantListBean> merchantListBeen;

    public LifeHuiAdapter(List<MerchantListBean> list, Context mContext) {
        super(list, mContext);
        this.mContext = mContext;
        this.merchantListBeen = list;
    }

    @Override
    public View initView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_life_hui_listview_item, parent, false);
            viewHolder.tv_zhekou = (TextView) convertView.findViewById(R.id.tv_zhekou);
            viewHolder.iv_merchant_photo = (ImageView) convertView.findViewById(R.id.iv_merchant_photo);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_jianjie = (TextView) convertView.findViewById(R.id.tv_jianjie);
            viewHolder.tv_juli = (TextView) convertView.findViewById(R.id.tv_juli);
            viewHolder.tv_number_zhekou = (TextView) convertView.findViewById(R.id.tv_number_zhekou);
            viewHolder.tv_adress_adapter = (TextView) convertView.findViewById(R.id.tv_adress_adapter);
            viewHolder.rela_zhekou = (RelativeLayout)convertView.findViewById(R.id.rela_zhekou);
            viewHolder.ll_dikou = (LinearLayout)convertView.findViewById(R.id.ll_dikou);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(list.get(position).getMuname());
        float discount = StringUtil.stringToFloat(list.get(position).getDiscount());
        if (discount > 0 && discount < 100) {
            viewHolder.rela_zhekou.setVisibility(View.VISIBLE);
            viewHolder.tv_zhekou.setText((discount / 10 )+"折");
        }else{
            viewHolder.rela_zhekou.setVisibility(View.GONE);
        }
        viewHolder.tv_jianjie.setText(list.get(position).getMerdesc());
        viewHolder.tv_juli.setText(list.get(position).getDistance());
        if (!StringUtil.isEmpty(list.get(position).getIntegral())) {
            viewHolder.ll_dikou.setVisibility(View.VISIBLE);
            viewHolder.tv_number_zhekou.setText(list.get(position).getIntegral());
        } else {
            viewHolder.ll_dikou.setVisibility(View.GONE);
        }
        viewHolder.tv_adress_adapter.setText(list.get(position).getAddress());
        ImageLoader.getInstance().displayImage(Config.web.picUrl + list.get(position).getLogo(), viewHolder.iv_merchant_photo, AppConfig.DEFAULT_IMG_MERCHANT_BG);

        return convertView;
    }

    class ViewHolder {
        TextView tv_zhekou;
        ImageView iv_merchant_photo;
        TextView tv_name;
        TextView tv_jianjie;
        TextView tv_juli;
        TextView tv_number_zhekou;
        TextView tv_adress_adapter;
        RelativeLayout rela_zhekou;
        LinearLayout ll_dikou;
    }
}
