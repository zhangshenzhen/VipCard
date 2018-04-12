package com.bjypt.vipcard.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.IssueCommentActivity;
import com.bjypt.vipcard.activity.MyOrderActivity;
import com.bjypt.vipcard.activity.PayOneActivity;
import com.bjypt.vipcard.adapter.UnPayOrderAdapter;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.AllOrderRootBean;
import com.bjypt.vipcard.model.UnPayOrderBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.utils.ToastUtils;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/1 0001.
 * 待使用订单
 */

public class WaitUseFra extends Fragment implements VolleyCallBack{
    private View view;
    private String userId;
    private Map<String, String> maps;
    private List<UnPayOrderBean> list;
    private Map<String, String> map;
    private PullToRefreshListView pullList;
    private UnPayOrderAdapter mAdapter;
//    private boolean isShow = false;
    private String delurl = Config.web.del_order;

    /* userId:用户主键
    begin：位移(分页)
    pageLength：长度(分页)
    status: (4:已失效  21:待使用 ； 22:待预约 23已消费 无此参数时查询所有订单)"
*/
    private String url = Config.web.find_all_order;
    private int pageLength = 10;
    private int isRefresh = 1;// 1刷新 2.加载更多
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;
    private int begin = 0;
    private int flag; // 1.全部订单 2.待预约订单，3待使用订单 4 已消费
    private updateUIUseReceiver mReceiver;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fra_my_oirder, container, false);
        initView();
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction("update_ui");
            mReceiver = new updateUIUseReceiver();
            getActivity().registerReceiver(mReceiver,filter);//注册广播接受者
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    class  updateUIUseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int update_flag = intent.getIntExtra("UPDATE_FLAG",0);
            Log.e("kiss","use_update_flag:"+update_flag);
            /*if(update_flag == 1){
                isShow = true;
            }else if(update_flag == 2){
                isShow = false;
            }*/
            refreshAdapter();
        }
    }



    /**
     * 实现类，响应按钮点击事件
     */
    private UnPayOrderAdapter.MyClickListener mListener = new UnPayOrderAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            switch (v.getId()) {
                case R.id.btn_affirmOrder:
                    //代预约
                    if (list.get(position).getStatus() == 23) {
                        Intent intent = new Intent(getActivity(), IssueCommentActivity.class);
                        intent.putExtra("preorderId", list.get(position).getPreorderId());
                        intent.putExtra("pkmuser", list.get(position).getPkmuser());
                        startActivity(intent);
                    } else if (list.get(position).getStatus() == 22) {
                        Intent intent_queren = new Intent(getActivity(), PayOneActivity.class);
                        intent_queren.putExtra("orderId", list.get(position).getOrderNo());
                        intent_queren.putExtra("pkmuser", list.get(position).getPkmuser());
                        intent_queren.putExtra("preorderId", list.get(position).getPreorderId());

                        intent_queren.putExtra("primaryk",list.get(position).getPksubscbptn());
                        intent_queren.putExtra("FLAG", "Y");
                        startActivity(intent_queren);
                    } else if (list.get(position).getStatus() == 21) {
                        Intent intent_queren = new Intent(getActivity(), PayOneActivity.class);
                        intent_queren.putExtra("orderId", list.get(position).getOrderNo());
                        intent_queren.putExtra("pkmuser", list.get(position).getPkmuser());
                        intent_queren.putExtra("preorderId", list.get(position).getPreorderId());
                        intent_queren.putExtra("FLAG", "N");
                        startActivity(intent_queren);
                    }
                  /*  Toast.makeText(
                            UnPayOrderActivity.this,
                            "预约订单被点击了" + position, Toast.LENGTH_SHORT)
                            .show();*/
                    break;
                case R.id.btn_unpay_order_del:
                    //删除订单

                    dialogShow(position);
                    //refreshAdapter();
                    break;
            }
        }
    };
    protected void dialogShow(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确认删除此订单？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (list.get(position).getStatus() != 21) {
                    delRequest(list.get(position).getPreorderId());
                    list.remove(position);
                    mAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showMessage(getActivity(), "不可删除");
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }
    private void initView(){
        pullList = (PullToRefreshListView) view.findViewById(R.id.order_unpay_order);
        map = new HashMap<>();
        maps = new HashMap<>();
        list = new ArrayList<UnPayOrderBean>();
        userId = SharedPreferenceUtils.getFromSharedPreference(getActivity(),Config.userConfig.pkregister);
        Log.e("kiss","pullList:"+pullList);

        pullList.setMode(PullToRefreshBase.Mode.BOTH);

        /****
         * 上拉下拉双向监听事件
         */
        pullList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            /***
             * 下拉
             * @param refreshView
             */
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                setRequest(QUERY_REFERSH, flag);
            }

            /****
             * 上拉
             * @param refreshView
             */
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                setRequest(QUERY_MORE, flag);

            }
        });

    }


    public void delRequest(String preorderId) {
        if (maps != null) {
            maps.clear();
        }
        maps.put("userId", userId);
        maps.put("preorderId", preorderId);
        Wethod.httpPost(getActivity(),45, delurl, maps, this);
    }

    public void setRequest(int type, int falg) {
        if (type == QUERY_REFERSH) {
            if (map != null) {
                map.clear();
            }
                map.put("status", "21");
            begin = 0;
            isRefresh = 1;
            map.put("userId", userId);
            map.put("begin", begin + "");
            map.put("pageLength", pageLength + "");
            Wethod.httpPost(getActivity(),44, url, map, this);

        } else {
            if (map != null) {
                map.clear();
            }
                map.put("status", "21");
            isRefresh = 2;
            begin += 10;
            map.put("userId", userId);
            map.put("begin", begin + "");
            map.put("pageLength", pageLength + "");
            Wethod.httpPost(getActivity(),44, url, map, this);
        }
        afterInitView();
    }

    public void afterInitView() {

        //添加适配
        refreshAdapter();
    }




    public void refreshAdapter() {
        mAdapter = new UnPayOrderAdapter(list, getActivity(), MyOrderActivity.isShow, mListener);
        pullList.setAdapter(mAdapter);
    }


    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 44) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                AllOrderRootBean bean = objectMapper.readValue(result.toString(), AllOrderRootBean.class);
                if (bean.getResultData().size()==0&&list.size()==0){

                }else if (bean.getResultData().size() > 0) {
                    if (isRefresh == 1) {
                        if (mAdapter != null) {
                            Log.e("liyunteee", "!null----------------");
                            list.clear();
                        }
                    }
                    //  resultList = bean.getResultData();
                    for (int i = 0; i < bean.getResultData().size(); i++) {

                        Log.e("liyunteee", "for--------------------");
                        UnPayOrderBean orderBean = new UnPayOrderBean();
                        orderBean.setUserRemark(bean.getResultData().get(i).getUserRemark());
                        orderBean.setGoodsImg(bean.getResultData().get(i).getLogo());
                        orderBean.setGoodsName(bean.getResultData().get(i).getMuname());
                        orderBean.setGoodsNum(bean.getResultData().get(i).getOrderCount());
                        orderBean.setPreorderId(bean.getResultData().get(i).getPreorderId());
                        orderBean.setGoodsPrice(bean.getResultData().get(i).getTotalPrice());
                        orderBean.setStatus(bean.getResultData().get(i).getStatus());
                        orderBean.setPkmuser(bean.getResultData().get(i).getPkmuser());
                        orderBean.setOrderNo(bean.getResultData().get(i).getOrderNo());
                        orderBean.setTime(bean.getResultData().get(i).getDisableTime());
                        orderBean.setCreatetime(bean.getResultData().get(i).getCreatetime());
                        orderBean.setPksubscbptn(bean.getResultData().get(i).getPksubscbptn());
                        orderBean.setEarnestmoney(bean.getResultData().get(i).getEarnestmoney());
                        list.add(orderBean);
                    }


                    if (isRefresh == 1) {
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                            pullList.onRefreshComplete();
                        } else {
                            Log.e("liyunteee", "adapter-----------------");
                            mAdapter = new UnPayOrderAdapter(list, getActivity(), MyOrderActivity.isShow, mListener);
                            mAdapter.notifyDataSetChanged();
                            pullList.onRefreshComplete();
                        }
                    } else if (isRefresh == 2) {
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                            pullList.onRefreshComplete();
                        } else {
                            mAdapter = new UnPayOrderAdapter(list, getActivity(), MyOrderActivity.isShow, mListener);
                            mAdapter.notifyDataSetChanged();
                            pullList.onRefreshComplete();
                        }
                    } else {
                        mAdapter.add(list);
                        mAdapter.notifyDataSetChanged();
                        pullList.onRefreshComplete();
                    }
                } else {
                    pullList.onRefreshComplete();
                }
            } catch (IOException e) {
                Log.e("liyunte", "eee" + e);
                e.printStackTrace();
            }
            pullList.onRefreshComplete();
        }
        if (reqcode == 45) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {
        pullList.onRefreshComplete();
    }

    @Override
    public void onResume() {
        super.onResume();
        userId = SharedPreferenceUtils.getFromSharedPreference(getActivity(),Config.userConfig.pkregister);
        setRequest(QUERY_REFERSH, flag);
    }
}
