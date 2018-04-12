package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.linkagemenu.SectionedBaseAdapter;
import com.bjypt.vipcard.model.ProductTypeListBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/28
 * Use by 点菜Adapter
 */
public class DishChooseAdapter extends SectionedBaseAdapter {

    private Context mContext;
    private String[] leftStr;
    private List<ProductTypeListBean> allList = new ArrayList<>();

    public DishChooseAdapter(Context context, List<ProductTypeListBean> leftStr){

        String [] firstStr = new String[leftStr.size()];
        allList = leftStr;
        for(int i = 0;i<leftStr.size();i++){
            firstStr[i] =  leftStr.get(i).getTypename();
        }

        this.mContext = context;
        this.leftStr = firstStr;
    }
    @Override
    public Object getItem(int section, int position) {
        return allList.get(section).getProductList().get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return leftStr.length;
    }

    @Override
    public int getCountForSection(int section) {
        return allList.get(section).getProductList().size();
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        final DishesChooseHolder dishesChooseHolder;

        if (convertView == null) {
            dishesChooseHolder = new DishesChooseHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dishes_list_item,null);
            dishesChooseHolder.mRela = (RelativeLayout) convertView.findViewById(R.id.dishes_rela);
            dishesChooseHolder.mDishesImg = (ImageView) convertView.findViewById(R.id.dishes_img);
            dishesChooseHolder.mDishesName = (TextView) convertView.findViewById(R.id.dishes_name);
            dishesChooseHolder.mSellNum = (TextView) convertView.findViewById(R.id.sell_num_mouth);
            dishesChooseHolder.mDishesPrice = (TextView) convertView.findViewById(R.id.dishes_price);
            dishesChooseHolder.mCbReduce = (ImageView) convertView.findViewById(R.id.dishes_reduce);
            dishesChooseHolder.mCbAdd = (ImageView) convertView.findViewById(R.id.dishes_add);
            dishesChooseHolder.mDishesNum = (TextView) convertView.findViewById(R.id.dishes_num);
            convertView.setTag(dishesChooseHolder);
        }
        else {

            dishesChooseHolder = (DishesChooseHolder) convertView.getTag();
    }

        dishesChooseHolder.mDishesImg.setImageResource(R.mipmap.ad_bg);
        if(!allList.get(section).getProductList().get(position).getProductImgUrl().equals("")){
            ImageLoader.getInstance(). displayImage(Config.web.picUrl + allList.get(section).getProductList().get(position).getProductImgUrl(), dishesChooseHolder.mDishesImg, AppConfig.DEFAULT_IMG_OPTIONS_AD);
        }
        dishesChooseHolder.mDishesName.setText("【" + allList.get(section).getProductList().get(position).getProductName() + "】");
        dishesChooseHolder.mSellNum.setText("月售" + allList.get(section).getProductList().get(position).getMonthSelledNum());
        dishesChooseHolder.mDishesPrice.setText("¥"+allList.get(section).getProductList().get(position).getEarnestMoney());
        Log.e("tyz", "section:" + section+"position："+position);

        dishesChooseHolder.mCbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*dishesChooseHolder.mCbReduce.setVisibility(View.VISIBLE);
                dishesChooseHolder.mDishesNum.setVisibility(View.VISIBLE);

                dishesChooseHolder.mCbReduce.setImageResource(R.mipmap.dishes_reduce_click);
                dishesChooseHolder.mCbAdd.setImageResource(R.mipmap.dishes_add_click);
                int num = Integer.parseInt(dishesChooseHolder.mDishesNum.getText().toString());
                num++;
                dishesChooseHolder.mDishesNum.setText(num + "");*/

            }
        });

        /*dishesChooseHolder.mCbReduce[section].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(dishesChooseHolder.mDishesNum[section].getText().toString()) > 1) {
                    int num = Integer.parseInt(dishesChooseHolder.mDishesNum[section].getText().toString());
                    num--;
                    dishesChooseHolder.mDishesNum[section].setText(num + "");
                } else if (Integer.parseInt(dishesChooseHolder.mDishesNum[section].getText().toString()) == 1) {

                    dishesChooseHolder.mDishesNum[section].setText(0 + "");
                    dishesChooseHolder.mCbReduce[section].setVisibility(View.INVISIBLE);
                    dishesChooseHolder.mDishesNum[section].setVisibility(View.INVISIBLE);
                    dishesChooseHolder.mCbAdd[section].setImageResource(R.mipmap.dishes_add);
                }
            }
        });*/



        dishesChooseHolder.mRela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(mContext, DetailDishesActivity.class);
                intent.putExtra("pkproduct", allList.get(section).getProductList().get(position).getPkproduct());
                Log.e("tyz", "pkID:" + allList.get(section).getProductList().get(position).getPkproduct());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
    class DishesChooseHolder{
        private RelativeLayout mRela;
        private ImageView mDishesImg;
        private TextView mDishesName,mSellNum,mDishesPrice;
        private TextView mDishesNum ;
        private ImageView mCbReduce ;
        private ImageView  mCbAdd ;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.dishes_header_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setClickable(false);
        ((TextView) layout.findViewById(R.id.textItem)).setText(leftStr[section]);
        return layout;
    }
}
