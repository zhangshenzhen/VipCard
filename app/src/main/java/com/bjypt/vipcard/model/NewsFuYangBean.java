package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 崔龙 on 2017/7/4.
 */

public class NewsFuYangBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : [{"aid":"8","catid":"4","bid":"0","uid":"1","username":"admin","title":"测试新闻2","highlight":"|||","author":"","from":"","fromurl":"","url":"","summary":"123123","pic":"","thumb":"0","remote":"0","id":"0","idtype":"","contents":"1","allowcomment":"1","owncomment":"0","click1":"0","click2":"0","click3":"0","click4":"0","click5":"0","click6":"0","click7":"0","click8":"0","tag":"7","dateline":"1499049720","status":"0","showinnernav":"0","preaid":"7","nextaid":"0","htmlmade":"0","htmlname":"","htmldir":""},{"aid":"6","catid":"6","bid":"0","uid":"1","username":"admin","title":"测试直播","highlight":"|||","author":"","from":"","fromurl":"http://live.lecloud.com/live/sdk/getView?activityId=A2017062900000an","url":"http://live.lecloud.com/live/sdk/getView?activityId=A2017062900000an","summary":"http://live.lecloud.com/live/sdk/getView?activityId=A2017062900000an","pic":"","thumb":"0","remote":"0","id":"0","idtype":"","contents":"1","allowcomment":"1","owncomment":"0","click1":"0","click2":"0","click3":"0","click4":"0","click5":"0","click6":"0","click7":"0","click8":"0","tag":"1","dateline":"1498724949","status":"0","showinnernav":"0","preaid":"5","nextaid":"0","htmlmade":"0","htmlname":"","htmldir":""}]
     */

    private int resultStatus;
    private String msg;
    private List<ResultDataBean> resultData;

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

    public List<ResultDataBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<ResultDataBean> resultData) {
        this.resultData = resultData;
    }

    public static class ResultDataBean {
        /**
         * aid : 8
         * catid : 4
         * bid : 0
         * uid : 1
         * username : admin
         * title : 测试新闻2
         * highlight : |||
         * author :
         * from :
         * fromurl :
         * url :
         * summary : 123123
         * pic :
         * thumb : 0
         * remote : 0
         * id : 0
         * idtype :
         * contents : 1
         * allowcomment : 1
         * owncomment : 0
         * click1 : 0
         * click2 : 0
         * click3 : 0
         * click4 : 0
         * click5 : 0
         * click6 : 0
         * click7 : 0
         * click8 : 0
         * tag : 7
         * dateline : 1499049720
         * status : 0
         * showinnernav : 0
         * preaid : 7
         * nextaid : 0
         * htmlmade : 0
         * htmlname :
         * htmldir :
         */

        private String aid;
        private String catid;
        private String bid;
        private String uid;
        private String username;
        private String title;
        private String highlight;
        private String author;
        private String from;
        private String fromurl;
        private String url;
        private String summary;
        private String pic;
        private String thumb;
        private String remote;
        private String id;
        private String idtype;
        private String contents;
        private String allowcomment;
        private String owncomment;
        private String click1;
        private String click2;
        private String click3;
        private String click4;
        private String click5;
        private String click6;
        private String click7;
        private String click8;
        private String tag;
        private String dateline;
        private String status;
        private String showinnernav;
        private String preaid;
        private String nextaid;
        private String htmlmade;
        private String htmlname;
        private String htmldir;

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getCatid() {
            return catid;
        }

        public void setCatid(String catid) {
            this.catid = catid;
        }

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getHighlight() {
            return highlight;
        }

        public void setHighlight(String highlight) {
            this.highlight = highlight;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getFromurl() {
            return fromurl;
        }

        public void setFromurl(String fromurl) {
            this.fromurl = fromurl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getRemote() {
            return remote;
        }

        public void setRemote(String remote) {
            this.remote = remote;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdtype() {
            return idtype;
        }

        public void setIdtype(String idtype) {
            this.idtype = idtype;
        }

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }

        public String getAllowcomment() {
            return allowcomment;
        }

        public void setAllowcomment(String allowcomment) {
            this.allowcomment = allowcomment;
        }

        public String getOwncomment() {
            return owncomment;
        }

        public void setOwncomment(String owncomment) {
            this.owncomment = owncomment;
        }

        public String getClick1() {
            return click1;
        }

        public void setClick1(String click1) {
            this.click1 = click1;
        }

        public String getClick2() {
            return click2;
        }

        public void setClick2(String click2) {
            this.click2 = click2;
        }

        public String getClick3() {
            return click3;
        }

        public void setClick3(String click3) {
            this.click3 = click3;
        }

        public String getClick4() {
            return click4;
        }

        public void setClick4(String click4) {
            this.click4 = click4;
        }

        public String getClick5() {
            return click5;
        }

        public void setClick5(String click5) {
            this.click5 = click5;
        }

        public String getClick6() {
            return click6;
        }

        public void setClick6(String click6) {
            this.click6 = click6;
        }

        public String getClick7() {
            return click7;
        }

        public void setClick7(String click7) {
            this.click7 = click7;
        }

        public String getClick8() {
            return click8;
        }

        public void setClick8(String click8) {
            this.click8 = click8;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getShowinnernav() {
            return showinnernav;
        }

        public void setShowinnernav(String showinnernav) {
            this.showinnernav = showinnernav;
        }

        public String getPreaid() {
            return preaid;
        }

        public void setPreaid(String preaid) {
            this.preaid = preaid;
        }

        public String getNextaid() {
            return nextaid;
        }

        public void setNextaid(String nextaid) {
            this.nextaid = nextaid;
        }

        public String getHtmlmade() {
            return htmlmade;
        }

        public void setHtmlmade(String htmlmade) {
            this.htmlmade = htmlmade;
        }

        public String getHtmlname() {
            return htmlname;
        }

        public void setHtmlname(String htmlname) {
            this.htmlname = htmlname;
        }

        public String getHtmldir() {
            return htmldir;
        }

        public void setHtmldir(String htmldir) {
            this.htmldir = htmldir;
        }
    }
}
