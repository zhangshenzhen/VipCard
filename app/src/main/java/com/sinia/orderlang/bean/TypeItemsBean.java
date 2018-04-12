package com.sinia.orderlang.bean;

import java.io.Serializable;

public class TypeItemsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2698426186816251018L;
	private String typeId;
	private String typeName;
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
