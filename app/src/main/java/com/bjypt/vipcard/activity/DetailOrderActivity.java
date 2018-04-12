package com.bjypt.vipcard.activity;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.DetailOderAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.DetailOrderBean;
import com.bjypt.vipcard.model.DetailOrderListBean;
import com.bjypt.vipcard.model.DetailOrderRootBean;
import com.bjypt.vipcard.model.LocationDingweiBean;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.BaiduUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单详情
 */
public class DetailOrderActivity extends BaseActivity implements VolleyCallBack, AMapLocationListener {
    private ListView listView;
    private DetailOderAdapter adapter = null;
    private List<DetailOrderListBean> list = new ArrayList<>();
    private View footer;
    private View title;
    private LinearLayout layout_order_detail_back;//返回
    private double lon;
    private double lat;

    /**
     * title 的布局
     */

    private LinearLayout layout_zhifu;//支付
    private ImageView iv_order_detail_photo;//图片
    private TextView tv_order_detail_name;//大娘水饺
    private TextView tv_order_detail_price;//总价
    private TextView tv_vip_order_zhekou;//会员折扣
    private TextView tv_adress;//地址
    private TextView order_detail_taocan;//地址

    private TextView tv_order_phone;//电话
    private RelativeLayout layout_phone;//拨打电话
    private LinearLayout layout_dao_zheli;//到这里
    /**
     * footer 的布局
     */
    private TextView tv_order_hao;//订单号
    private TextView tv_yuyue_time;//预约时间
    private TextView tv_end_time;//结束时间
    private TextView tv_shuliang;//数量
    private TextView tv_shiji_pay_price;//实际支付金额
    private TextView tv_order_youhui_price;//优惠金额
    private TextView tv_detail_youhui;
    private TextView mApplyRefund;
    private TextView mRefuseReason;
    private LinearLayout mRefuseLinear;
    private LinearLayout mRefuseTotal;


    /**
     *
     */
    private String preorder;//：订单主键
    private String url;

    private Map<String, String> map;

    private String phone;
    private String longitude;
    private String latitude;
    private TextView tv_zhifu;
    private int status;
    private String pkmuser;
    private DetailOrderBean detailOrderBean;
    private String createtime = "";
    private String orderTime = "";
    private int earnestmoney;
    private String primaryk;
    private String useRemark;
    private LinearLayout mRemarkLinearv;
    private TextView mRemarkTv;


    /**
     * 开始定位
     */
    public final static int MSG_LOCATION_START = 0;
    /**
     * 定位完成
     */
    public final static int MSG_LOCATION_FINISH = 1;
    /**
     * 停止定位
     */
    public final static int MSG_LOCATION_STOP = 2;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private TextView tv_zhifu_fangshi;
//    private LoadingFragDialog mFragDialog;

    @Override
    public void setContentLayout() {

        setContentView(R.layout.activity_detail_order);
        map = new HashMap<>();
        preorder = getIntent().getStringExtra("preorder");
        Log.i("aaa", "preorder=" + preorder);
        status = getIntent().getIntExtra("status", 0);
        pkmuser = getIntent().getStringExtra("pkmuser");
        createtime = getIntent().getStringExtra("createtime");
        orderTime = getIntent().getStringExtra("ordertime");
        earnestmoney = getIntent().getIntExtra("earnestmoney", 0);
        primaryk = getIntent().getStringExtra("primaryk");
        Log.e("yytt", "Detail primaryk:" + primaryk);
        useRemark = getIntent().getStringExtra("userRemark");
        Log.e("liyuntee", preorder);//df1c391df5274ead85f485615560268b
        footer = View.inflate(this, R.layout.layout_order_detail_footer, null);
        title = View.inflate(this, R.layout.layout_order_detail_title, null);

        Log.e("liyunteee", preorder);
        Wethod.configImageLoader(this);
    }


    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为低功耗模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        // 设置定位监听
        locationClient.setLocationListener(this);

        startLocation();


        listView = (ListView) findViewById(R.id.lv_order_detail);
        listView.addHeaderView(title);
        listView.addFooterView(footer);
        Log.v("TAG", "DetailOrderActivity-------------------initView");

        layout_order_detail_back = (LinearLayout) findViewById(R.id.layout_order_detail_back);
        layout_zhifu = (LinearLayout) findViewById(R.id.layout_zhifu);

        layout_phone = (RelativeLayout) findViewById(R.id.layout_phone);
        layout_dao_zheli = (LinearLayout) findViewById(R.id.layout_dao_zheli);
        iv_order_detail_photo = (ImageView) findViewById(R.id.iv_order_detail_photo);
        tv_order_detail_name = (TextView) findViewById(R.id.tv_order_detail_name);
        tv_zhifu = (TextView) findViewById(R.id.tv_zhifu);
        tv_detail_youhui = (TextView) findViewById(R.id.tv_detail_youhui);
        tv_order_detail_price = (TextView) findViewById(R.id.tv_order_detail_price);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        order_detail_taocan = (TextView) findViewById(R.id.order_detail_taocan);
        tv_vip_order_zhekou = (TextView) findViewById(R.id.tv_vip_order_zhekou);
        tv_adress = (TextView) findViewById(R.id.tv_adress);
        tv_order_phone = (TextView) findViewById(R.id.tv_order_phone);
        tv_yuyue_time = (TextView) findViewById(R.id.tv_yuyue_time);
        tv_order_hao = (TextView) findViewById(R.id.tv_order_hao);
        tv_shuliang = (TextView) findViewById(R.id.tv_shuliang);
        tv_shiji_pay_price = (TextView) findViewById(R.id.tv_shiji_pay_price);
        tv_order_youhui_price = (TextView) findViewById(R.id.tv_order_youhui_price);
        tv_zhifu_fangshi = (TextView) findViewById(R.id.tv_zhifu_fangshi);


        mRemarkLinearv = (LinearLayout) findViewById(R.id.use_remark_linear);
        mRemarkTv = (TextView) findViewById(R.id.tv_useremark);

        if (useRemark.equals("") || useRemark == null) {
            mRemarkLinearv.setVisibility(View.GONE);
        } else {
            mRemarkLinearv.setVisibility(View.VISIBLE);
            mRemarkTv.setText(useRemark);
        }

        tv_yuyue_time.setText(getUserDate(createtime));
        tv_end_time.setText(getUserDate(orderTime));

        mApplyRefund = (TextView) findViewById(R.id.apply_refund);//申请退款按钮
        mRefuseReason = (TextView) findViewById(R.id.refuse_reason);//拒绝退款理由
        mRefuseLinear = (LinearLayout) findViewById(R.id.refuse_linear);
        mRefuseTotal = (LinearLayout) findViewById(R.id.refuse_total);//总得退款布局
        Log.e("sada", "earnestmoney:" + earnestmoney + "  status:" + status);
        if (earnestmoney == 0) {
            mRefuseTotal.setVisibility(View.GONE);
        }

//        mFragDialog = new LoadingFragDialog(this,R.anim.loadingpage,R.style.MyDialog);
    }

    @Override
    public void afterInitView() {
        url = Config.web.detail_order;
        map.put("pkpropre", preorder);
        Wethod.httpPost(DetailOrderActivity.this, 50, url, map, this);
//        mFragDialog.showDialog();

        adapter = new DetailOderAdapter(this, list);
        listView.setAdapter(adapter);
        Log.e("liyunte", "status" + status);
        if (status == 23) {
            tv_zhifu.setText("去评价");
            tv_detail_youhui.setVisibility(View.VISIBLE);
            tv_order_youhui_price.setVisibility(View.VISIBLE);
            mRefuseTotal.setVisibility(View.GONE);
        } else if (status == 22) {
            tv_zhifu.setText("确认支付");
            mRefuseTotal.setVisibility(View.GONE);
        } else if (status == 21) {
            tv_zhifu.setText("去使用");
            mApplyRefund.setText("申请退款");

        } else if (status == 24) {
            tv_zhifu.setText("已评论");
            tv_zhifu.setBackgroundResource(R.mipmap.refuse_bg);
            mRefuseTotal.setVisibility(View.GONE);
        } else if (status == 4) {
            tv_zhifu.setText("已过期");
            tv_zhifu.setBackgroundResource(R.mipmap.refuse_bg);
            mRefuseTotal.setVisibility(View.GONE);
        } else if (status == 9) {
            //退款中
            layout_zhifu.setEnabled(false);
            tv_zhifu.setText("退款中");
            tv_zhifu.setBackgroundResource(R.mipmap.refuse_bg);
            mRefuseTotal.setVisibility(View.GONE);

        } else if (status == 10) {
            //退款成功
            layout_zhifu.setEnabled(false);
            tv_zhifu.setText("退款成功");
            tv_zhifu.setBackgroundResource(R.mipmap.refuse_bg);
            mRefuseTotal.setVisibility(View.GONE);

        } else if (status == 11) {
            //拒绝退款
            tv_zhifu.setText("去使用");
            mApplyRefund.setText("拒绝退款");
            mApplyRefund.setBackgroundResource(R.mipmap.refuse_bg);
            mApplyRefund.setEnabled(false);
            mRefuseLinear.setVisibility(View.VISIBLE);
        } else if (status == 31) {
            layout_zhifu.setEnabled(false);
            tv_zhifu.setText("支付中");
            mRefuseTotal.setVisibility(View.GONE);
        } else if (status == 32) {
            layout_zhifu.setEnabled(false);
            tv_zhifu.setText("尾款支付中");
            mRefuseTotal.setVisibility(View.GONE);
        }


    }

    @Override
    public void bindListener() {
        layout_order_detail_back.setOnClickListener(this);

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            /**
             * 返回
             */
            case R.id.layout_order_detail_back:
                finish();
                break;

            /**
             * 确认支付
             */
            case R.id.layout_zhifu:
                if (status == 23) {
                    // tv_zhifu.setText("去评价");
                    Intent intent = new Intent(DetailOrderActivity.this, IssueCommentActivity.class);
                    intent.putExtra("preorderId", preorder);
                    intent.putExtra("pkmuser", pkmuser);
                    startActivity(intent);

                    Log.i("aaa", ">preorder" + preorder);
                    Log.i("aaa", ">pkmuser" + pkmuser);
                    finish();
                } else if (status == 22) {
                    // tv_zhifu.setText("确认预约");
                    Intent intent_queren = new Intent(DetailOrderActivity.this, PayOneActivity.class);
                    intent_queren.putExtra("orderId", detailOrderBean.getOrderNo());
                    intent_queren.putExtra("pkmuser", detailOrderBean.getPkmuser());
                    intent_queren.putExtra("preorderId", preorder);
                    intent_queren.putExtra("primaryk", primaryk);
                    intent_queren.putExtra("FLAG", "Y");
                    startActivity(intent_queren);
                    finish();

                } else if (status == 21 || status == 11) {
                    //  tv_zhifu.setText("去使用");
                    Intent intent_queren = new Intent(DetailOrderActivity.this, PayOneActivity.class);
                    intent_queren.putExtra("orderId", detailOrderBean.getOrderNo());
                    intent_queren.putExtra("pkmuser", detailOrderBean.getPkmuser());
                    intent_queren.putExtra("primaryk", primaryk);
                    intent_queren.putExtra("preorderId", preorder);
                    intent_queren.putExtra("FLAG", "N");
                    startActivity(intent_queren);
                    finish();
                } else {
                    //  tv_zhifu.setText("已过期");
                }


                break;
            /**
             * 电话
             */
            case R.id.layout_phone:
                //android:name="android.permission.CALL_PHONE">
                requestPrommession();
                break;
            /**
             * 定位
             */
            case R.id.layout_dao_zheli:
                double mLat1 = lat;
                double mLon1 = lon;
                // 百度大厦坐标
                double mLat2 = Double.parseDouble(latitude);
                double mLon2 = Double.parseDouble(longitude);
                BaiduUtil.Daozheli(DetailOrderActivity.this, mLat1, mLon1, mLat2, mLon2, getFromSharePreference(Config.userConfig.adress), mudi);
                break;

            case R.id.apply_refund:
                //申请退款
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailOrderActivity.this);
                builder.setMessage("确认退款吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, String> params = new HashMap<>();
                        params.put("preorderId", preorder);
                        Wethod.httpPost(DetailOrderActivity.this, 10086, Config.web.refund_apply, params, DetailOrderActivity.this);
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


                break;
        }

    }

    public void requestPrommession() {
        if (Build.VERSION.SDK_INT >= 23) {

            //判断是否有权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                        1);
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CALL_PHONE)) {
                    Toast.makeText(this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
                }
            } else {
                Intent telIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                startActivity(telIntent);
            }
        } else {
            Intent telIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(telIntent);
        }
    }


    //    @Override
//    public void setLocation(double latitude, double longitude, String cityCode, String city, String address) {
//                         lon = longitude;
//                             lat = latitude;
//    }
    private String mudi;

    @Override
    public void onSuccess(int reqcode, Object result) {


        layout_zhifu.setOnClickListener(this);
        layout_phone.setOnClickListener(this);
        layout_dao_zheli.setOnClickListener(this);
        mApplyRefund.setOnClickListener(this);

        if (reqcode == 50) {
            Log.e("GGGGG", result.toString());
            try {
                Log.e("GGGG", "-----------------");
                DetailOrderRootBean rootBean = getConfiguration().readValue(result.toString(), DetailOrderRootBean.class);
                detailOrderBean = rootBean.getResultData();
                if (rootBean.getResultData().getHybProductList().size() > 0) {
                    list.addAll(rootBean.getResultData().getHybProductList());
                }
                tv_order_detail_name.setText(rootBean.getResultData().getMuname());
                tv_order_detail_price.setText(rootBean.getResultData().getProductTolprice() + "元");
                tv_vip_order_zhekou.setText(rootBean.getResultData().getDiscount() + "折");
                //tv_zhifu_fangshi.setText("");
                if (!rootBean.getResultData().getDesc_payment_return().equals("") && rootBean.getResultData().getDesc_payment_return() != null) {
                    tv_zhifu_fangshi.setText(rootBean.getResultData().getDesc_payment_return());
                }
                ImageLoader.getInstance().displayImage(Config.web.picUrl + rootBean.getResultData().getLogo(), iv_order_detail_photo);
                tv_adress.setText(rootBean.getResultData().getAddress());
                mudi = rootBean.getResultData().getAddress();
                if (!"".equals(rootBean.getResultData().getPhone())) {
                    phone = rootBean.getResultData().getPhone();
                    tv_order_phone.setText(rootBean.getResultData().getPhone());
                }

                tv_order_hao.setText(rootBean.getResultData().getOrderNo());
               /* if (rootBean.getResultData().getOrderTime()!=null&&!"".equals(rootBean.getResultData().getOrderTime())){
                    tv_yuyue_time.setText( getUserDate(rootBean.getResultData().getOrderTime()+"～"));
                }*/
                tv_shuliang.setText(rootBean.getResultData().getOrderCount());
                tv_shiji_pay_price.setText(rootBean.getResultData().getProductTolprices() + "");
                if (rootBean.getResultData().getRefundreason().equals("") || rootBean.getResultData().getRefundreason() == null) {

                } else {
                    mRefuseReason.setText(rootBean.getResultData().getRefundreason());
                }

                tv_order_youhui_price.setText(rootBean.getResultData().getDiscountNum() + "");
                latitude = rootBean.getResultData().getLatitude();
                longitude = rootBean.getResultData().getLongitude();
                pkmuser = rootBean.getResultData().getPkmuser();

            } catch (IOException e) {
                Log.e("GGGG", e.toString());
                e.printStackTrace();

            }
        } else if (reqcode == 10086) {
            Wethod.ToFailMsg(this, result);
            finish();
        }
//        mFragDialog.cancelDialog();
    }


    public static String getUserDate(String sformat) {
        if (!"".equals(sformat)) {
            Date currentTime = new Date();
            currentTime.setTime(Long.parseLong(sformat));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(currentTime);
            return dateString;
        } else {
            return "";
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }

    /*在启动当前APP的时候启动定位功能*/
    private void startLocation() {
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


    Handler locationHandler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_LOCATION_START:
//                    tvReult.setText("正在定位...");
                    break;
                //定位完成
                case MSG_LOCATION_FINISH:
                    AMapLocation loc = (AMapLocation) msg.obj;
                    LocationDingweiBean ld = getLocationStr(loc);
                    lat = ld.getmLat();
                    lon = ld.getmLng();
                    if (ld.getCityCode() != null || !ld.getCityCode().equals("0")) {
                        locationClient.stopLocation();
                        locationHandler.sendEmptyMessage(MSG_LOCATION_STOP);
                    }
                    break;
                case MSG_LOCATION_STOP:
//                    tvReult.setText("定位停止");
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 根据定位结果返回定位信息的字符串
     *
     * @return
     */
    private LocationDingweiBean getLocationStr(AMapLocation location) {

        LocationDingweiBean locationDingweiBean = new LocationDingweiBean();

        if (null == location) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if (location.getErrorCode() == 0) {
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

}
