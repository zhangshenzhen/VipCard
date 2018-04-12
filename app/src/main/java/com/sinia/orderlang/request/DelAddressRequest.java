package com.sinia.orderlang.request;

public class DelAddressRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1381287391747596613L;
	private String addressId;
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString()+"&addressId="+addressId;
	}
}
