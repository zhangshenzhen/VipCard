package com.sinia.orderlang.bean;

import java.io.Serializable;
import java.util.List;

public class CollectionListList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8594579037789366417L;
	private List<CollectionListBean> items;
	public List<CollectionListBean> getItems() {
		return items;
	}
	public void setItems(List<CollectionListBean> items) {
		this.items = items;
	}
	
}
