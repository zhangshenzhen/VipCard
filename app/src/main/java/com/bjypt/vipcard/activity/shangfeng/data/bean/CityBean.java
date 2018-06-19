package com.bjypt.vipcard.activity.shangfeng.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 城市
 */
public class CityBean implements Parcelable {

    private String cityname; // 城市名称
    private String pinyin;  // 城市拼音
    private String citycode;  // 城市编码
    private String address; // 地址
    private double latitude; // 纬度
    private double longitude; // 经度

    public CityBean(String cityname, String pinyin, String citycode, String address, double latitude, double longitude) {
        this.cityname = cityname;
        this.pinyin = pinyin;
        this.citycode = citycode;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    protected CityBean(Parcel in) {
        cityname = in.readString();
        pinyin = in.readString();
        citycode = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<CityBean> CREATOR = new Creator<CityBean>() {
        @Override
        public CityBean createFromParcel(Parcel in) {
            return new CityBean(in);
        }

        @Override
        public CityBean[] newArray(int size) {
            return new CityBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(cityname);
        parcel.writeString(pinyin);
        parcel.writeString(citycode);
        parcel.writeString(address);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }

    @Override
    public String toString() {
        return "CityBean{" +
                "cityname='" + cityname + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", citycode='" + citycode + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
