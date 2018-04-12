package com.sinia.orderlang.request;

public class UpdatePasswordRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -164682244722226316L;
	private String oldPassword;
	private String newPassword;
	private String userId;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "&oldPassword=" + oldPassword
				+ "&newPassword=" + newPassword + "&userId=" + userId;
	}
}
