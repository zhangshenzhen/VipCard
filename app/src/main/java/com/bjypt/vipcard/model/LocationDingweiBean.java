package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/5/25
 * Use by
 */
public class LocationDingweiBean {
    private double mLng;
    private double mLat;
    private String cityCode;
    private String adress;
    private String cityAdress;

    public String getCityAdress() {
        return cityAdress;
    }

    public void setCityAdress(String cityAdress) {
        this.cityAdress = cityAdress;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public double getmLng() {
        return mLng;
    }

    public void setmLng(double mLng) {
        this.mLng = mLng;
    }

    public double getmLat() {
        return mLat;
    }

    public void setmLat(double mLat) {
        this.mLat = mLat;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Override
    public String toString() {
        return "LocationDingweiBean{" +
                "mLng=" + mLng +
                ", mLat=" + mLat +
                ", cityCode='" + cityCode + '\'' +
                ", adress='" + adress + '\'' +
                ", cityAdress='" + cityAdress + '\'' +
                '}';
    }
}
