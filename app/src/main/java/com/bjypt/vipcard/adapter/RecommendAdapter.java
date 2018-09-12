package com.bjypt.vipcard.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.cycleviewpager.CycleViewPagerHandler;

import org.w3c.dom.Text;

public class RecommendAdapter extends BaseRecycleViewAdapter {
    public RecommendAdapter() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      View  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend,null);
      RecommendViewHolder  mViewHolder = new RecommendViewHolder(view);

      return mViewHolder;

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }


    class RecommendViewHolder extends RecyclerView.ViewHolder{

        private final ImageView icon;
        private final ImageView igv_zhongchou;
        private final TextView tvName;
        private final TextView tvProgress;
        private final TextView tvProgress_data;

        public RecommendViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.igv_icon);
            igv_zhongchou = itemView.findViewById(R.id.igv_zhongchou);
            tvName = itemView.findViewById(R.id.tev_name);
            tvProgress = itemView.findViewById(R.id.tev_progress);
            tvProgress_data = itemView.findViewById(R.id.tev_progress_data);

        }
    }

}
