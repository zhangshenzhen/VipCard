package com.bjypt.vipcard.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.bjypt.vipcard.NewBindBankCardActivity;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.BindCardBean;
import com.bjypt.vipcard.model.UpdataHeadBean;
import com.bjypt.vipcard.receiver.RegisterReceiverUtils;
import com.bjypt.vipcard.service.MQTTService;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.DataCleanManager;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.CheckUpdateAppVersionContext;
import com.bjypt.vipcard.view.LoadingPageDialog;
import com.bjypt.vipcard.view.RoundImageView;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.widget.BottomDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.tencent.tauth.Tencent;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import org.codehaus.jackson.map.ObjectMapper;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.ielse.view.SwitchView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import static com.bjypt.vipcard.common.Config.userConfig.nickname;
import static com.bjypt.vipcard.common.Config.userConfig.pkregister;

/**
 * Created by Administrator on 2016/11/30 0030.
 * 系统设置
 */


public class SystemSettingActivity extends BaseActivity implements VolleyCallBack<String>, BottomDialog.BottonRouteListener {

    // 返回按钮
    @BindView(R.id.back_iv)
    ImageButton back_iv;
    // 头像
    @BindView(R.id.riv_user_photo)
    RoundImageView riv_user_photo;
    // 昵称父布局
    @BindView(R.id.rl_alter_name)
    RelativeLayout rl_alter_name;
    // 昵称
    @BindView(R.id.my_name)
    TextView my_name;
    // 账号
    @BindView(R.id.telphone)
    TextView telphone;
    //手机号绑定
    @BindView(R.id.rela_phoneno)
    RelativeLayout rela_phoneno;
    // 登录密码设置
    @BindView(R.id.rela_login_psd)
    RelativeLayout rela_login_psd;
    // 支付密码
    @BindView(R.id.rela_pay_psd)
    RelativeLayout rela_pay_psd;
    // 设置卡支付密码 按钮
    @BindView(R.id.rela_card_pay_pwd)
    RelativeLayout rela_card_pay_pwd;
    @BindView(R.id.sv_is_open_card_pay_pwd)
    SwitchView sv_is_open_card_pay_pwd;

    //设置卡支付限额
    @BindView(R.id.rl_card_pay_quota)
    RelativeLayout rl_card_pay_quota;

    // 清除缓存
    @BindView(R.id.rl_clear_cache)
    RelativeLayout rl_clear_cache;

    @BindView(R.id.tv_cache)
    TextView tv_cache;

    //产品建议
    @BindView(R.id.rela_suggest)
    RelativeLayout rela_suggest;
    //我想入驻
    @BindView(R.id.rela_check_in)
    RelativeLayout rela_check_in;
    @BindView((R.id.rela_systemProposeList))
    RelativeLayout rela_systemProposeList;

    //退出
    @BindView(R.id.exit_login)
    Button exit_login;

    private TextView tv_many_phonenum;

    //判断是支付密码还是登陆密码
    private boolean PSD_TYPE;
    private BroadCastReceiverUtils utils;
    private LinearLayout parent;
    private int screenWidth;
    private int screenHeigh;
    private ImageView iv_many_news_point;//红点

    private static final int PHOTO_REQUEST_CAMERA = 1;   // 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;  // 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;      // 结果
    private static int CAMERA_CODE = 1; // 请求码

    private String imageFileName, filepath, str_datePic;


    public static String RECIVER_ACTIONS = "com.bjypt.vipcard";
    private boolean flag = false;
    private DownLoadReciver receiver;
    private Tencent mTencent;
    private RelativeLayout setting_band_card;                //绑定银行卡
    private TextView yes_or_not_band_card;                   //是否已绑卡
    private final static int YESORNOT_BANDCARD = 1112;
    private final static int SET_USER_NAME = 1;
    private BindCardBean bindCardBean;
    // 存储图片的路径
    private String PATH = Environment.getExternalStorageDirectory() + "/vipcard/Camera/";
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private String backUrl;


    private CheckUpdateAppVersionContext checkUpdateAppVersionContext;
    private BottomDialog bottomDialog;
    private Time time_gameImg;
    private Uri imageUri;
    private Uri photoUri;
    private LoadingPageDialog loadingPageDialog;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.fra_many);
        ButterKnife.bind(this);
//        mTencent = Tencent.createInstance("1105165398", this.getApplicationContext());
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
                        , phone.substring(0, 3) + "****" + phone.substring(7));
                telphone.setText(result);
            }

        } else {
            my_name.setText("");
            telphone.setText("账号：");
        }

        receiver = new DownLoadReciver();
        utils = new BroadCastReceiverUtils();
        utils.registerBroadCastReceiver(this, "action_news", receiver);
        loadingPageDialog = new LoadingPageDialog(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeigh = dm.heightPixels;


        getCache();
        //是否已绑定银行卡数据请求
//        isBandCard();
    }

    private void getCache() {
        try {
            String cacheData = DataCleanManager.getTotalCacheSize(this);
            if(cacheData.endsWith("Byte")) {
                tv_cache.setText(cacheData.replace("Byte",""));
            }else{
                tv_cache.setText(cacheData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initView() {
        initKeyboard();

        yes_or_not_band_card = (TextView) findViewById(R.id.yes_or_not_band_card);
        setting_band_card = (RelativeLayout) findViewById(R.id.setting_band_card);
//        mRelaName = (RelativeLayout) findViewById(R.id.rela_name);//昵称
//        mRelaAdress = (RelativeLayout) findViewById(R.id.rela_adress);//地址
//        mRelaPhoneno = (RelativeLayout) findViewById(R.id.rela_phoneno);//号码
//        mRelaLoginPsd = (RelativeLayout) findViewById(R.id.rela_login_psd);//登录密码
//        mRelaPayPsd = (RelativeLayout) findViewById(R.id.rela_pay_psd);//支付密码
//        mRelaQuestionPsd = (RelativeLayout) findViewById(R.id.rela_question_psd);//安保问题
//        mRelaNotica = (RelativeLayout) findViewById(R.id.rela_notica);//消息提醒
//        mRelaSuggest = (RelativeLayout) findViewById(R.id.rela_suggest);//产品建议
//        mRelaCheckIn = (RelativeLayout) findViewById(R.id.rela_check_in);//我想入住
//        mRelaUpdate = (RelativeLayout) findViewById(R.id.rela_check_update);//检查更新
//
//        /**登录密码**/
//        mLoginPsdIv = (ImageView) findViewById(R.id.login_psd_iv);
//        mLoginPsdTv = (TextView) findViewById(R.id.login_psd_embellish);
        /**更新**/
//        mUpdateStateTv = (TextView) findViewById(R.id.update_state_tv);

        tv_many_phonenum = (TextView) findViewById(R.id.tv_many_phonenum);//用户的手机号
        parent = (LinearLayout) findViewById(R.id.re_main);
        iv_many_news_point = (ImageView) findViewById(R.id.iv_many_news_point);
    }

    @Override
    public void afterInitView() {
        // 设置昵称
        my_name.setText(getFromSharePreference(Config.userConfig.nickname));

//        mUpdateStateTv.setText("当前版本：" + AppUtils.getVersionName(this));
//        if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
//            if (!"".equals(getFromSharePreference(Config.userConfig.phoneno))) {
//                String phone = getFromSharePreference(Config.userConfig.phoneno);
//                tv_many_phonenum.setText(phone.substring(0, 3) + "*******" + phone.substring(10));
//            }
//
//        } else {
//            exit_login.setVisibility(View.GONE);
//        }
//
        checkUpdateAppVersionContext = new CheckUpdateAppVersionContext(this, parent);


    }


    @Override
    public void bindListener() {
    }

    public void startLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @OnClick({R.id.back_iv, R.id.riv_user_photo, R.id.rl_alter_name, R.id.rela_phoneno, R.id.rela_login_psd,
            R.id.rela_pay_psd, R.id.rl_card_pay_quota, R.id.rl_clear_cache, R.id.rela_suggest,
            R.id.rela_check_in, R.id.exit_login, R.id.rela_card_pay_pwd, R.id.rela_systemProposeList})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv: // 返回
                finish();
                break;
            case R.id.riv_user_photo:  // 头像
                bottomDialog = new BottomDialog(this);
                bottomDialog.setClickListener(this);
                bottomDialog.setCanceledOnTouchOutside(true);
                bottomDialog.show();

                break;

            case R.id.rl_alter_name: // 修改昵称
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(this, ChangeNameActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.rela_phoneno:  // 手机号绑定
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(this, AfreshPhoneBindActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.rela_login_psd:  //  登录密码设置
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    PSD_TYPE = true;
                    Intent mLogPsdIntent = new Intent(this, ChangePasswordActivity.class);
                    mLogPsdIntent.putExtra("psdType", PSD_TYPE);
                    startActivityForResult(mLogPsdIntent, 0);
                } else {
                    startLogin();
                }
                /****************修改登录密码时设置一个标志位区别是去修改登录密码还是支付密码*********************/

                break;
            case R.id.rela_pay_psd:  // 支付密码
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    PSD_TYPE = false;
                    Intent mPayPsdIntent = new Intent(this, ChangePasswordActivity.class);
                    mPayPsdIntent.putExtra("psdType", PSD_TYPE);
                    startActivity(mPayPsdIntent);
                } else {
                    startLogin();
                }

                /****************修改登录密码时设置一个标志位区别是去修改登录密码还是支付密码*********************/
                break;
            case R.id.rl_card_pay_quota:  // 设置卡支付限额

                break;
            case R.id.rl_clear_cache:   // 清除缓存
                loadingPageDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);

                            SystemSettingActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DataCleanManager.clearAllCache(SystemSettingActivity.this);
                                    getCache();
                                    loadingPageDialog.dismiss();

                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;

            case R.id.rela_suggest:  // 产品建议
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(this, ProductSuggestActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.rela_check_in:  // 我想入驻
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(this, CheckInActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.exit_login:  // 退出
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    //清空shareprepance的本地存储数据
                    saveDataToSharePreference(Config.userConfig.is_Login, "N");
                    saveDataToSharePreference(Config.userConfig.pkregister, "");
//                    saveDataToSharePreference(Config.userConfig.phoneno, "");
                    saveDataToSharePreference(Config.userConfig.loginpassword, "");
                    saveDataToSharePreference(Config.userConfig.nickname, "");
                    saveDataToSharePreference(Config.userConfig.paypassword, "");
                    saveDataToSharePreference(Config.userConfig.position, "");
                    saveDataToSharePreference(Config.userConfig.is_card_sales, null);
                    saveDataToSharePreference(Config.userConfig.switch_citycode, "");
//                    mTencent.logout(SystemSettingActivity.this);
                    Toast.makeText(this, "退出成功", Toast.LENGTH_LONG).show();
                    SystemSettingActivity.this.finish();
                    JPushInterface.setAlias(this, "", new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {

                        }
                    });
                    Set<String> tagset = new HashSet<String>();
                    tagset.add("");
                    JPushInterface.setTags(this, tagset, new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {

                        }
                    });
                    stopService(new Intent(this, MQTTService.class));
                    exit_login.setVisibility(View.GONE);
                    tv_many_phonenum.setText("");
                    utils.sendBroadCastReceiver(this, "updatephoto");
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.rela_card_pay_pwd:  // 设置卡支付密码 按钮
                sv_is_open_card_pay_pwd.setOpened(!sv_is_open_card_pay_pwd.isOpened());
                break;
            case R.id.rela_systemProposeList:
//                Intent intent = new Intent(this, LifeServireH5Activity.class);
//                intent.putExtra("life_url", Config.web.system_proposelist);
//                intent.putExtra("isLogin", "Y");
//                intent.putExtra("isallurl", "Y");
//                intent.putExtra("serviceName", "意见反馈");
//                startActivity(intent);
                break;
        }
    }


    /**
     * 点击空白处隐藏键盘
     */
    private void initKeyboard() {
        findViewById(android.R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.setting_band_card:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intent3 = new Intent(SystemSettingActivity.this, NewBindBankCardActivity.class);
                    startActivity(intent3);
                } else {
                    startLogin();
                }
                break;
            case R.id.rela_name://昵称
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(this, ChangeNameActivity.class));
                } else {
                    startLogin();
                }

                break;
            case R.id.rela_adress://地址
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(this, DefaultAdressActivity.class));
                } else {
                    startLogin();
                }

//                startActivity(new Intent(getActivity(),AddAdressActivity.class));
                break;
            case R.id.rela_question_psd://安保问题
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {

                } else {
                    startLogin();
                }
                break;
            case R.id.rela_notica://提醒
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    iv_many_news_point.setVisibility(View.GONE);
                    startActivity(new Intent(this, NewsActivity.class));
                } else {
                    startLogin();
                }

                break;

            case R.id.rela_check_update://检查更新

                if (ContextCompat.checkSelfPermission(SystemSettingActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(SystemSettingActivity.this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(SystemSettingActivity.this, "没有获取到存储权限", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(SystemSettingActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    checkUpdateAppVersionContext.startCheck();
                }

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
            riv_user_photo.setVisibility(View.VISIBLE);

            Map<String, String> params = new HashMap<>();
            params.put("pkregister", getFromSharePreference(pkregister));
            Wethod.httpPost(this, 1, Config.web.system_infomatiob, params, this);
            my_name.setText(getFromSharePreference(nickname));

            String phone = getFromSharePreference(Config.userConfig.phoneno);
            String result = String.format(getResources().getString(R.string.my_account_number)
                    , phone.substring(0, 3) + "****" + phone.substring(7));
            telphone.setText(result);

            //没有头像显示默认图
            if (null == getFromSharePreference(Config.userConfig.position) || ("").equals(getFromSharePreference(Config.userConfig.position))) {
                riv_user_photo.setVisibility(View.VISIBLE);
            } else {
                riv_user_photo.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(getFromSharePreference(Config.userConfig.position), riv_user_photo);
            }

        }


        UmengCountContext.onPageStart("ManyFragment");
//        //更新密码强度
//        if (getFromSharePreference(Config.userConfig.strength) != null && !getFromSharePreference(Config.userConfig.strength).equals("")) {
//            setStrengthImg(Integer.parseInt(getFromSharePreference(Config.userConfig.strength)));
//        } else {
//            Log.i("aaa", "failed is 获取的值为空");
//        }
        //绑卡回来刷新数据
        isBandCard();
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPageEnd("ManyFragment");
    }

//    public void setStrengthImg(int sGrade) {
//        switch (sGrade) {
//            case MSTRENGTH_GRADE_ONE:
//                mLoginPsdIv.setImageResource(R.mipmap.psd_1);
//                mLoginPsdTv.setText("弱");
//                break;
//            case MSTRENGTH_GRADE_TWO:
//                mLoginPsdIv.setImageResource(R.mipmap.psd_2);
//                mLoginPsdTv.setText("中");
//                break;
//            case MSTRENGTH_GRADE_THREE:
//                mLoginPsdIv.setImageResource(R.mipmap.psd_3);
//                mLoginPsdTv.setText("强");
//                break;
//            case MSTRENGTH_GRADE_Four:
//                mLoginPsdIv.setImageResource(R.mipmap.psd_4);
//                mLoginPsdTv.setText("非常棒");
//                break;
//        }
//    }


    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == YESORNOT_BANDCARD) {
            try {
                bindCardBean = getConfiguration().readValue(result.toString(), BindCardBean.class);
                yes_or_not_band_card.setText("已绑定");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {
        if (reqcode == YESORNOT_BANDCARD) {
            yes_or_not_band_card.setText("未绑定");
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        checkUpdateAppVersionContext.destory();
//        if (myBinder != null) {
//            unbindService(mConnection);
//        }
    }

    @Override
    public void onItem1Listener() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // 拍照
            useCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_CODE);
        }


    }

    @Override
    public void onItem2Listener() {

        // 获取相册
        Intent intent1 = new Intent(Intent.ACTION_PICK,
                null);
        intent1.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_UNSPECIFIED);

        startActivityForResult(intent1, PHOTO_REQUEST_GALLERY);

    }


    public class DownLoadReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if ("action_news".equals(intent.getAction())) {
                iv_many_news_point.setVisibility(View.VISIBLE);

            }

        }
    }

    private void isBandCard() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Log.e("yang", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(SystemSettingActivity.this, YESORNOT_BANDCARD, Config.web.is_bind_card, params, this);
    }

    /**
     * 使用相机
     */
    private void useCamera() {
        // 设置时区
        time_gameImg = new Time("GMT+8");
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
//        com.orhanobut.logger.Logger.e("照片机被调用");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_CAMERA) {
            str_datePic = getFromSharePreference("str_datePic");
            File picture = new File(PATH + str_datePic);
            //生成URI地址
            imageUri = Uri.fromFile(picture);
//            Logger.e(imageUri.toString());
            startPhotoZoom(getUri());              //拍照进入裁剪状态
        }
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                imageUri = data.getData();
//                Logger.e(imageUri.toString());
                startPhotoZoom(data.getData());                       //裁剪图片
            }
        }

        if (requestCode == PHOTO_REQUEST_CUT) {
            if (null != data) {

                try {
                    Bitmap bp = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(photoUri));
                    String imgHand = getFromSharePreference(pkregister)
                            + System.currentTimeMillis() + ".jpg";
                    Time time = new Time("GMT+8");                          // 设置时区
                    time.setToNow();                                         // 当前时间
                    saveImage(bp, imgHand);
                    //发送广播，更改SlidingMenu头像
                    RegisterReceiverUtils.getInstance().sendBroadcast(this, "img_hand", null);

                    String imageUrl = ImageDownloader.Scheme.FILE.wrap(outputImage + "");

                    riv_user_photo.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(imageUrl, riv_user_photo);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    File outputImage;

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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 4) {
                Log.e("TYZ", "imageUrl:" + backUrl);
                riv_user_photo.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(backUrl, riv_user_photo);
            }
        }
    };

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
                        Toast.makeText(SystemSettingActivity.this, updataHeadBean.getMsg(), Toast.LENGTH_LONG).show();
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

    private void updateImage(Map<String, String> params) {
        Wethod.httpPost(this, 1106, Config.web.update_image, params, this);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        photoUri = Uri.fromFile(new File(PATH, "head.jpg"));
//        Logger.e(Uri.fromFile(new File(PATH, "head.jpg")).toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PATH, "head.jpg")));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        // 取消人脸识别功能
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
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
            return FileProvider.getUriForFile(this, "com.bjypt.vipcard.fileprovider", file);
        } else {
            return Uri.fromFile(file);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 拍照
                useCamera();
            } else {
                ToastUtil.showToast(this, "打开相机失败");

            }

        }
    }
}
