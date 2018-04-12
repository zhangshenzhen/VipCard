package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/4/9.
 */
public class MerchantListBean {
    private String pkmuser;
    private String muname;
    private String logo;
    private String merdesc;
    private String discount;
    private String distance;
    private String startLevel;
    private String judgeAllNum;
    private String selledAllNum;
    private String couponWelfare;
    private String memberCount;
    private List<ContentBean> contentBeans;
    private String maxDiscount;//最高享受8.5这
    private String consumeRedPackage;//消费最高送10元红包
    private String rechargeRedPackage;//充值最高送1元红包
    private String rechargeWelfare;//充20送5元
    private String registRedPackage;//首次注册送1元红包
    private String integral;
    private String hybcoin;
    private String isfirst;
    private String specialPrice;
    private String rechargeActivity;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRechargeActivity() {
        return rechargeActivity;
    }

    public void setRechargeActivity(String rechargeActivity) {
        this.rechargeActivity = rechargeActivity;
    }

    private int activitysSum;
    private int flag;



    public String getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(String specialPrice) {
        this.specialPrice = specialPrice;
    }

    public String getIsfirst() {
        return isfirst;
    }

    public void setIsfirst(String isfirst) {
        this.isfirst = isfirst;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getHybcoin() {
        return hybcoin;
    }

    public void setHybcoin(String hybcoin) {
        this.hybcoin = hybcoin;
    }

    public int getActivitysSum() {
        return activitysSum;
    }

    public void setActivitysSum(int activitysSum) {
        this.activitysSum = activitysSum;
    }

    public String getConsumeRedPackage() {
        return consumeRedPackage;
    }

    public void setConsumeRedPackage(String consumeRedPackage) {
        this.consumeRedPackage = consumeRedPackage;
    }

    public String getRechargeRedPackage() {
        return rechargeRedPackage;
    }

    public void setRechargeRedPackage(String rechargeRedPackage) {
        this.rechargeRedPackage = rechargeRedPackage;
    }

    public String getRegistRedPackage() {
        return registRedPackage;
    }

    public void setRegistRedPackage(String registRedPackage) {
        this.registRedPackage = registRedPackage;
    }

    public List<ContentBean> getContentBeans() {
        return contentBeans;
    }

    public void setContentBeans(List<ContentBean> contentBeans) {
        this.contentBeans = contentBeans;
    }

    public String getCouponWelfare() {
        return couponWelfare;
    }

    public void setCouponWelfare(String couponWelfare) {
        this.couponWelfare = couponWelfare;
    }

    public String getRechargeWelfare() {
        return rechargeWelfare;
    }

    public void setRechargeWelfare(String rechargeWelfare) {
        this.rechargeWelfare = rechargeWelfare;
    }

    public String getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(String maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public String getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(String memberCount) {
        this.memberCount = memberCount;
    }

    public String getSelledAllNum() {
        return selledAllNum;
    }

    public void setSelledAllNum(String selledAllNum) {
        this.selledAllNum = selledAllNum;
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

    public String getMerdesc() {
        return merdesc;
    }

    public void setMerdesc(String merdesc) {
        this.merdesc = merdesc;
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

    public String getStartLevel() {
        return startLevel;
    }

    public void setStartLevel(String startLevel) {
        this.startLevel = startLevel;
    }

    public String getJudgeAllNum() {
        return judgeAllNum;
    }

    public void setJudgeAllNum(String judgeAllNum) {
        this.judgeAllNum = judgeAllNum;
    }
}
