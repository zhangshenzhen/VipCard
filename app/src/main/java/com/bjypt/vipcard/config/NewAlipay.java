package com.bjypt.vipcard.config;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.utils.PayResult;
import com.bjypt.vipcard.utils.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/2/20 0020.
 */

public class NewAlipay {


    // 商户PID
    private static final String PARTNER = "2088221711245766";
    // 商户收款账号
    private static final String SELLER = "minszxfy@minszx.com";
    // 商户私钥，pkcs8格式
    private static String RSA_PRIVATE = "";
    // 支付宝公钥
    private static String RSA_PUBLIC = "";
    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;
    /************************************************************/
    private Context context;

    private Handler mHandler;

    private String content;

    public void setProduct(String content){
        Log.e("NewAlipay","setProduct");
       this.content = content;
    }

    /**
     * @param context 上下文
     * @param mHandler
     */
    public NewAlipay(Context context,Handler mHandler){
        Log.e("NewAlipay","context:"+context+"-----mHandler:"+mHandler);
        if(null == context || null == mHandler){
            return;
        }
        Log.e("NewAlipay","上下文");
        this.context = context;
        this.mHandler = mHandler;
        /**私钥 */
        RSA_PRIVATE = "MIICXAIBAAKBgQDB4QiddSVrHuVrl7DmvQ+ALvrNyVjH4p+vcYTLvCKvHxRA9BYtDCf2FQ16bXAiFJsVE0ogCvQekAj8vDyAyeaCEML5Sykp2lUeUXXiELDCkyH/YDFtI75mq+i05mFWNi2Zc6QSV6iafSwfl74QDDCT7Lu0KSOEp2zD8adIaEC3DwIDAQABAoGAem7La/PQX2cxZdJzKiL2KPKB+3ZNFF0ujjJXs/joxkIQHjyFzIjCq4n76vY1O7BbhuGZi8afBrXalw+f/YyCfUCSc9aKTf6U+4VA5FsIqX4+69nF+yC0/xZJQWiB3ZVW0cJRVmBmZ+1NwVMls9Cb+VvYd624qrAB/fyD8yl2WJECQQD+WubjfuC94PzC+Q3L4PL5JUmFPw3IaSIS+uzGbGysGNvt42nwD3tVvWBu76ZKgtNd/RIQnBDUvYMNgfaoyHi1AkEAwyICr+zdY3JZVmS4SQlVmihkmJaFqqHvM9cuY0LgN2BMMauSofxpeG4AI/NMaOpr4H7ZeynisNHuLF65bF/fMwJAXNftg4jinCyt1rV2Nb9MsWjM51hWvZqyl18EYoFdQYxpavIhz0C3clqIArZzaqtKX/pqR6nP8veiCKIx2PYkKQJAWPB0hxkQI1aoT49iy69cfAIzL7VTwV1DuL44X3fI2JSkxfLsx7ZToFeK6tfnGN7YQp+uosuJjoxw4XpfS307LwJBALYU2qrqMsRqvKpwkn7slxGTHiHfmUVLK/r0SEqvxlfMr+AB+EAKbPbjk3GoZGpCrqSpo/4m2MgaOOC5k6PbmkM=";
        /**公钥 */
        RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDB4QiddSVrHuVrl7DmvQ+ALvrNyVjH4p+vcYTLvCKvHxRA9BYtDCf2FQ16bXAiFJsVE0ogCvQekAj8vDyAyeaCEML5Sykp2lUeUXXiELDCkyH/YDFtI75mq+i05mFWNi2Zc6QSV6iafSwfl74QDDCT7Lu0KSOEp2zD8adIaEC3DwIDAQAB";
    }





    /**
     * 调用SDK支付
     *
     */
    private void pay(final String content) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
                || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(context)
                    .setTitle("警告")
                    .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                }
                            }).show();
            return;
        }

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) context);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(content);
                Log.e("NewAlipay", "payresult:"+result);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    /**
     * 开始支付
     * 1.查询终端设备是否存在支付宝认证账户
     */
    public void startAlipay(){

        Log.e("NewAlipay","checkRunnable");

        Runnable checkRunnable = new Runnable() {

            @Override
            public void run(){
                Log.e("NewAlipay","startAlipay");
                // 构造PayTask 对象
                PayTask payTask = new PayTask((Activity) context);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                if(!isExist){
                }

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                Log.e("NewAlipay","isExist:"+isExist);
                handler.sendMessage(msg);
            }
        };
        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();
    }
    /**
     * 检查结果
     */
    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("NewAlipay", msg.what+"--"+msg.obj);
            switch (msg.what) {
                case SDK_CHECK_FLAG:
//                    if((Boolean)msg.obj){
//                        Toast.makeText(context, "正确", Toast.LENGTH_SHORT).show();
                    pay(content);
//                    }else {
//                        Toast.makeText(context, "未检测到认证的支付宝账号", Toast.LENGTH_SHORT).show();
//                    }
                    break;
                case SDK_PAY_FLAG:
                    /**
                     * 9000	订单支付成功
                     * 8000	正在处理中
                     * 4000	订单支付失败
                     * 6001	用户中途取消
                     * 6002	网络连接出错
                     * TRADE_SUCCESS  交易成功
                     * TRADE_FAIL  交易失败
                     * TRADE_CANCEL  交易成功
                     */
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Message paymsg = mHandler.obtainMessage(9000, resultInfo);
                        mHandler.sendMessage(paymsg);
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Message paymsg = mHandler.obtainMessage(8000, resultInfo);
                            mHandler.sendMessage(paymsg);
                        }
                        // 判断resultStatus 为非“6001”则代表可能支付取消
                        else if(TextUtils.equals(resultStatus, "6001")){
                            Message paymsg = mHandler.obtainMessage(6001, resultInfo);
                            mHandler.sendMessage(paymsg);
                        }else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Message paymsg = mHandler.obtainMessage(10000, resultInfo);
                            mHandler.sendMessage(paymsg);
                        }
                    }
                    break;

            }
        }
    };
}
