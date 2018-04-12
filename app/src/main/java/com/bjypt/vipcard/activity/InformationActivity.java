package com.bjypt.vipcard.activity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.InfomationAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.InfomationBean;
import com.bjypt.vipcard.model.InfomationData;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.umeng.UmengCountContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/31
 * Use by消息
 */
public class InformationActivity extends BaseActivity implements VolleyCallBack{

    private PullToRefreshListView mInfomationList;
    private  RelativeLayout mBack;
    private InfomationAdapter infomationAdapter;
    private InfomationBean infomationBean;
    private int begin = 0;
    private int pageLength = 10;
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;
    private int isRefresh = 0;
    private Map<String,String> params = new HashMap<>();
    private List<InfomationData> lists = new ArrayList<>();
    private String type;
    private ImageView iv_default_informattion_pic;
    private TextView tv_info_title;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.actiivty_information);
    }

    @Override
    public void beforeInitView() {
//        userId：用户主键
//        begin：开始位置
//        pageLength：长度
        type = getIntent().getStringExtra("type");
        onRequest(QUERY_REFERSH);
        /*params.put("userId", getFromSharePreference(Config.userConfig.pkregister));
        params.put("begin", begin+"");
        params.put("pageLength", pageLength+"");

        Wethod.httpPost(1, Config.web.get_news_infomation, params, this);
    }*/
    }

    @Override
    public void initView() {
        tv_info_title = (TextView) findViewById(R.id.tv_info_title);
        iv_default_informattion_pic = (ImageView) findViewById(R.id.iv_default_informattion_pic);
        mInfomationList = (PullToRefreshListView) findViewById(R.id.infomation_list);
        mBack = (RelativeLayout) findViewById(R.id.info_back);
        mInfomationList.setMode(PullToRefreshBase.Mode.BOTH);
        Log.e("TYZ","initView  lists:"+lists.size());
        infomationAdapter = new InfomationAdapter(lists,this,type);
        mInfomationList.setAdapter(infomationAdapter);

        //实现上啦刷新，下拉加载
        mInfomationList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                onRequest(QUERY_REFERSH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                onRequest(QUERY_MORE);
            }
        });
    }

    @Override
    public void afterInitView() {
        if (type.equals("0")){
            tv_info_title.setText("商家活动");
        }else if (type.equals("3")){
            tv_info_title.setText("系统消息");
        }else {
            tv_info_title.setText("商家优惠信息");
        }

    }

    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id){
            case R.id.info_back:
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if(reqcode == 1){
            Log.e("liyunte",",result.toString()"+result.toString());
            try {
                infomationBean = getConfiguration().readValue(result.toString(), InfomationBean.class);
                if (infomationBean.getResultData().size()==0&&lists.size()==0){
                    mInfomationList.onRefreshComplete();
                    iv_default_informattion_pic.setVisibility(View.VISIBLE);
                }else
                if(infomationBean.getResultData().size()>0){
                    iv_default_informattion_pic.setVisibility(View.GONE);
                    if(isRefresh==1){
                        if(infomationAdapter!=null){
                            lists.clear();
                        }
                    }
                    for(int i = 0;i<infomationBean.getResultData().size();i++){
                        InfomationData infomationData = new InfomationData();
                        infomationData.setMuname(infomationBean.getResultData().get(i).getMuname());
                        infomationData.setContent(infomationBean.getResultData().get(i).getContent());
                        infomationData.setLogo(infomationBean.getResultData().get(i).getLogo());
                        infomationData.setSenddate(infomationBean.getResultData().get(i).getSenddate());
                        infomationData.setPkmuser(infomationBean.getResultData().get(i).getPkmuser());
                        infomationData.setPkregister(infomationBean.getResultData().get(i).getPkregister());
                        infomationData.setType(infomationBean.getResultData().get(i).getType());
                        lists.add(infomationData);
                    }

                    if(isRefresh==1){
                        Log.e("TYZ","LISTS1:"+lists.size());
                        if(infomationAdapter !=null){
                            infomationAdapter.notifyDataSetChanged();
                            mInfomationList.onRefreshComplete();
                        }else{
                            infomationAdapter = new InfomationAdapter(lists,this,type);
                            infomationAdapter.notifyDataSetChanged();
                            mInfomationList.onRefreshComplete();
                        }

                    }else if(isRefresh==2){
                        Log.e("TYZ","LISTS2:"+lists.size());
                        if(infomationAdapter !=null){
                            infomationAdapter.notifyDataSetChanged();
                            mInfomationList.onRefreshComplete();
                        }else{
                            infomationAdapter = new InfomationAdapter(lists,this,type);
                            infomationAdapter.notifyDataSetChanged();
                            mInfomationList.onRefreshComplete();
                        }
                    }else{
                        Log.e("TYZ","LISTS:"+lists.size());
                        infomationAdapter.notifyDataSetChanged();
                        mInfomationList.onRefreshComplete();
                    }
                }else{
                    Log.e("TYZ","LISTS3:"+lists.size());
                    mInfomationList.onRefreshComplete();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if(reqcode == 1) {
            Wethod.ToFailMsg(this,result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        mInfomationList.onRefreshComplete();
    }

    private void onRequest(int value) {

        if (value == QUERY_REFERSH) {
            if (params != null) {
                params.clear();
            }
            isRefresh = 1;
            begin = 0;
            params.put("type",type);
            params.put("userId", getFromSharePreference(Config.userConfig.pkregister));
            params.put("begin", 0+"");
            params.put("pageLength", pageLength + "");

        } else {
            if (params != null) {
                params.clear();
            }

            isRefresh = 2;
            begin += 10;
            params.put("type",type);
            params.put("userId", getFromSharePreference(Config.userConfig.pkregister));
            params.put("begin", begin + "");
            params.put("pageLength", pageLength + "");
        }

        Wethod.httpPost(InformationActivity.this,1, Config.web.get_news_infomation, params, this);
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
