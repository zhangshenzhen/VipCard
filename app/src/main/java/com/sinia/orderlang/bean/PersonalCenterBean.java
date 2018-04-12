package com.sinia.orderlang.bean;

import java.io.Serializable;

public class PersonalCenterBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -341133113545121315L;
	private String customerId;
	private String imageUrl;
	private String userName;
	private String telephone;
	private String nickName;
	private String sex;
	private String birthday;
	private int waitingPayNum;
	private int waitingShouHuoNum;
	private int waitingCommentNum;
	private int waitingApplyShouHouNum;
	private int messageCountNum;
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public int getWaitingPayNum() {
		return waitingPayNum;
	}
	public void setWaitingPayNum(int waitingPayNum) {
		this.waitingPayNum = waitingPayNum;
	}
	public int getWaitingShouHuoNum() {
		return waitingShouHuoNum;
	}
	public void setWaitingShouHuoNum(int waitingShouHuoNum) {
		this.waitingShouHuoNum = waitingShouHuoNum;
	}
	public int getWaitingCommentNum() {
		return waitingCommentNum;
	}
	public void setWaitingCommentNum(int waitingCommentNum) {
		this.waitingCommentNum = waitingCommentNum;
	}
	public int getWaitingApplyShouHouNum() {
		return waitingApplyShouHouNum;
	}
	public void setWaitingApplyShouHouNum(int waitingApplyShouHouNum) {
		this.waitingApplyShouHouNum = waitingApplyShouHouNum;
	}
	public int getMessageCountNum() {
		return messageCountNum;
	}
	public void setMessageCountNum(int messageCountNum) {
		this.messageCountNum = messageCountNum;
	}
	
}
