package com.sinia.orderlang.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.bean.AddOrderBean;
import com.sinia.orderlang.bean.AddRedBaoBean;
import com.sinia.orderlang.bean.AddRedBaoExBean;
import com.sinia.orderlang.bean.AddressListList;
import com.sinia.orderlang.bean.CarListBean;
import com.sinia.orderlang.bean.CollectionListList;
import com.sinia.orderlang.bean.OrderDetailBean;
import com.sinia.orderlang.bean.OrderGoodList;
import com.sinia.orderlang.bean.OrderManagerList;
import com.sinia.orderlang.bean.PersonalCenterBean;
import com.sinia.orderlang.bean.RedBaoDetailBean;
import com.sinia.orderlang.bean.RedBaoManagerDetailBean;
import com.sinia.orderlang.bean.RedBaoManagerList;
import com.sinia.orderlang.bean.TeJiaDetailBean;
import com.sinia.orderlang.bean.TeJiaListList;
import com.sinia.orderlang.http.HttpResponseListener;
import com.sinia.orderlang.utils.StringUtil;

public class BaseFragment extends Fragment implements HttpResponseListener {
	private Dialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void showToast(String text) {
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}

	public void showToast(int id) {
		showToast(id + "");
	}
	public void showLoad(String showText) {
		if (StringUtil.isEmpty(showText)) {
			showText = "正在加载中...";
		}
		dialog = new Dialog(getActivity(), R.style.dialog);
		View v = ((LayoutInflater) getActivity().getSystemService(
				getActivity().LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.view_progress_dialog_anim, null);
		((TextView) v.findViewById(R.id.tv_info)).setText(showText);
		Display d = getActivity().getWindowManager().getDefaultDisplay();
		dialog.setContentView(v, new LayoutParams((int) (d.getWidth() * 0.5),
				(int) (d.getHeight() * 0.15)));
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.dimAmount = 0.5f;
		dialog.getWindow().setAttributes(lp);
		dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	public void dismiss() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	@Override
	public void requestSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestException() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void httpRequestFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getPersonalCenterSuccess(PersonalCenterBean bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRedBaoDetailSuccess(RedBaoDetailBean bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCollectionSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCollectionFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRedBaoSuccess(AddRedBaoBean bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRedBaoFailed(AddRedBaoExBean addRedBaoExBean) {

	}

	@Override
	public void cancelCollectionSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelCollectionFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRedBaoManagerListSuccess(RedBaoManagerList list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRedBaoManagerDetailSuccess(RedBaoManagerDetailBean bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delRedBaoSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delRedBaoFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getCollectionListSuccess(CollectionListList list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getTeJiaListSuccess(TeJiaListList list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getTeJiaDetailSuccess(TeJiaDetailBean bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addShopCarSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addShopCarFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getShopCarListSuccess(CarListBean bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAddressListSuccess(AddressListList list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delAddressSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delAddressFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefaltAddressSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefaltAddressFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addOrderSuccess(AddOrderBean bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getOrderManagerListSuccess(OrderManagerList list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getOrderDetailSuccess(OrderDetailBean bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delCollectionSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delCollectionFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getGoodsListSuccess(OrderGoodList list) {
		// TODO Auto-generated method stub
		
	}

}
