package com.sinia.orderlang.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.bean.ImageUrlItemsBean;
import com.sinia.orderlang.bean.RedBaoDetailBean;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.request.AddCollectionRequest;
import com.sinia.orderlang.request.DelayCollectionRequest;
import com.sinia.orderlang.request.RedBaoDetailRequest;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.views.MyScrollView;
import com.sinia.orderlang.views.MyScrollView.OnScrollListener;
import com.sinia.orderlang.views.SlideShowView;

/**
 * 秒杀区详情
 */
public class SeckillDetailActivity extends BaseActivity implements
		OnScrollListener, OnClickListener {
	private List<String> picList = new ArrayList<String>();
	private SlideShowView slideshowview;
	private MyScrollView myScrollView;
	private LinearLayout mBuyLayout, ll_lianxi;
	private LinearLayout mTopBuyLayout, ll_h, ll1, ll2, ll3, ll4;
	private View view, hengxian;
	private TextView tv_name, tv_youhui, tv_price, tv_qiang, tv_price_door,
			tv_good_name, tv_address, tv_expiry_date, tv_use_time, tv_use_info,
			tv_end_time, tv_price1, tv_price2, tv_price3, tv_price4, tv_count1,
			tv_count2, tv_count3, tv_count4;
	private int m_width;
	private RelativeLayout rl_head, rl_collect;
	private ImageView iv_collect, iv_tuoyuan, iv_back, iv_share;
	private String goodId = "";
	private boolean not_collect = true;
	private String merchantPhone = "";
	private TextView tv_buy1, tv_buy2;
	private RedBaoDetailBean detailBean;
	private int fflag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seckill_detail);
		getHeadParentView().setVisibility(View.GONE);
		getHengxian().setVisibility(View.GONE);
		initView();
		goodId = getIntent().getStringExtra("goodId");
		getRedBaoDetail(goodId, "1");
		SeckillMainActivity.flag_area = "2";
	}

	// 获取红包详情
	private void getRedBaoDetail(String goodId, String userId) {
		showLoad("");
		RedBaoDetailRequest request = new RedBaoDetailRequest();
		request.setMethod("redBaoDetail");
		request.setGoodId(goodId);
		request.setUserId(userId);
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.REDBAODETAIL,
				request.toString());
	}

	@Override
	public void getRedBaoDetailSuccess(RedBaoDetailBean bean) {
		// TODO Auto-generated method stub
		super.getRedBaoDetailSuccess(bean);
		dismiss();
		detailBean = bean;
		merchantPhone = bean.getMerchantPhone();
		if (bean.getCollectionStauts() == 1) {
			not_collect = false;
			iv_collect.setImageResource(R.drawable.star_click);
		}
		picList.clear();
		List<ImageUrlItemsBean> list = bean.getImageUrlItems();
		for (int i = 0; i < list.size(); i++) {
			picList.add(list.get(i).getImageUrl());
		}
		slideshowview.setImagePath(picList);
		slideshowview.startPlay();
		tv_address.setText(bean.getAddress_kill());
		tv_price.setText("秒杀价 ¥ " + bean.getNowPrice());
		tv_price_door.setText("门店价：" + bean.getBeforePrice());
		tv_name.setText(bean.getGoodName() + " " + bean.getFuGoodName());
		tv_youhui.setText(bean.getTitle() + "（" + bean.getFuTitle() + "）");
		if(bean.getBuyNum().equals("")){
			tv_qiang.setText("已有0人抢购");
		}else{
			tv_qiang.setText("已有" + bean.getBuyNum() + "人抢购");
		}
		tv_use_info.setText(bean.getContent());
		tv_price1.setText("¥ " + bean.getNowPrice());
		tv_price3.setText("¥ " + bean.getNowPrice());
		tv_price2.setText("门店价：" + bean.getBeforePrice());
		tv_price4.setText("门店价：" + bean.getBeforePrice());

		tv_count1.setText("剩余数量：" + bean.getHasNum());
		tv_count3.setText("剩余数量：" + bean.getHasNum());
		tv_count2.setText("截止日期：" + bean.getEndTime());
		tv_count4.setText("截止日期：" + bean.getEndTime());

		tv_expiry_date.setText(bean.getStartTime() + "至" + bean.getEndTime());
		tv_use_time.setText(bean.getUseDayBeginTime() + "-"
				+ bean.getUseDayEndTime());
		tv_good_name.setText(bean.getGoodName() + " " + bean.getFuGoodName());
	}

	@Override
	public void requestFailed() {
		// TODO Auto-generated method stub
		super.requestFailed();
		dismiss();
		showToast("加载失败");
	}

	private void initView() {
		rl_head = (RelativeLayout) findViewById(R.id.rl_head1);
		rl_collect = (RelativeLayout) findViewById(R.id.rl_collect);
		iv_collect = (ImageView) findViewById(R.id.iv_collect);
		iv_tuoyuan = (ImageView) findViewById(R.id.iv_tuoyuan);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_collect.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		view = findViewById(R.id.view);
		hengxian = findViewById(R.id.hengxian);
		slideshowview = (SlideShowView) findViewById(R.id.slideshowview);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_youhui = (TextView) findViewById(R.id.tv_youhui);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_qiang = (TextView) findViewById(R.id.tv_qiang);
		tv_price_door = (TextView) findViewById(R.id.tv_price_door);
		tv_good_name = (TextView) findViewById(R.id.tv_good_name);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_expiry_date = (TextView) findViewById(R.id.tv_expiry_date);
		tv_use_time = (TextView) findViewById(R.id.tv_use_time);
		tv_use_info = (TextView) findViewById(R.id.tv_use_info);
		tv_end_time = (TextView) findViewById(R.id.tv_end_time);
		myScrollView = (MyScrollView) findViewById(R.id.scrollView);
		mBuyLayout = (LinearLayout) findViewById(R.id.buy);
		mTopBuyLayout = (LinearLayout) findViewById(R.id.top_buy_layout);
		tv_price1 = (TextView) mBuyLayout.findViewById(R.id.tv_price1);
		tv_price2 = (TextView) mBuyLayout.findViewById(R.id.tv_price2);
		tv_price3 = (TextView) mTopBuyLayout.findViewById(R.id.tv_price1);
		tv_price4 = (TextView) mTopBuyLayout.findViewById(R.id.tv_price2);
		tv_count1 = (TextView) mBuyLayout.findViewById(R.id.tv_count1);
		tv_count2 = (TextView) mBuyLayout.findViewById(R.id.tv_count2);
		tv_count3 = (TextView) mTopBuyLayout.findViewById(R.id.tv_count1);
		tv_count4 = (TextView) mTopBuyLayout.findViewById(R.id.tv_count2);

		tv_buy1 = (TextView) mBuyLayout.findViewById(R.id.tv_buy);
		tv_buy2 = (TextView) mTopBuyLayout.findViewById(R.id.tv_buy);
		tv_buy1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(SeckillDetailActivity.this,
						SeckillOrderSubmitActivity.class);
				it.putExtra("goodname", detailBean.getGoodName());
				it.putExtra("info",
						detailBean.getTitle() + "（" + detailBean.getFuTitle()
								+ "）");
				it.putExtra("price", Float.parseFloat(detailBean.getNowPrice()));
				it.putExtra("goodId", goodId);
				startActivity(it);
				finish();
			}
		});
		tv_buy2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(SeckillDetailActivity.this,
						SeckillOrderSubmitActivity.class);
				it.putExtra("goodname", detailBean.getGoodName());
				it.putExtra("info",
						detailBean.getTitle() + "（" + detailBean.getFuTitle()
								+ "）");
				it.putExtra("price", Float.parseFloat(detailBean.getNowPrice()));
				it.putExtra("goodId", goodId);
				startActivity(it);
				finish();
			}
		});
		ll_lianxi = (LinearLayout) findViewById(R.id.ll_lianxi);
		ll_lianxi.setOnClickListener(this);
		ll_h = (LinearLayout) findViewById(R.id.ll_h);
		ll1 = (LinearLayout) mTopBuyLayout.findViewById(R.id.ll1);
		ll2 = (LinearLayout) mTopBuyLayout.findViewById(R.id.ll2);
		ll3 = (LinearLayout) mBuyLayout.findViewById(R.id.ll1);
		ll4 = (LinearLayout) mBuyLayout.findViewById(R.id.ll2);
		myScrollView.setOnScrollListener(this);

		// 当布局的状态或者控件的可见性发生改变回调的接口,onGlobalLayout回调会在view布局完成时自动调用
//		findViewById(R.id.parent_layout).getViewTreeObserver()
//				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//
//					@Override
//					public void onGlobalLayout() {
//						// 这一步很重要，使得上面的购买布局和下面的购买布局重合
//						onScroll(myScrollView.getScrollY());
//
//						// System.out.println(myScrollView.getScrollY());
//						Log.d("lamp", "myScrollView.getScrollY()="
//								+ myScrollView.getScrollY());
//					}
//				});
		LayoutParams params = slideshowview.getLayoutParams();
		Display display = getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
		params.height = display.getWidth();
		params.width = display.getWidth();
		m_width = display.getWidth();
		slideshowview.setLayoutParams(params);

	}

	private void changeTitleView(int flag) {
		fflag = flag;
		if (flag == 0) {
			iv_back.setImageResource(R.drawable.back3);
			iv_share.setImageResource(R.drawable.share3);
			iv_tuoyuan.setVisibility(View.VISIBLE);
			if (not_collect) {
				iv_collect.setImageResource(R.drawable.star3);
			}
//			ll2.setVisibility(View.GONE);
//			ll1.setVisibility(View.VISIBLE);
//			ll4.setVisibility(View.VISIBLE);
//			ll3.setVisibility(View.GONE);
			tv_price.setVisibility(View.VISIBLE);
			tv_price_door.setVisibility(View.VISIBLE);
			hengxian.setVisibility(View.VISIBLE);
		} else if (flag == 1) {
			iv_back.setImageResource(R.drawable.back2);
			iv_share.setImageResource(R.drawable.share2);
			iv_tuoyuan.setVisibility(View.INVISIBLE);
			if (not_collect) {
				iv_collect.setImageResource(R.drawable.star2);
			}
//			ll1.setVisibility(View.GONE);
//			ll2.setVisibility(View.VISIBLE);
//			ll4.setVisibility(View.VISIBLE);
//			ll3.setVisibility(View.GONE);
			tv_price.setVisibility(View.INVISIBLE);
			tv_price_door.setVisibility(View.INVISIBLE);
			hengxian.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		//这里获得的高度准确
		Rect outRect2 = new Rect();
		this.getWindow().findViewById(Window.ID_ANDROID_CONTENT)
				.getDrawingRect(outRect2);
		setViewHeight(outRect2.height());
		Log.d("lamp", "outRect.height()=" + outRect2.height());
	}

	// 设置第二页高度
	private void setViewHeight(int hh) {
		Display display = getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		rl_head.measure(w, h);
		int height = rl_head.getMeasuredHeight();// 获取头部标题栏高度
		// Log.d("lamp", "rl_head.height="+height);
		mBuyLayout.measure(w, h);
		int height2 = mBuyLayout.getMeasuredHeight();// 获得抢购栏高度
		// Log.d("lamp", "mBuyLayout.height="+height2);
		ll_h.measure(w, h);
		int height3 = ll_h.getMeasuredHeight();
		Log.d("lamp", "ll_h.height=" + height3);
		LayoutParams params2 = ll_h.getLayoutParams();
		params2.width = display.getWidth();
		if (height3 < (int) ((hh - height - height2))) {
			params2.height = (int) ((hh - height - height2));// 第二页内容=应用高度-头部标题栏高度-抢购栏高度
		} else {
			params2.height = height3;
		}
		ll_h.setLayoutParams(params2);
		Log.d("lamp", "ll_h.height=" + (int) ((hh - height - height2) - 0.5));
	}

	@Override
	public void onScroll(int scrollY) {
		if (scrollY > m_width) {
			changeTitleView(1);
			view.setVisibility(View.VISIBLE);
		} else if (scrollY < m_width) {
			view.setVisibility(View.GONE);
			changeTitleView(0);
		}
		int mBuyLayout2ParentTop = Math.max(scrollY, mBuyLayout.getTop());
		mTopBuyLayout.layout(0, mBuyLayout2ParentTop, mTopBuyLayout.getWidth(),
				mBuyLayout2ParentTop + mTopBuyLayout.getHeight());
		Log.d("lamp", "scrollY=" + scrollY);
	}
	
	
	//添加收藏
	private void addCollect() {
		showLoad("");
		AddCollectionRequest request = new AddCollectionRequest();
		request.setMethod("addCollection");
		request.setUserId(Constants.userId);
		request.setGoodId(goodId);
		request.setType("1");
		Log.d("result", Constants.BASE_URL + request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.ADDCOLLECTION,
				request.toString());
	}

	//取消收藏
	private void cancelCollect() {
		showLoad("");
		DelayCollectionRequest request = new DelayCollectionRequest();
		request.setMethod("delayCollection");
		request.setGoodId(goodId);
		request.setUserId(Constants.userId);
		Log.d("result", Constants.BASE_URL + request.toString());
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
		not_collect = false;
		iv_collect.setImageResource(R.drawable.star_click);
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
		if(fflag == 0){
			iv_collect.setImageResource(R.drawable.star3);
		}else if(fflag ==1){
			iv_collect.setImageResource(R.drawable.star2);
		}
		not_collect = true;
	}

	@Override
	public void cancelCollectionFailed() {
		// TODO Auto-generated method stub
		super.cancelCollectionFailed();
		dismiss();
		showToast("取消收藏失败");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.ll_lianxi:
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ merchantPhone));
			startActivity(intent);
			break;
		case R.id.iv_collect:
			if (not_collect) {
				addCollect();
			} else {
				cancelCollect();
			}
			break;
		default:
			break;
		}
	}

}
