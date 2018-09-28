package com.bjypt.vipcard.model.cf;

import java.io.Serializable;
import java.util.List;

public class CfAccountListData implements Serializable {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"pageNum":1,"pageSize":10,"total":2,"pages":1,"list":[{"pkmerchantid":1,"merchantName":"测试公司","mupassport":"zxzx","mupassword":"123456","merchantAddress":"测试地址","content":"简介","contactName":"联系人","contactCellphone":"联系方式","isConsume":1,"hybMerchantid":"0004415fbc184a3fa1f00195c7f95914","createAt":1536028140000,"merchantBrand":"公司品牌","merchantCreditCode":"公司统一信用代码","merchantTimeLimit":"营业期限","merchantBusinessScope":"营业范围","merchantLogo":"商家logo","vip_name":null,"discount":null,"cardno":null,"elec_cardno":null},{"pkmerchantid":2,"merchantName":"测试公司1","mupassport":"zxzx","mupassword":"123456","merchantAddress":"测试地址","content":"简介","contactName":"联系人","contactCellphone":"联系方式","isConsume":1,"hybMerchantid":"0004415fbc184a3fa1f00195c7f95914","createAt":1536028140000,"merchantBrand":"公司品牌","merchantCreditCode":"公司统一信用代码","merchantTimeLimit":"营业期限","merchantBusinessScope":"营业范围","merchantLogo":"商家logo","vip_name":null,"discount":null,"cardno":null,"elec_cardno":null}],"isFirstPage":true,"isLastPage":true}
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

    public static class ResultDataBean implements Serializable {
        /**
         * pageNum : 1
         * pageSize : 10
         * total : 2
         * pages : 1
         * list : [{"pkmerchantid":1,"merchantName":"测试公司","mupassport":"zxzx","mupassword":"123456","merchantAddress":"测试地址","content":"简介","contactName":"联系人","contactCellphone":"联系方式","isConsume":1,"hybMerchantid":"0004415fbc184a3fa1f00195c7f95914","createAt":1536028140000,"merchantBrand":"公司品牌","merchantCreditCode":"公司统一信用代码","merchantTimeLimit":"营业期限","merchantBusinessScope":"营业范围","merchantLogo":"商家logo","vip_name":null,"discount":null,"cardno":null,"elec_cardno":null},{"pkmerchantid":2,"merchantName":"测试公司1","mupassport":"zxzx","mupassword":"123456","merchantAddress":"测试地址","content":"简介","contactName":"联系人","contactCellphone":"联系方式","isConsume":1,"hybMerchantid":"0004415fbc184a3fa1f00195c7f95914","createAt":1536028140000,"merchantBrand":"公司品牌","merchantCreditCode":"公司统一信用代码","merchantTimeLimit":"营业期限","merchantBusinessScope":"营业范围","merchantLogo":"商家logo","vip_name":null,"discount":null,"cardno":null,"elec_cardno":null}]
         * isFirstPage : true
         * isLastPage : true
         */

        private int pageNum;
        private int pageSize;
        private int total;
        private int pages;
        private boolean isFirstPage;
        private boolean isLastPage;
        private List<CfAccountData> list;

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

        public boolean isIsFirstPage() {
            return isFirstPage;
        }

        public void setIsFirstPage(boolean isFirstPage) {
            this.isFirstPage = isFirstPage;
        }

        public boolean isIsLastPage() {
            return isLastPage;
        }

        public void setIsLastPage(boolean isLastPage) {
            this.isLastPage = isLastPage;
        }

        public List<CfAccountData> getList() {
            return list;
        }

        public void setList(List<CfAccountData> list) {
            this.list = list;
        }


    }
}
