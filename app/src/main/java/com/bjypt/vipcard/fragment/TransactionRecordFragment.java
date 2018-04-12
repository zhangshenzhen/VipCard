package com.bjypt.vipcard.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.TransactionRecordAdapter;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.RechangeRecordRootBean;
import com.bjypt.vipcard.model.TransactionOrRechangeBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/10.
 */
public class TransactionRecordFragment extends Fragment implements VolleyCallBack{
    private PullToRefreshListView listView;
    private TransactionRecordAdapter adapter;
    private TransactionOrRechangeBean bean;
    private List<TransactionOrRechangeBean> lists;
    private View view;
    private String pkregister;
    private String type ="1";
    private int begin =0;
    private int pageLength = 10;
    private Map<String,String> map;
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;
    private int isrefresh;
    private ImageView iv_transaction_record_pic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(getActivity(),R.layout.layout_transaction_record_fragment,null);
        initView();
        initData();
        initListener();
        return view;
    }

    private void initView() {
        Log.e("liyunte", "TransactionRecordFragment        initView");
        iv_transaction_record_pic = (ImageView) view.findViewById(R.id.iv_transaction_record_pic);
        listView = (PullToRefreshListView) view.findViewById(R.id.transaction_listview);
        listView.setMode(PullToRefreshBase.Mode.BOTH);

    }
    public void setRequest(int value){
        if (value==QUERY_REFERSH){
            if (map!=null){
                map.clear();
            }
            begin=0;
            isrefresh=1;
            map.put("pkregister",pkregister);
            map.put("type",type);
            map.put("begin",begin+"");
            map.put("pageLength",pageLength+"");
            Wethod.httpPost(getActivity(),46, Config.web.find_transaction_record, map, TransactionRecordFragment.this);

        }else {
            if (map!=null){
                map.clear();
            }
            begin+=10;
            isrefresh=2;
            map.put("pkregister",pkregister);
            map.put("type",type);
            map.put("begin",begin+"");
            map.put("pageLength",pageLength+"");
            Wethod.httpPost(getActivity(),46, Config.web.find_transaction_record, map, TransactionRecordFragment.this);
        }

    }

    private void initData() {
        Log.e("liyunte","TransactionRecordFragment        initData");
        lists = new ArrayList<>();
        map = new HashMap<>();
        pkregister = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.pkregister);
        Log.e("liyunteee",pkregister);
        setRequest(QUERY_REFERSH);
    }

    private void initListener() {
        adapter = new TransactionRecordAdapter(lists,getActivity());
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                setRequest(QUERY_REFERSH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                setRequest(QUERY_MORE);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        UmengCountContext.onPageStart("TransactionRecordFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengCountContext.onPageEnd("TransactionRecordFragment");
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode==46){
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            Log.e("liyunteee", result.toString());
            try {
                RechangeRecordRootBean rootBean = objectMapper.readValue(result.toString(), RechangeRecordRootBean.class);
                Log.e("liyunteee", "------------------------");
                if (rootBean.getResultData().size()==0&&lists.size()==0){
                    iv_transaction_record_pic.setVisibility(View.VISIBLE);
                    listView.onRefreshComplete();
                }
                if(rootBean.getResultData().size()>0){
                    iv_transaction_record_pic.setVisibility(View.GONE);
                    if(isrefresh==1){
                        if(adapter!=null){
                            Log.e("liyunteee", "!null----------------");
                            lists.clear();
                        }
                    }
                    for (int i=0;i<rootBean.getResultData().size();i++){
                        bean= new TransactionOrRechangeBean();
                        bean.setLogo_url(rootBean.getResultData().get(i).getLogo_url());
                        bean.setStatus_desc(rootBean.getResultData().get(i).getStatus_desc());
                        bean.setTrade_desc(rootBean.getResultData().get(i).getTrade_desc());
                        bean.setTrade_money(rootBean.getResultData().get(i).getTrade_money());
                        bean.setTrade_time(rootBean.getResultData().get(i).getTrade_time());
                        lists.add(bean);

                    }
                    if(isrefresh==1){
                        if(adapter !=null){
                            adapter.notifyDataSetChanged();
                            listView.onRefreshComplete();
                        }else{
                            Log.e("liyunteee","adapter-----------------");
                            adapter =  new TransactionRecordAdapter(lists,getActivity());
                            adapter.notifyDataSetChanged();
                            listView.onRefreshComplete();
                        }
                    }else if(isrefresh==2){
                        if(adapter !=null){
                            adapter.notifyDataSetChanged();
                            listView.onRefreshComplete();
                        }else{
                            adapter = new TransactionRecordAdapter(lists,getActivity());
                            adapter.notifyDataSetChanged();
                            listView.onRefreshComplete();
                        }
                    }else{
                        adapter.add(lists);
                        adapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    }
                }else{
                    listView.onRefreshComplete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            listView.onRefreshComplete();
        }


    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {
        listView.onRefreshComplete();
    }
}
