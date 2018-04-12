package com.bjypt.vipcard.activity;

import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.LifeServiceAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.LIfeServiceData;
import com.bjypt.vipcard.model.LifeServiceBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/6/14
 * Use by生活服务记录列表
 */
public class LifeServiceActivity extends BaseActivity implements VolleyCallBack{
    private RelativeLayout mBack;
    private PullToRefreshListView mlifeServiceList;
    private LifeServiceAdapter lifeServiceAdapter;
    final int QUERY_EXERCISE_MORE = 0x0101;
    final int QUERY_EXERCISE_REFERSH = 0x0110;
    private Map<String,String> maps = new HashMap<>();
    private int currentPage;
    private int pageLength = 10;
    private int isRefresh;
    private List<LifeServiceBean> mLifeList = new ArrayList<>();
    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_liseservice);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        mBack = (RelativeLayout) findViewById(R.id.life_service_back);
        mlifeServiceList = (PullToRefreshListView) findViewById(R.id.life_pulllist);
        mlifeServiceList.setMode(PullToRefreshBase.Mode.BOTH);
        lifeServiceAdapter = new LifeServiceAdapter(this,mLifeList);
        mlifeServiceList.setAdapter(lifeServiceAdapter);

        mlifeServiceList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                onRequest(QUERY_EXERCISE_REFERSH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                onRequest(QUERY_EXERCISE_MORE);
            }
        });

    }

    @Override
    public void afterInitView() {
        onRequest(QUERY_EXERCISE_REFERSH);
    }

    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id){
            case R.id.life_service_back:
                finish();
                break;
        }
    }

    private void onRequest(int type){


        if(type == QUERY_EXERCISE_REFERSH){
            if(maps!=null){
                maps.clear();
            }
            isRefresh = 1;
            currentPage = 0;
            maps.put("pkregister",getFromSharePreference(Config.userConfig.pkregister));
            maps.put("begin", 0 + "");
            maps.put("pageLength", pageLength + "");

        }else{
            if(maps!=null){
                maps.clear();
            }
            currentPage+=10;
            isRefresh = 2;
            maps.put("pkregister",getFromSharePreference(Config.userConfig.pkregister));
            maps.put("begin", currentPage + "");
            maps.put("pageLength", pageLength + "");
        }

        Wethod.httpPost(LifeServiceActivity.this,1314, Config.web.life_service_list, maps, this);


    }

    @Override
    public void onSuccess(int reqcode, Object result) {
//        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LIfeServiceData lIfeServiceData = getConfiguration().readValue(result.toString(), LIfeServiceData.class);
            if (lIfeServiceData.getResultData().size()==0){
                mlifeServiceList.onRefreshComplete();
            }else
            if(lIfeServiceData.getResultData().size()>0){

                if(isRefresh==1){
                    if(lifeServiceAdapter!=null){
                        mLifeList.clear();
                    }
                }

                for(int i=0;i<lIfeServiceData.getResultData().size();i++){
                    LifeServiceBean lifeServiceBean = new LifeServiceBean();
                    lifeServiceBean.setConsumeType(lIfeServiceData.getResultData().get(i).getConsumeType());
                    lifeServiceBean.setMobile(lIfeServiceData.getResultData().get(i).getMobile());
                    lifeServiceBean.setOrderTime(lIfeServiceData.getResultData().get(i).getOrderTime());
                    lifeServiceBean.setRebateMoney(lIfeServiceData.getResultData().get(i).getRebateMoney());
                    lifeServiceBean.setStatus(lIfeServiceData.getResultData().get(i).getStatus());
                    lifeServiceBean.setTotalMoney(lIfeServiceData.getResultData().get(i).getTotalMoney());
                    mLifeList.add(lifeServiceBean);
                }

                if(isRefresh==1){
                    if(lifeServiceAdapter !=null){
                        lifeServiceAdapter.notifyDataSetChanged();
                        mlifeServiceList.onRefreshComplete();
                    }else{
                        lifeServiceAdapter = new LifeServiceAdapter(this,mLifeList);
                        lifeServiceAdapter.notifyDataSetChanged();
                        mlifeServiceList.onRefreshComplete();
                    }

                }else if(isRefresh==2){
                    if(lifeServiceAdapter !=null){
                        lifeServiceAdapter.notifyDataSetChanged();
                        mlifeServiceList.onRefreshComplete();
                    }else{
                        lifeServiceAdapter = new LifeServiceAdapter(this,mLifeList);
                        lifeServiceAdapter.notifyDataSetChanged();
                        mlifeServiceList.onRefreshComplete();
                    }
                }else{
                    lifeServiceAdapter.add(mLifeList);
                    lifeServiceAdapter.notifyDataSetChanged();
                    mlifeServiceList.onRefreshComplete();
                }

            }else{
                lifeServiceAdapter.notifyDataSetChanged();
                mlifeServiceList.onRefreshComplete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {
        mlifeServiceList.onRefreshComplete();
    }
}
