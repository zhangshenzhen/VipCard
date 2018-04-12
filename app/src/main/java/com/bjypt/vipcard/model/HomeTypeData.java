package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/5
 * Use by
 */
public class HomeTypeData {
    private String url;
    private String name;

    public HomeTypeData(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "HomeTypeData{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
