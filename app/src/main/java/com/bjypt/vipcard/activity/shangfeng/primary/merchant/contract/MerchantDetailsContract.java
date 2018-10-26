package com.bjypt.vipcard.activity.shangfeng.primary.merchant.contract;


import com.bjypt.vipcard.activity.shangfeng.base.BaseContract;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MerchantListBean;
import com.bjypt.vipcard.model.GetShareDataResultBean;

/**
 * 商家详情 契约类
 */

public interface MerchantDetailsContract {

    interface View extends BaseContract.View {
        /**
         * 初始化商家详情
         * @param merchantListBean
         */
        void initMerchantDetails(MerchantListBean merchantListBean);

        void updateView();

        void payShareData(GetShareDataResultBean shareDataResultBean);

    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 获取商家详情数据
         * @param pkmuser
         */
        void getMerchantData(String pkmuser, String pkregister, String price);

        /**
         * 预约下单
         * @param pkregister
         * @param pkmuser
         * @param consume_amount  订单金额
         */
        void bookingOrder(String pkregister, String pkmuser, String consume_amount);
        /**
         * 扫码支付时分享提示信息
         * @param pkregister
         */
        void getPayShareData(String pkregister);
    }

}
