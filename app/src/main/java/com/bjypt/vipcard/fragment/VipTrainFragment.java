package com.bjypt.vipcard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.NewSubscribeDishesActivity;
import com.bjypt.vipcard.adapter.MerchantItemAdapter;
import com.bjypt.vipcard.adapter.MyExpandListViewAdapter;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.ContentBean;
import com.bjypt.vipcard.model.LifeHuiListBean;
import com.bjypt.vipcard.model.MerchantListBean;
import com.bjypt.vipcard.model.NewHomeMerchantListBean;
import com.bjypt.vipcard.model.NewMerchantListBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshExpandableListView;
import com.bjypt.vipcard.utils.ObjectMapperFactory;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 崔龙 on 2017/3/20.
 */

public class VipTrainFragment extends BaseFrament implements View.OnClickListener, VolleyCallBack<String> {
    private View view;
    private PullToRefreshExpandableListView listView;
    private ExpandableListView expandableListView;
    private NewHomeMerchantListBean homeMerchantListBean;
    private MerchantItemAdapter mAdapter;
    private List<NewMerchantListBean>  listmerchant = new ArrayList<>();
    private Map<String, String> maps = new HashMap<>();
    private int currentPage;
    private int pageLength = 10;
    private int isRefresh;
    final int QUERY_EXERCISE_MORE = 0x0101;
    final int QUERY_EXERCISE_REFERSH = 0x0110;
    private List<String> pkmuserList = new ArrayList<>();//商户主键
    private List<String> distanceList = new ArrayList<>();//距离
    private ImageView iv_default_vip_train_pic;

    Handler handler_content = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 110) {
                String value = (String) msg.obj;
                int sum = Integer.parseInt(value.substring(0, value.indexOf("a")));
                if (sum % 2 == 0) {
                    expandableListView.collapseGroup(Integer.parseInt(value.substring(value.indexOf("a") + 1)));
                } else {
                    expandableListView.expandGroup(Integer.parseInt(value.substring(value.indexOf("a") + 1)));
                }
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vip_train, container, false);
        return view;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        iv_default_vip_train_pic = (ImageView) view.findViewById(R.id.iv_default_vip_train_pic);
        listView = (PullToRefreshExpandableListView) view.findViewById(R.id.listview_vip_train);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        expandableListView = listView.getRefreshableView();
        expandableListView.setGroupIndicator(null);
        expandableListView.setDividerHeight(0);
    }

    @Override
    public void afterInitView() {
        onRequest(QUERY_EXERCISE_REFERSH);
    }

    private void onRequest(int type) {
        if (type == QUERY_EXERCISE_REFERSH) {
            if (maps != null) {
                maps.clear();
            }
            isRefresh = 1;
            currentPage = 0;
            maps.put("userId", getFromSharePreference(Config.userConfig.pkregister));
            maps.put("lng", getFromSharePreference(Config.userConfig.lngu));
            maps.put("lat", getFromSharePreference(Config.userConfig.latu));
            maps.put("begin", 0 + "");
            maps.put("pageLength", pageLength + "");
            maps.put("versionCode", "v4.4.2");

        } else {
            if (maps != null) {
                maps.clear();
            }
            currentPage += 10;
            Log.e("woyaokk", "currentPage：" + currentPage);

            isRefresh = 2;
            maps.put("userId", getFromSharePreference(Config.userConfig.pkregister));
            maps.put("lng", getFromSharePreference(Config.userConfig.lngu));
            maps.put("lat", getFromSharePreference(Config.userConfig.latu));
            maps.put("begin", currentPage + "");
            maps.put("pageLength", pageLength + "");
            maps.put("versionCode", "v4.4.2");
        }

        Wethod.httpPost(getActivity(), 2, Config.web.vip_train, maps, this);
    }

    @Override
    public void bindListener() {
        mAdapter = new MerchantItemAdapter(listmerchant, getActivity(), handler_content);
        expandableListView.setAdapter(mAdapter);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ExpandableListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                onRequest(QUERY_EXERCISE_REFERSH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                onRequest(QUERY_EXERCISE_MORE);
            }
        });
    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void onSuccess(int reqcode, String result) {
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            homeMerchantListBean = objectMapper.readValue(result.toString(), NewHomeMerchantListBean.class);
            if (homeMerchantListBean.getResultData().size() > 0) {
                iv_default_vip_train_pic.setVisibility(View.GONE);
                if (isRefresh == 1) {
                    if (mAdapter != null) {
                        listmerchant.clear();
                        distanceList.clear();
                        pkmuserList.clear();
                    }
                }

                for (int i = 0; i < homeMerchantListBean.getResultData().size(); i++) {
                    distanceList.add(homeMerchantListBean.getResultData().get(i).getDistance());
                    pkmuserList.add(homeMerchantListBean.getResultData().get(i).getPkmuser());
                    Log.e("eee", "" + homeMerchantListBean.getResultData().get(i).getMuname());
                    NewMerchantListBean listBean = new NewMerchantListBean();
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
//                    listBean.setContentBeans(list_content);
                    listmerchant.add(listBean);
                }


                if (isRefresh == 1) {
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    } else {
                        mAdapter = new MerchantItemAdapter(listmerchant, getActivity() , handler_content);
                        mAdapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    }

                } else if (isRefresh == 2) {
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    } else {
                        mAdapter = new MerchantItemAdapter(listmerchant, getActivity(), handler_content);
                        mAdapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    }
                } else {
                    mAdapter.add(listmerchant);
                    mAdapter.notifyDataSetChanged();
                    listView.onRefreshComplete();
                }
            } else if (homeMerchantListBean.getResultData().size() == 0 && isRefresh != 2) {
                Toast.makeText(getActivity(), "您暂未加入任何商家,请加入商家", Toast.LENGTH_LONG).show();
                Log.e("GGGG", "NULL-----------------------");
                listmerchant.clear();
                iv_default_vip_train_pic.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
                listView.onRefreshComplete();
            } else if (homeMerchantListBean.getResultData().size() == 0 && isRefresh == 2) {
                listView.onRefreshComplete();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Intent intent = new Intent(getActivity(), NewSubscribeDishesActivity.class);
                intent.putExtra("distance", distanceList.get(groupPosition));
                intent.putExtra("pkmuser", pkmuserList.get(groupPosition));
                Log.e("vvv", "pkmuser" + distanceList.get(groupPosition));
                startActivity(intent);

                return true;
            }
        });
    }

    @Override
    public void onFailed(int reqcode, String result) {

    }

    @Override
    public void onError(VolleyError volleyError) {
        listView.onRefreshComplete();
    }

    @Override
    public void isConntectedAndRefreshData() {
        onRequest(QUERY_EXERCISE_REFERSH);
    }
}
