package com.sinia.orderlang.request;

public class PayRedBaoRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3801744799195354675L;
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
		return super.toString()+"&payType="+payType+"&orderId="+orderId;
	}
}
