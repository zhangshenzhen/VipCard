package com.bjypt.vipcard.activity.crowdfunding.projectdetail.entity;

import java.math.BigDecimal;
import java.util.List;

public class ProjectDetailAmountItem {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : [{"pkprogressitemid":12,"pkprojectid":7,"itemAmount":300,"itemCount":100,"itemDesc":"剩余：100份,收益","saleCount":0,"gift":null,"tips":null,"explain":null,"checkBankNo":false},{"pkprogressitemid":14,"pkprojectid":7,"itemAmount":500,"itemCount":30,"itemDesc":"剩余：30份,预计收益","saleCount":0,"gift":null,"tips":null,"explain":null,"checkBankNo":false},{"pkprogressitemid":15,"pkprojectid":7,"itemAmount":800,"itemCount":80,"itemDesc":"剩余：80份,预计收益","saleCount":0,"gift":null,"tips":null,"explain":null,"checkBankNo":false}]
     */

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

    public static class ResultDataBean {
        /**
         * pkprogressitemid : 12
         * pkprojectid : 7
         * itemAmount : 300.0
         * itemCount : 100
         * itemDesc : 剩余：100份,收益
         * saleCount : 0
         * gift : null
         * tips : null
         * explain : null
         * checkBankNo : false
         */

        private int pkprogressitemid;
        private int pkprojectid;
        private BigDecimal itemAmount;
        private int itemCount;
        private String itemDesc;
        private int saleCount;
        private  String gift;
        private String tips;
        private String explain;
        private boolean checkBankNo;

        public BigDecimal getItemAmount() {
            return itemAmount;
        }

        public void setItemAmount(BigDecimal itemAmount) {
            this.itemAmount = itemAmount;
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

        public boolean isCheckBankNo() {
            return checkBankNo;
        }

        public void setCheckBankNo(boolean checkBankNo) {
            this.checkBankNo = checkBankNo;
        }
    }
}
