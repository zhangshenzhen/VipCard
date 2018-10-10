package com.bjypt.vipcard.activity.crowdfunding;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.NewBindBankCardActivity;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.crowdfunding.pay.CrowdfundingPayActivity;
import com.bjypt.vipcard.activity.crowdfunding.projectdetail.entity.ProjectDetailDataBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.CommonWebData;
import com.bjypt.vipcard.activity.shangfeng.primary.commonweb.CommonWebActivity;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.cf.CfProjectDetailItemDataBean;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.wxapi.Util;
import com.google.gson.JsonObject;
import com.sinia.orderlang.utils.AppInfoUtil;
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
    private TextView tv_danger_instruc;
    private String selectTip;

    private static final int request_pay_result_code = 10001;
    private static final int request_check_result_code = 10002;
    private CheckBox ck_box;
    private TextView dialog_tontent;
    private Button btn_look;
    private ProjectDetailDataBean projectDetailDataBean;


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_support_info);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        projectDetailDataBean = (ProjectDetailDataBean) intent.getSerializableExtra("projectDetailDataBean");
        pkprogressitemid = intent.getIntExtra("pkprogressitemid", 0);
        pkmerchantid = intent.getIntExtra("pkmerchantid", 0);
        paytype = intent.getIntExtra("paytype", 0);
        selectTip = intent.getStringExtra("getSelectTipText"); //提示信息
    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void initView() {
        igv_black = (ImageView) findViewById(R.id.igv_back);//fanhui
        btn_real_name = (Button) findViewById(R.id.btn_real_name);//认证
        real_name_remind = (LinearLayout) findViewById(R.id.real_name_remind);
        tv_support_money = (TextView) findViewById(R.id.tv_support_money);//支持金额
        tv_recevice_remind = findViewById(R.id.tv_recevice_remind);//收益提示信息
        tv_danger_instruc = findViewById(R.id.tv_danger_instruc);//风险说明
        tv_go_payfor = findViewById(R.id.tv_payfor_money);//支付金额
        recevice_remind = findViewById(R.id.tv_recevice_remind);
        btn_go_payfor = findViewById(R.id.btn_go_payfor);
        btn_real_name.setOnClickListener(this);
        igv_black.setOnClickListener(this);
        btn_go_payfor.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!is_Realname){
          afterInitView();
        }
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
                if(is_Realname){
                 remindDialog();
                }else {
                 Toast.makeText(this,"实名认证之后才能购买",Toast.LENGTH_SHORT).show();
                }
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
        int width = AppInfoUtil.getScreenWidth(this);
        int icon_width = width - DensityUtil.dip2px(this, 1);
        // int icon_height = (int) (icon_width / 1.82);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View v = inflater.inflate(R.layout.myself_dialog, null);

        //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        btn_look = (Button) v.findViewById(R.id.btn_look);
        ck_box = (CheckBox) v.findViewById(R.id.ck_box);
        dialog_tontent = (TextView) v.findViewById(R.id.dialog_tontent);
        // dialog_tontent.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//设置所以文本的下划线
        final Dialog dialog = builder.create();
        dialog.show();
        //dialog设置宽高
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = width - width / 4;//设置宽
        params.height = width - width / 3;
        dialog.getWindow().setAttributes(params);

        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        // AlertDialog.setView(v,0,0,0,0); //去除边框
        //采用拼接的形式
        dialog_tontent.setText("购买前请仔细阅读支持协议");
        SpannableString clickString = new SpannableString("《项目支持协议》");
        clickString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //dialog.dismiss();
                Intent intent = new Intent(SupportInfoActivity.this, SupportAgreementActivity.class);
                crowdStartActivity(intent, request_check_result_code);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#52b9b8"));//设置颜色
                ds.setUnderlineText(true);
            }
        }, 0, clickString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        dialog_tontent.append(clickString);
        //相应点点击事件
        dialog_tontent.setMovementMethod(LinkMovementMethod.getInstance());


        ck_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ck_box.isChecked()) {
                    btn_look.setText("去支付");
                } else {
                    btn_look.setText("点击查看");
                }

            }
        });


        btn_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemAmount != null && (!ck_box.isChecked())) {
                    Intent intent = new Intent(SupportInfoActivity.this, SupportAgreementActivity.class);
                    crowdStartActivity(intent,request_check_result_code);
                } else if (itemAmount != null && (ck_box.isChecked())) {
                    dialog.dismiss();
                    Intent intent = new Intent(SupportInfoActivity.this, CrowdfundingPayActivity.class);
                    crowdStartActivity(intent, request_pay_result_code);
                }
            }
        });
    }


    public void crowdStartActivity(Intent intent, int request_code) {
        intent.putExtra("pkprogressitemid", pkprogressitemid);
        intent.putExtra("pkmerchantid", pkmerchantid);
        intent.putExtra("amount", itemAmount.stripTrailingZeros().toPlainString());
        intent.putExtra("paytype", paytype);
        intent.putExtra("projectDetailDataBean", projectDetailDataBean);
        startActivityForResult(intent, request_code);


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

        tv_support_money.setText(resultBeanData.getItemAmount().stripTrailingZeros().toPlainString() + "元");
        tv_go_payfor.setText(" " + itemAmount.stripTrailingZeros().toPlainString() + "元");

        if (selectTip != null) {
            recevice_remind.setText(Html.fromHtml(selectTip) + "");//提示信息
        } else {
            recevice_remind.setText("暂无信息");//提示信息
        }
        String html = resultBeanData.getExplain();
        if (html != null) {
            tv_danger_instruc.setText("\n" + Html.fromHtml(html) + "");//风险说明
        } else {
            tv_danger_instruc.setText("暂无信息");//风险说明
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_pay_result_code) {
            if (resultCode == RESULT_OK) {
                boolean gotoMain = data.getBooleanExtra("gotoCfMain", false);
                Intent intent = new Intent();
                intent.putExtra("gotoCfMain", gotoMain);
                setResult(RESULT_OK, intent);
                finish();
            }
        } else if (requestCode == request_check_result_code) {
            if (resultCode == RESULT_OK) {
                ck_box.setChecked(true);
                btn_look.setText("去支付");
            }
        }
    }
}
