package com.sinia.orderlang.request;

public class AddressListRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4680380766904345708L;
	private String userId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return super.toString()+"&userId="+userId;
	}
}
