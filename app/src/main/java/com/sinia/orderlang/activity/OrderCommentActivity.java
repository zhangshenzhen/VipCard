package com.sinia.orderlang.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.sinia.orderlang.bean.OrderManagerBean;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.request.OrderCommentRequest;
import com.sinia.orderlang.utils.BitmapUtilsHelp;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.StringUtil;

/**
 * 订单评价
 */
public class OrderCommentActivity extends BaseActivity {
	private TextView tv_ordernum, tv_name, tv_taste, tv_num, tv_price, tv_time,
			tv_submit;
	private ImageView img_good;
	private EditText et_content;
	private RatingBar rb_score;
	private OrderManagerBean orderManagerBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ordercomment, "订单评价");
		orderManagerBean = (OrderManagerBean) getIntent().getSerializableExtra(
				"OrderManagerBean");
		getDoingView().setVisibility(View.GONE);
		initView();
		setData();
	}

	private void initView() {
		tv_ordernum = (TextView) findViewById(R.id.tv_ordernum);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_taste = (TextView) findViewById(R.id.tv_taste);
		tv_num = (TextView) findViewById(R.id.tv_num);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_submit = (TextView) findViewById(R.id.tv_submit);
		img_good = (ImageView) findViewById(R.id.img_good);
		et_content = (EditText) findViewById(R.id.et_content);
		rb_score = (RatingBar) findViewById(R.id.rb_score);
		tv_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String content = et_content.getEditableText().toString().trim();
				float score = rb_score.getRating();
				if (score == 0) {
					showToast("请评分");
				} else if (StringUtil.isEmpty(content)) {
					showToast("请输入评价");
				} else {
					orderComment((int)score + "", content);
				}
			}
		});
	}

	private void setData() {
		tv_ordernum.setText("订单号：" + orderManagerBean.getOrderNum());
		BitmapUtilsHelp.getImage(this, R.drawable.default_img).display(
				img_good, orderManagerBean.getImageUrl());
		tv_name.setText(orderManagerBean.getGoodName());
		tv_num.setText(orderManagerBean.getGoodNum());
		tv_price.setText(orderManagerBean.getOrderPrice());
		 tv_time.setText(orderManagerBean.getCreateTime());
	}

	private void orderComment(String star, String content) {
		showLoad("");
		OrderCommentRequest request = new OrderCommentRequest();
		request.setMethod("orderComment");
		request.setOrderId(orderManagerBean.getOrderId());
		request.setUserId(getFromSharePreference(Config.userConfig.pkregister));
		request.setStar(star);
		request.setContent(content);
		logUrl(request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.ORDERCOMMENT,
				request.toString());
	}

	@Override
	public void requestSuccess() {
		// TODO Auto-generated method stub
		super.requestSuccess();
		dismiss();
		showToast("评价成功");
		finish();
	}

	@Override
	public void requestFailed() {
		// TODO Auto-generated method stub
		super.requestFailed();
		dismiss();
		showToast("评价失败");
	}
}
