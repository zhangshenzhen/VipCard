package com.sinia.orderlang.request;

import java.io.Serializable;

public class TeJiaListRequest extends BaseRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9199693469920982456L;
	private String conditional;
	private String goodType;
	private String city;
	private String areaName;
	private String goodName;
	private String begin;
	private String pageLength;
	private String address;
	private String flag_area;

	public String getFlag_area() {
		return flag_area;
	}

	public void setFlag_area(String flag_area) {
		this.flag_area = flag_area;
	}

	public String getConditional() {
		return conditional;
	}

	public void setConditional(String conditional) {
		this.conditional = conditional;
	}

	public String getGoodType() {
		return goodType;
	}

	public void setGoodType(String goodType) {
		this.goodType = goodType;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getPageLength() {
		return pageLength;
	}

	public void setPageLength(String pageLength) {
		this.pageLength = pageLength;
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
		return super.toString() + "&conditional=" + conditional + "&goodType="
				+ goodType + "&city=" + city + "&areaName=" + areaName
				+ "&goodName=" + goodName + "&begin=" + begin + "&pageLength=" + pageLength + "&address=" + address+"&flag_area="+flag_area;
	}
}
