package com.sinia.orderlang.request;

public class AddAdviceRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8040501709770086532L;
	private String userId;
	private String content;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "&userId=" + userId + "&content=" + content;
	}
}
