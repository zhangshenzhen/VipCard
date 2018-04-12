package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/26
 * Use by
 */
public class WithdrawData {

    private String pkmuser;
    private String type;
    private String bankusername;
    private String bankcardno;
    private String bankname;
    private String bankuserphone;
    private String balance;
    private String banklogo;
    private String wdmsg;
    private String noratebalance;
    private String rate;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getNoratebalance() {
        return noratebalance;
    }

    public void setNoratebalance(String noratebalance) {
        this.noratebalance = noratebalance;
    }

    public String getWdmsg() {
        return wdmsg;
    }

    public void setWdmsg(String wdmsg) {
        this.wdmsg = wdmsg;
    }

    public String getBanklogo() {
        return banklogo;
    }

    public void setBanklogo(String banklogo) {
        this.banklogo = banklogo;
    }

    public String getPkmuser() {
        return pkmuser;
    }

    public void setPkmuser(String pkmuser) {
        this.pkmuser = pkmuser;
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

    public String getBankcardno() {
        return bankcardno;
    }

    public void setBankcardno(String bankcardno) {
        this.bankcardno = bankcardno;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankuserphone() {
        return bankuserphone;
    }

    public void setBankuserphone(String bankuserphone) {
        this.bankuserphone = bankuserphone;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
