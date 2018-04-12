package com.sinia.orderlang.request;

public class DelXnOrderByIdRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6941789098107853230L;
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
