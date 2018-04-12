package com.sinia.orderlang.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.activity.GoodsDetailActivity;
import com.sinia.orderlang.utils.BitmapUtilsHelp;

public class ProductParameterFragment extends BaseFragment {
	
	private static ProductParameterFragment instance;
	private View rootView;
	private Activity activity;
	private LayoutInflater inflater;
	public static ImageView imgParameter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
	}

	public static ProductParameterFragment getInstance() {
		if (null == instance) {
			instance = new ProductParameterFragment();
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

	private void initView() {
		activity = getActivity();
		inflater = LayoutInflater.from(activity);
		rootView = inflater.inflate(R.layout.fragment_product_parameter, null);
		imgParameter=(ImageView) rootView.findViewById(R.id.img_detail);
	}
	
	public void setParameterImg(String path){
		BitmapUtilsHelp.getImage(getActivity(),R.drawable.default_img).display(imgParameter, path);
	}
}
