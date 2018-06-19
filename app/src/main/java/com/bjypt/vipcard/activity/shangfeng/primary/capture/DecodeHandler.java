package com.bjypt.vipcard.activity.shangfeng.primary.capture;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * Created by wanglei on 2018/5/30.
 */

public class DecodeHandler  extends Handler {
    private final MyCaptureActivity activity;
    private final MultiFormatReader multiFormatReader = new MultiFormatReader();
    private boolean running = true;

    public DecodeHandler(MyCaptureActivity activity, Map<DecodeHintType, Object> hints) {
        this.multiFormatReader.setHints(hints);
        this.activity = activity;
    }

    private static void bundleThumbnail(PlanarYUVLuminanceSource source, Bundle bundle) {
        int[] pixels = source.renderThumbnail();
        int width = source.getThumbnailWidth();
        int height = source.getThumbnailHeight();
        Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        bundle.putByteArray("barcode_bitmap", out.toByteArray());
    }

    public void handleMessage(Message message) {
        if(this.running) {
            if(message.what == com.jwsd.libzxing.R.id.jwstr_decode) {
                try {
                    this.decode((byte[])((byte[])message.obj), message.arg1, message.arg2);
                } catch (Throwable var3) {
                    ;
                }
            } else if(message.what == com.jwsd.libzxing.R.id.jwstr_quit) {
                this.running = false;
                Looper.myLooper().quit();
            }

        }
    }

    private void decode(byte[] data, int width, int height) {
        Camera.Size size = this.activity.getCameraManager().getPreviewSize();
        byte[] rotatedData = new byte[data.length];

        int y;
        for(y = 0; y < size.height; ++y) {
            for(int x = 0; x < size.width; ++x) {
                rotatedData[x * size.height + size.height - y - 1] = data[x + y * size.width];
            }
        }

        y = size.width;
        size.width = size.height;
        size.height = y;
        Result rawResult = null;
        PlanarYUVLuminanceSource source = this.buildLuminanceSource(rotatedData, size.width, size.height);
        if(source != null) {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            try {
                rawResult = this.multiFormatReader.decodeWithState(bitmap);
            } catch (ReaderException var14) {
                ;
            } finally {
                this.multiFormatReader.reset();
            }
        }

        Handler handler = this.activity.getHandler();
        Message message;
        if(rawResult != null) {
            if(handler != null) {
                message = Message.obtain(handler, com.jwsd.libzxing.R.id.jwstr_decode_succeeded, rawResult);
                Bundle bundle = new Bundle();
                bundleThumbnail(source, bundle);
                message.setData(bundle);
                message.sendToTarget();
            }
        } else if(handler != null) {
            message = Message.obtain(handler, com.jwsd.libzxing.R.id.jwstr_decode_failed);
            message.sendToTarget();
        }

    }

    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        Rect rect = this.activity.getCropRect();
        return rect == null?null:new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height(), false);
    }
}
