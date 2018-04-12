package com.bjypt.vipcard.common;

/**
 * Created by Administrator on 2017/3/10.
 */

public enum PayDealTypeEnum {
    Chongzhi(1, "充值"),
    Lijimaidan(2, "立即买单"),
    YouhuiQuan(3, "优惠券"),
    WuzhedianChongzhi(4, "五折店充值"),
    WuzhedianLijiMaidan(5, "五折店立即买单"),
    LifeServicePay(6, "生活服务支付"),
    PetroleumPay(10, "石油券");


    private int code;
    private String payName;

    private PayDealTypeEnum(int code, String payName) {
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
