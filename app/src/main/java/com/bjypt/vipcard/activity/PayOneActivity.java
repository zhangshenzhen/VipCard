package com.bjypt.vipcard.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.RespBase;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.AddOrderBean;
import com.bjypt.vipcard.model.PayOneBean;
import com.bjypt.vipcard.model.PayOneDetailsBean;
import com.bjypt.vipcard.model.ZbarBalanceBean;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.MD5;
import com.bjypt.vipcard.utils.MyDialogUtil;
import com.bjypt.vipcard.view.MyDialog;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付
 */
public class PayOneActivity extends BaseActivity implements VolleyCallBack {
    private ListView listView;
    private View view_one, view_two;
    private LinearLayout layout_back;//支付界面返回按钮
    private LinearLayout layout_at_once_pay;//立即支付
    private LinearLayout layout_youhui, mRedPackLinear, mPointLinear;//优惠按钮
    private LinearLayout layout_pay_zaixian;//在线支付
    private RelativeLayout layout_pay_merchant;//商家余额支付
    private RelativeLayout layout_pay_pingtai;//平台余额支付
    private ImageView iv_pay_zaixian;//在线支付
    private ImageView iv_pay_yue;//商家余额支付
    private ImageView iv_pay_pingtai;//平台余额支付
    private String pkpropre;
    private Boolean PSD_TYPE = false;

    private String amount;
    private TextView merchant_balance, pintai_balance;

    private TextView tv_price_sum;//小计总额
    private TextView tv_red_xianzhi;//红包 限制每单最多使用1元
    private TextView tv_red_price;//红包金额
    private TextView tv_jifen;//现有的积分
    private TextView tv_jifen_buzou;//提示 积分不足
    private TextView tv_zongjia;//总价
    private TextView tv_order_price;//订单已付金额
    private TextView tv_merchant_name;//商家名称
    private TextView tv_youhui_price;//已优惠金额
    private List<PayOneBean> list = null;
    private PayOneInfoAdapter adapter = null;
    private int Tag = 0;
    private String orderId;//商品流水号  例如:20160414120040
    private String primaryk;//商品订单主键  例如:fb3d0e64263745a3a0d699ca5999bdb3
    private String pkmuser;//商家主键
    private String preorderId;//订单主键
    private String pkpayid;
    private int PAY_STYLE = 1;//判断其支付方式
    private String createtime;//订单创建时间
    private String flag;
    private TextView change_dingjin;
    private AddOrderBean addOrderBean;
    private String queryPkid;
    private LinearLayout mAllPayStyle;

    private PayOneDetailsBean payOneDetailsBean;
    public static int FLAG_SKIP_PAY = 1;
    private PsdDialog psdDialog, psdDialog1;
    private LinearLayout mDIngjin, mHuiyuanbi;
    private TextView mHuiyuanbiNum;
    private TextView tv_youhui_price_huiyuanbi;
    private TextView pay_online_tv, pay_merchant_tv, pay_platform_tv;

    private int ONLINE_PAY = 0;
    private int MERCHANT_PAY = 1;
    private int PLATFORM_PAY = 2;

    private String pksystem;//系统主键
    private ZbarBalanceBean zbarBalanceBean;//平台余额和商家余额信息

    private String key = "";
    private EditText et;
//    private LoadingFragDialog mFragDialog;

    private Handler balanceHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    merchant_balance.setText("余额" + zbarBalanceBean.getResultData().getBalance_mer() + "元");
                    pintai_balance.setText("余额" + zbarBalanceBean.getResultData().getBalance_sys() + "元");
                    break;
            }
        }
    };


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 0:
                    //在线支付
                    tv_zongjia.setText(payOneDetailsBean.getResultData().getWaitMoney() + "元");
                    tv_youhui_price_huiyuanbi.setText("已优惠" + payOneDetailsBean.getResultData().getSaveMoney() + "元");

                    break;
                case 1:
                    //商家余额支付
                    tv_zongjia.setText(payOneDetailsBean.getResultData().getWaitMoney_merchant() + "元");
                    tv_youhui_price_huiyuanbi.setText("已优惠" + payOneDetailsBean.getResultData().getSaveMoney_merchant() + "元");
                    break;
                case 2:
                    //平台余额支付
                    tv_zongjia.setText(payOneDetailsBean.getResultData().getWaitMoney_platform() + "元");
                    tv_youhui_price_huiyuanbi.setText("已优惠" + payOneDetailsBean.getResultData().getSaveMoney_platform() + "元");
                    break;
            }
        }
    };


    @Override
    public void setContentLayout() {
        Log.v("TAG", "setContentLayout");
        setContentView(R.layout.activity_pay_one);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        primaryk = intent.getStringExtra("primaryk");
        Log.e("yytt", "payOne before primaryk:" + primaryk);
        pkmuser = intent.getStringExtra("pkmuser");
        createtime = intent.getStringExtra("createtime");
        preorderId = intent.getStringExtra("preorderId");
        flag = intent.getStringExtra("FLAG");//   FLAG == Y的情况表示支付定金， FLAG ==N的情况表示支付尾款，当支付尾款的时候需要调用一个创建支付订单的接口

        String scanResult = intent.getStringExtra("scanResult");//二维码扫描的数据


//         1) pkregister 用户Id
//         2) pkpayid 支付主键
//         3) payEntrance 支付入口 【1 充值支付 2 预约定金支付 3 消费支付】

        pkpayid = primaryk;
    }


    @Override
    public void initView() {

//        mFragDialog = new LoadingFragDialog(this, R.anim.loadingpage, R.style.MyDialogStyle);
        FLAG_SKIP_PAY = 1;
        Map<String, String> payInfoParams = new HashMap<>();
        payInfoParams.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        payInfoParams.put("pkpayid", preorderId);


        if (flag.equals("Y")) {
            payInfoParams.put("payEntrance", 2 + "");
        } else {
            payInfoParams.put("payEntrance", 3 + "");
        }

        Wethod.httpPost(PayOneActivity.this,2, Config.web.pay_info_, payInfoParams, this);
//        mFragDialog.showDialog();


        view_one = View.inflate(this, R.layout.layout_pay_one, null);
        view_two = View.inflate(this, R.layout.layout_pay_two, null);
        listView = (ListView) findViewById(R.id.lv_pay);
        listView.addHeaderView(view_one);
        listView.addFooterView(view_two);

        tv_price_sum = (TextView) findViewById(R.id.tv_price_sum);
        iv_pay_zaixian = (ImageView) findViewById(R.id.iv_pay_zaixian);
        iv_pay_zaixian.setImageResource(R.mipmap.selected_true);
        iv_pay_yue = (ImageView) findViewById(R.id.iv_pay_yue);
        iv_pay_pingtai = (ImageView) findViewById(R.id.iv_pay_pingtai);


        tv_red_xianzhi = (TextView) findViewById(R.id.tv_red_xianzhi);
        tv_red_price = (TextView) findViewById(R.id.tv_red_price);
        tv_jifen = (TextView) findViewById(R.id.tv_jifen);
        tv_jifen_buzou = (TextView) findViewById(R.id.tv_jifen_buzou);
        tv_zongjia = (TextView) findViewById(R.id.tv_zongjia);
        tv_order_price = (TextView) findViewById(R.id.tv_order_price);
        tv_merchant_name = (TextView) findViewById(R.id.tv_merchant_name);
        tv_youhui_price = (TextView) findViewById(R.id.tv_youhui_price);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        layout_youhui = (LinearLayout) findViewById(R.id.layout_youhui);//优惠
        mRedPackLinear = (LinearLayout) findViewById(R.id.red_pack_linear);//红包
        mPointLinear = (LinearLayout) findViewById(R.id.point_linear);//积分
        layout_pay_zaixian = (LinearLayout) findViewById(R.id.layout_pay_zaixian);
        layout_at_once_pay = (LinearLayout) findViewById(R.id.layout_at_once_pay);
        layout_pay_merchant = (RelativeLayout) findViewById(R.id.layout_pay_merchant);
        layout_pay_pingtai = (RelativeLayout) findViewById(R.id.layout_pay_pingtai);

        merchant_balance = (TextView) findViewById(R.id.merchant_balance);//商家余额
        pintai_balance = (TextView) findViewById(R.id.pintai_balance);//平台余额
        change_dingjin = (TextView) findViewById(R.id.change_dingjin);

        mAllPayStyle = (LinearLayout) findViewById(R.id.all_pay_style);

        mDIngjin = (LinearLayout) findViewById(R.id.dingjin_linear);
        mHuiyuanbi = (LinearLayout) findViewById(R.id.huiyuanbi_linear);

        mHuiyuanbiNum = (TextView) findViewById(R.id.tv_order_huiyuanbi);
        tv_youhui_price_huiyuanbi = (TextView) findViewById(R.id.tv_youhui_price_huiyuanbi);

        pay_online_tv = (TextView) findViewById(R.id.pay_online_tv);//在线支付所需要支付的金额
        pay_merchant_tv = (TextView) findViewById(R.id.pay_merchant_tv);//商家余额支付所需要支付的金额
        pay_platform_tv = (TextView) findViewById(R.id.pay_platform_tv);//平台余额支付所需要支付的金额

        if (flag.equals("Y")) {
            mDIngjin.setVisibility(View.VISIBLE);
            mHuiyuanbi.setVisibility(View.GONE);
        } else {
            mDIngjin.setVisibility(View.GONE);
            mHuiyuanbi.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void afterInitView() {

    }


    @Override
    public void bindListener() {
        layout_back.setOnClickListener(this);
        layout_youhui.setOnClickListener(this);
        layout_at_once_pay.setOnClickListener(this);
        layout_pay_zaixian.setOnClickListener(this);
        layout_pay_merchant.setOnClickListener(this);
        layout_pay_pingtai.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);

        Map<String, String> sysParams = new HashMap<>();
        sysParams.put("pkmuser", pkmuser);
        sysParams.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(PayOneActivity.this,987, Config.web.get_system_merchant_balance, sysParams, this);
//        mFragDialog.showDialog();

        if (FLAG_SKIP_PAY == 2) {
            finish();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            /***
             * 后退键
             */
            case R.id.layout_back:
                finish();
                break;
            /***
             * 优惠
             */
            case R.id.layout_youhui:

                break;
            //在线支付 默认
            case R.id.layout_pay_zaixian:
                PAY_STYLE = 1;
                iv_pay_zaixian.setImageResource(R.mipmap.selected_true);
                iv_pay_yue.setImageResource(R.mipmap.selected_false);
                iv_pay_pingtai.setImageResource(R.mipmap.selected_false);
                mHandler.sendEmptyMessage(0);

                break;
            /***
             * 商家余额支付
             */
            case R.id.layout_pay_merchant:
                PAY_STYLE = 2;
                iv_pay_yue.setImageResource(R.mipmap.selected_true);
                iv_pay_pingtai.setImageResource(R.mipmap.selected_false);
                iv_pay_zaixian.setImageResource(R.mipmap.selected_false);

                mHandler.sendEmptyMessage(1);

                break;
            /***
             * 平台余额支付
             */
            case R.id.layout_pay_pingtai:
                PAY_STYLE = 3;
                iv_pay_yue.setImageResource(R.mipmap.selected_false);
                iv_pay_pingtai.setImageResource(R.mipmap.selected_true);
                iv_pay_zaixian.setImageResource(R.mipmap.selected_false);
                mHandler.sendEmptyMessage(2);
                break;

            /***
             * 立即支付
             */
            case R.id.layout_at_once_pay:

                if (PAY_STYLE == 1) {
                    /*******跳转到下一个在线支付页面********/
                    Intent payIntent = new Intent(this, PayOnLineActivity.class);
                    payIntent.putExtra("orderId", orderId);
                    payIntent.putExtra("primaryk", primaryk);
                    Log.e("yytt", "payOne primaryk:" + pkpayid);
                    payIntent.putExtra("pkmuser", pkmuser);
                    payIntent.putExtra("preorderId", preorderId);
                    payIntent.putExtra("muname", payOneDetailsBean.getResultData().getMuname());
                    payIntent.putExtra("flag", flag);
                    payIntent.putExtra("amount", amount);
                    payIntent.putExtra("virtualMoney", payOneDetailsBean.getResultData().getVirtualMoney());
                    if (flag.equals("Y")) {
                        payIntent.putExtra("earnest", payOneDetailsBean.getResultData().getEarnestMoney());
                    } else {
                        payIntent.putExtra("redPacket", payOneDetailsBean.getResultData().getRedPacket());
                        payIntent.putExtra("pkWeal", payOneDetailsBean.getResultData().getPkWeal());
                        payIntent.putExtra("couponPay", payOneDetailsBean.getResultData().getCouponPay());
                        payIntent.putExtra("point", payOneDetailsBean.getResultData().getPoint());
                        payIntent.putExtra("pointMoney", payOneDetailsBean.getResultData().getPointMoney());

                        payIntent.putExtra("earnest", payOneDetailsBean.getResultData().getWaitMoney());
                    }
                    payIntent.putExtra("createtime", createtime);
                    Log.e("woyaokk", "createtime:" + createtime);
                    startActivity(payIntent);

                } else if (PAY_STYLE == 2 || PAY_STYLE == 3) {
                    /***********商家余额、平台余额支付***************/
                    if (!flag.equals("Y")) {
                        if (getFromSharePreference(Config.userConfig.paypassword).equals("") || getFromSharePreference(Config.userConfig.paypassword) == null) {
                            //未设置支付密码
                            Toast.makeText(this, "请设置支付密码", Toast.LENGTH_LONG).show();
                            PSD_TYPE = false;
                            Intent mLogPsdIntent = new Intent(this, ChangePasswordActivity.class);
                            mLogPsdIntent.putExtra("psdType", PSD_TYPE);
                            startActivityForResult(mLogPsdIntent, 0);
                        } else {
                            if (PAY_STYLE == 2) {
                                if (Double.parseDouble(zbarBalanceBean.getResultData().getBalance_mer()) > Double.parseDouble(payOneDetailsBean.getResultData().getWaitMoney_merchant())) {
                                    psdDialog = new PsdDialog(this, "金额", payOneDetailsBean.getResultData().getMuname(), payOneDetailsBean.getResultData().getWaitMoney_merchant());
                                    psdDialog.show();
                                } else {
                                    //弹出选择框，去充值还是取消商家余额支付
                                    new AlertDialog.Builder(this).setTitle("充值确认").setMessage("是否去充值？")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startRechage(1, pkmuser);
                                                }
                                            })
                                            .setNegativeButton("取消", null)
                                            .show();
                                }

                            } else if (PAY_STYLE == 3) {
                                if (Double.parseDouble(zbarBalanceBean.getResultData().getBalance_sys()) > Double.parseDouble(payOneDetailsBean.getResultData().getWaitMoney_platform())) {
                                    psdDialog = new PsdDialog(this, "金额", payOneDetailsBean.getResultData().getMuname(), payOneDetailsBean.getResultData().getWaitMoney_platform());
                                    psdDialog.show();
                                } else {
                                    //弹出选择框，去充值还是取消平台余额支付
                                    new AlertDialog.Builder(this).setTitle("充值确认").setMessage("是否去充值？")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startRechage(2, pksystem);
                                                }
                                            })
                                            .setNegativeButton("取消", null)
                                            .show();

                                }

                            }

                        }


                    } else {
                        if (getFromSharePreference(Config.userConfig.paypassword).equals("") || getFromSharePreference(Config.userConfig.paypassword) == null) {
                            //未设置支付密码
                            Toast.makeText(this, "请设置支付密码", Toast.LENGTH_LONG).show();
                            PSD_TYPE = false;
                            Intent mLogPsdIntent = new Intent(this, ChangePasswordActivity.class);
                            mLogPsdIntent.putExtra("psdType", PSD_TYPE);
                            startActivityForResult(mLogPsdIntent, 0);
                        } else {

                            if (PAY_STYLE == 2) {
                                //商家余额支付定金
                                if (Double.parseDouble(zbarBalanceBean.getResultData().getBalance_mer()) > Double.parseDouble(payOneDetailsBean.getResultData().getEarnestMoney())) {
                                    psdDialog1 = new PsdDialog(this, "定金", payOneDetailsBean.getResultData().getMuname(), payOneDetailsBean.getResultData().getEarnestMoney());
                                    psdDialog1.show();
                                } else {
                                    //弹出选择框，去充值还是取消商家余额支付
                                    new AlertDialog.Builder(this).setTitle("充值确认").setMessage("是否去充值？")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startRechage(1, pkmuser);
                                                }
                                            })
                                            .setNegativeButton("取消", null)
                                            .show();
                                }
                            } else if (PAY_STYLE == 3) {
                                //平台余额支付定金
                                if (Double.parseDouble(zbarBalanceBean.getResultData().getBalance_sys()) > Double.parseDouble(payOneDetailsBean.getResultData().getEarnestMoney())) {
                                    psdDialog1 = new PsdDialog(this, "定金", payOneDetailsBean.getResultData().getMuname(), payOneDetailsBean.getResultData().getEarnestMoney());
                                    psdDialog1.show();
                                } else {
                                    //弹出选择框，去充值还是取消平台余额支付
                                    new AlertDialog.Builder(this).setTitle("充值确认").setMessage("是否去充值？")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startRechage(2, pksystem);
                                                }
                                            })
                                            .setNegativeButton("取消", null)
                                            .show();
                                }

                            }

                        }
                    }
                } else if (PAY_STYLE == 4) {
                    psdDialog = new PsdDialog(this, "金额", payOneDetailsBean.getResultData().getMuname(), payOneDetailsBean.getResultData().getWaitMoney());
                    psdDialog.show();
                }

                break;

        }
    }

    /**
     * @param pkregister   用户Id
     * @param phoneNo      用户手机号
     * @param pkmuser      商家Id
     * @param pkpayid      充值或者预约或者消费主键
     * @param amountStatus 定金支付状态【0：未完成   1：已完成    2：充值异   常     3：充值失败 4：充值取消】(当用余额支付时，直接传入 1 )
     * @param payResult    支付结果
     * @param payment      支付方式 【1 支付宝 2 财付通 3 百度钱包 4 网银 5余额支付 6 微信支付 7现金支付  8 其他支付 9 平台余额支付】
     * @param amount       支付金额
     * @param payEntrance  支付入口 【1 充值支付 2 预约定金支付 3 消费支付】
     *                     14) redPacket 红包支付
     *                     15) pkWeal 优惠券主键
     *                     17) couponPay 优惠券金额
     *                     18) point 积分
     *                     19) pointMoney 积分金额
     */
    public void payMoneyByBalance(String virtualMoney, String pkregister, String phoneNo, String pkmuser, String pkpayid,
                                  String amountStatus, String payResult, String payment, String amount, String payEntrance, String waitMoney, String redPacket, String pkWeal, String couponPay, String point, String pointMoney, String exchange) {
        Map<String, String> params = new HashMap<>();
        if (virtualMoney != null && !virtualMoney.equals("")) {
            params.put("virtualMoney", AES.encrypt(virtualMoney, AES.key));
        }

        params.put("pkregister", pkregister);
        params.put("phoneNo", phoneNo);
        params.put("pkmuser", pkmuser);
        params.put("pkpayid", pkpayid);
        params.put("amountStatus", amountStatus);
        params.put("payResult", payResult);
        params.put("payment", payment);
        params.put("amount", AES.encrypt(amount, AES.key));
        params.put("payEntrance", payEntrance);
        params.put("waitMoney", AES.encrypt(waitMoney, AES.key));
        params.put("redPacket", redPacket);
        params.put("pkWeal", pkWeal);
        params.put("couponPay", couponPay);
        params.put("point", point);
        params.put("pointMoney", pointMoney);
        if (payment.equals("5")) {
            params.put("favourable_merchant", AES.encrypt(exchange, AES.key));
        } else if (payment.equals("9")) {
            params.put("favourable_platform", AES.encrypt(exchange, AES.key));
        }
        Log.e("liyunte", "virtualMoney" + virtualMoney + "pkregister" + pkregister + "phoneNo" + phoneNo + "pkmuser" + pkmuser
                + "pkpayid" + pkpayid + "amountStatus" + amountStatus + "payResult" + payResult + "payment" + payment
                + "amount" + amount + "payEntrance" + payEntrance + "waitMoney" + waitMoney + "redPacket" + redPacket + "pkWeal" + pkWeal
                + "couponPay" + couponPay + "point" + point + "pointMoney" + pointMoney + "favourable_merchant" + exchange + "favourable_platform" + exchange);
        Wethod.httpPost(PayOneActivity.this,1, Config.web.update_payresult, params, this);
//        mFragDialog.showDialog();
    }

    @Override
    public void onSuccess(int reqcode, final Object result) {
        if (reqcode == 1) {
            MyDialogUtil.setDialog(this, "支付成功", "恭喜您已支付成功!请尽快使用此订单", "消费有更多优惠哦", "返回订单", "返回首页", new MyDialog.ClickListenerInterface() {
                @Override
                public void doButtomOne() {
                    if (flag.equals("Y")) {
                        Wethod.ToFailMsg(PayOneActivity.this, result);
                        Intent intent = new Intent(PayOneActivity.this, UnPayOrderActivity.class);
                        Toast.makeText(PayOneActivity.this, "支付成功", Toast.LENGTH_LONG).show();
                        intent.putExtra("falg", 1);
                        startActivity(intent);
                        PayOneActivity.FLAG_SKIP_PAY = 2;
                        finish();
                    } else {
                        Wethod.ToFailMsg(PayOneActivity.this, result);
//                        Intent intent = new Intent(PayOneActivity.this, UnPayOrderActivity.class);
//                        Toast.makeText(PayOneActivity.this, "支付成功", Toast.LENGTH_LONG).show();
//                        intent.putExtra("falg", 2);
//                        startActivity(intent);
                        PayOneActivity.FLAG_SKIP_PAY = 2;
                        finish();
                    }

                }

                @Override
                public void doButtomTwo() {
                    Toast.makeText(PayOneActivity.this, "返回首页", Toast.LENGTH_LONG).show();
                    PayOneActivity.FLAG_SKIP_PAY = 2;
                    finish();
                    startActivity(new Intent(PayOneActivity.this, MainActivity.class));
                }
            });


        } else if (reqcode == 2) {
            try {
                payOneDetailsBean = getConfiguration().readValue(result.toString(), PayOneDetailsBean.class);
                tv_merchant_name.setText(payOneDetailsBean.getResultData().getMuname());

                amount = payOneDetailsBean.getResultData().getAmount();

                pksystem = payOneDetailsBean.getResultData().getPksystem();


                adapter = new PayOneInfoAdapter(PayOneActivity.this, payOneDetailsBean.getResultData().getOrderPayDetailList());
                listView.setAdapter(adapter);
                if (flag.equals("Y")) {
                    //表示此时是支付定金。故不显示积分，红包，优惠卷

                    layout_youhui.setVisibility(View.GONE);
                    mRedPackLinear.setVisibility(View.GONE);
                    mPointLinear.setVisibility(View.GONE);
                    tv_price_sum.setText(payOneDetailsBean.getResultData().getAmount() + "元");
                    tv_order_price.setText(payOneDetailsBean.getResultData().getEarnestMoney() + "元");
                    tv_youhui_price.setText("到店后可省" + payOneDetailsBean.getResultData().getSaveMoney() + "元");
                    tv_zongjia.setText(payOneDetailsBean.getResultData().getWaitMoney() + "元");
                    merchant_balance.setText("余额" + payOneDetailsBean.getResultData().getMerBalance() + "元");
                    pintai_balance.setText("余额" + payOneDetailsBean.getResultData().getSysBalance() + "元");
                    change_dingjin.setText("定金:");

                } else {
                    //表示此时是扫码支付,或者我的订单进入的入口。故显示积分，红包，优惠卷
                    pay_platform_tv.setVisibility(View.VISIBLE);
                    pay_online_tv.setVisibility(View.VISIBLE);
                    pay_merchant_tv.setVisibility(View.VISIBLE);

                    pay_online_tv.setText("(需支付" + payOneDetailsBean.getResultData().getWaitMoney() + "元)");
                    pay_merchant_tv.setText("(需支付" + payOneDetailsBean.getResultData().getWaitMoney_merchant() + "元)");
                    pay_platform_tv.setText("(需支付" + payOneDetailsBean.getResultData().getWaitMoney_platform() + "元)");

                    layout_youhui.setVisibility(View.VISIBLE);
                    mRedPackLinear.setVisibility(View.VISIBLE);
                    mPointLinear.setVisibility(View.VISIBLE);
                    tv_red_price.setText(payOneDetailsBean.getResultData().getRedPacket() + "元");
                    change_dingjin.setText("订单已付:");
                    tv_price_sum.setText(payOneDetailsBean.getResultData().getAmount() + "元");
                    tv_order_price.setText(payOneDetailsBean.getResultData().getEarnestMoney() + "元");
                    tv_youhui_price_huiyuanbi.setText("已优惠" + payOneDetailsBean.getResultData().getSaveMoney() + "元");
                    tv_zongjia.setText(payOneDetailsBean.getResultData().getWaitMoney() + "元");
                    if (payOneDetailsBean.getResultData().getVirtualMoney() == null) {
                        mHuiyuanbiNum.setText(0 + "元");
                    } else {
                        mHuiyuanbiNum.setText(payOneDetailsBean.getResultData().getVirtualMoney() + "元");
                    }


                    //此时当WaitMoney的值为0时，则让用户直接支付，不需要选择支付方式。但与余额支付的逻辑不变，
                    // 只是更改新建尾款支付订单中payment字段的参数为8，并且pay接口的payment也为8
                    if (payOneDetailsBean.getResultData().getWaitMoney().equals("0.00")) {
                        PAY_STYLE = 4;
                        mAllPayStyle.setVisibility(View.GONE);
                    }
                    merchant_balance.setText("余额" + payOneDetailsBean.getResultData().getMerBalance() + "元");
                    pintai_balance.setText("余额" + payOneDetailsBean.getResultData().getSysBalance() + "元");
                    if (Integer.parseInt(payOneDetailsBean.getResultData().getPoint()) < 40) {
                        tv_jifen.setText("积分不足");
                    } else {
                        tv_jifen.setText("现有" + payOneDetailsBean.getResultData().getPoint() + "积分");
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == 1234) {
            try {
                addOrderBean = getConfiguration().readValue(result.toString(), AddOrderBean.class);
                pkpropre = addOrderBean.getResultData().getPkpropre();
                queryPkid = addOrderBean.getResultData().getPkpayid();


                if (PAY_STYLE == 3) {
                    payMoneyByBalance(payOneDetailsBean.getResultData().getVirtualMoney_platform(), getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, pkpropre, 1 + "", "success", 9 + "", payOneDetailsBean.getResultData().getAmount(), 3 + "", payOneDetailsBean.getResultData().getWaitMoney_platform(), AES.encrypt(payOneDetailsBean.getResultData().getRedPacket_platform(), AES.key), payOneDetailsBean.getResultData().getPkWeal_platform(), AES.encrypt(payOneDetailsBean.getResultData().getCouponPay_platform(), AES.key), payOneDetailsBean.getResultData().getPoint_platform(), AES.encrypt(payOneDetailsBean.getResultData().getPointMoney_platform(), AES.key), payOneDetailsBean.getResultData().getFavourable_platform());
                } else if (PAY_STYLE == 2) {
                    payMoneyByBalance(payOneDetailsBean.getResultData().getVirtualMoney_merchant(), getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, pkpropre, 1 + "", "success", 5 + "", payOneDetailsBean.getResultData().getAmount(), 3 + "", payOneDetailsBean.getResultData().getWaitMoney_merchant(), AES.encrypt(payOneDetailsBean.getResultData().getRedPacket_merchant(), AES.key), payOneDetailsBean.getResultData().getPkWeal_merchant(), AES.encrypt(payOneDetailsBean.getResultData().getCouponPay_merchant(), AES.key), payOneDetailsBean.getResultData().getPoint_merchant(), AES.encrypt(payOneDetailsBean.getResultData().getPointMoney_merchant(), AES.key), payOneDetailsBean.getResultData().getFavourable_merchant());
                } else if (PAY_STYLE == 4) {
                    payMoneyByBalance(payOneDetailsBean.getResultData().getVirtualMoney(), getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, pkpropre, 1 + "", "success", 8 + "", payOneDetailsBean.getResultData().getAmount(), 3 + "", payOneDetailsBean.getResultData().getWaitMoney(), AES.encrypt(payOneDetailsBean.getResultData().getRedPacket(), AES.key), payOneDetailsBean.getResultData().getPkWeal(), AES.encrypt(payOneDetailsBean.getResultData().getCouponPay(), AES.key), payOneDetailsBean.getResultData().getPoint(), AES.encrypt(payOneDetailsBean.getResultData().getPointMoney(), AES.key), "");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == 1314) {
            Toast.makeText(this, "密码校验通过", Toast.LENGTH_LONG).show();
            if (psdDialog != null) {
                psdDialog.dismiss();
            }
            if (psdDialog1 != null) {
                psdDialog1.dismiss();
            }

            if (PAY_STYLE == 2) {//商家余额支付
                if (flag.equals("Y")) {
                    payMoneyByBalance(payOneDetailsBean.getResultData().getVirtualMoney(), getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, preorderId, 1 + "", "success", 5 + "", payOneDetailsBean.getResultData().getEarnestMoney(), 2 + "", payOneDetailsBean.getResultData().getEarnestMoney(), "", "", "", "", "", "");
                } else {
                    Map<String, String> orderAddParams = new HashMap<>();
                    orderAddParams.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
                    orderAddParams.put("pkmuser", pkmuser);
                    Log.e("FFFF", "payOneDetailsBean.getResultData().getWaitMoney_merchant()" + payOneDetailsBean.getResultData().getWaitMoney_merchant());
                    orderAddParams.put("waitMoney", AES.encrypt(payOneDetailsBean.getResultData().getWaitMoney_merchant(), AES.key));
                    orderAddParams.put("payEntrance", "3");
                    orderAddParams.put("pkpropre", preorderId);
                    orderAddParams.put("payment", "5");
                    orderAddParams.put("redPacket", AES.encrypt(payOneDetailsBean.getResultData().getRedPacket_merchant(), AES.key));
                    orderAddParams.put("pkWeal", payOneDetailsBean.getResultData().getPkWeal_merchant());
                    orderAddParams.put("point", payOneDetailsBean.getResultData().getPoint_merchant());
                    orderAddParams.put("virtualMoney", AES.encrypt(payOneDetailsBean.getResultData().getVirtualMoney_merchant(), AES.key));

                    Wethod.httpPost(PayOneActivity.this,1234, Config.web.create_new_order, orderAddParams, this);
//                    mFragDialog.showDialog();
                }
            } else if (PAY_STYLE == 3) {//平台余额支付
                if (!flag.equals("Y")) {
                    Map<String, String> orderAddParams = new HashMap<>();
                    orderAddParams.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
                    orderAddParams.put("pkmuser", pkmuser);
                    orderAddParams.put("waitMoney", AES.encrypt(payOneDetailsBean.getResultData().getWaitMoney_platform(), AES.key));
                    orderAddParams.put("payEntrance", "3");
                    orderAddParams.put("pkpropre", preorderId);
                    orderAddParams.put("payment", "9");
                    orderAddParams.put("redPacket", AES.encrypt(payOneDetailsBean.getResultData().getRedPacket_platform(), AES.key));
                    orderAddParams.put("pkWeal", payOneDetailsBean.getResultData().getPkWeal_platform());
                    orderAddParams.put("point", payOneDetailsBean.getResultData().getPoint_platform());
                    orderAddParams.put("virtualMoney", AES.encrypt(payOneDetailsBean.getResultData().getVirtualMoney_platform(), AES.key));

                    Wethod.httpPost(PayOneActivity.this,1234, Config.web.create_new_order, orderAddParams, this);
//                    mFragDialog.showDialog();
                } else {
                    payMoneyByBalance(payOneDetailsBean.getResultData().getVirtualMoney(), getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, preorderId, 1 + "", "success", 9 + "", payOneDetailsBean.getResultData().getEarnestMoney(), 2 + "", payOneDetailsBean.getResultData().getEarnestMoney(), "", "", "", "", "", "");
                }
            } else if (PAY_STYLE == 4) {//支付金额为0的情况  payment为8
                Map<String, String> orderAddParams = new HashMap<>();
                orderAddParams.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
                orderAddParams.put("pkmuser", pkmuser);
                orderAddParams.put("waitMoney", AES.encrypt(payOneDetailsBean.getResultData().getWaitMoney(), AES.key));
                orderAddParams.put("payEntrance", "3");
                orderAddParams.put("pkpropre", preorderId);
                orderAddParams.put("payment", "8");
                orderAddParams.put("redPacket", AES.encrypt(payOneDetailsBean.getResultData().getRedPacket(), AES.key));
                orderAddParams.put("pkWeal", payOneDetailsBean.getResultData().getPkWeal());
                orderAddParams.put("point", payOneDetailsBean.getResultData().getPoint());
                orderAddParams.put("virtualMoney", AES.encrypt(payOneDetailsBean.getResultData().getVirtualMoney(), AES.key));
                Wethod.httpPost(PayOneActivity.this,1234, Config.web.create_new_order, orderAddParams, this);
//                mFragDialog.showDialog();
            }


        } else if (reqcode == 987) {
            try {
                zbarBalanceBean = getConfiguration().readValue(result.toString(), ZbarBalanceBean.class);
                balanceHandler.sendEmptyMessage(1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        mFragDialog.cancelDialog();
    }

    @Override
    public void onFailed(int reqcode, Object result) {
//        mFragDialog.cancelDialog();
        if (reqcode == 1) {
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == 2) {
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == 1234) {
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == 1314) {
            try {
                RespBase respBase = getConfiguration().readValue(result.toString(), RespBase.class);
                if (respBase.getResultData().toString().contains("未查询到用户支付密码信息")) {
                    psdDialog.dismiss();
                    Intent intent = new Intent(this, ChangePasswordActivity.class);
                    intent.putExtra("psdType", PSD_TYPE);
                    startActivity(intent);
                } else if (respBase.getResultData().toString().contains("支付密码输入有误")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("提示");
                    builder.setMessage("支付密码有误,请重新输入");

                    builder.setNegativeButton("重试",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    key = "";
                                    et.setText("");
                                    dialog.dismiss();
                                }
                            });

                    builder.setPositiveButton("修改支付密码",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    psdDialog.dismiss();
                                    dialog.dismiss();
                                    Intent intent = new Intent(PayOneActivity.this, ForgetPayPsdActivity.class);
                                    startActivity(intent);
                                }
                            });
                    builder.create().show();

                } else {
                    Toast.makeText(this, respBase.getResultData().toString(), Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            Wethod.ToFailMsg(this, result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }


    class PsdDialog extends Dialog {


        private Context context;
        private String messageOne, MessageTwo, title;
        private TextView mProductName;
        private TextView mProductMoney;
        private RelativeLayout mBack;
        private TextView mPsdTv;

        TextView t1, t2, t3, t4, t5, t6;


        public PsdDialog(Context context, String title, String messageOne, String MessageTwo) {
            super(context, R.style.MyDialogStyle);
            this.context = context;
            this.title = title;
            this.messageOne = messageOne;
            this.MessageTwo = MessageTwo;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            init();
        }

        public void init() {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.psd_input_dialog, null);
            setContentView(view);

            mProductName = (TextView) findViewById(R.id.product_name);
            mProductMoney = (TextView) findViewById(R.id.product_money);
            mPsdTv = (TextView) findViewById(R.id.psd_tv);

            mProductName.setText(messageOne);
            mProductMoney.setText(MessageTwo);
            mPsdTv.setText(title);


            mBack = (RelativeLayout) findViewById(R.id.iv_paysuccess_back);

            t1 = (TextView) findViewById(R.id.t1);
            t2 = (TextView) findViewById(R.id.t2);
            t3 = (TextView) findViewById(R.id.t3);
            t4 = (TextView) findViewById(R.id.t4);
            t5 = (TextView) findViewById(R.id.t5);
            t6 = (TextView) findViewById(R.id.t6);
            et = (EditText) findViewById(R.id.editHide);
            et.addTextChangedListener(tw);

            Window dialogWindow = getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
            lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
            dialogWindow.setAttributes(lp);


            //隐藏
            mBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PsdDialog.this.dismiss();
                }
            });
        }

        void setKey() {
            char[] arr = key.toCharArray();
            t1.setText("");
            t2.setText("");
            t3.setText("");
            t4.setText("");
            t5.setText("");
            t6.setText("");
            for (int i = 0; i < arr.length; i++) {
                if (i == 0) {
                    t1.setText("*");
                } else if (i == 1) {
                    t2.setText("*");
                } else if (i == 2) {
                    t3.setText("*");
                } else if (i == 3) {
                    t4.setText("*");
                } else if (i == 4) {
                    t5.setText("*");
                } else if (i == 5) {
                    t6.setText("*");
                }
            }
        }


        TextWatcher tw = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                key = s.toString();
                if (key.length() == 6) {
                    Map<String, String> payPsdParams = new HashMap<>();
                    payPsdParams.put("phoneno", getFromSharePreference(Config.userConfig.phoneno));
                    try {
                        payPsdParams.put("payPassword", MD5.getMd5(key, MD5.key));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    Wethod.httpPost(PayOneActivity.this,1314, Config.web.checkout_pay_psd, payPsdParams, PayOneActivity.this);
//                    mFragDialog.showDialog();
                }
                setKey();
            }
        };

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mFragDialog.cancelDialog();
    }

    /**
     * 跳转到余额不足充值页面
     */
    public void startRechage(int flag, String pk) {
        Intent intent = new Intent(this, NewTopupWayActivity.class);
        intent.putExtra("FLAG", flag);
        intent.putExtra("pkmuser", pk);
        startActivity(intent);
    }


}
