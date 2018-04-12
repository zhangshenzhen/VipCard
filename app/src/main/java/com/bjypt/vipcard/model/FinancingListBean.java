package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 崔龙 on 2018/1/4.
 */

public class FinancingListBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
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
         * pkmuser : 8d91228426a344c68e0d4147b3cd7a80
         * pkproduct : 22222222
         * productday : 10
         * productexpiredate : {"date":7,"day":6,"hours":0,"minutes":0,"month":0,"seconds":0,"time":1483718400000,"timezoneOffset":-480,"year":117}
         * productmemo : 平台产批可能
         * productname : 余额宝2
         * productrate : 6.0
         * productstatus : 1
         * producttotalmoney : 22222000
         * diffdate : 2018-01-04到2018-01-14
         */

        private String pkmuser;
        private String pkproduct;
        private String productday;
        private ProductexpiredateBean productexpiredate;
        private String productmemo;
        private String productname;
        private String productrate;
        private int productstatus;
        private String producttotalmoney;
        private String diffdate;
        private String finance_amount;

        public String getFinance_amount() {
            return finance_amount;
        }

        public void setFinance_amount(String finance_amount) {
            this.finance_amount = finance_amount;
        }

        public String getPkmuser() {
            return pkmuser;
        }

        public void setPkmuser(String pkmuser) {
            this.pkmuser = pkmuser;
        }

        public String getPkproduct() {
            return pkproduct;
        }

        public void setPkproduct(String pkproduct) {
            this.pkproduct = pkproduct;
        }

        public String getProductday() {
            return productday;
        }

        public void setProductday(String productday) {
            this.productday = productday;
        }

        public ProductexpiredateBean getProductexpiredate() {
            return productexpiredate;
        }

        public void setProductexpiredate(ProductexpiredateBean productexpiredate) {
            this.productexpiredate = productexpiredate;
        }

        public String getProductmemo() {
            return productmemo;
        }

        public void setProductmemo(String productmemo) {
            this.productmemo = productmemo;
        }

        public String getProductname() {
            return productname;
        }

        public void setProductname(String productname) {
            this.productname = productname;
        }

        public String getProductrate() {
            return productrate;
        }

        public void setProductrate(String productrate) {
            this.productrate = productrate;
        }

        public int getProductstatus() {
            return productstatus;
        }

        public void setProductstatus(int productstatus) {
            this.productstatus = productstatus;
        }

        public String getProducttotalmoney() {
            return producttotalmoney;
        }

        public void setProducttotalmoney(String producttotalmoney) {
            this.producttotalmoney = producttotalmoney;
        }

        public String getDiffdate() {
            return diffdate;
        }

        public void setDiffdate(String diffdate) {
            this.diffdate = diffdate;
        }

        public static class ProductexpiredateBean {
            /**
             * date : 7
             * day : 6
             * hours : 0
             * minutes : 0
             * month : 0
             * seconds : 0
             * time : 1483718400000
             * timezoneOffset : -480
             * year : 117
             */

            private int date;
            private int day;
            private int hours;
            private int minutes;
            private int month;
            private int seconds;
            private long time;
            private int timezoneOffset;
            private int year;

            public int getDate() {
                return date;
            }

            public void setDate(int date) {
                this.date = date;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getHours() {
                return hours;
            }

            public void setHours(int hours) {
                this.hours = hours;
            }

            public int getMinutes() {
                return minutes;
            }

            public void setMinutes(int minutes) {
                this.minutes = minutes;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getSeconds() {
                return seconds;
            }

            public void setSeconds(int seconds) {
                this.seconds = seconds;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(int timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }
        }
    }
}
