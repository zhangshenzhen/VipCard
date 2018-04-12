package com.bjypt.vipcard.activity;


import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.YuEBaoAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.YuEBean;
import com.bjypt.vipcard.model.YuERootBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class YuEBaoActivity extends BaseActivity implements VolleyCallBack {
    private ListView plistview_yu_e_bao;
    private TextView goumai_record;
    private LinearLayout ly_back_yu_e_bao;
    private YuEBaoAdapter adapter;
    private String url = Config.web.yu_e_bao_list;
    private List<YuEBean> list = new ArrayList<>();

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_yu_ebao);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        plistview_yu_e_bao = (ListView) findViewById(R.id.plistview_yu_e_bao);
        goumai_record = (TextView) findViewById(R.id.goumai_record);
        ly_back_yu_e_bao = (LinearLayout) findViewById(R.id.ly_back_yu_e_bao);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_yuebao_listview_foot, null);
        plistview_yu_e_bao.addFooterView(view);
        View headerView = LayoutInflater.from(this).inflate(R.layout.activity_yuebao_listview_header, null);
        plistview_yu_e_bao.addHeaderView(headerView);
    }

    @Override
    public void afterInitView() {

        Wethod.httpPost(this, 11, url, null, this);
    }

    @Override
    public void bindListener() {
        ly_back_yu_e_bao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        goumai_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(YuEBaoActivity.this, YuEBaoRecordActivity.class));
            }
        });
    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 11) {
            Log.e("liyunteee", result.toString());
            try {
                YuERootBean rootBean = getConfiguration().readValue(result.toString(), YuERootBean.class);
                Log.e("liyunte", rootBean.getMsg());
                Log.e("liyunte", rootBean.getResultStatus());
                Log.e("liyunte", rootBean.getResultData().get(0).getProductname());
                if (list.size() > 0) {
                    list.clear();
                }
                list.addAll(rootBean.getResultData());
                adapter = new YuEBaoAdapter(this, list);
                plistview_yu_e_bao.setAdapter(adapter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
