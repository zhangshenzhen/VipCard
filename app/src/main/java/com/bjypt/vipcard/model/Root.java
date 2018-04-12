package com.bjypt.vipcard.model;
import java.util.List;
/**
 * Created by Administrator on 2016/4/8.
 */
public class Root {
    private int resultStatus;

    private String msg;

    private List<MerchantCategoryOneBeam> resultData ;


    public void setResultStatus(int resultStatus){
        this.resultStatus = resultStatus;
    }
    public int getResultStatus(){
        return this.resultStatus;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setResultData(List<MerchantCategoryOneBeam> resultData){
        this.resultData = resultData;
    }
    public List<MerchantCategoryOneBeam> getResultData(){
        return this.resultData;
    }

}


