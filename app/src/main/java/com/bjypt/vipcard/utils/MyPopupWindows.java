package com.bjypt.vipcard.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bjypt.vipcard.R;

/**
 * Created by Administrator on 2016/7/21.
 */
public class MyPopupWindows extends PopupWindow {
    private Activity context;
    private LayoutInflater inflater;
    private View contentView;
    private final int h;
    private final int w;
    private MyCallBack myCallBack;

    public MyPopupWindows(Activity context, int resource){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(resource, null);
        h = context.getWindowManager().getDefaultDisplay().getHeight();
        w = context.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(contentView);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        this.update();
        this.setBackgroundDrawable(new ColorDrawable());
        this.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

    }
    public void show(View parent,int width,int height){
        if (myCallBack!=null){
            myCallBack.func(contentView);
        }
        if (width<=0||height<=0) {
            this.setWidth(w);
            this.setHeight(h);
        }else {
            this.setWidth(width);
            this.setHeight(height);
        }
        this.showAsDropDown(parent);
    }
    public void show(View parent){
        if (myCallBack!=null){
            myCallBack.func(contentView);
        }
        this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.showAtLocation(parent, Gravity.BOTTOM,0,0);
    }

    public void setMyCallBack(MyCallBack myCallBack){
        this.myCallBack = myCallBack;
    }
    public interface MyCallBack{
        public void func(View view);
    }
    /*public void dismiss(){
        this.dismiss();
    }*/

}
