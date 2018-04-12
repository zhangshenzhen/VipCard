package com.bjypt.vipcard.model;

/**
 * Created by 崔龙 on 2017/3/28.
 */

public class DiplaybtnBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"linkage_pkdealer":"696a9c5179c04673a10188d0b3d86dfa","display":true}
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
         * linkage_pkdealer : 696a9c5179c04673a10188d0b3d86dfa
         * display : true
         */

        private String linkage_pkdealer;
        private boolean display;

        public String getLinkage_pkdealer() {
            return linkage_pkdealer;
        }

        public void setLinkage_pkdealer(String linkage_pkdealer) {
            this.linkage_pkdealer = linkage_pkdealer;
        }

        public boolean isDisplay() {
            return display;
        }

        public void setDisplay(boolean display) {
            this.display = display;
        }
    }
}
