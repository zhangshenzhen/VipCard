package com.bjypt.vipcard.activity.crowdfunding;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.NewBindBankCardActivity;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.data.bean.CommonWebData;
import com.bjypt.vipcard.activity.shangfeng.primary.commonweb.CommonWebActivity;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.cf.CfProjectDetailItemDataBean;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.wxapi.Util;
import com.google.gson.JsonObject;
import com.sinia.orderlang.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SupportInfoActivity extends BaseActivity implements VolleyCallBack {

    private TextView tv_support_money;
    private LinearLayout real_name_remind;
    private TextView tv_recevice_remind;
    private TextView tv_go_payfor;
    private Button btn_go_payfor;
    private ImageView igv_black;
    private final int resultCode = 123;
    private int pkprogressitemid;
    private int pkmerchantid;
    private int paytype;
    private boolean checkBankNo;
    private String gift;
    private double saleCount;
    private int itemCount;
    private int pkprojectid;
    private boolean is_Realname;
    private Button btn_real_name;
    private TextView recevice_remind;
    private String tips;
    private BigDecimal itemAmount;


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_support_info);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        pkprogressitemid = intent.getIntExtra("pkprogressitemid", 0);
        pkmerchantid = intent.getIntExtra("pkmerchantid", 0);
        paytype = intent.getIntExtra("paytype", 0);

    }

    @Override
    public void initView() {
        igv_black = (ImageView) findViewById(R.id.igv_back);//fanhui
        btn_real_name = (Button) findViewById(R.id.btn_real_name);//认证
        real_name_remind = (LinearLayout) findViewById(R.id.real_name_remind);
        tv_support_money = (TextView) findViewById(R.id.tv_support_money);//支持金额
        tv_recevice_remind = findViewById(R.id.tv_recevice_remind);//收益提示信息
        tv_go_payfor = findViewById(R.id.tv_payfor_money);//支付金额
        recevice_remind = findViewById(R.id.tv_recevice_remind);
        btn_go_payfor = findViewById(R.id.btn_go_payfor);
        btn_real_name.setOnClickListener(this);
        igv_black.setOnClickListener(this);
        btn_go_payfor.setOnClickListener(this);
    }

    @Override
    public void afterInitView() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getPkregister());
        LogUtil.debugPrint("SupportInfoActivity = pkregister " + getPkregister());
        params.put("pkprogressitemid", pkprogressitemid + "");
        params.put("pkregister", getPkregister());
        // String url = "http://123.57.232.188:19096/api/hybCfMerchantCrowdfundingProjectItem/getProjectItemExplain";
        Wethod.httpPost(this, resultCode, Config.web.zhongchou_supportInfo_url, params, this);
    }

    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_go_payfor:
                remindDialog();
                break;
            case R.id.igv_back:
                finish();
                break;
            case R.id.btn_real_name:
                if (!is_Realname && real_name_remind.getVisibility() == View.VISIBLE) {//可见的
                    //去实名认证
                    String params = "pkregister=" + getPkregister();
                    CommonWebData withdraw = new CommonWebData();
                    withdraw.setTitle("实名认证");
                    withdraw.setUrl(Config.web.h5_CFConsumereal_name + params);
                    CommonWebActivity.callActivity(this, withdraw);
                }
                break;
        }

    }

    private void remindDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View v = inflater.inflate(R.layout.myself_dialog, null);
        //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        Button btn_look = (Button) v.findViewById(R.id.btn_look);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        btn_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (itemAmount != null) {
                    Intent intent = new Intent(SupportInfoActivity.this, SupportAgreementActivity.class);
                    intent.putExtra("pkprogressitemid", pkprogressitemid);
                    intent.putExtra("pkmerchantid", pkmerchantid);
                    intent.putExtra("amount", itemAmount.stripTrailingZeros().toPlainString());
                    intent.putExtra("paytype", paytype);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        LogUtil.debugPrint("SupportInfoActivity = onSuccess " + reqcode + " result " + result);
        switch (reqcode) {
            case resultCode:
                try {
                    CfProjectDetailItemDataBean cfProjectDetailItemDataBean = ObjectMapperFactory.createObjectMapper().readValue(result.toString(), CfProjectDetailItemDataBean.class);
                    if (cfProjectDetailItemDataBean != null) {
                        updata(cfProjectDetailItemDataBean);
                    } else {
                        Toast.makeText(SupportInfoActivity.this, "请求结果为空", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private void updata(CfProjectDetailItemDataBean dataBean) {
        CfProjectDetailItemDataBean.ResultDataBean resultBeanData = dataBean.getResultData();

        itemCount = resultBeanData.getItemCount();
        saleCount = resultBeanData.getSaleCount();
        itemAmount = resultBeanData.getItemAmount();

        tv_support_money.setText(resultBeanData.getItemAmount().stripTrailingZeros().toPlainString() + "");
        tv_go_payfor.setText(itemAmount.stripTrailingZeros().toPlainString() + "");
        recevice_remind.setText(resultBeanData.getTips());
        is_Realname = resultBeanData.isCheckBankNo();
        if (is_Realname) {//是否实名认证
            real_name_remind.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        LogUtil.debugPrint("SupportInfoActivity = onFailed " + reqcode + " result " + result);

    }

    @Override
    public void onError(VolleyError volleyError) {
        LogUtil.debugPrint("SupportInfoActivity = onError " + volleyError.getMessage());

    }
}
