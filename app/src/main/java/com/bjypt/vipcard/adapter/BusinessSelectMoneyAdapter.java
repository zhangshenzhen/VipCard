package com.bjypt.vipcard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.BusinessFinancingSelectBean;

import java.util.ArrayList;

/**
 * Created by 崔龙 on 2017/12/8.
 * <p>
 * 商家理财金额选择适配器
 */

public class BusinessSelectMoneyAdapter extends BaseAdapter {
    private int clickTemp = -1;
    private Context context;
    private ArrayList<BusinessFinancingSelectBean.ResultDataBean> selectMoneyList;

    public BusinessSelectMoneyAdapter(Context context, ArrayList<BusinessFinancingSelectBean.ResultDataBean> selectMoneyList) {
        this.context = context;
        this.selectMoneyList = selectMoneyList;
    }

    public void setSelection(int position) {
        clickTemp = position;
    }

    @Override
    public int getCount() {
        return selectMoneyList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MoneyHolder moneyHolder = null;
        if (convertView == null) {
            moneyHolder = new MoneyHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_one_key_selector1, null);
            moneyHolder.rl_select_money = (RelativeLayout) convertView.findViewById(R.id.rl_select_money);
            moneyHolder.tv_select_money = (TextView) convertView.findViewById(R.id.tv_select_money);
            moneyHolder.tv_present = (TextView) convertView.findViewById(R.id.tv_present);
            convertView.setTag(moneyHolder);
        } else {
            moneyHolder = (MoneyHolder) convertView.getTag();
        }

        moneyHolder.tv_select_money.setText(String.valueOf(selectMoneyList.get(position).getFinance_amount()) + "元");
        moneyHolder.tv_present.setText(selectMoneyList.get(position).getHandsel_desc());

        if (clickTemp == position) {
            moneyHolder.rl_select_money.setBackgroundResource(R.mipmap.xuanzhongjine);
            moneyHolder.tv_select_money.setTextColor(context.getResources().getColor(
                    R.color.red_txt));
            moneyHolder.tv_present.setTextColor(context.getResources().getColor(
                    R.color.red_txt));
        } else {
            moneyHolder.rl_select_money.setBackgroundResource(R.mipmap.weixuanzhongjine);
            moneyHolder.tv_select_money.setTextColor(context.getResources().getColor(
                    R.color.hehe_bg));
            moneyHolder.tv_present.setTextColor(context.getResources().getColor(
                    R.color.hehe_bg));
        }

        return convertView;
    }


    private class MoneyHolder {
        private RelativeLayout rl_select_money;
        private TextView tv_select_money;
        private TextView tv_present;
    }
}
