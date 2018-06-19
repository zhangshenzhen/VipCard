package com.bjypt.vipcard.activity.shangfeng.data.bean;

/**
 * 订单Bean
 */
public class OrderBean {

    /**
     * 订单主键
     */
    private int id;

    /**
     * 商户组件
     */
    private String pkmuser;
    /**
     * 商户名称
     */
    private String muname;
    /**
     * 订单金额
     */
    private String consume_amount;
    /**
     * 订单创建时间
     */
    private String create_at;
    /**
     * 折扣
     */
    private String discount_rate_desc;


    public OrderBean(int id, String pkmuser, String muname, String consume_amount, String create_at, String discount_rate_desc) {
        this.id = id;
        this.pkmuser = pkmuser;
        this.muname = muname;
        this.consume_amount = consume_amount;
        this.create_at = create_at;
        this.discount_rate_desc = discount_rate_desc;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPkmuser() {
        return pkmuser;
    }

    public void setPkmuser(String pkmuser) {
        this.pkmuser = pkmuser;
    }

    public String getMuname() {
        return muname;
    }

    public void setMuname(String muname) {
        this.muname = muname;
    }

    public String getConsume_amount() {
        return consume_amount;
    }

    public void setConsume_amount(String consume_amount) {
        this.consume_amount = consume_amount;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getDiscount_rate_desc() {
        return discount_rate_desc;
    }

    public void setDiscount_rate_desc(String discount_rate_desc) {
        this.discount_rate_desc = discount_rate_desc;
    }

}
