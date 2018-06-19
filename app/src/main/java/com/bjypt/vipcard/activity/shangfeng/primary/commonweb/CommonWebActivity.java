package com.bjypt.vipcard.activity.shangfeng.primary.commonweb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;


import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.data.bean.CommonWebData;

import butterknife.BindView;

/**
 * Created by wanglei on 2018/5/18.
 */

public class CommonWebActivity extends BaseAgentWebActivity {

    @BindView(R.id.tv_merchant_title_name)
    TextView tvTitle;



    private String title;

    public static void callActivity(Context context, CommonWebData commonWebData) {
        Intent intent = new Intent(context, CommonWebActivity.class);
        intent.putExtra("url", commonWebData.getUrl());
        intent.putExtra("title", commonWebData.getTitle());
        context.startActivity(intent);
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_web);
//        ButterKnife.bind(this);
//
//
//    }


    @NonNull
    @Override
    protected ViewGroup getAgentWebParent() {
        return (ViewGroup) this.findViewById(R.id.container);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shangfeng_web;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void init() {
        url = getIntent().getStringExtra("url");
//        url = "http://192.168.0.103:8081/order/index.html#/myorder?&pkregister=26fbe557f6c03c459015f7aa7c22b238&phoneno=18362915512";
        title = getIntent().getStringExtra("title");

        tvTitle.setText(title);
        buildAgentWeb();
        findViewById(R.id.ibtn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean is_back = mAgentWeb.back();
                if(!is_back){
                    finish();
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb != null && mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void setTitle(WebView view, String title) {
        tvTitle.setText(title);
    }

    @Override
    protected int getIndicatorColor() {
        return Color.parseColor("#9BA2FD");
    }

    @Override
    protected int getIndicatorHeight() {
        return 3;
    }

    @Nullable
    @Override
    protected String getUrl() {
        return url;
    }
}
