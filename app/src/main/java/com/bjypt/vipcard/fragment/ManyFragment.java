package com.bjypt.vipcard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.InformationActivity;
import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.NewsRootBean;
import com.bjypt.vipcard.utils.ObjectMapperFactory;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/22
 * Use by 主界面首页Fragment
 */
public class ManyFragment extends BaseFrament implements VolleyCallBack {

    private LinearLayout ly_news_back;
    private LinearLayout ly_merchant_coupon_info;
    private LinearLayout ly_merchant_activitys;
    private LinearLayout sys_news;
    private TextView tv_time_merchant_counpon_info;
    private TextView tv_time_merchant_activitys;
    private TextView tv_time_merchant_sys;
    private String url ;
    private Map<String ,String > map;
    private List<String> list_time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_news, container, false);
    }

    @Override
    public void beforeInitView() {
        list_time = new ArrayList<>();
    }

    @Override
    public void initView() {
        ly_news_back = (LinearLayout)getActivity(). findViewById(R.id.ly_news_back);
        ly_merchant_coupon_info = (LinearLayout)getActivity(). findViewById(R.id.ly_merchant_coupon_info);
        ly_merchant_activitys = (LinearLayout) getActivity().findViewById(R.id.ly_merchant_activitys);
        sys_news = (LinearLayout) getActivity().findViewById(R.id.sys_news);
        tv_time_merchant_counpon_info = (TextView) getActivity().findViewById(R.id.tv_time_merchant_counpon_info);
        tv_time_merchant_activitys = (TextView) getActivity().findViewById(R.id.tv_time_merchant_activitys);
        tv_time_merchant_sys = (TextView) getActivity().findViewById(R.id.tv_time_merchant_sys);
    }

    @Override
    public void afterInitView() {
        url = Config.web.push_message_main;
        map = new HashMap<>();
        map.put("userId",getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(getActivity(),11, url, map, this);
    }

    @Override
    public void bindListener() {
        ly_news_back.setOnClickListener(this);
        ly_merchant_coupon_info.setOnClickListener(this);
        ly_merchant_activitys.setOnClickListener(this);
        sys_news.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()){
            /**
             * 返回
             */
            case R.id.ly_news_back:
                break;
            /**
             * 商家优惠信息
             */
            case R.id.ly_merchant_coupon_info:
//                startActivity(new Intent(NewsActivity.this, CouponActivity.class));
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intent_coupon = new Intent(getActivity(),InformationActivity.class);
                    intent_coupon.putExtra("type","2");
                    startActivity(intent_coupon);
                } else {
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }

                break;
            /**
             * 商家活动
             */
            case R.id.ly_merchant_activitys:
//                startActivity(new Intent(NewsActivity.this, RedPacketActivity.class));
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intent_activity = new Intent(getActivity(),InformationActivity.class);
                    intent_activity.putExtra("type","0");
                    startActivity(intent_activity);
                } else {
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }
                break;
            /**
             * 系统提醒
             */
            case R.id.sys_news:


                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intent_news = new Intent(getActivity(),InformationActivity.class);
                    intent_news.putExtra("type","3");
                    startActivity(intent_news);
                } else {
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }
                break;

        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode==11){
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            Log.e("liyunteee", result.toString());
            try {
                NewsRootBean rootBean = objectMapper.readValue(result.toString(),NewsRootBean.class);
                for (int i=0;i<rootBean.getResultData().size();i++){
                    list_time.add(rootBean.getResultData().get(i).getLatestest_time());
                    if ("0".equals(rootBean.getResultData().get(i).getType())){
                        tv_time_merchant_activitys.setText(rootBean.getResultData().get(i).getLatestest_time());
                    }else if ("3".equals(rootBean.getResultData().get(i).getType())){
                        tv_time_merchant_sys.setText(rootBean.getResultData().get(i).getLatestest_time());
                    }else if ("2".equals(rootBean.getResultData().get(i).getType())){
                        tv_time_merchant_counpon_info.setText(rootBean.getResultData().get(i).getLatestest_time());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if(reqcode == 11){
            Wethod.ToFailMsg(getActivity(),result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }




































































    /*private RelativeLayout mRelaName, mRelaAdress, mRelaPhoneno, mRelaLoginPsd, mRelaPayPsd, mRelaQuestionPsd, mRelaNotica, mRelaSuggest, mRelaCheckIn, mRelaUpdate;
    private TextView mLoginPsdTv, mUpdateStateTv, tv_many_phonenum;
    private ImageView mLoginPsdIv, mExitLogin;
    private final int MSTRENGTH_GRADE_ONE = 7;//密码强度一
    private final int MSTRENGTH_GRADE_TWO = 9;//密码强度二
    private final int MSTRENGTH_GRADE_THREE = 11;//密码强度三
    private final int MSTRENGTH_GRADE_Four = 13;//密码强度四
    //判断是支付密码还是登陆密码
    private boolean PSD_TYPE;
    private BroadCastReceiverUtils utils;
    private TextView tv_back;
    private TextView tv_entry;
    private TextView tv_version;
    private RelativeLayout parent;
    private int screenWidth;
    private int screenHeigh;
    private DownLoadBean downLoadBean;
    private DownLoadResultBean downLoadResultBean;
    private PopupWindow popupWindow;
    private ProgressDialog progressDialog;
    private ImageView iv_many_news_point;//红点
    private int progress;
    private DownLoadReciver reciver;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getFromSharePreference(Config.userConfig.strength) == null && getFromSharePreference(Config.userConfig.strength).equals(""))
            saveDataToSharePreference(Config.userConfig.strength, MSTRENGTH_GRADE_ONE + "");

        return inflater.inflate(R.layout.fra_many, container, false);
    }

    @Override
    public void beforeInitView() {
        receiver = new DownLoadReciver();
        utils = new BroadCastReceiverUtils();
        utils.registerBroadCastReceiver(getActivity(),"action_news",receiver);
        progressDialog = new ProgressDialog(getActivity());
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeigh = dm.heightPixels;
    }

    @Override
    public void initView() {
        mRelaName = (RelativeLayout) getActivity().findViewById(R.id.rela_name);//昵称
        mRelaAdress = (RelativeLayout) getActivity().findViewById(R.id.rela_adress);//地址
        mRelaPhoneno = (RelativeLayout) getActivity().findViewById(R.id.rela_phoneno);//号码
        mRelaLoginPsd = (RelativeLayout) getActivity().findViewById(R.id.rela_login_psd);//登录密码
        mRelaPayPsd = (RelativeLayout) getActivity().findViewById(R.id.rela_pay_psd);//支付密码
        mRelaQuestionPsd = (RelativeLayout) getActivity().findViewById(R.id.rela_question_psd);//安保问题
        mRelaNotica = (RelativeLayout) getActivity().findViewById(R.id.rela_notica);//消息提醒
        mRelaSuggest = (RelativeLayout) getActivity().findViewById(R.id.rela_suggest);//产品建议
        mRelaCheckIn = (RelativeLayout) getActivity().findViewById(R.id.rela_check_in);//我想入住
        mRelaUpdate = (RelativeLayout) getActivity().findViewById(R.id.rela_check_update);//检查更新

        *//**登录密码**//*
        mLoginPsdIv = (ImageView) getActivity().findViewById(R.id.login_psd_iv);
        mLoginPsdTv = (TextView) getActivity().findViewById(R.id.login_psd_embellish);
        *//**更新**//*
        mUpdateStateTv = (TextView) getActivity().findViewById(R.id.update_state_tv);
        *//**退出登录**//*
        mExitLogin = (ImageView) getActivity().findViewById(R.id.exit_login);
        tv_many_phonenum = (TextView) getActivity().findViewById(R.id.tv_many_phonenum);//用户的手机号
        parent = (RelativeLayout) getActivity().findViewById(R.id.re_main);
        iv_many_news_point = (ImageView) getActivity().findViewById(R.id.iv_many_news_point);


    }

    @Override
    public void afterInitView() {
        mUpdateStateTv.setText("当前版本：" + getVersion());
        if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
            if (!"".equals(getFromSharePreference(Config.userConfig.phoneno))) {
                String phone = getFromSharePreference(Config.userConfig.phoneno);
                tv_many_phonenum.setText(phone.substring(0, 3) + "*******" + phone.substring(10));
            }

        } else {
            mExitLogin.setVisibility(View.GONE);
        }

    }

    @Override
    public void bindListener() {
        mRelaName.setOnClickListener(this);
        mRelaAdress.setOnClickListener(this);
        mRelaPhoneno.setOnClickListener(this);
        mRelaLoginPsd.setOnClickListener(this);
        mRelaPayPsd.setOnClickListener(this);
        mRelaQuestionPsd.setOnClickListener(this);
        mRelaNotica.setOnClickListener(this);
        mRelaSuggest.setOnClickListener(this);
        mRelaCheckIn.setOnClickListener(this);
        mRelaUpdate.setOnClickListener(this);
        mExitLogin.setOnClickListener(this);
    }

    public void startLogin() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void onClickEvent(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.rela_name://昵称
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), ChangeNameActivity.class));
                } else {
                    startLogin();
                }

                break;
            case R.id.rela_adress://地址
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), DefaultAdressActivity.class));
                } else {
                    startLogin();
                }

//                startActivity(new Intent(getActivity(),AddAdressActivity.class));
                break;
            case R.id.rela_phoneno://电话
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), AfreshPhoneBindActivity.class));
                } else {
                    startLogin();
                }

                break;
            case R.id.rela_login_psd://登录密码
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    PSD_TYPE = true;
                    Intent mLogPsdIntent = new Intent(getActivity(), ChangePasswordActivity.class);
                    mLogPsdIntent.putExtra("psdType", PSD_TYPE);
                    startActivityForResult(mLogPsdIntent, 0);
                } else {
                    startLogin();
                }
                *//****************修改登录密码时设置一个标志位区别是去修改登录密码还是支付密码*********************//*

                break;
            case R.id.rela_pay_psd://支付密码
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    PSD_TYPE = false;
                    Intent mPayPsdIntent = new Intent(getActivity(), ChangePasswordActivity.class);
                    mPayPsdIntent.putExtra("psdType", PSD_TYPE);
                    startActivity(mPayPsdIntent);
                } else {
                    startLogin();
                }

                *//****************修改登录密码时设置一个标志位区别是去修改登录密码还是支付密码*********************//*
                break;
            case R.id.rela_question_psd://安保问题
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {

                } else {
                    startLogin();
                }
                break;
            case R.id.rela_notica://提醒
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    iv_many_news_point.setVisibility(View.GONE);
                    startActivity(new Intent(getActivity(), NewsActivity.class));
                } else {
                    startLogin();
                }

                break;
            case R.id.rela_suggest://产品建议
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), ProductSuggestActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.rela_check_in://我想入住
               *//* try {

                    Intent intent = Intent.getIntent("androidamap://viewMap?sourceApplication=厦门通&poiname=百度奎科大厦&lat=40.047669&lon=116.313082&dev=0");
                    getActivity().startActivity(intent);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }*//*

                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), CheckInActivity.class));
                } else {
                    startLogin();
                }

                break;
            case R.id.rela_check_update://检查更新

                Log.e("liyunte", getVersion());   //version/checkVersion    http://123.57.232.188:8080/hyb/S01/
                Map<String, String> map = new HashMap<>();
                map.put("versionCode", getVersion());
                map.put("type", "1");
                Wethod.httpPost(getActivity(),11, Config.web.download_apk, map, ManyFragment.this);


                break;
            case R.id.exit_login://退出登录
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    //清空shareprepance的本地存储数据
                    saveDataToSharePreference(Config.userConfig.is_Login, "N");
                    saveDataToSharePreference(Config.userConfig.pkregister, "");
//                    saveDataToSharePreference(Config.userConfig.phoneno, "");
                    saveDataToSharePreference(Config.userConfig.loginpassword, "");
                    saveDataToSharePreference(Config.userConfig.nickname, "");
                    saveDataToSharePreference(Config.userConfig.paypassword, "");
                    saveDataToSharePreference(Config.userConfig.position, "");
                    saveDataToSharePreference(Config.userConfig.switch_citycode,"");
                    Toast.makeText(getActivity(), "退出成功", Toast.LENGTH_LONG).show();
                    getActivity().stopService(new Intent(getActivity(),MQTTService.class));
                    mExitLogin.setVisibility(View.GONE);
                    tv_many_phonenum.setText("");
                    utils.sendBroadCastReceiver(getActivity(), "updatephoto");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    startLogin();
                }

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengCountContext.onPageStart("ManyFragment");
        //更新密码强度
        if (getFromSharePreference(Config.userConfig.strength) != null && !getFromSharePreference(Config.userConfig.strength).equals("")) {
            setStrengthImg(Integer.parseInt(getFromSharePreference(Config.userConfig.strength)));
        } else {
            Log.i("aaa", "failed is 获取的值为空");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengCountContext.onPageEnd("ManyFragment");
    }

    public void setStrengthImg(int sGrade) {
        switch (sGrade) {
            case MSTRENGTH_GRADE_ONE:
                mLoginPsdIv.setImageResource(R.mipmap.psd_1);
                mLoginPsdTv.setText("弱");
                break;
            case MSTRENGTH_GRADE_TWO:
                mLoginPsdIv.setImageResource(R.mipmap.psd_2);
                mLoginPsdTv.setText("中");
                break;
            case MSTRENGTH_GRADE_THREE:
                mLoginPsdIv.setImageResource(R.mipmap.psd_3);
                mLoginPsdTv.setText("强");
                break;
            case MSTRENGTH_GRADE_Four:
                mLoginPsdIv.setImageResource(R.mipmap.psd_4);
                mLoginPsdTv.setText("非常棒");
                break;
        }
    }

    public String getVersion() {
        try {
            PackageManager manager = getActivity().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 11) {
            Log.e("liyunte", result.toString());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            try {
                downLoadBean = objectMapper.readValue(result.toString(), DownLoadBean.class);
                downLoadResultBean = downLoadBean.getResultData();
                if (downLoadResultBean.getUrl() != null && !"".equals(downLoadResultBean.getUrl())) {
                    setPopup();
                } else {
                    Toast.makeText(getActivity(), "当前已是最新版本", Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    public static String RECIVER_ACTIONS = "com.bjypt.vipcard";
    private MyBinder myBinder;
    private boolean flag = false;
    private DownLoadReciver receiver;

    public void setPopup() {
        View view = View.inflate(getActivity(), R.layout.layout_popupwindow, null);
        popupWindow = new PopupWindow(view, screenWidth, screenHeigh);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        tv_back = (TextView) view.findViewById(R.id.tv_popup_back);
        tv_entry = (TextView) view.findViewById(R.id.tv_popup_entry);
        tv_version = (TextView) view.findViewById(R.id.tv_new_version);
        tv_version.setText("有新版本啦，请尽快更新！");
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                if(myBinder!=null) {
                    myBinder.setFlags(flag);
                }
                unBind();
                popupWindow.dismiss();
            }
        });
        tv_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("liyunte", downLoadResultBean.getUrl());
                if (!flag) {
                    //注册广播接收器
                    receiver = new DownLoadReciver();
                    IntentFilter filter = new IntentFilter();
                    filter.addAction(RECIVER_ACTIONS);
                    getActivity().registerReceiver(receiver, filter);
                    Log.i("aaa", "我的广播注册成功");

                    if(myBinder==null) {
                        Log.i("aaa","此时为空");
                        //启动Service
                        Intent IntentService = new Intent(getActivity(), DownLoadService.class);
                        getActivity().bindService(IntentService, mConnection, getActivity().MODE_PRIVATE);
                        getActivity().startService(IntentService);
                    }else {
                        Log.i("aaa","此时不为空");
                        flag = true;
                        //开始下载
                        myBinder.setFlags(flag);
                        tv_version.setText("正在准备资源...");
                        myBinder.startdownLoad(downLoadResultBean.getUrl());
                    }
                }
//                save(downLoadResultBean.getUrl());
            }
        });
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("aaa", "下载服务启动成功");
            myBinder = (MyBinder) service;

            flag = true;
            //开始下载
            myBinder.setFlags(flag);
            tv_version.setText("正在准备资源...");
            myBinder.startdownLoad(downLoadResultBean.getUrl());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.i("aaa", "下载服务启动失败");
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
       // utils.UnRegisterBroadCastReceiver(getActivity(),receiver);

        if(myBinder!=null) {
            getActivity().unbindService(mConnection);
        }
    }

    public class DownLoadReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(RECIVER_ACTIONS)) {
                int size = Integer.parseInt(intent.getStringExtra("size"));
                int numRead = Integer.parseInt(intent.getStringExtra("numRead"));
                progress = numRead / (size / 100);
                Log.e("liyunte", "progress" + progress);

                if(progress==100){
                    Toast.makeText(getActivity(),"下载完成",Toast.LENGTH_SHORT).show();
                    flag = false;
                    if(myBinder!=null) {
                        myBinder.setFlags(flag);
                    }
                    unBind();
                    popupWindow.dismiss();
                }else {
                    tv_version.setText("正在下载"+progress+"%");
                }
            }else if ("action_news".equals(intent.getAction())){
                iv_many_news_point.setVisibility(View.VISIBLE);

            }

        }
    }

    public void unBind(){
        if(receiver!=null) {
            getActivity().unregisterReceiver(receiver);
            receiver=null;
        }
    }
*/

}
