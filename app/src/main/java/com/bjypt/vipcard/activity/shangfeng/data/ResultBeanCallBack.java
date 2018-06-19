package com.bjypt.vipcard.activity.shangfeng.data;

import com.bjypt.vipcard.activity.shangfeng.data.bean.ResultDataBean;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by Administrator on 2018/5/4.
 */

public abstract class ResultBeanCallBack extends Callback<ResultDataBean>{
    @Override
    public ResultDataBean parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        ResultDataBean resultDataBean = new Gson().fromJson(string, ResultDataBean.class);
        return resultDataBean;
    }
}
