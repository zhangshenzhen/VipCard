package com.sinia.orderlang.bean;

import java.io.Serializable;
import java.util.List;

public class CarListBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8295067267016879099L;
	private List<MerchantItemsBean> merchantItems;
	private String freight;
	private String addressid;// 用户地址Id
	private String registername;// 用户名称
	private String phoneno;// 用户手机号码
	private String receiptaddress;// 用户收货地址
	public List<MerchantItemsBean> getMerchantItems() {
		return merchantItems;
	}
	public void setMerchantItems(List<MerchantItemsBean> merchantItems) {
		this.merchantItems = merchantItems;
	}
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}

	public String getAddressid() {
		return addressid;
	}

	public void setAddressid(String addressid) {
		this.addressid = addressid;
	}

	public String getRegistername() {
		return registername;
	}

	public void setRegistername(String registername) {
		this.registername = registername;
	}

	public String getPhoneno() {
		return phoneno;
	}

	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}

	public String getReceiptaddress() {
		return receiptaddress;
	}

	public void setReceiptaddress(String receiptaddress) {
		this.receiptaddress = receiptaddress;
	}
}
