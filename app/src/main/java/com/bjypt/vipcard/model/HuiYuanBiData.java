package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2017/7/26.
 */

public class HuiYuanBiData {

    private String pkRegister;
    private String amount;
    private String sourcepk;
    private String pkmuser;
    private String totalCount;
    private String virtualBalance;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSourcepk() {
        return sourcepk;
    }

    public void setSourcepk(String sourcepk) {
        this.sourcepk = sourcepk;
    }

    public String getPkmuser() {
        return pkmuser;
    }

    public void setPkmuser(String pkmuser) {
        this.pkmuser = pkmuser;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getVirtualBalance() {
        return virtualBalance;
    }

    public void setVirtualBalance(String virtualBalance) {
        this.virtualBalance = virtualBalance;
    }

    public String getPkRegister() {
        return pkRegister;
    }

    public void setPkRegister(String pkRegister) {
        this.pkRegister = pkRegister;
    }
}
