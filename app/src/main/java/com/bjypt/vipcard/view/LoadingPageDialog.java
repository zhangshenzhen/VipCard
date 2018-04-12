package com.bjypt.vipcard.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bjypt.vipcard.R;


/**
 * @author http://blog.csdn.net/finddreams
 * @Description:自定义对话框
 */
public class LoadingPageDialog extends ProgressDialog {

    //    private AnimationDrawable mAnimation;
//    private ImageView mImageView;
    private Context mContext;
    private TextView mLoadingTv;
    private int mResid;
    private static LoadingPageDialog loadingPageDialog;
    private View network_bg;

    public LoadingPageDialog(Context context) {
        super(context);
        this.mContext = context;
        setCanceledOnTouchOutside(true);
    }

    public static synchronized LoadingPageDialog getInstance(Context context) {
        if (loadingPageDialog == null) {
            loadingPageDialog = new LoadingPageDialog(context);
        }
        return loadingPageDialog;
    }

    public void setMsg(String msg) {
        mLoadingTv.setText(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {

        mLoadingTv.setText("加载中...");
//        mImageView.setBackgroundResource(R.drawable.loadingpage);
//         通过ImageView对象拿到背景显示的AnimationDrawable
//        mAnimation = (AnimationDrawable) mImageView.getBackground();
//        // 为了防止在onCreate方法中只显示第一帧的解决方案之一
//        mImageView.post(new Runnable() {
//            @Override
//            public void run() {
//                mAnimation.start();
//
//            }
//        });
    }

    private void initView() {
        setContentView(R.layout.network_loading_layout);
        network_bg = findViewById(R.id.network_bg);
        mLoadingTv = (TextView) findViewById(R.id.tv_msg);
//        mImageView = (ImageView) findViewById(R.id.loadingIv);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void show() {
        super.show();
        this.getWindow().setBackgroundDrawable(new ColorDrawable());
    }


}
