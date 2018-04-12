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
import com.bjypt.vipcard.model.SelectMoneyBean;

import java.util.ArrayList;

/**
 * Created by 崔龙 on 2017/12/8.
 */

public class SelectMoneyAdapter extends BaseAdapter {
    private int clickTemp = -1;
    private Context context;
    private ArrayList<SelectMoneyBean.ResultDataBean> moneyList;

    public SelectMoneyAdapter(Context context, ArrayList<SelectMoneyBean.ResultDataBean> moneyList) {
        this.context = context;
        this.moneyList = moneyList;
    }

    public void setSelection(int position) {
        clickTemp = position;
    }

    @Override
    public int getCount() {
        return moneyList.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_one_key_selector, null);
            moneyHolder.rl_select_money = (RelativeLayout) convertView.findViewById(R.id.rl_select_money);
            moneyHolder.tv_select_money = (TextView) convertView.findViewById(R.id.tv_select_money);
            moneyHolder.tv_present = (TextView) convertView.findViewById(R.id.tv_present);
            convertView.setTag(moneyHolder);
        } else {
            moneyHolder = (MoneyHolder) convertView.getTag();
        }
        moneyHolder.tv_select_money.setText(String.valueOf(moneyList.get(position).getRecharge_amount()) + "元");

        if (Double.parseDouble(moneyList.get(position).getHandsel_amount()) > 0) {
            String handsel_amount = moneyList.get(position).getHandsel_amount();
            if (handsel_amount.endsWith(".0")) {
                moneyHolder.tv_present.setText("充 " + moneyList.get(position).getRecharge_amount() + "元 送 " + handsel_amount.substring(0, handsel_amount.lastIndexOf(".")) + "元");
            } else {
                moneyHolder.tv_present.setText("充 " + moneyList.get(position).getRecharge_amount() + "元 送 " + handsel_amount + "元");
            }
        } else {
            moneyHolder.tv_present.setVisibility(View.GONE);
        }

        if (clickTemp == position) {
            moneyHolder.rl_select_money.setBackgroundResource(R.mipmap.one_key_money_ok);
        } else {
            moneyHolder.rl_select_money.setBackgroundResource(R.mipmap.one_key_money_no);
        }

        return convertView;
    }


    private class MoneyHolder {
        private RelativeLayout rl_select_money;
        private TextView tv_select_money;
        private TextView tv_present;
    }
}
