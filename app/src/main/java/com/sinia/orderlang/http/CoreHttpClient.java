package com.sinia.orderlang.http;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sinia.orderlang.bean.AddOrderBean;
import com.sinia.orderlang.bean.AddRedBaoBean;
import com.sinia.orderlang.bean.AddRedBaoExBean;
import com.sinia.orderlang.bean.AddressListList;
import com.sinia.orderlang.bean.CollectionListList;
import com.sinia.orderlang.bean.OrderDetailBean;
import com.sinia.orderlang.bean.OrderGoodList;
import com.sinia.orderlang.bean.OrderManagerList;
import com.sinia.orderlang.bean.PersonalCenterBean;
import com.sinia.orderlang.bean.RedBaoDetailBean;
import com.sinia.orderlang.bean.RedBaoManagerDetailBean;
import com.sinia.orderlang.bean.RedBaoManagerList;
import com.sinia.orderlang.bean.TeJiaDetailBean;
import com.sinia.orderlang.utils.Constants;

public class CoreHttpClient {
	public static AsyncHttpClient client = new AsyncHttpClient();
	public static HttpResponseListener listen;

	public static void get(final Context context, final int requestCode,
			String urlString) {
		try {
			client.get(context, Constants.BASE_URL + urlString,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, JSONObject json) {
							Gson gson = new Gson();
							Log.d("result", json.toString());
							try {
								if (json.has("state")
										&& (json.getInt("state") == Constants.RESPONSE_TYPE.STATUS_SUCCESS)) {
									if (json.has("isSuccessful")
											&& json.getInt("isSuccessful") == Constants.RESPONSE_TYPE.STATUS_SUCCESS) {
										// state=0 isSuccessful=0
										switch (requestCode) {
										case Constants.REQUEST_TYPE.PERSONAL_CENTER:
											PersonalCenterBean personalCenterBean = gson.fromJson(
													json.toString(),
													PersonalCenterBean.class);
											listen.getPersonalCenterSuccess(personalCenterBean);
											break;
										case Constants.REQUEST_TYPE.UPDATE_PER_INFO:
											listen.requestSuccess();
											break;
										case Constants.REQUEST_TYPE.UPDATE_PASSWORD:
											listen.requestSuccess();
											break;
										case Constants.REQUEST_TYPE.ADDADVICE:
											listen.requestSuccess();
											break;
										case Constants.REQUEST_TYPE.REDBAODETAIL:
											RedBaoDetailBean redBaoDetailBean = gson.fromJson(
													json.toString(),
													RedBaoDetailBean.class);
											listen.getRedBaoDetailSuccess(redBaoDetailBean);
											break;
										case Constants.REQUEST_TYPE.ADDCOLLECTION:
											listen.addCollectionSuccess();
											break;
										case Constants.REQUEST_TYPE.ADDREDBAO:
											AddRedBaoBean addRedBaoBean = gson.fromJson(
													json.toString(),
													AddRedBaoBean.class);
											listen.addRedBaoSuccess(addRedBaoBean);
											break;
										case Constants.REQUEST_TYPE.DELAYCOLLECTION:
											listen.cancelCollectionSuccess();
											break;
										case Constants.REQUEST_TYPE.PAYREDBAO:
											listen.requestSuccess();
											break;
										case Constants.REQUEST_TYPE.REDBAOMANAGER:
											RedBaoManagerList list = gson.fromJson(
													json.toString(),
													RedBaoManagerList.class);
											listen.getRedBaoManagerListSuccess(list);
											break;
										case Constants.REQUEST_TYPE.REDBAOMANAGER_DETAIL:
											RedBaoManagerDetailBean redBaoManagerDetailBean = gson.fromJson(
													json.toString(),
													RedBaoManagerDetailBean.class);
											listen.getRedBaoManagerDetailSuccess(redBaoManagerDetailBean);
											break;
										case Constants.REQUEST_TYPE.DELREDBAO:
											listen.delRedBaoSuccess();
											break;
										case Constants.REQUEST_TYPE.COLLECTIONLIST:
											CollectionListList collectionListList = gson.fromJson(
													json.toString(),
													CollectionListList.class);
											listen.getCollectionListSuccess(collectionListList);
											break;
										case Constants.REQUEST_TYPE.TEJIADETAIL:
											TeJiaDetailBean teJiaDetailBean = gson.fromJson(
													json.toString(),
													TeJiaDetailBean.class);
											listen.getTeJiaDetailSuccess(teJiaDetailBean);
											break;
										case Constants.REQUEST_TYPE.ADDSHOPPINGCAR:
											listen.addShopCarSuccess();
											break;
										case Constants.REQUEST_TYPE.ADD_ADDRESS:
											listen.requestSuccess();
											break;
										case Constants.REQUEST_TYPE.ADDRESS_LIST:
											AddressListList addressListList = gson.fromJson(
													json.toString(),
													AddressListList.class);
											listen.getAddressListSuccess(addressListList);
											break;
										case Constants.REQUEST_TYPE.DELADDRESS:
											listen.delAddressSuccess();
											break;
										case Constants.REQUEST_TYPE.ADDRESSSETMOREN:
											listen.setDefaltAddressSuccess();
											break;
										case Constants.REQUEST_TYPE.ADDORDER:
											AddOrderBean addOrderBean = gson
													.fromJson(json.toString(),
															AddOrderBean.class);
											listen.addOrderSuccess(addOrderBean);
											break;
										case Constants.REQUEST_TYPE.PAYORDER:
											listen.requestSuccess();
											break;
										case Constants.REQUEST_TYPE.ORDERMANAGER:
											OrderManagerList orderManagerList = gson.fromJson(
													json.toString(),
													OrderManagerList.class);
											listen.getOrderManagerListSuccess(orderManagerList);
											break;
										case Constants.REQUEST_TYPE.ORDERCOMMENT:
											listen.requestSuccess();
											break;
										case Constants.REQUEST_TYPE.APPLYREFUND:
											listen.requestSuccess();
											break;
										case Constants.REQUEST_TYPE.ORDERDETAIL:
											OrderDetailBean orderDetailBean = gson.fromJson(
													json.toString(),
													OrderDetailBean.class);
											listen.getOrderDetailSuccess(orderDetailBean);
											break;
										case Constants.REQUEST_TYPE.DELCOLLECTION:
											listen.delCollectionSuccess();
											break;
										case Constants.REQUEST_TYPE.ORDERGOOD:
											OrderGoodList orderGoodList = gson.fromJson(json.toString(), OrderGoodList.class);
											listen.getGoodsListSuccess(orderGoodList);
											break;
										default:
											break;
										}
									} else if (json.has("isSuccessful")
											&& json.getInt("isSuccessful") == Constants.RESPONSE_TYPE.STATUS_EXCEPTION) {
										// state=0 isSuccessful=1
										switch (requestCode) {
										case Constants.REQUEST_TYPE.UPDATE_PASSWORD:
											listen.requestFailed();
											break;
										case Constants.REQUEST_TYPE.ADDADVICE:
											listen.requestFailed();
											break;
										case Constants.REQUEST_TYPE.REDBAODETAIL:
											listen.requestFailed();
											break;
										case Constants.REQUEST_TYPE.ADDCOLLECTION:
											listen.addCollectionFailed();
											break;
										case Constants.REQUEST_TYPE.ADDREDBAO:
											AddRedBaoExBean addRedBaoExBean = gson.fromJson(
													json.toString(),
													AddRedBaoExBean.class);
											listen.addRedBaoFailed(addRedBaoExBean);
											break;
										case Constants.REQUEST_TYPE.DELAYCOLLECTION:
											listen.cancelCollectionFailed();
											break;
										case Constants.REQUEST_TYPE.PAYREDBAO:
											listen.requestFailed();
											break;
										case Constants.REQUEST_TYPE.REDBAOMANAGER:
											listen.requestFailed();
											break;
										case Constants.REQUEST_TYPE.REDBAOMANAGER_DETAIL:
											listen.requestFailed();
											break;
										case Constants.REQUEST_TYPE.DELREDBAO:
											listen.requestFailed();
											break;
										case Constants.REQUEST_TYPE.COLLECTIONLIST:
											listen.requestFailed();
											break;
										case Constants.REQUEST_TYPE.TEJIADETAIL:
											listen.requestFailed();
											break;
										case Constants.REQUEST_TYPE.ADDSHOPPINGCAR:
											listen.addShopCarFailed();
											break;
										case Constants.REQUEST_TYPE.ADD_ADDRESS:
											listen.requestFailed();
											break;
										case Constants.REQUEST_TYPE.DELADDRESS:
											listen.delAddressFailed();
											break;
										case Constants.REQUEST_TYPE.ADDRESSSETMOREN:
											listen.setDefaltAddressFailed();
											break;
										case Constants.REQUEST_TYPE.ADDORDER:
											listen.requestFailed();
											break;
										case Constants.REQUEST_TYPE.PAYORDER:
											listen.requestFailed();
											break;
										case Constants.REQUEST_TYPE.ORDERCOMMENT:
											listen.requestFailed();
											break;
										case Constants.REQUEST_TYPE.APPLYREFUND:
											listen.requestFailed();
											break;
										case Constants.REQUEST_TYPE.DELCOLLECTION:
											listen.delCollectionFailed();
											break;
										default:
											break;
										}
									} else if (json.has("isSuccessful")
											&& json.getInt("isSuccessful") == Constants.RESPONSE_TYPE.STATUS_FAILED) {
										// state=0 successful=2
									} else if (json.has("isSuccessful")
											&& json.getInt("isSuccessful") == Constants.RESPONSE_TYPE.STATUS_3) {
										// state=0 successful=3
									} else if (json.has("isSuccessful")
											&& json.getInt("isSuccessful") == 4) {
										// state=0 successful=4
									} else if (json.has("isSuccessful")
											&& json.getInt("isSuccessful") == 5) {
										// state=0 successful=5
									}

								}
								// state=1
								else {
									if (json.has("isSuccessful")
											&& json.getInt("isSuccessful") == Constants.RESPONSE_TYPE.STATUS_SUCCESS) {
									}
								}
							}

							catch (Exception e) {
								e.printStackTrace();
							}

						}

						@Override
						public void onFailure(Throwable arg0) {
							listen.httpRequestFailed();
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void get(final Context context, String urlString,
			final HttpCallBackListener httpCallBackListener) {
		client.get(context, Constants.BASE_URL + urlString,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, JSONObject json) {
						// TODO Auto-generated method stub
						super.onSuccess(arg0, json);
						httpCallBackListener.onSuccess(json);
					}

					@Override
					public void onFailure(Throwable arg0) {
						httpCallBackListener.onRequestFailed();
					}
				});
	}

	public static String getString(JSONObject json, String key) {
		String result = null;
		try {
			if (json.has(key)) {
				result = json.getString(key);
			} else {
				return "";
			}
		} catch (Exception e) {
			result = "";
		}
		return result;
	}

	public int getInt(JSONObject json, String key) {
		int result = 0;
		try {
			if (json.has(key)) {
				result = json.getInt(key);
			} else {
				return 0;
			}
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}

	public double getDouble(JSONObject json, String key) {
		double result = 0;
		try {
			if (json.has(key)) {
				result = json.getDouble(key);
			} else {
				return 0;
			}
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}
}
