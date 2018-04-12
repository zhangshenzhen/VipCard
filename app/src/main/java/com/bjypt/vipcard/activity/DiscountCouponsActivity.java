package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.DiscountCouponsBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.lidroid.xutils.util.LogUtils;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bjypt.vipcard.R.id.lv_discount;

/**
 * Created by 崔龙 on 2017/3/10.
 */

public class DiscountCouponsActivity extends BaseActivity implements VolleyCallBack<String> {
    private static final int CONSUME = 110;
    // ImageView
    private ImageView iv_discount_back;    // 返回键
    private LinearLayout iv_discount_search;  // 搜索键
    private ImageView iv_discount_rule;    // 使用规则

    // EditText
    private EditText et_discount_content;  // 搜索内容
    private PullToRefreshListView pull_discount;

    final int QUERY_EXERCISE_MORE = 0x0101;
    final int QUERY_EXERCISE_REFERSH = 0x0110;
    private boolean is_refresh = true;

    // ListView
//    private ListView lv_discount;

    // LinearLayout
    private LinearLayout ll_item;          // 条目点击


    //    private View mHeadView;
    private List<DiscountCouponsBean.ResultDataBean> resultDatas = new ArrayList<>();
    private DisAdapter mAdapter;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_discount);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        pull_discount = (PullToRefreshListView) findViewById(lv_discount);
        pull_discount.setMode(PullToRefreshBase.Mode.BOTH);
        pull_discount.setScrollingWhileRefreshingEnabled(true);
//        lv_discount = pull_discount.getRefreshableView();

//        mHeadView = LayoutInflater.from(this).inflate(R.layout.head_view_discount, null);
        // 添加头布局时设置头布局控件可以被点击
//        lv_discount.addHeaderView(mHeadView, null, true);
        iv_discount_back = (ImageView) findViewById(R.id.iv_discount_back);
        iv_discount_search = (LinearLayout) findViewById(R.id.iv_discount_search);
        iv_discount_rule = (ImageView) findViewById(R.id.iv_discount_rule);
        et_discount_content = (EditText) findViewById(R.id.et_discount_content);
        mAdapter = new DisAdapter();
        pull_discount.setAdapter(mAdapter);
        pull_discount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DiscountCouponsActivity.this, NewSubscribeDishesActivity.class);
                int itemId = (int) adapterView.getAdapter().getItemId(i);

                // 判断是否登录，如果登录跳转到购买页面
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {

                    intent.putExtra("pkmuser", resultDatas.get(itemId).getPkmuser());
                    intent.putExtra("distance", "*");
                    intent.putExtra("rechargeactivity", "");
                    intent.putExtra("pkmerchantcoupon", resultDatas.get(itemId).getPkmerchantcoupon());
                    startActivity(intent);
                } else {
                    // 否则跳转到登录界面
                    startActivity(new Intent(DiscountCouponsActivity.this, LoginActivity.class));
                }
//                ToastUtil.showToast(DiscountCouponsActivity.this,itemId+"");
            }
        });
    }

    @Override
    public void afterInitView() {
        pull_discount.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadDatas(QUERY_EXERCISE_REFERSH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadDatas(QUERY_EXERCISE_MORE);
            }

        });
        //首次来到页面加载数据
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                pull_discount.setRefreshing();
                return true;
            }
        }).sendEmptyMessageDelayed(0, 300);
        et_discount_content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                loadDatas(QUERY_EXERCISE_REFERSH);
                return false;
            }
        });
    }

    @Override
    public void bindListener() {
        // 头布局控件监听
        iv_discount_back.setOnClickListener(this);
        iv_discount_search.setOnClickListener(this);
        iv_discount_rule.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            // 点击返回键
            case R.id.iv_discount_back:
                finish();
                break;
            // 点击问号
            case R.id.iv_discount_rule:
                //                CouponToastActivity
                Intent intent = new Intent(this, CouponToastActivity.class);
                startActivity(intent);
                break;
            // 点击搜索
            case R.id.iv_discount_search:
                loadDatas(QUERY_EXERCISE_REFERSH);
                break;
        }

    }


    private int begin = 0;
    private int pageLength = 10;

    /**
     * cityCode 城市编码
     * begin 分页查询起始
     * pagteLength 分页长度
     */
    private void loadDatas(int refresh_type) {
        if (refresh_type == QUERY_EXERCISE_REFERSH) {
            begin = 0;
            is_refresh = true;
        } else {
            begin += pageLength;
            is_refresh = false;
        }
        Map<String, String> params = new HashMap<>();
        params.put("cityCode", getFromSharePreference(Config.userConfig.citycode));//getFromSharePreference(Config.userConfig.citycode)
        params.put("merchantName", et_discount_content.getText().toString());
        params.put("begin", begin + "");
        params.put("pageLength", pageLength + "");
        Wethod.httpPost(this, CONSUME, Config.web.fine_discount_get_couponsLis, params, this);
    }


    /**
     * pkmuser : a0245c8f8a6043fdaad230698b74fbbf
     * muname : 大伙铺子
     * payamount : 9
     * valueamount : 100
     * startdate : 2017-02-27
     * enddate : 2017-03-31
     * remark : 无门槛
     */
    @Override
    public void onSuccess(int reqcode, String result) {
        pull_discount.onRefreshComplete();// 停止刷新
        switch (reqcode) {
            case CONSUME:
                LogUtils.e(result);
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                DiscountCouponsBean discountCouponsBeans = null;
                try {
                    discountCouponsBeans = objectMapper.readValue(result.toString(), DiscountCouponsBean.class);
//                    LogUtils.e(resultDatas.toString());
                    // 判断是下拉刷新还是上拉加载
                    if (is_refresh) { // 下拉刷新
                        resultDatas.clear();
                        resultDatas.addAll(discountCouponsBeans.getResultData());
                    } else { // 加载更多
                        resultDatas.addAll(discountCouponsBeans.getResultData());
                    }
                    mAdapter.notifyDataSetChanged();
                    pull_discount.onRefreshComplete();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {
        pull_discount.onRefreshComplete();// 停止刷新
    }

    @Override
    public void onError(VolleyError volleyError) {

    }


    private class DisAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return resultDatas.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            DiscountHolder mHolder = null;
            if (view == null) {
                mHolder = new DiscountHolder();
                view = View.inflate(DiscountCouponsActivity.this, R.layout.item_discount1, null);
                mHolder.tv_payamount = (TextView) view.findViewById(R.id.tv_payamount);
                mHolder.tv_valueamount = (TextView) view.findViewById(R.id.tv_valueamount);
                mHolder.tv_merchant = (TextView) view.findViewById(R.id.tv_merchant);
                mHolder.tv_startdate = (TextView) view.findViewById(R.id.tv_startdate);
                mHolder.tv_discount_address = (TextView) view.findViewById(R.id.tv_discount_address);
                mHolder.tv_enddate = (TextView) view.findViewById(R.id.tv_enddate);
                mHolder.wumenkan = (TextView) view.findViewById(R.id.wumenkan);
                view.setTag(mHolder);
            } else {
                mHolder = (DiscountHolder) view.getTag();
            }
            mHolder.tv_payamount.setText(resultDatas.get(position).getPayamount());
            mHolder.tv_valueamount.setText(resultDatas.get(position).getValueamount());
            mHolder.tv_merchant.setText(resultDatas.get(position).getMuname());
            mHolder.tv_startdate.setText(resultDatas.get(position).getStartdate());
            mHolder.tv_enddate.setText(resultDatas.get(position).getEnddate());
            mHolder.wumenkan.setText(resultDatas.get(position).getRemark());
            mHolder.tv_discount_address.setText(resultDatas.get(position).getAddres());
            return view;
        }
    }

    class DiscountHolder {
        // TextView
        private TextView tv_payamount;         // 支付金额
        private TextView tv_valueamount;       // 抵扣金额
        private TextView tv_merchant;          // 商家名称
        private TextView tv_startdate;         // 开始时间
        private TextView tv_enddate;           // 结束时间
        private TextView tv_discount_address;  // 地址
        private TextView wumenkan;             // 结束时间
    }
}
