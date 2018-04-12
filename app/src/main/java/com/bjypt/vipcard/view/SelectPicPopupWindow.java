package com.bjypt.vipcard.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.bjypt.vipcard.R;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/28
 * Use by 自定义的popupwindow
 */
public class SelectPicPopupWindow extends PopupWindow{
    private View mMenuView;

    public SelectPicPopupWindow(Activity context,View.OnClickListener itemsOnClick){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.shop_car_item,null);
    }

}
