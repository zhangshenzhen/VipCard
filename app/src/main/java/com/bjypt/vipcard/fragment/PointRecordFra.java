package com.bjypt.vipcard.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.PointRecordAdapter;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.PointCommonBean;
import com.bjypt.vipcard.model.PointRecordListData;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.ObjectMapperFactory;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/5/11
 * Use by 积分记录
 */
public class PointRecordFra extends BaseFrament implements VolleyCallBack {

    private PullToRefreshListView mPointList;
    private String type = "0";
    private int begin = 0;
    private int pageLength = 10;
    final int QUERY_EXERCISE_MORE = 0x0101;
    final int QUERY_EXERCISE_REFERSH = 0x0110;
    Map<String, String> params = new HashMap<>();
    private int isRefresh;
    PointRecordListData pointRecordListData;
    PointRecordAdapter pointRecordAdapter;
    private List<PointCommonBean> pointRecordList;
    private ImageView iv_default_point_record_pic;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fra_point_record, container, false);
    }

    @Override
    public void beforeInitView() {
        pointRecordList = new ArrayList<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        params.put("pointtype", 0 + "");
        params.put("begin", begin + "");
        params.put("pageLength", pageLength + "");
        Wethod.httpPost(getActivity(), 1, Config.web.point_record_list, params, this);
    }

    @Override
    public void initView() {
        iv_default_point_record_pic = (ImageView) getActivity().findViewById(R.id.iv_default_point_record_pic);
        mPointList = (PullToRefreshListView) getActivity().findViewById(R.id.point_record_list);
        Log.e("TYZ", "mPointList:" + mPointList);
        mPointList.setMode(PullToRefreshBase.Mode.BOTH);
        /*当传递的FLAG = 1的时候表示当前为积分记录，当传递的FLAG = 2的时候表示当前为消分记录*/
        pointRecordAdapter = new PointRecordAdapter(getActivity(), pointRecordList, 1);
        mPointList.setAdapter(pointRecordAdapter);

        /****
         * 上拉下拉双向监听事件
         */
        mPointList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            /***
             * 下拉
             * @param refreshView
             */
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                onRequest(QUERY_EXERCISE_REFERSH);
            }

            /****
             * 上拉
             * @param refreshView
             */
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                onRequest(QUERY_EXERCISE_MORE);
            }
        });


    }


    private void onRequest(int type) {


        if (type == QUERY_EXERCISE_REFERSH) {
            if (params != null) {
                params.clear();
            }
            isRefresh = 1;
            begin = 0;
            params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
            params.put("pointtype", 0 + "");
            params.put("begin", begin + "");
            params.put("pageLength", pageLength + "");

        } else {
            if (params != null) {
                params.clear();
            }
            begin += 10;

            isRefresh = 2;
            params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
            params.put("pointtype", 0 + "");
            params.put("begin", begin + "");
            params.put("pageLength", pageLength + "");
        }

        Wethod.httpPost(getActivity(), 1, Config.web.point_record_list, params, this);


    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 1) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                pointRecordListData = objectMapper.readValue(result.toString(), PointRecordListData.class);
//                pointRecordList = pointRecordListData.getResultData();
                if (pointRecordListData.getResultData().size() == 0 && pointRecordList.size() == 0) {
                    iv_default_point_record_pic.setVisibility(View.VISIBLE);
                    mPointList.onRefreshComplete();
                } else if (pointRecordListData.getResultData().size() > 0) {
                    iv_default_point_record_pic.setVisibility(View.GONE);
                    if (isRefresh == 1) {
                        if (pointRecordAdapter != null) {
                            Log.e("liyunteee", "!null----------------");
                            pointRecordList.clear();
                        }
                    }
                    //  resultList = bean.getResultData();
                    for (int i = 0; i < pointRecordListData.getResultData().size(); i++) {

                        Log.e("liyunteee", "for--------------------");
                        PointCommonBean orderBean = new PointCommonBean();
                        orderBean.setCreatetime(pointRecordListData.getResultData().get(i).getCreatetime());
                        orderBean.setPoint(pointRecordListData.getResultData().get(i).getPoint());
                        orderBean.setPointtype(pointRecordListData.getResultData().get(i).getPointtype());
                        pointRecordList.add(orderBean);
                    }


                    if (isRefresh == 1) {
                        if (pointRecordAdapter != null) {
                            pointRecordAdapter.notifyDataSetChanged();
                            mPointList.onRefreshComplete();
                        } else {
                            Log.e("liyunteee", "adapter-----------------");
                            pointRecordAdapter = new PointRecordAdapter(getActivity(), pointRecordList, 1);
                            pointRecordAdapter.notifyDataSetChanged();
                            mPointList.onRefreshComplete();
                        }
                    } else if (isRefresh == 2) {
                        if (pointRecordAdapter != null) {
                            pointRecordAdapter.notifyDataSetChanged();
                            mPointList.onRefreshComplete();
                        } else {
                            pointRecordAdapter = new PointRecordAdapter(getActivity(), pointRecordList, 1);
                            pointRecordAdapter.notifyDataSetChanged();
                            mPointList.onRefreshComplete();
                        }
                    } else {
//                        pointRecordAdapter.add(pointRecordList);
                        pointRecordAdapter.notifyDataSetChanged();
                        mPointList.onRefreshComplete();
                    }
                } else {
                    mPointList.onRefreshComplete();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if (reqcode == 1) {
            Wethod.ToFailMsg(getActivity(), result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        mPointList.onRefreshComplete();
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengCountContext.onPageStart("PointRecordFra");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengCountContext.onPageEnd("PointRecordFra");
    }
}
