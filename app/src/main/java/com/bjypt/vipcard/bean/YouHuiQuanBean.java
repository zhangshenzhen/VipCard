package com.bjypt.vipcard.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class YouHuiQuanBean implements Serializable{
    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : [{"pkcoupon":"c39d1122946f457891965c39e626719c","activity_id":1,"title":"登录送","start_time":"2018-11-17 00:00:00.0","payamount":"2.000","valueamount":"10.000"},{"pkcoupon":"3e7ead42f0e74565a0088735bd6dce55","activity_id":2,"title":"登录送","start_time":"2018-11-19 00:00:00.0","payamount":"75.000","valueamount":"100.000","label":"水果生鲜，商超"},{"pkcoupon":"ea236efdb777468e81105c2a973675b0","activity_id":3,"title":"登录送","start_time":"2018-11-19 00:00:00.0","payamount":"70.000","valueamount":"100.000"}]
     */

    private int resultStatus;
    private String msg;
    private List<YouHuiQuanDataBean> resultData;

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

    public List<YouHuiQuanDataBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<YouHuiQuanDataBean> resultData) {
        this.resultData = resultData;
    }

    public static class YouHuiQuanDataBean implements Serializable {
        /**
         * pkcoupon : c39d1122946f457891965c39e626719c
         * activity_id : 1
         * title : 登录送
         * start_time : 2018-11-17 00:00:00.0
         * payamount : 2.000
         * valueamount : 10.000
         * label : 水果生鲜，商超
         */

        private String pkcoupon;
        private int activity_id;
        private String title;
        private String start_time;
        private BigDecimal payamount;//抵扣
        private BigDecimal valueamount;//满减
        private String label;


        public String getPkcoupon() {
            return pkcoupon;
        }

        public void setPkcoupon(String pkcoupon) {
            this.pkcoupon = pkcoupon;
        }

        public int getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(int activity_id) {
            this.activity_id = activity_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public BigDecimal getPayamount() {
            return payamount;
        }

        public void setPayamount(BigDecimal payamount) {
            this.payamount = payamount;
        }

        public BigDecimal getValueamount() {
            return valueamount;
        }

        public void setValueamount(BigDecimal valueamount) {
            this.valueamount = valueamount;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}
