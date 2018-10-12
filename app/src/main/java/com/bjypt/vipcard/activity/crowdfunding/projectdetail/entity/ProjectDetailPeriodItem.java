package com.bjypt.vipcard.activity.crowdfunding.projectdetail.entity;

import java.util.List;

public class ProjectDetailPeriodItem {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : [{"pkprogressdurationid":1,"pkprojectid":133,"interestRate":5,"durationTitle":"1个月","settledAt":1539076850000,"createAt":1539076857000},{"pkprogressdurationid":2,"pkprojectid":133,"interestRate":5,"durationTitle":"2个月","settledAt":1542187346000,"createAt":1539076947000}]
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
         * pkprogressdurationid : 1
         * pkprojectid : 133
         * interestRate : 5.0
         * durationTitle : 1个月
         * settledAt : 1539076850000//结算日期
         * createAt : 1539076857000
         */

        private int pkprogressdurationid;
        private int pkprojectid;
        private double interestRate;
        private String durationTitle;
        private long settledAt;
        private long createAt;

        public int getPkprogressdurationid() {
            return pkprogressdurationid;
        }

        public void setPkprogressdurationid(int pkprogressdurationid) {
            this.pkprogressdurationid = pkprogressdurationid;
        }

        public int getPkprojectid() {
            return pkprojectid;
        }

        public void setPkprojectid(int pkprojectid) {
            this.pkprojectid = pkprojectid;
        }

        public double getInterestRate() {
            return interestRate;
        }

        public void setInterestRate(double interestRate) {
            this.interestRate = interestRate;
        }

        public String getDurationTitle() {
            return durationTitle;
        }

        public void setDurationTitle(String durationTitle) {
            this.durationTitle = durationTitle;
        }

        public long getSettledAt() {
            return settledAt;
        }

        public void setSettledAt(long settledAt) {
            this.settledAt = settledAt;
        }

        public long getCreateAt() {
            return createAt;
        }

        public void setCreateAt(long createAt) {
            this.createAt = createAt;
        }
    }
}
