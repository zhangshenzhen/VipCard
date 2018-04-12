package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.MerchantItemAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.NewMerchantListBean;
import com.bjypt.vipcard.model.RootMerchant;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshExpandableListView;
import com.bjypt.vipcard.umeng.UmengCountContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/13
 * Use by 搜索附近商家
 */
public class SearchMerchantActivity extends BaseActivity implements VolleyCallBack<String> {

    private RelativeLayout mBack;
    private TextView btn_searchStart;
    private EditText mSearchMerchant;
    private PullToRefreshExpandableListView mSearchListView;
    private MerchantItemAdapter homeMerchantAdapter;
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;
    private int isRefresh = 0;
    private Map<String, String> params = new HashMap<>();
    private List<NewMerchantListBean> list;
    private int begin = 0;
    private int pageLength = 10;//限制每页显示data条数
    private ExpandableListView listView;
    private ImageView iv_default_serchant_merchant_pic;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_search_merchant);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        mBack = (RelativeLayout) findViewById(R.id.search_back);
        mSearchMerchant = (EditText) findViewById(R.id.search_merchant);
        btn_searchStart = (TextView) findViewById(R.id.btn_searchStart);
        iv_default_serchant_merchant_pic = (ImageView) findViewById(R.id.iv_default_serchant_merchant_pic);
        mSearchListView = (PullToRefreshExpandableListView) findViewById(R.id.search_merchant_list);
        listView = mSearchListView.getRefreshableView();
        mSearchListView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setGroupIndicator(null);
        listView.setDividerHeight(0);
        mSearchListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ExpandableListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                onRequest(QUERY_REFERSH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                onRequest(QUERY_MORE);
            }
        });


    }

    @Override
    public void afterInitView() {

        list = new ArrayList<NewMerchantListBean>();
    }

    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
        btn_searchStart.setOnClickListener(this);

    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.search_back:

                this.finish();
                break;
            case R.id.btn_searchStart:
                isRefresh = 0;
                begin = 0;
                if (mSearchMerchant.getText().toString() == null || mSearchMerchant.getText().equals("")) {
                    Toast.makeText(this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                } else {
                    beginSearch();
                }
                break;
        }
    }

    /**
     * 执行搜索
     */
    public void beginSearch() {

        if (params != null) {
            params.clear();
        }
        params.put("cityCode", Config.web.cityCode);
        params.put("lng", getFromSharePreference(Config.userConfig.lngu));
        params.put("lat", getFromSharePreference(Config.userConfig.latu));
        params.put("begin", begin + "");
        params.put("pageLength", pageLength + "");
        params.put("merchantName", mSearchMerchant.getText().toString());
        Wethod.httpPost(SearchMerchantActivity.this,1, Config.web.new_home_merchant_listview , params, this);
    }

    /**
     * 执行查询商家接口
     **/
    private void onRequest(int type) {

        if (type == QUERY_REFERSH) {
            if (params != null) {
                params.clear();
            }
            isRefresh = 1;
            begin = 0;
            params.put("cityCode",  Config.web.cityCode);
            params.put("lng", getFromSharePreference(Config.userConfig.lngu));
            params.put("lat", getFromSharePreference(Config.userConfig.latu));
            params.put("begin", begin + "");
            params.put("pageLength", pageLength + "");
            params.put("merchantName", mSearchMerchant.getText().toString());

        } else {
            if (params != null) {
                params.clear();
            }

            isRefresh = 2;
            begin += 10;
            params.put("cityCode",  Config.web.cityCode);
            params.put("lng", getFromSharePreference(Config.userConfig.lngu));
            params.put("lat", getFromSharePreference(Config.userConfig.latu));
            params.put("begin", begin + "");
            params.put("pageLength", pageLength + "");
            params.put("merchantName", mSearchMerchant.getText().toString());
        }

        Wethod.httpPost(SearchMerchantActivity.this,1, Config.web.new_home_merchant_listview , params, this);
    }

    @Override
    public void onSuccess(int reqcode, String result) {

        if (reqcode == 1) {
            Log.e("GGGG", result.toString());
            try {
                RootMerchant homeMerchantListBean = getConfiguration().readValue(result.toString(), RootMerchant.class);
                if (homeMerchantListBean.getResultData().size() != 0) {
                    iv_default_serchant_merchant_pic.setVisibility(View.GONE);
                    if (isRefresh == 1 || isRefresh == 0) {
                        if (homeMerchantAdapter != null) {
                            list.clear();
                        }
                    }
                    for (int i = 0; i < homeMerchantListBean.getResultData().size(); i++) {
                        NewMerchantListBean listBean = new NewMerchantListBean();
//                        listBean.setDiscount(homeMerchantListBean.getResultData().get(i).getDiscount());
//                        listBean.setDistance(homeMerchantListBean.getResultData().get(i).getDistance());
//                        listBean.setLogo(homeMerchantListBean.getResultData().get(i).getLogo());
//                        listBean.setMuname(homeMerchantListBean.getResultData().get(i).getMuname());
//                        listBean.setPkmuser(homeMerchantListBean.getResultData().get(i).getPkmuser());
//                        listBean.setAddress(homeMerchantListBean.getResultData().get(i).getAddress());
//                        listBean.setIntegral(homeMerchantListBean.getResultData().get(i).getIntegral());
//                        listBean.setMerdesc(homeMerchantListBean.getResultData().get(i).getMerdesc());
//                        if (!"".equals(homeMerchantListBean.getResultData().get(i).getLinkage_pkdealer())) {
//                            listBean.setLinkage_pkdealer(homeMerchantListBean.getResultData().get(i).getLinkage_pkdealer());
//                        }

                        listBean.setDiscount(homeMerchantListBean.getResultData().get(i).getDiscount());
                        listBean.setDistance(homeMerchantListBean.getResultData().get(i).getDistance());
                        listBean.setLogo(homeMerchantListBean.getResultData().get(i).getLogo());
                        listBean.setMuname(homeMerchantListBean.getResultData().get(i).getMuname());
                        listBean.setPkmuser(homeMerchantListBean.getResultData().get(i).getPkmuser());
                        listBean.setAddress(homeMerchantListBean.getResultData().get(i).getAddress());
                        listBean.setIntegral(homeMerchantListBean.getResultData().get(i).getIntegral());
                        listBean.setMerdesc(homeMerchantListBean.getResultData().get(i).getMerdesc());
                        if (!"".equals(homeMerchantListBean.getResultData().get(i).getLinkage_pkdealer())) {
                            listBean.setLinkage_pkdealer(homeMerchantListBean.getResultData().get(i).getLinkage_pkdealer());
                        }
                        if (!"".equals(homeMerchantListBean.getResultData().get(i).getVirtualActivity())) {
                            listBean.setVirtualActivity(homeMerchantListBean.getResultData().get(i).getVirtualActivity());
                        }
                        if (!"".equals(homeMerchantListBean.getResultData().get(i).getFirstConsume())) {
                            listBean.setFirstConsume(homeMerchantListBean.getResultData().get(i).getFirstConsume());
                        }
                        if (!"".equals(homeMerchantListBean.getResultData().get(i).getConsumeReduction())) {
                            listBean.setConsumeReduction(homeMerchantListBean.getResultData().get(i).getConsumeReduction());
                        }
                        list.add(listBean);
                    }


                    if (isRefresh == 1) {
                        if (homeMerchantAdapter != null) {
                            homeMerchantAdapter.notifyDataSetChanged();
                            mSearchListView.onRefreshComplete();
                        } else {
                            homeMerchantAdapter = new MerchantItemAdapter(list, this, handler);
                            homeMerchantAdapter.notifyDataSetChanged();
                            mSearchListView.onRefreshComplete();
                        }

                    } else if (isRefresh == 2) {
                        if (homeMerchantAdapter != null) {
                            homeMerchantAdapter.notifyDataSetChanged();
                            mSearchListView.onRefreshComplete();
                        } else {
                            homeMerchantAdapter = new MerchantItemAdapter(list, this,handler);
                            homeMerchantAdapter.notifyDataSetChanged();
                            mSearchListView.onRefreshComplete();
                        }
                    } else {
                        homeMerchantAdapter = new MerchantItemAdapter(list,this,  handler);
                        listView.setAdapter(homeMerchantAdapter);
                        homeMerchantAdapter.notifyDataSetChanged();
                        mSearchListView.onRefreshComplete();
                    }
                } else {
                    mSearchListView.onRefreshComplete();
                }

            } catch (IOException e) {
                Log.i("aaa", "" + e.toString());
                e.printStackTrace();
            }

            listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    Intent intent = new Intent(SearchMerchantActivity.this, NewSubscribeDishesActivity.class);
                    intent.putExtra("distance", list.get(groupPosition).getDistance() + "");
                    intent.putExtra("pkmuser", list.get(groupPosition).getPkmuser() + "");
                    startActivity(intent);

                    return true;
                }
            });


        }

    }

    @Override
    public void onFailed(int reqcode, String result) {
        if (reqcode == 1) {
            Wethod.ToFailMsg(this, result.toString());
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        mSearchListView.onRefreshComplete();
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 110) {
                String value = (String) msg.obj;
                int sum = Integer.parseInt(value.substring(0, value.indexOf("a")));
                if (sum % 2 == 0) {
                    listView.collapseGroup(Integer.parseInt(value.substring(value.indexOf("a") + 1)));
                } else {
                    listView.expandGroup(Integer.parseInt(value.substring(value.indexOf("a") + 1)));
                }
            }
        }
    };
}
