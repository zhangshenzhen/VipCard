package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/4/25.
 */
public class ListDetailTypeOneBean {
/* {
    "resultStatus": 0,
    "msg": "SUCCESS",
    "resultData": {

    }
}
    }*/
    private String resultStatus;
    private String msg;
    private ListDetailTypeOneListBean resultData;


    public ListDetailTypeOneListBean getResultData() {
        return resultData;
    }

    public void setResultData(ListDetailTypeOneListBean resultData) {
        this.resultData = resultData;
    }


    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
