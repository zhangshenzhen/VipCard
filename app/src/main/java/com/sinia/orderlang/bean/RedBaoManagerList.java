package com.sinia.orderlang.bean;

import java.io.Serializable;
import java.util.List;

public class RedBaoManagerList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1784233103957143022L;
	private List<RedBaoManagerBean> items;
	public List<RedBaoManagerBean> getItems() {
		return items;
	}
	public void setItems(List<RedBaoManagerBean> items) {
		this.items = items;
	}
}
