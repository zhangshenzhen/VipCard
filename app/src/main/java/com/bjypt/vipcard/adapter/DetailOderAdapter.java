package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.DetailOrderListBean;

import java.util.List;

/**
 * Created by Administrator on 2016/3/31.
 */
public class DetailOderAdapter extends BaseAdapter {
    private Context context;
    private List<DetailOrderListBean> list;
    public DetailOderAdapter(Context context, List<DetailOrderListBean> list) {
        this.context = context;
        this.list = list;
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
        ViewHoder hoder = null;
        if (convertView==null){
            hoder = new ViewHoder();
            convertView = View.inflate(context, R.layout.layout_taocan_item,null);
            hoder.name =(TextView) convertView.findViewById(R.id.tv_item_name);
            hoder.price =(TextView) convertView.findViewById(R.id.tv_item_price);
            hoder.fen =(TextView) convertView.findViewById(R.id.tv_item_fen);
            hoder.tv_item_taocan = (TextView) convertView.findViewById(R.id.tv_item_taocan);
            convertView.setTag(hoder);
        }
            hoder = (ViewHoder)convertView.getTag();

       if ("Y".equals(list.get(position).getIspackage())){
         hoder.tv_item_taocan.setVisibility(View.VISIBLE);
       }
           hoder.name.setText(list.get(position).getProductName());
           hoder.price.setText(list.get(position).getProductPrice()+"");
           hoder.fen.setText(list.get(position).getUnit());
        return convertView;
    }
    class ViewHoder{
        TextView name;
        TextView fen;
        TextView price;
        TextView tv_item_taocan;
    }
}
