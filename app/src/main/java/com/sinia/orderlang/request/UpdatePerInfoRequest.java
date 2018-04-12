package com.sinia.orderlang.request;

public class UpdatePerInfoRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3330914529716998307L;
	private String userId;
	private String imageUrl;
	private String nickName;
	private String birthday;
	private String sex;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "&" + "userId=" + userId + "&imageUrl="
				+ imageUrl + "&sex=" + sex + "&nickName=" + nickName
				+ "&birthday=" + birthday;
	}
}
