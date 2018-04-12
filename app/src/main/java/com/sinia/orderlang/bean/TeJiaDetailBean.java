package com.sinia.orderlang.bean;

import java.io.Serializable;
import java.util.List;

public class TeJiaDetailBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8139478436771906179L;

	private String addressid;// 用户地址Id
	private String registername;// 用户名称
	private String phoneno;// 用户手机号码
	private String receiptaddress;// 用户收货地址

	private String collectionStauts;
	private String goodName;
	private String nowPrice;
	private String beforePrice;
	private String fright;
	private String saleNum;
	private String detailImage;
	private String canshuImage;
	private List<CurrentImageUrlItemsBean> currentImageUrlItems;
	private List<TypeItemsBean> typeItems;
	private List<CommentItemsBean> commentItems;
	private String commentSize;
	public String getCollectionStauts() {
		return collectionStauts;
	}
	public void setCollectionStauts(String collectionStauts) {
		this.collectionStauts = collectionStauts;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getNowPrice() {
		return nowPrice;
	}
	public void setNowPrice(String nowPrice) {
		this.nowPrice = nowPrice;
	}
	public String getBeforePrice() {
		return beforePrice;
	}
	public void setBeforePrice(String beforePrice) {
		this.beforePrice = beforePrice;
	}
	public String getFright() {
		return fright;
	}
	public void setFright(String fright) {
		this.fright = fright;
	}
	public String getSaleNum() {
		return saleNum;
	}
	public void setSaleNum(String saleNum) {
		this.saleNum = saleNum;
	}
	public String getDetailImage() {
		return detailImage;
	}
	public void setDetailImage(String detailImage) {
		this.detailImage = detailImage;
	}
	public String getCanshuImage() {
		return canshuImage;
	}
	public void setCanshuImage(String canshuImage) {
		this.canshuImage = canshuImage;
	}
	public List<CurrentImageUrlItemsBean> getCurrentImageUrlItems() {
		return currentImageUrlItems;
	}
	public void setCurrentImageUrlItems(
			List<CurrentImageUrlItemsBean> currentImageUrlItems) {
		this.currentImageUrlItems = currentImageUrlItems;
	}
	public List<TypeItemsBean> getTypeItems() {
		return typeItems;
	}
	public void setTypeItems(List<TypeItemsBean> typeItems) {
		this.typeItems = typeItems;
	}
	public List<CommentItemsBean> getCommentItems() {
		return commentItems;
	}
	public void setCommentItems(List<CommentItemsBean> commentItems) {
		this.commentItems = commentItems;
	}
	public String getCommentSize() {
		return commentSize;
	}
	public void setCommentSize(String commentSize) {
		this.commentSize = commentSize;
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
