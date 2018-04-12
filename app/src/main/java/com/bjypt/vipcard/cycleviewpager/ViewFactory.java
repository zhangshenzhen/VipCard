package com.bjypt.vipcard.cycleviewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.config.AppConfig;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/12
 * Use by iamgeView创建工厂
 */
public class ViewFactory {
    /**
     * 获取ImageView视图的同时加载url
     */
    public static ImageView getImageView(Context context, String url) {
        ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(
                R.layout.view_banner, null);
        ImageLoader.getInstance().displayImage(url, imageView, AppConfig.DEFAULT_IMG_OPTIONS_AD);
        return imageView;
    }
}
