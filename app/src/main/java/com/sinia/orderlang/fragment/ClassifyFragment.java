package com.sinia.orderlang.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.activity.SeckillMainActivity;
import com.sinia.orderlang.activity.ShowDialogActivity;
import com.sinia.orderlang.activity.SpecialPriceMainActivity;
import com.sinia.orderlang.adapter.ClassifyAdapter;
import com.sinia.orderlang.bean.ArrayXnGoodTypeItemsBean;

public class ClassifyFragment extends BaseFragment {
	private View rootView;
	private TextView tv_cancle;
	private ListView listview;
	private ClassifyAdapter adapter;
	private ShowDialogActivity showDialogActivity;
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
		showDialogActivity = (ShowDialogActivity) getActivity();
		rootView = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_classify, null);
		tv_cancle = (TextView) rootView.findViewById(R.id.tv_cancle);
		tv_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialogActivity.handler.sendEmptyMessage(100);
			}
		});
		listview = (ListView) rootView.findViewById(R.id.listview);
		adapter = new ClassifyAdapter(getActivity());
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.check = position;
				adapter.notifyDataSetChanged();
				Log.e("woyaokk","showDialogActivity.secOrSpec:"+showDialogActivity.secOrSpec);
				if(showDialogActivity.secOrSpec==1){
					if(position == 0){
						Log.e("woyaokk","secOrSpec.");
						SeckillMainActivity.goodType = "-1";
						SeckillMainActivity.goodName = "-1";
					}else{
						Log.e("woyaokk","showDialogActivity.");
						SeckillMainActivity.goodType = (position)+"";
					}
				}else if(showDialogActivity.secOrSpec==2){
					if(position == 0){
						SpecialPriceMainActivity.goodType = "-1";
						SpecialPriceMainActivity.goodName = "-1";
					}else{
						SpecialPriceMainActivity.goodType = (position)+"";
					}
				}
				
				
				showDialogActivity.handler.sendEmptyMessage(100);
			}
		});
		if(showDialogActivity.secOrSpec==1){
			adapter.data.clear();
			ArrayXnGoodTypeItemsBean bean = new ArrayXnGoodTypeItemsBean();
			bean.setTypeName("全部");
			adapter.data.add(bean);
			if(null!=SeckillMainActivity.redpacketBean){
				adapter.data.addAll(SeckillMainActivity.redpacketBean.getArrayXnGoodTypeItems());
			}
			adapter.notifyDataSetChanged();
		}else if(showDialogActivity.secOrSpec==2){
			adapter.data.clear();
			ArrayXnGoodTypeItemsBean bean = new ArrayXnGoodTypeItemsBean();
			bean.setTypeName("全部");
			adapter.data.add(bean);
			if(null!=SpecialPriceMainActivity.teJiaListList){
				adapter.data.addAll(SpecialPriceMainActivity.teJiaListList.getArrayXnGoodTypeItems());
			}
			adapter.notifyDataSetChanged();
		}
	}
}
