package com.bjypt.vipcard.activity.crowdfunding.projectdetail;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.crowdfunding.SupportInfoActivity;
import com.bjypt.vipcard.activity.crowdfunding.projectdetail.entity.ProjectDetailDataBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.CommonWebData;
import com.bjypt.vipcard.activity.shangfeng.primary.commonweb.CommonWebActivity;
import com.bjypt.vipcard.base.BaseFraActivity;
import com.bjypt.vipcard.base.BaseFragment;
import com.bjypt.vipcard.base.RespBase;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.pulltorefresh.social.custom.AppBarHeaderBehavior;
import com.bjypt.vipcard.utils.AmountDisplayUtil;
import com.bjypt.vipcard.utils.DialogUtil;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.PermissionUtils;
import com.bjypt.vipcard.utils.FomartToolUtils;
import com.bjypt.vipcard.view.CfProjectDetailAmountItemView;
import com.bjypt.vipcard.view.CfProjectDetailPeriodItemView;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.view.categoryview.CrowdfundingDetailBannerView;
import com.bjypt.vipcard.widget.SucessTipAutoCloseDialog;
import com.flyco.tablayout.SlidingTabLayout;
import com.githang.statusbar.StatusBarCompat;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CrowdfundingDetailActivity extends BaseFraActivity implements VolleyCallBack {

    private static final int request_code_project_deatil = 10023;
    private static final int request_code_favo_project = 10024;
    private static final int request_code_unfavo_project = 10025;

    private SlidingTabLayout slidingTab = null;
    private ViewPager viewPager = null;

    private AppBarLayout appBar = null;
    private AppBarHeaderBehavior behavior;
    private HomeSubFragmentAdapter fragmentAdapter;
    BaseFragment[] projectContentFragments;

    ImageButton ibtn_back;
    TextView tv_project_name;
    TextView tv_cf_amount;
    TextView tv_progress_amount;
    ProgressBar pb_project_progress;
    TextView tv_project_amount_rate;
    TextView tv_remaining_count;
    TextView tv_remaining_days;
    LinearLayout linear_merchant_project;
    ImageView iv_merchant_headimg;
    TextView tv_merchant_name;
    TextView tv_merchant_desc;
    ImageView iv_project_favo;
    ImageView iv_project_customer_service;
    Button btn_topay;
    ImageView igv_zhongchou_status;//众筹状态
    TextView tv_high_year_rate;//最高年化率
    TextView tv_hight_income;//最高收益
    TextView tv_end_time;//截止时间
    TextView tv_settle_type;//返息类型
    private Integer pkprojectid;
    ProjectDetailDataBean projectDetailDataBean;
    CrowdfundingDetailBannerView crowdfundingDetailBannerView;
    CfProjectDetailAmountItemView cfProjectDetailAmountItemView;
    private LinearLayout linear_collection;

    public static final int request_pay_result_code = 10001;
    private String telephone;
    private CfProjectDetailPeriodItemView cfProjectDetailPeriodItemView;

    @Override
    public void setContentLayout() {
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.cf_account_detail_head));
        setContentView(R.layout.activity_crowdfunding_detail);
    }

    @Override
    public void beforeInitView() {
        pkprojectid = getIntent().getIntExtra("pkprojectid", 0);
        if (pkprojectid == 0) {
            finish();
        }
    }

    @Override
    public void initView() {
        slidingTab = findViewById(R.id.slidingTab);
        viewPager = findViewById(R.id.viewPager);
        appBar = findViewById(R.id.appBar);
        behavior = (AppBarHeaderBehavior) ((CoordinatorLayout.LayoutParams) appBar.getLayoutParams()).getBehavior();
        appBar.setExpanded(true, false);

        ibtn_back = findViewById(R.id.ibtn_back);
        tv_project_name = findViewById(R.id.tv_project_name);
        tv_cf_amount = findViewById(R.id.tv_cf_amount);
        tv_progress_amount = findViewById(R.id.tv_progress_amount);
        pb_project_progress = findViewById(R.id.pb_project_progress);
        tv_project_amount_rate = findViewById(R.id.tv_project_amount_rate);
        tv_remaining_count = findViewById(R.id.tv_remaining_count);
        tv_remaining_days = findViewById(R.id.tv_remaining_days);
        linear_merchant_project = findViewById(R.id.linear_merchant_project);
        iv_merchant_headimg = findViewById(R.id.iv_merchant_headimg);
        tv_merchant_name = findViewById(R.id.tv_merchant_name);
        tv_merchant_desc = findViewById(R.id.tv_merchant_desc);
        iv_project_favo = findViewById(R.id.iv_project_favo);
        iv_project_customer_service = findViewById(R.id.iv_project_customer_service);
        crowdfundingDetailBannerView = findViewById(R.id.crowdfundingDetailBannerView);
        cfProjectDetailAmountItemView = findViewById(R.id.cfProjectDetailAmountItemView);
        cfProjectDetailPeriodItemView = findViewById(R.id.cfProjectDetailPeriodItemView);
        btn_topay = findViewById(R.id.btn_topay);
        igv_zhongchou_status = findViewById(R.id.igv_zhongchou_status);
        tv_high_year_rate = findViewById(R.id.tv_high_year_rate);
        tv_hight_income = findViewById(R.id.tv_hight_income);
        tv_end_time = findViewById(R.id.tv_end_time);
        tv_settle_type = findViewById(R.id.tv_settle_type);
        ibtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void afterInitView() {
        projectContentFragments = new ProjectContentFragment[4];
        String tabparams = String.format("pkregister=%s&pkprojectid=%s", getPkregister(), pkprojectid + "");
        projectContentFragments[0] = ProjectContentFragment.newInstance(Config.web.h5_cf_product_info + tabparams);

        projectContentFragments[1] = ProjectContentFragment.newInstance(Config.web.h5_cf_team_introduction + tabparams);
        projectContentFragments[2] = ProjectContentFragment.newInstance(Config.web.h5_cf_common_problem + tabparams);
        projectContentFragments[3] = ProjectContentFragment.newInstance(Config.web.h5_cf_project_progress + tabparams);
        fragmentAdapter = new HomeSubFragmentAdapter(getSupportFragmentManager(), projectContentFragments);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(projectContentFragments.length);

        slidingTab.setViewPager(viewPager);

        getProjectDetail();

        cfProjectDetailPeriodItemView.loadProjectPeroidItem(pkprojectid,cfProjectDetailAmountItemView);
        //cfProjectDetailPeriodItemView.
            //移动到 loadProjectPeroidItem 方法中
       // cfProjectDetailAmountItemView.loadProjectAmountItem(cfProjectDetailPeriodItemView.getprogressdurationid());
    }

    private void getProjectDetail() {
        Map<String, String> params = new HashMap<>();
        params.put("pkprojectid", pkprojectid + "");
        params.put("pkregister", getPkregister() + "");
        Wethod.httpPost(this, request_code_project_deatil, Config.web.get_crowdfunding_project_detail, params, this, View.GONE);
    }

    private void favoProjectDetail() {
        Map<String, String> params = new HashMap<>();
        params.put("pkprojectid", pkprojectid + "");
        params.put("pkregister", getPkregister() + "");
        Wethod.httpPost(this, request_code_favo_project, Config.web.cf_favo_project, params, this);
    }

    private void unfavoProjectDetail() {
        Map<String, String> params = new HashMap<>();
        params.put("id", projectDetailDataBean.getResultData().getCheckId() + "");
        Wethod.httpPost(this, request_code_unfavo_project, Config.web.cf_unfavo_project, params, this);
    }


    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == request_code_project_deatil) {
            handlerProjectDetail(result);
            LogUtil.debugPrint("handlerProjectDetail = "+ result);
        } else if (reqcode == request_code_favo_project) {
            try {
                iv_project_favo.setSelected(true);
                RespBase respBase = ObjectMapperFactory.createObjectMapper().readValue(result.toString(), RespBase.class);
                projectDetailDataBean.getResultData().setCheckId(Integer.parseInt(respBase.getResultData().toString()));
                showSucessTip("收藏成功");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == request_code_unfavo_project) {
            iv_project_favo.setSelected(false);
            projectDetailDataBean.getResultData().setCheckId(null);
            showSucessTip("取消收藏成功");
        }
    }

    private void showSucessTip(String msg) {
        SucessTipAutoCloseDialog rechargeStateDialog = new SucessTipAutoCloseDialog(this);
        rechargeStateDialog.setTextContent(msg);
        rechargeStateDialog.show();
    }


    @Override
    public void onFailed(int reqcode, Object result) {
        Wethod.ToFailMsg(this, "DetailsonFailed"+result);
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    @Override
    public void bindListener() {
        btn_topay.setOnClickListener(this);
        linear_merchant_project.setOnClickListener(this);
        iv_project_customer_service.setOnClickListener(this);
        iv_project_favo.setOnClickListener(this);

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_topay:
                if (projectDetailDataBean == null) {
                    ToastUtil.showToast(this, "正在加载,请稍后...");
                    return;
                }
                if (cfProjectDetailAmountItemView.getSelectProjectItemId() == 0) {
                    ToastUtil.showToast(this, "请选择一个金额");
                } else {
                    Intent topay = new Intent(this, SupportInfoActivity.class);
                    topay.putExtra("getSelectTipText",cfProjectDetailAmountItemView.getSelectTipText());
                    topay.putExtra("paytype", projectDetailDataBean.getResultData().getPayType());
                    topay.putExtra("pkprogressitemid", cfProjectDetailAmountItemView.getSelectProjectItemId());
                    topay.putExtra("pkmerchantid", projectDetailDataBean.getResultData().getPkmerchantid());
                    topay.putExtra("projectDetailDataBean",projectDetailDataBean);
                    startActivityForResult(topay, request_pay_result_code);
                }
                break;
            case R.id.linear_merchant_project:
                if (projectDetailDataBean != null && projectDetailDataBean.getResultData() != null) {
                    CommonWebData merchant = new CommonWebData();
                    merchant.setTitle("商家信息");
                    merchant.setUrl(Config.web.h5_CFMerchantInfo + "pkmerchantid=" + projectDetailDataBean.getResultData().getPkmerchantid());
                    CommonWebActivity.callActivity(this, merchant);
                }
                break;
            case R.id.iv_project_favo://收藏View
                if (projectDetailDataBean != null && projectDetailDataBean.getResultData() != null) {
                   if (projectDetailDataBean.getResultData().getCheckId() == null) {
                        favoProjectDetail();
                   } else {
                        unfavoProjectDetail();
                    }
                }
                break;

            case R.id.iv_project_customer_service:
                DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case Dialog.BUTTON_POSITIVE:
//                                requestPermission();
                                PermissionUtils permissionUtils = new PermissionUtils(CrowdfundingDetailActivity.this, telephone);
                                permissionUtils.requestPermission();
                                break;
                            case Dialog.BUTTON_NEGATIVE:
                                break;
                            case Dialog.BUTTON_NEUTRAL:
                                break;
                        }
                    }
                };
                //弹窗让用户选择，是否允许申请权限
                DialogUtil.showConfirm(this, "客服热线", "是否拨打客服热线"+telephone+"(08:00-17:00)", dialogOnclicListener, dialogOnclicListener);

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == request_pay_result_code && resultCode == RESULT_OK){
            if(data != null){
                boolean gotoMain = data.getBooleanExtra("gotoCfMain", false);
                if(gotoMain){
                    finish();
                }
            }else{
                finish();
            }

        }
    }

    private void handlerProjectDetail(Object result) {
        try {
            projectDetailDataBean = ObjectMapperFactory.createObjectMapper().readValue(result.toString(), ProjectDetailDataBean.class);
            if (projectDetailDataBean != null && projectDetailDataBean.getResultData() != null) {
                tv_project_name.setText(projectDetailDataBean.getResultData().getProjectName());
                tv_cf_amount.setText(AmountDisplayUtil.displayChineseWan(projectDetailDataBean.getResultData().getCfAmount()));
                tv_progress_amount.setText(AmountDisplayUtil.displayChineseWan2(projectDetailDataBean.getResultData().getProgressCfAmount()));
                if (projectDetailDataBean.getResultData().getCfAmount().compareTo(new BigDecimal(0)) > 0) {
                    BigDecimal progress = projectDetailDataBean.getResultData().getProgressCfAmount().divide(projectDetailDataBean.getResultData().getCfAmount(), 2, BigDecimal.ROUND_HALF_UP);
                    pb_project_progress.setProgress(progress.intValue() > 100 ? 100 : progress.intValue());
                    tv_project_amount_rate.setText(progress.multiply(new BigDecimal(100)).intValue() + "%");
                    pb_project_progress.setProgress(progress.multiply(new BigDecimal(100)).intValue());
                } else {
                    pb_project_progress.setProgress(100);
                    tv_project_amount_rate.setText("100%");
                }
                tv_remaining_count.setText(projectDetailDataBean.getResultData().getNumber() + "份");
                tv_remaining_days.setText(projectDetailDataBean.getResultData().getDays() + "天");
                tv_merchant_name.setText(projectDetailDataBean.getResultData().getMerchantName());
                tv_merchant_desc.setText(projectDetailDataBean.getResultData().getOneContent());//修改了字段

                //新增部分
                  tv_high_year_rate.setText(FomartToolUtils.fomartNum(projectDetailDataBean.getResultData().getMaxInterestRate()+"")+"%");
                  tv_hight_income.setText(projectDetailDataBean.getResultData().getMaximumIncome()==null ?projectDetailDataBean.getResultData().getMaximumIncome()+""
                          :FomartToolUtils.fomartMoney(projectDetailDataBean.getResultData().getMaximumIncome()));
                  tv_end_time.setText(projectDetailDataBean.getResultData().getBuyEndAt()>0 ?
                    FomartToolUtils.fomartDate(projectDetailDataBean.getResultData().getBuyEndAt()+""):"2018—08-08");//截止

               if (projectDetailDataBean.getResultData().getSettleType()==0){
                  tv_settle_type.setText("每日返息");
               }else if(projectDetailDataBean.getResultData().getSettleType()==1){
                 tv_settle_type.setText("每日返本息");
               }else {
                   tv_settle_type.setText("到期返本息");
               }

               if (projectDetailDataBean.getResultData().getStatus()==0){
                igv_zhongchou_status.setImageDrawable(this.getResources().getDrawable(R.mipmap.cf_project_status_build));
               }else if (projectDetailDataBean.getResultData().getStatus()==1){
                igv_zhongchou_status.setImageDrawable(this.getResources().getDrawable(R.mipmap.cf_project_status_start));
               }else if (projectDetailDataBean.getResultData().getStatus()==2){
                igv_zhongchou_status.setImageDrawable(this.getResources().getDrawable(R.mipmap.cf_project_status_end));
               }else {

               }

                Picasso.with(this)
                        .load(projectDetailDataBean.getResultData().getMerchantLogo())
                        .error(R.mipmap.merchant_item_error)
                        .into(iv_merchant_headimg);
                if (projectDetailDataBean.getResultData().getCheckId() != null && projectDetailDataBean.getResultData().getCheckId() > 0) {
                    iv_project_favo.setSelected(true);
                } else {
                    iv_project_favo.setSelected(false);
                }
                //客服电话
                telephone = projectDetailDataBean.getResultData().getTelephone();

                crowdfundingDetailBannerView.setDataSource(projectDetailDataBean.getResultData().getHybAttachmentList());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        crowdfundingDetailBannerView.resumePlay();
        super.onResume();
    }

    /**
     * ViewPager 的适配器
     */
    private static class HomeSubFragmentAdapter extends FragmentStatePagerAdapter {
        private BaseFragment[] fragment = null;
        private String[] titles = null;

        public HomeSubFragmentAdapter(FragmentManager fm, BaseFragment... fragment) {
            super(fm);
            this.fragment = fragment;
            this.titles = new String[]{"产品介绍", "团队介绍", "常见问题", "项目进展"};
        }

        @Override
        public Fragment getItem(int position) {
            return fragment[position];
        }

        @Override
        public int getCount() {
            return fragment.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }


    }
}
