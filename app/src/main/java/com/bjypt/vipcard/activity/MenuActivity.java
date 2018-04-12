package com.bjypt.vipcard.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.DishesLeftAdapter;
import com.bjypt.vipcard.adapter.RightAdapter;
import com.bjypt.vipcard.assistant.onCallBackListener;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.linkagemenu.PinnedHeaderListView;
import com.bjypt.vipcard.model.MenuModel;
import com.bjypt.vipcard.model.MenuTest;
import com.bjypt.vipcard.model.ProductListBean;
import com.bjypt.vipcard.model.ProductTypeListBean;
import com.bjypt.vipcard.utils.DoubleUtil;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuActivity extends BaseActivity implements VolleyCallBack<String>, onCallBackListener {

    private LinearLayout back;
    private TextView title;
    private boolean isScroll = true;
    private ListView mainList;
    private PinnedHeaderListView moreList;

    private Map<String, String> params = new HashMap<>();

    private MenuTest merchantDetailsTest;
    private DishesLeftAdapter dishesLeftAdapter;

    private RightAdapter rightAdapter;   //右侧适配器

    private List<ProductTypeListBean> productTypeslist = new ArrayList<>();

    private boolean isClean = false;          // 是否完成清理
    private int number = 0;                    // 正在执行的动画数量
    private FrameLayout animation_viewGroup;
    private TextView settlement;               //点好了
    private ImageView shopping_cart;          //“惠”logo
    private TextView shopping;
    private List<ProductListBean> productList;

    private TextView shoppingNum;             //购物车件数
    private int shopNum;

    private TextView shoppingPrise;           //购物车价格
    private String totalPrice;

    private String pkmuser;

    List<MenuModel> menuModelList = new ArrayList<>();

    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 用来清除动画后留下的垃圾
                    try {
                        animation_viewGroup.removeAllViews();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isClean = false;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_menu);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        pkmuser = intent.getStringExtra("pkmuser");
    }

    @Override
    public void initView() {
        productList = new ArrayList<>();
        shopping = (TextView) findViewById(R.id.shopping);
        back = (LinearLayout) findViewById(R.id.shopping_back);
        title = (TextView) findViewById(R.id.menu_shopname);
        mainList = (ListView) findViewById(R.id.classify_mainlist);
        moreList = (PinnedHeaderListView) findViewById(R.id.classify_morelist);
        settlement = (TextView) findViewById(R.id.settlement);
        shopping_cart = (ImageView) findViewById(R.id.shopping_cart);
        shoppingNum = (TextView) findViewById(R.id.shoppingNum);
        shoppingPrise = (TextView) findViewById(R.id.shoppingPrice);
        animation_viewGroup = createAnimLayout();

        params.put("pkmuser", pkmuser);//"50e5355183b04453b869972220eb4c83"
        params.put("versionCode", getVersion());
        Wethod.httpPost(MenuActivity.this, 1, Config.web.merchant_details, params, this);
    }

    @Override
    public void afterInitView() {

    }

    /**
     * @param
     * @return void
     * @throws
     * @Description: 创建动画层
     */
    private FrameLayout createAnimLayout() {
        ViewGroup rootView = (ViewGroup) getWindow().getDecorView();
        FrameLayout animLayout = new FrameLayout(MenuActivity.this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;

    }

    @Override
    public void bindListener() {
        settlement.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.shopping_back:
                finish();
                break;
            case R.id.settlement:   //点好了
                if (productList.size() != 0) {
                    //跳转新页面
                    menuModelList.clear();
                    for (int j = 0; j < productTypeslist.size(); j++) {
                        MenuModel menuModel = new MenuModel();
                        menuModel.setTypename(productTypeslist.get(j).getTypename());
                        List<ProductListBean> list = new ArrayList<>();
                        for (int i = 0; i < productList.size(); i++) {
                            if (productList.get(i).getPktypeid().equals(productTypeslist.get(j).getPktypeid())) {
                                list.add(productList.get(i));
                            }
                        }
                        if (list.size() > 0) {
                            menuModel.setMenuProductions(list);
                            menuModelList.add(menuModel);
                        }
                    }
                    Intent intent = new Intent(this, TotalPriceActivity.class);
                    intent.putExtra("totalPrice", totalPrice);
                    intent.putExtra("title", merchantDetailsTest.getResultData().getMuname());
                    intent.putExtra("menuModelList", (Serializable) menuModelList);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == 1) {
            try {
                merchantDetailsTest = getConfiguration().readValue(result.toString(), MenuTest.class);
                productTypeslist = merchantDetailsTest.getResultData().getProductTypeList();

                title.setText(merchantDetailsTest.getResultData().getMuname());

                rightAdapter = new RightAdapter(this, productTypeslist, merchantDetailsTest.getResultData().getRechargeActivity(),
                        merchantDetailsTest.getResultData().getHeadpkmuser(), merchantDetailsTest.getResultData().getMuname(),
                        merchantDetailsTest.getResultData().getPreorderStartPrice());
                //动画
                rightAdapter.SetOnSetHolderClickListener(new RightAdapter.HolderClickListener() {
                    @Override
                    public void onHolderClick(Drawable drawable, int[] start_location) {
                        doAnim(drawable, start_location);
                    }

                });
                moreList.setAdapter(rightAdapter);
                rightAdapter.setCallBackListener(this);

                dishesLeftAdapter = new DishesLeftAdapter(this, merchantDetailsTest.getResultData().getProductTypeList());
                mainList.setAdapter(dishesLeftAdapter);
                mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        isScroll = false;
                        //设置左侧list点击后的背景颜色
                        dishesLeftAdapter.setSelectPostion(position);
                        int rightSection = 0;
                        for (int i = 0; i < position; i++) {
                            rightSection += rightAdapter.getCountForSection(i) + 1;
                        }
                        moreList.setSelection(rightSection);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            moreList.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView arg0, int arg1) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    if (isScroll) {
                        for (int i = 0; i < mainList.getChildCount(); i++) {
                            if (i == rightAdapter.getSectionForPosition(firstVisibleItem)) {
                                dishesLeftAdapter.setSelectPostion(i);
                            }
                        }
                    } else {
                        isScroll = true;
                    }
                }
            });
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    //获取版本号
    private String getVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateProduct(ProductListBean product, String type) {

        if (type.equals("1")) {
            if (!productList.contains(product)) {
                productList.add(product);
            } else {
                for (ProductListBean shopProduct : productList) {
                    if (product.getPkproduct().equals(shopProduct.getPkproduct())) {
                        shopProduct.setNumber(shopProduct.getNumber());
                    }
                }
            }
        } else if (type.equals("2")) {
            if (productList.contains(product)) {
                if (product.getNumber() == 0) {
                    productList.remove(product);
                } else {
                    for (ProductListBean shopProduct : productList) {
                        if (product.getPkproduct().equals(shopProduct.getPkproduct())) {
                            shopProduct.setNumber(shopProduct.getNumber());
                        }
                    }
                }
            }
        }
        setPrise();
    }

    /**
     * 更新购物车价格
     */
    public void setPrise() {
        double sum = 0;
        shopNum = 0;
        for (ProductListBean pro : productList) {
            sum = DoubleUtil.sum(sum, DoubleUtil.mul((double) pro.getNumber(), Double.parseDouble(pro.getProductPrice())));
            shopNum = shopNum + pro.getNumber();
        }

        if (shopNum > 0) {
            settlement.setBackgroundColor(Color.parseColor("#f94c64"));
            shopping_cart.setBackgroundResource(getResources().getIdentifier("hui", "drawable", "com.bjypt.vipcard"));

        } else {
            settlement.setBackgroundColor(Color.parseColor("#B0B0B0"));
            shopping_cart.setBackgroundResource(getResources().getIdentifier("menu_hui_gray", "drawable", "com.bjypt.vipcard"));
        }

        if (shopNum > 0) {
            shoppingNum.setVisibility(View.VISIBLE);
        } else {
            shoppingNum.setVisibility(View.GONE);
        }
        if (sum > 0) {
            shoppingPrise.setVisibility(View.VISIBLE);
            shopping.setVisibility(View.VISIBLE);
        } else {
            shoppingPrise.setVisibility(View.GONE);
            shopping.setVisibility(View.GONE);
        }
        totalPrice = (new DecimalFormat("0.00")).format(sum);
        shoppingPrise.setText("¥" + " " + (new DecimalFormat("0.00")).format(sum));
        shoppingNum.setText(String.valueOf(shopNum));
    }

    private void doAnim(Drawable drawable, int[] start_location) {
        if (!isClean) {
            setAnim(drawable, start_location);
        } else {
            try {
                animation_viewGroup.removeAllViews();
                isClean = false;
                setAnim(drawable, start_location);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isClean = true;
            }
        }
    }

    /**
     * 动画效果设置
     *
     * @param drawable       将要加入购物车的商品
     * @param start_location 起始位置
     */
    @SuppressLint("NewApi")
    private void setAnim(Drawable drawable, int[] start_location) {
        Animation mScaleAnimation = new ScaleAnimation(1.2f, 0.6f, 1.2f, 0.6f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mScaleAnimation.setFillAfter(true);

        final ImageView iview = new ImageView(MenuActivity.this);
        iview.setImageDrawable(drawable);
        final View view = addViewToAnimLayout(animation_viewGroup, iview,
                start_location);


        view.setAlpha(0.6f);

        int[] end_location = new int[2];
        settlement.getLocationInWindow(end_location);

        // 计算位移
        int endX = 0 - start_location[0] + 100;// 动画位移的X坐标
        int endY = end_location[1] - start_location[1];// 动画位移的y坐标
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);


        Animation mRotateAnimation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.3f, Animation.RELATIVE_TO_SELF,
                0.3f);
        mRotateAnimation.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(mRotateAnimation);
        set.addAnimation(mScaleAnimation);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(500);// 动画的执行时间
        view.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                number++;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                number--;
                if (number == 0) {
                    isClean = true;
                    myHandler.sendEmptyMessage(0);
                }

                ObjectAnimator.ofFloat(shopping_cart, "translationY", 0, 4, -2, 0).setDuration(400).start();
                ObjectAnimator.ofFloat(shoppingNum, "translationY", 0, 4, -2, 0).setDuration(400).start();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });
    }

    /**
     * @param vg       动画运行的层 这里是frameLayout
     * @param view     要运行动画的View
     * @param location 动画的起始位置
     * @return
     * @deprecated 将要执行动画的view 添加到动画层
     */
    private View addViewToAnimLayout(ViewGroup vg, View view, int[] location) {
        int x = location[0];
        int y = location[1];
        vg.addView(view);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setPadding(5, 5, 5, 5);
        view.setLayoutParams(lp);

        return view;
    }

}
