package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/21
 * Use by 点赞商品的模型类
 */
public class LikeProduct {


    private List<likeProduct> likeProduct;

    public List<LikeProduct.likeProduct> getLikeProduct() {
        return likeProduct;
    }

    public void setLikeProduct(List<LikeProduct.likeProduct> likeProduct) {
        this.likeProduct = likeProduct;
    }

    public class likeProduct {
        private String productId;
        private int detailType;
        private int orderCount;

        public int getOrderCount() {
            return orderCount;
        }

        public void setOrderCount(int orderCount) {
            this.orderCount = orderCount;
        }


        public likeProduct(String productId, int detailType, int orderCount) {
            this.productId = productId;
            this.orderCount = orderCount;
            this.detailType = detailType;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public int getDetailType() {
            return detailType;
        }

        public void setDetailType(int detailType) {
            this.detailType = detailType;
        }
    }
}
