package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 崔龙 on 2017/4/19.
 */

public class NewMerchantDetailBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"pkmuser":"a0245c8f8a6043fdaad230698b74fbbf","muname":"大伙铺子","logo":"4d7110b38cd641bc931dda2a269a3bff.jpg","bigPic":"merchantAd6007409e3ad84c40882519ed4cb2369f.jpg","discount":"1.00","address":"江苏南京花神大道23号","latitude":"31.979043","longitude":"118.784259","switch_recharge":"1","switch_pay":"1","phone":"15156679232","switch_product":"1","merchant_activitys":[{"pkactivity":"de1f76c634554985b67df4de2c05ed98","remark":"无门槛使用","payamount":"5","valueamount":"10","pkmerchantcoupon":"23"},{"pkactivity":"de1f76c634554985b67df4de2c05ed98","remark":"无门槛使用","payamount":"9","valueamount":"10","pkmerchantcoupon":"23"},{"pkactivity":"de1f76c634554985b67df4de2c05ed98","remark":"无门槛使用","payamount":"120","valueamount":"10","pkmerchantcoupon":"23"}],"album_total":"1","album_records":[{"imageurl":"hybMerchant_444a208706174804b22af6842462afa3.jpg","typename":"菜品"}],"merdesc":"最美的","purchaserules":"如需发票，请您在消费时向商户咨询。此消费只能使用一次，过期不能使用。消费时如有其他金额变动，请与商家进行沟通补退差价。如未预约到店进行消费，如遇高峰期您可能需要排队。","moreservices":"WiFi","opening_times":["08:00:00-10:00:00","12:00:00-14:00:00","20:00:00-22:00:00"],"tcs":[{"pkproduct":"16740945302650891","productName":"干锅包菜","productPrice":"22.00","orginprice":"23.00","productImgUrl":""},{"pkproduct":"17016611101212672","productName":"套餐测试","productPrice":"12.00","orginprice":"15.00","productImgUrl":""}],"comment_total":4,"comments":[{"pkregister":"5a77ae772cb34106b334338673956c44","score":5,"content":"行不行不喜不悲行吧行吧行吧行吧","replycontent":"谢谢好评，，，，，","commenttime":"2016-05-25 14:34","replytime":"2016-05-26 19:00","nickname":"大山山水水","phoneno":"15156679232","position":"userposition_57dfa4a51a5a4807acdf5b216e10a106.jpg"},{"pkregister":"5a77ae772cb34106b334338673956c44","score":5,"content":"友情提示，那你呢扭扭捏捏扭扭捏捏你","commenttime":"2016-05-25 14:33","nickname":"大山山水水","phoneno":"15156679232","position":"userposition_57dfa4a51a5a4807acdf5b216e10a106.jpg"},{"pkregister":"5a77ae772cb34106b334338673956c44","score":5,"content":"非常棒，环境非常好，你想不到的才是最好的","commenttime":"2016-05-21 18:57","nickname":"大山山水水","phoneno":"15156679232","position":"userposition_57dfa4a51a5a4807acdf5b216e10a106.jpg"}]}
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
         * pkmuser : a0245c8f8a6043fdaad230698b74fbbf
         * muname : 大伙铺子
         * logo : 4d7110b38cd641bc931dda2a269a3bff.jpg
         * bigPic : merchantAd6007409e3ad84c40882519ed4cb2369f.jpg
         * discount : 1.00
         * address : 江苏南京花神大道23号
         * latitude : 31.979043
         * longitude : 118.784259
         * switch_recharge : 1
         * switch_pay : 1
         * phone : 15156679232
         * switch_product : 1
         * merchant_activitys : [{"pkactivity":"de1f76c634554985b67df4de2c05ed98","remark":"无门槛使用","payamount":"5","valueamount":"10","pkmerchantcoupon":"23"},{"pkactivity":"de1f76c634554985b67df4de2c05ed98","remark":"无门槛使用","payamount":"9","valueamount":"10","pkmerchantcoupon":"23"},{"pkactivity":"de1f76c634554985b67df4de2c05ed98","remark":"无门槛使用","payamount":"120","valueamount":"10","pkmerchantcoupon":"23"}]
         * album_total : 1
         * album_records : [{"imageurl":"hybMerchant_444a208706174804b22af6842462afa3.jpg","typename":"菜品"}]
         * merdesc : 最美的
         * purchaserules : 如需发票，请您在消费时向商户咨询。此消费只能使用一次，过期不能使用。消费时如有其他金额变动，请与商家进行沟通补退差价。如未预约到店进行消费，如遇高峰期您可能需要排队。
         * moreservices : WiFi
         * opening_times : ["08:00:00-10:00:00","12:00:00-14:00:00","20:00:00-22:00:00"]
         * tcs : [{"pkproduct":"16740945302650891","productName":"干锅包菜","productPrice":"22.00","orginprice":"23.00","productImgUrl":""},{"pkproduct":"17016611101212672","productName":"套餐测试","productPrice":"12.00","orginprice":"15.00","productImgUrl":""}]
         * comment_total : 4
         * comments : [{"pkregister":"5a77ae772cb34106b334338673956c44","score":5,"content":"行不行不喜不悲行吧行吧行吧行吧","replycontent":"谢谢好评，，，，，","commenttime":"2016-05-25 14:34","replytime":"2016-05-26 19:00","nickname":"大山山水水","phoneno":"15156679232","position":"userposition_57dfa4a51a5a4807acdf5b216e10a106.jpg"},{"pkregister":"5a77ae772cb34106b334338673956c44","score":5,"content":"友情提示，那你呢扭扭捏捏扭扭捏捏你","commenttime":"2016-05-25 14:33","nickname":"大山山水水","phoneno":"15156679232","position":"userposition_57dfa4a51a5a4807acdf5b216e10a106.jpg"},{"pkregister":"5a77ae772cb34106b334338673956c44","score":5,"content":"非常棒，环境非常好，你想不到的才是最好的","commenttime":"2016-05-21 18:57","nickname":"大山山水水","phoneno":"15156679232","position":"userposition_57dfa4a51a5a4807acdf5b216e10a106.jpg"}]
         */

        private String pkmuser;
        private String muname;
        private String logo;
        private String bigPic;
        private String discount;
        private String address;
        private String latitude;
        private String longitude;
        private String switch_recharge;
        private String switch_pay;
        private String phone;
        private String switch_product;
        private String album_total;
        private String merdesc;
        private String purchaserules;
        private String moreservices;
        private String startLevel;
        private String distance;

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getStartLevel() {
            return startLevel;
        }

        public void setStartLevel(String startLevel) {
            this.startLevel = startLevel;
        }

        private int comment_total;
        private List<MerchantActivitysBean> merchant_activitys;
        private List<AlbumRecordsBean> album_records;
        private List<String> opening_times;
        private List<TcsBean> tcs;
        private List<CommentsBean> comments;

        public String getPkmuser() {
            return pkmuser;
        }

        public void setPkmuser(String pkmuser) {
            this.pkmuser = pkmuser;
        }

        public String getMuname() {
            return muname;
        }

        public void setMuname(String muname) {
            this.muname = muname;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getBigPic() {
            return bigPic;
        }

        public void setBigPic(String bigPic) {
            this.bigPic = bigPic;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getSwitch_recharge() {
            return switch_recharge;
        }

        public void setSwitch_recharge(String switch_recharge) {
            this.switch_recharge = switch_recharge;
        }

        public String getSwitch_pay() {
            return switch_pay;
        }

        public void setSwitch_pay(String switch_pay) {
            this.switch_pay = switch_pay;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSwitch_product() {
            return switch_product;
        }

        public void setSwitch_product(String switch_product) {
            this.switch_product = switch_product;
        }

        public String getAlbum_total() {
            return album_total;
        }

        public void setAlbum_total(String album_total) {
            this.album_total = album_total;
        }

        public String getMerdesc() {
            return merdesc;
        }

        public void setMerdesc(String merdesc) {
            this.merdesc = merdesc;
        }

        public String getPurchaserules() {
            return purchaserules;
        }

        public void setPurchaserules(String purchaserules) {
            this.purchaserules = purchaserules;
        }

        public String getMoreservices() {
            return moreservices;
        }

        public void setMoreservices(String moreservices) {
            this.moreservices = moreservices;
        }

        public int getComment_total() {
            return comment_total;
        }

        public void setComment_total(int comment_total) {
            this.comment_total = comment_total;
        }

        public List<MerchantActivitysBean> getMerchant_activitys() {
            return merchant_activitys;
        }

        public void setMerchant_activitys(List<MerchantActivitysBean> merchant_activitys) {
            this.merchant_activitys = merchant_activitys;
        }

        public List<AlbumRecordsBean> getAlbum_records() {
            return album_records;
        }

        public void setAlbum_records(List<AlbumRecordsBean> album_records) {
            this.album_records = album_records;
        }

        public List<String> getOpening_times() {
            return opening_times;
        }

        public void setOpening_times(List<String> opening_times) {
            this.opening_times = opening_times;
        }

        public List<TcsBean> getTcs() {
            return tcs;
        }

        public void setTcs(List<TcsBean> tcs) {
            this.tcs = tcs;
        }

        public List<CommentsBean> getComments() {
            return comments;
        }

        public void setComments(List<CommentsBean> comments) {
            this.comments = comments;
        }

        public static class MerchantActivitysBean {
            /**
             * pkactivity : de1f76c634554985b67df4de2c05ed98
             * remark : 无门槛使用
             * payamount : 5
             * valueamount : 10
             * pkmerchantcoupon : 23
             */

            private String pkactivity;
            private String remark;
            private String payamount;
            private String valueamount;
            private String pkmerchantcoupon;

            public String getPkactivity() {
                return pkactivity;
            }

            public void setPkactivity(String pkactivity) {
                this.pkactivity = pkactivity;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getPayamount() {
                return payamount;
            }

            public void setPayamount(String payamount) {
                this.payamount = payamount;
            }

            public String getValueamount() {
                return valueamount;
            }

            public void setValueamount(String valueamount) {
                this.valueamount = valueamount;
            }

            public String getPkmerchantcoupon() {
                return pkmerchantcoupon;
            }

            public void setPkmerchantcoupon(String pkmerchantcoupon) {
                this.pkmerchantcoupon = pkmerchantcoupon;
            }
        }

        public static class AlbumRecordsBean {
            /**
             * imageurl : hybMerchant_444a208706174804b22af6842462afa3.jpg
             * typename : 菜品
             */

            private String imageurl;
            private String typename;

            public String getImageurl() {
                return imageurl;
            }

            public void setImageurl(String imageurl) {
                this.imageurl = imageurl;
            }

            public String getTypename() {
                return typename;
            }

            public void setTypename(String typename) {
                this.typename = typename;
            }
        }

        public static class TcsBean {
            /**
             * pkproduct : 16740945302650891
             * productName : 干锅包菜
             * productPrice : 22.00
             * orginprice : 23.00
             * productImgUrl :
             */

            private String pkproduct;
            private String productName;
            private String productPrice;
            private String orginprice;
            private String productImgUrl;

            public String getPkproduct() {
                return pkproduct;
            }

            public void setPkproduct(String pkproduct) {
                this.pkproduct = pkproduct;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getProductPrice() {
                return productPrice;
            }

            public void setProductPrice(String productPrice) {
                this.productPrice = productPrice;
            }

            public String getOrginprice() {
                return orginprice;
            }

            public void setOrginprice(String orginprice) {
                this.orginprice = orginprice;
            }

            public String getProductImgUrl() {
                return productImgUrl;
            }

            public void setProductImgUrl(String productImgUrl) {
                this.productImgUrl = productImgUrl;
            }
        }

        public static class CommentsBean {
            /**
             * pkregister : 5a77ae772cb34106b334338673956c44
             * score : 5
             * content : 行不行不喜不悲行吧行吧行吧行吧
             * replycontent : 谢谢好评，，，，，
             * commenttime : 2016-05-25 14:34
             * replytime : 2016-05-26 19:00
             * nickname : 大山山水水
             * phoneno : 15156679232
             * position : userposition_57dfa4a51a5a4807acdf5b216e10a106.jpg
             */

            private String pkregister;
            private String score;
            private String content;
            private String replycontent;
            private String commenttime;
            private String replytime;
            private String nickname;
            private String phoneno;
            private String position;

            public String getPkregister() {
                return pkregister;
            }

            public void setPkregister(String pkregister) {
                this.pkregister = pkregister;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getReplycontent() {
                return replycontent;
            }

            public void setReplycontent(String replycontent) {
                this.replycontent = replycontent;
            }

            public String getCommenttime() {
                return commenttime;
            }

            public void setCommenttime(String commenttime) {
                this.commenttime = commenttime;
            }

            public String getReplytime() {
                return replytime;
            }

            public void setReplytime(String replytime) {
                this.replytime = replytime;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getPhoneno() {
                return phoneno;
            }

            public void setPhoneno(String phoneno) {
                this.phoneno = phoneno;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }
        }
    }
}
