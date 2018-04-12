package com.bjypt.vipcard.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.bean.WeatherBean;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Dell on 2018/3/31.
 */

public class GlidePopupWindow extends PopupWindow {

    private View mPopView;
    private TextView tv_temperature;
    private TextView tv_date;
    private TextView tv_air_quality;
    private ImageView iv_weather;

    public GlidePopupWindow(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
        setPopupWindow();

    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(context);
        //绑定布局
        mPopView = inflater.inflate(R.layout.layout_popupwindow_view, null);

        tv_temperature = (TextView) mPopView.findViewById(R.id.tv_temperature);
        tv_date = (TextView) mPopView.findViewById(R.id.tv_date);
        tv_air_quality = (TextView) mPopView.findViewById(R.id.tv_air_quality);
        iv_weather = (ImageView) mPopView.findViewById(R.id.iv_weather);
    }

    /**
     * 设置窗口的相关属性
     */
    @SuppressLint("InlinedApi")
    private void setPopupWindow() {
        this.setContentView(mPopView);// 设置View
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
        this.setFocusable(true);// 设置弹出窗口可
        this.setAnimationStyle(R.style.style_pop_animation);// 设置动画
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
        mPopView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();

                return true;
            }
        });
    }

    public void setmPopupWindowData(WeatherBean weatherBean){
        tv_temperature.setText(weatherBean.getAir_temperature());
        tv_date.setText(weatherBean.getDate());
        tv_air_quality.setText("空气:  "+weatherBean.getAir_quality());

        ImageLoader.getInstance().displayImage(weatherBean.getImageUrl(), iv_weather);
    }


}
