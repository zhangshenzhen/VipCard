package com.bjypt.vipcard.activity.shangfeng.data.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/26.
 */

public class LifeServiceBean {


    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : [{"app_id":7,"app_name":"测试菜单1","app_icon":"http://12132sas213.com","app_code":null,"app_type":null,"mtlevel":null,"parent_app_id":null,"city_code":null,"isentry":2,"link_type":1,"link_url":"测试1","android_native_url":"测试1","ios_native_url":"测试1","native_params":"测试1","isuse":null,"sorting":null,"remark":null,"create_at":null,"last_updatetime":null,"platform_type":null,"platform_name":null,"versioncode":null,"type_name":null,"pageNo":null,"pageSize":null}]
     */

    private int resultStatus;
    private String msg;
    private List<BannerBean> resultData;

    public int getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(int resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<BannerBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<BannerBean> resultData) {
        this.resultData = resultData;
    }


}
