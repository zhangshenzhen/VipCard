package com.sinia.orderlang.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.bean.OrderManagerBean;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.request.ApplyRefundRequest;
import com.sinia.orderlang.utils.BitmapUtilsHelp;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.ScreenUtils;
import com.sinia.orderlang.utils.StringUtil;
import com.sinia.orderlang.utils.Utils;

/**
 * 申请售后（退货退款）
 */
public class ApplyAfterSaleActivity extends BaseActivity implements
		OnClickListener {

	private TextView tvNeed;
	private RelativeLayout rlNeed;
	private Dialog dialog;
	private TextView tv_ordernum, tv_name, tv_color, tv_num, tv_price, tv_time,
			tv_submit, tv_lianxi;
	private ImageView img_good;
	private EditText et_content;
	private OrderManagerBean orderManagerBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applyaftersale, "申请售后");
		getDoingView().setVisibility(View.GONE);
		orderManagerBean = (OrderManagerBean) getIntent().getSerializableExtra(
				"OrderManagerBean");
		initView();
		setData();
	}

	public void initView() {
		rlNeed = (RelativeLayout) findViewById(R.id.rl_need);
		tvNeed = (TextView) findViewById(R.id.tv_need);
		rlNeed.setOnClickListener(this);
		tv_ordernum = (TextView) findViewById(R.id.tv_ordernum);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_num = (TextView) findViewById(R.id.tv_num);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_lianxi = (TextView) findViewById(R.id.tv_lianxi);
		tv_lianxi.setOnClickListener(this);
		tv_submit = (TextView) findViewById(R.id.tv_submit);
		img_good = (ImageView) findViewById(R.id.img_good);
		et_content = (EditText) findViewById(R.id.et_content);
		tv_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String content = et_content.getEditableText().toString().trim();
				if (StringUtil.isEmpty(content)) {
					showToast("请输入备注");
				} else {
					// submit(content);
					applyRefund(content);
				}
			}
		});
	}

	private void setData() {
		tv_ordernum.setText("订单号：" + orderManagerBean.getOrderNum());
		BitmapUtilsHelp.getImage(this, R.drawable.default_img).display(
				img_good, orderManagerBean.getImageUrl());
		tv_name.setText(orderManagerBean.getGoodName());
		tv_num.setText("数量：x" + orderManagerBean.getGoodNum());
		tv_price.setText("¥" + orderManagerBean.getOrderPrice());
		tv_time.setText(orderManagerBean.getCreateTime());

	}

	private void applyRefund(String content) {
		showLoad("");
		ApplyRefundRequest request = new ApplyRefundRequest();
		request.setMethod("applyRefund");
		request.setOrderId(orderManagerBean.getOrderId());
		request.setContent(content);
		
		logUrl(request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.APPLYREFUND,
				request.toString());
	}

	@Override
	public void requestSuccess() {
		// TODO Auto-generated method stub
		super.requestSuccess();
		dismiss();
		showToast("申请成功");
		finish();
	}

	@Override
	public void requestFailed() {
		// TODO Auto-generated method stub
		super.requestFailed();
		dismiss();
		showToast("申请失败");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_need:
			// createNeedDialog();
			break;
		case R.id.tv_lianxi:
			
			break;
		default:
			break;
		}
	}

	public Dialog createNeedDialog() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.dialog_need, null);
		dialog = new AlertDialog.Builder(this).create();
		dialog.show();

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.setContentView(v, params);
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		// lp.y = 0;
		lp.width = ScreenUtils.getScreenWidth(this) - Utils.dip2px(this, 100);
		// lp.height = ScreenUtils.getScreenHeight(this) / 3;
		// - Utils.dip2px(this, 15);
		window.setAttributes(lp);
		TextView tvBackGoodMoney = (TextView) v
				.findViewById(R.id.tv_backgoodmoney);
		TextView tvBackMoney = (TextView) v.findViewById(R.id.tv_backmoney);
		tvBackGoodMoney.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				tvNeed.setText("退货退款");
				dialog.dismiss();
			}
		});
		tvBackMoney.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				tvNeed.setText("退款");
				dialog.dismiss();
			}
		});
		return dialog;
	}
}
