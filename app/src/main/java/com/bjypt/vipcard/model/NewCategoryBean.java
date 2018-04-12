package com.bjypt.vipcard.model;

import com.bjypt.vipcard.utils.StringBytes;

/**
 * Created by Administrator on 2016/12/14.
 */
public class NewCategoryBean {
    private String pkhomeapplicationcategory;
    private String category_name;
    private String category_icon;
    private String isjump;
    private String use_url;
    private String isentry;

    public String getIsentry() {
        return isentry;
    }

    public void setIsentry(String isentry) {
        this.isentry = isentry;
    }

    public String getUse_url() {
        return use_url;
    }

    public void setUse_url(String use_url) {
        this.use_url = use_url;
    }

    public String getIsjump() {
        return isjump;
    }

    public void setIsjump(String isjump) {
        this.isjump = isjump;
    }

    public String getPkhomeapplicationcategory() {
        return pkhomeapplicationcategory;
    }

    public void setPkhomeapplicationcategory(String pkhomeapplicationcategory) {
        this.pkhomeapplicationcategory = pkhomeapplicationcategory;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_icon() {
        return category_icon;
    }

    public void setCategory_icon(String category_icon) {
        this.category_icon = category_icon;
    }
}
