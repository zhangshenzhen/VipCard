package com.bjypt.vipcard.bean;

/**
 * Created by Dell on 2018/3/31.
 */

public class WeatherBean {

    // 温度
    private String air_temperature;
    // 日期
    private String date;
    // 空气质量
    private String air_quality;
    // 图片
    private String imageUrl;

    public WeatherBean() {
    }

    public WeatherBean(String air_temperature, String date, String air_quality, String imageUrl) {
        this.air_temperature = air_temperature;
        this.date = date;
        this.air_quality = air_quality;
        this.imageUrl = imageUrl;
    }

    public String getAir_temperature() {
        return air_temperature;
    }

    public void setAir_temperature(String air_temperature) {
        this.air_temperature = air_temperature;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAir_quality() {
        return air_quality;
    }

    public void setAir_quality(String air_quality) {
        this.air_quality = air_quality;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
