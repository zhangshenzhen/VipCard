package com.bjypt.vipcard.fragment.crowdfunding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.crowdfunding.CollectionProjectActivity;
import com.bjypt.vipcard.activity.crowdfunding.CrowdfundingAccountListActivity;
import com.bjypt.vipcard.activity.shangfeng.data.bean.CommonWebData;
import com.bjypt.vipcard.activity.shangfeng.primary.commonweb.CommonWebActivity;
import com.bjypt.vipcard.activity.shangfeng.util.StringUtils;
import com.bjypt.vipcard.base.BaseFragment;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.fragment.crowdfunding.entity.CfTabData;
import com.bjypt.vipcard.model.cf.CfUserInfoData;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinia.orderlang.utils.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyCrowdfundingFragment extends BaseFragment implements VolleyCallBack {


    ImageView iv_headpic;
    ImageView iv_back;
    ImageView iv_real_name;
    TextView tv_nickname;
    Button btn_realname;

    LinearLayout linear_item_account_list;
    LinearLayout linear_item_buy_record;
    LinearLayout linear_item_bind_bank;
    LinearLayout linear_item_favolist;
    LinearLayout linear_item_request_view;
    LinearLayout linear_item_request_online;
    LinearLayout linear_real_name;

    private static final int request_user_code = 123;
    private int pkmerchantid;
    private String phoneno;

    View view;


    @Override
    public void beforeInitView() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_my_crowdfunding, container, false);
        return view;
    }



    @Override
    public void initView() {
        iv_headpic = (ImageView) view.findViewById(R.id.iv_headpic);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        iv_real_name = (ImageView) view.findViewById(R.id.iv_real_name);
        tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
        linear_item_account_list = (LinearLayout) view.findViewById(R.id.linear_item_account_list);
        linear_item_buy_record = (LinearLayout) view.findViewById(R.id.linear_item_buy_record);
        linear_item_bind_bank = (LinearLayout) view.findViewById(R.id.linear_item_bind_bank);
        linear_item_favolist = (LinearLayout) view.findViewById(R.id.linear_item_favolist);
        linear_item_request_view = (LinearLayout) view.findViewById(R.id.linear_item_request_view);
        linear_item_request_online = (LinearLayout) view.findViewById(R.id.linear_item_request_online);
        btn_realname = (Button) view.findViewById(R.id.btn_real_name);
        linear_real_name = view.findViewById(R.id.linear_real_name);
    }

    @Override
    public void afterInitView() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getPkregister());
        Wethod.httpPost(getContext(), request_user_code, Config.web.getCFUserData, params, this, View.GONE);
    }

    @Override
    public void bindListener() {
        linear_item_account_list.setOnClickListener(this);
        linear_item_buy_record.setOnClickListener(this);
        linear_item_bind_bank.setOnClickListener(this);
        linear_item_favolist.setOnClickListener(this);
        linear_item_request_view.setOnClickListener(this);
        linear_item_request_online.setOnClickListener(this);
        btn_realname.setOnClickListener(this);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    @Override
    public void onClickEvent(View v) {
        String params = "pkregister=" + getPkregister() ;
        switch (v.getId()) {
            case R.id.linear_item_account_list:
                Intent intent = new Intent(getContext(), CrowdfundingAccountListActivity.class);
                intent.putExtra("phoneno",phoneno);
                startActivity(intent);
                break;
            case R.id.linear_item_buy_record://众筹购买记录
                CommonWebData buy_record = new CommonWebData();
                buy_record.setTitle("众筹购买记录");
                buy_record.setUrl(Config.web.h5_CrowdFBuyRecord + params+"&pkmerchantid=" );
                CommonWebActivity.callActivity(getContext(), buy_record);

                break;
            case R.id.linear_item_bind_bank://绑定银行卡
                CommonWebData bind_record = new CommonWebData();
                bind_record.setTitle("绑定银行卡");
                bind_record.setUrl(Config.web.h5_CFConsumeBinder + params);
                CommonWebActivity.callActivity(getContext(), bind_record);

                break;
            case R.id.linear_item_favolist://我的收藏
                Intent collection = new Intent(getContext(), CollectionProjectActivity.class);
                collection.putExtra("pkregister",getPkregister());
                startActivity(collection);

                break;
            case R.id.linear_item_request_view://申请查看记录
                CommonWebData request_view_record = new CommonWebData();
                request_view_record.setTitle("申请查看记录");
                request_view_record.setUrl(Config.web.h5_CFConsumerequest_view + params);
                CommonWebActivity.callActivity(getContext(), request_view_record);

                break;
            case R.id.linear_item_request_online://众筹申请
                CommonWebData request_crowd = new CommonWebData();
                request_crowd.setTitle("众筹申请");
                request_crowd.setUrl(Config.web.h5_CFConsumerequest_crowd);
                CommonWebActivity.callActivity(getContext(), request_crowd);

                break;
            case R.id.btn_real_name://立即认证
                CommonWebData real_name = new CommonWebData();
                real_name.setTitle("立即认证");
                real_name.setUrl(Config.web.h5_CFConsumereal_name + params);
                CommonWebActivity.callActivity(getContext(), real_name);

                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        LogUtil.debugPrint("Result = "+ result);
        if (reqcode == request_user_code) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                CfUserInfoData userInfoData = objectMapper.readValue(result.toString(), CfUserInfoData.class);
                tv_nickname.setText(userInfoData.getResultData().getNickname());
                phoneno = userInfoData.getResultData().getPhoneno();//获取phoneno
                if (userInfoData != null && userInfoData.getResultData() != null && StringUtil.isNotEmpty(userInfoData.getResultData().getPosition())) {
                    ImageLoader.getInstance().displayImage(userInfoData.getResultData().getPosition(), iv_headpic, AppConfig.CF_HEADER_GRAY);
                }
                if(StringUtils.isNotEmpty(userInfoData.getResultData().getBankcardno())){
                    iv_real_name.setVisibility(View.VISIBLE);
                    linear_real_name.setVisibility(View.GONE);
                }else{
                    iv_real_name.setVisibility(View.GONE);
                    linear_real_name.setVisibility(View.VISIBLE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
