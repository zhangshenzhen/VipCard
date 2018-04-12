package com.bjypt.vipcard.tool;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.StyleRes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 崔龙 on 2017/9/28.
 */

public class PictureSelectionModel {
    private PictureSelectionConfig selectionConfig;
    private PictureSelector selector;

    public PictureSelectionModel(PictureSelector selector, int mimeType) {
        this.selector = selector;
        this.selectionConfig = PictureSelectionConfig.getCleanInstance();
        this.selectionConfig.mimeType = mimeType;
    }

    public PictureSelectionModel(PictureSelector selector, int mimeType, boolean camera) {
        this.selector = selector;
        this.selectionConfig = PictureSelectionConfig.getCleanInstance();
        this.selectionConfig.camera = camera;
        this.selectionConfig.mimeType = mimeType;
    }

    public PictureSelectionModel theme(@StyleRes int themeStyleId) {
        this.selectionConfig.themeStyleId = themeStyleId;
        return this;
    }

    public PictureSelectionModel selectionMode(int selectionMode) {
        this.selectionConfig.selectionMode = selectionMode;
        return this;
    }

    public PictureSelectionModel enableCrop(boolean enableCrop) {
        this.selectionConfig.enableCrop = enableCrop;
        return this;
    }

    public PictureSelectionModel enablePreviewAudio(boolean enablePreviewAudio) {
        this.selectionConfig.enablePreviewAudio = enablePreviewAudio;
        return this;
    }

    public PictureSelectionModel freeStyleCropEnabled(boolean freeStyleCropEnabled) {
        this.selectionConfig.freeStyleCropEnabled = freeStyleCropEnabled;
        return this;
    }

    public PictureSelectionModel scaleEnabled(boolean scaleEnabled) {
        this.selectionConfig.scaleEnabled = scaleEnabled;
        return this;
    }

    public PictureSelectionModel rotateEnabled(boolean rotateEnabled) {
        this.selectionConfig.rotateEnabled = rotateEnabled;
        return this;
    }

    public PictureSelectionModel circleDimmedLayer(boolean circleDimmedLayer) {
        this.selectionConfig.circleDimmedLayer = circleDimmedLayer;
        return this;
    }

    public PictureSelectionModel showCropFrame(boolean showCropFrame) {
        this.selectionConfig.showCropFrame = showCropFrame;
        return this;
    }

    public PictureSelectionModel showCropGrid(boolean showCropGrid) {
        this.selectionConfig.showCropGrid = showCropGrid;
        return this;
    }

    public PictureSelectionModel hideBottomControls(boolean hideBottomControls) {
        this.selectionConfig.hideBottomControls = hideBottomControls;
        return this;
    }

    public PictureSelectionModel withAspectRatio(int aspect_ratio_x, int aspect_ratio_y) {
        this.selectionConfig.aspect_ratio_x = aspect_ratio_x;
        this.selectionConfig.aspect_ratio_y = aspect_ratio_y;
        return this;
    }

    public PictureSelectionModel maxSelectNum(int maxSelectNum) {
        this.selectionConfig.maxSelectNum = maxSelectNum;
        return this;
    }

    public PictureSelectionModel minSelectNum(int minSelectNum) {
        this.selectionConfig.minSelectNum = minSelectNum;
        return this;
    }

    public PictureSelectionModel videoQuality(int videoQuality) {
        this.selectionConfig.videoQuality = videoQuality;
        return this;
    }

    public PictureSelectionModel cropWH(int cropWidth, int cropHeight) {
        this.selectionConfig.cropWidth = cropWidth;
        this.selectionConfig.cropHeight = cropHeight;
        return this;
    }

    public PictureSelectionModel videoSecond(int videoSecond) {
        this.selectionConfig.videoSecond = videoSecond * 1000;
        return this;
    }

    public PictureSelectionModel recordVideoSecond(int recordVideoSecond) {
        this.selectionConfig.recordVideoSecond = recordVideoSecond;
        return this;
    }

    public PictureSelectionModel glideOverride(@IntRange(from = 100L) int width, @IntRange(from = 100L) int height) {
        this.selectionConfig.overrideWidth = width;
        this.selectionConfig.overrideHeight = height;
        return this;
    }

    public PictureSelectionModel sizeMultiplier(@FloatRange(from = 0.10000000149011612D) float sizeMultiplier) {
        this.selectionConfig.sizeMultiplier = sizeMultiplier;
        return this;
    }

    public PictureSelectionModel imageSpanCount(int imageSpanCount) {
        this.selectionConfig.imageSpanCount = imageSpanCount;
        return this;
    }

    public PictureSelectionModel compressMode(int compressMode) {
        this.selectionConfig.compressMode = compressMode;
        return this;
    }

    public PictureSelectionModel compressWH(int width, int height) {
        this.selectionConfig.compressWidth = width;
        this.selectionConfig.compressHeight = height;
        return this;
    }

    public PictureSelectionModel compressMaxKB(int kb) {
        this.selectionConfig.compressMaxkB = kb * 1024;
        return this;
    }

    public PictureSelectionModel cropCompressQuality(int compressQuality) {
        this.selectionConfig.cropCompressQuality = compressQuality;
        return this;
    }

    public PictureSelectionModel compressGrade(int compressGrade) {
        this.selectionConfig.compressGrade = compressGrade;
        return this;
    }

    public PictureSelectionModel compress(boolean isCompress) {
        this.selectionConfig.isCompress = isCompress;
        return this;
    }

    public PictureSelectionModel isZoomAnim(boolean zoomAnim) {
        this.selectionConfig.zoomAnim = zoomAnim;
        return this;
    }

    public PictureSelectionModel previewEggs(boolean previewEggs) {
        this.selectionConfig.previewEggs = previewEggs;
        return this;
    }

    public PictureSelectionModel isCamera(boolean isCamera) {
        this.selectionConfig.isCamera = isCamera;
        return this;
    }

    public PictureSelectionModel setOutputCameraPath(String outputCameraPath) {
        this.selectionConfig.outputCameraPath = outputCameraPath;
        return this;
    }

    public PictureSelectionModel isGif(boolean isGif) {
        this.selectionConfig.isGif = isGif;
        return this;
    }

    public PictureSelectionModel previewImage(boolean enablePreview) {
        this.selectionConfig.enablePreview = enablePreview;
        return this;
    }

    public PictureSelectionModel previewVideo(boolean enPreviewVideo) {
        this.selectionConfig.enPreviewVideo = enPreviewVideo;
        return this;
    }

    public PictureSelectionModel openClickSound(boolean openClickSound) {
        this.selectionConfig.openClickSound = openClickSound;
        return this;
    }

    public PictureSelectionModel selectionMedia(List<LocalMedia> selectionMedia) {
        if(selectionMedia == null) {
            selectionMedia = new ArrayList();
        }

        this.selectionConfig.selectionMedias = (List)selectionMedia;
        return this;
    }

//    public void forResult(int requestCode) {
//        if(!DoubleUtils.isFastDoubleClick()) {
//            Activity activity = this.selector.getActivity();
//            if(activity == null) {
//                return;
//            }
//
//            Intent intent = new Intent(activity, PictureSelectorActivity.class);
//            Fragment fragment = this.selector.getFragment();
//            if(fragment != null) {
//                fragment.startActivityForResult(intent, requestCode);
//            } else {
//                activity.startActivityForResult(intent, requestCode);
//            }
//
//            activity.overridePendingTransition(anim.a5, 0);
//        }
//
//    }
}
