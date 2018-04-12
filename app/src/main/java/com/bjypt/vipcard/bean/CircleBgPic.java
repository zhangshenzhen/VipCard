package com.bjypt.vipcard.bean;

/**
 * Created by 崔龙 on 2017/11/28.
 */

public class CircleBgPic {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"picUrl":"http://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/userposition_69de1640987b4c6d96ad9cbdac3930f6.jpg"}
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
         * picUrl : http://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/userposition_69de1640987b4c6d96ad9cbdac3930f6.jpg
         */

        private String picUrl;
        private String signs;

        public String getSigns() {
            return signs;
        }

        public void setSigns(String signs) {
            this.signs = signs;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }
    }
}
