package com.sinia.orderlang.bean;

import java.io.Serializable;

public class AddRedBaoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1126005610231780052L;
	private String orderId;
	private String truePrice;
	private String payType;
	private String orderNum;
	private String goodNum;
	private String createTime;
	private String redBaoStatus;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTureCost() {
		return truePrice;
	}
	public void setTureCost(String tureCost) {
		this.truePrice = tureCost;
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
	public String getGoodNum() {
		return goodNum;
	}
	public void setGoodNum(String goodNum) {
		this.goodNum = goodNum;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getRedBaoStatus() {
		return redBaoStatus;
	}
	public void setRedBaoStatus(String redBaoStatus) {
		this.redBaoStatus = redBaoStatus;
	}
	
}
