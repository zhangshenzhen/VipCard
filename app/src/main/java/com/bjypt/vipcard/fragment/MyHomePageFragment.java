package com.bjypt.vipcard.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.CircleMyActivity;
import com.bjypt.vipcard.activity.FriendsCircleWeb;
import com.bjypt.vipcard.adapter.MyHomePageAdapter;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.MyHomePageBean;
import com.bjypt.vipcard.model.MyHomePageData;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.MyListView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class MyHomePageFragment extends BaseFrament implements VolleyCallBack<String> {

    private MyListView myListView;
    private View view;
    private LinearLayout myhomepage_ivbg;
    private LinearLayout zhanwei_linear;
    private TextView more_tv;
    private CircleProgressBar progressbar;

    private MyHomePageAdapter myHomePageAdapter;

    private MyHomePageBean myHomePageBean;
    private List<MyHomePageData> myHomePageDataList = new ArrayList<>();
    private List<MyHomePageData> myHomePageBeen = new ArrayList<>();
    Map<String, Integer> insertYear = new HashMap<>();
    final int QUERY_ATTENTION_MORE = 0x0101;
    final int QUERY_ATTENTION_REFERSH = 0x0110;
    private static final int MYHOMEPAGE_DATA_REQUEST = 0001;

    private String myUid;
    private int index;
    private int page = 0;
    private boolean is_refresh = true;

    public MyHomePageFragment() {
    }

    public MyHomePageFragment(int index, String myUid) {
        this.myUid = myUid;
        this.index = index;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_and_collect, container, false);
        return view;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {

        //进度条
        progressbar = (CircleProgressBar) view.findViewById(R.id.progressbar);
        progressbar.setColorSchemeResources(R.color.gallery_black);

        zhanwei_linear = (LinearLayout) view.findViewById(R.id.zhanwei_linear);
        myhomepage_ivbg = (LinearLayout) view.findViewById(R.id.myhomepage_ivbg);
        myListView = (MyListView) view.findViewById(R.id.my_mylistview);
        more_tv = (TextView) view.findViewById(R.id.more_tv);
        myHomePageAdapter = new MyHomePageAdapter(getActivity(), myHomePageBeen);
        myListView.setAdapter(myHomePageAdapter);

    }

    @Override
    public void afterInitView() {
        progressbar.setVisibility(View.VISIBLE);
        if (index == 0) {
            getMyHomePageData(QUERY_ATTENTION_REFERSH);
        } else {
            getCollectionData(QUERY_ATTENTION_REFERSH);
        }
    }

    @Override
    public void bindListener() {
        more_tv.setOnClickListener(this);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), FriendsCircleWeb.class);
                String url = Config.web.circle_web_activity + myHomePageBeen.get(i).getId();
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.more_tv:
                if (index == 0) {
                    getMyHomePageData(QUERY_ATTENTION_MORE);
                } else {
                    getCollectionData(QUERY_ATTENTION_MORE);
                }
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (isVisible()) {
            CircleMyActivity circleMyActivity = (CircleMyActivity) getActivity();
            circleMyActivity.onRefreshComplete();
        }
        progressbar.setVisibility(View.GONE);
        if (reqcode == MYHOMEPAGE_DATA_REQUEST) {                    //我的
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                myHomePageBean = objectMapper.readValue(result.toString(), MyHomePageBean.class);

                if (myHomePageDataList.size() != 0) {
                    myHomePageDataList.clear();
                    myHomePageDataList.addAll(myHomePageBean.getResultData());
                } else {
                    myHomePageDataList.addAll(myHomePageBean.getResultData());
                }

                //重新组合数据，显示年和月和内容的type=1,显示月和内容的type=2，显示内容的type=3
                //判断是下拉刷新需要清一下myHomePageBeen防止出现重复数据
                if (is_refresh) {
                    myHomePageBeen.clear();
                    insertYear = new HashMap<>();
                }
                for (int i = 0; i < myHomePageDataList.size(); i++) {
                    MyHomePageData bean = myHomePageDataList.get(i);
                    if (!insertYear.containsKey(bean.getYear() + "")) {
                        insertYear.put(bean.getMonth() + "", 0);
                        insertYear.put(bean.getYear() + "", 0);
                        insertYear.put(bean.getYear() + "-" + bean.getMonth(), 0);
                        bean.setType(1);
                    } else if (!insertYear.containsKey(bean.getYear() + "-" + bean.getMonth())) {  //当年当月不包含在map中
                        insertYear.put(bean.getYear() + "-" + bean.getMonth(), 0);          //如果没有设置过type=2的数据，就设置第一个数据为type=2
                        insertYear.put(bean.getMonth() + "", 0);
                        bean.setType(2);
                    } else if (!insertYear.containsKey(bean.getMonth() + "")) {
                        insertYear.put(bean.getMonth() + "", 0);
                        bean.setType(2);
                    } else {
                        bean.setType(3);
                    }
                    myHomePageBeen.add(bean);
                }

                //没有数据显示默认的，暂无内容
                if (myHomePageBean == null || myHomePageBeen.size() == 0) {
                    myhomepage_ivbg.setVisibility(View.VISIBLE);
                    zhanwei_linear.setVisibility(View.GONE);
                    myListView.setVisibility(View.GONE);
                } else {
                    myhomepage_ivbg.setVisibility(View.GONE);
                    zhanwei_linear.setVisibility(View.VISIBLE);
                    myListView.setVisibility(View.VISIBLE);
                    myHomePageAdapter.notifyDataSetChanged();
                }

                if (myHomePageBean.getResultData().size() < 10) {          //数据小于十条，不显示加载更多
                    more_tv.setVisibility(View.GONE);
                } else {
                    more_tv.setVisibility(View.VISIBLE);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {
        if (reqcode == MYHOMEPAGE_DATA_REQUEST) {
            //请求失败的时候显示暂无内容
            myhomepage_ivbg.setVisibility(View.VISIBLE);
            myListView.setVisibility(View.GONE);
        }
    }

    /* 我的主页数据请求*/
    private void getMyHomePageData(int refresh_type) {
        if (refresh_type == QUERY_ATTENTION_REFERSH) {
            page = 0;
            is_refresh = true;
        } else {
            page++;
            is_refresh = false;
        }
        Map<String, String> params = new HashMap<>();
        params.put("uid", myUid);       //uid+""
        params.put("page", page + "");
        params.put("size", "10");
        params.put("system_code", "android");
        Wethod.httpPost(getActivity(), MYHOMEPAGE_DATA_REQUEST, Config.web.myHomePageList, params, this, View.GONE);
    }

    /* 收藏数据请求*/
    private void getCollectionData(int refresh_type) {
        if (refresh_type == QUERY_ATTENTION_REFERSH) {
            page = 0;
            is_refresh = true;
        } else {
            page++;
            is_refresh = false;
        }

        Map<String, String> params = new HashMap<>();
        params.put("uid", myUid);       //uid+""
        params.put("size", "10");
        params.put("page", page + "");
        params.put("system_code", "android");
        Wethod.httpPost(getActivity(), MYHOMEPAGE_DATA_REQUEST, Config.web.collectionList, params, this, View.GONE);
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

}
