package com.sinia.orderlang.request;

import java.io.Serializable;

import com.loopj.android.http.RequestParams;

public class BaseRequest extends RequestParams implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sessionId;

	private String appKey = "001";

	private String locale = "zh_CN";

	private String messageFormat = "json/xml";

	private String loginName;

	private String sign = "123";

	private String type = "1";

	private String method;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getMessageFormat() {
		return messageFormat;
	}

	public void setMessageFormat(String messageFormat) {
		this.messageFormat = messageFormat;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {

		return "sign=" + sign + "&" + "messageFormat=" + messageFormat + "&"
				+ "locale=" + locale + "&" + "appKey=" + appKey + "&"
				+ "method=" + method;
	}

	public String toAds() {
		return "&method=" + method + "&" + "appKey=" + appKey + "&" + "locale="
				+ locale + "&" + "messageFormat=" + messageFormat + "&"
				+ "sign=" + sign + "&" + "type=" + type;
	}

}
