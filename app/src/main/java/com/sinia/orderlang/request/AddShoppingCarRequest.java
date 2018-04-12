package com.sinia.orderlang.request;

public class AddShoppingCarRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8250591778217566913L;
	private String userId;
	private String goodId;
	private String goodType;
	private String goodNum;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGoodId() {
		return goodId;
	}

	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}

	public String getGoodType() {
		return goodType;
	}

	public void setGoodType(String goodType) {
		this.goodType = goodType;
	}

	public String getGoodNum() {
		return goodNum;
	}

	public void setGoodNum(String goodNum) {
		this.goodNum = goodNum;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "&userId=" + userId + "&goodId=" + goodId
				+ "&goodType=" + goodType + "&goodNum=" + goodNum;
	}
}
