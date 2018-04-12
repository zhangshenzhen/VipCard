package com.sinia.orderlang.bean;

import java.io.Serializable;
import java.util.List;

public class RedpacketBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4863878701553951935L;
	private List<RedpacketItems> goodItems;
	private List<ArrayXnGoodTypeItemsBean> arrayXnGoodTypeItems;
	public List<RedpacketItems> getGoodItems() {
		return goodItems;
	}

	public void setGoodItems(List<RedpacketItems> goodItems) {
		this.goodItems = goodItems;
	}

	public List<ArrayXnGoodTypeItemsBean> getArrayXnGoodTypeItems() {
		return arrayXnGoodTypeItems;
	}

	public void setArrayXnGoodTypeItems(
			List<ArrayXnGoodTypeItemsBean> arrayXnGoodTypeItems) {
		this.arrayXnGoodTypeItems = arrayXnGoodTypeItems;
	}
	
}
