package com.bjypt.vipcard.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.Interface.SelectSuccessListener;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.CommentDetailsClBean;
import com.bjypt.vipcard.model.FinanceAmountWhereaboutsInfoBean;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.AutoZoomTextView;
import com.bjypt.vipcard.view.CheckPwdDialog;
import com.bjypt.vipcard.view.ToastUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 崔龙 on 2018/1/17.
 * <p>
 * 石油广告
 */

public class GoToSelectView extends LinearLayout implements VolleyCallBack, View.OnClickListener {
    private static final int REQUEST_FINANCE_AMOUNT_WHEREABOUTS = 201811901;
    private static final int REQUEST_UPDATE_AMOUNT_WHEREABOUTS = 20181231;
    private Context mContext;
    private String mPkmuser;
    private String mInvestid;
    private String mCategoryid;
    private ImageView iv_type1;
    private ImageView iv_type2;
    private ImageView iv_type3;
    private LinearLayout ll_type1;
    private LinearLayout ll_type2;
    private LinearLayout ll_type3;
    private AutoZoomTextView tv_name1;
    private AutoZoomTextView tv_name2;
    private AutoZoomTextView tv_name3;
    private View v_use2;
    private View v_use3;
    private AutoZoomTextView tv_platform_balance_desc;
    private AutoZoomTextView tv_buy_next_desc;
    private AutoZoomTextView tv_merchant_balance_desc;
    private int money_whereabout = -1; // 到期后去向默认值
    private SelectSuccessListener mListener;

    public GoToSelectView(Context context) {
        super(context);
        initView(context);
    }

    public GoToSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public GoToSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.go_to_select_item, this);
        iv_type1 = (ImageView) findViewById(R.id.iv_type1);
        iv_type2 = (ImageView) findViewById(R.id.iv_type2);
        iv_type3 = (ImageView) findViewById(R.id.iv_type3);
        ll_type1 = (LinearLayout) findViewById(R.id.ll_type1);
        ll_type2 = (LinearLayout) findViewById(R.id.ll_type2);
        ll_type3 = (LinearLayout) findViewById(R.id.ll_type3);
        tv_name1 = (AutoZoomTextView) findViewById(R.id.tv_name1);
        tv_name2 = (AutoZoomTextView) findViewById(R.id.tv_name2);
        tv_name3 = (AutoZoomTextView) findViewById(R.id.tv_name3);
        tv_platform_balance_desc = (AutoZoomTextView) findViewById(R.id.tv_platform_balance_desc);
        tv_buy_next_desc = (AutoZoomTextView) findViewById(R.id.tv_buy_next_desc);
        tv_merchant_balance_desc = (AutoZoomTextView) findViewById(R.id.tv_merchant_balance_desc);
        v_use2 = findViewById(R.id.v_use2);
        v_use3 = findViewById(R.id.v_use3);
        ll_type1.setOnClickListener(this);
        ll_type2.setOnClickListener(this);
        ll_type3.setOnClickListener(this);
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        switch (reqcode) {
            case REQUEST_FINANCE_AMOUNT_WHEREABOUTS:
                loadData(result);
                break;
            case REQUEST_UPDATE_AMOUNT_WHEREABOUTS:
                loadUpdateData(result);
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        Wethod.ToFailMsg(mContext, result);
    }

    @Override
    public void onError(VolleyError volleyError) {
        ToastUtil.showToast(mContext, "网络错误，请检查您的网络设置");
    }

    /**
     * 请求购买石油理财的入口
     *
     * @param context 上下文
     * @param pkmuser pkmuser
     */
    public void requestGoToSelect(final Context context, String pkmuser, String investid, String categoryid) {
        mContext = context;
        mPkmuser = pkmuser;
        mInvestid = investid;
        mCategoryid = categoryid;
        Map<String, String> params = new HashMap<>();
        params.put("pkmuser", pkmuser);
        params.put("categoryid", categoryid);
        params.put("pkregister", SharedPreferenceUtils.getFromSharedPreference(context, Config.userConfig.pkregister));
        Wethod.httpPost(context, REQUEST_FINANCE_AMOUNT_WHEREABOUTS, Config.web.finance_amount_info, params, this, View.GONE);
    }

    /**
     * 加载解析正确返回数据
     *
     * @param result 数据
     */
    @SuppressLint("SetTextI18n")
    private void loadData(Object result) {
        Log.e("gotogoto", result + "");
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            FinanceAmountWhereaboutsInfoBean financeAmountWhereaboutsInfoBean = objectMapper.readValue(result.toString(), FinanceAmountWhereaboutsInfoBean.class);
            if (financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts() != null) {
                // 平台余额
                if (financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getPlatform_banance().getUse().equals("1")) {
                    ll_type1.setVisibility(View.VISIBLE);
                    if (financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getPlatform_banance().getPlatform_balance_check().equals("1")) {
                        iv_type1.setImageResource(R.mipmap.quxiangred);
                        money_whereabout = 0;
                    } else {
                        iv_type1.setImageResource(R.mipmap.quxianggray);
                    }
                    tv_name1.setText(financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getPlatform_banance().getName());
                    tv_platform_balance_desc.setText(financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getPlatform_banance().getPlatform_balance_desc());
                } else {
                    ll_type1.setVisibility(View.GONE);
                }
                // 购买下一期
                if (financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getBuy_next().getUse().equals("1")) {
                    ll_type2.setVisibility(View.VISIBLE);
                    v_use2.setVisibility(View.VISIBLE);
                    if (financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getBuy_next().getBuy_next_check().equals("1")) {
                        iv_type2.setImageResource(R.mipmap.quxiangred);
                        money_whereabout = 1;
                    } else {
                        iv_type2.setImageResource(R.mipmap.quxianggray);
                    }
                    tv_name2.setText(financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getBuy_next().getName());
                    tv_buy_next_desc.setText(financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getBuy_next().getBuy_next_desc());
                } else {
                    ll_type2.setVisibility(View.GONE);
                    v_use2.setVisibility(View.GONE);
                }
                // 石油券
                if (financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getMerchant_balance().getUse().equals("1")) {
                    ll_type3.setVisibility(View.VISIBLE);
                    v_use3.setVisibility(View.VISIBLE);
                    if (financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getMerchant_balance().getMerchant_balance_check().equals("1")) {
                        iv_type3.setImageResource(R.mipmap.quxiangred);
                        money_whereabout = 2;
                    } else {
                        iv_type3.setImageResource(R.mipmap.quxianggray);
                    }
                    tv_name3.setText(financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getMerchant_balance().getName());
                    tv_merchant_balance_desc.setText(financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getMerchant_balance().getMerchant_balance_desc());
                } else {
                    ll_type3.setVisibility(View.GONE);
                    v_use3.setVisibility(View.GONE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_type1:
                money_whereabout = 0;
                iv_type1.setImageResource(R.mipmap.quxiangred);
                iv_type2.setImageResource(R.mipmap.quxianggray);
                iv_type3.setImageResource(R.mipmap.quxianggray);
                break;
            case R.id.ll_type2:
                money_whereabout = 1;
                iv_type1.setImageResource(R.mipmap.quxianggray);
                iv_type2.setImageResource(R.mipmap.quxiangred);
                iv_type3.setImageResource(R.mipmap.quxianggray);
                break;
            case R.id.ll_type3:
                money_whereabout = 2;
                iv_type1.setImageResource(R.mipmap.quxianggray);
                iv_type2.setImageResource(R.mipmap.quxianggray);
                iv_type3.setImageResource(R.mipmap.quxiangred);
                break;
        }
    }

    public void pleaseInputPassword() {
        CheckPwdDialog psdDialog = new CheckPwdDialog(getContext(), "", "", "", new CheckPwdDialog.CheckPayPasswordListener() {
            @Override
            public void onPass() {
                requestUpdateAmountWhereAbout();
            }
        });
        psdDialog.show();
    }

    public void requestUpdateAmountWhereAbout() {
        Map<String, String> params = new HashMap<>();
        params.put("pkmuser", mPkmuser);
        params.put("money_whereabout", money_whereabout + "");
        params.put("investid", mInvestid);
        params.put("categoryid", mCategoryid);
        params.put("pkregister", SharedPreferenceUtils.getFromSharedPreference(mContext, Config.userConfig.pkregister));
        Wethod.httpPost(mContext, REQUEST_UPDATE_AMOUNT_WHEREABOUTS, Config.web.finance_amount_updateAmountWhereabout, params, this, View.GONE);
    }

    public void setSelectSuccessListener(SelectSuccessListener arg) {
        mListener = arg;
    }

    /**
     * 加载更新去向数据
     *
     * @param result
     */
    private void loadUpdateData(Object result) {
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            CommentDetailsClBean commentDetailsClBean = objectMapper.readValue(result.toString(), CommentDetailsClBean.class);
            String resultData = commentDetailsClBean.getResultData();
            ToastUtil.showToast(mContext, resultData);
            Intent intent = new Intent();
            intent.setAction("selectOk");
            mContext.sendBroadcast(intent);
            if (mListener != null) {
                mListener.selectOk();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
