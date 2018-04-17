package com.bjypt.vipcard.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.BillDetailsActivity;
import com.bjypt.vipcard.activity.HuiyuanbiRecordActivity;
import com.bjypt.vipcard.activity.LifeServireH5Activity;
import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.activity.MyQRCodeActivity;
import com.bjypt.vipcard.activity.NewsActivity;
import com.bjypt.vipcard.activity.RechargeAccountActivity;
import com.bjypt.vipcard.activity.RechargeRecordActivity;
import com.bjypt.vipcard.activity.SystemSettingActivity;
import com.bjypt.vipcard.adapter.HomeRecyclerViewAdapter;
import com.bjypt.vipcard.adapter.MineRecyclerViewAdapter;
import com.bjypt.vipcard.adapter.MyServeGridViewAdapter;
import com.bjypt.vipcard.adapter.SpaceItemDecoration;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.bean.MyWalletBean;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.AppCategoryBean;
import com.bjypt.vipcard.model.AppCategoryLifeTypeBean;
import com.bjypt.vipcard.model.AppCategroyLifeTypeResultDataBean;
import com.bjypt.vipcard.model.SignINBean;
import com.bjypt.vipcard.model.SystemInfomationBean;
import com.bjypt.vipcard.model.UpdataHeadBean;
import com.bjypt.vipcard.receiver.RegisterReceiverUtils;
import com.bjypt.vipcard.utils.DialogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.PermissionUtils;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.RoundImageView;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.widget.BottomDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.orhanobut.logger.Logger;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bjypt.vipcard.common.Config.userConfig.nickname;
import static com.bjypt.vipcard.common.Config.userConfig.pkregister;

/**
 * Created by Dell on 2018/3/27.
 */

public class MineFragment extends BaseFrament implements AdapterView.OnItemClickListener, VolleyCallBack<String>, BottomDialog.BottonRouteListener {

    // 消息
    @BindView(R.id.ibtn_message)
    ImageButton ibtn_message;
    // 设置
    @BindView(R.id.ibtn_setting)
    ImageButton ibtn_setting;
    @BindView(R.id.riv_user_photo)
    RoundImageView riv_user_photo;

    @BindView(R.id.ll_user_info)
    LinearLayout ll_user_info;

    //登录注册按钮
    @BindView(R.id.btn_login_register)
    Button btn_login_register;

    // 签到
    @BindView(R.id.ll_sign_in)
    LinearLayout ll_sign_in;
    @BindView(R.id.continue_days)
    TextView continue_days;

    // 已登录积分
    @BindView(R.id.ll_integral)
    LinearLayout ll_integral;
    @BindView(R.id.tv_my_vip_bi)
    TextView tv_my_vip_bi;

    // 昵称
    @BindView(R.id.my_name)
    TextView my_name;
    // 账号
    @BindView(R.id.telphone)
    TextView telphone;

    // 我的钱包
    @BindView(R.id.rcv_commonality_serve)
    RecyclerView rcv_commonality_serve;
    // 更多服务
    @BindView(R.id.gv_serve)
    GridView gv_serve;

    @BindView(R.id.statusBarView)
    View statusBarView;

    @BindView(R.id.v2)
    View v2;

    private SystemInfomationBean systemInfomationBean;
    private static int CAMERA_CODE = 1; // 请求码


    private View view;
    private String imageFileName, filepath, str_datePic;
    // 存储图片的路径
    private String PATH = Environment.getExternalStorageDirectory() + "/vipcard/Camera/";
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private String backUrl;
    private MyServeGridViewAdapter myServeGridViewAdapter;
    private static final int PHOTO_REQUEST_CAMERA = 1;   // 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;  // 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;      // 结果

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    tv_my_vip_bi.setText(sub(stringToDouble(systemInfomationBean.getResultData().getWholebalance()), stringToDouble(systemInfomationBean.getResultData().getBalance())) + "");
                    break;
            }
        }
    };

    private BottomDialog bottomDialog;
    private Uri imageUri;
    private Bitmap photo;
    private Time time_gameImg;
    private Uri photoUri;
    private ArrayList<MyWalletBean> myWalletBeans;
    private MineRecyclerViewAdapter adapter;
    private int v2Height;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void beforeInitView() {

        ViewGroup.LayoutParams layoutParams = statusBarView.getLayoutParams();
        layoutParams.height = getStatusBarHeight();

        if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
            ll_user_info.setVisibility(View.VISIBLE);
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
            ll_user_info.setVisibility(View.GONE);
            btn_login_register.setVisibility(View.VISIBLE);
        }

        getMyWallet();

    }

    @Override
    public void initView() {
        initRecyclerView();
    }

    @Override
    public void afterInitView() {

        myServeGridViewAdapter = new MyServeGridViewAdapter(getContext());
        gv_serve.setAdapter(myServeGridViewAdapter);
        gv_serve.setOnItemClickListener(this);
    }


    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {

    }

    @OnClick({R.id.ibtn_message, R.id.ibtn_setting, R.id.btn_login_register,
            R.id.riv_user_photo, R.id.ll_integral, R.id.ll_sign_in})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_message: //消息
                startActivity(new Intent(getActivity(), NewsActivity.class));
                break;
            case R.id.ibtn_setting: //设置
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), SystemSettingActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.btn_login_register:  //登录注册按钮
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {

                } else {
                    startLogin();
                }
                break;
            case R.id.riv_user_photo:  // 用户头像
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    bottomDialog = new BottomDialog(getContext());
                    bottomDialog.setClickListener(this);
                    bottomDialog.setCanceledOnTouchOutside(true);
                    bottomDialog.show();

                } else {
                    startLogin();
                }
                break;
            case R.id.ll_integral:  // 积分
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), HuiyuanbiRecordActivity.class));
                } else {
                    startLogin();
                }
                break;
            case R.id.ll_sign_in: // 签到
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
        }
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
                    Logger.json(s);
                    String continueDays = json.getString("continueDays");
                    String signDaysString = json.getString("signDays");
                    signInBean.setContinueDays(continueDays);
                    signInBean.setSignDays(signDaysString);
                    if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                        continue_days.setText(signInBean.getContinueDays()+"天");
                    } else{
                        continue_days.setText(0+"天");
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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {
            case 0:  // 交易记录
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), BillDetailsActivity.class));
                } else {
                    startLogin();
                }
                break;
            case 1:  // 提现记录
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intent = new Intent(getActivity(), RechargeRecordActivity.class);
                    intent.putExtra("type", 3);
                    startActivity(intent);
                } else {
                    startLogin();
                }
                break;
            case 2:  // 充值记录
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(getActivity(), RechargeAccountActivity.class));
                } else {
                    startLogin();
                }
                break;
            case 3:  // 推荐给朋友
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Log.i("aaa", "" + filepath);
                    startActivity(new Intent(getActivity(), MyQRCodeActivity.class));
                } else {
                    startLogin();
                }
                break;
            case 5:  // 推广收益
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intentSignIn = new Intent(getActivity(), LifeServireH5Activity.class);
                    intentSignIn.putExtra("life_url", Config.web.cardSalesH5 + getFromSharePreference(Config.userConfig.pkregister));
                    intentSignIn.putExtra("isLogin", "N");
                    intentSignIn.putExtra("serviceName", "推广人收益");
                    startActivity(intentSignIn);
                } else {
                    startLogin();
                }

                break;
            case 4:  // 招商热线
                DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case Dialog.BUTTON_POSITIVE:
//                                requestPermission();
                                PermissionUtils permissionUtils = new PermissionUtils(getActivity(), "4001808366");
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
                DialogUtil.showConfirm(getActivity(), "招商及客服热线", "是否拨打招商及客服热线4001808366(08:00-17:00)", dialogOnclicListener, dialogOnclicListener);
                break;
        }
    }

    /**
     * 登录
     */
    public void startLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
//        intent.putExtra("loginsss", "Y");
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
            ll_user_info.setVisibility(View.VISIBLE);
            btn_login_register.setVisibility(View.GONE);
            riv_user_photo.setVisibility(View.VISIBLE);

            Map<String, String> params = new HashMap<>();
            params.put("pkregister", getFromSharePreference(pkregister));
            Wethod.httpPost(getActivity(), 1, Config.web.system_infomatiob, params, this);
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

        } else {

            riv_user_photo.setVisibility(View.GONE);
            ll_user_info.setVisibility(View.GONE);
            btn_login_register.setVisibility(View.VISIBLE);
            tv_my_vip_bi.setText("0");
        }

        myServeGridViewAdapter = new MyServeGridViewAdapter(getContext());
        gv_serve.setAdapter(myServeGridViewAdapter);
        gv_serve.setOnItemClickListener(this);

        //连续签到天数
        requestSignDays();

    }

    private void getMyWallet() {
        final Map<String, String> maps = new HashMap<>();
        maps.put("city_code", SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.web.cityCode, "1558"));
        maps.put("app_type", "12");
        Wethod.httpPost(getActivity(), 1113, Config.web.MY_WALLET, maps, new VolleyCallBack<String>() {
            @Override
            public void onSuccess(int reqcode, String result) {
//                Logger.e("reqcode :" + reqcode);
                Logger.e("result :" + result);
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("resultData");
                        if(jsonArray != null && jsonArray.length() > 0){
                            myWalletBeans = new ArrayList<>();
                            for(int i = 0; i < jsonArray.length(); i++){
                                MyWalletBean myWalletBean = new MyWalletBean();
                                JSONObject object = jsonArray.getJSONObject(i);
                                String app_id = object.getString("app_id");
                                myWalletBean.setApp_id(app_id);
                                String app_name = object.getString("app_name");
                                myWalletBean.setApp_name(app_name);
                                String app_icon = object.getString("app_icon");
                                myWalletBean.setApp_icon(app_icon);
                                if(object.toString().contains("isentry")) {
                                    int isentry = object.getInt("isentry");
                                    myWalletBean.setIsentry(isentry);
                                }
                                if(object.toString().contains("link_type")) {
                                    int link_type = object.getInt("link_type");
                                    myWalletBean.setLink_type(link_type);
                                }
                                if(object.toString().contains("link_url")) {
                                    String link_url = object.getString("link_url");
                                    myWalletBean.setLink_url(link_url);
                                }
                                if(object.toString().contains("city_code")) {
                                    String city_code = object.getString("city_code");
                                    myWalletBean.setCity_code(city_code);
                                }
                                if(object.toString().contains("android_native_url")){
                                    String android_native_url = object.getString("android_native_url");
                                    myWalletBean.setAndroid_native_url(android_native_url);
                                }
                                if(object.toString().contains("native_params")){
                                    String native_params = object.getString("native_params");
                                    myWalletBean.setNative_params(native_params);
                                }
                                myWalletBeans.add(myWalletBean);
                            }
                            setRecylerView(myWalletBeans);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int reqcode, String result) {

            }

            @Override
            public void onError(VolleyError volleyError) {
                ToastUtil.showToast(getContext(), volleyError.toString());
//                Logger.e(volleyError.toString());
            }
        });
    }

    private void setRecylerView(List<MyWalletBean> myWalletBeans) {
        adapter = new MineRecyclerViewAdapter(getContext(), myWalletBeans);
        rcv_commonality_serve.setAdapter(adapter);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext());
        layoutmanager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //设置RecyclerView 布局
        rcv_commonality_serve.setLayoutManager(layoutmanager);
        rcv_commonality_serve.addItemDecoration(new SpaceItemDecoration(30));
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == 1) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                systemInfomationBean = objectMapper.readValue(result.toString(), SystemInfomationBean.class);
//                Logger.e("签到 : "+systemInfomationBean.getResultData().getWholebalance());
                mHandler.sendEmptyMessage(1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

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

    @Override
    public void onItem1Listener() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // 拍照
            useCamera();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, CAMERA_CODE);
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
                    Bitmap bp = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(photoUri));
                    String imgHand = getFromSharePreference(pkregister)
                            + System.currentTimeMillis() + ".jpg";
                    Time time = new Time("GMT+8");                          // 设置时区
                    time.setToNow();                                         // 当前时间
                    saveImage(bp, imgHand);
                    //发送广播，更改SlidingMenu头像
                    RegisterReceiverUtils.getInstance().sendBroadcast(getActivity(), "img_hand", null);

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

    private void updateImage(Map<String, String> params) {
        Wethod.httpPost(getActivity(), 1106, Config.web.update_image, params, this);
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
            return FileProvider.getUriForFile(getContext(), "com.bjypt.vipcard.fileprovider", file);
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
                ToastUtil.showToast(getActivity(), "打开相机失败");

            }

        }
    }
}
