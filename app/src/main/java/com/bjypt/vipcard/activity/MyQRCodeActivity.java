package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.QRCodeBean;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.MyPopupWindows;
import com.bjypt.vipcard.utils.ShareSDKUtil;
import com.bjypt.vipcard.view.RoundImageView;
import com.bjypt.vipcard.zbar.encoding.EncodingUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Administrator on 2016/4/5.
 * <p>
 * 崔龙2017/11/9修改
 */
public class MyQRCodeActivity extends BaseActivity implements VolleyCallBack, PlatformActionListener {


    private ImageView myImgQRCode;
    private RoundImageView mHeadPhoto;
    private TextView mPhone;
    private TextView mAdress;
    private LinearLayout li_back;
    private String mQRCodeURL;
    private Bitmap mQRCodeBitmap;
    private String filepath;
    MyPopupWindows popupWindows;
    private Platform weibo;
    private TextView my_inite;
    private TextView iv_menu;


    @Override
    public void setContentLayout() {
        setContentView(R.layout.actiivty_my_qrcode);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }

    @Override
    public void initView() {
        ShareSDK.initSDK(this);
        popupWindows = new MyPopupWindows(this, R.layout.layout_share);
        li_back = (LinearLayout) findViewById(R.id.li_back);
        myImgQRCode = (ImageView) findViewById(R.id.my_Img_QRCode);
        mHeadPhoto = (RoundImageView) findViewById(R.id.rv_qrcode_myphoto);
        mPhone = (TextView) findViewById(R.id.tv_qrcode_myphoto);
        mAdress = (TextView) findViewById(R.id.tv_qrcode_myaddress);
        my_inite = (TextView) findViewById(R.id.my_inite);
        iv_menu = (TextView) findViewById(R.id.iv_menu);

    }

    @Override
    public void afterInitView() {

        if (getFromSharePreference(Config.userConfig.position) == null || getFromSharePreference(Config.userConfig.position).toString().equals("")) {
            mHeadPhoto.setImageResource(R.drawable.photo);
        } else {
            ImageLoader.getInstance().displayImage(getFromSharePreference(Config.userConfig.position), mHeadPhoto);
        }
//        mPhone.setText(getFromSharePreference(Config.userConfig.phoneno));
        String phone = getFromSharePreference(Config.userConfig.phoneno);
        String result = String.format(getResources().getString(R.string.phone_number)
                , phone.substring(0, 3) + "****" + phone.substring(7));
        mPhone.setText(result);

        mAdress.setText(getFromSharePreference(Config.userConfig.nickname));

        HashMap<String, String> params = new HashMap<>();
        params.put("userId", getFromSharePreference(Config.userConfig.pkregister));

        Wethod.httpPost(MyQRCodeActivity.this, 11, Config.web.share_registUrl, params, this);
    }

    @Override
    public void bindListener() {
        iv_menu.setOnClickListener(this);
        li_back.setOnClickListener(this);
        my_inite.setOnClickListener(this);
        popupWindows.setMyCallBack(new MyPopupWindows.MyCallBack() {
            @Override
            public void func(View view) {
                LinearLayout qq_friend_bt = (LinearLayout) view.findViewById(R.id.qq_friend_bt);
                LinearLayout weixin_bt = (LinearLayout) view.findViewById(R.id.weixin_bt);
                LinearLayout weixin_friend_bt = (LinearLayout) view.findViewById(R.id.weixin_friend_bt);
                LinearLayout sina_bt = (LinearLayout) view.findViewById(R.id.sina_bt);
                View view_close_share_two = view.findViewById(R.id.view_close_share_two);
                View view_close_share = view.findViewById(R.id.view_close_share);
                view_close_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (popupWindows != null) {
                            popupWindows.dismiss();
                        }

                    }
                });
                view_close_share_two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (popupWindows != null) {
                            popupWindows.dismiss();
                        }
                    }
                });
                qq_friend_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareLogo(0);
                    }
                });
                weixin_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareLogo(1);
                    }
                });
                weixin_friend_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareLogo(2);
                    }
                });
                sina_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareLogo(3);
                    }
                });

            }
        });
    }

    public void shareLogo(int type) {
        if (type == 0) {
            ShareSDKUtil.shareQZone(this, mQRCodeURL, filepath);
        } else if (type == 1) {
            ShareSDKUtil.shareWechat(this, mQRCodeURL, MyApplication.mWxApi, type);
        } else if (type == 2) {
            ShareSDKUtil.shareWechat(this, mQRCodeURL, MyApplication.mWxApi, type);
        } else {
            ShareSDKUtil.shareSinaWeibo(this, mQRCodeURL, filepath);
        }
        popupWindows.dismiss();
    }

    @Override
    public void onClickEvent(View v) {

        switch (v.getId()) {
            case R.id.li_back:
                finish();
                break;
            case R.id.iv_menu:
                popupWindows.show(li_back, 0, 0);
                break;
            case R.id.my_inite:
                //我的邀请页面
                startActivity(new Intent(this, MyInviteActivity.class));
                break;
        }
    }


    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 11) {
            try {
                QRCodeBean mQrCodeBean = getConfiguration().readValue(result.toString(), QRCodeBean.class);

                mQRCodeURL = mQrCodeBean.getResultData();
                mQRCodeBitmap = EncodingUtils.createQRCode(mQRCodeURL, 500, 500, null);
                if (mQRCodeBitmap != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            saveImage(mQRCodeBitmap, "erweima.jpg");
                        }
                    }).start();
                }
                myImgQRCode.setImageBitmap(mQRCodeBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if (reqcode == 1) {
            Wethod.ToFailMsg(this, result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    private void saveImage(Bitmap photo, String fileName) {
        if (photo != null) {
            //裁剪后的图片存放路径
            String imageFileName = Environment
                    .getExternalStorageDirectory()
                    + "/ypt/vipcard/";
            File file = new File(imageFileName); //创建新文件夹
            if (!file.exists()) {
                file.mkdirs();
            }
            filepath = file + "/" + fileName;
            File imageFile = new File(filepath);
            if (imageFile.exists()) {
                imageFile.delete();
            }
            try {
                imageFile.createNewFile();  // 创建文件
                BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(imageFile, false));
                photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);

                bos.flush();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }

        return;
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Toast.makeText(MyQRCodeActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Toast.makeText(MyQRCodeActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Toast.makeText(MyQRCodeActivity.this, "取消分享", Toast.LENGTH_SHORT).show();
    }
}
