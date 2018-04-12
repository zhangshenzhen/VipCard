package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2017/11/22.
 */

public class AttentionBean {


    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : [{"bkname":null,"status":null,"mutual":null,"followUser":{"uid":21,"username":"admin","email":"admin@hyb.com","avatar":null}}]
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
         * bkname : null
         * status : null
         * mutual : null
         * followUser : {"uid":21,"username":"admin","email":"admin@hyb.com","avatar":null}
         */

        private String bkname;
        private String status;
        private String mutual;
        private FollowUserBean followUser;

        public String getBkname() {
            return bkname;
        }

        public void setBkname(String bkname) {
            this.bkname = bkname;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMutual() {
            return mutual;
        }

        public void setMutual(String mutual) {
            this.mutual = mutual;
        }

        public FollowUserBean getFollowUser() {
            return followUser;
        }

        public void setFollowUser(FollowUserBean followUser) {
            this.followUser = followUser;
        }

        public static class FollowUserBean {
            /**
             * uid : 21
             * username : admin
             * email : admin@hyb.com
             * avatar : null
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
}
