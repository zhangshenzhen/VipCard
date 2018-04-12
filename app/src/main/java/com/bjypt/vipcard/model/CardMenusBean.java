package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2018/3/12.
 */

public class CardMenusBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : [{"id":1,"name":"消费记录"},{"id":2,"name":"礼品金纪录"},{"id":4,"name":"积分兑换"},{"id":5,"name":"抵扣金说明"},{"id":6,"name":"线上商家"}]
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
         * id : 1
         * name : 消费记录
         */

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
