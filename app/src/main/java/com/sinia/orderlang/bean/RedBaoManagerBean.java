package com.sinia.orderlang.bean;

import java.io.Serializable;

public class RedBaoManagerBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3695363087856116843L;
	private String orderId;
	private String goodName;
	private String fuGoodName;
	private String title;
	private String fuTitle;
	private String truePrice;
	private String shopName;
	private String bgtime;
	private String edtime;
	private String goodNum;
	private String orderPrice;
	private String imageUrl;
	private String redbaoStatus;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getFuGoodName() {
		return fuGoodName;
	}
	public void setFuGoodName(String fuGoodName) {
		this.fuGoodName = fuGoodName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFuTitle() {
		return fuTitle;
	}
	public void setFuTitle(String fuTitle) {
		this.fuTitle = fuTitle;
	}
	public String getTruePrice() {
		return truePrice;
	}
	public void setTruePrice(String truePrice) {
		this.truePrice = truePrice;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getBgtime() {
		return bgtime;
	}
	public void setBgtime(String bgtime) {
		this.bgtime = bgtime;
	}
	public String getEdtime() {
		return edtime;
	}
	public void setEdtime(String edtime) {
		this.edtime = edtime;
	}
	public String getGoodNum() {
		return goodNum;
	}
	public void setGoodNum(String goodNum) {
		this.goodNum = goodNum;
	}
	public String getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getRedbaoStatus() {
		return redbaoStatus;
	}
	public void setRedbaoStatus(String redbaoStatus) {
		this.redbaoStatus = redbaoStatus;
	}
	
}
