package com.bjypt.vipcard.activity.crowdfunding.projectdetail.entity;

public class CfOrderInfoDataBean {

    /**
     * msg : SUCCESS
     * resultStatus : 0
     * resultData : {"incomeAmount":"42.189","orderid":"248948931537343046403","outOrderId":"248948931537343046403","payAmount":"10000","payAway":1,"payDate":"2018-09-19 15:44:06","projectName":"到期返本息，周期7天","settleEndAt":"2018-09-26 15:44:06"}
     */

    private String msg;
    private int resultStatus;
    private ResultDataBean resultData;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(int resultStatus) {
        this.resultStatus = resultStatus;
    }

    public ResultDataBean getResultData() {
        return resultData;
    }

    public void setResultData(ResultDataBean resultData) {
        this.resultData = resultData;
    }

    public static class ResultDataBean {
        /**
         * incomeAmount : 42.189
         * orderid : 248948931537343046403
         * outOrderId : 248948931537343046403
         * payAmount : 10000
         * payAway : 1
         * payDate : 2018-09-19 15:44:06
         * projectName : 到期返本息，周期7天
         * settleEndAt : 2018-09-26 15:44:06
         */

        private String incomeAmount;
        private String orderid;
        private String outOrderId;
        private String payAmount;
        private int payAway;
        private String payDate;
        private String projectName;
        private String settleEndAt;

        private Integer order_status;

        public Integer getOrder_status() {
            return order_status;
        }

        public void setOrder_status(Integer order_status) {
            this.order_status = order_status;
        }

        public String getIncomeAmount() {
            return incomeAmount;
        }

        public void setIncomeAmount(String incomeAmount) {
            this.incomeAmount = incomeAmount;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getOutOrderId() {
            return outOrderId;
        }

        public void setOutOrderId(String outOrderId) {
            this.outOrderId = outOrderId;
        }

        public String getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(String payAmount) {
            this.payAmount = payAmount;
        }

        public int getPayAway() {
            return payAway;
        }

        public void setPayAway(int payAway) {
            this.payAway = payAway;
        }

        public String getPayDate() {
            return payDate;
        }

        public void setPayDate(String payDate) {
            this.payDate = payDate;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getSettleEndAt() {
            return settleEndAt;
        }

        public void setSettleEndAt(String settleEndAt) {
            this.settleEndAt = settleEndAt;
        }
    }
}
