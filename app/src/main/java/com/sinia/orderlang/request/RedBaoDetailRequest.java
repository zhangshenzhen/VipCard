package com.sinia.orderlang.request;

public class RedBaoDetailRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -873329837801631032L;
	private String goodId;
	private String userId;
	public String getGoodId() {
		return goodId;
	}
	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString()+"&goodId="+goodId+"&userId="+userId;
	}
}
