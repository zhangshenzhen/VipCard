package com.bjypt.vipcard.adapter.cityconnect;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;

import java.util.ArrayList;
import java.util.List;

public class ShopStreetRecycleAdapter extends RecyclerView.Adapter{

    private Context context;
    private List<String> list;
    private List<Integer> checklist;
    private boolean isCkeck;
    private int dposion = -1;
    private Handler handler;


    public ShopStreetRecycleAdapter(Context context, List<String> list,Handler handler) {
        this.context = context;
        this.list = list;
        this.handler = handler;
        checklist = new ArrayList<>();
    }
    public void reFresh(List<String> list,Handler handler,int dposion){
        this.list = list;
        this.dposion = dposion;
        this.handler = handler;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gridview_shopstreet,null);
        ShopStreetHoldr streetHoldr = new ShopStreetHoldr(view);
        return streetHoldr;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ShopStreetHoldr streetViewHoldr  = (ShopStreetHoldr) holder;
        streetViewHoldr.tv_name.setText(list.get(position));

         if(position ==dposion){
           streetViewHoldr.tv_name.setBackgroundResource(R.mipmap.bg_grid_mian);
           streetViewHoldr.tv_name.setTextColor(Color.parseColor("#ffffff"));
         }else {
             streetViewHoldr.tv_name.setBackgroundResource(R.mipmap.bg_grid_white);
             streetViewHoldr.tv_name.setTextColor(Color.parseColor("#999999"));
         }


        streetViewHoldr.reltv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dposion = streetViewHoldr.getAdapterPosition();//选择的点
                Message msg = Message.obtain();
                msg.what = dposion;
                handler.sendMessage(msg);
                notifyDataSetChanged();
            }

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ShopStreetHoldr extends  RecyclerView.ViewHolder{
        private  final TextView tv_name;
        private final RelativeLayout reltv;
        public ShopStreetHoldr(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            reltv = itemView.findViewById(R.id.reltv);
        }
    }
}
