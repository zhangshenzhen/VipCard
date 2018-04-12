package com.sinia.orderlang.activity;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.xmlpull.v1.XmlPullParser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.ChangePasswordActivity;
import com.bjypt.vipcard.activity.ForgetPayPsdActivity;
import com.bjypt.vipcard.activity.MainActivity;
import com.bjypt.vipcard.activity.TopupAmountActivity;
import com.bjypt.vipcard.base.RespBase;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.SystemInfomationBean;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.sinia.orderlang.base.SignUtils;
import com.sinia.orderlang.bean.AddRedBaoBean;
import com.sinia.orderlang.bean.PayResult;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.request.PayRedBaoRequest;
import com.sinia.orderlang.utils.ActivityManager;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.MD5;
import com.sinia.orderlang.utils.ScreenUtils;
import com.sinia.orderlang.utils.SharedPreferencesUtils;
import com.sinia.orderlang.utils.Util;
import com.sinia.orderlang.utils.Utils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


/**
 * 秒杀区支付
 */
public class SeckillOrderPayActivity extends BaseActivity implements VolleyCallBack,OnClickListener{
	private int showWhat = 1;
	private TextView tv_count, tv_money, tv_pay_type, tv_order_num,
			tv_order_time, tv_pay;
	private LinearLayout ll1, ll2, ll3;
	private AddRedBaoBean addRedBaoBean;

	// 商户PID
	public static final String PARTNER = "2088121265699315";
	// 商户收款账号
	public static final String SELLER = "1459535363@qq.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANobyKJmOKJJrSiVijUqRno3Lb8SEvqUEx2BSEHspHJ7+CsdkPiO2HM6bGAwyBbrg99aVqM2lQRggVlf0cDqcNS6jVAROIGW9KS3xAKVArRpY9dwdxUArIOSL8MAMwBYqsGDnYD8vTRnxJw9ViS1U7VVm0Az5+fCMsqpZGCMCNNtAgMBAAECgYEAx9q1mbm5I1wHgyyjm9vFALAXBwH0yg9tBJg2MdI5TX4zBK7u7BNYWx2puOp6LiZYjA0nXh7hpUUYwWB/vxoTj+jewzZxC/1vzg6ji8NwUa6TTSaOw5nrf8D7fkezXD4km/An43yrTSMwNBnkHB+6a2KPvkdy0Zl24GA3UFgybIECQQDu5FZQjLYTfTvDUiEe6aZhIcVud9aIgeukL2rJqiNLQjhRqg8CRSPuLH432JKHXq1UzJ9PIE5Nd3YBIHdrFa1pAkEA6bprWOAIFpuqGYafY7uN9dIgOJ1XBW2VTTjoqbrtlhe32IgWizXpkapvHH4bq2oC0LC///BMPmtiM0pnsJ4BZQJAbIjDCl+TjXE1SL00nQXMeI7OUy0RA/364CJG4w5wcI3KZpfbr81X0KrYQWkc0XHbUA6TRnDIAnwG2eY1OyqjGQJAFJEx9w0nyQ2Dp2FgTz9m25XU/gZibPjapYP3fVAUrhuCMwyupytkVUwhIOm795aJjuGu04/KtcdvScInY7RTvQJBAObI0Q3oXJBDnUxImMMPLg+LEaFgE4WLbTHzeMRIEB0RlkBmiIA9DcrltfLpFe6Hjv+j1noj+zj6yhX3qnDTYg4=";

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;
	private String balance;
	private TextView lang_balance;
	private TextView lang_to_pay;
	private String key = "";
	private EditText et;
	private PsdDialog psdDialog;
	private Boolean PSD_TYPE = false;

	// 微信
	StringBuffer sb = new StringBuffer();
	Map<String, String> resultunifiedorder;
	PayReq req = new PayReq();
	private static final String TAG = "MicroMsg.SDKSample.PayActivity";
	private float money;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private IWXAPI api;
	private String orderId="",price="";
	private String systemPkmuser;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
				case 1:
					lang_balance.setText("余额:" + balance + "元");
					break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		Map<String,String> params = new HashMap<>();
		params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
		Wethod.httpPost(SeckillOrderPayActivity.this,1234, Config.web.system_infomatiob, params, this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seckill_order_pay, "支付订单");
		getDoingView().setVisibility(View.GONE);
		showWhat = getIntent().getIntExtra("showWhat", 0);
		Log.d("lamp", "showWhat="+showWhat);
		addRedBaoBean = (AddRedBaoBean) getIntent().getSerializableExtra(
				"AddRedBaoBean");
//		orderId = addRedBaoBean.getOrderId();
		orderId = getIntent().getStringExtra("orderId");
		Log.e("woyaokk","orderId:"+orderId);
		SharedPreferencesUtils.putShareValue(this, "WX_orderId", orderId);
		price = addRedBaoBean.getTureCost();
		initView();
	}

	/*private void payRedBao(String type) {
		showLoad("");
		PayRedBaoRequest request = new PayRedBaoRequest();
		request.setMethod("payRedBao");
		request.setPayType(type);
		request.setOrderId(addRedBaoBean.getOrderId());
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.PAYREDBAO,
				request.toString());
	}*/

	@Override
	public void requestSuccess() {
		// TODO Auto-generated method stub
		super.requestSuccess();
		dismiss();
		showToast("订单支付成功");
		ActivityManager.getInstance().finishActivity(
				SeckillOrderSubmitActivity.class);
		ActivityManager.getInstance().finishActivity(
				SeckillDetailActivity.class);
		finish();
	}

	@Override
	public void requestFailed() {
		// TODO Auto-generated method stub
		super.requestFailed();
		dismiss();
		showToast("订单支付失败");
	}

	private void initView() {



		lang_to_pay = (TextView) findViewById(R.id.lang_to_pay);
		tv_count = (TextView) findViewById(R.id.tv_count);
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_pay_type = (TextView) findViewById(R.id.tv_pay_type);
		tv_order_num = (TextView) findViewById(R.id.tv_order_num);
		tv_order_time = (TextView) findViewById(R.id.tv_order_time);
		tv_pay = (TextView) findViewById(R.id.tv_pay);

		lang_balance = (TextView) findViewById(R.id.lang_balance);


		ll1 = (LinearLayout) findViewById(R.id.ll1);
		ll2 = (LinearLayout) findViewById(R.id.ll2);
		ll3 = (LinearLayout) findViewById(R.id.ll3);
		ll3 = (LinearLayout) findViewById(R.id.ll3);
		lang_to_pay.setOnClickListener(this);

		tv_count.setText(addRedBaoBean.getGoodNum());
		tv_money.setText("¥ " + addRedBaoBean.getTureCost());
		String state = addRedBaoBean.getRedBaoStatus();
		String status = "";
		if (state.equals("1")) {
			status = "未支付";
		} else if (state.equals("2")) {
			status = "未验证";
		} else if (state.equals("3")) {
			status = "已验证";
		}
		tv_pay_type.setText("订单状态：" + status);
		tv_order_num.setText("订单编号：" + addRedBaoBean.getOrderNum());
		tv_order_time.setText("订单时间：" + addRedBaoBean.getCreateTime());
	}

	/**
	 * 支付遇到问题
	 *
	 * @param context
	 * @return
	 */
	public Dialog createPayProblemDialog(final Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_pay_problem, null);
		final Dialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		dialog.setContentView(v, params);
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = ScreenUtils.getScreenWidth(context)
				- Utils.dip2px(context, 90);
		lp.height = ScreenUtils.getScreenHeight(context) / 4 + 25;
		window.setAttributes(lp);
		TextView mEnsure = (TextView) v.findViewById(R.id.tv_complete);
		TextView mCancel = (TextView) v.findViewById(R.id.tv_haveproblem);
		mEnsure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				psdDialog.dismiss();
				Intent intent = new Intent(SeckillOrderPayActivity.this, ForgetPayPsdActivity.class);
				startActivity(intent);

				finish();

			}
		});
		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				key = "";
				et.setText("");
				dialog.dismiss();
			}
		});
		return dialog;
	}

	@Override
	public void onSuccess(int reqcode, Object result) {
 		if(reqcode == 1123){
			Toast.makeText(SeckillOrderPayActivity.this, "支付成功",Toast.LENGTH_SHORT).show();
			startActivity(new Intent(this, RedPacketActivity.class));
			MainActivity.lang_flag_redpack = 2;
			MainActivity.SECKILL_FLAG = 2;
			finish();
		}else if(reqcode == 1234){
			ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
				try {

					SystemInfomationBean systemInfomationBean = objectMapper.readValue(result.toString(), SystemInfomationBean.class);
					balance = systemInfomationBean.getResultData().getBalance();

					systemPkmuser = systemInfomationBean.getResultData().getPkmuser();
					mHandler.sendEmptyMessage(1);
					if(Double.parseDouble(addRedBaoBean.getTureCost())<Double.parseDouble(balance)){
						tv_pay.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// createPayProblemDialog(SeckillOrderPayActivity.this);
								//试用余额支付
								psdDialog = new PsdDialog(SeckillOrderPayActivity.this, "充值", "", "余额:" + balance + "元");
								psdDialog.show();
							}
						});
					}else{
						Toast.makeText(this,"余额不足，请充值",Toast.LENGTH_LONG).show();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
		}else if(reqcode == 2234){
			Map<String, String> params = new HashMap<String, String>();
			params.put("payEntrance", "1");
			params.put("orderId", orderId);//订单的单号
			params.put("payResult", "Success");
			Wethod.httpPost(SeckillOrderPayActivity.this,1123, Config.web.lang_pay_balance, params, SeckillOrderPayActivity.this);
			psdDialog.dismiss();
		}
	}

	@Override
	public void onFailed(int reqcode, Object result) {
		if(reqcode == 1123){
			Toast.makeText(SeckillOrderPayActivity.this, "支付失败",Toast.LENGTH_SHORT).show();
		}else if(reqcode == 1234){
			Wethod.ToFailMsg(this,result);
		}else if(reqcode == 2234){
//			psdDialog.dismiss();
			Wethod.ToFailMsg(this, result);

			ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
			objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			try {
				RespBase respBase = objectMapper.readValue(result.toString(), RespBase.class);
				if (respBase.getResultData().toString().contains("未查询到用户支付密码信息")) {
					psdDialog.dismiss();
					Intent intent = new Intent(this, ChangePasswordActivity.class);
					intent.putExtra("psdType", PSD_TYPE);
					startActivity(intent);
				} else if (respBase.getResultData().toString().contains("支付密码输入有误")) {

					/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("提示");
					builder.setMessage("支付密码有误,请重新输入");

					builder.setNegativeButton("重试",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									key = "";
									et.setText("");
									dialog.dismiss();
								}
							});

					builder.setPositiveButton("修改支付密码",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									psdDialog.dismiss();
									dialog.dismiss();
									Intent intent = new Intent(SeckillOrderPayActivity.this, ForgetPayPsdActivity.class);
									startActivity(intent);
								}
							});
					builder.create().show();*/
					createPayProblemDialog(SeckillOrderPayActivity.this);
				} else {
					Toast.makeText(this, respBase.getResultData().toString(), Toast.LENGTH_LONG).show();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onError(VolleyError volleyError) {

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id){
			case R.id.lang_to_pay:
				//跳转到余额充值界面
				Intent intent = new Intent(this, TopupAmountActivity.class);
				intent.putExtra("FLAG", 3);
				intent.putExtra("pkmuser", systemPkmuser);
				startActivity(intent);
				break;
		}
	}
	/*********************************
	 * 支付密码校验
	 ********************************************/
	class PsdDialog extends Dialog {


		private Context context;
		private String messageOne, MessageTwo, title;
		private TextView mProductName;
		private TextView mProductMoney;
		private RelativeLayout mBack;
		private TextView mPsdTv;

		TextView t1, t2, t3, t4, t5, t6;


		public PsdDialog(Context context, String title, String messageOne, String MessageTwo) {
			super(context, R.style.MyDialogStyle);
			this.context = context;
			this.title = title;
			this.messageOne = messageOne;
			this.MessageTwo = MessageTwo;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			init();
		}

		public void init() {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.psd_input_dialog, null);
			setContentView(view);

			mProductName = (TextView) findViewById(R.id.product_name);
			mProductMoney = (TextView) findViewById(R.id.product_money);
			mPsdTv = (TextView) findViewById(R.id.psd_tv);

			mProductName.setText(messageOne);
			mProductMoney.setText(MessageTwo);
			mPsdTv.setText(title);


			mBack = (RelativeLayout) findViewById(R.id.iv_paysuccess_back);

			t1 = (TextView) findViewById(R.id.t1);
			t2 = (TextView) findViewById(R.id.t2);
			t3 = (TextView) findViewById(R.id.t3);
			t4 = (TextView) findViewById(R.id.t4);
			t5 = (TextView) findViewById(R.id.t5);
			t6 = (TextView) findViewById(R.id.t6);
			et = (EditText) findViewById(R.id.editHide);

			et.addTextChangedListener(tw);

			Window dialogWindow = getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
			lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
			dialogWindow.setAttributes(lp);


			//隐藏
			mBack.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					PsdDialog.this.dismiss();
				}
			});
		}

		void setKey() {
			char[] arr = key.toCharArray();
			t1.setText("");
			t2.setText("");
			t3.setText("");
			t4.setText("");
			t5.setText("");
			t6.setText("");
			for (int i = 0; i < arr.length; i++) {

				if (i == 0) {
					t1.setText("*");
				} else if (i == 1) {
					t2.setText("*");
				} else if (i == 2) {
					t3.setText("*");
				} else if (i == 3) {
					t4.setText("*");
				} else if (i == 4) {
					t5.setText("*");
				} else if (i == 5) {
					t6.setText("*");
				}

			}
		}


		TextWatcher tw = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				key = s.toString();
				if (key.length() == 6) {
					Map<String, String> payPsdParams = new HashMap<>();
					payPsdParams.put("phoneno", getFromSharePreference(Config.userConfig.phoneno));
					try {
						payPsdParams.put("payPassword", com.bjypt.vipcard.utils.MD5.getMd5(key, com.bjypt.vipcard.utils.MD5.key));
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}
					Wethod.httpPost(SeckillOrderPayActivity.this,2234, Config.web.checkout_pay_psd, payPsdParams, SeckillOrderPayActivity.this);
				}
				setKey();
			}
		};

	}

}
