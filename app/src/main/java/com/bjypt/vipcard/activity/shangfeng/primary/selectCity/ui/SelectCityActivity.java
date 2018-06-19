package com.bjypt.vipcard.activity.shangfeng.primary.selectCity.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.adapter.CityListAdapter;
import com.bjypt.vipcard.activity.shangfeng.adapter.SearchCityAdapter;
import com.bjypt.vipcard.activity.shangfeng.base.BaseActivity;
import com.bjypt.vipcard.activity.shangfeng.common.enums.LocateState;
import com.bjypt.vipcard.activity.shangfeng.data.bean.CityBean;
import com.bjypt.vipcard.activity.shangfeng.primary.selectCity.contract.SelectCityContract;
import com.bjypt.vipcard.activity.shangfeng.primary.selectCity.contract.impl.SelectCityPresenterImpl;
import com.bjypt.vipcard.activity.shangfeng.util.StringUtils;
import com.bjypt.vipcard.activity.shangfeng.widget.SideLetterBar;
import com.bjypt.vipcard.base.MyApplication;
import com.githang.statusbar.StatusBarCompat;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 城市列表界面
 */
public class SelectCityActivity extends BaseActivity implements SelectCityContract.View, SwipeRefreshLayout.OnRefreshListener, AMapLocationListener, AdapterView.OnItemClickListener {
    /**
     * 取消
     */
    @BindView(R.id.btn_cancal)
    TextView btn_cancal;
    /**
     * 搜索框
     */
    @BindView(R.id.et_search_content)
    EditText et_search_content;

    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srl_refresh;
    /**
     * 城市列表
     */
    @BindView(R.id.lv_list)
    ListView lv_list;

    @BindView(R.id.tv_letter_overlay)
    TextView tv_letter_overlay;
    /**
     * 快速索引
     */
    @BindView(R.id.side_letter_bar)
    SideLetterBar side_letter_bar;
    /**
     * 搜索结果列表
     */
    @BindView(R.id.listview_search_result)
    ListView listview_search_result;

    private CityListAdapter cityListAdapter;
    private AMapLocationClient mLocationClient;
    private SelectCityPresenterImpl selectCityPresenter;

    private List<CityBean> cityBeanList;
    private List<CityBean> searchCityBeans;

    private SearchCityAdapter searchCityAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_city;

    }

    @Override
    protected void initInjector() {
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.app_theme_color));

        selectCityPresenter = new SelectCityPresenterImpl();
        //让presenter保持view接口的引用
        selectCityPresenter.attachView(this);
        //让baseactivity自动执行oncreate 以及 在activitydestroy时能及时释放subscribe
        superPresenter = selectCityPresenter;

    }

    @Override
    protected void init() {

        srl_refresh.setOnRefreshListener(this);
        srl_refresh.setColorSchemeResources(R.color.app_theme_color);
        srl_refresh.setDistanceToTriggerSync(500);
        srl_refresh.setProgressBackgroundColor(R.color.white);
        srl_refresh.setSize(SwipeRefreshLayout.DEFAULT);

        selectCityPresenter.loadData(this);

        initPosition();
        initLocation();

        et_search_content.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString();
                Logger.v(searchText);
                if (StringUtils.isEmpty(searchText)) {
                    if (listview_search_result.getVisibility() == View.VISIBLE) {
                        listview_search_result.setVisibility(View.GONE);
                        searchCityAdapter = null;
                    }
                    return;
                }
                if (cityBeanList != null && cityBeanList.size() > 0) {
                    if(searchCityBeans != null){
                        searchCityBeans.clear();
                        searchCityBeans = null;
                    }
                    for (CityBean cityBean : cityBeanList) {
                        if (cityBean.getCityname().contains(searchText)) {
                            if (searchCityBeans == null) {
                                searchCityBeans = new ArrayList<>();
                            }
                            searchCityBeans.add(cityBean);
                        }
                    }

                    listview_search_result.setVisibility(View.VISIBLE);
                    if (searchCityAdapter == null) {
                        searchCityAdapter = new SearchCityAdapter(SelectCityActivity.this, searchCityBeans);
                        listview_search_result.setAdapter(searchCityAdapter);
                        listview_search_result.setOnItemClickListener(SelectCityActivity.this);
                    } else {
                        searchCityAdapter.updateData(searchCityBeans);
                    }
                }
            }
        });

    }

    @OnClick(R.id.btn_cancal)
    public void finishActivity() {
        if (listview_search_result.getVisibility() == View.VISIBLE) {
            listview_search_result.setVisibility(View.GONE);
            searchCityAdapter = null;
            et_search_content.setText("");
        } else {
            back(null);
        }
    }


    @Override
    public void showProgress() {
        srl_refresh.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        srl_refresh.setRefreshing(false);
    }

    /**
     * 初始化城市列表
     *
     * @param cityBeans
     */
    @Override
    public void setCityList(List<CityBean> cityBeans) {
        initCityList(cityBeans);
        cityBeanList = cityBeans;
    }


    private void initCityList(List<CityBean> cityList) {
        cityListAdapter = new CityListAdapter(cityList, SelectCityActivity.this);
        lv_list.setAdapter(cityListAdapter);
        cityListAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(CityBean cityBean) {
//                ToastUtils.showToast(cityBean.getCityname());
                back(cityBean);
            }

            @Override
            public void onLocateClick() {
                cityListAdapter.updateLocateState(LocateState.LOCATING, null);
                initLocation();
            }
        });
        initLocation();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        back(searchCityAdapter.getItem(position));
    }

    /**
     * 返回键跳转
     *
     * @param cityBean
     */
    private void back(CityBean cityBean) {
        Intent data = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("picked_city", cityBean);
        data.putExtras(bundle);
        setResult(0, data);
        finish();
    }

    @Override
    public void onBackPressed() {
        back(null);
    }

    /**
     * 初始化索引
     */
    private void initPosition() {
        side_letter_bar.setOverlay(tv_letter_overlay);
        side_letter_bar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                Logger.i(letter);
                if (cityListAdapter != null) {
                    int position = cityListAdapter.getCityListPosition(letter);
                    lv_list.setSelection(position);
                }
            }
        });
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        MyApplication.getInstance().getMapLocationUtil().satrtMapLocation(this);

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null != aMapLocation) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                //城市信息
                String city = aMapLocation.getCity();
                CityBean locationCity = new CityBean(
                        city.replace("市", "")
                        , null
                        , aMapLocation.getCityCode()
                        , aMapLocation.getStreet() + aMapLocation.getStreetNum()
                        , aMapLocation.getLatitude()
                        , aMapLocation.getLongitude()
                );
                cityListAdapter.updateLocateState(LocateState.SUCCESS, locationCity);

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Logger.e("定位失败");
                Logger.e("ErrCode = " + aMapLocation.getErrorCode());
                Logger.e("ErrorInfo = " + aMapLocation.getErrorInfo());
                //定位失败
                cityListAdapter.updateLocateState(LocateState.FAILED, null);
            }
        }
    }


    @Override
    public void onRefresh() {
        selectCityPresenter.loadData(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        selectCityPresenter = null;
    }


}
