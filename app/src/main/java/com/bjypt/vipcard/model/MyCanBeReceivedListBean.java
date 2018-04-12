package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 崔龙 on 2017/10/19.
 */

public class MyCanBeReceivedListBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"canBeReceivedList":[{"cardid":0,"cardnum":"","categoryid":16,"category_name":"南京开发电子卡类别","scope_type":1,"card_pic":"card_pic89678238a5a14dc1ac4172ba25d9f711","status":0,"bind_time":"","logo_url":"1"}]}
     */

    private int resultStatus;
    private String msg;
    private ResultDataBean resultData;

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

    public ResultDataBean getResultData() {
        return resultData;
    }

    public void setResultData(ResultDataBean resultData) {
        this.resultData = resultData;
    }

    public static class ResultDataBean {
        private List<CanBeReceivedListBean> canBeReceivedList;

        public List<CanBeReceivedListBean> getCanBeReceivedList() {
            return canBeReceivedList;
        }

        public void setCanBeReceivedList(List<CanBeReceivedListBean> canBeReceivedList) {
            this.canBeReceivedList = canBeReceivedList;
        }

        public static class CanBeReceivedListBean {
            /**
             * cardid : 0
             * cardnum :
             * categoryid : 16
             * category_name : 南京开发电子卡类别
             * scope_type : 1
             * card_pic : card_pic89678238a5a14dc1ac4172ba25d9f711
             * status : 0
             * bind_time :
             * logo_url : 1
             */

            private int cardid;
            private String cardnum;
            private int categoryid;
            private String category_name;
            private int scope_type;
            private String card_pic;
            private int status;
            private String bind_time;
            private String logo_url;

            public String getList_url() {
                return list_url;
            }

            public void setList_url(String list_url) {
                this.list_url = list_url;
            }

            private String list_url;
            public int getCardid() {
                return cardid;
            }

            public void setCardid(int cardid) {
                this.cardid = cardid;
            }

            public String getCardnum() {
                return cardnum;
            }

            public void setCardnum(String cardnum) {
                this.cardnum = cardnum;
            }

            public int getCategoryid() {
                return categoryid;
            }

            public void setCategoryid(int categoryid) {
                this.categoryid = categoryid;
            }

            public String getCategory_name() {
                return category_name;
            }

            public void setCategory_name(String category_name) {
                this.category_name = category_name;
            }

            public int getScope_type() {
                return scope_type;
            }

            public void setScope_type(int scope_type) {
                this.scope_type = scope_type;
            }

            public String getCard_pic() {
                return card_pic;
            }

            public void setCard_pic(String card_pic) {
                this.card_pic = card_pic;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getBind_time() {
                return bind_time;
            }

            public void setBind_time(String bind_time) {
                this.bind_time = bind_time;
            }

            public String getLogo_url() {
                return logo_url;
            }

            public void setLogo_url(String logo_url) {
                this.logo_url = logo_url;
            }
        }
    }
}
