package com.bjypt.vipcard.adapter.cf;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.crowdfunding.CrowdfundingAccountInfoActivity;
import com.bjypt.vipcard.adapter.MerchantPullListAdapter;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.MerchantPullListBean;
import com.bjypt.vipcard.model.cf.CfAccountData;
import com.bjypt.vipcard.utils.DensityUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinia.orderlang.utils.StringUtil;

import java.io.Serializable;
import java.util.List;

public class AccountListAdapter  extends BaseAdapter{
    private Context context;
    private List<CfAccountData> list;
   private String phoneno;
    public AccountListAdapter(Context context, List<CfAccountData> list,String phoneno) {
        this.context = context;
        this.list = list;
        this.phoneno = phoneno;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyViewHolder holder = null;
        if (view == null){
            holder = new MyViewHolder();
            view = View.inflate(context, R.layout.activity_crowdfunding_account_list_item, null);
            holder.tv_merchantname = (TextView) view.findViewById(R.id.tv_merchantname);
            holder.tv_vipname = (TextView) view.findViewById(R.id.tv_vipname);
            holder.tv_cardnum = (TextView) view.findViewById(R.id.tv_cardnum);
            holder.btn_enter = (Button) view.findViewById(R.id.btn_enter);
            view.setTag(holder);
        }else {
            holder = (MyViewHolder) view.getTag();
        }
        holder.tv_merchantname.setText(list.get(i).getMerchant_name());
//        holder.tv_vipname.setText(list.get(i).getAddress());
        String cardno = list.get(i).getCardno();
        if(StringUtil.isNotEmpty(cardno) && cardno.length() >= 12){
            cardno = cardno.substring(0,4) +"  " +
                    cardno.substring(4,8) + "  " +
                    cardno.substring(8,12);
        }
        holder.tv_cardnum.setText(cardno);
        holder.tv_vipname.setText(list.get(i).getVip_name());

        holder.btn_enter.setTag(cardno);
        if(list.get(i).getType_num() == null){
            holder.tv_vipname.setVisibility(View.GONE);
        }else{
           //暂时隐藏 holder.tv_vipname.setVisibility(View.VISIBLE);
            if(list.get(i).getType_num() ==2){
                holder.tv_vipname.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.cf_vip_level_2), null, null, null);
            }else if(list.get(i).getType_num() == 3){
                holder.tv_vipname.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.cf_vip_level_3), null, null, null);
            }else{
                holder.tv_vipname.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.cf_vip_level_1), null, null, null);
            }
            holder.tv_vipname.setCompoundDrawablePadding(DensityUtil.dip2px(context, 5));
        }
        holder.tv_vipname.setVisibility(View.GONE);//安全隐藏
        holder.btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("pkuseraccountid", list.get(i).getPkuseraccountid());
                intent.putExtra("cardno", list.get(i).getCardno());
                intent.putExtra("displaycardno", view.getTag().toString());
                intent.putExtra("pkmerchantid", list.get(i).getPkmerchantid());
                intent.putExtra("merchant_name", list.get(i).getMerchant_name());
                intent.putExtra("vip_name", list.get(i).getVip_name());
                intent.putExtra("type_num", list.get(i).getType_num());
                intent.putExtra("phoneno",phoneno);
                intent.setClass(context, CrowdfundingAccountInfoActivity.class);
                context.startActivity(intent);
            }
        });


        return view;
    }

    class MyViewHolder{
        TextView tv_merchantname;
        TextView tv_vipname;
        TextView tv_cardnum;
        Button btn_enter;

    }
}
