package com.sinia.orderlang.request;

public class OrderCommentRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1348999714020745029L;
	private String orderId;
	private String userId;
	private String star;
	private String content;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
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
		return super.toString() + "&orderId=" + orderId + "&userId=" + userId
				+ "&star=" + star + "&content=" + content;
	}
}
