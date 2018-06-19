package com.bjypt.vipcard.activity.shangfeng.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 商家列表Bean
 */
public class MerchantListBean implements Parcelable {
    /**
     * 商家主键
     */
    private String pkmuser;
    /**
     * 商家店名
     */
    private String muname;
    /**
     * 商家缩略图url
     */
    private String logo;

    /**
     * 订单状态
     * 0：已经预约下单
     */
    private Integer order_status;
    /**
     * 距离
     */
    private String distance;
    /**
     * 商家地址
     */
    private String address;
    /**
     * 商家电话
     */
    private String phone;
    /**
     * 经度
     */
    private double longitude;
    /**
     * 纬度
     */
    private double latitude;

    /**
     * 商家活动内容
     */
    private String activity_content;
    /**
     * 折扣
     */
    private String fee;
    /**
     * 商家信息(简介)
     */
    private String merdesc;

    public MerchantListBean(String pkmuser, String muname, String logo, Integer order_status, String distance, String address, String phone, double longitude, double latitude, String activity_content, String fee, String merdesc) {
        this.pkmuser = pkmuser;
        this.muname = muname;
        this.logo = logo;
        this.order_status = order_status;
        this.distance = distance;
        this.address = address;
        this.phone = phone;
        this.longitude = longitude;
        this.latitude = latitude;
        this.activity_content = activity_content;
        this.fee = fee;
        this.merdesc = merdesc;
    }

    public Integer getOrder_status() {
        return order_status;
    }

    public void setOrder_status(Integer order_status) {
        this.order_status = order_status;
    }

    public String getPkmuser() {
        return pkmuser;
    }

    public void setPkmuser(String pkmuser) {
        this.pkmuser = pkmuser;
    }

    public String getMuname() {
        return muname;
    }

    public void setMuname(String muname) {
        this.muname = muname;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getActivity_content() {
        return activity_content;
    }

    public void setActivity_content(String activity_content) {
        this.activity_content = activity_content;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getMerdesc() {
        return merdesc;
    }

    public void setMerdesc(String merdesc) {
        this.merdesc = merdesc;
    }

    @Override
    public String toString() {
        return "MerchantListBean{" +
                "pkmuser='" + pkmuser + '\'' +
                ", muname='" + muname + '\'' +
                ", logo='" + logo + '\'' +
                ", order_status=" + order_status +
                ", distance='" + distance + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", activity_content='" + activity_content + '\'' +
                ", fee='" + fee + '\'' +
                ", merdesc='" + merdesc + '\'' +
                '}';
    }

    protected MerchantListBean(Parcel in) {
        pkmuser = in.readString();
        muname = in.readString();
        logo = in.readString();
        order_status = in.readInt();
        distance = in.readString();
        address = in.readString();
        phone = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        activity_content = in.readString();
        fee = in.readString();
        merdesc = in.readString();
    }

    public static final Creator<MerchantListBean> CREATOR = new Creator<MerchantListBean>() {
        @Override
        public MerchantListBean createFromParcel(Parcel in) {
            return new MerchantListBean(in);
        }

        @Override
        public MerchantListBean[] newArray(int size) {
            return new MerchantListBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(pkmuser);
        parcel.writeString(muname);
        parcel.writeString(logo);
        if (order_status == null) {
            order_status = new Integer(3);
        }
        parcel.writeInt(order_status);
        parcel.writeString(distance);
        parcel.writeString(address);
        parcel.writeString(phone);
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
        parcel.writeString(activity_content);
        parcel.writeString(fee);
        parcel.writeString(merdesc);
    }


}
