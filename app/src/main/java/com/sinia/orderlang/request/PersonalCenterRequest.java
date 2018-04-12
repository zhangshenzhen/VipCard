package com.sinia.orderlang.request;

public class PersonalCenterRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6988622346424481837L;
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
		return super.toString() + "&" + "userId=" + userId;
	}
}
