package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.config.AppConfig;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/29
 * Use by 商家图片GridView
 */
public class MerchantPicAdapter extends MyBaseAdapter<String>{


    public MerchantPicAdapter(List<String> list, Context mContext) {
        super(list, mContext);
    }

    @Override
    public View initView(int position, View convertView, ViewGroup parent) {
        MerchantPicViewHolder merchantPicViewHolder;

        final ImageView imageView;
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        final int defaultWidth = wm.getDefaultDisplay().getWidth();
        final int defaultHeight = defaultWidth*3/4;
        if(null == convertView){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(defaultWidth,defaultHeight));
            imageView.setPadding(8,8,8,8);
        }else{
            imageView = (ImageView) convertView;
        }
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);


        if(list.get(position)!=null||!list.get(position).equals("")){
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));
            ImageLoader.getInstance().displayImage(list.get(position), imageView, AppConfig.DEFAULT_IMG_OPTIONS_AD);
        }else{
            imageView.setImageResource(R.mipmap.ad_bg);
        }

           return imageView;

    }


    class MerchantPicViewHolder{
        private ImageView mIv;
    }
}
