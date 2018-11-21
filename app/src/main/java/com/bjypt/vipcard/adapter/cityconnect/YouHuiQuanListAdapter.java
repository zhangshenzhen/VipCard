package com.bjypt.vipcard.adapter.cityconnect;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bjypt.vipcard.R;

import com.bjypt.vipcard.bean.YouHuiquanListBean;
import com.bjypt.vipcard.utils.FomartToolUtils;
import com.sinia.orderlang.utils.StringUtil;

import java.util.List;

public class YouHuiQuanListAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<YouHuiquanListBean.ResultDataBean> ResultDataBeanlist;
    public YouHuiQuanListAdapter(Context context,List<YouHuiquanListBean.ResultDataBean> ResultDataBeanlist) {
        this.context = context;
        this.ResultDataBeanlist = ResultDataBeanlist;
    }

    //刷新

    public void reFresh(List<YouHuiquanListBean.ResultDataBean> ResultDataBeanlist){
        this.ResultDataBeanlist = ResultDataBeanlist;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_youhuiquan_list,null);
       YouHuiListViewHolder youHuiListHolder =new YouHuiListViewHolder(view);

       return youHuiListHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        YouHuiListViewHolder youHuiHolder = (YouHuiListViewHolder) holder;
        YouHuiquanListBean.ResultDataBean dataBean = ResultDataBeanlist.get(position);

        //绑定数据
        //标签数据
        if(StringUtil.isNotEmpty(dataBean.getLabel())){
            String [] lableArr = dataBean.getLabel().split("，");//
            if(lableArr.length==1){
                youHuiHolder.tv_lable1.setText(lableArr[0]);
                youHuiHolder.tv_lable2.setVisibility(View.INVISIBLE);

            }else {
                youHuiHolder.tv_lable1.setText(lableArr[0]);
                youHuiHolder.tv_lable2.setText(lableArr[1]);
            }
        }else {
            youHuiHolder.tv_lable1.setVisibility(View.INVISIBLE);
            youHuiHolder.tv_lable2.setVisibility(View.INVISIBLE);
        }
        if(dataBean.getUsestatus()==0){//未使用。立即使用
        youHuiHolder.btn_now_use.setText("立即使用");
        }else if(dataBean.getUsestatus()==1){//已经使用
         youHuiHolder.btn_now_use.setText("已使用");
        }else if(dataBean.getUsestatus()==2){//已冻结
         youHuiHolder.btn_now_use.setText("已冻结");
        }else if(dataBean.getUsestatus()==3){//已过期
         youHuiHolder.btn_now_use.setText("已过期");
        }

        //优惠金额
        youHuiHolder.tv_youhui_money.setText(FomartToolUtils.fomartMoneyNoSymbol(dataBean.getPayamount()));
        //满减金额
        String condition = FomartToolUtils.fomartMoneyNoSymbol(dataBean.getValueamount());
        youHuiHolder.tv_manjian_money.setText("满"+condition+"元可用");
        //标题
        youHuiHolder.tv_title.setText(dataBean.getTitle());
        //期限
        youHuiHolder.tv_time_limit.setText(dataBean.getEnddate());

    }

    @Override
    public int getItemCount() {
        if (ResultDataBeanlist != null && ResultDataBeanlist.size() >0){
            return ResultDataBeanlist.size();
        }
        return 0;
    }

    static class YouHuiListViewHolder extends RecyclerView.ViewHolder{

        private final Button btn_now_use;//立即使用
        private final TextView tv_youhui_money;//优惠金额
        private final TextView tv_manjian_money;//满减
        private final TextView tv_title;//标题
        private final TextView tv_lable1;
        private final TextView tv_lable2;
        private final TextView tv_time_limit;

        public YouHuiListViewHolder(View itemView) {
            super(itemView);
            btn_now_use = itemView.findViewById(R.id.btn_now_use);
            tv_youhui_money = itemView.findViewById(R.id.tv_youhui_money);
            tv_manjian_money = itemView.findViewById(R.id.tv_manjian_money);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_lable1 = itemView.findViewById(R.id.tv_lable1);
            tv_lable2 = itemView.findViewById(R.id.tv_lable2);
            tv_time_limit = itemView.findViewById(R.id.tv_time_limit);
        }
    }
}
