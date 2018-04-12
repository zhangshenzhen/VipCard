package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 崔龙 on 2017/12/8.
 */

public class SelectMoneyBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : [{"recharge_amount":10,"handsel_amount":2},{"recharge_amount":50,"handsel_amount":12},{"recharge_amount":100,"handsel_amount":20},{"recharge_amount":200,"handsel_amount":50},{"recharge_amount":500,"handsel_amount":200}]
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
         * recharge_amount : 10
         * handsel_amount : 2
         */

        private String recharge_amount;
        private String handsel_amount;

        public String getRecharge_amount() {
            return recharge_amount;
        }

        public void setRecharge_amount(String recharge_amount) {
            this.recharge_amount = recharge_amount;
        }

        public String getHandsel_amount() {
            return handsel_amount;
        }

        public void setHandsel_amount(String handsel_amount) {
            this.handsel_amount = handsel_amount;
        }
    }
}
