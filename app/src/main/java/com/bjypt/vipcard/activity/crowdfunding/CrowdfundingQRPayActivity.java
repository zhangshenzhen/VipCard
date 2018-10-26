package com.bjypt.vipcard.activity.crowdfunding;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.android.volley.VolleyError;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.BigOneCodeActivity;
import com.bjypt.vipcard.activity.GoPayCardManageActivity;
import com.bjypt.vipcard.activity.LifeServireH5Activity;
import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.activity.OneKeyTopupAmountActivity;
import com.bjypt.vipcard.activity.shangfeng.data.bean.CommonWebData;
import com.bjypt.vipcard.activity.shangfeng.primary.commonweb.CommonWebActivity;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.dialog.CardCodePayDialog;
import com.bjypt.vipcard.dialog.CodePaySuccessDialog;
import com.bjypt.vipcard.dialog.MoneyBeyongDialog;
import com.bjypt.vipcard.model.MyRandomBean;
import com.bjypt.vipcard.model.TwoCodeInfoBean;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.StringUtils;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.zbar.encoding.EncodingUtils;
import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.sinia.orderlang.utils.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class CrowdfundingQRPayActivity extends BaseActivity implements VolleyCallBack {
    private static final int REQUEST_RANDOM = 201710201;
    private static final int REQUEST_GET_STATUS = 201710202;
    private static final String TAG = "CrowdfundingQRPay";
    private static final String CODE = "utf-8";
    private static final int SUCCESS_CODE = 456258;


    private String mBarcode;
    private int pkmerchantid;//商家id
    private TextView tv_code2;
    private ImageView iv_two_code2;
    private ImageView iv_one_code2;
    private LinearLayout ll_recharge2;
    private LinearLayout ll_look_number2;
    private RelativeLayout back_card_management2;
    private Handler handler4;
    private Runnable update4;
    private Handler handler3;
    private Runnable update3;
    private Bitmap mQRCodeBitmap;
    private String deviceId;
    private String pkregister;
    private TextView tv_vip_typle;
    private String vip_name;
    private int type_num;
    private String url_icon;
    private ImageView img_icon;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_go_pay_crowdfuding_play);
    }

    @Override
    public void beforeInitView() {
        getMerchantid();  // 获得商家id
        getDeviceId();
        requestRandom();// 请求网络接口
    }

    @Override
    public void initView() {
        img_icon = findViewById(R.id.img_icon);
        tv_code2 = (TextView) findViewById(R.id.tv_code2);
        iv_two_code2 = (ImageView) findViewById(R.id.iv_two_code2);
        iv_one_code2 = (ImageView) findViewById(R.id.iv_one_code2);
        ll_recharge2 = (LinearLayout) findViewById(R.id.ll_recharge2);
        ll_look_number2 = (LinearLayout) findViewById(R.id.ll_look_number2);
        back_card_management2 = (RelativeLayout) findViewById(R.id.back_card_management2);
        tv_vip_typle = findViewById(R.id.tv_vip_typle);
    }

    @Override
    public void afterInitView() {

        //会员等级状态显示
        if (StringUtil.isNotEmpty(vip_name)){
            tv_vip_typle.setText(vip_name);
        }else {
            tv_vip_typle.setVisibility(View.INVISIBLE);
        }
        if (StringUtil.isNotEmpty(url_icon)){
            Glide.with(this).load(url_icon).into(img_icon);
        }else {
            img_icon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void bindListener() {
        iv_one_code2.setOnClickListener(this);
        iv_two_code2.setOnClickListener(this);
        ll_look_number2.setOnClickListener(this);
        back_card_management2.setOnClickListener(this);


    }


    /**
     * 获得设备ID
     */
    private void getDeviceId() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        deviceId = tm.getDeviceId();
    }
    //获取商家Id
    private void getMerchantid() {
        Intent intent = getIntent();
        pkregister = intent.getStringExtra("pkregister");
        pkmerchantid = intent.getIntExtra("pkmerchantid",0);
        vip_name = intent.getStringExtra("vip_name");
        type_num = intent.getIntExtra("type_num",1);
        url_icon = intent.getStringExtra("icon_url");
        LogUtil.debugPrint("商家id : "+ pkmerchantid);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
           /* case R.id.ll_recharge2:
                jumpRecharge();
                break;*/
            case R.id.back_card_management2:
                finish();
                break;
            case R.id.iv_one_code2:
            case R.id.iv_two_code2:
            case R.id.ll_look_number2:
                numberCardPromptDialog();
                break;

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler3 != null) {
            handler3.removeCallbacks(update3);//停止指令
        }
        if (handler4 != null) {
            handler4.removeCallbacks(update4);
        }
    }

    /**
     * 请求每60秒刷新一次的二维码代码
     */
    private void requestRandom() {
        startRequestRandom();
    }


    /**
     * 每60S请求一次
     */
    private void startRequestRandom() {
        handler3 = new Handler();
        update3 = new Runnable() {
            @Override
            public void run() {
                Map<String, String> param = new HashMap<>();
                param.put("pkregister", pkregister);
                param.put("pkmerchantid", pkmerchantid+"");
                param.put("deviceid", deviceId);
               // param.put("cardnum", "XXX");
               // param.put("pkmerchantid", String.valueOf(pkmerchantid));
                Wethod.httpPost(CrowdfundingQRPayActivity.this, REQUEST_RANDOM, Config.web.zhongchou_tuo_code_url, param, CrowdfundingQRPayActivity.this);
                handler3.postDelayed(update3, 60000);
            }
        };
        handler3.post(update3 ); //2秒后执行
    }

    /**
     * 加载二维码、条形码生成数据
     *
     * @param result 网络回调数据
     */
    private void loadTwoAndOneCode(Object result) {
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            MyRandomBean myRandomBean = objectMapper.readValue(result.toString(), MyRandomBean.class);
            mBarcode = myRandomBean.getResultData().getBarcode();
            if (StringUtil.isNotEmpty(mBarcode)) {
                requestUpdateStatus();
                transformationalCode(mBarcode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 使用Zxing转换生成二维码、条形码
     *
     * @param barcode
     */
    private void transformationalCode(String barcode) {
        mQRCodeBitmap = EncodingUtils.createQRCode(barcode, 500, 500, null);
        String codeNumber = barcode.substring(0, 4) + "****";
        tv_code2.setText(codeNumber);
        iv_two_code2.setImageBitmap(mQRCodeBitmap);
        iv_one_code2.setImageBitmap(getBarcode(barcode, 800, 200));
    }

    /**
     * 请求卡状态更新
     */
    private void requestUpdateStatus() {
        handler4 = new Handler();
        update4 = new Runnable() {
            @Override
            public void run() {
                Map<String, String> param = new HashMap<>();
                param.put("barcode", mBarcode);
                Wethod.httpPost(CrowdfundingQRPayActivity.this, REQUEST_GET_STATUS, Config.web.zhongchou_get_barcode_url, param, CrowdfundingQRPayActivity.this, View.GONE);
                handler4.postDelayed(update4, 2000);
            }
        };
        handler4.post(update4); //2秒后执行
    }
    /**
     * 加载二维码信息
     *
     * @param result
     */
    private void loadTwoCodeInfo(Object result) {
        try {
            TwoCodeInfoBean twoCodeInfoBean = getConfiguration().readValue(result.toString(), TwoCodeInfoBean.class);
            int status = twoCodeInfoBean.getResultData().getStatus();
            Log.e(TAG, status + "");
            if (status == 1) {
                handler3.removeCallbacks(update3);//停止指令
            } else if (status == 3) {
                handler4.removeCallbacks(update4);//停止指令
                final CodePaySuccessDialog dialog = new CodePaySuccessDialog(this);
                dialog.setTitle("支付成功");
                dialog.setPositiveButton("到消费记录");
                dialog.setOnNegativeListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        CrowdfundingQRPayActivity.this.finish();
                    }
                });
                dialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String params = "pkregister=" + getPkregister() + "&pkmerchantid=" + pkmerchantid;
                        CommonWebData consume_record = new CommonWebData();
                        consume_record.setTitle("会员消费记录");
                        consume_record.setUrl(Config.web.h5_CFConsumeRecord + params);
                        CommonWebActivity.callActivity(CrowdfundingQRPayActivity.this, consume_record);
                        dialog.dismiss();
                        CrowdfundingQRPayActivity.this.finish();
                    }
                });
            } else if (status == 4) {
                ToastUtil.showToast(this, "卡余额不足，请充值");
                handler4.removeCallbacks(update4);//停止指令
//                final MoneyBeyongDialog dialog2 = new MoneyBeyongDialog(this);
//                dialog2.setTitle("卡余额不足，请充值");
//                dialog2.setOnPositiveListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent1 = new Intent(CrowdfundingQRPayActivity.this, OneKeyTopupAmountActivity.class);
//                        intent1.putExtra("pkmuser", 0);
//                        intent1.putExtra("muname", 0);
//                        intent1.putExtra("FLAG", 1);
//                        startActivity(intent1);
//                        CrowdfundingQRPayActivity.this.finish();
//                        dialog2.dismiss();
//                    }
//                });
//                dialog2.setOnNegativeListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        CrowdfundingQRPayActivity.this.finish();
//                        dialog2.dismiss();
//                    }
//                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBarcode(String str, Integer width,
                                    Integer height) {

        if (width == null || width < 200) {
            width = 200;
        }

        if (height == null || height < 50) {
            height = 50;
        }

        try {
            // 文字编码
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, CODE);

            BitMatrix bitMatrix = new MultiFormatWriter().encode(str,
                    BarcodeFormat.CODE_128, width, height, hints);

            return BitMatrixToBitmap(bitMatrix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * BitMatrix转换成Bitmap
     *
     * @param matrix
     * @return
     */
    private static Bitmap BitMatrixToBitmap(BitMatrix matrix) {
        final int WHITE = 0xFFFFFFFF;
        final int BLACK = 0xFF000000;

        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
            }
        }
        return createBitmap(width, height, pixels);
    }

    /**
     * 生成Bitmap
     *
     * @param width
     * @param height
     * @param pixels
     * @return
     */
    private static Bitmap createBitmap(int width, int height, int[] pixels) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 跳转到充值界面
     */
//    private void jumpRecharge() {
//        if (getFromSharePreference(Config.userConfig.is_Login).equals("Y")) {
//            Intent intent1 = new Intent(this, OneKeyTopupAmountActivity.class);
//            intent1.putExtra("pkmuser", 0);
//            intent1.putExtra("muname", 0);
//            intent1.putExtra("FLAG", 1);
//            startActivity(intent1);
//        } else {
//            Intent intentLogin = new Intent(this, LoginActivity.class);
//            intentLogin.putExtra("loginsss", "Y");
//            startActivity(intentLogin);
//        }
//    }
    /**
     * 数字卡查看提示弹窗
     */
    private void numberCardPromptDialog() {
       /* final CardCodePayDialog dialog = new CardCodePayDialog(this);
        dialog.setTitle("该数字仅用于付款，请不要发给其他人");
        dialog.setPositiveButton("我知道了");
        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpBigOneCode();
                dialog.dismiss();
            }
        });*/
        jumpBigOneCode();
    }

    /**
     * 跳转到条形码新界面
     */
    private void jumpBigOneCode() {
        Intent intent = new Intent(this, SafeRemindActivity.class);
        intent.putExtra("barcode", mBarcode);
        intent.putExtra("pkmuser", 0);
        intent.putExtra("muname", 0);
        intent.putExtra("cardnum", 0);
        startActivityForResult(intent, SUCCESS_CODE);
    }
    @Override
    public void onSuccess(int reqcode, Object result) {
        switch (reqcode) {
            case REQUEST_RANDOM:
                loadTwoAndOneCode(result);
                break;
            case REQUEST_GET_STATUS:
                loadTwoCodeInfo(result);
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        switch (reqcode){
            case REQUEST_RANDOM:
                Wethod.ToFailMsg(this, result.toString());
                finish();
                break;
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SUCCESS_CODE && resultCode == 1117) {
            String successData = data.getStringExtra("success");
            if (successData.equals("success")) {
                this.finish();
            }
        }
    }
}
