package com.bjypt.vipcard.assistant;


import com.bjypt.vipcard.model.ProductListBean;

/**
 * 购物车添加接口回调
 */
public interface onCallBackListener {
    /**
     * Type表示添加或减少
     * @param product
     * @param type
     */
    void updateProduct(ProductListBean product, String type);
}
