package com.bjypt.vipcard.adapter.cityconnect;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bjypt.vipcard.R;

public class YouHuiAdapter extends RecyclerView.Adapter {

    private Context context;
   private  int num;
    public YouHuiAdapter(Context context, int num) {
        this.context = context;
        this.num = num;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_youhui,null);
        YouHuiHolder youHuiHolder = new YouHuiHolder(view);

        return youHuiHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return num;
    }

    static class  YouHuiHolder  extends RecyclerView.ViewHolder{

        public YouHuiHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
