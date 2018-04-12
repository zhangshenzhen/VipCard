package com.bjypt.vipcard.utils;

import android.app.Activity;
import android.content.Intent;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.activity.PaySuccessActivity;
import com.bjypt.vipcard.activity.SuccessActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.HuiYuanBiBean;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/7/28.
 */

public class VirtualmoneySuccessHelper implements VolleyCallBack<String> {

    Activity context;
    private HuiYuanBiBean huiYuanBiBean;
    private String stringType;     //用于显示是支付成功，还是充值成功

    public VirtualmoneySuccessHelper(Activity ctx) {
        this.context = ctx;
    }

    public VirtualmoneySuccessHelper(Activity ctx,String string) {
        this.context = ctx;
        this.stringType = string;

    }

    public void checkVirtualmoney(String pkregister, String sourcepk) {
        Wethod.httpGet(this.context, 1001, Config.web.virtualcount + "?pkregister=" + pkregister + "&sourcepk=" + sourcepk, this);
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == 1001) {
            try {
                huiYuanBiBean = ObjectMapperFactory.createObjectMapper().readValue(result.toString(), HuiYuanBiBean.class);
                BigDecimal bigDecimal = new BigDecimal(huiYuanBiBean.getResultData().getAmount());
                if (bigDecimal.floatValue() > 0) {
                    Intent intent = new Intent(this.context, SuccessActivity.class);
                    intent.putExtra("huiYuanBiBean", result);
                    this.context.startActivity(intent);
                } else {
                    Intent intent = new Intent(this.context, PaySuccessActivity.class);
                    intent.putExtra("stringType",stringType);
                    this.context.startActivity(intent);
                }
                this.context.finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
