package com.sinia.orderlang.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.fragment.MineFragment;
import com.sinia.orderlang.fragment.SeckillFragment;
import com.sinia.orderlang.fragment.ShoppingCartFragment;
import com.sinia.orderlang.fragment.SpecialOfferFragment;
import com.sinia.orderlang.utils.Constants;

/**
 * 主界面
 */
public class LangMainActivity extends FragmentActivity implements OnClickListener {
	private RelativeLayout rl_container;
	private TextView tv_tejia, tv_shopcar, tv_mine;
	private FragmentManager fragmentManager;
	private MineFragment mineFragment;
	private SeckillFragment seckillFragment;
	private SpecialOfferFragment specialOfferFragment;
	private ShoppingCartFragment shoppingCartFragment;
	public boolean showWaht = true;
	public static int showGouWuChe = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainss);
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (showGouWuChe != -1) {
			selectFragment(1);
			showGouWuChe = -1;
		}
	}
	
	private void initView() {
		rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		tv_tejia = (TextView) findViewById(R.id.tv_tejia);
		tv_shopcar = (TextView) findViewById(R.id.tv_shopcar);
		tv_mine = (TextView) findViewById(R.id.tv_mine);
		tv_tejia.setOnClickListener(this);
		tv_shopcar.setOnClickListener(this);
		tv_mine.setOnClickListener(this);
		fragmentManager = getSupportFragmentManager();
		mineFragment = new MineFragment();
		shoppingCartFragment = new ShoppingCartFragment();
		seckillFragment = new SeckillFragment();
		specialOfferFragment = new SpecialOfferFragment();
		fragmentManager.beginTransaction()
				.add(R.id.rl_container, seckillFragment).show(seckillFragment)
				.commit();
//		fragmentManager.beginTransaction()
//				.add(R.id.rl_container, specialOfferFragment)
//				.hide(specialOfferFragment).commit();
	}

	private void selectFragment(int index) {
		switch (index) {
		case 0:
			tv_tejia.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.tejia_select), null,
					null);
			tv_tejia.setTextColor(Color.parseColor("#CC333C"));
			tv_shopcar.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.shopcar_unselect),
					null, null);
			tv_shopcar.setTextColor(Color.parseColor("#888888"));
			tv_mine.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.mine_unselect), null,
					null);
			tv_mine.setTextColor(Color.parseColor("#888888"));
			if (showWaht) {
				showWaht = false;
				tv_tejia.setText("特价区");
				if(!specialOfferFragment.isAdded()){
					fragmentManager.beginTransaction()
					.add(R.id.rl_container, specialOfferFragment)
					.show(specialOfferFragment).commit();
				}else{
					showFragment(specialOfferFragment);
				}
				hideFragments(mineFragment, seckillFragment,shoppingCartFragment);
			} else {
				showWaht = true;
				tv_tejia.setText("秒杀区");
				if(!seckillFragment.isAdded()){
					fragmentManager.beginTransaction()
					.add(R.id.rl_container, seckillFragment).show(seckillFragment)
					.commit();
				}else{
					showFragment(seckillFragment);
				}
				hideFragments(mineFragment, specialOfferFragment,
						shoppingCartFragment);
			}
			break;
		case 1:
			tv_shopcar.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.shopcar_select),
					null, null);
			tv_shopcar.setTextColor(Color.parseColor("#CC333C"));
			tv_tejia.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.tejia_unselect),
					null, null);
			tv_tejia.setTextColor(Color.parseColor("#888888"));
			tv_mine.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.mine_unselect), null,
					null);
			tv_mine.setTextColor(Color.parseColor("#888888"));
			if (!shoppingCartFragment.isAdded()) {
				fragmentManager.beginTransaction()
						.add(R.id.rl_container, shoppingCartFragment)
						.show(shoppingCartFragment).commit();
			} else {
				shoppingCartFragment.getData();
				showFragment(shoppingCartFragment);
			}
			hideFragments(seckillFragment, specialOfferFragment, mineFragment);
			break;
		case 2:
			tv_mine.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.mine_select), null,
					null);
			tv_mine.setTextColor(Color.parseColor("#CC333C"));
			tv_shopcar.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.shopcar_unselect),
					null, null);
			tv_shopcar.setTextColor(Color.parseColor("#888888"));
			tv_tejia.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.tejia_unselect),
					null, null);
			tv_tejia.setTextColor(Color.parseColor("#888888"));
			if (!mineFragment.isAdded()) {
				fragmentManager.beginTransaction()
						.add(R.id.rl_container, mineFragment)
						.show(mineFragment).commit();
			} else {
				mineFragment.personalCenter();
				mineFragment.getMessage();
				showFragment(mineFragment);
			}
			hideFragments(seckillFragment, specialOfferFragment,
					shoppingCartFragment);
			break;
		default:
			break;
		}
	}

	private void showFragment(Fragment fragment) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.show(fragment).commit();
	}

	private void hideFragments(Fragment... event) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
		for (int i = 0; i < event.length; i++) {
			ft.hide(event[i]);
		}
		ft.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_tejia:
			selectFragment(0);
			break;
		case R.id.tv_shopcar:
			selectFragment(1);
			break;
		case R.id.tv_mine:
			selectFragment(2);
			break;

		default:
			break;
		}
	}
}
