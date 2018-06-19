package com.bjypt.vipcard.activity.shangfeng.data.bean;

/**
 * 天气 Bean
 */
public class WeatherBean {

    private String city;
    /**
     * 城市编码
     */
    private String citycode;
    /**
     * 日期
     */
    private String date;
    /**
     * 星期
     */
    private String week;
    /**
     * 天气
     */
    private String weather;
    /**
     * 天气图标
     */
    private String img;
    /**
     * 风
     */
    private String winddirect;
    /**
     * 温度
     */
    private String temp;
    /**
     * 空气质量对象
     */
    private Quality aqi;

    public WeatherBean(String city, String citycode, String date, String week, String weather, String img, String winddirect, String temp, Quality aqi) {
        this.city = city;
        this.citycode = citycode;
        this.date = date;
        this.week = week;
        this.weather = weather;
        this.img = img;
        this.winddirect = winddirect;
        this.temp = temp;
        this.aqi = aqi;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getWinddirect() {
        return winddirect;
    }

    public void setWinddirect(String winddirect) {
        this.winddirect = winddirect;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public Quality getAqi() {
        return aqi;
    }

    public void setAqi(Quality aqi) {
        this.aqi = aqi;
    }

    class Quality{
        /**
         * 空气质量
         */
        private String quality;

        public Quality(String quality) {
            this.quality = quality;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }
    }




}
