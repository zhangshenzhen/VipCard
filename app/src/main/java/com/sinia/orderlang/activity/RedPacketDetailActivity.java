package com.sinia.orderlang.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.MainActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sinia.orderlang.bean.AddRedBaoBean;
import com.sinia.orderlang.bean.RedBaoManagerBean;
import com.sinia.orderlang.bean.RedBaoManagerDetailBean;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.request.DelRedBaoRequest;
import com.sinia.orderlang.request.RedBaoManagerDetailRequest;
import com.sinia.orderlang.utils.BitmapUtilsHelp;
import com.sinia.orderlang.utils.Constants;
//import com.zxing.encoding.EncodingHandler;


/**
 * 红包详情
 */
public class RedPacketDetailActivity extends BaseActivity implements
		OnClickListener {
	private ImageView  iv_delete, img_good, iv_erweima;
	private LinearLayout back_red;
	private TextView tv_order_num, tv_order_create_time, tv_shop_name,
			tv_orderstatus, tv_goodname, tv_info, tv_number, tv_pwd,
			tv_good_name, tv_address, tv_to_pay,tv_term,tv_use_time,tv_use_content;
	private LinearLayout ll_chat;
	private String orderId, status;
	private boolean flag;
	private String telephone;
	private AddRedBaoBean addRedBaoBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_red_packet_detail);
		getHeadParentView().setVisibility(View.GONE);
		getHengxian().setVisibility(View.GONE);
		orderId = getIntent().getStringExtra("orderId");
		status = getIntent().getStringExtra("status");
		if (status.equals("1")) {
			flag = true;
		}
		initView();
		redBaoManagerDetail();
	}

	private void redBaoManagerDetail() {
		showLoad("");
		RedBaoManagerDetailRequest request = new RedBaoManagerDetailRequest();
		request.setMethod("redBaoManagerDetail");
		request.setOrderId(orderId);
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.REDBAOMANAGER_DETAIL,
				request.toString());
	}

	@Override
	public void getRedBaoManagerDetailSuccess(RedBaoManagerDetailBean bean) {
		// TODO Auto-generated method stub
		super.getRedBaoManagerDetailSuccess(bean);

		dismiss();
		addRedBaoBean = new AddRedBaoBean();
		addRedBaoBean.setCreateTime(bean.getCreateTime());
		addRedBaoBean.setGoodNum(bean.getGoodNum());
		addRedBaoBean.setOrderId(bean.getOrderId());
		addRedBaoBean.setOrderNum(bean.getOrderNum());
		addRedBaoBean.setPayType(bean.getPayType());
		addRedBaoBean.setRedBaoStatus(bean.getRedBaoStatus());
		addRedBaoBean.setTureCost(bean.getTruePrice());
		tv_order_num.setText("订单号：" + bean.getOrderNum());
		tv_order_create_time.setText("订单创建时间：" + bean.getCreateTime());
		tv_shop_name.setText(bean.getMerchantName());
		switch (status) {
		case "1":
			tv_orderstatus.setText("未支付");
			break;
		case "2":
			tv_orderstatus.setText("待使用");
			break;
		case "3":
			tv_orderstatus.setText("已使用");
			break;
		default:
			break;
		}
		BitmapUtilsHelp.getImage(this, R.drawable.default_img).display(img_good, bean.getImageUrl());
		tv_goodname.setText(bean.getGoodName()+bean.getFuGoodName());
		tv_info.setText(bean.getTitle()+"（"+bean.getFuTitle()+"）");
		tv_number.setText("数量："+bean.getGoodNum());
		if(!flag){
			tv_pwd.setText(bean.getPassword());
		}
		tv_good_name.setText(bean.getGoodName()+bean.getFuGoodName());
		tv_address.setText(bean.getMerchantAddress());
		telephone = bean.getTelephone();
		tv_term.setText(bean.getBeginTime()+"至"+bean.getEndTime());
		tv_use_time.setText(bean.getBgtime()+"-"+bean.getEdtime());
		tv_use_content.setText(bean.getContent());


		Bitmap qecode = generateQRCode(bean.getPassword());
		iv_erweima.setImageBitmap(qecode);
		
	}

	private Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
		int w = matrix.getWidth();
		int h = matrix.getHeight();
		int[] rawData = new int[w * h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int color = Color.WHITE;
				if (matrix.get(i, j)) {
					color = Color.BLACK;
				}
				rawData[i + (j * w)] = color;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
		return bitmap;
	}

	private Bitmap generateQRCode(String content){
		QRCodeWriter writer = new QRCodeWriter();
		try {
			BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE,500,500);
			return bitMatrix2Bitmap(matrix);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void delRedBao(){
		showLoad("");
		DelRedBaoRequest request = new DelRedBaoRequest();
		request.setMethod("delRedBao");
		request.setOrderId(orderId);
		Log.d("URL", Constants.BASE_URL+request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.DELREDBAO, request.toString());
	}
	
	@Override
	public void delRedBaoSuccess() {
		// TODO Auto-generated method stub
		super.delRedBaoSuccess();
		dismiss();
		showToast("删除成功");
		finish();
	}
	
	@Override
	public void delRedBaoFailed() {
		// TODO Auto-generated method stub
		super.delRedBaoFailed();
		dismiss();
		showToast("删除失败");
	}
	@Override
	public void requestFailed() {
		// TODO Auto-generated method stub
		super.requestFailed();
		dismiss();
		showToast("请求失败");
	}

	private void initView() {
		back_red = (LinearLayout) findViewById(R.id.back_red);
		iv_delete = (ImageView) findViewById(R.id.iv_delete);
		img_good = (ImageView) findViewById(R.id.img_good);
		iv_erweima = (ImageView) findViewById(R.id.iv_erweima);
		tv_order_num = (TextView) findViewById(R.id.tv_order_num);
		tv_order_create_time = (TextView) findViewById(R.id.tv_order_create_time);
		tv_shop_name = (TextView) findViewById(R.id.tv_shop_name);
		tv_orderstatus = (TextView) findViewById(R.id.tv_orderstatus);
		tv_goodname = (TextView) findViewById(R.id.tv_goodname);
		tv_info = (TextView) findViewById(R.id.tv_info);
		tv_number = (TextView) findViewById(R.id.tv_number);
		tv_pwd = (TextView) findViewById(R.id.tv_pwd);
		tv_good_name = (TextView) findViewById(R.id.tv_good_name);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_to_pay = (TextView) findViewById(R.id.tv_to_pay);
		tv_term = (TextView) findViewById(R.id.tv_term);
		tv_use_time = (TextView) findViewById(R.id.tv_use_time);
		tv_use_content = (TextView) findViewById(R.id.tv_use_content);
		ll_chat = (LinearLayout) findViewById(R.id.ll_chat);
		back_red.setOnClickListener(this);
		iv_delete.setOnClickListener(this);
		ll_chat.setOnClickListener(this);
		tv_to_pay.setOnClickListener(this);
		if (flag) {
			tv_to_pay.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_red:
			Log.e("woyaokk","点击返回");
			RedPacketDetailActivity.this.finish();
			break;
		case R.id.iv_delete:
			// 删除红包
//			delRedBao();
			createCancelOrDeleteDialog("确定删除该红包？");
			break;
		case R.id.ll_chat:
			// 联系卖家
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ telephone));
			startActivity(intent);
			break;

			case R.id.tv_to_pay:
				MainActivity.SECKILL_FLAG_LIST = 2;
				Intent it = new Intent(this,SeckillOrderPayActivity.class);
				it.putExtra("AddRedBaoBean", addRedBaoBean);
				it.putExtra("orderId",addRedBaoBean.getOrderId());
				startActivity(it);
				finish();
				break;
		default:
			break;
		}
	}
	private Dialog createCancelOrDeleteDialog(final String content) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.dialog_loginout, null);
		final Dialog dialog = new AlertDialog.Builder(this).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		WindowManager windowManager = ((Activity) this).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (display.getWidth() - 100); // 设置宽度
		lp.height = (display.getHeight() * 2 / 5); // 设置高度
		dialog.getWindow().setAttributes(lp);
		dialog.setContentView(v, lp);

		final TextView ok = (TextView) dialog.findViewById(R.id.tv_add);
		final TextView tv_name = (TextView) dialog.findViewById(R.id.tv_name);
		final TextView tv_cancel = (TextView) dialog
				.findViewById(R.id.tv_cancel);
		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		tv_name.setText(content);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				delRedBao();
				dialog.dismiss();
			}
		});
		return dialog;
	}
}
