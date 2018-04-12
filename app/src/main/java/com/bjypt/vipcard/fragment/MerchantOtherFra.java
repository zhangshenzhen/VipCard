package com.bjypt.vipcard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.MerchantPicAdapter;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.MerchantPicBean;
import com.bjypt.vipcard.photoutils.ImagePagerActivity;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.ObjectMapperFactory;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/29
 * Use by 商家图片其他
 */
public class MerchantOtherFra extends BaseFrament implements VolleyCallBack{

    private ArrayList<String> detailsPicLists = new ArrayList<>();
    private PullToRefreshListView mPicGrid;
    private String pkmuser;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        pkmuser = bundle.getString("pkmuser");
        Log.e("kx", "pkmuser:" + pkmuser);
        Map<String,String> params = new HashMap<>();
        params.put("pkmuser",pkmuser);
        params.put("typecode","2");
        Wethod.httpPost(getActivity(),1, Config.web.getMerchant_details_pic, params, this);
        return inflater.inflate(R.layout.merchant_fra_other, container, false);
    }

    @Override
    public void beforeInitView() {


    }

    @Override
    public void initView() {
        mPicGrid = (PullToRefreshListView) getActivity().findViewById(R.id.grid_other);
        mPicGrid.setMode(PullToRefreshBase.Mode.BOTH);
    }

    @Override
    public void afterInitView() {
        /*MerchantPicAdapter merchantPicAdapter = new MerchantPicAdapter(detailsPicLists,getActivity());
        mPicGrid.setAdapter(merchantPicAdapter);
        mPicGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageBrower(position,detailsPicLists);
            }
        });*/
    }

    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {

    }

    private void imageBrower(int position,ArrayList<String> list) {
        Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,list);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if(reqcode == 1){
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                MerchantPicBean merchantPicBean = objectMapper.readValue(result.toString(), MerchantPicBean.class);
                String [] imgTemp = null;
                imgTemp = merchantPicBean.getResultData().getHybImagesList().split(",");
                for(int i = 0;i < imgTemp.length;i++){
                    detailsPicLists.add(Config.web.ImgURL+imgTemp[i]);
                }

                MerchantPicAdapter merchantPicAdapter = new MerchantPicAdapter(detailsPicLists,getActivity());
                mPicGrid.setAdapter(merchantPicAdapter);
                mPicGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        imageBrower(position - 1, detailsPicLists);
                    }
                });
            } catch (IOException e) {
                Log.e("Tagggg",e.toString());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {
        mPicGrid.onRefreshComplete();
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengCountContext.onPageStart("MerchantFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengCountContext.onPageEnd("MerchantFragment");
    }
}
