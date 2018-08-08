package com.bjypt.vipcard.activity.shangfeng.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.activity.shangfeng.common.LocateResultFields;
import com.bjypt.vipcard.activity.shangfeng.common.enums.ActionTypeEnum;
import com.bjypt.vipcard.activity.shangfeng.common.enums.LoginTypeEnum;
import com.bjypt.vipcard.activity.shangfeng.common.enums.ResultCode;
import com.bjypt.vipcard.activity.shangfeng.common.enums.UserInformationFields;
import com.bjypt.vipcard.activity.shangfeng.data.RequestCallBack;
import com.bjypt.vipcard.activity.shangfeng.data.bean.BannerBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.CommonWebData;
import com.bjypt.vipcard.activity.shangfeng.data.bean.ResultDataBean;
import com.bjypt.vipcard.activity.shangfeng.primary.commonweb.CommonWebActivity;
import com.bjypt.vipcard.activity.shangfeng.primary.merchant.ui.MerchantActivity;
import com.bjypt.vipcard.activity.shangfeng.primary.merchant.ui.MerchantDetailsActivity;
import com.bjypt.vipcard.activity.shangfeng.primary.pay.H5PayActivity;
import com.bjypt.vipcard.activity.shangfeng.primary.pay.ScanPayActivity;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.utils.ShareSDKUtil;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.widget.ShareBottomDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.sinia.orderlang.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by wanglei on 2018/5/17.
 */

public class ShangfengUriHelper {

    private static final String system_seheme = "shangfeng";
    private static final String ali_secheme = "alipays";

    private Context context;
    private boolean isLogin;

    private boolean is_login_callback;

    private OnSearchListener onSearchListener;

    public ShangfengUriHelper(Context context) {
        this.context = context;
    }

    /**
     * @param is_login_callback true 需要进入登录页面登录后， 直接跳转到下一个页面
     */

    public void startSearch(String uriString, boolean is_login_callback) {
        this.is_login_callback = is_login_callback;
        startSearch(uriString);
    }

    /**
     * 解析uri
     * shangfeng://merchant_search?tag=xxx  商家语音搜索
     * shangfeng://menu_action?tag=xxx      菜单、生活服务搜索
     * shangfeng://app/h5/pay               H5调用本地支付
     * shangfeng://app/h5/callbrowse        系统浏览器打开页面
     * shangfeng://app/h5/callapp           H5 app内部打开
     * shangfeng://app/h5/share             H5 分享
     * -------------------------title  分享标题
     * -------------------------content  分享内容
     * -------------------------linkurl   分享链接
     * ------------------------- shangfeng://app/h5/share?title=123&content=456&linkurl=httpbaidu.com
     * alipays://platformapi                支付宝
     */
    public void startSearch(String uriString) {
        Logger.v("uriString = " + uriString);
        boolean isLogin = "Y".equals(String.valueOf(SharedPreferencesUtils.get(UserInformationFields.IS_LOGIN, "N")));
        Uri uri = Uri.parse(uriString);
        String scheme = uri.getScheme();
        String part = uri.getSchemeSpecificPart();
        if (system_seheme.equals(scheme)) {
            if (part.startsWith("//app/merchant_search")) {
                String tag = uri.getQueryParameter("tag");
                goSearchMerchant(tag);
            } else if (part.startsWith("//app/menu_action")) {
                String tag = uri.getQueryParameter("tag");
                searchMenuAction(tag);
            } else if (part.startsWith("//app/h5/pay")) {
                H5PayActivity.callActivity(context, uriString);
            } else if (part.startsWith("//app/h5/callbrowse")) {
                callBrowse(uri);
            } else if (part.startsWith("//app/h5/share")) {
                ShareSDK.initSDK(context);
                shareUri(uri);
            } else if (part.startsWith("//app/h5/callapp")) {
                String page = uri.getQueryParameter("page");
                String is_login = uri.getQueryParameter("is_login");//默认不需要登录
                //需要登录，但是app 未登录
                if (StringUtils.isNotEmpty(is_login) && "Y".equalsIgnoreCase(is_login) && !isLogin) {
                    if (is_login_callback) {//登录之后直接跳转到支付页面
                        LoginActivity.callActivity(context, uriString, null);
                    } else {
                        LoginActivity.callActivity(context);
                    }
                }
                if (StringUtils.isEmpty(page) || "web".equalsIgnoreCase(page)) {
                    callCommonWeb(uri);
                } else if ("native".equalsIgnoreCase(page)) {
                    //todo 暂时没有可支持的页面
                    String natvie_url = uri.getQueryParameter("url");
                    if (StringUtils.isNotEmpty(natvie_url)) {
                        if (natvie_url.equalsIgnoreCase("merchantdetails")) {
                            String pkmuser = uri.getQueryParameter("pkmuser");
                            String fee = uri.getQueryParameter("fee");
                            String price = uri.getQueryParameter("price");
                            MerchantDetailsActivity.callActivity(context, pkmuser, price + "");
                        } else if (natvie_url.equalsIgnoreCase("scanpay")) {
                            String barcode = uri.getQueryParameter("barcode");
                            String dealtype = uri.getQueryParameter("dealtype");
                            if ("14".equals(dealtype)) {//智慧尚峰抢单，
                                dealtype = "15";//繁城都市抢单
                            }
                            ScanPayActivity.callActivity(context, dealtype, barcode);
                        }
                    }

                }
            } else {
                callOnSearchFailListener("暂时不支持此功能");
            }
        } else if (ali_secheme.equals(scheme)) {//支付宝
            callAliPay(uri);
        } else {
            callOnSearchFailListener("暂时不支持此功能");
        }
    }

    private void shareUri(Uri uri) {
        final String title = uri.getQueryParameter("title");
        final String content = uri.getQueryParameter("content");
        final String linkurl = uri.getQueryParameter("linkurl");
        if (StringUtil.isEmpty(title) || StringUtil.isEmpty(linkurl)) {
            ToastUtil.showToast(context, "分享参数错误title和linkurl都不能为空");
            return;
        }

        ShareBottomDialog dialog = new ShareBottomDialog(context);
        dialog.show();
        dialog.setClickListener(new ShareBottomDialog.ShareBtnListener() {
            @Override
            public void onWeixinFriendClick() {
                ShareSDKUtil.shareWechatContent(context, linkurl, MyApplication.mWxApi, 1, content, title);
            }

            @Override
            public void onWinxinFriendCircleClick() {
                ShareSDKUtil.shareWechatContent(context, linkurl, MyApplication.mWxApi, 2, content,title);
            }
        });
    }

    public boolean isContains(String url) {
        Uri uri = Uri.parse(url);
        String scheme = uri.getScheme();
        return system_seheme.equalsIgnoreCase(scheme) || ali_secheme.equalsIgnoreCase(scheme);
    }

    private void callBrowse(Uri uri) {
        String url = uri.getQueryParameter("url");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    private void callCommonWeb(Uri uri) {
        String url = uri.getQueryParameter("url");
        String title = uri.getQueryParameter("title");
        if (StringUtils.isEmpty(url)) {
            ToastUtils.showToast("暂时不支持此功能");
            return;
        }
        if (StringUtils.isEmpty(title)) title = "详情";
        String is_login = uri.getQueryParameter("is_login");//默认不需要登录
        if (StringUtils.isNotEmpty(is_login) && "Y".equalsIgnoreCase(is_login)) {
            if (!url.endsWith("&")) {
                url = url + "&";
            }
            if (!url.endsWith("pkregister=")) {
                url = url + "pkregister=";
            }
            url = url + SharedPreferencesUtils.get(UserInformationFields.USER_KEY, "");
            if (!url.endsWith("&phoneno=")) {
                url = url + "&phoneno=" + SharedPreferencesUtils.get(UserInformationFields.PHONE_NUMBER, "");
            }
        }
        CommonWebActivity.callActivity(context, new CommonWebData(url, title));
    }

    private void searchMenuAction(String tag) {
        Map<String, String> params = new HashMap<>();
        params.put("app_type", "5");
        params.put("app_name", tag);
        params.put("platform_type", "0");
        params.put("city_code", SharedPreferencesUtils.get(LocateResultFields.CITY_CODE, "") + "");
        OkHttpUtil.postUrl(new RequestCallBack<ResultDataBean>() {
            @Override
            public void success(int resultCode, ResultDataBean data) {
                Gson gson = new Gson();
                LogUtils.print(" searchMenuAction : " + gson.toJson(data));
                if (ResultCode.SUCCESS == data.getResultStatus()) {
                    BannerBean resultDataBean = gson.fromJson(gson.toJson(data.getResultData()), new TypeToken<BannerBean>() {
                    }.getType());
                    boolean result = onAppCategoryItemClick(resultDataBean);
                    if (result) {
                        callOnSearchSucessListener();
                    } else {
                        callOnSearchFailListener("暂未开通");
                    }
                } else {
                    callOnSearchFailListener("暂无服务，您可以试试");
                }
            }

            @Override
            public void onError(int resultCode, String errorMsg) {
                LogUtils.print("searchMenuAction : resultCode=" + resultCode + " errorMsg=" + errorMsg);
            }
        }, Config.web.GETONEMENU, params);
    }

    /**
     * @param appCategoryBean
     * @param is_login_callback true 需要进入登录页面登录后， 直接跳转到下一个页面
     * @return
     */
    public boolean onAppCategoryItemClick(BannerBean appCategoryBean, boolean is_login_callback) {
        this.is_login_callback = is_login_callback;
        return onAppCategoryItemClick(appCategoryBean);
    }

    /**
     * 设置点击事件
     *
     * @return
     */
    public boolean onAppCategoryItemClick(BannerBean appCategoryBean) {
        if (appCategoryBean != null) {
            isLogin = "Y".equals(String.valueOf(SharedPreferencesUtils.get(UserInformationFields.IS_LOGIN, "N")));
            if (appCategoryBean.getIsentry() == LoginTypeEnum.Login && !isLogin) {
                if (is_login_callback) {
                    LoginActivity.callActivity(context, null, appCategoryBean);
                } else {
                    LoginActivity.callActivity(context);
                }
                return false;
            }
            if (appCategoryBean.getLink_type() == ActionTypeEnum.H5) {
                String url = appCategoryBean.getLink_url();
                /**************处理URl 中 参数中? & 问题************************************/
                Uri webUri = Uri.parse(url);
                if (!webUri.getQueryParameterNames().isEmpty()) {//如果链接后面参数为空并且不是？结尾的，拼接？
                    if (!url.endsWith("&")) {
                        url = url + "&";
                    }
                }

                if (appCategoryBean.getIsentry() == LoginTypeEnum.Login) {
                    if (!url.endsWith("pkregister=")) {
                        url = url + "pkregister=";
                    }
                    url = url + SharedPreferencesUtils.get(UserInformationFields.USER_KEY, "");
                    if (!url.endsWith("&phoneno=")) {
                        url = url + "&phoneno=" + SharedPreferencesUtils.get(UserInformationFields.PHONE_NUMBER, "");
                    }
                }
                if (!url.endsWith("&")) {
                    url = url + "&";
                }
                url = url + "citycode=" + SharedPreferencesUtils.get(LocateResultFields.CITY_CODE, "");
                url = url + "&latitude=" + SharedPreferencesUtils.get(LocateResultFields.LOCATION_LATITUDE, "");
                url = url + "&longitude=" + SharedPreferencesUtils.get(LocateResultFields.LOCATION_LONGITUDE, "");
                if (appCategoryBean.getLink_url().contains("alipays://platformapi")) {
                    callAliPay(Uri.parse(appCategoryBean.getLink_url()));
                }
                CommonWebActivity.callActivity(context, new CommonWebData(url, appCategoryBean.getApp_name()));
                return true;
            } else if (appCategoryBean.getLink_type() == ActionTypeEnum.Native) {
                if (StringUtils.isEmpty(appCategoryBean.getAndroid_native_url())) {
                    // todo Native
                } else {
                    nativeHandler(appCategoryBean);
                }
            } else if (appCategoryBean.getLink_type() == ActionTypeEnum.NoAction) {

            }
        }
        return false;
    }

    private void nativeHandler(BannerBean appCategoryBean) {
        Intent intent = new Intent();
        Class aClass = null;
        try {
            aClass = Class.forName(appCategoryBean.getAndroid_native_url());
            intent.setClass(context, aClass);
            if (context.getPackageManager().resolveActivity(intent, 0) == null) {
                // 说明系统中不存在这个activity
                callOnSearchFailListener("暂未开通");
            } else {
                if (StringUtils.isNotEmpty(appCategoryBean.getNative_params())) {
                    JSONObject params = new JSONObject(appCategoryBean.getNative_params());
                    Iterator<String> iterator = params.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        Object value = params.get(key);
                        if (value instanceof String) {
                            intent.putExtra(key, value.toString());
                        } else if (value instanceof Integer) {
                            intent.putExtra(key, (Integer) value);
                        } else {
                            JSONArray jsonArray = (JSONArray) value;
                            if (jsonArray != null) {
                                ArrayList<String> item = new ArrayList<String>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    item.add(jsonArray.getString(i));
                                }
                                intent.putStringArrayListExtra(key, item);
                            }
                        }
                        //do something
                    }
                }
                context.startActivity(intent);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            ToastUtils.showToast("暂未开通");
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            ToastUtils.showToast("暂未开通");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callAliPay(Uri uri) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
            return;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            ToastUtils.showToast("该功能需跳转支付宝");
        }
    }

    private void goSearchMerchant(String tag) {
        MerchantActivity.callActivity(this.context, tag, "100");
    }


    private void callOnSearchSucessListener() {
        if (onSearchListener != null) {
            onSearchListener.onSucess();
        }
    }

    private void callOnSearchFailListener(String msg) {
        if (onSearchListener != null) {
            onSearchListener.onFail(msg);
        }
    }


    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    public String buildWebPage(String url, String title) {
        return "shangfeng://app/h5/callapp?page=web&url=" + URLEncoder.encode(url) + "&title=" + title + "&is_login=Y";
    }

    public interface OnSearchListener {
        public void onFail(String msg);

        public void onSucess();
    }

}
