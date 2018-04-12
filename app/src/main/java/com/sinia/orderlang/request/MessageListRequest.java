package com.sinia.orderlang.request;

public class MessageListRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5147439183941381286L;
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
		return super.toString() + "&userId=" + userId;
	}
}
