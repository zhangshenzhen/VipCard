package com.bjypt.vipcard.activity.crowdfunding;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.cf.AccountListAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.MerchantPullListBean;
import com.bjypt.vipcard.model.cf.CfAccountData;
import com.bjypt.vipcard.model.cf.CfAccountListData;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.utils.ObjectMapperFactory;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrowdfundingAccountListActivity extends BaseActivity implements VolleyCallBack {

    TextView tv_merchant_title_name;
    PullToRefreshListView lv_account_list;
    ImageButton ibtn_back;

    final int QUERY_EXERCISE_MORE = 0x0101;
    final int QUERY_EXERCISE_REFERSH = 0x0110;
    private int begin = 0;
    private int pageLength = 10;
    private boolean is_refresh = true;

    private static final int request_code_account_list = 123;
    private List<CfAccountData> resultDataBeanList ;

    AccountListAdapter accountListAdapter;


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_crowdfunding_account_list);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_merchant_title_name = (TextView) findViewById(R.id.tv_merchant_title_name);
        lv_account_list = (PullToRefreshListView) findViewById(R.id.lv_account_list);
        ibtn_back = (ImageButton) findViewById(R.id.ibtn_back);

        ibtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_merchant_title_name.setText("众筹列表");
    }


    private void loadDatas(int refresh_type) {
        Map<String, String> params = new HashMap<>();
        if (refresh_type == QUERY_EXERCISE_REFERSH) {
            begin = 1;
            is_refresh = true;
        } else {
            begin += 1;
            is_refresh = false;
        }

        params.put("pageNum", begin + "");
        params.put("pageSize", pageLength + "");
        params.put("pkregister", getPkregister());
        Wethod.httpPost(this, request_code_account_list, Config.web.getCFAccountList, params, this, View.GONE);

    }

    @Override
    public void afterInitView() {

        resultDataBeanList = new ArrayList<>();
        accountListAdapter = new AccountListAdapter(this, resultDataBeanList);
        lv_account_list.setAdapter(accountListAdapter);

        lv_account_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadDatas(QUERY_EXERCISE_REFERSH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadDatas(QUERY_EXERCISE_MORE);
            }
        });

        //首次来到页面加载数据
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                lv_account_list.setRefreshing();
                return true;
            }
        }).sendEmptyMessageDelayed(0, 300);
    }

    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()){




        }

    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        lv_account_list.onRefreshComplete();
        switch (reqcode) {
            case request_code_account_list:
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                try {
                    CfAccountListData merchantListBean = objectMapper.readValue(result.toString(), CfAccountListData.class);
                    // 判断是下拉刷新还是上拉加载
                    if (is_refresh) { // 下拉刷新
                        resultDataBeanList.clear();
                        resultDataBeanList.addAll(merchantListBean.getResultData().getList());
                    } else { // 加载更多
                        resultDataBeanList.addAll(merchantListBean.getResultData().getList());
                    }
                    accountListAdapter.notifyDataSetChanged();
                    lv_account_list.onRefreshComplete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        lv_account_list.onRefreshComplete();
    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
