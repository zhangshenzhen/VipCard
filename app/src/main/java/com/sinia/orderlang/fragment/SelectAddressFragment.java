package com.sinia.orderlang.fragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.activity.SeckillMainActivity;
import com.sinia.orderlang.activity.ShowDialogActivity;
import com.sinia.orderlang.activity.SpecialPriceMainActivity;
import com.sinia.orderlang.utils.ViewHolder;

public class SelectAddressFragment extends BaseFragment implements
		OnClickListener, OnItemClickListener {
	private View rootView;
	private Activity activity;
	private LayoutInflater inflater;
	private ListView select_city_listview;
	private TextView tv_province, tv_city, tv_town, tv_area, tv_area2;
	private Button back_btn;

	private List<String> provinceList = new ArrayList<String>();// 省list
	private List<String> cityList = new ArrayList<String>();// 市list
	private List<String> townList = new ArrayList<String>();// 区list
	private List<String> list = new ArrayList<String>();// listView中显示的数据
	private Map<String, Map<String, List<String>>> provinceMap = null;
	private JSONArray arrayData;
	private MyAdapter adapter;
	private String selectedProvince = "", selectedCity = "", selectedTown = "";
	private int choiceTag = 1;// 1表示当前选择省，2表示当前选择市，3表示当前选则区
	private RelativeLayout rl_back;
	private ShowDialogActivity showDialogActivity;
	private int chooseIndex1 = -1, chooseIndex2 = -1, chooseIndex3 = -1;
	public boolean choose;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		Log.d("lamp", "SelectAddressFragment---init---over");
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
		showDialogActivity = (ShowDialogActivity) getActivity();
		inflater = LayoutInflater.from(activity);
		rootView = inflater.inflate(R.layout.activity_selectcity, null);
		RelativeLayout top = (RelativeLayout) rootView.findViewById(R.id.top);
		select_city_listview = (ListView) rootView
				.findViewById(R.id.select_city_listview);
		tv_province = (TextView) rootView.findViewById(R.id.tv_province);
		tv_city = (TextView) rootView.findViewById(R.id.tv_city);
		tv_town = (TextView) rootView.findViewById(R.id.tv_town);
		back_btn = (Button) rootView.findViewById(R.id.back_btn);
		// updateBg(top);
		// updateBtnBg2(back_btn);
		tv_area = (TextView) rootView.findViewById(R.id.tv_area);
		tv_area2 = (TextView) rootView.findViewById(R.id.tv_area2);
		rl_back = (RelativeLayout) rootView.findViewById(R.id.rl_back);
		rl_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (choiceTag) {
				case 1:
					showDialogActivity.handler.sendEmptyMessage(100);
					break;
				case 2:
					choiceTag = 1;
					selectedProvince = "";
					selectedCity = "";
					selectedTown = "";
					tv_province.setBackgroundColor(Color.parseColor("#D9D9D9"));
					tv_city.setBackgroundColor(Color.parseColor("#FFFFFF"));
					tv_town.setBackgroundColor(Color.parseColor("#FFFFFF"));
					tv_province.setText("省");
					tv_city.setText("市");
					// tv_town.setText("区");
					tv_area.setText("省");
					tv_area2.setText("请选择地区");
					if (provinceList.size() == 0) {
						setProvinceMap();
					}
					list.clear();
					for (int i = 0; i < provinceList.size(); i++) {
						list.add(provinceList.get(i));
					}
					adapter.check = chooseIndex1;
					adapter.notifyDataSetChanged();
					select_city_listview.smoothScrollToPosition(0);
					break;
				case 3:
					if (!selectedProvince.equals("")) {
						selectedCity = "";
						selectedTown = "";
						choiceTag = 2;
						tv_city.setBackgroundColor(Color.parseColor("#D9D9D9"));
						tv_province.setBackgroundColor(Color
								.parseColor("#FFFFFF"));
						tv_town.setBackgroundColor(Color.parseColor("#FFFFFF"));
						tv_city.setText("市");
						tv_area.setText("市");
						tv_area2.setText(selectedProvince);
						setCityList(selectedProvince);
						list.clear();
						for (int i = 0; i < cityList.size(); i++) {
							list.add(cityList.get(i));
						}
						adapter.check = chooseIndex2;
						adapter.notifyDataSetChanged();
						select_city_listview.smoothScrollToPosition(0);
					} else {
						Toast.makeText(getActivity(), "请先选择您所在的省份",
								Toast.LENGTH_SHORT).show();
					}
					break;
				default:
					break;
				}
			}
		});
		setProvinceMap();
		for (int i = 0; i < provinceList.size(); i++) {
			list.add(provinceList.get(i));
		}
		back_btn.setOnClickListener(this);
		tv_city.setOnClickListener(this);
		tv_province.setOnClickListener(this);
		tv_town.setOnClickListener(this);
		tv_province.setBackgroundColor(Color.parseColor("#D9D9D9"));
		tv_city.setBackgroundColor(Color.parseColor("#FFFFFF"));
		tv_town.setBackgroundColor(Color.parseColor("#FFFFFF"));
		select_city_listview.setOnItemClickListener(this);
		adapter = new MyAdapter();
		select_city_listview.setAdapter(adapter);
	}

	private List<String> getIdforAddress(List<String> addressList) {
		List<String> list = new ArrayList<String>();
		if (addressList.size() >= 3) {
			try {
				for (int i = 0; i < arrayData.length(); i++) {
					if (addressList.get(0).equals(
							arrayData.getJSONObject(i).getString("name"))) {
						list.add(arrayData.getJSONObject(i).getString("id"));
						JSONArray cityArray = arrayData.getJSONObject(i)
								.getJSONArray("city");
						for (int j = 0; j < cityArray.length(); j++) {
							if (cityArray.getJSONObject(j).getString("name")
									.equals(addressList.get(1))) {
								list.add(cityArray.getJSONObject(j).getString(
										"id"));
								JSONArray townArray = cityArray
										.getJSONObject(j).getJSONArray("city");
								if (!addressList.get(2).equals("1")
										&& townArray.length() != 0) {
									for (int k = 0; k < townArray.length(); k++) {
										if (townArray.getJSONObject(k)
												.getString("name")
												.equals(addressList.get(2))) {
											list.add(townArray.getJSONObject(k)
													.getString("id"));
											break;
										}
									}
								}
								break;
							}
						}
						break;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 获取assets中address.json文件中所有信息，返回StringBuffer类的数据
	 */
	public StringBuffer getData() {
		StringBuffer json = new StringBuffer();
		try {
			InputStreamReader isr = new InputStreamReader(getResources()
					.getAssets().open("address.json"));
			BufferedReader br = new BufferedReader(isr);
			char[] tempchars = new char[1024];
			int charread = 0;
			while ((charread = br.read(tempchars)) != -1) {
				json.append(tempchars);
			}
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取assets中address.json文件中的省市区中的省份信息 同时也会获取provinceList
	 * 
	 * @return
	 */
	private void setProvinceMap() {
		String json = getData().toString();
		try {
			arrayData = new JSONArray(json);
			provinceMap = new HashMap<String, Map<String, List<String>>>();
			// provinceList.add("");
			for (int i = 0; i < arrayData.length(); i++) {
				JSONObject provinceJson = arrayData.getJSONObject(i);
				String provinceString = provinceJson.getString("name");// province_name
				provinceList.add(provinceString);
				JSONArray cityArray = provinceJson.getJSONArray("city");
				Map<String, List<String>> cityMap = new HashMap<String, List<String>>();
				for (int j = 0; j < cityArray.length(); j++) {
					JSONObject cityJson = cityArray.getJSONObject(j);
					String cityString = cityJson.getString("name");// city_name
					JSONArray townArray = cityJson.getJSONArray("city");
					List<String> townMapList = new ArrayList<String>();// 存放town的list
					for (int k = 0; k < townArray.length(); k++) {
						JSONObject townJson = townArray.getJSONObject(k);
						String townString = townJson.getString("name");// town_name
						townMapList.add(townString);
					}
					cityMap.put(cityString, townMapList);
				}
				provinceMap.put(provinceString, cityMap);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据省的名字获取市的名字
	 * 
	 * @param provinceName
	 * @return
	 */
	private void setCityList(String provinceName) {
		if (provinceMap == null) {
			setProvinceMap();
		}
		Map<String, List<String>> cityMap = provinceMap.get(provinceName);
		Set<Map.Entry<String, List<String>>> set = cityMap.entrySet();
		cityList.clear();
		// cityList.add("");
		for (Map.Entry<String, List<String>> entity : set) {
			String cityName = entity.getKey();
			cityList.add(cityName);
		}
	}

	/**
	 * 根据省和市的名字获取区的名字
	 * 
	 * @param provinceName
	 * @return
	 */
	private void setTownList(String provinceName, String cityName) {
		if (provinceMap == null) {
			setProvinceMap();
		}
		townList.clear();
		List<String> list = provinceMap.get(provinceName).get(cityName);
		// townList.add("");
		for (int i = 0; i < list.size(); i++) {
			townList.add(list.get(i));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		switch (choiceTag) {
		case 1:
			choose = true;
			chooseIndex1 = position;
			chooseIndex2 = -1;
			selectedProvince = provinceList.get(position);
			selectedCity = "";
			selectedTown = "";
			choiceTag = 2;
			tv_province.setText(selectedProvince);
			tv_city.setText("市");
			tv_area.setText("市");
			tv_area2.setText(selectedProvince);
			setCityList(selectedProvince);
			list.clear();
			for (int i = 0; i < cityList.size(); i++) {
				list.add(cityList.get(i));
			}
			adapter.check = chooseIndex2;
			adapter.notifyDataSetChanged();
			select_city_listview.smoothScrollToPosition(0);
			tv_city.setBackgroundColor(Color.parseColor("#D9D9D9"));
			tv_province.setBackgroundColor(Color.parseColor("#FFFFFF"));
			tv_town.setBackgroundColor(Color.parseColor("#FFFFFF"));
			break;
		case 2:
			choose = true;
			chooseIndex2 = position;
			chooseIndex3 = -1;
			selectedCity = cityList.get(position);
			selectedTown = "";
			choiceTag = 3;
			tv_area.setText("区");
			tv_area2.setText(selectedCity);
			tv_city.setText(selectedCity);
			setTownList(selectedProvince, selectedCity);
			if (townList.size() <= 0) {
				List<String> addressList = new ArrayList<String>();
				addressList.add(selectedProvince);
				addressList.add(selectedCity);
				addressList.add("1");
				// sendBack(addressList);
				return;
			}
			list.clear();
			for (int i = 0; i < townList.size(); i++) {
				list.add(townList.get(i));
			}
			adapter.check = chooseIndex3;
			adapter.notifyDataSetChanged();
			select_city_listview.smoothScrollToPosition(0);
			tv_town.setBackgroundColor(Color.parseColor("#D9D9D9"));
			tv_province.setBackgroundColor(Color.parseColor("#FFFFFF"));
			tv_city.setBackgroundColor(Color.parseColor("#FFFFFF"));
			break;
		case 3:
			choose = true;
			chooseIndex3 = position;
			selectedTown = townList.get(position);
			tv_town.setText(selectedTown);



			tv_town.setBackgroundColor(Color.parseColor("#FFFFFF"));
			List<String> addressList = new ArrayList<String>();
			addressList.add(selectedProvince);
			addressList.add(selectedCity);
			addressList.add(selectedTown);
			adapter.check = chooseIndex3;
			adapter.notifyDataSetChanged();
			if (showDialogActivity.secOrSpec == 1) {
				SeckillMainActivity.city = selectedCity;
				SeckillMainActivity.areaName = selectedTown;
				
				Log.d("lamp", "SeckillMainActivity.city=" + SeckillMainActivity.city
						+ ",SeckillMainActivity.areaName=" + SeckillMainActivity.areaName);
			} else if (showDialogActivity.secOrSpec == 2) {
				SpecialPriceMainActivity.city = selectedCity;
				SpecialPriceMainActivity.areaName = selectedTown;
				Log.d("lamp", "SpecialPriceMainActivity.city=" + SpecialPriceMainActivity.city
						+ ",SpecialPriceMainActivity.areaName=" + SpecialPriceMainActivity.areaName);
			}
			Log.e("woyaokk","selectedProvince:"+selectedProvince+"  selectedCity:"+selectedCity+"   selectedTown:"+selectedTown);
			SeckillMainActivity.address = selectedProvince+selectedCity+selectedTown;
			SpecialPriceMainActivity.specialAddess = selectedProvince+selectedCity+selectedTown;
			SeckillMainActivity.flag_area = "1";

			showDialogActivity.handler.sendEmptyMessage(100);
			// callback = (CallBackValue) getActivity();
			// callback.SendMessageValue(addressList);
			break;
		}
	}

	CallBackValue callback;

	/**
	 * 传值给activity 接口
	 */
	public interface CallBackValue {
		public void SendMessageValue(List<String> addressList);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_province:
			choiceTag = 1;
			selectedProvince = "";
			selectedCity = "";
			selectedTown = "";
			tv_province.setBackgroundColor(Color.parseColor("#D9D9D9"));
			tv_city.setBackgroundColor(Color.parseColor("#FFFFFF"));
			tv_town.setBackgroundColor(Color.parseColor("#FFFFFF"));
			tv_province.setText("省");
			tv_city.setText("市");
			// tv_town.setText("区");
			if (provinceList.size() == 0) {
				setProvinceMap();
			}
			list.clear();
			for (int i = 0; i < provinceList.size(); i++) {
				list.add(provinceList.get(i));
			}
			adapter.notifyDataSetChanged();
			select_city_listview.smoothScrollToPosition(0);
			break;
		case R.id.tv_city:
			if (!selectedProvince.equals("")) {
				selectedCity = "";
				selectedTown = "";
				choiceTag = 2;
				tv_city.setBackgroundColor(Color.parseColor("#D9D9D9"));
				tv_province.setBackgroundColor(Color.parseColor("#FFFFFF"));
				tv_town.setBackgroundColor(Color.parseColor("#FFFFFF"));
				tv_city.setText("市");
				setCityList(selectedProvince);
				list.clear();
				for (int i = 0; i < cityList.size(); i++) {
					list.add(cityList.get(i));
				}
				adapter.notifyDataSetChanged();
				select_city_listview.smoothScrollToPosition(0);
			} else {
				Toast.makeText(getActivity(), "请先选择您所在的省份", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.tv_town:
			if (selectedProvince.equals("")) {
				Toast.makeText(getActivity(), "请先选择您所在的省份", Toast.LENGTH_SHORT)
						.show();
			} else if (selectedCity.equals("")) {
				Toast.makeText(getActivity(), "请先选择您所在的城市", Toast.LENGTH_SHORT)
						.show();
			} else {
				choiceTag = 3;
			}
			break;
		case R.id.back_btn:
			// onBackPressed();
			break;

		default:
			// onBackPressed();
			break;
		}
	}

	class MyAdapter extends BaseAdapter {
		public int check = -1;

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.item_classify, null);
			}
			TextView tv_good_name = ViewHolder.get(convertView,
					R.id.tv_good_name);
			ImageView iv_check = ViewHolder.get(convertView, R.id.iv_check);
			tv_good_name.setText(list.get(position));
			if (check == position) {
				iv_check.setVisibility(View.VISIBLE);
			} else {
				iv_check.setVisibility(View.INVISIBLE);
			}
			return convertView;
		}

	}
}
