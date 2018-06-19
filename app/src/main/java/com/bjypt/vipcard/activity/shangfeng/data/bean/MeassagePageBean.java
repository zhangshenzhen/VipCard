package com.bjypt.vipcard.activity.shangfeng.data.bean;

/**
 * 消息列表 分页Bean
 */
public class MeassagePageBean {

    private String total;

    private String pages;

    private Object list;

    public MeassagePageBean(String total, String pages, Object list) {
        this.total = total;
        this.pages = pages;
        this.list = list;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public Object getList() {
        return list;
    }

    public void setList(Object list) {
        this.list = list;
    }
}
