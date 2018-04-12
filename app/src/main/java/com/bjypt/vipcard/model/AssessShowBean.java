package com.bjypt.vipcard.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/30.
 */
public class AssessShowBean implements Serializable{
    private String img_url;
    private String username;
    private String content;
    private String time;
    private int star;
    private String merchantcontent;

    public AssessShowBean(String img_url, String username, String content, String time, int star, String merchantcontent) {
        this.img_url = img_url;
        this.username = username;
        this.content = content;
        this.time = time;
        this.star = star;
        this.merchantcontent = merchantcontent;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getMerchantcontent() {
        return merchantcontent;
    }

    public void setMerchantcontent(String merchantcontent) {
        this.merchantcontent = merchantcontent;
    }
}
