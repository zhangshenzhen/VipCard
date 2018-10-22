package com.bjypt.vipcard.fragment.crowdfunding.entity;

import java.math.BigDecimal;

public class CfProjectItemNew {
    private Integer pkprojectid;
    private Integer pkmerchantid;
    private String projectName;
    private long buyEndAt;
    private Object userSettleDays;
    private long settleEndAt;
    private Object sponsor;
    private String headImg;
    private BigDecimal maxInterestRate;


    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    private String project_name;


    private  BigDecimal progress_cf_amount;
    private BigDecimal cf_amount;
    private String head_img;
    private Object underwrite;
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
    private String gridUrl;

    private BigDecimal optimalMoney;

    private BigDecimal maximumIncome;

    public void setMaximumIncome(BigDecimal maximumIncome) {
        this.maximumIncome = maximumIncome;
    }


    public BigDecimal getMaximumIncome() {
        return maximumIncome;
    }


    private boolean collection;


    public BigDecimal getOptimalMoney() {
        return optimalMoney;
    }

    public void setOptimalMoney(BigDecimal optimalMoney) {
        this.optimalMoney = optimalMoney;
    }

    public String getGridUrl() {
        return gridUrl;
    }

    public void setGridUrl(String gridUrl) {
        this.gridUrl = gridUrl;
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

    public Object getUserSettleDays() {
        return userSettleDays;
    }

    public void setUserSettleDays(Object userSettleDays) {
        this.userSettleDays = userSettleDays;
    }

    public long getSettleEndAt() {
        return settleEndAt;
    }

    public void setSettleEndAt(long settleEndAt) {
        this.settleEndAt = settleEndAt;
    }

    public Object getSponsor() {
        return sponsor;
    }

    public void setSponsor(Object sponsor) {
        this.sponsor = sponsor;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Object getUnderwrite() {
        return underwrite;
    }

    public void setUnderwrite(Object underwrite) {
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

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public BigDecimal getCf_amount() {
        return cf_amount;
    }

    public void setCf_amount(BigDecimal cf_amount) {
        this.cf_amount = cf_amount;
    }

    public BigDecimal getProgress_cf_amount() {
        return progress_cf_amount;
    }

    public void setProgress_cf_amount(BigDecimal progress_cf_amount) {
        this.progress_cf_amount = progress_cf_amount;
    }

    //--------------------------------------

}
