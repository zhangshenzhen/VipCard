package com.bjypt.vipcard.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.ChangePasswordActivity;
import com.bjypt.vipcard.activity.ForgetPayPsdActivity;
import com.bjypt.vipcard.base.RespBase;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.utils.MD5;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/10.
 */

public class CheckPwdDialog extends Dialog implements VolleyCallBack<String> {

    private Context context;
    private String messageOne, MessageTwo, title;
    private TextView mProductName;
    private TextView mProductMoney;
    private RelativeLayout mBack;
    private RelativeLayout psd_details;
    private TextView mPsdTv;

    TextView t1, t2, t3, t4, t5, t6;

    private EditText et;
    private String key = "";
    private static final int request_code_check_pwd = 10;
    private CheckPayPasswordListener listener;

    public CheckPwdDialog(Context context, String title, String messageOne, String MessageTwo) {
        super(context, R.style.MyDialogStyle);
        this.context = context;
        this.title = title;
        this.messageOne = messageOne;
        this.MessageTwo = MessageTwo;
    }

    public CheckPwdDialog(Context context, String title, String messageOne, String MessageTwo, CheckPayPasswordListener listener) {
        super(context, R.style.MyDialogStyle);
        this.context = context;
        this.title = title;
        this.messageOne = messageOne;
        this.MessageTwo = MessageTwo;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        psd_details = (RelativeLayout) findViewById(R.id.psd_details);
        View psd_view = findViewById(R.id.psd_view);

        if (messageOne.equals("")) {
            mProductName.setVisibility(View.GONE);
        } else {
            mProductName.setVisibility(View.VISIBLE);
        }
        if (MessageTwo.equals("")) {
            mProductMoney.setVisibility(View.GONE);
        } else {
            mProductMoney.setVisibility(View.VISIBLE);
        }
        if (title.equals("")) {
            mPsdTv.setVisibility(View.GONE);
        } else {
            mPsdTv.setVisibility(View.VISIBLE);
        }
        if (messageOne.equals("") && MessageTwo.equals("") && title.equals("")) {
            psd_details.setVisibility(View.GONE);
            psd_view.setVisibility(View.GONE);
        } else {
            psd_details.setVisibility(View.VISIBLE);
            psd_view.setVisibility(View.VISIBLE);
        }

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
                CheckPwdDialog.this.dismiss();
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
                payPsdParams.put("phoneno", SharedPreferenceUtils.getFromSharedPreference(context, Config.userConfig.phoneno));
                try {
                    payPsdParams.put("payPassword", MD5.getMd5(key, MD5.key));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                Wethod.httpPost(context, request_code_check_pwd, Config.web.checkout_pay_psd, payPsdParams, CheckPwdDialog.this);
            }
            setKey();
        }
    };


    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == request_code_check_pwd) {
            ToastUtil.showToast(context, "密码校验通过");
            dismiss();
            if (listener != null) {
                listener.onPass();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            RespBase respBase = objectMapper.readValue(result.toString(), RespBase.class);
            if (respBase.getResultData().toString().contains("未查询到用户支付密码信息")) {
                this.dismiss();
                Intent intent = new Intent(context, ChangePasswordActivity.class);
                intent.putExtra("psdType", false);
                context.startActivity(intent);
            } else if (respBase.getResultData().toString().contains("支付密码输入有误")) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                                CheckPwdDialog.this.dismiss();
                                dialog.dismiss();
                                Intent intent = new Intent(context, ForgetPayPsdActivity.class);
                                context.startActivity(intent);
                            }
                        });
                builder.create().show();

            } else {
                ToastUtil.showToast(context, respBase.getResultData().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Wethod.ToFailMsg(context, result);
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    public interface CheckPayPasswordListener {
        public void onPass();

//        public void onError();
    }

}
