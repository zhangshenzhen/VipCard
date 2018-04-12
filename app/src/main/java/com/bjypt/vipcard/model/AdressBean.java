package com.bjypt.vipcard.model;

import java.io.Serializable;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/1
 * Use by 地址的静态类
 */
public class AdressBean implements Serializable{
    private String addressid;
    private String defaultaddress;
    private String phoneno;
    private String receiptaddress;
    private String registername;
    private String pkregister;

    public String getAddressid() {
        return addressid;
    }

    public void setAddressid(String addressid) {
        this.addressid = addressid;
    }

    public String getDefaultaddress() {
        return defaultaddress;
    }

    public void setDefaultaddress(String defaultaddress) {
        this.defaultaddress = defaultaddress;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getReceiptaddress() {
        return receiptaddress;
    }

    public void setReceiptaddress(String receiptaddress) {
        this.receiptaddress = receiptaddress;
    }

    public String getRegistername() {
        return registername;
    }

    public void setRegistername(String registername) {
        this.registername = registername;
    }

    public String getPkregister() {
        return pkregister;
    }

    public void setPkregister(String pkregister) {
        this.pkregister = pkregister;
    }
}
