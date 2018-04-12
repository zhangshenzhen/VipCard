package com.bjypt.vipcard.model;

/**
 * Created by User on 2016/11/19.
 */
public class UserMechantInfoBean {
    /*{
resultStatus: 0成功; -1 失败
msg: SUCCESS;FAIL
resultData: {

}
}*/
    private String resultStatus;
    private String msg;
    private UserMechantBean resultData;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserMechantBean getResultData() {
        return resultData;
    }

    public void setResultData(UserMechantBean resultData) {
        this.resultData = resultData;
    }

    public String getResultStatus() {

        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }
}
