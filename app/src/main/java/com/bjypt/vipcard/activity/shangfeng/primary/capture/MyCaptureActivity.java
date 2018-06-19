package com.bjypt.vipcard.activity.shangfeng.primary.capture;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.google.zxing.Result;
import com.jwsd.libzxing.activity.CaptureActivity;
import com.jwsd.libzxing.camera.CameraManager;
import com.jwsd.libzxing.decode.DecodeBitmap;
import com.jwsd.libzxing.utils.BeepManager;
import com.jwsd.libzxing.utils.InactivityTimer;
import com.jwsd.libzxing.utils.SelectAlbumUtils;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by wanglei on 2018/5/30.
 */

public class MyCaptureActivity  extends Activity implements SurfaceHolder.Callback, View.OnClickListener {
    private static final String TAG = CaptureActivity.class.getSimpleName();
    public static final int RESULT_MULLT = 5;
    private CameraManager cameraManager;
    private MyCaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private SurfaceView scanPreview = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;
    private Rect mCropRect = null;
    private boolean isHasSurface = false;
    private ImageView ivBack;
    private ImageView ivMullt;
    private int captureType = 0;
    private TextView tvAlbum;
    private static final int CODE_GALLERY_REQUEST = 101;
    private DialogInterface.OnClickListener mOnClickListener;

    public Handler getHandler() {
        return this.handler;
    }

    public CameraManager getCameraManager() {
        return this.cameraManager;
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.requestWindowFeature(1);
        Window window = this.getWindow();
        window.addFlags(128);
        this.setContentView(R.layout.activity_my_capture);
        this.captureType = this.getIntent().getIntExtra("type", 0);
//        String string = this.getApplication().getResources().getString(string.jwstr_scan_it);
        this.scanPreview = (SurfaceView)this.findViewById(com.jwsd.libzxing.R.id.capture_preview);
        this.scanContainer = (RelativeLayout)this.findViewById(com.jwsd.libzxing.R.id.capture_container);
        this.scanCropView = (RelativeLayout)this.findViewById(com.jwsd.libzxing.R.id.capture_crop_view);
        this.scanLine = (ImageView)this.findViewById(com.jwsd.libzxing.R.id.capture_scan_line);
        this.ivBack = (ImageView)this.findViewById(com.jwsd.libzxing.R.id.iv_back);
        this.ivMullt = (ImageView)this.findViewById(com.jwsd.libzxing.R.id.iv_mudle);
        this.tvAlbum = (TextView)this.findViewById(com.jwsd.libzxing.R.id.tv_capture_select_album_jwsd);
        this.ivBack.setTag(Integer.valueOf(123));
        this.ivMullt.setTag(Integer.valueOf(124));
        this.tvAlbum.setTag(Integer.valueOf(125));
        this.tvAlbum.setOnClickListener(this);
        this.ivBack.setOnClickListener(this);
        this.ivMullt.setOnClickListener(this);
        this.inactivityTimer = new InactivityTimer(this);
        this.beepManager = new BeepManager(this);
        if(this.captureType == 1) {
            this.ivMullt.setVisibility(View.GONE);
        }

        TranslateAnimation animation = new TranslateAnimation(2, 0.0F, 2, 0.0F, 2, 0.0F, 2, 1.0F);
        animation.setDuration(4500L);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(1);
        this.scanLine.startAnimation(animation);
        TextView textView =(TextView)findViewById(R.id.tv_merchant_title_name);
        textView.setText(getString(R.string.scan_merchant_pay));

        findViewById(R.id.ibtn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        this.cameraManager = new CameraManager(this.getApplication());
        this.handler = null;
        if(this.isHasSurface) {
            this.initCamera(this.scanPreview.getHolder());
        } else {
            this.scanPreview.getHolder().addCallback(this);
        }

        this.inactivityTimer.onResume();
    }

    protected void onPause() {
        if(this.handler != null) {
            this.handler.quitSynchronously();
            this.handler = null;
        }

        this.inactivityTimer.onPause();
        this.beepManager.close();
        this.cameraManager.closeDriver();
        if(!this.isHasSurface) {
            this.scanPreview.getHolder().removeCallback(this);
        }

        super.onPause();
    }

    protected void onDestroy() {
        this.inactivityTimer.shutdown();
        super.onDestroy();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if(holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }

        if(!this.isHasSurface) {
            this.isHasSurface = true;
            this.initCamera(holder);
        }

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.isHasSurface = false;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void handleDecode(Result rawResult, Bundle bundle) {
        this.inactivityTimer.onActivity();
        this.beepManager.playBeepSoundAndVibrate();
        if(this.captureType == 0) {
            Intent resultIntent = new Intent();
            bundle.putInt("width", this.mCropRect.width());
            bundle.putInt("height", this.mCropRect.height());
            bundle.putString("result", rawResult.getText());
            resultIntent.putExtras(bundle);
            this.setResult(-1, resultIntent);
            this.finish();
        } else {
            this.scanDeviceSuccess(rawResult.toString(), bundle);
        }

    }

    private void scanDeviceSuccess(String rawResult, Bundle bundle) {
        Intent resultIntent = new Intent();
        bundle.putString("result", rawResult);
        resultIntent.putExtras(bundle);
        this.setResult(-1, resultIntent);
        this.finish();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if(surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        } else if(this.cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
        } else {
            try {
                this.cameraManager.openDriver(surfaceHolder);
                if(this.handler == null) {
                    this.handler = new MyCaptureActivityHandler(this, this.cameraManager, 768);
                }

                this.initCrop();
            } catch (IOException var3) {
                Log.w(TAG, var3);
                this.displayFrameworkBugMessageAndExit();
            } catch (RuntimeException var4) {
                Log.w(TAG, "Unexpected error initializing camera", var4);
                this.displayFrameworkBugMessageAndExit();
            }

        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getString(com.jwsd.libzxing.R.string.jwstr_prompt));
        builder.setMessage(this.getString(com.jwsd.libzxing.R.string.jwstr_camera_error));
        builder.setPositiveButton(this.getString(com.jwsd.libzxing.R.string.jwstr_confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MyCaptureActivity.this.finish();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                MyCaptureActivity.this.finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if(this.handler != null) {
            this.handler.sendEmptyMessageDelayed(com.jwsd.libzxing.R.id.jwstr_restart_preview, delayMS);
        }

    }

    public Rect getCropRect() {
        return this.mCropRect;
    }

    private void initCrop() {
        int cameraWidth = this.cameraManager.getCameraResolution().y;
        int cameraHeight = this.cameraManager.getCameraResolution().x;
        int[] location = new int[2];
        this.scanCropView.getLocationInWindow(location);
        int cropLeft = location[0];
        int cropTop = location[1] - this.getStatusBarHeight();
        int cropWidth = this.scanCropView.getWidth();
        int cropHeight = this.scanCropView.getHeight();
        int containerWidth = this.scanContainer.getWidth();
        int containerHeight = this.scanContainer.getHeight();
        int x = cropLeft * cameraWidth / containerWidth;
        int y = cropTop * cameraHeight / containerHeight;
        int width = cropWidth * cameraWidth / containerWidth;
        int height = cropHeight * cameraHeight / containerHeight;
        this.mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return this.getResources().getDimensionPixelSize(x);
        } catch (Exception var5) {
            var5.printStackTrace();
            return 0;
        }
    }

    public void onClick(View v) {
        Intent mullt;
        switch(Integer.parseInt(v.getTag().toString())) {
            case 123:
                Intent resultIntent = new Intent();
                this.setResult(0, resultIntent);
                this.finish();
                break;
            case 124:
                if(this.captureType == 0) {
                    mullt = new Intent();
                    this.setResult(5, mullt);
                    this.finish();
                } else {
                    mullt = new Intent();
                    this.setResult(0, mullt);
                    this.finish();
                }
                break;
            case 125:
                mullt = new Intent("android.intent.action.GET_CONTENT");
                mullt.addCategory("android.intent.category.OPENABLE");
                mullt.setType("image/*");
                this.startActivityForResult(mullt, 101);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == -1 && requestCode == 101) {
            String picPath = SelectAlbumUtils.getPicPath(this, data);
            Result result = DecodeBitmap.scanningImage(picPath);
            if(result == null) {
                Toast.makeText(this, this.getString(com.jwsd.libzxing.R.string.jwstr_pic_no_qrcode), 0).show();
            } else {
                this.beepManager.playBeepSoundAndVibrate();
                String scanResult = DecodeBitmap.parseReuslt(result.toString());
                this.scanDeviceSuccess(scanResult, new Bundle());
            }
        }

    }
}
