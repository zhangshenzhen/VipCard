package com.bjypt.vipcard.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.SystemSettingActivity;
import com.bjypt.vipcard.utils.FileUtil;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/5/13.
 */
public class DownLoadService extends Service {
    private boolean flag;
    private boolean isCancel = false;

    private MyBinder myBinder = new MyBinder();


    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyBinder extends Binder {

        public void startdownLoad(String url) {
            save(url);
        }

        public void setFlags(boolean flags) {
            flag = flags;
            if (flag) {
                startNotification();
                setToast("开始下载");
                isCancel = true;
            } else {
                cancelNotification();
                mHander.removeCallbacks(mRunnable);
                if (isCancel) {

                    setToast("下载取消");
                    isCancel = false;
                }
            }
        }
    }

    public void save(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = downLoadFile(url);
                FileUtil.openFile(DownLoadService.this, file);
            }
        }).start();
    }

    private Intent intent = new Intent();

    protected File downLoadFile(String httpUrl) {
        final String fileName = FileUtil.apkSaveName;
        int numRead = 0;
        File tmpFile = new File(FileUtil.apkSavePath);
        if (!tmpFile.exists()) {
            tmpFile.mkdir();
        }
        File file = new File(FileUtil.apkSavePath + "/" + fileName);
        try {
            URL url = new URL(httpUrl);
            try {
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[4096];
                conn.connect();
                double count = 0;
                if (conn.getResponseCode() >= 400) {
                    setToast("链接超时");
                } else {
                    int size = conn.getContentLength();
                    while (count <= 100 && flag) {
                        if (is != null) {
                            int read = is.read(buf);
                            numRead += read;

                            if (read <= 0) {
                                break;
                            } else if (flag) {

                                fos.write(buf, 0, read);
                                Bundle bundle = new Bundle();
                                bundle.putString("size", size + "");
                                bundle.putString("numRead", numRead + "");
                                intent.putExtras(bundle);
                                intent.setAction(SystemSettingActivity.RECIVER_ACTIONS);
                                sendBroadcast(intent);

                                position = numRead / (size / 100);
                            }
                        } else {
                            Log.i("aaa", "下载终止1");
                            SharedPreferenceUtils.saveToSharedPreference(DownLoadService.this,"gengxinxiazai","1");
//                            Uri packageURI = Uri.parse("package:com.bjypt.vipcard");
//                            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
//                            startActivity(uninstallIntent);
                            break;
                        }
                    }
                }
                conn.disconnect();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (flag) {
            return file;
        } else {
            return null;
        }

    }

    private NotificationManager mNotificationManager;
    private int position = 0;
    private NotificationCompat.Builder mBuilder;
    private RemoteViews view;
    private Notification notification;
    int notification_id = 19172439;

    public void cancelNotification() {
        if (mNotificationManager != null)
            mNotificationManager.cancel(notification_id);
    }

    public void startNotification() {

        if (view == null) {
            //先设定RemoteViews
            view = new RemoteViews(getPackageName(), R.layout.layout_notification);
            //设置对应IMAGEVIEW的ID的资源图片
            view.setImageViewResource(R.id.iv, R.mipmap.app_ic_launcher);
            view.setTextViewText(R.id.tv, 0 + "%");
            view.setProgressBar(R.id.pro, 100, 0, false);

            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(this);
            notification = new Notification(R.mipmap.app_ic_launcher, "正在下载", System.currentTimeMillis());
            mBuilder.setContentTitle("")//设置通知栏标题
                    .setContentText("")
                    .setTicker("开始下载") //通知首次出现在通知栏，带上升动画效果的
                    .setPriority(Notification.PRIORITY_MAX)//设置该通知优先级
                    .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                    .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                    .setSmallIcon(R.mipmap.app_ic_launcher);//设置通知小ICON

            mBuilder.setContent(view);
        }

        new Thread(mRunnable).start();
    }


    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            Message msg = mHander.obtainMessage();
            msg.arg1 = position;
            mHander.sendMessage(msg);
        }
    };

    Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.i("aaa", "" + msg.arg1);
            if (msg.arg1 < 100) {
//                mBuilder.setProgress(100, position, false);
                view.setTextViewText(R.id.tv, msg.arg1 + "%");
                view.setProgressBar(R.id.pro, 100, msg.arg1, false);
                mNotificationManager.notify(notification_id, mBuilder.build());
                mHander.postDelayed(mRunnable, 500);
            } else {
                mHander.removeCallbacks(mRunnable);
                cancelNotification();
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        cancelNotification();
        mHander.removeCallbacks(mRunnable);
        mNotificationManager = null;
        mBuilder = null;
    }

    public void setToast(String text) {
        Toast.makeText(DownLoadService.this, text, Toast.LENGTH_SHORT).show();
    }
}
