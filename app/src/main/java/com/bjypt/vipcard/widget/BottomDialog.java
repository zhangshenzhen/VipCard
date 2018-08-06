package com.bjypt.vipcard.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.utils.DensityUtil;

/**
 * 底部弹出Dialog
 */
public class BottomDialog extends Dialog {

    private final Context mContext;
    private BottonRouteListener mListener;
    private Button tv_route_one,tv_route_two,tv_route_three;
    private Button btn_cancel;
    private LinearLayout linear_route_one, linear_route_two;
    private ImageView iv_route_two, iv_route_one;


    public BottomDialog(Context context) {

        super(context, R.style.BottomAnimDialogStyle);
        this.mContext = context;
        initView();

    }


    private void initView() {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_bottom_view, null);

        Window window = this.getWindow();
        if (window != null) { //设置dialog的布局样式 让其位于底部
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.y = DensityUtil.dip2px(mContext,10); //设置居于底部的距离
            window.setAttributes(lp);
        }

        tv_route_one = (Button) view.findViewById(R.id.tv_route_one);
        tv_route_two = (Button) view.findViewById(R.id.tv_route_two);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);


        linear_route_one = (LinearLayout)view.findViewById(R.id.linear_route_one);
        linear_route_two = (LinearLayout)view.findViewById(R.id.linear_route_two);

        iv_route_one = (ImageView)view.findViewById(R.id.iv_route_one);
        iv_route_two = (ImageView)view.findViewById(R.id.iv_route_two);

        linear_route_one.setOnClickListener(new BottomDialog.clickListener());
        linear_route_two.setOnClickListener(new BottomDialog.clickListener());
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        setContentView(view);

    }


    public void setOneText(String text, int iconIds){
        tv_route_one.setText(text);
        iv_route_one.setVisibility(View.VISIBLE);
        iv_route_one.setImageDrawable(getContext().getResources().getDrawable(iconIds));

    }

    public void setTwoText(String text, int iconIds){
        tv_route_two.setText(text);
        iv_route_two.setVisibility(View.VISIBLE);
        iv_route_two.setImageDrawable(getContext().getResources().getDrawable(iconIds));
    }


    public void setClickListener(BottonRouteListener listener) {
        this.mListener = listener;
    }

    public interface BottonRouteListener {
        void onItem1Listener();

        void onItem2Listener();
    }


    private class clickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.linear_route_one:
                    if (mListener != null) {
                        mListener.onItem1Listener();
                        dismiss();
                    }
                    break;
                case R.id.linear_route_two:
                    if (mListener != null) {
                        mListener.onItem2Listener();
                        dismiss();
                    }
                    break;

            }
        }
    }
}
