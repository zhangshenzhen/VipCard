package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 崔龙 on 2017/3/16.
 */

public class CarouselBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : [{"msg":"充值满1.00即赠送1.00元"},{"msg":"充值满100.00即赠送10.00元"}]
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
         * msg : 充值满1.00即赠送1.00元
         */

        private String msg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
