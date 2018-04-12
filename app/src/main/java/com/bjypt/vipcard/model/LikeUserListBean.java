package com.bjypt.vipcard.model;

/**
 * Created by 崔龙 on 2017/9/29.
 */

public class LikeUserListBean {

    /**
     * uid : 18
     * username : 17625921150
     * email : 17625921150@hyb.com
     * avatar : http://47.93.79.174/base_discuz/uc_server/avatar.php?uid=18&size=small
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
