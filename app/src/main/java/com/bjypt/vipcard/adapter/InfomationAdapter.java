package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.InfomationData;
import com.bjypt.vipcard.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/31
 * Use by消息Adapter
 */
public class InfomationAdapter  extends MyBaseAdapter<InfomationData>{

private String type;
    public InfomationAdapter(List<InfomationData> list, Context mContext,String type) {
        super(list, mContext);
        this.type =type;
        Wethod.configImageLoader(mContext);
    }

    @Override
    public View initView(int position, View convertView, ViewGroup parent) {
        InfoHolder infoHolder ;
        if(convertView == null){
            infoHolder = new InfoHolder();
            convertView = inflater.inflate(R.layout.infomation_list_item,null);
            infoHolder.mHead = (RoundImageView) convertView.findViewById(R.id.infomation_head);
            infoHolder.tv1 = (TextView) convertView.findViewById(R.id.info_one_tv);
            infoHolder.tv2 = (TextView) convertView.findViewById(R.id.info_two_tv);
            infoHolder.tv3 = (TextView) convertView.findViewById(R.id.info_time);
            infoHolder.tv_info_time = (TextView) convertView.findViewById(R.id.tv_info_time);
            infoHolder.tv_information_coupon_redpackge = (TextView) convertView.findViewById(R.id.tv_information_coupon_redpackge);
            infoHolder.ll_information_coupon = (LinearLayout) convertView.findViewById(R.id.ll_information_coupon);
            convertView.setTag(infoHolder);
        }else{
            infoHolder = (InfoHolder) convertView.getTag();
        }
        infoHolder.tv1.setText(list.get(position).getMuname());
        if (type.equals("0")){
            infoHolder.tv_info_time.setVisibility(View.VISIBLE);
            infoHolder.tv3.setVisibility(View.GONE);
            infoHolder.tv_info_time.setText("时间：" + list.get(position).getSenddate());
        }else if (type.equals("2")){
            infoHolder.tv_info_time.setVisibility(View.VISIBLE);
            infoHolder.ll_information_coupon.setVisibility(View.VISIBLE);
            infoHolder.ll_information_coupon.setBackgroundResource(R.mipmap.redpacket_style);
            infoHolder.tv_information_coupon_redpackge.setText("");
            infoHolder.tv3.setVisibility(View.GONE);
            infoHolder.tv_info_time.setText("时间："+list.get(position).getSenddate());
        }
        infoHolder.mHead.setImageResource(R.mipmap.ic_launcher);
       /* if(list.get(position).getLogo().equals("")||list.get(position).getLogo()==null){

        }else {
            }*/
        ImageLoader.getInstance().displayImage(Config.web.picUrl+list.get(position).getLogo()
                ,infoHolder.mHead, AppConfig.DEFAULT_MERCHANT_TITLE);

        infoHolder.tv2.setText(list.get(position).getContent());
        infoHolder.tv3.setText(list.get(position).getSenddate());
        return convertView;
    }
    class InfoHolder{
        RoundImageView mHead;
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv_info_time;
        TextView tv_information_coupon_redpackge;
        LinearLayout ll_information_coupon;
    }
}
