package com.bjypt.vipcard.config;


public interface  Constant {
	/*-----------------------网络读取相关（json，图片等等）----------------------------*/
	// 网络连接超时时间
	public  int SOCKET_TIME_OUT_TIME = 30000;

	// 网络读取时间
	public  int SOCKET_READ_TIME = 20000;

	/*-------------------------图片相关 ----------------------------*/

	public  String DL_IMG_EXTENSION = ".lockImg";

	public  int LOCAL_COVER_MAX_COUNT = 20;
	public final String SHAREPERSISE_FILENAME="sharePersistent";



	/*-------------------------download 相关 ----------------------------*/

	public static int BUFFER_SIZE = 8192; // 缓冲字节

	public static final int TIMEOUT_TIME = 30000; // 链接超时时间

	public static final int READ_DATA_TIMEOUT_TIME = 60000;// 读取网络数据超时时间





	/**生活服务里面的用到的 */
	public   String Plane_from = "plane_from";
	public   String Plane_go = "plane_go";
	public   String Plane_from_Code = "plane_from_code";
	public   String Plane_go_Code = "plane_go_code";
	public   String Plane_SeatCode = "plane_seatcode";
	public   String Plane_time = "plane_time";
	public   String PlaneListInfo = "PlaneListInfo";
	public   String PlanePosition = "PlanePosition";

	public  String URL = "payurl";
	public  String SysOrderId = "sysorderid";
	public  String PayBundle = "paybundle";
	public  String PayAllMoney = "payallmoney";
	public  String GoPayType = "paytype";


	public  String RechargeRecord = "充值记录";
	public String ConsumeRecord = "消费记录";
	public String MsgRecord = "消息列表";
	public String BalanceRecord = "余额记录";
	public String CouponRecord = "优惠劵记录";
	public String WhitDraw = "提现记录";
	public String SysConsumeRecord = "系统消费记录";

	public int HandlerSuccessTure = 0;
	public int HandlerSuccessFail = -1;
	public int HandlerFail = 1;
	public int HandlerException = 2;
	public int HandlerGetBalance = 3;

	//注册
	public int AchieveRegister = 1;
	//密码修改
	public int ModifyPassword = 2;
	//手机号码修改
	public int ModifyPhone = 3;
	//设备变更
	public int ModifyDevice = 4;
	//商家推送
	public int MerchantPush = 5;
	//成为商家会员验证
	public int AddVip = 6;
	public  int Accept = 8;
	public  int Reject = 10;
	public int ResultCode_ProtocolActivity = 11;
	public int RequestCode_SystemCard = 444;
	public int ResultCode_SystemCard = 400;
	public int RequestCode_PhoneRecharge = 110;
	public int ResultCode_PhoneRecharge = 111;
	public int RequestCode_SinopecRecharge = 220;//中石化请求值
	public int ResultCode_SINOPECRecharge = 221; //中石化结果值
	public int RequestCode_PetroChinaRecharge = 220;//中石油请求值
	public int ResultCode_PetroChinaRecharge = 221; //中石油结果值
	public int RequestCode_QQRecharge = 300;//中石油请求值
	public int ResultCode_QQRecharge = 301; //中石油结果值
	
	
}
