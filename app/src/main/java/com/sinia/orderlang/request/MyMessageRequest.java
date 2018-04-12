package com.sinia.orderlang.request;

public class MyMessageRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5588707868231016793L;

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
