package com.sinia.orderlang.request;

public class AddOrderRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7548673469545141540L;
	private String userId;
	private String addressId;
	private String carId;
	private String goodId;
	private String goodNum;
	private String goodType;
	private String payType;
	private String orderPrice;
	private String truePrice;

	
	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getAddressId() {
		return addressId;
	}


	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}


	public String getCarId() {
		return carId;
	}


	public void setCarId(String carId) {
		this.carId = carId;
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


	public String getGoodType() {
		return goodType;
	}


	public void setGoodType(String goodType) {
		this.goodType = goodType;
	}


	public String getPayType() {
		return payType;
	}


	public void setPayType(String payType) {
		this.payType = payType;
	}


	public String getOrderPrice() {
		return orderPrice;
	}


	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
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
		return super.toString() + "&userId=" + userId + "&addressId="
				+ addressId + "&carId=" + carId + "&goodId=" + goodId
				+ "&goodNum=" + goodNum + "&goodType=" + goodType + "&payType="
				+ payType + "&orderPrice=" + orderPrice + "&truePrice="
				+ truePrice;
	}
}
