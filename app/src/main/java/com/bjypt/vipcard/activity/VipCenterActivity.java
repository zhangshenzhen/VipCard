package com.bjypt.vipcard.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.MyBaseAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.VipCenterInMarchantBean;
import com.bjypt.vipcard.model.VipcenterButtomBean;
import com.bjypt.vipcard.model.VipcenterResultDataBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/19.
 */
public class VipCenterActivity extends BaseActivity implements VolleyCallBack<String> {

    private PullToRefreshListView pullListView;
    //    private View mHeadView;
    private List<VipcenterButtomBean> lists;
    private RelativeLayout layout_back;
    private ImageView mVipImge;
    private TextView mVipName, mVipIncomeInfo;
    private int begin = 0;//开始条数
    private int pageLength = 10;//每页显示条数
    private Map<String, String> Params;

    private String grade = "";//会员等级
    private String discount;//折扣
    private VipcenterResultDataBean mVipcenterResultDataBean;
    private VipCenterInMarchantBean mVipCenterInMarchantBean;
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;
    private int isRefresh = 0;
    private VipcenterAdapter mAdapter;
    private String distance;
    private RelativeLayout mHeadView;
    private ImageView iv_default_vipcenter_pic;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_vipcenter);
    }

    @Override
    public void beforeInitView() {
        request();
    }

    public void request() {
        Params = new HashMap<>();
        Params.put("userId", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(VipCenterActivity.this, 1, Config.web.get_platBalance, Params, this);
        Params.put("begin", begin + "");
        Params.put("pageLength", pageLength + "");
        Params.put("lng", SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.lngu));
        Params.put("lat", SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.latu));
        Wethod.httpPost(VipCenterActivity.this, 2, Config.web.get_merchantBalance, Params, this);
    }

    @Override
    public void initView() {
        iv_default_vipcenter_pic = (ImageView) findViewById(R.id.iv_default_vipcenter_pic);
        layout_back = (RelativeLayout) findViewById(R.id.layout_back);
        pullListView = (PullToRefreshListView) findViewById(R.id.vipcenter_pulllist);
        mHeadView = (RelativeLayout) findViewById(R.id.hyb_balance);
        pullListView.setMode(PullToRefreshBase.Mode.BOTH);
        mVipIncomeInfo = (TextView) findViewById(R.id.iv_vipcenter_incomevipInfo);
    }

    @Override
    public void afterInitView() {
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        lists = new ArrayList<>();
        mHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVipcenterResultDataBean != null) {
                    Intent mIntent = new Intent(VipCenterActivity.this, VipCenterAccountActivity.class);
                    mIntent.putExtra("wholebalance", mVipcenterResultDataBean.getResultData().getWholebalance());
                    mIntent.putExtra("grade", grade);
                    startActivity(mIntent);
                }

            }
        });
    }

    @Override
    public void bindListener() {

        layout_back.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {

        switch (v.getId()) {
            case R.id.layout_back:

                this.finish();
                break;
        }
    }

    /**
     * 执行查询商家接口
     **/
    private void onRequest(int type) {

        if (type == QUERY_MORE) {
            if (Params != null) {
                Params.clear();
            }
            isRefresh = 1;
            begin = 0;

            Params.put("userId", getFromSharePreference(Config.userConfig.pkregister));
            Params.put("begin", begin + "");
            Params.put("pageLength", pageLength + "");


        } else {
            if (Params != null) {
                Params.clear();
            }

            isRefresh = 2;
            begin += 10;

            Params.put("userId", getFromSharePreference(Config.userConfig.pkregister));
            Params.put("begin", begin + "");
            Params.put("pageLength", pageLength + "");
        }

        Wethod.httpPost(VipCenterActivity.this, 2, Config.web.get_merchantBalance, Params, this);
    }


    @Override
    public void onSuccess(int reqcode, String result) {

        if (reqcode == 1) {
            Log.i("aaa", "aaa=" + result.toString());
            try {
                mVipcenterResultDataBean = getConfiguration().readValue(result.toString(), VipcenterResultDataBean.class);
                if (mVipcenterResultDataBean.getResultData().getGrade() == null)
                    grade = "普通会员";
                else
                    grade = mVipcenterResultDataBean.getResultData().getGrade();

                mVipIncomeInfo.setText("余额：" + mVipcenterResultDataBean.getResultData().getWholebalance()
                        + "\t\t昨日收益" + mVipcenterResultDataBean.getResultData().getInterest());
            } catch (IOException e) {

                Log.i("aaa", "111" + e.toString());
                e.printStackTrace();
            }

        } else if (reqcode == 2) {

            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                Log.i("aaa", "" + result.toString());
                mVipCenterInMarchantBean = objectMapper.readValue(result.toString(), VipCenterInMarchantBean.class);


                Log.i("aaa", "welcome to json");
                if (mVipCenterInMarchantBean.getResultData().size() == 0 && lists.size() == 0) {
                    pullListView.onRefreshComplete();
                    iv_default_vipcenter_pic.setVisibility(View.VISIBLE);
                } else if (mVipCenterInMarchantBean.getResultData().size() != 0) {
                    iv_default_vipcenter_pic.setVisibility(View.GONE);
                    if (isRefresh == 1 || isRefresh == 0) {
                        if (mAdapter != null) {
                            lists.clear();
                        }
                    }
                    for (int i = 0; i < mVipCenterInMarchantBean.getResultData().size(); i++) {

                        VipcenterButtomBean mBean = new VipcenterButtomBean();
                        mBean.setVipcenter_discount(mVipCenterInMarchantBean.getResultData().get(i).getDiscount());
                        mBean.setVipcenter_Balance(mVipCenterInMarchantBean.getResultData().get(i).getBalance());
                        mBean.setVipcenter_lastdayIncome(mVipCenterInMarchantBean.getResultData().get(i).getInterest());
                        mBean.setVipcenter_Name(mVipCenterInMarchantBean.getResultData().get(i).getMuname());
                        mBean.setVipcenter_photo(mVipCenterInMarchantBean.getResultData().get(i).getLogo());
                        mBean.setDistance(mVipCenterInMarchantBean.getResultData().get(i).getDistance());
                        mBean.setPkmuser(mVipCenterInMarchantBean.getResultData().get(i).getPkmuser());
                        lists.add(mBean);
                    }

                    if (isRefresh == 1) {
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                            pullListView.onRefreshComplete();
                        } else {
                            mAdapter = new VipcenterAdapter(lists, this);
                            mAdapter.notifyDataSetChanged();
                            pullListView.onRefreshComplete();
                        }

                    } else if (isRefresh == 2) {
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                            pullListView.onRefreshComplete();
                        } else {
                            mAdapter = new VipcenterAdapter(lists, this);
                            mAdapter.notifyDataSetChanged();
                            pullListView.onRefreshComplete();
                        }
                    } else {
                        mAdapter = new VipcenterAdapter(lists, this);
                        pullListView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        pullListView.onRefreshComplete();
                    }
                } else {
                    pullListView.onRefreshComplete();
                }

                pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                        onRequest(QUERY_MORE);
                    }

                    @Override
                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                        onRequest(QUERY_REFERSH);
                    }
                });


                pullListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(VipCenterActivity.this, NewSubscribeDishesActivity.class);
                        intent.putExtra("pkmuser", lists.get(position - 1).getPkmuser());
                        intent.putExtra("distance", lists.get(position - 1).getDistance());
                        startActivity(intent);
                    }
                });
            } catch (IOException e) {

                Log.i("aaa", "222" + e.toString());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {

        Log.i("aaa", "onFailed");
        if (reqcode == 1) {
            Wethod.ToFailMsg(this, result.toString());
        } else if (reqcode == 2) {
            Wethod.ToFailMsg(this, result.toString());
        }
    }


    public class VipcenterAdapter extends MyBaseAdapter<VipcenterButtomBean> {

        public VipcenterAdapter(List<VipcenterButtomBean> list, Context mContext) {
            super(list, mContext);
        }

        @Override
        public View initView(int position, View convertView, ViewGroup parent) {
            VipcenterHoler mHoler = null;
            if (convertView == null) {
                mHoler = new VipcenterHoler();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_vipcenter_buttom, null);

                mHoler.VipcenterBalance = (TextView) convertView.findViewById(R.id.tv_vipcenterBalance_And_vipcenterlastdayIncome);
                mHoler.VipcenterName = (TextView) convertView.findViewById(R.id.tv_vipcenterName);
                mHoler.Vipcenterdiscount = (TextView) convertView.findViewById(R.id.tv_vipcenter_discount);
                mHoler.VipcenterPhoto = (RoundImageView) convertView.findViewById(R.id.iv_vipcenter_vipdefImg);

                convertView.setTag(mHoler);
            } else {
                mHoler = (VipcenterHoler) convertView.getTag();
            }

            mHoler.VipcenterBalance.setText("余额:" + list.get(position).getVipcenter_Balance()
                    + "   昨日收益:" + list.get(position).getVipcenter_lastdayIncome());
            mHoler.VipcenterName.setText(list.get(position).getVipcenter_Name());
            if (list.get(position).getVipcenter_discount() != null) {
                mHoler.Vipcenterdiscount.setText(setCharType(list.get(position).getVipcenter_discount()));
            } else {
                mHoler.Vipcenterdiscount.setText("100");
            }

            if (!"".equals(list.get(position).getVipcenter_photo())) {
                ImageLoader.getInstance().displayImage(Config.web.picUrl + list.get(position).getVipcenter_photo(), mHoler.VipcenterPhoto, AppConfig.DEFAULT_IMG_OPTIONS_AD);
            }
            return convertView;
        }

        public class VipcenterHoler {
            TextView VipcenterName, VipcenterBalance, Vipcenterdiscount;
            RoundImageView VipcenterPhoto;
        }

        public String setCharType(String str) {
            str = Float.parseFloat(str) * 100 + "";
            String strs[] = str.split("\\.");
            return strs[0];
        }
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

    @Override
    public void onError(VolleyError volleyError) {
        pullListView.onRefreshComplete();
    }

    @Override
    public void isConntectedAndRefreshData() {
        request();
    }
}
