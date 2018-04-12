package com.bjypt.vipcard.model;

/**
 * Created by 崔龙 on 2018/1/18.
 */

public class PetroleumBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"finance_url":"https://www.baidu.com/img/searchcraft_43b6045b3249aeccf3ccb42e838ba6b0.png"}
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
         * finance_url : https://www.baidu.com/img/searchcraft_43b6045b3249aeccf3ccb42e838ba6b0.png
         */

        private String finance_url;

        public String getFinance_url() {
            return finance_url;
        }

        public void setFinance_url(String finance_url) {
            this.finance_url = finance_url;
        }
    }
}
