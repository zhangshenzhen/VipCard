package com.bjypt.vipcard.activity.shangfeng.common;

/**
 * Created by wanglei on 2018/5/22.
 */

public class EventBusMessageEvent {

    public int what ;
    public Object data;

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
