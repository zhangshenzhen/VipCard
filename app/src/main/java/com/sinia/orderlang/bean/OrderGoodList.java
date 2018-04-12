package com.sinia.orderlang.bean;

import java.io.Serializable;
import java.util.List;

public class OrderGoodList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7795187078141685144L;
	private List<OrderGoodBean> orderGoodItems;
	public List<OrderGoodBean> getOrderGoodItems() {
		return orderGoodItems;
	}
	public void setOrderGoodItems(List<OrderGoodBean> orderGoodItems) {
		this.orderGoodItems = orderGoodItems;
	}
	
}
