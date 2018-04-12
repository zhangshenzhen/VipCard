package com.sinia.orderlang.bean;

import java.io.Serializable;
import java.util.List;

public class AddressListList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2906794983968752968L;
	private List<AddressListBean> items;
	public List<AddressListBean> getItems() {
		return items;
	}
	public void setItems(List<AddressListBean> items) {
		this.items = items;
	}
	
}
