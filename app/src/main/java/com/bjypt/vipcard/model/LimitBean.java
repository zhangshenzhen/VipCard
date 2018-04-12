package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2017/12/14.
 */

public class LimitBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"start":1,"end":100000}
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
         * start : 1
         * end : 100000
         */

        private String start;
        private String end;
        private String tip_start;
        private String tip_end;

        public String getTip_start() {
            return tip_start;
        }

        public void setTip_start(String tip_start) {
            this.tip_start = tip_start;
        }

        public String getTip_end() {
            return tip_end;
        }

        public void setTip_end(String tip_end) {
            this.tip_end = tip_end;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String  start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }
    }
}
