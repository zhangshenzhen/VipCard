package com.bjypt.vipcard.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.bjypt.vipcard.activity.LifeServireH5Activity;
import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.model.AppCategoryBean;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.RoundImageView;
import com.bjypt.vipcard.view.ToastUtil;
import com.sinia.orderlang.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
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

            toItemInfor(appCategoryBeans.get(getLayoutPosition()));
        }
    }

    private void toItemInfor(AppCategoryBean appCategoryBean) {
        if (appCategoryBean != null) {
            if (appCategoryBean.getIsentry() == AppCategoryBean.LoginTypeEnum.Login.getValue() && !"Y".equals(SharedPreferenceUtils.getFromSharedPreference(mContext, Config.userConfig.is_Login))) {
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
                return;
            }
            if (appCategoryBean.getLink_type() == AppCategoryBean.ActionTypeEnum.H5.getValue()) {
                String url = appCategoryBean.getLink_url();
                if (appCategoryBean.getIsentry() == AppCategoryBean.LoginTypeEnum.Login.getValue()) {
                    if (!url.endsWith("pkregister=")) {
                        url = url + "pkregister=";
                    }
                    url = url + SharedPreferenceUtils.getFromSharedPreference(mContext, Config.userConfig.pkregister);
                }
                if (appCategoryBean.getLink_url().contains("alipays://platformapi")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appCategoryBean.getLink_url()));
                        mContext.startActivity(intent);
                        return;
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        ToastUtil.showToast(mContext, "该功能需跳转支付宝");
                    }
                }
                Intent intent = new Intent(mContext, LifeServireH5Activity.class);
                intent.putExtra("life_url", url);
                intent.putExtra("isLogin", "N");
                intent.putExtra("serviceName", appCategoryBean.getApp_name());
                mContext.startActivity(intent);
            } else if (appCategoryBean.getLink_type() == AppCategoryBean.ActionTypeEnum.Native.getValue()) {
                if (StringUtil.isEmpty(appCategoryBean.getAndroid_native_url())) {
                    ToastUtil.showToast(mContext, "暂未开通");
                } else {
                    nativeHandler(appCategoryBean);
                }
            } else if (appCategoryBean.getLink_type() == AppCategoryBean.ActionTypeEnum.NoAction.getValue()) {
                ToastUtil.showToast(mContext, "暂未开通");
            }

        }
    }

    private void nativeHandler(AppCategoryBean appCategoryBean) {
        Intent intent = new Intent();
        Class aClass = null;
        try {
            aClass = Class.forName(appCategoryBean.getAndroid_native_url());
            intent.setClass(mContext, aClass);
            if (mContext.getPackageManager().resolveActivity(intent, 0) == null) {
                // 说明系统中不存在这个activity
                ToastUtil.showToast(mContext, "暂未开通");
            } else {
                if (StringUtil.isNotEmpty(appCategoryBean.getNative_params())) {
                    JSONObject params = new JSONObject(appCategoryBean.getNative_params());
                    Iterator<String> iterator = params.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        Object value = params.get(key);
                        if (value instanceof String) {
                            intent.putExtra(key, value.toString());
                        } else if (value instanceof Integer) {
                            intent.putExtra(key, (Integer) value);
                        } else {
                            JSONArray jsonArray = (JSONArray) value;
                            if (jsonArray != null) {
                                ArrayList<String> item = new ArrayList<String>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    item.add(jsonArray.getString(i));
                                }
                                intent.putStringArrayListExtra(key, item);
                            }
                        }
                        //do something
                    }
                }
                mContext.startActivity(intent);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            ToastUtil.showToast(mContext, "暂未开通");
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            ToastUtil.showToast(mContext, "暂未开通");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建一个回调
     */
    public interface MyItemClickListener {
        void onItemClick(View view);
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
