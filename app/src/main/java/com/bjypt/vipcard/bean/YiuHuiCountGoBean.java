package com.bjypt.vipcard.bean;

public class YiuHuiCountGoBean {


    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"canUseCoupon":4,"offlineCoupon":1}
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
         * canUseCoupon : 4
         * offlineCoupon : 1
         */

        private int canUseCoupon;
        private int offlineCoupon;

        public int getCanUseCoupon() {
            return canUseCoupon;
        }

        public void setCanUseCoupon(int canUseCoupon) {
            this.canUseCoupon = canUseCoupon;
        }

        public int getOfflineCoupon() {
            return offlineCoupon;
        }

        public void setOfflineCoupon(int offlineCoupon) {
            this.offlineCoupon = offlineCoupon;
        }
    }
}
