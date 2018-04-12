package com.bjypt.vipcard.model;

/**
 * Created by 崔龙 on 2017/10/20.
 */

public class MyRandomBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"pkscanpay":"3197e0b5a2434bce9caa532a90836d57","pkregister":"03921e20078b4a20b04f7893ccf00bbd","barcode":"288322414821970747","cardnum":"1234567","status":0,"deviceid":"123","create_at":1508322414822}
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
        /**
         * pkscanpay : 3197e0b5a2434bce9caa532a90836d57
         * pkregister : 03921e20078b4a20b04f7893ccf00bbd
         * barcode : 288322414821970747
         * cardnum : 1234567
         * status : 0
         * deviceid : 123
         * create_at : 1508322414822
         */

        private String pkscanpay;
        private String pkregister;
        private String barcode;
        private String cardnum;
        private int status;
        private String deviceid;
        private long create_at;

        public String getPkscanpay() {
            return pkscanpay;
        }

        public void setPkscanpay(String pkscanpay) {
            this.pkscanpay = pkscanpay;
        }

        public String getPkregister() {
            return pkregister;
        }

        public void setPkregister(String pkregister) {
            this.pkregister = pkregister;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getCardnum() {
            return cardnum;
        }

        public void setCardnum(String cardnum) {
            this.cardnum = cardnum;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public long getCreate_at() {
            return create_at;
        }

        public void setCreate_at(long create_at) {
            this.create_at = create_at;
        }
    }
}
