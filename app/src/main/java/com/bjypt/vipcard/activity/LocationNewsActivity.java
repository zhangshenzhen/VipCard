package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.NewHomeAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.XinWenAdBean;
import com.bjypt.vipcard.model.XinWenData;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/9 0009.
 */

public class LocationNewsActivity extends BaseActivity implements VolleyCallBack<String> {

    private PullToRefreshListView mListView;
    private int page = 1;
    private long lasttime;
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;
    private int isRefresh = 0;
    private NewHomeAdapter newHomeAdapter;
    private List<Integer> picList  = new ArrayList<>();//表示新闻当中惠出现几张图片
    private XinWenData xinWenData;
    private RelativeLayout location_news_back;
    private List<XinWenAdBean> xinwenList = new ArrayList<>();

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_locationnews);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        location_news_back = (RelativeLayout) findViewById(R.id.location_news_back);
        mListView = (PullToRefreshListView) findViewById(R.id.listview_location_news);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    @Override
    public void afterInitView() {
        location_news_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Wethod.httpPost(this,1991, Config.web.new_home_xinwen+page+"&lasttime="+lasttime,null,this);
    }

    @Override
    public void bindListener() {

        newHomeAdapter = new NewHomeAdapter(this,xinwenList,picList);
        mListView.setAdapter(newHomeAdapter);

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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
    public void onClickEvent(View v) {

    }

    private void onRequest(int type) {

        if (type == QUERY_REFERSH) {
            isRefresh = 1;
            page = 1;
            lasttime=Long.parseLong(String.valueOf(System.currentTimeMillis()).toString().substring(0,10));
        } else {
            isRefresh = 2;
            page += 1;
            lasttime=Long.parseLong(String.valueOf(System.currentTimeMillis()).toString().substring(0,10));
        }

        Wethod.httpPost(this,1991,Config.web.new_home_xinwen+page+"&lasttime="+lasttime,null,this);
    }


    @Override
    public void onSuccess(int reqcode, String result) {
        try {
            xinWenData = getConfiguration().readValue(result.toString(), XinWenData.class);
            if(picList!=null&&isRefresh!=2){
                picList.clear();
            }
            for(int i = 0;i<xinWenData.getResultData().size();i++){
                picList.add(Integer.parseInt(xinWenData.getResultData().get(i).getPicSize()));
            }

            if(xinwenList!=null&&isRefresh!=2){
                Log.e("zxc","xinwenList:"+xinwenList.size()+"   isRefresh:"+isRefresh);
                xinwenList.clear();
            }
            if(isRefresh == 1){
                if(xinwenList!=null){
                    Log.e("zxc","xinwenList2:"+xinwenList.size()+"   isRefresh2:"+isRefresh);
                    xinwenList.clear();
                }
            }
            Log.e("xyxy","xinwen size :"+xinwenList.size());

            for(int m = 0;m<xinWenData.getResultData().size();m++){
                XinWenAdBean xinWenAdBean = new XinWenAdBean();
                xinWenAdBean.setId(xinWenData.getResultData().get(m).getId());
                xinWenAdBean.setLastTime(xinWenData.getResultData().get(m).getLasttime());
                xinWenAdBean.setTitle(xinWenData.getResultData().get(m).getTitle());
                xinWenAdBean.setPics(xinWenData.getResultData().get(m).getPics());
                xinwenList.add(xinWenAdBean);
            }

            if(isRefresh == 1){
                if(newHomeAdapter == null){
                    newHomeAdapter = new NewHomeAdapter(this,xinwenList,picList);
                    newHomeAdapter.notifyDataSetChanged();
                }else{
                    newHomeAdapter.notifyDataSetChanged();
                    mListView.onRefreshComplete();
                }
            }else if(isRefresh == 2){
                newHomeAdapter = new NewHomeAdapter(this,xinwenList,picList);
                newHomeAdapter.notifyDataSetChanged();
                mListView.onRefreshComplete();
            }else{
                if(newHomeAdapter == null){
                    newHomeAdapter = new NewHomeAdapter(this,xinwenList,picList);
                    newHomeAdapter.notifyDataSetChanged();
                }else{
                    newHomeAdapter.notifyDataSetChanged();
                    mListView.onRefreshComplete();
                }
            }


            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(LocationNewsActivity.this,XinWenH5Activity.class);
                    intent.putExtra("xinwen_id",xinwenList.get(position-1).getId());
                    startActivity(intent);
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailed(int reqcode, String result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
