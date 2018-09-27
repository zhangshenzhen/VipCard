package com.bjypt.vipcard.activity.crowdfunding;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.crowdfunding.pay.CrowdfundingPayActivity;
import com.bjypt.vipcard.activity.crowdfunding.projectdetail.entity.ProjectDetailDataBean;
import com.bjypt.vipcard.base.BaseActivity;

import java.math.BigDecimal;

public class SupportAgreementActivity extends BaseActivity {

    private Button btn_cancel, btn_sure;

    private int pkprogressitemid;
    private int pkmerchantid;
    private String amount;
    private int paytype;
    private TextView tv_agreement;


    private static final int request_pay_result_code = 10001;
    private ProjectDetailDataBean projectDetailDataBean;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_agreement);
    }

    @Override
    public void beforeInitView() {
        pkprogressitemid = getIntent().getIntExtra("pkprogressitemid", 0);
        pkmerchantid = getIntent().getIntExtra("pkmerchantid", 0);
        amount = getIntent().getStringExtra("amount");
        paytype = getIntent().getIntExtra("paytype", 0);
        projectDetailDataBean = (ProjectDetailDataBean) getIntent().getSerializableExtra("projectDetailDataBean");
        if (pkprogressitemid == 0) {
            finish();
        }
    }

    @Override
    public void initView() {
        tv_agreement = findViewById(R.id.tv_agreement);
        btn_cancel = (Button) findViewById(R.id.btn_cancel_agreement);
        btn_sure = (Button) findViewById(R.id.btn_sure_agreement);
        btn_cancel.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        String html_agreement = projectDetailDataBean.getResultData().getAgreement();
        if (html_agreement !=null) {
            tv_agreement.setText("\n" + Html.fromHtml(html_agreement));
        }else {
            tv_agreement.setText("还没有文本数据");
        }
     }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel_agreement:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.btn_sure_agreement:
               /* Intent intent = new Intent(this, CrowdfundingPayActivity.class);
                intent.putExtra("pkprogressitemid", pkprogressitemid);
                intent.putExtra("paytype", paytype);
                intent.putExtra("amount", amount);
                intent.putExtra("pkmerchantid", pkmerchantid);
                startActivityForResult(intent,request_pay_result_code);*/
                setResult(RESULT_OK);
                finish();
                //打开支付界面
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == request_pay_result_code){
            if(resultCode == RESULT_CANCELED){
                finish();
            }else if(resultCode == RESULT_OK){
                boolean gotoMain = data.getBooleanExtra("gotoCfMain", false);
                Intent intent = new Intent();
                intent.putExtra("gotoCfMain", gotoMain);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
