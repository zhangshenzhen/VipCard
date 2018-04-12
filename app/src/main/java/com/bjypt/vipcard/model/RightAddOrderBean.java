package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/11/18 0018.
 */

public class RightAddOrderBean {

//    "pkregister":"5a77ae772cb34106b334338673956c44","pkmuser":"a0245c8f8a6043fdaad230698b74fbbf","waitMoney":"68.80","payEntrance":"5","payment":"9","pkWeal":"","redPacket":"20.0","virtualMoney":"11.20","pkpayid":"8f7684009ec840e5ae446649e3d7d117","amount":"100"}}
    private String pkregister;
    private String pkmuser;
    private String waitMoney;
    private String payEntrance;
    private String payment;
    private String virtualMoney;
    private String pkpayid;
    private String amount;
    private String pkWeal;
    private String version_code;
    private String non_discount_amount;
    private String non_discount_amount_aes;
    private String outorderid;

    public String getOutorderid() {
        return outorderid;
    }

    public void setOutorderid(String outorderid) {
        this.outorderid = outorderid;
    }

    public String getNon_discount_amount() {
        return non_discount_amount;
    }

    public void setNon_discount_amount(String non_discount_amount) {
        this.non_discount_amount = non_discount_amount;
    }

    public String getNon_discount_amount_aes() {
        return non_discount_amount_aes;
    }

    public void setNon_discount_amount_aes(String non_discount_amount_aes) {
        this.non_discount_amount_aes = non_discount_amount_aes;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public String getPkregister() {
        return pkregister;
    }

    public void setPkregister(String pkregister) {
        this.pkregister = pkregister;
    }

    public String getPkmuser() {
        return pkmuser;
    }

    public void setPkmuser(String pkmuser) {
        this.pkmuser = pkmuser;
    }

    public String getWaitMoney() {
        return waitMoney;
    }

    public void setWaitMoney(String waitMoney) {
        this.waitMoney = waitMoney;
    }

    public String getPayEntrance() {
        return payEntrance;
    }

    public void setPayEntrance(String payEntrance) {
        this.payEntrance = payEntrance;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getVirtualMoney() {
        return virtualMoney;
    }

    public void setVirtualMoney(String virtualMoney) {
        this.virtualMoney = virtualMoney;
    }

    public String getPkpayid() {
        return pkpayid;
    }

    public void setPkpayid(String pkpayid) {
        this.pkpayid = pkpayid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPkWeal() {
        return pkWeal;
    }

    public void setPkWeal(String pkWeal) {
        this.pkWeal = pkWeal;
    }
}
