package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 崔龙 on 2017/7/5.
 */

public class FuYangNews {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : [{"aid":"4","catid":"1","title":"20万级纯正血统的德系神车 实拍大众(进口)Tiguan","create_time":"2017-06-27 09:21:00","catname":"热点","viewnum":"29","commentnum":"0","attachment":[{"attachid":"20","isimage":"1","attachment":"201706/27/092502s6p3aeas138o8tm3.jpg","remote":"0","thumb":"1"},{"attachid":"21","isimage":"1","attachment":"201706/27/092503cr6i6aantzs7aktj.jpg","remote":"0","thumb":"1"},{"attachid":"22","isimage":"1","attachment":"201706/27/092504d1v1u4o4c4g9l398.jpg","remote":"0","thumb":"1"}]},{"aid":"3","catid":"1","title":"最后两个","create_time":"2017-06-26 18:35:00","catname":"热点","viewnum":"21","commentnum":"0","attachment":[{"attachid":"26","isimage":"1","attachment":"201706/27/142002v2f5rqeprp8g593u.jpg","remote":"0","thumb":"1"},{"attachid":"27","isimage":"1","attachment":"201706/27/142003r1198gvv1zd8fv1r.jpg","remote":"0","thumb":"1"},{"attachid":"28","isimage":"1","attachment":"201706/27/142004dki9k9typjz3ykip.jpg","remote":"0","thumb":"1"}]},{"aid":"2","catid":"1","title":"最后一个","create_time":"2017-06-26 18:31:00","catname":"热点","viewnum":"20","commentnum":"0","attachment":[{"attachid":"29","isimage":"1","attachment":"201706/27/142054odmdhidm1hskivih.jpg","remote":"0","thumb":"1"},{"attachid":"30","isimage":"1","attachment":"201706/27/142056p9qrqmo6rd2aehhr.jpg","remote":"0","thumb":"1"},{"attachid":"31","isimage":"1","attachment":"201706/27/142057x5f5b4f8hnf65q8f.jpg","remote":"0","thumb":"1"}]},{"aid":"1","catid":"1","title":"暴雨蓝色预警继续发布 北京河北等局地大到暴雨","create_time":"2017-06-21 15:06:00","catname":"热点","viewnum":"64","commentnum":"0","attachment":[]}]
     */

    private int resultStatus;
    private String msg;
    private List<ResultDataBean> resultData;

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

    public List<ResultDataBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<ResultDataBean> resultData) {
        this.resultData = resultData;
    }

    public static class ResultDataBean {
        /**
         * aid : 4
         * catid : 1
         * title : 20万级纯正血统的德系神车 实拍大众(进口)Tiguan
         * create_time : 2017-06-27 09:21:00
         * catname : 热点
         * viewnum : 29
         * commentnum : 0
         * attachment : [{"attachid":"20","isimage":"1","attachment":"201706/27/092502s6p3aeas138o8tm3.jpg","remote":"0","thumb":"1"},{"attachid":"21","isimage":"1","attachment":"201706/27/092503cr6i6aantzs7aktj.jpg","remote":"0","thumb":"1"},{"attachid":"22","isimage":"1","attachment":"201706/27/092504d1v1u4o4c4g9l398.jpg","remote":"0","thumb":"1"}]
         */

        private String aid;
        private String catid;
        private String title;
        private String create_time;
        private String catname;
        private String viewnum;
        private String commentnum;
        private List<AttachmentBean> attachment;
        private String mobileurl;

        public String getMobileurl() {
            return mobileurl;
        }

        public void setMobileurl(String mobileurl) {
            this.mobileurl = mobileurl;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getCatid() {
            return catid;
        }

        public void setCatid(String catid) {
            this.catid = catid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getCatname() {
            return catname;
        }

        public void setCatname(String catname) {
            this.catname = catname;
        }

        public String getViewnum() {
            return viewnum;
        }

        public void setViewnum(String viewnum) {
            this.viewnum = viewnum;
        }

        public String getCommentnum() {
            return commentnum;
        }

        public void setCommentnum(String commentnum) {
            this.commentnum = commentnum;
        }

        public List<AttachmentBean> getAttachment() {
            return attachment;
        }

        public void setAttachment(List<AttachmentBean> attachment) {
            this.attachment = attachment;
        }

        public static class AttachmentBean {
            /**
             * attachid : 20
             * isimage : 1
             * attachment : 201706/27/092502s6p3aeas138o8tm3.jpg
             * remote : 0
             * thumb : 1
             */

            private String attachid;
            private String isimage;
            private String attachment;
            private String remote;
            private String thumb;

            public String getAttachid() {
                return attachid;
            }

            public void setAttachid(String attachid) {
                this.attachid = attachid;
            }

            public String getIsimage() {
                return isimage;
            }

            public void setIsimage(String isimage) {
                this.isimage = isimage;
            }

            public String getAttachment() {
                return attachment;
            }

            public void setAttachment(String attachment) {
                this.attachment = attachment;
            }

            public String getRemote() {
                return remote;
            }

            public void setRemote(String remote) {
                this.remote = remote;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }
        }
    }
}
