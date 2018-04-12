package com.bjypt.vipcard.fragment;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bjypt.vipcard.NewBindBankCardActivity;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.BalanceEarningsActivity;
import com.bjypt.vipcard.activity.BillDetailsActivity;
import com.bjypt.vipcard.activity.CitizenCardListActivity;
import com.bjypt.vipcard.activity.CouponActivity;
import com.bjypt.vipcard.activity.DeductionStoreActivity;
import com.bjypt.vipcard.activity.FeePointActivity;
import com.bjypt.vipcard.activity.FinancingActivity;
import com.bjypt.vipcard.activity.FriendsCircleActivity;
import com.bjypt.vipcard.activity.GoConversionActivity;
import com.bjypt.vipcard.activity.H5DeductionActivity;
import com.bjypt.vipcard.activity.H5ReturnActivity;
import com.bjypt.vipcard.activity.HuiyuanbiRecordActivity;
import com.bjypt.vipcard.activity.LifeServireH5Activity;
import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.activity.MainActivity;
import com.bjypt.vipcard.activity.MyOrderActivity;
import com.bjypt.vipcard.activity.MyQRCodeActivity;
import com.bjypt.vipcard.activity.NewTopupWayActivity;
import com.bjypt.vipcard.activity.NewsActivity;
import com.bjypt.vipcard.activity.PointRecordActivity;
import com.bjypt.vipcard.activity.RechargeAccountActivity;
import com.bjypt.vipcard.activity.RechargeRecordActivity;
import com.bjypt.vipcard.activity.RedPacketActivity;
import com.bjypt.vipcard.activity.SystemSettingActivity;
import com.bjypt.vipcard.activity.UnPayOrderActivity;
import com.bjypt.vipcard.activity.UserRecordActivity;
import com.bjypt.vipcard.activity.VipCenterAccountActivity;
import com.bjypt.vipcard.activity.WithdrawActivity;
import com.bjypt.vipcard.activity.YuEBaoActivity;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.SignINBean;
import com.bjypt.vipcard.model.SignInGongGaoBean;
import com.bjypt.vipcard.model.SystemInfomationBean;
import com.bjypt.vipcard.model.UpdataHeadBean;
import com.bjypt.vipcard.receiver.RegisterReceiverUtils;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.DialogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.PermissionUtils;
import com.bjypt.vipcard.view.RoundImageView;
import com.brioal.adtextviewlib.entity.AdEntity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bjypt.vipcard.common.Config.userConfig.nickname;
import static com.bjypt.vipcard.common.Config.userConfig.pkregister;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/22
 * Use by 主界面我的Fragment
 */
public class MyFragment extends BaseFrament implements View.OnClickListener, VolleyCallBack {

    private RoundImageView roundIV_my_photo_moren;
    private View view;
    private SelectPhoto selectPhoto;
    private LinearLayout bt_my_myself;                          //我的
    private RelativeLayout ll_phone_number;                      // 拨打客服热线
    private LinearLayout layout_bg;                             //我的
    private RelativeLayout bt_my_lingdang;                     //右上角的铃铛图标
    private RoundImageView roundIV_my_photo;
    private ImageView bt_my_login;                                //登录按钮
    private TextView telphone;                                 //电话号码  默认隐藏
    private RelativeLayout my_order;                           //我的订单
    private RelativeLayout order_to_be_used;                  //待使用
    private RelativeLayout ry_my_return_record;              //返还记录
    private RelativeLayout order_to_be_paid;                  //待支付
    private RelativeLayout order_to_be_evaluated;            //待评价
    private RelativeLayout coupon_button;                     //优惠券
    private RelativeLayout hongbao;                           //红包
    private RelativeLayout integral_button;                  //积分
    private RelativeLayout my_zichan;                        //我的资产
    private RelativeLayout zd_detail;                        //账单明细
    private RelativeLayout my_QR_Code;                       //我的二维码
    private RelativeLayout mDeductionShop;                  //抵扣商城
    private RelativeLayout myBenefit;                       //我的分润
    private RelativeLayout phone_zhaoshang;                //招商热线
    private ImageView iv_news_red_point;                   //消息提醒红点
    private String imageUrl;                                //本地图片url

    private static final int PHOTO_REQUEST_CAMERA = 1;   // 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;  // 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;      // 结果
    private static final int CONVERSION = 1001;           // 商家立即买单去兑换

    public static final String IMAGE_UNSPECIFIED = "image/*";
    private Uri imageUri = null;                           //图片URI地址
    private Bitmap photo = null;
    private String imageFileName, filepath, str_datePic;
    private String backUrl;
    private Time time_gameImg;


    private RelativeLayout sec_kill_rela, some_city_rela;
    // 存储图片的路径
    private String PATH = Environment.getExternalStorageDirectory() + "/vipcard/Camera/";
    private BroadCastReceiverUtils utils;

    // private MyBroadcastReceiver receiver;
    // private NewsBroastCastReceiver receiver_news;

    private TextView tv_my_vip_bi;                        //积分
    private TextView tv_my_zhanghu_yu_e;                 //积分
    private View sales_line;
    private RelativeLayout ry_my_tixian_detail;
    private RelativeLayout rl_my_chognzhi_detail;
    private RelativeLayout ry_my_bi_detail;
    private RelativeLayout rl_friends; //朋友圈
    private RelativeLayout ry_my_yue_zenyi_detail;
    private SystemInfomationBean systemInfomationBean;
    private RelativeLayout yu_ebao;
    private RelativeLayout rl_sales;                   // 推广人收益
    private LinearLayout rl_my_dikou;                  // 抵扣商城
    private ImageView goConversion;                      //去兑换  兑换积分
    private RelativeLayout citizen_card_bind;           //市民卡绑定
    private RelativeLayout balance_of_account;          //账户余额
    private RelativeLayout hui_yuan_bi;                  //积分
    private ImageView iv_zhanghu;                        //购买理财

    private RelativeLayout linear_vc_myaccount;           //余额宝
    private LinearLayout linear_vc_tonghuaInfo;         //我要提现
    private LinearLayout linear_vc_keyong_account;     //我要充值
    private LinearLayout linear_vc_binding_bankcard;   //绑定银行卡
    private LinearLayout one_card;                       //一卡通
    private TextView my_name;                            //昵称
    private LinearLayout my_message;                     //头像信息
    private TextView my_sign_in;                        //签到
    private TextView continue_days;                     //连续签到天数
//    private AdTextViewMult scroll_textview;      //系统公告
    private static final int SIGN_IN_GONGGAO = 1124;

    private int[] images = {R.mipmap.withdraw_deposit_record, R.mipmap.withdraw_deposit_record,
            R.mipmap.recharge_record, R.mipmap.shape, R.mipmap.generalize, R.mipmap.attract_investment};
    private String[] imagesName = {"交易记录", "提现记录", "充值记录", "推荐给朋友", "推广受益人", "招商客服热线(08:00~17:00)"};
    private List<Map<String, Object>> data_list;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    tv_my_zhanghu_yu_e.setText(systemInfomationBean.getResultData().getBalance() + "元");
                    tv_my_vip_bi.setText(sub(stringToDouble(systemInfomationBean.getResultData().getWholebalance()), stringToDouble(systemInfomationBean.getResultData().getBalance())) + "");
                    break;
            }
        }
    };

    private MyBroadcastReceiver mReceiver;
    private SimpleAdapter simpleAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fra_my, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void beforeInitView() {

        if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
            if (!"".equals(getFromSharePreference(Config.userConfig.nickname))) {
                my_name.setText(getFromSharePreference(Config.userConfig.nickname));
            }
            if (!"".equals(getFromSharePreference(Config.userConfig.phoneno))) {
                String phone = getFromSharePreference(Config.userConfig.phoneno);
                String result = String.format(getResources().getString(R.string.my_account_number)
                        ,phone.substring(0, 3) + "****" + phone.substring(7));
                telphone.setText(result);
            }

        } else {
            my_name.setText("");
            telphone.setText("账号：");
        }

        Wethod.configImageLoader(getActivity());
        utils = new BroadCastReceiverUtils();
        mReceiver = new MyBroadcastReceiver();
        utils.registerBroadCastReceiver(getActivity(), "updatephoto", mReceiver);  //注册广播
        requestGongGao();
    }

    @Override
    public void initView() {
        selectPhoto = new SelectPhoto();
        roundIV_my_photo_moren = (RoundImageView) view.findViewById(R.id.roundIV_my_photo_moren);
        sales_line = view.findViewById(R.id.sales_line);
//        scroll_textview = (AdTextViewMult) view.findViewById(R.id.scroll_textview);
        continue_days = (TextView) view.findViewById(R.id.continue_days);
        my_sign_in = (TextView) view.findViewById(R.id.my_sign_in);
        my_message = (LinearLayout) view.findViewById(R.id.my_message);
        my_name = (TextView) view.findViewById(R.id.my_name);
        one_card = (LinearLayout) view.findViewById(R.id.one_card);
        bt_my_myself = (LinearLayout) view.findViewById(R.id.bt_my_myself);
        layout_bg = (LinearLayout) view.findViewById(R.id.layout_bg);
        bt_my_lingdang = (RelativeLayout) view.findViewById(R.id.bt_my_lingdang);
        bt_my_login = (ImageView) view.findViewById(R.id.bt_my_login);
        roundIV_my_photo = (RoundImageView) view.findViewById(R.id.roundIV_my_photo);
        telphone = (TextView) view.findViewById(R.id.telphone);
        my_order = (RelativeLayout) view.findViewById(R.id.my_order);
        rl_sales = (RelativeLayout) view.findViewById(R.id.rl_sales);
        rl_friends = (RelativeLayout) view.findViewById(R.id.rl_friends);
        order_to_be_used = (RelativeLayout) view.findViewById(R.id.order_to_be_used);
        order_to_be_evaluated = (RelativeLayout) view.findViewById(R.id.order_to_be_evaluated);
        order_to_be_paid = (RelativeLayout) view.findViewById(R.id.order_to_be_paid);
        coupon_button = (RelativeLayout) view.findViewById(R.id.coupon_button);
        hongbao = (RelativeLayout) view.findViewById(R.id.hongbao);
        integral_button = (RelativeLayout) view.findViewById(R.id.integral_button);
        my_zichan = (RelativeLayout) view.findViewById(R.id.my_zichan);
        ry_my_bi_detail = (RelativeLayout) view.findViewById(R.id.ry_my_bi_detail);
        iv_news_red_point = (ImageView) view.findViewById(R.id.iv_news_red_point);

        sec_kill_rela = (RelativeLayout) view.findViewById(R.id.sec_kill_rela);
        some_city_rela = (RelativeLayout) view.findViewById(R.id.some_city_rela);
        zd_detail = (RelativeLayout) view.findViewById(R.id.zd_detail);
        my_QR_Code = (RelativeLayout) view.findViewById(R.id.my_QR_Code);
        mDeductionShop = (RelativeLayout) view.findViewById(R.id.my_DeductionShop);
        myBenefit = (RelativeLayout) view.findViewById(R.id.my_benefit);
        ry_my_tixian_detail = (RelativeLayout) view.findViewById(R.id.ry_my_tixian_detail);
        rl_my_chognzhi_detail = (RelativeLayout) view.findViewById(R.id.rl_my_chognzhi_detail);
        ry_my_yue_zenyi_detail = (RelativeLayout) view.findViewById(R.id.ry_my_yue_zenyi_detail);
        ry_my_bi_detail = (RelativeLayout) view.findViewById(R.id.ry_my_bi_detail);
        tv_my_vip_bi = (TextView) view.findViewById(R.id.tv_my_vip_bi);
        tv_my_zhanghu_yu_e = (TextView) view.findViewById(R.id.tv_my_zhanghu_yu_e);
        yu_ebao = (RelativeLayout) view.findViewById(R.id.yu_ebao);
        ll_phone_number = (RelativeLayout) view.findViewById(R.id.ll_phone_number);
        ry_my_return_record = (RelativeLayout) view.findViewById(R.id.ry_my_return_record);
        rl_my_dikou = (LinearLayout) view.findViewById(R.id.rl_my_dikou);

        goConversion = (ImageView) view.findViewById(R.id.iv_go_conversion);                 //去兑换，兑换积分
        citizen_card_bind = (RelativeLayout) view.findViewById(R.id.citizen_card_bind);     //市民卡绑定
        balance_of_account = (RelativeLayout) view.findViewById(R.id.balance_of_account);   //账户余额
        hui_yuan_bi = (RelativeLayout) view.findViewById(R.id.hui_yuan_bi);                   //积分
        iv_zhanghu = (ImageView) view.findViewById(R.id.iv_zhanghu);                          //购买理财

        linear_vc_myaccount = (RelativeLayout) view.findViewById(R.id.linear_vc_myaccount);                   //余额宝
        linear_vc_tonghuaInfo = (LinearLayout) view.findViewById(R.id.linear_vc_tonghuaInfo);              //我要提现
        linear_vc_keyong_account = (LinearLayout) view.findViewById(R.id.linear_vc_keyong_account);       //我要充值
        linear_vc_binding_bankcard = (LinearLayout) view.findViewById(R.id.linear_vc_binding_bankcard);  //绑定银行卡
        phone_zhaoshang = (RelativeLayout) view.findViewById(R.id.phone_zhaoshang);                         //招商热线
    }

    @Override
    public void afterInitView() {
        isCardSales();
        judgeIsLogin_Phone();
        refreshHeadImg();
    }

    public List<Map<String, Object>> initList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < images.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", images[i]);
            map.put("ItemText", imagesName[i]);
            list.add(map);
        }
        return list;
    }

    public void judgeIsLogin_Phone() {
        if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
            my_message.setVisibility(View.VISIBLE);
            bt_my_login.setVisibility(View.GONE);
            telphone.setText(getFromSharePreference(Config.userConfig.phoneno));
            my_name.setText(getFromSharePreference(Config.userConfig.nickname));
            //没有头像显示默认图
            if (null == getFromSharePreference(Config.userConfig.position) || ("").equals(getFromSharePreference(Config.userConfig.position))) {
                roundIV_my_photo.setVisibility(View.GONE);
                roundIV_my_photo_moren.setVisibility(View.VISIBLE);
            } else {
                roundIV_my_photo_moren.setVisibility(View.GONE);
                roundIV_my_photo.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(getFromSharePreference(Config.userConfig.position), roundIV_my_photo);
            }
        } else {
            my_message.setVisibility(View.GONE);
            bt_my_login.setVisibility(View.VISIBLE);
        }
    }

    //更新头像
    private void refreshHeadImg() {
        if (null == backUrl || ("").equals(backUrl)) {
            if (null == getFromSharePreference(Config.userConfig.position) || ("").equals(getFromSharePreference(Config.userConfig.position))) {
                roundIV_my_photo.setVisibility(View.GONE);
                roundIV_my_photo_moren.setVisibility(View.VISIBLE);
                roundIV_my_photo_moren.setImageResource(R.drawable.photo);
            } else {
                roundIV_my_photo_moren.setVisibility(View.GONE);
                roundIV_my_photo.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(getFromSharePreference(Config.userConfig.position), roundIV_my_photo);
            }
        } else {
            roundIV_my_photo_moren.setVisibility(View.GONE);
            roundIV_my_photo.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(backUrl, roundIV_my_photo);
        }
    }

    @Override
    public void bindListener() {
        my_sign_in.setOnClickListener(this);
        one_card.setOnClickListener(this);
        ll_phone_number.setOnClickListener(this);
        bt_my_myself.setOnClickListener(this);
        bt_my_lingdang.setOnClickListener(this);
        bt_my_login.setOnClickListener(this);
        roundIV_my_photo_moren.setOnClickListener(this);
        roundIV_my_photo.setOnClickListener(this);
        my_order.setOnClickListener(this);
        order_to_be_used.setOnClickListener(this);
        order_to_be_evaluated.setOnClickListener(this);
        order_to_be_paid.setOnClickListener(this);
        coupon_button.setOnClickListener(this);
        hongbao.setOnClickListener(this);
        integral_button.setOnClickListener(this);
        my_zichan.setOnClickListener(this);
        zd_detail.setOnClickListener(this);
        rl_sales.setOnClickListener(this);
        sec_kill_rela.setOnClickListener(this);
        rl_friends.setOnClickListener(this);
        some_city_rela.setOnClickListener(this);
        my_QR_Code.setOnClickListener(this);
        mDeductionShop.setOnClickListener(this);
        myBenefit.setOnClickListener(this);
        ry_my_tixian_detail.setOnClickListener(this);
        rl_my_chognzhi_detail.setOnClickListener(this);
        ry_my_yue_zenyi_detail.setOnClickListener(this);
        ry_my_bi_detail.setOnClickListener(this);
        yu_ebao.setOnClickListener(this);
        ry_my_return_record.setOnClickListener(this);
        layout_bg.setOnClickListener(this);
        rl_my_dikou.setOnClickListener(this);
        goConversion.setOnClickListener(this);
        citizen_card_bind.setOnClickListener(this);
        balance_of_account.setOnClickListener(this);
        hui_yuan_bi.setOnClickListener(this);
        iv_zhanghu.setOnClickListener(this);

        linear_vc_myaccount.setOnClickListener(this);
        linear_vc_tonghuaInfo.setOnClickListener(this);
        linear_vc_binding_bankcard.setOnClickListener(this);
        linear_vc_keyong_account.setOnClickListener(this);
        phone_zhaoshang.setOnClickListener(this);
    }

    public void startLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
//        intent.putExtra("loginsss", "Y");
        startActivity(intent);
    }


    @OnClick({R.id.ibtn_message, R.id.ibtn_setting, R.id.btn_login_register, R.id.riv_user_photo,
            R.id.ll_integral, R.id.ll_sign_in, R.id.tv_my_zhanghu_yu_e})
    public void view_onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_message: //消息
                startActivity(new Intent(getActivity(), NewsActivity.class));
                break;
            case R.id.ibtn_setting: //设置
                startActivity(new Intent(getActivity(), SystemSettingActivity.class));
                break;
            case R.id.btn_login_register:  //登录注册按钮
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {

                } else {
                    startLogin();
                }
                break;
            case R.id.riv_user_photo:  // 用户头像
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    selectPhoto.setPhoto(view.getWidth(), getPopupHeight(), my_message, getActivity(), handler);
                } else {
                    startLogin();
                }
                break;
            case R.id.ll_integral:  // 已登录积分
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), HuiyuanbiRecordActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.ll_sign_in: // 已登录 签到
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intentSignIn = new Intent(getActivity(), LifeServireH5Activity.class);
                    intentSignIn.putExtra("life_url", Config.web.sign_in);
                    intentSignIn.putExtra("isLogin", "Y");
                    intentSignIn.putExtra("isallurl", "Y");
                    intentSignIn.putExtra("serviceName", "签到");
                    startActivity(intentSignIn);
                } else {
                    startLogin();
                }
                break;
            case R.id.tv_my_zhanghu_yu_e: // 账户余额
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), UserRecordActivity.class));
                } else {
                    startLogin();
                }
                break;
        }
    }


    /***
     * 处理各控件的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone_zhaoshang:
                //先new出一个监听器，设置好监听
                DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case Dialog.BUTTON_POSITIVE:
//                                requestPermission();
                                PermissionUtils permissionUtils = new PermissionUtils(getActivity(), "025-5852-0907");
                                permissionUtils.requestPermission();
                                break;
                            case Dialog.BUTTON_NEGATIVE:
                                break;
                            case Dialog.BUTTON_NEUTRAL:
                                break;
                        }
                    }
                };
                //弹窗让用户选择，是否允许申请权限
                DialogUtil.showConfirm(getActivity(), "招商热线", "是否拨打招商热线025-5852-0907", dialogOnclicListener, dialogOnclicListener);
                break;
            case R.id.my_sign_in:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intentSignIn = new Intent(getActivity(), LifeServireH5Activity.class);
                    intentSignIn.putExtra("life_url", Config.web.sign_in);
                    intentSignIn.putExtra("isLogin", "Y");
                    intentSignIn.putExtra("isallurl", "Y");
                    intentSignIn.putExtra("serviceName", "签到");
                    startActivity(intentSignIn);
                } else {
                    startLogin();
                }
                break;
            case R.id.linear_vc_myaccount:                               //卡卡理财
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intent1 = new Intent(getActivity(), FinancingActivity.class);
                    intent1.putExtra("pkmuser", systemInfomationBean.getResultData().getPkmuser());
                    startActivity(intent1);
                } else {
                    startLogin();
                }
                break;
            case R.id.linear_vc_tonghuaInfo:                           //我要提现
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intent2 = new Intent(getActivity(), WithdrawActivity.class);
                    startActivity(intent2);
                } else {
                    startLogin();
                }

                break;
            case R.id.linear_vc_binding_bankcard:                      //绑定银行卡
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intent3 = new Intent(getActivity(), NewBindBankCardActivity.class);
                    startActivity(intent3);
                } else {
                    startLogin();
                }
                break;
            case R.id.linear_vc_keyong_account:                       //我要充值
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intent4 = new Intent(getActivity(), NewTopupWayActivity.class);
                    intent4.putExtra("FLAG", 2);
                    intent4.putExtra("pkmuser", systemInfomationBean.getResultData().getPkmuser());
                    startActivity(intent4);
                } else {
                    startLogin();
                }

                break;
            case R.id.iv_zhanghu:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), YuEBaoActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.hui_yuan_bi:                                  //积分
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), HuiyuanbiRecordActivity.class));
                } else {
                    startLogin();
                }

                break;
            case R.id.balance_of_account:                          //账户余额
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), UserRecordActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.one_card:
            case R.id.citizen_card_bind:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), CitizenCardListActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.iv_go_conversion:                                   //去抵扣
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivityForResult(new Intent(getActivity(), GoConversionActivity.class), CONVERSION);
                } else {
                    startLogin();
                }
                break;
            case R.id.layout_bg:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), YuEBaoActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.ry_my_bi_detail://积分记录
                startActivity(new Intent(getActivity(), HuiyuanbiRecordActivity.class));
                break;
            case R.id.rl_my_chognzhi_detail://充值记录
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), RechargeAccountActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.ry_my_yue_zenyi_detail://余额增益记录
                startActivity(new Intent(getActivity(), BalanceEarningsActivity.class));
                break;
            case R.id.ry_my_tixian_detail://提现记录
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intent = new Intent(getActivity(), RechargeRecordActivity.class);
                    intent.putExtra("type", 3);
                    startActivity(intent);
                } else {
                    startLogin();
                }

                break;
            /****
             * 我的点击事件
             */
            case R.id.bt_my_myself:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    // startActivity(new Intent(getActivity(), RechargeRecordActivity.class));
                } else {
                    startLogin();
                }
//                startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                break;
            /****
             *右上角铃铛点击事件（原消息页面跳转，现在改为设置跳转）
             */
            case R.id.bt_my_lingdang:
                /*if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    iv_news_red_point.setVisibility(View.GONE);
                    startActivity(new Intent(getActivity(), NewsActivity.class));
                } else {
                    startLogin();
                }*/

                //  startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                startActivity(new Intent(getActivity(), SystemSettingActivity.class));
//                startActivity(new Intent(getActivity(), LifeTypeActivity.class));
                break;
            /****
             *登录点击事件
             */
            case R.id.bt_my_login:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {

                } else {
                    startLogin();
                }
                //  startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                break;
            /****
             *头像点击事件
             */
            case R.id.roundIV_my_photo:
            case R.id.roundIV_my_photo_moren:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    selectPhoto.setPhoto(view.getWidth(), getPopupHeight(), my_message, getActivity(), handler);
                } else {
                    startLogin();
                }
                break;
            /****
             *我的订单点击事件
             */
            case R.id.my_order:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    /*Intent intent = new Intent(getActivity(), UnPayOrderActivity.class);
                    intent.putExtra("flag", 1);
                    startActivity(intent);*/

                    startActivity(new Intent(getActivity(), MyOrderActivity.class));

                } else {
                    startLogin();
                }


                break;
            /****
             *待使用 点击事件
             */
            case R.id.order_to_be_used:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent inte = new Intent(getActivity(), UnPayOrderActivity.class);
                    inte.putExtra("flag", 2);
                    startActivity(inte);
                } else {
                    startLogin();
                }


                //  startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                break;
            /****
             *待评论点击事件
             */
            case R.id.order_to_be_evaluated:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {

                    Intent in = new Intent(getActivity(), UnPayOrderActivity.class);
                    in.putExtra("flag", 4);
                    startActivity(in);
                } else {
                    startLogin();
                }

                break;
            /****
             *待支付点击事件
             */
            case R.id.order_to_be_paid:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent inten = new Intent(getActivity(), UnPayOrderActivity.class);
                    inten.putExtra("flag", 3);
                    startActivity(inten);
                } else {
                    startLogin();
                }


                break;
            /****
             *优惠券点击事件
             */
            case R.id.coupon_button:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intents = new Intent(getActivity(), CouponActivity.class);
                    intents.putExtra("isSelect", "N");
                    intents.putExtra("flag", "2");
                    startActivity(intents);
//                    startActivity(new Intent(getActivity(), CouponActivity.class));
                } else {
                    startLogin();
                }


                break;
            /****
             *红包点击事件
             */
            case R.id.hongbao:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), RedPacketActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.yu_ebao:  //余额宝
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), YuEBaoActivity.class));
                } else {
                    startLogin();
                }
                break;
            /****
             *积分点击事件
             */
            case R.id.integral_button:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), PointRecordActivity.class));
                } else {
                    startLogin();
                }

                //  startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                break;
            /****
             *我的资产点击事件
             */
            case R.id.my_zichan:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), VipCenterAccountActivity.class));
                } else {
                    startLogin();
                }


                break;
            /****
             *会员中心点击事件
             */
          /*  case R.id.vip_center:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), VipCenterActivity.class));
                } else {
                    startLogin();
                }


                //startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                break;*/
            /****
             *账单明细点击事件
             */
            case R.id.zd_detail:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), BillDetailsActivity.class));
                } else {
                    startLogin();
                }
                break;

            case R.id.some_city_rela:
                //同城送订单
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), com.sinia.orderlang.activity.AllOrderActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.sec_kill_rela:
                //秒杀区订单
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), com.sinia.orderlang.activity.RedPacketActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.my_QR_Code:
                //我的二维码
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Log.i("aaa", "" + filepath);
                    startActivity(new Intent(getActivity(), MyQRCodeActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.my_DeductionShop:
                //抵扣商城
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {

                    startActivity(new Intent(getActivity(), DeductionStoreActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.my_benefit:
                //消费分红
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {

                    startActivity(new Intent(getActivity(), FeePointActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.ll_phone_number:
//                //拨打电话
//                Intent intentPhone = new Intent(Intent.ACTION_CALL,Uri.parse("tel:400-878-0858"));
//                startActivity(intentPhone);
                //先new出一个监听器，设置好监听
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case Dialog.BUTTON_POSITIVE:
//                                requestPermission();
                                PermissionUtils permissionUtils = new PermissionUtils(getActivity(), "025-58520902");
                                permissionUtils.requestPermission();
                                break;
                            case Dialog.BUTTON_NEGATIVE:
                                break;
                            case Dialog.BUTTON_NEUTRAL:
                                break;
                        }
                    }
                };
                //弹窗让用户选择，是否允许申请权限
                DialogUtil.showConfirm(getActivity(), "客服热线", "是否拨打客服热线025-58520902", dialogClick, dialogClick);
                break;
            case R.id.ry_my_return_record:
                Intent intentH5Return = new Intent(getActivity(), H5ReturnActivity.class);
                intentH5Return.putExtra("pkregister", getFromSharePreference(pkregister));
                startActivity(intentH5Return);
                break;
            case R.id.rl_my_dikou:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intentDikou = new Intent(getActivity(), H5DeductionActivity.class);
                    startActivity(intentDikou);
                } else {
                    startLogin();
                }
                break;
            case R.id.rl_friends:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intent = new Intent(getActivity(), FriendsCircleActivity.class);
                    startActivity(intent);
                } else {
                    startLogin();
                }
                break;
            case R.id.rl_sales:
                Intent intentSignIn = new Intent(getActivity(), LifeServireH5Activity.class);
                intentSignIn.putExtra("life_url", Config.web.cardSalesH5 + getFromSharePreference(Config.userConfig.pkregister));
                intentSignIn.putExtra("isLogin", "N");
                intentSignIn.putExtra("serviceName", "推广人收益");
                startActivity(intentSignIn);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONVERSION && resultCode == 11) {
            ((MainActivity) getActivity()).setTabSelection(3);         //我的页面
        } else if (requestCode == CONVERSION && resultCode == 12) {
            ((MainActivity) getActivity()).setTabSelection(1);         //店铺街页面
        }

        if (requestCode == PHOTO_REQUEST_CAMERA) {
            str_datePic = getFromSharePreference("str_datePic");
            File picture = new File(PATH + str_datePic);
            imageUri = Uri.fromFile(picture);       //生成URI地址
//            startPhotoZoom(imageUri);              //进入裁剪状态
            startPhotoZoom(getUri());              //进入裁剪状态
        }

        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                imageUri = data.getData();
                startPhotoZoom(data.getData());                       //裁剪图片
            }

        }
        if (requestCode == PHOTO_REQUEST_CUT && data != null) {
            photo = data.getExtras().getParcelable("data");
            String imgHand = getFromSharePreference(pkregister)
                    + System.currentTimeMillis() + ".jpg";
            Time time = new Time("GMT+8");                          // 设置时区
            time.setToNow();                                         // 当前时间
            saveImage(photo, imgHand);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            byte[] bitmapByte = baos.toByteArray();
//
//            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
//            photo.compress(Bitmap.CompressFormat.PNG, 100, baos2);
//            byte[] bit = baos2.toByteArray();
            //发送广播，更改SlidingMenu头像
            RegisterReceiverUtils.getInstance().sendBroadcast(getActivity(), "img_hand", null);

            String imageUrl = ImageDownloader.Scheme.FILE.wrap(outputImage + "");

            roundIV_my_photo_moren.setVisibility(View.GONE);
            roundIV_my_photo.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(imageUrl, roundIV_my_photo);
            selectPhoto.dismiss();
        }
    }

    /**
     * @param photo    :图片裁剪完毕后返回的Bitmap
     * @param fileName :文件名（hand_pic），自定义的
     */
    private void saveImage(Bitmap photo, String fileName) {
        if (photo != null) {
            //裁剪后的图片存放路径
            imageFileName = Environment
                    .getExternalStorageDirectory()
                    + "/ypt/vipcard/";
            File file = new File(imageFileName); //创建新文件夹
            if (!file.exists()) {
                file.mkdirs();
            }
            filepath = file + "/" + fileName; //文件名
            File imageFile = new File(filepath);
            try {
                imageFile.createNewFile();  // 创建文件
                BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(imageFile, false));
                photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);

                bos.flush();
                bos.close();
                //保存到服务器
                saveImagetoServer(imageFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }

        return;
    }

    private Uri getUri() {
        str_datePic = getFromSharePreference("str_datePic");
        File path = new File(PATH);
//        File path = new File(Environment.getExternalStorageDirectory(), "Android/data/包名/files/header");
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path, str_datePic);
        //由于一些Android 7.0以下版本的手机在剪裁保存到URI会有问题，所以根据版本处理下兼容性
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(getContext(), "com.bjypt.vipcard.fileprovider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * 进入裁剪状态 （剪裁图片）
     *
     * @param uri 图片的路径
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        //Stirng picLocNameString = uri.getPath();
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PATH, "head.jpg")));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        // 取消人脸识别功能
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private void saveImagetoServer(File file) {
        FinalHttp http = new FinalHttp();
        AjaxParams params = new AjaxParams();
        try {
            params.put("image", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        http.post(Config.web.uploading_image, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object t) {
                // TODO Auto-generated method stub
                super.onSuccess(t);
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                try {
                    UpdataHeadBean updataHeadBean = objectMapper.readValue(t.toString(), UpdataHeadBean.class);
                    if (updataHeadBean.getResultStatus().equals("0")) {
                        backUrl = updataHeadBean.getResultData().getFileName();
                        saveDataToSharePreference(Config.userConfig.position, backUrl);
                        Map<String, String> params = new HashMap<String, String>();
//                        pkregister：用户主键
//                        fileName:文件名称
                        params.put("pkregister", getFromSharePreference(pkregister));
                        params.put("fileName", backUrl);
                        updateImage(params);
                        Log.e("TYZ", "backUrl:" + backUrl);
                        handler.sendEmptyMessage(4);
                    } else {
                        Toast.makeText(getActivity(), updataHeadBean.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    Log.e("TYZ", "eee:" + e.toString());
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
                if (errorNo == 0) {
                }
            }

            @Override
            public void onLoading(long count, long current) {
                // TODO Auto-generated method stub
                super.onLoading(count, current);
            }
        });
    }

    @Override
    public void onClickEvent(View v) {

    }


    public int getPopupHeight() {
        return (int) (this.view.getHeight() - bt_my_myself.getHeight()
                - my_message.getHeight());
    }

    File outputImage;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 4) {
                Log.e("TYZ", "imageUrl:" + backUrl);
                roundIV_my_photo_moren.setVisibility(View.GONE);
                roundIV_my_photo.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(backUrl, roundIV_my_photo);
                selectPhoto.dismiss();
            }
        }
    };

    public void uri() {

        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(outputImage);
    }


    /**
     * 上传图片到服务器 成功
     *
     * @param reqcode
     * @param result
     */
    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 1) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                systemInfomationBean = objectMapper.readValue(result.toString(), SystemInfomationBean.class);

                mHandler.sendEmptyMessage(1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == SIGN_IN_GONGGAO) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                SignInGongGaoBean signInGongGaoBean = objectMapper.readValue(result.toString(), SignInGongGaoBean.class);

                List<AdEntity> gongGao = new ArrayList<AdEntity>();
                for (int i = 0; i < signInGongGaoBean.getResultData().size(); i++) {
                    gongGao.add(new AdEntity(signInGongGaoBean.getResultData().get(i).getSlogan(), "", ""));
                }
                if (gongGao.size() > 0) {
//                    scroll_textview.setTexts(gongGao, 1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传图片到服务器 失败 后二次上传
     *
     * @param reqcode
     * @param result
     */
    @Override
    public void onFailed(int reqcode, Object result) {
        Wethod.httpPost(getActivity(), 2010, null, null, MyFragment.this);
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    private void updateImage(Map<String, String> params) {
        Wethod.httpPost(getActivity(), 1106, Config.web.update_image, params, this);
    }

    public class SelectPhoto {

        PopupWindow popupWindow;

        public void setPhoto(int width, int height, View parnet,
                             final Context context,
                             final Handler handler) {

            if (null != popupWindow && popupWindow.isShowing()) {
                popupWindow.dismiss();
                popupWindow = null;
            }
            final View view = LayoutInflater.from(context).inflate(
                    R.layout.layout_select_photo, null);
            popupWindow = new PopupWindow(view);
            final ImageView iv_big_photo = (ImageView) view.findViewById(R.id.iv_big_photo);
            TextView tv_chakan_big_photo = (TextView) view.findViewById(R.id.tv_chakan_big_photo);
            TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
            TextView tv_photo = (TextView) view.findViewById(R.id.tv_photo);
            tv_chakan_big_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (!outputImage.exists()) {
                        /*if (!getFromSharePreference(Config.userConfig.position).equals("")) {
                            ImageLoader.getInstance().displayImage(Config.web.picUrl+getFromSharePreference(Config.userConfig.position), iv_big_photo);
                        } else {
                            ToastUtils.showMessage(getActivity(), "亲！请设置头像");
                            iv_big_photo.setVisibility(View.GONE);
                        }*/

                    if (null == backUrl || ("").equals(backUrl)) {

                        if (null == getFromSharePreference(Config.userConfig.position)) {
                            roundIV_my_photo.setVisibility(View.GONE);
                            roundIV_my_photo_moren.setVisibility(View.VISIBLE);
                        } else {
                            roundIV_my_photo_moren.setVisibility(View.GONE);
                            roundIV_my_photo.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(getFromSharePreference(Config.userConfig.position), iv_big_photo);
                        }
                    } else {
                        ImageLoader.getInstance().displayImage(backUrl, iv_big_photo);
                    }

//                    } else {
//                        ImageLoader.getInstance().displayImage(getFromSharePreference(Config.userConfig.position), iv_big_photo);
//                    }
                }
            });
            tv_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*uri();
                    //隐式调用照相机程序
                    Intent in = new Intent("android.media.action.IMAGE_CAPTURE");
                    //拍下的照片会被输出到output_image.jpg中去
                    in.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(in, PHOTO_REQUEST_CAMERA);*/

                    time_gameImg = new Time("GMT+8");// 设置时区
                    time_gameImg.setToNow();       //当前时间
                    str_datePic = time_gameImg.year
                            + (time_gameImg.month + 1)
                            + time_gameImg.monthDay + time_gameImg.hour
                            + time_gameImg.minute + time_gameImg.second
                            + ".jpg";

                    File f2 = new File(PATH); //创建文件夹
                    if (!f2.exists()) {
                        f2.mkdirs();
                    }
                    // 调用系统照相机
                    // 拍照动作完成时图片被存储到指定路径
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PATH, str_datePic)));
                    saveDataToSharePreference("str_datePic", str_datePic);
                    startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
                    Log.e("--------启动相机----", "---------启动相机---------");
                }
            });
            tv_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*uri();
                    Intent intent = new Intent(Intent.ACTION_PICK,null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image*//*");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, XIANGCE_PHOTO);*/

                    Intent intent1 = new Intent(Intent.ACTION_PICK,
                            null);
                    intent1.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            IMAGE_UNSPECIFIED);

                    startActivityForResult(intent1, PHOTO_REQUEST_GALLERY);
                }
            });
            LinearLayout other = (LinearLayout) view
                    .findViewById(R.id.layout_image);

            other.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popupWindow.dismiss();
                    return true;
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
            popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
            popupWindow.setWidth(width);
            popupWindow.setHeight(height);
            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.update();
            popupWindow.showAsDropDown(parnet);
        }


        /**
         * 消失并滞空
         */
        public void dismiss() {
            if (null != popupWindow && popupWindow.isShowing()) {
                popupWindow.dismiss();
                popupWindow = null;
            }
        }


        public boolean isShowing() {
            if (null != popupWindow) {
                if (popupWindow.isShowing()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        public boolean isNull() {
            if (null == popupWindow) {
                return false;
            } else
                return true;
        }

    }


    /**
     * 注销广播
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        utils.UnRegisterBroadCastReceiver(getActivity(), mReceiver);

    }

    /**
     * 当用户退出登录后  将头像设置为默认头像，且登录按钮显示 号码隐藏
     */
  /*  BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {


    }
};*/
    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("liyunte", intent.getAction());
            if ("updatephoto".equals(intent.getAction())) {
                judgeIsLogin_Phone();
            }
        }

    }
  /*  class NewsBroastCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("action_news".equals(intent.getAction())) {
                Log.e("liyunte", "success--------------");
//                iv_news_red_point.setVisibility(View.VISIBLE);
            }
        }
    }*/

    @Override
    public void onResume() {
        isCardSales();
        judgeIsLogin_Phone();
        if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
            Map<String, String> params = new HashMap<>();
            params.put("pkregister", getFromSharePreference(pkregister));
            Wethod.httpPost(getActivity(), 1, Config.web.system_infomatiob, params, this);
            my_name.setText(getFromSharePreference(nickname));


        }
        //连续签到天数
        requestSignDays();
        super.onResume();
        UmengCountContext.onPageStart("MyFragment");
    }

    //公告数据
    public void requestGongGao() {
        Map<String, String> params = new HashMap<>();
        params.put("adtype", "8");
        Wethod.httpPost(getActivity(), SIGN_IN_GONGGAO, Config.web.sign_in_gonggao, params, this);
    }

    //连续签到天数数据请求
    public void requestSignDays() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Config.web.sign_in_continue_days + getFromSharePreference(pkregister) + "&pageIndex=0&pageSize=10", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                SignINBean signInBean = new SignINBean();
                try {
                    JSONObject json = new JSONObject(s.toString());
                    String continueDays = json.getString("continueDays");
                    String signDaysString = json.getString("signDays");
                    signInBean.setContinueDays(continueDays);
                    signInBean.setSignDays(signDaysString);
                    if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                        continue_days.setVisibility(View.VISIBLE);
                        continue_days.setText("已经连续签到" + signInBean.getContinueDays() + "天");
                    } else {
                        continue_days.setVisibility(View.GONE);
                    }

                    //判断是否已经签到
                    String[] all = signInBean.getSignDays().split("\\|");
                    Calendar c = Calendar.getInstance();
                    String day = c.get(Calendar.DAY_OF_MONTH) + "";
                    if (Arrays.asList(all).contains(day)) {
                        my_sign_in.setText("已签到");
                    } else {
                        my_sign_in.setText("未签到");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getHttpQueue().add(stringRequest);
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengCountContext.onPageEnd("MyFragment");
    }

    public double sub(double v1, double v2) {
        if (v1 == v2) {
            return 0.00;
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        String value = b1.subtract(b2).doubleValue() + "";
        if (value.contains(".")) {
            if (value.substring(value.indexOf("."), value.length()).length() > 3) {
                return Double.parseDouble(value.substring(0, value.indexOf(".") + 3));
            }
        }
        return Double.parseDouble(value);
    }

    public double stringToDouble(String value) {
        String valusePiont = value.substring(value.indexOf("."), value.length());
        if (value != null && !value.equals("")) {
            double val;
            if (value.contains(".") && valusePiont.length() >= 3) {
                val = Double.parseDouble(value.substring(0, value.indexOf(".") + 3));
            } else {
                val = Double.parseDouble(value);
            }
            return val;
        }

        return 0.00;

    }

    /**
     * 推广人权限字段（0: 未开通， 1: 已开通）， 如果为1 在【我的】显示对应的推广人收益H5 页面
     */
    private void isCardSales() {
        if (getFromSharePreference(Config.userConfig.is_card_sales) != null && getFromSharePreference(Config.userConfig.is_card_sales).equals("1")) {
            sales_line.setVisibility(View.VISIBLE);
            rl_sales.setVisibility(View.VISIBLE);
        } else {
            sales_line.setVisibility(View.GONE);
            rl_sales.setVisibility(View.GONE);
        }
    }

}
