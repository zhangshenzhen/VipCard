package com.bjypt.vipcard.activity.shangfeng.primary.merchant.contract.impl;

import com.bjypt.vipcard.activity.shangfeng.base.impl.BasePresenterImpl;
import com.bjypt.vipcard.activity.shangfeng.data.RequestCallBack;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MerchantListBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.PageBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.ResultDataBean;
import com.bjypt.vipcard.activity.shangfeng.primary.merchant.contract.MerchantListContract;
import com.bjypt.vipcard.activity.shangfeng.util.OkHttpUtil;
import com.bjypt.vipcard.common.Config;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 商家类表 实现类
 */
public class MerchantListPresenterImpl extends BasePresenterImpl<MerchantListContract.View> implements MerchantListContract.Presenter {


    @Override
    public void onStart() {

    }

    @Override
    public void cancelRequest() {

    }

    /**
     * 获取商家类表
     *
     * @param citycode  城市编码
     * @param tag       商家类型标签
     * @param price     预计消费金额
     * @param longitude 经度
     * @param latitude  纬度
     * @param pageNo    第几页
     * @param pageSize  每页条数
     * @param orderType 排序方式  0：离我最近 1：优惠最多 2：智能排序
     */
    @Override
    public void getMerchantsData(String citycode, String tag, String price, String longitude, String latitude, int pageNo, int pageSize, int orderType) {
        mView.showProgress();
        final Map<String, String> mMap = new LinkedHashMap<>();
        mMap.put("citycode", citycode);
        mMap.put("tag", tag);
        mMap.put("price", price);
        mMap.put("latitude", latitude);
        mMap.put("longitude", longitude);
        mMap.put("pageNo", String.valueOf(pageNo));
        mMap.put("pageSize", String.valueOf(pageSize));
        mMap.put("orderType", String.valueOf(orderType));

        OkHttpUtil.postUrl(new RequestCallBack<ResultDataBean>() {
            @Override
            public void success(int resultCode, ResultDataBean data) {
                if (mView != null) {
                    if (data != null) {
                        Gson gson = new Gson();
                        PageBean pageBean = gson.fromJson(gson.toJson(data.getResultData()), PageBean.class);
                        if (pageBean != null) {
                            ArrayList<MerchantListBean> merchantListBeans = gson.fromJson(gson.toJson(pageBean.getList()), new TypeToken<ArrayList<MerchantListBean>>() {
                            }.getType());
                            if (merchantListBeans != null && merchantListBeans.size() > 0) {
                                mView.initMerchantList(data.getResultStatus(), merchantListBeans);
                            }else{
                                mView.initMerchantList(1, null);
                            }
                        }

                    }
                    mView.hideProgress();
                }

            }

            @Override
            public void onError(int resultCode, String errorMsg) {
                if(mView != null){
                    mView.initMerchantList(-1, null);
                    mView.hideProgress();
                }
            }
        }, Config.web.MERCHANTS_URL, mMap);
    }
}
