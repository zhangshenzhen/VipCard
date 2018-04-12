package com.bjypt.vipcard.model;

/**
 * Created by 崔龙 on 2018/1/3.
 */

public class FinancingInfoBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"status":true,"msg":"购买成功！","doinginvestmoney":0,"doingincomemoney":0,"doneinvestmoney":0,"doneincomemoney":0}
     */

    private int resultStatus;
    private String msg;
    private ResultDataBean resultData;

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

    public ResultDataBean getResultData() {
        return resultData;
    }

    public void setResultData(ResultDataBean resultData) {
        this.resultData = resultData;
    }

    public static class ResultDataBean {
        /**
         * status : true
         * msg : 购买成功！
         * doinginvestmoney : 0.0
         * doingincomemoney : 0.0
         * doneinvestmoney : 0.0
         * doneincomemoney : 0.0
         */

        private boolean status;
        private String msg;
        private double doinginvestmoney;
        private double doingincomemoney;
        private double doneinvestmoney;
        private double doneincomemoney;
        private double balance;

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public double getDoinginvestmoney() {
            return doinginvestmoney;
        }

        public void setDoinginvestmoney(double doinginvestmoney) {
            this.doinginvestmoney = doinginvestmoney;
        }

        public double getDoingincomemoney() {
            return doingincomemoney;
        }

        public void setDoingincomemoney(double doingincomemoney) {
            this.doingincomemoney = doingincomemoney;
        }

        public double getDoneinvestmoney() {
            return doneinvestmoney;
        }

        public void setDoneinvestmoney(double doneinvestmoney) {
            this.doneinvestmoney = doneinvestmoney;
        }

        public double getDoneincomemoney() {
            return doneincomemoney;
        }

        public void setDoneincomemoney(double doneincomemoney) {
            this.doneincomemoney = doneincomemoney;
        }
    }
}
