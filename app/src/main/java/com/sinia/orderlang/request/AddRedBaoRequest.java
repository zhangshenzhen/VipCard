package com.sinia.orderlang.request;

public class AddRedBaoRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2591022192680389714L;
	private String userId;
	private String goodId;
	private String goodNum;
	private String payType;
	private String truePrice;

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

	public String getGoodNum() {
		return goodNum;
	}

	public void setGoodNum(String goodNum) {
		this.goodNum = goodNum;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getTruePrice() {
		return truePrice;
	}

	public void setTruePrice(String truePrice) {
		this.truePrice = truePrice;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "&userId=" + userId + "&goodId=" + goodId
				+ "&goodNum=" + goodNum + "&payType=" + payType + "&truePrice="
				+ truePrice;
	}
}
