package com.bjypt.vipcard.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/1
 * Use by 产品建议
 */
public class ProductSuggestActivity extends BaseActivity implements VolleyCallBack{

    private EditText mSuggestEt;
    private ImageButton back_iv;
    private Button mEntrue;

    /**
     * String pkregister, String phoneno,String propose

     */
    private String pkregister = "11111";//用户主键
    private String phoneno ="18114451409";//电话号码
    private String propose;//建议内容
    private Map<String,String> map;
    private String url = Config.web.prosuct_suggest;//产品建议

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_product_suggest);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        back_iv = (ImageButton) findViewById(R.id.back_iv);
        mSuggestEt = (EditText) findViewById(R.id.suggest_et);
        mEntrue = (Button) findViewById(R.id.suggest_entrue);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        back_iv.setOnClickListener(this);
        mEntrue.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id){
            case R.id.back_iv:
                finish();
                break;
            case R.id.suggest_entrue:
                if ((mSuggestEt.getText().toString().length()<15)) {
                    ToastUtils.showMessage(ProductSuggestActivity.this,"评论内容少于15");

                }else {
                    propose = mSuggestEt.getText().toString();
                    map = new HashMap<>();
                    map.put("pkregister",getFromSharePreference(Config.userConfig.pkregister));
                    map.put("phoneno",getFromSharePreference(Config.userConfig.phoneno));
                    map.put("propose",propose);
                    Wethod.httpPost(ProductSuggestActivity.this,60,url,map,ProductSuggestActivity.this);

                }
                //确认提交
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode==60){
            Log.e("liyunte",result.toString());
            ToastUtils.showMessage(ProductSuggestActivity.this,"提交成功");
            finish();
        }

    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

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
}
