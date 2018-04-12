package com.bjypt.vipcard.model;

/**
 * Created by 崔龙 on 2017/9/28.
 */

public class UploadPicResultItem {
    /**
     * filename : 20170920231246-468012304_800_800.jpg
     * attachment : https://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/6880615001394e33a038f574eb3b780a.jpg
     * filesize : 29957
     */

    private String filename;
    private String attachment;
    private int filesize;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public int getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    @Override
    public String toString() {
        return "UploadPicResultItem{" +
                "filename='" + filename + '\'' +
                ", attachment='" + attachment + '\'' +
                ", filesize=" + filesize +
                '}';
    }
}
