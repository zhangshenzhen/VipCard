package com.bjypt.vipcard.activity.shangfeng.primary.capture;

import android.os.Handler;
import android.os.Looper;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.jwsd.libzxing.decode.DecodeFormatManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by wanglei on 2018/5/30.
 */

public class DecodeThread extends Thread {
    public static final String BARCODE_BITMAP = "barcode_bitmap";
    public static final int BARCODE_MODE = 256;
    public static final int QRCODE_MODE = 512;
    public static final int ALL_MODE = 768;
    private final MyCaptureActivity activity;
    private final Map<DecodeHintType, Object> hints;
    private final CountDownLatch handlerInitLatch;
    private Handler handler;

    public DecodeThread(MyCaptureActivity activity, int decodeMode) {
        this.activity = activity;
        this.handlerInitLatch = new CountDownLatch(1);
        this.hints = new EnumMap(DecodeHintType.class);
        Collection<BarcodeFormat> decodeFormats = new ArrayList();
        decodeFormats.addAll(EnumSet.of(BarcodeFormat.AZTEC));
        decodeFormats.addAll(EnumSet.of(BarcodeFormat.PDF_417));
        switch(decodeMode) {
            case 256:
                decodeFormats.addAll(DecodeFormatManager.getBarCodeFormats());
                break;
            case 512:
                decodeFormats.addAll(DecodeFormatManager.getQrCodeFormats());
                break;
            case 768:
                decodeFormats.addAll(DecodeFormatManager.getBarCodeFormats());
                decodeFormats.addAll(DecodeFormatManager.getQrCodeFormats());
        }

        this.hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
    }

    public Handler getHandler() {
        try {
            this.handlerInitLatch.await();
        } catch (InterruptedException var2) {
            ;
        }

        return this.handler;
    }

    public void run() {
        Looper.prepare();
        this.handler = new DecodeHandler(this.activity, this.hints);
        this.handlerInitLatch.countDown();
        Looper.loop();
    }
}
