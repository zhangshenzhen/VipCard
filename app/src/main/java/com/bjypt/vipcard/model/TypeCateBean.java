package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/12/14 0014.
 */

public class TypeCateBean {
    private List<SmallTypeData> homeApplicationSubCategoryList;
    private List<SmallTypeData> homeApplicationRecommendList;

    public List<SmallTypeData> getHomeApplicationRecommendList() {
        return homeApplicationRecommendList;
    }

    public void setHomeApplicationRecommendList(List<SmallTypeData> homeApplicationRecommendList) {
        this.homeApplicationRecommendList = homeApplicationRecommendList;
    }

    public List<SmallTypeData> getHomeApplicationSubCategoryList() {
        return homeApplicationSubCategoryList;
    }

    public void setHomeApplicationSubCategoryList(List<SmallTypeData> homeApplicationSubCategoryList) {
        this.homeApplicationSubCategoryList = homeApplicationSubCategoryList;
    }


}
