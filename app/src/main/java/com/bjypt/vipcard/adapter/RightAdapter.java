package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.DetailDishesActivity;
import com.bjypt.vipcard.activity.TaoCanActivity;
import com.bjypt.vipcard.assistant.onCallBackListener;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.linkagemenu.SectionedBaseAdapter;
import com.bjypt.vipcard.model.ProductListBean;
import com.bjypt.vipcard.model.ProductTypeListBean;
import com.bjypt.vipcard.view.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.List;

import static com.bjypt.vipcard.R.id.right_adapter_view;

/**
 * Created by Administrator on 2017/4/13.
 */

public class RightAdapter extends SectionedBaseAdapter {

    private List<ProductTypeListBean> productTypes;
    private LayoutInflater mInflater;
    private Context context;
    private String rechargeActivity;     //是否是充值活动（1：是  0：否）
    private String isHaveBranch;         //是否存在分店
    private String merchantName;         //商家名称
    private String preorderStartPrice;  //最小起送价格

    private HolderClickListener mHolderClickListener;
    private onCallBackListener callBackListener;

    public void setCallBackListener(onCallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    public RightAdapter(Context context, List<ProductTypeListBean> productTypes, String rechargeActivity,
                        String isHaveBranch, String merchantName, String preorderStartPrice) {

        this.context = context;
        this.productTypes = productTypes;
        this.rechargeActivity = rechargeActivity;
        this.isHaveBranch = isHaveBranch;
        this.merchantName = merchantName;
        this.preorderStartPrice = preorderStartPrice;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public Object getItem(int section, int position) {
        return productTypes.get(section).getProductList().get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        //左边有多少分类
        return productTypes.size();
    }

    @Override
    public int getCountForSection(int section) {
        //右边每一个分类有几件产品
        return productTypes.get(section).getProductList().size();
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.right_adapter_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mDishesName = (TextView) convertView.findViewById(R.id.dishes_name);
            viewHolder.mSellNum = (TextView) convertView.findViewById(R.id.sell_num_mouth);
            viewHolder.mDishesPrice = (TextView) convertView.findViewById(R.id.dishes_price);
            viewHolder.mVipCharge = (TextView) convertView.findViewById(R.id.tv_vipcharge);
            viewHolder.mplatformyouhui = (TextView) convertView.findViewById(R.id.tv_platformyouhui);
            viewHolder.mPlatformLinear = (RelativeLayout) convertView.findViewById(R.id.platform_pay_linear);
            viewHolder.mMerchantLinear = (RelativeLayout) convertView.findViewById(R.id.merchant_pay_linear);
            viewHolder.mDishesImg = (ImageView) convertView.findViewById(R.id.dishes_img);
            viewHolder.mRela = (RelativeLayout) convertView.findViewById(R.id.dishes_rela);
            viewHolder.mCbReduce = (ImageView) convertView.findViewById(R.id.dishes_reduce);
            viewHolder.mCbIncrease = (ImageView) convertView.findViewById(R.id.dishes_add);
            viewHolder.mDishesNum = (TextView) convertView.findViewById(R.id.dishes_num);
            viewHolder.lineView = convertView.findViewById(right_adapter_view);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ProductListBean product = productTypes.get(section).getProductList().get(position);

        //商品名称
        if (product.getProductName() != null) {
            viewHolder.mDishesName.setText(product.getProductName());
        }

        //商品描述
        if (product.getProductdesc() != null) {
            viewHolder.mSellNum.setText(product.getProductdesc());
        }
        //会员价
        if (product.getProductPrice() != null) {
            viewHolder.mDishesPrice.setText("会员价¥" + product.getProductPrice());
        }
//        //商品原价
//        viewHolder.mCostPrice.setText("原价:" + product.getOrginprice());
        if (BigDecimal.valueOf(Double.parseDouble(product.getRechargeprice())).compareTo(BigDecimal.valueOf(0)) == 0 && BigDecimal.valueOf(Double.parseDouble(product.getPlatformprice())).compareTo(BigDecimal.valueOf(0)) == 0){
            viewHolder.lineView.setVisibility(View.GONE);
        }else {
            viewHolder.lineView.setVisibility(View.VISIBLE);
        }

        if (BigDecimal.valueOf(Double.parseDouble(product.getRechargeprice())).compareTo(BigDecimal.valueOf(0)) == 0) {
            viewHolder.mMerchantLinear.setVisibility(View.GONE);

        } else {
            viewHolder.mMerchantLinear.setVisibility(View.VISIBLE);
            viewHolder.mVipCharge.setText("会员充值价¥" + product.getRechargeprice());
        }

        if (BigDecimal.valueOf(Double.parseDouble(product.getPlatformprice())).compareTo(BigDecimal.valueOf(0)) == 0) {
            viewHolder.mPlatformLinear.setVisibility(View.GONE);
        } else {
            viewHolder.mPlatformLinear.setVisibility(View.VISIBLE);
            viewHolder.mplatformyouhui.setText("平台补贴价¥" + product.getPlatformprice());
        }

        if (product.getProductImgUrl() == null || product.getProductImgUrl().equals("")) {
            viewHolder.mDishesImg.setImageResource(R.mipmap.product_bg_);
        } else {
            int temp = product.getProductImgUrl().indexOf("@");
            if (temp < 0) {
                temp = product.getProductImgUrl().length();
            }
            ImageLoader.getInstance().displayImage(Config.web.picUrl + product.getProductImgUrl().substring(0, temp), viewHolder.mDishesImg, AppConfig.DEFAULT_IMG_PRODUCT_BG);
        }
        if (product.getNumber() > 0) {
            viewHolder.mCbReduce.setVisibility(View.VISIBLE);
            viewHolder.mDishesNum.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mCbReduce.setVisibility(View.GONE);
            viewHolder.mDishesNum.setVisibility(View.GONE);
        }

        viewHolder.mCbIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                product.setNumber(product.getNumber() + 1);
                viewHolder.mDishesNum.setText(product.getNumber() + "");
                if (callBackListener != null) {
                    callBackListener.updateProduct(product, "1");
                }
                notifyDataSetChanged();
                if (mHolderClickListener != null) {
                    int[] start_location = new int[2];
                    //获取点击商品图片的位置
                    viewHolder.mDishesNum.getLocationInWindow(start_location);
                    //复制一个新的商品图标
                    Drawable drawable = context.getResources().getDrawable(R.drawable.adddetail);
                    mHolderClickListener.onHolderClick(drawable, start_location);
                }
            }
        });

        viewHolder.mCbReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = product.getNumber();
                if (num > 0) {
                    product.setNumber(product.getNumber() - 1);
                    viewHolder.mDishesNum.setText(product.getNumber() + "");
                    if (callBackListener != null) {
                        callBackListener.updateProduct(product, "2");
                    }
                }
                notifyDataSetChanged();
                if (num <= 0) {
                    viewHolder.mCbReduce.setVisibility(View.GONE);
                    viewHolder.mDishesNum.setVisibility(View.GONE);
                } else {
                    viewHolder.mCbReduce.setVisibility(View.VISIBLE);
                    viewHolder.mDishesNum.setVisibility(View.VISIBLE);
                }
            }
        });

        viewHolder.mDishesNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    //此处为得到焦点时的处理内容
                } else {
                    //此处为失去焦点时的处理内容
                    int shoppingNum = Integer.parseInt(viewHolder.mDishesNum.getText().toString());
                }
            }
        });

        viewHolder.mRela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (product.getIspackage().equals("Y")) {
                    Intent intent = new Intent(context, TaoCanActivity.class);
                    intent.putExtra("pkproduct", product.getPkproduct());
                    intent.putExtra("position", position);//当前点击的position
                    Log.e("tyz", "position:" + position);
                    intent.putExtra("num", product.getNumber());//当前点击的position选中的菜品数量
                    intent.putExtra("isSpecGoods", product.getIsSpecGoods());//是否有规格
                    intent.putExtra("imageUrl", product.getProductImgUrl());//菜品图片
                    intent.putExtra("merchantName", merchantName);//商家名称
                    intent.putExtra("dishesName", product.getProductName());//菜品名称
                    intent.putExtra("mouthSell", product.getMonthSelledNum());//月售
                    intent.putExtra("dishesGoodsNum", product.getJudgeAllNums());//好评量
                    intent.putExtra("dishesPrice", product.getProductPrice());//菜品单价
                    intent.putExtra("discount", product.getDiscount());//折扣率
                    intent.putExtra("dishesIntroduce", isHaveBranch);//是否存在分店
                    intent.putExtra("payment", product.getEarnestMoney());//定金
                    intent.putExtra("rechargeActivity", rechargeActivity);//是否是充值活动（1：是  0：否）
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, DetailDishesActivity.class);
                    intent.putExtra("pkproduct", product.getPkproduct());
                    intent.putExtra("dishesName", product.getProductName());//菜品名称
                    intent.putExtra("dishesUrl", product.getProductImgUrl());//商品图片
                    intent.putExtra("mouthSell", product.getMonthSelledNum());//月售
                    intent.putExtra("dishesGoodsNum", product.getJudgeAllNums());//好评量
                    intent.putExtra("isSpecGoods", product.getIsSpecGoods());//是否有规格
                    intent.putExtra("dishesPrice", product.getProductPrice());//菜品单价
                    intent.putExtra("dishesIntroduce", isHaveBranch);//是否存在分店
                    intent.putExtra("payment", product.getEarnestMoney());//定金
                    intent.putExtra("discount", product.getDiscount());//折扣率
                    intent.putExtra("preorderStartPrice", preorderStartPrice);//最小起送价格
                    intent.putExtra("position", position);//当前点击的position
                    intent.putExtra("num", product.getNumber());//当前点击的position选中的菜品数量
                    intent.putExtra("isSpecGoods", product.getIsSpecGoods());
                    intent.putExtra("rechargeActivity", rechargeActivity);//是否是充值活动（1：是  0：否）
                    context.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    /**
     * 右侧title的设置
     */
    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {

        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflater.inflate(R.layout.header_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setClickable(false);
        //右侧title设值
        ((TextView) layout.findViewById(R.id.textItem)).setText(productTypes.get(section).getTypename());
        return layout;
    }


    class ViewHolder {
        RelativeLayout mRela;
        ImageView mDishesImg;
        TextView mDishesName, mSellNum, mDishesPrice;
        TextView mDishesNum;
        ImageView mCbReduce;
        ImageView mCbIncrease;
        TextView mVipCharge;
        TextView mplatformyouhui;
        RelativeLayout mPlatformLinear;
        RelativeLayout mMerchantLinear;
        View lineView;
    }

    public void SetOnSetHolderClickListener(HolderClickListener holderClickListener) {
        this.mHolderClickListener = holderClickListener;
    }

    public interface HolderClickListener {
        public void onHolderClick(Drawable drawable, int[] start_location);
    }
}
