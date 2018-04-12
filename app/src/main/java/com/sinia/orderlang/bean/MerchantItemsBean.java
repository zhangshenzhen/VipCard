package com.sinia.orderlang.bean;

import java.io.Serializable;
import java.util.List;

public class MerchantItemsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3104603716578701894L;
	private String merchantName;
	private List<GoodItemsBean> goodItems;
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public List<GoodItemsBean> getGoodItems() {
		return goodItems;
	}
	public void setGoodItems(List<GoodItemsBean> goodItems) {
		this.goodItems = goodItems;
	}
	
}
