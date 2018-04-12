package com.sinia.orderlang.bean;

import java.io.Serializable;

public class AddOrderBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8871761794636389755L;
	private String orderId;
	private String tureCost;
	private String payType;
	private String orderNum;
	private String buyNum;
	private String createTime;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTureCost() {
		return tureCost;
	}
	public void setTureCost(String tureCost) {
		this.tureCost = tureCost;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(String buyNum) {
		this.buyNum = buyNum;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
