package com.sinia.orderlang.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
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
import com.sinia.orderlang.utils.ActivityManager;
import com.sinia.orderlang.utils.BaseExceptionHandler;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.StringUtil;

/**
 * 基类
 */
public class BaseActivity extends FragmentActivity implements
		HttpResponseListener {

	private LinearLayout headParent;

	private LinearLayout bodyParent;

	private LinearLayout footParent;

	private TextView devideTv;

	private ImageView backView;

	private TextView titleView;

	private TextView doingView;
	private ImageView selectView;

	private Dialog dialog;

	public void setDoingView(TextView doingView) {
		this.doingView = doingView;
	}

	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	private final String mPageName = "BaseActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		BaseExceptionHandler.getInstance().setContext(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		ActivityManager.getInstance().addActivity(this);
	}

	protected void onAfterSetContentView() {
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				backView();
				finish();
			}
		});
		doingView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				doing();
			}
		});
	}
	
	protected void backView() {
		
	}
	
	protected void logUrl(String url){
		Log.d("URL", Constants.BASE_URL + url);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		setContentView(layoutResID, "");

	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		// TODO Auto-generated method stub
		super.setContentView(view, params);
	}

	public void setContentView(int layoutResID, String title) {
		View body = View.inflate(this, layoutResID, null);
		setContentView(body, title);
		onAfterSetContentView();
	}

	@Override
	public void setContentView(View view) {
		// TODO Auto-generated method stub
		super.setContentView(view);
	}

	public void setContentView(View body, String title) {
		View root = View.inflate(this, R.layout.root_layout, null);
		headParent = (LinearLayout) root.findViewById(R.id.root_layout_head);
		bodyParent = (LinearLayout) root.findViewById(R.id.root_layout_body);
		footParent = (LinearLayout) root.findViewById(R.id.root_layout_foot);
		devideTv = (TextView) root.findViewById(R.id.root_layout_tv);
		buildHeadView(headParent, title);
		bodyParent.addView(body, params);
		super.setContentView(root);
	}

	private void buildHeadView(LinearLayout parent, String title) {
		View head = createHeadView(title);
		if (head != null && !hideHeadView() /* && !TextUtils.isEmpty(title) */) {
			parent.setVisibility(View.VISIBLE);
			parent.addView(head, params);
			devideTv.setVisibility(View.VISIBLE);
		} else {
			parent.setVisibility(View.GONE);
			devideTv.setVisibility(View.GONE);
		}
	}

	public View createHeadView(String title) {
		View head = View.inflate(this, R.layout.head_layout, null);
		backView = (ImageView) head.findViewById(R.id.back);
		titleView = (TextView) head.findViewById(R.id.title);
		doingView = (TextView) head.findViewById(R.id.doing);
		titleView.setText(title);
		if (hasShowDoingView()) {
			doingView.setVisibility(View.VISIBLE);
		}
		return head;
	}

	public View createHomeHeadView(String title) {
		View head = View.inflate(this, R.layout.home_head_layout, null);
		backView = (ImageView) head.findViewById(R.id.open_menu);
		titleView = (TextView) head.findViewById(R.id.title);
		selectView = (ImageView) head.findViewById(R.id.doing);
		titleView.setText(title);
		if (hasShowDoingView()) {
			selectView.setVisibility(View.VISIBLE);
		}
		return head;
	}

	/**存储数据到SharePreference**/
	public  void saveDataToSharePreference(String key,String value){
		SharedPreferenceUtils.saveToSharedPreference(this, key, value);
	}

	/**丛SharePreference中获取数据**/
	public String getFromSharePreference(String key){
		return SharedPreferenceUtils.getFromSharedPreference(this,key);
	}

	public boolean hasShowDoingView() {
		return true;
	}

	public boolean hideHeadView() {
		return false;
	}

	public View getFootParentView() {
		return footParent;
	}

	public TextView getHengxian() {
		return devideTv;
	}

	public View getHeadParentView() {
		return headParent;
	}

	public TextView getDoingView() {
		return doingView;
	}

	public View getTitleView() {
		return titleView;
	}

	public void doing() {

	}

	public void showToast(final String text) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void showToast(int id) {
		showToast(id + "");
	}

	public void showLoad(String showText) {
		if (StringUtil.isEmpty(showText)) {
			showText = "正在加载中...";
		}
		dialog = new Dialog(this, R.style.dialog);
		View v = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.view_progress_dialog_anim, null);
		((TextView) v.findViewById(R.id.tv_info)).setText(showText);
		Display d = getWindowManager().getDefaultDisplay();
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

	protected void startActivityForNoIntent(Class forwordClass) {
		Intent intent = new Intent(this, forwordClass);
		startActivity(intent);
		// overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	/**
	 * @Description: TODO(�??)
	 */
	protected void startActivityForIntent(Class forwordClass, Intent intent) {
		intent.setClass(this, forwordClass);
		startActivity(intent);
		// overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	protected void back(View backView) {
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityManager.getInstance().finishCurrentActivity();
				overridePendingTransition(R.anim.right_in, R.anim.right_out);
			}
		});
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
