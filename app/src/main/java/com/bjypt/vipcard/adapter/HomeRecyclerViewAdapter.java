package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.AppCategoryBean;

import java.util.List;

/**
 * Created by Dell on 2018/3/26.
 */

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private List<AppCategoryBean> appCategoryBeans;
    private AppCategoryBean appCategoryBean;
    private RequestQueue mQueue;
    private MyItemClickListener myItemClickListener;
    private View view;

    public HomeRecyclerViewAdapter(Context context, List<AppCategoryBean> appCategoryBeans) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        this.appCategoryBeans = appCategoryBeans;
        mQueue = Volley.newRequestQueue(context);
    }

    @Override
    public int getItemCount() {

        return appCategoryBeans.size();
    }


    @Override
    public void onBindViewHolder(HomeRecyclerViewAdapter.ViewHolder holder, int position) {
        appCategoryBean = appCategoryBeans.get(position);
        loadImageUrl(appCategoryBean.getApp_icon(), holder.iv_photo);
        holder.tv_name.setText(appCategoryBean.getApp_name());

    }

    @Override
    public HomeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        view = inflater.inflate(R.layout.item_home_rcv, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
        viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        ImageView iv_photo;
        TextView tv_name;

        @Override
        public void onClick(View view) {
            if (myItemClickListener != null)
                myItemClickListener.onItemClick(appCategoryBeans.get(getLayoutPosition()));
        }
    }

    /**
     * 创建一个回调
     */
    public interface MyItemClickListener {
        void onItemClick(AppCategoryBean appCategoryBean);
    }


    /**
     * 在activity或者fragment 里面adapter调用这个方法将点击事件传进来，赋值给全局监听
     *
     * @param myItemClickListener
     */
    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    /**
     * 加载图片
     *
     * @param url
     * @param imageView
     */
    private void loadImageUrl(String url, final ImageView imageView) {
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                },
                0, 0, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        imageView.setImageBitmap(BitmapFactory.decodeResource(
                                mContext.getResources(),
                                R.mipmap.home_accumulation_fund,
                                new BitmapFactory.Options()
                        ));
                    }
                });
        mQueue.add(imageRequest);
    }
}
