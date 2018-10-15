package com.bjypt.vipcard.activity.shangfeng.primary.commonweb;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;


import com.bjypt.vipcard.activity.crowdfunding.CrowdfundingAccountInfoActivity;
import com.bjypt.vipcard.activity.shangfeng.util.ApplicationUtils;
import com.bjypt.vipcard.activity.shangfeng.util.IsJudgeUtils;
import com.bjypt.vipcard.activity.shangfeng.util.ToastUtils;
import com.bjypt.vipcard.activity.shangfeng.widget.dialog.BottomDialog;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.PhoneCpuId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.just.agentweb.AgentWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by cenxiaozhong on 2017/5/14.
 * source code  https://github.com/Justson/AgentWeb
 */

public class AndroidInterface {

    private Handler deliver = new Handler(Looper.getMainLooper());
    private AgentWeb agent;
    private Context context;

    private BaseAgentWebActivity baseAgentWebActivity;

    public AndroidInterface(AgentWeb agent, Context context, BaseAgentWebActivity baseAgentWebActivity) {
        this.agent = agent;
        this.context = context;
        this.baseAgentWebActivity = baseAgentWebActivity;
    }


    @JavascriptInterface
    public void callAndroid(final String msg) {

        deliver.post(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(context.getApplicationContext(), "" + msg, Toast.LENGTH_LONG).show();
            }
        });


        Log.i("Info", "Thread:" + Thread.currentThread());

    }

    @JavascriptInterface
    public String getSign(Object paramsStr) {
        LogUtil.debugPrint(paramsStr.toString());
        java.lang.reflect.Type type = new TypeToken<HashMap<Integer, String>>() {
        }.getType();
        Map<String, String> params = new Gson().fromJson(paramsStr.toString(), type);
        return AES.createSign(new TreeMap<String, String>(params));
    }

    @JavascriptInterface
    public void closeWindow() {
        deliver.post(new Runnable() {
            @Override
            public void run() {
                baseAgentWebActivity.finish();
            }
        });
    }

    @JavascriptInterface
    public String getDeviceId() {
        LogUtil.debugPrint(PhoneCpuId.getDeviceId(context));
        return PhoneCpuId.getDeviceId(context);
    }

    @JavascriptInterface
    public void setTitle(String s) {
        deliver.post(new Runnable() {
            @Override
            public void run() {
                baseAgentWebActivity.setTitle(s);
            }
        });
    }


    @JavascriptInterface
    public String getParamsSign(String paramsStr) {
        try {
            JSONObject jsonObject = new JSONObject(paramsStr);
            Iterator<String> keys = jsonObject.keys();
            Map<String, String> params = new HashMap<>();
            while (keys.hasNext()) {
                String key = keys.next();
                params.put(key, jsonObject.get(key).toString());
            }
            JSONObject json = new JSONObject();
            json.put("sign", AES.createSign(new TreeMap<String, String>(params)));
            String versionCode = ApplicationUtils.getVersionName(MyApplication.getContext());
            int versionInt = ApplicationUtils.getVersionCode(MyApplication.getContext());
            json.put("versionName", versionCode);
            json.put("versionInt", versionInt);
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    @JavascriptInterface
    public void openMap(final String locations) {
        deliver.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject(locations);
                    showDialog(object.getString("address"), object.getString("lat"), object.getString("lon"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
    * 进入卡包详情*/
     @JavascriptInterface
      public void showCardbag(String json){
         try {
         JSONObject jsonObject = new JSONObject(json);
             int pkmerchantid = jsonObject.optInt("pkmerchantid");//商家ID
             String cardno = jsonObject.optString("cardno");//卡号
             String elec_cardno = jsonObject.optString("elec_cardno");// 电子卡号
             String merchant_name = jsonObject.optString("merchant_name");// 商家名称
             String vip_name = jsonObject.optString("vip_name");//    会员等级
             String discount = jsonObject.optString("discount");//    折扣
             int type_num = jsonObject.optInt("type_num");//    级别类型： 1.黄金 2.铂金 3.钻石

             Log.i("jsonshowCardbag", "Thread:" + json);

             Intent intent = new Intent();
            // intent.putExtra("pkuseraccountid", getPkuseraccountid());
             intent.putExtra("cardno", cardno);
             intent.putExtra("displaycardno", cardno);
             intent.putExtra("pkmerchantid", pkmerchantid);
             intent.putExtra("merchant_name", merchant_name);
             intent.putExtra("vip_name", vip_name);
             intent.putExtra("type_num", type_num);
             intent.putExtra("phoneno", Config.userConfig.phoneno);
             intent.setClass(context, CrowdfundingAccountInfoActivity.class);
             context.startActivity(intent);


         } catch (JSONException e) {
             e.printStackTrace();
         }

     }

    /*
     * 进入扫码界面*/
    @JavascriptInterface
    public void showBuypage(String json){
        try {
         JSONObject jsonObject = new JSONObject(json);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * 显示 第三方地图应用
     */
    private void showDialog(final String address, final String lat, final String lon) {

//        currentAddress = String.valueOf(SharedPreferencesUtils.get(LocateResultFields.CURRENT_ADDRESS, ""));

        BottomDialog dialog = new BottomDialog(context, "使用高德地图", "使用百度地图");
        dialog.setClickListener(new BottomDialog.BtnBottomListener() {
            @Override
            public void onBtn1Click() {
                if (IsJudgeUtils.isAvilible(context, "com.autonavi.minimap")) {
                    try {
                        Intent intent = Intent.getIntent("androidamap://navi?sourceApplication= &poiname=" + address + "&lat="
                                + lat + "&lon="
                                + lon + "&dev=0");
                        context.startActivity(intent);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    //未安装
                    //market为路径，id为包名
                    //显示手机上所有的market商店
                    ToastUtils.showToast("您尚未安装高德地图");
                    try {
                        Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
                        Intent mIntent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(mIntent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onBtn2Click() {
                if (IsJudgeUtils.isAvilible(context, "com.baidu.BaiduMap")) {
                    try {
//                        String uri = "intent://map/direction?origin=latlng:0,0|name:"+currentAddress+"&destination=" + merchantLBean.getAddress() + "&mode=drivingion=" + "城市" + "&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";

                        Intent intent = Intent.getIntent("intent://map/direction?" +
                                "destination=latlng:" + lat + ","
                                + lon + "|name:" + address +       //终点
                                "&mode=driving&" +          //导航路线方式
                                "region=北京" +           //
                                "&src=慧医#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                        context.startActivity(intent); //启动调用
                    } catch (URISyntaxException e) {
                        Log.e("intent", e.getMessage());
                    }
                } else {
                    //未安装
                    //market为路径，id为包名
                    //显示手机上所有的market商店
                    ToastUtils.showToast("您尚未安装百度地图");
                    Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
                    Intent mIntent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(mIntent);
                }

            }
        });
        dialog.show();
    }




}
