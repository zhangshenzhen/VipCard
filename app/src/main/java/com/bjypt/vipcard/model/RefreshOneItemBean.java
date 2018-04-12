package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 崔龙 on 2017/12/6.
 */

public class RefreshOneItemBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"tid":197,"subject":"😂😂😂","dateline":1512530763,"views":1,"replies":0,"authorid":2408,"postTime":"刚刚","favor_flag":0,"like_flag":0,"follow_flag":0,"authorInfo":{"uid":2408,"username":"对对对","email":"15156679232@hyb.com","avatar":"http://img-cn-hangzhou.aliyuncs.com/huiyuanbao/userposition_989d444e19854c829e17df9bea683e54.jpg"},"attachmentList":[{"aid":375,"attachment":"http://47.93.79.174/base_discuz/data/attachment/forum/201712/06/112602tb56yeer4zva959j.jpeg","isimage":true}],"likeUserList":[],"commentList":[]}
     */

    private int resultStatus;
    private String msg;
    private ResultDataBean resultData;

    public int getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(int resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultDataBean getResultData() {
        return resultData;
    }

    public void setResultData(ResultDataBean resultData) {
        this.resultData = resultData;
    }

    public static class ResultDataBean {
        /**
         * tid : 197
         * subject : 😂😂😂
         * dateline : 1512530763
         * views : 1
         * replies : 0
         * authorid : 2408
         * postTime : 刚刚
         * favor_flag : 0
         * like_flag : 0
         * follow_flag : 0
         * authorInfo : {"uid":2408,"username":"对对对","email":"15156679232@hyb.com","avatar":"http://img-cn-hangzhou.aliyuncs.com/huiyuanbao/userposition_989d444e19854c829e17df9bea683e54.jpg"}
         * attachmentList : [{"aid":375,"attachment":"http://47.93.79.174/base_discuz/data/attachment/forum/201712/06/112602tb56yeer4zva959j.jpeg","isimage":true}]
         * likeUserList : []
         * commentList : []
         */

        private int tid;
        private String subject;
        private int dateline;
        private int views;
        private int replies;
        private int authorid;
        private String postTime;
        private int favor_flag;
        private int like_flag;
        private int follow_flag;
        private AuthorInfoBean authorInfo;
        private List<AttachmentListBean> attachmentList;
        private List<LikeUserListBean> likeUserList;
        private List<socialCommentResponseListBean> commentList;

        public int getTid() {
            return tid;
        }

        public void setTid(int tid) {
            this.tid = tid;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public int getDateline() {
            return dateline;
        }

        public void setDateline(int dateline) {
            this.dateline = dateline;
        }

        public int getViews() {
            return views;
        }

        public void setViews(int views) {
            this.views = views;
        }

        public int getReplies() {
            return replies;
        }

        public void setReplies(int replies) {
            this.replies = replies;
        }

        public int getAuthorid() {
            return authorid;
        }

        public void setAuthorid(int authorid) {
            this.authorid = authorid;
        }

        public String getPostTime() {
            return postTime;
        }

        public void setPostTime(String postTime) {
            this.postTime = postTime;
        }

        public int getFavor_flag() {
            return favor_flag;
        }

        public void setFavor_flag(int favor_flag) {
            this.favor_flag = favor_flag;
        }

        public int getLike_flag() {
            return like_flag;
        }

        public void setLike_flag(int like_flag) {
            this.like_flag = like_flag;
        }

        public int getFollow_flag() {
            return follow_flag;
        }

        public void setFollow_flag(int follow_flag) {
            this.follow_flag = follow_flag;
        }

        public AuthorInfoBean getAuthorInfo() {
            return authorInfo;
        }

        public void setAuthorInfo(AuthorInfoBean authorInfo) {
            this.authorInfo = authorInfo;
        }

        public List<AttachmentListBean> getAttachmentList() {
            return attachmentList;
        }

        public void setAttachmentList(List<AttachmentListBean> attachmentList) {
            this.attachmentList = attachmentList;
        }

        public List<LikeUserListBean> getLikeUserList() {
            return likeUserList;
        }

        public void setLikeUserList(List<LikeUserListBean> likeUserList) {
            this.likeUserList = likeUserList;
        }

        public List<socialCommentResponseListBean> getCommentList() {
            return commentList;
        }

        public void setCommentList(List<socialCommentResponseListBean> commentList) {
            this.commentList = commentList;
        }

        public static class AuthorInfoBean {
            /**
             * uid : 2408
             * username : 对对对
             * email : 15156679232@hyb.com
             * avatar : http://img-cn-hangzhou.aliyuncs.com/huiyuanbao/userposition_989d444e19854c829e17df9bea683e54.jpg
             */

            private int uid;
            private String username;
            private String email;
            private String avatar;

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }

        public static class AttachmentListBean {
            /**
             * aid : 375
             * attachment : http://47.93.79.174/base_discuz/data/attachment/forum/201712/06/112602tb56yeer4zva959j.jpeg
             * isimage : true
             */

            private int aid;
            private String attachment;
            private boolean isimage;

            public int getAid() {
                return aid;
            }

            public void setAid(int aid) {
                this.aid = aid;
            }

            public String getAttachment() {
                return attachment;
            }

            public void setAttachment(String attachment) {
                this.attachment = attachment;
            }

            public boolean isIsimage() {
                return isimage;
            }

            public void setIsimage(boolean isimage) {
                this.isimage = isimage;
            }
        }
    }
}
