package com.sinia.orderlang.bean;

import java.io.Serializable;
import java.util.List;

public class RedBaoDetailBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -389538923992340113L;
	
	private int collectionStauts;
	private String title;
	private String goodName;
	private String nowPrice;
	private String beforePrice;
	private String buyNum;
	private String hasNum;
	private String startTime;
	private String endTime;
	private String area;
	private String city;
	private String street;
	private String areaCircle;
	private String shopName;
	private String merchantPhone;
	private String useDayBeginTime;
	private String useDayEndTime;
	private String deadLine;
	private String fuTitle;
	private String content;
	private String fuGoodName;
	private String address_kill;
	private List<ImageUrlItemsBean> imageUrlItems;
	
	public int getCollectionStauts() {
		return collectionStauts;
	}
	public void setCollectionStauts(int collectionStauts) {
		this.collectionStauts = collectionStauts;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getNowPrice() {
		return nowPrice;
	}
	public void setNowPrice(String nowPrice) {
		this.nowPrice = nowPrice;
	}
	public String getBeforePrice() {
		return beforePrice;
	}
	public void setBeforePrice(String beforePrice) {
		this.beforePrice = beforePrice;
	}
	public String getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(String buyNum) {
		this.buyNum = buyNum;
	}
	public String getHasNum() {
		return hasNum;
	}
	public void setHasNum(String hasNum) {
		this.hasNum = hasNum;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getAreaCircle() {
		return areaCircle;
	}
	public void setAreaCircle(String areaCircle) {
		this.areaCircle = areaCircle;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getMerchantPhone() {
		return merchantPhone;
	}
	public void setMerchantPhone(String merchantPhone) {
		this.merchantPhone = merchantPhone;
	}
	public String getUseDayBeginTime() {
		return useDayBeginTime;
	}
	public void setUseDayBeginTime(String useDayBeginTime) {
		this.useDayBeginTime = useDayBeginTime;
	}
	public String getUseDayEndTime() {
		return useDayEndTime;
	}
	public void setUseDayEndTime(String useDayEndTime) {
		this.useDayEndTime = useDayEndTime;
	}
	public String getDeadLine() {
		return deadLine;
	}
	public void setDeadLine(String deadLine) {
		this.deadLine = deadLine;
	}
	public List<ImageUrlItemsBean> getImageUrlItems() {
		return imageUrlItems;
	}
	public void setImageUrlItems(List<ImageUrlItemsBean> imageUrlItems) {
		this.imageUrlItems = imageUrlItems;
	}
	public String getFuTitle() {
		return fuTitle;
	}
	public void setFuTitle(String fuTitle) {
		this.fuTitle = fuTitle;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFuGoodName() {
		return fuGoodName;
	}
	public void setFuGoodName(String fuGoodName) {
		this.fuGoodName = fuGoodName;
	}

	public String getAddress_kill() {
		return address_kill;
	}

	public void setAddress_kill(String address_kill) {
		this.address_kill = address_kill;
	}
}
