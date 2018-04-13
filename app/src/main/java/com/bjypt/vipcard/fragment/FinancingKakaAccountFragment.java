package com.bjypt.vipcard.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.FinancingActivity;
import com.bjypt.vipcard.activity.FinancingTakeNotesActivity;
import com.bjypt.vipcard.activity.LifeServireH5Activity;
import com.bjypt.vipcard.activity.NewTopupWayActivity;
import com.bjypt.vipcard.activity.WithdrawActivity;
import com.bjypt.vipcard.adapter.FinancingAdapter;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.FinancingInfoBean;
import com.bjypt.vipcard.model.FinancingListBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.utils.DialogUtil;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.PermissionUtils;

import org.codehaus.jackson.map.ObjectMapper;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FinancingKakaAccountFragment extends BaseFrament implements View.OnClickListener, VolleyCallBack {

    private View view;
    final private static int REQUEST_BALANCE_INVEST_INFO = 20171312;
    final private static int REQUEST_BALANCE_INVEST_LIST = 20181456;
    private RelativeLayout rl_back;
    private FinancingAdapter financingAdapter;
    private String pkmuser;
    private TextView tv_head_name;
    private TextView tv_balance;
    private TextView tv_doneincomemoney;
    private TextView tv_doneinvestmoney;
    private TextView tv_doinginvestmoney;
    private TextView tv_doingincomemoney;
    private LinearLayout ll_head_service;
    private LinearLayout ll_head_take_notes;
    private LinearLayout ll_head_calculator;
    private ArrayList<FinancingListBean.ResultDataBean> lists = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_financing_kaka_account, container, false);
        return view;
    }
    @Override
    public void onSuccess(int reqcode, Object result) {
        switch (reqcode) {
            case REQUEST_BALANCE_INVEST_INFO:
                loadFinancingInfo(result);
                break;
            case REQUEST_BALANCE_INVEST_LIST:
                LogUtil.debugPrint("INVEST_LIST========" + result);
                loadFinancingList(result);
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    @Override
    public void beforeInitView() {
        Bundle bundle=getArguments();
        pkmuser=bundle.getString("pkmuser");
    }

    @Override
    public void initView() {

        PullToRefreshListView lv_financing = (PullToRefreshListView) view.findViewById(R.id.lv_financing);
        View headerView = LayoutInflater.from(view.getContext()).inflate(R.layout.financing_head, null, false);
        View footerView = LayoutInflater.from(view.getContext()).inflate(R.layout.financing_foot, null, false);
        tv_head_name = (TextView) headerView.findViewById(R.id.tv_head_name);
        tv_balance = (TextView) headerView.findViewById(R.id.tv_balance);
        tv_doneincomemoney = (TextView) headerView.findViewById(R.id.tv_doneincomemoney);
        tv_doneinvestmoney = (TextView) headerView.findViewById(R.id.tv_doneinvestmoney);
        tv_doinginvestmoney = (TextView) headerView.findViewById(R.id.tv_doinginvestmoney);
        tv_doingincomemoney = (TextView) headerView.findViewById(R.id.tv_doingincomemoney);
        ll_head_take_notes = (LinearLayout) headerView.findViewById(R.id.ll_head_take_notes);
        ll_head_calculator = (LinearLayout) headerView.findViewById(R.id.ll_head_calculator);
        ll_head_service = (LinearLayout) headerView.findViewById(R.id.ll_head_service);
//        TextView tt_head_calculator=(TextView) ll_head_calculator.findViewById(R.id.tt_financing_calc);
//        tt_head_calculator.setText("充值");
//        TextView tt_head_service=(TextView) ll_head_service.findViewById(R.id.tt_financing_service);
//        tt_head_service.setText("提现");
        lv_financing.setMode(PullToRefreshBase.Mode.DISABLED);
        ListView listView = lv_financing.getRefreshableView();
        listView.setDividerHeight(0);
        listView.addHeaderView(headerView);
//        listView.addFooterView(footerView);
        financingAdapter = new FinancingAdapter(lists, view.getContext(), pkmuser);
        lv_financing.setAdapter(financingAdapter);
    }

    @Override
    public void afterInitView() {
        getBalanceInvestInfo();
        getBalanceInvestList();
        tv_head_name.setText(getFromSharePreference(Config.userConfig.nickname));
    }

    @Override
    public void bindListener() {
        ll_head_service.setOnClickListener(this);
        ll_head_take_notes.setOnClickListener(this);
        ll_head_calculator.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.ll_head_take_notes:
                // 理财记录
                goTakeNotes();
                break;
            case R.id.ll_head_calculator:
                // 计算器
                goCalculator();
                break;
            case R.id.ll_head_service:
                // 客服
                veryBestCall();
                break;
        }
    }
    /**
     * 请求balanceInvestInfo
     */
    public void getBalanceInvestInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
//        params.put("pkmuser", pkmuser);
        Wethod.httpPost(getActivity(), REQUEST_BALANCE_INVEST_INFO, Config.web.financing_balanceInvestInfo, params, this);
    }

    /**
     * 加载用户信息
     *
     * @param result obj
     */
    @SuppressLint("SetTextI18n")
    private void loadFinancingInfo(Object result) {
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            FinancingInfoBean financingInfoBean = objectMapper.readValue(result.toString(), FinancingInfoBean.class);
            tv_balance.setText(String.valueOf(financingInfoBean.getResultData().getBalance()) + "元");
            tv_doingincomemoney.setText(String.valueOf(financingInfoBean.getResultData().getDoingincomemoney()));
            tv_doinginvestmoney.setText(String.valueOf(financingInfoBean.getResultData().getDoinginvestmoney()));
            tv_doneincomemoney.setText("总收益" + String.valueOf(financingInfoBean.getResultData().getDoneincomemoney()) + "元");
            tv_doneinvestmoney.setText(String.valueOf(financingInfoBean.getResultData().getDoneinvestmoney()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求理财列表
     */
    private void getBalanceInvestList() {
//        Map<String, String> params = new HashMap<>();
//        params.put("pkmuser", pkmuser);
        Wethod.httpPost(view.getContext(), REQUEST_BALANCE_INVEST_LIST, Config.web.yu_e_bao_list, null, this);
    }

    /**
     * 加载首页列表
     *
     * @param result obj
     */
    private void loadFinancingList(Object result) {
        if (lists.size() > 0) {
            lists.clear();
        }
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            FinancingListBean financingListBean = objectMapper.readValue(result.toString(), FinancingListBean.class);
            lists.addAll(financingListBean.getResultData());
            financingAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转至理财记录界面
     */
    private void goTakeNotes() {
        Intent intent = new Intent(view.getContext(), FinancingTakeNotesActivity.class);
        intent.putExtra("pkmuser", pkmuser);
        intent.putExtra("pageFlag", "2");
        startActivity(intent);
    }

    /**
     * 跳转到理财计算器界面
     */
    private void goCalculator() {
//        if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
//            Intent intent4 = new Intent(getActivity(), NewTopupWayActivity.class);
//            intent4.putExtra("FLAG", 2);
//            intent4.putExtra("pkmuser", pkmuser);
//            startActivity(intent4);
//        }
        Intent intent1 = new Intent(getActivity(), LifeServireH5Activity.class);
        intent1.putExtra("life_url", Config.web.calculator_finances);
        intent1.putExtra("isLogin", "N");
        startActivity(intent1);
    }

    /**
     * 提现
     */
//    private void veryBestCall() {
//        if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
//            Intent intent2 = new Intent(getActivity(), WithdrawActivity.class);
//            startActivity(intent2);
//        }
//    }

    /**
     * 打电话给客服
     */
    private void veryBestCall() {
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        PermissionUtils permissionUtils = new PermissionUtils(getActivity(), "4001808366");
                        permissionUtils.requestPermission();
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        break;
                    case Dialog.BUTTON_NEUTRAL:
                        break;
                }
            }
        };
        //弹窗让用户选择，是否允许申请权限
        DialogUtil.showConfirm(getActivity(), "客服热线", "是否拨打招商及客服热线4001808366(08:00-17:00)", dialogOnclicListener, dialogOnclicListener);
    }

}
