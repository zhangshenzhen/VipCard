package com.sinia.orderlang.activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.request.AddAddressRequest;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.DialogUtils;
import com.sinia.orderlang.utils.MyApplication;
import com.sinia.orderlang.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 添加地址
 */
public class AddAddressActivity extends BaseActivity {

	private TextView tv_add, tv_region;
	private EditText name, phone, et_detail;

	private ArrayAdapter<String> mProvinceAdapter;//省份数据适配器
	private ArrayAdapter<String> mCityAdapter;//城市数据适配器
	private ArrayAdapter<String> mAreaAdapter;//省份数据适配器

	private String mCurrentProviceName;//当前省的名称
	private String mCurrentAreaName ="";//当前区的名称
	private String mCurrentCityName = "";//当前城市名称

	private Spinner mSpinnerProvice,mSpinnerCity,mSpinnerCounty;
	private JSONObject mJsonObj;//把全国的省市区的信息以json的格式保存，解析完成后赋值为null
	private String[] mProvinceDatas;//所有省

	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();//key - 省 value - 市s
	private Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();//key - 市 values - 区s


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_addressss, "添加地址");
		((TextView) getDoingView()).setText("保存");
		initView();
	}

	private void initView() {

		initJsonData();
		initDatas();
		mSpinnerProvice = (Spinner) findViewById(R.id.spinner_province);
		mSpinnerCity = (Spinner) findViewById(R.id.spinner_city);
		mSpinnerCounty = (Spinner) findViewById(R.id.spinner_county);

//		tv_region = (TextView) findViewById(R.id.tv_region);
		name = (EditText) findViewById(R.id.name);
		phone = (EditText) findViewById(R.id.phone);
		et_detail = (EditText) findViewById(R.id.et_detail);
//		tv_region.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				DialogUtils.createAddressDialog(AddAddressActivity.this,
//						tv_region);
//			}
//		});
	}

	@Override
	public void doing() {
		super.doing();
		if (!StringUtil.isEmpty(name.getText().toString().trim())) {
			if (!StringUtil.isEmpty(phone.getText().toString().trim())) {
				if (!StringUtil.isEmpty(tv_region.getText().toString().trim())) {
					if (!StringUtil.isEmpty(et_detail.getText().toString()
							.trim())) {
						addAddress();
					} else {
						showToast("请输入详细地址");
					}
				} else {
					showToast("请选择区域");
				}
			} else {
				showToast("请输入手机号");
			}
		} else {
			showToast("请输入收货人");
		}
	}


	/**
	 * 解析整个Json对象，完成后释放Json对象的内存
	 */
	private void initDatas()
	{
		try
		{
			JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
			mProvinceDatas = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
				String province = jsonP.getString("p");// 省名字

				mProvinceDatas[i] = province;

				JSONArray jsonCs = null;
				try
				{
					/**
					 * Throws JSONException if the mapping doesn't exist or is
					 * not a JSONArray.
					 */
					jsonCs = jsonP.getJSONArray("c");
				} catch (Exception e1)
				{
					continue;
				}
				String[] mCitiesDatas = new String[jsonCs.length()];
				for (int j = 0; j < jsonCs.length(); j++)
				{
					JSONObject jsonCity = jsonCs.getJSONObject(j);
					String city = jsonCity.getString("n");// 市名字
					mCitiesDatas[j] = city;
					JSONArray jsonAreas = null;
					try
					{
						/**
						 * Throws JSONException if the mapping doesn't exist or
						 * is not a JSONArray.
						 */
						jsonAreas = jsonCity.getJSONArray("a");
					} catch (Exception e)
					{
						continue;
					}

					String[] mAreasDatas = new String[jsonAreas.length()];// 当前市的所有区
					for (int k = 0; k < jsonAreas.length(); k++)
					{
						String area = jsonAreas.getJSONObject(k).getString("s");// 区域的名称
						mAreasDatas[k] = area;
					}
					mAreaDatasMap.put(city, mAreasDatas);
				}

				mCitisDatasMap.put(province, mCitiesDatas);
			}

		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		mJsonObj = null;
	}


	/**
	 * 从assert文件夹中读取省市区的json文件，然后转化为json对象
	 */
	private void initJsonData()
	{
		try
		{
			StringBuffer sb = new StringBuffer();
			InputStream is = getAssets().open("city.json");
			int len = -1;
			byte[] buf = new byte[is.available()];
			while ((len = is.read(buf)) != -1)
			{
				sb.append(new String(buf, 0, len, "gb2312"));
			}
			is.close();
			mJsonObj = new JSONObject(sb.toString());
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
	}


	private void addAddress() {
		showLoad("");
		AddAddressRequest request = new AddAddressRequest();
		request.setMethod("addAddress");
		request.setUserId(getFromSharePreference(Config.userConfig.pkregister));
		request.setAddCusName(name.getEditableText().toString());
		request.setAddCusAddress(mCurrentProviceName+mCurrentCityName+mCurrentAreaName+et_detail.getEditableText().toString());
		String t = tv_region.getText().toString();
		try {
			t = URLEncoder.encode(tv_region.getText().toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAddCusArea(t);
		request.setAddCusTelephone(phone.getEditableText().toString());
		logUrl(request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.ADD_ADDRESS,
				request.toString());
	}

	@Override
	public void requestSuccess() {
		// TODO Auto-generated method stub
		super.requestSuccess();
		dismiss();
		showToast("添加成功");
		finish();
	}

	@Override
	public void requestFailed() {
		// TODO Auto-generated method stub
		super.requestFailed();
		dismiss();
		showToast("添加失败");
	}

	// @Override
	// public void addXnAddressSuccess() {

	// }

	// @Override
	// public void addXnAddressFailed() {
	// showToast("添加失败");
	// }
}
