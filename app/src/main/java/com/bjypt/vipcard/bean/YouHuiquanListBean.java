package com.bjypt.vipcard.bean;

import java.math.BigDecimal;
import java.util.List;

public class YouHuiquanListBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : [{"pkcoupon":"030ba751786d415fa61346529be72dbe","pkmuser":"f1556414e0ed485bb79224d41082f3b7","payamount":75,"valueamount":100,"startdate":"2017-03-13","enddate":"2018-03-13","remark":"无门槛","usestatus":2,"scope_of_use":2},{"pkcoupon":"d85da30c2d274ffea9a6a27277a6dec8","pkmuser":"f1556414e0ed485bb79224d41082f3b7","payamount":80,"valueamount":100,"startdate":"2018-11-11","enddate":"2018-11-30","remark":"无门槛","usestatus":0,"scope_of_use":2},{"pkcoupon":"9a6b41cfb94b4664a3f27c278e710abe","pkmuser":"8d91228426a344c68e0d4147b3cd7a80","payamount":43,"valueamount":50,"startdate":"2017-03-10","enddate":"2018-12-26","remark":"无门槛","usestatus":0,"scope_of_use":2},{"pkcoupon":"9a6b41cfb94b4664a3f27c278e710abe","pkmuser":"8d91228426a344c68e0d4147b3cd7a80","payamount":43,"valueamount":50,"startdate":"2017-03-10","enddate":"2018-12-26","remark":"无门槛","usestatus":0,"scope_of_use":2},{"pkcoupon":"46091847bfb145b7b015cc2838d373e7","pkmuser":"f1556414e0ed485bb79224d41082f3b7","payamount":80,"valueamount":100,"startdate":"2017-03-04","enddate":"2019-03-11","remark":"无门槛","usestatus":0,"scope_of_use":2}]
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
        private  String label;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        private String title;
        public String getLabel() {
            return label;
        }

        public void setLabel(String lable) {
            this.label = label;
        }

        /**
         * pkcoupon : 030ba751786d415fa61346529be72dbe
         * pkmuser : f1556414e0ed485bb79224d41082f3b7
         * payamount : 75
         * valueamount : 100
         * startdate : 2017-03-13
         * enddate : 2018-03-13
         * remark : 无门槛
         * usestatus : 2
         * scope_of_use : 2
         *
         */

        private String pkcoupon;
        private String pkmuser;
        private BigDecimal payamount;
        private BigDecimal valueamount;
        private String startdate;
        private String enddate;
        private String remark;
        private int usestatus;
        private int scope_of_use;

        public String getPkcoupon() {
            return pkcoupon;
        }

        public void setPkcoupon(String pkcoupon) {
            this.pkcoupon = pkcoupon;
        }

        public String getPkmuser() {
            return pkmuser;
        }

        public void setPkmuser(String pkmuser) {
            this.pkmuser = pkmuser;
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

        public String getStartdate() {
            return startdate;
        }

        public void setStartdate(String startdate) {
            this.startdate = startdate;
        }

        public String getEnddate() {
            return enddate;
        }

        public void setEnddate(String enddate) {
            this.enddate = enddate;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getUsestatus() {
            return usestatus;
        }

        public void setUsestatus(int usestatus) {
            this.usestatus = usestatus;
        }

        public int getScope_of_use() {
            return scope_of_use;
        }

        public void setScope_of_use(int scope_of_use) {
            this.scope_of_use = scope_of_use;
        }
    }
}
