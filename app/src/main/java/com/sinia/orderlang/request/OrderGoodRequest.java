package com.sinia.orderlang.request;

public class OrderGoodRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7867717235022398012L;
	private String orderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "&orderId=" + orderId;
	}
}
