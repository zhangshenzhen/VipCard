package com.bjypt.vipcard.config;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.utils.PayResult;
import com.bjypt.vipcard.utils.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/14
 * Use by 支付宝支付配置文件
 */
public class Alipay {



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

    //商品的主题
    private String subject;
    //商品的描述
    private String body;
    //商品的金额
    private String price;
    //订单号
    private String OrderId;
    //自己服务器的订单号
    private String pk;

    private String paytype;
    /**
     * 设置要支付的商品
     * @param body 内容
     * @param subject 名称
     * @param price 金额
     * @param OrderId 订单号
     * @param pk 自己服务器订单号
     * @param paytype  1 前台充值 2 后台充值 3 前台消费
     * @author syj
     */
    public void setProduct(String subject,String body,String price,String OrderId,String pk,String paytype){
        this.subject = subject;
        this.body = body;
        this.price = price;
        this.OrderId = OrderId;
        this.pk = pk;
        this.paytype = paytype;
    }

    /**
     * @param context 上下文
     * @param mHandler
     */
    public Alipay(Context context,Handler mHandler){
        if(null == context || null == mHandler){
            return;
        }
        this.context = context;
        this.mHandler = mHandler;
        /**私钥 */
        RSA_PRIVATE = "MIICXAIBAAKBgQDB4QiddSVrHuVrl7DmvQ+ALvrNyVjH4p+vcYTLvCKvHxRA9BYtDCf2FQ16bXAiFJsVE0ogCvQekAj8vDyAyeaCEML5Sykp2lUeUXXiELDCkyH/YDFtI75mq+i05mFWNi2Zc6QSV6iafSwfl74QDDCT7Lu0KSOEp2zD8adIaEC3DwIDAQABAoGAem7La/PQX2cxZdJzKiL2KPKB+3ZNFF0ujjJXs/joxkIQHjyFzIjCq4n76vY1O7BbhuGZi8afBrXalw+f/YyCfUCSc9aKTf6U+4VA5FsIqX4+69nF+yC0/xZJQWiB3ZVW0cJRVmBmZ+1NwVMls9Cb+VvYd624qrAB/fyD8yl2WJECQQD+WubjfuC94PzC+Q3L4PL5JUmFPw3IaSIS+uzGbGysGNvt42nwD3tVvWBu76ZKgtNd/RIQnBDUvYMNgfaoyHi1AkEAwyICr+zdY3JZVmS4SQlVmihkmJaFqqHvM9cuY0LgN2BMMauSofxpeG4AI/NMaOpr4H7ZeynisNHuLF65bF/fMwJAXNftg4jinCyt1rV2Nb9MsWjM51hWvZqyl18EYoFdQYxpavIhz0C3clqIArZzaqtKX/pqR6nP8veiCKIx2PYkKQJAWPB0hxkQI1aoT49iy69cfAIzL7VTwV1DuL44X3fI2JSkxfLsx7ZToFeK6tfnGN7YQp+uosuJjoxw4XpfS307LwJBALYU2qrqMsRqvKpwkn7slxGTHiHfmUVLK/r0SEqvxlfMr+AB+EAKbPbjk3GoZGpCrqSpo/4m2MgaOOC5k6PbmkM=";
        /**公钥 */
        RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDB4QiddSVrHuVrl7DmvQ+ALvrNyVjH4p+vcYTLvCKvHxRA9BYtDCf2FQ16bXAiFJsVE0ogCvQekAj8vDyAyeaCEML5Sykp2lUeUXXiELDCkyH/YDFtI75mq+i05mFWNi2Zc6QSV6iafSwfl74QDDCT7Lu0KSOEp2zD8adIaEC3DwIDAQAB";
    }
    /**
     * 创建订单信息 create the order info.
     * @param subject 商品的名字
     * @param body 商品的描述
     * @param price 金额
     * @param pk 自己服务器的订单号
     * @param paytype
     * @return
     */
    private String getOrderInfo(String subject, String body, String price,String OrderId,String pk,String paytype) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + OrderId + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + Config.web.URL_pay+"pay/yinlian/aliPayBackUrl"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        //自己服务器订单号
        orderInfo += "&pk=\""+pk+"\"";

        orderInfo += "&paytype=\""+paytype+"\"";

        return orderInfo;
    }

    //	/**
    //	 *
    //	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
    //	 *
    //	 */
    //	private String getOutTradeNo() {
    //		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
    //				Locale.getDefault());
    //		Date date = new Date();
    //		String key = format.format(date);
    //
    //		Random r = new Random();
    //		key = key + r.nextInt();
    //		key = key.substring(0, 15);
    //		return key;
    //	}

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }
    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    private void getSDKVersion() {
        PayTask payTask = new PayTask((Activity) context);
        String version = payTask.getVersion();
    }

    /**
     * 调用SDK支付
     *
     */
    private void pay() {
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
        // 订单
        String orderInfo = getOrderInfo(subject, body, price,OrderId,pk,paytype);
        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        Log.e("支付宝demo","sign:"+sign);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) context);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);
                Log.e("支付宝demo", "payresult:"+result);

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

        Runnable checkRunnable = new Runnable() {

            @Override
            public void run(){
                // 构造PayTask 对象
                PayTask payTask = new PayTask((Activity) context);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                if(!isExist){
                }

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                Log.e("TYZ","isExist:"+isExist);
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
            Log.e("TYZ", msg.what+"--"+msg.obj);
            switch (msg.what) {
                case SDK_CHECK_FLAG:
//                    if((Boolean)msg.obj){
//                        Toast.makeText(context, "正确", Toast.LENGTH_SHORT).show();
                        pay();
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
