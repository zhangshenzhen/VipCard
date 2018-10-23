package com.bjypt.vipcard.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.crowdfunding.projectdetail.entity.ProjectDetailPeriodItem;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.FomartToolUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CfProjectDetailPeriodItemView extends LinearLayout implements VolleyCallBack {

    private static final int request_code_project_deatil_period_item = 10003;


    private LinearLayout linear_table;

    private List<View> childs;

    private int selectItemId =0;
    private TextView tv_end_time;

    public CfProjectDetailPeriodItemView(Context context) {
        super(context);
    }

    public CfProjectDetailPeriodItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CfProjectDetailPeriodItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_crowdfunding_project_period_item, this);
        //结算日期
        tv_end_time = (TextView)findViewById(R.id.tv_end_time);
       //tv_project_item_desc = findViewById(R.id.tv_project_item_desc);
        linear_table = findViewById(R.id.linear_table);
       // relate_desc = findViewById(R.id.relate_desc);
        childs = new ArrayList<>();
    }
   protected CfProjectDetailAmountItemView cfProjectDetailAmountItemView;
    public void loadProjectPeroidItem(Integer pkprojectid,CfProjectDetailAmountItemView cfProjectDetailAmountItemView) {
        this.cfProjectDetailAmountItemView = cfProjectDetailAmountItemView;
        Map<String, String> params = new HashMap<>();
        params.put("pkprojectid", pkprojectid + "");
        Wethod.httpPost(getContext(), request_code_project_deatil_period_item, Config.web.cf_project_period_item, params, this, View.GONE);
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == request_code_project_deatil_period_item) {
            LogUtil.debugPrint("request_code_project_deatil_period_item:" + result.toString());
            try {
                ProjectDetailPeriodItem projectDetailPeriodItem = ObjectMapperFactory.createObjectMapper().readValue(result.toString(), ProjectDetailPeriodItem.class);
                if (projectDetailPeriodItem != null && projectDetailPeriodItem.getResultData() != null) {
                    int rows = projectDetailPeriodItem.getResultData().size() / 3;
                    if (projectDetailPeriodItem.getResultData().size() % 3 > 0) {
                        rows = rows + 1;
                    }
                    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                    for (int i = 0; i < rows; i++) {
                        LinearLayout linearLayout = new LinearLayout(getContext());
                        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout.setWeightSum(3);
                        linearLayout.setLayoutParams(layoutParams);
                        int cols = Math.min(3*(i+1), projectDetailPeriodItem.getResultData().size());
                      /*  boolean isTip = false;
                        for (int j=3*i;j<cols;j++){
                            if(StringUtil.isNotEmpty(projectDetailPeriodItem.getResultData().get(j).getGift())){
                                isTip = true;
                            }
                        }*/
                        for (int j=3*i;j<cols;j++){
                            View view = layoutInflater.inflate(R.layout.view_crowdfunding_project_period_item_sub, null);
                            LayoutParams subLayoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                            subLayoutParams.weight = 1;
                            subLayoutParams.topMargin = DensityUtil.dip2px(getContext(),5);
                            Button btn_item_period = view.findViewById(R.id.btn_item_period);
                            TextView tv_interestRate = view.findViewById(R.id.tv_interestRate);

                            tv_interestRate.setText(FomartToolUtils.fomartPercentNum(projectDetailPeriodItem.getResultData().get(j).getInterestRate()+"")+"%");
                            btn_item_period.setText(projectDetailPeriodItem.getResultData().get(j).getDurationTitle()+"");

                            RelativeLayout relativeLayout = view.findViewById(R.id.re_layout);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            relativeLayout.setLayoutParams(params);
                           /* ViewTreeObserver treeObserver = btn_item_period.getViewTreeObserver();
                            treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    btn_item_period.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                    int width = btn_item_period.getWidth();
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    relativeLayout.setLayoutParams(params);
                                }
                            });*/

                            btn_item_period.setOnClickListener(new OnClickListener(){
                                @Override
                                public void onClick(View view) {
                                    ProjectDetailPeriodItem.ResultDataBean resultDataBean = (ProjectDetailPeriodItem.ResultDataBean)view.getTag();
                                    refreshSelectItem(resultDataBean);
                                }
                            });

                            view.setLayoutParams(subLayoutParams);
                            btn_item_period.setTag(projectDetailPeriodItem.getResultData().get(j));
                            childs.add(view);
                            linearLayout.addView(view);
                        }
                        linear_table.addView(linearLayout);

                        //默认选中第一个
                        if(i==0){
                         refreshSelectItem(projectDetailPeriodItem.getResultData().get(i));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshSelectItem(ProjectDetailPeriodItem.ResultDataBean resultDataBean) {

        for (int i=0;i< childs.size();i++){
            Button btn_item_period = childs.get(i).findViewById(R.id.btn_item_period);
            ProjectDetailPeriodItem.ResultDataBean childBean = (ProjectDetailPeriodItem.ResultDataBean)  btn_item_period.getTag();
            if(resultDataBean.getPkprogressdurationid() == childBean.getPkprogressdurationid()){
                btn_item_period.setSelected(true);
                tv_end_time.setText(FomartToolUtils.fomartDate(childBean.getSettledAt()+""));
                selectItemId = childBean.getPkprogressdurationid();//作为参数传递
                cfProjectDetailAmountItemView.loadProjectAmountItem(selectItemId ,FomartToolUtils.fomartNum(childBean.getInterestRate()+""),"");

            }else{
                btn_item_period.setSelected(false);
            }
        }
    }
    public int getprogressdurationid(){
        return selectItemId;
    }


    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
