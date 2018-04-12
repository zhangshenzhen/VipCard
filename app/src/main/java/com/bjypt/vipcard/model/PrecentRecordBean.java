package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/5/10.
 */
public class PrecentRecordBean {
   /* bankusername：用户关联银行卡上姓名
    bankuserphone：用户在银行预留手机号码
    bankname：银行名称
    bankcardno: 银行卡号
    apply_time: 申请时间
    amount: 提现金额
    status_desc: 提现状态描述*/

    private String begin;

    private String pageLength;

    private String type;

    private String bankusername;

    private String bankuserphone;

    private String bankname;

    private String bankcardno;

    private String apply_time;

    private String amount;

    private String status_desc;

    private String status;
    private String resultamount;
    private String rate;

    public String getResultamount() {
        return resultamount;
    }

    public void setResultamount(String resultamount) {
        this.resultamount = resultamount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    ;
    ;


    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getPageLength() {
        return pageLength;
    }

    public void setPageLength(String pageLength) {
        this.pageLength = pageLength;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBankusername() {
        return bankusername;
    }

    public void setBankusername(String bankusername) {
        this.bankusername = bankusername;
    }

    public String getBankuserphone() {
        return bankuserphone;
    }

    public void setBankuserphone(String bankuserphone) {
        this.bankuserphone = bankuserphone;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankcardno() {
        return bankcardno;
    }

    public void setBankcardno(String bankcardno) {
        this.bankcardno = bankcardno;
    }

    public String getApply_time() {
        return apply_time;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus_desc() {
        return status_desc;
    }

    public void setStatus_desc(String status_desc) {
        this.status_desc = status_desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
