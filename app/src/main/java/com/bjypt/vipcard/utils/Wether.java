package com.bjypt.vipcard.utils;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Wethod;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Administrator on 2016/12/14.
 */

public class Wether {
    public static void getWratherInfo(String city,final TextView textView) {
        FinalHttp http = new FinalHttp();
        AjaxParams params = new AjaxParams();
        System.out.println("定位所在的城市" + city + "" + city.indexOf("市"));
        params.put("location", city);
        params.put("ak", "A72e372de05e63c8740b2622d0ed8ab1");
        params.put("output", "json");
        http.post("http://api.map.baidu.com/telematics/v3/weather?", params,
                new AjaxCallBack<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        super.onSuccess(o);
                        try {
                            JSONObject jobj = new JSONObject(o.toString());
                            if (jobj.getInt("error") == 0) {
                                JSONArray jarr = jobj.getJSONArray("results");
                                JSONObject weather = jarr.getJSONObject(0);
                                JSONArray jarr_weather = weather
                                        .getJSONArray("weather_data");
                                JSONObject jobj_weather = jarr_weather.getJSONObject(0);
                                Log.e("liyunte",jobj_weather.getString("temperature"));
                                Log.e("liyunte",jobj_weather.getString("weather"));
                                textView.setText(jobj_weather.getString("temperature")+"\n"+jobj_weather.getString("weather"));
//                                view1.setText(jobj_weather.getString("temperature"));
//                                view2.setText(jobj_weather.getString("weather"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    /*   @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);

                    }*/

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                    }
                });

    }
    }
