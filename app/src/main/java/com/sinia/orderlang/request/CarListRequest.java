package com.sinia.orderlang.request;

public class CarListRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8762874889298578321L;
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
