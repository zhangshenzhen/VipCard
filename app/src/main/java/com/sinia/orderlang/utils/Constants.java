package com.sinia.orderlang.utils;

public class Constants {

//	public static final String BASE_URL = "http://192.168.0.120:8080/OrderLang/xiNaiInterfaceController.do?interface&";
	/* 杨建*/
//	public static final String BASE_URL = "http://192.168.0.12:7778/OrderLang/xiNaiInterfaceController.do?interface&";
	/*杨峰*/
//public static final String BASE_URL = "http://192.168.1.121:8080/OrderLang/xiNaiInterfaceController.do?interface&";
	/*188测试*/
//	public static final String BASE_URL = "http://123.57.232.188:8080/OrderLang/xiNaiInterfaceController.do?interface&";
	/*王凯旋*/
//public static final String BASE_URL = "http://192.168.1.115:8080/OrderLang/xiNaiInterfaceController.do?interface&";
//	/*正式服务器*/
	public static final String BASE_URL = "http://120.55.199.24:8080/OrderLang/xiNaiInterfaceController.do?interface&";
//	public static final String BASE_URL = "http://120.55.199.24:8080/OrderLang/xiNaiInterfaceController.do?interface&";
	/*原订货郎*/
//	public static final String BASE_URL = "http://139.196.105.61:8080/OrderLang/xiNaiInterfaceController.do?interface&";
	public static final String userId = "2";

	public static class REQUEST_TYPE {
		/** 个人中心 */
		public final static int PERSONAL_CENTER = 100;
		/** 更新资料 */
		public final static int UPDATE_PER_INFO = 101;
		/** 修改密码 */
		public final static int UPDATE_PASSWORD = 102;
		/** 意见反馈 */
		public final static int ADDADVICE = 103;
		/** 红包详情 */
		public final static int REDBAODETAIL = 104;
		/** 收藏 */
		public final static int ADDCOLLECTION = 105;
		/** 提交红包 */
		public final static int ADDREDBAO = 106;
		/** 取消收藏 */
		public final static int DELAYCOLLECTION = 107;
		/** 支付红包 */
		public final static int PAYREDBAO = 108;
		/** 红包管理 */
		public final static int REDBAOMANAGER = 109;
		/** 红包详情 */
		public final static int REDBAOMANAGER_DETAIL = 110;
		/** 删除红包 */
		public final static int DELREDBAO = 111;
		/** 收藏列表 */
		public final static int COLLECTIONLIST = 112;
		/** 特价详情 */
		public final static int TEJIADETAIL = 113;
		/** 添加购物车 */
		public final static int ADDSHOPPINGCAR = 114;
		/** 购物车列表 */
		public final static int CARLIST = 115;
		/** 添加地址 */
		public final static int ADD_ADDRESS = 116;
		/** 地址列表 */
		public final static int ADDRESS_LIST = 117;
		/** 删除地址 */
		public final static int DELADDRESS = 118;
		/** 地址默认 */
		public final static int ADDRESSSETMOREN = 119;
		/** 提交订单 */
		public final static int ADDORDER = 120;
		/** 支付订单 */
		public final static int PAYORDER = 121;
		/** 订单管理 */
		public final static int ORDERMANAGER = 122;
		/** 评价订单 */
		public final static int ORDERCOMMENT = 123;
		/** 申请售后 */
		public final static int APPLYREFUND = 124;
		/** 订单详情 */
		public final static int ORDERDETAIL = 125;
		/** 删除收藏 */
		public final static int DELCOLLECTION = 126;
		/** 商品清单 */
		public final static int ORDERGOOD = 127;

	}
	public final static String PARTNER_ID = "1310887401";

	public final static String APP_ID = "wx6e3e3269c5587287";

	public final static String APPSECRET = "0b6a10106c8b142a31d65a57f211e71e";

	public final static String APPKEY = "9megnOnOsMPOmtWQC4MS1WhEHERseB96";
	public static class RESPONSE_TYPE {
		public final static int STATUS_SUCCESS = 0;

		public final static int STATUS_EXCEPTION = 1;

		public final static int STATUS_FAILED = 2;

		public final static int STATUS_3 = 3;

	}

	public static class HANDLE_TYPE {

		public final static int LOGIN_VALUE_NOT_COMPLETE = 1;


	}

	public static class SP_HELPER {
		public final static String USER_INFO = "user_info";

		public final static String IS_LOGIN = "is_login";

		public final static String HOT_COIN = "hot_coin";

		public final static String BROWSE_RECORD = "browse_record";

		public final static String IMG_URL = "img_url";

		public final static String IS_SET_PWD = "is_set_pwd";

		public final static String HAS_MIMA = "has_mima";
	}

}
