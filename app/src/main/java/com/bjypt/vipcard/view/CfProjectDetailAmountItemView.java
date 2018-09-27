package com.bjypt.vipcard.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.crowdfunding.projectdetail.entity.ProjectDetailAmountItem;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.widget.FlyBanner;
import com.sinia.orderlang.utils.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CfProjectDetailAmountItemView extends LinearLayout implements VolleyCallBack {

    private static final int request_code_project_deatil_amount_item = 10003;

    private TextView tv_project_item_desc;
    private LinearLayout linear_table;
    private RelativeLayout relate_desc;
    private List<View> childs;

    private int selectItemId =0;

    public CfProjectDetailAmountItemView(Context context) {
        super(context);
    }

    public CfProjectDetailAmountItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CfProjectDetailAmountItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_crowdfunding_project_amount_item, this);
        tv_project_item_desc = findViewById(R.id.tv_project_item_desc);
        linear_table = findViewById(R.id.linear_table);
        relate_desc = findViewById(R.id.relate_desc);
        childs = new ArrayList<>();
    }

    public void loadProjectAmountItem(Integer pkprojectid) {
        Map<String, String> params = new HashMap<>();
        params.put("pkprojectid", pkprojectid + "");
        Wethod.httpPost(getContext(), request_code_project_deatil_amount_item, Config.web.cf_project_amount_item, params, this, View.GONE);
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == request_code_project_deatil_amount_item) {
            LogUtil.debugPrint("request_code_project_deatil_amount_item:" + result.toString());
            try {
                ProjectDetailAmountItem projectDetailAmountItem = ObjectMapperFactory.createObjectMapper().readValue(result.toString(), ProjectDetailAmountItem.class);
                if (projectDetailAmountItem != null && projectDetailAmountItem.getResultData() != null) {
                    int rows = projectDetailAmountItem.getResultData().size() / 3;
                    if (projectDetailAmountItem.getResultData().size() % 3 > 0) {
                        rows = rows + 1;
                    }
                    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                    for (int i = 0; i < rows; i++) {
                        LinearLayout linearLayout = new LinearLayout(getContext());
                        LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout.setWeightSum(3);
                        linearLayout.setLayoutParams(layoutParams);
                        int cols = Math.min(3*(i+1), projectDetailAmountItem.getResultData().size());
                        boolean isTip = false;
                        for (int j=3*i;j<cols;j++){
                            if(StringUtil.isNotEmpty(projectDetailAmountItem.getResultData().get(j).getGift())){
                                isTip = true;
                            }
                        }
                        for (int j=3*i;j<cols;j++){
                            View view = layoutInflater.inflate(R.layout.view_crowdfunding_project_amount_item_sub, null);
                            LinearLayout.LayoutParams subLayoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                            subLayoutParams.weight = 1;
                            subLayoutParams.topMargin = DensityUtil.dip2px(getContext(),5);
                            TextView tv_item_desc = view.findViewById(R.id.tv_item_desc);
                            Button btn_item_amount = view.findViewById(R.id.btn_item_amount);
                            if(StringUtil.isNotEmpty(projectDetailAmountItem.getResultData().get(j).getGift())){
                                tv_item_desc.setText(projectDetailAmountItem.getResultData().get(j).getGift());
                            }else{
                                if(isTip){
                                    tv_item_desc.setVisibility(INVISIBLE);
                                }else{
                                    tv_item_desc.setVisibility(View.GONE);
                                }
                            }

                        /*   if(i==0){ //默认选中第一个
                              //  ProjectDetailAmountItem.ResultDataBean resultDataBean = (ProjectDetailAmountItem.ResultDataBean)  view.getTag();
                                tv_project_item_desc.setText(projectDetailAmountItem.getResultData().get(i).getItemDesc());
                                relate_desc.setVisibility(View.VISIBLE);
                                refreshSelectItem(projectDetailAmountItem.getResultData().get(i));
                            }*/

                            btn_item_amount.setText("￥"+projectDetailAmountItem.getResultData().get(j).getItemAmount().stripTrailingZeros().toPlainString()+"");
                            btn_item_amount.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View view) {
                                    ProjectDetailAmountItem.ResultDataBean resultDataBean = (ProjectDetailAmountItem.ResultDataBean)  view.getTag();
                                    tv_project_item_desc.setText(resultDataBean.getItemDesc());
                                    relate_desc.setVisibility(View.VISIBLE);
                                    refreshSelectItem(resultDataBean);
                                }
                            });
                            view.setLayoutParams(subLayoutParams);
                            btn_item_amount.setTag(projectDetailAmountItem.getResultData().get(j));
                            childs.add(view);
                            linearLayout.addView(view);
                        }
                        linear_table.addView(linearLayout);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshSelectItem(ProjectDetailAmountItem.ResultDataBean resultDataBean) {
        for (int i=0;i< childs.size();i++){

            Button btn_item_amount = childs.get(i).findViewById(R.id.btn_item_amount);
            ProjectDetailAmountItem.ResultDataBean childBean = (ProjectDetailAmountItem.ResultDataBean)  btn_item_amount.getTag();
            if(resultDataBean.getPkprogressitemid() == childBean.getPkprogressitemid()){
                btn_item_amount.setSelected(true);
                selectItemId = childBean.getPkprogressitemid();
            }else{
                btn_item_amount.setSelected(false);
            }
        }
    }

    public int getSelectProjectItemId(){
        return selectItemId;
    }

    public String getSelectTipText(){
        return tv_project_item_desc.getText().toString();
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
