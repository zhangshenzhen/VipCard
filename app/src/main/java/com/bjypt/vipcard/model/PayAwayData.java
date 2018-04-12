package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by admin on 2017/4/26.
 */

public class PayAwayData {
    private int shownum;
    private List<PayAway> payTypeList;

    public int getShownum() {
        return shownum;
    }

    public void setShownum(int shownum) {
        this.shownum = shownum;
    }

    public List<PayAway> getPayTypeList() {
        return payTypeList;
    }

    public void setPayTypeList(List<PayAway> payTypeList) {
        this.payTypeList = payTypeList;
    }
}
