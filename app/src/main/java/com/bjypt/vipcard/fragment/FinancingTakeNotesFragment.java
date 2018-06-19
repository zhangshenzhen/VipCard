package com.bjypt.vipcard.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.FinancingTakeNotesAdapter;
import com.bjypt.vipcard.adapter.PetroleumFinancingTakeNotesAdapter;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.FinancingTakeNotesListBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.ToastUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 崔龙 on 2018/1/4.
 * 理财记录嵌套fragment
 */

public class FinancingTakeNotesFragment extends BaseFrament implements VolleyCallBack {
    private static final int REQUEST_FINANCING_TAKE_NOTES = 201814789;
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFRESH = 0x0110;
    private int begin = 1;
    private View view;
    private String name;
    private String pkmuser;
    private String categoryid;
    private String pageFlag; // "1" 代表商家理财  "2" 代表普通理财
    private BroadCastReceiverUtils utils;
    private PullToRefreshListView lv_financing_item;
    private boolean is_refresh = true;
    private ArrayList<FinancingTakeNotesListBean.ResultDataBean> lists = new ArrayList<>();
    private FinancingTakeNotesAdapter mAdapter;
    private PetroleumFinancingTakeNotesAdapter petroleumFinancingTakeNotesAdapter;
    private MyBroadCastReceiver myBroadCastReceiver;

    public static FinancingTakeNotesFragment getInstance(String name, String pkmuser, String pageFlag, String categoryid) {
        FinancingTakeNotesFragment fragment = new FinancingTakeNotesFragment();
        fragment.name = name;
        fragment.pkmuser = pkmuser;
        fragment.categoryid = categoryid;
        fragment.pageFlag = pageFlag; // "1" 代表商家理财  "2" 代表普通理财
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_financing_take_notes, container, false);
        return view;
    }

    @Override
    public void beforeInitView() {
        utils = new BroadCastReceiverUtils();
        myBroadCastReceiver = new MyBroadCastReceiver();
        utils.registerBroadCastReceiver(getActivity(), "selectOk", myBroadCastReceiver);
    }

    @Override
    public void initView() {
        lv_financing_item = (PullToRefreshListView) view.findViewById(R.id.lv_financing_item);
        lv_financing_item.setMode(PullToRefreshBase.Mode.BOTH);
        ListView listView = lv_financing_item.getRefreshableView();
        listView.setDividerHeight(DensityUtil.dip2px(getActivity(), 9));
    }

    @Override
    public void afterInitView() {
        requestFinancingTakeInfo(QUERY_REFRESH);
        if (pageFlag.equals("1")) { // "1" 代表商家理财  "2" 代表普通理财
            petroleumFinancingTakeNotesAdapter = new PetroleumFinancingTakeNotesAdapter(lists, getActivity(), categoryid);
            lv_financing_item.setAdapter(petroleumFinancingTakeNotesAdapter);
        } else {
            mAdapter = new FinancingTakeNotesAdapter(lists, getActivity());
            lv_financing_item.setAdapter(mAdapter);
        }
        lv_financing_item.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestFinancingTakeInfo(QUERY_REFRESH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestFinancingTakeInfo(QUERY_MORE);
            }
        });
    }

    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        lv_financing_item.onRefreshComplete();// 停止刷新
        switch (reqcode) {
            case REQUEST_FINANCING_TAKE_NOTES:
                LogUtil.debugPrint("FinancingTakeNotesFragment====" + result);
                loadFinancingTakeNotesList(result);
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
//        Wethod.ToFailMsg(getActivity(), result);
        lv_financing_item.onRefreshComplete();// 停止刷新
    }

    @Override
    public void onError(VolleyError volleyError) {
        ToastUtil.showToast(getActivity(), "网络错误，请检查您的网络");
        lv_financing_item.onRefreshComplete();// 停止刷新
    }

    /**
     * 请求理财记录接口
     */
    private void requestFinancingTakeInfo(int type) {
        int pageLength = 10;
        if (type == QUERY_REFRESH) {
            begin = 1;
            is_refresh = true;
        } else {
            begin += 1;
            is_refresh = false;
        }
        Map<String, String> params = new HashMap<>();
        params.put("pkRegister", getFromSharePreference(Config.userConfig.pkregister));
//        params.put("pkmuser", pkmuser);
        params.put("pageIndex", begin + "");
        params.put("pageSize", pageLength + "");
        if (name.equals("已结束")) {
            params.put("type", "2");
        } else {
            params.put("type", "1");
        }
        if (pageFlag.equals("1")) { // "1" 代表商家理财  "2" 代表普通理财
            if (null!=categoryid){
                params.put("categoryid", categoryid);
            }
        }
        Wethod.httpPost(getActivity(), REQUEST_FINANCING_TAKE_NOTES, Config.web.yu_e_bao_record, params, this);
    }

    /**
     * 加载列表
     *
     * @param result obj
     */
    private void loadFinancingTakeNotesList(Object result) {
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            FinancingTakeNotesListBean financingTakeNotesListBean = objectMapper.readValue(result.toString(), FinancingTakeNotesListBean.class);
            if (is_refresh) { // 下拉刷新
                lists.clear();
                lists.addAll(financingTakeNotesListBean.getResultData());
            } else { // 加载更多
                lists.addAll(financingTakeNotesListBean.getResultData());
            }
            if (pageFlag.equals("1")) { // "1" 代表商家理财  "2" 代表普通理财
                petroleumFinancingTakeNotesAdapter.notifyDataSetChanged();
            } else {
                mAdapter.notifyDataSetChanged();
            }
            lv_financing_item.onRefreshComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            requestFinancingTakeInfo(QUERY_REFRESH);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (utils != null) {
            utils.UnRegisterBroadCastReceiver(getActivity(), myBroadCastReceiver);
        }
    }
}
