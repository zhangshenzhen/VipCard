package com.sinia.orderlang.bean;

import java.io.Serializable;

public class ArrayXnGoodTypeItemsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1260829391102647252L;
	private String typeName;
	private String id;
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
