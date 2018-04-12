package com.bjypt.vipcard.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.PayDealTypeEnum;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.dialog.PurchaseFinancingSuccessDialog;
import com.bjypt.vipcard.model.FinanceAmountWhereaboutsInfoBean;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.PayAwayView;
import com.bjypt.vipcard.view.ToastUtil;
import com.sinia.orderlang.utils.StringUtil;
import com.unionpay.UPPayAssistEx;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 崔龙 on 2018/1/17.
 * <p>
 * 商家理财购买界面
 */

public class PurchaseFinancingActivity extends BaseActivity implements VolleyCallBack, PayAwayView.OnPayListener {
    private static final int REQUEST_FINANCE_AMOUNT_WHEREABOUTS = 201811901;
    private RelativeLayout rl_back;
    private TextView tv_show;
    private TextView tv_productname;
    private TextView tv_profit;
    private TextView tv_profit_money;
    private TextView tv_total_money;
    private TextView tv_begin_date;
    private TextView tv_over_date;
    private TextView tv_notice;
    private TextView tv_name1;
    private TextView tv_name2;
    private TextView tv_name3;
    private TextView tv_platform_balance_desc;
    private TextView tv_buy_next_desc;
    private TextView tv_merchant_balance_desc;
    private PurchaseFinancingSuccessDialog successDialog;
    private String mTv_productname;
    private String mTv_profit;
    private String mTv_profit_money;
    private String mTv_total_money;
    private String mDiffdate;
    private String pkmuser;
    private LinearLayout ll_use1;
    private LinearLayout ll_use2;
    private LinearLayout ll_use3;
    private View v_use2;
    private View v_use3;
    private ImageView iv_platform_balance_check;
    private ImageView iv_buy_next_check;
    private ImageView iv_merchant_balance_check;
    private int money_whereabout = -1; // 到期后去向默认值
    private PayAwayView payAwayView;          // payAwayView
    private int UN_FLAG = 1;
    private String mPkproduct;
    private String cardnum;
    private Button btn_ok_pay;
    private String mTitle;
    private String categoryid;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_purchase_financing);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        mTv_productname = intent.getStringExtra("tv_productname");
        mTv_profit = intent.getStringExtra("tv_profit");
        mTv_profit_money = intent.getStringExtra("tv_profit_money");
        mTv_total_money = intent.getStringExtra("tv_total_money");
        mDiffdate = intent.getStringExtra("diffdate");
        pkmuser = intent.getStringExtra("pkmuser");
        cardnum = intent.getStringExtra("cardnum");
        mPkproduct = intent.getStringExtra("pkproduct");
        categoryid = intent.getStringExtra("categoryid");
    }

    @Override
    public void initView() {
        payAwayView = (PayAwayView) findViewById(R.id.payAwayView);
        btn_ok_pay = (Button) findViewById(R.id.btn_ok_pay);
        payAwayView.setOnPayListener(this);
        payAwayView.setActivity(this);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        tv_show = (TextView) findViewById(R.id.tv_show);
        tv_productname = (TextView) findViewById(R.id.tv_productname);
        tv_profit = (TextView) findViewById(R.id.tv_profit);
        tv_profit_money = (TextView) findViewById(R.id.tv_profit_money);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        tv_begin_date = (TextView) findViewById(R.id.tv_begin_date);
        tv_over_date = (TextView) findViewById(R.id.tv_over_date);
        tv_notice = (TextView) findViewById(R.id.tv_notice);
        tv_platform_balance_desc = (TextView) findViewById(R.id.tv_platform_balance_desc);
        tv_buy_next_desc = (TextView) findViewById(R.id.tv_buy_next_desc);
        tv_merchant_balance_desc = (TextView) findViewById(R.id.tv_merchant_balance_desc);
        iv_platform_balance_check = (ImageView) findViewById(R.id.iv_platform_balance_check);
        iv_buy_next_check = (ImageView) findViewById(R.id.iv_buy_next_check);
        iv_merchant_balance_check = (ImageView) findViewById(R.id.iv_merchant_balance_check);
        tv_name1 = (TextView) findViewById(R.id.tv_name1);
        tv_name2 = (TextView) findViewById(R.id.tv_name2);
        tv_name3 = (TextView) findViewById(R.id.tv_name3);
        ll_use1 = (LinearLayout) findViewById(R.id.ll_use1);
        ll_use2 = (LinearLayout) findViewById(R.id.ll_use2);
        ll_use3 = (LinearLayout) findViewById(R.id.ll_use3);
        v_use2 = findViewById(R.id.v_use2);
        v_use3 = findViewById(R.id.v_use3);
    }

    @Override
    public void afterInitView() {
        assignment();
        initPaySuccessDialog();
        getFinanceAmountWhereabouts();
        payAwayView.sendHttpPayAways(PayDealTypeEnum.PetroleumPay.getCode() + "", pkmuser, getFromSharePreference(Config.userConfig.pkregister));
    }

    @Override
    public void bindListener() {
        rl_back.setOnClickListener(this);
        tv_show.setOnClickListener(this);
        ll_use1.setOnClickListener(this);
        ll_use2.setOnClickListener(this);
        ll_use3.setOnClickListener(this);
        btn_ok_pay.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_show:
                break;
            case R.id.ll_use1:
                // 平台余额
                money_whereabout = 0;
                iv_platform_balance_check.setImageResource(R.mipmap.quxiangred);
                iv_buy_next_check.setImageResource(R.mipmap.quxianggray);
                iv_merchant_balance_check.setImageResource(R.mipmap.quxianggray);
                break;
            case R.id.ll_use2:
                // 购买下一期
                money_whereabout = 1;
                iv_platform_balance_check.setImageResource(R.mipmap.quxianggray);
                iv_buy_next_check.setImageResource(R.mipmap.quxiangred);
                iv_merchant_balance_check.setImageResource(R.mipmap.quxianggray);
                break;
            case R.id.ll_use3:
                // 石油卡余额
                money_whereabout = 2;
                iv_platform_balance_check.setImageResource(R.mipmap.quxianggray);
                iv_buy_next_check.setImageResource(R.mipmap.quxianggray);
                iv_merchant_balance_check.setImageResource(R.mipmap.quxiangred);
                break;
            case R.id.btn_ok_pay:
                // 石油卡余额
                payAwayView.startPay();
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        switch (reqcode) {
            case REQUEST_FINANCE_AMOUNT_WHEREABOUTS:
                loadFinanceInfo(result);
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        Wethod.ToFailMsg(this, result);
    }

    @Override
    public void onError(VolleyError volleyError) {
        ToastUtil.showToast(this, "网络错误，请检查您的网络");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        successDialog.dismiss();
    }

    /**
     * 基本赋值
     */
    @SuppressLint("SetTextI18n")
    private void assignment() {
        tv_productname.setText("商品：" + mTv_productname);
        if (mTv_profit.contains(".")) {
            tv_profit.setText(mTv_profit.substring(0, mTv_profit.indexOf(".") + 2) + "%");
        } else {
            tv_profit.setText(mTv_profit + "%");
        }
        tv_profit_money.setText(mTv_profit_money + "元");
        tv_total_money.setText(mTv_total_money + "元");
        if (mDiffdate != null && mDiffdate.contains("到")) {
            String[] dates = mDiffdate.split("到");
            tv_begin_date.setText("开始：" + dates[0]);
            tv_over_date.setText("结束：" + dates[1]);
        }
    }

    /**
     * 请求信息
     */
    private void getFinanceAmountWhereabouts() {
        Map<String, String> params = new HashMap<>();
        params.put("pkmuser", pkmuser);
        if (null!=categoryid){
            params.put("categoryid", categoryid);
        }
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(this, REQUEST_FINANCE_AMOUNT_WHEREABOUTS, Config.web.finance_amount_info, params, this, View.GONE);
    }

    /**
     * 加载脚布局数据
     *
     * @param result obj
     */
    @SuppressLint("SetTextI18n")
    private void loadFinanceInfo(Object result) {
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            FinanceAmountWhereaboutsInfoBean financeAmountWhereaboutsInfoBean = objectMapper.readValue(result.toString(), FinanceAmountWhereaboutsInfoBean.class);
            if (StringUtil.isEmpty(financeAmountWhereaboutsInfoBean.getResultData().getFinance_desc())) {
                tv_notice.setText("");
            } else {
                tv_notice.setText(financeAmountWhereaboutsInfoBean.getResultData().getFinance_desc());
            }
            mTitle = financeAmountWhereaboutsInfoBean.getResultData().getTitle();
            if (financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts() != null) {
                // 平台余额
                if (financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getPlatform_banance().getUse().equals("1")) {
                    ll_use1.setVisibility(View.VISIBLE);
                    if (financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getPlatform_banance().getPlatform_balance_check().equals("1")) {
                        iv_platform_balance_check.setImageResource(R.mipmap.quxiangred);
                        money_whereabout = 0;
                    } else {
                        iv_platform_balance_check.setImageResource(R.mipmap.quxianggray);
                    }
                    tv_name1.setText(financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getPlatform_banance().getName());
                    tv_platform_balance_desc.setText(financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getPlatform_banance().getPlatform_balance_desc());
                } else {
                    ll_use1.setVisibility(View.GONE);
                }
                // 购买下一期
                if (financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getBuy_next().getUse().equals("1")) {
                    ll_use2.setVisibility(View.VISIBLE);
                    v_use2.setVisibility(View.VISIBLE);
                    if (financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getBuy_next().getBuy_next_check().equals("1")) {
                        iv_buy_next_check.setImageResource(R.mipmap.quxiangred);
                        money_whereabout = 1;
                    } else {
                        iv_buy_next_check.setImageResource(R.mipmap.quxianggray);
                    }
                    tv_name2.setText(financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getBuy_next().getName());
                    tv_buy_next_desc.setText(financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getBuy_next().getBuy_next_desc());
                } else {
                    ll_use2.setVisibility(View.GONE);
                    v_use2.setVisibility(View.GONE);
                }
                // 石油券
                if (financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getMerchant_balance().getUse().equals("1")) {
                    ll_use3.setVisibility(View.VISIBLE);
                    v_use3.setVisibility(View.VISIBLE);
                    if (financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getMerchant_balance().getMerchant_balance_check().equals("1")) {
                        iv_merchant_balance_check.setImageResource(R.mipmap.quxiangred);
                        money_whereabout = 2;
                    } else {
                        iv_merchant_balance_check.setImageResource(R.mipmap.quxianggray);
                    }
                    tv_name3.setText(financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getMerchant_balance().getName());
                    tv_merchant_balance_desc.setText(financeAmountWhereaboutsInfoBean.getResultData().getWhereAbouts().getMerchant_balance().getMerchant_balance_desc());
                } else {
                    ll_use3.setVisibility(View.GONE);
                    v_use3.setVisibility(View.GONE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnPayFinish() {
        payFinish();
    }

    @Override
    public void onPayCancel() {

    }

    @Override
    public Map<String, String> toPayParams(String outorderid, String payMoney) {
        Map<String, String> param = new HashMap<>();
        param.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        param.put("orderid", outorderid);                                     //订单主键
        param.put("pkmuser", pkmuser);
        param.put("dealtype", PayDealTypeEnum.PetroleumPay.getCode() + "");     //业务类型 1.充值 2立即买单 3优惠券  后续待定
        param.put("amount", payMoney);                                       //金额
        param.put("paytype", payAwayView.getSelectPayCode().getCode() + "");
        param.put("paySubject", mTitle);
        param.put("payBody", mTitle);
        return param;
    }

    @Override
    public Map<String, String> createOrderParams() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        params.put("pkmuser", pkmuser);
        if (null!=cardnum){
            params.put("cardnum", cardnum);
        }
        params.put("amount", AES.encrypt(mTv_total_money + "", AES.key));
        params.put("waitMoney", AES.encrypt(mTv_total_money + "", AES.key));
        params.put("payment", payAwayView.getSelectPayCode().getCode() + "");
        params.put("money_whereabout", money_whereabout + "");
        params.put("pkfinance", mPkproduct);
        params.put("payEntrance", "8");
        return params;
    }

    @Override
    protected void onResume() {
        super.onResume();
        payAwayView.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /********************************************
         *  银联支付回调处理结果
         ********************************************/
        if (UN_FLAG == 1) {
            String str = data.getExtras().getString("pay_result");
            if (str.equalsIgnoreCase("success")) {
                /**********支付成功************/
                payFinish();
            } else if (str.equalsIgnoreCase("fail")) {
                /***********支付失败************/
                ToastUtil.showToast(this, "银联支付失败");
            } else if (str.equalsIgnoreCase("cancel")) {
                /***********取消支付************/
                payAwayView.chargeUpdateCancle();
            }
        } else {
            UN_FLAG = 1;
        }
    }

    /**
     * 银联
     *
     * @param activity
     * @param tn
     * @param mode
     */
    @Override
    public void doStartUnionPayPlugin(Activity activity, String tn, String mode) {
        int ret = UPPayAssistEx.startPay(this, null, null, tn, mode);
        Log.e("TYZ", "ret:" + ret);
        if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
            // 需要重新安装控件
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

            builder.setNegativeButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UPPayAssistEx
                                    .installUPPayPlugin(PurchaseFinancingActivity.this);
                            UN_FLAG = 2;
                            dialog.dismiss();
                        }
                    });

            builder.setPositiveButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }
    }

    private void payFinish() {
        successDialog.show();
    }

    /**
     * 初始化购买成功dialog
     */
    private void initPaySuccessDialog() {
        successDialog = new PurchaseFinancingSuccessDialog(this) {
            @Override
            public void goTakeNotes() {
                super.goTakeNotes();
                Intent intent = new Intent(PurchaseFinancingActivity.this, FinancingTakeNotesActivity.class);
                intent.putExtra("pageFlag", "1");
                intent.putExtra("pkmuser", pkmuser);
                intent.putExtra("categoryid", categoryid);
                startActivity(intent);
                dismiss();
                PurchaseFinancingActivity.this.finish();
            }

            @Override
            public void backHome() {
                super.backHome();
                dismiss();
                PurchaseFinancingActivity.this.finish();
            }
        };
    }
}
