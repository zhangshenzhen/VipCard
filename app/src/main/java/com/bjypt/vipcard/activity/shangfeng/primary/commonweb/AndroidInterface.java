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
import com.bjypt.vipcard.activity.crowdfunding.CrowdfundingQRPayActivity;
import com.bjypt.vipcard.activity.shangfeng.util.ApplicationUtils;
import com.bjypt.vipcard.activity.shangfeng.util.IsJudgeUtils;
import com.bjypt.vipcard.activity.shangfeng.util.ToastUtils;
import com.bjypt.vipcard.activity.shangfeng.widget.dialog.BottomDialog;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.PhoneCpuId;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
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

public class AndroidInterface extends DefaultAndroidInterface{

    private Handler deliver = new Handler(Looper.getMainLooper());
    private AgentWeb agent;
    private Context context;

    private BaseAgentWebActivity baseAgentWebActivity;

    public AndroidInterface(AgentWeb agent, Context context, BaseAgentWebActivity baseAgentWebActivity) {
        super(baseAgentWebActivity, context);
        this.agent = agent;
        this.context = context;
        this.baseAgentWebActivity = baseAgentWebActivity;
    }


}
