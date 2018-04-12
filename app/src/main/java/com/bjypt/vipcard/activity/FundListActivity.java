package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.FundListAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.BillsDatailBean;
import com.bjypt.vipcard.model.FundListBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.umeng.UmengCountContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/5.
 */
public class FundListActivity extends BaseActivity implements VolleyCallBack<String> {

    private RelativeLayout layout_back;
    private PullToRefreshListView mListView;
    private List<FundListBean> lists;
    private int begin = 0;
    private int pageLength = 10;
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;
    private int isRefresh = 0;
    private Map<String, String> params = new HashMap<>();
    private BillsDatailBean billsDatailBean;
    private FundListAdapter mAdapter;
    private ImageView iv_default_fundlist_pic;

    @Override
    public void setContentLayout() {

        setContentView(R.layout.activity_fundlist);
    }

    @Override
    public void beforeInitView() {

        /**"userId：用户主键
         begin：起始
         pageLength：长度
         "f3c3dbacb96a49bba876be678b45c33f
         012af63bb1e6424aa163778aabcdb809
         2f024a7581e344c5b04512f618924c5e


         */

        lists = new ArrayList<>();
        params.put("userId", getFromSharePreference(Config.userConfig.pkregister));
        params.put("begin", begin + "");
        params.put("pageLength", pageLength + "");

        Wethod.httpPost(FundListActivity.this,1, Config.web.bills_detail, params, this);
    }

    @Override
    public void initView() {
        iv_default_fundlist_pic = (ImageView) findViewById(R.id.iv_default_fundlist_pic);
        layout_back = (RelativeLayout) findViewById(R.id.layout_back);
        mListView = (PullToRefreshListView) findViewById(R.id.mPullListview);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);


        mAdapter = new FundListAdapter(this);
        mListView.setAdapter(mAdapter);

    }

    @Override
    public void afterInitView() {

        lists = new ArrayList<>();
    }

    @Override
    public void bindListener() {

        layout_back.setOnClickListener(this);
        //实现上啦刷新，下拉加载
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
        switch (v.getId()) {
            case R.id.layout_back:

                this.finish();
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {

        if (reqcode == 1) {
            Log.i("aaa",""+result.toString());
            try {

                billsDatailBean = getConfiguration().readValue(result.toString(), BillsDatailBean.class);
                if (billsDatailBean.getResultData().size() == 0&&lists.size()==0){
                    iv_default_fundlist_pic.setVisibility(View.VISIBLE);
                }else if (billsDatailBean.getResultData().size() > 0) {
                    iv_default_fundlist_pic.setVisibility(View.GONE);
                    if (isRefresh == 1) {
                        if (mAdapter != null)
                            lists.clear();
                    }


                    for (int i = 0; i < billsDatailBean.getResultData().size(); i++) {

                        FundListBean mBean = new FundListBean();

                        mBean.setTradeLogo(billsDatailBean.getResultData().get(i).getLogo());
                        mBean.setType(billsDatailBean.getResultData().get(i).getType());
                        mBean.setTradeMoney(billsDatailBean.getResultData().get(i).getBalance());
                        mBean.setTradeNum(billsDatailBean.getResultData().get(i).getOrder_no());
                        mBean.setTradeTime(billsDatailBean.getResultData().get(i).getCreatetime());
                        mBean.setTradeName(billsDatailBean.getResultData().get(i).getMuname());
                        mBean.setPkid(billsDatailBean.getResultData().get(i).getPkid());
                        lists.add(mBean);
                    }

                    if (isRefresh == 1) {
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                            mListView.onRefreshComplete();
                        } else {
                            mAdapter = new FundListAdapter(this);
                            mAdapter.notifyDataSetChanged();
                            mListView.onRefreshComplete();
                        }

                    } else if (isRefresh == 2) {
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                            mListView.onRefreshComplete();

                        } else {
                            mAdapter = new FundListAdapter(this);
                            mAdapter.notifyDataSetChanged();
                            mListView.onRefreshComplete();
                        }
                    } else {

                        mAdapter.add(lists);
                        mAdapter.notifyDataSetChanged();
                        mListView.onRefreshComplete();
                    }
                } else {
                    mListView.onRefreshComplete();
                }

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                        Log.i("aaa",""+position+">>>pkid="+lists.get(position - 1).getPkid());
//                        20a046be3c6f4627be8da6f42ed0d8c5  //这个pkid里没有任何数据  position=22时

                        if(lists.get(position-1).getType().equals("3")){
                            Intent mIntent = new Intent();
                            mIntent.setClass(FundListActivity.this, ListDetailsNoOrderActivity.class);
                            mIntent.putExtra("tradeType", lists.get(position - 1).getType());
                            mIntent.putExtra("pkid", lists.get(position - 1).getPkid());
                            mIntent.putExtra("tradeTime", lists.get(position - 1).getTradeTime());
                            startActivity(mIntent);

                        }else{
                            Intent mIntent = new Intent();
                            mIntent.setClass(FundListActivity.this, ListDetailsActivity.class);
                            mIntent.putExtra("tradeLogo", lists.get(position - 1).getTradeLogo());
                            mIntent.putExtra("tradeMoney", lists.get(position - 1).getTradeMoney());
                            mIntent.putExtra("tradeName", lists.get(position - 1).getTradeName());
                            mIntent.putExtra("tradeType", lists.get(position - 1).getType());
                            mIntent.putExtra("pkid", lists.get(position - 1).getPkid());
                            mIntent.putExtra("tradeTime", lists.get(position - 1).getTradeTime());
                            startActivity(mIntent);
                        }
                        

                    }
                });

            } catch (IOException e) {

                Log.i("aaa", "error=" + e.toString());
                e.printStackTrace();
            }
            mListView.onRefreshComplete();
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {
        Log.i("aaa", "onFailed");
        if (reqcode == 1) {
            Wethod.ToFailMsg(this, result.toString());
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        mListView.onRefreshComplete();
    }

    private void onRequest(int type) {

        if (type == QUERY_REFERSH) {
            if (params != null) {
                params.clear();
            }
            isRefresh = 1;
            begin = 0;
            params.put("userId", getFromSharePreference(Config.userConfig.pkregister));
            params.put("begin", 0 + "");
            params.put("pageLength", pageLength + "");

        } else {
            if (params != null) {
                params.clear();
            }
            isRefresh = 2;

            begin += 10;
            params.put("userId", getFromSharePreference(Config.userConfig.pkregister));
            params.put("begin", begin + "");
            params.put("pageLength", pageLength + "");
        }

        Wethod.httpPost(FundListActivity.this,1, Config.web.bills_detail, params, this);
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
