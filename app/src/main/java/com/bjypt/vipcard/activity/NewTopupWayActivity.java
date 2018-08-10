package com.bjypt.vipcard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.SelectMoneyAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.PayDealTypeEnum;
import com.bjypt.vipcard.common.TrackCommon;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.LimitBean;
import com.bjypt.vipcard.model.SelectMoneyBean;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.MyDialogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.MyDialog;
import com.bjypt.vipcard.view.PayAwayView;
import com.bjypt.vipcard.widget.MyGridView;
import com.orhanobut.logger.Logger;
import com.sinia.orderlang.utils.StringUtil;
import com.unionpay.UPPayAssistEx;

import org.codehaus.jackson.map.ObjectMapper;
import org.piwik.sdk.extra.TrackHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 崔龙 on 2017/6/2.
 */
public class NewTopupWayActivity extends BaseActivity implements VolleyCallBack, PayAwayView.OnPayListener {

    private static final int REQUEST_SELECT_MONEY = 20171288;
    private RelativeLayout back_way;            // 返回
    private String pkmuser;                   // 主键
    private TextView tv_way_amount;           // 金额显示
    private PayAwayView payAwayView;          // payAwayView
    private Button btn_ok_pay;                // 确认按钮
    private String primaryk;                  // 前一页面生成的订单返回的订单号
    private String outorderid;                // 支付中心对用的订单号
    private String payMoney;                  // 显示金额
    private MyGridView gv_select_money;
    private LinearLayout ll_et_show;
    private EditText et_top_up_amount;
    private TextView tv_renminbi;             // 人民币符号根据内容移动
    private int FLAG;
    private int UN_FLAG = 1;
    private TextView tv_weixintishi;         //绑定银行卡跳转此页面时显示温馨提示
    private ArrayList<SelectMoneyBean.ResultDataBean> moneyList = new ArrayList<>();
    private String orderId;
    private String cardnum = "";
    private SelectMoneyAdapter mMoneyAdapter;
    private String mRecharge_amount;
    private int showType = -1; // 默认走输入金额逻辑
    private static final int LIMIT = 1110; // 保留2位小数
    private LimitBean limitBean;
    private String startMoney;
    private String endMoney;
    private String tipEnd;
    private String tipStart;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_topup_way_new);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        payMoney = intent.getStringExtra("payMoney");
        pkmuser = intent.getStringExtra("pkmuser");
        primaryk = intent.getStringExtra("primaryk");
        outorderid = intent.getStringExtra("outorderid");
        cardnum = getIntent().getStringExtra("cardnum");

        //充值、提现需调用的接口获取上下限
        requestLimit();

        TrackHelper.track().screen(TrackCommon.ViewTrackRecharge).title(TrackCommon.ViewTrackRecharge).with(getTracker());
    }

    @Override
    public void initView() {
        gv_select_money = (MyGridView) findViewById(R.id.gv_select_money);
        ll_et_show = (LinearLayout) findViewById(R.id.ll_et_show);
        tv_weixintishi = (TextView) findViewById(R.id.tv_weixintishi);
        back_way = (RelativeLayout) findViewById(R.id.back_way);
        payAwayView = (PayAwayView) findViewById(R.id.payAwayView);
        payAwayView.setOnPayListener(this);
        payAwayView.setActivity(this);
        tv_way_amount = (TextView) findViewById(R.id.tv_way_amount);
        btn_ok_pay = (Button) findViewById(R.id.btn_ok_pay);
        et_top_up_amount = (EditText) findViewById(R.id.et_top_up_amount);
        tv_renminbi = (TextView) findViewById(R.id.tv_renminbi);
        tv_way_amount.setText("¥ " + payMoney);

//        SpannableString ss = new SpannableString("温馨提示：尊敬的用户您好，提现之前请先完成平台余额充值，充值的金额可全部提现(每日15:00前当天到账，节假日顺延)，也可在平台所有商家消费使用。");
//        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#f94c64")),
//                0, 38, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#7AC6E8")),
//                38, 58, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#f94c64")),
//                58, 73, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tv_weixintishi.setText(ss);

        mMoneyAdapter = new SelectMoneyAdapter(this, moneyList);
        gv_select_money.setAdapter(mMoneyAdapter);
    }

    @Override
    public void afterInitView() {
        requestSelectMoney();
        payAwayView.sendHttpPayAways(null, pkmuser, getFromSharePreference(Config.userConfig.pkregister));
    }

    @Override
    public void bindListener() {
        back_way.setOnClickListener(this);
        btn_ok_pay.setOnClickListener(this);

        et_top_up_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtil.isNotEmpty(s.toString())) {
                    btn_ok_pay.setEnabled(true);
                    tv_renminbi.setVisibility(View.VISIBLE);
                    Paint paint = new Paint();
                    paint.setTextSize(DensityUtil.sp2px(NewTopupWayActivity.this, 16));
                    int off = DensityUtil.getTextWidth(paint, s.toString());
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) tv_renminbi.getLayoutParams();
                    params.rightMargin = DensityUtil.dip2px(NewTopupWayActivity.this, 15) + off;
                    tv_renminbi.setLayoutParams(params);
                } else {
                    btn_ok_pay.setEnabled(false);
                    tv_renminbi.setVisibility(View.GONE);
                }
            }
        });

        gv_select_money.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 得到的条目点击数据
                mRecharge_amount = moneyList.get(position).getRecharge_amount();
                mMoneyAdapter.setSelection(position);
                mMoneyAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.back_way:
                finish();
                break;
            case R.id.btn_ok_pay:
                if (showType == -1) {
                    if (et_top_up_amount.getText().toString().equals("") || et_top_up_amount.getText().toString() == null) {
                        Toast.makeText(this, "请输入充值金额", Toast.LENGTH_LONG).show();
                    } else if (Integer.parseInt(et_top_up_amount.getText().toString()) * 1 == 0) {
                        Toast.makeText(this, "请输入充值金额大于0", Toast.LENGTH_LONG).show();
                    } else {
                        if (payAwayView.getSelectPayCode() == null) {
                            Toast.makeText(this, "请选择充值方式", Toast.LENGTH_LONG).show();
                            return;
                        }
//                        else if (Double.parseDouble(et_top_up_amount.getText().toString()) > Double.parseDouble(endMoney)) {
//                            Toast.makeText(this, tipEnd, Toast.LENGTH_LONG).show();
//                        }
                        else if (Double.parseDouble(et_top_up_amount.getText().toString()) < Double.parseDouble(startMoney)) {
                            Toast.makeText(this, tipStart, Toast.LENGTH_LONG).show();
                        }

                        if (et_top_up_amount.getText().toString().substring(0, 1).equals("0")) {
                            Toast.makeText(this, "首位数字不能为0", Toast.LENGTH_LONG).show();
                        } else {
                            payAwayView.startPay();
                        }
                    }
                } else if (showType == 1) {
                    payAwayView.startPay();
                }
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (moneyList != null) {
            moneyList.clear();
        }
        switch (reqcode) {
            case LIMIT:
                try {
                    limitBean = getConfiguration().readValue(result.toString(), LimitBean.class);
                    startMoney = limitBean.getResultData().getStart();
                    endMoney = limitBean.getResultData().getEnd();
                    tipEnd = limitBean.getResultData().getTip_end();
                    tipStart = limitBean.getResultData().getTip_start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_SELECT_MONEY:
                gv_select_money.setVisibility(View.VISIBLE);
                ll_et_show.setVisibility(View.GONE);
                btn_ok_pay.setEnabled(true);
                showType = 1;
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                try {
                    SelectMoneyBean selectMoneyBean = objectMapper.readValue(result.toString(), SelectMoneyBean.class);
                    List<SelectMoneyBean.ResultDataBean> resultData = selectMoneyBean.getResultData();
                    moneyList.addAll(resultData);
                    // 默认 情况下选择
                    mRecharge_amount = moneyList.get(0).getRecharge_amount();
                    mMoneyAdapter.setSelection(0);
                    mMoneyAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        switch (reqcode) {
            case REQUEST_SELECT_MONEY:
                gv_select_money.setVisibility(View.GONE);
                ll_et_show.setVisibility(View.VISIBLE);
                showType = -1;
                break;
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        gv_select_money.setVisibility(View.GONE);
        ll_et_show.setVisibility(View.GONE);
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
        orderId = outorderid;
        Map<String, String> param = new HashMap<>();
        param.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        param.put("orderid", outorderid);                                     //订单主键
        param.put("pkmuser", pkmuser);
        param.put("dealtype", PayDealTypeEnum.Chongzhi.getCode() + "");     //业务类型 1.充值 2立即买单 3优惠券  后续待定
        param.put("amount", payMoney);                                       //金额
        param.put("paytype", "" + payAwayView.getSelectPayCode().getCode() + "");
        param.put("paySubject", "平台充值");
        param.put("payBody", "充值");
        return param;
    }

    @Override
    public Map<String, String> createOrderParams() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        params.put("pkmuser", pkmuser);
        if (showType == -1) {
            params.put("waitMoney", AES.encrypt(et_top_up_amount.getText().toString(), AES.key));
        } else {
            params.put("waitMoney", AES.encrypt(mRecharge_amount + "", AES.key));
        }
        params.put("payEntrance", "1");
        if (cardnum != null) {
            params.put("cardnum", cardnum);
        }
        params.put("pkpropre", "");
        return params;
    }

    @Override
    protected void onResume() {
        super.onResume();
        payAwayView.onResume();
    }

    private void payFinish() {
        if (FLAG == 3) {
            MyDialogUtil.setDialog(this, "充值成功", "恭喜您已充值成功!", "", "返回明细", "返回话费充值", new MyDialog.ClickListenerInterface() {
                @Override
                public void doButtomOne() {
                    //跳转到资金明细页面
                    Intent intent = new Intent(NewTopupWayActivity.this, BillDetailsActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void doButtomTwo() {
                    //跳转到首页
                    finish();
                }
            });
        } else {
//            finish();
            payAwayView.goSuccessPage(getFromSharePreference(Config.userConfig.pkregister));
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
                                    .installUPPayPlugin(NewTopupWayActivity.this);
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
                payFinish();
            } else if (str.equalsIgnoreCase("cancel")) {
                /***********取消支付************/
                payAwayView.chargeUpdateCancle();
            }
        } else {
            UN_FLAG = 1;
        }
    }

    /**
     * 请求加载选择金额的接口
     */
    private void requestSelectMoney() {
        Map<String, String> params = new HashMap<>();
        params.put("pkmuser", pkmuser);
        Wethod.httpPost(NewTopupWayActivity.this, REQUEST_SELECT_MONEY, Config.web.recharge_select_money, params, this);
    }

    //充值、提现需调用的接口获取上下限
    private void requestLimit() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "recharge");
        Wethod.httpPost(NewTopupWayActivity.this, LIMIT, Config.web.Limit, params, this);
    }
}
