package com.bjypt.vipcard.h5;

/**
 * Created by WANG427 on 2016/4/19.
 */
public interface JavaScriptListener  {
    /**
     * 页面恢复的时候是否刷新
     * @param isRefresh
     */
    public void isResumeRefresh(boolean isRefresh);

    /**
     * 重新加载网页
     */
    void webViewReload();
}
