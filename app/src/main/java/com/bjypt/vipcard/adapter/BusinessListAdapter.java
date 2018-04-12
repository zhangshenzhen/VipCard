package com.bjypt.vipcard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.PurchaseFinancingActivity;
import com.bjypt.vipcard.model.FinancingListBean;
import com.bjypt.vipcard.utils.FinancingProfitUtil;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by 崔龙 on 2018/1/17.
 * 商家理财首页列表adapter
 */

public class BusinessListAdapter extends BaseAdapter {
    private Context context;
    private String pkmuser;
    private String cardnum;
    private String categoryid;
    private String finance_amount;
    private ArrayList<FinancingListBean.ResultDataBean> lists;

    public BusinessListAdapter(Context context, ArrayList<FinancingListBean.ResultDataBean> lists, String pkmuser, String categoryid, String cardnum) {
        this.context = context;
        this.lists = lists;
        this.cardnum = cardnum;
        this.pkmuser = pkmuser;
        this.categoryid = categoryid;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MoneyHolder moneyHolder = null;
        if (convertView == null) {
            moneyHolder = new MoneyHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_business_financing_list, null);
            moneyHolder.tv_profit_money = (TextView) convertView.findViewById(R.id.tv_profit_money);
            moneyHolder.tv_productname = (TextView) convertView.findViewById(R.id.tv_productname);
            moneyHolder.tv_total_money = (TextView) convertView.findViewById(R.id.tv_total_money);
            moneyHolder.tv_profit = (TextView) convertView.findViewById(R.id.tv_profit);
            moneyHolder.tv_day = (TextView) convertView.findViewById(R.id.tv_day);
            convertView.setTag(moneyHolder);
        } else {
            moneyHolder = (MoneyHolder) convertView.getTag();
        }
        // 条目名字
        moneyHolder.tv_productname.setText(lists.get(position).getProductname());
        // 年化率
        if (lists.get(position).getProductrate().contains(".")) {
            moneyHolder.tv_profit.setText(lists.get(position).getProductrate().substring(0, lists.get(position).getProductrate().indexOf(".") + 2) + "%");
        } else {
            moneyHolder.tv_profit.setText(lists.get(position).getProductrate() + "%");
        }
        // 天数
        moneyHolder.tv_day.setText(lists.get(position).getProductday());
        // 总金额
        moneyHolder.tv_total_money.setText(finance_amount + "元");
        // 预计收益元
        moneyHolder.tv_profit_money.setText(lists.get(position).getFinance_amount() + "元");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PurchaseFinancingActivity.class);
                intent.putExtra("tv_productname", lists.get(position).getProductname());
                intent.putExtra("tv_profit", lists.get(position).getProductrate());
                intent.putExtra("tv_profit_money", lists.get(position).getFinance_amount());
                intent.putExtra("tv_total_money", finance_amount);
                intent.putExtra("diffdate", lists.get(position).getDiffdate());
                intent.putExtra("pkmuser", pkmuser);
                intent.putExtra("cardnum", cardnum);
                intent.putExtra("categoryid", categoryid);
                intent.putExtra("pkproduct", lists.get(position).getPkproduct());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private class MoneyHolder {
        private TextView tv_profit_money;
        private TextView tv_productname;
        private TextView tv_total_money;
        private TextView tv_profit;
        private TextView tv_day;
    }

    /**
     * 获取总金额
     *
     * @param totalMoney 总金额
     */
    public void setTotalMoney(String totalMoney) {
        finance_amount = totalMoney;
        if (lists != null) {
            for (int i = 0; i < lists.size(); i++) {
                String rate = lists.get(i).getProductrate();
                String day = lists.get(i).getProductday();
                BigDecimal money = FinancingProfitUtil.getFinanceMoney(new BigDecimal(rate), Integer.parseInt(day), new BigDecimal(totalMoney));
                lists.get(i).setFinance_amount(FinancingProfitUtil.getFractionTwo(money));
            }
            notifyDataSetChanged();
        }

    }
}
