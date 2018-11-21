package com.bjypt.vipcard.adapter.cityconnect;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.Text;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.cityconnect.YouHuiQuanListActivity;
import com.bjypt.vipcard.bean.YouHuiQuanBean;
import com.bjypt.vipcard.utils.FomartToolUtils;
import com.sinia.orderlang.utils.StringUtil;

import java.io.Serializable;
import java.util.List;

public class YouHuiDialogAdapter extends RecyclerView.Adapter {

    private Context context;
    private  int num;
    private Dialog dialog;
    private List<YouHuiQuanBean.YouHuiQuanDataBean> youHuiQuanDataBeanlist;
   // private YouHuiQuanBean.YouHuiQuanDataBean youHuiQuanData;

    public YouHuiDialogAdapter(Context context,List<YouHuiQuanBean.YouHuiQuanDataBean> youHuiQuanDataBeanlist,  Dialog dialog) {
        this.context = context;
        this.youHuiQuanDataBeanlist = youHuiQuanDataBeanlist;
        this.dialog = dialog;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_youhui,null);
        YouHuiHolder youHuiHolder = new YouHuiHolder(view);
        return youHuiHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
          YouHuiHolder  youHuiHolder = (YouHuiHolder) holder;
          YouHuiQuanBean.YouHuiQuanDataBean  youHuiQuanData = youHuiQuanDataBeanlist.get(position);
         //绑定数据
        //标签数据
        if(StringUtil.isNotEmpty(youHuiQuanData.getLabel())){
        String [] lableArr = youHuiQuanData.getLabel().split("，");//
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

        //优惠金额
        youHuiHolder.tv_youhui_money.setText(FomartToolUtils.fomartMoneyNoSymbol(youHuiQuanData.getPayamount()));
        //满减金额
        String condition = FomartToolUtils.fomartMoneyNoSymbol(youHuiQuanData.getValueamount());
        youHuiHolder.tv_manjian_money.setText("满"+condition+"元可用");
        //标题
        youHuiHolder.tv_title.setText(youHuiQuanData.getTitle());
        //期限
        youHuiHolder.tv_time_limit.setText(youHuiQuanData.getStart_time());


        //测试有点条转逻辑
         youHuiHolder.btn_now_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context,"这事第 "+ position +" 个条目",Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(context, YouHuiQuanListActivity.class);
               intent.putExtra("youHuiQuanDataBeanlist",  (Serializable)youHuiQuanDataBeanlist);
                context.startActivity(intent);
                 if (dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                 }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(youHuiQuanDataBeanlist != null&& youHuiQuanDataBeanlist.size()>0){
            if(youHuiQuanDataBeanlist.size()>=3){
                return 3;
            }else {
                return youHuiQuanDataBeanlist.size();
            }
        }
        return 0;
    }

    static class  YouHuiHolder  extends RecyclerView.ViewHolder{

        private final Button btn_now_use;//立即使用
        private final TextView tv_youhui_money;//优惠金额
        private final TextView tv_manjian_money;//满减
        private final TextView tv_title;//标题
        private final TextView tv_lable1;
        private final TextView tv_lable2;
        private final TextView tv_time_limit;

        public YouHuiHolder(@NonNull View itemView) {
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
