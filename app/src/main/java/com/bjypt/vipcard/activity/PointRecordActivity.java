package com.bjypt.vipcard.activity;


import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseFraActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.fragment.PointRecordFra;
import com.bjypt.vipcard.fragment.UsePointFra;
import com.bjypt.vipcard.model.PointRecordInfo;
import com.bjypt.vipcard.umeng.UmengCountContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/5/10
 * Use by 积分记录
 */
public class PointRecordActivity extends BaseFraActivity implements VolleyCallBack {

    private TextView mLeftTv,mRightTv;
    private View mLeftLine,mRightLine;
    private RelativeLayout mLeft,mRight;
    FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private UsePointFra usePointFra;
    private PointRecordFra pointRecordFra;
    private RelativeLayout mBack;
    private TextView mAccountPoint;
    private TextView mConsumePoint;
    private PointRecordInfo pointRecordInfo;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_point_record);
    }

    @Override
    public void beforeInitView() {
        Map<String,String> params = new HashMap<>();
        params.put("pkregister",getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(PointRecordActivity.this,1,Config.web.total_point_record,params,this);
    }

    @Override
    public void initView() {

        mAccountPoint = (TextView) findViewById(R.id.account_point);
        mConsumePoint = (TextView) findViewById(R.id.point_point);

        mLeftTv = (TextView) findViewById(R.id.left_tv);
        mLeftLine = findViewById(R.id.left_view);

        mRightTv = (TextView) findViewById(R.id.right_tv);
        mRightLine = findViewById(R.id.right_view);

        mLeft = (RelativeLayout) findViewById(R.id.left_relative);
        mRight = (RelativeLayout) findViewById(R.id.right_relative);

        mBack = (RelativeLayout) findViewById(R.id.point_record_back);

        fragmentManager = getSupportFragmentManager();

        setTabSelection(0);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        mLeft.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mRight.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id){
            case R.id.left_relative:
                //点击左边做处理
                setTabSelection(0);
                break;
            case R.id.right_relative:
                //点击右边做处理
                setTabSelection(1);
                break;
            case R.id.point_record_back:
                finish();
                break;
        }
    }

    private void setTabSelection(int index){
        //重置按钮
        resetBtn();
        //开启一个Fragment事物
        transaction =fragmentManager.beginTransaction();
        //先隐藏掉所有的Fragement，以防止有多个Fragement显示在界面上情况
        hideFragment(transaction);
        switch (index){
            case 0:
                mLeftTv.setTextColor(Color.parseColor("#EC584E"));
                mLeftLine.setVisibility(View.VISIBLE);
                if(pointRecordFra==null){
                    //如果Fragment为空，则创建一个新的并创建到界面上
                    pointRecordFra = new PointRecordFra();
                    transaction.add(R.id.id_content,pointRecordFra);
                }else{
                    //如果Fragment不为空，则直接将他显示出来
                    transaction.show(pointRecordFra);
                }
                break;
            case 1:
                mRightTv.setTextColor(Color.parseColor("#EC584E"));
                mRightLine.setVisibility(View.VISIBLE);
                if(usePointFra == null){
                    Log.e("woyaokk", "merchanrt=null");
                    usePointFra = new UsePointFra();
                    transaction.add(R.id.id_content,usePointFra);
                }else{
                    transaction.show(usePointFra);
                }
                break;


        }
        transaction.commit();
    }

    private void resetBtn(){
        mLeftTv.setTextColor(Color.parseColor("#999999"));
        mRightTv.setTextColor(Color.parseColor("#999999"));
        mLeftLine.setVisibility(View.GONE);
        mRightLine.setVisibility(View.GONE);
    }

    /**
     * 将所有的Fragment都置为隐藏状态
     */
    private void hideFragment(FragmentTransaction transaction){
        if(pointRecordFra!=null){
            transaction.hide(pointRecordFra);
        }
        if(usePointFra!=null){
            transaction.hide(usePointFra);
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if(reqcode == 1){
            try {
                pointRecordInfo = getConfiguration().readValue(result.toString(),PointRecordInfo.class);
                mAccountPoint.setText(pointRecordInfo.getResultData().getPoint());
                mConsumePoint.setText(pointRecordInfo.getResultData().getConsumePoint());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if(reqcode == 1){
            Wethod.ToFailMsg(this,result);
        }
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
