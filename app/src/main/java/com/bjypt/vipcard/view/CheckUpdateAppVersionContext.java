package com.bjypt.vipcard.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.VersionInfoAdapter;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.fragment.NewHomeFrag;
import com.bjypt.vipcard.model.DownLoadBean;
import com.bjypt.vipcard.model.DownLoadResultBean;
import com.bjypt.vipcard.service.DownLoadService;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.orhanobut.logger.Logger;
import com.sinia.orderlang.utils.AppInfoUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanglei on 2017/12/20.
 * 检查更新APP版本 容器
 */

public class CheckUpdateAppVersionContext implements VolleyCallBack {

    private Context context;
    private ImageView tv_back;
    private Button tv_entry;
    private TextView tv_version;
    private PopupWindow popupWindow;
    private ProgressBar update_progress_bar;
    private DownLoadResultBean downLoadResultBean;
    public static String RECIVER_ACTIONS = "com.bjypt.vipcard";
    private DownLoadService.MyBinder myBinder;
    private boolean flag = false;
    private DownLoadReciver receiver;
    private int progress = 0;

    private ListView listview_version;
    private VersionInfoAdapter versionInfoAdapter;
    private String[] arr;

    private View toParentLocation;
    private RelativeLayout rl_close;

    /**
     * 使用步骤 1： new 一个对象  2：检查更新的时候调用方法 startCheck  3： 容器回收的时候调用destory
     * @param context
     * @param toParentLocation
     */
    public CheckUpdateAppVersionContext(Context context, View toParentLocation) {
        this.context = context;
        this.toParentLocation = toParentLocation;
    }

    private void showDialog() {
        View view = View.inflate(context, R.layout.layout_popupwindow, null);
        popupWindow = new PopupWindow(view, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.color.transparency_B3));
        update_progress_bar = (ProgressBar) view.findViewById(R.id.update_progress_bar);
        listview_version = (ListView) view.findViewById(R.id.listview_version);
        rl_close = (RelativeLayout) view.findViewById(R.id.rl_close);
        tv_back = (ImageView) view.findViewById(R.id.tv_popup_back);
        tv_entry = (Button) view.findViewById(R.id.tv_popup_entry);
        tv_version = (TextView) view.findViewById(R.id.tv_new_version);
        tv_version.setText("有新版本啦，请尽快更新！");
        if(null != arr && arr.length > 0) {
            versionInfoAdapter = new VersionInfoAdapter(arr, context);
            listview_version.setAdapter(versionInfoAdapter);
        }
        rl_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                if (myBinder != null) {
                    myBinder.setFlags(flag);
                }
                unBind();
                popupWindow.dismiss();

            }
        });
        tv_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag) {
                    //注册广播接收器
                    receiver = new DownLoadReciver();
                    IntentFilter filter = new IntentFilter();
                    filter.addAction(RECIVER_ACTIONS);
                    context.registerReceiver(receiver, filter);
                    Log.i("aaa", "我的广播注册成功");

                    if (myBinder == null) {
                        //启动Service
                        Intent IntentService = new Intent(context, DownLoadService.class);
                        context.bindService(IntentService, mConnection, Context.MODE_PRIVATE);
                        context.startService(IntentService);
                    } else {
                        flag = true;
                        //开始下载
                        myBinder.setFlags(flag);
                        tv_version.setText("正在准备资源...");
                        myBinder.startdownLoad(downLoadResultBean.getUrl());
                    }
                }
            }
            //                save(downLoadResultBean.getUrl());
            //            }
        });
        popupWindow.showAtLocation(toParentLocation, Gravity.BOTTOM, 0, 0);
    }

    private void unBind() {
        if (receiver != null) {
            context.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    /**
     * 开始检查版本更新
     */
    public void startCheck() {
        Map<String, String> map = new HashMap<>();
        map.put("versionCode", AppInfoUtil.getVersionName(context));
        map.put("type", "1");
        Wethod.httpPost(context, 3, Config.web.download_apk, map, this);
    }

    public void destory(){
//        unBind();
        if (myBinder != null) {
            context.unbindService(mConnection);
        }
    }


    public class DownLoadReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(RECIVER_ACTIONS)) {
                int size = Integer.parseInt(intent.getStringExtra("size"));
                int numRead = Integer.parseInt(intent.getStringExtra("numRead"));
                progress = numRead / (size / 100);
                update_progress_bar.setProgress(progress);
                if (progress == 100) {
                    unBind();
                    ToastUtil.showToast(context, "下载完成");
                    popupWindow.dismiss();
                    flag = !flag;
                } else {
                    tv_version.setText("正在下载" + progress + "%");
                }
            }
        }
    }


    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("aaa", "下载服务启动成功");
            myBinder = (DownLoadService.MyBinder) service;

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
    public void onSuccess(int reqcode, Object result) {
        Logger.d(result);
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!(activity.isDestroyed() || activity.isFinishing())) {
                    ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                    try {
                        DownLoadBean downLoadBean = objectMapper.readValue(result.toString(), DownLoadBean.class);
                        downLoadResultBean = downLoadBean.getResultData();
                        if(null != downLoadResultBean) {
                            if (null != downLoadResultBean.getMsg()) {
                                arr = downLoadResultBean.getMsg().split(";");
                            }
                            if (downLoadResultBean.getUrl() != null && !"".equals(downLoadResultBean.getUrl())) {
                                showDialog();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
