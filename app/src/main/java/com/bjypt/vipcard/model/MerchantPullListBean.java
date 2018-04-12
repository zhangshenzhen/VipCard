package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2017/3/27.
 */

public class MerchantPullListBean {

    private int resultStatus;
    private String msg;
    private List<ResultDataBean> resultData;

    public int getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(int resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ResultDataBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<ResultDataBean> resultData) {
        this.resultData = resultData;
    }

    @Override
    public String toString() {
        return "MerchantPullListBean{" +
                "resultStatus=" + resultStatus +
                ", msg='" + msg + '\'' +
                ", resultData=" + resultData +
                '}';
    }

    public static class ResultDataBean {
        private String pkmuser;
        private String muname;
        private String logo;
        private String merdesc;
        private String discount;
        private String distance;
        private String startLevel;
        private String judgeAllNum;
        private String selledAllNum;
        private String rechargeWelfare;
        private String maxDiscount;
        private String memberCount;
        private String registRedPackage;
        private String consumeRedPackage;
        private String rechargeRedPackage;
        private String hybcoin;
        private String isfirst;
        private String specialPrice;
        private String rechargeActivity;
        private String address;

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

        public String getSelledAllNum() {
            return selledAllNum;
        }

        public void setSelledAllNum(String selledAllNum) {
            this.selledAllNum = selledAllNum;
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

        public String getRegistRedPackage() {
            return registRedPackage;
        }

        public void setRegistRedPackage(String registRedPackage) {
            this.registRedPackage = registRedPackage;
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

        public String getHybcoin() {
            return hybcoin;
        }

        public void setHybcoin(String hybcoin) {
            this.hybcoin = hybcoin;
        }

        public String getIsfirst() {
            return isfirst;
        }

        public void setIsfirst(String isfirst) {
            this.isfirst = isfirst;
        }

        public String getSpecialPrice() {
            return specialPrice;
        }

        public void setSpecialPrice(String specialPrice) {
            this.specialPrice = specialPrice;
        }

        public String getRechargeActivity() {
            return rechargeActivity;
        }

        public void setRechargeActivity(String rechargeActivity) {
            this.rechargeActivity = rechargeActivity;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "ResultDataBean{" +
                    "pkmuser='" + pkmuser + '\'' +
                    ", muname='" + muname + '\'' +
                    ", logo='" + logo + '\'' +
                    ", merdesc='" + merdesc + '\'' +
                    ", discount='" + discount + '\'' +
                    ", distance='" + distance + '\'' +
                    ", startLevel='" + startLevel + '\'' +
                    ", judgeAllNum='" + judgeAllNum + '\'' +
                    ", selledAllNum='" + selledAllNum + '\'' +
                    ", rechargeWelfare='" + rechargeWelfare + '\'' +
                    ", maxDiscount='" + maxDiscount + '\'' +
                    ", memberCount='" + memberCount + '\'' +
                    ", registRedPackage='" + registRedPackage + '\'' +
                    ", consumeRedPackage='" + consumeRedPackage + '\'' +
                    ", rechargeRedPackage='" + rechargeRedPackage + '\'' +
                    ", hybcoin='" + hybcoin + '\'' +
                    ", isfirst='" + isfirst + '\'' +
                    ", specialPrice='" + specialPrice + '\'' +
                    ", rechargeActivity='" + rechargeActivity + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }
}
