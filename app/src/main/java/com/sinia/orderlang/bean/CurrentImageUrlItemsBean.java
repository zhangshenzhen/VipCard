package com.sinia.orderlang.bean;

import java.io.Serializable;

public class CurrentImageUrlItemsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7553146081851428913L;
	private String imageId;
	private String imageUrl;
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
}
