package com.bjypt.vipcard.activity;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.MerchantItemAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.MerchantCategoryOneBeam;
import com.bjypt.vipcard.model.MerchantCategroyBean;
import com.bjypt.vipcard.model.NewMerchantListBean;
import com.bjypt.vipcard.model.Root;
import com.bjypt.vipcard.model.RootMerchant;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshExpandableListView;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.GaoDeMapLocation;
import com.bjypt.vipcard.view.CategoryPopup;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/1 0001.
 */

public class MerchantTypeActivity extends BaseActivity implements VolleyCallBack {


    private RelativeLayout mSearch, mAllClassify, mDistanceClassify, mCapacityClassify, relativelayout_merchant;
    private LinearLayout view;
    private TextView quanbu_category, zhineng, category_juli;//全部分类，距离，智能排序，的 textview
    private View view_line;
    private ImageView iv_zhineng_dian, iv_juli_dian, iv_quanbu_dian;//全部分类，距离，智能排序，的右下角三角形
    private CategoryPopup pop = new CategoryPopup();//弹出的分类popupwindo
    private PullToRefreshExpandableListView plistView;
    private ExpandableListView listView;
    private MerchantItemAdapter adapter = null;
    private String pkmertype = "";
    private String mtlevel = "";
    private String juli = "";
    private String intelligentType = "";//智能排序（1：评价最好  2：最近加入 3：销量优先 4：人气最高)）
    Map<String, String> maps = new HashMap<>();
    //    final int CITYCODE = 99;
    private TextView tv_title;
    /**
     * 一级分类
     */
    private List<MerchantCategoryOneBeam> one_list;
    //    private List<MerchantCategoryOneBeam> two_list;
    private List<List<MerchantCategoryOneBeam>> listall = new ArrayList<>();
    private List<NewMerchantListBean> listmerchant = new ArrayList<NewMerchantListBean>();
    private List<String> pkmuserList = new ArrayList<>();            //商户主键
    private List<String> distanceList = new ArrayList<>();           //距离
    private String merchant_url = Config.web.merchantcatetory;     //商家分类列表url  121
    //    private String url = Config.web.merchant_list;                   //商家列表
    private String url = Config.web.new_home_merchant_listview;    //商家列表
    private int currentPage = 0;
    private int pageLength = 10;
    private int isRefresh = 1;                                       // 1刷新 2.加载更多
    final int QUERY_EXERCISE_MORE = 0x0101;
    final int QUERY_EXERCISE_REFERSH = 0x0110;
    final int ONE_CATEGORY = 0x0100;
    private BroadCastReceiverUtils utils;
    private MyBroadCastReceiver receiver;
    private RelativeLayout search_tv_rela;
    private ImageView iv_default_merchant_pic;
    private ImageView iv_merhcant_back;
    private ImageView location;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /***
     * @return 计算popwindow 的高度= 屏幕的高度-title-按钮-虚线
     */
    public int getPopupHeight() {
        return (int) (view.getHeight() - relativelayout_merchant.getHeight()
                - mAllClassify.getHeight() - 0.3);
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.fra_merchant);
    }

    @Override
    public void beforeInitView() {
        utils = new BroadCastReceiverUtils();
        receiver = new MyBroadCastReceiver();
        utils.registerBroadCastReceiver(this, "change_city", receiver);
//        utils.registerBroadCastReceiver(this,"re_dingwei",receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (utils != null) {
            utils.UnRegisterBroadCastReceiver(this, receiver);
            utils = null;
        }
    }

    /**
     * 数据请求
     */
    private void request(int type, String juli, String pkmertype, String mtlevel, String intelligentType) {
        /**
         * 获取一级分类信息
         */
        if (type == ONE_CATEGORY) {
            Log.v("TAGGGG", "ONE_CATEGORY---------------------");
            Map<String, String> maps = new HashMap<>();
            maps.put("mtlevel", "1");
            maps.put("type", "0");
            Wethod.httpPost(this, 66, merchant_url, maps, this);
        } else {
            /**
             * 上拉刷新请求商家列表数据
             */

            if (type == QUERY_EXERCISE_REFERSH) {
                if (maps != null) {
                    maps.clear();
                }
                Log.e("tyz", "QUERY_EXERCISE_REFERSH" + type);
                panduan(juli, pkmertype, mtlevel);
                if (!"".equals(intelligentType)) {
                    maps.put("intelligentType", intelligentType);
                }
                isRefresh = 1;
                currentPage = 0;

                maps.put("cityCode", getFromSharePreference(Config.userConfig.citycode));
                maps.put("lng", getFromSharePreference(Config.userConfig.lngu));
                maps.put("lat", getFromSharePreference(Config.userConfig.latu));
                maps.put("begin", 0 + "");
                maps.put("pageLength", pageLength + "");
                Log.e("tyz", "mapsL:" + maps.toString());
                Wethod.httpPost(this, 69, url, maps, this);
            } else {
                if (maps != null) {
                    maps.clear();
                }
                currentPage += 10;
                Log.e("woyaokk", "currentPage：" + currentPage);
                panduan(juli, pkmertype, mtlevel);
                if (!"".equals(intelligentType)) {
                    maps.put("intelligentType", intelligentType);
                }
                isRefresh = 2;
                maps.put("cityCode", getFromSharePreference(Config.userConfig.citycode));
                maps.put("lng", getFromSharePreference(Config.userConfig.lngu));
                maps.put("lat", getFromSharePreference(Config.userConfig.latu));
                maps.put("begin", currentPage + "");
                maps.put("pageLength", pageLength + "");
                Wethod.httpPost(this, 69, url, maps, this);
            }


        }
    }

    /**
     * @param juli      距离分类查询  例如 1km 2km 查找
     * @param pkmertype 分类二级的 主键
     * @param mtlevel   分类等级  1级 或2级
     */
    private void panduan(String juli, String pkmertype, String mtlevel) {
        if ((!"a".equals(juli)) && (!"".equals(pkmertype)) && (!"".equals(mtlevel))) {
            maps.put("range", juli);
            maps.put("pkmertype", pkmertype);
            maps.put("mtlevel", mtlevel);
        } else if ((!"a".equals(juli)) && (!"".equals(pkmertype))) {
            maps.put("range", juli);
            maps.put("pkmertype", pkmertype);
        } else if ((!"a".equals(juli)) && (!"".equals(mtlevel))) {
            maps.put("range", juli);
            maps.put("mtlevel", mtlevel);
        } else if ((!"".equals(mtlevel)) && (!"".equals(pkmertype))) {
            Log.v("TAGGGG", "-----判断  左两----------------");
            maps.put("pkmertype", pkmertype);
            maps.put("mtlevel", mtlevel);
        } else if (!"a".equals(juli)) {
            maps.put("range", juli);
        } else if (!"".equals(pkmertype)) {
            maps.put("pkmertype", pkmertype);
        } else if (!"".equals(mtlevel)) {
            maps.put("mtlevel", mtlevel);
        }
    }

    @Override
    public void initView() {
        Log.e("liyunte", "initView");
        view = (LinearLayout) findViewById(R.id.linear_view);
        iv_merhcant_back = (ImageView) findViewById(R.id.iv_merhcant_back);
        location = (ImageView) findViewById(R.id.location);
        search_tv_rela = (RelativeLayout) findViewById(R.id.search_tv_rela);

        view_line = findViewById(R.id.view_line);
        iv_zhineng_dian = (ImageView) findViewById(R.id.iv_zhineng_dian);
        iv_juli_dian = (ImageView) findViewById(R.id.iv_juli_dian);
        iv_quanbu_dian = (ImageView) findViewById(R.id.iv_quanbu_dian);
        quanbu_category = (TextView) findViewById(R.id.quanbu_category);
        category_juli = (TextView) findViewById(R.id.category_juli);
        tv_title = (TextView) findViewById(R.id.search_for_tv);
        tv_title.setSelected(true);
        tv_title.setText(getFromSharePreference(Config.userConfig.cityAdress));
        zhineng = (TextView) findViewById(R.id.zhineng);
        relativelayout_merchant = (RelativeLayout) findViewById(R.id.relativelayout_merchant);
        mSearch = (RelativeLayout) findViewById(R.id.search);
        mAllClassify = (RelativeLayout) findViewById(R.id.all_classify);//全部分类
        mDistanceClassify = (RelativeLayout) findViewById(R.id.distance_classify);//距离排序
        mCapacityClassify = (RelativeLayout) findViewById(R.id.capacity_classify);//智能排序
        plistView = (PullToRefreshExpandableListView) findViewById(R.id.listview_expand);//商家列表
        plistView.setMode(PullToRefreshBase.Mode.BOTH);
        listView = plistView.getRefreshableView();
        listView.setGroupIndicator(null);
        listView.setDividerHeight(0);
        iv_default_merchant_pic = (ImageView) findViewById(R.id.iv_default_merchant_pic);
    }

    @Override
    public void afterInitView() {
        pkmertype = getIntent().getStringExtra("pkmertype");
        String name = getIntent().getStringExtra("name");
        quanbu_category.setText(name);
        iv_quanbu_dian.setImageResource(R.mipmap.dian);
        mtlevel = "1";
        request(ONE_CATEGORY, "", "", "", "");
        request(QUERY_EXERCISE_REFERSH, juli, pkmertype, mtlevel, intelligentType);
    }

    @Override
    public void bindListener() {
        location.setOnClickListener(this);
        iv_merhcant_back.setOnClickListener(this);
        search_tv_rela.setOnClickListener(this);
        adapter = new MerchantItemAdapter(listmerchant, this, handler_content);
        listView.setAdapter(adapter);
        plistView.setPullToRefreshOverScrollEnabled(true);
        plistView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ExpandableListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                request(QUERY_EXERCISE_REFERSH, juli, pkmertype, mtlevel, intelligentType);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                request(QUERY_EXERCISE_MORE, juli, pkmertype, mtlevel, intelligentType);
            }
        });

        /**
         * 商家列表点击事件监听
         */
        mSearch.setOnClickListener(this);
        mAllClassify.setOnClickListener(this);
        mDistanceClassify.setOnClickListener(this);
        mCapacityClassify.setOnClickListener(this);
    }

    public void startLogin() {
        startActivity(new Intent(MerchantTypeActivity.this, LoginActivity.class));
    }

    @Override
    public void onClickEvent(View v) {
        /**
         * 计算显示popupd的高
         */
        int id = v.getId();
        switch (id) {
            case R.id.iv_merhcant_back:
                finish();
                break;

            case R.id.location:
                //此处执行重新定位
                Toast.makeText(this, "正在获取当前位置", Toast.LENGTH_LONG).show();
                GaoDeMapLocation gaoDeMapLocation = new GaoDeMapLocation(MerchantTypeActivity.this);
                gaoDeMapLocation.startLocation();
                gaoDeMapLocation.setDingWeiSuccessListener(new GaoDeMapLocation.DingWeiSuccessListener() {
                    @Override
                    public void onDingWeiSuccessListener() {
                        tv_title.setText(getFromSharePreference(Config.userConfig.cityAdress));
                        request();
                    }
                });
                break;

            case R.id.search:          //搜索
                startActivity(new Intent(MerchantTypeActivity.this, SearchMerchantActivity.class));
                break;

            case R.id.all_classify:    //全部分类
                if (pop.isShowing()) {
                    pop.dismiss();
                } else {
                    pop.setPopu_toSift(ActionBar.LayoutParams.MATCH_PARENT, getPopupHeight(),
                            view_line, MerchantTypeActivity.this, one_list, listall, handler, mAllClassify.getWidth());
                }
                break;

            case R.id.distance_classify:   //距离
                if (pop.isShowing()) {
                    pop.dismiss();
                } else {
                    pop.setJuli(ActionBar.LayoutParams.MATCH_PARENT, getPopupHeight(), view_line, MerchantTypeActivity.this, handler);
                }
                break;

            case R.id.capacity_classify:  //智能排序
                if (pop.isShowing()) {
                    pop.dismiss();
                } else {
                    pop.setPopu_toSift_znsx(ActionBar.LayoutParams.MATCH_PARENT, getPopupHeight(), view_line, MerchantTypeActivity.this, handler);
                }
                break;

            case R.id.search_tv_rela:
                startActivity(new Intent(MerchantTypeActivity.this, CityActivity.class));
                break;
        }
    }

    /**
     * 筛选完成后得到最终的少选结果
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                /**
                 * oneBeam  全部分类二级返回的数据
                 */
                if ("".equals(msg.obj)) {
                    pkmertype = "";
                    quanbu_category.setText("全部");
                } else {
                    MerchantCategoryOneBeam oneBeam = (MerchantCategoryOneBeam) msg.obj;
                    quanbu_category.setText(oneBeam.getMtname() + "");
                    pkmertype = oneBeam.getPkmertype();
                }
                Log.e("lll", "" + pkmertype);
                mtlevel = "2";
                request(QUERY_EXERCISE_REFERSH, juli, pkmertype, mtlevel, intelligentType);
                Log.e("liyunte", "juli" + juli + "pkmertype" + pkmertype + "mtlevel" + mtlevel + "intelligentType" + intelligentType);
                iv_quanbu_dian.setImageResource(R.mipmap.dian);
                pop.dismiss();
            } else if (msg.what == 2) {
                /**
                 * 距离返回的数据
                 */
                iv_juli_dian.setImageResource(R.mipmap.dian);
                String s = String.valueOf(msg.obj);
                if ("a".equals(s)) {
                    category_juli.setText("全部");
                } else {
                    category_juli.setText(msg.obj + "");
                }
                juli = s.substring(0, 1);
                Log.e("fff", "" + juli);
                request(QUERY_EXERCISE_REFERSH, juli, pkmertype, mtlevel, intelligentType);
            } else {
                /**
                 * 智能查询
                 */
                iv_zhineng_dian.setImageResource(R.mipmap.dian);
                zhineng.setText(msg.obj + "");
                if ("评价最好".equals(msg.obj)) {
                    intelligentType = "1";
                } else if ("最近加入".equals(msg.obj)) {
                    intelligentType = "2";
                } else if ("销量优先".equals(msg.obj)) {
                    intelligentType = "3";
                } else {
                    intelligentType = "4";
                }
                request(QUERY_EXERCISE_REFERSH, juli, pkmertype, mtlevel, intelligentType);
            }
        }

    };

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 66) {
            Log.e("liyunte", "---66" + result.toString());
            try {
                Root bean = getConfiguration().readValue(result.toString(), Root.class);
                if (bean.getResultStatus() == 0) {
                    one_list = bean.getResultData();
                    for (int i = 0; i < bean.getResultData().size(); i++) {
                        //获取二级列表的请求
                        Map<String, String> maps = new HashMap<>();
                        maps.put("mtlevel", "2");
                        maps.put("type", "0");
                        maps.put("cityCode", getFromSharePreference(Config.userConfig.citycode));
                        maps.put("parentpk", one_list.get(i).getPkmertype());
                        Wethod.httpPost(this, 67, merchant_url, maps, this);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == 67) {
            Log.e("liyunte", "---67" + result.toString());
            try {
                Root bean = getConfiguration().readValue(result.toString(), Root.class);

                listall.add(bean.getResultData());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == 69) {
            List<MerchantCategroyBean> other = new ArrayList<MerchantCategroyBean>();
            Log.e("GGGG", result.toString());
            try {

                RootMerchant homeMerchantListBean = getConfiguration().readValue(result.toString(), RootMerchant.class);
                if (homeMerchantListBean.getResultData().size() > 0) {
                    iv_default_merchant_pic.setVisibility(View.GONE);
                    if (isRefresh == 1) {
                        if (adapter != null) {
                            listmerchant.clear();
                            distanceList.clear();
                            pkmuserList.clear();

                        }
                    }

                    for (int i = 0; i < homeMerchantListBean.getResultData().size(); i++) {
                        distanceList.add(homeMerchantListBean.getResultData().get(i).getDistance());
                        pkmuserList.add(homeMerchantListBean.getResultData().get(i).getPkmuser());
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
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                            plistView.onRefreshComplete();
                        } else {
                            adapter = new MerchantItemAdapter(listmerchant, this, handler_content);
                            adapter.notifyDataSetChanged();
                            plistView.onRefreshComplete();
                        }
                    } else if (isRefresh == 2) {
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                            plistView.onRefreshComplete();
                        } else {
                            adapter = new MerchantItemAdapter(listmerchant, this, handler_content);
                            adapter.notifyDataSetChanged();
                            plistView.onRefreshComplete();
                        }
                    } else {
                        adapter.add(listmerchant);
                        adapter.notifyDataSetChanged();
                        plistView.onRefreshComplete();
                    }
                } else if (homeMerchantListBean.getResultData().size() == 0 && isRefresh != 2) {
                    Log.e("GGGG", "NULL-----------------------");
                    listmerchant.clear();
                    iv_default_merchant_pic.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    plistView.onRefreshComplete();
                } else if (homeMerchantListBean.getResultData().size() == 0 && isRefresh == 2) {
                    plistView.onRefreshComplete();
                }
            } catch (IOException e) {
                Log.e("liyunte", "eee" + e);
                e.printStackTrace();
            }
            listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    Intent intent = new Intent(MerchantTypeActivity.this, NewSubscribeDishesActivity.class);
                    intent.putExtra("distance", distanceList.get(groupPosition));
                    intent.putExtra("pkmuser", pkmuserList.get(groupPosition));
                    startActivity(intent);
                    return true;
                }
            });


        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }
   /* public Handler getHandle(){
        return handler_m;
    }*/

    /* private Handler handler_m = new Handler(){
         @Override
         public void handleMessage(Message msg) {
             super.handleMessage(msg);
             if (msg.what==8){

                 String data = (String) msg.obj;
                 pkmertype = data.substring(0,data.indexOf(","));
                 String name = data.substring(data.indexOf(",")+1);
                 Log.e("liyunte", "data:"+data.substring(0,data.indexOf(",")));
                 Log.e("liyunte","name:"+name);
                 quanbu_category.setText(name);
                 iv_quanbu_dian.setImageResource(R.mipmap.dian);
                 mtlevel="1";
                 request(QUERY_EXERCISE_REFERSH, juli, pkmertype,mtlevel,intelligentType);
             }
         }
     };*/
    Handler handler_content = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 110) {
                String value = (String) msg.obj;
                int sum = Integer.parseInt(value.substring(0, value.indexOf("a")));
                if (sum % 2 == 0) {
                    listView.collapseGroup(Integer.parseInt(value.substring(value.indexOf("a") + 1)));
                } else {
                    listView.expandGroup(Integer.parseInt(value.substring(value.indexOf("a") + 1)));
                }
            }
        }
    };

    public void request() {
        updata_home_utils.sendBroadCastReceiver(this, "updata_home_utils");
        request(QUERY_EXERCISE_REFERSH, juli, pkmertype, mtlevel, intelligentType);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MerchantType Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("change_city".equals(intent.getAction())) {
                String city = intent.getStringExtra("updata");
                if (city.length() >= 5) {
                    city = city.substring(0, 5);
                }
                Log.e("liyunteee", "back");
                tv_title.setText(city);
                GaoDeMapLocation gaoDeMapLocation = new GaoDeMapLocation(MerchantTypeActivity.this);
                gaoDeMapLocation.doSearchQuery(city);
                gaoDeMapLocation.setCityChangeListener(new GaoDeMapLocation.CityChangeListener() {
                    @Override
                    public void onCityChangeListner() {
                        request();
                    }
                });
            }

        }
    }

    private BroadCastReceiverUtils updata_home_utils = new BroadCastReceiverUtils();

    @Override
    public void isConntectedAndRefreshData() {
        GaoDeMapLocation gaoDeMapLocation = new GaoDeMapLocation(this);
        gaoDeMapLocation.startLocation();
        gaoDeMapLocation.setDingWeiSuccessListener(new GaoDeMapLocation.DingWeiSuccessListener() {
            @Override
            public void onDingWeiSuccessListener() {
                tv_title.setText(getFromSharePreference(Config.userConfig.cityAdress));
                request(ONE_CATEGORY, "", "", "", "");
                request();
            }
        });
    }
}
