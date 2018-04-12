package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */

public class NewMerchantListBean {

    private String pkmuser;
    private String muname;
    private String logo;
    private String discount;
    private String distance;
    private String address;
    private String integral;
    private String linkage_pkdealer;
    private String merdesc;
    private List<ContentBean> contentBeans;
    private int flag;

    private String rechargeActivity;

    private String virtualActivity;  //消费立返积分
    private String firstConsume;     //首单立减
    private String consumeReduction; //消费立减

    public String getRechargeActivity() {
        return rechargeActivity;
    }

    public void setRechargeActivity(String rechargeActivity) {
        this.rechargeActivity = rechargeActivity;
    }

    public String getConsumeReduction() {
        return consumeReduction;
    }

    public void setConsumeReduction(String consumeReduction) {
        this.consumeReduction = consumeReduction;
    }

    public String getVirtualActivity() {
        return virtualActivity;
    }

    public void setVirtualActivity(String virtualActivity) {
        this.virtualActivity = virtualActivity;
    }

    public String getFirstConsume() {
        return firstConsume;
    }

    public void setFirstConsume(String firstConsume) {
        this.firstConsume = firstConsume;
    }

    public String getMerdesc() {
        return merdesc;
    }

    public void setMerdesc(String merdesc) {
        this.merdesc = merdesc;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public List<ContentBean> getContentBeans() {
        return contentBeans;
    }

    public void setContentBeans(List<ContentBean> contentBeans) {
        this.contentBeans = contentBeans;
    }

    public String getLinkage_pkdealer() {
        return linkage_pkdealer;
    }

    public void setLinkage_pkdealer(String linkage_pkdealer) {
        this.linkage_pkdealer = linkage_pkdealer;
    }

    public String getPkmuser() {
        return pkmuser;
    }

    public void setPkmuser(String pkmuser) {
        this.pkmuser = pkmuser;
    }

    public String getMuname() {
        return muname;
    }

    public void setMuname(String muname) {
        this.muname = muname;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }
}
