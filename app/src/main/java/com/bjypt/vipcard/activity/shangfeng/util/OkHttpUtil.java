package com.bjypt.vipcard.activity.shangfeng.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import com.bjypt.vipcard.activity.shangfeng.common.enums.ResultCode;
import com.bjypt.vipcard.activity.shangfeng.data.BaseRequestCallBack;
import com.bjypt.vipcard.activity.shangfeng.data.RequestCallBack;
import com.bjypt.vipcard.activity.shangfeng.data.ResultBeanCallBack;
import com.bjypt.vipcard.activity.shangfeng.data.bean.ResultDataBean;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeoutException;

import okhttp3.Call;

public class OkHttpUtil {

    /**
     * GET请求
     * @param callBack
     * @param contetx
     * @param url
     * @param mMap
     */
    public static void getUrl(final RequestCallBack callBack, Context contetx, String url, Map<String, String> mMap) {
        GetBuilder getBuilder = OkHttpUtils.get().url(url);

        if (null != mMap) {
            for (String key : mMap.keySet()) {
                getBuilder.addParams(key, mMap.get(key));
            }
        }
        getBuilder.tag(contetx).build()
                .execute(new ResultBeanCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (e instanceof TimeoutException || e instanceof SocketTimeoutException
                                || e instanceof ConnectException) {
                            callBack.onError(id, "网络连接异常，请检查");
                        } else {
                            callBack.onError(id, e.getMessage());
                        }
                        Logger.e("resultStatus = " + id + " ; " + "Exception = " + e.getMessage());
                    }

                    @Override
                    public void onResponse(ResultDataBean response, int id) {
                        if (null != response) {
                            switch (response.getResultStatus()) {
                                case ResultCode.SUCCESS: // 成功
                                    callBack.success(id, response);
                                    break;
                            }
                        }

                    }
                });
    }

    /**
     * POST请求
     * @param callBack
     * @param url
     * @param mMap
     * @return
     */
    public static RequestCall postUrl(final RequestCallBack callBack, final String url, Map<String, String> mMap) {
        LogUtils.print("网络请求地址：" + url);
        if (null != mMap)
            LogUtils.print("网络请求参数：" + new Gson().toJson(mMap));
        PostFormBuilder postFormBuilder = OkHttpUtils.post().url(url);

        if (null != mMap) {
            for (String key : mMap.keySet()) {
                postFormBuilder.addParams(key, mMap.get(key));
            }
        }
        String versionCode = ApplicationUtils.getVersionName(MyApplication.getContext());
        int versionInt = ApplicationUtils.getVersionCode(MyApplication.getContext());
        String deviceId = "";
        TelephonyManager tm = (TelephonyManager) MyApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (MyApplication.getContext().checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            deviceId = tm.getDeviceId();
        }
        postFormBuilder.addHeader("cpuid", deviceId);
        postFormBuilder.addHeader("versionCode", versionCode);
        postFormBuilder.addHeader( "versionName", versionCode);
        postFormBuilder.addHeader("pkregister", SharedPreferenceUtils.getFromSharedPreference(MyApplication.getContext(), Config.userConfig.pkregister));
        postFormBuilder.addHeader("versionInt", AES.encrypt(versionInt + "", AES.key));
        if(mMap != null && mMap.size()>0){
            postFormBuilder.addHeader("sign",  AES.createSign(new TreeMap<String, String>(mMap)));
        }

        final RequestCall requestCall = postFormBuilder.build();
        requestCall.execute(new BaseRequestCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (e instanceof TimeoutException || e instanceof SocketTimeoutException
                        || e instanceof ConnectException) {
                    callBack.onError(id, "网络连接异常，请检查");
                } else {

                    callBack.onError(id, e.getMessage());
                }
                Logger.e("resultStatus = " + id + " ; " + "Exception = " + e.getMessage());
            }

            @Override
            public void onResponse(ResultDataBean response, int id) {
                LogUtils.print("请求网络返回结果：" + new Gson().toJson(response));
                if (null != response) {
                    switch (response.getResultStatus()) {
                        case ResultCode.SUCCESS: // 成功
                            callBack.success(id, response);

                            break;
                        case ResultCode.HTTPSIGNERROR:
                            LogUtils.print(url+" 请求非法：" + response.getMsg());
                        default:
                            callBack.success(id, response);
                            break;
                    }

                }
            }
        });
        return requestCall;
    }

    /**
     * Post表单形式上传文件
     *
     * @param callBack
     * @param url
     * @param mMapFiles
     * @param mMapParams
     */
    public static void postFileUrl(final RequestCallBack callBack, String url, Map<String, String> mMapFiles, Map<String, String> mMapParams) {
        LogUtils.print("网络请求地址：" + url);

        PostFormBuilder postFormBuilder = OkHttpUtils.post();

        if (null != mMapFiles) {
            LogUtils.print("参数：" + new Gson().toJson(mMapFiles));
            for (String key : mMapFiles.keySet()) {
                String fileName = mMapFiles.get(key).substring(url.lastIndexOf("/") + 1);
                postFormBuilder.addFile(key, fileName,new File(mMapFiles.get(key)));
            }
        }

        postFormBuilder.url(url);

        if (null != mMapParams) {
            LogUtils.print("参数：" + new Gson().toJson(mMapParams));
            for (String key : mMapParams.keySet()) {
                postFormBuilder.addParams(key, mMapParams.get(key));
            }
        }

        String versionCode = ApplicationUtils.getVersionName(MyApplication.getContext());
        int versionInt = ApplicationUtils.getVersionCode(MyApplication.getContext());
        postFormBuilder.addHeader("versionName",  versionCode);
        postFormBuilder.addHeader("versionInt", String.valueOf(versionInt));

        if(mMapParams != null && mMapParams.size()>0){
            postFormBuilder.addHeader("sign",  AES.createSign(new TreeMap<String, String>(mMapParams)));
        }

        final RequestCall requestCall = postFormBuilder.build();
        requestCall.execute(new BaseRequestCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (e instanceof TimeoutException || e instanceof SocketTimeoutException
                        || e instanceof ConnectException) {
                    callBack.onError(id, "网络连接异常，请检查");
                } else {

                    callBack.onError(id, e.getMessage());
                }
                Logger.e("resultStatus = " + id + " ; " + "Exception = " + e.getMessage());
            }

            @Override
            public void onResponse(ResultDataBean response, int id) {
                LogUtils.print("请求网络返回结果：" + new Gson().toJson(response));
                if (null != response) {
                    switch (response.getResultStatus()) {
                        case ResultCode.SUCCESS: // 成功
                            callBack.success(id, response);

                            break;
                       default:
                            callBack.success(id, response);
                            break;
                    }

                }
            }
        });
    }




}
