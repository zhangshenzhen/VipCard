package com.sinia.orderlang.bean;

import java.io.Serializable;

public class ImageUrlItemsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8825253843259858176L;

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
