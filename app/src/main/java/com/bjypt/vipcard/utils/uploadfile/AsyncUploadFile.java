package com.bjypt.vipcard.utils.uploadfile;

import android.os.AsyncTask;

import java.util.Map;

public class AsyncUploadFile extends AsyncTask<String, Void, String> {

	private String url;
	private Map<String, String> params;
	private FormFile[] listFile;
	public String backString = "";
	private int requestCode;
	private HttpHandler handler;

	private long starTime;

	public AsyncUploadFile(HttpHandler ctx, String url, Map<String, String> params, FormFile[] listFile) {
		this.handler = ctx;
		this.url = url;
		this.params = params;
		this.listFile = listFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[]) requestCode,
	 * 第一个参数，请求标识符
	 */
	@Override
	protected String doInBackground(String... params) {
		starTime = System.currentTimeMillis();
		if (params[0] != null) {
			requestCode = Integer.parseInt(params[0]);
		}
		this.backString = HttpRequestUtil.uploadFilesHttp(this.url, this.params, this.listFile);
		return this.backString;
	}

	@Override
	protected void onPostExecute(String result) {
		if (handler != null)
			handler.callBackFunction(result, requestCode);
		System.out.println("Url:" + url + " ---------> " + (System.currentTimeMillis() - starTime));
	}

	public interface HttpHandler {
		/*
		 * result 请求的返回结果requestCode 请求标识符
		 */
		public void callBackFunction(String result, int requestCode);
	}

}
