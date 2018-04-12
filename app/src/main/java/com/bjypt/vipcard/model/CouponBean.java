package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/4/20.
 */
public class CouponBean {
    private String payamount;
    private String valueamount;
    private String startdate;
    private String enddate;
    private String remark;
    private String usestatus;
    private String muname;
    private String paytime;
    private String flag;
    private String isSelect;
    private String pkorderid;
    private String pkcoupon;
    private String policycontent;
    private String policystatus;
    private String pkmuser;
    private boolean ischeck;

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    public String getPkmuser() {
        return pkmuser;
    }

    public void setPkmuser(String pkmuser) {
        this.pkmuser = pkmuser;
    }

    public String getPkorderid() {
        return pkorderid;
    }

    public void setPkorderid(String pkorderid) {
        this.pkorderid = pkorderid;
    }

    public String getPkcoupon() {
        return pkcoupon;
    }

    public void setPkcoupon(String pkcoupon) {
        this.pkcoupon = pkcoupon;
    }

    public String getPolicycontent() {
        return policycontent;
    }

    public void setPolicycontent(String policycontent) {
        this.policycontent = policycontent;
    }

    public String getPolicystatus() {
        return policystatus;
    }

    public void setPolicystatus(String policystatus) {
        this.policystatus = policystatus;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(String isSelect) {
        this.isSelect = isSelect;
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

    public String getUsestatus() {
        return usestatus;
    }

    public void setUsestatus(String usestatus) {
        this.usestatus = usestatus;
    }
}
