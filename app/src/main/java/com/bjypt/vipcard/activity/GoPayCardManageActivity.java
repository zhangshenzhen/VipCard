package com.bjypt.vipcard.activity;

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

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.dialog.CardCodePayDialog;
import com.bjypt.vipcard.dialog.CodePaySuccessDialog;
import com.bjypt.vipcard.dialog.MoneyBeyongDialog;
import com.bjypt.vipcard.model.MyRandomBean;
import com.bjypt.vipcard.model.TwoCodeInfoBean;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.zbar.encoding.EncodingUtils;
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

public class GoPayCardManageActivity extends BaseActivity implements VolleyCallBack {

    private static final int REQUEST_RANDOM = 201710201;
    private static final int REQUEST_UPDATE_STATUS = 201710202;
    private static final String TAG = "GoPayCardManageActivity";
    private static final String CODE = "utf-8";
    private static final int SUCCESS_CODE = 456258;

    private Bitmap mQRCodeBitmap;
    private ImageView iv_two_code;
    private ImageView iv_one_code;
    private LinearLayout ll_recharge;
    private LinearLayout ll_look_number;
    private RelativeLayout back_card_management;
    private String cardnum;
    private String deviceId;
    private String mBarcode;
    Handler handler = null;
    Runnable update = null;
    Handler handler2 = null;
    Runnable update2 = null;
    private String pkmuser;
    private String muname;
    private TextView tv_code;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_go_pay_card_manage);
    }

    @Override
    public void beforeInitView() {
        getDeviceId();    // 获得设备ID
        getCardNumber();  // 获得卡号
        requestRandom();  // 请求网络接口
    }

    @Override
    public void initView() {
        tv_code = (TextView) findViewById(R.id.tv_code);
        iv_two_code = (ImageView) findViewById(R.id.iv_two_code);
        iv_one_code = (ImageView) findViewById(R.id.iv_one_code);
        ll_recharge = (LinearLayout) findViewById(R.id.ll_recharge);
        ll_look_number = (LinearLayout) findViewById(R.id.ll_look_number);
        back_card_management = (RelativeLayout) findViewById(R.id.back_card_management);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
//        ll_recharge.setOnClickListener(this);
        iv_one_code.setOnClickListener(this);
        iv_two_code.setOnClickListener(this);
        ll_look_number.setOnClickListener(this);
        back_card_management.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.ll_recharge:
                jumpRecharge();
                break;
            case R.id.back_card_management:
                finish();
                break;
            case R.id.iv_one_code:
            case R.id.iv_two_code:
            case R.id.ll_look_number:
                numberCardPromptDialog();
                break;

        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        switch (reqcode) {
            case REQUEST_RANDOM:
                loadTwoAndOneCode(result);
                break;
            case REQUEST_UPDATE_STATUS:
                loadTwoCodeInfo(result);
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        switch (reqcode) {
            case REQUEST_RANDOM:
                ToastUtil.showToast(this, "请检查网络！");
                break;
            case REQUEST_UPDATE_STATUS:
                handler.removeCallbacks(update);//停止指令
                handler2.removeCallbacks(update2);//停止指令
                ToastUtil.showToast(this, "请检查网络");
                break;
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        ToastUtil.showToast(this, "请检查网络！");
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

    /**
     * 获得卡号
     */
    private void getCardNumber() {
        Intent intent = getIntent();
        cardnum = intent.getStringExtra("cardnum");
        pkmuser = intent.getStringExtra("pkmuser");
        muname = intent.getStringExtra("muname");
    }

    /**
     * 跳转到充值界面
     */
    private void jumpRecharge() {
        if (getFromSharePreference(Config.userConfig.is_Login).equals("Y")) {
            Intent intent1 = new Intent(this, OneKeyTopupAmountActivity.class);
            intent1.putExtra("pkmuser", pkmuser);
            intent1.putExtra("muname", muname);
            intent1.putExtra("FLAG", 1);
            startActivity(intent1);
        } else {
            Intent intentLogin = new Intent(this, LoginActivity.class);
            intentLogin.putExtra("loginsss", "Y");
            startActivity(intentLogin);
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
        handler = new Handler();
        update = new Runnable() {
            @Override
            public void run() {
                Map<String, String> param = new HashMap<>();
                param.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
                param.put("deviceid", "123");
                param.put("cardnum", cardnum);
                Wethod.httpPost(GoPayCardManageActivity.this, REQUEST_RANDOM, Config.web.random, param, GoPayCardManageActivity.this);
                handler.postDelayed(update, 60000);
            }
        };
        handler.post(update); //2秒后执行
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(update);//停止指令
        }
        if (handler2 != null) {
            handler2.removeCallbacks(update2);
        }
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
        tv_code.setText(codeNumber);
        iv_two_code.setImageBitmap(mQRCodeBitmap);
        iv_one_code.setImageBitmap(getBarcode(barcode, 800, 200));
    }

    /**
     * 请求卡状态更新
     */
    private void requestUpdateStatus() {
        handler2 = new Handler();
        update2 = new Runnable() {
            @Override
            public void run() {
                Map<String, String> param = new HashMap<>();
                param.put("barcode", mBarcode);
                Wethod.httpPost(GoPayCardManageActivity.this, REQUEST_UPDATE_STATUS, Config.web.status, param, GoPayCardManageActivity.this, View.GONE);
                handler2.postDelayed(update2, 2000);
            }
        };
        handler2.post(update2); //2秒后执行
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
                handler.removeCallbacks(update);//停止指令
            } else if (status == 3) {
                handler2.removeCallbacks(update2);//停止指令
                final CodePaySuccessDialog dialog = new CodePaySuccessDialog(this);
                dialog.setTitle("支付成功");
                dialog.setOnNegativeListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        GoPayCardManageActivity.this.finish();
                    }
                });
                dialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GoPayCardManageActivity.this, LifeServireH5Activity.class);
                        intent.putExtra("life_url", Config.web.citizen_card_expense_calendar + "?type_main=7" + "&cardnum=" + cardnum + "&pkregister=");
                        intent.putExtra("isLogin", "Y");
                        intent.putExtra("isallurl", "Y");
                        intent.putExtra("serviceName", "消费记录");
                        startActivity(intent);
                        dialog.dismiss();
                        GoPayCardManageActivity.this.finish();
                    }
                });
            } else if (status == 4) {
//                ToastUtil.showToast(this, "卡余额不足，请充值");
                handler2.removeCallbacks(update2);//停止指令
                final MoneyBeyongDialog dialog2 = new MoneyBeyongDialog(this);
                dialog2.setTitle("卡余额不足，请充值");
                dialog2.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(GoPayCardManageActivity.this, OneKeyTopupAmountActivity.class);
                        intent1.putExtra("pkmuser", pkmuser);
                        intent1.putExtra("muname", muname);
                        intent1.putExtra("FLAG", 1);
                        startActivity(intent1);
                        GoPayCardManageActivity.this.finish();
                        dialog2.dismiss();
                    }
                });
                dialog2.setOnNegativeListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GoPayCardManageActivity.this.finish();
                        dialog2.dismiss();
                    }
                });
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
     * 跳转到条形码新界面
     */
    private void jumpBigOneCode() {
        Intent intent = new Intent(GoPayCardManageActivity.this, BigOneCodeActivity.class);
        intent.putExtra("barcode", mBarcode);
        intent.putExtra("pkmuser", pkmuser);
        intent.putExtra("muname", muname);
        intent.putExtra("cardnum", cardnum);
        startActivityForResult(intent, SUCCESS_CODE);
    }

    /**
     * 数字卡查看提示弹窗
     */
    private void numberCardPromptDialog() {
        final CardCodePayDialog dialog = new CardCodePayDialog(this);
        dialog.setTitle("该数字仅用于付款，请不要发给其他人");
        dialog.setPositiveButton("我知道了");
        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpBigOneCode();
                dialog.dismiss();
            }
        });
    }

    /**
     * 判断下一个界面如果付款成功的话，直接关闭这个界面
     *
     * @param requestCode 请求码
     * @param resultCode  返回码
     * @param data        参数
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SUCCESS_CODE && resultCode == 1117) {
            String successData = data.getStringExtra("success");
            if (successData.equals("success")) {
                GoPayCardManageActivity.this.finish();
            }
        }
    }
}
