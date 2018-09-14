package com.bjypt.vipcard.adapter.cf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.cf.base.BaseHomeCrowdfundingProject;
import com.bjypt.vipcard.utils.DensityUtil;
import com.sinia.orderlang.utils.AppInfoUtil;

public class HomeCrowdfundingRecommendAdapter extends BaseHomeCrowdfundingProject {

    public HomeCrowdfundingRecommendAdapter(Context context) {
        super(context);
    }

    @Override
    public void initSize(Context context) {
        int width = AppInfoUtil.getScreenWidth(context);
        icon_width = (width - DensityUtil.dip2px(context, 37)) / 2;
        icon_height = (int) (icon_width / 1.24);
    }

    @Override
    public View getItemView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_crowdfuning_item_recommend, null);
    }


}
