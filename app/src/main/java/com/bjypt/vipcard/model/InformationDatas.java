package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2017/7/26.
 */

public class InformationDatas {
    private String waterUrl;
    private String waterRate;
    private String phoneRate;
    private String phoneUrl;
    private String immediateUrl;

    public String getImmediateUrl() {
        return immediateUrl;
    }

    public void setImmediateUrl(String immediateUrl) {
        this.immediateUrl = immediateUrl;
    }

    public String getWaterUrl() {
        return waterUrl;
    }

    public void setWaterUrl(String waterUrl) {
        this.waterUrl = waterUrl;
    }

    public String getWaterRate() {
        return waterRate;
    }

    public void setWaterRate(String waterRate) {
        this.waterRate = waterRate;
    }

    public String getPhoneRate() {
        return phoneRate;
    }

    public void setPhoneRate(String phoneRate) {
        this.phoneRate = phoneRate;
    }

    public String getPhoneUrl() {
        return phoneUrl;
    }

    public void setPhoneUrl(String phoneUrl) {
        this.phoneUrl = phoneUrl;
    }
}
