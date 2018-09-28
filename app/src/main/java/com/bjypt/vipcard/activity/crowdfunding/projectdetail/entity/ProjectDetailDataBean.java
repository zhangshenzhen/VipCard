package com.bjypt.vipcard.activity.crowdfunding.projectdetail.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ProjectDetailDataBean  implements Serializable{

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"pkprojectid":3,"pkmerchantid":1,"projectName":"到期一次性全返众筹测试","buyEndAt":1544069520000,"userSettleDays":90,"settleEndAt":1544069520000,"sponsor":"wl","headImg":"http://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/common78e9ee4c89924d9f9a8efaaf51d4638c.jpg","underwrite":"平安保险承保","cfAmount":10000,"progressCfAmount":5500,"personalMaxBuyAmount":1000,"personalMaxBuyCount":3,"interestRate":20,"content":null,"settleType":0,"sortNum":9999,"status":1,"payType":6,"viewAuth":0,"tags":0,"createAt":null,"isRecommend":null,"progress_desc":null,"sortType":null,"teamIntroduction":null,"commonProblem":null,"pkregister":null,"optimalMoney":null,"days":81,"number":620,"checkId":null,"gridUrl":"http://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/common840a9d8b4928455d93547dc9d4a13ef6.jpg","hybAttachmentList":[{"attach_type":0,"attachment":"https://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/f2742f94a6614274bda6212236ae3368.jpg"},{"attach_type":0,"attachment":"https://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/f2742f94a6614274bda6212236ae3368.jpg"}],"hybCfProjectProgressList":null,"hybCfProjectTagList":null,"collection":false}
     */

    private Integer resultStatus;
    private String msg;
    private ResultDataBean resultData;

    public Integer getResultStatus() {
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

    public ResultDataBean getResultData() {
        return resultData;
    }

    public void setResultData(ResultDataBean resultData) {
        this.resultData = resultData;
    }

    public static class ResultDataBean implements Serializable {
        /**
         * pkprojectid : 3
         * pkmerchantid : 1
         * projectName : 到期一次性全返众筹测试
         * buyEndAt : 1544069520000
         * userSettleDays : 90
         * settleEndAt : 1544069520000
         * sponsor : wl
         * headImg : http://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/common78e9ee4c89924d9f9a8efaaf51d4638c.jpg
         * underwrite : 平安保险承保
         * cfAmount : 10000.0
         * progressCfAmount : 5500.0
         * personalMaxBuyAmount : 1000.0
         * personalMaxBuyCount : 3
         * interestRate : 20.0
         * content : null
         * settleType : 0
         * sortNum : 9999
         * status : 1
         * payType : 6
         * viewAuth : 0
         * tags : 0
         * createAt : null
         * isRecommend : null
         * progress_desc : null
         * sortType : null
         * teamIntroduction : null
         * commonProblem : null
         * pkregister : null
         * optimalMoney : null
         * agreement ：协议
         * days : 81
         * number : 620
         * checkId : null
         * gridUrl : http://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/common840a9d8b4928455d93547dc9d4a13ef6.jpg
         * hybAttachmentList : [{"attach_type":0,"attachment":"https://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/f2742f94a6614274bda6212236ae3368.jpg"},{"attach_type":0,"attachment":"https://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/f2742f94a6614274bda6212236ae3368.jpg"}]
         * hybCfProjectProgressList : null
         * hybCfProjectTagList : null
         * collection : false
         */

        private Integer pkprojectid;
        private Integer pkmerchantid;
        private String projectName;
        private long buyEndAt;
        private Integer userSettleDays;
        private long settleEndAt;
        private String sponsor;
        private String headImg;
        private String underwrite;
        private BigDecimal cfAmount;
        private BigDecimal progressCfAmount;
        private BigDecimal personalMaxBuyAmount;
        private Integer personalMaxBuyCount;
        private BigDecimal interestRate;
        private String content;
        private Integer settleType;
        private Integer sortNum;
        private Integer status;
        private Integer payType;
        private Integer viewAuth;
        private Integer tags;
        private Object createAt;
        private Object isRecommend;
        private Object progress_desc;
        private Object sortType;
        private Object teamIntroduction;
        private Object commonProblem;
        private Object pkregister;
        private Integer days;
        private Integer number;
        private Integer checkId;
        private String gridUrl;
        private Object hybCfProjectProgressList;
        private Object hybCfProjectTagList;
        private boolean collection;
        private String telephone;

        private List<HybAttachmentListBean> hybAttachmentList;

        private String merchantName;
        private String merchantLogo;
        private String merchantContent;
        private String oneContent;
        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getOneContent() {
            return oneContent;
        }

        public void setOneContent(String oneContent) {
            this.oneContent = oneContent;
        }


        public String getAgreement() {
            return agreement;
        }

        public void setAgreement(String agreement) {
            this.agreement = agreement;
        }

        private String agreement;//购买协议

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public String getMerchantLogo() {
            return merchantLogo;
        }

        public void setMerchantLogo(String merchantLogo) {
            this.merchantLogo = merchantLogo;
        }



        public String getMerchantContent() {
            return merchantContent;
        }

        public void setMerchantContent(String merchantContent) {
            this.merchantContent = merchantContent;
        }

        public Integer getPkprojectid() {
            return pkprojectid;
        }

        public void setPkprojectid(Integer pkprojectid) {
            this.pkprojectid = pkprojectid;
        }

        public Integer getPkmerchantid() {
            return pkmerchantid;
        }

        public void setPkmerchantid(Integer pkmerchantid) {
            this.pkmerchantid = pkmerchantid;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public long getBuyEndAt() {
            return buyEndAt;
        }

        public void setBuyEndAt(long buyEndAt) {
            this.buyEndAt = buyEndAt;
        }

        public Integer getUserSettleDays() {
            return userSettleDays;
        }

        public void setUserSettleDays(Integer userSettleDays) {
            this.userSettleDays = userSettleDays;
        }

        public long getSettleEndAt() {
            return settleEndAt;
        }

        public void setSettleEndAt(long settleEndAt) {
            this.settleEndAt = settleEndAt;
        }

        public String getSponsor() {
            return sponsor;
        }

        public void setSponsor(String sponsor) {
            this.sponsor = sponsor;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getUnderwrite() {
            return underwrite;
        }

        public void setUnderwrite(String underwrite) {
            this.underwrite = underwrite;
        }

        public BigDecimal getCfAmount() {
            return cfAmount;
        }

        public void setCfAmount(BigDecimal cfAmount) {
            this.cfAmount = cfAmount;
        }

        public BigDecimal getProgressCfAmount() {
            return progressCfAmount;
        }

        public void setProgressCfAmount(BigDecimal progressCfAmount) {
            this.progressCfAmount = progressCfAmount;
        }

        public BigDecimal getPersonalMaxBuyAmount() {
            return personalMaxBuyAmount;
        }

        public void setPersonalMaxBuyAmount(BigDecimal personalMaxBuyAmount) {
            this.personalMaxBuyAmount = personalMaxBuyAmount;
        }

        public Integer getPersonalMaxBuyCount() {
            return personalMaxBuyCount;
        }

        public void setPersonalMaxBuyCount(Integer personalMaxBuyCount) {
            this.personalMaxBuyCount = personalMaxBuyCount;
        }

        public BigDecimal getInterestRate() {
            return interestRate;
        }

        public void setInterestRate(BigDecimal interestRate) {
            this.interestRate = interestRate;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Integer getSettleType() {
            return settleType;
        }

        public void setSettleType(Integer settleType) {
            this.settleType = settleType;
        }

        public Integer getSortNum() {
            return sortNum;
        }

        public void setSortNum(Integer sortNum) {
            this.sortNum = sortNum;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getPayType() {
            return payType;
        }

        public void setPayType(Integer payType) {
            this.payType = payType;
        }

        public Integer getViewAuth() {
            return viewAuth;
        }

        public void setViewAuth(Integer viewAuth) {
            this.viewAuth = viewAuth;
        }

        public Integer getTags() {
            return tags;
        }

        public void setTags(Integer tags) {
            this.tags = tags;
        }

        public Object getCreateAt() {
            return createAt;
        }

        public void setCreateAt(Object createAt) {
            this.createAt = createAt;
        }

        public Object getIsRecommend() {
            return isRecommend;
        }

        public void setIsRecommend(Object isRecommend) {
            this.isRecommend = isRecommend;
        }

        public Object getProgress_desc() {
            return progress_desc;
        }

        public void setProgress_desc(Object progress_desc) {
            this.progress_desc = progress_desc;
        }

        public Object getSortType() {
            return sortType;
        }

        public void setSortType(Object sortType) {
            this.sortType = sortType;
        }

        public Object getTeamIntroduction() {
            return teamIntroduction;
        }

        public void setTeamIntroduction(Object teamIntroduction) {
            this.teamIntroduction = teamIntroduction;
        }

        public Object getCommonProblem() {
            return commonProblem;
        }

        public void setCommonProblem(Object commonProblem) {
            this.commonProblem = commonProblem;
        }

        public Object getPkregister() {
            return pkregister;
        }

        public void setPkregister(Object pkregister) {
            this.pkregister = pkregister;
        }

        public Integer getDays() {
            return days;
        }

        public void setDays(Integer days) {
            this.days = days;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public Integer getCheckId() {
            return checkId;
        }

        public void setCheckId(Integer checkId) {
            this.checkId = checkId;
        }

        public String getGridUrl() {
            return gridUrl;
        }

        public void setGridUrl(String gridUrl) {
            this.gridUrl = gridUrl;
        }

        public Object getHybCfProjectProgressList() {
            return hybCfProjectProgressList;
        }

        public void setHybCfProjectProgressList(Object hybCfProjectProgressList) {
            this.hybCfProjectProgressList = hybCfProjectProgressList;
        }

        public Object getHybCfProjectTagList() {
            return hybCfProjectTagList;
        }

        public void setHybCfProjectTagList(Object hybCfProjectTagList) {
            this.hybCfProjectTagList = hybCfProjectTagList;
        }

        public boolean isCollection() {
            return collection;
        }

        public void setCollection(boolean collection) {
            this.collection = collection;
        }

        public List<HybAttachmentListBean> getHybAttachmentList() {
            return hybAttachmentList;
        }

        public void setHybAttachmentList(List<HybAttachmentListBean> hybAttachmentList) {
            this.hybAttachmentList = hybAttachmentList;
        }


    }
}
