package com.sinia.orderlang.bean;

import java.io.Serializable;

public class GoodsListBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3699348468468308241L;
	private String imageUrl;
	private String goodName;
	private String goodNum;
	private String price;
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getGoodNum() {
		return goodNum;
	}
	public void setGoodNum(String goodNum) {
		this.goodNum = goodNum;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
}
