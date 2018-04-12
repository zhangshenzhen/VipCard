package com.bjypt.vipcard.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.h5.AndroidApi;
import com.bjypt.vipcard.h5.JavaScriptListener;
import com.sinia.orderlang.utils.StringUtil;

import java.io.File;

/**
 * Created by WANG427 on 2016/3/22.
 */

public abstract class BaseWebViewActivity extends Activity implements JavaScriptListener {

    public ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;
    public final static int FILECHOOSER_RESULTCODE = 1;
    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;
    public int ANDROID_Version = 5;

    public final static int PHOTO_RESULTCODE = 3;
    public final static int PHOTO_RESULTCODE_FOR_ANDROID_5 = 4;


    public static final String PUSHINFO = "webviewpushinfo";
    public boolean isResumeRefresh = false;
    protected WebView webView;
    protected AndroidApi androidApi;

    private Dialog fileDialog;
    private int request_result_code;
    private Uri fileUri;
    private int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
//    PermissionsHelper permissionsHelperCamera;
//    PermissionsHelper permissionsHelperStorage;

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        permissionsHelperCamera = new PermissionsHelper(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        permissionsHelperStorage = new PermissionsHelper(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void setTitle(String title) {
        TextView titleTv = ((TextView) findViewById(R.id.tv_title));
        if (titleTv != null) {
            titleTv.setVisibility(View.VISIBLE);
            titleTv.setText(title);
        }
    }


    public void init(WebView v) {
        webView = v;
        webView.setVerticalScrollbarOverlay(true);
        //设置WebView支持JavaScript
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setSupportMultipleWindows(true);
        ws.setGeolocationEnabled(true);
        ws.setGeolocationDatabasePath(getFilesDir().getPath());
        webView.requestFocus();
//        webView.getSettings().setJavaScriptEnabled(true);//添加对JavaScript支持
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            ws.setAllowFileAccessFromFileURLs(true);
//        }
//        ws.setAppCacheEnabled(true);
//        androidApi = new AndroidApi(this, this);
//        webView.setWebChromeClient(new WebChromeClient() {
//            //扩展浏览器上传文件
//            //3.0++版本
//            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
//                ANDROID_Version = 3;
//                mUploadMessage = uploadMsg;
//                showFileDialog();
////                openFileChooserImpl(uploadMsg);
//            }
//
//            //3.0--版本
//            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//                ANDROID_Version = 3;
//                mUploadMessage = uploadMsg;
//                showFileDialog();
////                openFileChooserImpl(uploadMsg);
//            }
//
//            // Android  > 4.1.1
//            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//                ANDROID_Version = 3;
//                mUploadMessage = uploadMsg;
//                showFileDialog();
////                openFileChooserImpl(uploadMsg);
//            }
//
//            // For Android > 5.0
//            @Override
//            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
////                openFileChooserImplForAndroid5(uploadMsg);
//                ANDROID_Version = 5;
//                mUploadMessageForAndroid5 = uploadMsg;
//                showFileDialog();
//                return true;
//            }
//        });
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(final WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.loadUrl("file:///android_asset/index.html");
            }


        });

        loadUrl();
        //在js中调用本地java方法
        webView.addJavascriptInterface(androidApi, "AndroidApi");
    }

    private void loadUrl() {
        webView.loadUrl(getUrl());
    }

//    private void openFileChooserImpl() {
//        Intent i = new Intent(Intent.ACTION_PICK);
////        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("image/*");
//        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
//    }

//    private void openFileChooserImplForAndroid5() {
//        Intent contentSelectionIntent = new Intent(Intent.ACTION_PICK);
//        contentSelectionIntent.setType("image/*");
//        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
//        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
//        chooserIntent.putExtra(Intent.EXTRA_TITLE, "图片选择");
//        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
//        showFileDialog();
//    }

//    private void showFileDialog() {
//        if (fileDialog != null && fileDialog.isShowing()) {
//            return;
//        }
//        fileDialog = new Dialog(this, R.style.my_dialog);
//        fileDialog.setContentView(R.layout.upload_file_dialog);
//        Window window = fileDialog.getWindow();
//        window.setGravity(Gravity.BOTTOM);
//        window.setWindowAnimations(R.style.dialog_enter_animation);
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        Button btn_dismiss = (Button) fileDialog.findViewById(R.id.btn_dismiss);
//        btn_dismiss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (fileDialog != null) {
//                    fileDialog.dismiss();
//                    fileDialog = null;
//                }
//            }
//        });
//
//        Button btn_add_phone = (Button) fileDialog.findViewById(R.id.btn_add_phone);
//        btn_add_phone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (ANDROID_Version == 5) {
//                    openPhotoForAndroid5();
//                } else {
//                    openPhotoForAndroid();
//                }
//
//
//            }
//        });
//
//        Button btn_add_image = (Button) fileDialog.findViewById(R.id.btn_add_image);
//        //根据系统版本选择图片
//        btn_add_image.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (ANDROID_Version == 5) {
//                    openFileChooserImplForAndroid5();
//                } else {
//                    openFileChooserImpl();
//                }
//            }
//        });
//
//        //弹出框注销
//        fileDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                if (ANDROID_Version == 5) {
//                    if (mUploadMessageForAndroid5 == null) return;
//                    mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
//                    mUploadMessageForAndroid5 = null;
//                } else {
//                    if (mUploadMessage == null) return;
//                    mUploadMessage.onReceiveValue(null);
//                    mUploadMessage = null;
//                }
//            }
//        });
//        fileDialog.show();
//    }

//    private void openPhotoForAndroid5() {
//        permissionsHelperCamera.onPermissionGranted("系统需要访问本地存储的权限，请接受相关授权", new PermissionsHelper.PermissionsCallBack() {
//            @Override
//            public void callBack() {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
//                File file = getOutputMediaFileUri();
//                fileUri = Uri.fromFile(file);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                startActivityForResult(intent, PHOTO_RESULTCODE_FOR_ANDROID_5);
//            }
//        });
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (fileDialog != null && fileDialog.isShowing()) {
//            fileDialog.dismiss();
//        }
    }


    private void openPhotoForAndroid() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
        File file = getOutputMediaFileUri();
        fileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, PHOTO_RESULTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        request_result_code = resultCode;
        if (requestCode == FILECHOOSER_RESULTCODE) {//图片选择回调
            if (null == mUploadMessage) return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {//图片选择回调Android 5
            if (null == mUploadMessageForAndroid5) return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        } else if (requestCode == PHOTO_RESULTCODE) {//拍照回调
            Uri result = (resultCode != RESULT_OK) ? null : fileUri;
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else if (requestCode == PHOTO_RESULTCODE_FOR_ANDROID_5) {//拍照回调Android 5
            if (null == mUploadMessageForAndroid5) return;
            Uri result = (resultCode != RESULT_OK) ? null : fileUri;
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
        fileDialog.dismiss();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isResumeRefresh) {
            webView.reload();
        }
    }

    @Override
    public void isResumeRefresh(boolean isRefresh) {
        isResumeRefresh = isRefresh;
    }

    public abstract String getUrl();

    public void setRightBtnInfo(String btntext, String iconName, final String btnevent) {
        TextView tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        if (StringUtil.isNotEmpty(btntext)) {
            tv_right.setText(btntext);
        }
//        else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                tv_right.setBackground(getResources().getDrawable(ResourceUtil.getDrableId(iconName)));
//            }
//        }
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:" + btnevent + "");
            }
        });


    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFileUri() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = null;
        try {
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "photo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG.jpg");
        if (mediaFile.exists()) {
            mediaFile.delete();
        }
        return mediaFile;
    }

    @Override
    public void webViewReload() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadUrl();
            }
        });
    }


}
