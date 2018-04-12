package com.sinia.orderlang.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.bean.AddOrderBean;
import com.sinia.orderlang.bean.CarCheckBean;
import com.sinia.orderlang.bean.GoodItemsBean;
import com.sinia.orderlang.bean.OrderDetailBean;
import com.sinia.orderlang.bean.OrderGoodBean;
import com.sinia.orderlang.bean.OrderGoodList;
import com.sinia.orderlang.bean.OrderManagerBean;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.http.HttpCallBackListener;
import com.sinia.orderlang.request.ComfireorderRequest;
import com.sinia.orderlang.request.DelXnOrderByIdRequest;
import com.sinia.orderlang.request.OrderDetailRequest;
import com.sinia.orderlang.request.OrderGoodRequest;
import com.sinia.orderlang.utils.BitmapUtilsHelp;
import com.sinia.orderlang.utils.Constants;

/**
 * 订单详情
 */
public class OrderDetailActivity extends BaseActivity implements
		OnClickListener {
	private TextView tv_order_num, tv_logistics_msg, tv_logistics_time,
			tv_name, tv_telephone, tv_address, tv_shop_name, tv_pay_state,
			tv_goodname, tv_number, btn1, btn2, tv_pay_type, tv_kuaidi_name,
			tv_good_money, tv_freight, tv_realmoney, tv_order_time;
	private LinearLayout ll_lianxi;
	private ImageView img_good;
	private String orderId = "";
	private OrderManagerBean orderManagerBean;
	private RelativeLayout rl_good_list;
	private OrderGoodList goodList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderdetail, "订单详情");
		getDoingView().setVisibility(View.GONE);
		orderId = getIntent().getStringExtra("orderId");
		orderManagerBean = (OrderManagerBean) getIntent().getSerializableExtra("OrderManagerBean");
		getOrderDetail();
		getGoodList();
		initView();
	}

	private void initView() {
		tv_order_num = (TextView) findViewById(R.id.tv_order_num);
		tv_logistics_msg = (TextView) findViewById(R.id.tv_logistics_msg);
		tv_logistics_time = (TextView) findViewById(R.id.tv_logistics_time);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_telephone = (TextView) findViewById(R.id.tv_telephone);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_shop_name = (TextView) findViewById(R.id.tv_shop_name);
		tv_pay_state = (TextView) findViewById(R.id.tv_pay_state);
		tv_goodname = (TextView) findViewById(R.id.tv_goodname);
		tv_number = (TextView) findViewById(R.id.tv_number);
		btn1 = (TextView) findViewById(R.id.btn1);
		btn2 = (TextView) findViewById(R.id.btn2);
		tv_pay_type = (TextView) findViewById(R.id.tv_pay_type);
		tv_kuaidi_name = (TextView) findViewById(R.id.tv_kuaidi_name);
		tv_good_money = (TextView) findViewById(R.id.tv_good_money);
		tv_freight = (TextView) findViewById(R.id.tv_freight);
		tv_realmoney = (TextView) findViewById(R.id.tv_realmoney);
		tv_order_time = (TextView) findViewById(R.id.tv_order_time);
		ll_lianxi = (LinearLayout) findViewById(R.id.ll_lianxi);
		img_good = (ImageView) findViewById(R.id.img_good);
		rl_good_list = (RelativeLayout) findViewById(R.id.rl_good_list);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		ll_lianxi.setOnClickListener(this);
		rl_good_list.setOnClickListener(this);
	}
	
	private void getOrderDetail() {
		OrderDetailRequest request = new OrderDetailRequest();
		request.setMethod("orderDetail");
		request.setOrderId(orderId);
		logUrl(request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.ORDERDETAIL,
				request.toString());

	}

	@Override
	public void getOrderDetailSuccess(OrderDetailBean bean) {
		// TODO Auto-generated method stub
		super.getOrderDetailSuccess(bean);
		tv_order_num.setText("订单号："+bean.getOrderNum());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
		Date date = new Date();
		tv_logistics_time.setText(sdf.format(date));
		switch (bean.getPayType()) {
		case "1":
			tv_pay_type.setText("支付宝支付");
			break;
		case "2":
			tv_pay_type.setText("微信支付");
			break;
		case "3":
			tv_pay_type.setText("银联支付");
			break;
		default:
			break;
		}
		tv_shop_name.setText(orderManagerBean.getShopName());
		switch (bean.getOrderStatus()) {
		case "1":
			tv_pay_state.setText("待支付");
			btn1.setText("取消订单");
			btn2.setText("立即支付");
			break;
		case "2":
			tv_pay_state.setText("待收货");
			btn1.setText("查看物流");
			btn2.setText("确认收货");
			break;
		case "3":
			tv_pay_state.setText("待收货");
			btn1.setText("查看物流");
			btn2.setText("确认收货");
			break;
		case "4":
			tv_pay_state.setText("交易完成");
			btn1.setText("申请售后");
			btn2.setBackgroundResource(R.drawable.red_btn_bg2);
			btn2.setTextColor(Color.parseColor("#D13C47"));
			btn2.setText("评价");
			break;
		case "5":
			tv_pay_state.setText("交易完成");
			btn1.setText("申请售后");
			btn2.setBackgroundResource(R.drawable.gray_btn_bg);
			btn2.setTextColor(Color.parseColor("#C7C7C7"));
			btn2.setText("已评价");
			break;
		case "7":
			tv_pay_state.setText("交易完成");
			btn1.setText("审核中");
			if (orderManagerBean.getCommentStatus().equals("1")) {
				btn2.setBackgroundResource(R.drawable.gray_btn_bg);
				btn2.setTextColor(Color.parseColor("#C7C7C7"));
				btn2.setText("已评价");
			} else {
				btn2.setBackgroundResource(R.drawable.red_btn_bg2);
				btn2.setTextColor(Color.parseColor("#D13C47"));
				btn2.setText("评价");
			}
			break;
		case "8":
			tv_pay_state.setText("交易完成");
			btn1.setText("退款成功");
			if (orderManagerBean.getCommentStatus().equals("1")) {
				btn2.setBackgroundResource(R.drawable.gray_btn_bg);
				btn2.setTextColor(Color.parseColor("#C7C7C7"));
				btn2.setText("已评价");
			} else {
				btn2.setBackgroundResource(R.drawable.red_btn_bg2);
				btn2.setTextColor(Color.parseColor("#D13C47"));
				btn2.setText("评价");
			}
			break;
		case "9":
			tv_pay_state.setText("交易完成");
			btn1.setText("申请失败");
			if (orderManagerBean.getCommentStatus().equals("1")) {
				btn2.setBackgroundResource(R.drawable.gray_btn_bg);
				btn2.setTextColor(Color.parseColor("#C7C7C7"));
				btn2.setText("已评价");
			} else {
				btn2.setBackgroundResource(R.drawable.red_btn_bg2);
				btn2.setTextColor(Color.parseColor("#D13C47"));
				btn2.setText("评价");
			}
			break;
		}
		tv_good_money.setText("¥"+orderManagerBean.getOrderPrice());
		tv_freight.setText("¥"+orderManagerBean.getFreight());
		tv_realmoney.setText("¥"+(Float.parseFloat(orderManagerBean.getOrderPrice())+Float.parseFloat(orderManagerBean.getFreight())));
		tv_order_time.setText("下单时间： "+orderManagerBean.getCreateTime());
		BitmapUtilsHelp.getImage(this, R.drawable.default_img).display(img_good, orderManagerBean.getImageUrl());
		tv_goodname.setText(orderManagerBean.getGoodName());
		tv_number.setText("数量：x"+orderManagerBean.getGoodNum());
		tv_name.setText(bean.getName());
		tv_telephone.setText(bean.getTelephone());
		tv_address.setText(bean.getAddress());
	}

	
	private void getGoodList(){
		OrderGoodRequest request = new OrderGoodRequest();
		request.setMethod("orderGood");
		request.setOrderId(orderId);
		logUrl(request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.ORDERGOOD, request.toString());
	}
	
	@Override
	public void getGoodsListSuccess(OrderGoodList list) {
		// TODO Auto-generated method stub
		super.getGoodsListSuccess(list);
		goodList = list;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn1:
			switch (orderManagerBean.getOrderStatus()) {
			case "1":
				delOrder(orderId);
				break;
			case "2":
				break;
			case "3":
				break;
			case "4":
				// 申请售后
				Intent intent4 = new Intent(this,
						ApplyAfterSaleActivity.class);
				intent4.putExtra("OrderManagerBean", orderManagerBean);
				startActivity(intent4);
				break;
			case "5":
				// 申请售后
				Intent intent5 = new Intent(this,
						ApplyAfterSaleActivity.class);
				intent5.putExtra("OrderManagerBean", orderManagerBean);
				startActivity(intent5);
				break;
			case "9":
				// 申请售后
				Intent intent9 = new Intent(this,
						ApplyAfterSaleActivity.class);
				intent9.putExtra("OrderManagerBean", orderManagerBean);
				startActivity(intent9);
				break;
			default:
				break;
			}
			break;
		case R.id.btn2:
			switch (orderManagerBean.getOrderStatus()) {
			case "1":
				// 去支付
				Intent it = new Intent(this,
						TejiaOrderPayActivity.class);
				it.putExtra("showWhat", 1);
				AddOrderBean addOrderBean = new AddOrderBean();
				addOrderBean.setBuyNum(orderManagerBean.getGoodNum());
				addOrderBean.setCreateTime(orderManagerBean.getCreateTime());
				addOrderBean.setOrderId(orderManagerBean.getOrderId());
				addOrderBean.setOrderNum(orderManagerBean.getOrderNum());
				addOrderBean.setPayType("1");
				addOrderBean.setTureCost(orderManagerBean.getOrderPrice());
				it.putExtra("AddOrderBean", addOrderBean);
				startActivity(it);
				break;
			case "2":
				comfireorder(orderId);
				break;
			case "3":
				comfireorder(orderId);
				break;
			case "4":
				Intent it4 = new Intent(this,
						OrderCommentActivity.class);
				it4.putExtra("OrderManagerBean", orderManagerBean);
				startActivity(it4);
				break;
			case "5":

				break;
			case "7":
				if (orderManagerBean.getCommentStatus().equals("1")) {

				} else {
					Intent it7 = new Intent(this,
							OrderCommentActivity.class);
					it7.putExtra("OrderManagerBean", orderManagerBean);
					startActivity(it7);
				}
				break;
			case "8":
				if (orderManagerBean.getCommentStatus().equals("1")) {

				} else {
					Intent it8 = new Intent(this,
							OrderCommentActivity.class);
					it8.putExtra("OrderManagerBean", orderManagerBean);
					startActivity(it8);
				}
				break;
			case "9":
				if (orderManagerBean.getCommentStatus().equals("1")) {

				} else {
					Intent it9 = new Intent(this,
							OrderCommentActivity.class);
					it9.putExtra("OrderManagerBean", orderManagerBean);
					startActivity(it9);
				}
				break;

			default:
				break;
			}
			break;
		case R.id.ll_lianxi:

			break;
		case R.id.rl_good_list:
			//跳转到订单清单列表
			Intent it = new Intent(this, GoodsListActivity.class);
			it.putExtra("fromWhere", true);
			CarCheckBean carCheckBean = new CarCheckBean();
			List<GoodItemsBean> data = new ArrayList<GoodItemsBean>();
			List<OrderGoodBean> lits = goodList.getOrderGoodItems();
			for(OrderGoodBean bean:lits){
				GoodItemsBean goodItemsBean = new GoodItemsBean();
				goodItemsBean.setImageUrl(bean.getImageUrl());
				goodItemsBean.setGoodName(bean.getGoodName());
				goodItemsBean.setGoodNum(bean.getGoodNum());
				goodItemsBean.setGoodPrice(bean.getGoodPrice());
				data.add(goodItemsBean);
			}
			carCheckBean.setData(data);
			it.putExtra("CarCheckBean", carCheckBean);
			startActivity(it);
			break;
		default:
			break;
		}
	}
	
	private void delOrder(String orderId) {
		DelXnOrderByIdRequest request = new DelXnOrderByIdRequest();
		request.setMethod("delXnOrderById");
		request.setOrderId(orderId);
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.get(OrderDetailActivity.this, request.toString(),
				new HttpCallBackListener() {

					@Override
					public void onSuccess(JSONObject json) {
						Log.d("result", json.toString());
						if (json.optInt("isSuccessful") == 0) {
							Toast.makeText(OrderDetailActivity.this, "删除成功",Toast.LENGTH_SHORT).show();
							finish();
						} else {
							Toast.makeText(OrderDetailActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onRequestFailed() {
						Toast.makeText(OrderDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
					}
				});
	}
	
	private void comfireorder(String orderId) {
		ComfireorderRequest request = new ComfireorderRequest();
		request.setMethod("comfireorder");
		request.setOrderId(orderId);
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.get(OrderDetailActivity.this, request.toString(),
				new HttpCallBackListener() {

					@Override
					public void onSuccess(JSONObject json) {
						Log.d("result", json.toString());
						if (json.optInt("isSuccessful") == 0) {
							Toast.makeText(OrderDetailActivity.this, "确认收货成功", Toast.LENGTH_SHORT).show();
							btn2.setClickable(false);

						} else {
							Toast.makeText(OrderDetailActivity.this, "确认收货失败", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onRequestFailed() {
						Toast.makeText(OrderDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
					}
				});
	}
}
