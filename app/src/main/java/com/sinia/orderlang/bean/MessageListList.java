package com.sinia.orderlang.bean;

import java.io.Serializable;
import java.util.List;

public class MessageListList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -214319421120975998L;
	private List<MessageListBean> items;
	public List<MessageListBean> getItems() {
		return items;
	}
	public void setItems(List<MessageListBean> items) {
		this.items = items;
	}
	
}
