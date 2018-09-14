package com.bjypt.vipcard.model.cf;

import java.util.List;

public class CfNoticeResultData {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : [{"app_id":63,"app_name":"繁城都市轻松生活","app_icon":"null","mtlevel":1,"parent_app_id":0,"city_code":"1558","isentry":0,"link_type":0}]
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
         * app_id : 63
         * app_name : 繁城都市轻松生活
         * app_icon : null
         * mtlevel : 1
         * parent_app_id : 0
         * city_code : 1558
         * isentry : 0
         * link_type : 0
         */

        private int app_id;
        private String app_name;
        private String app_icon;
        private int mtlevel;
        private int parent_app_id;
        private String city_code;
        private int isentry;
        private int link_type;

        public int getApp_id() {
            return app_id;
        }

        public void setApp_id(int app_id) {
            this.app_id = app_id;
        }

        public String getApp_name() {
            return app_name;
        }

        public void setApp_name(String app_name) {
            this.app_name = app_name;
        }

        public String getApp_icon() {
            return app_icon;
        }

        public void setApp_icon(String app_icon) {
            this.app_icon = app_icon;
        }

        public int getMtlevel() {
            return mtlevel;
        }

        public void setMtlevel(int mtlevel) {
            this.mtlevel = mtlevel;
        }

        public int getParent_app_id() {
            return parent_app_id;
        }

        public void setParent_app_id(int parent_app_id) {
            this.parent_app_id = parent_app_id;
        }

        public String getCity_code() {
            return city_code;
        }

        public void setCity_code(String city_code) {
            this.city_code = city_code;
        }

        public int getIsentry() {
            return isentry;
        }

        public void setIsentry(int isentry) {
            this.isentry = isentry;
        }

        public int getLink_type() {
            return link_type;
        }

        public void setLink_type(int link_type) {
            this.link_type = link_type;
        }
    }
}
