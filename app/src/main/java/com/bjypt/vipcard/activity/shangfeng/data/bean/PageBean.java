package com.bjypt.vipcard.activity.shangfeng.data.bean;

/**
 * 商家列表每页字段
 */
public class PageBean {
    /**
     * 当前页数
     */
    private int pageNum;
    /**
     * 一页几条数据
     */
    private int pageSize;
    /**
     * 总条数
     */
    private String total;
    /**
     * 总页数
     */
    private String pages;
    /**
     * 商家数组
     */
    private Object list;


    public PageBean(int pageNum, int pageSize, String total, String pages, Object list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.pages = pages;
        this.list = list;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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

    @Override
    public String toString() {
        return "PageBean{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", total='" + total + '\'' +
                ", pages='" + pages + '\'' +
                ", list=" + list +
                '}';
    }
}
