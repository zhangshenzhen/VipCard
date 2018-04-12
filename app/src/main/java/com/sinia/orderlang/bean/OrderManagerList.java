package com.sinia.orderlang.bean;

import java.io.Serializable;
import java.util.List;

public class OrderManagerList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7409308944231151274L;
	private List<OrderManagerBean> items;
	public List<OrderManagerBean> getItems() {
		return items;
	}
	public void setItems(List<OrderManagerBean> items) {
		this.items = items;
	}
	
}
