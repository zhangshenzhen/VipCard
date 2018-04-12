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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.SelectMoneyAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.PayDealTypeEnum;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.SelectMoneyBean;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.MyDialogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.MyDialog;
import com.bjypt.vipcard.view.PayAwayView;
import com.bjypt.vipcard.widget.MyGridView;
import com.bjypt.vipcard.widget.PetroleumView;
import com.bjypt.vipcard.widget.RechargeStateDialog;
import com.orhanobut.logger.Logger;
import com.sinia.orderlang.utils.StringUtil;
import com.unionpay.UPPayAssistEx;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/26.
 * 一键充值
 */

public class OneKeyTopupAmountActivity extends BaseActivity implements VolleyCallBack, PayAwayView.OnPayListener {

    private static final int REQUEST_SELECT_MONEY = 20171288;
    private String pkmuser;
    private String muname;
    private PayAwayView payAwayView;
    private EditText et_top_up_amount;
    private MyGridView gv_select_money;
    private Button btn_topay;
    private LinearLayout ll_et_show;
    private static final int request_pay_before = 2222;
    private String primaryk;
    private int FLAG;
    private String cardnum = "";
    private int UN_FLAG = 1;
    private TextView tv_renminbi;//人民币符号根据内容移动
    private ArrayList<SelectMoneyBean.ResultDataBean> moneyList = new ArrayList<>();
    private SelectMoneyAdapter mMoneyAdapter;
    private String mRecharge_amount;
    private int showType = -1; // 默认走输入金额逻辑
    private PetroleumView pv_poster;
    private String categoryId;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_one_key_topup_amount);
    }

    @Override
    public void beforeInitView() {
        pkmuser = getIntent().getStringExtra("pkmuser");
        cardnum = getIntent().getStringExtra("cardnum");
        muname = getIntent().getStringExtra("muname");
        categoryId = getIntent().getStringExtra("categoryid");
        FLAG = getIntent().getIntExtra("FLAG", 0);
    }

    @Override
    public void initView() {
        gv_select_money = (MyGridView) findViewById(R.id.gv_select_money);
        ll_et_show = (LinearLayout) findViewById(R.id.ll_et_show);
        ((TextView) findViewById(R.id.title)).setText("商家充值");
        payAwayView = (PayAwayView) findViewById(R.id.payAwayView);
        pv_poster = (PetroleumView) findViewById(R.id.pv_poster);
        payAwayView.setActivity(this);
        payAwayView.setOnPayListener(this);
        et_top_up_amount = (EditText) findViewById(R.id.et_top_up_amount);
        btn_topay = (Button) findViewById(R.id.btn_topay);
        tv_renminbi = (TextView) findViewById(R.id.tv_renminbi);
        ((TextView) findViewById(R.id.tv_merchant_name)).setText(muname);
        mMoneyAdapter = new SelectMoneyAdapter(this, moneyList);
        gv_select_money.setAdapter(mMoneyAdapter);
    }

    @Override
    public void afterInitView() {
        pv_poster.requestPetroleumEntrance(this, pkmuser, categoryId, cardnum);
        requestSelectMoney();
        payAwayView.sendHttpPayAways(null, pkmuser, getFromSharePreference(Config.userConfig.pkregister));
    }

    @Override
    public void bindListener() {
        findViewById(R.id.back).setOnClickListener(this);
        btn_topay.setOnClickListener(this);
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
                    btn_topay.setEnabled(true);
                    tv_renminbi.setVisibility(View.VISIBLE);
                    Paint paint = new Paint();
                    paint.setTextSize(DensityUtil.sp2px(OneKeyTopupAmountActivity.this, 16));
                    int off = DensityUtil.getTextWidth(paint, s.toString());
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) tv_renminbi.getLayoutParams();
                    params.rightMargin = DensityUtil.dip2px(OneKeyTopupAmountActivity.this, 15) + off;
                    tv_renminbi.setLayoutParams(params);
                } else {
                    btn_topay.setEnabled(false);
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
    protected void onResume() {
        super.onResume();
        payAwayView.onResume();
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_topay:
                Logger.i("确认充值");
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

    private void payFinish() {
        if (FLAG == 3) {

//            RechargeStateDialog rechargeStateDialog = new RechargeStateDialog(this)
//                    .setButtonText("充值成功", "确定", null)
//                    .setBtnClick(new RechargeStateDialog.ButtonClickListener() {
//                        @Override
//                        public void btn1Click() {
//
//                        }
//
//                        @Override
//                        public void btn2Click() {
//
//                        }
//                    });

            MyDialogUtil.setDialog(this, "充值成功", "恭喜您已充值成功!", "", "返回明细", "返回话费充值", new MyDialog.ClickListenerInterface() {
                @Override
                public void doButtomOne() {
                    //跳转到资金明细页面
                    Intent intent = new Intent(OneKeyTopupAmountActivity.this, BillDetailsActivity.class);
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
//            VipCenterAccountActivity.FLAG_MY_FRAGMENT = 2;
            payAwayView.goSuccessPage(getFromSharePreference(Config.userConfig.pkregister));


//            RightAwayActivity.CANCLE_ACTIVITY = 2;
//            finish();
        }
    }


    @Override
    public void onSuccess(int reqcode, Object result) {
        if (moneyList != null) {
            moneyList.clear();
        }
        switch (reqcode) {
            case REQUEST_SELECT_MONEY:
                gv_select_money.setVisibility(View.VISIBLE);
                ll_et_show.setVisibility(View.GONE);
                btn_topay.setEnabled(true);
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
                                    .installUPPayPlugin(OneKeyTopupAmountActivity.this);
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
        Map<String, String> param = new HashMap<>();
        param.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        param.put("orderid", outorderid);//订单主键
        param.put("pkmuser", pkmuser);
        param.put("dealtype", PayDealTypeEnum.Chongzhi.getCode() + "");//业务类型 1.充值 2立即买单 3优惠券  后续待定
        param.put("amount", payMoney);//金额
        param.put("paytype", "" + payAwayView.getSelectPayCode().getCode() + "");
        param.put("paySubject", muname + "充值");
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
        if (StringUtil.isNotEmpty(cardnum)) {
            params.put("cardnum", cardnum);
        }

        params.put("pkpropre", "");
        return params;
    }

    /**
     * 请求加载选择金额的接口
     */
    private void requestSelectMoney() {
        Map<String, String> params = new HashMap<>();
        params.put("pkmuser", pkmuser);
        if (StringUtil.isNotEmpty(cardnum)) {
            params.put("cardnum", cardnum);
        }
        Wethod.httpPost(OneKeyTopupAmountActivity.this, REQUEST_SELECT_MONEY, Config.web.recharge_select_money, params, this);
    }
}
