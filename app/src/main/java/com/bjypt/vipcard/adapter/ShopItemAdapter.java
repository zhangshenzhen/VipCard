package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.OrderContentData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/30
 * Use by
 */
public class ShopItemAdapter extends MyBaseAdapter<OrderContentData> {

    public static Map<Integer,Boolean> params = new HashMap<>();


    public ShopItemAdapter(List<OrderContentData> list, Context mContext) {
        super(list, mContext);
    }

    public Map<String,Integer> getClickCount(){
        return null;
    }

    @Override
    public View initView(final int position, View convertView, final ViewGroup parent) {
        ShopItemHolder shopItemHolder;
        if(convertView == null){
            shopItemHolder = new ShopItemHolder();
            convertView = inflater.inflate(R.layout.shop_comment_item,null);
            shopItemHolder.mRadioButton = (CheckBox) convertView.findViewById(R.id.shop_comment_zan);
            shopItemHolder.mItemName = (TextView) convertView.findViewById(R.id.shop_comment_name);
            convertView.setTag(shopItemHolder);
        }else{
            shopItemHolder = (ShopItemHolder) convertView.getTag();
        }
        shopItemHolder.mItemName.setText(list.get(position).getDetailName());
        params.put(position,false);

        shopItemHolder.mRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    params.put(position,true);
                }else{
                    params.put(position,false);
                }
            }
        });
        return convertView;
    }

    public  class ShopItemHolder{
         CheckBox mRadioButton;
        TextView mItemName;
    }
}
