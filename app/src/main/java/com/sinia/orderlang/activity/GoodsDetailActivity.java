package com.sinia.orderlang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.sinia.orderlang.bean.CommentItemsBean;
import com.sinia.orderlang.bean.CurrentImageUrlItemsBean;
import com.sinia.orderlang.bean.TeJiaDetailBean;
import com.sinia.orderlang.fragment.PicLagDetailFragment;
import com.sinia.orderlang.fragment.ProductDetailFragment;
import com.sinia.orderlang.fragment.ProductParameterFragment;
import com.sinia.orderlang.fragment.SelectAddress2Fragment;
import com.sinia.orderlang.fragment.SelectTypeFragment;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.request.AddCollectionRequest;
import com.sinia.orderlang.request.AddShoppingCarRequest;
import com.sinia.orderlang.request.DelayCollectionRequest;
import com.sinia.orderlang.request.TeJiaDetailRequest;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.views.SlideShowView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情
 */
public class GoodsDetailActivity extends BaseActivity implements
		OnClickListener/* , CallBackValue */{

	private static GoodsDetailActivity instance;
	private SlideShowView mySlideShowView;
	private List<String> picList = new ArrayList<String>();
	private RelativeLayout rl_choose_color, rl_choose_address, rl_leijipl,
			rl_gouwuche, rlComment, rl1;
	private TextView tv_more, tv_to_cart, tv_to_buy, tvGoodName, tvSpecialCost,
			tvLastPrice, tvFreight, tvSales, tvCommentCount, tvCommentPhone,
			tvCommentContent, tvCommentTime, tvCommentScore, tv_address;
	public static DrawerLayout drawer_layout;
	public static String id;
	private ImageView iv1, iv2, iv3, iv4, iv5, imgCollect;
	private String[] strColor = null;
	private Boolean isCollect = false;
	private List<String> listColor;
	public static String goodColor, goodSize = "0";
	public static int nowPrice;
	public static String unit;
	public static double num, price, cost;
	public static TextView tvGoodColor;
	private SelectTypeFragment selectTypeFragment;
	private SelectAddress2Fragment selectAddressFragment;
	private String userId;
	// public static QueryDetailByGoodIdItemsBean goodBean;
	public static TeJiaDetailBean teJiaDetailBean;
	private boolean isOpen;
	/**
	 * 0--添加,1--买
	 */
	private int flag = -1;
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				drawer_layout.closeDrawer(Gravity.RIGHT);
				break;
			case 101:
				// 加入购物车
				addShopCar();
				break;
			case 102:
				// 立即购买
				jumpToSubMit();
				break;
			case 103:
				// 选择地址
				tv_address.setText(msg.obj + "");
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_detail);
		getHeadParentView().setVisibility(View.GONE);
		getHengxian().setVisibility(View.GONE);
		initView();
		id = getIntent().getStringExtra("goodId");
		teJiaDetail();
		SelectTypeFragment.num = 1;
	}

	// 特价详情
	private void teJiaDetail() {
		showLoad("");
		TeJiaDetailRequest request = new TeJiaDetailRequest();
		request.setMethod("teJiaDetail");
		request.setGoodId(id);
		request.setUserId(getFromSharePreference(Config.userConfig.pkregister));
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.TEJIADETAIL,
				request.toString());
	}

	@Override
	public void getTeJiaDetailSuccess(TeJiaDetailBean bean) {
		// TODO Auto-generated method stub
		super.getTeJiaDetailSuccess(bean);
		dismiss();
		teJiaDetailBean = bean;
		tvCommentCount.setText(bean.getCommentSize());
		if (bean.getCommentItems().size() > 0) {
			CommentItemsBean commentItemsBean = bean.getCommentItems().get(0);
			tvCommentPhone.setText(commentItemsBean.getTelephone().replace(
					commentItemsBean.getTelephone().substring(3, 7), "****"));
			tvCommentScore.setText(commentItemsBean.getStar() + "分");
			switch (commentItemsBean.getStar()) {
			case "0":

				break;
			case "1":
				setVisible(iv1);
				setInvisible(iv2, iv3, iv4, iv5);
				break;
			case "2":
				setVisible(iv1, iv2);
				setInvisible(iv3, iv4, iv5);
				break;
			case "3":
				setVisible(iv1, iv2, iv3);
				setInvisible(iv4, iv5);
				break;
			case "4":
				setVisible(iv1, iv2, iv3, iv4);
				setInvisible(iv5);
				break;
			case "5":
				setVisible(iv1, iv2, iv3, iv4, iv5);
				break;
			default:
				break;
			}
			tvCommentContent.setText(commentItemsBean.getContent());
			tvCommentTime.setText(commentItemsBean.getCreateTime());
		}
		if (null != bean.getCurrentImageUrlItems()
				&& bean.getCurrentImageUrlItems().size() > 0) {
			for (int i = 0; i < bean.getCurrentImageUrlItems().size(); i++) {
				CurrentImageUrlItemsBean cuBean = bean
						.getCurrentImageUrlItems().get(i);
				picList.add(cuBean.getImageUrl());
			}
		} else {
			picList.add("");
		}
		mySlideShowView.startPlay();
		mySlideShowView.setImagePath(picList);
		tvGoodName.setText(bean.getGoodName());
		tvSpecialCost.setText("平台价 ¥ " + bean.getNowPrice());
		tvLastPrice.setText("商超价 ¥ " + bean.getBeforePrice());
		tvFreight.setText("运费: " + bean.getFright() + "元");
		if(bean.getSaleNum().equals("")){
			tvSales.setText("销量: 0");
		}else{
			tvSales.setText("销量: " + bean.getSaleNum());
		}
		ProductDetailFragment.getInstance().setDetailImg(bean.getDetailImage());
		ProductParameterFragment.getInstance().setParameterImg(
				bean.getCanshuImage());
		PicLagDetailFragment.getInstance().setId(id);
		PicLagDetailFragment.getInstance().setCommentNum(bean.getCommentSize());
		if (bean.getCollectionStauts().equals("1")) {
			// 已收藏
			isCollect = true;
			imgCollect.setImageResource(R.drawable.star_click);
		} else {
			// 未收藏
			isCollect = false;
			imgCollect.setImageResource(R.drawable.star);
		}
		if (null != bean.getTypeItems() && bean.getTypeItems().size() > 0) {
			tvGoodColor.setText(bean.getTypeItems().get(0).getTypeName());
		}
		selectTypeFragment.setData(teJiaDetailBean);
	}

	// 添加收藏
	private void addCollect() {
		showLoad("");
		AddCollectionRequest request = new AddCollectionRequest();
		request.setMethod("addCollection");
		request.setUserId(Constants.userId);
		request.setGoodId(id);
		request.setType("2");
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.ADDCOLLECTION,
				request.toString());
	}

	// 取消收藏
	private void cancelCollect() {
		showLoad("");
		DelayCollectionRequest request = new DelayCollectionRequest();
		request.setMethod("delayCollection");
		request.setUserId(Constants.userId);
		request.setGoodId(id);
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.DELAYCOLLECTION,
				request.toString());
	}

	@Override
	public void addCollectionSuccess() {
		// TODO Auto-generated method stub
		super.addCollectionSuccess();
		dismiss();
		showToast("收藏成功");
		isCollect = true;
		imgCollect.setImageResource(R.drawable.star_click);
	}

	@Override
	public void addCollectionFailed() {
		// TODO Auto-generated method stub
		super.addCollectionFailed();
		dismiss();
		showToast("收藏失败");
	}

	@Override
	public void cancelCollectionSuccess() {
		// TODO Auto-generated method stub
		super.cancelCollectionSuccess();
		dismiss();
		showToast("取消收藏成功");
		isCollect = false;
		imgCollect.setImageResource(R.drawable.star);
	}

	@Override
	public void cancelCollectionFailed() {
		// TODO Auto-generated method stub
		super.cancelCollectionFailed();
		dismiss();
		showToast("取消收藏失败");
	}

	private void addShopCar() {
		showLoad("");
		AddShoppingCarRequest request = new AddShoppingCarRequest();
		request.setMethod("addShoppingCar");
		request.setUserId(getFromSharePreference(Config.userConfig.pkregister));
		request.setGoodId(id);
		if (!tvGoodColor.getText().toString().equals("")) {
			request.setGoodType(tvGoodColor.getText().toString());
		} else {
			request.setGoodType("-1");
		}
		request.setGoodNum(SelectTypeFragment.num + "");
		logUrl(request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.ADDSHOPPINGCAR,
				request.toString());
	}

	@Override
	public void addShopCarSuccess() {
		// TODO Auto-generated method stub
		super.addShopCarSuccess();
		dismiss();
		showToast("添加购物车成功");
	}

	@Override
	public void addShopCarFailed() {
		// TODO Auto-generated method stub
		super.addShopCarFailed();
		dismiss();
		showToast("添加购物车失败");
	}

	private void setVisible(ImageView... imageViews) {
		for (ImageView img : imageViews) {
			img.setVisibility(View.VISIBLE);
		}
	}

	private void setInvisible(ImageView... imageViews) {
		for (ImageView img : imageViews) {
			img.setVisibility(View.INVISIBLE);
		}
	}

	public static GoodsDetailActivity getInstance() {
		if (instance == null) {
			instance = new GoodsDetailActivity();
		}
		return instance;
	}

	private void initView() {
		mySlideShowView = (SlideShowView) findViewById(R.id.mySlideShowView);
		rl_choose_color = (RelativeLayout) findViewById(R.id.rl_choose_color);
		rl_gouwuche = (RelativeLayout) findViewById(R.id.rl_gouwuche);
		rl_choose_address = (RelativeLayout) findViewById(R.id.rl_choose_address);
		rl_leijipl = (RelativeLayout) findViewById(R.id.rl_leijipl);
		tv_more = (TextView) findViewById(R.id.tv_more);
		tv_to_cart = (TextView) findViewById(R.id.tv_to_cart);
		tv_to_buy = (TextView) findViewById(R.id.tv_to_buy);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tvGoodName = (TextView) findViewById(R.id.tv_goodname);
		tvLastPrice = (TextView) findViewById(R.id.tv_last_price);
		tvFreight = (TextView) findViewById(R.id.tv_freight);
		tvSales = (TextView) findViewById(R.id.tv_sales);
		tvGoodColor = (TextView) findViewById(R.id.tv_goodcolor);
		tvCommentCount = (TextView) findViewById(R.id.tv_commentcount);
		rlComment = (RelativeLayout) findViewById(R.id.rl_comment);
		tvCommentPhone = (TextView) findViewById(R.id.tv_phone);
		iv1 = (ImageView) findViewById(R.id.iv1);
		iv2 = (ImageView) findViewById(R.id.iv2);
		iv3 = (ImageView) findViewById(R.id.iv3);
		iv4 = (ImageView) findViewById(R.id.iv4);
		iv5 = (ImageView) findViewById(R.id.iv5);
		tvCommentContent = (TextView) findViewById(R.id.tv_comment);
		tvCommentTime = (TextView) findViewById(R.id.tv_commenttime);
		tvCommentScore = (TextView) findViewById(R.id.tv_score);
		tvSpecialCost = (TextView) findViewById(R.id.tv_specialcost);
		imgCollect = (ImageView) findViewById(R.id.iv_collect);
		rl1 = (RelativeLayout) findViewById(R.id.rl1);
		rl_choose_color.setOnClickListener(this);
		rl_choose_address.setOnClickListener(this);
		rl_leijipl.setOnClickListener(this);
		tv_more.setOnClickListener(this);
		tv_to_cart.setOnClickListener(this);
		tv_to_buy.setOnClickListener(this);
		rl_gouwuche.setOnClickListener(this);
		imgCollect.setOnClickListener(this);
		selectTypeFragment = new SelectTypeFragment();
		selectAddressFragment = new SelectAddress2Fragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, selectTypeFragment)
				.add(R.id.fragment_container, selectAddressFragment).commit();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_2, PicLagDetailFragment.getInstance())
				.show(PicLagDetailFragment.getInstance()).commit();
		drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		// userId = MyApplication.getInstance().getUserbean(this).getId();
		findViewById(R.id.rl_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		LayoutParams params = mySlideShowView.getLayoutParams();
		Display display = getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
		params.height = display.getWidth();
		params.width = display.getWidth();
		mySlideShowView.setLayoutParams(params);
	}

	private void jumpToSubMit() {
		Intent it = new Intent(GoodsDetailActivity.this,
				CompleteOrderActivity.class);
		it.putExtra("teJiaDetailBean", teJiaDetailBean);
		it.putExtra("num", SelectTypeFragment.num);
		it.putExtra("goodId", id);
		it.putExtra("addressid",teJiaDetailBean.getAddressid());
		Log.e("tuyouze","teJiaDetailBean.getAddressid():"+teJiaDetailBean.getAddressid());
		it.putExtra("registername",teJiaDetailBean.getRegistername());
		it.putExtra("phoneno",teJiaDetailBean.getPhoneno());
		it.putExtra("receiptaddress",teJiaDetailBean.getReceiptaddress());
		startActivity(it);
		finish();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.rl_choose_color:
			// GoodsDetailRightFragment.getInstance().showSelectTypeFragment();
			getSupportFragmentManager().beginTransaction()
					.hide(selectAddressFragment).show(selectTypeFragment)
					.commit();
			drawer_layout.openDrawer(Gravity.RIGHT);
			break;
		case R.id.rl_choose_address:
			// GoodsDetailRightFragment.getInstance().showSelectAddressFragment();
			getSupportFragmentManager().beginTransaction()
					.hide(selectTypeFragment).show(selectAddressFragment)
					.commit();
			drawer_layout.openDrawer(Gravity.RIGHT);

			break;
		case R.id.rl_leijipl:
			Intent it = new Intent(this,CommentDetailActivity.class);
			it.putExtra("TeJiaDetailBean", teJiaDetailBean);
			startActivity(it);
			break;
		case R.id.tv_more:
			Intent it2 = new Intent(this,CommentDetailActivity.class);
			it2.putExtra("TeJiaDetailBean", teJiaDetailBean);
			startActivity(it2);
			break;
		case R.id.tv_to_cart:
			addShopCar();
			break;
		case R.id.rl_gouwuche:
			// if (MyApplication.getInstance().getBoolValue("is_login")) {
			LangMainActivity.showGouWuChe = 1;
			startActivityForNoIntent(LangMainActivity.class);
			finish();
			// } else {
			// showToast("请先登陆");
			// }
			break;
		case R.id.tv_to_buy:
			jumpToSubMit();
			break;
		case R.id.iv_collect:
			if (isCollect) {
				// 取消收藏
				cancelCollect();
			} else {
				// 收藏
				addCollect();
			}
			break;
		default:
			break;
		}
	}

	public void addShopCart() {
		// if (MyApplication.getInstance().getBoolValue("is_login")) {
		if (goodSize.equals("") || goodSize.equals("0")) {
			showToast("请输入购买尺寸");
		} else {
			num = Double.parseDouble(goodSize);
			price = (double) nowPrice;
			double f = num * price;
			BigDecimal b = new BigDecimal(f);
			cost = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			// double f1 =
			// b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			// cost = String.valueOf(f1);
			Log.e("tag", "总额=" + cost + " goodSize " + goodSize + " unit "
					+ unit);
			// buyButton(userId, id, "-1", "1", goodColor, cost, goodSize);
			// addShoppingCar(userId, id, 1, cost, goodSize, goodColor);
		}
		// } else {
		// showToast("请先登录");
		// }
	}

	public void submitOrderToComplement() {
		// if (MyApplication.getInstance().getBoolValue("is_login")) {
		if (goodSize.equals("") || goodSize.equals("0")) {
			showToast("请输入购买尺寸");
		} else {
			num = Double.parseDouble(goodSize);
			price = (double) nowPrice;
			double f = num * price;
			BigDecimal b = new BigDecimal(f);
			cost = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			// double f1 =
			// b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			// cost = String.valueOf(f1);
			Log.e("tag", "总额=" + cost);
			// addShoppingCarToOrder(userId,cost,id,"2",goodSize,goodColor);
			// buyButton(userId, id, "-1", "2", goodColor, cost, goodSize);
		}
		// } else {
		// showToast("请先登录");
		// }
	}

	public void setGoodColor(String color) {
		tvGoodColor.setText(color);
	}

	/*
	 * @Override public void SendMessageValue(List<String> addressList) { //
	 * TODO Auto-generated method stub
	 * 
	 * }
	 */

	/*
	 * // 商品详情接口 public void queryDetailByGoodId(String goodId, String userId) {
	 * QueryDetailByGoodIdRequest request = new QueryDetailByGoodIdRequest();
	 * request.setMethod("queryDetailByGoodId"); request.setGoodId(goodId);
	 * request.setUserId(userId); showLoad("正在加载"); CoreHttpClient.listen =
	 * this; CoreHttpClient.get(this,
	 * Constants.REQUEST_TYPE.QUERY_DETAIL_BY_GOOD_ID, request.toString());
	 * Log.e("tag", "商品详情接口..." + Constants.BASE_URL + request.toString()); }
	 * 
	 * @Override public void
	 * queryDetailByGoodIdSuccess(QueryDetailByGoodIdItemsBean bean) {
	 * dismiss(); goodBean = bean; if (bean != null) { if
	 * (bean.getGoodImageUrlItems() != null &&
	 * bean.getGoodImageUrlItems().size() != 0) { for (int i = 0; i <
	 * bean.getGoodImageUrlItems().size(); i++) {
	 * picList.add(bean.getGoodImageUrlItems().get(i) .getGoodImageUrl()); } }
	 * else { picList.add(""); } mySlideShowView.startPlay();
	 * mySlideShowView.setImagePath(picList);
	 * tvGoodName.setText(bean.getGoodName()); tvSpecialCost.setText("¥ " +
	 * bean.getSpecialCost()); nowPrice = bean.getSpecialCost();
	 * tvLastPrice.setText("¥ " + bean.getCost()); tvFreight.setText("运费: " +
	 * bean.getFreight() + "元"); tvSales.setText("销量: " + bean.getSales()); if
	 * (bean.getGoodColorNames().length() != 0) { strColor =
	 * bean.getGoodColorNames().split(","); } if (strColor != null) {
	 * Log.i("tag", "有颜色数组"); tvGoodColor.setText(strColor[0]); listColor = new
	 * ArrayList<>(); for (int i = 0; i < strColor.length; i++) {
	 * listColor.add(strColor[i]); Log.i("tag", "strColor[" + i + "]=" +
	 * strColor[i]); } goodColor = strColor[0];
	 * SelectTypeFragment.getInstance().adapter.setData(listColor);
	 * SelectTypeFragment.getInstance().adapter.notifyDataSetChanged(); } else {
	 * Log.i("tag", "没有颜色数组"); tvGoodColor.setText(bean.getGoodColorNames()); }
	 * tvCommentCount.setText(bean.getContentCount() + "人评论"); if
	 * (bean.getContentList() != null && bean.getContentList().size() >= 1) {
	 * rlComment.setVisibility(View.VISIBLE); tvCommentPhone.setText(bean
	 * .getContentList() .get(0) .getContentTelephone() .replace(
	 * bean.getContentList().get(0) .getContentTelephone().substring(3, 7),
	 * "****")); tvCommentContent.setText(bean.getContentList().get(0)
	 * .getContent()); tvCommentScore.setText(bean.getContentList().get(0)
	 * .getContentScore() + "分");
	 * tvCommentTime.setText(bean.getContentList().get(0) .getContentTime()); }
	 * else { rlComment.setVisibility(View.GONE); } if
	 * (bean.getGoodImageUrlItems() != null &&
	 * bean.getGoodImageUrlItems().size() != 0) { if (strColor != null &&
	 * strColor.length != 0) { SelectTypeFragment.getInstance().setGoodDetail(
	 * bean.getGoodImageUrlItems().get(0) .getGoodImageUrl(),
	 * bean.getSpecialCost() + "", bean.getUnit(), strColor[0]); } else {
	 * SelectTypeFragment.getInstance().setGoodDetail(
	 * bean.getGoodImageUrlItems().get(0) .getGoodImageUrl(),
	 * bean.getSpecialCost() + "", bean.getUnit(), " "); } } else { if (strColor
	 * != null && strColor.length != 0) {
	 * SelectTypeFragment.getInstance().setGoodDetail("", bean.getSpecialCost()
	 * + "", bean.getUnit(), strColor[0]); } else {
	 * SelectTypeFragment.getInstance().setGoodDetail("", bean.getSpecialCost()
	 * + "", bean.getUnit(), " "); } } }
	 * ProductDetailFragment.getInstance().setDetailImg(
	 * bean.getGoodDetailImageUrl());
	 * ProductParameterFragment.getInstance().setParameterImg(
	 * bean.getGoodCanImageUrl()); PicLagDetailFragment.getInstance().setId(id);
	 * PicLagDetailFragment.getInstance().setCommentNum( bean.getContentCount()
	 * + ""); if (bean.getIsCollection() == 1) { // 收藏 Log.e("tag", "收藏");
	 * isCollect = true; imgCollect.setImageResource(R.drawable.star_click); }
	 * else { // 未收藏 Log.e("tag", "没有收藏"); isCollect = false;
	 * imgCollect.setImageResource(R.drawable.star); } unit = bean.getUnit();
	 * dismiss(); }
	 */
	/*
	 * // 添加收藏接口 public void addXnCollection(String userId, String goodId) {
	 * AddXnCollectionRequest request = new AddXnCollectionRequest();
	 * request.setMethod("addXnCollection"); request.setUserId(userId);
	 * request.setGoodId(goodId); CoreHttpClient.listen = this;
	 * CoreHttpClient.get(this, Constants.REQUEST_TYPE.ADD_XNCOLLECTION,
	 * request.toString()); Log.e("tag", "添加收藏..." + Constants.BASE_URL +
	 * request.toString()); }
	 * 
	 * @Override public void addXnCollectionSuccess(AddXnCollectionBean
	 * addXnCollectionBean) { Log.i("tag", "收藏成功"); showToast("收藏成功"); isCollect
	 * = true; imgCollect.setImageResource(R.drawable.star_click); }
	 */
	/*
	 * // 删除收藏接口 public void delXnCollection(String userId, String goodsId) {
	 * DelXnCollectionRequest request = new DelXnCollectionRequest();
	 * request.setMethod("delXnCollection"); request.setUserId(userId);
	 * request.setGoodsId(goodsId); CoreHttpClient.listen = this;
	 * CoreHttpClient.get(this, Constants.REQUEST_TYPE.DEL_XNCOLLECTION,
	 * request.toString()); Log.e("tag", "取消收藏..." + Constants.BASE_URL +
	 * request.toString()); }
	 * 
	 * @Override public void delXnCollectionSuccess() { Log.i("tag", "取消收藏成功");
	 * showToast("取消收藏成功"); isCollect = false;
	 * imgCollect.setImageResource(R.drawable.star); }
	 */
	/*
	 * // 添加到购物车接口 public void addShoppingCar(String userId, String goodId, int
	 * buyCount, double cost, String goodSize, String goodColor) {
	 * AddShoppingCarRequest request = new AddShoppingCarRequest();
	 * request.setMethod("addShoppingCar"); request.setUserId(userId);
	 * request.setGoodId(goodId); request.setBuyCount(buyCount);
	 * request.setCost(cost); request.setGoodColor(goodColor);
	 * request.setGoodSize(goodSize); request.setGoodType("1");
	 * CoreHttpClient.listen = this; CoreHttpClient.get(this,
	 * Constants.REQUEST_TYPE.ADD_SHOPPING_CAR, request.toString());
	 * Log.e("tag", "添加购物车接口..." + Constants.BASE_URL + request.toString()); }
	 */

	/*
	 * @Override public void addShoppingCarSuccess() { showToast("已添加至购物车");
	 * Log.i("tag", "添加购物车成功"); }
	 */

	/*
	 * // 购物车提交接口 public void addShoppingCarToOrder(String userId,double
	 * cost,String goodId,String type,String buyCount, String goodColor){
	 * AddShoppingCarToOrderRequest request=new AddShoppingCarToOrderRequest();
	 * request.setMethod("AddShoppingCarToOrder"); request.setUserId(userId);
	 * request.setCost(cost); request.setGoodId(goodId); request.setType(type);
	 * request.setBuyCount(buyCount); request.setGoodColor(goodColor);
	 * request.setCarId("-1"); showLoad("正在加载"); CoreHttpClient.listen=this;
	 * CoreHttpClient.get(this,
	 * Constants.REQUEST_TYPE.ADD_SHOPPING_CAR_TO_ORDER, request.toString());
	 * Log.e("tag", "购物车提交接口..."+Constants.BASE_URL+request.toString()); }
	 * 
	 * @Override public void addShoppingCarToOrderSuccess(
	 * AddShoppingCarToOrderBean bean) { dismiss(); Log.i("tag",
	 * "购物车提交接口success"); Intent intent=new Intent(GoodsDetailActivity.this,
	 * CompleteOrderActivity.class); Bundle bundle = new Bundle();
	 * bundle.putSerializable("orderBean", bean); bundle.putString("goodSize",
	 * goodSize + unit); intent.putExtras(bundle); startActivity(intent); }
	 */

	/*
	 * @Override public void SendMessageValue(List<String> addressList) {
	 * tv_address.setText(StringUtil.listToString(addressList).replace(",",
	 * " ")); }
	 */

	/*
	 * // 购物车提交接口 public void addShoppingCarToOrder(String userId,double
	 * cost,String goodId,String type,String buyCount, String goodColor){
	 * AddShoppingCarToOrderRequest request=new AddShoppingCarToOrderRequest();
	 * request.setMethod("AddShoppingCarToOrder"); request.setUserId(userId);
	 * request.setCost(cost); request.setGoodId(goodId); request.setType(type);
	 * request.setBuyCount(buyCount); request.setGoodColor(goodColor);
	 * request.setCarId("-1"); showLoad("正在加载"); CoreHttpClient.listen=this;
	 * CoreHttpClient.get(this,
	 * Constants.REQUEST_TYPE.ADD_SHOPPING_CAR_TO_ORDER, request.toString());
	 * Log.e("tag", "购物车提交接口..."+Constants.BASE_URL+request.toString()); }
	 * 
	 * @Override public void addShoppingCarToOrderSuccess(
	 * AddShoppingCarToOrderBean bean) { dismiss(); Log.i("tag",
	 * "购物车提交接口success"); Intent intent=new Intent(GoodsDetailActivity.this,
	 * CompleteOrderActivity.class); Bundle bundle = new Bundle();
	 * bundle.putSerializable("orderBean", bean); bundle.putString("goodSize",
	 * goodSize + unit); intent.putExtras(bundle); startActivity(intent); }
	 */

	/*
	 * // 添加到购物车接口 public void buyButton(String userId, String goodId, String
	 * carId, String type, String goodColor, double cost, String buyCount) {
	 * BuyButtonRequest request = new BuyButtonRequest();
	 * request.setMethod("buyButton"); request.setUserId(userId);
	 * request.setGoodId(goodId); request.setCarId(carId);
	 * request.setType(type); request.setGoodColor(goodColor);
	 * request.setCost(cost + ""); request.setBuyCount(buyCount);
	 * CoreHttpClient.listen = this; CoreHttpClient.get(this,
	 * Constants.REQUEST_TYPE.BUY_BUTTON, request.toString()); Log.i("URL",
	 * "保存列表..." + Constants.BASE_URL + request.toString()); }
	 * 
	 * @Override public void buyButtonSuccess() { super.buyButtonSuccess();
	 * Intent intent = new Intent(GoodsDetailActivity.this,
	 * CompleteOrderActivity.class); Bundle bundle = new Bundle();
	 * bundle.putSerializable("orderBean", goodBean);
	 * bundle.putString("goodSize", goodSize + unit);
	 * bundle.putString("goodColor", goodColor); bundle.putString("goodId", id);
	 * bundle.putDouble("cost", cost); bundle.putString("count", "1");
	 * intent.putExtras(bundle); startActivity(intent); }
	 * 
	 * @Override public void buyButtonFailed() { super.buyButtonFailed();
	 * Log.i("msg", "buyButtonFailed"); }
	 */

}
