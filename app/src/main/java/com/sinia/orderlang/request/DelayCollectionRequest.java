package com.sinia.orderlang.request;

import java.io.Serializable;

public class DelayCollectionRequest extends BaseRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2737390258797352477L;
	private String goodId;
	private String userId;
	public String getGoodId() {
		return goodId;
	}
	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString()+"&goodId="+goodId+"&userId="+userId;
	}
}
