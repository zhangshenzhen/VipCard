package com.sinia.orderlang.request;

public class RedBaoListRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3187047000822826514L;
	private String city;
	private String areaName;
	private String goodType;
	private String goodName;
	private String address;
	private String begin;
	private String pageLength;
	private String flag_area;

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

	public String getGoodType() {
		return goodType;
	}

	public void setGoodType(String goodType) {
		this.goodType = goodType;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getFlag_area() {
		return flag_area;
	}

	public void setFlag_area(String flag_area) {
		this.flag_area = flag_area;
	}

	@Override
	public String toString() {
		return super.toString() + "&city=" + city + "&areaName=" + areaName
				+ "&goodType=" + goodType + "&goodName=" + goodName+"&address=" + address +"&begin=" + begin+ "&pageLength=" + pageLength + "&flag_area=" + flag_area;
	}
}
