package com.bjypt.vipcard.activity;


import android.view.View;
import android.widget.LinearLayout;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.ReturnRecodAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.model.ReturnRecodBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.umeng.UmengCountContext;

import java.util.ArrayList;
import java.util.List;

public class ReturnRecodActivity extends BaseActivity {

private PullToRefreshListView listView;
    private LinearLayout layout_return_back;
    private List<ReturnRecodBean> list;
    private ReturnRecodAdapter adapter;
    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_return_recod);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        listView = (PullToRefreshListView) findViewById(R.id.listview_return);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        layout_return_back = (LinearLayout) findViewById(R.id.layout_return_back);

    }

    @Override
    public void afterInitView() {
        list = new ArrayList<>();
        for (int i=0;i<5;i++){
            ReturnRecodBean bean = new ReturnRecodBean();
            bean.setChognzhi("100");
            bean.setName("支付宝充值");
            bean.setReturn_money("10");
            bean.setTime("2016-5-7 14:20");
            list.add(bean);
        }

    }

    @Override
    public void bindListener() {
        adapter = new ReturnRecodAdapter(this,list);
        listView.setAdapter(adapter);
        layout_return_back.setOnClickListener(this);

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.layout_return_back:
                finish();
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }
}
