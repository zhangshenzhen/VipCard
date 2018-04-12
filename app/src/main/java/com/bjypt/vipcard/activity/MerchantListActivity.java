package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.MerchantPullListAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.MerchantPullListBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.utils.ObjectMapperFactory;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MerchantListActivity extends BaseActivity implements VolleyCallBack<String>{

    private LinearLayout back;      //返回键
    private PullToRefreshListView pull_listView;  //列表list
    private int begin = 0;
    private int pageLength = 10;
    private List<MerchantPullListBean.ResultDataBean> resultDataBeanList = new ArrayList<>();
    private boolean is_refresh = true;
    final int QUERY_EXERCISE_MORE = 0x0101;
    final int QUERY_EXERCISE_REFERSH = 0x0110;
    private MerchantPullListAdapter myAdapter;
    private ListView listView;

    private String linkage_pkdealer;  // 连锁店主键
    private String pkmuser;            //商家主键
    private String lat;                //经纬度
    private String lng;                //经纬度
    private String versionCode;       //版本号

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_merchant_list);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        pkmuser = intent.getStringExtra("pkmuser");
        linkage_pkdealer = intent.getStringExtra("linkage_pkdealer");
        lat = intent.getStringExtra("lat");
        lng = intent.getStringExtra("lng");
        versionCode = intent.getStringExtra("versionCode");
    }

    @Override
    public void initView() {
        back = (LinearLayout) findViewById(R.id.ly_back_merchantList);
        //PullToRefreshListView的操作
        pull_listView = (PullToRefreshListView) findViewById(R.id.lv_merchantlist);
        pull_listView.setMode(PullToRefreshBase.Mode.BOTH);
        pull_listView.setScrollingWhileRefreshingEnabled(true);
        myAdapter = new MerchantPullListAdapter(this,resultDataBeanList);
        pull_listView.setAdapter(myAdapter);

        pull_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MerchantListActivity.this, NewSubscribeDishesActivity.class);
                int itemId = (int) adapterView.getAdapter().getItemId(i);
                intent.putExtra("pkmuser", resultDataBeanList.get(itemId).getPkmuser());
                intent.putExtra("distance", resultDataBeanList.get(itemId).getDistance());
                intent.putExtra("rechargeactivity", resultDataBeanList.get(itemId).getRechargeActivity());
                startActivity(intent);
            }
        });
    }

    @Override
    public void afterInitView() {
        pull_listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadDatas(QUERY_EXERCISE_REFERSH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadDatas(QUERY_EXERCISE_MORE);
            }
        });

        //首次来到页面加载数据
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                pull_listView.setRefreshing();
                return true;
            }
        }).sendEmptyMessageDelayed(0, 300);


    }

    @Override
    public void bindListener() {
        back.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch(v.getId()){
            case R.id.ly_back_merchantList:
                finish();
                break;
        }
    }

    private void loadDatas(int refresh_type){
        Map<String, String> params = new HashMap<>();
        if (refresh_type == QUERY_EXERCISE_REFERSH) {
            begin = 0;
            is_refresh = true;
        } else {
            begin += pageLength;
            is_refresh = false;
        }
        params.put("versionCode", versionCode);
        params.put("pkmuser",pkmuser);
        params.put("cityCode", getFromSharePreference(Config.userConfig.citycode));//getFromSharePreference(Config.userConfig.citycode)
        params.put("lng", lng);
        params.put("lat", lat);
        params.put("begin", begin+"");
        params.put("pageLength", pageLength+"");
        params.put("linkage_pkdealer", linkage_pkdealer);

//        params.put("versionCode", "4.4.5");
//        params.put("pkmuser",pkmuser);
//        params.put("cityCode", "025");//getFromSharePreference(Config.userConfig.citycode)
//        params.put("lng", "118.909761");
//        params.put("lat", "32.091509");
//        params.put("begin", begin+"");
//        params.put("pageLength", pageLength+"");
//        params.put("linkage_pkdealer", "696a9c5179c04673a10188d0b3d86dfa");

        Wethod.httpPost(this, 1822, Config.web.chain_stores_list, params, this);

    }


    @Override
    public void onSuccess(int reqcode, String result) {
        pull_listView.onRefreshComplete();
        switch(reqcode){
            case 1822:
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                try {
                    MerchantPullListBean merchantListBean =objectMapper.readValue(result.toString(),MerchantPullListBean.class);
                    // 判断是下拉刷新还是上拉加载
                    if (is_refresh) { // 下拉刷新
                        resultDataBeanList.clear();
                        resultDataBeanList.addAll(merchantListBean.getResultData());
                    } else { // 加载更多
                        resultDataBeanList.addAll(merchantListBean.getResultData());
                    }
                    myAdapter.notifyDataSetChanged();
                    pull_listView.onRefreshComplete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {
        pull_listView.onRefreshComplete();// 停止刷新
    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
