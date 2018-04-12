package com.bjypt.vipcard.service;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.base.RespBase;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.LoginData;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.MD5;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.PhoneCpuId;
import com.bjypt.vipcard.view.ServiceUpgradeDialog;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanglei on 2018/4/3.
 */

public class UpgradeService implements VolleyCallBack<String> {


    private static final int request_check_code = 1;
    private Context context;
    private boolean isChecking = false; //是否正在检测
    private ServiceUpgradeDialog serviceUpgradeDialog;

    private static UpgradeService upgradeService;

    private UpgradeService(Context context){
        this.context = context;
    }

    public static UpgradeService getInstance(Context context){
        if(upgradeService  == null){
            upgradeService = new UpgradeService(context);
        }
        upgradeService.context = context;
        return upgradeService;
    }

    public void startCheck(){
        LogUtil.debugPrint("startCheck ...");
        if(!isChecking){ //没有正在请求
            isChecking = true;
            LogUtil.debugPrint("startCheck ...post ...");
            Wethod.httpPost(context, request_check_code, Config.web.check_upgrade, null, this, View.GONE);
        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if(request_check_code == reqcode){
            isChecking = false;
            LogUtil.debugPrint(result);
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                RespBase respBase = objectMapper.readValue(result, RespBase.class);
                // resultStatus 判断是否正在升级   10000  服务正常， 10001 服务正在升级
                if("10000".equals(respBase.getResultStatus())){
                    cancelDialog();
                    MyApplication.getInstance().setUpgrading(false);
                }else if("10001".equals(respBase.getResultStatus())){
                    LogUtil.debugPrint("isUpgrading: " + MyApplication.getInstance().isUpgrading());
                    if(!MyApplication.getInstance().isUpgrading()){
                        showDialog();
                        MyApplication.getInstance().setUpgrading(true);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showDialog(){
        if(serviceUpgradeDialog == null || !serviceUpgradeDialog.isShowing()){
            LogUtil.debugPrint("showDialog show");
            cancelDialog();
            serviceUpgradeDialog = new ServiceUpgradeDialog(context);
//            WindowManager.LayoutParams lp= serviceUpgradeDialog.getWindow().getAttributes();
//            lp.alpha=0.0f;
//            serviceUpgradeDialog.getWindow().setAttributes(lp);
//            serviceUpgradeDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            serviceUpgradeDialog.setCancelable(true);
            serviceUpgradeDialog.setCanceledOnTouchOutside(true);
            serviceUpgradeDialog.show();
        }
    }

    private void cancelDialog(){
        if(serviceUpgradeDialog != null && serviceUpgradeDialog.isShowing()){
            serviceUpgradeDialog.cancel();
        }
        serviceUpgradeDialog = null;
    }

    @Override
    public void onFailed(int reqcode, String result) {
        if(request_check_code == reqcode){
            isChecking = false;
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        isChecking = false;
        LogUtil.debugPrint("startCheck ..." + volleyError.getMessage());
    }
}
