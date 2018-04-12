package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.FineDiscountMerchantAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.FineMerchantBean;
import com.bjypt.vipcard.model.ListModelBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/3/13.
 */

public class FineDiscountSearchMerchantListActivity extends BaseActivity implements VolleyCallBack<String> {

    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;
    private int isRefresh = 0;
    private int currentPage = 0;
    private int pageLength = 10;//限制每页显示data条数
    private PullToRefreshListView search_merchant_list;
    private ListView pullListView;
    private List<FineMerchantBean> listmerchant = new ArrayList<FineMerchantBean>();
    private FineDiscountMerchantAdapter fineDiscountMerchantAdapter;

    private ImageView iv_default_serchant_merchant_pic;

    private RelativeLayout search_back;
    private EditText search_merchant;
    private TextView btn_searchStart;

    private static final int request_code_merchant_list = 1;
    private String lat;
    private String lng;

    @Override
    public void onSuccess(int reqcode, String result) {
        if(reqcode == request_code_merchant_list){
            analyze_result_merchant(result);
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {
        Wethod.ToFailMsg(this, result.toString());
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_fine_discount_search_merchantlist);
    }

    @Override
    public void beforeInitView() {
        lat = SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.latu);
        lng = SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.lngu);
    }

    @Override
    public void initView() {
        search_back = (RelativeLayout)findViewById(R.id.search_back);
        search_merchant = (EditText)findViewById(R.id.search_merchant);
        btn_searchStart = (TextView)findViewById(R.id.btn_searchStart);

        search_merchant_list = (PullToRefreshListView) findViewById(R.id.search_merchant_list);
        search_merchant_list.setMode(PullToRefreshBase.Mode.BOTH);
        pullListView = search_merchant_list.getRefreshableView();
        pullListView.setDividerHeight(0);

        iv_default_serchant_merchant_pic = (ImageView)findViewById(R.id.iv_default_serchant_merchant_pic);
    }

    @Override
    public void afterInitView() {
        fineDiscountMerchantAdapter = new FineDiscountMerchantAdapter(listmerchant, this);
        pullListView.setAdapter(fineDiscountMerchantAdapter);
    }

    @Override
    public void bindListener() {
        search_back.setOnClickListener(this);
        btn_searchStart.setOnClickListener(this);
        search_merchant_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getHttpMerchantList(QUERY_REFERSH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getHttpMerchantList(QUERY_MORE);
            }
        });
        search_merchant_list.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(FineDiscountSearchMerchantListActivity.this, NewSubscribeDishesActivity.class);
                intent.putExtra("distance", listmerchant.get(position-1).getDistance());
                intent.putExtra("rechargeactivity", "");
                intent.putExtra("is_half", "Y");
                intent.putExtra("pkmuser", listmerchant.get(position-1).getPkmuser());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.search_back:
                finish();
                break;
            case R.id.btn_searchStart:
                getHttpMerchantList(QUERY_REFERSH);
                break;
        }
    }

    /**
     * 5.14	【五折店】五折店商家列表
     */
    private void getHttpMerchantList(int type) {
        Map<String, String> params = new HashMap<String, String>();
        if (type == QUERY_REFERSH) {
            isRefresh = 1;
            currentPage = 0;
        } else {
            isRefresh = 2;
            currentPage += 10;
        }
        params.put("queryDate", "");
        params.put("timeType",  ""); //1 全天 2 早上 3 中午 4 下午
        params.put("cityCode", getFromSharePreference(Config.userConfig.citycode));
        params.put("begin", currentPage + "");
        params.put("pageLength", pageLength + "");
        params.put("queryName", search_merchant.getText().toString());
        params.put("lat",lat);
        params.put("lng",lng);
        Wethod.httpPost(this, request_code_merchant_list, Config.web.fine_discount_get_merchant, params, this);
    }

    private void analyze_result_merchant(String result) {
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            ListModelBean<FineMerchantBean> listModelBean = null;
            listModelBean = objectMapper.readValue(result.toString(), new TypeReference<ListModelBean<FineMerchantBean>>() {
            });
            if (listModelBean.getResultData().size() > 0) {
                if (isRefresh == 1) {
                    listmerchant.clear();
                }
                listmerchant.addAll(listModelBean.getResultData());
                fineDiscountMerchantAdapter.notifyDataSetChanged();
                search_merchant_list.onRefreshComplete();
            } else if (listModelBean.getResultData().size() == 0 && isRefresh == 1) {
                listmerchant.clear();
                fineDiscountMerchantAdapter.notifyDataSetChanged();
                search_merchant_list.onRefreshComplete();
            } else {
                search_merchant_list.onRefreshComplete();
            }
            if(listmerchant.size() ==0 ){
                iv_default_serchant_merchant_pic.setVisibility(View.VISIBLE);
            }else{
                iv_default_serchant_merchant_pic.setVisibility(View.GONE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
