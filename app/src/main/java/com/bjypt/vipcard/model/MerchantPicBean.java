package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/18
 * Use by商家图片
 */
public class MerchantPicBean {
    private String msg;
    private String resultStatus;
    private ImageViewBean resultData;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public ImageViewBean getResultData() {
        return resultData;
    }

    public void setResultData(ImageViewBean resultData) {
        this.resultData = resultData;
    }
}
