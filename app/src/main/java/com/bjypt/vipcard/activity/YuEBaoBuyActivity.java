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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.SystemInfomationBean;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.MD5;
import com.bjypt.vipcard.utils.MyPopupWindows;
import com.bjypt.vipcard.utils.ObjectMapperFactory;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.bjypt.vipcard.common.Config.userConfig.pkregister;

public class YuEBaoBuyActivity extends BaseActivity implements VolleyCallBack {

    private TextView at_once_buy;
    private TextView tv_shouyi_yu_e_bao_by;
    //private TextView tv_price_yu_e_bao_by;
    private TextView tv_days_yu_e_bao_by;
    private TextView tv_yu_e_by_name;
    private EditText et_price_yu_e_bao;
    private LinearLayout ly_back_yu_e_by;


    private Map<String, String> map = new HashMap<>();
    private YuEBaoBuyActivity.PsdDialog psdDialog;
    private MyPopupWindows myPopupWindows;

    private SystemInfomationBean systemInfomationBean;
    private TextView tv_my_zhanghu_yu_e;
    private ImageView add_money;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    tv_my_zhanghu_yu_e.setText("余额：" + systemInfomationBean.getResultData().getBalance() + "元");
                    break;
            }
        }
    };
    private ImageView iv_pay_yue;


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_yu_ebao_buy);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        ly_back_yu_e_by = (LinearLayout) findViewById(R.id.ly_back_yu_e_by);
        at_once_buy = (TextView) findViewById(R.id.at_once_buy);
        tv_yu_e_by_name = (TextView) findViewById(R.id.tv_yu_e_by_name);
        tv_shouyi_yu_e_bao_by = (TextView) findViewById(R.id.tv_shouyi_yu_e_bao_by);
        tv_days_yu_e_bao_by = (TextView) findViewById(R.id.tv_days_yu_e_bao_by);
        et_price_yu_e_bao = (EditText) findViewById(R.id.et_price_yu_e_bao);

        tv_my_zhanghu_yu_e = (TextView) findViewById(R.id.tv_my_zhanghu_yu_e);
        add_money = (ImageView) findViewById(R.id.add_money);

        iv_pay_yue = (ImageView) findViewById(R.id.iv_pay_yue);

        iv_pay_yue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_pay_yue.setSelected(!iv_pay_yue.isSelected());


            }
        });
    }

    @Override
    public void afterInitView() {
        myPopupWindows = new MyPopupWindows(this, R.layout.layout_goumai_success);
        myPopupWindows.setMyCallBack(new MyPopupWindows.MyCallBack() {
            @Override
            public void func(View view) {
                view.findViewById(R.id.tv_jixu_buy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myPopupWindows.dismiss();
                    }
                });
                view.findViewById(R.id.go_back_order).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(YuEBaoBuyActivity.this, FinancingTakeNotesActivity.class);
                        intent.putExtra("pkmuser", getIntent().getStringExtra("pkmuser"));
                        intent.putExtra("pageFlag", "2");
                        startActivity(intent);
                        myPopupWindows.dismiss();
                    }
                });
            }
        });
        if (getIntent().getStringExtra("productrate").contains(".")) {
            tv_shouyi_yu_e_bao_by.setText(getIntent().getStringExtra("productrate").substring(0, getIntent().getStringExtra("productrate").indexOf(".") + 2) + "%");
        } else {
            tv_shouyi_yu_e_bao_by.setText(getIntent().getStringExtra("productrate") + "%");
        }

        tv_yu_e_by_name.setText(getIntent().getStringExtra("productName"));
        tv_days_yu_e_bao_by.setText(getIntent().getStringExtra("time"));
    }

    @Override
    public void onResume() {

        if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
            Map<String, String> params = new HashMap<>();
            params.put("pkregister", getFromSharePreference(pkregister));
            Wethod.httpPost(YuEBaoBuyActivity.this, 1, Config.web.system_infomatiob, params, this);
        }
        super.onResume();
        UmengCountContext.onPageStart("YuEBaoBuyActivity");
    }

    @Override
    public void bindListener() {

        add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRechage(2, systemInfomationBean.getResultData().getPkmuser());
            }
        });

        ly_back_yu_e_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        at_once_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (et_price_yu_e_bao.getText().toString().equals("")) {
                    Toast.makeText(YuEBaoBuyActivity.this, "购买金额不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (Double.parseDouble(et_price_yu_e_bao.getText().toString()) > Double.parseDouble(systemInfomationBean.getResultData().getBalance())) {
                        // 弹出选择框，去充值还是取消平台余额支付
                        new AlertDialog.Builder(YuEBaoBuyActivity.this).setTitle("充值确认").setMessage("是否去充值？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        startRechage(2, systemInfomationBean.getResultData().getPkmuser());
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                    } else {
                        psdDialog = new YuEBaoBuyActivity.PsdDialog(YuEBaoBuyActivity.this, "购买", "", "¥" + et_price_yu_e_bao.getText().toString());
                        psdDialog.show();
                    }
                }

            }
        });
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


    private void request(String password) {
        if (!map.isEmpty()) {
            map.clear();
        }
        try {
            String time = System.currentTimeMillis() + "";
            String secretStr = MD5.string2MD5((MD5.string2MD5(getFromSharePreference(Config.userConfig.pkregister))
                    + MD5.string2MD5(time) +//当前时间戳
                    MD5.string2MD5(et_price_yu_e_bao.getText().toString())//购买金额
                    + MD5.string2MD5(MD5.getMd5(password, MD5.key))));//密码
            Log.e("liyunte", "MD5--" + secretStr);
            map.put("pkRegister", getFromSharePreference(Config.userConfig.pkregister));
            map.put("pkProduct", getIntent().getStringExtra("pkProduct"));
            map.put("timeStamp", time);
            map.put("investMoney", et_price_yu_e_bao.getText().toString());
            map.put("secretStr", secretStr);
            Log.e("liyunte", "pkRegister==" +
                    getFromSharePreference(Config.userConfig.pkregister) +
                    "pkProduct==" + getIntent().getStringExtra("pkProduct") + "timeStamp==" + time +
                    "investMoney==" + et_price_yu_e_bao.getText().toString() + "secretStr==" + secretStr);
            Wethod.httpPost(this, 11, Config.web.yu_e_bao_buy, map, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 11) {
            psdDialog.dismiss();
            myPopupWindows.show(ly_back_yu_e_by, 0, 0);
        } else if (reqcode == 1) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                systemInfomationBean = objectMapper.readValue(result.toString(), SystemInfomationBean.class);

                mHandler.sendEmptyMessage(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if (reqcode == 11) {
            psdDialog.dismiss();
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
            EditText et = (EditText) findViewById(R.id.editHide);

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
                    YuEBaoBuyActivity.PsdDialog.this.dismiss();
                }
            });
        }

        void setKey(String key) {
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
                if (s.toString().length() == 6) {
                    request(s.toString());
                }
                setKey(s.toString());
            }
        };

    }

    public static String getUserDate(String sformat) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(sformat));
        return sf.format(c.getTime());
    }
}
