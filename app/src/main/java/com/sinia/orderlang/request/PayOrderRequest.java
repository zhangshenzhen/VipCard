package com.sinia.orderlang.request;

public class PayOrderRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -528764138552225038L;
	private String payType;
	private String orderId;

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "&payType=" + payType + "&orderId=" + orderId;
	}

}
