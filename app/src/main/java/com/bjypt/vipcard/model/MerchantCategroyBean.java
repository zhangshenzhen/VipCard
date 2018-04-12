package com.bjypt.vipcard.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/28.
 */
public class MerchantCategroyBean implements Serializable{
    private String merchantName;
    private String merchantPhoto;
    private String jianjie;
    private String vipzuan;
    private String youhui;
    private int pingjia;
    private String juli;
    private String Zhekou;
    private String yuanjai;
    private int startLevel;
    private String maxDiscount;
    private String gradeNum;

    public MerchantCategroyBean(String merchantName, String merchantPhoto, String jianjie, String vipzuan, String youhui, int pingjia, String juli, String zhekou, String yuanjai,int startLevel,String maxDiscount) {
        this.merchantName = merchantName;
        this.merchantPhoto = merchantPhoto;
        this.jianjie = jianjie;
        this.vipzuan = vipzuan;
        this.youhui = youhui;
        this.pingjia = pingjia;
        this.juli = juli;
        Zhekou = zhekou;
        this.yuanjai = yuanjai;
        this.startLevel =startLevel;
        this.maxDiscount = maxDiscount;
    }

    public String getGradeNum() {
        return gradeNum;
    }

    public void setGradeNum(String gradeNum) {
        this.gradeNum = gradeNum;
    }

    public String getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(String maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public int getStartLevel() {
        return startLevel;
    }

    public void setStartLevel(int startLevel) {
        this.startLevel = startLevel;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantPhoto() {
        return merchantPhoto;
    }

    public void setMerchantPhoto(String merchantPhoto) {
        this.merchantPhoto = merchantPhoto;
    }

    public String getJianjie() {
        return jianjie;
    }

    public void setJianjie(String jianjie) {
        this.jianjie = jianjie;
    }

    public String getVipzuan() {
        return vipzuan;
    }

    public void setVipzuan(String vipzuan) {
        this.vipzuan = vipzuan;
    }

    public String getYouhui() {
        return youhui;
    }

    public void setYouhui(String youhui) {
        this.youhui = youhui;
    }

    public int getPingjia() {
        return pingjia;
    }

    public void setPingjia(int pingjia) {
        this.pingjia = pingjia;
    }

    public String getJuli() {
        return juli;
    }

    public void setJuli(String juli) {
        this.juli = juli;
    }

    public String getZhekou() {
        return Zhekou;
    }

    public void setZhekou(String zhekou) {
        Zhekou = zhekou;
    }

    public String getYuanjai() {
        return yuanjai;
    }

    public void setYuanjai(String yuanjai) {
        this.yuanjai = yuanjai;
    }
}
