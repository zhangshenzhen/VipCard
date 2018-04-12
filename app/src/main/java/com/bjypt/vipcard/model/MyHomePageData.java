package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2017/11/24.
 */

public class MyHomePageData {
    private int favid;
    private int id;
    private String title;
    private int aid;
    private int year;
    private int month;
    private String time;
    private int type;
    private List<AttachmentListData> attachmentList;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getFavid() {
        return favid;
    }

    public void setFavid(int favid) {
        this.favid = favid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public List<AttachmentListData> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<AttachmentListData> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MyHomePageData{" +
                "favid=" + favid +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", aid=" + aid +
                ", year=" + year +
                ", month=" + month +
                ", type=" + type +
                ", attachmentList=" + attachmentList +
                '}';
    }
}
