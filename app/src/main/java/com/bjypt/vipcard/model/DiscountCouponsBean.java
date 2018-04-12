package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 崔龙 on 2017/3/13.
 */

public class DiscountCouponsBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : [{"pkmuser":"a0245c8f8a6043fdaad230698b74fbbf","muname":"大伙铺子","payamount":9,"valueamount":100,"startdate":"2017-02-27","enddate":"2017-03-31","remark":"无门槛"},{"pkmuser":"6d9b64172373478a9b332d459e60634d","muname":"支付测试","payamount":9,"valueamount":100,"startdate":"2017-02-27","enddate":"2017-03-31","remark":"无门槛"},{"pkmuser":"04833eeba2cf42159a6928b60a8b8148","muname":"测试药品3","payamount":9,"valueamount":100,"startdate":"2017-02-27","enddate":"2017-03-31","remark":"无门槛"},{"pkmuser":"998fe32d3f9a49d29eb00c7385fd9ace","muname":"测试药品2","payamount":11,"valueamount":100,"startdate":"2017-03-02","enddate":"2017-03-31","remark":"无门槛"},{"pkmuser":"ada116504acd4f698eaefe406fcb765f","muname":"冬冬的铺子","payamount":15,"valueamount":100,"startdate":"2017-03-02","enddate":"2017-03-31","remark":"无门槛"}]
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
         * pkmuser : a0245c8f8a6043fdaad230698b74fbbf
         * muname : 大伙铺子
         * payamount : 9
         * valueamount : 100
         * startdate : 2017-02-27
         * enddate : 2017-03-31
         * remark : 无门槛
         */

        private String pkmuser;
        private String muname;
        private String payamount;
        private String valueamount;
        private String startdate;
        private String enddate;
        private String remark;
        private String pkmerchantcoupon;
        private String address;

        public String getAddres(){
            return address;
        }

        public void setAddress(String address){
            this.address = address;
        }

        public String getPkmuser() {
            return pkmuser;
        }

        public void setPkmuser(String pkmuser) {
            this.pkmuser = pkmuser;
        }

        public String getMuname() {
            return muname;
        }

        public void setMuname(String muname) {
            this.muname = muname;
        }

        public String getPayamount() {
            return payamount;
        }

        public void setPayamount(String payamount) {
            this.payamount = payamount;
        }

        public String getValueamount() {
            return valueamount;
        }

        public void setValueamount(String valueamount) {
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
        public String getPkmerchantcoupon() {
            return pkmerchantcoupon;
        }

        public void setPkmerchantcoupon(String pkmerchantcoupon) {
            this.pkmerchantcoupon = pkmerchantcoupon;
        }

        @Override
        public String toString() {
            return "ResultDataBean{" +
                    "pkmuser='" + pkmuser + '\'' +
                    ", muname='" + muname + '\'' +
                    ", payamount='" + payamount + '\'' +
                    ", valueamount='" + valueamount + '\'' +
                    ", startdate='" + startdate + '\'' +
                    ", enddate='" + enddate + '\'' +
                    ", remark='" + remark + '\'' +
                    ", pkmerchantcoupon='" + pkmerchantcoupon + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DiscountCouponsBean{" +
                "resultStatus=" + resultStatus +
                ", msg='" + msg + '\'' +
                ", resultData=" + resultData +
                '}';
    }
}
