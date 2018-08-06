package com.bjypt.vipcard.model;

public class GetShareDataResultBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"HYB_SHARE_CONTENT":"点击下载繁城app,加油95折","HYB_LOGO":"http://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/userposition_72cd6e7d9ff34d2ba5bed0965d9dfb04.jpg","HYB_SHARE_SWITCH":"1","HYB_SHARE_TITLE":"繁城都市分享有礼","regist_url":"http://123.57.232.188:8080/hyb/ws/post/toRegistPage?userId=7a30cbe6695a4e239fe727fc1241889a"}
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
         * HYB_SHARE_CONTENT : 点击下载繁城app,加油95折
         * HYB_LOGO : http://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/userposition_72cd6e7d9ff34d2ba5bed0965d9dfb04.jpg
         * HYB_SHARE_SWITCH : 1
         * HYB_SHARE_TITLE : 繁城都市分享有礼
         * regist_url : http://123.57.232.188:8080/hyb/ws/post/toRegistPage?userId=7a30cbe6695a4e239fe727fc1241889a
         */

        private String HYB_SHARE_CONTENT;
        private String HYB_LOGO;
        private String HYB_SHARE_SWITCH;
        private String HYB_SHARE_TITLE;
        private String regist_url;

        public String getHYB_SHARE_CONTENT() {
            return HYB_SHARE_CONTENT;
        }

        public void setHYB_SHARE_CONTENT(String HYB_SHARE_CONTENT) {
            this.HYB_SHARE_CONTENT = HYB_SHARE_CONTENT;
        }

        public String getHYB_LOGO() {
            return HYB_LOGO;
        }

        public void setHYB_LOGO(String HYB_LOGO) {
            this.HYB_LOGO = HYB_LOGO;
        }

        public String getHYB_SHARE_SWITCH() {
            return HYB_SHARE_SWITCH;
        }

        public void setHYB_SHARE_SWITCH(String HYB_SHARE_SWITCH) {
            this.HYB_SHARE_SWITCH = HYB_SHARE_SWITCH;
        }

        public String getHYB_SHARE_TITLE() {
            return HYB_SHARE_TITLE;
        }

        public void setHYB_SHARE_TITLE(String HYB_SHARE_TITLE) {
            this.HYB_SHARE_TITLE = HYB_SHARE_TITLE;
        }

        public String getRegist_url() {
            return regist_url;
        }

        public void setRegist_url(String regist_url) {
            this.regist_url = regist_url;
        }
    }
}
