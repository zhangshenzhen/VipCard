package com.bjypt.vipcard.activity.shangfeng.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MerchantListBean;
import com.bjypt.vipcard.activity.shangfeng.primary.merchant.ui.MerchantDetailsActivity;
import com.bjypt.vipcard.activity.shangfeng.util.StringUtils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 商家列表
 */
public class MerchantListAdapter extends RecyclerView.Adapter<MerchantListAdapter.ViewHolder> {

    private List<MerchantListBean> merchantListBeans;
    private Context context;
    private MerchantListBean merchantListBean;
    private PlayMerchantPhoneListener listener;
    private String price;


    public MerchantListAdapter(Context context, List<MerchantListBean> merchantListBeans, String price) {
        this.merchantListBeans = merchantListBeans;
        this.context = context;
        this.price= price;
    }

    @Override
    public int getItemCount() {
        return merchantListBeans.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        merchantListBean = merchantListBeans.get(position);
        loadImage(merchantListBean.getLogo(), holder.iv_merchant_photo);
        holder.tv_merchant_title.setText(merchantListBean.getMuname());
        String distance = merchantListBean.getDistance();
        holder.tv_list_distance.setText(StringUtils.formatFloatNumber(Double.valueOf(distance)) + "km");
        if (StringUtils.isNotEmpty(merchantListBean.getFee())) {
            holder.tv_discount.setText(merchantListBean.getFee());
            holder.relate_discount.setVisibility(View.VISIBLE);
        } else {
            holder.relate_discount.setVisibility(View.GONE);
        }

        holder.tv_merchant_activity.setText(merchantListBean.getActivity_content());
        holder.ibtn_play_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.callPhone(merchantListBeans.get(position).getPhone());
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                MerchantDetailsActivity.callActivity(context, merchantListBeans.get(position).getPkmuser(),price);
            }
        });

        holder.relate_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_merchant, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    public void updateMerchantList(List<MerchantListBean> merchantListBeans) {
        this.merchantListBeans = merchantListBeans;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_merchant_photo;  // 商家缩略图
        TextView tv_merchant_title;  // 商家店名
        TextView tv_list_distance;  // 距离
        ImageButton ibtn_play_phone; // 拨打电话
        TextView tv_discount;  // 折扣
        TextView tv_merchant_activity; // 商家活动
        private final View itemView;
        private View relate_discount;

        public ViewHolder(View view) {
            super(view);
            itemView = view;
            iv_merchant_photo = (ImageView) view.findViewById(R.id.iv_merchant_photo);
            tv_merchant_title = (TextView) view.findViewById(R.id.tv_merchant_title);
            tv_list_distance = (TextView) view.findViewById(R.id.tv_list_distance);
            ibtn_play_phone = (ImageButton) view.findViewById(R.id.ibtn_play_phone);
            tv_discount = (TextView) view.findViewById(R.id.tv_discount);
            tv_merchant_activity = (TextView) view.findViewById(R.id.tv_merchant_activity);
            relate_discount = view.findViewById(R.id.relate_discount);

        }

    }


    private void loadImage(String imageUrl, ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .error(R.mipmap.merchant_item_error)
                .into(imageView);
    }

    public void playMerchantPhone(PlayMerchantPhoneListener listener) {
        this.listener = listener;
    }

    public interface PlayMerchantPhoneListener {
        void callPhone(String phoneNumber);
    }

}
