package com.bjypt.vipcard.model.cf;

public class CfUserInfoData {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : {"pkregister":"7a30cbe6695a4e239fe727fc1241889a","birthday":"","bank_valid_date":"2018-02-09 15:37:17.0","is_card_sales":1,"paypassword":"c354d051ab57f2a7c6ddbc734ac1d98c","bankcardno":"6217001370021015177","bankcity":"南京","bankuserphone":"18362915512","uid":2406,"flag_getmemberpoint":1,"wx_openid":"o58nLwQMMSOnsBFK146cc--TF6S8","signs":"很好","flag_inviteramount":0,"nickname":"雷哥:imp:","bankprovince":"江苏","email":null,"bankCode":"105301000013","dividend_memberpoint_growvalue":0.704,"cpuid":"869818028563877","locktime":null,"agrstatus":1,"loginpassword":"c354d051ab57f2a7c6ddbc734ac1d98c","sex":null,"bankusername":"王雷","qq_openid":null,"idcardno":"341225198905201215","bank_valid_count":99,"registerdate":"1486698082210","phoneno":"18362915515","bgpic":"userposition_2117789258b047a68ac8ad25531f8268.jpg","dividend_memberpoint_value":0,"dividend_memberpoint_value_backup":null,"nick_name":"","appid":"hyb","position":"http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTImxHSUP7Zhib0v2KlrzliaiamoWy5ewgkKpYh5hI8Q5H0yH9beeqgUdicEjBsTqHZRvqMJ85rc8cVljA/0","bankname":"中国建设银行-建设银行","card_sale_rate":1,"economize_amount":0,"lname":"中国建设银行江苏省分行"}
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
         * pkregister : 7a30cbe6695a4e239fe727fc1241889a
         * birthday :
         * bank_valid_date : 2018-02-09 15:37:17.0
         * is_card_sales : 1
         * paypassword : c354d051ab57f2a7c6ddbc734ac1d98c
         * bankcardno : 6217001370021015177
         * bankcity : 南京
         * bankuserphone : 18362915512
         * uid : 2406
         * flag_getmemberpoint : 1
         * wx_openid : o58nLwQMMSOnsBFK146cc--TF6S8
         * signs : 很好
         * flag_inviteramount : 0
         * nickname : 雷哥:imp:
         * bankprovince : 江苏
         * email : null
         * bankCode : 105301000013
         * dividend_memberpoint_growvalue : 0.704
         * cpuid : 869818028563877
         * locktime : null
         * agrstatus : 1
         * loginpassword : c354d051ab57f2a7c6ddbc734ac1d98c
         * sex : null
         * bankusername : 王雷
         * qq_openid : null
         * idcardno : 341225198905201215
         * bank_valid_count : 99
         * registerdate : 1486698082210
         * phoneno : 18362915515
         * bgpic : userposition_2117789258b047a68ac8ad25531f8268.jpg
         * dividend_memberpoint_value : 0.0
         * dividend_memberpoint_value_backup : null
         * nick_name :
         * appid : hyb
         * position : http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTImxHSUP7Zhib0v2KlrzliaiamoWy5ewgkKpYh5hI8Q5H0yH9beeqgUdicEjBsTqHZRvqMJ85rc8cVljA/0
         * bankname : 中国建设银行-建设银行
         * card_sale_rate : 1.0
         * economize_amount : 0.0
         * lname : 中国建设银行江苏省分行
         */

        private String pkregister;
        private String birthday;
        private String bank_valid_date;
        private String bankcardno;
        private String bankcity;
        private String bankuserphone;
        private Integer uid;
        private String wx_openid;
        private String nickname;
        private String bankprovince;
        private String bankCode;
        private String cpuid;
        private String bankusername;
        private Object qq_openid;
        private String idcardno;
        private Integer bank_valid_count;
        private String registerdate;
        private String phoneno;
        private String appid;
        private String position;
        private String bankname;
        private String lname;

        public String getPkregister() {
            return pkregister;
        }

        public void setPkregister(String pkregister) {
            this.pkregister = pkregister;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getBank_valid_date() {
            return bank_valid_date;
        }

        public void setBank_valid_date(String bank_valid_date) {
            this.bank_valid_date = bank_valid_date;
        }



        public String getBankcardno() {
            return bankcardno;
        }

        public void setBankcardno(String bankcardno) {
            this.bankcardno = bankcardno;
        }

        public String getBankcity() {
            return bankcity;
        }

        public void setBankcity(String bankcity) {
            this.bankcity = bankcity;
        }

        public String getBankuserphone() {
            return bankuserphone;
        }

        public void setBankuserphone(String bankuserphone) {
            this.bankuserphone = bankuserphone;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }


        public String getWx_openid() {
            return wx_openid;
        }

        public void setWx_openid(String wx_openid) {
            this.wx_openid = wx_openid;
        }



        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getBankprovince() {
            return bankprovince;
        }

        public void setBankprovince(String bankprovince) {
            this.bankprovince = bankprovince;
        }



        public String getBankCode() {
            return bankCode;
        }

        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }


        public String getCpuid() {
            return cpuid;
        }

        public void setCpuid(String cpuid) {
            this.cpuid = cpuid;
        }


        public String getBankusername() {
            return bankusername;
        }

        public void setBankusername(String bankusername) {
            this.bankusername = bankusername;
        }

        public Object getQq_openid() {
            return qq_openid;
        }

        public void setQq_openid(Object qq_openid) {
            this.qq_openid = qq_openid;
        }

        public String getIdcardno() {
            return idcardno;
        }

        public void setIdcardno(String idcardno) {
            this.idcardno = idcardno;
        }

        public int getBank_valid_count() {
            return bank_valid_count;
        }

        public void setBank_valid_count(int bank_valid_count) {
            this.bank_valid_count = bank_valid_count;
        }

        public String getRegisterdate() {
            return registerdate;
        }

        public void setRegisterdate(String registerdate) {
            this.registerdate = registerdate;
        }

        public String getPhoneno() {
            return phoneno;
        }

        public void setPhoneno(String phoneno) {
            this.phoneno = phoneno;
        }


        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getBankname() {
            return bankname;
        }

        public void setBankname(String bankname) {
            this.bankname = bankname;
        }

        public String getLname() {
            return lname;
        }

        public void setLname(String lname) {
            this.lname = lname;
        }
    }
}
