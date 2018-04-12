package com.bjypt.vipcard.utils;

import android.content.Context;

import com.bjypt.vipcard.model.CITYS;
import com.bjypt.vipcard.model.CityRoot;
import com.bjypt.vipcard.model.CitysBean;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyunte on 2017/1/10.
 */

public class ParserAssetsUtil {

    public static List<CITYS> initJsonData(Context context) {
        StringBuffer sb = null;
        CityRoot root = null;
        try {
             sb = new StringBuffer();
            InputStream is = context.getAssets().open("mycity.json");
            int len = -1;
            byte[] buf = new byte[is.available()];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "utf-8"));
            }
            is.close();
             root = new Gson().fromJson(sb.toString(),CityRoot.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root.getCITYS();
    }

}
