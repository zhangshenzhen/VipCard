package com.bjypt.vipcard.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.DetailsChooseAdater;
import com.bjypt.vipcard.adapter.DishesLeftAdapter;
import com.bjypt.vipcard.adapter.MyBaseAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.CarouselBean;
import com.bjypt.vipcard.model.DiplaybtnBean;
import com.bjypt.vipcard.model.DishesCarBean;
import com.bjypt.vipcard.model.IsVipBean;
import com.bjypt.vipcard.model.MerchantDetailsTest;
import com.bjypt.vipcard.model.ProductListBean;
import com.bjypt.vipcard.model.SaveMoneyBean;
import com.bjypt.vipcard.model.SaveMoneyData;
import com.bjypt.vipcard.model.ShoppingDetailList;
import com.bjypt.vipcard.model.UserMechantInfoBean;
import com.bjypt.vipcard.model.saveMoneyListGoBean;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.BaiduUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.StringUtils;
import com.brioal.adtextviewlib.entity.AdEntity;
import com.brioal.adtextviewlib.view.ADTextView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bjypt.vipcard.R.color.red_text_dishes;
import static com.bjypt.vipcard.common.Config.userConfig.pkregister;

//import com.mszx.vipcard.adapter.DishesCarAdapter;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/28
 * Use by 预约菜品
 */
public class SubscribeDishesActivity extends BaseActivity implements VolleyCallBack<String> {

    private boolean isScroll = true;
    private boolean is_half = false;//是否为五折店详情
    private RelativeLayout mAdBg;
    private Animation mAppShowAction;
    private RelativeLayout mCommentShopname, mJoinVips;
    private ImageView btn_goCommentone, btn_goCommenttwo;
    private LinearLayout mLocationPhone;
    private LinearLayout layout_sub_daozheli;//到这里
    private RelativeLayout mRelaTitle, rela_title;
    public static RelativeLayout mShopCarLinear;
    private DishesLeftAdapter dishesLeftAdapter;
    private RelativeLayout photo_num_rela;
    private LinearLayout mSubscribeBack;
    private RelativeLayout mPhoneNum;
    private TextView startPrice, mSetFunction;
    public static TextView mCarTotalMoney;
    public static TextView mTotalPrice;
    private String isVipPkmuser;
    private DishesCarAdapter dishesCarAdapter;
    public static double totalMoney;//总价格
    public static double totalPayment;//总定金
    private TextView mMerchantName, mPhoneNumTv, mMerchantAdress, mMerchantDistance, mMerchantPhone;
    private ImageView mMerchantBg, mSubscribeImg;
    private List<ProductListBean> dishesList = new ArrayList<>();
    private DetailsChooseAdater detailsChooseAdater;
    private RelativeLayout mPutOrder;

    private ListView mLeftListView;
    private ListView mRightListView;
    private RelativeLayout mTitle;
    private String pkmuser;//商家组建
    private String distance;//距离
    private Map<String, String> params = new HashMap<>();
    private MerchantDetailsTest merchantDetailsTest;
    public static List<DishesCarBean> carList = new ArrayList<>();
    public static int SKIP_ACTIVITY = 0;//为了跳转页面时的判断
    private int FLAG_CLICK = 0;
    private int CLICK_LEFT = 0;
    private String longitudeFrom;
    private String latitudeFrom;
    private String phone;
    private RelativeLayout mToComment;
    private boolean isVip = false;//判断是否为会员
    private TextView mSubTitle;
    private ImageView mSeeImgDetail;
    private RelativeLayout mVisibleComment;
    private LinearLayout mLastLine;//双Listview
    private String scanResult = "";
    private TextView mMerchantDetail;
    private RelativeLayout mLijiAccount;//立即买单
    private TextView mAccount;
    private TextView comment_tv;
    //    private RelativeLayout mTishi;
    //    private RelativeLayout mLayoutPianyi;
    private ADTextView mSendMore;
    private TextView tv_jian;
    private LinearLayout ly_sub_dish_join;
    private RelativeLayout rel_at_join;
    // RelativeLayout
    private RelativeLayout rl_multiple_shop;  // 更多连锁店

    /**
     * 定位
     */
    private double latitude;
    private double longitude;
    private String cityCode;
    private String EXIT_FLAG = "0";
    private View first;
    private RelativeLayout youhui_rela;
    private TextView youhui_final;
    private TextView youhui_shiji;
    private TextView youhui_jieshao;
    private View youhui_line;
    List<AdEntity> texts = new ArrayList<AdEntity>();

    /**
     * 开始定位
     */
    //    public final static int MSG_LOCATION_START = 0;
    /**
     * 定位完成
     */
    //    public final static int MSG_LOCATION_FINISH = 1;
    /**
     * 停止定位
     */
    //   / public final static int MSG_LOCATION_STOP = 2;

    //    private AMapLocationClient locationClient = null;
    //    private AMapLocationClientOption locationOption = null;

    public static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    Log.i("aaa", "totalMoney==" + totalMoney);
                    if (totalMoney == 0 || (totalMoney + "").equals("0.0")) {
                        mCarTotalMoney.setText("¥ 0元");
                    } else if (totalMoney < 1) {
                        mCarTotalMoney.setText("¥ 0" + StringUtils.setScale(totalMoney + "") + "元");
                    } else {
                        mCarTotalMoney.setText("¥ " + StringUtils.setScale(totalMoney + "") + "元");
                    }
                    mShopCarLinear.postInvalidate();
                    break;
                case 2:
                    Log.i("aaa", ">>>>" + totalMoney + "");
                    if (StringUtils.setScale(totalMoney + "").equals(".00")) {
                        mTotalPrice.setText("¥ 0元");
                    } else if (totalMoney < 1) {
                        mTotalPrice.setText("¥ 0" + StringUtils.setScale(totalMoney + "") + "元");
                    } else {
                        mTotalPrice.setText("¥ " + StringUtils.setScale(totalMoney + "") + "元");
                    }

                    mCarTotalMoney.setText(mTotalPrice.getText().toString());
                    mShopCarLinear.postInvalidate();
                    break;

            }
            return false;
        }
    });

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    mLeftListView.getChildAt(tempIndex).setBackgroundColor(
                            Color.rgb(255, 255, 255));
                    TextView tv = (TextView) mLeftListView.getChildAt(tempIndex).findViewById(R.id.left_tv);
                    tv.setTextColor(Color.rgb(233, 88, 78));
                    break;
                case 2:

                    Log.i("wanglei", "111111");
                    break;
            }
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private String merdesc = "";
    private int request_code_carousel = 110;
    private String linkage_pkdealer;
    private boolean display;
    private String balance_mer;


    public void refreshViewTotalMoney() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SubscribeDishesActivity.totalMoney = 0;
                SubscribeDishesActivity.totalPayment = 0;
                Log.i("aaa", "11>>>totalMoney=" + totalMoney);
                for (int i = 0; i < dishesList.size(); i++) {
                    if (DetailsChooseAdater.numMap.get(i) > 0) {
                        SubscribeDishesActivity.totalMoney += DetailsChooseAdater.numMap.get(i) * Double.parseDouble(dishesList.get(i).getProductPrice());
                        SubscribeDishesActivity.totalPayment += DetailsChooseAdater.numMap.get(i) * Double.parseDouble(dishesList.get(i).getEarnestMoney());
                    }


                    Log.i("aaa", "222>>>totalMoney=" + totalMoney);
                }
                Log.e("TYZ", "************************" + SubscribeDishesActivity.totalMoney);
                SubscribeDishesActivity.handler.sendEmptyMessage(1);
            }
        }).start();
        detailsChooseAdater.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        if (dishesList.size() > 0) {
            refreshViewTotalMoney();
        }
        Intent intent1 = getIntent();
        distance = intent1.getStringExtra("distance");
        Log.e("vvv", "distance==" + distance);
       /* if ((distance == null || distance.equals("")) && !distance.contains("*")) {
//            Map<String, String> firstIsVip = new HashMap<>();
//            firstIsVip.put("pkmuser", isVipPkmuser);
//            firstIsVip.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
//            Wethod.httpPost(2, Config.web.is_vip, firstIsVip, this);
            Log.e("vvv", "distance==11111111111111111111");
            isVip = true;
//            Toast.makeText(this, "尊贵的VIP,欢迎回来", Toast.LENGTH_SHORT).show();
            mSetFunction.setText("充\t值");
        }*/
        {
            Log.e("vvv", "distance==222222222222222222");
            //判断是否是会员
            Map<String, String> isVipParams = new HashMap<>();
            isVipParams.put("pkmuser", pkmuser);
            isVipParams.put("pkregister", getFromSharePreference(pkregister));
            Log.i("aaa", "1>>>>>>>>>>>>>");
            //判断当前用户是否是当前商家的会员
            Wethod.httpPost(SubscribeDishesActivity.this, 2, Config.web.is_vip, isVipParams, this);
        }


        Log.e("TYZ", "SKIP_ACTIVITY：" + SKIP_ACTIVITY);
        if (SKIP_ACTIVITY == 1) {
            SubscribeDishesActivity.this.finish();
            SKIP_ACTIVITY = 0;
        }
        Log.i("aaaa", "onResume");
        if (detailsChooseAdater != null) {
            Log.e("tyz", "detailsChooseAdater");
            detailsChooseAdater.notifyDataSetChanged();
        }
        if (dishesCarAdapter != null) {
            dishesCarAdapter.notifyDataSetChanged();
        }
        super.onResume();
        UmengCountContext.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }

    //    public static Map<Integer,List<DishesCarBean>> carList = new HashMap<>();

    @Override
    public void setContentLayout() {
        Log.e("GGGG", "setContentLayout");
        setContentView(R.layout.activity_subscribedisher);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DetailsChooseAdater.FLAG_DETAILS_ADAPTER = 0;
    }


    @Override
    public void beforeInitView() {
        is_half = (getIntent().hasExtra("is_half") && "Y".equals(getIntent().getStringExtra("is_half")));
        Wethod.configImageLoader(this);
        Log.e("GGGG", "beforeInitView");

        //开启一个主线程刷新购物车金额

        Intent intent = getIntent();

        pkmuser = intent.getStringExtra("pkmuser");
        /********当扫描商家二维码进入此页面的时候，distance所传的距离为扫码所获得的数据*********/
        distance = intent.getStringExtra("distance");
        merdesc = intent.getStringExtra("rechargeactivity");

        /*获取商家详情接口*/
        params.put("pkmuser", pkmuser);
        if (is_half) {
            params.put("special_price", "1");
            params.put("versionCode", getVersion());
        }
        if (pkmuser == null || pkmuser.equals("")) {
            Map<String, String> firstMap = new HashMap<>();
            firstMap.put("userId", getFromSharePreference(pkregister));
            Wethod.httpPost(SubscribeDishesActivity.this, 1, Config.web.get_first_merchant, firstMap, this);
        } else {
            Wethod.httpPost(SubscribeDishesActivity.this, 1, Config.web.merchant_details, params, this);
        }


        mAppShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        final Animation mAppHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f);

        mTitle = (RelativeLayout) findViewById(R.id.subscribe_title);
        rela_title = (RelativeLayout) findViewById(R.id.rela_title);

        mLeftListView = (ListView) findViewById(R.id.left_listview);
        mShopCarLinear = (RelativeLayout) findViewById(R.id.shop_car_linear);
        layout_sub_daozheli = (LinearLayout) findViewById(R.id.layout_sub_daozheli);
        mRightListView = (ListView) findViewById(R.id.pinnedListView);
        mCommentShopname = (RelativeLayout) findViewById(R.id.comment_shopname);
        mLocationPhone = (LinearLayout) findViewById(R.id.location_phone);
        ly_sub_dish_join = (LinearLayout) findViewById(R.id.ly_sub_dish_join);
        startPrice = (TextView) findViewById(R.id.starting_price);
        mCarTotalMoney = (TextView) findViewById(R.id.car_total_money);

        mAdBg = (RelativeLayout) findViewById(R.id.ad_bg);
        mLastLine = (LinearLayout) findViewById(R.id.last_line);
        mRelaTitle = (RelativeLayout) findViewById(R.id.rela_title);

        realtive_anim = (RelativeLayout) findViewById(R.id.realtive_anim);
        mLijiAccount = (RelativeLayout) findViewById(R.id.subscribe_account);
        mAccount = (TextView) findViewById(R.id.tv_pianyi);
        mSendMore = (ADTextView) findViewById(R.id.tv_sendMore);

        rl_multiple_shop = (RelativeLayout) findViewById(R.id.rl_multiple_shop);


        /** 头条速度和样式 */
        mSendMore.setSpeed(1);
        mSendMore.setFrontColor(getResources().getColor(red_text_dishes));
        mSendMore.setBackColor(getResources().getColor(red_text_dishes));
        texts.add(new AdEntity("", "", ""));
        mSendMore.setTexts(texts);
        tv_jian = (TextView) findViewById(R.id.tv_jian);
        rel_at_join = (RelativeLayout) findViewById(R.id.rel_at_join);
        //        mTishi= (RelativeLayout) findViewById(R.id.relative_tishi);
        //        mLayoutPianyi= (RelativeLayout) findViewById(R.id.relative_pianyi);
        getHttpCarousel();
        isMore();
    }

    private RelativeLayout realtive_anim;

    @Override
    public void initView() {

       /* locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为低功耗模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        // 设置定位监听
        locationClient.setLocationListener(this);
*/
        //        startLocation();


        Log.e("GGGG", "initView");
        first = findViewById(R.id.first);
        relative_visible = (RelativeLayout) findViewById(R.id.relative_visible);
        mPhoneNum = (RelativeLayout) findViewById(R.id.phone_num);
        photo_num_rela = (RelativeLayout) findViewById(R.id.photo_num_rela);
        mSubscribeBack = (LinearLayout) findViewById(R.id.subscribe_back);

        /*界面初始化*/
        mMerchantPhone = (TextView) findViewById(R.id.merchant_phone);
        mMerchantBg = (ImageView) findViewById(R.id.merchant_bg);//商家背景
        mMerchantName = (TextView) findViewById(R.id.merchant_name);//商家名称
        mPhoneNumTv = (TextView) findViewById(R.id.photo_num_tv);//商家图片数量
        mMerchantAdress = (TextView) findViewById(R.id.merchant_adress);//商家地址
        mMerchantDistance = (TextView) findViewById(R.id.merchant_distance);//距离
        mPutOrder = (RelativeLayout) findViewById(R.id.put_order);
        mSeeImgDetail = (ImageView) findViewById(R.id.iv_seeImgDetail);
        mSetFunction = (TextView) findViewById(R.id.tv_joinViporRecharge);//加入会员或者充值
        mJoinVips = (RelativeLayout) findViewById(R.id.subscribe_join);//加入会员
        btn_goCommentone = (ImageView) findViewById(R.id.btn_goCommentone);//
        btn_goCommenttwo = (ImageView) findViewById(R.id.btn_goCommenttwo);//去评论界面

        mMerchantDetail = (TextView) findViewById(R.id.merchant_detail_click);
        mSubTitle = (TextView) findViewById(R.id.sub_title);
        mVisibleComment = (RelativeLayout) findViewById(R.id.visible_comment);
        comment_tv = (TextView) findViewById(R.id.comment_tv);

        mToComment = (RelativeLayout) findViewById(R.id.to_comment);

        youhui_rela = (RelativeLayout) findViewById(R.id.youhui_rela);
        youhui_final = (TextView) findViewById(R.id.youhui_final);
        youhui_shiji = (TextView) findViewById(R.id.youhui_shiji);
        youhui_jieshao = (TextView) findViewById(R.id.youhui_jieshao);
        youhui_line = findViewById(R.id.youhui_line);
    }

    @Override
    public void afterInitView() {
        if (merdesc != null && merdesc.equals("1")) {
            tv_jian.setText("消费多少返多少");
        } else {
            tv_jian.setText("随机立减\t最高免单");
        }

    }

    private void setData() {

        mMerchantBg.setImageResource(R.mipmap.ad_bg);
        if (!"".equals(merchantDetailsTest.getResultData().getLogo())) {
            ImageLoader.getInstance().displayImage(Config.web.picUrl + merchantDetailsTest.getResultData().getLogo(), mMerchantBg, AppConfig.DEFAULT_IMG_OPTIONS_AD);
        }
        mMerchantAdress.setText(merchantDetailsTest.getResultData().getAddress());
        startPrice.setText(merchantDetailsTest.getResultData().getPreorderStartPrice() + "元起预售");
        mMerchantPhone.setText(merchantDetailsTest.getResultData().getPhone());
        if (!distance.contains("*")) {
            mMerchantDistance.setText(distance);
        }

        mMerchantName.setText(merchantDetailsTest.getResultData().getMuname());
        mSubTitle.setText(merchantDetailsTest.getResultData().getMuname());
        mPhoneNumTv.setText(merchantDetailsTest.getResultData().getImgnum() + "张");
    }

    @Override
    public void bindListener() {
        mPhoneNum.setOnClickListener(this);
        layout_sub_daozheli.setOnClickListener(this);
        mSeeImgDetail.setOnClickListener(this);
        mShopCarLinear.setOnClickListener(this);
        photo_num_rela.setOnClickListener(this);
        mSubscribeBack.setOnClickListener(this);
        mPutOrder.setOnClickListener(this);
        mToComment.setOnClickListener(this);
        mJoinVips.setOnClickListener(this);
        rela_title.setOnClickListener(this);
        mVisibleComment.setOnClickListener(this);
        mMerchantDetail.setOnClickListener(this);
        youhui_rela.setOnClickListener(this);
        comment_tv.setOnClickListener(this);
        rl_multiple_shop.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.youhui_rela:
                // TODO 跳转到优惠劵的购买(判断是否登录)
                if (getFromSharePreference(Config.userConfig.is_Login).equals("Y")) {
                    Intent ins = new Intent(this, YouhuiBuyActivity.class);
                    ins.putExtra("pkmerchantcoupon", merchantDetailsTest.getResultData().getCouponMap().getPkmerchantcoupon());
                    ins.putExtra("pkcoupon", merchantDetailsTest.getResultData().getCouponMap().getPkcoupon());
                    ins.putExtra("pkmuser", pkmuser);
                    startActivity(ins);
                } else {
                    Intent intent = new Intent(SubscribeDishesActivity.this, LoginActivity.class);
                    intent.putExtra("loginsss", "Y");
                    startActivity(intent);
                }

                break;
            case R.id.merchant_detail_click:
                //点击后跳转到商家详情的H5页面中
                Intent merchantIntent = new Intent(this, MerchantDetailsActivity.class);
                merchantIntent.putExtra("pkmuser", pkmuser);
                startActivity(merchantIntent);
                break;

            case R.id.iv_seeImgDetail:

                Intent in = new Intent(this, MerchantPicActivity.class);
                in.putExtra("pkmuser", pkmuser);
                startActivity(in);
                break;
            case R.id.comment_tv:
                Intent commentIntent = new Intent(this, CommentActivity.class);
                commentIntent.putExtra("pkmuser", pkmuser);
                startActivity(commentIntent);
                break;


            case R.id.put_order:
                if (getFromSharePreference(Config.userConfig.is_Login).equals("Y")) {
                    if (carList.size() == 0) {
                        Toast.makeText(this, "请选择商品", Toast.LENGTH_LONG).show();
                    } else {
                        if (isVip) {

                            //查询节省金额
                            SaveMoneyBean saveMoneyBean = new SaveMoneyBean();
                            saveMoneyBean.setPkmuser(merchantDetailsTest.getResultData().getPkmuser());
                            saveMoneyBean.setPkregister(getFromSharePreference(pkregister));
                            List<ShoppingDetailList> shoppingDetailList = new ArrayList<>();
                            for (int i = 0; i < carList.size(); i++) {
                                ShoppingDetailList shop = new ShoppingDetailList();
                                shop.setPkproduct(carList.get(i).getPkId());
                                shop.setProductPrice(AES.encrypt(carList.get(i).getDishesPrice() + "", AES.key));
                                shop.setProductCount(carList.get(i).getDishesNum() + "");
                                shop.setEarnestMoney(AES.encrypt(carList.get(i).getDishesPayMent() + "", AES.key));
                                Log.e("jjjj", "主键:" + carList.get(i).getPkId() + "价格:" + carList.get(i).getDishesPrice() + "数量:" + carList.get(i).getDishesNum());
                                shoppingDetailList.add(shop);
                            }
                            saveMoneyBean.setShoppingDetailList(shoppingDetailList);
                            Map<String, String> saveParams = new HashMap<>();
                            String strjson = new Gson().toJson(saveMoneyBean);
                            Log.i("aaa", ">>>>>>>>>>" + strjson.toString());
                            saveParams.put("extralParam", strjson);
                            Wethod.httpPost(SubscribeDishesActivity.this, 3, Config.web.save_money, saveParams, this);
                        } else {
                            Toast.makeText(this, "请您加入商家会员", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Intent intent = new Intent(SubscribeDishesActivity.this, LoginActivity.class);
                    intent.putExtra("loginsss", "Y");
                    startActivity(intent);
                }


                break;
            case R.id.shop_car_linear:
                Popup popup = new Popup();
                popup.setPopup(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, mTitle, this);
                break;
            case R.id.photo_num_rela:
                Intent intent = new Intent(this, MerchantPicActivity.class);
                intent.putExtra("pkmuser", pkmuser);
                startActivity(intent);
                break;
            case R.id.subscribe_back:
                Intent intent01 = new Intent(this,MenuActivity.class);
                startActivity(intent01);
//                if (carList != null) {
//                    carList.clear();
//                }
//                //                if (distance.contains("*")) {
//                //                    startActivity(new Intent(this, MainActivity.class));
//                //                }
//                finish();
                break;
            case R.id.phone_num:
                Intent telIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(telIntent);
                break;
            case R.id.layout_sub_daozheli:
                String adress = getFromSharePreference(Config.userConfig.adress);
                String mudi = merchantDetailsTest.getResultData().getAddress();
                latitude = Double.parseDouble(getFromSharePreference(Config.userConfig.latu));
                longitude = Double.parseDouble(getFromSharePreference(Config.userConfig.lngu));
                if (!"".equals(latitudeFrom) && !"".equals(longitudeFrom)) {
                    BaiduUtil.Daozheli(SubscribeDishesActivity.this, latitude, longitude, Double.parseDouble(latitudeFrom), Double.parseDouble(longitudeFrom), adress, mudi);
                } else {
                    Toast.makeText(SubscribeDishesActivity.this, "暂未获取到商家经纬度", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.subscribe_join:
                if (getFromSharePreference(Config.userConfig.is_Login).equals("Y")) {
                    if (isVip) {
                        //充值
                        Intent intent1 = new Intent(this, TopupAmountActivity.class);
                        intent1.putExtra("pkmuser", pkmuser);
                        intent1.putExtra("FLAG", 1);
                        startActivity(intent1);
                    } else {
                        dialogShow();
                    }
                } else {
                    Intent intentLogin = new Intent(this, LoginActivity.class);
                    intentLogin.putExtra("loginsss", "Y");
                    //                    intentLogin.putExtra("pkmuser",pkmuser);
                    //                    intentLogin.putExtra("distance",distance);
                    startActivity(intentLogin);
                }


                break;
            case R.id.rela_title://结束本界面
                this.finish();
                break;
            /*case R.id.btn_goCommenttwo:
                startActivity(new Intent(SubscribeDishesActivity.this, CommentActivity.class));
                break;
            case R.id.btn_goCommentone:
                startActivity(new Intent(SubscribeDishesActivity.this, CommentActivity.class));
                break;*/
            case R.id.visible_comment:
                Intent visibleCommentIntent = new Intent(this, CommentActivity.class);
                visibleCommentIntent.putExtra("pkmuser", pkmuser);
                startActivity(visibleCommentIntent);
                break;

            case R.id.subscribe_account:
                Intent intent1 = new Intent();
                //这是写立即买单的点击
                if (is_half) {
                    intent1.setClass(this, RightAwayNewActivity.class);
                } else {
                    intent1.setClass(this, RightAwayActivity.class);
                }
                intent1.putExtra("muname", merchantDetailsTest.getResultData().getMuname());
                intent1.putExtra("pkmuser", pkmuser);
                intent1.putExtra("merchantName", merchantDetailsTest.getResultData().getMuname());
                intent1.putExtra("balance_mer",balance_mer);
                startActivity(intent1);
                break;
            case R.id.rl_multiple_shop:
                Intent intent2 = new Intent(this, MerchantListActivity.class);
//                LogUtils.e("longitudeFrom"+longitudeFrom+"latitudeFrom::"+latitudeFrom+"getVersion()::"+getVersion()+"linkage_pkdealer::"+linkage_pkdealer+"pkmuser::"+pkmuser);
                intent2.putExtra("pkmuser", pkmuser);
                intent2.putExtra("linkage_pkdealer", linkage_pkdealer);
                intent2.putExtra("versionCode", getVersion());
                intent2.putExtra("lng", getFromSharePreference(Config.userConfig.lngu));
                intent2.putExtra("lat", getFromSharePreference(Config.userConfig.latu));
                startActivity(intent2);
                break;
        }
    }

    protected void dialogShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您目前还不是vip,是否立即成为vip？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                isVipSetting();       //加入会员
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void isVipSetting() {
        //        "pkmuser:商家主键
        //        pkregister:用户主键
        //        name：用户姓名"

        Map<String, String> isVipParams = new HashMap<>();

        isVipParams.put("pkmuser", pkmuser);
        isVipParams.put("pkregister", getFromSharePreference(pkregister));
        isVipParams.put("name", getFromSharePreference(Config.userConfig.nickname));

        //判断当前用户是否是当前商家的会员
        Wethod.httpPost(SubscribeDishesActivity.this, 4, Config.web.join_merchantmember, isVipParams, this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (carList != null) {
                carList.clear();
            }
            //            if (distance.contains("*")) {
            //                startActivity(new Intent(this, MainActivity.class));
            //            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *
     * */
    private RelativeLayout relative_visible;//图片所在的布局
    private Boolean isScrolltoOneChild = false;//滑动坐标判断

    private int SRCOLLSTATE_TOUCH_SCROLL = 1;//正在滚动
    private int SRCOLLSTATE_STATE_FLING = 2;//抛 动作
    private int SRCOLLSTATE_STATE_IDLE = 0;//停止滑动

    /**
     * 商家图片
     */
    private Thread mThread;
    private float hei;
    private boolean isXianshi = true;
    //定义一个函数将dp转换为像素

    public int Dp2Px(Context context, float dp) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dp * scale + 0.5f);

    }

    public void srcolltoTop(Boolean isSro) {

        Animation scaleAnimation = null;
        if (!isSro && isXianshi) {

            Animation translateIn = new TranslateAnimation(0, 0, 0, -mMerchantBg.getHeight());
            translateIn.setDuration(100);
            mMerchantBg.startAnimation(translateIn);
            realtive_anim.startAnimation(translateIn);

            translateIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    //                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    //                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    mAdBg.setVisibility(View.GONE);
                    mTitle.setVisibility(View.GONE);
                    rela_title.setVisibility(View.VISIBLE);
                    isXianshi = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


        } else if (!isXianshi && isSro) {

            mAdBg.setVisibility(View.VISIBLE);
            mTitle.setVisibility(View.VISIBLE);
            rela_title.setVisibility(View.GONE);
            isXianshi = true;
        } else {
            Log.i("aaa", "这个并没有什么用");
        }
    }

    /**
     * 滑动距离
     */
    public int getScrollY() {
        View c = mRightListView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = mRightListView.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

    private int imgHeight = 181;
    private int tempIndex;

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == 1) {
            Log.e("GGGG", result.toString());
            try {
                merchantDetailsTest = getConfiguration().readValue(result.toString(), MerchantDetailsTest.class);
                isVipPkmuser = merchantDetailsTest.getResultData().getPkmuser();
                String payamount = merchantDetailsTest.getResultData().getCouponMap().getPayamount();
                String pkcoupon = merchantDetailsTest.getResultData().getCouponMap().getPkcoupon();
                Log.e("cess", "payamount:" + payamount);
                if (pkcoupon == null || pkcoupon.equals("")) {
                    youhui_rela.setVisibility(View.GONE);
                    youhui_line.setVisibility(View.GONE);
                } else {
                    youhui_rela.setVisibility(View.VISIBLE);
                    youhui_line.setVisibility(View.VISIBLE);
                    youhui_final.setText(merchantDetailsTest.getResultData().getCouponMap().getPayamount());
                    youhui_jieshao.setText(merchantDetailsTest.getResultData().getCouponMap().getRemark());
                    youhui_shiji.setText("元抵扣" + merchantDetailsTest.getResultData().getCouponMap().getValueamount() + "元");
                }
                Log.e("GGGG", "merchantDetailsTest" + merchantDetailsTest.toString());
                longitudeFrom = merchantDetailsTest.getResultData().getLongitude();
                Log.e("GGGG", longitudeFrom.toString());
                phone = merchantDetailsTest.getResultData().getPhone();
                latitudeFrom = merchantDetailsTest.getResultData().getLatitude();
                Log.e("liyunteee", "latitudeFrom" + latitudeFrom);
                Log.e("liyunteee", "latitudeFrom" + longitudeFrom);
                setData();
                //if (merchantDetailsTest.getResultData().getProductTypeList().size()<=0){
                //    ly_sub_dish_join.setVisibility(View.GONE);
                //}else {
                //    ly_sub_dish_join.setVisibility(View.VISIBLE);
                //}
                for (int i = 0; i < merchantDetailsTest.getResultData().getProductTypeList().size(); i++) {
                    dishesList.addAll(merchantDetailsTest.getResultData().getProductTypeList().get(i).getProductList());
                }
                detailsChooseAdater = new DetailsChooseAdater(dishesList, this, merchantDetailsTest.getResultData().getHeadpkmuser(), merchantDetailsTest.getResultData().getPreorderStartPrice(), merchantDetailsTest.getResultData().getMuname(), merchantDetailsTest.getResultData().getRechargeActivity());
                mRightListView.setAdapter(detailsChooseAdater);

                dishesLeftAdapter = new DishesLeftAdapter(this, merchantDetailsTest.getResultData().getProductTypeList());
                mLeftListView.setAdapter(dishesLeftAdapter);
                // dishChooseAdapter = new DishChooseAdapter(this,merchantDetailsTest.getResultData().getProductTypeList());
                // mRightListView.setAdapter(dishChooseAdapter);
                mLeftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        isScroll = false;
                        if (position == 0) {
                            isScrolltoOneChild = true;
                        } else {
                            isScrolltoOneChild = false;
                        }
                        for (int i = 0; i < mLeftListView.getChildCount(); i++) {
                            if (i == position) {
                                tempIndex = i;
                                srcolltoTop(false);
                                mHandler.sendEmptyMessage(1);
                            } else {
                                mLeftListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                                TextView tv = (TextView) mLeftListView.getChildAt(i).findViewById(R.id.left_tv);
                                tv.setTextColor(Color.rgb(153, 153, 153));
                            }
                        }
                        int rightSection = 0;
                        for (int m = 0; m < position; m++) {
                            // rightSection += dishChooseAdapter.getCountForSection(m) + 1;
                        }
                        rightSection = detailsChooseAdater.getPosition(merchantDetailsTest.getResultData().getProductTypeList().get(position).getPktypeid());
                        Log.e("tyz", "rightSection:" + rightSection);
                        mRightListView.setSelection(rightSection);
                        Log.i("aaa", "rightSection=" + rightSection);
                    }
                });
                mRightListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView arg0, int arg1) {
                        // if (mRightListView.getFirstVisiblePosition() == 0) {
                        //   srcolltoTop(true);
                        // }
                        // SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动
                        //第2次：scrollState = SCROLL_STATE_FLING(2) 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
                        //第3次：scrollState = SCROLL_STATE_IDLE(0) 停止滚动
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem,
                                         int visibleItemCount, int totalItemCount) {
                        Log.e("hhhhh", "FLAG_CLICK:" + FLAG_CLICK + "firstVisibleItem:" + firstVisibleItem);
                        //**  向下滑操作*/
                        if (getScrollY() == 0 && SRCOLLSTATE_STATE_IDLE == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                            if (isScrolltoOneChild) {
                                srcolltoTop(true);
                                isScrolltoOneChild = false;
                            }
                        } else if (SRCOLLSTATE_TOUCH_SCROLL == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL ||
                                SRCOLLSTATE_STATE_FLING == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                            isScrolltoOneChild = true;
                        } else {
                            isScrolltoOneChild = false;
                        }
                        if (isScroll) {
                            for (int i = 0; i < mLeftListView.getChildCount(); i++) {
                                if (dishesList.size() > 0) {
                                    if (dishesList.get(firstVisibleItem).getPktypeid().
                                            equals(merchantDetailsTest.getResultData().getProductTypeList().get(i).getPktypeid())) {
                                        TextView tv = (TextView) mLeftListView.getChildAt(i).findViewById(R.id.left_tv);
                                        mLeftListView.getChildAt(i).setBackgroundColor(Color.rgb(255, 255, 255));
                                        tv.setTextColor(Color.rgb(233, 88, 78));
                                    } else {
                                        TextView tv = (TextView) mLeftListView.getChildAt(i).findViewById(R.id.left_tv);
                                        mLeftListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                                        tv.setTextColor(Color.rgb(153, 153, 153));
                                    }
                                }
                            }
                        } else {
                            isScroll = true;
                        }
                    }
                });
            } catch (IOException e) {
                Log.e("xy", "eeee--->" + e.toString());
                e.printStackTrace();
            }
        } else if (reqcode == 2) {
            mLijiAccount.setOnClickListener(this);
            try {
                IsVipBean isVipBean = getConfiguration().readValue(result.toString(), IsVipBean.class);
                Log.e("aaa", "2resultData:" + isVipBean.getResultData().getIsfirst());
                if (isVipBean.getResultData().getIsfirst().equals("Y")) {
                    isVip = true;
                    //测试
                    mLijiAccount.setVisibility(View.VISIBLE);
                    mCommentShopname.setVisibility(View.VISIBLE);
                    first.setVisibility(View.VISIBLE);
                    // mLayoutPianyi.setVisibility(View.VISIBLE);
                    // Toast.makeText(this, "尊贵的VIP,欢迎回来", Toast.LENGTH_SHORT).show();
                    mSetFunction.setText("充\t值");
                    if (is_half) {
                        rel_at_join.setVisibility(View.GONE);
                        tv_jian.setText("支付立享五折");
                        youhui_rela.setVisibility(View.GONE);
                    }
                    sendRechageHyb();
                } else {
                    isVip = false;
                    mSetFunction.setText("加入会员");
                    if (distance.contains("0km")) {
                        isVipSetting();
                    }
                }
            } catch (IOException e) {
                Log.e("woyaokk", "eeeeee" + e.toString());
                e.printStackTrace();
            }
        } else if (reqcode == 3) {
            Log.i("aaa", "" + result.toString());
            try {
                SaveMoneyData saveMoneyData = getConfiguration().readValue(result.toString(), SaveMoneyData.class);
                if (saveMoneyData.getResultStatus().equals("0")) {
                    Intent intent = new Intent(this, YuYueActivity.class);
                    /*  "abatetime": 120,
                        "begintime": -3600000,
                        "endtime": 50400000,
                        "purchaserules": "如需发票,请您在消费时向商户咨询。此消费只能使用一次，过期不能使用。消费时如有其他金额变动，请与商家进行沟通补退差价。如未预约到店进行消费，如遇高峰期您可能需要排队。",
                        "moreservices": "WiFi",
                        "saveMoney": "9.05",
                        "effectiveTime": 1461678751882*/
                    saveMoneyListGoBean mBean = new saveMoneyListGoBean();
                    mBean.setEffectiveTime(saveMoneyData.getResultData().getEffectiveTime());
                    mBean.setAbatetime(saveMoneyData.getResultData().getAbatetime());
                    mBean.setSaveMoney(saveMoneyData.getResultData().getSaveMoney());
                    mBean.setMoreservices(saveMoneyData.getResultData().getMoreservices());
                    mBean.setPurchaserules(saveMoneyData.getResultData().getPurchaserules());
                    mBean.setBegintime(saveMoneyData.getResultData().getBegintime());
                    mBean.setEndtime(saveMoneyData.getResultData().getEndtime());

                    intent.putExtra("saveMoenyModle", (Serializable) mBean);
                    intent.putExtra("carlist", (Serializable) carList);
                    intent.putExtra("pkmuser", merchantDetailsTest.getResultData().getPkmuser());
                    if (totalMoney < 1) {
                        intent.putExtra("totalPrice", "0" + StringUtils.setScale(totalMoney + ""));
                    } else {
                        intent.putExtra("totalPrice", StringUtils.setScale(totalMoney + ""));
                    }
                    intent.putExtra("orderPhone", merchantDetailsTest.getResultData().getPhone());
                    intent.putExtra("totalEarnet", totalPayment + "");
                    intent.putExtra("merchantName", merchantDetailsTest.getResultData().getMuname());
                    startActivity(intent);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == 4) {
            // Map<String, String> isVipParams = new HashMap<>();
            // isVipParams.put("pkmuser", pkmuser);
            // isVipParams.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
            //  Wethod.httpPost(2, Config.web.is_vip, isVipParams, this);
            mSetFunction.setText("充\t值");
            isVip = true;
            mLijiAccount.setVisibility(View.VISIBLE);
            mCommentShopname.setVisibility(View.VISIBLE);
            first.setVisibility(View.VISIBLE);
            // mLayoutPianyi.setVisibility(View.VISIBLE);
            if (is_half) {
                rel_at_join.setVisibility(View.GONE);
                tv_jian.setText("支付立享五折");
                youhui_rela.setVisibility(View.GONE);
                mAccount.setVisibility(View.GONE);
            }else {
                mAccount.setVisibility(View.VISIBLE);
            }
            sendRechageHyb();
            mLijiAccount.setOnClickListener(this);
            Toast.makeText(this, "恭喜成为本店会员", Toast.LENGTH_SHORT).show();
            Log.i("aaa", "会员接口接入成功!!-->现在是会员了");
        } else if (reqcode == 5) {
            try {
                UserMechantInfoBean userMechantInfoBean = getConfiguration().readValue(result.toString(), UserMechantInfoBean.class);
                if (is_half) {
                    mAccount.setText("");
                }else {
                mAccount.setText("我的商家余额：" + userMechantInfoBean.getResultData().getBalance_mer() + "元");}
                balance_mer = userMechantInfoBean.getResultData().getBalance_mer();
                // flag 0不开启 1开启
                // if (userMechantInfoBean.getResultData().getFlag().toString().equals("1")) {
                // mSendMore.setText("充值" + userMechantInfoBean.getResultData().getAmount_recharge()
                // + "元送" + userMechantInfoBean.getResultData().getAmount_virtualmoney() + "积分");
                // mTishi.setVisibility(View.VISIBLE);
                // } else {
                // mSendMore.setText("充值享受充值价");
                // mTishi.setVisibility(View.GONE);
                // }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (reqcode == request_code_carousel) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            CarouselBean carouselBean = null;
            texts.clear();
            try {
                carouselBean = objectMapper.readValue(result.toString(), CarouselBean.class);
                for (int i = 0; i < carouselBean.getResultData().size(); i++) {
                    texts.add(new AdEntity(carouselBean.getResultData().get(i).getMsg(), "", ""));
                }
                if (texts.size() == 0 || texts == null || texts.equals("")) {
                    texts.add(new AdEntity("", "", ""));
                } else {
                    mSendMore.setTexts(texts);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(reqcode == 520){
//            LogUtils.e("====================="+result.toString());
//            ToastUtil.showToast(SubscribeDishesActivity.this,result);
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                DiplaybtnBean diplaybtnBean = objectMapper.readValue(result.toString(), DiplaybtnBean.class);
                linkage_pkdealer = diplaybtnBean.getResultData().getLinkage_pkdealer();
                display = diplaybtnBean.getResultData().isDisplay();
                if(display){
                    rl_multiple_shop.setVisibility(View.VISIBLE);
                }else {
                    rl_multiple_shop.setVisibility(View.GONE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {

        Log.i("aaa", "onFailed");
        if (reqcode == 1) {
            Wethod.ToFailMsg(this, result.toString());
        } else if (reqcode == 2) {
            Wethod.ToFailMsg(this, result.toString());
        } else if (reqcode == 3) {
            Wethod.ToFailMsg(this, result.toString());
        } else if (reqcode == 4) {
            Wethod.ToFailMsg(this, result.toString());
        } else if (reqcode == 5) {
            Wethod.ToFailMsg(this, result.toString());
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }


    public void postThreeWethod(Map<String, String> saveParams) {
        Wethod.httpPost(SubscribeDishesActivity.this, 3, Config.web.save_money, saveParams, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SubscribeDishes Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class Popup {
        private PopupWindow popupWindow;
        private LinearLayout other;
        private ImageView mDismissCar;
        private ListView mDishesCarList;
        private RelativeLayout mDeleteCar;
        private RelativeLayout car_true;

        private void setPopup(int width, int height, View parent, final Context context) {
            if (null != popupWindow && popupWindow.isShowing()) {
                popupWindow.dismiss();
                popupWindow = null;
            }
            //popupwindow的布局
            final View view = LayoutInflater.from(context).inflate(R.layout.shop_car_item, null);
            other = (LinearLayout) view.findViewById(R.id.other);
            mTotalPrice = (TextView) view.findViewById(R.id.shop_car_price);
            mDismissCar = (ImageView) view.findViewById(R.id.dismiss_car);
            mDishesCarList = (ListView) view.findViewById(R.id.dishes_details);
            mDeleteCar = (RelativeLayout) view.findViewById(R.id.delete_car_shop);
            car_true = (RelativeLayout) view.findViewById(R.id.car_true);
            // mTotalPrice.setText("¥ " + StringUtils.setScale(totalMoney + "") + "元")

            handler.sendEmptyMessage(2);

            final DishesCarAdapter dishesCarAdapter = new DishesCarAdapter(carList, SubscribeDishesActivity.this);
            mDishesCarList.setAdapter(dishesCarAdapter);
            car_true.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getFromSharePreference(Config.userConfig.is_Login).equals("Y")) {
                        if (isVip) {
                            //查询节省金额
                            SaveMoneyBean saveMoneyBean = new SaveMoneyBean();
                            saveMoneyBean.setPkmuser(merchantDetailsTest.getResultData().getPkmuser());
                            saveMoneyBean.setPkregister(getFromSharePreference(pkregister));
                            List<ShoppingDetailList> shoppingDetailList = new ArrayList<>();
                            for (int i = 0; i < carList.size(); i++) {
                                ShoppingDetailList shop = new ShoppingDetailList();
                                shop.setPkproduct(carList.get(i).getPkId());
                                shop.setProductPrice(AES.encrypt(carList.get(i).getDishesPrice() + "", AES.key));
                                shop.setProductCount(carList.get(i).getDishesNum() + "");
                                shop.setEarnestMoney(AES.encrypt(carList.get(i).getDishesPayMent() + "", AES.key));
                                Log.e("jjjj", "主键:" + carList.get(i).getPkId() + "价格:" + carList.get(i).getDishesPrice() + "数量:" + carList.get(i).getDishesNum());
                                shoppingDetailList.add(shop);
                            }
                            saveMoneyBean.setShoppingDetailList(shoppingDetailList);
                            Map<String, String> saveParams = new HashMap<>();
                            String strjson = new Gson().toJson(saveMoneyBean);
                            saveParams.put("extralParam", strjson);
                            postThreeWethod(saveParams);
                        } else {
                            Toast.makeText(SubscribeDishesActivity.this, "请您加入商家会员", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Intent intent = new Intent(SubscribeDishesActivity.this, LoginActivity.class);
                        intent.putExtra("loginsss", "Y");
                        startActivity(intent);
                    }
                }
            });

            mDeleteCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (carList.size() > 0) {
                        carList.clear();
                        SubscribeDishesActivity.totalMoney = 0;
                        SubscribeDishesActivity.totalPayment = 0;
                        dishesCarAdapter.notifyDataSetChanged();
                        view.postInvalidate();
                        if ((totalMoney + "").contains("0.0")) {
                            mCarTotalMoney.setText("¥ 0元");
                            mTotalPrice.setText("¥ 0元");
                        }
                        Log.i("aaa", "" + mCarTotalMoney.getText().toString() + ">>>" + totalMoney);
                        for (int i = 0; i < dishesList.size(); i++) {
                            detailsChooseAdater.numMap.put(i, 0);
                        }
                        detailsChooseAdater.notifyDataSetChanged();
                    }
                    popupWindow.dismiss();
                }
            });

            mDismissCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });

            view.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        popupWindow.dismiss();
                        return true;
                    }
                    return false;
                }
            });

            //            other.setOnTouchListener(new View.OnTouchListener() {
            //                @Override
            //                public boolean onTouch(View v, MotionEvent event) {
            //
            //                    return false;
            //                }
            //            });
            other.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    Log.i("aaa", "点击了空白");
                }
            });
            //加载布局
            popupWindow = new PopupWindow(view);

            popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
            popupWindow.setWidth(width);
            popupWindow.setHeight(height);
            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.update();
            popupWindow.showAsDropDown(parent);
        }
    }

    /*
    * 判断是否显示充值积分信息
    * */
    public void sendRechageHyb() {
        Map<String, String> isShowUserMachant = new HashMap<>();
        isShowUserMachant.put("pkmuser", pkmuser);
        isShowUserMachant.put("pkregister", getFromSharePreference(pkregister));

        Wethod.httpPost(SubscribeDishesActivity.this, 5, Config.web.get_UserMerchantInfo, isShowUserMachant, this);
    }


    public class DishesCarAdapter extends MyBaseAdapter<DishesCarBean> {


        private boolean is = false;

        public DishesCarAdapter(List<DishesCarBean> list, Context mContext) {
            super(list, mContext);
        }

        @Override
        public View initView(final int position, View convertView, ViewGroup parent) {

            final DishesCarHolder dishesCarHolder;
            if (convertView == null) {
                dishesCarHolder = new DishesCarHolder();
                convertView = inflater.inflate(R.layout.item_dishes_car, null);
                dishesCarHolder.mCarName = (TextView) convertView.findViewById(R.id.dishes_car_name);
                dishesCarHolder.mCarPrice = (TextView) convertView.findViewById(R.id.dishes_car_price);
                dishesCarHolder.mCarReduce = (ImageView) convertView.findViewById(R.id.dishes_car_reduce);
                dishesCarHolder.mCarType = (TextView) convertView.findViewById(R.id.dishes_car_type);
                dishesCarHolder.mCarNum = (TextView) convertView.findViewById(R.id.dishes_car_num);
                dishesCarHolder.mCarAdd = (ImageView) convertView.findViewById(R.id.dishes_car_add);
                convertView.setTag(dishesCarHolder);
            } else {
                dishesCarHolder = (DishesCarHolder) convertView.getTag();
            }
            dishesCarHolder.mCarName.setText(list.get(position).getDishesName());
            dishesCarHolder.mCarPrice.setText(list.get(position).getDishesPrice() + "元");
            dishesCarHolder.mCarNum.setText(list.get(position).getDishesNum() + "");
            dishesCarHolder.mCarType.setText(list.get(position).getType());

            if (is == false) {
                refreshView();
                is = true;
            }
            //此处用于保存数量 防止滑动时自动刷新
            for (int i = 0; i < dishesList.size(); i++) {
                if (list.get(position).getPkId().equals(dishesList.get(i).getPkproduct())) {
                    Log.e("TYZ", "detailsChooseAdater.numMap.get(i):" + detailsChooseAdater.numMap.get(i));
                    dishesCarHolder.mCarNum.setText(detailsChooseAdater.numMap.get(i) + "");
                }
            }

            dishesCarHolder.mCarAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < dishesList.size(); i++) {
                        if (list.get(position).getPkId().equals(dishesList.get(i).getPkproduct())) {
                            int num = detailsChooseAdater.numMap.get(position);
                            Log.e("woyaokk", "--->:" + num);
                            num++;
                            dishesCarHolder.mCarReduce.setVisibility(View.VISIBLE);
                            dishesCarHolder.mCarNum.setVisibility(View.VISIBLE);
                            dishesCarHolder.mCarNum.setText(num + "");

                            list.get(position).isClick = true;

                            carList.get(position).setDishesNum(num);

                            detailsChooseAdater.numMap.put(position, num);
                        }
                    }
                    refreshView();
                }
            });

            dishesCarHolder.mCarReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < dishesList.size(); i++) {
                        if (list.get(position).getPkId().equals(dishesList.get(i).getPkproduct())) {
                            int num = detailsChooseAdater.numMap.get(position);
                            Log.e("woyaokk", "--->:" + num);
                            if (num > 0) {
                                num--;
                                dishesCarHolder.mCarReduce.setVisibility(View.VISIBLE);
                                dishesCarHolder.mCarNum.setVisibility(View.VISIBLE);
                                dishesCarHolder.mCarNum.setText(num + "");

                                list.get(position).isClick = true;

                                carList.get(position).setDishesNum(num);
                                detailsChooseAdater.numMap.put(position, num);

                            } else {
                                dishesCarHolder.mCarReduce.setVisibility(View.GONE);
                                dishesCarHolder.mCarNum.setVisibility(View.GONE);
                                dishesCarHolder.mCarNum.setText(num + "");

                                list.get(position).isClick = true;

                                carList.get(position).setDishesNum(num);
                                detailsChooseAdater.numMap.put(position, num);
                            }

                        }
                    }
                    refreshView();

                }
            });
            return convertView;
        }


        public void refreshView() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SubscribeDishesActivity.totalMoney = 0;
                    SubscribeDishesActivity.totalPayment = 0;
                    Log.i("aaa", "11>>>totalMoney=" + totalMoney);
                    for (int i = 0; i < list.size(); i++) {
                        Log.e("aaa", "price" + list.get(i).getDishesPrice());
                        Log.i("aaa", "num=" + list.get(i).getDishesNum());
                        SubscribeDishesActivity.totalMoney += list.get(i).getDishesNum() * list.get(i).getDishesPrice();
                        SubscribeDishesActivity.totalPayment += list.get(i).getDishesNum() * list.get(i).getDishesPayMent();

                        Log.i("aaa", "222>>>totalMoney=" + totalMoney);
                    }
                    Log.e("TYZ", "************************" + SubscribeDishesActivity.totalMoney);
                    SubscribeDishesActivity.handler.sendEmptyMessage(2);
                }
            }).start();
            detailsChooseAdater.notifyDataSetChanged();
        }


        class DishesCarHolder {
            private TextView mCarName;
            private TextView mCarPrice;
            private TextView mCarType;
            private ImageView mCarReduce;
            private ImageView mCarAdd;
            private TextView mCarNum;
        }
    }


    /**********************************************************************************************************/
   /* *//*在启动当前APP的时候启动定位功能*//*
    private void startLocation() {
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
        locationHandler.sendEmptyMessage(MSG_LOCATION_START);
    }*/

/*
    *//*地址切换*//*
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null != aMapLocation) {
            Message msg = locationHandler.obtainMessage();
            msg.obj = aMapLocation;
            msg.what = MSG_LOCATION_FINISH;
            locationHandler.sendMessage(msg);
        }
    }*/


    /*Handler locationHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOCATION_START:
//                    tvReult.setText("正在定位...");
                    break;
                //定位完成
                case MSG_LOCATION_FINISH:
                    AMapLocation loc = (AMapLocation) msg.obj;
//                    String result = getLocationStr(loc);
//                    tvReult.setText(result);
                    LocationDingweiBean ld = getLocationStr(loc);
                    *//*SharedPreferenceUtils.saveToSharedPreference(WelComeActivity.this, "lng", ld.getmLng()+"");
                    SharedPreferenceUtils.saveToSharedPreference(WelComeActivity.this,"lat",ld.getmLat()+"");
                    SharedPreferenceUtils.saveToSharedPreference(WelComeActivity.this,"cityCode",ld.getCityCode());*//*
                    Log.e("DINGWEI", "ld.getCityCode():" + ld.getCityCode());
                    latitude = ld.getmLat();
                    longitude = ld.getmLng();
                    cityCode = ld.getCityCode();
//                    saveDataToSharePreference(Config.userConfig.lngu, ld.getmLng()+"");
//                    saveDataToSharePreference(Config.userConfig.latu,ld.getmLat()+"");
//                    saveDataToSharePreference(Config.userConfig.citycode,ld.getCityCode());
//                    Log.e("DINGWEI","Share:"+getFromSharePreference(Config.userConfig.citycode)+"   33"+getFromSharePreference(Config.userConfig.lngu));
                    if (ld.getCityCode() != null || !ld.getCityCode().equals("0")) {
                        locationClient.stopLocation();
                        locationHandler.sendEmptyMessage(MSG_LOCATION_STOP);
                    }
                    break;
                case MSG_LOCATION_STOP:
//                    tvReult.setText("定位停止");
                    break;
                default:
                    break;
            }
        }

        ;
    };*/

    /**
     * 根据定位结果返回定位信息的字符串
     *
     * @return
     */
  /*  private LocationDingweiBean getLocationStr(AMapLocation location) {

        LocationDingweiBean locationDingweiBean = new LocationDingweiBean();

        if (null == location) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if (location.getErrorCode() == 0) {
            sb.append("定位成功" + "\n");
            sb.append("定位类型: " + location.getLocationType() + "\n");
            sb.append("经    度    : " + location.getLongitude() + "\n");
            sb.append("纬    度    : " + location.getLatitudeLatitude() + "\n");
            sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
            sb.append("提供者    : " + location.getProvider() + "\n");
            locationDingweiBean.setmLng(location.getLongitude());
            locationDingweiBean.setmLat(location.getLatitude());

            if (location.getProvider().equalsIgnoreCase(
                    LocationManager.GPS_PROVIDER)) {
                // 以下信息只有提供者是GPS时才会有
                sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                sb.append("角    度    : " + location.getBearing() + "\n");
                // 获取当前提供定位服务的卫星个数
                sb.append("星    数    : "
                        + location.getSatellites() + "\n");
            } else {
                // 提供者是GPS时是没有以下信息的
                sb.append("国    家    : " + location.getCountry() + "\n");
                sb.append("省            : " + location.getProvince() + "\n");
                sb.append("市            : " + location.getCity() + "\n");
                sb.append("城市编码 : " + location.getCityCode() + "\n");
                sb.append("区            : " + location.getDistrict() + "\n");
                sb.append("区域 码   : " + location.getAdCode() + "\n");
                sb.append("地    址    : " + location.getAddress() + "\n");
                sb.append("兴趣点    : " + location.getPoiName() + "\n");

                locationDingweiBean.setCityCode(location.getCityCode());
            }
        } else {
            //定位失败
            sb.append("定位失败" + "\n");
            sb.append("错误码:" + location.getErrorCode() + "\n");
            sb.append("错误信息:" + location.getErrorInfo() + "\n");
            sb.append("错误描述:" + location.getLocationDetail() + "\n");
        }

        return locationDingweiBean;
    }*/
    private void getHttpCarousel() {
        Map<String, String> params = new HashMap<>();
        if (pkmuser.equals("") || pkmuser != null)
            params.put("pkmuser", pkmuser);
        Wethod.httpPost(this, request_code_carousel, Config.web.halfstore_recharge_carousel, params, this);
    }

    public void isMore(){
        Map<String, String> params = new HashMap<>();
        params.put("versionCode", getVersion());
        params.put("pkmuser", pkmuser);
        Wethod.httpPost(this, 520, Config.web.chain_stores_list_diplaybtn, params, this);
    }

    public String getVersion() {
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
}
