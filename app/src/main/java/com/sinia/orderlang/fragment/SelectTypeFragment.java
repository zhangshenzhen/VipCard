package com.sinia.orderlang.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.activity.GoodsDetailActivity;
import com.sinia.orderlang.bean.TeJiaDetailBean;
import com.sinia.orderlang.utils.BitmapUtilsHelp;

public class SelectTypeFragment extends BaseFragment implements OnClickListener {
	private View rootView;
	private ImageView iv_good;
	private TextView tv_price, tv_bianhao, tv_to_cart, tv_to_buy, tv_num;
	private RelativeLayout rl_minus, rl_plus;
	public static int num = 1;
	GoodsDetailActivity activity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
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
		activity = (GoodsDetailActivity) getActivity();
		rootView = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_select_type, null);
		iv_good = (ImageView) rootView.findViewById(R.id.iv_good);
		tv_price = (TextView) rootView.findViewById(R.id.tv_price);
		tv_bianhao = (TextView) rootView.findViewById(R.id.tv_bianhao);
		tv_to_cart = (TextView) rootView.findViewById(R.id.tv_to_cart);
		tv_to_buy = (TextView) rootView.findViewById(R.id.tv_to_buy);
		tv_num = (TextView) rootView.findViewById(R.id.tv_num);
		rl_minus = (RelativeLayout) rootView.findViewById(R.id.rl_minus);
		rl_plus = (RelativeLayout) rootView.findViewById(R.id.rl_plus);
		rl_minus.setOnClickListener(this);
		rl_plus.setOnClickListener(this);
		tv_to_cart.setOnClickListener(this);
		tv_to_buy.setOnClickListener(this);
	}

	public void setData(TeJiaDetailBean teJiaDetailBean){
		BitmapUtilsHelp.getImage(getActivity(), R.drawable.default_img).display(iv_good, teJiaDetailBean.getCurrentImageUrlItems().get(0).getImageUrl());
		tv_price.setText("¥ "+teJiaDetailBean.getNowPrice());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_minus:
			int num1 = Integer.parseInt(tv_num.getText().toString());
			if(num1>0){
				int num2 = num1 - 1;
				tv_num.setText(num2+"");
				num = num2;
				tv_price.setText("¥ "+num*(Float.parseFloat(activity.teJiaDetailBean.getNowPrice())));
			}
			break;
		case R.id.rl_plus:
			int num3 = Integer.parseInt(tv_num.getText().toString());
			int num4 = num3 + 1;
			tv_num.setText(num4+"");
			num = num4;
			tv_price.setText("¥ "+num*(Float.parseFloat(activity.teJiaDetailBean.getNowPrice())));
			break;
		case R.id.tv_to_cart:
			activity.handler.sendEmptyMessage(100);
			activity.handler.sendEmptyMessage(101);
			break;
		case R.id.tv_to_buy:
			activity.handler.sendEmptyMessage(100);
			activity.handler.sendEmptyMessage(102);
			break;
		default:
			break;
		}
	}
}