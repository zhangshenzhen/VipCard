package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.FundListBean;
import com.bjypt.vipcard.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/4/5.
 */
public class FundListAdapter extends MyBaseAdapter<FundListBean> {

    private Context context;

    public FundListAdapter( Context mContext) {
        super(mContext);

        this.context=mContext;
        Wethod.configImageLoader(mContext);
    }

    @Override
    public View initView(int position, View convertView, ViewGroup parent) {


        childHolder mHolder = null;
        if (convertView == null) {

            mHolder = new childHolder();
            convertView = inflater.inflate(R.layout.layout_fundlist_item, null);

            mHolder.tv_fundlist_tradeTime = (TextView) convertView.findViewById(R.id.tv_fundlist_tradeTime);
            mHolder.tv_fundlist_tradeMoney = (TextView) convertView.findViewById(R.id.tv_fundlist_tradeMoney);
            mHolder.tv_fundlist_tradeNum = (TextView) convertView.findViewById(R.id.tv_fundlist_tradeNum);
            mHolder.iv_fundlist_storeLogo = (ImageView) convertView.findViewById(R.id.iv_fundlist_storeLogo);

            convertView.setTag(mHolder);
        } else {

            mHolder = (childHolder) convertView.getTag();
        }

        mHolder.tv_fundlist_tradeTime.setText(list.get(position).getTradeTime());

        /**1  消费  2   充值 */
        if (list.get(position).getType().equals("2")) {

            mHolder.tv_fundlist_tradeMoney.setText("+"+list.get(position).getTradeMoney());
            mHolder.tv_fundlist_tradeNum.setText(list.get(position).getTradeName() + "充值,交易编号:" + list.get(position).getTradeNum());

        }else {

            mHolder.tv_fundlist_tradeMoney.setText("-"+list.get(position).getTradeMoney());
            mHolder.tv_fundlist_tradeNum.setText(list.get(position).getTradeName() + "消费,交易编号:" + list.get(position).getTradeNum());

        }

        if ("".equals(list.get(position).getTradeLogo())) {
            mHolder.iv_fundlist_storeLogo.setImageResource(R.mipmap.fundlist_default_img);
        } else {
            ImageLoader.getInstance().displayImage(Config.web.picUrl + list.get(position).getTradeLogo(), mHolder.iv_fundlist_storeLogo, AppConfig.DEFAULT_IMG_OPTIONS_AD);
        }
        return convertView;
    }

    public class childHolder {

        private TextView tv_fundlist_tradeTime, tv_fundlist_tradeMoney, tv_fundlist_tradeNum;
        private ImageView iv_fundlist_storeLogo;
    }
}
