package com.bjypt.vipcard.common;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.base.RespBase;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.dialog.NormalDialog;
import com.bjypt.vipcard.model.LifeMoneyOptionBean;
import com.bjypt.vipcard.model.QqCurrencyBeen;
import com.bjypt.vipcard.service.UpgradeService;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.utils.SsX509TrustManager;
import com.bjypt.vipcard.view.LoadingPageDialog;
import com.bjypt.vipcard.view.ToastUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.orhanobut.logger.Logger;
import com.sinia.orderlang.utils.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/8
 * Use by 公共方法
 */
public class Wethod {
    public static final int NETWORK_NONE = 0;
    public static final int NETWORK_WIFI = 1;
    public static final int NETWORK_MOBILE = 2;


    /**
     * 配置ImageLoder
     */
    public static void configImageLoader(Context context) {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.welcome02) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.welcome02) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.welcome02) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
//                         .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    /***Volley框架Post请求***/
    public static void httpPost(final Context context, final int resultCode, String url, final Map<String, String> hMap, final VolleyCallBack<String> callBack) {
        httpPost(context, resultCode, url, hMap, callBack, View.VISIBLE);
    }

    /**
     *  visableProgress  View.Gone
     */
    /***Volley框架Post请求***/
    public static void httpPost(final Context context, final int resultCode, final String url, final Map<String, String> hMap, final VolleyCallBack<String> callBack, final int visableProgress) {
//        SsX509TrustManager.allowAllSSL();
        final LoadingPageDialog loadingPageDialog = new LoadingPageDialog(context);
        if (visableProgress == View.VISIBLE) {
            if (context instanceof Activity && !((Activity) context).isFinishing()) {
                loadingPageDialog.show();
            } else {
                loadingPageDialog.show();
            }
        }
        SsX509TrustManager.allowAllSSL();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (loadingPageDialog != null) {
                    if (loadingPageDialog.isShowing()) {
                        loadingPageDialog.dismiss();
                    }
                }
                LogUtil.debugPrint("url = " + url + "  " + s);
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                try {
                    RespBase respBase = objectMapper.readValue(s, RespBase.class);

                    // {"resultStatus": "-9000", "msg": "非法请求，请重新登录！"}
                    if (("-9000").equals(respBase.getResultStatus())) {
                        cleanLogin(context);
                        final NormalDialog dialog = NormalDialog.getInstance(context);
                        dialog.setDesc("您的账号已在别处登录请重新登录");
                        dialog.setOnNegativeListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setOnPositiveListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
//                                cleanLogin(context);
                                Intent intent = new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                            }
                        });
                        return;
                    } else if ("10001".equals(respBase.getResultStatus()) || "10000".equals(respBase.getResultStatus())) {
                        callBack.onSuccess(resultCode, s);
                        return;
                    }

                    if (respBase.isSuccess()) {
                        callBack.onSuccess(resultCode, s);
                    } else {
                        callBack.onFailed(resultCode, s);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error", "volleyError:" + volleyError + " :  url=" + url);
                if (volleyError.networkResponse == null) {
                    if (loadingPageDialog != null) {
                        if (loadingPageDialog.isShowing()) {
                            loadingPageDialog.dismiss();
                        }
                    }
                    Log.e("error", "volleyError:" + volleyError + " :  url=" + url);
                    callBack.onError(volleyError);
//                    ToastUtil.showToast(context, "您的网络好像不给力，请检查网络");
                } else {
                    if (url.contains(Config.web.base) && volleyError.networkResponse.statusCode == 404) {//只检测hyb接口
                        UpgradeService.getInstance(context).startCheck();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map = hMap;
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String deviceId = "";
                if (context.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    deviceId = tm.getDeviceId();
                }
                PackageManager manager = context.getPackageManager();
                PackageInfo info = null;
                try {
                    info = manager.getPackageInfo(context.getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                String version = info.versionName;
                Map<String, String> map = new HashMap<String, String>();
                map.put("cpuid", deviceId);
                map.put("versionCode", version);
                map.put("pkregister", SharedPreferenceUtils.getFromSharedPreference(context, Config.userConfig.pkregister));
                map.put("versionInt", AES.encrypt(info.versionCode + "", AES.key));
                if (getParams() != null && getParams().size() > 0) {
                    map.put("sign", AES.createSign(new TreeMap<String, String>(getParams())));
                }
                return map;
            }
        };
        stringRequest.setTag(resultCode);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        MyApplication.getHttpQueue().add(stringRequest);
    }

//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    private static boolean isValidContext(Context c) {
//        if (c instanceof Activity) {
//
//        }
//        Activity a = (Activity) c;
//
//        if (a.isDestroyed() || a.isFinishing()) {
//            Log.i("YXH", "Activity is invalid." + " isDestoryed-->" + a.isDestroyed() + " isFinishing-->" + a.isFinishing());
//            return false;
//        } else {
//            return true;
//        }
//    }

    private static void cleanLogin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("hyb", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Config.userConfig.pkregister);
        editor.remove(Config.userConfig.loginpassword);
        editor.remove(Config.userConfig.is_Login);
        editor.commit();
    }


    /***Volley框架Get请求***/
    public static void httpGet(final Context context, final int resultCode, String url, final VolleyCallBack<String> callBack) {
        SsX509TrustManager.allowAllSSL();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                try {
                    RespBase respBase = objectMapper.readValue(s.toString(), RespBase.class);
                    if (respBase.isSuccess()) {
                        callBack.onSuccess(resultCode, s);
                    } else {
                        callBack.onFailed(resultCode, s);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError.networkResponse == null) {
                    ToastUtil.showToast(context, "您的网络好像不给力，请稍后再试");
                }

            }
        });
        stringRequest.setTag(resultCode);
        MyApplication.getHttpQueue().add(stringRequest);
    }

    /***Volley框架Get请求***/
    public static void httpGetOutSystem(final Context context, final int resultCode, String url, final VolleyCallBack<String> callBack) {
        SsX509TrustManager.allowAllSSL();
        Utf8StringRequest stringRequest = new Utf8StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                if (StringUtil.isNotEmpty(s)) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        callBack.onSuccess(resultCode, s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callBack.onFailed(resultCode, s);
                    }
                } else {
                    callBack.onFailed(resultCode, s);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError.networkResponse == null) {
                    ToastUtil.showToast(context, "您的网络好像不给力，请稍后再试");
                }

            }
        });
        stringRequest.setTag(resultCode);
        MyApplication.getHttpQueue().add(stringRequest);
    }

    /***Volley框架Get请求***/
    public static void httpxmlGet(final Context context, final int resultCode, String url, final VolleyCallBack<String> callBack) {
//        SsX509TrustManager.allowAllSSL();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                Log.e("liyunte", "sssssss" + s);
                callBack.onSuccess(resultCode, s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError.networkResponse == null) {
                    ToastUtil.showToast(context, "您的网络好像不给力，请稍后再试");
                }

            }
        }) {
           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Charset", "gbk");
                return headers;
//                return super.getHeaders();
            }*/

          /*  @Override
            protected String getParamsEncoding() {
                return "  GBK";
//                return super.getParamsEncoding();
            }*/

        };

        MyApplication.getHttpQueue().add(stringRequest);
    }

    /**
     * jsonObject 解析的get请求
     *
     * @param resultCode
     * @param url
     * @param callBack
     */
    public static void httpget(final Context context, final int resultCode, String url, final VolleyCallBack<String> callBack) {
     /*   if (loadingPageDialog==null){
            loadingPageDialog = new LoadingPageDialog(context);
        }
        if (!loadingPageDialog.isShowing()){
            loadingPageDialog.show();
        }*/
//        SsX509TrustManager.allowAllSSL();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
               /* if (loadingPageDialog!=null){
                    if (loadingPageDialog.isShowing()){
                        loadingPageDialog.dismiss();
                    }
                }*/

                JSONObject object;
                try {
                    object = new JSONObject(s.toString());
                    if ("ok".equalsIgnoreCase(object.getString("status"))) {
                        callBack.onSuccess(resultCode, s);
                    } else {
                        callBack.onFailed(resultCode, s);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError.networkResponse == null) {
                 /*   if (loadingPageDialog!=null){
                        if (loadingPageDialog.isShowing()){
                            loadingPageDialog.dismiss();
                        }
                    }*/

                    ToastUtil.showToast(context, "您的网络好像不给力，请稍后再试");
                }
            }
        });

        MyApplication.getHttpQueue().add(stringRequest);
    }

    /*发送jsonObjectRequest的Get请求*//*
    public static void HttpObjectGet(String url){
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError.networkResponse==null){

                }
            }
        });
        MyApplication.getHttpQueue().add(jr);
    }*/

    /*********************当请求结果为ResultStatus为-1时******************************/
    public static void ToFailMsg(Context context, Object result) {
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            RespBase respBase = objectMapper.readValue(result.toString(), RespBase.class);
            Logger.e(respBase.getResultData().toString());
            Toast.makeText(context, respBase.getResultData().toString(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断网络类型
     *
     * @param context
     * @return
     */
    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return NETWORK_WIFI;
        }

        // 3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return NETWORK_MOBILE;
        }
        return NETWORK_NONE;
    }


    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 打开网络设置界面
     */
    public static void openNetworkSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 弹出网络设置提示
     */
    public static void netWorkSettingAlert(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("网络不可用，请检查网络连接")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (android.os.Build.VERSION.SDK_INT > 10) {
                            //3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                            context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                        } else {
                            context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    }
                })
                .create().show();
    }

    /**
     * 获取手机Ip地址
     *
     * @return
     */

    public static String getPsdnIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        //if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 5  6 7  8  9  11 12
     *
     * @param beenlist
     * @param flag     对应的消费类型
     * @return
     */
    public static ArrayList<QqCurrencyBeen> setList(ArrayList<QqCurrencyBeen> beenlist, int flag) {
        beenlist.clear();
        if (flag == 5) {//QQ币
            beenlist.add(new QqCurrencyBeen(5, "220614"));
            beenlist.add(new QqCurrencyBeen(10, "220615"));
            beenlist.add(new QqCurrencyBeen(20, "220616"));
            beenlist.add(new QqCurrencyBeen(30, "220617"));
            beenlist.add(new QqCurrencyBeen(50, "220698"));
            beenlist.add(new QqCurrencyBeen(100, "220699"));
        } else if (flag == 6)
            beenlist.add(new QqCurrencyBeen(10, "222301"));//红钻
        else if (flag == 7)
            beenlist.add(new QqCurrencyBeen(10, "222304"));//紫钻
        else if (flag == 8)
            beenlist.add(new QqCurrencyBeen(10, "222306"));//绿钻
        else if (flag == 9)
            beenlist.add(new QqCurrencyBeen(10, "222309"));//QQ会员
        else if (flag == 11)
            beenlist.add(new QqCurrencyBeen(10, "222308"));// 蓝钻
        else if (flag == 12)
            beenlist.add(new QqCurrencyBeen(10, "222302"));//黄钻

        return beenlist;

    }


    /**
     * @param flag 1:充话费     2流量:电信             3流量:联通         4流量:移动
     * @return
     */
    public static ArrayList<LifeMoneyOptionBean> initMoneyOption(int flag) {
        ArrayList<LifeMoneyOptionBean> list = null;
        if (list == null)
            list = new ArrayList<LifeMoneyOptionBean>();
        list.clear();
        LifeMoneyOptionBean bean = null;
        if (flag == 2) {//电信
            bean = new LifeMoneyOptionBean("5", "30M");//（钱，流量）
            list.add(bean);
            bean = new LifeMoneyOptionBean("10", "100M");
            list.add(bean);
            bean = new LifeMoneyOptionBean("30", "500M");
            list.add(bean);
            bean = new LifeMoneyOptionBean("50", "1G");
            list.add(bean);

        } else if (flag == 4) {//联通
            bean = new LifeMoneyOptionBean("6", "50M");
            list.add(bean);
            bean = new LifeMoneyOptionBean("10", "100M");
            list.add(bean);
            bean = new LifeMoneyOptionBean("15", "200M");
            list.add(bean);
            bean = new LifeMoneyOptionBean("30", "500M");
            list.add(bean);
        } else if (flag == 3) {//移动
            bean = new LifeMoneyOptionBean("5", "30M");
            list.add(bean);
            bean = new LifeMoneyOptionBean("30", "500M");
            list.add(bean);
            bean = new LifeMoneyOptionBean("40", "700M");
            list.add(bean);
            bean = new LifeMoneyOptionBean("50", "1G");
            list.add(bean);
        }

        return list;

    }

}
