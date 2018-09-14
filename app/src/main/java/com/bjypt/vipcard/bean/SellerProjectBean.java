package com.bjypt.vipcard.bean;

import java.util.ArrayList;

public class SellerProjectBean {
    public boolean isFirstPage;
    public boolean isLastPage;
    private int pageNum;
    private int pageSize;
    private int total;
    private int pages;

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(boolean firstPage) {
        isFirstPage = firstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public ArrayList<SellBean> getSelllist() {
        return selllist;
    }

    public void setSelllist(ArrayList<SellBean> selllist) {
        this.selllist = selllist;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public ArrayList<SellBean> selllist ;
    /**
     * pageNum : 1
     * pageSize : 6
     * total : 1
     * pages : 1
     */


    public static class SellBean{
           /**
            * pkprojectid : 3
            * pkmerchantid : 1
            * projectName : 到期一次性全返众筹测试
            * buyEndAt : 1536293522000
            * userSettleDays : 90
            * settleEndAt : 1536293522000
            * sponsor : wl
            * headImg : http://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/common78e9ee4c89924d9f9a8efaaf51d4638c.jpg
            * underwrite : 平安保险承保
            * cfAmount : 10000.0
            * progressCfAmount : 5500.0
            * personalMaxBuyAmount : 1000.0
            * personalMaxBuyCount : 3
            * interestRate : 20.0
            * content : null
            * settleType : 2
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
            * optimalMoney : 1000.0
            * days : null
            * number : null
            * checkId : null
            * hybCfProjectProgressList : null
            * hybCfProjectTagList : null
            * collection : false
            */

           private int pkprojectid;
           private int pkmerchantid;
           private String projectName;
           private long buyEndAt;
           private int userSettleDays;
           private long settleEndAt;
           private String sponsor;
           private String headImg;
           private String underwrite;
           private double cfAmount;
           private double progressCfAmount;
           private double personalMaxBuyAmount;
           private int personalMaxBuyCount;
           private double interestRate;
           private Object content;
           private int settleType;
           private int sortNum;
           private int status;
           private int payType;
           private int viewAuth;
           private int tags;
           private Object createAt;
           private Object isRecommend;
           private Object progress_desc;
           private Object sortType;
           private Object teamIntroduction;
           private Object commonProblem;
           private Object pkregister;
           private double optimalMoney;
           private Object days;
           private Object number;
           private Object checkId;
           private Object hybCfProjectProgressList;
           private Object hybCfProjectTagList;
           private boolean collection;

           public int getPkprojectid() {
               return pkprojectid;
           }

           public void setPkprojectid(int pkprojectid) {
               this.pkprojectid = pkprojectid;
           }

           public int getPkmerchantid() {
               return pkmerchantid;
           }

           public void setPkmerchantid(int pkmerchantid) {
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

           public int getUserSettleDays() {
               return userSettleDays;
           }

           public void setUserSettleDays(int userSettleDays) {
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

           public double getCfAmount() {
               return cfAmount;
           }

           public void setCfAmount(double cfAmount) {
               this.cfAmount = cfAmount;
           }

           public double getProgressCfAmount() {
               return progressCfAmount;
           }

           public void setProgressCfAmount(double progressCfAmount) {
               this.progressCfAmount = progressCfAmount;
           }

           public double getPersonalMaxBuyAmount() {
               return personalMaxBuyAmount;
           }

           public void setPersonalMaxBuyAmount(double personalMaxBuyAmount) {
               this.personalMaxBuyAmount = personalMaxBuyAmount;
           }

           public int getPersonalMaxBuyCount() {
               return personalMaxBuyCount;
           }

           public void setPersonalMaxBuyCount(int personalMaxBuyCount) {
               this.personalMaxBuyCount = personalMaxBuyCount;
           }

           public double getInterestRate() {
               return interestRate;
           }

           public void setInterestRate(double interestRate) {
               this.interestRate = interestRate;
           }

           public Object getContent() {
               return content;
           }

           public void setContent(Object content) {
               this.content = content;
           }

           public int getSettleType() {
               return settleType;
           }

           public void setSettleType(int settleType) {
               this.settleType = settleType;
           }

           public int getSortNum() {
               return sortNum;
           }

           public void setSortNum(int sortNum) {
               this.sortNum = sortNum;
           }

           public int getStatus() {
               return status;
           }

           public void setStatus(int status) {
               this.status = status;
           }

           public int getPayType() {
               return payType;
           }

           public void setPayType(int payType) {
               this.payType = payType;
           }

           public int getViewAuth() {
               return viewAuth;
           }

           public void setViewAuth(int viewAuth) {
               this.viewAuth = viewAuth;
           }

           public int getTags() {
               return tags;
           }

           public void setTags(int tags) {
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

           public double getOptimalMoney() {
               return optimalMoney;
           }

           public void setOptimalMoney(double optimalMoney) {
               this.optimalMoney = optimalMoney;
           }

           public Object getDays() {
               return days;
           }

           public void setDays(Object days) {
               this.days = days;
           }

           public Object getNumber() {
               return number;
           }

           public void setNumber(Object number) {
               this.number = number;
           }

           public Object getCheckId() {
               return checkId;
           }

           public void setCheckId(Object checkId) {
               this.checkId = checkId;
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

           @Override
           public String toString() {
               return "SellBean{" +
                       "pkprojectid=" + pkprojectid +
                       ", pkmerchantid=" + pkmerchantid +
                       ", projectName='" + projectName + '\'' +
                       ", buyEndAt=" + buyEndAt +
                       ", userSettleDays=" + userSettleDays +
                       ", settleEndAt=" + settleEndAt +
                       ", sponsor='" + sponsor + '\'' +
                       ", headImg='" + headImg + '\'' +
                       ", underwrite='" + underwrite + '\'' +
                       ", cfAmount=" + cfAmount +
                       ", progressCfAmount=" + progressCfAmount +
                       ", personalMaxBuyAmount=" + personalMaxBuyAmount +
                       ", personalMaxBuyCount=" + personalMaxBuyCount +
                       ", interestRate=" + interestRate +
                       ", content=" + content +
                       ", settleType=" + settleType +
                       ", sortNum=" + sortNum +
                       ", status=" + status +
                       ", payType=" + payType +
                       ", viewAuth=" + viewAuth +
                       ", tags=" + tags +
                       ", createAt=" + createAt +
                       ", isRecommend=" + isRecommend +
                       ", progress_desc=" + progress_desc +
                       ", sortType=" + sortType +
                       ", teamIntroduction=" + teamIntroduction +
                       ", commonProblem=" + commonProblem +
                       ", pkregister=" + pkregister +
                       ", optimalMoney=" + optimalMoney +
                       ", days=" + days +
                       ", number=" + number +
                       ", checkId=" + checkId +
                       ", hybCfProjectProgressList=" + hybCfProjectProgressList +
                       ", hybCfProjectTagList=" + hybCfProjectTagList +
                       ", collection=" + collection +
                       '}';
           }
       }




}
