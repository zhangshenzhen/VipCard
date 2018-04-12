package com.sinia.orderlang.request;

public class ApplyRefundRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3310940657204840303L;
	private String orderId;
	private String content;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "&orderId=" + orderId +"&content="+content;
	}
}
