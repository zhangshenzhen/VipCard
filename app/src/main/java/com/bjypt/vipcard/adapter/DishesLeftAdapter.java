package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.ProductTypeListBean;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/28
 * Use by
 */
public class DishesLeftAdapter extends BaseAdapter {

    private String[] leftStr;
    private Context mContext;
    private LayoutInflater mInflater;
    private int selectPostion;

    public DishesLeftAdapter(Context context, List<ProductTypeListBean> leftStr) {

        String[] nameStr = new String[leftStr.size()];
        for (int i = 0; i < leftStr.size(); i++) {
            nameStr[i] = leftStr.get(i).getTypename();
        }

        this.mContext = context;
        this.leftStr = nameStr;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return leftStr.length;
    }

    @Override
    public Object getItem(int position) {
        return leftStr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DishesViewHolder dishesViewHolder;
        if (convertView == null) {
            dishesViewHolder = new DishesViewHolder();
            convertView = mInflater.inflate(R.layout.dishes_left_list_item, null);
            dishesViewHolder.mTv = (TextView) convertView.findViewById(R.id.left_tv);
            dishesViewHolder.line = (TextView) convertView.findViewById(R.id.menu_shopping_line);
            convertView.setTag(dishesViewHolder);
        } else {
            dishesViewHolder = (DishesViewHolder) convertView.getTag();
        }
//设置左侧list点击后的背景颜色
        if (selectPostion == position) {
            dishesViewHolder.line.setVisibility(View.VISIBLE);
            convertView.setBackgroundColor(Color.rgb(255, 255, 255));
        } else {
            dishesViewHolder.line.setVisibility(View.INVISIBLE);
            convertView.setBackgroundColor(Color.parseColor("#f4f5f7"));
        }
        dishesViewHolder.mTv.setText(leftStr[position]);

        return convertView;
    }

    public void setSelectPostion(int position) {
        if(position != selectPostion) {
            selectPostion = position;
            notifyDataSetChanged();
        }
    }

    class DishesViewHolder {
        private TextView mTv;
        private TextView line;
    }
}
