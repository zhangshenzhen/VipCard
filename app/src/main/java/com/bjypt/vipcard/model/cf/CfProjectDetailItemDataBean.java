package com.bjypt.vipcard.model.cf;

import java.math.BigDecimal;

public class CfProjectDetailItemDataBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"pkprogressitemid":21,"pkprojectid":11,"itemAmount":0.01,"itemCount":100,"itemDesc":"测试","saleCount":0,"gift":null,"tips":null,"explain":null,"checkBankNo":true}
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
         * pkprogressitemid : 21
         * pkprojectid : 11
         * itemAmount : 0.01
         * itemCount : 100
         * itemDesc : 测试
         * saleCount : 0
         * gift : null
         * tips : null
         * explain : null
         * checkBankNo : true
         */

        private int pkprogressitemid;
        private int pkprojectid;
        private BigDecimal itemAmount;
        private int itemCount;
        private String itemDesc;
        private int saleCount;
        private String gift;
        private String tips;
        private String explain;
        private boolean checkBankNo;

        public int getPkprogressitemid() {
            return pkprogressitemid;
        }

        public void setPkprogressitemid(int pkprogressitemid) {
            this.pkprogressitemid = pkprogressitemid;
        }

        public int getPkprojectid() {
            return pkprojectid;
        }

        public void setPkprojectid(int pkprojectid) {
            this.pkprojectid = pkprojectid;
        }

        public BigDecimal getItemAmount() {
            return itemAmount;
        }

        public void setItemAmount(BigDecimal itemAmount) {
            this.itemAmount = itemAmount;
        }

        public int getItemCount() {
            return itemCount;
        }

        public void setItemCount(int itemCount) {
            this.itemCount = itemCount;
        }

        public String getItemDesc() {
            return itemDesc;
        }

        public void setItemDesc(String itemDesc) {
            this.itemDesc = itemDesc;
        }

        public int getSaleCount() {
            return saleCount;
        }

        public void setSaleCount(int saleCount) {
            this.saleCount = saleCount;
        }

        public String getGift() {
            return gift;
        }

        public void setGift(String gift) {
            this.gift = gift;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public boolean isCheckBankNo() {
            return checkBankNo;
        }

        public void setCheckBankNo(boolean checkBankNo) {
            this.checkBankNo = checkBankNo;
        }
    }
}
