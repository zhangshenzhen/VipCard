package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/5
 * Use by 首页公告
 */
public class SysNoticeBean {
    private String adurl;//连接地址
    private String begin;
    private String count;
    private String current;
    private String page;
    private String pageLength;
    private String pkad;
    private String rows;
    private String slogan;
    private String total;
    private String totalrow;
    private String uuid;

    public void setAdurl(String adurl) {
        this.adurl = adurl;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setPageLength(String pageLength) {
        this.pageLength = pageLength;
    }

    public void setPkad(String pkad) {
        this.pkad = pkad;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setTotalrow(String totalrow) {
        this.totalrow = totalrow;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAdurl() {
        return adurl;
    }

    public String getBegin() {
        return begin;
    }

    public String getCount() {
        return count;
    }

    public String getCurrent() {
        return current;
    }

    public String getPage() {
        return page;
    }

    public String getPageLength() {
        return pageLength;
    }

    public String getPkad() {
        return pkad;
    }

    public String getRows() {
        return rows;
    }

    public String getSlogan() {
        return slogan;
    }

    public String getTotal() {
        return total;
    }

    public String getTotalrow() {
        return totalrow;
    }

    public String getUuid() {
        return uuid;
    }
}
