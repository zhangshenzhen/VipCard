package com.sinia.orderlang.bean;

import java.io.Serializable;

public class CommentItemsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1867281855536620938L;
	private String typeId;
	private String content;
	private String createTime;
	private String telephone;
	private String star;
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getStar() {
		return star;
	}
	public void setStar(String star) {
		this.star = star;
	}
	
}
