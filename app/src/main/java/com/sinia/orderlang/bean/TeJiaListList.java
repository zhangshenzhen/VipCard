package com.sinia.orderlang.bean;

import java.io.Serializable;
import java.util.List;

public class TeJiaListList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -270985480593128120L;
	private List<TeJiaListBean> goodItems;
	private List<ArrayXnGoodTypeItemsBean> arrayXnGoodTypeItems;
	public List<TeJiaListBean> getGoodItems() {
		return goodItems;
	}
	public void setGoodItems(List<TeJiaListBean> goodItems) {
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
