package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.MyExpandListViewAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.ContentBean;
import com.bjypt.vipcard.model.LifeHuiListBean;
import com.bjypt.vipcard.model.MerchantListBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshExpandableListView;
import com.bjypt.vipcard.utils.ObjectMapperFactory;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VipTrainActivity extends BaseActivity implements VolleyCallBack {
    private PullToRefreshExpandableListView listView;
    private ExpandableListView expandableListView;
    private RelativeLayout rl_back_vip_train;
    private LifeHuiListBean homeMerchantListBean;
    private MyExpandListViewAdapter mAdapter;
    private List<MerchantListBean> listmerchant = new ArrayList<MerchantListBean>();
    private Map<String, String> maps = new HashMap<>();
    private int currentPage;
    private int pageLength = 10;
    private int isRefresh;
    final int QUERY_EXERCISE_MORE = 0x0101;
    final int QUERY_EXERCISE_REFERSH = 0x0110;
    private List<String> pkmuserList = new ArrayList<>();//商户主键
    private List<String> distanceList = new ArrayList<>();//距离
    private ImageView iv_default_vip_train_pic;


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_vip_train);

    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        rl_back_vip_train = (RelativeLayout) findViewById(R.id.rl_back_vip_train);
        iv_default_vip_train_pic = (ImageView) findViewById(R.id.iv_default_vip_train_pic);
        listView = (PullToRefreshExpandableListView) findViewById(R.id.listview_vip_train);
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
        }

        Wethod.httpPost(VipTrainActivity.this, 2, Config.web.vip_train, maps, this);


    }

    @Override
    public void bindListener() {
        mAdapter = new MyExpandListViewAdapter(this, listmerchant, handler_content);
        expandableListView.setAdapter(mAdapter);
        rl_back_vip_train.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.rl_back_vip_train:
                finish();
                break;
        }

    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            homeMerchantListBean = objectMapper.readValue(result.toString(), LifeHuiListBean.class);
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
                    MerchantListBean listBean = new MerchantListBean();
                    listBean.setIsfirst(homeMerchantListBean.getResultData().get(i).getIsfirst());
                    listBean.setSpecialPrice(homeMerchantListBean.getResultData().get(i).getSpecialPrice());
                    listBean.setIsfirst(homeMerchantListBean.getResultData().get(i).getIsfirst());
                    listBean.setDiscount(homeMerchantListBean.getResultData().get(i).getDiscount());
                    listBean.setDistance(homeMerchantListBean.getResultData().get(i).getDistance());
                    listBean.setJudgeAllNum(homeMerchantListBean.getResultData().get(i).getJudgeAllNum());
                    listBean.setLogo(homeMerchantListBean.getResultData().get(i).getLogo());
                    listBean.setMerdesc(homeMerchantListBean.getResultData().get(i).getMerdesc());
                    listBean.setMuname(homeMerchantListBean.getResultData().get(i).getMuname());
                    listBean.setPkmuser(homeMerchantListBean.getResultData().get(i).getPkmuser());
                    listBean.setStartLevel(homeMerchantListBean.getResultData().get(i).getStartLevel());
                    listBean.setCouponWelfare(homeMerchantListBean.getResultData().get(i).getCouponWelfare());
                    listBean.setMemberCount(homeMerchantListBean.getResultData().get(i).getMemberCount());

                    int sum = 7;
                    List<ContentBean> list_content = new ArrayList<>();
                    if ("".equals(homeMerchantListBean.getResultData().get(i).getConsumeRedPackage()) ||
                            null == homeMerchantListBean.getResultData().get(i).getConsumeRedPackage()) {
                        sum--;
                        if ("".equals(homeMerchantListBean.getResultData().get(i).getMaxDiscount()) ||
                                null == homeMerchantListBean.getResultData().get(i).getMaxDiscount()) {
                            if ("".equals(homeMerchantListBean.getResultData().get(i).getRechargeRedPackage())
                                    ||
                                    null == homeMerchantListBean.getResultData().get(i).getRechargeRedPackage()) {
                                if ("".equals(homeMerchantListBean.getResultData().get(i).getRechargeWelfare())
                                        ||
                                        null == homeMerchantListBean.getResultData().get(i).getRechargeWelfare()) {
                                    if ("".equals(homeMerchantListBean.getResultData().get(i).getRegistRedPackage())
                                            ||
                                            null == homeMerchantListBean.getResultData().get(i).getRegistRedPackage()) {
                                        if ("".equals(homeMerchantListBean.getResultData().get(i).getHybcoin())
                                                ||
                                                null == homeMerchantListBean.getResultData().get(i).getHybcoin()) {
                                            if ("".equals(homeMerchantListBean.getResultData().get(i).getIntegral())
                                                    ||
                                                    null == homeMerchantListBean.getResultData().get(i).getIntegral()) {
                                                listBean.setConsumeRedPackage("");
                                            } else {
                                                listBean.setFlag(5);
                                                listBean.setConsumeRedPackage(homeMerchantListBean.getResultData().get(i).getIntegral());
                                            }

                                        } else {
                                            listBean.setFlag(4);
                                            listBean.setConsumeRedPackage(homeMerchantListBean.getResultData().get(i).getHybcoin());
                                        }

                                    } else {
                                        listBean.setFlag(3);
                                        listBean.setConsumeRedPackage(homeMerchantListBean.getResultData().get(i).getRegistRedPackage());
                                    }
                                } else {
                                    listBean.setFlag(2);
                                    listBean.setConsumeRedPackage(homeMerchantListBean.getResultData().get(i).getRechargeWelfare());
                                }
                            } else {
                                listBean.setFlag(1);
                                listBean.setConsumeRedPackage(homeMerchantListBean.getResultData().get(i).getRechargeRedPackage());
                            }
                        } else {
                            listBean.setFlag(0);
                            listBean.setConsumeRedPackage(homeMerchantListBean.getResultData().get(i).getMaxDiscount());
                        }
                    } else {
                        listBean.setFlag(11);
                        listBean.setConsumeRedPackage(homeMerchantListBean.getResultData().get(i).getConsumeRedPackage());
                    }
                    if ("".equals(homeMerchantListBean.getResultData().get(i).getMaxDiscount()) ||
                            null == homeMerchantListBean.getResultData().get(i).getMaxDiscount()) {
                        sum--;
                    }
                    if ("".equals(homeMerchantListBean.getResultData().get(i).getRechargeRedPackage()) ||
                            null == homeMerchantListBean.getResultData().get(i).getRechargeRedPackage()) {
                        sum--;
                    }
                    if ("".equals(homeMerchantListBean.getResultData().get(i).getRechargeWelfare()) ||
                            null == homeMerchantListBean.getResultData().get(i).getRechargeWelfare()) {
                        sum--;
                    }
                    if ("".equals(homeMerchantListBean.getResultData().get(i).getRegistRedPackage()) ||
                            null == homeMerchantListBean.getResultData().get(i).getRegistRedPackage()) {
                        sum--;
                    }
                    if ("".equals(homeMerchantListBean.getResultData().get(i).getIntegral()) ||
                            null == homeMerchantListBean.getResultData().get(i).getIntegral()) {
                        sum--;
                    }
                    if ("".equals(homeMerchantListBean.getResultData().get(i).getHybcoin()) ||
                            null == homeMerchantListBean.getResultData().get(i).getHybcoin()) {
                        sum--;
                    }
                    listBean.setActivitysSum(sum);

                    list_content.add(new ContentBean(homeMerchantListBean.getResultData().get(i).getMaxDiscount()));
                    list_content.add(new ContentBean(homeMerchantListBean.getResultData().get(i).getRechargeRedPackage()));
                    list_content.add(new ContentBean(homeMerchantListBean.getResultData().get(i).getRechargeWelfare()));
                    list_content.add(new ContentBean(homeMerchantListBean.getResultData().get(i).getRegistRedPackage()));
                    list_content.add(new ContentBean(homeMerchantListBean.getResultData().get(i).getHybcoin()));
                    list_content.add(new ContentBean(homeMerchantListBean.getResultData().get(i).getIntegral()));
                    listBean.setContentBeans(list_content);
                    listmerchant.add(listBean);
                }


                if (isRefresh == 1) {
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    } else {
                        mAdapter = new MyExpandListViewAdapter(this, listmerchant, handler_content);
                        mAdapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    }

                } else if (isRefresh == 2) {
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    } else {
                        mAdapter = new MyExpandListViewAdapter(this, listmerchant, handler_content);
                        mAdapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    }
                } else {
                    mAdapter.add(listmerchant);
                    mAdapter.notifyDataSetChanged();
                    listView.onRefreshComplete();
                }
            } else if (homeMerchantListBean.getResultData().size() == 0 && isRefresh != 2) {
                Toast.makeText(this, "您暂未加入任何商家,请加入商家", Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(VipTrainActivity.this, NewSubscribeDishesActivity.class);
                intent.putExtra("distance", distanceList.get(groupPosition));
                intent.putExtra("pkmuser", pkmuserList.get(groupPosition));
                Log.e("vvv", "pkmuser" + distanceList.get(groupPosition));
                startActivity(intent);

                return true;
            }
        });
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {
        listView.onRefreshComplete();
    }

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

    @Override
    public void isConntectedAndRefreshData() {
        onRequest(QUERY_EXERCISE_REFERSH);
    }
}
