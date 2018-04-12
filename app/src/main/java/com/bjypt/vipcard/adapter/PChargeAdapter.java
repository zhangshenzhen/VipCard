package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.PchargeBean;

import java.util.List;

/**
 * Created by User on 2016/6/7.
 */
public class PChargeAdapter extends BaseAdapter{

    private Context context;
    private List<PchargeBean> list;

    private int selectIndex;

    public PChargeAdapter(Context context, List<PchargeBean> list){

        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder;
        if(convertView==null){

            mHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_gv_pcharge_item, null);
            mHolder.mPrice= (TextView) convertView.findViewById(R.id.tv_price);
            mHolder.mAllPrice= (TextView) convertView.findViewById(R.id.tv_allprice);

            convertView.setTag(mHolder);
        }else {
            mHolder= (ViewHolder) convertView.getTag();
        }

        if(selectIndex==position){
            mHolder.mPrice.setTextColor(context.getResources().getColor(R.color.blue));
            mHolder.mAllPrice.setTextColor(context.getResources().getColor(R.color.blue));
        }else {
            mHolder.mPrice.setTextColor(context.getResources().getColor(R.color.background_gradient_start));
            mHolder.mAllPrice.setTextColor(context.getResources().getColor(R.color.godbule));
        }
        mHolder.mPrice.setText(list.get(position).getPrice()+"元");
        mHolder.mAllPrice.setText("立返"+list.get(position).getAllprice()+"元");

        return convertView;
    }

    public int selectChargePrice(int index){
        selectIndex=index;
        notifyDataSetChanged();
        return list.get(index).getPrice();
    }
    public class ViewHolder{
        TextView mPrice;
        TextView mAllPrice;
    }

}
