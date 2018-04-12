package com.sinia.orderlang.bean;

import java.io.Serializable;
import java.util.List;

public class RedpacketItems implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -46178693747577874L;
	private String goodId;
	private String logo;
	private String title;
	private String goodName;
	private String nowPrice;
	private String hasNum;
	private String endTime;
	private String area;
	private String street;
	private String areaCircle;
	private String shopName;
	private String muname;
	private String buyNum;
	private String fuTitle;
	private String fuGoodName;
	private String city;
	private int goodStatus;
	private String address_kill;

	public String getAddress_kill() {
		return address_kill;
	}

	public void setAddress_kill(String address_kill) {
		this.address_kill = address_kill;
	}

	public int getGoodStatus() {
		return goodStatus;
	}

	public void setGoodStatus(int goodStatus) {
		this.goodStatus = goodStatus;
	}

	public String getGoodId() {
		return goodId;
	}

	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
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

	public String getHasNum() {
		return hasNum;
	}

	public void setHasNum(String hasNum) {
		this.hasNum = hasNum;
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

	public String getMuname() {
		return muname;
	}

	public void setMuname(String muname) {
		this.muname = muname;
	}

	public String getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(String buyNum) {
		this.buyNum = buyNum;
	}

	public String getFuTitle() {
		return fuTitle;
	}

	public void setFuTitle(String fuTitle) {
		this.fuTitle = fuTitle;
	}

	public String getFuGoodName() {
		return fuGoodName;
	}

	public void setFuGoodName(String fuGoodName) {
		this.fuGoodName = fuGoodName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
