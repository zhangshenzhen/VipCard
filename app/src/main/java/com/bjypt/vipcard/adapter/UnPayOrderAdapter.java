package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.DetailOrderActivity;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.UnPayOrderBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/3/31.
 */
public class UnPayOrderAdapter extends MyBaseAdapter<UnPayOrderBean> {

    private List<UnPayOrderBean> list;
    private Context mContext;
    private LinearLayout linear;
    private boolean isShow;
    private MyClickListener mListener;
    private String falg;
    private int flag;

    //判斷是否可以連續點擊
    private int count;

    public UnPayOrderAdapter(List<UnPayOrderBean> list, Context mContext, boolean isShow, MyClickListener mListener) {
        super(list, mContext);

        this.isShow = isShow;
        this.list = list;
        this.mContext = mContext;
        this.mListener = mListener;
        Wethod.configImageLoader(mContext);
    }

    @Override
    public View initView(final int position, View convertView, ViewGroup parent) {

        UnPayOrderHoder mHoder = null;
        if (convertView == null) {

            mHoder = new UnPayOrderHoder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_lv_unpay_order_itemstyle, null);
            mHoder.tv_goodsName = (TextView) convertView.findViewById(R.id.tv_goodsName);
            mHoder.tv_goodsNum = (TextView) convertView.findViewById(R.id.tv_goodsNum);
            mHoder.tv_unPay = (TextView) convertView.findViewById(R.id.tv_unPay);
            mHoder.tv_time = (TextView) convertView.findViewById(R.id.tv_time_distable);
            mHoder.tv_goodsPrice = (TextView) convertView.findViewById(R.id.tv_goodsPrice);
            mHoder.tv_time_create = (TextView) convertView.findViewById(R.id.tv_time_create);
            mHoder.iv_goodsImg = (ImageView) convertView.findViewById(R.id.iv_goodsImg);
            mHoder.btn_affirm_order = (TextView) convertView.findViewById(R.id.btn_affirmOrder);//确认预约
            mHoder.btn_unpay_order_del = (Button) convertView.findViewById(R.id.btn_unpay_order_del);

            //即将进行删除操作
            mHoder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relative_use);
            mHoder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linear_use_Gone);
            convertView.setTag(mHoder);
        } else {
            mHoder = (UnPayOrderHoder) convertView.getTag();
        }

        if (isShow) {

            mHoder.relativeLayout.setVisibility(View.GONE);//确认按钮
            mHoder.linearLayout.setVisibility(View.VISIBLE);//删除按钮
        } else {

            mHoder.relativeLayout.setVisibility(View.VISIBLE);//确认按钮
            mHoder.linearLayout.setVisibility(View.GONE);//删除按钮
        }

        if (list.get(position).getStatus() == 21) {
            mHoder.tv_unPay.setText("待使用");
            // mHoder.relativeLayout.setVisibility(View.GONE);//确认按钮
            mHoder.btn_affirm_order.setVisibility(View.GONE);
            mHoder.linearLayout.setVisibility(View.GONE);//删除按钮
            // mHoder.btn_affirm_order.setText("确认使用");

        } else if (list.get(position).getStatus() == 22) {
            mHoder.tv_unPay.setText("待支付");
            mHoder.btn_affirm_order.setText("确认支付");

        } else if (list.get(position).getStatus() == 23) {
            mHoder.tv_unPay.setText("已消费");
            mHoder.btn_affirm_order.setText("去评论");
        } else if (list.get(position).getStatus() == 4) {
            mHoder.tv_unPay.setText("已过期");
            mHoder.btn_affirm_order.setText("已过期");
            mHoder.btn_affirm_order.setVisibility(View.GONE);
            // mHoder.relativeLayout.setVisibility(View.GONE);
        } else if (list.get(position).getStatus() == 24) {
            mHoder.tv_unPay.setText("已评论");
            mHoder.btn_affirm_order.setText("已评论");
            mHoder.btn_affirm_order.setVisibility(View.GONE);
        } else if (list.get(position).getStatus() == 11) {
            mHoder.tv_unPay.setText("拒绝退款");
            mHoder.btn_affirm_order.setText("拒绝退款");
            mHoder.btn_affirm_order.setVisibility(View.GONE);
        } else if (list.get(position).getStatus() == 10) {
            mHoder.tv_unPay.setText("退款成功");
            mHoder.btn_affirm_order.setText("退款成功");
            mHoder.btn_affirm_order.setVisibility(View.GONE);
        } else if (list.get(position).getStatus() == 9) {
            mHoder.tv_unPay.setText("退款中");
            mHoder.btn_affirm_order.setText("退款中");
            mHoder.btn_affirm_order.setVisibility(View.GONE);
        } else if (list.get(position).getStatus() == 31) {
            mHoder.tv_unPay.setText("支付中");
            mHoder.btn_affirm_order.setVisibility(View.GONE);
        } else if (list.get(position).getStatus() == 32) {
            mHoder.tv_unPay.setText("尾款支付中");
            mHoder.btn_affirm_order.setVisibility(View.GONE);
        }
//        Log.i("UnPayOrderApdater","position=="+position);
        flag = list.get(position).getStatus();
        mHoder.tv_goodsName.setText(list.get(position).getGoodsName());
        mHoder.tv_goodsNum.setText(list.get(position).getGoodsNum() + "");
        if (!"".equals(list.get(position).getTime()) && list.get(position).getTime() != null) {
            mHoder.tv_time.setText("～" + getUserDate(list.get(position).getTime()));
        }
        if (!"".equals(list.get(position).getCreatetime()) && list.get(position).getCreatetime() != null) {
            mHoder.tv_time_create.setText("订单时效：" + getUserDate(list.get(position).getCreatetime()));

        }

        mHoder.tv_goodsPrice.setText(list.get(position).getGoodsPrice() + "元");
        if ("".equals(list.get(position).getGoodsImg())) {
            mHoder.iv_goodsImg.setImageResource(R.mipmap.unload_bg);
        }
        ImageLoader.getInstance().displayImage(Config.web.picUrl + list.get(position).getGoodsImg(), mHoder.iv_goodsImg);
        mHoder.btn_affirm_order.setOnClickListener(mListener);
        mHoder.btn_unpay_order_del.setOnClickListener(mListener);
        mHoder.btn_affirm_order.setTag(position);
        mHoder.btn_unpay_order_del.setTag(position);
        //注册预约订单和删除的监听
        final View ConvertView = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count == 0) {
                    Message message = mHandler.obtainMessage();
                    message.obj = 1;
                    mHandler.sendMessageDelayed(message, 10);

                    Intent intent = new Intent(mContext, DetailOrderActivity.class);
                    intent.putExtra("preorder", list.get(position).getPreorderId());
                    intent.putExtra("status", list.get(position).getStatus());
                    intent.putExtra("pkmuser", list.get(position).getPkmuser());
                    intent.putExtra("earnestmoney", list.get(position).getEarnestmoney());
                    intent.putExtra("userRemark", list.get(position).getUserRemark());
                    Log.e("yytt", "Adapter primaryk:" + list.get(position).getPksubscbptn());
                    intent.putExtra("primaryk", list.get(position).getPksubscbptn());

                    if (!"".equals(list.get(position).getCreatetime()) && list.get(position).getCreatetime() != null) {
                        intent.putExtra("createtime", list.get(position).getCreatetime());
                    } else {
                        intent.putExtra("createtime", "");
                    }
                    if (!"".equals(list.get(position).getTime()) && list.get(position).getTime() != null) {
                        intent.putExtra("ordertime", list.get(position).getTime());
                    } else {
                        intent.putExtra("ordertime", "");
                    }
                    Log.e("liyunte", "li" + list.get(position).getStatus());
                    Log.e("liyunteee", list.get(position).getPreorderId());
                    mContext.startActivity(intent);

//                ((Activity)mContext).finish();
                }
            }
        });
        return convertView;
    }

    public class UnPayOrderHoder {

        TextView tv_goodsName, tv_goodsNum, tv_goodsPrice, tv_unPay, btn_affirm_order, tv_time, tv_time_create;
        ImageView iv_goodsImg;
        Button btn_unpay_order_del;
        RelativeLayout relativeLayout;
        LinearLayout linearLayout;
    }

    /**
     * 75      * 用于回调的抽象类
     * 76      * @author Ivan Xu
     * 77      * 2014-11-26
     * 78
     */
    public static abstract class MyClickListener implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }

        public abstract void myOnClick(int position, View v);
    }

    public void add(List<UnPayOrderBean> l) {
        if (list == null) {
            list = l;
        } else {
            list.addAll(l);

        }

    }

    public static String getUserDate(String sformat) {
        Date currentTime = new Date();
        currentTime.setTime(Long.parseLong(sformat));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            count++;
            if (count > 2) {
                count=0;
                Log.i("zc","ok");
            } else {
                Message msgs = mHandler.obtainMessage();
                msgs.obj = 1;
                mHandler.sendMessageDelayed(msgs, 1000);
            }
        }
    };
}
