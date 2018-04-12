package com.sinia.orderlang.request;

public class OrderManagerRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 248176819955681850L;
	private String userId;
	private String type;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "&userId=" + userId + "&type=" + type;
	}
}
