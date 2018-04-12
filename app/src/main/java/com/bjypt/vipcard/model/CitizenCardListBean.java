package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2017/8/10.
 */

public class CitizenCardListBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"bindlist":[{"cardid":71,"cardnum":"55889966","categoryid":1,"category_name":"阜阳公交卡","scope_type":0,"status":1,"amount":"600.00","virtual_amount":"529.12","bind_time":"2017-09-06","showCardNum":"558**966"},{"cardid":1,"cardnum":"20170001","categoryid":3,"category_name":"阜阳华联卡","scope_type":1,"card_pic":"card_pic26c5f96952514e2fba1d17c28381a006.jpg","status":1,"amount":"600.00","virtual_amount":"529.12","bind_time":"2017-08-16","showCardNum":"201**001"},{"cardid":107,"cardnum":"712345678","categoryid":1,"category_name":"阜阳公交卡","scope_type":0,"status":1,"amount":"600.00","virtual_amount":"529.12","bind_time":"2017-09-18","showCardNum":"712***678"},{"cardid":108,"cardnum":"712345679","categoryid":1,"category_name":"阜阳公交卡","scope_type":0,"status":1,"amount":"600.00","virtual_amount":"529.12","bind_time":"2017-09-18","showCardNum":"712***679"},{"cardid":109,"cardnum":"258369147","categoryid":1,"category_name":"阜阳公交卡","scope_type":0,"status":1,"amount":"600.00","virtual_amount":"529.12","bind_time":"2017-09-18","showCardNum":"258***147"},{"cardid":110,"cardnum":"963528741","categoryid":12,"category_name":"阜阳春秋卡","scope_type":0,"card_pic":"card_pic1bc82fe03eaf490cbe3f03cda57c07a8.jpg","status":1,"amount":"600.00","virtual_amount":"529.12","bind_time":"2017-09-18","showCardNum":"963***741"}],"canBeReceivedList":[{"cardid":0,"cardnum":"","categoryid":16,"category_name":"南京开发电子卡类别","scope_type":1,"card_pic":"card_pic89678238a5a14dc1ac4172ba25d9f711","status":0,"bind_time":""}]}
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
        public int getCanBeReceivedListSize() {
            return canBeReceivedListSize;
        }

        public void setCanBeReceivedListSize(int canBeReceivedListSize) {
            this.canBeReceivedListSize = canBeReceivedListSize;
        }

        private int canBeReceivedListSize;
        private List<BindlistBean> bindlist;
        private List<CanBeReceivedListBean> canBeReceivedList;

        public List<BindlistBean> getBindlist() {
            return bindlist;
        }

        public void setBindlist(List<BindlistBean> bindlist) {
            this.bindlist = bindlist;
        }

        public List<CanBeReceivedListBean> getCanBeReceivedList() {
            return canBeReceivedList;
        }

        public void setCanBeReceivedList(List<CanBeReceivedListBean> canBeReceivedList) {
            this.canBeReceivedList = canBeReceivedList;
        }

        public static class BindlistBean {
            /**
             * cardid : 71
             * cardnum : 55889966
             * categoryid : 1
             * category_name : 阜阳公交卡
             * scope_type : 0
             * status : 1
             * amount : 600.00
             * virtual_amount : 529.12
             * bind_time : 2017-09-06
             * showCardNum : 558**966
             * card_pic : card_pic26c5f96952514e2fba1d17c28381a006.jpg
             */

            private int cardid;
            private String cardnum;
            private int categoryid;
            private String category_name;
            private int scope_type;
            private int status;
            private String amount;
            private String virtual_amount;
            private String bind_time;
            private String showCardNum;
            private String card_pic;

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

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getVirtual_amount() {
                return virtual_amount;
            }

            public void setVirtual_amount(String virtual_amount) {
                this.virtual_amount = virtual_amount;
            }

            public String getBind_time() {
                return bind_time;
            }

            public void setBind_time(String bind_time) {
                this.bind_time = bind_time;
            }

            public String getShowCardNum() {
                return showCardNum;
            }

            public void setShowCardNum(String showCardNum) {
                this.showCardNum = showCardNum;
            }

            public String getCard_pic() {
                return card_pic;
            }

            public void setCard_pic(String card_pic) {
                this.card_pic = card_pic;
            }
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
             */

            private int cardid;
            private String cardnum;
            private int categoryid;
            private String category_name;
            private int scope_type;

            public String getList_url() {
                return list_url;
            }

            public void setList_url(String list_url) {
                this.list_url = list_url;
            }

            private String list_url;
            private String card_pic;
            private int status;
            private String bind_time;

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
        }
    }
}
