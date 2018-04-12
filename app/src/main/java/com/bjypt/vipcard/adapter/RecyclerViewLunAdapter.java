package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.LunTan;
import com.bjypt.vipcard.model.RecycleViewBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */

public class RecyclerViewLunAdapter extends RecyclerView.Adapter<RecyclerViewLunAdapter.MyViewHolder> {
    private Context context;
    private List<LunTan> list;

    public RecyclerViewLunAdapter(Context context, List<LunTan> list) {
        this.context = context;
        this.list = list;
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(configuration);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_lun_item, parent,false));
        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {
        holder.tv_name.setText(list.get(position).getProjectName());
        ImageLoader.getInstance().displayImage(Config.web.picUrl+list.get(position).getProjectIocn(),holder.tv, AppConfig.DEFAULT_IMG_LIFE_TYPE);
        holder.tv_liear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( listener!=null){
listener.onItemClickListener(v,position,list);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder
    {

        ImageView tv;
        TextView tv_name;
        LinearLayout tv_liear;

        public MyViewHolder(View view)
        {
            super(view);
            tv = (ImageView) view.findViewById(R.id.iv_lun_pic);
            tv_name = (TextView) view.findViewById(R.id.tv_lun_name);
            tv_liear = (LinearLayout) view.findViewById(R.id.iv_liao_linear);
        }
    }
    private OnRecyclerViewItemClickListener listener;
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener){
this.listener = listener;
    }
    public interface OnRecyclerViewItemClickListener{
        public void onItemClickListener(View view, int posiont, List<LunTan> list);
    }
}
