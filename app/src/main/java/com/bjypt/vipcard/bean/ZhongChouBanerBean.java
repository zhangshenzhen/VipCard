package com.bjypt.vipcard.bean;

import java.util.List;

public class ZhongChouBanerBean {


    /**
     * app_id : 100
     * isuse : 1
     * app_icon : http://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/commonb35477cbcc564f32b310cbdeed683acf.jpg
     * country : null
     * last_updatetime : 1530263707000
     * createtime : 1529929576000
     * app_type : 13
     * city : 阜阳
     * isentry : 0
     * mtlevel : 1
     * ios_native_url : null
     * city_code : 1558
     * remark : null
     * link_type : 1
     * app_name : 分享
     * province : 安徽
     * parent_app_id : 0
     * native_params : null
     * sorting : 0
     * link_url : https://mp.weixin.qq.com/s/J7ERCzmkzl-9If9K5MUSfg?
     * android_native_url : null
     * app_code : null;
     */

 /*   public List<BanerData> banerList;

    public ZhongChouBanerBean(List<BanerData> banerList) {
        this.banerList = banerList;
    }

    public List<BanerData> getBanerList() {
        return banerList;
    }

    public class BanerData{*/
        private int app_id;
        private int isuse;
        private String app_icon;
        private Object country;
        private long last_updatetime;
        private long createtime;
        private int app_type;
        private String city;
        private int isentry;
        private int mtlevel;
        private Object ios_native_url;
        private String city_code;
        private Object remark;
        private int link_type;
        private String app_name;
        private String province;
        private int parent_app_id;
        private Object native_params;
        private int sorting;
        private String link_url;
        private Object android_native_url;
        private Object app_code;

        public ZhongChouBanerBean() {
        }

        public ZhongChouBanerBean(int app_id, String app_icon, int app_type, String city) {
            this.app_id = app_id;
            this.app_icon = app_icon;
            this.app_type = app_type;
            this.city = city;
        }

        public int getApp_id() {
            return app_id;
        }

        public void setApp_id(int app_id) {
            this.app_id = app_id;
        }

        public int getIsuse() {
            return isuse;
        }

        public void setIsuse(int isuse) {
            this.isuse = isuse;
        }

        public String getApp_icon() {
            return app_icon;
        }

        public void setApp_icon(String app_icon) {
            this.app_icon = app_icon;
        }

        public Object getCountry() {
            return country;
        }

        public void setCountry(Object country) {
            this.country = country;
        }

        public long getLast_updatetime() {
            return last_updatetime;
        }

        public void setLast_updatetime(long last_updatetime) {
            this.last_updatetime = last_updatetime;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public int getApp_type() {
            return app_type;
        }

        public void setApp_type(int app_type) {
            this.app_type = app_type;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getIsentry() {
            return isentry;
        }

        public void setIsentry(int isentry) {
            this.isentry = isentry;
        }

        public int getMtlevel() {
            return mtlevel;
        }

        public void setMtlevel(int mtlevel) {
            this.mtlevel = mtlevel;
        }

        public Object getIos_native_url() {
            return ios_native_url;
        }

        public void setIos_native_url(Object ios_native_url) {
            this.ios_native_url = ios_native_url;
        }

        public String getCity_code() {
            return city_code;
        }

        public void setCity_code(String city_code) {
            this.city_code = city_code;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public int getLink_type() {
            return link_type;
        }

        public void setLink_type(int link_type) {
            this.link_type = link_type;
        }

        public String getApp_name() {
            return app_name;
        }

        public void setApp_name(String app_name) {
            this.app_name = app_name;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public int getParent_app_id() {
            return parent_app_id;
        }

        public void setParent_app_id(int parent_app_id) {
            this.parent_app_id = parent_app_id;
        }

        public Object getNative_params() {
            return native_params;
        }

        public void setNative_params(Object native_params) {
            this.native_params = native_params;
        }

        public int getSorting() {
            return sorting;
        }

        public void setSorting(int sorting) {
            this.sorting = sorting;
        }

        public String getLink_url() {
            return link_url;
        }

        public void setLink_url(String link_url) {
            this.link_url = link_url;
        }

        public Object getAndroid_native_url() {
            return android_native_url;
        }

        public void setAndroid_native_url(Object android_native_url) {
            this.android_native_url = android_native_url;
        }

        public Object getApp_code() {
            return app_code;
        }

        public void setApp_code(Object app_code) {
            this.app_code = app_code;
        }
 /*   }*/

    @Override
    public String toString() {
        return "ZhongChouBanerBean{" +
                "app_id=" + app_id +
                ", isuse=" + isuse +
                ", app_icon='" + app_icon + '\'' +
                ", country=" + country +
                ", last_updatetime=" + last_updatetime +
                ", createtime=" + createtime +
                ", app_type=" + app_type +
                ", city='" + city + '\'' +
                ", isentry=" + isentry +
                ", mtlevel=" + mtlevel +
                ", ios_native_url=" + ios_native_url +
                ", city_code='" + city_code + '\'' +
                ", remark=" + remark +
                ", link_type=" + link_type +
                ", app_name='" + app_name + '\'' +
                ", province='" + province + '\'' +
                ", parent_app_id=" + parent_app_id +
                ", native_params=" + native_params +
                ", sorting=" + sorting +
                ", link_url='" + link_url + '\'' +
                ", android_native_url=" + android_native_url +
                ", app_code=" + app_code +
                '}';
    }
}
