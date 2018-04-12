package com.bjypt.vipcard.model;
/**
 * 生活服务里面的消费的项目的实体
 * @author syj
 */
public class LifeMoneyOptionBean {

	//内容
	private String body; //售价
	//售价
	private String sellingPrice;  //流量
	
	/**
	 * @param body 内容
	 */
	public LifeMoneyOptionBean(String body, String sellingPrice) {
		this.body = body;
		this.sellingPrice = sellingPrice;
	}
	
	//获取内容
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	//获取售价
	public String getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(String sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	
	
}
