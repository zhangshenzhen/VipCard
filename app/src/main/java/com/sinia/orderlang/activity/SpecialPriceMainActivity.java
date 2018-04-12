package com.sinia.orderlang.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.DefaultAdressActivity;
import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.activity.MainActivity;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.LocationDingweiBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.google.gson.Gson;
import com.sinia.orderlang.adapter.SpecialOfferAdapter;
import com.sinia.orderlang.bean.TeJiaListList;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.http.HttpCallBackListener;
import com.sinia.orderlang.request.TeJiaListRequest;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.StringUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/7/6
 * Use by 特价区
 */
public class SpecialPriceMainActivity extends Activity implements View.OnClickListener,PoiSearch.OnPoiSearchListener,AMapLocationListener {

    private View rootView;
    private TextView tv1, tv_classify, tv_area;
    private PullToRefreshListView listview;
    private ImageView back, search;
    private RelativeLayout rl_classify, rl_area, rl_sifting;
    private SpecialOfferAdapter adapter;
    private PopupWindow popWindow;
    public static String specialAddess = "-1";
    private RelativeLayout location_special;
    private ImageView shop_lang,order_list_lang;
    private int begin = 0;
    private int pageLength = 10;
    private int isRefresh = 0;
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;


    private RelativeLayout mDingdan,mGeren,mGouwu;
    /**
     *  开始定位
     */
    public final static int MSG_LOCATION_START = 0;
    /**
     * 定位完成
     */
    public final static int MSG_LOCATION_FINISH = 1;
    /**
     * 停止定位
     */
    public final static int MSG_LOCATION_STOP= 2;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    /**
     * conditional 同城送
     */
    public static String conditional = "-1", goodType = "-1";
    public static String city = SeckillMainActivity.city,
            areaName = SeckillMainActivity.areaName, goodName = "-1";
    public static TeJiaListList teJiaListList;
    public static String flag_area_special = "2";
    private LangMainActivity activity;
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                // case 300:
                // listview.onRefreshComplete();
                // break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_special_offer);
        initView();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        MainActivity.tab_select_flag = 0;
        super.onResume();
        if(MainActivity.lang_flag_tejia == 2){
            finish();
        }else{
            if (city.equals("-1")) {
                tv_area.setText(SharedPreferenceUtils.getFromSharedPreference(SpecialPriceMainActivity.this, Config.userConfig.cname));
            } else {
                tv_area.setText(areaName);
            }
            if (goodType.equals("-1")) {
                tv_classify.setText("分类");
            } else {
                int pos = Integer.parseInt(goodType);
                tv_classify.setText(teJiaListList.getArrayXnGoodTypeItems()
                        .get(pos - 1).getTypeName());
            }
            isRefresh = 0;
            teJiaList(begin,pageLength);
        }
        Log.e("woyaokk", "onResume");
//        if (!activity.showWaht) {

//        }
        Log.d("lamp", "SpecialOfferFragment---onResume");
    }

    private Dialog dialog;
    public void showLoad(String showText) {
        if (StringUtil.isEmpty(showText)) {
            showText = "正在加载中...";
        }
        dialog = new Dialog(this, R.style.dialog);
        View v = ((LayoutInflater) this.getSystemService(
                this.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.view_progress_dialog_anim, null);
        ((TextView) v.findViewById(R.id.tv_info)).setText(showText);
        Display d = this.getWindowManager().getDefaultDisplay();
        dialog.setContentView(v, new ViewGroup.LayoutParams((int) (d.getWidth() * 0.5),
                (int) (d.getHeight() * 0.15)));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void teJiaList(int begin,int pageLength) {
        showLoad("");
        TeJiaListRequest request = new TeJiaListRequest();
        request.setMethod("teJiaList");
        request.setConditional(conditional);
        request.setGoodType(goodType);
        request.setCity(SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.citycode));
        request.setAreaName(areaName);

        request.setFlag_area(flag_area_special);

        request.setGoodName(goodName);
        request.setBegin(begin + "");
        request.setPageLength(pageLength + "");
        request.setAddress(specialAddess);
        Log.e("tyz", "areaName:" + areaName + "      specialAddess:" + specialAddess);
        Log.d("URL", Constants.BASE_URL + request.toString());
        CoreHttpClient.get(this, request.toString(),
                new HttpCallBackListener() {

                    @Override
                    public void onSuccess(JSONObject json) {
                        Log.d("result", json.toString());
                        dialog.dismiss();
                        if (json.optInt("isSuccessful") == 0) {

                            if (isRefresh == 1) {
                                if (adapter != null) {
                                    adapter.data.clear();
                                }
                            }


                            Gson gson = new Gson();
                            TeJiaListList list = gson.fromJson(json.toString(),
                                    TeJiaListList.class);
                            teJiaListList = list;
//                            adapter.data.clear();
//                            adapter.data.addAll(list.getGoodItems());
//                            adapter.notifyDataSetChanged();


                            if (isRefresh == 0) {
                                //没有执行上拉刷新和下拉加载操作
                                adapter.data.clear();
                                if (list != null) {
                                    adapter.data.addAll(list.getGoodItems());
                                }
                                adapter.notifyDataSetChanged();
                                listview.onRefreshComplete();
                            } else if (isRefresh == 1) {
                                //执行下拉刷新操作
                                if (adapter != null) {
                                    adapter.data.addAll(list.getGoodItems());
                                    adapter.notifyDataSetChanged();
                                    listview.onRefreshComplete();
                                } else {
                                    adapter = new SpecialOfferAdapter(SpecialPriceMainActivity.this);
                                    adapter.notifyDataSetChanged();
                                    listview.onRefreshComplete();
                                }

                            } else if (isRefresh == 2) {
                                //执行上拉加载操作
                                if (adapter != null) {
                                    adapter.add(list.getGoodItems());
                                    adapter.notifyDataSetChanged();
                                    listview.onRefreshComplete();
                                } else {
                                    adapter = new SpecialOfferAdapter(SpecialPriceMainActivity.this);
                                    adapter.notifyDataSetChanged();
                                    listview.onRefreshComplete();
                                }
                            }

                        }
                    }

                    @Override
                    public void onRequestFailed() {

                    }
                });
    }

    private void onRequest(int type){
        if(type == QUERY_REFERSH){
            //下拉刷新
            isRefresh = 1;
            begin = 0;
            teJiaList(begin,pageLength);
        }else{
            //上拉加载
            isRefresh = 2;
            begin+=10;
            teJiaList(begin,pageLength);
        }
    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeAllViewsInLayout();
        }
        return rootView;
    }*/

    private void initView() {
        areaName = "-1";
        city = "-1";
        specialAddess = SharedPreferenceUtils.getFromSharedPreference(SpecialPriceMainActivity.this, Config.userConfig.pname)+SharedPreferenceUtils.getFromSharedPreference(SpecialPriceMainActivity.this, Config.userConfig.cname);
//        +SharedPreferenceUtils.getFromSharedPreference(SpecialPriceMainActivity.this, Config.userConfig.adName

        locationClient = new AMapLocationClient(SpecialPriceMainActivity.this);
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为低功耗模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        // 设置定位监听
        locationClient.setLocationListener(this);
        listview = (PullToRefreshListView) findViewById(R.id.listview);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        location_special = (RelativeLayout) findViewById(R.id.location_special);

        order_list_lang = (ImageView) findViewById(R.id.order_list_lang);
        shop_lang = (ImageView) findViewById(R.id.shop_lang);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv_classify = (TextView) findViewById(R.id.tv_classify);
        tv_area = (TextView) findViewById(R.id.tv_area);
        back = (ImageView) findViewById(R.id.back_special);
        search = (ImageView) findViewById(R.id.search);
        rl_classify = (RelativeLayout) findViewById(R.id.rl_classify);
        rl_sifting = (RelativeLayout) findViewById(R.id.rl_sifting);
        rl_area = (RelativeLayout) findViewById(R.id.rl_area);

        mDingdan = (RelativeLayout) findViewById(R.id.dingdan_rela);
        mGeren = (RelativeLayout) findViewById(R.id.geren_rela);
        mGouwu  = (RelativeLayout) findViewById(R.id.gouwu_rela);

        mDingdan.setOnClickListener(this);
        mGeren.setOnClickListener(this);
        mGouwu.setOnClickListener(this);

        adapter = new SpecialOfferAdapter(this);
        listview.setAdapter(adapter);
        back.setOnClickListener(this);
        rl_sifting.setOnClickListener(this);
        rl_classify.setOnClickListener(this);
        rl_area.setOnClickListener(this);
        search.setOnClickListener(this);
        location_special.setOnClickListener(this);
        order_list_lang.setOnClickListener(this);
        shop_lang.setOnClickListener(this);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                if(SharedPreferenceUtils.getFromSharedPreference(SpecialPriceMainActivity.this,Config.userConfig.is_Login).equals("Y")){
                    // 跳转到特价商品详情页
                    Intent it = new Intent(SpecialPriceMainActivity.this, GoodsDetailActivity.class);
                    it.putExtra("goodId", adapter.data.get(position - 1)
                            .getGoodId());
                    startActivity(it);
                }else {
                    startActivity(new Intent(SpecialPriceMainActivity.this, LoginActivity.class));
                }
            }
        });

        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                onRequest(QUERY_REFERSH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                onRequest(QUERY_MORE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_sifting:
                showPopWindow();
                break;
            case R.id.rl_classify:
                Intent it = new Intent(this, ShowDialogActivity.class);
                it.putExtra("showWhat", false);
                it.putExtra("secOrSpec", 2);
                startActivity(it);
                break;
            case R.id.rl_area:
                Intent it2 = new Intent(this, ShowDialogActivity.class);
                it2.putExtra("showWhat", true);
                it2.putExtra("secOrSpec", 2);
                startActivity(it2);
                break;
            case R.id.search:
                Intent it3 = new Intent(this, Search1Activity.class);
                it3.putExtra("secOrSpec", 2);
                startActivity(it3);
                break;
            case R.id.back_special:
                finish();
                flag_area_special = "2";
                break;
            case R.id.location_special:
                startLocation();
                SeckillMainActivity.areaName = "-1";
                SeckillMainActivity.city = "-1";
                tv_area.setText(SharedPreferenceUtils.getFromSharedPreference(SpecialPriceMainActivity.this, Config.userConfig.cname));
                break;
            case R.id.shop_lang:
                LangMainActivity.showGouWuChe = 1;
                startActivityForNoIntent(LangMainActivity.class);
                break;
            case R.id.order_list_lang:
                startActivity(new Intent(this, com.sinia.orderlang.activity.AllOrderActivity.class));
                break;
            case R.id.dingdan_rela:
                if(SharedPreferenceUtils.getFromSharedPreference(SpecialPriceMainActivity.this,Config.userConfig.is_Login).equals("Y")){
                    // 跳转到订单
                    startActivity(new Intent(this, com.sinia.orderlang.activity.AllOrderActivity.class));
                }else {
                    startActivity(new Intent(SpecialPriceMainActivity.this, LoginActivity.class));
                }
                break;
            case R.id.geren_rela:
                if(SharedPreferenceUtils.getFromSharedPreference(SpecialPriceMainActivity.this,Config.userConfig.is_Login).equals("Y")){
                    // 跳转到个人中心
                    startActivity(new Intent(this, DefaultAdressActivity.class));
                }else {
                    startActivity(new Intent(SpecialPriceMainActivity.this, LoginActivity.class));
                }
                break;
            case R.id.gouwu_rela:

                if(SharedPreferenceUtils.getFromSharedPreference(SpecialPriceMainActivity.this,Config.userConfig.is_Login).equals("Y")){
                    // 跳转到购物车
                    LangMainActivity.showGouWuChe = 1;
                    startActivityForNoIntent(LangMainActivity.class);
                }else {
                    startActivity(new Intent(SpecialPriceMainActivity.this, LoginActivity.class));
                }



                break;
            default:
                break;
        }
    }
    protected void startActivityForNoIntent(Class forwordClass) {
        Intent intent = new Intent(this, forwordClass);
        startActivity(intent);
    }

    private void showPopWindow() {
        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_sifting_popwindow,
                null);
        popWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popWindow.setAnimationStyle(R.style.ActionSheetDialogAnimation2);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.showAsDropDown(rl_sifting, 0, 0);
        WindowManager.LayoutParams lp = this.getWindow()
                .getAttributes();
        // lp.alpha = 0.7f;
        lp.alpha = 1f;
        this.getWindow().setAttributes(lp);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
            }
        });

        TextView tv_comprehensive = (TextView) view
                .findViewById(R.id.tv_comprehensive);
        TextView tv_same_city = (TextView) view.findViewById(R.id.tv_same_city);
        TextView tv_techan = (TextView) view.findViewById(R.id.tv_techan);
        tv_comprehensive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tv1.setText("综合");
                popWindow.dismiss();
                conditional = "-1";
                teJiaList(begin,pageLength);
            }
        });
        tv_same_city.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tv1.setText("同城送");
                popWindow.dismiss();
                conditional = "1";
                teJiaList(begin,pageLength);
            }
        });
        tv_techan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tv1.setText("特产区");
                popWindow.dismiss();
                conditional = "2";
                teJiaList(begin,pageLength);
            }
        });
    }

    /**
     * 选择地址后重新选择当前定位城市
     */
/*在启动当前APP的时候启动定位功能*/
    private void startLocation(){
        Log.e("HHH","设置参数");
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
        locationHandler.sendEmptyMessage(MSG_LOCATION_START);
    }

    /*地址切换*/
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null != aMapLocation) {
            Message msg = locationHandler.obtainMessage();
            msg.obj = aMapLocation;
            msg.what = MSG_LOCATION_FINISH;
            locationHandler.sendMessage(msg);
        }
    }
    private BroadCastReceiverUtils updata_home_utils;

    Handler locationHandler = new Handler(){
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_LOCATION_START:
//                    tvReult.setText("正在定位...");
                    break;
                //定位完成
                case MSG_LOCATION_FINISH:
                    updata_home_utils = new BroadCastReceiverUtils();
                    AMapLocation loc = (AMapLocation)msg.obj;
                    LocationDingweiBean ld = getLocationStr(loc);
                    Log.e("HHH","开始定位:"+ld.getAdress()+"------"+ld.getCityAdress());

//                    specialAddess = ld.getAdress();
//                    lat = ld.getmLat() + "";
//                    lng = ld.getmLng() + "";
//                    cityCode = ld.getCityCode() + "";
//                    tv_title.setText(ld.getCityAdress() + "");
//                    SharedPreferenceUtils.saveToSharedPreference(getActivity(), Config.userConfig.lngu, ld.getmLng() + "");
//                    SharedPreferenceUtils.saveToSharedPreference(getActivity(), Config.userConfig.latu, ld.getmLat() + "");
//                    SharedPreferenceUtils.saveToSharedPreference(SeckillMainActivity.this, Config.userConfig.citycode, ld.getCityCode() + "");
//                    SharedPreferenceUtils.saveToSharedPreference(SeckillMainActivity.this, Config.userConfig.adress, ld.getAdress() + "");
//                    updata_home_utils.sendBroadCastReceiver(SeckillMainActivity.this, "updata_home_utils");

                    adapter.notifyDataSetChanged();
                    if(Wethod.isConnected(SpecialPriceMainActivity.this)){
                        if(ld.getCityCode()!=null||!ld.getCityCode().equals("0")){
                            locationClient.stopLocation();
                            locationHandler.sendEmptyMessage(MSG_LOCATION_STOP);
                        }
                    }


                    break;
                case MSG_LOCATION_STOP:
//                    tvReult.setText("定位停止");
                    break;
                default:
                    break;
            }
        };
    };

    /**
     * 根据定位结果返回定位信息的字符串
     * @return
     */
    private LocationDingweiBean getLocationStr(AMapLocation location){

        LocationDingweiBean locationDingweiBean = new LocationDingweiBean();

        if(null == location){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if(location.getErrorCode() == 0){
            sb.append("定位成功" + "\n");
            sb.append("定位类型: " + location.getLocationType() + "\n");
            sb.append("经    度    : " + location.getLongitude() + "\n");
            sb.append("纬    度    : " + location.getLatitude() + "\n");
            sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
            sb.append("提供者    : " + location.getProvider() + "\n");
            locationDingweiBean.setmLng(location.getLongitude());
            locationDingweiBean.setmLat(location.getLatitude());

            if (location.getProvider().equalsIgnoreCase(
                    android.location.LocationManager.GPS_PROVIDER)) {
                // 以下信息只有提供者是GPS时才会有
                sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                sb.append("角    度    : " + location.getBearing() + "\n");
                // 获取当前提供定位服务的卫星个数
                sb.append("星    数    : "
                        + location.getSatellites() + "\n");
            } else {
                // 提供者是GPS时是没有以下信息的
                sb.append("国    家    : " + location.getCountry() + "\n");
                sb.append("省            : " + location.getProvince() + "\n");
                sb.append("市            : " + location.getCity() + "\n");
                sb.append("城市编码 : " + location.getCityCode() + "\n");
                sb.append("区            : " + location.getDistrict() + "\n");
                sb.append("区域 码   : " + location.getAdCode() + "\n");
                sb.append("地    址    : " + location.getAddress() + "\n");
                sb.append("兴趣点    : " + location.getPoiName() + "\n");
                specialAddess = location.getProvince()+location.getCity()+location.getDistrict();
                Log.e("woyaokk","location specialAddess:"+specialAddess);
                teJiaList(begin,pageLength);
                if(location.getAddress().contains("省")){
                    int sort = location.getAddress().indexOf("省");
                    String b = location.getAddress().substring(sort+1,location.getAddress().length());
                    if(location.getAddress().contains("市")){
                        int sortCity = b.indexOf("市");
                        String c = b.substring(sortCity+1,b.length());
                        locationDingweiBean.setCityAdress(c);
                    }else{
                        locationDingweiBean.setCityAdress(b);
                    }

                }else{
                    locationDingweiBean.setCityAdress(location.getAddress());
                }


                locationDingweiBean.setAdress(location.getCity());
                locationDingweiBean.setCityCode(location.getCityCode());
            }
        } else {
            //定位失败
            sb.append("定位失败" + "\n");
            sb.append("错误码:" + location.getErrorCode() + "\n");
            sb.append("错误信息:" + location.getErrorInfo() + "\n");
            sb.append("错误描述:" + location.getLocationDetail() + "\n");
        }

        return locationDingweiBean;
    }


    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        //地图搜索的回调
        if(i == 1000){
            if(poiResult !=null && poiResult.getQuery()!=null){
                //取得第一页的poiitem的数据，页数从0开始
                List<PoiItem> poiItems = poiResult.getPois();

                String adName = poiItems.get(0).getAdName();//区的地址，为了给供货区提供区的地址
                String pname = poiItems.get(0).getProvinceName();//省的地址，为了给供货区提供省的地址
                String cname = poiItems.get(0).getCityName();//市的地址，为了给供货区提供市的地址

                /********************************将供货区所需要的省市区存入本地************************************/
                SharedPreferenceUtils.saveToSharedPreference(this, Config.userConfig.pname, pname);
                SharedPreferenceUtils.saveToSharedPreference(this, Config.userConfig.adName, adName);
                SharedPreferenceUtils.saveToSharedPreference(this, Config.userConfig.cname, cname);
                specialAddess = pname + cname + adName;
                Log.e("woyaokk","specialAddess:"+specialAddess);
                LatLonPoint latLonPoint = poiItems.get(0).getLatLonPoint();
                //  lat = latLonPoint.getLatitude()+"";
                // lng = latLonPoint.getLongitude()+"";
//                Log.e("GAODE", "***********   lat:" + lat + "   lng:" + lng + "    citycode:" + cityCode);
                //tv_title.setText(poiItems.get(0).get);
//                SharedPreferenceUtils.saveToSharedPreference(SeckillMainActivity.this,"lng",latLonPoint.getLatitude()+"");
//                SharedPreferenceUtils.saveToSharedPreference(SeckillMainActivity.this, "lat", latLonPoint.getLongitude() + "");
//                SharedPreferenceUtils.saveToSharedPreference(SeckillMainActivity.this, "cityCode", cityCode);
                teJiaList(begin,pageLength);
                adapter.notifyDataSetChanged();

            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            SeckillMainActivity.areaName = "-1";
            SeckillMainActivity.city = "-1";
            flag_area_special = "2";
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("tyz","onActivityResult");
//        SpecialPriceMainActivity.flag_area_special = "1";
        super.onActivityResult(requestCode, resultCode, data);
    }
}
