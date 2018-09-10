package com.bjypt.vipcard.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.BindPhoneNumberActivity;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.dialog.IntegralToast;
import com.bjypt.vipcard.model.CommentDetailsClBean;
import com.bjypt.vipcard.model.IntegralBean;
import com.bjypt.vipcard.model.LoginData;
import com.bjypt.vipcard.model.WXResultInfoBean;
import com.bjypt.vipcard.model.WXTokenBean;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.ToastUtil;
import com.sinia.orderlang.utils.StringUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler, VolleyCallBack {

    // IWXAPI 是第三方APP和微信通信的openapi接口
    private static final String TAG = "WXEntryActivity";
    private static final int RETURN_MSG_TYPE_LOGIN = 1; //登录
    private static final int RETURN_MSG_TYPE_SHARE = 2; //分享
    private static final int REQUEST_WX_TOKEN = 123456;
    private static final int REQUEST_USER_INFO = 456124;
    private static final int REQUEST_AUTHORIZE = 4564545;
    private static final int REQUEST_INTEGRAL = 784761461;
    private Context mContext;
    private String openid;
    private String nickname;
    private String headUrl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //如果没回调onResp，估计是这句没有写
        MyApplication.mWxApi.handleIntent(getIntent(), this);
    }

    // ΢微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        Toast.makeText(this, "openid = " + req.openId +"  " + req.getType(), Toast.LENGTH_SHORT).show();

        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//			goToGetMsg();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//			goToShowMsg((ShowMessageFromWX.Req) req);
                break;
            case ConstantsAPI.COMMAND_LAUNCH_BY_WX:
                Toast.makeText(this, R.string.launch_from_wx, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        Log.i(TAG, "onResp:------>");
        Log.i(TAG, "error_code:---->" + resp.errCode);
        int type = resp.getType(); //类型：分享还是登录
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝授权
                ToastUtil.showToast(mContext, "拒绝授权微信登录");
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                String message = "";
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    message = "取消了微信登录";
                    WXEntryActivity.this.finish();
                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    message = "取消了微信分享";
                    WXEntryActivity.this.finish();
                }
                ToastUtil.showToast(mContext, message);
                break;
            case BaseResp.ErrCode.ERR_OK:
                //用户同意
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    //用户换取access_token的code，仅在ErrCode为0时有效
                    String code = ((SendAuth.Resp) resp).code;
                    Log.i(TAG, "code:------>" + code);
//                    ToastUtil.showToast(mContext, code);
                    //这里拿到了这个code，去做2次网络请求获取access_token和用户个人信息
//					WXLoginUtils().getWXLoginResult(code, this);
                    getAccessToken(code);
                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    String shareType = SharedPreferenceUtils.getFromSharedPreference(WXEntryActivity.this, "shareType");
                    if (StringUtil.isNotEmpty(shareType) && shareType.equals("web")) {
                        requestIntegral();
                    } else {
                        ToastUtil.showToast(mContext, "微信分享成功");
                        WXEntryActivity.this.finish();
                    }
                }
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        switch (reqcode) {
            case REQUEST_WX_TOKEN:
                loadToken(result);
                break;
            case REQUEST_USER_INFO:
                loadUserInfo(result);
                break;
            case REQUEST_AUTHORIZE:
                Log.e(TAG, "请求成功");
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                try {
                    LoginData loginData = objectMapper.readValue(result.toString(), LoginData.class);
                    SharedPreferenceUtils.saveToSharedPreference(WXEntryActivity.this, Config.userConfig.phoneno, loginData.getResultData().getPhoneno());
                    SharedPreferenceUtils.saveToSharedPreference(WXEntryActivity.this, Config.userConfig.loginpassword, loginData.getResultData().getLoginpassword());
                    SharedPreferenceUtils.saveToSharedPreference(WXEntryActivity.this, Config.userConfig.pkregister, loginData.getResultData().getPkregister());
                    SharedPreferenceUtils.saveToSharedPreference(WXEntryActivity.this, Config.userConfig.nickname, loginData.getResultData().getNickname());
                    SharedPreferenceUtils.saveToSharedPreference(WXEntryActivity.this, Config.userConfig.paypassword, loginData.getResultData().getPaypassword());
                    SharedPreferenceUtils.saveToSharedPreference(WXEntryActivity.this, Config.userConfig.position, loginData.getResultData().getPosition());
                    SharedPreferenceUtils.saveToSharedPreference(WXEntryActivity.this, Config.userConfig.nativeLoginPassword, loginData.getResultData().getNativeLoginPassword());
                    SharedPreferenceUtils.saveToSharedPreference(WXEntryActivity.this, Config.userConfig.uid, loginData.getResultData().getUid());
                    SharedPreferenceUtils.saveToSharedPreference(WXEntryActivity.this, Config.userConfig.is_card_sales, loginData.getResultData().getIs_card_sales());
                    SharedPreferenceUtils.saveToSharedPreference(WXEntryActivity.this, Config.userConfig.is_Login, "Y");
//                    startActivity(new Intent(this, MainActivity.class));
                    WXEntryActivity.this.finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_INTEGRAL:
                ObjectMapper objectMapper2 = ObjectMapperFactory.createObjectMapper();
                try {
                    IntegralBean integralBean = objectMapper2.readValue(result.toString(), IntegralBean.class);
                    if (StringUtil.isNotEmpty(integralBean.getResultData().getVirtualBalance())) {
                        String resultData = integralBean.getResultData().getVirtualBalance();
                        if (!resultData.equals("0")) {
                            IntegralToast.getIntegralToast().ToastShow(WXEntryActivity.this, null, "已分享，加" + resultData + "积分");
                            WXEntryActivity.this.finish();
                        } else {
                            WXEntryActivity.this.finish();
                        }
                    } else {
                        WXEntryActivity.this.finish();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        switch (reqcode) {
            case REQUEST_WX_TOKEN:
                Log.e(TAG, "REQUEST_WX_TOKEN----->" + "请求失败" + result.toString());
                break;
            case REQUEST_USER_INFO:
                Log.e(TAG, "REQUEST_USER_INFO----->" + "请求失败" + result.toString());
                break;
            case REQUEST_AUTHORIZE:
                loadBindAndJumpPhoneInfo(result);
                break;
            case REQUEST_INTEGRAL:
                ObjectMapper objectMapper2 = ObjectMapperFactory.createObjectMapper();
                try {
                    CommentDetailsClBean commentDetailsClBean = objectMapper2.readValue(result.toString(), CommentDetailsClBean.class);
                    if (StringUtil.isNotEmpty(commentDetailsClBean.getResultData())) {
                        String resultData = commentDetailsClBean.getResultData();
                        ToastUtil.showToast(WXEntryActivity.this, resultData);
                        WXEntryActivity.this.finish();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        ToastUtil.showToast(this, "请检查您的网络");
    }

    /**
     * 请求token
     *
     * @param code
     */
    private void getAccessToken(String code) {
        Log.e(TAG, "getAccessToken=====" + code);
        String WX_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + Constants.APP_ID
                + "&secret="
//                + "b3aec7d84186ff3bc5a9b3d793ba9ce7"
                + "ca6f318f1bfbcc6755c2cb6c31fd228d"
                + "&code="
                + code
                + "&grant_type=authorization_code";
        Wethod.httpGetOutSystem(WXEntryActivity.this, REQUEST_WX_TOKEN, WX_TOKEN_URL, this);
    }

    /**
     * 加载token
     *
     * @param result
     */
    private void loadToken(Object result) {
        Log.e(TAG, "loadToken====" + result + "");
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            WXTokenBean wxTokenBean = objectMapper.readValue(result.toString(), WXTokenBean.class);
            String access_token = wxTokenBean.getAccess_token();
            openid = wxTokenBean.getOpenid();
            String refresh_token = wxTokenBean.getRefresh_token();
            String unionid = wxTokenBean.getUnionid();
            getWXUserInfo(access_token, openid, unionid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求微信用户信息
     *
     * @param access_token
     * @param openid
     * @param unionid
     */
    private void getWXUserInfo(final String access_token, final String openid, final String unionid) {
        Log.e(TAG, "getWXUserInfo=====" + "access_token:" + access_token + "openid:" + openid);
        String WX_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
        Wethod.httpGetOutSystem(WXEntryActivity.this, REQUEST_USER_INFO, WX_USER_INFO_URL, this);
    }

    /**
     * 加载用户信息
     *
     * @param result
     */
    private void loadUserInfo(Object result) {
        Log.e(TAG, "loadUserInfo====" + result + "");
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            WXResultInfoBean wxResultInfoBean = objectMapper.readValue(result.toString(), WXResultInfoBean.class);
            headUrl = wxResultInfoBean.getHeadimgurl();
            nickname = wxResultInfoBean.getNickname();
            requestBindPhone();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求服务器判断是否绑定手机
     */
    private void requestBindPhone() {
        Map<String, String> param = new HashMap<>();
        param.put("openId", openid);
        param.put("loginType", "1");
        param.put("headImgUrl", headUrl);
        param.put("nickName", nickname);
        Wethod.httpPost(this, REQUEST_AUTHORIZE, Config.web.authorize, param, this);
    }

    /**
     * 加载绑定手机号与跳转信息
     *
     * @param result
     */
    private void loadBindAndJumpPhoneInfo(Object result) {
        Log.e(TAG, "loadBindAndJumpPhoneInfo====" + result + "");
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            CommentDetailsClBean commentDetailsClBean = objectMapper.readValue(result.toString(), CommentDetailsClBean.class);
            String resultData = commentDetailsClBean.getResultData();
            if (resultData != null && resultData.contains("请绑定手机")) {
                Intent intent = new Intent(this, BindPhoneNumberActivity.class);
                intent.putExtra("openId", openid);
                intent.putExtra("loginType", "1");
                intent.putExtra("headImgUrl", headUrl);
                intent.putExtra("nickName", nickname);
                startActivity(intent);
                WXEntryActivity.this.finish();
            } else {
                ToastUtil.showToast(this, resultData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求积分
     */
    private void requestIntegral() {
        Map<String, String> param = new HashMap<>();
        param.put("uid", SharedPreferenceUtils.getFromSharedPreference(WXEntryActivity.this, Config.userConfig.uid));
        param.put("action", "share");
        Wethod.httpPost(this, REQUEST_INTEGRAL, Config.web.request_credit, param, this);
    }
}
