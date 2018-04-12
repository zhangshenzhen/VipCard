package com.sinia.orderlang.request;

public class RedBaoManagerDetailRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4125118500309196769L;
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
