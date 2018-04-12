package com.sinia.orderlang.request;

public class CarDeleteRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6046402026564992806L;
	private String carId;

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "&carId=" + carId;
	}
}
