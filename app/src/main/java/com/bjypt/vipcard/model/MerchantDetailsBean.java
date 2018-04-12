package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/7
 * Use by 商家详情内部信息模型类
 */
public class MerchantDetailsBean {
    private String muname;//商家名称
    private String imgnum;//图片数量
    private String headpkmuser;//是否存在分店
    private String address;//地址
    private String longitude;
    private CouponMap couponMap;

    public CouponMap getCouponMap() {
        return couponMap;
    }

    public void setCouponMap(CouponMap couponMap) {
        this.couponMap = couponMap;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    private String latitude;
    private String judgeAllNum;//评价数量
    private String phone;//电话号码
    private String preorderStartPrice;//最小预约金额
    private String selledAllNum;//已售数量
    private String startLevel;//星级
    private String pkmuser;
    private String logo;
    private String rechargeActivity;

    public String getHeadpkmuser() {
        return headpkmuser;
    }

    public void setHeadpkmuser(String headpkmuser) {
        this.headpkmuser = headpkmuser;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPkmuser() {
        return pkmuser;
    }

    public void setPkmuser(String pkmuser) {
        this.pkmuser = pkmuser;
    }

    private List<ProductTypeListBean> productTypeList;//菜品分类

    public String getMuname() {
        return muname;
    }

    public void setMuname(String muname) {
        this.muname = muname;
    }

    public String getImgnum() {
        return imgnum;
    }

    public void setImgnum(String imgnum) {
        this.imgnum = imgnum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getJudgeAllNum() {
        return judgeAllNum;
    }

    public void setJudgeAllNum(String judgeAllNum) {
        this.judgeAllNum = judgeAllNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPreorderStartPrice() {
        return preorderStartPrice;
    }

    public void setPreorderStartPrice(String preorderStartPrice) {
        this.preorderStartPrice = preorderStartPrice;
    }

    public String getSelledAllNum() {
        return selledAllNum;
    }

    public void setSelledAllNum(String selledAllNum) {
        this.selledAllNum = selledAllNum;
    }

    public String getStartLevel() {
        return startLevel;
    }

    public void setStartLevel(String startLevel) {
        this.startLevel = startLevel;
    }

    public List<ProductTypeListBean> getProductTypeList() {
        return productTypeList;
    }

    public void setProductTypeList(List<ProductTypeListBean> productTypeList) {
        this.productTypeList = productTypeList;
    }

    public String getRechargeActivity() {
        return rechargeActivity;
    }

    public void setRechargeActivity(String rechargeActivity) {
        this.rechargeActivity = rechargeActivity;
    }
}
