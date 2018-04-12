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
import com.bjypt.vipcard.model.RecycleViewBean;
import com.bjypt.vipcard.model.RedPackageBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private List<RedPackageBean> list;

    public RecyclerViewAdapter(Context context, List<RedPackageBean> list) {
        this.context = context;
        this.list = list;
//        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
//        ImageLoader.getInstance().init(configuration);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_recyclerview_item, parent,false));
        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {
        if (position%3==0){
            holder.ly_red_package.setBackgroundResource(R.mipmap.red_package_red);
        }else if (position%3==1){
            holder.ly_red_package.setBackgroundResource(R.mipmap.red_package_blue);
        }else if (position%3==2){
            holder.ly_red_package.setBackgroundResource(R.mipmap.red_package_yello);
        }
        holder.tv_name_red_package.setText(list.get(position).getName());
        holder.tv_price_red_package.setText("¥"+list.get(position).getPrice());

//        ImageLoader.getInstance().displayImage(list.get(position).getUrl(),holder.tv);
        holder.ly_red_package.setOnClickListener(new View.OnClickListener() {
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

        TextView tv_name_red_package;
        TextView tv_price_red_package;
        LinearLayout ly_red_package;

        public MyViewHolder(View view)
        {
            super(view);
           ly_red_package = (LinearLayout) view.findViewById(R.id.ly_red_package);
            tv_name_red_package = (TextView) view.findViewById(R.id.tv_name_red_package);
            tv_price_red_package = (TextView) view.findViewById(R.id.tv_price_red_package);
        }
    }
    private OnRecyclerViewItemClickListener listener;
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener){
this.listener = listener;
    }
    public interface OnRecyclerViewItemClickListener{
        public void onItemClickListener(View view,int posiont,List<RedPackageBean> list);
    }
}
