package com.bjypt.vipcard.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.io.File;

/**
 * Created by User on 2016/5/18.
 */
public class FileUtil {

    public static String apkSavePath = "/sdcard/update";
    public static String apkSaveName = "VipCard.apk";

    public static boolean queryIsExistAPK(String apkFilename) {
        if (!(new File(apkFilename)).exists()) {
            Log.i("aaa", "输出路径=" + apkFilename + "-----//存在该文件");
            return true;
        }
        return false;
    }

    public static void openFile(Context context,File file) {
        if (file != null) {
            Log.e("OpenFile", file.getName());
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);

        } else {
            Log.i("aaa", "文件不存在--->FileUtil>openFile");
        }
        // TODO Auto-generated method stub

    }
    public static String getUninstallAPKInfo(Context ctx, String archiveFilePath) {
        //archiveFilePath=Environment.getExternalStorageDirectory()+"/"+"TestB.apk"
        String versionName = null;
//        String appName = null;
//        String pakName = null;
        PackageManager pm = ctx.getPackageManager();
        PackageInfo pakinfo = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        if (pakinfo != null) {
            ApplicationInfo appinfo = pakinfo.applicationInfo;
            versionName = pakinfo.versionName;
//            Drawable icon = pm.getApplicationIcon(appinfo);//图标
//            appName = (String) pm.getApplicationLabel(appinfo);//apk名
//            pakName = appinfo.packageName;
        }
        return versionName;
    }
}
