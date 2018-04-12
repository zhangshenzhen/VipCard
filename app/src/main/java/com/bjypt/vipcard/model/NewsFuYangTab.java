package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 崔龙 on 2017/7/6.
 */

public class NewsFuYangTab {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : [{"catid":"6","catname":"科技","displayorder":"5","url":""},{"catid":"5","catname":"娱乐","displayorder":"4","url":""},{"catid":"3","catname":"本地","displayorder":"3","url":""},{"catid":"1","catname":"热点","displayorder":"1","url":""},{"catid":"4","catname":"社会","displayorder":"0","url":""},{"catid":"7","catname":"汽车","displayorder":"0","url":""},{"catid":"8","catname":"房产","displayorder":"0","url":""},{"catid":"9","catname":"财经","displayorder":"0","url":"http://news.baidu.com/finance"}]
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
         * catid : 6
         * catname : 科技
         * displayorder : 5
         * url :
         */

        private String catid;
        private String catname;
        private String displayorder;
        private String url;

        public String getCatid() {
            return catid;
        }

        public void setCatid(String catid) {
            this.catid = catid;
        }

        public String getCatname() {
            return catname;
        }

        public void setCatname(String catname) {
            this.catname = catname;
        }

        public String getDisplayorder() {
            return displayorder;
        }

        public void setDisplayorder(String displayorder) {
            this.displayorder = displayorder;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
