package com.sinia.orderlang.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.activity.CommentDetailActivity;
import com.sinia.orderlang.activity.GoodsDetailActivity;

public class PicLagDetailFragment extends BaseFragment implements
		OnClickListener {

	private static PicLagDetailFragment instance;
	private View rootView;
	private Activity activity;
	private LayoutInflater inflater;
	private FragmentManager fragmentManager;
	private ProductDetailFragment pdFragment;
	private ProductParameterFragment ppFragment;
	private TextView tv_goods_detail, tv_goods_canshu, tv_pinglun_num;
	private LinearLayout ll_pinglun;
	private ImageView iv1, iv2;
	public static String detailPath;
	public static String id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	public static PicLagDetailFragment getInstance() {
		if (null == instance) {
			instance = new PicLagDetailFragment();
		}
		return instance;
	}

	public void setId(String id) {
		PicLagDetailFragment.id = id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeAllViewsInLayout();
		}
		return rootView;
	}

	private void initView() {
		activity = getActivity();
		inflater = LayoutInflater.from(activity);
		rootView = inflater.inflate(R.layout.layout_piclag_detail, null);
		ll_pinglun = (LinearLayout) rootView.findViewById(R.id.ll_pinglun);
		tv_goods_detail = (TextView) rootView
				.findViewById(R.id.tv_goods_detail);
		tv_goods_canshu = (TextView) rootView
				.findViewById(R.id.tv_goods_canshu);
		tv_pinglun_num = (TextView) rootView.findViewById(R.id.tv_pinglun_num);
		tv_goods_detail.setOnClickListener(this);
		tv_goods_canshu.setOnClickListener(this);
		ll_pinglun.setOnClickListener(this);
		if (null == pdFragment) {
			pdFragment = new ProductDetailFragment();
		}
		if (null == ppFragment) {
			ppFragment = new ProductParameterFragment();
		}
		fragmentManager = getActivity().getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.add(R.id.ll_fragment, pdFragment);
		ft.add(R.id.ll_fragment, ppFragment).hide(ppFragment).commit();
		iv1 = (ImageView) rootView.findViewById(R.id.iv1);
		iv2 = (ImageView) rootView.findViewById(R.id.iv2);
	}

	public void setCommentNum(String num) {
		tv_pinglun_num.setText("(" + num + ")");
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_pinglun:
			Intent it2 = new Intent(getActivity(),CommentDetailActivity.class);
			it2.putExtra("TeJiaDetailBean", GoodsDetailActivity.teJiaDetailBean);
			startActivity(it2);
			break;
		case R.id.tv_goods_detail:
			tv_goods_detail.setTextColor(Color.parseColor("#C81320"));
			tv_goods_canshu.setTextColor(Color.parseColor("#000000"));
			iv1.setVisibility(View.VISIBLE);
			iv2.setVisibility(View.INVISIBLE);
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.hide(ppFragment);
			ft.show(pdFragment).commit();
			break;
		case R.id.tv_goods_canshu:
			tv_goods_detail.setTextColor(Color.parseColor("#000000"));
			tv_goods_canshu.setTextColor(Color.parseColor("#C81320"));
			iv2.setVisibility(View.VISIBLE);
			iv1.setVisibility(View.INVISIBLE);
			FragmentTransaction ft1 = fragmentManager.beginTransaction();
			ft1.hide(pdFragment);
			ft1.show(ppFragment).commit();
			break;
		default:
			break;
		}
	}
}
