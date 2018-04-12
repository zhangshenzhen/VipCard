package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/21
 * Use by
 */
public class CommentDetailsData {
//    avgScore：商家评分
//    rank：高于同行百分率(整数)
//    totalComment：全部评论
//    praiseComment：好评
//    otherComment：其他
    private float avgScore;
    private String rank;
    private String totalComment;
    private String praiseComment;
    private String otherComment;

    public float getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(float avgScore) {
        this.avgScore = avgScore;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(String totalComment) {
        this.totalComment = totalComment;
    }

    public String getPraiseComment() {
        return praiseComment;
    }

    public void setPraiseComment(String praiseComment) {
        this.praiseComment = praiseComment;
    }

    public String getOtherComment() {
        return otherComment;
    }

    public void setOtherComment(String otherComment) {
        this.otherComment = otherComment;
    }
}
