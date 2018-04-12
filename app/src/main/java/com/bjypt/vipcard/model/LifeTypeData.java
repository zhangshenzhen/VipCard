package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/12/13 0013.
 */

public class LifeTypeData {
    private String category_icon;
    private String category_name;
    private String pkhomeapplicationcategory;
    private String subcategorysize;
    private List<SmallTypeData> homeApplicationSubCategoryList;

    public String getCategory_icon() {
        return category_icon;
    }

    public void setCategory_icon(String category_icon) {
        this.category_icon = category_icon;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getPkhomeapplicationcategory() {
        return pkhomeapplicationcategory;
    }

    public void setPkhomeapplicationcategory(String pkhomeapplicationcategory) {
        this.pkhomeapplicationcategory = pkhomeapplicationcategory;
    }

    public String getSubcategorysize() {
        return subcategorysize;
    }

    public void setSubcategorysize(String subcategorysize) {
        this.subcategorysize = subcategorysize;
    }

    public List<SmallTypeData> getHomeApplicationSubCategoryList() {
        return homeApplicationSubCategoryList;
    }

    public void setHomeApplicationSubCategoryList(List<SmallTypeData> homeApplicationSubCategoryList) {
        this.homeApplicationSubCategoryList = homeApplicationSubCategoryList;
    }
}
