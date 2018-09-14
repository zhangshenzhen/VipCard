package com.bjypt.vipcard.fragment.crowdfunding.entity;

import java.math.BigDecimal;

public class CfProjectItem {
    private Integer pkprojectid;
    private Integer pkmerchantid;
    private String projectName;
    private long buyEndAt;
    private Object userSettleDays;
    private long settleEndAt;
    private Object sponsor;
    private String headImg;
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

    private boolean collection;

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
}