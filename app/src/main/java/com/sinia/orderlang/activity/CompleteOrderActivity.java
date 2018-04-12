package com.sinia.orderlang.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.DefaultAdressActivity;
import com.bjypt.vipcard.common.Config;
import com.sinia.orderlang.bean.AddOrderBean;
import com.sinia.orderlang.bean.AddressListBean;
import com.sinia.orderlang.bean.CarCheckBean;
import com.sinia.orderlang.bean.GoodItemsBean;
import com.sinia.orderlang.bean.GoodsListBean;
import com.sinia.orderlang.bean.GoodsListList;
import com.sinia.orderlang.bean.TeJiaDetailBean;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.request.AddOrderRequest;
import com.sinia.orderlang.utils.BitmapUtilsHelp;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.DialogUtils;

/**
 * 支付订单
 */
public class CompleteOrderActivity extends BaseActivity implements
		OnClickListener {

	private ImageView img1, img2, img3;
	private RelativeLayout rl_use_jifen, rl_pay_type, rl_send_type, goto_pay,
			rlAddress, rl_detail;
	private boolean isCheck = false;
	private TeJiaDetailBean teJiaDetailBean;
	private String goodSize, credit, goodColor, count;
	private TextView tvNum, tvCredit, tvFreightMoney, tvGoodsMoney, tvUseMoney,
			tvCreditMoney, tvFinialPrice;
	public static TextView tv_pay_type, tv_send_type, tvPersonName, tvPhone,
			tvAddress;
	public String payType = "1", sendType = "1", addressId = "", goodId = "";
	private double useMoney = 0;
	public static boolean isAddress = false;
	private int useCredit = 2;
	private double cost;
	private String fixCost, carId;
	private int showIndex = 1;
	private boolean fromWhere;
	private int num;
	private String transId = "", numId = "", priceId = "", allPrice = "",
			freight = "";
	private String addressid = "";// 用户地址Id(直接支付进入GoodsDetailActivity页面进入)
	private String registername= "" ;//用户名称(直接支付进入GoodsDetailActivity页面进入)
	private String phoneno= "";// 用户手机号码(直接支付进入GoodsDetailActivity页面进入)
	private String receiptaddress = "";// 用户收货地址(直接支付进入GoodsDetailActivity页面进入)

	private String addressid2 = "";// 用户地址Id(购物车进入ShoppingCarFragment页面进入)
	private String registername2= "" ;//用户名称(购物车进入ShoppingCarFragment页面进入)
	private String phoneno2= "";// 用户手机号码(购物车进入ShoppingCarFragment页面进入)
	private String receiptaddress2 = "";// 用户收货地址(购物车进入ShoppingCarFragment页面进入)
	private CarCheckBean carCheckBean;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				showIndex = 1;
				tv_pay_type.setBackgroundResource(R.drawable.alipay);
				break;
			case 101:
				showIndex = 2;
				tv_pay_type.setBackgroundResource(R.drawable.wechat);
				break;
			case 102:
				showIndex = 3;
				tv_pay_type.setBackgroundResource(R.drawable.bank);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complete_order, "完善订单");
		getDoingView().setVisibility(View.GONE);
		teJiaDetailBean = (TeJiaDetailBean) getIntent().getSerializableExtra(
				"teJiaDetailBean");
		goodId = getIntent().getStringExtra("goodId");
		num = getIntent().getIntExtra("num", 0);
		fromWhere = getIntent().getBooleanExtra("fromWhere", false);

		initView();
		if (fromWhere) {
			// 从购物车跳过来的
			carCheckBean = (CarCheckBean) getIntent().getSerializableExtra(
					"CarCheckBean");
			transId = getIntent().getStringExtra("transId");
			numId = getIntent().getStringExtra("numId");
			priceId = getIntent().getStringExtra("priceId");
			allPrice = getIntent().getStringExtra("allPrice");
			freight = getIntent().getStringExtra("freight");

			addressid2 = getIntent().getStringExtra("addressid2");

			registername2 = getIntent().getStringExtra("registername2");
			phoneno2 = getIntent().getStringExtra("phoneno2");
			receiptaddress2 = getIntent().getStringExtra("receiptaddress2");

			initData2();
		} else {
			addressid = getIntent().getStringExtra("addressid");
			Log.e("tuyouze","addressid2 intent :"+addressid);
			registername = getIntent().getStringExtra("registername");
			phoneno = getIntent().getStringExtra("phoneno");
			receiptaddress = getIntent().getStringExtra("receiptaddress");
			initData();
		}
	}

	private void initView() {
		rl_send_type = (RelativeLayout) findViewById(R.id.rl_send_type);
		rl_pay_type = (RelativeLayout) findViewById(R.id.rl_pay_type);
		goto_pay = (RelativeLayout) findViewById(R.id.goto_pay);
		rlAddress = (RelativeLayout) findViewById(R.id.rl_address);
		rl_detail = (RelativeLayout) findViewById(R.id.rl_detail);
		img1 = (ImageView) findViewById(R.id.img1);
		img2 = (ImageView) findViewById(R.id.img2);
		img3 = (ImageView) findViewById(R.id.img3);
		tvNum = (TextView) findViewById(R.id.tv_num);
		tv_pay_type = (TextView) findViewById(R.id.tv_pay_type);
		tv_send_type = (TextView) findViewById(R.id.tv_send_type);
		tvGoodsMoney = (TextView) findViewById(R.id.tv_goods_money);
		tvFreightMoney = (TextView) findViewById(R.id.tv_freight_money);
		tvUseMoney = (TextView) findViewById(R.id.tv_use_money);
		tvPersonName = (TextView) findViewById(R.id.person_name);
		tvPhone = (TextView) findViewById(R.id.phone);
		tvAddress = (TextView) findViewById(R.id.address);
		tvFinialPrice = (TextView) findViewById(R.id.price_text);

		rl_detail.setOnClickListener(this);
		rl_send_type.setOnClickListener(this);
		goto_pay.setOnClickListener(this);
		rlAddress.setOnClickListener(this);
		rl_pay_type.setOnClickListener(this);
	}

	private void initData() {
		tvNum.setText("1件");
		if (teJiaDetailBean.getCurrentImageUrlItems().size() > 0) {
			BitmapUtilsHelp.getImage(this, R.drawable.default_img).display(
					img1,
					teJiaDetailBean.getCurrentImageUrlItems().get(0)
							.getImageUrl());
		}
		img1.setVisibility(View.VISIBLE);
		float sPrice = Float.parseFloat(teJiaDetailBean.getNowPrice());
		tvGoodsMoney.setText("¥ "+sPrice * num );
		tvFreightMoney.setText("¥ "+teJiaDetailBean.getFright());
		tvFinialPrice.setText("¥ "+(sPrice * num + Float.parseFloat(teJiaDetailBean
				.getFright())));

		if(addressid!=null||!addressid.equals("")){
			tvPersonName.setText(registername);
			tvPhone.setText(phoneno);
			tvAddress.setText(receiptaddress);
		}
	}

	private void initData2() {


		if(addressid2!=null||!addressid2.equals("")){
			tvPersonName.setText(registername2);
			tvPhone.setText(phoneno2);
			tvAddress.setText(receiptaddress2);
		}


		List<GoodItemsBean> itemsBeans = carCheckBean.getData();
		tvNum.setText(itemsBeans.size() + "件");
		if (itemsBeans.size() == 1) {
			img1.setVisibility(View.VISIBLE);
			BitmapUtilsHelp.getImage(this, R.drawable.default_img).display(
					img1, itemsBeans.get(0).getImageUrl());
		} else if (itemsBeans.size() == 2) {
			img1.setVisibility(View.VISIBLE);
			img2.setVisibility(View.VISIBLE);
			BitmapUtilsHelp.getImage(this, R.drawable.default_img).display(
					img1, itemsBeans.get(0).getImageUrl());
			BitmapUtilsHelp.getImage(this, R.drawable.default_img).display(
					img2, itemsBeans.get(1).getImageUrl());
		} else if (itemsBeans.size() > 2) {
			img1.setVisibility(View.VISIBLE);
			img2.setVisibility(View.VISIBLE);
			img3.setVisibility(View.VISIBLE);
			BitmapUtilsHelp.getImage(this, R.drawable.default_img).display(
					img1, itemsBeans.get(0).getImageUrl());
			BitmapUtilsHelp.getImage(this, R.drawable.default_img).display(
					img2, itemsBeans.get(1).getImageUrl());
			BitmapUtilsHelp.getImage(this, R.drawable.default_img).display(
					img3, itemsBeans.get(2).getImageUrl());
		}
		tvGoodsMoney.setText("¥ " + allPrice);
		tvFreightMoney.setText("¥ " + freight);
		tvFinialPrice.setText("¥ " + (Float.parseFloat(allPrice)
				+ Float.parseFloat(freight)));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_address:
			Intent it = new Intent(this, DefaultAdressActivity.class);
			it.putExtra("canSelect", true);
			it.putExtra("FLAG",1);
			startActivityForResult(it, 100);
			break;
		case R.id.rl_detail:
			// 跳转到商品清单
			if (fromWhere) {
				Intent it2 = new Intent(this, GoodsListActivity.class);
				it2.putExtra("fromWhere", fromWhere);
				it2.putExtra("CarCheckBean", carCheckBean);
				startActivity(it2);
			} else {
				GoodsListList glist = new GoodsListList();
				List<GoodsListBean> list = new ArrayList<GoodsListBean>();
				GoodsListBean goodsListBean = new GoodsListBean();
				goodsListBean.setGoodName(teJiaDetailBean.getGoodName());
				goodsListBean.setGoodNum(num + "");
				if (teJiaDetailBean.getCurrentImageUrlItems().size() > 0) {
					goodsListBean.setImageUrl(teJiaDetailBean
							.getCurrentImageUrlItems().get(0).getImageUrl());
				} else {
					goodsListBean.setImageUrl("");
				}
				goodsListBean.setPrice(teJiaDetailBean.getNowPrice());
				list.add(goodsListBean);
				glist.setItems(list);
				Intent it2 = new Intent(this, GoodsListActivity.class);
				it2.putExtra("goodsListList", glist);
				startActivity(it2);
			}
			break;
		case R.id.rl_pay_type:
			// 选择支付方式
//			DialogUtils.createChoosePayDialog(this, handler, showIndex);
			break;
		case R.id.goto_pay:
			// 提交订单

			Log.e("tuyouze","addressId:"+addressId+"    addressid2:"+addressid2);

			if (addressId.equals("")&&addressid2.equals("")&&addressid.equals("")) {
				showToast("选择地址");
				return;
			}
			if (fromWhere) {
				if(!addressid.equals("")){
					addOrder2(addressid);
				}else if(!addressId.equals("")){
					addOrder2(addressId);
				}else if(!addressid2.equals("")){
					addOrder2(addressid2);
				}

			} else {
				if(!addressid.equals("")){
					addOrder(addressid);
				}else if(!addressId.equals("")){
					addOrder(addressId);
				}else if(!addressid2.equals("")){
					addOrder(addressid2);
				}
			}
			break;
		default:
			break;
		}
	}

	private void addOrder(String addressId) {
		showLoad("");
		AddOrderRequest request = new AddOrderRequest();
		request.setMethod("addOrder");
		request.setUserId(getFromSharePreference(Config.userConfig.pkregister));
		request.setAddressId(addressId);
		request.setCarId("-1");
		request.setGoodId(goodId);
		request.setGoodNum(num + "");
		if (teJiaDetailBean.getTypeItems().size() > 0) {
			request.setGoodType(teJiaDetailBean.getTypeItems().get(0)
					.getTypeName());
		} else {
			request.setGoodType("-1");
		}

		if(Double.parseDouble(teJiaDetailBean.getNowPrice())*num==0.0){
			request.setPayType( "10");
		}else{
			request.setPayType( "9");
		}

		request.setOrderPrice(Float.parseFloat(teJiaDetailBean.getNowPrice())
				* num + "");
		request.setTruePrice((Float.parseFloat(teJiaDetailBean.getNowPrice())
				* num + Float.parseFloat(teJiaDetailBean.getFright()))
				+ "");
		logUrl(request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.ADDORDER,
				request.toString());
	}

	private void addOrder2(String addressId) {
		showLoad("");
		AddOrderRequest request = new AddOrderRequest();
		request.setMethod("addOrder");
		request.setUserId(getFromSharePreference(Config.userConfig.pkregister));
		request.setAddressId(addressId);
		request.setCarId(transId);
		request.setGoodId("-1");
		request.setGoodNum(numId);
		request.setGoodType("-1");
//		if(Double.parseDouble(priceId)==0.0){
//			request.setPayType("10");
//		}else{
			request.setPayType("9");
//		}

		Log.e("woyaokk","priceId:"+priceId);
		request.setOrderPrice(priceId);
		request.setTruePrice((Float.parseFloat(allPrice) + Float
				.parseFloat(freight)) + "");
		logUrl(request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.ADDORDER,
				request.toString());
	}

	@Override
	public void addOrderSuccess(AddOrderBean bean) {
		// TODO Auto-generated method stub
		super.addOrderSuccess(bean);
		dismiss();
		showToast("提交成功");
		// 跳转到
		Intent it = new Intent(this, TejiaOrderPayActivity.class);
		it.putExtra("showWhat", showIndex);
		it.putExtra("AddOrderBean", bean);
		startActivity(it);
		finish();
	}

	@Override
	public void requestFailed() {
		// TODO Auto-generated method stub
		super.requestFailed();
		dismiss();
		showToast("提交失败");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
//			switch (requestCode) {
//			case 100:
				// 地址设值
				AddressListBean bean = (AddressListBean) data
						.getSerializableExtra("AddressListBean");
				tvPersonName.setText(bean.getName());
				tvPhone.setText(bean.getTelephone());
				tvAddress.setText(bean.getAddress());
				addressId = bean.getAddressId();
//				break;
//
//			default:
//				break;
//			}
		}
	}

}
