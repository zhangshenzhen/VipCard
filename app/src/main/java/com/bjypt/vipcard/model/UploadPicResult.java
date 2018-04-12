package com.bjypt.vipcard.model;

/**
 * Created by 崔龙 on 2017/9/28.
 */

public class UploadPicResult {
    /**
     * resultStatus : 0
     * resultData : {"fileName":"http://127.0.0.1/base_discuz/data/attachment/forum/201711/25/162728uess7m7yzwdwbha4.png","aid":"62"}
     * msg : 1
     */

    private String resultStatus;
    private ResultDataBean resultData;
    private String msg;

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public ResultDataBean getResultData() {
        return resultData;
    }

    public void setResultData(ResultDataBean resultData) {
        this.resultData = resultData;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class ResultDataBean {
        /**
         * fileName : http://127.0.0.1/base_discuz/data/attachment/forum/201711/25/162728uess7m7yzwdwbha4.png
         * aid : 62
         */

        private String fileName;
        private String aid;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }
    }
}
