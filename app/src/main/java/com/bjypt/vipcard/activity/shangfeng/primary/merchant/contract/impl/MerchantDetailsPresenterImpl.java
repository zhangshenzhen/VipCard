package com.bjypt.vipcard.activity.shangfeng.primary.merchant.contract.impl;

import com.bjypt.vipcard.activity.shangfeng.base.impl.BasePresenterImpl;
import com.bjypt.vipcard.activity.shangfeng.common.EventBusMessageEvent;
import com.bjypt.vipcard.activity.shangfeng.common.enums.EventBusWhat;
import com.bjypt.vipcard.activity.shangfeng.common.enums.ResultCode;
import com.bjypt.vipcard.activity.shangfeng.data.RequestCallBack;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MerchantListBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.ResultDataBean;
import com.bjypt.vipcard.activity.shangfeng.primary.merchant.contract.MerchantDetailsContract;
import com.bjypt.vipcard.activity.shangfeng.util.OkHttpUtil;
import com.bjypt.vipcard.activity.shangfeng.util.ToastUtils;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.model.GetShareDataResultBean;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 商家详情 实现类
 */
public class MerchantDetailsPresenterImpl extends BasePresenterImpl<MerchantDetailsContract.View> implements MerchantDetailsContract.Presenter {


    @Override
    public void onStart() {
    }

    @Override
    public void cancelRequest() {

    }

    @Override
    public void getMerchantData(String pkmuser,String pkregister, String price) {
        mView.showProgress();
        Map<String, String> map = new LinkedHashMap<>();
        map.put("pkmuser", pkmuser);
        map.put("pkregister", pkregister);
        map.put("price", price);
        OkHttpUtil.postUrl(new RequestCallBack<ResultDataBean>() {
            @Override
            public void success(int resultCode, ResultDataBean data) {
                if (mView != null) {
                    Gson gson = new Gson();
                    MerchantListBean merchantListBean = gson.fromJson(gson.toJson(data.getResultData()), MerchantListBean.class);
                    mView.initMerchantDetails(merchantListBean);

                    mView.hideProgress();
                }
            }

            @Override
            public void onError(int resultCode, String errorMsg) {
                if (mView != null) {
                    mView.hideProgress();
                }

            }
        }, Config.web.MERCHANT_PARTICULARS_URL, map);
    }

    @Override
    public void bookingOrder(String pkregister, String pkmuser, String consume_amount) {
        mView.showProgress();
        Map<String, String> map = new LinkedHashMap<>();
        map.put("pkregister", pkregister);
        map.put("pkmuser", pkmuser);
        map.put("consume_amount", consume_amount);

        OkHttpUtil.postUrl(new RequestCallBack<ResultDataBean>() {
            @Override
            public void success(int resultCode, ResultDataBean data) {
                if (mView != null) {
                    if (ResultCode.SUCCESS == data.getResultStatus()) {
                        mView.updateView();
                        EventBusMessageEvent event = new EventBusMessageEvent();
                        event.setWhat(EventBusWhat.NewOrder);
                        EventBus.getDefault().post(event);
                        ToastUtils.showToastBottom("下单成功");
                    }
                    mView.hideProgress();
                }
            }

            @Override
            public void onError(int resultCode, String errorMsg) {
                if (mView != null) {
                    mView.hideProgress();
                }

            }
        }, Config.web.BOOKING_ORDER_URL, map);
    }

    @Override
    public void getPayShareData(String pkregister) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("userId", pkregister);
        OkHttpUtil.postUrl(new RequestCallBack<ResultDataBean>() {
            @Override
            public void success(int resultCode, ResultDataBean data) {
                Gson gson = new Gson();
                GetShareDataResultBean getShareDataResultBean = gson.fromJson(gson.toJson(data.getResultData()), GetShareDataResultBean.class);
                mView.payShareData(getShareDataResultBean);
            }

            @Override
            public void onError(int resultCode, String errorMsg) {

            }
        }, Config.web.PayShareData, map);
    }

}
