package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2018/3/27.
 */

public class ShopStreetBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : [{"pkmertype":"317d7cf98c44467688dfc99f8e9284d7","mtname":"烤肉","mtlevel":2,"parentpk":"7a5ad72b8b29411cbf79a10f2a6204cb","type":"0","merchantCount":2},{"pkmertype":"3e18adc0abcf4354b2c7cc8396e2b894","mtname":"地方特色菜","mtlevel":2,"parentpk":"7a5ad72b8b29411cbf79a10f2a6204cb","type":"0","merchantCount":43},{"pkmertype":"4c6a2b3bbff44fecb350a6e75f7d11c5","mtname":"蛋糕甜点","mtlevel":2,"parentpk":"7a5ad72b8b29411cbf79a10f2a6204cb","type":"0","merchantCount":1},{"pkmertype":"7778ad69dfc642efb1bfda5535e89fc1","mtname":"甜点饮品","mtlevel":2,"parentpk":"7a5ad72b8b29411cbf79a10f2a6204cb","type":"0","merchantCount":2},{"pkmertype":"8faecbe778a04e56bde99c8bc4dc084b","mtname":"西餐","mtlevel":2,"parentpk":"7a5ad72b8b29411cbf79a10f2a6204cb","type":"0","merchantCount":0},{"pkmertype":"8fb7d65820b24085b8b240322bdc8166","mtname":"料理","mtlevel":2,"parentpk":"7a5ad72b8b29411cbf79a10f2a6204cb","type":"0","merchantCount":1},{"pkmertype":"d33ec729a2e64eeeb943358833a2e555","mtname":"自助餐","mtlevel":2,"parentpk":"7a5ad72b8b29411cbf79a10f2a6204cb","type":"0","merchantCount":1},{"pkmertype":"e2e887b297b74ab7abb29835892d3570","mtname":"小吃快餐","mtlevel":2,"parentpk":"7a5ad72b8b29411cbf79a10f2a6204cb","type":"0","merchantCount":39},{"pkmertype":"f190c0156c064dbbbde40f86ee6cc43e","mtname":"火锅","mtlevel":2,"parentpk":"7a5ad72b8b29411cbf79a10f2a6204cb","type":"0","merchantCount":3},{"pkmertype":"a9cb9631cda04b12b166ddf8fed16c5d","mtname":"特色馆","mtlevel":2,"parentpk":"7a5ad72b8b29411cbf79a10f2a6204cb","type":"0","logourl":"mertypelogod46c33298cf1490482b42bd7f0953e20.jpg","addressurl":"1","merchantCount":108},{"pkmertype":"65834979d19c4a0d8029f18b7b2a2a7f","mtname":"特色","mtlevel":2,"parentpk":"7a5ad72b8b29411cbf79a10f2a6204cb","type":"0","logourl":"mertypelogobc1911bfc08c4012be76e4cb79713211.jpg","merchantCount":1}]
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
         * pkmertype : 317d7cf98c44467688dfc99f8e9284d7
         * mtname : 烤肉
         * mtlevel : 2
         * parentpk : 7a5ad72b8b29411cbf79a10f2a6204cb
         * type : 0
         * merchantCount : 2
         * logourl : mertypelogod46c33298cf1490482b42bd7f0953e20.jpg
         * addressurl : 1
         */

        private String pkmertype;
        private String mtname;
        private int mtlevel;
        private String parentpk;
        private String type;
        private int merchantCount;
        private String logourl;
        private String addressurl;

        public String getPkmertype() {
            return pkmertype;
        }

        public void setPkmertype(String pkmertype) {
            this.pkmertype = pkmertype;
        }

        public String getMtname() {
            return mtname;
        }

        public void setMtname(String mtname) {
            this.mtname = mtname;
        }

        public int getMtlevel() {
            return mtlevel;
        }

        public void setMtlevel(int mtlevel) {
            this.mtlevel = mtlevel;
        }

        public String getParentpk() {
            return parentpk;
        }

        public void setParentpk(String parentpk) {
            this.parentpk = parentpk;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getMerchantCount() {
            return merchantCount;
        }

        public void setMerchantCount(int merchantCount) {
            this.merchantCount = merchantCount;
        }

        public String getLogourl() {
            return logourl;
        }

        public void setLogourl(String logourl) {
            this.logourl = logourl;
        }

        public String getAddressurl() {
            return addressurl;
        }

        public void setAddressurl(String addressurl) {
            this.addressurl = addressurl;
        }
    }
}
