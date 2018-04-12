package com.sinia.orderlang.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.bjypt.vipcard.R;
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
import com.sinia.orderlang.adapter.SecKillAdapter;
import com.sinia.orderlang.bean.RedpacketBean;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.http.HttpCallBackListener;
import com.sinia.orderlang.request.RedBaoListRequest;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.StringUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/7/6
 * Use by秒杀区
 */
public class SeckillMainActivity extends Activity implements View.OnClickListener,PoiSearch.OnPoiSearchListener,AMapLocationListener {
    private View rootView;
    private String afreshCityCode;
    private PullToRefreshListView listview;
    private ImageView back, search;
    private RelativeLayout rl_classify, rl_area;
    private SecKillAdapter adapter;
    private TextView tv_classify, tv_area;
    public static String city = "-1", areaName = "-1", goodName = "-1",
            goodType = "-1";
    private boolean flag = false;
    public static RedpacketBean redpacketBean;
    private LangMainActivity activity;
    private RelativeLayout location;
    public static String address = "-1";
    public static String flag_area = "2";
    private ImageView order_list_seckill;
    private int begin = 0;
    private int pageLength = 10;
    private int isRefresh = 0;
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;
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

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    flag = true;
                    redBaoList(SharedPreferenceUtils.getFromSharedPreference(SeckillMainActivity.this, Config.userConfig.citycode), areaName, goodType, goodName,address,begin+"",pageLength+"",flag_area);
                    break;
                case 300:
                    // listview.onRefreshComplete();
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_seckill);
        initView();
        // initLocat();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        if(MainActivity.lang_flag_redpack == 2){
            finish();
            if(dialog!=null){
                dialog.dismiss();
            }
        }else{
            if (areaName.equals("-1")) {
                tv_area.setText(SharedPreferenceUtils.getFromSharedPreference(SeckillMainActivity.this, Config.userConfig.cname));
            } else {
                tv_area.setText(areaName);
            }
            Log.d("lamp", "goodType=" + goodType);
            if (goodType.equals("-1")) {
                tv_classify.setText("分类");
            } else {
                int pos = Integer.parseInt(goodType);
                tv_classify.setText(redpacketBean.getArrayXnGoodTypeItems()
                        .get(pos - 1).getTypeName());
            }
            isRefresh = 0;
            redBaoList(SharedPreferenceUtils.getFromSharedPreference(SeckillMainActivity.this, Config.userConfig.citycode), SeckillMainActivity.areaName,
                    goodType, goodName,address,begin+"",pageLength+"",flag_area);
        }
        super.onResume();
//        if (activity.showWaht) {


//        }
        Log.d("lamp", "SeckillFragment---onResume");
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeAllViewsInLayout();
        }
        return rootView;
    }*/

    // 定位请求
	/*private void initLocat() {
		Log.d("lamp", "initLocat");
		TencentLocationRequest request = TencentLocationRequest.create();
		request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
		TencentLocationManager.getInstance(getActivity())
				.requestLocationUpdates(request, this);
	}*/

    private void redBaoList(String city, String areaname, String goodType,
                            String goodName,String address,String begin,String pageLength, final String flag_area) {
        showLoad("");
        RedBaoListRequest request = new RedBaoListRequest();
        request.setMethod("redBaoList");
        request.setCity(city);
        request.setAreaName(areaname);
        request.setGoodType(goodType);
        request.setGoodName(goodName);
        request.setAddress(address);
        request.setBegin(begin);
        request.setPageLength(pageLength);
        request.setFlag_area(flag_area);
        Log.d("URL", Constants.BASE_URL + request.toString());
        CoreHttpClient.get(this, request.toString(),
                new HttpCallBackListener() {

                    @Override
                    public void onSuccess(JSONObject json) {
                        Log.d("result", json.toString());
//                        SeckillMainActivity.flag_area = "1";
                        dialog.dismiss();
                        if (json.optInt("isSuccessful") == 0
                                && json.optInt("state") == 0) {

                            if (isRefresh == 1) {
                                if (adapter != null) {
                                    adapter.data.clear();
                                }
                            }

                            Gson gson = new Gson();
                            RedpacketBean redpacketBean1 = gson.fromJson(
                                    json.toString(), RedpacketBean.class);
                            redpacketBean = redpacketBean1;
                            Log.e("woyaokk", "isRefresh:" + isRefresh + "     adapter:" + adapter);
//                            adapter.data.clear();
//                            adapter.data.addAll(redpacketBean1.getGoodItems());
//                            adapter.notifyDataSetChanged();
                            if (isRefresh == 0) {
                                //没有执行上拉刷新和下拉加载操作
                                adapter.data.clear();
                                adapter.data.addAll(redpacketBean1.getGoodItems());
                                adapter.notifyDataSetChanged();
                                listview.onRefreshComplete();
                            } else if (isRefresh == 1) {
                                //执行下拉刷新操作
                                if (adapter != null) {
                                    adapter.data.addAll(redpacketBean1.getGoodItems());
                                    adapter.notifyDataSetChanged();
                                    listview.onRefreshComplete();
                                } else {
                                    adapter = new SecKillAdapter(SeckillMainActivity.this);
                                    adapter.notifyDataSetChanged();
                                    listview.onRefreshComplete();
                                }

                            } else if (isRefresh == 2) {
                                //执行上拉加载操作
                                if (adapter != null) {
                                    adapter.add(redpacketBean1.getGoodItems());
                                    adapter.notifyDataSetChanged();
                                    listview.onRefreshComplete();
                                } else {
                                    adapter = new SecKillAdapter(SeckillMainActivity.this);
                                    adapter.notifyDataSetChanged();
                                    listview.onRefreshComplete();
                                }
                            }
                        }

                    }

                    @Override
                    public void onRequestFailed() {
                        dialog.dismiss();
                        Toast.makeText(SeckillMainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void initView() {
        areaName = "-1";
        address = SharedPreferenceUtils.getFromSharedPreference(SeckillMainActivity.this, Config.userConfig.pname)+SharedPreferenceUtils.getFromSharedPreference(SeckillMainActivity.this, Config.userConfig.cname)+SharedPreferenceUtils.getFromSharedPreference(SeckillMainActivity.this, Config.userConfig.adName);
        locationClient = new AMapLocationClient(SeckillMainActivity.this);
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为低功耗模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        // 设置定位监听
        locationClient.setLocationListener(this);

        order_list_seckill = (ImageView) findViewById(R.id.order_list_seckill);
        listview = (PullToRefreshListView) findViewById(R.id.listview);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        location = (RelativeLayout) findViewById(R.id.location_lang);
        back = (ImageView) findViewById(R.id.back_seckill);
        search = (ImageView) findViewById(R.id.search);
        rl_classify = (RelativeLayout) findViewById(R.id.rl_classify);
        rl_area = (RelativeLayout) findViewById(R.id.rl_area);
        tv_classify = (TextView) findViewById(R.id.tv_classify);
        tv_area = (TextView) findViewById(R.id.tv_area);
        tv_area.setText(SharedPreferenceUtils.getFromSharedPreference(SeckillMainActivity.this, Config.userConfig.cname));
        adapter = new SecKillAdapter(this);
        listview.setAdapter(adapter);
        rl_classify.setOnClickListener(this);
        rl_area.setOnClickListener(this);
        search.setOnClickListener(this);
        back.setOnClickListener(this);
        location.setOnClickListener(this);
        order_list_seckill.setOnClickListener(this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                if(SharedPreferenceUtils.getFromSharedPreference(SeckillMainActivity.this,Config.userConfig.is_Login).equals("Y")){
                    Intent it = new Intent(SeckillMainActivity.this,
                            SeckillDetailActivity.class);
                    it.putExtra("goodId", adapter.data.get(position-1).getGoodId());
                    startActivity(it);
                }else{
                    startActivity(new Intent(SeckillMainActivity.this, LoginActivity.class));
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

    private void onRequest(int type){
        if(type == QUERY_REFERSH){
            //下拉刷新
            isRefresh = 1;
            begin = 0;
            redBaoList(SharedPreferenceUtils.getFromSharedPreference(SeckillMainActivity.this, Config.userConfig.citycode), SeckillMainActivity.areaName,
                    goodType, goodName,address,begin+"",pageLength+"",flag_area);
        }else{
            //上拉加载
            isRefresh = 2;
            begin+=10;
            redBaoList(SharedPreferenceUtils.getFromSharedPreference(SeckillMainActivity.this, Config.userConfig.citycode), SeckillMainActivity.areaName,
                    goodType, goodName,address,begin+"",pageLength+"",flag_area);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_classify:
                Intent it = new Intent(this, ShowDialogActivity.class);
                it.putExtra("showWhat", false);
                it.putExtra("secOrSpec", 1);
                startActivityForResult(it, 100);
                break;
            case R.id.rl_area:
                Intent it2 = new Intent(this, ShowDialogActivity.class);
                it2.putExtra("showWhat", true);
                it2.putExtra("secOrSpec", 1);
                startActivityForResult(it2, 101);
                break;
            case R.id.search:
                Intent it3 = new Intent(this, Search1Activity.class);
                it3.putExtra("flag", true);
                it3.putExtra("secOrSpec", 1);
                startActivity(it3);
                break;
            case R.id.back_seckill:
                finish();
                flag_area = "2";
                areaName = "-1";
                break;
            case R.id.location_lang:
                areaName = "-1";
                tv_area.setText(SharedPreferenceUtils.getFromSharedPreference(SeckillMainActivity.this, Config.userConfig.cname));
                startLocation();
                break;
            case R.id.order_list_seckill:
                startActivity(new Intent(this, com.sinia.orderlang.activity.RedPacketActivity.class));
                break;
            default:
                break;
        }
    }

	/*@Override
	public void onLocationChanged(TencentLocation tencentLocation, int code,
			String arg2) {
		switch (code) {
		case TencentLocation.ERROR_OK:
			Log.d("lamp",
					tencentLocation.getProvince() + tencentLocation.getCity()
							+ tencentLocation.getDistrict());
			String location = tencentLocation.getProvince()
					+ tencentLocation.getCity() + tencentLocation.getDistrict();
			Log.d("lamp", location);
			city = tencentLocation.getCity();
			areaName = tencentLocation.getDistrict();
			tv_area.setText(city);
			handler.sendEmptyMessage(100);
			TencentLocationManager.getInstance(getActivity()).removeUpdates(
					this);
			break;
		case TencentLocation.ERROR_NETWORK:
			// showToast("网络异常");
			break;
		case TencentLocation.ERROR_BAD_JSON:
			// showToast("定位失败，请手动搜索城市");
			break;
		case TencentLocation.ERROR_UNKNOWN:
			// showToast("定位失败，请手动搜索城市");
			break;
		default:
			break;
		}

	}*/

	/*@Override
	public void onStatusUpdate(String arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub

	}*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 100:

                    break;
                case 101:

                    break;
                default:
                    break;
            }
        }
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
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOCATION_START:
//                    tvReult.setText("正在定位...");
                    break;
                //定位完成
                case MSG_LOCATION_FINISH:
                    updata_home_utils = new BroadCastReceiverUtils();
                    AMapLocation loc = (AMapLocation)msg.obj;
                    LocationDingweiBean ld = getLocationStr(loc);
                    Log.e("HHH","开始定位:"+ld.getCityCode());
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
                    if(Wethod.isConnected(SeckillMainActivity.this)){
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
                redBaoList(location.getCityCode(), "-1",
                        goodType, goodName, location.getProvince()+location.getCity()+location.getDistrict(), begin+"",pageLength+"",flag_area);
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
                afreshCityCode = poiItems.get(0).getCityCode();

                String adName = poiItems.get(0).getAdName();//区的地址，为了给供货区提供区的地址
                String pname = poiItems.get(0).getProvinceName();//省的地址，为了给供货区提供省的地址
                String cname = poiItems.get(0).getCityName();//市的地址，为了给供货区提供市的地址

                /********************************将供货区所需要的省市区存入本地************************************/
                SharedPreferenceUtils.saveToSharedPreference(this, Config.userConfig.pname, pname);
                SharedPreferenceUtils.saveToSharedPreference(this, Config.userConfig.adName, adName);
                SharedPreferenceUtils.saveToSharedPreference(this, Config.userConfig.cname, cname);

                LatLonPoint latLonPoint = poiItems.get(0).getLatLonPoint();
                //  lat = latLonPoint.getLatitude()+"";
                // lng = latLonPoint.getLongitude()+"";
//                Log.e("GAODE", "***********   lat:" + lat + "   lng:" + lng + "    citycode:" + cityCode);
                //tv_title.setText(poiItems.get(0).get);
//                SharedPreferenceUtils.saveToSharedPreference(SeckillMainActivity.this,"lng",latLonPoint.getLatitude()+"");
//                SharedPreferenceUtils.saveToSharedPreference(SeckillMainActivity.this, "lat", latLonPoint.getLongitude() + "");
//                SharedPreferenceUtils.saveToSharedPreference(SeckillMainActivity.this, "cityCode", cityCode);
                redBaoList(afreshCityCode, "-1",
                        goodType, goodName,pname+cname+adName,begin+"",pageLength+"",flag_area);
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
            flag_area = "2";
            areaName = "-1";
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
