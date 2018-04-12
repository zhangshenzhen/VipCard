package com.sinia.orderlang.bean;

import java.io.Serializable;
import java.util.List;

public class TeJiaListBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6093446756311176975L;
	private String goodId;
	private String logo;
	private String goodName;
	private String nowPrice;
	private String beforePrice;
	private String saleNum;
	private String address;
	private String muname;
	private String city;
	private String area;
	private String tradeName;
	private String street;
	private String shopName;
	private String address_tj;
	private List<ArrayXnGoodTypeItemsBean> arrayXnGoodTypeItems;

	public String getAddress_tj() {
		return address_tj;
	}

	public void setAddress_tj(String address_tj) {
		this.address_tj = address_tj;
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
	public String getSaleNum() {
		return saleNum;
	}
	public void setSaleNum(String saleNum) {
		this.saleNum = saleNum;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMuname() {
		return muname;
	}
	public void setMuname(String muname) {
		this.muname = muname;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public List<ArrayXnGoodTypeItemsBean> getArrayXnGoodTypeItems() {
		return arrayXnGoodTypeItems;
	}
	public void setArrayXnGoodTypeItems(
			List<ArrayXnGoodTypeItemsBean> arrayXnGoodTypeItems) {
		this.arrayXnGoodTypeItems = arrayXnGoodTypeItems;
	}
	
}
