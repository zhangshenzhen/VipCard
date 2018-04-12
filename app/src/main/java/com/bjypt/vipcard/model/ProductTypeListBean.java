package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/7
 * Use by 商家详情分类模型
 */
public class ProductTypeListBean {

    private String typename;//分类名称
    private String pktypeid;
    private List<ProductListBean> productList;//单个菜品
    private boolean isCheck;


    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public List<ProductListBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductListBean> productList) {
        this.productList = productList;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getPktypeid() {
        return pktypeid;
    }

    public void setPktypeid(String pktypeid) {
        this.pktypeid = pktypeid;
    }
}
