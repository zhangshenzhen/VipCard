package com.sinia.orderlang.request;

public class TeJiaDetailRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3697464952177088326L;
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
		return super.toString() + "&goodId=" + goodId + "&userId=" + userId;
	}
}
