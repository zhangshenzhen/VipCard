package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.UnPayOrderBean;

import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public class ListDetailelvAdapter extends MyBaseAdapter<UnPayOrderBean> {
    public ListDetailelvAdapter(List<UnPayOrderBean> list, Context mContext) {
        super(list, mContext);
    }

    @Override
    public View initView(int position, View convertView, ViewGroup parent) {

        listDetailsHolder mHolder=new listDetailsHolder();
        if (convertView == null) {

            mHolder=new listDetailsHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_listdetails_item, null);

            mHolder.tv_goodsName= (TextView) convertView.findViewById(R.id.tv_goodsName);
            mHolder.tv_goodsNum= (TextView) convertView.findViewById(R.id.tv_goodsNum);
            mHolder.tv_goodsPrice= (TextView) convertView.findViewById(R.id.tv_goodsPrice);

            convertView.setTag(mHolder);
        } else {

            mHolder= (listDetailsHolder) convertView.getTag();
        }

        mHolder.tv_goodsPrice.setText(list.get(position).getGoodsPrice()+"元");
        mHolder.tv_goodsNum.setText(list.get(position).getGoodsNum()+"份");
        mHolder.tv_goodsName.setText(list.get(position).getGoodsName());

        return convertView;
    }

    public class listDetailsHolder {
        private TextView tv_goodsName, tv_goodsNum, tv_goodsPrice;
    }
}
