package com.sinia.orderlang.bean;

import java.io.Serializable;
import java.util.List;

public class GoodsListList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1503137412343891269L;
	private List<GoodsListBean> items;
	public List<GoodsListBean> getItems() {
		return items;
	}
	public void setItems(List<GoodsListBean> items) {
		this.items = items;
	}
	
}
