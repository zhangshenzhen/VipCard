package com.sinia.orderlang.bean;

import java.io.Serializable;

public class CollectionListBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2014032971669272749L;
	private String collectionId;
	private String goodName;
	private String beforePrice;
	private String nowPrice;
	private String saleNum;
	private String imageUrl;
	private String title;
	private String endTime;
	private String hasNum;
	private String type;
	private String goodId;
	
	public String getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getBeforePrice() {
		return beforePrice;
	}
	public void setBeforePrice(String beforePrice) {
		this.beforePrice = beforePrice;
	}
	public String getNowPrice() {
		return nowPrice;
	}
	public void setNowPrice(String nowPrice) {
		this.nowPrice = nowPrice;
	}
	public String getSaleNum() {
		return saleNum;
	}
	public void setSaleNum(String saleNum) {
		this.saleNum = saleNum;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getHasNum() {
		return hasNum;
	}
	public void setHasNum(String hasNum) {
		this.hasNum = hasNum;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGoodId() {
		return goodId;
	}
	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}
	
}
