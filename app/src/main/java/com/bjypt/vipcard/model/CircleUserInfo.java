package com.bjypt.vipcard.model;

/**
 * Created by 崔龙 on 2017/10/10.
 */

public class CircleUserInfo {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"uid":26,"username":"飞翔企鹅","email":"17625921150@hyb.com","avatar":"https://dafuyang.oss-cn-beijing.aliyuncs.com/userposition_5158828730494bea990fabf70d20d540.jpg"}
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
         * uid : 26
         * username : 飞翔企鹅
         * email : 17625921150@hyb.com
         * avatar : https://dafuyang.oss-cn-beijing.aliyuncs.com/userposition_5158828730494bea990fabf70d20d540.jpg
         */

        private int uid;
        private String username;
        private String email;
        private String avatar;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
