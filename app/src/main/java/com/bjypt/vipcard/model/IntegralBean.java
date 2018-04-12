package com.bjypt.vipcard.model;

/**
 * Created by 崔龙 on 2017/12/13.
 */

public class IntegralBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"virtualBalance":"0"}
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
         * virtualBalance : 0
         */

        private String virtualBalance;

        public String getVirtualBalance() {
            return virtualBalance;
        }

        public void setVirtualBalance(String virtualBalance) {
            this.virtualBalance = virtualBalance;
        }
    }
}
