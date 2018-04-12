package com.sinia.orderlang.utils;

import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Looper;
import android.util.Log;

import com.bjypt.vipcard.R;


public class BaseExceptionHandler implements UncaughtExceptionHandler
{

    private static BaseExceptionHandler mCatchHandler;

    private UncaughtExceptionHandler mDefaultHandler;

    private static Context mContext;

    private HashMap<String, String> infos = new HashMap<String, String>();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public synchronized static BaseExceptionHandler getInstance()
    {
        if (mCatchHandler == null)
        {
            mCatchHandler = new BaseExceptionHandler();
        }
        return mCatchHandler;
    }

    public void setContext(Context context)
    {
        mContext = context;
    }

    public void init()
    {
        Thread.setDefaultUncaughtExceptionHandler(this);
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        if (!handleException(ex) && mDefaultHandler != null)
        {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 自定义异常处�?:收集错误信息&发�?�错误报�?
     * 
     * @param ex
     * @return true:处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex)
    {

        if (mContext == null)
        {
            return false;
        }
        PackageInfo pinfo = getDeviceInfo(mContext);
        if (null != pinfo)
        {
            final String versionName = pinfo.versionName;
            final int versionCode = pinfo.versionCode;
            final String crashReport = getCrashReport(versionCode, versionName,
                    mContext, ex);
            Log.e("handleException", crashReport);
            new Thread()
            {
                public void run()
                {
                    Looper.prepare();
                    sendAppCrashReport(versionCode, versionName, mContext,
                            crashReport);
                    Looper.loop();
                }

            }.start();
            return true;
        }
        return false;
    }

    /**
     * 发�?�App异常崩溃报告
     * 
     * @param cont
     * @param crashReport
     */
    public static void sendAppCrashReport(final int versionCode,
            final String versionName, final Context cont,
            final String crashReport)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(R.string.app_error);
        builder.setMessage(R.string.app_error_message);
        builder.setPositiveButton(R.string.submit_report,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        String vName = "";
                        if (mContext != null)
                        {
                            vName = AppInfoUtil.getVersionName(mContext);
                        }

                        String subject = "(V" + vName
                                + ") - 错误报告";
                        Intent i = new Intent(Intent.ACTION_SEND);
                        // i.setType("text/plain"); //模拟�?
                        i.setType("message/rfc822"); // 真机
                        i.putExtra(Intent.EXTRA_EMAIL,
                                new String[] { "954828748@qq.com" });
                        i.putExtra(Intent.EXTRA_SUBJECT, subject);
                        i.putExtra(Intent.EXTRA_TEXT, crashReport);

                        cont.startActivity(Intent.createChooser(i, "发送错误报告"));
                        // �?出应用程�?
                        ActivityManager.getInstance().finishAllActivity();
                        android.os.Process.killProcess(android.os.Process
                                .myPid());
                        System.exit(1);
                    }
                });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        ActivityManager.getInstance().finishAllActivity();
                        android.os.Process.killProcess(android.os.Process
                                .myPid());
                        System.exit(1);
                    }
                });
        builder.create().show();
    }

    /**
     * 获取APP崩溃异常报告
     * 
     * @param ex
     * @return
     */
    private String getCrashReport(int versionCode, String versionName,
            Context context, Throwable ex)
    {
        StringBuffer exceptionStr = new StringBuffer();
        exceptionStr.append("Version: " + versionName + "(" + versionCode
                + ")\n");
        exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE
                + "(" + android.os.Build.MODEL + ")\n");
        exceptionStr.append("Exception: " + (ex == null ? "" : ex.getMessage())
                + "\n");
        if (ex != null)
        {
            StackTraceElement[] elements = ex.getStackTrace();
            for (int i = 0; i < elements.length; i++)
            {
                exceptionStr.append(elements[i].toString() + "\n");
            }
        }

        return exceptionStr.toString();
    }

    public PackageInfo getDeviceInfo(Context ctx)
    {

        PackageInfo pi = null;
        try
        {
            PackageManager pm = ctx.getPackageManager();
            pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
        }
        catch(NameNotFoundException e)
        {
        }
        return pi;
    }

}
