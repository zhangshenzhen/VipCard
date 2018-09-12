package com.bjypt.vipcard.model.cf;

import java.math.BigDecimal;

public class CfAccountData {

    /**
     * pkuseraccountid : 8
     * pkregister : 7fd0f7ad13de42bbb05ccee88e08e92d
     * pkmerchantid : 1
     * canWithdrawPrincipal : 0.0
     * canWithdrawIncome : 0.0
     * canConsumePrincipal : 1780.0
     * merchantUseingPrincipal : 0.0
     * historyTotalPrincipal : 0.0
     * historyTotalIncome : 0.0
     * cardno : 318618099256
     * elecCardno : 318618099256
     * status : 0
     * createAt : 1536316862000
     * merchant_name : 测试公司
     * bankcardno : null
     * bankname : null
     * vip_name : 黄金
     * discount : 98.00000
     * totle_assets : null
     * expected_earnings : null
     * amount_of_cash : null
     * consumption_amount : null
     */

    private int pkuseraccountid;
    private String pkregister;
    private int pkmerchantid;
    private BigDecimal canWithdrawPrincipal;
    private BigDecimal canWithdrawIncome;
    private BigDecimal canConsumePrincipal;
    private BigDecimal merchantUseingPrincipal;
    private BigDecimal historyTotalPrincipal;
    private BigDecimal historyTotalIncome;
    private String cardno;
    private String elecCardno;
    private int status;
    private long createAt;
    private String merchant_name;
    private Object bankcardno;
    private Object bankname;
    private String vip_name;
    private Integer type_num;
    private String discount;
    private BigDecimal totle_assets;
    private BigDecimal expected_earnings;
    private BigDecimal amount_of_cash;
    private BigDecimal consumption_amount;

    public Integer getType_num() {
        return type_num;
    }

    public void setType_num(Integer type_num) {
        this.type_num = type_num;
    }

    public int getPkuseraccountid() {
        return pkuseraccountid;
    }

    public void setPkuseraccountid(int pkuseraccountid) {
        this.pkuseraccountid = pkuseraccountid;
    }

    public String getPkregister() {
        return pkregister;
    }

    public void setPkregister(String pkregister) {
        this.pkregister = pkregister;
    }

    public int getPkmerchantid() {
        return pkmerchantid;
    }

    public void setPkmerchantid(int pkmerchantid) {
        this.pkmerchantid = pkmerchantid;
    }

    public BigDecimal getCanWithdrawPrincipal() {
        return canWithdrawPrincipal;
    }

    public void setCanWithdrawPrincipal(BigDecimal canWithdrawPrincipal) {
        this.canWithdrawPrincipal = canWithdrawPrincipal;
    }

    public BigDecimal getCanWithdrawIncome() {
        return canWithdrawIncome;
    }

    public void setCanWithdrawIncome(BigDecimal canWithdrawIncome) {
        this.canWithdrawIncome = canWithdrawIncome;
    }

    public BigDecimal getCanConsumePrincipal() {
        return canConsumePrincipal;
    }

    public void setCanConsumePrincipal(BigDecimal canConsumePrincipal) {
        this.canConsumePrincipal = canConsumePrincipal;
    }

    public BigDecimal getMerchantUseingPrincipal() {
        return merchantUseingPrincipal;
    }

    public void setMerchantUseingPrincipal(BigDecimal merchantUseingPrincipal) {
        this.merchantUseingPrincipal = merchantUseingPrincipal;
    }

    public BigDecimal getHistoryTotalPrincipal() {
        return historyTotalPrincipal;
    }

    public void setHistoryTotalPrincipal(BigDecimal historyTotalPrincipal) {
        this.historyTotalPrincipal = historyTotalPrincipal;
    }

    public BigDecimal getHistoryTotalIncome() {
        return historyTotalIncome;
    }

    public void setHistoryTotalIncome(BigDecimal historyTotalIncome) {
        this.historyTotalIncome = historyTotalIncome;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getElecCardno() {
        return elecCardno;
    }

    public void setElecCardno(String elecCardno) {
        this.elecCardno = elecCardno;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public Object getBankcardno() {
        return bankcardno;
    }

    public void setBankcardno(Object bankcardno) {
        this.bankcardno = bankcardno;
    }

    public Object getBankname() {
        return bankname;
    }

    public void setBankname(Object bankname) {
        this.bankname = bankname;
    }

    public String getVip_name() {
        return vip_name;
    }

    public void setVip_name(String vip_name) {
        this.vip_name = vip_name;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public BigDecimal getTotle_assets() {
        return totle_assets;
    }

    public void setTotle_assets(BigDecimal totle_assets) {
        this.totle_assets = totle_assets;
    }

    public BigDecimal getExpected_earnings() {
        return expected_earnings;
    }

    public void setExpected_earnings(BigDecimal expected_earnings) {
        this.expected_earnings = expected_earnings;
    }

    public BigDecimal getAmount_of_cash() {
        return amount_of_cash;
    }

    public void setAmount_of_cash(BigDecimal amount_of_cash) {
        this.amount_of_cash = amount_of_cash;
    }

    public BigDecimal getConsumption_amount() {
        return consumption_amount;
    }

    public void setConsumption_amount(BigDecimal consumption_amount) {
        this.consumption_amount = consumption_amount;
    }
}
