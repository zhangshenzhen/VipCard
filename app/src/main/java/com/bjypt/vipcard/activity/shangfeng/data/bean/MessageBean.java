package com.bjypt.vipcard.activity.shangfeng.data.bean;

/**
 * 消息 Bean
 */
public class MessageBean {
    /**
     * 消息 ID
     */
    private int jpushrecordid;

    /**
     * 消息类型
     * user_notification：用户消息
     * server_notification：系统消息
     */
    private String business_code;
    /**
     * 消息标题
     */
    private String title;

    /**
     * content  消息内容
     */
    private String content;

    /**
     * 状态 0: 正常 9:已删除
     */
    private int status;

    /**
     * 创建时间
     */
    private String create_at;


    public MessageBean(int jpushrecordid, String business_code, String title, String content, int status, String create_at) {
        this.jpushrecordid = jpushrecordid;
        this.business_code = business_code;
        this.title = title;
        this.content = content;
        this.status = status;
        this.create_at = create_at;
    }

    public int getJpushrecordid() {
        return jpushrecordid;
    }

    public void setJpushrecordid(int jpushrecordid) {
        this.jpushrecordid = jpushrecordid;
    }

    public String getBusiness_code() {
        return business_code;
    }

    public void setBusiness_code(String business_code) {
        this.business_code = business_code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "jpushrecordid=" + jpushrecordid +
                ", business_code='" + business_code + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", create_at='" + create_at + '\'' +
                '}';
    }
}
