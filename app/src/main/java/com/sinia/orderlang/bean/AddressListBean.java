package com.sinia.orderlang.bean;

import java.io.Serializable;

public class AddressListBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6648417400315053177L;
	private String addressId;
	private String name;
	private String telephone;
	private String area;
	private String address;

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "&addressId=" + addressId + "&name=" + name
				+ "&telephone=" + telephone + "&area=" + area + "&address="
				+ address;
	}
}
