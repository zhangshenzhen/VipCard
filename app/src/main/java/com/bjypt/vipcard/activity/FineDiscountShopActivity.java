package com.bjypt.vipcard.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.FineDiscountMerchantAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.h5.WebViewPushInfo;
import com.bjypt.vipcard.model.BannerResultdataBean;
import com.bjypt.vipcard.model.FineMerchantBean;
import com.bjypt.vipcard.model.HalfbalanceBean;
import com.bjypt.vipcard.model.ListModelBean;
import com.bjypt.vipcard.model.Notice;
import com.bjypt.vipcard.model.ObjectModelBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.ViewPagerManager;
import com.bjypt.vipcard.view.categoryview.AppCategoryHomeBannerView;
import com.brioal.adtextviewlib.entity.AdEntity;
import com.brioal.adtextviewlib.view.ADTextView;
import com.lidroid.xutils.util.LogUtils;
import com.sinia.orderlang.utils.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/2.
 */

public class FineDiscountShopActivity extends BaseActivity implements VolleyCallBack<String> {

    String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    ObjectModelBean<HalfbalanceBean> halfbalanceBean; //余额
    private PullToRefreshListView fine_discount_listvew;
    private FineDiscountMerchantAdapter fineDiscountMerchantAdapter;
    private ListView pullListView;
    private View mHeadView;
    private List<FineMerchantBean> listmerchant = new ArrayList<FineMerchantBean>();

    /*广告轮播  start*/
//    private ViewPager viewpager_new_home;//广告轮播
//    private LinearLayout linear_adv_new_home;//广告轮播小圆点的布局
//    private ViewPagerManager adv_new_home;//广告轮播管理
//    private ArrayList<String> adv_list = new ArrayList<>();

    AppCategoryHomeBannerView fine_discount_appCategoryHomeBannerView;

    private ADTextView vsts_notice;

//    private static final int request_code_banner = 1;
    private static final int request_code_notice = 2;
    private static final int request_code_merchant_list = 3;
    private static final int request_code_balance = 4; //商家余额

    private TextView tv_fine_mony;
    private Button btn_recharge;
    private Button btn_pay_history;
    private Button btn_use_rule;

    private List<DayViewHolder> dayViewHolderArrayList = new ArrayList<DayViewHolder>();//日期
    private int currentDayIndex = 0;
    private List<PeriodViewHolder> periodViewHolderList = new ArrayList<PeriodViewHolder>(); //上午下午中午
    private PeriodViewEntity periodViewEntity = new PeriodViewEntity(); //上午下午中午 选中状态
    List<DayViewEntity> dayViewEntityList = new ArrayList<DayViewEntity>();
    private Calendar calendar = Calendar.getInstance();

    private int currentPage;
    private int pageLength = 10;
    private int isRefresh;
    final int QUERY_EXERCISE_MORE = 0x0101;
    final int QUERY_EXERCISE_REFERSH = 0x0110;

    private EditText search_new_home;
    private String lat;
    private String lng;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_fine_discount_shop);
        lat = SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.latu);
        lng = SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.lngu);
    }

    @Override
    public void beforeInitView() {
//        TextView tv_title = (TextView) findViewById(R.id.tv_title);
//        tv_title.setText(getIntent().getStringExtra("title"));
        LinearLayout layout_back = (LinearLayout) findViewById(R.id.layout_back);
        layout_back.setOnClickListener(this);

        search_new_home = (EditText)findViewById(R.id.search_new_home);
        search_new_home.setOnClickListener(this);


        fine_discount_listvew = (PullToRefreshListView) findViewById(R.id.fine_discount_listvew);
        fine_discount_listvew.setMode(PullToRefreshBase.Mode.BOTH);
        pullListView = fine_discount_listvew.getRefreshableView();
        pullListView.setDividerHeight(0);

        mHeadView = LayoutInflater.from(this).inflate(R.layout.head_view_fine_discount, null);
        pullListView.addHeaderView(mHeadView);
//        viewpager_new_home = (ViewPager) mHeadView.findViewById(R.id.viewpager_new_home);
//        linear_adv_new_home = (LinearLayout) mHeadView.findViewById(R.id.linear_adv_new_home);
        fine_discount_appCategoryHomeBannerView = (AppCategoryHomeBannerView)mHeadView.findViewById(R.id.fine_discount_appCategoryHomeBannerView);
        tv_fine_mony = (TextView) mHeadView.findViewById(R.id.tv_fine_mony);
        vsts_notice = (ADTextView) mHeadView.findViewById(R.id.vsts_notice);
        btn_recharge = (Button) findViewById(R.id.btn_recharge);
        btn_pay_history = (Button) findViewById(R.id.btn_pay_history);
        btn_use_rule = (Button)findViewById(R.id.btn_use_rule);

        /** 头条速度和样式 */
        vsts_notice.setSpeed(1);
        vsts_notice.setFrontColor(Color.BLACK);
        vsts_notice.setBackColor(Color.BLACK);
    }

    @Override
    public void initView() {
        fineDiscountMerchantAdapter = new FineDiscountMerchantAdapter(listmerchant, this);
        pullListView.setAdapter(fineDiscountMerchantAdapter);

        initDayView();
        initTimeView();

    }

    private void initTimeView() {

    }


    private void initDayView() {

        getDayViewEntity();

        DayViewHolder dayViewHolder1 = new DayViewHolder();
        dayViewHolder1.tv_day = (TextView) mHeadView.findViewById(R.id.tv_date_12);
        dayViewHolder1.tv_weekday = (TextView) mHeadView.findViewById(R.id.tv_date_11);
        dayViewHolder1.linear_day = mHeadView.findViewById(R.id.linear_date_1);
        dayViewHolder1.linear_day.setOnClickListener(this);
        dayViewHolderArrayList.add(dayViewHolder1);
        dayViewHolder1.tv_weekday.setText(getMonthText());

        DayViewHolder dayViewHolder2 = new DayViewHolder();
        dayViewHolder2.tv_day = (TextView) mHeadView.findViewById(R.id.tv_date_22);
        dayViewHolder2.tv_weekday = (TextView) mHeadView.findViewById(R.id.tv_date_21);
        dayViewHolder2.linear_day = mHeadView.findViewById(R.id.linear_date_2);
        dayViewHolder2.linear_day.setOnClickListener(this);
        dayViewHolderArrayList.add(dayViewHolder2);


        DayViewHolder dayViewHolder3 = new DayViewHolder();
        dayViewHolder3.tv_day = (TextView) mHeadView.findViewById(R.id.tv_date_32);
        dayViewHolder3.tv_weekday = (TextView) mHeadView.findViewById(R.id.tv_date_31);
        dayViewHolder3.linear_day = mHeadView.findViewById(R.id.linear_date_3);
        dayViewHolder3.linear_day.setOnClickListener(this);
        dayViewHolderArrayList.add(dayViewHolder3);

        DayViewHolder dayViewHolder4 = new DayViewHolder();
        dayViewHolder4.tv_day = (TextView) mHeadView.findViewById(R.id.tv_date_42);
        dayViewHolder4.tv_weekday = (TextView) mHeadView.findViewById(R.id.tv_date_41);
        dayViewHolder4.linear_day = mHeadView.findViewById(R.id.linear_date_4);
        dayViewHolder4.linear_day.setOnClickListener(this);
        dayViewHolderArrayList.add(dayViewHolder4);

        DayViewHolder dayViewHolder5 = new DayViewHolder();
        dayViewHolder5.tv_day = (TextView) mHeadView.findViewById(R.id.tv_date_52);
        dayViewHolder5.tv_weekday = (TextView) mHeadView.findViewById(R.id.tv_date_51);
        dayViewHolder5.linear_day = mHeadView.findViewById(R.id.linear_date_5);
        dayViewHolder5.linear_day.setOnClickListener(this);
        dayViewHolderArrayList.add(dayViewHolder5);

        DayViewHolder dayViewHolder6 = new DayViewHolder();
        dayViewHolder6.tv_weekday = (TextView) mHeadView.findViewById(R.id.tv_date_61);
        dayViewHolder6.tv_day = (TextView) mHeadView.findViewById(R.id.tv_date_62);
        dayViewHolder6.linear_day = mHeadView.findViewById(R.id.linear_date_6);
        dayViewHolder6.linear_day.setOnClickListener(this);
        mHeadView.findViewById(R.id.iv_61).setOnClickListener(this);//日期

//        dayViewHolder6.linear_day.setOnClickListener(this);
        dayViewHolderArrayList.add(dayViewHolder6);

        refreshDayView();

        selectDay(0);

        PeriodViewHolder periodViewHolder1 = new PeriodViewHolder();
        periodViewHolder1.relativeLayout = (RelativeLayout) mHeadView.findViewById(R.id.relat_zaoshang);
        periodViewHolder1.relativeLayout.setOnClickListener(this);
        periodViewHolder1.tv_period = (TextView) mHeadView.findViewById(R.id.tv_zaoshang);
        periodViewHolder1.view_line = mHeadView.findViewById(R.id.v_zaoshang_status_bg);
        periodViewHolder1.value = 2; //1 全天 2 早上 3 中午 4 下午
        periodViewHolderList.add(periodViewHolder1);

        PeriodViewHolder periodViewHolder2 = new PeriodViewHolder();
        periodViewHolder2.relativeLayout = (RelativeLayout) mHeadView.findViewById(R.id.relat_zhongwu);
        periodViewHolder2.relativeLayout.setOnClickListener(this);
        periodViewHolder2.tv_period = (TextView) mHeadView.findViewById(R.id.tv_zhongwu);
        periodViewHolder2.view_line = mHeadView.findViewById(R.id.view_zhongwu_status_bg);
        periodViewHolder2.value = 3;
        periodViewHolderList.add(periodViewHolder2);

        PeriodViewHolder periodViewHolder3 = new PeriodViewHolder();
        periodViewHolder3.relativeLayout = (RelativeLayout) mHeadView.findViewById(R.id.relat_xiawu);
        periodViewHolder3.relativeLayout.setOnClickListener(this);
        periodViewHolder3.tv_period = (TextView) mHeadView.findViewById(R.id.tv_xiawu);
        periodViewHolder3.view_line = mHeadView.findViewById(R.id.view_xiawu_status_bg);
        periodViewHolder3.value = 4;
        periodViewHolderList.add(periodViewHolder3);

        periodViewEntity.check_value = 1; //1 全天 2 早上 3 中午 4 下午

    }

    private void refreshDayView() {
        for (int i = 1; i < dayViewHolderArrayList.size(); i++) {
            dayViewHolderArrayList.get(i).tv_weekday.setText(dayViewEntityList.get(i).week_text);
            dayViewHolderArrayList.get(i).tv_day.setText(dayViewEntityList.get(i).day_text);
        }
    }


    private void selectPeriod(int index) {
        for (int i = 0; i < periodViewHolderList.size(); i++) {
            if (i == index) {
                if (periodViewEntity.check_value == periodViewHolderList.get(i).value) {
                    periodViewEntity.check_value = 1;
                    periodViewHolderList.get(i).tv_period.setTextColor(getResources().getColor(R.color.fine_text_color));
                    periodViewHolderList.get(i).view_line.setBackgroundColor(getResources().getColor(R.color.fine_gray_line));
                } else {
                    periodViewEntity.is_check = true;
                    periodViewEntity.check_value = periodViewHolderList.get(i).value;
                    periodViewHolderList.get(i).tv_period.setTextColor(getResources().getColor(R.color.fine_red_bg));
                    periodViewHolderList.get(i).view_line.setBackgroundColor(getResources().getColor(R.color.fine_red_bg));
                }
            } else {
                periodViewHolderList.get(i).tv_period.setTextColor(getResources().getColor(R.color.fine_text_color));
                periodViewHolderList.get(i).view_line.setBackgroundColor(getResources().getColor(R.color.fine_gray_line));
            }
        }
        getHttpMerchantList(QUERY_EXERCISE_REFERSH);
    }

    private void selectDay(int index) {
        for (int i = 0; i < dayViewHolderArrayList.size(); i++) {
            if (index == i) {
                currentDayIndex = index;
                dayViewHolderArrayList.get(i).linear_day.setBackgroundColor(getResources().getColor(R.color.fine_red_bg));
                if (dayViewHolderArrayList.get(i).tv_weekday != null) {
                    dayViewHolderArrayList.get(i).tv_weekday.setTextColor(getResources().getColor(R.color.bg_white));
                }
                dayViewHolderArrayList.get(i).tv_day.setTextColor(getResources().getColor(R.color.bg_white));
            } else {
                dayViewHolderArrayList.get(i).linear_day.setBackgroundColor(getResources().getColor(R.color.bg_white));
                if (dayViewHolderArrayList.get(i).tv_weekday != null) {
                    dayViewHolderArrayList.get(i).tv_weekday.setTextColor(getResources().getColor(R.color.black_color));
                }
                dayViewHolderArrayList.get(i).tv_day.setTextColor(getResources().getColor(R.color.black_color));
            }
        }
        initPeriodView();//初始化全天的
        getHttpMerchantList(QUERY_EXERCISE_REFERSH);
    }

    private void initPeriodView() {
        for (int i = 0; i < periodViewHolderList.size(); i++) {
            periodViewHolderList.get(i).tv_period.setTextColor(getResources().getColor(R.color.fine_text_color));
            periodViewHolderList.get(i).view_line.setBackgroundColor(getResources().getColor(R.color.fine_gray_line));
        }
        periodViewEntity.check_value = 1;
    }

    @Override
    protected void onResume() {
        getHttpHalfBalance();
        super.onResume();
    }

    @Override
    public void afterInitView() {
        getHttpBanners();
        getHttpNotice();
//        getHttpHalfBalance();
        getHttpMerchantList(QUERY_EXERCISE_REFERSH);
    }

    @Override
    public void bindListener() {
        btn_recharge.setOnClickListener(this);
        btn_pay_history.setOnClickListener(this);
        btn_use_rule.setOnClickListener(this);

        fine_discount_listvew.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getHttpBanners();
                getHttpHalfBalance();
                getHttpNotice();
                getHttpMerchantList(QUERY_EXERCISE_REFERSH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getHttpMerchantList(QUERY_EXERCISE_MORE);
            }
        });

        fine_discount_listvew.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(FineDiscountShopActivity.this, NewSubscribeDishesActivity.class);
                position = position - 2;
                intent.putExtra("distance", listmerchant.get(position).getDistance());
                intent.putExtra("rechargeactivity", "");
                intent.putExtra("is_half", "Y");
                intent.putExtra("pkmuser", listmerchant.get(position).getPkmuser());
                startActivity(intent);
            }
        });
    }

    /**
     * 点击时间控件，更新UI
     */
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {  //
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            calendar.set(Calendar.YEAR, arg1);
            calendar.set(Calendar.MONTH, arg2);
            calendar.set(Calendar.DAY_OF_MONTH, arg3);
            String value = dateToStrLong(calendar.getTime());
            dayViewEntityList.get(5).value = value;
            dayViewEntityList.get(5).day_text = arg3 + "";
            int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0)
                w = 0;
            dayViewEntityList.get(5).week_text = weekDays[w];
            refreshDayView();
            selectDay(5);
        }
    };

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.linear_date_1:
                selectDay(0);
                break;
            case R.id.linear_date_2:
                selectDay(1);
                break;
            case R.id.linear_date_3:
                selectDay(2);
                break;
            case R.id.linear_date_4:
                selectDay(3);
                break;
            case R.id.linear_date_5:
                selectDay(4);
                break;
//            case R.id.linear_date_6:
//                selectDay(5);
//                break;
            case R.id.relat_zaoshang:
                selectPeriod(0);
                break;
            case R.id.relat_zhongwu:
                selectPeriod(1);
                break;
            case R.id.relat_xiawu:
                selectPeriod(2);
                break;
            case R.id.btn_recharge:
                Intent intent = new Intent(this, RightAwayOnLineNewActivity.class);
                intent.putExtra("platbalance", halfbalanceBean.getResultData().getPlatbalance());
                startActivity(intent);
                break;
            // 五折店支付页面
            case R.id.btn_pay_history:
                Intent h51 = new Intent(FineDiscountShopActivity.this, LifeServireH5Activity.class);
                h51.putExtra("serviceName", "使用记录");
                h51.putExtra("life_url", Config.web.fine_discount_pay_history + "?pkregister=" );
                h51.putExtra("isLogin", "Y");
                h51.putExtra("isallurl", "Y");
                startActivity(h51);
                break;
            case R.id.linear_date_6:
            case R.id.iv_61://日期控件
                DatePickerDialog dialog = new DatePickerDialog(this, R.style.DateTheme,
                        listener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                dialog.show();
                break;
            case R.id.btn_use_rule:
                Intent h52 = new Intent(FineDiscountShopActivity.this, LifeServireH5Activity.class);
                h52.putExtra("serviceName", "使用规则");
                h52.putExtra("life_url", Config.web.fine_discount_amount_rule +"?pkregister=");
                h52.putExtra("isallurl", "Y");
                h52.putExtra("isLogin", "Y");
                startActivity(h52);
                break;
            case R.id.search_new_home:
                Intent intentSearch = new Intent(FineDiscountShopActivity.this, FineDiscountSearchMerchantListActivity.class);
                startActivity(intentSearch);
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        LogUtils.d("result" + reqcode + "==" + result);
        switch (reqcode) {
//            case request_code_banner:
//                analyze_result_banner(result);
//                break;
            case request_code_notice:
                analyze_result_notice(result);
                break;
            case request_code_balance:
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                try {
                    halfbalanceBean = objectMapper.readValue(result.toString(), new TypeReference<ObjectModelBean<HalfbalanceBean>>() {
                    });
                    tv_fine_mony.setText(halfbalanceBean.getResultData().getHalfbalance() + " 元");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case request_code_merchant_list:
//                Log.e("resultlist","==================="+result+"==================");
                analyze_result_merchant(result);
                break;
        }
    }

    private void analyze_result_merchant(String result) {
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            ListModelBean<FineMerchantBean> listModelBean = null;
            listModelBean = objectMapper.readValue(result.toString(), new TypeReference<ListModelBean<FineMerchantBean>>() {
            });
            if (listModelBean.getResultData().size() > 0) {
                if (isRefresh == 1) {
                    listmerchant.clear();
                }
                listmerchant.addAll(listModelBean.getResultData());
                fineDiscountMerchantAdapter.notifyDataSetChanged();
                fine_discount_listvew.onRefreshComplete();
            } else if (listModelBean.getResultData().size() == 0 && isRefresh == 1) {
                listmerchant.clear();
                fineDiscountMerchantAdapter.notifyDataSetChanged();
                fine_discount_listvew.onRefreshComplete();
            } else {
                fine_discount_listvew.onRefreshComplete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析banner 数据
     *
     * @param result
     */
//    private void analyze_result_banner(String result) {
//        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
//        try {
//            if (adv_list.size() > 0) {
//                adv_list.clear();
//            }
//            final BannerResultdataBean bannerResultdataBean = objectMapper.readValue(result.toString(), BannerResultdataBean.class);
//            for (int i = 0; i < bannerResultdataBean.getResultData().size(); i++) {
//                adv_list.add(Config.web.ImgURL + bannerResultdataBean.getResultData().get(i).getAdurl());
//            }
//            adv_new_home = new ViewPagerManager(this, adv_list, linear_adv_new_home, viewpager_new_home);
//            adv_new_home.setOnLunBoItemClickListener(new ViewPagerManager.LunBoItemClickListener() {
//                @Override
//                public void onLunBoItemClickListener(int position) {
//                    if (StringUtil.isNotEmpty(bannerResultdataBean.getResultData().get(position).getLinkurl())) {
//                        Intent intent = new Intent(FineDiscountShopActivity.this, CommonWebViewActivity.class);
//                        WebViewPushInfo webViewPushInfo = new WebViewPushInfo();
//                        webViewPushInfo.setUrl(bannerResultdataBean.getResultData().get(position).getLinkurl() + getFromSharePreference(Config.userConfig.pkregister));
//                        webViewPushInfo.setTitle(bannerResultdataBean.getResultData().get(position).getAddesc());
//                        intent.putExtra(BaseWebViewActivity.PUSHINFO, webViewPushInfo);
//                        startActivity(intent);
//                    }
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void analyze_result_notice(String result) {
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            ListModelBean<Notice> noticeListModelBean = objectMapper.readValue(result, new TypeReference<ListModelBean<Notice>>() {
            });
            ArrayList<AdEntity> texts = new ArrayList<AdEntity>();
            for (int i = 0; i < noticeListModelBean.getResultData().size(); i++) {
                texts.add(new AdEntity(noticeListModelBean.getResultData().get(i).getSlogan(), "", ""));
            }
            if(texts.size() >0){
                vsts_notice.setTexts(texts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onFailed(int reqcode, String result) {
//        if(StringUtil.isNotEmpty(result)){
//            Wethod.ToFailMsg(this, result);
//        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }


    /**
     * 获取banner
     */
    private void getHttpBanners() {
        fine_discount_appCategoryHomeBannerView.reload();
//        Wethod.httpGet(this, request_code_banner, Config.web.fine_discount_get_banner + "?adtype=6", this);
    }

    /**
     * 获取通知
     */
    private void getHttpNotice() {
        Wethod.httpGet(this, request_code_notice, Config.web.fine_discount_get_notice + "?adtype=7", this);
    }

    /**
     * 获取五折店余额
     */
    private void getHttpHalfBalance() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(this, request_code_balance, Config.web.fine_discount_get_balance, params, this);
    }

    /**
     * 5.14	【五折店】五折店商家列表
     */
    private void getHttpMerchantList(int type) {
        Map<String, String> params = new HashMap<String, String>();
        if (type == QUERY_EXERCISE_REFERSH) {
            isRefresh = 1;
            currentPage = 0;
        } else {
            isRefresh = 2;
            currentPage += 10;
        }
        params.put("queryDate", dayViewEntityList.get(currentDayIndex).value);
        params.put("timeType", periodViewEntity.check_value + ""); //1 全天 2 早上 3 中午 4 下午
        params.put("cityCode", getFromSharePreference(Config.userConfig.citycode));
        params.put("begin", currentPage + "");
        params.put("pageLength", pageLength + "");
        params.put("queryName", "");
        params.put("lat",lat);
        params.put("lng",lng);
        Wethod.httpPost(this, request_code_merchant_list, Config.web.fine_discount_get_merchant, params, this);
    }

    /**
     * 日期
     */
    class DayViewHolder {
        TextView tv_weekday;
        TextView tv_day;
        View linear_day;
    }

    /**
     * 时间段
     */
    class PeriodViewHolder {
        private RelativeLayout relativeLayout;
        private TextView tv_period;
        private View view_line;
        private int value;
    }

    class PeriodViewEntity {
        int check_value;
        boolean is_check;
    }

    class DayViewEntity {
        String day_text;
        String week_text;
        String value;
    }

    private List<DayViewEntity> getDayViewEntity() {
        for (int i = 0; i < 6; i++) {

            DayViewEntity dayViewEntity = new DayViewEntity();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, i);
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0)
                w = 0;
            dayViewEntity.week_text = weekDays[w];
            dayViewEntity.day_text = cal.get(Calendar.DAY_OF_MONTH) + "";
            dayViewEntity.value = dateToStrLong(cal.getTime());
            dayViewEntityList.add(dayViewEntity);
        }
        return dayViewEntityList;
    }

    public static String dateToStrLong(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    private String getMonthText() {
        String[] weekDays = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
        Calendar cal = Calendar.getInstance();
        return weekDays[cal.get(Calendar.MONTH)];
    }

}
