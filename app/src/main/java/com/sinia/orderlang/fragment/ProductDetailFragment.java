package com.sinia.orderlang.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.activity.GoodsDetailActivity;
import com.sinia.orderlang.utils.BitmapUtilsHelp;

public class ProductDetailFragment extends BaseFragment {
	
	private static ProductDetailFragment instance;
	private View rootView;
	private Activity activity;
	private LayoutInflater inflater;
	public static ImageView imgDetail;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
	}

	public static ProductDetailFragment getInstance() {
		if (null == instance) {
			instance = new ProductDetailFragment();
		}
		return instance;
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

	public void initView() {
		activity = getActivity();
		inflater = LayoutInflater.from(activity);
		rootView = inflater.inflate(R.layout.fragment_product_detail, null);
		imgDetail=(ImageView) rootView.findViewById(R.id.img_detail);
	}
	
	public void setDetailImg(String path){
		BitmapUtilsHelp.getImage(getActivity(),R.drawable.default_img).display(imgDetail, path);
	}
}
