package com.bjypt.vipcard.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.HalfbalanceBean;
import com.bjypt.vipcard.model.ObjectModelBean;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.CheckPwdDialog;
import com.bjypt.vipcard.view.ToastUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 五折卡充值界面
 */

public class RightAwayNewActivity extends BaseActivity implements VolleyCallBack<String> {
    private static final int CONSUME = 7758521;
    public static final String TAG = "RightAwayNewActivity";
    ObjectModelBean<HalfbalanceBean> halfbalanceBean; //余额

    // LinearLayout
    private LinearLayout edit_two_show_linear; // 输入不参与优惠金额
    private LinearLayout layout_back_right;    // 返回键
    private Button layout_at_once_pay;   // 立即支付
    // RelativeLayout
    private RelativeLayout edit_two_show_rela; // 隐藏的相对布局
    // EditText
    private EditText right_away_edit;          // 请和服务员确定金额
    private EditText right_away_edit_no;       // 请和服务员确定金额
    // ImageView
    private ImageView no_youhui_iv;            // 输入不参与优惠金额 +
    // TextView
    private TextView merchant_name;            // 商家名字
    private TextView close_tv;                 // 关闭的字
    private TextView close_tv_no;              // 关闭的字
    private TextView tv_youhui;                // 优惠多少
    private TextView tv_xuzhi;                 // 总额多少
    private TextView tv_yue;                 // 五折卡余额

    private int FLAG = 1;
    private String pkmuser;
    private String pkregister;
    private String muname;
    private static final int request_code_balance = 4;
    private boolean isHalfOff = true;
    private String halfbalance;
    private boolean isRightNew;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_new_right_away_now);
        getHttpHalfBalance();
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        pkmuser = intent.getStringExtra("pkmuser");
        muname = intent.getStringExtra("muname");
        isRightNew = intent.getBooleanExtra("isRightNew", false);
        pkregister = getFromSharePreference(Config.userConfig.pkregister);
        if (isRightNew) {
            getHttpHalfBalance();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHttpHalfBalance();
    }

    @Override
    public void initView() {
        edit_two_show_linear = (LinearLayout) findViewById(R.id.edit_two_show_linear);
        layout_back_right = (LinearLayout) findViewById(R.id.layout_back_right);
        layout_at_once_pay = (Button) findViewById(R.id.layout_at_once_pay);

        edit_two_show_rela = (RelativeLayout) findViewById(R.id.edit_two_show_rela);

        right_away_edit = (EditText) findViewById(R.id.right_away_edit);
        right_away_edit_no = (EditText) findViewById(R.id.right_away_edit_no);

        no_youhui_iv = (ImageView) findViewById(R.id.no_youhui_iv);

        merchant_name = (TextView) findViewById(R.id.merchant_name);
        // 改变支付商家的名称
        merchant_name.setText(muname);

        close_tv = (TextView) findViewById(R.id.close_tv);
        close_tv_no = (TextView) findViewById(R.id.close_tv_no);
        tv_youhui = (TextView) findViewById(R.id.tv_youhui);
        tv_xuzhi = (TextView) findViewById(R.id.tv_xuzhi);
        tv_yue = (TextView) findViewById(R.id.tv_yue);

        right_away_edit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        right_away_edit_no.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

    }


    @Override
    public void afterInitView() {
        // 实例化监听对象
        //        textChange = new TextChange();
        right_away_edit.addTextChangedListener(new TextChange(right_away_edit));
        right_away_edit_no.addTextChangedListener(new TextChange(right_away_edit_no));
        right_away_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //获取焦点
                    close_tv.setVisibility(View.GONE);
                } else if (!hasFocus && right_away_edit.getText().toString().equals("")) {
                    close_tv.setVisibility(View.VISIBLE);
                }
            }
        });
        right_away_edit_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //获取焦点
                    close_tv_no.setVisibility(View.GONE);
                } else if (!hasFocus && right_away_edit_no.getText().toString().equals("")) {
                    //失去焦点
                    right_away_edit_no.setText("");
                    close_tv_no.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void bindListener() {
        edit_two_show_linear.setOnClickListener(this);
        layout_back_right.setOnClickListener(this);
        layout_at_once_pay.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            // 输入不参与优惠金额的展开与关闭
            case R.id.edit_two_show_linear:
                if (FLAG == 1) {
                    //展开时
                    no_youhui_iv.setImageResource(R.mipmap.right_away_open1);
                    edit_two_show_rela.setVisibility(View.VISIBLE);
                    right_away_edit_no.setText("");
                    right_away_edit_no.setEnabled(true);
                    FLAG = 2;
                } else {
                    //收起时
                    no_youhui_iv.setImageResource(R.mipmap.right_away_close1);
                    edit_two_show_rela.setVisibility(View.GONE);
                    if (!right_away_edit_no.getText().toString().equals("")) {
                        right_away_edit_no.setText("");
                        right_away_edit_no.setEnabled(false);
                    }
                    FLAG = 1;
                }
                break;
            // 顶部返回键
            case R.id.layout_back_right:
                finish();
                break;
            // 确认支付
            case R.id.layout_at_once_pay:
                String xuzhi = tv_xuzhi.getText().toString();
                String yue = tv_yue.getText().toString();
                if (Double.parseDouble(xuzhi) > Double.parseDouble(yue)) {
                    //弹出选择框，去充值还是取消平台余额支付
                    new AlertDialog.Builder(this).setTitle("五折卡余额不足").setMessage("是否去充值？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(RightAwayNewActivity.this, RightAwayOnLineNewActivity.class);
                                    intent.putExtra("isHalfOff", isHalfOff);
                                    intent.putExtra("platbalance", halfbalanceBean.getResultData().getPlatbalance());
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                } else {
                    if (!tv_xuzhi.getText().toString().equals("0.00")) {
                        layout_at_once_pay.setClickable(true);
                        CheckPwdDialog psdDialog = new CheckPwdDialog(this, "金额", "五折店支付", "¥" + tv_xuzhi.getText(), new CheckPwdDialog.CheckPayPasswordListener() {
                            @Override
                            public void onPass() {
                                getHttpCreateOrder();
                            }
                        });
                        psdDialog.show();
                    } else {
                        ToastUtil.showToast(this, "请输入支付金额");
                        layout_at_once_pay.setClickable(false);
                    }
                }
                break;
        }
    }

    /**
     * 获取五折店余额
     */
    private void getHttpHalfBalance() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(this, request_code_balance, Config.web.fine_discount_get_balance, params, this);
    }

    private void getHttpCreateOrder() {
        String total_amount = right_away_edit.getText().toString();
        String nonDiscount_amount = right_away_edit_no.getText().toString();
        // AES加密后的消费总金额
        String total_amount_aes = AES.encrypt(total_amount, AES.key);
        // AES加密后的不参与优惠金额
        String nonDiscount_amount_aes = AES.encrypt(nonDiscount_amount, AES.key);
        Map<String, String> params = new HashMap<>();
        params.put("pkmuser", pkmuser);
        params.put("pkregister", pkregister);
        params.put("total_amount_aes", total_amount_aes);
        params.put("nonDiscount_amount_aes", nonDiscount_amount_aes);
        Wethod.httpPost(this, CONSUME, Config.web.fine_discount_post_consume, params, this);
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        switch (reqcode) {
            case CONSUME:
                ToastUtil.showToast(this, "支付成功");
                finish();
                Intent h51 = new Intent(RightAwayNewActivity.this, LifeServireH5Activity.class);
                h51.putExtra("serviceName", "使用记录");
                h51.putExtra("life_url", Config.web.fine_discount_pay_history + "?pkregister=");
                h51.putExtra("isLogin", "Y");
                h51.putExtra("isallurl", "Y");
                startActivity(h51);
                break;
            case request_code_balance:
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                try {
                    halfbalanceBean = objectMapper.readValue(result.toString(), new TypeReference<ObjectModelBean<HalfbalanceBean>>() {
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                halfbalance = halfbalanceBean.getResultData().getHalfbalance();
                tv_yue.setText(halfbalance);
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {
        switch (reqcode) {
            case CONSUME:
//                ToastUtil.showToast(this, "五折账户余额不足，请充值");
                break;
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    class TextChange implements TextWatcher {

        private EditText editText;

        public TextChange(EditText et) {
            this.editText = et;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().contains(".")) {
                if (charSequence.length() - 1 - charSequence.toString().indexOf(".") > 2) {
                    charSequence = charSequence.toString().subSequence(0, charSequence.toString().indexOf(".") + 3);
                    if (editText.getId() == R.id.right_away_edit) {
                        right_away_edit.setText(charSequence);
                        right_away_edit.setSelection(charSequence.length());
                    } else {
                        right_away_edit_no.setText(charSequence);
                        right_away_edit_no.setSelection(charSequence.length());
                    }
                }
            }
            if (charSequence.toString().trim().substring(0).equals(".")) {
                charSequence = "0" + charSequence;
                if (editText.getId() == R.id.right_away_edit) {
                    right_away_edit.setText(charSequence);
                    right_away_edit.setSelection(2);
                } else {
                    right_away_edit_no.setText(charSequence);
                    right_away_edit_no.setSelection(2);
                }
            }

            if (charSequence.toString().startsWith("0")
                    && charSequence.toString().trim().length() > 1) {
                if (!charSequence.toString().substring(1, 2).equals(".")) {
                    if (editText.getId() == R.id.right_away_edit) {
                        right_away_edit.setText(charSequence.subSequence(0, 1));
                        right_away_edit.setSelection(1);
                    } else {
                        right_away_edit_no.setText(charSequence.subSequence(0, 1));
                        right_away_edit_no.setSelection(1);
                    }
                    return;
                }
            }

            // 五折店支付算法
            double zong = 0;
            double buke = 0;
            if ((right_away_edit.getText().toString()) != null && (right_away_edit.getText().toString()).length() > 0) {
                zong = Double.parseDouble((right_away_edit.getText().toString()));
            }
            if (right_away_edit.getText().equals("") && (right_away_edit.getText().toString()).length() <= 0) {
                tv_xuzhi.setText("0.00");
            }
            if ((right_away_edit_no.getText().toString()) != null && (right_away_edit_no.getText().toString()).length() > 0) {
                buke = Double.parseDouble(right_away_edit_no.getText().toString());
            }
            if (buke > zong) {
                ToastUtil.showToast(RightAwayNewActivity.this, "请输入合适金额");
                layout_at_once_pay.setClickable(false);
            } else {
                layout_at_once_pay.setClickable(true);
            }
            double youhuijine1 = (zong - buke) * 0.5;
            double meiyou1 = zong * 0.5;
            double xuzhifu1 = zong - youhuijine1;
            String youhuijine = String.format("%.2f", youhuijine1);
            String meiyou = String.format("%.2f", meiyou1);
            String xuzhifu = String.format("%.2f", xuzhifu1);
            String zong1 = String.format("%.2f", zong);
            if (youhuijine1 != 0) {
                tv_youhui.setText(youhuijine);
                tv_xuzhi.setText(xuzhifu);
            } else if (zong == buke) {
                tv_youhui.setText("0.00");
                tv_xuzhi.setText(zong1);
            } else {
                tv_youhui.setText("0.00");
                tv_xuzhi.setText("0.00");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 2) {
                if (!s.toString().contains(".") && s.toString().substring(0, 1).equals("0") && !(s.toString().substring(1, 2).equals("0"))) {
                    if (editText.getId() == R.id.right_away_edit) {
                        right_away_edit.setText(s.toString().substring(s.length() - 1) + ".00");
                        right_away_edit.setSelection((s.toString().substring(s.length() - 1) + ".00").length());
                    } else {
                        right_away_edit_no.setText(s.toString().substring(s.length() - 1) + ".00");
                        right_away_edit_no.setSelection((s.toString().substring(s.length() - 1) + ".00").length());
                    }
                } else if (s.toString().substring(0, 1).equals("0") && s.toString().substring(1, 2).equals("0")) {
                    if (editText.getId() == R.id.right_away_edit) {
                        right_away_edit.setText(s.toString().substring(s.length() - 1) + ".00");
                        right_away_edit.setSelection((s.toString().substring(s.length() - 1) + ".00").length());
                    } else {
                        right_away_edit_no.setText(s.toString().substring(s.length() - 1) + ".00");
                        right_away_edit_no.setSelection((s.toString().substring(s.length() - 1) + ".00").length());
                    }
                }
            }
        }
    }
}
