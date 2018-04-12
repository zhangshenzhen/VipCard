package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.BusinessListAdapter;
import com.bjypt.vipcard.adapter.BusinessSelectMoneyAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.BusinessFinancingSelectBean;
import com.bjypt.vipcard.model.FinanceAmountWhereaboutsInfoBean;
import com.bjypt.vipcard.model.FinancingListBean;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.widget.MyGridView;
import com.sinia.orderlang.utils.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 崔龙 on 2018/1/17.
 * <p>
 * 商家理财
 */

public class BusinessFinancingActivity extends BaseActivity implements VolleyCallBack {
    private static final int REQUEST_SELECT_MONEY = 201811901;
    private static final int REQUEST_BALANCE_INVEST_LIST = 201811902;
    private static final int REQUEST_FINANCE_AMOUNT_WHEREABOUTS = 201811903;
    private ListView lv_business;
    private RelativeLayout rl_back;
    private TextView tv_take_notes;
    private TextView tv_title;
    private MyGridView gv_select_money;
    private BusinessSelectMoneyAdapter mMoneyAdapter;
    private BusinessListAdapter mBusinessListAdapter;
    private String pkmuser;
    private String cardnum;
    private String categoryid;
    private ArrayList<BusinessFinancingSelectBean.ResultDataBean> selectMoneyList = new ArrayList<>();
    private ArrayList<FinancingListBean.ResultDataBean> lists = new ArrayList<>();
    private TextView tv_notice;
    private ImageView iv_moren;
    private LinearLayout ll_content;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_business_financing);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        pkmuser = intent.getStringExtra("pkmuser");
        cardnum = intent.getStringExtra("cardnum");
        categoryid = intent.getStringExtra("categoryid");
    }

    @Override
    public void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        iv_moren = (ImageView) findViewById(R.id.iv_moren);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        tv_take_notes = (TextView) findViewById(R.id.tv_take_notes);
        tv_title = (TextView) findViewById(R.id.tv_title);
        gv_select_money = (MyGridView) findViewById(R.id.gv_select_money);
        lv_business = (ListView) findViewById(R.id.lv_business);
        View footerView = LayoutInflater.from(this).inflate(R.layout.footer_business_financing, null);
        tv_notice = (TextView) footerView.findViewById(R.id.tv_notice);
        lv_business.addFooterView(footerView);

        // 请求网络
        requestSelectMoney();
        getFinanceAmountWhereabouts();
    }

    @Override
    public void afterInitView() {
        mMoneyAdapter = new BusinessSelectMoneyAdapter(this, selectMoneyList);
        gv_select_money.setAdapter(mMoneyAdapter);
        mBusinessListAdapter = new BusinessListAdapter(this, lists, pkmuser, categoryid, cardnum);
        lv_business.setAdapter(mBusinessListAdapter);

        gv_select_money.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 得到的条目点击数据
                // 设置选择适配器的选中状态
                mMoneyAdapter.setSelection(position);
                // 给列表适配器传入总金额
                mBusinessListAdapter.setTotalMoney(selectMoneyList.get(position).getFinance_amount());
                // 刷新适配器
                mMoneyAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void bindListener() {
        rl_back.setOnClickListener(this);
        tv_take_notes.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_take_notes:
                Intent intent = new Intent(this, FinancingTakeNotesActivity.class);
                intent.putExtra("pageFlag", "1");
                intent.putExtra("pkmuser", pkmuser);
                intent.putExtra("categoryid", categoryid);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        switch (reqcode) {
            case REQUEST_SELECT_MONEY:
                loadSelectMoneyData(result);
                break;
            case REQUEST_BALANCE_INVEST_LIST:
                loadFinancingList(result);
                break;
            case REQUEST_FINANCE_AMOUNT_WHEREABOUTS:
                loadFinanceInfo(result);
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
//        Wethod.ToFailMsg(this, result);
    }

    @Override
    public void onError(VolleyError volleyError) {
        ToastUtil.showToast(this, "网络错误，请检查您的网络");
    }

    /**
     * 请求加载选择金额的接口
     */
    private void requestSelectMoney() {
        Map<String, String> params = new HashMap<>();
        params.put("pkmuser", pkmuser);
        if (null != categoryid) {
            params.put("categoryid", categoryid);
        }
        Wethod.httpPost(this, REQUEST_SELECT_MONEY, Config.web.recharge_merchant_select_money, params, this);
    }

    /**
     * 加载解析正确返回数据
     *
     * @param result 数据
     */
    private void loadSelectMoneyData(Object result) {
        if (selectMoneyList.size() > 0) {
            selectMoneyList.clear();
        }
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            BusinessFinancingSelectBean businessFinancingSelectBean = objectMapper.readValue(result.toString(), BusinessFinancingSelectBean.class);
            if (businessFinancingSelectBean.getResultData().size() > 0) {
                selectMoneyList.addAll(businessFinancingSelectBean.getResultData());
            } else {
                ll_content.setVisibility(View.GONE);
                iv_moren.setVisibility(View.VISIBLE);
            }
            // 选择默认的金额
            mMoneyAdapter.setSelection(0);
            // 请求列表网络
            getBalanceInvestList();
            // 刷新适配器
            mMoneyAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求理财列表
     */
    private void getBalanceInvestList() {
        Map<String, String> params = new HashMap<>();
        params.put("pkmuser", pkmuser);
        if (null != categoryid) {
            params.put("categoryid", categoryid);
        }
        Wethod.httpPost(this, REQUEST_BALANCE_INVEST_LIST, Config.web.yu_e_bao_list, params, this, View.GONE);
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
            if (selectMoneyList != null && selectMoneyList.size() > 0) {
                mBusinessListAdapter.setTotalMoney(selectMoneyList.get(0).getFinance_amount());
            }
            mBusinessListAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求信息
     */
    private void getFinanceAmountWhereabouts() {
        Map<String, String> params = new HashMap<>();
        params.put("pkmuser", pkmuser);
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        if (null != categoryid) {
            params.put("categoryid", categoryid);
        }
        Wethod.httpPost(this, REQUEST_FINANCE_AMOUNT_WHEREABOUTS, Config.web.finance_amount_info, params, this, View.GONE);
    }

    /**
     * 加载脚布局数据
     *
     * @param result obj
     */
    private void loadFinanceInfo(Object result) {
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            FinanceAmountWhereaboutsInfoBean financeAmountWhereaboutsInfoBean = objectMapper.readValue(result.toString(), FinanceAmountWhereaboutsInfoBean.class);
            if (StringUtil.isEmpty(financeAmountWhereaboutsInfoBean.getResultData().getFinance_desc())) {
                tv_notice.setText("");
            } else {
                tv_notice.setText(financeAmountWhereaboutsInfoBean.getResultData().getFinance_desc());
            }
            if (financeAmountWhereaboutsInfoBean.getResultData().getTitle() != null) {
                tv_title.setText(financeAmountWhereaboutsInfoBean.getResultData().getTitle());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
