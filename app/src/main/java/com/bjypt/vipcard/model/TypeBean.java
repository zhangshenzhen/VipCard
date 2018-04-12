package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/12/13 0013.
 */

public class TypeBean {
    private String msg;
    private String resultStatus;
    private TypeCateBean resultData;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public TypeCateBean getResultData() {
        return resultData;
    }

    public void setResultData(TypeCateBean resultData) {
        this.resultData = resultData;
    }
}
