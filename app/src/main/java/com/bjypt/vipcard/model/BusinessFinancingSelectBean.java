package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 崔龙 on 2018/1/19.
 */

public class BusinessFinancingSelectBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : [{"finance_amount":100,"handsel_amount":10,"handsel_desc":"赠送10石油金"},{"finance_amount":500,"handsel_amount":80,"handsel_desc":"赠送80石油金"},{"finance_amount":1000,"handsel_amount":200,"handsel_desc":"赠送200石油金"},{"finance_amount":2000,"handsel_amount":500,"handsel_desc":"赠送500石油金"},{"finance_amount":10000,"handsel_amount":2000,"handsel_desc":"赠送2000石油金"}]
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
         * finance_amount : 100
         * handsel_amount : 10
         * handsel_desc : 赠送10石油金
         */

        private String finance_amount;
        private String handsel_amount;
        private String handsel_desc;

        public String getFinance_amount() {
            return finance_amount;
        }

        public void setFinance_amount(String finance_amount) {
            this.finance_amount = finance_amount;
        }

        public String getHandsel_amount() {
            return handsel_amount;
        }

        public void setHandsel_amount(String handsel_amount) {
            this.handsel_amount = handsel_amount;
        }

        public String getHandsel_desc() {
            return handsel_desc;
        }

        public void setHandsel_desc(String handsel_desc) {
            this.handsel_desc = handsel_desc;
        }
    }
}
