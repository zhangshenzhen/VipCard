package com.bjypt.vipcard.model;

import com.bjypt.vipcard.utils.StringBytes;

/**
 * Created by Administrator on 2016/4/19.
 */
public class VipcenterButtomBean {

    private String vipcenter_Name;//名
    private String vipcenter_Balance;//余额
    private String vipcenter_lastdayIncome;//昨日收益
    private String vipcenter_discount;//折扣
    private String vipcenter_photo;//图片
    private String pkmuser;
    private String distance;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPkmuser() {
        return pkmuser;
    }

    public void setPkmuser(String pkmuser) {
        this.pkmuser = pkmuser;
    }

    public String getVipcenter_discount() {
        return vipcenter_discount;
    }

    public void setVipcenter_discount(String vipcenter_discount) {
        this.vipcenter_discount = vipcenter_discount;
    }


    public String getVipcenter_photo() {
        return vipcenter_photo;
    }

    public void setVipcenter_photo(String vipcenter_photo) {
        this.vipcenter_photo = vipcenter_photo;
    }


    public String getVipcenter_Name() {
        return vipcenter_Name;
    }

    public void setVipcenter_Name(String vipcenter_Name) {
        this.vipcenter_Name = vipcenter_Name;
    }

    public String getVipcenter_Balance() {
        return vipcenter_Balance;
    }

    public void setVipcenter_Balance(String vipcenter_Balance) {
        this.vipcenter_Balance = vipcenter_Balance;
    }

    public String getVipcenter_lastdayIncome() {
        return vipcenter_lastdayIncome;
    }

    public void setVipcenter_lastdayIncome(String vipcenter_lastdayIncome) {
        this.vipcenter_lastdayIncome = vipcenter_lastdayIncome;
    }
}
