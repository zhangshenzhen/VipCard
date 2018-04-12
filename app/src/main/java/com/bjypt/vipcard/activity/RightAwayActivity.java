package com.bjypt.vipcard.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.base.RespBase;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.PayDealTypeEnum;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.RightAddOrderData;
import com.bjypt.vipcard.model.RightAwayBeforeData;
import com.bjypt.vipcard.model.RightAwayData;
import com.bjypt.vipcard.model.StringListResultBean;
import com.bjypt.vipcard.model.SystemInfomationBean;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.MD5;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.VirtualmoneySuccessHelper;
import com.bjypt.vipcard.view.AdTextViewMult;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.widget.PetroleumView;
import com.brioal.adtextviewlib.entity.AdEntity;
import com.sinia.orderlang.utils.StringUtil;

import net.frakbot.jumpingbeans.JumpingBeans;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by tuyouze on 2016/11/16 0016
 * 立即支付.
 */

public class RightAwayActivity extends BaseActivity implements VolleyCallBack<String> {

    private String pkmuser;//商家主键
    private String merchantName;//商家名称
    private TextView merchant_name;
    private String pkregister;//用户主键
    private EditText right_away_edit;
    private RelativeLayout youhuijuan_rela;
    private RelativeLayout hongbao_rela;
    private RelativeLayout zhifuzongjia_rela;
    private RelativeLayout huiyuanbi_rela;
    private RelativeLayout rl_dazhe_item;
    private RightAwayData rightAwayData;
    private TextView pintai_balance, merchant_balance;
    private TextView hongbao_money;
    private TextView zhifu_totalmoney;
    private TextView youhuijuan_decri;
    private TextView huiyuanbi_money;
    private TextView tv_renminbi;
    private TextView tv_jump;
    private TextView tv_renminbi2;
    private TextView total_youhui;
    private LinearLayout mBack;
    private LinearLayout ll_dongtai_youhui;
    private Boolean PSD_TYPE = false;
    private String key = "";
    private EditText et;
    private PsdDialog psdDialog;
    private RightAddOrderData addOrderBean;
    private String queryPkid;
    private String pksystem;
    private LinearLayout edit_two_show_linear;
    private RelativeLayout edit_two_show_rela;
    private EditText right_away_edit_no;
    private String jiyishuzhi = "";//去充值的时候记住当前的充值数
    public static int CANCLE_ACTIVITY = 1;
    private RightAwayBeforeData rightAwayBeforeData;

    private AdTextViewMult scroll_textview;

    private LinearLayout layout_pay_zaixian;//在线支付
    private LinearLayout layout_pay_card;//在线支付
    private RelativeLayout layout_pay_merchant;//商家余额支付
    private LinearLayout layout_pay_pingtai;//平台余额支付
    private ImageView iv_pay_zaixian;//在线支付
    private ImageView iv_pay_yue;//商家余额支付
    private ImageView iv_pay_pingtai;//平台余额支付
    private LinearLayout btn_at_once_pay;//立即支付
    private int PAY_STYLE = 3;//判断其支付方式
    private int check_pay_style = 3; //用户选中的支付方式
    public static String COUP_FLAG = "Y";
    private String coups = "";
    private TextView rightDiscount;
    private TextView youhuijuan_txt, card_balance;
    private String pk_merchant;
    private View v_line0;
    private View v_line1;
    private int FLAG = 1;
    private static final int request_pay_before = 2222;
    private int request_random_front = 1122; //每次请求随机一个整数
    private int request_discount_info = 123456;
    private TextView pay_online_discount_desc;
    private TextView pay_pingtai_discount_desc;
    private TextView pay_merchant_discount_desc;
    private TextView card_pay_discount_des;
    private String categoryId;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    String[] payStyles = new String[]{};
                    payStyles = rightAwayData.getResultData().getPaytype().split("\\|");
                    showPayStyle(payStyles.length, payStyles);
                    pintai_balance.setText("余额:" + rightAwayData.getResultData().getBalance_sys() + "元");        //平台余额
                    merchant_balance.setText("余额:" + rightAwayData.getResultData().getBalance_mer() + "元");      //商家余额
                    card_balance.setText("余额:" + rightAwayBeforeData.getResultData().getAreaCardBalance() + "元");      //卡余额
                    hongbao_money.setText(rightAwayData.getResultData().getRedPacket() + "元");
                    zhifu_totalmoney.setText(rightAwayData.getResultData().getWaitMoney());
                    // youhuijuan_decri.setText(rightAwayData.getResultData().getDesc_coupon());
                    huiyuanbi_money.setText(rightAwayData.getResultData().getVirtualMoney());
                    //设置折扣
                    if (StringUtil.isNotEmpty(rightAwayData.getResultData().getOnlineDiscount())) {
                        pay_online_discount_desc.setText(rightAwayData.getResultData().getOnlineDiscount());
                    } else {
                        pay_online_discount_desc.setText("");
                    }

                    //卡余额支付优惠
                    if (StringUtil.isNotEmpty(rightAwayData.getResultData().getCardDiscount())) {
                        card_pay_discount_des.setText(rightAwayData.getResultData().getCardDiscount());
                    } else {
                        card_pay_discount_des.setText("");
                    }

                    if (StringUtil.isNotEmpty(rightAwayData.getResultData().getBalanceDiscount())) {
                        pay_pingtai_discount_desc.setText(rightAwayData.getResultData().getBalanceDiscount());
                        pay_merchant_discount_desc.setText(rightAwayData.getResultData().getMerchantDiscount());
                    } else {
                        pay_pingtai_discount_desc.setText("");
                        pay_merchant_discount_desc.setText("");
                    }
//                    isEmptyMoney();
//                    new Handler(new Handler.Callback() {
//                        @Override
//                        public boolean handleMessage(Message message) {
//                            tv_jump.setVisibility(View.GONE);
//                            ll_dongtai_youhui.setVisibility(View.VISIBLE);
//                            total_youhui.setText(rightAwayData.getResultData().getSaveMoney());
//                            isEmptyMoney();
//                            return false;
//                        }
//                    }).sendEmptyMessageDelayed(0, 1000);
                    tv_jump.setVisibility(View.GONE);
                    ll_dongtai_youhui.setVisibility(View.VISIBLE);
                    total_youhui.setText(rightAwayData.getResultData().getSaveMoney());
                    isEmptyMoney();
                    pk_merchant = rightAwayData.getResultData().getPkmuser();
                    break;
                case 2:
                    if (rightAwayBeforeData.getResultData().getDiscountType().equals("0") && rightAwayBeforeData.getResultData().getCouponsType().equals("0")) {
                        //TODO 无优惠
                        youhuijuan_rela.setVisibility(View.GONE);
                        rl_dazhe_item.setVisibility(View.GONE);
                        v_line1.setVisibility(View.GONE);
                        v_line0.setVisibility(View.GONE);
                    } else if (rightAwayBeforeData.getResultData().getDiscountType().equals("0")) {
                        //如果没有折扣有优惠券
                        youhuijuan_rela.setVisibility(View.VISIBLE);
                        v_line0.setVisibility(View.VISIBLE);
                        rl_dazhe_item.setVisibility(View.GONE);
                        v_line1.setVisibility(View.GONE);
                        youhuijuan_txt.setText(rightAwayBeforeData.getResultData().getCouponsMsg());
                    } else if (rightAwayBeforeData.getResultData().getCouponsType().equals("0")) {
                        //如果有优惠券没有折扣
                        youhuijuan_rela.setVisibility(View.GONE);
                        v_line1.setVisibility(View.GONE);
                    }
                    if (rightAwayBeforeData.getResultData().getDiscountType().equals("1") || rightAwayBeforeData.getResultData().getCouponsType().equals("1")) {
                        //TODO 显示折扣
                        if (rightAwayBeforeData.getResultData().getDiscountType().equals("0")) {
                            rl_dazhe_item.setVisibility(View.GONE);
                            v_line1.setVisibility(View.GONE);
                        } else {
                            rl_dazhe_item.setVisibility(View.VISIBLE);
                            youhuijuan_rela.setVisibility(View.VISIBLE);
                            if (rightAwayBeforeData.getResultData().getCouponsType().equals("0")) {
                                youhuijuan_rela.setVisibility(View.GONE);
                                v_line1.setVisibility(View.GONE);
                            }
                            v_line0.setVisibility(View.VISIBLE);
                            v_line1.setVisibility(View.VISIBLE);
                            rightDiscount.setText(rightAwayBeforeData.getResultData().getDiscountMsg());
                            youhuijuan_txt.setText(rightAwayBeforeData.getResultData().getCouponsMsg());
                        }
                    } else if (rightAwayBeforeData.getResultData().getDiscountType().equals("2")) {
                        //TODO 显示优惠劵
                        youhuijuan_rela.setVisibility(View.VISIBLE);
                        v_line0.setVisibility(View.VISIBLE);
                        rl_dazhe_item.setVisibility(View.GONE);
                        v_line1.setVisibility(View.GONE);
                        youhuijuan_decri.setText(rightAwayBeforeData.getResultData().getDiscountMsg());
                    }
                    break;

            }
            super.handleMessage(msg);
        }
    };
    private String isSystemRecharge;

    /**
     * 为空的时候显示为0
     */
    private void isEmptyMoney() {
        if (StringUtil.isEmpty(right_away_edit.getText().toString())) {
            zhifu_totalmoney.setText("0.00");
            huiyuanbi_money.setText("0.00");
            total_youhui.setText("0.00");
        }
    }

    private ImageView no_youhui_iv;
    private ImageView iv_pay_card;
    private TextView close_tv;
    private TextView close_tv_2;
    private LinearLayout mOnlinePay;
    private RelativeLayout mMerchantPay;
    private LinearLayout mPintaiPay;
    private LinearLayout mAllPayStyle;
    private String balance_mer;  //商家余额
    private SystemInfomationBean systemInfomationBean;
    private String balance, cardNum;//平台可用余额
    private JumpingBeans jumpingBeans;
    private PetroleumView pv_poster;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_right_away_now);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryid");
        balance_mer = intent.getStringExtra("balance_mer");
        pkmuser = intent.getStringExtra("pkmuser");
        pkregister = getFromSharePreference(Config.userConfig.pkregister);
        merchantName = intent.getStringExtra("merchantName");
        Log.e("rightAway", "coups:" + coups);

        //请求支付方式并展示
        Map<String, String> params = new HashMap<>();
        params.put("pkmuser", pkmuser);
        params.put("pkregister", pkregister);
        params.put("versionCode", getVersion());
        Wethod.httpPost(RightAwayActivity.this, 1909, Config.web.get_right_away_before, params, RightAwayActivity.this);

        //获取平台余额数据并展示
        if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
            Map<String, String> params1 = new HashMap<>();
            params1.put("pkregister", pkregister);
            Wethod.httpPost(this, 1, Config.web.system_infomatiob, params1, this);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void initView() {
        merchant_name = (TextView) findViewById(R.id.merchant_name);
        card_balance = (TextView) findViewById(R.id.card_balance);
        pv_poster = (PetroleumView) findViewById(R.id.pv_poster);
        merchant_name.setText(merchantName);
        right_away_edit = (EditText) findViewById(R.id.right_away_edit);
        right_away_edit_no = (EditText) findViewById(R.id.right_away_edit_no);
        youhuijuan_rela = (RelativeLayout) findViewById(R.id.youhuijuan_rela);
        hongbao_rela = (RelativeLayout) findViewById(R.id.hongbao_rela);
        zhifuzongjia_rela = (RelativeLayout) findViewById(R.id.zhifuzongjia_rela);
        huiyuanbi_rela = (RelativeLayout) findViewById(R.id.huiyuanbi_rela);
        rl_dazhe_item = (RelativeLayout) findViewById(R.id.rl_dazhe_item);
        hongbao_money = (TextView) findViewById(R.id.hongbao_money);
        youhuijuan_txt = (TextView) findViewById(R.id.youhuijuan_txt);
        zhifu_totalmoney = (TextView) findViewById(R.id.zhifu_totalmoney);
        youhuijuan_decri = (TextView) findViewById(R.id.youhuijuan_decri);
        huiyuanbi_money = (TextView) findViewById(R.id.huiyuanbi_money);
        total_youhui = (TextView) findViewById(R.id.total_youhui);
        tv_jump = (TextView) findViewById(R.id.tv_jump);
        tv_renminbi = (TextView) findViewById(R.id.tv_renminbi);
        tv_renminbi2 = (TextView) findViewById(R.id.tv_renminbi2);
        close_tv = (TextView) findViewById(R.id.close_tv);
        close_tv_2 = (TextView) findViewById(R.id.close_tv_no);
        iv_pay_zaixian = (ImageView) findViewById(R.id.iv_pay_zaixian);
//        iv_pay_zaixian.setImageResource(R.mipmap.select_true);
        iv_pay_yue = (ImageView) findViewById(R.id.iv_pay_yue);
        iv_pay_card = (ImageView) findViewById(R.id.iv_pay_card);
        iv_pay_pingtai = (ImageView) findViewById(R.id.iv_pay_pingtai);
        iv_pay_pingtai.setImageResource(R.mipmap.select_true);
        layout_pay_zaixian = (LinearLayout) findViewById(R.id.layout_pay_zaixian);
        layout_pay_card = (LinearLayout) findViewById(R.id.layout_pay_card);
        btn_at_once_pay = (LinearLayout) findViewById(R.id.btn_at_once_pay);
        ll_dongtai_youhui = (LinearLayout) findViewById(R.id.ll_dongtai_youhui);
        layout_pay_merchant = (RelativeLayout) findViewById(R.id.layout_pay_merchant);
        layout_pay_pingtai = (LinearLayout) findViewById(R.id.layout_pay_pingtai);
        edit_two_show_rela = (RelativeLayout) findViewById(R.id.edit_two_show_rela);
        no_youhui_iv = (ImageView) findViewById(R.id.no_youhui_iv);
        mBack = (LinearLayout) findViewById(R.id.layout_back_right);
        pintai_balance = (TextView) findViewById(R.id.pintai_balance);
        merchant_balance = (TextView) findViewById(R.id.merchant_balance);
        rightDiscount = (TextView) findViewById(R.id.right_away_discount);
        edit_two_show_linear = (LinearLayout) findViewById(R.id.edit_two_show_linear);
        right_away_edit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        right_away_edit_no.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mOnlinePay = (LinearLayout) findViewById(R.id.layout_pay_zaixian);
        mMerchantPay = (RelativeLayout) findViewById(R.id.layout_pay_merchant);
        mPintaiPay = (LinearLayout) findViewById(R.id.layout_pay_pingtai);
        mAllPayStyle = (LinearLayout) findViewById(R.id.all_pay_style);
        scroll_textview = (AdTextViewMult) findViewById(R.id.scroll_textview);
        scroll_textview.setAnmiDuration(3000);
        scroll_textview.setFlipInterval(6000);
        v_line0 = findViewById(R.id.v_line0);
        v_line1 = findViewById(R.id.v_line1);
        jumpingBeans = JumpingBeans.with(tv_jump)
                .appendJumpingDots()
                .build();
        tv_jump.setVisibility(View.GONE);
        ll_dongtai_youhui.setVisibility(View.VISIBLE);
        total_youhui.setText("0.00");

        pay_online_discount_desc = (TextView) findViewById(R.id.pay_online_discount_desc);
        pay_merchant_discount_desc = (TextView) findViewById(R.id.pay_merchant_discount_desc);
        pay_pingtai_discount_desc = (TextView) findViewById(R.id.pay_pingtai_discount_desc);
        card_pay_discount_des = (TextView) findViewById(R.id.card_pay_discount_des);
        //// TODO: 2017/4/28
        isEmptyMoney();

        httpDiscountInfo();
    }

    @Override
    public void afterInitView() {
        pv_poster.requestPetroleumEntrance(this, pkmuser, categoryId, cardNum);
        merchant_balance.setText("余额：" + balance_mer + "元");

        right_away_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //获取焦点
                    close_tv.setVisibility(View.GONE);
                } else if (!hasFocus && right_away_edit.getText().toString().equals("")) {
                    //失去焦点
                    close_tv.setVisibility(View.VISIBLE);
                }
            }
        });

        right_away_edit_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //获取焦点
                    close_tv_2.setVisibility(View.GONE);
                } else if (!hasFocus && right_away_edit_no.getText().toString().equals("")) {
                    //失去焦点
                    close_tv_2.setVisibility(View.VISIBLE);
                }
            }
        });


        right_away_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        right_away_edit.setText(s);
                        right_away_edit.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    right_away_edit.setText(s);
                    right_away_edit.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        right_away_edit.setText(s.subSequence(0, 1));
                        right_away_edit.setSelection(1);
                        return;
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                /*String num = null;
                if(s.toString().contains(".")){

                }
                num = s.toString();*/
                if (s.length() == 2) {
                    if (!s.toString().contains(".") && s.toString().substring(0, 1).equals("0") && !(s.toString().substring(1, 2).equals("0"))) {
                        right_away_edit.setText(s.toString().substring(s.length() - 1) + ".00");
                        right_away_edit.setSelection((s.toString().substring(s.length() - 1) + ".00").length());
                    } else if (s.toString().substring(0, 1).equals("0") && s.toString().substring(1, 2).equals("0")) {
                        right_away_edit.setText(s.toString().substring(s.length() - 1) + ".00");
                        right_away_edit.setSelection((s.toString().substring(s.length() - 1) + ".00").length());
                    }
                }

                if (StringUtil.isNotEmpty(s.toString())) {
                    tv_renminbi.setVisibility(View.VISIBLE);

                    Paint paint = new Paint();
                    paint.setTextSize(DensityUtil.sp2px(RightAwayActivity.this, 15));
                    int off = DensityUtil.getTextWidth(paint, right_away_edit.getText().toString());
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv_renminbi.getLayoutParams();
                    params.rightMargin = DensityUtil.dip2px(RightAwayActivity.this, 20) + off;
                    tv_renminbi.setLayoutParams(params);

                    tv_jump.setVisibility(View.VISIBLE);
                    ll_dongtai_youhui.setVisibility(View.GONE);
                    btn_at_once_pay.setEnabled(true);
                } else {
                    tv_renminbi.setVisibility(View.GONE);
                    tv_jump.setVisibility(View.GONE);
                    ll_dongtai_youhui.setVisibility(View.VISIBLE);
                    total_youhui.setText("0.00");
                    zhifu_totalmoney.setText("0.00");
                    huiyuanbi_money.setText("0.00");
                    btn_at_once_pay.setEnabled(false);
                }

                if (s.toString().length() > 0 && Double.parseDouble(right_away_edit.getText().toString()) > 0) {
                    if (Double.parseDouble(s.toString()) > 99998 || right_away_edit.getText().toString().length() >= 8) {
                        Toast.makeText(RightAwayActivity.this, "您输入的金额超出范围", Toast.LENGTH_LONG).show();
                        btn_at_once_pay.setEnabled(false);
                    } else {
                        btn_at_once_pay.setEnabled(true);
                    }
                }

                if (right_away_edit.getText().toString().length() > 0) {
                    http_get_pay_front();
                }
            }
        });


        right_away_edit_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        right_away_edit_no.setText(s);
                        right_away_edit_no.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    right_away_edit_no.setText(s);
                    right_away_edit_no.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        right_away_edit_no.setText(s.subSequence(0, 1));
                        right_away_edit_no.setSelection(1);
                        return;
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 2) {
                    if (!s.toString().contains(".") && s.toString().substring(0, 1).equals("0") && !(s.toString().substring(1, 2).equals("0"))) {
                        right_away_edit_no.setText(s.toString().substring(s.length() - 1) + ".00");
                        right_away_edit_no.setSelection((s.toString().substring(s.length() - 1) + ".00").length());
                    } else if (s.toString().substring(0, 1).equals("0") && s.toString().substring(1, 2).equals("0")) {
                        right_away_edit_no.setText(s.toString().substring(s.length() - 1) + ".00");
                        right_away_edit_no.setSelection((s.toString().substring(s.length() - 1) + ".00").length());
                    }
                }

                if (StringUtil.isNotEmpty(s.toString())) {
                    tv_renminbi2.setVisibility(View.VISIBLE);
                    Paint paint = new Paint();
                    paint.setTextSize(DensityUtil.sp2px(RightAwayActivity.this, 15));
                    int off = DensityUtil.getTextWidth(paint, right_away_edit_no.getText().toString());
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv_renminbi2.getLayoutParams();
                    params.rightMargin = DensityUtil.dip2px(RightAwayActivity.this, 20) + off;
                    tv_renminbi2.setLayoutParams(params);
                    tv_jump.setVisibility(View.VISIBLE);
                    ll_dongtai_youhui.setVisibility(View.GONE);
                } else {
                    tv_renminbi2.setVisibility(View.GONE);
                    tv_jump.setVisibility(View.GONE);
                    ll_dongtai_youhui.setVisibility(View.VISIBLE);
                }

                if (right_away_edit.getText().toString().length() > 0) {
                    http_get_pay_front();
                }
            }
        });

    }

    private void http_get_pay_front() {
        String value = getEditTextFormat(right_away_edit);
        if (Double.parseDouble(value) > 0) {
            MyApplication.getHttpQueue().cancelAll(request_random_front);
            request_random_front = new Random().nextInt(100000) + 100000;
            Log.i("wanglei", "code:" + request_random_front + "");
            Map<String, String> params = new HashMap<>();
            params.put("pkregister", pkregister);
            params.put("pkmuser", pkmuser);
            params.put("coupons", coups);
            params.put("nonDiscountAmount", AES.encrypt(getEditTextFormat(right_away_edit_no), AES.key));
            params.put("amount", AES.encrypt(value, AES.key));
            params.put("payment", getPayMent() + "");
            Wethod.httpPost(RightAwayActivity.this, request_random_front, Config.web.get_pay_front, params, RightAwayActivity.this, View.GONE);
        }
    }

    private String getEditTextFormat(EditText edittext) {
        String value = edittext.getText().toString();
        if (StringUtil.isEmpty(value)) {
            return "0.0";
        } else {
            value = "0" + value;//防止 .0的情况出现
            if (value.endsWith(".")) {//如果最后一位是. 加0
                value = value + "0";
            }
            return value;
        }
    }

    @Override
    public void bindListener() {
        btn_at_once_pay.setOnClickListener(this);
        layout_pay_zaixian.setOnClickListener(this);
        layout_pay_merchant.setOnClickListener(this);
        layout_pay_pingtai.setOnClickListener(this);
        edit_two_show_linear.setOnClickListener(this);
        youhuijuan_rela.setOnClickListener(this);
        youhuijuan_decri.setOnClickListener(this);
        mBack.setOnClickListener(this);
        layout_pay_card.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (data == null) {

                } else {
                    if (RESULT_OK == resultCode) {
                        coups = data.getStringExtra("coupons");
                        if (StringUtil.isEmpty(coups)) {
                            rl_dazhe_item.setVisibility(View.VISIBLE);
                        } else {
                            rl_dazhe_item.setVisibility(View.GONE);
                            v_line1.setVisibility(View.GONE);
                        }
                        if (StringUtil.isEmpty(right_away_edit.toString())) {
                            return;
                        }
                        http_get_pay_front();
                    }
                }
                break;
            case 1:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.youhuijuan_decri:
                String price1 = "";
                if (right_away_edit.getText().toString() != null && !right_away_edit.getText().toString().equals("")) {
                    price1 = right_away_edit.getText().toString();
                } else {
                    price1 = "0";
                }
                Intent couIntent1 = new Intent(this, CouponActivity.class);
                couIntent1.putExtra("isSelect", "Y");
                couIntent1.putExtra("flag", "0");
                couIntent1.putExtra("pkmuser", pkmuser);
                couIntent1.putExtra("amount", price1);
                startActivityForResult(couIntent1, 0);
                break;
            case R.id.youhuijuan_rela:
                String price = "";
                if (right_away_edit.getText().toString() != null && !right_away_edit.getText().toString().equals("")) {
                    price = right_away_edit.getText().toString();
                } else {
                    price = "0";
                }
                Intent couIntent = new Intent(this, CouponActivity.class);
                couIntent.putExtra("isSelect", "Y");
                couIntent.putExtra("flag", "0");
                couIntent.putExtra("pkmuser", pkmuser);
                couIntent.putExtra("coupons", coups);
                couIntent.putExtra("amount", price);
                startActivityForResult(couIntent, 0);
                break;
            case R.id.edit_two_show_linear:
                //TODO 显示不可优惠金额输入框
                if (FLAG == 1) {
                    //展开时
                    no_youhui_iv.setImageResource(R.mipmap.right_away_open1);
                    edit_two_show_rela.setVisibility(View.VISIBLE);
                    FLAG = 2;
                } else {
                    //收起时
                    no_youhui_iv.setImageResource(R.mipmap.right_away_close1);
                    edit_two_show_rela.setVisibility(View.GONE);
                    FLAG = 1;
                    if (!right_away_edit_no.getText().toString().equals("")) {
                        right_away_edit_no.setText("");

                        if (StringUtil.isNotEmpty(right_away_edit.getText().toString())) {
                            http_get_pay_front();
                        }
                    }
                }
                break;
            //在线支付
            case R.id.layout_pay_zaixian:
                PAY_STYLE = 1;
                check_pay_style = 1;
                iv_pay_zaixian.setImageResource(R.mipmap.select_true);
                iv_pay_yue.setImageResource(R.mipmap.select_false);
                iv_pay_pingtai.setImageResource(R.mipmap.select_false);
                iv_pay_card.setImageResource(R.mipmap.select_false);
                http_get_pay_front();
                break;
            /***
             * 商家余额支付
             */
            case R.id.layout_pay_merchant:
                PAY_STYLE = 2;
                check_pay_style = 2;
                iv_pay_yue.setImageResource(R.mipmap.select_true);
                iv_pay_pingtai.setImageResource(R.mipmap.select_false);
                iv_pay_zaixian.setImageResource(R.mipmap.select_false);
                iv_pay_card.setImageResource(R.mipmap.select_false);
                coups = "";
                http_get_pay_front();
                break;
            /***
             * 平台余额支付
             */
            case R.id.layout_pay_pingtai:
                PAY_STYLE = 3;
                check_pay_style = 3;
                iv_pay_yue.setImageResource(R.mipmap.select_false);
                iv_pay_pingtai.setImageResource(R.mipmap.select_true);
                iv_pay_zaixian.setImageResource(R.mipmap.select_false);
                iv_pay_card.setImageResource(R.mipmap.select_false);
                http_get_pay_front();
                break;
            case R.id.layout_pay_card:
                PAY_STYLE = 44;
                check_pay_style = 5;
                iv_pay_yue.setImageResource(R.mipmap.select_false);
                iv_pay_pingtai.setImageResource(R.mipmap.select_false);
                iv_pay_zaixian.setImageResource(R.mipmap.select_false);
                iv_pay_card.setImageResource(R.mipmap.select_true);
                http_get_pay_front();
                break;
            case R.id.layout_back_right:
                if (psdDialog != null) {
                    psdDialog.dismiss();
                }
                finish();
                break;
            case R.id.btn_at_once_pay:
                //立即支付 todo
                if (rightAwayData != null) {
                    if (right_away_edit.getText().toString() == null || right_away_edit.getText().toString().equals("")) {
                        Toast.makeText(this, "请输入支付金额", Toast.LENGTH_LONG).show();
                    } else if (Double.parseDouble(right_away_edit.getText().toString()) <= 0) {
                        Toast.makeText(this, "请输入正确的支付金额", Toast.LENGTH_LONG).show();
                    } else if (Double.parseDouble(rightAwayData.getResultData().getWaitMoney()) == 0.00) {
                        PAY_STYLE = 11;
                        if (getFromSharePreference(Config.userConfig.paypassword).equals("") || getFromSharePreference(Config.userConfig.paypassword) == null) {
                            //未设置支付密码
                            Toast.makeText(this, "请设置支付密码", Toast.LENGTH_LONG).show();
                            PSD_TYPE = false;
                            Intent mLogPsdIntent = new Intent(this, ChangePasswordActivity.class);
                            mLogPsdIntent.putExtra("psdType", PSD_TYPE);
                            startActivityForResult(mLogPsdIntent, 0);
                        } else {
                            if (Double.parseDouble(rightAwayData.getResultData().getBalance_sys()) >= Double.parseDouble(rightAwayData.getResultData().getWaitMoney())) {
                                psdDialog = new PsdDialog(this, "金额", merchantName, rightAwayData.getResultData().getWaitMoney());
                                psdDialog.show();
                            } else {
                                //弹出选择框，去充值还是取消平台余额支付
                                new AlertDialog.Builder(this).setTitle("充值确认").setMessage("是否去充值？")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                //跳转到余额不足充值页面
                                                startRechage(2, pksystem);
                                            }
                                        })
                                        .setNegativeButton("取消", null)
                                        .show();
                            }
                        }
                    } else {
                        if (PAY_STYLE == 1) {
                            //TODO 跳转到在线支付页面
                            Intent intent = new Intent(this, RightAwayOnLineActivity.class);
                            intent.putExtra("mname", merchantName);//商家名称
                            intent.putExtra("waitMoney", rightAwayData.getResultData().getWaitMoney());//支付金额
                            intent.putExtra("pkmuser", pkmuser);//商家主键
                            intent.putExtra("pk_merchant", pk_merchant); //商家主键或者是连锁店主键 用于调用topay接口使用
                            intent.putExtra("redPacket", rightAwayData.getResultData().getRedPacket());//红包
                            intent.putExtra("pkWeal", coups);
                            intent.putExtra("virtualMoney", rightAwayData.getResultData().getVirtualMoney());
                            intent.putExtra("amount", rightAwayData.getResultData().getAmount());
                            intent.putExtra("non_discount_amount_aes", right_away_edit_no.getText().toString());
                            startActivity(intent);
                        } else if (PAY_STYLE == 2 || PAY_STYLE == 3) {
                            if (getFromSharePreference(Config.userConfig.paypassword).equals("") || getFromSharePreference(Config.userConfig.paypassword) == null) {
                                //未设置支付密码
                                Toast.makeText(this, "请设置支付密码", Toast.LENGTH_LONG).show();
                                PSD_TYPE = false;
                                Intent mLogPsdIntent = new Intent(this, ChangePasswordActivity.class);
                                mLogPsdIntent.putExtra("psdType", PSD_TYPE);
                                startActivityForResult(mLogPsdIntent, 0);
                            } else {
                                if (PAY_STYLE == 2) {
                                    if (Double.parseDouble(rightAwayData.getResultData().getBalance_mer()) >= Double.parseDouble(rightAwayData.getResultData().getWaitMoney())) {
                                        psdDialog = new PsdDialog(this, "金额", merchantName, rightAwayData.getResultData().getWaitMoney());
                                        psdDialog.show();
                                    } else {
                                        //弹出选择框，去充值还是取消商家余额支付
                                        new AlertDialog.Builder(this).setTitle("充值确认").setMessage("是否去充值？")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        startRechage(1, pkmuser);
                                                        jiyishuzhi = rightAwayData.getResultData().getAmount();
                                                    }
                                                })
                                                .setNegativeButton("取消", null)
                                                .show();
                                    }
                                } else if (PAY_STYLE == 3) {
                                    if (Double.parseDouble(rightAwayData.getResultData().getBalance_sys()) >= Double.parseDouble(rightAwayData.getResultData().getWaitMoney())) {
                                        psdDialog = new PsdDialog(this, "金额", merchantName, rightAwayData.getResultData().getWaitMoney());
                                        psdDialog.show();
                                    } else {

                                        //isSystemRecharge   = 0: 无系统充值送， 弹对话框有去加款   = 1:  有系统充值送 弹toast提示平台余额不足
                                        if ("1".equals(isSystemRecharge)) {
                                            ToastUtil.showToast(RightAwayActivity.this, "平台余额不足，可选择其他方式支付");
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
                        } else if (PAY_STYLE == 44) {
                            if (Double.parseDouble(rightAwayBeforeData.getResultData().getAreaCardBalance()) >= Double.parseDouble(rightAwayData.getResultData().getWaitMoney())) {
                                psdDialog = new PsdDialog(this, "金额", merchantName, rightAwayData.getResultData().getWaitMoney());
                                psdDialog.show();
                            } else {
                                ToastUtil.showToast(this, "您的卡余额不足,请充值后再消费");
                            }
                        } else if (PAY_STYLE == 4) {
                            psdDialog = new PsdDialog(this, "金额", merchantName, rightAwayData.getResultData().getWaitMoney());
                            psdDialog.show();
                        }
                    }
                }
                break;
        }
    }

    private int getPayMent() {
        if (PAY_STYLE == 1) {
            return 1;
        } else if (PAY_STYLE == 3) {
            return 9;
        } else if (PAY_STYLE == 2) {
            return 5;
        } else if (PAY_STYLE == 44) {
            return 44;
        }
        return 1;
    }

    private void showPayStyle(int count, String[] style) {
        for (int i = 0; i < count; i++) {
            if (style[i].equals("1")) {
                //支付宝显示
                mOnlinePay.setVisibility(View.VISIBLE);
            } else if (style[i].equals("4")) {
                //网银支付显示
                mOnlinePay.setVisibility(View.VISIBLE);
            } else if (style[i].equals("5")) {
                //商家余额支付
                mMerchantPay.setVisibility(View.VISIBLE);
            } else if (style[i].equals("6")) {
                //微信支付显示
                mOnlinePay.setVisibility(View.VISIBLE);
            } else if (style[i].equals("9")) {
                //平台余额支付显示
                mPintaiPay.setVisibility(View.VISIBLE);
            } else if (style[i].equals("44")) {
                //卡余额支付
                layout_pay_card.setVisibility(View.VISIBLE);
            } else if (style[i].equals("11")) {
                //隐藏所有支付方式
                mAllPayStyle.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == 1) {
            Log.e("ddddddddd", result.toString());
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                systemInfomationBean = objectMapper.readValue(result.toString(), SystemInfomationBean.class);
                pintai_balance.setText("余额：" + systemInfomationBean.getResultData().getBalance() + "元");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (reqcode == 1909) {
            try {
                rightAwayBeforeData = getConfiguration().readValue(result.toString(), RightAwayBeforeData.class);
                mHandler.sendEmptyMessage(2);
                String[] payStyle = new String[]{};
                payStyle = rightAwayBeforeData.getResultData().getPaytype().split("\\|");
                showPayStyle(payStyle.length, payStyle);
                card_balance.setText("余额:" + rightAwayBeforeData.getResultData().getAreaCardBalance() + "元");      //卡余额
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (reqcode == request_random_front) {
            try {
                rightAwayData = getConfiguration().readValue(result.toString(), RightAwayData.class);
                pksystem = rightAwayData.getResultData().getPksystem();
                cardNum = rightAwayData.getResultData().getCardnum();
                isSystemRecharge = rightAwayData.getResultData().getIsSystemRecharge();
                if (rightAwayData.getResultData().getWaitMoney().equals("0")) {
                    PAY_STYLE = 4;
                } else {
                    if (check_pay_style == -1) {
                        PAY_STYLE = 3;
                    }
                }
                jiyishuzhi = "";
                mHandler.sendEmptyMessage(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == 1314) {
            Toast.makeText(this, "密码校验通过", Toast.LENGTH_LONG).show();
            if (psdDialog != null) {
                psdDialog.dismiss();
            }
            if (PAY_STYLE == 2) {//商家余额支付
                Map<String, String> orderAddParams = new HashMap<>();
                orderAddParams.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
                orderAddParams.put("pkmuser", pkmuser);
                orderAddParams.put("waitMoney", AES.encrypt(rightAwayData.getResultData().getWaitMoney(), AES.key));
                orderAddParams.put("payEntrance", "5");
                orderAddParams.put("payment", "5");
                // orderAddParams.put("redPacket", AES.encrypt(rightAwayData.getResultData().getRedPacket(), AES.key));
                orderAddParams.put("pkWeal", AES.encrypt(coups, AES.key));
                orderAddParams.put("virtualMoney", AES.encrypt(rightAwayData.getResultData().getVirtualMoney(), AES.key));
                orderAddParams.put("amount", AES.encrypt(rightAwayData.getResultData().getAmount(), AES.key));
                orderAddParams.put("non_discount_amount_aes", AES.encrypt(right_away_edit_no.getText().toString(), AES.key));
                Wethod.httpPost(RightAwayActivity.this, 1234, Config.web.create_new_order, orderAddParams, this);
                //  mFragDialog.showDialog();

            } else if (PAY_STYLE == 3) {//平台余额支付
                Map<String, String> orderAddParams = new HashMap<>();
                orderAddParams.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
                orderAddParams.put("pkmuser", pkmuser);
                orderAddParams.put("waitMoney", AES.encrypt(rightAwayData.getResultData().getWaitMoney(), AES.key));
                orderAddParams.put("payEntrance", "5");
                orderAddParams.put("payment", "9");
                //orderAddParams.put("redPacket", AES.encrypt(rightAwayData.getResultData().getRedPacket(), AES.key));
                orderAddParams.put("pkWeal", AES.encrypt(coups, AES.key));
                orderAddParams.put("virtualMoney", AES.encrypt(rightAwayData.getResultData().getVirtualMoney(), AES.key));
                orderAddParams.put("amount", AES.encrypt(rightAwayData.getResultData().getAmount(), AES.key));
                orderAddParams.put("non_discount_amount_aes", AES.encrypt(right_away_edit_no.getText().toString(), AES.key));
                Wethod.httpPost(RightAwayActivity.this, 1234, Config.web.create_new_order, orderAddParams, this);
                // mFragDialog.showDialog();

            } else if (PAY_STYLE == 4) {//支付金额为0的情况  payment为8
                Map<String, String> orderAddParams = new HashMap<>();
                orderAddParams.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
                orderAddParams.put("pkmuser", pkmuser);
                orderAddParams.put("waitMoney", AES.encrypt(rightAwayData.getResultData().getWaitMoney(), AES.key));
                orderAddParams.put("payEntrance", "3");
                orderAddParams.put("payment", "8");
                // orderAddParams.put("redPacket", AES.encrypt(rightAwayData.getResultData().getRedPacket(), AES.key));
                orderAddParams.put("pkWeal", AES.encrypt(coups, AES.key));
                orderAddParams.put("virtualMoney", AES.encrypt(rightAwayData.getResultData().getVirtualMoney(), AES.key));
                orderAddParams.put("amount", AES.encrypt(rightAwayData.getResultData().getAmount(), AES.key));
                orderAddParams.put("non_discount_amount_aes", AES.encrypt(right_away_edit_no.getText().toString(), AES.key));

                Wethod.httpPost(RightAwayActivity.this, 1234, Config.web.create_new_order, orderAddParams, this);
            } else if (PAY_STYLE == 11) {
                Map<String, String> orderAddParams = new HashMap<>();
                orderAddParams.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
                orderAddParams.put("pkmuser", pkmuser);
                orderAddParams.put("waitMoney", AES.encrypt(rightAwayData.getResultData().getWaitMoney(), AES.key));
                orderAddParams.put("payEntrance", "5");
                orderAddParams.put("payment", "11");
                // orderAddParams.put("redPacket", AES.encrypt(rightAwayData.getResultData().getRedPacket(), AES.key));
                orderAddParams.put("pkWeal", AES.encrypt(coups, AES.key));
                orderAddParams.put("virtualMoney", AES.encrypt(rightAwayData.getResultData().getVirtualMoney(), AES.key));
                orderAddParams.put("amount", AES.encrypt(rightAwayData.getResultData().getAmount(), AES.key));
                orderAddParams.put("non_discount_amount_aes", AES.encrypt(right_away_edit_no.getText().toString(), AES.key));
                Wethod.httpPost(RightAwayActivity.this, 1234, Config.web.create_new_order, orderAddParams, this);
            } else if (PAY_STYLE == 44) {
                Map<String, String> orderAddParams = new HashMap<>();
                orderAddParams.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
                orderAddParams.put("pkmuser", pkmuser);
                orderAddParams.put("cardnum", cardNum);
                orderAddParams.put("waitMoney", AES.encrypt(rightAwayData.getResultData().getWaitMoney(), AES.key));
                orderAddParams.put("payEntrance", "7");
                orderAddParams.put("payment", "44");
                // orderAddParams.put("redPacket", AES.encrypt(rightAwayData.getResultData().getRedPacket(), AES.key));
                orderAddParams.put("pkWeal", AES.encrypt(coups, AES.key));
                orderAddParams.put("virtualMoney", AES.encrypt(rightAwayData.getResultData().getVirtualMoney(), AES.key));
                orderAddParams.put("amount", AES.encrypt(rightAwayData.getResultData().getAmount(), AES.key));
                orderAddParams.put("non_discount_amount_aes", AES.encrypt(right_away_edit_no.getText().toString(), AES.key));
                Wethod.httpPost(RightAwayActivity.this, 1234, Config.web.create_new_order, orderAddParams, this);
            }
        } else if (reqcode == 1234) {
            try {
                addOrderBean = getConfiguration().readValue(result.toString(), RightAddOrderData.class);
                //pkpropre = addOrderBean.getResultData().getPkpropre();
                queryPkid = addOrderBean.getResultData().getPkpayid();

                if (PAY_STYLE == 3) {//平台余额
                    toPay(addOrderBean.getResultData().getOutorderid(), addOrderBean.getResultData().getWaitMoney(), 9);
                    // updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, queryPkid, "success", 9, addOrderBean.getResultData().getAmount(), 3, addOrderBean.getResultData().getWaitMoney(), "2");
//                    payFinish();
                } else if (PAY_STYLE == 2) {//商家余额
                    toPay(addOrderBean.getResultData().getOutorderid(), addOrderBean.getResultData().getWaitMoney(), 5);
                    // updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, queryPkid, "success", 5, addOrderBean.getResultData().getAmount(), 3, addOrderBean.getResultData().getWaitMoney(), "2");
//                    payFinish();
                } else if (PAY_STYLE == 4) {
                    toPay(addOrderBean.getResultData().getOutorderid(), addOrderBean.getResultData().getWaitMoney(), 8);
                    //                    updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, queryPkid, "success", 8, addOrderBean.getResultData().getAmount(), 3, addOrderBean.getResultData().getWaitMoney(), "2");
                } else if (PAY_STYLE == 11) {
                    toPay(addOrderBean.getResultData().getOutorderid(), addOrderBean.getResultData().getWaitMoney(), 11);
                    //updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, queryPkid, "success", 11, addOrderBean.getResultData().getAmount(), 3, addOrderBean.getResultData().getWaitMoney(), "2");
                } else if (PAY_STYLE == 44) {
                    toPay(addOrderBean.getResultData().getOutorderid(), addOrderBean.getResultData().getWaitMoney(), 44);
                    //updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, queryPkid, "success", 11, addOrderBean.getResultData().getAmount(), 3, addOrderBean.getResultData().getWaitMoney(), "2");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == 666) {
            startActivity(new Intent(this, BillDetailsActivity.class));
            finish();
//            payFinish();
        } else if (reqcode == request_pay_before) {
            //topay支付成功后的处理逻辑
            payFinish();
        } else if (reqcode == request_discount_info) {
            try {
                StringListResultBean stringListResultBean = getConfiguration().readValue(result.toString(), StringListResultBean.class);
                if (stringListResultBean.getResultData() != null && stringListResultBean.getResultData().size() > 0) {
                    List<AdEntity> gongGao = new ArrayList<AdEntity>();
                    for (int i = 0; i < stringListResultBean.getResultData().size(); i++) {
                        gongGao.add(new AdEntity(stringListResultBean.getResultData().get(i), "", ""));
                    }
                    if (gongGao.size() > 0) {
                        scroll_textview.setTexts(gongGao, 1);
                        if (gongGao.size() == 1) {
                            scroll_textview.stopFilpping();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void payFinish() {
//        startActivity(new Intent(this, BillDetailsActivity.class));
//        Intent intent = new Intent(this, SuccessActivity.class);
//        intent.putExtra("sourcepk", queryPkid);
//        intent.putExtra("pkregister", getFromSharePreference(Config.userConfig.pkregister));
//        startActivity(intent);
        VirtualmoneySuccessHelper virtualmoneySuccessHelper = new VirtualmoneySuccessHelper(this, "支付成功");
        virtualmoneySuccessHelper.checkVirtualmoney(getFromSharePreference(Config.userConfig.pkregister), queryPkid);
        RightAwayActivity.CANCLE_ACTIVITY = 2;
//        finish();
    }


    private void httpDiscountInfo() {
        Map<String, String> orderAddParams = new HashMap<>();
        orderAddParams.put("pkmuser", pkmuser);
        Wethod.httpPost(RightAwayActivity.this, request_discount_info, Config.web.discountInfo, orderAddParams, this);
    }


    @Override
    public void onFailed(int reqcode, String result) {
        if (reqcode == request_random_front) {
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
                                    Intent intent = new Intent(RightAwayActivity.this, ForgetPayPsdActivity.class);
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

            // 当不可优惠金额大于总金额时由网络返回的数据。并且用土司弹出
            Wethod.ToFailMsg(this, result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    /**
     * @param outorderid
     * @param amount
     * @param payment
     */
    private void toPay(String outorderid, String amount, int payment) {
        Map<String, String> param = new HashMap<>();
        param.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        param.put("pkmuser", pk_merchant);
        param.put("orderid", outorderid);//订单主键
        param.put("dealtype", PayDealTypeEnum.Lijimaidan.getCode() + "");//业务类型 1.充值 2立即买单 3优惠券  后续待定
        param.put("amount", amount);//金额
        param.put("paytype", "" + payment);
        param.put("paySubject", "在" + merchantName + "消费");
        param.put("payBody", "消费");
        Wethod.httpPost(this, request_pay_before, Config.web.pay_new_before, param, this);
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
                    RightAwayActivity.PsdDialog.this.dismiss();
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
                    Wethod.httpPost(RightAwayActivity.this, 1314, Config.web.checkout_pay_psd, payPsdParams, RightAwayActivity.this);
                    //                    mFragDialog.showDialog();
                }
                setKey();
            }
        };

    }

    /**
     * 跳转到余额不足充值页面
     */
    public void startRechage(int flag, String pk) {
        Intent intent = null;
        if (flag == 1) {
            intent = new Intent(this, OneKeyTopupAmountActivity.class);
        } else {
            intent = new Intent(this, NewTopupWayActivity.class);
        }
        new Intent(this, NewTopupWayActivity.class);
        intent.putExtra("FLAG", flag);
        intent.putExtra("pkmuser", pk);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CANCLE_ACTIVITY == 2) {
            finish();
            CANCLE_ACTIVITY = 1;
        }
        //        if (jiyishuzhi.equals("") || jiyishuzhi == null) {
        //
        //        } else {
        //            MyApplication.getHttpQueue().cancelAll(1122);
        //            Map<String, String> params = new HashMap<>();
        //            params.put("pkregister", pkregister);
        //            params.put("pkmuser", pkmuser);
        //            params.put("amount", AES.encrypt(jiyishuzhi, AES.key));
        //            params.put("coupons", "");
        //            params.put("nonDiscountAmount", AES.encrypt("", AES.key));
        //            params.put("payment", getPayMent() + "");
        //            Wethod.httpPost(RightAwayActivity.this, 1122, Config.web.get_pay_front, params, RightAwayActivity.this);
        //        }
        http_get_pay_front();

    }

    public String getVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        jumpingBeans.stopJumping();
    }
}
