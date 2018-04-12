package com.sinia.orderlang.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.adapter.CommentAdapter;
import com.sinia.orderlang.adapter.MyViewPagerAdapter;
import com.sinia.orderlang.bean.CommentItemsBean;
import com.sinia.orderlang.bean.TeJiaDetailBean;

/**
 * 评论详情
 */
public class CommentDetailActivity extends BaseActivity implements
		OnClickListener {
	private LinearLayout ll_back;
	private ViewPager viewpager;
	private ImageView iv_red_line, iv_collect;
	private TextView tv_all, tv_good, tv_zhong, tv_bad;
	private CommentAdapter commentAdapter1, commentAdapter2, commentAdapter3,
			commentAdapter4;
	private ListView listview1, listview2, listview3, listview4;
	private MyViewPagerAdapter cViewPagerAdapter;
	private boolean isCollectClick;
	/** 偏移量 */
	private int offset;
	private int index = 0;
	private TeJiaDetailBean teJiaDetailBean;
	// private List<QueryCommentByGoodIdItemsBean> listGood=new
	// ArrayList<QueryCommentByGoodIdItemsBean>();
	// private List<QueryCommentByGoodIdItemsBean> listMiddle=new
	// ArrayList<QueryCommentByGoodIdItemsBean>();
	// private List<QueryCommentByGoodIdItemsBean> listBad=new
	// ArrayList<QueryCommentByGoodIdItemsBean>();
	private int commentType = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_detailss);
		getHeadParentView().setVisibility(View.GONE);
		getHengxian().setVisibility(View.GONE);
		initView();
		initImage();
		initViewPager();
		teJiaDetailBean = (TeJiaDetailBean) getIntent().getSerializableExtra("TeJiaDetailBean");
		initData();
	}

	private void initView() {
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		tv_all = (TextView) findViewById(R.id.tv_all);
		tv_good = (TextView) findViewById(R.id.tv_good);
		tv_zhong = (TextView) findViewById(R.id.tv_zhong);
		tv_bad = (TextView) findViewById(R.id.tv_bad);
		iv_red_line = (ImageView) findViewById(R.id.iv_red_line);
		iv_collect = (ImageView) findViewById(R.id.iv_collect);
		ll_back = (LinearLayout) findViewById(R.id.ll_back);
		ll_back.setOnClickListener(this);
		iv_collect.setOnClickListener(this);
		tv_all.setOnClickListener(this);
		tv_good.setOnClickListener(this);
		tv_zhong.setOnClickListener(this);
		tv_bad.setOnClickListener(this);
	}

	private void initViewPager() {
		commentAdapter1 = new CommentAdapter(this);
		commentAdapter2 = new CommentAdapter(this);
		commentAdapter3 = new CommentAdapter(this);
		commentAdapter4 = new CommentAdapter(this);
		List<View> views = new ArrayList<View>();
		View view1 = LayoutInflater.from(this).inflate(R.layout.view_comment,
				null);
		listview1 = (ListView) view1.findViewById(R.id.listview);
		View view2 = LayoutInflater.from(this).inflate(R.layout.view_comment,
				null);
		listview2 = (ListView) view2.findViewById(R.id.listview);
		View view3 = LayoutInflater.from(this).inflate(R.layout.view_comment,
				null);
		listview3 = (ListView) view3.findViewById(R.id.listview);
		View view4 = LayoutInflater.from(this).inflate(R.layout.view_comment,
				null);
		listview4 = (ListView) view4.findViewById(R.id.listview);
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		listview1.setAdapter(commentAdapter1);
		listview2.setAdapter(commentAdapter2);
		listview3.setAdapter(commentAdapter3);
		listview4.setAdapter(commentAdapter4);
		cViewPagerAdapter = new MyViewPagerAdapter(views);
		viewpager.setAdapter(cViewPagerAdapter);
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				TranslateAnimation animation = new TranslateAnimation(offset
						* index, offset * arg0, 0, 0);

				animation.setFillAfter(true);
				animation.setDuration(300);
				iv_red_line.startAnimation(animation);
				index = arg0;
				changeTextColor(index);
				switch (arg0) {
				case 0:
					commentType = 1;
					break;
				case 1:
					commentType = 2;
					break;
				case 2:
					commentType = 3;
					break;
				case 3:
					commentType = 4;
					break;

				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	public void initImage() {
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);

		int windowWidth = dm.widthPixels;// 获得屏 final PullToRefreshView
											// ptrv幕分辨率宽度
		offset = (windowWidth / 4);// image设置在windowWidth长度
		LayoutParams lp = iv_red_line.getLayoutParams();
		lp.width = offset;
		iv_red_line.setLayoutParams(lp);
	}

	private void initData(){
		List<CommentItemsBean> list1 = teJiaDetailBean.getCommentItems();
		commentAdapter1.data.clear();
		commentAdapter2.data.clear();
		commentAdapter3.data.clear();
		commentAdapter4.data.clear();
		commentAdapter1.data.addAll(list1);
		commentAdapter1.notifyDataSetChanged();
		
		for(int i = 0 ;i<list1.size();i++){
			CommentItemsBean bean = list1.get(i);
			int star = Integer.parseInt(bean.getStar());
			if(star<2){
				commentAdapter4.data.add(bean);
			}else if(star>=2&&star<4){
				commentAdapter3.data.add(bean);
			}else if(star>=4&&star<=5){
				commentAdapter2.data.add(bean);
			}
		}
		commentAdapter2.notifyDataSetChanged();
		commentAdapter3.notifyDataSetChanged();
		commentAdapter4.notifyDataSetChanged();
		
		tv_all.setText("全部("+commentAdapter1.data.size()+")");
		tv_good.setText("好评("+commentAdapter2.data.size()+")");
		tv_zhong.setText("中评("+commentAdapter3.data.size()+")");
		tv_bad.setText("差评("+commentAdapter4.data.size()+")");
	}
	
	private void changeTextColor(int index) {
		switch (index) {
		case 0:
			tv_all.setTextColor(Color.parseColor("#CD313B"));
			tv_good.setTextColor(Color.parseColor("#575757"));
			tv_zhong.setTextColor(Color.parseColor("#575757"));
			tv_bad.setTextColor(Color.parseColor("#575757"));
			break;
		case 1:
			tv_all.setTextColor(Color.parseColor("#575757"));
			tv_good.setTextColor(Color.parseColor("#CD313B"));
			tv_zhong.setTextColor(Color.parseColor("#575757"));
			tv_bad.setTextColor(Color.parseColor("#575757"));
			break;
		case 2:
			tv_all.setTextColor(Color.parseColor("#575757"));
			tv_good.setTextColor(Color.parseColor("#575757"));
			tv_zhong.setTextColor(Color.parseColor("#CD313B"));
			tv_bad.setTextColor(Color.parseColor("#575757"));
			break;
		case 3:
			tv_all.setTextColor(Color.parseColor("#575757"));
			tv_good.setTextColor(Color.parseColor("#575757"));
			tv_zhong.setTextColor(Color.parseColor("#575757"));
			tv_bad.setTextColor(Color.parseColor("#CD313B"));
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.iv_collect:
			if (isCollectClick) {
				iv_collect.setImageResource(R.drawable.star);
				isCollectClick = false;
			} else {
				iv_collect.setImageResource(R.drawable.star_click);
				isCollectClick = true;
			}
			break;
		case R.id.ll_back:
			finish();
			break;
		case R.id.tv_all:
			changeTextColor(0);
			viewpager.setCurrentItem(0);
			break;
		case R.id.tv_good:
			changeTextColor(1);
			viewpager.setCurrentItem(1);
			break;
		case R.id.tv_zhong:
			changeTextColor(2);
			viewpager.setCurrentItem(2);
			break;
		case R.id.tv_bad:
			changeTextColor(3);
			viewpager.setCurrentItem(3);
			break;
		default:
			break;
		}
	}
}
