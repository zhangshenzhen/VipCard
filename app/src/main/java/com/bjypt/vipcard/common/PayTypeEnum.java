package com.bjypt.vipcard.common;

/**
 * Created by Administrator on 2017/3/10.
 * 1支付宝 2财付通 3百度钱包 4网银 5余额支付 6微信支付 7现金支付 8其他支付 9平台支付 10建行在线
 */

public enum  PayTypeEnum {
    Zhifubao(1, "支付宝"),
    CaiFutong(2, "财付通"),
    BaiduMoney(3, "百度钱包"),
    Wangyin(4, "网银"),
    ShangjiaYuE(5, "商家余额支付"),
    Weixin(6, "微信"),
    Xianjin(7, "现金支付"),
    Qita(8, "其他支付"),
    Pingtai(9, "平台支付"),
    Jianhang(10, "建行在线");

    private int code;
    private String payName;
    private PayTypeEnum(int code, String payName){
        this.code = code;
        this.payName = payName;
    }

    public int getCode() {
        return code;
    }

    public String getPayName() {
        return payName;
    }
}
