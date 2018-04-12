package com.sinia.orderlang.fragment;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.google.gson.Gson;
import com.sinia.orderlang.activity.AddressManageActivity;
import com.sinia.orderlang.activity.AfterSaleActivity;
import com.sinia.orderlang.activity.AllOrderActivity;
import com.sinia.orderlang.activity.CommonSettingActivity;
import com.sinia.orderlang.activity.MessageActivity;
import com.sinia.orderlang.activity.MyCollectActivity;
import com.sinia.orderlang.activity.PersonalDataActivity;
import com.sinia.orderlang.activity.RedPacketActivity;
import com.sinia.orderlang.bean.PersonalCenterBean;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.http.HttpCallBackListener;
import com.sinia.orderlang.request.MyMessageRequest;
import com.sinia.orderlang.request.PersonalCenterRequest;
import com.sinia.orderlang.utils.BitmapUtilsHelp;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.StringUtil;

public class MineFragment extends BaseFragment implements OnClickListener {
	private View rootView;
	private Activity activity;
	private LayoutInflater inflater;
	private RelativeLayout rl_person_info, rl_order_manager,
			rl_red_packet_manager, rl_my_collect, rl_address_manager,
			rl_setting, rl_notpay, rl_delivery, rl_notcomment, rl_refund,
			rl_news;
	private ImageView iv_has_unread_news, iv_head;
	private TextView tv_username, tv_telephone, tv_daizhifucount,
			tv_daishouhuocount, tv_daipingjiacount, tv_shouhoucount;
	private PersonalCenterBean personalCenterBean;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		personalCenter();
		getMessage();
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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("lamp", "onresume");
//		personalCenter(Constants.userId);
//		getMessage();
	}

	private void initView() {
		activity = getActivity();
		inflater = LayoutInflater.from(activity);
		rootView = inflater.inflate(R.layout.fragment_mine, null);
		rl_person_info = (RelativeLayout) rootView
				.findViewById(R.id.rl_person_info);
		rl_order_manager = (RelativeLayout) rootView
				.findViewById(R.id.rl_order_manager);
		rl_red_packet_manager = (RelativeLayout) rootView
				.findViewById(R.id.rl_red_packet_manager);
		rl_my_collect = (RelativeLayout) rootView
				.findViewById(R.id.rl_my_collect);
		rl_address_manager = (RelativeLayout) rootView
				.findViewById(R.id.rl_address_manager);
		rl_setting = (RelativeLayout) rootView.findViewById(R.id.rl_setting);
		rl_notpay = (RelativeLayout) rootView.findViewById(R.id.rl_notpay);
		rl_delivery = (RelativeLayout) rootView.findViewById(R.id.rl_delivery);
		rl_notcomment = (RelativeLayout) rootView
				.findViewById(R.id.rl_notcomment);
		rl_refund = (RelativeLayout) rootView.findViewById(R.id.rl_refund);
		rl_news = (RelativeLayout) rootView.findViewById(R.id.rl_news);
		iv_has_unread_news = (ImageView) rootView
				.findViewById(R.id.iv_has_unread_news);
		iv_head = (ImageView) rootView.findViewById(R.id.iv_head);
		tv_username = (TextView) rootView.findViewById(R.id.tv_username);
		tv_telephone = (TextView) rootView.findViewById(R.id.tv_telephone);
		tv_daizhifucount = (TextView) rootView
				.findViewById(R.id.tv_daizhifucount);
		tv_daishouhuocount = (TextView) rootView
				.findViewById(R.id.tv_daishouhuocount);
		tv_daipingjiacount = (TextView) rootView
				.findViewById(R.id.tv_daipingjiacount);
		tv_shouhoucount = (TextView) rootView
				.findViewById(R.id.tv_shouhoucount);
		rl_notpay.setOnClickListener(this);
		rl_delivery.setOnClickListener(this);
		rl_notcomment.setOnClickListener(this);
		rl_refund.setOnClickListener(this);
		rl_person_info.setOnClickListener(this);
		rl_order_manager.setOnClickListener(this);
		rl_red_packet_manager.setOnClickListener(this);
		rl_my_collect.setOnClickListener(this);
		rl_address_manager.setOnClickListener(this);
		rl_setting.setOnClickListener(this);
		rl_news.setOnClickListener(this);
	}

	public void personalCenter() {
		showLoad("");
		PersonalCenterRequest request = new PersonalCenterRequest();
		request.setMethod("personalCenter");
		request.setUserId(Constants.userId);
		Log.d("URL", Constants.BASE_URL + request.toString());

		CoreHttpClient.get(getActivity(), request.toString(),
				new HttpCallBackListener() {

					@Override
					public void onSuccess(JSONObject json) {
						dismiss();
						Log.d("result", json.toString());
						if (json.optInt("isSuccessful") == 0) {
							Gson gson = new Gson();
							personalCenterBean = gson.fromJson(json.toString(),
									PersonalCenterBean.class);
							refreshData(personalCenterBean);
						}
					}

					@Override
					public void onRequestFailed() {

					}
				});

	}

	public void getMessage() {
		// img_order.setVisibility(View.VISIBLE);
		// tv_order.setText("");
		//
		// img_order.setVisibility(View.GONE);
		// tv_order.setText("暂无新消息");
		MyMessageRequest request = new MyMessageRequest();
		request.setMethod("myMessage");
		request.setUserId(Constants.userId);
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.get(getActivity(), request.toString(),
				new HttpCallBackListener() {

					@Override
					public void onSuccess(JSONObject json) {
						Log.d("result", json.toString());
						if (json.optInt("isSuccessful") == 0) {
							if (json.optInt("messStatus") == 2) {
								iv_has_unread_news.setVisibility(View.VISIBLE);
							}else{
								iv_has_unread_news.setVisibility(View.INVISIBLE);
							}
						}
					}

					@Override
					public void onRequestFailed() {

					}
				});
	}

	private void refreshData(PersonalCenterBean personalCenterBean) {
		if (personalCenterBean.getWaitingPayNum() != 0) {
			tv_daizhifucount
					.setText(personalCenterBean.getWaitingPayNum() + "");
			tv_daizhifucount.setVisibility(View.VISIBLE);
		} else {
			tv_daizhifucount.setVisibility(View.INVISIBLE);
		}

		if (personalCenterBean.getWaitingShouHuoNum() != 0) {
			tv_daishouhuocount.setText(personalCenterBean
					.getWaitingShouHuoNum() + "");
			tv_daishouhuocount.setVisibility(View.VISIBLE);
		} else {
			tv_daishouhuocount.setVisibility(View.INVISIBLE);
		}

		if (personalCenterBean.getWaitingCommentNum() != 0) {
			tv_daipingjiacount.setText(personalCenterBean
					.getWaitingCommentNum() + "");
			tv_daipingjiacount.setVisibility(View.VISIBLE);
		} else {
			tv_daipingjiacount.setVisibility(View.INVISIBLE);
		}
		if (personalCenterBean.getWaitingApplyShouHouNum() != 0) {
			tv_shouhoucount.setText(personalCenterBean
					.getWaitingApplyShouHouNum() + "");
			tv_shouhoucount.setVisibility(View.VISIBLE);
		} else {
			tv_shouhoucount.setVisibility(View.INVISIBLE);
		}
		BitmapUtilsHelp.getImage(getActivity(), R.drawable.default_head)
				.display(iv_head, personalCenterBean.getImageUrl());
		tv_telephone.setText(personalCenterBean.getTelephone());
		tv_username.setText(personalCenterBean.getNickName());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_notpay:
			Intent it = new Intent(getActivity(), AllOrderActivity.class);
			it.putExtra("flag", 1);
			startActivity(it);
			break;
		case R.id.rl_delivery:
			Intent it2 = new Intent(getActivity(), AllOrderActivity.class);
			it2.putExtra("flag", 2);
			startActivity(it2);
			break;
		case R.id.rl_notcomment:
			Intent it3 = new Intent(getActivity(), AllOrderActivity.class);
			it3.putExtra("flag", 3);
			startActivity(it3);
			break;
		case R.id.rl_refund:
			startActivity(new Intent(getActivity(), AfterSaleActivity.class));
			break;
		case R.id.rl_person_info:
			Intent it4 = new Intent(getActivity(), PersonalDataActivity.class);
			it4.putExtra("personalCenterBean", personalCenterBean);
			startActivity(it4);
			break;
		case R.id.rl_order_manager:
			startActivity(new Intent(getActivity(), AllOrderActivity.class));
			break;
		case R.id.rl_red_packet_manager:
			startActivity(new Intent(getActivity(), RedPacketActivity.class));
			break;
		case R.id.rl_my_collect:
			// if (MyApplication.getInstance().getBoolValue("is_login")) {
			startActivity(new Intent(getActivity(), MyCollectActivity.class));
			// } else {
			// showToast("请先登录");
			// }
			break;
		case R.id.rl_address_manager:
			startActivity(new Intent(getActivity(), AddressManageActivity.class));
			break;
		case R.id.rl_setting:
			startActivity(new Intent(getActivity(), CommonSettingActivity.class));
			break;
		case R.id.rl_news:
			startActivity(new Intent(getActivity(), MessageActivity.class));
			break;
		default:
			break;
		}
	}
}
