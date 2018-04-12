package com.bjypt.vipcard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.dialog.PurchaseFinancingTakeNotesDialog;
import com.bjypt.vipcard.model.FinancingTakeNotesListBean;

import java.util.ArrayList;

/**
 * Created by 崔龙 on 2018/1/4.
 * <p>
 * 商家理财记录adapter
 */

public class PetroleumFinancingTakeNotesAdapter extends BaseAdapter {
    private ArrayList<FinancingTakeNotesListBean.ResultDataBean> lists;
    private Context context;
    private String categoryid;

    public PetroleumFinancingTakeNotesAdapter(ArrayList<FinancingTakeNotesListBean.ResultDataBean> lists, Context context,String categoryid) {
        this.lists = lists;
        this.context = context;
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
        ViewHolder holder;
        View view;
        /*  缓存子布局文件中的控件对象*/
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_financing_take_notes2, null, false);
            holder = new ViewHolder();
            holder.tv_investdate = (TextView) view.findViewById(R.id.tv_investdate);
            holder.tv_handsel_money_name = (TextView) view.findViewById(R.id.tv_handsel_money_name);
            holder.ll_select_go_to = (LinearLayout) view.findViewById(R.id.ll_select_go_to);
            holder.tv_productrate = (TextView) view.findViewById(R.id.tv_productrate);
            holder.tv_productname = (TextView) view.findViewById(R.id.tv_productname);
            holder.tv_productday = (TextView) view.findViewById(R.id.tv_productday);
            holder.tv_handsel_money = (TextView) view.findViewById(R.id.tv_handsel_money);
            holder.tv_investmoney = (TextView) view.findViewById(R.id.tv_investmoney);
            holder.tv_incomemoney = (TextView) view.findViewById(R.id.tv_incomemoney);
            holder.tv_money_whereabout_desc = (TextView) view.findViewById(R.id.tv_money_whereabout_desc);
            holder.tv_progress = (TextView) view.findViewById(R.id.tv_progress);
            holder.iv_kuang = (ImageView) view.findViewById(R.id.iv_kuang);
            view.setTag(holder);
        }
        //缓存已滑入ListView中的item view
        else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.tv_money_whereabout_desc.setText(lists.get(position).getMoney_whereabout_desc());
        holder.tv_investdate.setText("（始" + lists.get(position).getInvestdate() + "至" + lists.get(position).getExpiredate() + "）");
        if (lists.get(position).getInveststatus() == 1) {
            holder.ll_select_go_to.setVisibility(View.VISIBLE);
            holder.tv_progress.setText("进行中" + lists.get(position).getProgress() + "%");
        } else {
            holder.ll_select_go_to.setVisibility(View.GONE);
            holder.iv_kuang.setVisibility(View.GONE);
            holder.tv_progress.setText(lists.get(position).getMoney_whereabout_desc());
            holder.tv_progress.setTextSize(12);
        }
        holder.tv_handsel_money_name.setText(lists.get(position).getHandsel_money_name());
        holder.tv_productrate.setText(lists.get(position).getProductrate() + "%");
        holder.tv_productname.setText(lists.get(position).getProductname());
        holder.tv_productday.setText(lists.get(position).getProductday());
        holder.tv_handsel_money.setText(lists.get(position).getHandsel_money() + "元");
        holder.tv_investmoney.setText(lists.get(position).getInvestmoney() + "元");
        holder.tv_incomemoney.setText(lists.get(position).getIncomemoney() + "元");
        holder.ll_select_go_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initToGoToDialog(lists.get(position).getInvestid(), lists.get(position).getPkmuser());
            }
        });

        return view;
    }

    /**
     * 金额去向dialog
     *
     * @param investid 购买理财纪录主键
     */
    private void initToGoToDialog(String investid, String pkmuser) {
        PurchaseFinancingTakeNotesDialog takeNotesDialog = new PurchaseFinancingTakeNotesDialog(context, pkmuser, investid,categoryid);
        takeNotesDialog.show();
    }

    class ViewHolder {
        TextView tv_productname;
        ImageView iv_kuang;
        TextView tv_money_whereabout_desc;
        TextView tv_handsel_money_name;
        LinearLayout ll_select_go_to;
        TextView tv_productrate;
        TextView tv_investdate;
        TextView tv_productday;
        TextView tv_investmoney;
        TextView tv_handsel_money;
        TextView tv_incomemoney;
        TextView tv_progress;
    }
}
