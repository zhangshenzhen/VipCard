package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.LifeHuiAdapter;
import com.bjypt.vipcard.adapter.MerchantItemListViewAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.HomeMerchantListBean;
import com.bjypt.vipcard.model.LifeHuiListBean;
import com.bjypt.vipcard.model.LifeHuiTypeData;
import com.bjypt.vipcard.model.MerchantListBean;
import com.bjypt.vipcard.model.NewHomeMerchantListBean;
import com.bjypt.vipcard.model.NewMerchantListBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class LifeHuiPersonActivity extends BaseActivity implements VolleyCallBack<String> {

    private PullToRefreshListView mpullList;
    private String pkmertype;
    private LinearLayout mBack;
    private NewHomeMerchantListBean homeMerchantListBean;
    private int isRefresh;
    final int QUERY_EXERCISE_MORE = 0x0101;
    final int QUERY_EXERCISE_REFERSH = 0x0110;
    private Map<String, String> maps = new HashMap<>();
    private int currentPage;
    private int pageLength = 10;
    private MerchantItemListViewAdapter mAdapter;
    private List<NewMerchantListBean> listmerchant = new ArrayList<NewMerchantListBean>();
    private List<String> pkmuserList = new ArrayList<>();//商户主键
    private List<String> distanceList = new ArrayList<>();//距离
    private ListView pullList;
    private ListView actualListView;
    private View mHeadView;
    ;
    private LifeHuiTypeData lifeHuiTypeData;


    private LinearLayout mTwoMerchantLinear;
    private TextView mNameOne;
    private TextView mNameTwo;
    private TextView mNameThree;
    private TextView mNameFour;
    private TextView mNameFive;
    private TextView mNameSix;
    private TextView mDescriOne;
    private TextView mDescriTwo;
    private TextView mDescriThree;
    private TextView mDescriFour;
    private TextView mDescriFive;
    private TextView mDescriSix;
    private ImageView mLogoOne;
    private ImageView mLogoTwo;
    private ImageView mLogoThree;
    private ImageView mLogoFour;
    private ImageView mLogoFive;
    private ImageView mLogoSix;
    private RelativeLayout mMerchantOne;
    private RelativeLayout mMerchantTwo;
    private RelativeLayout mMerchantThree;
    private RelativeLayout mMerchantFour;
    private RelativeLayout mMerchantFive;
    private RelativeLayout mMerchantSix;
    private RelativeLayout rl_search_merchant;
    private String lat;
    private String lng;
    private String mtlevel;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_life_hui);
        lat = SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.latu);
        lng = SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.lngu);
    }

    @Override
    public void beforeInitView() {

        Intent intent = getIntent();
        pkmertype = intent.getStringExtra("pkmertype");
        mtlevel = intent.getStringExtra("mtlevel");
        Map<String, String> params = new HashMap<>();
        params.put("cityCode", getFromSharePreference(Config.userConfig.citycode));
        params.put("pkmertype", pkmertype);
        params.put("versionCode", getVersion());
        params.put("lat", lat);
        params.put("lng", lng);
        Wethod.httpPost(this, 1111, Config.web.life_tuijian_merchant, params, this);
        onRequest(QUERY_EXERCISE_REFERSH);
    }

    @Override
    public void initView() {


        mBack = (LinearLayout) findViewById(R.id.layout_back_life_hui);
        rl_search_merchant = (RelativeLayout)findViewById(R.id.rl_search_merchant);

        mpullList = (PullToRefreshListView) findViewById(R.id.life_merchant_listvew);
        pullList = mpullList.getRefreshableView();
        pullList.setDividerHeight(0);


        mHeadView = LayoutInflater.from(this).inflate(R.layout.head_view_life_hui, null);
        pullList.addHeaderView(mHeadView);



        //第二行显示的商家
        mTwoMerchantLinear = (LinearLayout) mHeadView.findViewById(R.id.merchant_linear_two);

        mNameOne = (TextView) mHeadView.findViewById(R.id.life_merchant_name_one);
        mNameTwo = (TextView) mHeadView.findViewById(R.id.life_merchant_name_two);
        mNameThree = (TextView) mHeadView.findViewById(R.id.life_merchant_name_three);
        mNameFour = (TextView) mHeadView.findViewById(R.id.life_merchant_name_four);
        mNameFive = (TextView) mHeadView.findViewById(R.id.life_merchant_name_five);
        mNameSix = (TextView) mHeadView.findViewById(R.id.life_merchant_name_six);

        mDescriOne = (TextView) mHeadView.findViewById(R.id.life_merchant_descri_one);
        mDescriTwo = (TextView) mHeadView.findViewById(R.id.life_merchant_descri_two);
        mDescriThree = (TextView) mHeadView.findViewById(R.id.life_merchant_descri_three);
        mDescriFour = (TextView) mHeadView.findViewById(R.id.life_merchant_descri_four);
        mDescriFive = (TextView) mHeadView.findViewById(R.id.life_merchant_descri_five);
        mDescriSix = (TextView) mHeadView.findViewById(R.id.life_merchant_descri_six);

        mLogoOne = (ImageView) mHeadView.findViewById(R.id.life_merchant_logo_one);
        mLogoTwo = (ImageView) mHeadView.findViewById(R.id.life_merchant_logo_two);
        mLogoThree = (ImageView) mHeadView.findViewById(R.id.life_merchant_logo_three);
        mLogoFour = (ImageView) mHeadView.findViewById(R.id.life_merchant_logo_four);
        mLogoFive = (ImageView) mHeadView.findViewById(R.id.life_merchant_logo_five);
        mLogoSix = (ImageView) mHeadView.findViewById(R.id.life_merchant_logo_six);


        mMerchantOne = (RelativeLayout) mHeadView.findViewById(R.id.merchant_one);
        mMerchantTwo = (RelativeLayout) mHeadView.findViewById(R.id.merchant_two);
        mMerchantThree = (RelativeLayout) mHeadView.findViewById(R.id.merchant_three);
        mMerchantFour = (RelativeLayout) mHeadView.findViewById(R.id.merchant_four);
        mMerchantFive = (RelativeLayout) mHeadView.findViewById(R.id.merchant_five);
        mMerchantSix = (RelativeLayout) mHeadView.findViewById(R.id.merchant_six);


        mpullList.setMode(PullToRefreshBase.Mode.BOTH);

        mAdapter = new MerchantItemListViewAdapter(listmerchant,this);
        actualListView = mpullList.getRefreshableView();
        pullList.setAdapter(mAdapter);

        mpullList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                onRequest(QUERY_EXERCISE_REFERSH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                onRequest(QUERY_EXERCISE_MORE);
            }
        });


    }

    @Override
    public void afterInitView() {
//        TextView tv_title = (TextView) findViewById(R.id.tv_title);
//        tv_title.setText(getIntent().getStringExtra("title"));
    }

    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
        mMerchantOne.setOnClickListener(this);
        mMerchantTwo.setOnClickListener(this);
        mMerchantThree.setOnClickListener(this);
        mMerchantFour.setOnClickListener(this);
        mMerchantFive.setOnClickListener(this);
        mMerchantSix.setOnClickListener(this);
        rl_search_merchant.setOnClickListener(this);

    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout_back_life_hui:
                finish();
                break;
            case R.id.merchant_one:
                if (lifeHuiTypeData.getResultData().size() >= 1) {
                    Intent merIntent1 = new Intent(this, NewSubscribeDishesActivity.class);
                    merIntent1.putExtra("distance", lifeHuiTypeData.getResultData().get(0).getDistance());
                    merIntent1.putExtra("rechargeactivity", lifeHuiTypeData.getResultData().get(0).getRechargeActivity());
                    merIntent1.putExtra("pkmuser", lifeHuiTypeData.getResultData().get(0).getPkmuser());
                    startActivity(merIntent1);
                }else{
                    ToastUtil.showToast(this, "暂未推荐");
                }
                break;
            case R.id.merchant_two:
                if (lifeHuiTypeData.getResultData().size() >= 2) {
                    Intent merIntent2 = new Intent(this, NewSubscribeDishesActivity.class);
                    merIntent2.putExtra("distance", lifeHuiTypeData.getResultData().get(1).getDistance());
                    merIntent2.putExtra("rechargeactivity", lifeHuiTypeData.getResultData().get(1).getRechargeActivity());
                    merIntent2.putExtra("pkmuser", lifeHuiTypeData.getResultData().get(1).getPkmuser());
                    startActivity(merIntent2);
                }else{
                    ToastUtil.showToast(this, "暂未推荐");
                }
                break;
            case R.id.merchant_three:
                if (lifeHuiTypeData.getResultData().size() >= 3) {
                    Intent merIntent3 = new Intent(this, NewSubscribeDishesActivity.class);
                    merIntent3.putExtra("distance", lifeHuiTypeData.getResultData().get(2).getDistance());
                    merIntent3.putExtra("rechargeactivity", lifeHuiTypeData.getResultData().get(2).getRechargeActivity());
                    merIntent3.putExtra("pkmuser", lifeHuiTypeData.getResultData().get(2).getPkmuser());
                    startActivity(merIntent3);
                }else{
                    ToastUtil.showToast(this, "暂未推荐");
                }
                break;
            case R.id.merchant_four:
                if (lifeHuiTypeData.getResultData().size() >= 4) {
                    Intent merIntent4 = new Intent(this, NewSubscribeDishesActivity.class);
                    merIntent4.putExtra("distance", lifeHuiTypeData.getResultData().get(3).getDistance());
                    merIntent4.putExtra("rechargeactivity", lifeHuiTypeData.getResultData().get(3).getRechargeActivity());
                    merIntent4.putExtra("pkmuser", lifeHuiTypeData.getResultData().get(3).getPkmuser());
                    startActivity(merIntent4);
                }else{
                    ToastUtil.showToast(this, "暂未推荐");
                }
                break;
            case R.id.merchant_five:
                if (lifeHuiTypeData.getResultData().size() >= 5) {
                    Intent merIntent5 = new Intent(this, NewSubscribeDishesActivity.class);
                    merIntent5.putExtra("distance", lifeHuiTypeData.getResultData().get(4).getDistance());
                    merIntent5.putExtra("rechargeactivity", lifeHuiTypeData.getResultData().get(4).getRechargeActivity());
                    merIntent5.putExtra("pkmuser", lifeHuiTypeData.getResultData().get(4).getPkmuser());
                    startActivity(merIntent5);
                }else{
                    ToastUtil.showToast(this, "暂未推荐");
                }
                break;
            case R.id.merchant_six:
                if (lifeHuiTypeData.getResultData().size() >= 6) {
                    Intent merIntent6 = new Intent(this, NewSubscribeDishesActivity.class);
                    merIntent6.putExtra("distance", lifeHuiTypeData.getResultData().get(5).getDistance());
                    merIntent6.putExtra("rechargeactivity", lifeHuiTypeData.getResultData().get(5).getRechargeActivity());
                    merIntent6.putExtra("pkmuser", lifeHuiTypeData.getResultData().get(5).getPkmuser());
                    startActivity(merIntent6);
                }else{
                    ToastUtil.showToast(this, "暂未推荐");
                }
                break;
            case R.id.rl_search_merchant:
                Intent intent  = new Intent(LifeHuiPersonActivity.this, LifeHuiPersonSearchListActivity.class);
                intent.putExtra("pkmertype", pkmertype);
                startActivity(intent);
                break;
        }
    }

    private void onRequest(int type) {


        if (type == QUERY_EXERCISE_REFERSH) {
            if (maps != null) {
                maps.clear();
            }
            isRefresh = 1;
            currentPage = 0;

//            maps.put("range", "100000");
            maps.put("pkmertype", pkmertype);
            maps.put("mtlevel", mtlevel);
            maps.put("iseight", "1");

//            maps.put("userId", getFromSharePreference(Config.userConfig.pkregister));
            maps.put("cityCode", getFromSharePreference(Config.userConfig.citycode));
            maps.put("lng", getFromSharePreference(Config.userConfig.lngu));
            maps.put("lat", getFromSharePreference(Config.userConfig.latu));
            maps.put("begin", 0 + "");
            maps.put("versionCode", getVersion());
            maps.put("pageLength", pageLength + "");

        } else {
            if (maps != null) {
                maps.clear();
            }
            currentPage += 10;

            maps.put("range", "100000");
            maps.put("pkmertype", pkmertype);
            maps.put("mtlevel", mtlevel);
            maps.put("iseight", "1");


            isRefresh = 2;
//            maps.put("userId", getFromSharePreference(Config.userConfig.pkregister));
            maps.put("cityCode", getFromSharePreference(Config.userConfig.citycode));
            maps.put("lng", getFromSharePreference(Config.userConfig.lngu));
            maps.put("lat", getFromSharePreference(Config.userConfig.latu));
            maps.put("begin", currentPage + "");
            maps.put("versionCode", getVersion());
            maps.put("pageLength", pageLength + "");
        }

        Wethod.httpPost(this, 2222, Config.web.home_life_hui_merchant_list, maps, this);


    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == 2222) {
//            ObjectMapper objectMapper = new ObjectMapper();
            try {
                homeMerchantListBean = getConfiguration().readValue(result.toString(), NewHomeMerchantListBean.class);
                if (homeMerchantListBean.getResultData().size() > 0) {
                    // iv_default_pic_home.setVisibility(View.GONE);
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
                        listmerchant.add(listBean);
                    }

                    if (isRefresh == 1) {
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                            mpullList.onRefreshComplete();
                        } else {
                            mAdapter = new MerchantItemListViewAdapter(listmerchant, this);
                            mAdapter.notifyDataSetChanged();
                            mpullList.onRefreshComplete();
                        }

                    } else if (isRefresh == 2) {
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                            mpullList.onRefreshComplete();
                        } else {
                            mAdapter = new MerchantItemListViewAdapter(listmerchant, this);
                            mAdapter.notifyDataSetChanged();
                            mpullList.onRefreshComplete();
                        }
                    } else {
                        if (mAdapter != null) {
                            Log.e("lifetest", "1");
//                            mAdapter.add(listmerchant);
                            mAdapter.notifyDataSetChanged();
                            mpullList.onRefreshComplete();
                        } else {
                            Log.e("lifetest", "2");
                            mAdapter = new MerchantItemListViewAdapter(listmerchant,this);
                            mAdapter.notifyDataSetChanged();
                            mpullList.onRefreshComplete();
                        }

                    }
                } else if (homeMerchantListBean.getResultData().size() == 0 && isRefresh != 2) {
                    Log.e("GGGG", "NULL-----------------------");
                    listmerchant.clear();
                    // iv_default_pic_home.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                    mpullList.onRefreshComplete();
                } else if (homeMerchantListBean.getResultData().size() == 0 && isRefresh == 2) {
                    mpullList.onRefreshComplete();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            pullList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(LifeHuiPersonActivity.this, NewSubscribeDishesActivity.class);
                    position = position - 2;
                    if(position <0)position =0;
                    intent.putExtra("distance", distanceList.get(position));
                    intent.putExtra("rechargeactivity", listmerchant.get(position).getRechargeActivity());

                    intent.putExtra("pkmuser", pkmuserList.get(position));
                    Log.e("GGGG", "pkmuser" + pkmuserList.get(position));
                    startActivity(intent);
                }
            });
//            pullList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int section, int position, long id) {
//                    Intent intent = new Intent(LifeHuiPersonActivity.this, SubscribeDishesActivity.class);
//                    intent.putExtra("distance", distanceList.get(position));
//                    intent.putExtra("rechargeactivity", listmerchant.get(position).getRechargeActivity());
//
//                    intent.putExtra("pkmuser", pkmuserList.get(position));
//                    Log.e("GGGG", "pkmuser" + pkmuserList.get(position));
//                    startActivity(intent);
//                }
//
//                @Override
//                public void onSectionClick(AdapterView<?> adapterView, View view, int section, long id) {
//
//                }
//            });


        } else if (reqcode == 1111) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                lifeHuiTypeData = objectMapper.readValue(result.toString(), LifeHuiTypeData.class);
                if (lifeHuiTypeData.getResultData().size() > 3) {
                    mTwoMerchantLinear.setVisibility(View.VISIBLE);
                } else {
                    mTwoMerchantLinear.setVisibility(View.GONE);
                }
                setViewData(lifeHuiTypeData.getResultData().size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    private void setViewData(int size) {
        if (size == 1) {
            mNameOne.setText(lifeHuiTypeData.getResultData().get(0).getMuname());
            mDescriOne.setText(lifeHuiTypeData.getResultData().get(0).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(0).getLogo(), mLogoOne, AppConfig.DEFAULT_IMG_MERCHANT_BG);
        } else if (size == 2) {
            mNameOne.setText(lifeHuiTypeData.getResultData().get(0).getMuname());
            mDescriOne.setText(lifeHuiTypeData.getResultData().get(0).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(0).getLogo(), mLogoOne, AppConfig.DEFAULT_IMG_MERCHANT_BG);

            mNameTwo.setText(lifeHuiTypeData.getResultData().get(1).getMuname());
            mDescriTwo.setText(lifeHuiTypeData.getResultData().get(1).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(1).getLogo(), mLogoTwo, AppConfig.DEFAULT_IMG_MERCHANT_BG);
        } else if (size == 3) {
            mNameOne.setText(lifeHuiTypeData.getResultData().get(0).getMuname());
            mDescriOne.setText(lifeHuiTypeData.getResultData().get(0).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(0).getLogo(), mLogoOne, AppConfig.DEFAULT_IMG_MERCHANT_BG);

            mNameTwo.setText(lifeHuiTypeData.getResultData().get(1).getMuname());
            mDescriTwo.setText(lifeHuiTypeData.getResultData().get(1).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(1).getLogo(), mLogoTwo, AppConfig.DEFAULT_IMG_MERCHANT_BG);

            mNameThree.setText(lifeHuiTypeData.getResultData().get(2).getMuname());
            mDescriThree.setText(lifeHuiTypeData.getResultData().get(2).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(2).getLogo(), mLogoThree, AppConfig.DEFAULT_IMG_MERCHANT_BG);

        } else if (size == 4) {
            mNameOne.setText(lifeHuiTypeData.getResultData().get(0).getMuname());
            mDescriOne.setText(lifeHuiTypeData.getResultData().get(0).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(0).getLogo(), mLogoOne, AppConfig.DEFAULT_IMG_MERCHANT_BG);

            mNameTwo.setText(lifeHuiTypeData.getResultData().get(1).getMuname());
            mDescriTwo.setText(lifeHuiTypeData.getResultData().get(1).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(1).getLogo(), mLogoTwo, AppConfig.DEFAULT_IMG_MERCHANT_BG);

            mNameThree.setText(lifeHuiTypeData.getResultData().get(2).getMuname());
            mDescriThree.setText(lifeHuiTypeData.getResultData().get(2).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(2).getLogo(), mLogoThree, AppConfig.DEFAULT_IMG_MERCHANT_BG);

            mNameFour.setText(lifeHuiTypeData.getResultData().get(3).getMuname());
            mDescriFour.setText(lifeHuiTypeData.getResultData().get(3).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(3).getLogo(), mLogoFour, AppConfig.DEFAULT_IMG_MERCHANT_BG);
        } else if (size == 5) {
            mNameOne.setText(lifeHuiTypeData.getResultData().get(0).getMuname());
            mDescriOne.setText(lifeHuiTypeData.getResultData().get(0).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(0).getLogo(), mLogoOne, AppConfig.DEFAULT_IMG_MERCHANT_BG);

            mNameTwo.setText(lifeHuiTypeData.getResultData().get(1).getMuname());
            mDescriTwo.setText(lifeHuiTypeData.getResultData().get(1).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(1).getLogo(), mLogoTwo, AppConfig.DEFAULT_IMG_MERCHANT_BG);

            mNameThree.setText(lifeHuiTypeData.getResultData().get(2).getMuname());
            mDescriThree.setText(lifeHuiTypeData.getResultData().get(2).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(2).getLogo(), mLogoThree, AppConfig.DEFAULT_IMG_MERCHANT_BG);

            mNameFour.setText(lifeHuiTypeData.getResultData().get(3).getMuname());
            mDescriFour.setText(lifeHuiTypeData.getResultData().get(3).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(3).getLogo(), mLogoFour, AppConfig.DEFAULT_IMG_MERCHANT_BG);

            mNameFive.setText(lifeHuiTypeData.getResultData().get(4).getMuname());
            mDescriFive.setText(lifeHuiTypeData.getResultData().get(4).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(4).getLogo(), mLogoFive, AppConfig.DEFAULT_IMG_MERCHANT_BG);

        } else if (size == 6) {
            mNameOne.setText(lifeHuiTypeData.getResultData().get(0).getMuname());
            mDescriOne.setText(lifeHuiTypeData.getResultData().get(0).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(0).getLogo(), mLogoOne, AppConfig.DEFAULT_IMG_MERCHANT_BG);

            mNameTwo.setText(lifeHuiTypeData.getResultData().get(1).getMuname());
            mDescriTwo.setText(lifeHuiTypeData.getResultData().get(1).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(1).getLogo(), mLogoTwo, AppConfig.DEFAULT_IMG_MERCHANT_BG);

            mNameThree.setText(lifeHuiTypeData.getResultData().get(2).getMuname());
            mDescriThree.setText(lifeHuiTypeData.getResultData().get(2).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(2).getLogo(), mLogoThree, AppConfig.DEFAULT_IMG_MERCHANT_BG);

            mNameFour.setText(lifeHuiTypeData.getResultData().get(3).getMuname());
            mDescriFour.setText(lifeHuiTypeData.getResultData().get(3).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(3).getLogo(), mLogoFour, AppConfig.DEFAULT_IMG_MERCHANT_BG);

            mNameFive.setText(lifeHuiTypeData.getResultData().get(4).getMuname());
            mDescriFive.setText(lifeHuiTypeData.getResultData().get(4).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(4).getLogo(), mLogoFive, AppConfig.DEFAULT_IMG_MERCHANT_BG);

            mNameSix.setText(lifeHuiTypeData.getResultData().get(5).getMuname());
            mDescriSix.setText(lifeHuiTypeData.getResultData().get(5).getMerdesc());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + lifeHuiTypeData.getResultData().get(5).getLogo(), mLogoSix, AppConfig.DEFAULT_IMG_MERCHANT_BG);

        }
    }
    public String getVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
