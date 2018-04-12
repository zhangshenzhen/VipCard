package com.sinia.orderlang.http;

import org.json.JSONObject;

public interface HttpCallBackListener {

	void onSuccess(JSONObject json);
	void onRequestFailed();

}
