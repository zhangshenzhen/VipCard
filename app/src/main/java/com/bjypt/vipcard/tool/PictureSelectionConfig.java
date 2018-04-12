package com.bjypt.vipcard.tool;

import android.support.annotation.StyleRes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 崔龙 on 2017/9/28.
 */

public class PictureSelectionConfig implements Serializable {
    public int mimeType;
    public boolean camera;
    public String outputCameraPath;
    @StyleRes
    public int themeStyleId;
    public int selectionMode;
    public int maxSelectNum;
    public int minSelectNum;
    public int videoQuality;
    public int cropCompressQuality;
    public int videoSecond;
    public int recordVideoSecond;
    public int compressMaxkB;
    public int compressGrade;
    public int imageSpanCount;
    public int compressMode;
    public int compressWidth;
    public int compressHeight;
    public int overrideWidth;
    public int overrideHeight;
    public int aspect_ratio_x;
    public int aspect_ratio_y;
    public float sizeMultiplier;
    public int cropWidth;
    public int cropHeight;
    public boolean zoomAnim;
    public boolean isCompress;
    public boolean isCamera;
    public boolean isGif;
    public boolean enablePreview;
    public boolean enPreviewVideo;
    public boolean enablePreviewAudio;
    public boolean checkNumMode;
    public boolean openClickSound;
    public boolean enableCrop;
    public boolean freeStyleCropEnabled;
    public boolean circleDimmedLayer;
    public boolean showCropFrame;
    public boolean showCropGrid;
    public boolean hideBottomControls;
    public boolean rotateEnabled;
    public boolean scaleEnabled;
    public boolean previewEggs;
    public List<LocalMedia> selectionMedias;

    public PictureSelectionConfig() {
    }

    private void reset() {
        this.mimeType = 1;
        this.camera = false;
//        this.themeStyleId = style.picture_default_style;
        this.selectionMode = 2;
        this.maxSelectNum = 9;
        this.minSelectNum = 0;
        this.videoQuality = 1;
        this.cropCompressQuality = 90;
        this.videoSecond = 0;
        this.recordVideoSecond = 60;
        this.compressMaxkB = 102400;
        this.compressGrade = 3;
        this.imageSpanCount = 4;
        this.compressMode = 1;
        this.compressWidth = 0;
        this.compressHeight = 0;
        this.overrideWidth = 0;
        this.overrideHeight = 0;
        this.isCompress = false;
        this.aspect_ratio_x = 0;
        this.aspect_ratio_y = 0;
        this.cropWidth = 0;
        this.cropHeight = 0;
        this.isCamera = true;
        this.isGif = false;
        this.enablePreview = true;
        this.enPreviewVideo = true;
        this.enablePreviewAudio = true;
        this.checkNumMode = false;
        this.openClickSound = false;
        this.enableCrop = false;
        this.freeStyleCropEnabled = false;
        this.circleDimmedLayer = false;
        this.showCropFrame = true;
        this.showCropGrid = true;
        this.hideBottomControls = true;
        this.rotateEnabled = true;
        this.scaleEnabled = true;
        this.previewEggs = false;
        this.zoomAnim = true;
        this.outputCameraPath = "";
        this.sizeMultiplier = 0.5F;
        this.selectionMedias = new ArrayList();
//        DebugUtil.i("*******", "reset PictureSelectionConfig");
    }

    public static PictureSelectionConfig getInstance() {
        return PictureSelectionConfig.InstanceHolder.INSTANCE;
    }

    public static PictureSelectionConfig getCleanInstance() {
        PictureSelectionConfig selectionSpec = getInstance();
        selectionSpec.reset();
        return selectionSpec;
    }

    private static final class InstanceHolder {
        private static final PictureSelectionConfig INSTANCE = new PictureSelectionConfig();

        private InstanceHolder() {
        }
    }
}
