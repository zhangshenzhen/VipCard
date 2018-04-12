package com.bjypt.vipcard.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bjypt.vipcard.R;


/**
 * @author http://blog.csdn.net/finddreams
 * @Description:自定义对话框
 */
public class LoadingFragDialog extends ProgressDialog {

    private AnimationDrawable mAnimation;
    private Context mContext;
    private ImageView mImageView;
    private int count = 0;
    private int mResid;


    public LoadingFragDialog(Context context, int id, int theme) {
        super(context, theme);
        this.mContext = context;
        this.mResid = id;
        setCanceledOnTouchOutside(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {

        mImageView.setBackgroundResource(mResid);
        // 通过ImageView对象拿到背景显示的AnimationDrawable
        mAnimation = (AnimationDrawable) mImageView.getBackground();
        // 为了防止在onCreate方法中只显示第一帧的解决方案之一
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();

            }
        });
    }

    private void initView() {
        setContentView(R.layout.loading_progress_frag);

        mImageView = (ImageView) findViewById(R.id.loadingIv);
    }

    /**
     * 显示Dialog
     */
    public void showDialog() {
        if (mAnimation != null && !mAnimation.isRunning()) {
            mAnimation.start();
        }
        this.getWindow().setBackgroundDrawable(new ColorDrawable());
        this.show();

        count = 0;
        Message ms = mHandler.obtainMessage();
        ms.obj = count;
        mHandler.sendMessageDelayed(ms, 1000);
    }

    /**
     * 取消Dialog以及逻辑
     */
    public void cancelDialog() {
        mAnimation.stop();
        this.dismiss();
        this.cancel();
        count = 10;
    }

    /**
     * 转转君是否还在转
     */
    public boolean isShowinged() {
        if (this.isShowing()) {
            return true;
        }
        return false;
    }

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.i("zc", ">>>>>>>>"+count);
            if (count > 10) {

                if (isShowinged()) {
                    cancelDialog();
                    count = 0;
                    Toast.makeText(mContext, "请检查网络连接", Toast.LENGTH_SHORT).show();
                }
            } else {
                count++;
                Message ms = mHandler.obtainMessage();
                ms.obj = count;
                mHandler.sendMessageDelayed(ms, 1000);
            }
        }
    };

}
