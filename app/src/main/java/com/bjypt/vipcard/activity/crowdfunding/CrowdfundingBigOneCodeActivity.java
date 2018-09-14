package com.bjypt.vipcard.activity.crowdfunding;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.LifeServireH5Activity;
import com.bjypt.vipcard.activity.OneKeyTopupAmountActivity;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.dialog.CodePaySuccessDialog;
import com.bjypt.vipcard.dialog.MoneyBeyongDialog;
import com.bjypt.vipcard.model.TwoCodeInfoBean;
import com.bjypt.vipcard.utils.LogUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by 崔龙 on 2017/10/23.
 * <p>
 * 数字码界面
 */

public class CrowdfundingBigOneCodeActivity extends BaseActivity implements VolleyCallBack {

    private static final int REQUEST_UPDATE_STATUS = 201710202;
    private static final String CODE = "utf-8";
    private static final String TAG = "BigOneCodeActivity";
    private String barcode;
    private String cardnum;
    private TextView tv_one_code;
    private ImageView iv_one_code;
    private RelativeLayout back_card_management;

    Handler handler2 = null;
    Runnable update2 = null;

    private String pkmuser;
    private String muname;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_crowdfunding_big_one_code);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        barcode = intent.getStringExtra("barcode");
        pkmuser = intent.getStringExtra("pkmuser");
        muname = intent.getStringExtra("muname");
        cardnum = intent.getStringExtra("cardnum");
    }

    @Override
    public void initView() {
        tv_one_code = (TextView) findViewById(R.id.tv_one_code);
        iv_one_code = (ImageView) findViewById(R.id.iv_one_code);
       // back_card_management = (RelativeLayout) findViewById(R.id.back_card_management);
    }

    @Override
    public void afterInitView() {
        iv_one_code.setImageBitmap(getBarcode(barcode, 1200, 400));
        tv_one_code.setText(barcode);
        requestUpdateStatus();
    }

    @Override
    public void bindListener() {
       // back_card_management.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
//            case R.id.back_card_management:
//                finish();
//                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        switch (reqcode) {
            case REQUEST_UPDATE_STATUS:
                loadTwoCodeInfo(result);
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

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
     * 请求卡状态更新
     */
    private void requestUpdateStatus() {
        handler2 = new Handler();
        update2 = new Runnable() {
            @Override
            public void run() {
                Map<String, String> param = new HashMap<>();
                param.put("barcode", barcode);
                Wethod.httpPost(CrowdfundingBigOneCodeActivity.this, REQUEST_UPDATE_STATUS, Config.web.zhongchou_tuo_code_Update_url, param, CrowdfundingBigOneCodeActivity.this, View.GONE);
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
        LogUtil.debugPrint("轮询支付结果。。。。");
        try {
            TwoCodeInfoBean twoCodeInfoBean = getConfiguration().readValue(result.toString(), TwoCodeInfoBean.class);
            int status = twoCodeInfoBean.getResultData().getStatus();
            Log.e(TAG, status + "");
            if (status == 3) {
                handler2.removeCallbacks(update2);//停止指令
                final CodePaySuccessDialog dialog = new CodePaySuccessDialog(this);
                dialog.setTitle("支付成功");
                dialog.setOnNegativeListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("success", "success");
                        setResult(1117, intent);
                        CrowdfundingBigOneCodeActivity.this.finish();
                    }
                });
                dialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CrowdfundingBigOneCodeActivity.this, LifeServireH5Activity.class);
                        intent.putExtra("life_url", Config.web.citizen_card_expense_calendar + "?type_main=7" + "&cardnum=" + cardnum + "&pkregister=");
                        intent.putExtra("isLogin", "Y");
                        intent.putExtra("isallurl", "Y");
                        intent.putExtra("serviceName", "消费记录");
                        startActivity(intent);
                        dialog.dismiss();
                        CrowdfundingBigOneCodeActivity.this.finish();
                    }
                });
            } else if (status == 4) {
//                ToastUtil.showToast(this, "卡余额不足，请充值");
                final MoneyBeyongDialog dialog = new MoneyBeyongDialog(this);
                dialog.setTitle("卡余额不足，请充值");
                dialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(CrowdfundingBigOneCodeActivity.this, OneKeyTopupAmountActivity.class);
                        intent1.putExtra("pkmuser", pkmuser);
                        intent1.putExtra("muname", muname);
                        intent1.putExtra("FLAG", 1);
                        startActivity(intent1);
                        dialog.dismiss();
                        CrowdfundingBigOneCodeActivity.this.finish();
                    }
                });
                dialog.setOnNegativeListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        CrowdfundingBigOneCodeActivity.this.finish();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler2.removeCallbacks(update2);//停止指令
    }


}
