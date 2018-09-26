package com.bjypt.vipcard.wxapi;







import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.PayOnLineActivity;
import com.bjypt.vipcard.activity.RightAwayOnLineActivity;
import com.bjypt.vipcard.activity.TopupWayActivity;
import com.bjypt.vipcard.activity.ZbarPayActivity;
import com.bjypt.vipcard.activity.shangfeng.common.EventBusMessageEvent;
import com.bjypt.vipcard.activity.shangfeng.common.enums.EventBusWhat;
import com.bjypt.vipcard.activity.shangfeng.widget.PayTypeView;
import com.bjypt.vipcard.view.PayAwayView;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.e("GGGG", "onPayFinish, errCode = " + resp.errCode);

		//resp.errCode == 0
		//resp.errCode == -2时为支付取消
		//resp.errCode == -1时为签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
		if(resp.errCode == 0){
			if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

				PayOnLineActivity.WXPAY_FLAG = 2;
				ZbarPayActivity.WXPAY_FLAG = 2;
				TopupWayActivity.WXPAY_FLAG = 2;
				RightAwayOnLineActivity.WXPAY_FLAG = 2;
				PayAwayView.WXPAY_FLAG  =2;
				finish();

				if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
					processPayResult(PayTypeView.PAY_SUCCESS);
				}
			/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.app_tip);
			builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
			builder.show();*/
			}
		}else if(resp.errCode == -2){
			PayOnLineActivity.WXPAY_FLAG = 3;
			ZbarPayActivity.WXPAY_FLAG = 3;
			TopupWayActivity.WXPAY_FLAG = 3;
			PayAwayView.WXPAY_FLAG  =3;
			RightAwayOnLineActivity.WXPAY_FLAG = 3;

			processPayResult(PayTypeView.PAY_CANCEL);
//			Toast.makeText(this,"支付取消",Toast.LENGTH_LONG).show();
			finish();
		}else if(resp.errCode == -1){
			Toast.makeText(this,"微信支付异常",Toast.LENGTH_LONG).show();
			finish();
			processPayResult(PayTypeView.PAY_FAIL);
		}
	}


	private void processPayResult(int resultCode){
		EventBusMessageEvent event = new EventBusMessageEvent();
		event.setWhat(EventBusWhat.WxPayResult);
		event.setData(resultCode);
		EventBus.getDefault().post(event);
		finish();
	}
}