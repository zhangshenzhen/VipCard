package com.bjypt.vipcard.adapter.interconnect;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.view.layoutbanner.BannerLayout;
import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by test on 2017/11/22.
 */


public class ReaycleBannerAdapter extends RecyclerView.Adapter<ReaycleBannerAdapter.MzViewHolder> {

    private Context context;
    private List<String> urlList;
    private BannerLayout.OnBannerItemClickListener onBannerItemClickListener;
    private final List<Integer> defaultList;//默认图片

    public ReaycleBannerAdapter(Context context, List<String> urlList) {
        this.context = context;
        this.urlList = urlList;
        defaultList = new ArrayList<>();
        defaultList.add(R.mipmap.ad_bg);
        defaultList.add(R.mipmap.ad_bg);
        defaultList.add(R.mipmap.ad_bg);
    }

    public void setOnBannerItemClickListener(BannerLayout.OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    @Override
    public ReaycleBannerAdapter.MzViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MzViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(ReaycleBannerAdapter.MzViewHolder holder, final int position) {
         MzViewHolder  mzViewHolder = holder;

        if (urlList == null || urlList.size()>0){
        final int P = position % urlList.size();
        String url = urlList.get(P);
        Glide.with(context).load(url).into(mzViewHolder.imageView);

        mzViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBannerItemClickListener != null) {
                    onBannerItemClickListener.onItemClick(P);
                }
            }
        });
        }else {//显示默认图片
            final int P2 = position % defaultList.size();
            int url2 = defaultList.get(P2);
            Glide.with(context).load(url2).into(mzViewHolder.imageView);

            mzViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBannerItemClickListener != null) {
                        onBannerItemClickListener.onItemClick(P2);
                    }
                }
            });
        }


        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        int dex = layoutManager.findFirstVisibleItemPosition();//获取第一个可见的View的索引

    }

    @Override
    public int getItemCount() {
        if (urlList != null&&urlList.size()>0) {
           return urlList.size();
        }
       return 3;//默认
    }


    class MzViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        MzViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }

}
