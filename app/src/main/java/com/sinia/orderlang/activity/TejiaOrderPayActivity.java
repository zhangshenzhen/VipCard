package com.sinia.orderlang.activity;

import java.io.IOException;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
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
import com.sinia.orderlang.bean.AddOrderBean;
import com.sinia.orderlang.utils.ScreenUtils;
import com.sinia.orderlang.utils.SharedPreferencesUtils;
import com.sinia.orderlang.utils.Utils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 特价订单支付
 */
public class TejiaOrderPayActivity extends BaseActivity implements VolleyCallBack,OnClickListener{
	private int showWhat = 1;
	private TextView tv_count, tv_money, tv_pay_type, tv_order_num,
			tv_order_time, tv_pay;
	private LinearLayout ll1, ll2, ll3;
	private AddOrderBean addOrderBean;
	private PsdDialog psdDialog;
	private String key = "";
	private EditText et;
	private TextView lang_balance;

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;
	private TextView lang_to_pay;
	private Boolean PSD_TYPE = false;

	// 微信
	StringBuffer sb = new StringBuffer();
	Map<String, String> resultunifiedorder;
	PayReq req = new PayReq();
	private static final String TAG = "MicroMsg.SDKSample.PayActivity";
	private float money;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private IWXAPI api;
	private String orderId = "", price = "";
	private String balance;
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seckill_order_pay, "支付订单");
		getDoingView().setVisibility(View.GONE);
		showWhat = getIntent().getIntExtra("showWhat", 0);
		addOrderBean = (AddOrderBean) getIntent().getSerializableExtra(
				"AddOrderBean");
		orderId = addOrderBean.getOrderId();
		SharedPreferencesUtils.putShareValue(this, "WX_orderId", orderId);
		price = addOrderBean.getTureCost();
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Map<String,String> params = new HashMap<>();
		params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
		Wethod.httpPost(TejiaOrderPayActivity.this,1234, Config.web.system_infomatiob, params, this);
	}

	private void initView() {


		lang_to_pay = (TextView) findViewById(R.id.lang_to_pay);
		lang_balance = (TextView) findViewById(R.id.lang_balance);
		tv_count = (TextView) findViewById(R.id.tv_count);
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_pay_type = (TextView) findViewById(R.id.tv_pay_type);
		tv_order_num = (TextView) findViewById(R.id.tv_order_num);
		tv_order_time = (TextView) findViewById(R.id.tv_order_time);
		tv_pay = (TextView) findViewById(R.id.tv_pay);
//		tv_pay.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// createPayProblemDialog(SeckillOrderPayActivity.this);
//
//			}
//		});
		lang_to_pay.setOnClickListener(this);
		ll1 = (LinearLayout) findViewById(R.id.ll1);
		ll2 = (LinearLayout) findViewById(R.id.ll2);
		ll3 = (LinearLayout) findViewById(R.id.ll3);
		tv_count.setText(addOrderBean.getBuyNum());
		tv_money.setText(addOrderBean.getTureCost());
		String state = addOrderBean.getPayType();
		String status = "";
		if (state.equals("1")) {
			status = "未支付";
		} else if (state.equals("2")) {
			status = "未验证";
		} else if (state.equals("3")) {
			status = "已验证";
		}
		tv_pay_type.setText("订单状态：" + status);
		tv_order_num.setText("订单编号：" + addOrderBean.getOrderNum());
		tv_order_time.setText("订单时间：" + addOrderBean.getCreateTime());
	}

//	private void payOrder(String payType) {
//		showLoad("");
//		PayOrderRequest request = new PayOrderRequest();
//		request.setMethod("payOrder");
//		request.setOrderId(addOrderBean.getOrderId());
//		request.setPayType(payType);
//		logUrl(request.toString());
//		CoreHttpClient.listen = this;
//		CoreHttpClient.get(this, Constants.REQUEST_TYPE.PAYORDER,
//				request.toString());
//	}

	@Override
	public void requestSuccess() {


		// TODO Auto-generated method stub
		super.requestSuccess();
		dismiss();
		showToast("支付成功");
		startActivityForNoIntent(LangMainActivity.class);
		finish();
	}

	@Override
	public void requestFailed() {
		// TODO Auto-generated method stub
		super.requestFailed();
		dismiss();
		showToast("支付失败");
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
				Intent intent = new Intent(TejiaOrderPayActivity.this, ForgetPayPsdActivity.class);
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
		if(reqcode == 1234){
			ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
			try {

				SystemInfomationBean systemInfomationBean = objectMapper.readValue(result.toString(), SystemInfomationBean.class);
				balance = systemInfomationBean.getResultData().getBalance();
				systemPkmuser = systemInfomationBean.getResultData().getPkmuser();
				mHandler.sendEmptyMessage(1);
				if(Double.parseDouble(addOrderBean.getTureCost())<Double.parseDouble(balance)){
					tv_pay.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// createPayProblemDialog(SeckillOrderPayActivity.this);
							//试用余额支付
							psdDialog = new PsdDialog(TejiaOrderPayActivity.this, "充值", "", "余额:" + balance + "元");
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
			params.put("payEntrance", "2");
			params.put("orderId", orderId);//订单的单号
			params.put("payResult", "Success");
			Wethod.httpPost(TejiaOrderPayActivity.this,1123, Config.web.lang_pay_balance, params, TejiaOrderPayActivity.this);
		}else if(reqcode == 1123){
			Toast.makeText(TejiaOrderPayActivity.this, "支付成功",Toast.LENGTH_SHORT).show();
			startActivity(new Intent(this, com.sinia.orderlang.activity.AllOrderActivity.class));
			MainActivity.lang_flag_tejia = 2;
			finish();
		}
	}


	@Override
	public void onFailed(int reqcode, Object result) {
		if(reqcode == 1234){
			Wethod.ToFailMsg(this, result);
		}else if(reqcode == 2234){
//			psdDialog.dismiss();
			Wethod.ToFailMsg(this, result);

			ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
			try {
				RespBase respBase = objectMapper.readValue(result.toString(),RespBase.class);
				if(respBase.getResultData().toString().contains("未查询到用户支付密码信息")){
					psdDialog.dismiss();
					Intent intent = new Intent(this, ChangePasswordActivity.class);
					intent.putExtra("psdType",PSD_TYPE);
					startActivity(intent);
				}else if(respBase.getResultData().toString().contains("支付密码输入有误")){

					/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("提示");
					builder.setMessage("支付密码有误,请重新输入");
					builder.setNegativeButton("重试",null);
							*//*new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							key = "";
							et.setText("");
							dialog.dismiss();
						}
					});*//*
					builder.setPositiveButton("修改支付密码",null);

							*//*new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							psdDialog.dismiss();
							dialog.dismiss();
							Intent intent = new Intent(TejiaOrderPayActivity.this, ForgetPayPsdActivity.class);
							startActivity(intent);
						}
					});*//*
					builder.create().show();*/
					createPayProblemDialog(TejiaOrderPayActivity.this);
				}else{
					Toast.makeText(this,respBase.getResultData().toString(),Toast.LENGTH_LONG).show();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}else if(reqcode == 1123){
			Toast.makeText(TejiaOrderPayActivity.this, "支付失败",Toast.LENGTH_SHORT).show();
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
					Wethod.httpPost(TejiaOrderPayActivity.this,2234, Config.web.checkout_pay_psd, payPsdParams, TejiaOrderPayActivity.this);
				}
				setKey();
			}
		};

	}
}
