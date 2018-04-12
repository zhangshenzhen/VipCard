package com.sinia.orderlang.request;

public class DelRedBaoRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4106903992740501838L;
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
