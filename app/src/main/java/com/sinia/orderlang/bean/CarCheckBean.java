package com.sinia.orderlang.bean;

import java.io.Serializable;
import java.util.List;

public class CarCheckBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7516853677407185691L;
	private List<GoodItemsBean> data;
	public List<GoodItemsBean> getData() {
		return data;
	}
	public void setData(List<GoodItemsBean> data) {
		this.data = data;
	}
	
}
