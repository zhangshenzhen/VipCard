package com.sinia.orderlang.activity;

import java.util.Arrays;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.adapter.HistorySearchAdapter;

/**
 * 搜索页面
 */
public class Search1Activity extends BaseActivity implements OnClickListener {
	private ListView lv_history_search;
	private HistorySearchAdapter adapter;
	private EditText et_search;
	private TextView tv_empty;
	private String[] history_arr = new String[] {};
	/** 
	 * 从秒杀还是特价跳转过来的
	 * 1秒杀 2特价
	 *  */
	public int secOrSpec;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search1);
		getHeadParentView().setVisibility(View.GONE);
		getHengxian().setVisibility(View.GONE);
		secOrSpec = getIntent().getIntExtra("secOrSpec", 0);
		
		initView();
		getSearchHistory();
	}

	private void getSearchHistory() {
		SharedPreferences sp = getSharedPreferences("search_history", 0);
		String history = sp.getString("history", "");
		// 用逗号分割内容返回数组
		history_arr = history.split(",");
		if (history_arr[0].equals("")) {
			history_arr = new String[] {};
		}
		adapter = new HistorySearchAdapter(this, Arrays.asList(history_arr));
		tv_empty = (TextView) findViewById(R.id.tv_empty);
		// 保留前20条数据
		if (history_arr.length > 20) {
			String[] newArrays = new String[20];
			// 实现数组之间的复制
			System.arraycopy(history_arr, 0, newArrays, 0, 20);
			adapter = new HistorySearchAdapter(this, Arrays.asList(newArrays));
		}
		lv_history_search.setEmptyView(tv_empty);
		lv_history_search.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getSearchHistory();
	}

	private void initView() {
		lv_history_search = (ListView) findViewById(R.id.lv_history_search);
		et_search = (EditText) findViewById(R.id.et_search);
		et_search.setFocusable(true);
		et_search.setFocusableInTouchMode(true);
		et_search.requestFocus();
		lv_history_search.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(secOrSpec == 1){
					SeckillMainActivity.goodName = adapter.list.get(arg2);
				}else if(secOrSpec == 2){
					SpecialPriceMainActivity.goodName = adapter.list.get(arg2);
				}
				finish();
			}
		});
		findViewById(R.id.ll_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		et_search.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					if (!et_search.getEditableText().toString().trim()
							.equals("")) {
						saveSearch(et_search.getEditableText().toString()
								.trim());
						if(secOrSpec == 1){
							SeckillMainActivity.goodName = et_search.getEditableText().toString();
						}else if(secOrSpec == 2){
							SpecialPriceMainActivity.goodName = et_search.getEditableText().toString();
						}
					} else {
						if(secOrSpec == 1){
							SeckillMainActivity.goodName = "-1";
						}else if(secOrSpec == 2){
							SpecialPriceMainActivity.goodName = "-1";
						}
					}
					finish();
					return true;
				}
				return false;
			}
		});
	}

	protected void saveSearch(String s) {
		SharedPreferences mysp = getSharedPreferences("search_history", 0);
		String old_text = mysp.getString("history", "");
		// 利用StringBuilder.append新增内容，逗号便于读取内容时用逗号拆分开
		StringBuilder sb = new StringBuilder(old_text);
		sb.append(s + ",");
		// 判断搜索内容是否已经存在于历史文件，已存在则不重复添加
		if (!old_text.contains(s + ",")) {
			SharedPreferences.Editor myeditor = mysp.edit();
			myeditor.putString("history", sb.toString());
			myeditor.commit();
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		}
	}

}
