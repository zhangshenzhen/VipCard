package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/5
 * Use by
 */
public class HomeTest {

    private String resultStatus;
    private String msg;
    private HomeInfoBean resultData;

    public void setResultData(HomeInfoBean resultData) {
        this.resultData = resultData;
    }

    public HomeInfoBean getResultData() {
        return resultData;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



    public String getResultStatus() {
        return resultStatus;
    }

    public String getMsg() {
        return msg;
    }


}
