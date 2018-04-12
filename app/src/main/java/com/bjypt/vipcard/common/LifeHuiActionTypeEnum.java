package com.bjypt.vipcard.common;

/**
 * Created by Administrator on 2017/3/2.
 */

public enum  LifeHuiActionTypeEnum {
    LifeHui(0,"生活汇"), FineDisCount(1, "五折店"), Coupon(2, "优惠券"), NativePage(3, "原生页面跳转"), JumpPage(4, "页面跳转");

    private int value;
    private String name;

    private LifeHuiActionTypeEnum(int value, String name){
        this.name = name;
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
