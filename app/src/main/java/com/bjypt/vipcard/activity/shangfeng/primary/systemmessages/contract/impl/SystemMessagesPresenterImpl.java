package com.bjypt.vipcard.activity.shangfeng.primary.systemmessages.contract.impl;

import com.bjypt.vipcard.activity.shangfeng.base.impl.BasePresenterImpl;
import com.bjypt.vipcard.activity.shangfeng.common.enums.ResultCode;
import com.bjypt.vipcard.activity.shangfeng.data.RequestCallBack;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MeassagePageBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MessageBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.ResultDataBean;
import com.bjypt.vipcard.activity.shangfeng.primary.systemmessages.contract.SystemMessagesContract;
import com.bjypt.vipcard.activity.shangfeng.util.OkHttpUtil;
import com.bjypt.vipcard.common.Config;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dell on 2018/5/14.
 */

public class SystemMessagesPresenterImpl extends BasePresenterImpl<SystemMessagesContract.View> implements SystemMessagesContract.Presenter {


    @Override
    public void onStart() {

    }

    @Override
    public void cancelRequest() {

    }

    @Override
    public void getMessages(String userid, String business_code, int pageNum, int pageSize) {
        mView.showProgress();
        final Map<String, String> mMap = new LinkedHashMap<>();
        mMap.put("userid", userid);
        mMap.put("business_code", business_code);
        mMap.put("pageNum", String.valueOf(pageNum));
        mMap.put("pageSize", String.valueOf(pageSize));

        OkHttpUtil.postUrl(new RequestCallBack<ResultDataBean>() {
            @Override
            public void success(int resultCode, ResultDataBean response) {
                if (mView != null) {
                    if(ResultCode.SUCCESS == response.getResultStatus() && response.getResultData() != null) {
                        Gson gson = new Gson();
                        MeassagePageBean meassagePageBean = gson.fromJson(gson.toJson(response.getResultData()), MeassagePageBean.class);
                        List<MessageBean> messageBeans = gson.fromJson(gson.toJson(meassagePageBean.getList()), new TypeToken<ArrayList<MessageBean>>() {
                        }.getType());
                        Logger.d(meassagePageBean);
                        mView.showMessageList(resultCode, messageBeans);
                    }else{
                        mView.showMessageList(resultCode, null);
                    }
                    mView.hideProgress();
                }
            }

            @Override
            public void onError(int resultCode, String errorMsg) {
                if (mView != null) {
                    mView.showMessageList(-1, null);
                    mView.hideProgress();
                }
            }
        }, Config.web.MESSAGES_URL, mMap);


    }


}
