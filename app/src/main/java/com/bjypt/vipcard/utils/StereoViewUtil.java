package com.bjypt.vipcard.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.view.StereoView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */

public class StereoViewUtil {
    Context context;
    StereoView stereoView;
    List<String> list;
    private List<Integer> in_list;
    boolean isCan3D = false;
    private Runnable runnable;
    private boolean isLooping;

    public StereoViewUtil(Context context,StereoView stereoView,List<String> list,boolean isLooping) {
        this.context = context;
        this.list = list;
        this.stereoView = stereoView;
        this.isLooping = isLooping;
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(configuration);
        init();
    }
    public StereoViewUtil(Context context,StereoView stereoView,List<Integer> in_list,boolean isCan3D,boolean isLooping) {
        this.context = context;
        this.isCan3D = isCan3D;
        this.in_list = in_list;
        this.stereoView = stereoView;
        this.isLooping = isLooping;
        init_ster();
    }

    private void init_ster() {

        for ( int i = 0;i<in_list.size();i++){
           final int position = i;
            View view = LayoutInflater.from(context).inflate(R.layout.layout_imageview_new_home,new LinearLayout(context),false);
            ImageView imageView = (ImageView) view.findViewById(R.id.tv_str_new_home);
           imageView.setImageResource(in_list.get(i));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
if (onClickListener !=null){
    onClickListener.onClickListener(position);
}
                }
            });
            stereoView.addView(view);
        }
        stereoView.setResistance(4f)//设置滑动时阻力
                .setInterpolator(new BounceInterpolator())//设置
                .setAngle(145)
                .setCan3D(isCan3D)
                .setStartScreen(1);//设置开始时item，注意不能是开头和结尾
        runnable = new Runnable() {
            @Override
            public void run() {
                stereoView.toNext();
                if (isLooping){
                    stereoView.postDelayed(runnable,2000);
                }

            }
        };
        stereoView.postDelayed(runnable,2000);
    }


    public void init(){
        for (int i=0;i<list.size();i++){
            View view = LayoutInflater.from(context).inflate(R.layout.layout_imageview_new_home,new LinearLayout(context),false);
            ImageView imageView = (ImageView) view.findViewById(R.id.tv_str_new_home);
                    ImageLoader.getInstance().displayImage(list.get(i),imageView, AppConfig.DEFAULT_LUNBO);
            stereoView.addView(view);
        }
        stereoView.setResistance(4f)//设置滑动时阻力
                .setInterpolator(new BounceInterpolator())//设置
                .setAngle(145)
                .setCan3D(isCan3D)
                .setStartScreen(1);//设置开始时item，注意不能是开头和结尾
        runnable = new Runnable() {
             @Override
             public void run() {
                 stereoView.toNext();
                 if (isLooping){
                     stereoView.postDelayed(runnable,2000);
                 }

             }
         };
        stereoView.postDelayed(runnable,2000);

    }
    private OnClickListener onClickListener;
    public void setOnStereoViewOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }
    public interface OnClickListener{
        public void onClickListener(int position);
    }


}
