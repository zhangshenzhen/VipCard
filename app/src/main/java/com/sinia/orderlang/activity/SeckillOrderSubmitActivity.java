package com.sinia.orderlang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.sinia.orderlang.bean.AddRedBaoBean;
import com.sinia.orderlang.bean.AddRedBaoExBean;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.request.AddRedBaoRequest;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.DialogUtils;

/**
 * 秒杀区订单确认
 */
public class SeckillOrderSubmitActivity extends BaseActivity implements
		OnClickListener {
	private TextView tv_good_name, tv_info, tv_num, tv_telephone, tv_need_pay,
			tv_submit,tv_total;
	private RelativeLayout rl_minus, rl_plus, rl_choose_pay;
	private ImageView iv_pay;
	private int showIndex = 1;
	private String goodname,info;
	private float price;
	private String goodId;
	private String totalPrice ="";
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				showIndex = 1;
				iv_pay.setImageResource(R.drawable.alipay);
				break;
			case 101:
				showIndex = 2;
				iv_pay.setImageResource(R.drawable.wechat);
				break;
			case 102:
				showIndex = 3;
				iv_pay.setImageResource(R.drawable.bank);
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seckill__submit, "提交订单");
		getDoingView().setVisibility(View.GONE);
		goodname = getIntent().getStringExtra("goodname");
		info = getIntent().getStringExtra("info");
		goodId = getIntent().getStringExtra("goodId");
		price = getIntent().getFloatExtra("price", 0);
		totalPrice = price+"";
		Log.d("lamp", "price="+price);
		initView();
	}

	private void addRedBao(String payType){
		showLoad("");
		AddRedBaoRequest request = new AddRedBaoRequest();
		request.setMethod("addRedBao");
		request.setUserId(getFromSharePreference(Config.userConfig.pkregister));
		request.setGoodId(goodId);
		request.setGoodNum(tv_num.getText().toString());
		if(price==0){
			request.setPayType("10");
		}else{
			request.setPayType("9");
		}
		request.setTruePrice(totalPrice);
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.ADDREDBAO, request.toString());
	}
	
	@Override
	public void addRedBaoSuccess(AddRedBaoBean bean) {
		// TODO Auto-generated method stub
		super.addRedBaoSuccess(bean);
		dismiss();
		if(price==0){
			showIndex = 4;
		}
		Intent it = new Intent(SeckillOrderSubmitActivity.this,SeckillOrderPayActivity.class);
		it.putExtra("AddRedBaoBean", bean);
		Log.e("woyaokk","orderId:"+bean.getOrderId());
		it.putExtra("orderId",bean.getOrderId());
		it.putExtra("showWhat", showIndex);
		startActivity(it);
		showToast("提交成功");
		finish();
	}

	@Override
	public void addRedBaoFailed(AddRedBaoExBean addRedBaoExBean) {
		super.addRedBaoFailed(addRedBaoExBean);
		dismiss();
		Toast.makeText(this,addRedBaoExBean.getReturnResult(),Toast.LENGTH_LONG).show();
	}
	
	/*@Override
	public void requestFailed() {
		// TODO Auto-generated method stub
		super.requestFailed();
		dismiss();
		showToast("提交失败");
	}*/
	
	private void initView() {
		iv_pay = (ImageView) findViewById(R.id.iv_pay);
		tv_good_name = (TextView) findViewById(R.id.tv_good_name);
		tv_info = (TextView) findViewById(R.id.tv_info);
		tv_num = (TextView) findViewById(R.id.tv_num);
		tv_telephone = (TextView) findViewById(R.id.tv_telephone);
		tv_need_pay = (TextView) findViewById(R.id.tv_need_pay);
		tv_submit = (TextView) findViewById(R.id.tv_submit);
		tv_total = (TextView) findViewById(R.id.tv_total);
		rl_minus = (RelativeLayout) findViewById(R.id.rl_minus);
		rl_plus = (RelativeLayout) findViewById(R.id.rl_plus);
		rl_choose_pay = (RelativeLayout) findViewById(R.id.rl_choose_pay);
		rl_minus.setOnClickListener(this);
		rl_plus.setOnClickListener(this);
		rl_choose_pay.setOnClickListener(this);
		tv_submit.setOnClickListener(this);
		tv_good_name.setText(goodname);
		tv_info.setText(info);
		tv_total.setText("¥ "+((float)(price)));
		tv_need_pay.setText("¥ "+((float)(price)));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_minus:
			int num1 = Integer.parseInt(tv_num.getText().toString());
			if (num1 > 1) {
				int num2 = num1 - 1;
				tv_num.setText(num2 + "");
				tv_total.setText("¥ "+((float)(num2*price)));
				tv_need_pay.setText("¥ "+((float)(num2*price)));
				totalPrice = ((float)(num2*price))+"";
			}
			break;
		case R.id.rl_plus:
			int num3 = Integer.parseInt(tv_num.getText().toString());
//			if (num3 < 2) {
				int num4 = num3 + 1;
				tv_num.setText(num4 + "");
				tv_total.setText("¥ "+((float)(num4*price)));
				tv_need_pay.setText("¥ "+((float)(num4*price)));
				totalPrice = ((float)(num4*price))+"";
//			}
			break;
		case R.id.rl_choose_pay:
//			DialogUtils.createChoosePayDialog(this, handler, showIndex);
			break;
		case R.id.tv_submit:
			int num = Integer.parseInt(tv_num.getText().toString());
			if(num>0){
				addRedBao(showIndex+"");
			}else{
				showToast("购买数量为0，无法提交");
			}
			break;
		default:
			break;
		}
	}
}
