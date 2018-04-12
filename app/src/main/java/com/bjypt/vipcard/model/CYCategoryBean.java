package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */

public class CYCategoryBean {
    private List<HomeASCategoryBean> homeApplicationSubCategoryList;
    private List<AdNewBean> hybAdvertisementList;
    private List<LunTan> otherApplicationList;

    public List<HomeASCategoryBean> getHomeApplicationSubCategoryList() {
        return homeApplicationSubCategoryList;
    }

    public void setHomeApplicationSubCategoryList(List<HomeASCategoryBean> homeApplicationSubCategoryList) {
        this.homeApplicationSubCategoryList = homeApplicationSubCategoryList;
    }

    public List<AdNewBean> getHybAdvertisementList() {
        return hybAdvertisementList;
    }

    public void setHybAdvertisementList(List<AdNewBean> hybAdvertisementList) {
        this.hybAdvertisementList = hybAdvertisementList;
    }

    public List<LunTan> getOtherApplicationList() {
        return otherApplicationList;
    }

    public void setOtherApplicationList(List<LunTan> otherApplicationList) {
        this.otherApplicationList = otherApplicationList;
    }
}
