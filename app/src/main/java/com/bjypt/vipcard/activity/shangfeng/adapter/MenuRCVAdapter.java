package com.bjypt.vipcard.activity.shangfeng.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.data.bean.BannerBean;
import com.bjypt.vipcard.activity.shangfeng.util.ShangfengUriHelper;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 主页面 Menu菜单适配器
 */
public class MenuRCVAdapter extends RecyclerView.Adapter<MenuRCVAdapter.ViewHolder> {

    private Context mContext;
    private List<BannerBean> bannerBeans;
    private BannerBean bannerBean;
    private View view;
    private ShangfengUriHelper shangfengUriHelper;


    public MenuRCVAdapter(Context context, List<BannerBean> bannerBeans) {
        this.mContext = context;
        this.bannerBeans = bannerBeans;
    }

    @Override
    public int getItemCount() {
        return bannerBeans.size();
    }


    @Override
    public void onBindViewHolder(MenuRCVAdapter.ViewHolder holder, final int position) {
        bannerBean = bannerBeans.get(position);
        loadImageUrl(bannerBean.getApp_icon(), holder.iv_photo);
        holder.tv_name.setText(bannerBean.getApp_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shangfengUriHelper == null) {
                    shangfengUriHelper = new ShangfengUriHelper(mContext);
                }
                shangfengUriHelper.onAppCategoryItemClick(bannerBeans.get(position), true);
            }
        });
    }

    public void updateMenu(List<BannerBean> bannerBeans) {
        this.bannerBeans = bannerBeans;
        notifyDataSetChanged();
    }

    @Override
    public MenuRCVAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menu, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        ImageView iv_photo;
        TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            iv_photo = (ImageView) itemView.findViewById(R.id.iv_photo);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }

    }

    /**
     * 加载图片
     *
     * @param url
     * @param imageView
     */
    private void loadImageUrl(String url, final ImageView imageView) {
        Glide.with(mContext)
                .load(url)
                .error(R.mipmap.meun_error)
                .into(imageView);
    }
}
