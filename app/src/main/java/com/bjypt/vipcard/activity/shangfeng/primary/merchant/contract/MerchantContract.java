package com.bjypt.vipcard.activity.shangfeng.primary.merchant.contract;


import com.bjypt.vipcard.activity.shangfeng.base.BaseContract;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MerchantListBean;

import java.util.List;

/**
 * 商家详情 契约类
 */

public interface MerchantContract {

    interface View extends BaseContract.View {
        /**
         * 初始化商家列表
         * @param statusCode  状态码
         * @param merchantListBeans
         */
        void initMerchantList(int statusCode, List<MerchantListBean> merchantListBeans);

    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 获取商家类表
         * @param citycode  城市编码
         * @param tag  商家类型标签
         * @param price  预计消费金额
         * @param longitude  经度
         * @param latitude   纬度
         * @param pageNo  第几页
         * @param pageSize  每页条数
         * @param orderType  排序方式  0：离我最近 1：优惠最多 2：智能排序
         */
        void getMerchantsData(String citycode, String tag, String price,
                              String longitude, String latitude, int pageNo, int pageSize, int orderType);


    }

}
