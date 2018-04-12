package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.TaoCanBean;

import java.util.List;

/**
 * Created by Administrator on 2016/3/31.
 */
public class TaoCanAdapter extends BaseAdapter {
    private Context context;
    private List<TaoCanBean> list_one;
    public TaoCanAdapter(Context context, List<TaoCanBean> list_one) {
        this.context = context;
        this.list_one = list_one;
    }

    @Override
    public int getCount() {
        return list_one.size();
    }

    @Override
    public Object getItem(int position) {

            return list_one.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder hoder = null;
//        if (convertView==null){
            hoder = new ViewHoder();
            convertView = View.inflate(context, R.layout.layout_taocan_item,null);
            hoder.name =(TextView) convertView.findViewById(R.id.tv_item_name);
            hoder.price =(TextView) convertView.findViewById(R.id.tv_item_price);
            hoder.fen =(TextView) convertView.findViewById(R.id.tv_item_fen);
            convertView.setTag(hoder);

//        }else{
//            hoder = (ViewHoder)convertView.getTag();
//        }
        if ("".equals(list_one.get(position).getName())||list_one.get(position).getName()==null||list_one.get(position).getFen()==null){
            convertView = View.inflate(context, R.layout.layout_taocan_two_item,null);
            TextView textView = (TextView) convertView.findViewById(R.id.tv_item_two);
            if (position==0){
                textView.setText("主食");
            }else {
                textView.setText("其他");
            }

        }else {
            hoder.name.setText(list_one.get(position).getName());
            hoder.price.setText(list_one.get(position).getPrice());
            hoder.fen.setText(list_one.get(position).getFen());
        }
        return convertView;
    }
    class ViewHoder{
        TextView name;
        TextView fen;
        TextView price;
    }
}
