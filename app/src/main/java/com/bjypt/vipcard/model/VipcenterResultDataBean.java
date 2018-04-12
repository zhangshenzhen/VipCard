package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/4/20.
 */
public class VipcenterResultDataBean {

    private String resultStatus;
    private String msg;
    private VipcenterResultDataOne resultData;

    public VipcenterResultDataOne getResultData() {
        return resultData;
    }

    public void setResultData(VipcenterResultDataOne resultData) {
        this.resultData = resultData;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class VipcenterResultDataOne {
        private String balance;  //可用余额
        private String totalinterest;//总收益
        private String interest;  //昨日收益
        private String wholebalance;//余额
        private String discount;//折扣
        private String grade;//会员等级
        private Double historytotalinterest;//总收益

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getTotalinterest() {
            return totalinterest;
        }

        public void setTotalinterest(String totalinterest) {
            this.totalinterest = totalinterest;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public String getWholebalance() {
            return wholebalance;
        }

        public void setWholebalance(String wholebalance) {
            this.wholebalance = wholebalance;
        }

        public Double getHistorytotalinterest() {
            return historytotalinterest;
        }

        public void setHistorytotalinterest(Double historytotalinterest) {
            this.historytotalinterest = historytotalinterest;
        }
    }
}
