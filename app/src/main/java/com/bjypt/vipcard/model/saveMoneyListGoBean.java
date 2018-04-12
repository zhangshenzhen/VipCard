package com.bjypt.vipcard.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/26.
 */
public class saveMoneyListGoBean implements Serializable {
    private String abatetime;
    private String begintime;
    private String endtime;
    private String purchaserules;
    private String moreservices;
    private String saveMoney;
    private String effectiveTime;
    public String getAbatetime() {
        return abatetime;
    }

    public void setAbatetime(String abatetime) {
        this.abatetime = abatetime;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getPurchaserules() {
        return purchaserules;
    }

    public void setPurchaserules(String purchaserules) {
        this.purchaserules = purchaserules;
    }

    public String getMoreservices() {
        return moreservices;
    }

    public void setMoreservices(String moreservices) {
        this.moreservices = moreservices;
    }

    public String getSaveMoney() {
        return saveMoney;
    }

    public void setSaveMoney(String saveMoney) {
        this.saveMoney = saveMoney;
    }

    public String getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }
}
