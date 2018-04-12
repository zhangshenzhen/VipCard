package com.bjypt.vipcard.model;

/**
 * Created by User on 2016/11/19.
 */
public class UserMechantBean {
    /*pkregister:用户主键
pkmuser:商家主键
balance_mer:用户在商户余额
flag: 该商家是否开启充值送积分活动:0不开启;1开启
amount_recharge：充值送积分下限金额
amount_virtualmoney：amount_virtualmoney
*/
    private String pkregister;
    private String pkmuser;
    private String balance_mer;
    private String flag;
    private String amount_recharge;
    private String amount_virtualmoney;

    public String getPkmuser() {
        return pkmuser;
    }

    public void setPkmuser(String pkmuser) {
        this.pkmuser = pkmuser;
    }

    public String getBalance_mer() {
        return balance_mer;
    }

    public void setBalance_mer(String balance_mer) {
        this.balance_mer = balance_mer;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getAmount_recharge() {
        return amount_recharge;
    }

    public void setAmount_recharge(String amount_recharge) {
        this.amount_recharge = amount_recharge;
    }

    public String getAmount_virtualmoney() {
        return amount_virtualmoney;
    }

    public void setAmount_virtualmoney(String amount_virtualmoney) {
        this.amount_virtualmoney = amount_virtualmoney;
    }

    public String getPkregister() {

        return pkregister;
    }

    public void setPkregister(String pkregister) {
        this.pkregister = pkregister;
    }
}
