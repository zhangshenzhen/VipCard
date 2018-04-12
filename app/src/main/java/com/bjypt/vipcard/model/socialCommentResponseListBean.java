package com.bjypt.vipcard.model;

/**
 * Created by 崔龙 on 2017/9/29.
 */

public class socialCommentResponseListBean {


    /**
     * pid : 17
     * userResponse : {"uid":21,"username":"admin","email":"admin@hyb.com","avatar":"http://47.93.79.174/base_discuz/uc_server/avatar.php?uid=21&size=small"}
     * subject :
     * message : 当官的地方给对方个人股洱海风光大锅饭
     * dateline : 一天前
     * authorid : 21
     */

    private int pid;
    private String subject;
    private String message;
    private String dateline;

    public String getVirtualBalance() {
        return virtualBalance;
    }

    public void setVirtualBalance(String virtualBalance) {
        this.virtualBalance = virtualBalance;
    }

    private String virtualBalance;
    private int authorid;
    private UserResponseBean userInfo;
    private UserResponseBean repliedInfo;

    public UserResponseBean getRepliedInfo() {
        return repliedInfo;
    }

    public void setRepliedInfo(UserResponseBean repliedInfo) {
        this.repliedInfo = repliedInfo;
    }

    public UserResponseBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserResponseBean userInfo) {
        this.userInfo = userInfo;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

//    public static class UserResponseBean {
//        /**
//         * uid : 21
//         * username : admin
//         * email : admin@hyb.com
//         * avatar : http://47.93.79.174/base_discuz/uc_server/avatar.php?uid=21&size=small
//         */
//
//        private int uid;
//        private String username;
//        private String email;
//        private String avatar;
//
//        public int getUid() {
//            return uid;
//        }
//
//        public void setUid(int uid) {
//            this.uid = uid;
//        }
//
//        public String getUsername() {
//            return username;
//        }
//
//        public void setUsername(String username) {
//            this.username = username;
//        }
//
//        public String getEmail() {
//            return email;
//        }
//
//        public void setEmail(String email) {
//            this.email = email;
//        }
//
//        public String getAvatar() {
//            return avatar;
//        }
//
//        public void setAvatar(String avatar) {
//            this.avatar = avatar;
//        }
//    }
}
