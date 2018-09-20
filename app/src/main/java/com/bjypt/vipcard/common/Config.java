package com.bjypt.vipcard.common;

import android.os.Environment;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/19
 * Use by 服务器地址，接口地址
 */
public final class Config {

    public static String DEFAULT_CITY = "阜阳";
    public static String DEFAULT_CITY_CODE = "1558";
    public static double DEFAULT_LONGITUDE = 115.809206;
    public static double DEFAULT_LATITUDE = 32.883249;

    public static final class web {

        public static final String cityCode = "1558";


        //public static final String hus_url = "http://123.57.232.188:9980/hus/";//检测服务器升级服务
        public static final String hus_url = "http://121.196.233.207:9090/hus";   //检测服务器升级服务(正式


        //测试服务器地址
        public static final String hyb_url = "http://123.57.232.188:8080/hyb/";  //测试服务器地址
        public static final String shangfengh5 = "http://123.57.232.188:19094/";
        public static final String app_h5_url = "http://testsun.hybjiekou.com/hyb_ct_h5app/";
        public static final String cf_url = "http://123.57.232.188:19096/";
//        public static final String cf_h5_url = "";
//        public stati
       // http://123.57.232.188:19096/api/hybCfMerchantCrowdfundingProjectItem/getProjectItemExplain";
        //正式服务器地址
//        public static final String shangfengh5= "http://jk.zhihuisf.com:9094/";
//        public static final String hyb_url = "https://hybjiekou.com:8443/hyb/";    //207地址
//        public static final String app_h5_url = "http://sun.hybjiekou.com/hyb_ct_h5app/";
//        public static final String cf_url = "http://123.57.232.188:19096/api/";


        //预上线服务器地址
//        public static final String hyb_url = "http://47.93.79.174:9002/hyb/";
//        public static final String shangfengh5= "http://jk.zhihuisf.com:9094/";


        public static final String base = hyb_url + "ws/";

        public static final String DZ_PYQ = "http://ff.hybjiekou.com/hyb-discuz/";

//        public static final String base= "http://192.168.1.121:8080/hyb/ws/";//杨峰本地服务器地址

        public static final String picUrl = "http://img-cn-hangzhou.aliyuncs.com/huiyuanbao/";

        //                public static final String URL_pay = "http://120.55.199.24:8080/hyb/";//正式
        public static final String URL_pay = hyb_url;//测试

//        public static final String URL_pay = "http://47.93.79.174:9002/hyb/";//预上线
//        public static final String URL_pay = "http://192.168.1.115:9999/hyb/";//凯旋
//        public static final String URL_pay = "http://192.168.1.112:7778/hyb/";//杨建

        //                public static  final  String type_base = "http://192.168.1.129:8080/"; //wanglei
//        public static final String type_base = "http://123.57.232.188:8080/";//分类调用H5端口 测试
//        public static final String type_base = "http://47.93.79.174:9002/";//分类调用H5端口 预上线
        public static final String type_base = "https://hybjiekou.com:8443/";   //59地址


        /*生活服务调用以前接口，故使用以前端口号*/
//        public static final String life_base = "http://123.57.232.188:8080/hyb/S01/";//测试服务器地址
//        public static final String life_base = "http://47.93.79.174:9001/hyb/S01/";//预上线服务器地址
        public static final String life_base = "   https://hyb.minszx.com:8443/hyb/S01/";//正式服务器地址
//        public static final String life_base = "https://hybjiekou.com:8443/hyb/S01/";   //59地址

        public static final String ImgURL = "http://img-cn-hangzhou.aliyuncs.com/huiyuanbao/";

        //Discuz 测试域名
        public static final String DZURL = "http://47.93.79.174/base_discuz/";
        public static final String fy_picUrl = DZURL + "data/attachment/portal/";                   //首页新闻图片前缀

        /***********************************************************************************************************/
        // 天气
        public static final String WEATHER = base + "/remote/get/weather";
        // 我的钱包
        public static final String MY_WALLET = base + "/get/application/ad";
        // 头条
        public static final String HEADLINE_NEWS = base + "remote/get/message";
        // 请求理财列表
        public static final String MANAGE_MONEY_MATTERS = base + "post/balanceInvestList";
        //校验服务端是否升级
        public static final String check_upgrade = hus_url + "api/appsetting/upgrade";


        public static final String system_proposelist = hus_url + "post/getSystemProposeList?pkregister="; //意见反馈

        public static final String home_service = base + "post/getHomeInfo";//首页数据获取
        public static final String calculator_finances = hyb_url + "resource/finances/index.html?";//理财计算器
        public static final String home_merchant = base + "post/v4.3.5/merchantList";//首页商户列表
        public static final String merchant_details = base + "post/getMerchantDetail";//商家详情
        public static final String product_spec = base + "post/getProductSpec";//商家详情
        public static final String subscribe_order = base + "post/createPreOrder";//预约订单
        public static final String is_vip = base + "post/checkMerchantMember";//判断当前当前用户是否是商家会员
        public static final String save_money = base + "rest/getShoppingSaveMoney";//判断省多少钱
        public static final String update_payresult = base + "rest/pay";//更新订单状态
        public static final String update_payresult_new = base + "rest/v4.4.2/pay";//更新订单状态
        public static final String product_details = base + "post/getProductTCDetails";//商品详情界面
        public static final String getMerchant_details_pic = base + "post/getMerchantUploadImgs";//获取商家详情图片
        public static final String get_verification_code = base + "post/registSmsCode";//获取短信验证码
        public static final String get_bind_sms = hyb_url + "ws/post/bind/sms";//获取短信验证码
        public static final String register_ = base + "post/registUserInfo";//注册
        public static final String login_ = base + "post/login";//登录
        public static final String get_forget_code = base + "post/backPaswSmsCode";//忘记密码获取验证码
        public static final String get_password = base + "post/backPassword";//找回登录密码
        public static final String is_bind_card = base + "post/getBankInfo";//是否绑定了银行卡
        public static final String un_bind_card = base + "get/unbindbutton";//解绑按钮显示与否
        public static final String bind_info = base + "delete/bankinfo";//解绑银行卡

        public static final String get_Card_name = base + "post/getBankNameList";//获取银行卡名称
        public static final String change_name = base + "post/modifiedNickname";//修改昵称
        public static final String get_platBalance = base + "post/platBalance";//平台余额及收益商户界面
        public static final String get_merchantBalance = base + "post/merchantBalance";//平台余额及收益商户界面
        public static final String find_all_order = base + "post/getPreOrder";//查看所有订单
        public static final String del_order = base + "post/delOrder";//删除订单
        public static final String coupon = base + "post/getWealCoupon";//优惠券及红包
        public static final String merchantcatetory = base + "post/merchantCatetory";//商家分类列表 例如 一级二级列表
        public static final String merchant_list = base + "post/v4.3.5/merchantList";//商家列表
        public static final String detail_order = base + "post/getMyOrderDetail";//订单详情z
        public static final String get_billdetailbypkid = base + "post/getBillsDetailByPkId";//资金详情
        public static final String prosuct_suggest = base + "post/saveSystemPropose";//产品建议
        public static final String checkin = base + "post/saveMerchantSettled";//我想入住
        public static final String order_details = base + "post/getProductOfPreorder";//某订单下的订餐内容
        public static final String add_comment = base + "post/insertUserComment";//添加评论
        public static final String updata_logpassword = base + "post/editPassword";//修改登陆密码
        public static final String add_paypassword = base + "post/updatePayPasswordByPhoneno";//修改支付密码(未设置时)
        public static final String updata_paypassword = base + "post/editPayPasswordByPhoneno";//修改支付密码(设置后)
        public static final String default_adress = base + "post/queryReceiptAddress";//获得地址列表
        public static final String delete_default_adress = base + "post/deleteReceiptAddress";//删除默认地址
        public static final String update_default_adress = base + "post/updateReceiptAddress";//更新默认地址
        public static final String get_comment_list = base + "post/getCommentList";//获取评论列表
        public static final String comment_details = base + "post/getMerchantAvgScore";//评论详情
        public static final String pay_info_ = base + "post/getPayInfo";//获取支付前页面信息
        public static final String join_merchantmember = base + "post/joinMerchantMember";//用户加入商户会员
        public static final String get_paypsdcode = base + "post/backPayPaswSmsCode";//获取支付面膜验证码
        public static final String reset_paypsd = base + "post/backPayPassword";//重置支付密码
        public static final String uploading_image = base + "post/uploadImage";//上传头像到头像服务器
        public static final String bills_detail = base + "post/getBillsDetail";//账单明细
        public static final String change_phone = base + "post/changePhoneSmsCode";//新旧手机发送验证码
        public static final String check_old_verification_code = base + "post/getChangePhoneSmsCode";//验证旧手机号码
        public static final String update_phone = base + "updateNewPhone";//验证新手机号码
        public static final String update_image = base + "post/updateUserPosition";//更新头像信息
        public static final String welcome_ad = base + "post/getwelcome";//欢迎页面广告
        public static final String query_withdraw = base + "post/withdrawSearch";//提现查询
        public static final String bind_bank_card = base + "post/addBankInfo";//绑定银行卡
        public static final String request_bank_key = base + "remote/get/bankaps";//银联号查询
        public static final String update_bank_card = base + "post/updateBankInfo";//更新绑定银行卡信息
        public static final String withdraw_balance = base + "rest/withdrawApply";//提现余额
        public static final String get_banner = base + "post/getbannerByCity";//首页轮播图
        public static final String create_new_order = base + "rest/v4.4.2/orderAdd";//创建新的充值订单
        public static final String system_infomatiob = base + "post/getRegisterSysInfo";// 用户系统资产信息
        public static final String single_product_details = base + "post/getSingleProductDetails";//单品详情
        public static final String get_news_infomation = base + "post/getMyPushMessage";//获取消息
        public static final String get_first_merchant = base + "post/getFirstMerchant";//获取第一商家信息
        public static final String add_shopping_adress = base + "post/saveReceiptAddress";//添加收货地址
        public static final String checkout_pay_psd = base + "post/checkPayPasswordByPhoneno";//校验支付密码
        public static final String download_apk = base + "post/checkVersion";//版本更新
        public static final String total_point_record = base + "post/detailsInfo";//积分记录的账户积分，已消费积分
        public static final String point_record_list = base + "post/integralOrEliminationRecord";//积分记录列表
        public static final String find_transaction_record = base + "post/getUserTrade";//交易记录
        public static final String find_recharge_record = base + "post/getRechargeRecord";//充值记录
        public static final String find_present_record = base + "post/getWithdrawRecord";//提现记录
        public static final String push_message_main = base + "post/getMyPushMessageMain";//消息主页
        public static final String refund_apply = base + "post/applyRefundSubscription";//申请退定金
        public static final String query_phone_home = base + "post/queryPhoneHome";//归属地查询
        public static final String on_line_order = base + "post/onlineorder";//话费充值
        public static final String vip_train = base + "post/myJoinMerchantList";//会员直通车
        public static final String preferential_merchant = base + "post/platformMerchant";//商家特惠区
        public static final String life_service_list = base + "post/queryLifeOrder";//生活服务消费记录
        public static final String get_system_merchant_balance = base + "post/getUserSysMerBalanceInfo";//获取商家余额和平台余额
        public static final String wx_pay = base + "rest/thirdpay/app/getThirdPayInfo";//微信支付
        public static final String zbar_merchant = base + "post/getPkmsuerByIp";//扫商家二维码进入商家详情页面
        public static final String lang_pay_balance = base + "rest/orderlang/pay";//订货郎余额支付
        public static final String share_registUrl = base + "post/getShareRegistUrl";//我的二维码生成
        public static final String my_fee_point = base + "page/dividend/toDividendMemberRecord?pkregister=";//我的分润
        public static final String my_bill_detail = base + "post/getUserBillsMainH5?pkregister=";//账单明细
        public static final String get_UserMerchantInfo = base + "post/getUserMerchantInfo";//显示充值送积分信息
        public static final String recharge_account_ = base + "post/getUserBillsMainH5?pkregister=";//充值记录


        public static final String deduction_store = base + "page/pointshop/index?pkregister=";//抵扣商城
        public static final String update_cancle_result = base + "post/thirdpay/app/userOrderCancelBack";
        public static final String merchant_detail_ = base + "post/getMerchantDetailsByPk?pkmuser=";//商家H5详情
        public static final String get_bind_bank_code = base + "post/bindCardNoSmsCode";         //绑定银行卡获取验证码
        public static final String get_pay_front = base + "post/v4.4.2/getBeforeTotalPayInfo";//获取立即买单支付前页面

        public static final String my_invite_ = base + "post/getUserInviteH5?pkregister=";//我的邀请

        public static final String all_life_type = base + "post/getMoreCommonServicesList";//点击更多全部分类
        public static final String small_type = base + "post/getSubCategoryListByPk";//点击首页分类跳转的小分类

        public static final String new_application = base + "post/getNewApplicationList";//首页大分类
        public static final String common_services_list = base + "post/getCommonServicesList";//首页大分类
        public static final String statistics_type = base + "post/statisticsHotApplication";//统计大分类点击
        public static final String yu_e_bao_record = base + "post/balanceInvestRecord";//余额宝购买记录
        public static final String yu_e_bao_buy = base + "post/balanceInvestBuy";//余额宝购买
        public static final String yu_e_bao_list = base + "post/balanceInvestList";//余额宝购买记录
        public static final String finance_amount_info = base + "merchant/finance/financeAmountWhereaboutsInfo";
        public static final String finance_amount_updateAmountWhereabout = base + "merchant/finance/updateAmountWhereabout";
        public static final String financing_balanceInvestInfo = base + "post/balanceInvestInfo";//理财投资金额统计
        public static final String youhui_buy_before = base + "post/confirmCouponOrder";//获取优惠劵购买前页面
        public static final String new_coupn = base + "post/getCouponsList";//首页新闻列表
        public static final String check_quan = base + "post/getUserCoupons";//检验优惠券
        public static final String get_quan = base + "post/getCanUseCoupons";//获取优惠券
        public static final String get_right_away_before = base + "post/v4.5.0/discountType";
        public static final String create_buy_youhui_order = base + "post/createCouponOrder";//优惠劵订单创建
        public static final String pay_new_before = base + "post/toPay";// 	支付前向后台服务器下单

        public static final String life_tuijian_merchant = base + "post/getRecommendMerchantList";//生活汇推荐商家
        public static final String home_life_hui_merchant_list = base + "post/v4.5.0/newMerchantList";//首页分类列表
        public static final String home_wuzhe_merchant_list = base + "post/newMerchantList";//五折分类列表
        public static final String new_home_xinwen = hyb_url + "hyb-lifeservice/news/newsList?page=";//首页新闻列表

        public static final String fine_discount_get_banner = base + "halfstore/get/banner";  //5.8【五折店】首页banner
        public static final String fine_discount_get_notice = base + "halfstore/get/notice";  //5.7	【五折店】头条
        public static final String fine_discount_post_consume = base + "halfstore/post/consume";  //5.7	【五折店】立即支付
        public static final String fine_discount_paytype = base + "halfstore/get/paytype"; //5.12	【五折店】五折店充值方式
        public static final String fine_discount_recharge = base + "halfstore/post/recharge"; //5.10	【五折店】五折店充值
        public static final String fine_discount_get_balance = base + "halfstore/get/halfbalance"; //5.13	【五折店】五折店账户余额
        public static final String fine_discount_get_merchant = base + "halfstore/get/merchant"; //5.14	【五折店】五折店商家列表
        public static final String fine_discount_get_couponsLis = base + "post/getNewMerchantCouponsList"; //5.14	【五折店】优惠券列表
        public static final String fine_discount_pay_history = base + "halfstore/wap/toRecordPage";
        public static final String fine_discount_amount_rule = base + "halfstore/wap/toRulesPage";


        public static final String halfstore_recharge_carousel = base + "halfstore/get/rechargeActivity";   //商家详情轮播

        public static final String chain_stores_list = base + "linkage/merchant_list";  //连锁店下的商家
        public static final String chain_stores_list_diplaybtn = base + "linkage/diplaybtn";  //连锁店下的商家
        public static final String new_merchant_details = base + "merchantdetail/v4.5.0/info";  //新商家详情

        public static final String new_home_merchant_listview = base + "post/v4.5.0/getMerchantList";  //新店铺街列表
        public static final String user_record_h5 = base + "post/getUserBalanceChangeH5?pkregister=";  //用户余额记录

        public static final String get_bank_detail = base + "post/getBankDetail";  //获取银行卡信息
        public static final String get_order_status = base + "remote/get/orderStatus";  //获取银行卡信息


        public static final String get_pay_away = base + "rest/get/payinfo";

        public static final String go_conversion = base + "get/virtualsetting";   //积分兑换
        public static final String virtualcount = base + "get/virtualcount";       //积分数量

        public static final String card_list = base + "card/bindlist";               //实体卡卡列表
        public static final String can_bind = base + "card/canbind";              //市民卡界面是否还可以绑卡

        public static final String card_balance = base + "card/cardbalance";        //卡管理页面卡的信息
        public static final String request_petroleum = base + "merchant/finance/banner";        //卡管理页面卡的信息
        public static final String card_balance_toPlatformBalance = base + "card/toPlatformBalance"; //转出到余额
        public static final String card_menus = base + "card/cardmenus";            //卡管理界面菜单

        /***
         * 众筹
         */
        public static final String getCFUserData = cf_url + "api/hybRegister/getRegisterById";
        public static final String getCFAccountList = cf_url + "api/hybCfUserAccount/findAllPage";
        public static final String getCFAccountInfo = cf_url + "api/hybCfUserAccount/getAccount";
        //我要提现
        public static final String h5_CFWithdraw = app_h5_url + "#/zccash?";
        //余额记录
        public static final String h5_CFBalanceRecord = app_h5_url + "#/balancerecord?";
        //提现记录
        public static final String h5_CFWithdrawRecord = app_h5_url + "#/zccashrecord?";
        //商家信息
        public static final String h5_CFMerchantInfo = app_h5_url + "#sellerinfor?";
        //项目收益
        public static final String h5_CFProjectInterest = app_h5_url + "#zcprofitlist?";
        //购买记录
        public static final String h5_CFBuyRecord = app_h5_url + "#buyrecord?";
        //消费记录
        public static final String h5_CFConsumeRecord = app_h5_url + "#spendrecord?";
        //绑定银行卡
        public static final String h5_CFConsumeBinder = app_h5_url + "#/zccardinfor?";
        //查看申请记录
        public static final String h5_CFConsumerequest_view = app_h5_url + "#/viewrecord?";
        //众筹申请
        public static final String h5_CFConsumerequest_crowd = app_h5_url + "#/apply";
        //立即认证
        public static final String h5_CFConsumereal_name = app_h5_url + "#/zcbindcard?";
        //我的收藏
         public static final String h5_CFConsumeCollection = cf_url +"api/hybCfProjectCollectionHtml/findAllByPkregister";
        /*
         * 众筹界面首页的baner 地址*/
        public static final String zhongchou_baner_url = cf_url + "api/hybApplicationConfig/getBanner";
       /*
       * 支持信息*/
       public static final String zhongchou_supportInfo_url = cf_url + "api/hybCfMerchantCrowdfundingProjectItem/getProjectItemExplain";

        /*
         * 首页今日推荐
         * */
        public static final String zhongchou_recomend_url = cf_url + "api/hybCfProject/getRecommendProject";
        /*
         * 众筹列表*/
        public static final String zhongchou_list_url = cf_url + "api/hybCfProject/getIsNoRecommendProject";
        /*生成二维码
         * */
        public static final String zhongchou_tuo_code_url = cf_url + "api/hybScanpayStatus/randomScanPay";

        /*生成二维码更新
         * */
        public static final String zhongchou_tuo_code_Update_url = cf_url + "api/hybScanpayStatus/updateScanPayStatus";
        /*
         * 商家众筹项目*/
        public static final String Seller_project_url = cf_url + "api/hybCfProject/getProjectByMerchantId";
        /**
         * 根据项目ID查询项目详情
         */
        public static final String get_crowdfunding_project_detail = cf_url + "api/hybCfProjectHtml/getProjectById";
        /**
         * 众筹帮详情 产品介绍
         */
        public static final String h5_cf_product_info = app_h5_url + "#/productinfor?";
        /**
         * 众筹帮详情 团队介绍
         */
        public static final String h5_cf_team_introduction = app_h5_url + "#/teamIntroduction?";
        /**
         * 众筹帮详情 常见问题
         */
        public static final String h5_cf_common_problem = app_h5_url + "#/commonProblem?";
        /**
         * 众筹帮详情 项目进展
         */
        public static final String h5_cf_project_progress = app_h5_url + "#projectProgress?";
        /**
         * 添加收藏
         */
        public static final String cf_favo_project = cf_url +"api/hybCfProjectCollection/insCollection";
        /**
         * 删除收藏
         */
        public static final String cf_unfavo_project = cf_url + "api/hybCfProjectCollection/delCollection";
        /**
         *项目子项信息
         */
        public static final String cf_project_amount_item = cf_url + "api/hybCfMerchantCrowdfundingProjectItem/findAllByProjectId";
        /**
         * 众筹购买支付--预下单
         */
        public static final String cf_buy_pre_order = cf_url +"api/order/v1/add";
        /**
         * 众筹购买支付下单
         */
        public  static final String cf_buy_topay = cf_url + "api/order/v1/toPay";

        /**
         * 获取订单信息
         */
        public static final String cf_order_info = cf_url + "api/order/v1/info";








        public static final String citizen_card_register = hyb_url + "app/h5/card/activation?pkregister=";   //实体卡激活
        public static final String citizen_card_dissolving = hyb_url + "app/h5/card/guashi";   //实体卡挂失
        public static final String citizen_card_undissolving = hyb_url + "app/h5/card/jiegua";   //实体卡解挂
        public static final String citizen_card_usedesc = hyb_url + "app/h5/deduct/virtual?"; //抵扣金说明

        public static final String citizen_card_expense_calendar = base + "post/getUserBillsMainH5";   //实体卡消费记录
        public static final String card_merchant_list = base + "card/cardmerchant";
        public static final String citizen_card_gifts = base + "post/getUserBillsMainH5";   //礼品金记录

        public static final String send_sms_citizen_card = base + "card/smsCode";   //一卡通发送验证码
        public static final String band_citizen_card = base + "card/bindCard";      //一卡通绑卡
        public static final String getSearchCardList = hyb_url + "ws/card/getSearchCardList";      //一卡通绑卡
        public static final String receivedCard = hyb_url + "ws/card/receivedCard";                 //一卡通领卡

        public static final String sign_in = hyb_url + "SignIn/index?pkregister=";                      //签到
        public static final String cardSalesH5 = hyb_url + "app/h5/getCardSalesH5?pkregister=";                      //签到
        public static final String sign_in_continue_days = hyb_url + "SignIn/record?pkregister=";      //连续签到天数
        public static final String sign_in_gonggao = base + "get/mySystemNotice";                        //公告

        public static final String random = hyb_url + "ws/card/scanpay/random"; // 条码生成
        public static final String status = hyb_url + "ws/card/scanpay/status"; // 条码状态

        public static final String cardUseIllustrate = hyb_url + "app/h5/category/explain?categoryid="; // 卡使用说明
        public static final String authorize = base + "post/third/authorize"; // 登陆绑手机
        public static final String send_phoneno = base + "post/third/phoneno"; // 发送验证码
        public static final String discountInfo = base + "get/discountInfo";
        public static final String recharge_select_money = hyb_url + "ws/get/grade/recharge";
        public static final String recharge_merchant_select_money = base + "merchant/finance/canBuyAmountList";

        public static final String fyzx_news_category = base + "remote/get/categorys";                       //首页新闻分类
        public static final String fyzx_news_more = base + "remote/get/news";                                 //首页新闻列表
        public static final String fyzx_news_item = base + "remote/get/message";                              //首页两条独立新闻

        public static final String getAttention = DZ_PYQ + "home/user/getFriendUserList";             // 关注列表
        public static final String getFansList = DZ_PYQ + "home/user/getFansUserList";                // 粉丝列表
        public static final String collectionList = DZ_PYQ + "thread/forum/get/favorite/list";       // 收藏列表
        public static final String my_member_count = DZ_PYQ + "home/user/getMemberCountInfo";         // 获取数量
        public static final String cancle_attention = DZ_PYQ + "home/user/cancelFollowUser";          // 取消关注
        public static final String confirm_attention = DZ_PYQ + "home/user/followUser";                //  关注
        public static final String myHomePageList = DZ_PYQ + "thread/getMyThreadList";                 //  我的帖子


        public static final String request_user_info = base + "social/post/userinfo";      //用户信息
        public static final String request_update_pic = hyb_url + "ws/post/user/upload_bgpic";      //更新背景图
        public static final String request_bg_pic = hyb_url + "ws/get/user/bgpic";      //获取背景图
        public static final String request_social_list = DZ_PYQ + "thread/getAllThreadList";      //推荐列表
        public static final String request_social_list2 = DZ_PYQ + "thread/getFriendThreadList";      //好友动态
        public static final String request_topiclike = DZ_PYQ + "thread/forum/threadLike";      //点赞
        public static final String request_credit = DZ_PYQ + "thread/forum/post/reward/credit";      //获取奖励积分
        public static final String request_cancel_topiclike = DZ_PYQ + "thread/forum/cancelThreadLike";      //取消点赞
        public static final String request_topiccomment = DZ_PYQ + "thread/forum/post/thread/docomment";      //评论
        public static final String request_create_topic = DZ_PYQ + "thread/forum/post/subject";      //发帖
        public static final String request_refreshOne_item = DZ_PYQ + "thread/forum/get/thread/detail";    //请求刷新某条item数据
        public static final String request_uploadImage_information = hyb_url + "ws/post/uploadImage";      //上传图片
        public static final String request_release_information = DZ_PYQ + "api/mobile/index.php?version=4&module=upload_file"; //上传图片
        public static final String circle_web_activity = DZ_PYQ + "pages/thread/show?tid="; //上传图片
        public static final String Limit = base + "get/biz/limit"; //充值、提现需调用的接口获取上下限
        public static final String vip_minszx = "http://vip.minszx.com/app/index.php?i=1&c=entry&do=home&m=superman_creditmall&pkregister="; //积分兑换商城


        public static final String application_home_category = base + "get/application/home/category";
        public static final String application_ad = base + "get/application/ad";
        public static final String application_common_menu = base + "get/application/home/life";
        public static final String yue_change = base + "get/merchantBalanceChangeH5?pkmuser=";
        public static final String yu_e_bao_can = base + "post/balanceInvestCan";
        public static final String yu_e_bao_index = base + "post/balanceInvestIndex";

        public static final String getShareData = hyb_url + "S03/share/getShare";

        public static final String appShareUrl = app_h5_url + "#/hybshare?pkregister=";


        /**********************************智慧尚峰 start*********************************************/
        /**
         * 首页菜单
         */
        public static String MENU_URL = base + "shangfeng/applicationConfig/getApplication";

        /**
         * 生活服务
         */
        public static String LIFE_SERVICE = base + "shangfeng/applicationConfig/findApplicationConfigByForm";

        /**
         * 城市列表
         */
        public static String SELECT_CITY_URL = base + "shangfeng/city/findAllCity";
        /**
         * 商家列表
         */
        public static String MERCHANTS_URL = base + "shangfeng/hybMerchant/findAllHybMerchant";

        /**
         * 商家详情
         */
        public static String MERCHANT_PARTICULARS_URL = base + "shangfeng/hybMerchant/getMerchantDetailsById";
        /**
         * 获取最近一笔未完成订单
         */
        public static final String LAST_ORDER_URL = base + "shangfeng/order/v1/light/last";
        /**
         * 预约下单
         */
        public static final String BOOKING_ORDER_URL = base + "shangfeng/order/v1/light/pre";
        /**
         * 取消订单
         */
        public static final String CANCEL_ORDER_URL = base + "shangfeng/order/v1/light/cancel";
        /**
         * 查询单个服务
         */
        public static String GETONEMENU = base + "shangfeng/applicationConfig/getOneMenu";

        public static String OrderRecordList = shangfengh5 + "/order/index.html#/myorder?syscode=hyb";

        public static String BusinessHistory = shangfengh5 + "/purchases.html?syscode=hyb";

        public static String PAY_TYPE = base + "shangfeng/order/v1/paytype";

        public static String SCANQCODE_DISCOUNT = base + "shangfeng/order/v1/discount";

//        public static String UpdateSCANPAY = base + "shangfeng/order/v1/update/scan/status";

        public static String ORDERADD = base + "shangfeng/order/v1/add";

        public static String TOPAY = base + "shangfeng/order/v1/toPay";
        /**
         * 系统消息
         */
        public static String MESSAGES_URL = base + "shangfeng/hybCtPlatformJpushRecord/findAllByUserId";

        /**********************************智慧尚峰 end*********************************************/

    }


    /**************************************************************************************************************
     * SharePreference存储数据
     ************************************************************************************************************/
    public static final class userConfig {
        public static final String phoneno = "phoneno";//电话好吗
        public static final String pkregister = "pkregister";//账号
        public static final String loginpassword = "loginpassword";//登陆密码
        public static final String nickname = "nickname";
        public static final String nativeLoginPassword = "nativeLoginPassword";//登陆密码
        public static final String uid = "uid";
        public static final String is_card_sales = "is_card_sales";//推广人权限字段（0: 未开通， 1: 已开通）
        public static final String paypassword = "paypassword";//支付密码
        public static final String is_Login = "login";//判断是否已经登录
        public static final String position = "position";//用户头像
        public static final String strength = "strength";//密码等级
        public static final String latu = "latu";//当前定位的经纬度
        public static final String lngu = "lngu";//当前定位的经纬度
        public static final String citycode = "citycode";//当前定位的城市编码
        public static final String switch_lat = "switch_lat";//切换后的城市经纬度
        public static final String switch_lng = "switch_lng";//切换后的城市经纬度
        public static final String switch_citycode = "switch_citycode";//切换后的城市编码
        public static final String lang_lat = "lang_lat";//订货郎重新定位的城市编码
        public static final String adress = "adress";
        public static final String cityAdress = "cityAdress";
        public static final String detail_adress = "detail_adress";//具体的地址
        public static final String sign_name = "sign_name";//朋友圈签名


        public static final String CURRENT_CITY = "CURRENT_CITY";//当前城市
        public static final String CURRENT_LATU = "CURRENT_LATU";//当前城市纬度
        public static final String CURRENT_LNGU = "CURRENT_LNGU";//当前城市经度


        /*************
         * 供货区所需要的省市区
         ***************/
        public static final String pname = "pname";//省
        public static final String cname = "cname";//市
        public static final String adName = "adName";//区

        /**
         * 众筹显示余额
         */
        public static final String cf_display_amount_key = "cf_display_amount_key";

    }


    /**************************************************************************************************************
     * 图片存储路径
     ************************************************************************************************************/
    public static final class path {
        public static final String FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/hyb/";
        public static final String PORTRAIT_PATH = FOLDER_PATH + Config.name.PORTRAIT_NAME;
        public static final String CLUB_PATH = FOLDER_PATH + Config.name.CLUB_PIC_NAME;
        public static final String USER_PIC_PATH = FOLDER_PATH + Config.name.USER_PIC_NAME;
        public static final String ID_PIC_PATH = FOLDER_PATH + Config.name.ID_PIC_NAME;
        public static final String TEMP_FOLDER_PATH = FOLDER_PATH + "/temp/";
        public static final String BANNER_PATH = FOLDER_PATH + Config.name.BANNER_NAME;
    }

    /**************************************************************************************************************
     * 图片存储路径尾缀
     ************************************************************************************************************/
    public static final class name {
        public static final String PORTRAIT_NAME = "portrait.jpg";
        public static final String CLUB_PIC_NAME = "clubpic.jpg";
        public static final String USER_PIC_NAME = "userPic.jpg";
        public static final String ID_PIC_NAME = "idCardPic.jpg";
        public static final String TEMP_PIC_NAME = "tempPic.jpg";
        public static final String BANNER_NAME = "banner.jpg";
    }


}
