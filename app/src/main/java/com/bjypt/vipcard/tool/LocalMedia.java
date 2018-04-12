package com.bjypt.vipcard.tool;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by 崔龙 on 2017/9/28.
 */

public class LocalMedia implements Serializable {
    private String path;
    private String compressPath;
    private String cutPath;
    private long duration;
    private boolean isChecked;
    private boolean isCut;
    public int position;
    private int num;
    private int mimeType;
    private String pictureType;
    private boolean compressed;
    private int width;
    private int height;

    public LocalMedia() {
    }

    public LocalMedia(String path, long duration, int mimeType, String pictureType) {
        this.path = path;
        this.duration = duration;
        this.mimeType = mimeType;
        this.pictureType = pictureType;
    }

    public LocalMedia(String path, long duration, int mimeType, String pictureType, int width, int height) {
        this.path = path;
        this.duration = duration;
        this.mimeType = mimeType;
        this.pictureType = pictureType;
        this.width = width;
        this.height = height;
    }

    public LocalMedia(String path, long duration, boolean isChecked, int position, int num, int mimeType) {
        this.path = path;
        this.duration = duration;
        this.isChecked = isChecked;
        this.position = position;
        this.num = num;
        this.mimeType = mimeType;
    }

    public String getPictureType() {
        if(TextUtils.isEmpty(this.pictureType)) {
            this.pictureType = "image/jpeg";
        }

        return this.pictureType;
    }

    public void setPictureType(String pictureType) {
        this.pictureType = pictureType;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCompressPath() {
        return this.compressPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

    public String getCutPath() {
        return this.cutPath;
    }

    public void setCutPath(String cutPath) {
        this.cutPath = cutPath;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    public boolean isCut() {
        return this.isCut;
    }

    public void setCut(boolean cut) {
        this.isCut = cut;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getNum() {
        return this.num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(int mimeType) {
        this.mimeType = mimeType;
    }

    public boolean isCompressed() {
        return this.compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
