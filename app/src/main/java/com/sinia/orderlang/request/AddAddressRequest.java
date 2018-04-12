package com.sinia.orderlang.request;

public class AddAddressRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2150487107870212830L;
	private String userId;
	private String addCusName;
	private String addCusAddress;
	private String addCusArea;
	private String addCusTelephone;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAddCusName() {
		return addCusName;
	}

	public void setAddCusName(String addCusName) {
		this.addCusName = addCusName;
	}

	public String getAddCusAddress() {
		return addCusAddress;
	}

	public void setAddCusAddress(String addCusAddress) {
		this.addCusAddress = addCusAddress;
	}

	public String getAddCusArea() {
		return addCusArea;
	}

	public void setAddCusArea(String addCusArea) {
		this.addCusArea = addCusArea;
	}

	public String getAddCusTelephone() {
		return addCusTelephone;
	}

	public void setAddCusTelephone(String addCusTelephone) {
		this.addCusTelephone = addCusTelephone;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "&userId=" + userId + "&addCusName="
				+ addCusName + "&addCusAddress=" + addCusAddress
				+ "&addCusArea=" + addCusArea + "&addCusTelephone="
				+ addCusTelephone;
	}
}
