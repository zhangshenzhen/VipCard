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
import com.bjypt.vipcard.adapter.PrecentRecordAdapter;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.PrecentRecordBean;
import com.bjypt.vipcard.model.PresentRecordRootBean;
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
 * 提现记录
 * Created by liyunte on 2016/5/10.
 */
public class PrecentRecordFragment extends Fragment implements VolleyCallBack{
    private PullToRefreshListView listView;
    private PrecentRecordAdapter adapter;
    private PrecentRecordBean bean;
    private List<PrecentRecordBean> lists;

    /*userId：用户Id
    type: 查询者类型: 1 商户 2用户 3经销商
    begin:起始位置
    pageLength:页数*/
    private String userId;
    private String type ="2";
    private int begin =0;
    private int pageLength = 10;
    private Map<String,String> map;
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;
    private int isrefresh;
private ImageView iv_default_present_record_pic;

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(getActivity(),R.layout.layout_present_record_fragment,null);
        initView();
        initData();
        initListener();
        return view;
    }



    private void initView() {
        iv_default_present_record_pic = (ImageView) view.findViewById(R.id.iv_default_present_record_pic);
        listView = (PullToRefreshListView) view.findViewById(R.id.present_listview);
        listView.setMode(PullToRefreshBase.Mode.BOTH);

    }

    private void initData() {
        lists = new ArrayList<>();
        map = new HashMap<>();
        userId = SharedPreferenceUtils.getFromSharedPreference(getActivity(),Config.userConfig.pkregister);
       setRequest(QUERY_REFERSH);
       adapter = new  PrecentRecordAdapter(lists,getActivity());
        listView.setAdapter(adapter);

    }
    public void setRequest(int value){
        if (value==QUERY_REFERSH){
            if (map!=null){
                map.clear();
            }
            begin=0;
            isrefresh=1;
            map.put("userId",userId);
            map.put("type",type);
            map.put("begin",begin+"");
            map.put("pageLength",pageLength+"");
            Wethod.httpPost(getActivity(),46, Config.web.find_present_record, map, this);

        }else {
            if (map!=null){
                map.clear();
            }
            begin+=10;
            isrefresh=2;
            map.put("userId",userId);
            map.put("type",type);
            map.put("begin",begin+"");
            map.put("pageLength",pageLength+"");
            Wethod.httpPost(getActivity(),46, Config.web.find_present_record, map, this);
        }

    }


    private void initListener() {
        adapter = new PrecentRecordAdapter(lists,getActivity());
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
        UmengCountContext.onPageStart("PrecentRecordFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengCountContext.onPageEnd("PrecentRecordFragment");
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode==46){
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            Log.e("liyunteee", result.toString());
            try {
                PresentRecordRootBean rootBean = objectMapper.readValue(result.toString(), PresentRecordRootBean.class);
                Log.e("liyunteee", "------------------------");
                if (rootBean.getResultData().size()==0&&lists.size()==0){
                    listView.onRefreshComplete();
                    iv_default_present_record_pic.setVisibility(View.VISIBLE);
                }else
                if(rootBean.getResultData().size()>0){
                    iv_default_present_record_pic.setVisibility(View.GONE);
                    if(isrefresh==1){
                        if(adapter!=null){
                            Log.e("liyunteee", "!null----------------");
                            lists.clear();
                        }
                    }
                    for (int i=0;i<rootBean.getResultData().size();i++){
                         bean= new PrecentRecordBean();
                        bean.setAmount(rootBean.getResultData().get(i).getAmount());
                        bean.setApply_time(rootBean.getResultData().get(i).getApply_time());
                        bean.setBankcardno(rootBean.getResultData().get(i).getBankcardno());
                        bean.setBankname(rootBean.getResultData().get(i).getBankname());
                        bean.setBankusername(rootBean.getResultData().get(i).getBankusername());
                        bean.setBankuserphone(rootBean.getResultData().get(i).getBankuserphone());
                        bean.setStatus_desc(rootBean.getResultData().get(i).getStatus_desc());
                        bean.setRate(rootBean.getResultData().get(i).getRate());
                        bean.setResultamount(rootBean.getResultData().get(i).getResultamount());
                        lists.add(bean);

                    }
                    if(isrefresh==1){
                        if(adapter !=null){
                            adapter.notifyDataSetChanged();
                            listView.onRefreshComplete();
                        }else{
                            Log.e("liyunteee","adapter-----------------");
                            adapter =  new PrecentRecordAdapter(lists,getActivity());
                            adapter.notifyDataSetChanged();
                            listView.onRefreshComplete();
                        }
                    }else if(isrefresh==2){
                        if(adapter !=null){
                            adapter.notifyDataSetChanged();
                            listView.onRefreshComplete();
                        }else{
                            adapter = new PrecentRecordAdapter(lists,getActivity());
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
