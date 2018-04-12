package com.sinia.orderlang.bean;

import java.io.Serializable;

public class MessageListBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4434802685789400262L;
	private String messageId;
	private String content;
	private String messStatus;
	private String createTime;
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMessStatus() {
		return messStatus;
	}
	public void setMessStatus(String messStatus) {
		this.messStatus = messStatus;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
