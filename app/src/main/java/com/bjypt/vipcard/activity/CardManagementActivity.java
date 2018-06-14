package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.Interface.PetroleumViewCallBackListener;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.CardMenusAdapter;
import com.bjypt.vipcard.adapter.OffineBusinessItemAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.CardBalanceBean;
import com.bjypt.vipcard.model.CardMenusBean;
import com.bjypt.vipcard.model.CitizenCardMerchantBean;
import com.bjypt.vipcard.model.CitizenCardMerchantData;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.widget.MyGridView;
import com.bjypt.vipcard.widget.PetroleumView;
import com.recker.flybanner.FlyBanner;
import com.sinia.orderlang.utils.StringUtil;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class CardManagementActivity extends BaseActivity implements VolleyCallBack<String> {

    private RelativeLayout back_card_management;                  //返回
    private PullToRefreshListView pull_card_management;
    private ListView listView;

    private TextView card_name;                                    //卡的名称
    private TextView card_number;                                 //卡号
    private TextView account_balance;                             //账户余额
    private TextView integral_card_management;                   //积分
    private TextView gifts_of_gold;                               //礼品金

    private LinearLayout go_pay;                                  //点击付款
    private LinearLayout banner_linear;                          //banner
    private LinearLayout ll_yue_change;

    private EditText search_merchant_name;
    private ImageView iv_search;
    private ImageView iv_card_pic;

    private String cardNum;                                       //卡号
    private String cardNumShow;
    private String cardName;                                      //卡名
    //    private String cardAmount;                                   //余额
//    private String cardVirtualAmount;                           //积分
//    private int cardStatus;                                     //卡的状态
    private int scope_type;                                    //卡类别
    private String card_pic;                                    //卡图片

    private FlyBanner card_manager_banner;                     //广告bannerf
    private ArrayList<String> banner_list = new ArrayList<>();//banner广告数据

    private static int request_merchant_list = 1110;
    private static final int request_system_card_info = 1101;
    private static final int request_card_menus = 20180312;

    private List<CitizenCardMerchantData> citizenCardMerchantDatas = new ArrayList<>();
    OffineBusinessItemAdapter adapter;
    private TextView tv_can_withdraw;
    private CardBalanceBean cardBalanceBean;
    private String amount;
    private String showCardNum;
    private String pkmuser;
    private String muname;
    private int categoryId;
    private TextView tv_zhanghu_yu_e;
    private RelativeLayout enter_pay_relative;
    private View view_use;
    private PetroleumView pv_poster;

    //中间按钮菜单
    private MyGridView myGridView;
    private CardMenusBean cardMenusBean;
    private RelativeLayout search_card_manage;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_card_management);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();

        cardName = intent.getStringExtra("cardName");
        showCardNum = intent.getStringExtra("showCardNum");
        cardNum = intent.getStringExtra("cardNum");
        scope_type = intent.getIntExtra("scope_type", -1);
        card_pic = intent.getStringExtra("card_pic");
        categoryId = intent.getIntExtra("categoryId", -1);
        Log.e("yang1", "onItemClick: " + categoryId);
        requestCardBalance();
        requestCardMenus();     //菜单请求
    }

    private void searchMerchantList() {
        Map<String, String> params1 = new HashMap<>();
        params1.put("cardnum", cardNum);
        params1.put("merchantName", search_merchant_name.getText().toString());
        params1.put("cityCode", SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.citycode));
        params1.put("lng", SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.lngu));
        params1.put("lat", SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.latu));
        Wethod.httpPost(CardManagementActivity.this, request_merchant_list, Config.web.card_merchant_list, params1, this, View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                requestCardBalanceAgain();
                return false;
            }
        }).sendEmptyMessageDelayed(0, 2000);

        requestCardMenus();
    }

    @Override
    public void initView() {
        back_card_management = (RelativeLayout) findViewById(R.id.back_card_management);
        //pullToRefreshListView
        pull_card_management = (PullToRefreshListView) findViewById(R.id.pull_card_management);
        View headerView = LayoutInflater.from(CardManagementActivity.this).inflate(R.layout.card_management_head, null, false);
        pull_card_management.setMode(PullToRefreshBase.Mode.DISABLED);
        listView = pull_card_management.getRefreshableView();
        listView.setDividerHeight(0);
        listView.addHeaderView(headerView);
        adapter = new OffineBusinessItemAdapter(this, citizenCardMerchantDatas);
        pull_card_management.setAdapter(adapter);
        //卡信息
        card_name = (TextView) headerView.findViewById(R.id.card_name);
        ll_yue_change = (LinearLayout) headerView.findViewById(R.id.ll_yue_change);
        account_balance = (TextView) headerView.findViewById(R.id.account_balance);
        integral_card_management = (TextView) headerView.findViewById(R.id.integral_card_management);
        card_number = (TextView) headerView.findViewById(R.id.card_number);
        pv_poster = (PetroleumView) headerView.findViewById(R.id.pv_poster);
        gifts_of_gold = (TextView) headerView.findViewById(R.id.gifts_of_gold);
        iv_card_pic = (ImageView) headerView.findViewById(R.id.iv_card_pic);
        go_pay = (LinearLayout) headerView.findViewById(R.id.go_pay);
        tv_can_withdraw = (TextView) headerView.findViewById(R.id.tv_can_withdraw);
        tv_zhanghu_yu_e = (TextView) headerView.findViewById(R.id.tv_zhanghu_yu_e);
        enter_pay_relative = (RelativeLayout) headerView.findViewById(R.id.enter_pay_relative);
        view_use = headerView.findViewById(R.id.view_use);
        search_card_manage = (RelativeLayout) headerView.findViewById(R.id.search_card_manage);

        //banner广告
        card_manager_banner = (FlyBanner) headerView.findViewById(R.id.card_manager_banner);
        banner_linear = (LinearLayout) headerView.findViewById(R.id.banner_linear);

        //充值 消费记录 解挂 线上商家 余额转出
        search_merchant_name = (EditText) headerView.findViewById(R.id.search_merchant_name);
        iv_search = (ImageView) headerView.findViewById(R.id.iv_search);

        //卡管理菜单显示
        myGridView = (MyGridView) headerView.findViewById(R.id.my_gridview);

        //   scope_type = 0   0: 全部商家， 1:指定商家
        if (scope_type == 0) {
            tv_can_withdraw.setVisibility(View.VISIBLE);
            tv_zhanghu_yu_e.setText("平台余额");
        } else {
            tv_can_withdraw.setVisibility(View.GONE);
            tv_zhanghu_yu_e.setText("卡余额");
        }

        //卡信息设置值
        //让Android支持自定义的ttf字体
//        Typeface fontFace = Typeface.createFromAsset(this.getAssets(),
//                "fonts/citizen_card_text.ttf");
//        card_number.setTypeface(fontFace);
        //123 456 789 123
        if (cardNum.length() == 12) {
            String num1 = cardNum.substring(0, 3);
            String num2 = cardNum.substring(3, 6);
            String num3 = cardNum.substring(6, 9);
            String num4 = cardNum.substring(9, 12);
            cardNumShow = num1 + " " + num2 + " " + num3 + " " + num4 + " ";
            card_number.setText(cardNumShow);
        } else {
            card_number.setText(cardNum);
        }

        card_name.setText(cardName);

        Picasso.with(this)
                .load(Config.web.picUrl + card_pic)
                .error(R.mipmap.more)
                .into(iv_card_pic);
//        ImageLoader.getInstance().displayImage(Config.web.picUrl + card_pic, iv_card_pic, AppConfig.DEFAULT_IMG_CITIZEN_CARD);
        pv_poster.setPetroleumViewCallBackListener(new PetroleumViewCallBackListener() {
            @Override
            public void hideView() {
//                rl_my_dikou.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void afterInitView() {
        searchMerchantList();
    }

    @Override
    public void bindListener() {
        search_card_manage.setOnClickListener(this);
        view_use.setOnClickListener(this);
        enter_pay_relative.setOnClickListener(this);
        go_pay.setOnClickListener(this);
        back_card_management.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        ll_yue_change.setOnClickListener(this);

        search_merchant_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int arg1, KeyEvent keyEvent) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    Random random = new Random(System.currentTimeMillis());
                    request_merchant_list = random.nextInt(1000000) + 9999;
                    searchMerchantList();
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 2) {
                    Intent intent = new Intent(CardManagementActivity.this, NewSubscribeDishesActivity.class);
                    intent.putExtra("pkmuser", citizenCardMerchantDatas.get(i - 2).getPkmuser());
                    intent.putExtra("categoryid", categoryId + "");
                    startActivity(intent);
                }
            }
        });
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (cardMenusBean.getResultData().get(i).getId()) {
                    case 0:         //卡充值
                        //   scope_type = 0   0: 全部商家 跳平台充值， 1:指定商家 跳商家充值
                        if (scope_type == 0) {
                            Intent intents = new Intent(CardManagementActivity.this, NewTopupWayActivity.class);
                            intents.putExtra("FLAG", 2);
                            intents.putExtra("pkmuser", pkmuser);
                            intents.putExtra("cardnum", cardNum);
                            startActivity(intents);
                        } else {
                            Intent intentGo = new Intent(CardManagementActivity.this, OneKeyTopupAmountActivity.class);
                            intentGo.putExtra("pkmuser", pkmuser);
                            intentGo.putExtra("FLAG", 1);
                            intentGo.putExtra("cardnum", cardNum);
                            intentGo.putExtra("categoryid", categoryId + "");
                            intentGo.putExtra("muname", muname);
                            startActivity(intentGo);
                        }
                        break;
                    case 1:        //“消费记录”
                        Intent intent = new Intent(CardManagementActivity.this, LifeServireH5Activity.class);
                        intent.putExtra("life_url", Config.web.citizen_card_expense_calendar + "?type_main=7" + "&cardnum=" + cardNum + "&pkregister=");
                        intent.putExtra("isLogin", "Y");
                        intent.putExtra("isallurl", "Y");
//                        intent.putExtra("serviceName", "消费记录");
                        intent.putExtra("serviceName", cardMenusBean.getResultData().get(i).getName());
                        startActivity(intent);
                        break;
                    case 2:        //“礼品金纪录”
                        Intent intentGifts = new Intent(CardManagementActivity.this, LifeServireH5Activity.class);
                        intentGifts.putExtra("life_url", Config.web.citizen_card_gifts + "?type_main=9" + "&pkregister=");
                        intentGifts.putExtra("isLogin", "Y");
                        intentGifts.putExtra("isallurl", "Y");
//                        intentGifts.putExtra("serviceName", "礼品金记录");
                        intentGifts.putExtra("serviceName", cardMenusBean.getResultData().get(i).getName());
                        startActivity(intentGifts);
                        break;
                    case 3:        //“余额转出”
                        Intent intentRollOut = new Intent(CardManagementActivity.this, BalanceOutActivity.class);
                        intentRollOut.putExtra("pkmuser", pkmuser);
                        intentRollOut.putExtra("cardnum", cardNum);
                        if (("").equals(cardBalanceBean) || null == cardBalanceBean) {
                            amount = "0.00";
                        } else {
                            amount = cardBalanceBean.getResultData().getAmount();
                        }
                        intentRollOut.putExtra("amount", amount);
                        startActivity(intentRollOut);
                        break;
                    case 4:        //”积分兑换”
                        Intent intentDikou = new Intent(CardManagementActivity.this, H5DeductionActivity.class);
                        startActivity(intentDikou);
                        break;
                    case 5:        //”抵扣金说明”
                        Intent intent3 = new Intent(CardManagementActivity.this, LifeServireH5Activity.class);
                        intent3.putExtra("life_url", Config.web.citizen_card_usedesc);
                        intent3.putExtra("isLogin", "N");
                        intent3.putExtra("isallurl", "Y");
//                        intent3.putExtra("serviceName", "抵扣金说明");
                        intent3.putExtra("serviceName", cardMenusBean.getResultData().get(i).getName());
                        startActivity(intent3);
                        break;
                    case 6:        //”线上商家”
                        //跳店铺街
                        setResult(RESULT_OK);
                        finish();
                        break;
                    case 7:        //”我提款”
                        Intent intent4 = new Intent(CardManagementActivity.this, WithdrawActivity.class);
                        startActivity(intent4);
                        break;
                    case 8:        // 8,"卡挂失"
                        Intent intent1 = new Intent(CardManagementActivity.this, LifeServireH5Activity.class);
                        intent1.putExtra("life_url", Config.web.citizen_card_dissolving + "?cardnum=" + cardNum + "&pkregister=");
                        intent1.putExtra("isLogin", "Y");
                        intent1.putExtra("isallurl", "Y");
                        intent1.putExtra("serviceName", "挂失");
                        startActivity(intent1);
                        break;
                    case 9:        //9,"解除挂失"
                        Intent intent2 = new Intent(CardManagementActivity.this, LifeServireH5Activity.class);
                        intent2.putExtra("life_url", Config.web.citizen_card_undissolving + "?cardnum=" + cardNum + "&pkregister=");
                        intent2.putExtra("isLogin", "Y");
                        intent2.putExtra("isallurl", "Y");
                        intent2.putExtra("serviceName", "解挂");
                        startActivity(intent2);
                        break;

                }
            }
        });
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.search_card_manage:
                Intent searchMerchantIntent = new Intent(CardManagementActivity.this, SearchMerchantActivity.class);
                startActivity(searchMerchantIntent);
                break;
            case R.id.view_use:
                Intent intentUse = new Intent(this, LifeServireH5Activity.class);
                intentUse.putExtra("life_url", Config.web.cardUseIllustrate + categoryId);
                intentUse.putExtra("isLogin", "N");
                intentUse.putExtra("serviceName", "卡使用说明");
                startActivity(intentUse);
                break;
            case R.id.enter_pay_relative:
            case R.id.go_pay:                                  //点击付款
                Intent intentGopay = new Intent(CardManagementActivity.this, GoPayCardManageActivity.class);
                intentGopay.putExtra("cardnum", cardNum);
                intentGopay.putExtra("pkmuser", pkmuser);
                intentGopay.putExtra("muname", cardName);
                startActivity(intentGopay);
                break;
            case R.id.iv_search:                               //商家搜索
                searchMerchantList();
                break;
            case R.id.back_card_management:                   //返回
                finish();
                break;
            case R.id.ll_yue_change:
//                Intent intent5 = new Intent(CardManagementActivity.this, LifeServireH5Activity.class);
//                intent5.putExtra("life_url", Config.web.yue_change + pkmuser);
//                intent5.putExtra("isLogin", "N");
//                intent5.putExtra("serviceName", "余额变动记录");
//                startActivity(intent5);
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == request_card_menus) {            //卡管理界面菜单
            try {
                cardMenusBean = getConfiguration().readValue(result.toString(), CardMenusBean.class);
                CardMenusAdapter adapter = new CardMenusAdapter(cardMenusBean.getResultData(), CardManagementActivity.this);
                myGridView.setAdapter(adapter);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == request_merchant_list) {
            try {
                citizenCardMerchantDatas.clear();
                CitizenCardMerchantBean citizenCardMerchantBean = getConfiguration().readValue(result.toString(), CitizenCardMerchantBean.class);
                citizenCardMerchantDatas.addAll(citizenCardMerchantBean.getResultData());
                adapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == request_system_card_info) {
            LogUtil.debugPrint("cardManagement" + result);
            try {
                cardBalanceBean = getConfiguration().readValue(result.toString(), CardBalanceBean.class);

                muname = cardBalanceBean.getResultData().getMuname();
                pkmuser = cardBalanceBean.getResultData().getPkmuser();
                pv_poster.requestPetroleumEntrance(CardManagementActivity.this, pkmuser, categoryId + "", cardNum);
                int withdraw_power = cardBalanceBean.getResultData().getWithdraw_power();


                //banner广告  没有时不显示
                if (banner_list.size() > 0) {
                    banner_list.clear();
                }
                for (int i = 0; i < cardBalanceBean.getResultData().getAdlist().size(); i++) {
                    banner_list.add(Config.web.ImgURL + cardBalanceBean.getResultData().getAdlist().get(i).getAdurl());
                }
                if (banner_list.size() > 0) {
                    banner_linear.setVisibility(View.VISIBLE);
                    card_manager_banner.setImagesUrl(banner_list);
                } else {
                    banner_linear.setVisibility(View.GONE);
                }

                card_manager_banner.setOnItemClickListener(new FlyBanner.OnItemClickListener() {

                    @Override
                    public void onItemClick(int position) {
                        if (banner_list.size() > 0) {
                            if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                                if (StringUtil.isNotEmpty(cardBalanceBean.getResultData().getAdlist().get(position).getLinkurl())) {
                                    Intent intent = new Intent(CardManagementActivity.this, LifeServireH5Activity.class);
                                    intent.putExtra("life_url", cardBalanceBean.getResultData().getAdlist().get(position).getLinkurl());
                                    intent.putExtra("isLogin", "Y");
                                    intent.putExtra("isallurl", "Y");
                                    intent.putExtra("serviceName", "详情");
                                    startActivity(intent);
                                }
                            } else {
                                startActivity(new Intent(CardManagementActivity.this, LoginActivity.class));
                            }
                        }
                    }
                });

                //账户余额
                if (("").equals(cardBalanceBean.getResultData().getAmount()) || null == cardBalanceBean.getResultData().getAmount()) {
                    account_balance.setText("0.00元");
                } else {
                    account_balance.setText(cardBalanceBean.getResultData().getAmount() + "元");
                }
                //积分
                if (("").equals(cardBalanceBean.getResultData().getVirtual_amount()) || null == cardBalanceBean.getResultData().getVirtual_amount()) {
                    integral_card_management.setText("0.00元");
                } else {
                    integral_card_management.setText(cardBalanceBean.getResultData().getVirtual_amount() + "元");
                }
                //礼品金
                if (("").equals(cardBalanceBean.getResultData().getCard_gift()) || null == cardBalanceBean.getResultData().getCard_gift()) {
                    gifts_of_gold.setText("0.00元");
                } else {
                    gifts_of_gold.setText(cardBalanceBean.getResultData().getCard_gift() + "元");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {
        if (reqcode == request_system_card_info) {
            try {
                cardBalanceBean = getConfiguration().readValue(result.toString(), CardBalanceBean.class);
                if (cardBalanceBean.getResultData() != null) {
                    ToastUtil.showToast(this, cardBalanceBean.getResultData().toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    public void requestCardBalance() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        params.put("cardnum", cardNum);
        Wethod.httpPost(CardManagementActivity.this, request_system_card_info, Config.web.card_balance, params, this);
    }

    public void requestCardBalanceAgain() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        params.put("cardnum", cardNum);
        Wethod.httpPost(CardManagementActivity.this, request_system_card_info, Config.web.card_balance, params, this, View.GONE);
    }

    //菜单请求
    public void requestCardMenus() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        params.put("cardnum", cardNum);
        Wethod.httpPost(CardManagementActivity.this, request_card_menus, Config.web.card_menus, params, this, View.GONE);
    }
}
