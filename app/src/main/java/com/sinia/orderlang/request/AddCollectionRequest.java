package com.sinia.orderlang.request;

public class AddCollectionRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9085878752582962009L;
	private String userId;
	private String goodId;
	//类别1.限时2.特价
	private String type;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGoodId() {
		return goodId;
	}
	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString()+"&userId="+userId+"&goodId="+goodId+"&type="+type;
	}
}
