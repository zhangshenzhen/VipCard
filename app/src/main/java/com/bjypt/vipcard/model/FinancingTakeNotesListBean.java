package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 崔龙 on 2018/1/4.
 */

public class FinancingTakeNotesListBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
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
         * expiredate : 2018-01-14
         * incomemoney : 0.025
         * investdate : 2018-01-04
         * investid : dba7e0664b064903801982636f0963fd
         * investmoney : 15.0
         * investstatus : 1
         * pkproduct : 22222222
         * pkregister : 7a2f26fca4194961aa9c48e87dc55624
         * processdate : 2018-01-14
         * productday : 10
         * productname : 余额宝2
         * productrate : 6.0
         * totalmoney : 15.025
         * progress : 0
         */

        private String expiredate;
        private String incomemoney;
        private String investdate;
        private String investid;
        private String investmoney;
        private int investstatus;
        private String pkproduct;
        private String pkregister;
        private String processdate;
        private String productday;
        private String productname;
        private String productrate;
        private String totalmoney;
        private String progress;
        private String pkmuser;
        private String handsel_money;
        private int money_whereabout;
        private String money_whereabout_desc;
        private String handsel_money_name;

        public String getHandsel_money_name() {
            return handsel_money_name;
        }

        public void setHandsel_money_name(String handsel_money_name) {
            this.handsel_money_name = handsel_money_name;
        }

        public String getPkmuser() {
            return pkmuser;
        }

        public void setPkmuser(String pkmuser) {
            this.pkmuser = pkmuser;
        }

        public String getMoney_whereabout_desc() {
            return money_whereabout_desc;
        }

        public void setMoney_whereabout_desc(String money_whereabout_desc) {
            this.money_whereabout_desc = money_whereabout_desc;
        }

        public int getMoney_whereabout() {
            return money_whereabout;
        }

        public void setMoney_whereabout(int money_whereabout) {
            this.money_whereabout = money_whereabout;
        }

        public String getHandsel_money() {
            return handsel_money;
        }

        public void setHandsel_money(String handsel_money) {
            this.handsel_money = handsel_money;
        }

        public String getExpiredate() {
            return expiredate;
        }

        public void setExpiredate(String expiredate) {
            this.expiredate = expiredate;
        }

        public String getIncomemoney() {
            return incomemoney;
        }

        public void setIncomemoney(String incomemoney) {
            this.incomemoney = incomemoney;
        }

        public String getInvestdate() {
            return investdate;
        }

        public void setInvestdate(String investdate) {
            this.investdate = investdate;
        }

        public String getInvestid() {
            return investid;
        }

        public void setInvestid(String investid) {
            this.investid = investid;
        }

        public String getInvestmoney() {
            return investmoney;
        }

        public void setInvestmoney(String investmoney) {
            this.investmoney = investmoney;
        }

        public int getInveststatus() {
            return investstatus;
        }

        public void setInveststatus(int investstatus) {
            this.investstatus = investstatus;
        }

        public String getPkproduct() {
            return pkproduct;
        }

        public void setPkproduct(String pkproduct) {
            this.pkproduct = pkproduct;
        }

        public String getPkregister() {
            return pkregister;
        }

        public void setPkregister(String pkregister) {
            this.pkregister = pkregister;
        }

        public String getProcessdate() {
            return processdate;
        }

        public void setProcessdate(String processdate) {
            this.processdate = processdate;
        }

        public String getProductday() {
            return productday;
        }

        public void setProductday(String productday) {
            this.productday = productday;
        }

        public String getProductname() {
            return productname;
        }

        public void setProductname(String productname) {
            this.productname = productname;
        }

        public String getProductrate() {
            return productrate;
        }

        public void setProductrate(String productrate) {
            this.productrate = productrate;
        }

        public String getTotalmoney() {
            return totalmoney;
        }

        public void setTotalmoney(String totalmoney) {
            this.totalmoney = totalmoney;
        }

        public String getProgress() {
            return progress;
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }
    }
}
