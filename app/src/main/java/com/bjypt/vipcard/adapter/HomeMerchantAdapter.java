package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.MerchantCategroyBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/6
 * Use by 首页商家列表Adapter
 */
public class HomeMerchantAdapter  extends MyBaseAdapter<MerchantCategroyBean>{
    private Context context;

    public HomeMerchantAdapter(Context mContext) {
        super(mContext);
    }
    public HomeMerchantAdapter(Context mContext,List<MerchantCategroyBean > list1) {
        super(list1,mContext);
    }
    @Override
    public View initView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Log.e("woyaokk", "notifyDataSetChanged");
//        if (null==convertView){
            holder= new ViewHolder();
            convertView = inflater.inflate(R.layout.merchant_category_listview_item,null);
            holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            //holder.tv_vip = (TextView)convertView.findViewById(R.id.tv_vip);
            holder.tv_youhui = (TextView)convertView.findViewById(R.id.tv_youhui);
            holder.tv_jianjie = (TextView)convertView.findViewById(R.id.tv_jianjie);

            holder.tv_zhekou = (TextView)convertView.findViewById(R.id.tv_zhekou);
            holder.tv_yuanjia = (TextView)convertView.findViewById(R.id.tv_yuanjia);
            holder.tv_juli = (TextView)convertView.findViewById(R.id.tv_juli);
//        holder.tv_pingjia = (TextView)convertView.findViewById(R.id.tv_pingjia);
//            holder.star_1 = (ImageView)convertView.findViewById(R.id.stars_1);
//            holder.star_2 = (ImageView)convertView.findViewById(R.id.stars_2);
//            holder.star_3 = (ImageView)convertView.findViewById(R.id.stars_3);
//            holder.star_4 = (ImageView)convertView.findViewById(R.id.stars_4);
//            holder.star_5 = (ImageView)convertView.findViewById(R.id.stars_5);
            holder.iv_photo = (ImageView)convertView.findViewById(R.id.iv_merchant_photo);
            holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            holder.tv_discount = (TextView) convertView.findViewById(R.id.tv_discount);

//            convertView.setTag(holder);
//        }else{
//            holder = (ViewHolder) convertView.getTag();
//        }

        holder.tv_name.setText(list.get(position).getMerchantName());
//        if(list.get(position).getPingjia()==0){
//            holder.tv_pingjia.setText("0");
//        }else{
//            holder.tv_pingjia.setText(list.get(position).getPingjia()+"");
//        }

        if(list.get(position).getMaxDiscount()==null||list.get(position).getMaxDiscount().equals("")){
            holder.tv_discount.setVisibility(View.GONE);
        }else{
            holder.tv_discount.setText(list.get(position).getMaxDiscount());
        }

        if(list.get(position).getVipzuan()==null||list.get(position).getVipzuan().equals("")){
           // holder.tv_vip.setVisibility(View.GONE);
        }else{
           // holder.tv_vip.setText(list.get(position).getVipzuan());
        }

        if(list.get(position).getYouhui()==null||list.get(position).getYouhui().equals("")){
            holder.tv_youhui.setVisibility(View.GONE);
        }else{
            holder.tv_youhui.setText(list.get(position).getYouhui());
        }


        holder.tv_jianjie.setText(list.get(position).getJianjie());
        holder.tv_juli.setText(list.get(position).getJuli());

        holder.tv_zhekou.setText(list.get(position).getZhekou());
        holder.tv_yuanjia.setText(list.get(position).getYuanjai() + "");

        holder.iv_photo.setImageResource(R.mipmap.merchant_bg_);
        if (!"".equals(list.get(position).getMerchantPhoto())){
            ImageLoader.getInstance(). displayImage(Config.web.picUrl + list.get(position).getMerchantPhoto(), holder.iv_photo, AppConfig.DEFAULT_IMG_MERCHANT_BG);
        }

//        if (list.get(position).getStartLevel()>=5){
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_true);
//            holder.star_3.setImageResource(R.drawable.stars_true);
//            holder.star_4.setImageResource(R.drawable.stars_true);
//            holder.star_5.setImageResource(R.drawable.stars_true);
//        }else if (list.get(position).getStartLevel()>=4&&list.get(position).getStartLevel()<5){
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_true);
//            holder.star_3.setImageResource(R.drawable.stars_true);
//            holder.star_4.setImageResource(R.drawable.stars_true);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        }else if (list.get(position).getStartLevel()>=3&&list.get(position).getStartLevel()<4){
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_true);
//            holder.star_3.setImageResource(R.drawable.stars_true);
//            holder.star_4.setImageResource(R.drawable.stars_flase);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        }else if (list.get(position).getStartLevel()>=2&&list.get(position).getStartLevel()<3){
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_true);
//            holder.star_3.setImageResource(R.drawable.stars_flase);
//            holder.star_4.setImageResource(R.drawable.stars_flase);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        }else if (list.get(position).getStartLevel()>=1&&list.get(position).getStartLevel()<2){
//            holder.star_1.setImageResource(R.drawable.stars_true);
//            holder.star_2.setImageResource(R.drawable.stars_flase);
//            holder.star_3.setImageResource(R.drawable.stars_flase);
//            holder.star_4.setImageResource(R.drawable.stars_flase);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        }else if (list.get(position).getStartLevel()<1){
//            holder.star_1.setImageResource(R.drawable.stars_flase);
//            holder.star_2.setImageResource(R.drawable.stars_flase);
//            holder.star_3.setImageResource(R.drawable.stars_flase);
//            holder.star_4.setImageResource(R.drawable.stars_flase);
//            holder.star_5.setImageResource(R.drawable.stars_flase);
//        }
        return convertView;
    }
    class ViewHolder{
        TextView tv_name;//商家名称
        TextView tv_jianjie;//商家简介
     //   TextView tv_vip;//会员级别
        TextView tv_youhui;//优惠
        ImageView star_1,star_2,star_3,star_4,star_5,iv_photo;
        TextView tv_pingjia;//有多少评价
        TextView tv_yuanjia;//原价
        TextView tv_zhekou;//打折
        TextView tv_juli;//距离
        TextView tv_discount;


    }
}
