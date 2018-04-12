package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.DetailDishesActivity;
import com.bjypt.vipcard.activity.SubscribeDishesActivity;
import com.bjypt.vipcard.activity.TaoCanActivity;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.DishesCarBean;
import com.bjypt.vipcard.model.ProductListBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinia.orderlang.utils.StringUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/8
 * Use by
 */
public class DetailsChooseAdater extends MyBaseAdapter<ProductListBean> {

    private Boolean is = false;
    public static Map<Integer, Integer> numMap;
    private int singleMoney;
    private String isHaveBranch;
    private String preorderStartPrice;
    public static int FLAG_DETAILS_ADAPTER = 0;
    private String merchantName;
    private String rechargeActivity;//是否是充值活动（1：是  0：否）

    public DetailsChooseAdater(List<ProductListBean> list, Context mContext, String isHaveBranch, String preorderStartPrice, String merchantName,String rechargeActivity) {
        super(list, mContext);
        this.preorderStartPrice = preorderStartPrice;
        this.isHaveBranch = isHaveBranch;
        this.merchantName = merchantName;
        this.rechargeActivity = rechargeActivity;
        numMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            numMap.put(i, 0);
        }
    }

    @Override
    public View initView(final int position, View convertView, ViewGroup parent) {
        final DetailsHolder dishesChooseHolder;

        if (convertView == null) {
            dishesChooseHolder = new DetailsHolder();
            convertView = inflater.inflate(R.layout.dishes_list_item, null);
            dishesChooseHolder.mRela = (RelativeLayout) convertView.findViewById(R.id.dishes_rela);
            dishesChooseHolder.mDishesImg = (ImageView) convertView.findViewById(R.id.dishes_img);
            dishesChooseHolder.mDishesName = (TextView) convertView.findViewById(R.id.dishes_name);
            dishesChooseHolder.mSellNum = (TextView) convertView.findViewById(R.id.sell_num_mouth);
            dishesChooseHolder.mDishesPrice = (TextView) convertView.findViewById(R.id.dishes_price);
            dishesChooseHolder.mCbReduce = (ImageView) convertView.findViewById(R.id.dishes_reduce);
            dishesChooseHolder.mCbAdd = (ImageView) convertView.findViewById(R.id.dishes_add);
            dishesChooseHolder.mDishesNum = (TextView) convertView.findViewById(R.id.dishes_num);

            dishesChooseHolder.mVipCharge= (TextView) convertView.findViewById(R.id.tv_vipcharge);
            dishesChooseHolder.mCostPrice= (TextView) convertView.findViewById(R.id.tv_costPrice);
            dishesChooseHolder.mplatformyouhui= (TextView) convertView.findViewById(R.id.tv_platformyouhui);
            dishesChooseHolder.mPlatformLinear = (RelativeLayout) convertView.findViewById(R.id.platform_pay_linear);
            dishesChooseHolder.mMerchantLinear = (RelativeLayout) convertView.findViewById(R.id.merchant_pay_linear);

            convertView.setTag(dishesChooseHolder);
        } else {
            dishesChooseHolder = (DetailsHolder) convertView.getTag();
        }


        Log.e("CXY", "list.get(position).getProductImgUrl():" + list.get(position).getProductImgUrl());
        if (list.get(position).getProductImgUrl() == null || list.get(position).getProductImgUrl().equals("")) {
            dishesChooseHolder.mDishesImg.setImageResource(R.mipmap.product_bg_);
        } else {
            int temp = list.get(position).getProductImgUrl().indexOf("@");
            if(temp < 0){
                temp = list.get(position).getProductImgUrl().length();
            }
            ImageLoader.getInstance().displayImage(Config.web.picUrl + list.get(position).getProductImgUrl().substring(0,temp), dishesChooseHolder.mDishesImg, AppConfig.DEFAULT_IMG_PRODUCT_BG);
        }

        if (list.get(position).getProductName() != null) {
            dishesChooseHolder.mDishesName.setText(list.get(position).getProductName());
        }
        if (list.get(position).getProductdesc() != null) {
            dishesChooseHolder.mSellNum.setText( list.get(position).getProductdesc());
        }
        if (list.get(position).getProductPrice() != null) {
            dishesChooseHolder.mDishesPrice.setText("会员价¥" + list.get(position).getProductPrice());
        }

        dishesChooseHolder.mCostPrice.setText("原价:"+list.get(position).getOrginprice());
        Log.e("HHHH","list.get(position).getRechargeprice():"+list.get(position).getRechargeprice());
        if(BigDecimal.valueOf(Double.parseDouble(list.get(position).getRechargeprice())).compareTo(BigDecimal.valueOf(0))==0){
            dishesChooseHolder.mMerchantLinear.setVisibility(View.GONE);
        }else{
            dishesChooseHolder.mMerchantLinear.setVisibility(View.VISIBLE);
            dishesChooseHolder.mVipCharge.setText("会员充值价¥"+list.get(position).getRechargeprice());
        }

        if(BigDecimal.valueOf(Double.parseDouble(list.get(position).getPlatformprice())).compareTo(BigDecimal.valueOf(0))==0){
            dishesChooseHolder.mPlatformLinear.setVisibility(View.GONE);
        }else{
            dishesChooseHolder.mPlatformLinear.setVisibility(View.VISIBLE);
            dishesChooseHolder.mplatformyouhui.setText("平台补贴价¥"+list.get(position).getPlatformprice());
        }


        //此处用于保存数量 防止滑动时自动刷新
        dishesChooseHolder.mDishesNum.setText(numMap.get(position) + "");

        if (FLAG_DETAILS_ADAPTER == 1) {
//            list.get(position).isBoolean = true;
            FLAG_DETAILS_ADAPTER = 0;
        } else if (FLAG_DETAILS_ADAPTER == 2) {
//            list.get(TaoCanActivity.position).isBoolean = true;
            FLAG_DETAILS_ADAPTER = 0;
        }

        //刷新界面
        refreshAdapter(position, dishesChooseHolder.mCbAdd, dishesChooseHolder.mDishesNum, dishesChooseHolder.mCbReduce);
        if (is == false) {
            refreshView();
            is = true;
        }


        dishesChooseHolder.mCbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int num = numMap.get(position);
                num++;
                dishesChooseHolder.mCbReduce.setVisibility(View.VISIBLE);
                dishesChooseHolder.mDishesNum.setVisibility(View.VISIBLE);


                dishesChooseHolder.mCbReduce.setImageResource(R.mipmap.dishes_reduce_click);
                dishesChooseHolder.mCbAdd.setImageResource(R.mipmap.dishes_add_click);

                dishesChooseHolder.mDishesNum.setText(num + "");

                if (list.get(position).getIspackage().equals("Y")) {
                    Log.e("cxy", "success");
                    if (num == 1) {
                        SubscribeDishesActivity.carList.add(new DishesCarBean(list.get(position).getPkproduct(), list.get(position).getProductName(), Double.parseDouble(list.get(position).getProductPrice()), Double.parseDouble(list.get(position).getEarnestMoney()), "", "", "", num, list.get(position).getIspackage()));
                    } else {
                        for (int i = 0; i < SubscribeDishesActivity.carList.size(); i++) {
                            if (SubscribeDishesActivity.carList.get(i).getPkId().equals(list.get(position).getPkproduct()) && list.get(position).isBoolean) {
                                SubscribeDishesActivity.carList.get(i).setDishesNum(num);
                                Log.e("xy", "----->aaaaaaddddd" + i);
                            } else {
                                //  SubscribeDishesActivity.carList.add(new DishesCarBean(list.get(position).getPktypeid(),list.get(position).getProductName(),22,"这个好吃",num));
                                Log.e("xy", "----->bbbbb");
                            }
                        }
                    }
                } else {
                    if (num == 1) {
                        SubscribeDishesActivity.carList.add(new DishesCarBean(list.get(position).getPkproduct(), list.get(position).getProductName(), Double.parseDouble(list.get(position).getProductPrice()), Double.parseDouble(list.get(position).getEarnestMoney()), "", "2", "", num, list.get(position).getIspackage()));
                    } else {
                        for (int i = 0; i < SubscribeDishesActivity.carList.size(); i++) {
                            if (SubscribeDishesActivity.carList.get(i).getPkId().equals(list.get(position).getPkproduct()) && list.get(position).isBoolean) {
                                SubscribeDishesActivity.carList.get(i).setDishesNum(num);
                                Log.e("xy", "----->aaaaaaddddd" + i);
                            } else {
                                // SubscribeDishesActivity.carList.add(new DishesCarBean(list.get(position).getPktypeid(),list.get(position).getProductName(),22,"这个好吃",num));
                                Log.e("xy", "----->bbbbb");
                            }
                        }
                    }
                }

                list.get(position).isBoolean = true;
                Log.e("woyaokk", "numMap:" + num);
                numMap.put(position, num);

                refreshView();
            }
        });

        dishesChooseHolder.mCbReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numMap.get(position) > 0) {
                    int num = numMap.get(position);
                    num--;
                    Log.i("aaa", "num=" + num);
                    if (num <= 0) {

                        list.get(position).isBoolean = false;
                        for (int m = 0; m < SubscribeDishesActivity.carList.size(); m++) {
                            if (SubscribeDishesActivity.carList.get(m).getPkId().equals(list.get(position).getPkproduct())) {
                                SubscribeDishesActivity.carList.remove(m);
                            }
                        }

                    } else {

                        dishesChooseHolder.mDishesNum.setText(num + "");
                        for (int i = 0; i < SubscribeDishesActivity.carList.size(); i++) {

                            if (SubscribeDishesActivity.carList.get(i).getPkId().equals(list.get(position).getPkproduct())) {

                                SubscribeDishesActivity.carList.get(i).setDishesNum(num);
                                Log.e("xy", "----->aaaaaa" + i);
                            } else {
                                Log.e("xy", "----->bbbbb");
                            }
                        }
                        list.get(position).isBoolean = true;

                    }


                    numMap.put(position, num);
                    refreshView();

                } else {
                    Toast.makeText(mContext, "请添加商品", Toast.LENGTH_LONG).show();
                }

            }
        });


        dishesChooseHolder.mRela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (list.get(position).getIspackage().equals("Y")) {

                    Intent intent = new Intent(mContext, TaoCanActivity.class);
                    intent.putExtra("pkproduct", list.get(position).getPkproduct());
                    intent.putExtra("position", position);//当前点击的position
                    Log.e("tyz", "position:" + position);
                    intent.putExtra("num", numMap.get(position));//当前点击的position选中的菜品数量
                    intent.putExtra("isSpecGoods", list.get(position).getIsSpecGoods());//是否有规格
                    intent.putExtra("imageUrl", list.get(position).getProductImgUrl());//菜品图片
                    intent.putExtra("merchantName", merchantName);//商家名称
                    intent.putExtra("dishesName", list.get(position).getProductName());//菜品名称
                    intent.putExtra("mouthSell", list.get(position).getMonthSelledNum());//月售
                    intent.putExtra("dishesGoodsNum", list.get(position).getJudgeAllNums());//好评量
                    intent.putExtra("dishesPrice", list.get(position).getProductPrice());//菜品单价
                    intent.putExtra("discount", list.get(position).getDiscount());//折扣率
                    intent.putExtra("dishesIntroduce", isHaveBranch);//是否存在分店
                    intent.putExtra("payment", list.get(position).getEarnestMoney());//定金
                    intent.putExtra("rechargeActivity",rechargeActivity);//是否是充值活动（1：是  0：否）
                    mContext.startActivity(intent);


                } else {
                    Intent intent = new Intent(mContext, DetailDishesActivity.class);
                    intent.putExtra("pkproduct", list.get(position).getPkproduct());
                    intent.putExtra("dishesName", list.get(position).getProductName());//菜品名称
                    intent.putExtra("dishesUrl", list.get(position).getProductImgUrl());//商品图片
                    intent.putExtra("mouthSell", list.get(position).getMonthSelledNum());//月售
                    intent.putExtra("dishesGoodsNum", list.get(position).getJudgeAllNums());//好评量
                    intent.putExtra("isSpecGoods", list.get(position).getIsSpecGoods());//是否有规格
                    intent.putExtra("dishesPrice", list.get(position).getProductPrice());//菜品单价
                    intent.putExtra("dishesIntroduce", isHaveBranch);//是否存在分店
                    intent.putExtra("payment", list.get(position).getEarnestMoney());//定金
                    intent.putExtra("discount", list.get(position).getDiscount());//折扣率
                    intent.putExtra("preorderStartPrice", preorderStartPrice);//最小起送价格
                    intent.putExtra("position", position);//当前点击的position
                    intent.putExtra("num", numMap.get(position));//当前点击的position选中的菜品数量
                    intent.putExtra("isSpecGoods", list.get(position).getIsSpecGoods());
                    intent.putExtra("rechargeActivity",rechargeActivity);//是否是充值活动（1：是  0：否）
                    mContext.startActivity(intent);

                }
            }
        });


        return convertView;
    }

    public int getPosition(String pkId) {
        int positionNum = 0;
        for (int i = 0; i < list.size(); i++) {
            Log.e("tyz", "list.get(i).getPktypeid():" + list.get(i).getPktypeid() + "----->" + pkId);
            if (list.get(i).getPktypeid().equals(pkId)) {

                positionNum = i;
                return positionNum;
            }
        }
        return positionNum;
    }


    /**
     * 增加减少按钮的处理
     * */
    public void refreshAdapter(int position, ImageView iv_one, TextView tv, ImageView iv_two) {
        if (numMap.get(position) <= 0) {
            list.get(position).isBoolean = false;
        } else {
            list.get(position).isBoolean = true;
        }
        if (list.get(position).isBoolean) {
            Log.e("tyz", "position isboolena  true");

            iv_one.setImageResource(R.mipmap.dishes_add_click);

            tv.setVisibility(View.VISIBLE);
            iv_two.setVisibility(View.VISIBLE);

            iv_two.setImageResource(R.mipmap.dishes_reduce_click);

        } else {

            Log.e("tyz", "position isboolena  false");
            tv.setVisibility(View.INVISIBLE);
            iv_two.setVisibility(View.INVISIBLE);

            iv_one.setImageResource(R.mipmap.dishes_add);

        }
    }

    public void refreshView() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                SubscribeDishesActivity.totalMoney = 0;
                SubscribeDishesActivity.totalPayment = 0;

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getProductPrice() != null) {
                        SubscribeDishesActivity.totalMoney += numMap.get(i) * StringUtil.getDouble(list.get(i).getProductPrice());
                    }
                    SubscribeDishesActivity.totalPayment += numMap.get(i) * StringUtil.getDouble(list.get(i).getEarnestMoney());
                }
                SubscribeDishesActivity.handler.sendEmptyMessage(1);
            }
        }).start();
        notifyDataSetChanged();
    }


    class DetailsHolder {
        RelativeLayout mRela;
        ImageView mDishesImg;
        TextView mDishesName, mSellNum, mDishesPrice;
        TextView mDishesNum;
        ImageView mCbReduce;
        ImageView mCbAdd;
        TextView mCostPrice;
        TextView mVipCharge;
        TextView mplatformyouhui;
        RelativeLayout mPlatformLinear;
        RelativeLayout mMerchantLinear;
    }
}
