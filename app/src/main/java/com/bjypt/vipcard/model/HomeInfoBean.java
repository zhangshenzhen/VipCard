package com.bjypt.vipcard.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/5
 * Use by主页接口静态类
 */
public class HomeInfoBean  {

    private List<SysNoticeBean> sysNotice;//系统公告
    private List<HomeTypeBean> firstMerCategory = new ArrayList<HomeTypeBean>();//第一商家信息
    private List<HomeTypeBean> merCategory = new ArrayList<HomeTypeBean>();
    private String popPicUrl;

    public String getPopPicUrl() {
        return popPicUrl;
    }

    public void setPopPicUrl(String popPicUrl) {
        this.popPicUrl = popPicUrl;
    }

    public void setFirstMerCategory(List<HomeTypeBean> firstMerCategory) {
        this.firstMerCategory = firstMerCategory;
    }

    public List<HomeTypeBean> getFirstMerCategory() {
        return firstMerCategory;
    }



    public void setMerCategory(List<HomeTypeBean> merCategory) {
        this.merCategory = merCategory;
    }


    public void setSysNotice(List<SysNoticeBean> sysNotice) {
        this.sysNotice = sysNotice;
    }

    public List<SysNoticeBean> getSysNotice() {
        return sysNotice;
    }



    public List<HomeTypeBean> getMerCategory() {
        return merCategory;
    }
}
