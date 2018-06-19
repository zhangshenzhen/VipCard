package com.bjypt.vipcard.activity.shangfeng.primary.capture;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.zxing.Result;
import com.jwsd.libzxing.camera.CameraManager;

/**
 * Created by wanglei on 2018/5/30.
 */

public class MyCaptureActivityHandler extends Handler {
    private final MyCaptureActivity activity;
    private final DecodeThread decodeThread;
    private final CameraManager cameraManager;
    private MyCaptureActivityHandler.State state;

    public MyCaptureActivityHandler(MyCaptureActivity activity, CameraManager cameraManager, int decodeMode) {
        this.activity = activity;
        this.decodeThread = new DecodeThread(activity, decodeMode);
        this.decodeThread.start();
        this.state = MyCaptureActivityHandler.State.SUCCESS;
        this.cameraManager = cameraManager;
        cameraManager.startPreview();
        this.restartPreviewAndDecode();
    }

    public void handleMessage(Message message) {
        if(message.what == com.jwsd.libzxing.R.id.jwstr_restart_preview) {
            this.restartPreviewAndDecode();
        } else if(message.what == com.jwsd.libzxing.R.id.jwstr_decode_succeeded) {
            this.state = MyCaptureActivityHandler.State.SUCCESS;
            Bundle bundle = message.getData();
            this.activity.handleDecode((Result)message.obj, bundle);
        } else if(message.what == com.jwsd.libzxing.R.id.jwstr_decode_failed) {
            this.state = MyCaptureActivityHandler.State.PREVIEW;
            this.cameraManager.requestPreviewFrame(this.decodeThread.getHandler(), com.jwsd.libzxing.R.id.jwstr_decode);
        } else if(message.what == com.jwsd.libzxing.R.id.jwstr_return_scan_result) {
            this.activity.setResult(-1, (Intent)message.obj);
            this.activity.finish();
        }

    }

    public void quitSynchronously() {
        this.state = MyCaptureActivityHandler.State.DONE;
        this.cameraManager.stopPreview();
        Message quit = Message.obtain(this.decodeThread.getHandler(), com.jwsd.libzxing.R.id.jwstr_quit);
        quit.sendToTarget();

        try {
            this.decodeThread.join(500L);
        } catch (InterruptedException var3) {
            ;
        }

        this.removeMessages(com.jwsd.libzxing.R.id.jwstr_decode_succeeded);
        this.removeMessages(com.jwsd.libzxing.R.id.jwstr_decode_failed);
    }

    private void restartPreviewAndDecode() {
        if(this.state == MyCaptureActivityHandler.State.SUCCESS) {
            this.state = MyCaptureActivityHandler.State.PREVIEW;
            this.cameraManager.requestPreviewFrame(this.decodeThread.getHandler(), com.jwsd.libzxing.R.id.jwstr_decode);
        }

    }

    private static enum State {
        PREVIEW,
        SUCCESS,
        DONE;

        private State() {
        }
    }
}
