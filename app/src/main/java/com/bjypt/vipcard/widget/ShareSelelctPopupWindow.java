package com.bjypt.vipcard.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bjypt.vipcard.R;

/**
 * Created by 崔龙 on 2017/11/28.
 * <p>
 * 更换背景的POPWindow
 */

public class ShareSelelctPopupWindow extends PopupWindow {
    private LinearLayout ll_wechat_share, ll_wechat_circle_share, ll_qq_friends_share, ll_qq_zone_share;
    private View mMenuView;

    public ShareSelelctPopupWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.share_select_pop_layout, null);
        ll_wechat_share = (LinearLayout) mMenuView.findViewById(R.id.ll_wechat_share);
        ll_wechat_circle_share = (LinearLayout) mMenuView.findViewById(R.id.ll_wechat_circle_share);
        ll_qq_friends_share = (LinearLayout) mMenuView.findViewById(R.id.ll_qq_friends_share);
        ll_qq_zone_share = (LinearLayout) mMenuView.findViewById(R.id.ll_qq_zone_share);

        // 设置按钮监听
        ll_wechat_share.setOnClickListener(itemsOnClick);
        ll_wechat_circle_share.setOnClickListener(itemsOnClick);
        ll_qq_friends_share.setOnClickListener(itemsOnClick);
        ll_qq_zone_share.setOnClickListener(itemsOnClick);

        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00FFFFFF);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.ll_pop_share).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
    }
}
