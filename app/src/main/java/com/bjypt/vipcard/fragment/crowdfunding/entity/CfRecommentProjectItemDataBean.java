package com.bjypt.vipcard.fragment.crowdfunding.entity;

import java.util.List;

public class CfRecommentProjectItemDataBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : [{"pkprojectid":7,"pkmerchantid":null,"projectName":"每日返息，周期30天","buyEndAt":1539327600000,"userSettleDays":null,"settleEndAt":1539327600000,"sponsor":null,"headImg":"http://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/common0bd5dcb16674414e84f2a1ce226fca35.jpg","underwrite":null,"cfAmount":100,"progressCfAmount":0,"personalMaxBuyAmount":0,"personalMaxBuyCount":3,"interestRate":0,"content":null,"settleType":0,"sortNum":9999,"status":1,"payType":1,"viewAuth":0,"tags":0,"createAt":null,"isRecommend":null,"progress_desc":null,"sortType":null,"teamIntroduction":null,"commonProblem":null,"pkregister":null,"optimalMoney":null,"days":null,"number":null,"checkId":null,"hybCfProjectProgressList":null,"hybCfProjectTagList":[],"collection":false},{"pkprojectid":1,"pkmerchantid":null,"projectName":"大幅度","buyEndAt":1537413060000,"userSettleDays":null,"settleEndAt":1537459200000,"sponsor":"张三","headImg":"http://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/common840a9d8b4928455d93547dc9d4a13ef6.jpg","underwrite":"是的分公司电饭锅第三方","cfAmount":110,"progressCfAmount":2900,"personalMaxBuyAmount":110,"personalMaxBuyCount":5,"interestRate":15,"content":"发个梵蒂冈讽德诵功","settleType":1,"sortNum":9999,"status":1,"payType":6,"viewAuth":1,"tags":0,"createAt":1535947922000,"isRecommend":null,"progress_desc":null,"sortType":null,"teamIntroduction":null,"commonProblem":null,"pkregister":null,"optimalMoney":null,"days":null,"number":null,"checkId":null,"hybCfProjectProgressList":null,"hybCfProjectTagList":[],"collection":false},{"pkprojectid":3,"pkmerchantid":null,"projectName":"到期一次性全返众筹测试","buyEndAt":1536293522000,"userSettleDays":null,"settleEndAt":1536293522000,"sponsor":"wl","headImg":"http://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/common78e9ee4c89924d9f9a8efaaf51d4638c.jpg","underwrite":"平安保险承保","cfAmount":10000,"progressCfAmount":5500,"personalMaxBuyAmount":1000,"personalMaxBuyCount":3,"interestRate":20,"content":null,"settleType":2,"sortNum":9999,"status":1,"payType":6,"viewAuth":0,"tags":0,"createAt":null,"isRecommend":null,"progress_desc":null,"sortType":null,"teamIntroduction":null,"commonProblem":null,"pkregister":null,"optimalMoney":null,"days":null,"number":null,"checkId":null,"hybCfProjectProgressList":null,"hybCfProjectTagList":[],"collection":false}]
     */

    private int resultStatus;
    private String msg;
    private List<CfProjectItem> resultData;

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

    public List<CfProjectItem> getResultData() {
        return resultData;
    }

    public void setResultData(List<CfProjectItem> resultData) {
        this.resultData = resultData;
    }

}
