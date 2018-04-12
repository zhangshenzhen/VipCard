package com.sinia.orderlang.request;

public class CollectionListRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3710875765396524156L;
	private String userId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString()+"&userId="+userId;
	}
}
