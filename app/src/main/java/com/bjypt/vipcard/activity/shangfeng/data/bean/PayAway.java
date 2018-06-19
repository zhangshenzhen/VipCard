package com.bjypt.vipcard.activity.shangfeng.data.bean;

import java.math.BigDecimal;

/**
 * Created by wanglei on 2018/5/15.
 */

public class PayAway {

    /**
     * dealtype : null
     * payname : 支付宝
     * code : 1
     * balance : null
     * shownum : 4
     * syscode : null
     */

    private Object dealtype;
    private String payname;
    private int code;
    private BigDecimal balance;
    private int shownum;
    private String syscode;

    public Object getDealtype() {
        return dealtype;
    }

    public void setDealtype(Object dealtype) {
        this.dealtype = dealtype;
    }

    public String getPayname() {
        return payname;
    }

    public void setPayname(String payname) {
        this.payname = payname;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getShownum() {
        return shownum;
    }

    public void setShownum(int shownum) {
        this.shownum = shownum;
    }

    public String getSyscode() {
        return syscode;
    }

    public void setSyscode(String syscode) {
        this.syscode = syscode;
    }
}
