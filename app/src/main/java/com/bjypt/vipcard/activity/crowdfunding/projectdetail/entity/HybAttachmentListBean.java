package com.bjypt.vipcard.activity.crowdfunding.projectdetail.entity;

import java.io.Serializable;

public class HybAttachmentListBean implements Serializable {
    /**
     * attach_type : 0
     * attachment : https://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/f2742f94a6614274bda6212236ae3368.jpg
     */

    private Integer attach_type;
    private String attachment;

    public Integer getAttach_type() {
        return attach_type;
    }

    public void setAttach_type(int attach_type) {
        this.attach_type = attach_type;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

}
