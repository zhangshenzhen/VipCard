package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/6/15
 * Use by
 */
public class DetailsNoOrderData {
//    "muname":"咱家酸菜鱼",
//            "logo":"merchantLogobed0aae40b1b41369e732b3dcf1bc6a0.jpg",
//            "balance":0.01,
//            "payment_desc":"平台余额",
//            "createtime":1465984668000,
//            "order_no":"1465984637438"}}
    private String muname;
    private String logo;
    private String balance;
    private String payment_desc;
    private String order_no;
    private String createtime;

    public String getMuname() {
        return muname;
    }

    public void setMuname(String muname) {
        this.muname = muname;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPayment_desc() {
        return payment_desc;
    }

    public void setPayment_desc(String payment_desc) {
        this.payment_desc = payment_desc;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
