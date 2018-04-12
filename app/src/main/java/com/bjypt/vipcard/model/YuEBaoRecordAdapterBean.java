package com.bjypt.vipcard.model;

/**
 * Created by liyunte on 2017/1/9.
 */

public class YuEBaoRecordAdapterBean {
     private String yearLv;
    private String days;
    private String startTime;
    private String endTime;
    private String buyprice;
    private String shouyi;
    private int progress;
    private String isEnd;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYearLv() {
        return yearLv;
    }

    public void setYearLv(String yearLv) {
        this.yearLv = yearLv;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBuyprice() {
        return buyprice;
    }

    public void setBuyprice(String buyprice) {
        this.buyprice = buyprice;
    }

    public String getShouyi() {
        return shouyi;
    }

    public void setShouyi(String shouyi) {
        this.shouyi = shouyi;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(String isEnd) {
        this.isEnd = isEnd;
    }
}
