package com.bjypt.vipcard.activity;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.CouponAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.CouponBean;
import com.bjypt.vipcard.model.CouponRootBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.DensityUtil;
import com.sinia.orderlang.utils.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 优惠券
 * 调用选择优惠券传入 isSelect="Y",flag ="0" pkmuser商家主键 amount需要支付的金额
 * 调用正常的优惠券传入 isSelect="N" flag=“0”
 */
public class CouponActivity extends BaseActivity implements VolleyCallBack {
    //private LinearLayout ll_back;//返回
    private PullToRefreshListView listView;

    private TextView tv_title;
    private String url = Config.web.new_coupn;
    private String pkregister;
    private String couType = "1";
    private int begin;
    private int pageLength = 10;
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;
    private int isrefresh;
    private Map<String, String> map;
    private List<CouponBean> list;
    private CouponAdapter adapter;
    private ImageView iv_default_coupon_pic;
    private String flag = "0";//0:优惠券可使用 1即已过期或已使用
    private String isSelect = "N";//Y：处于选择状态的优惠券 N:非选择状态
    private TextView tv_quan_entry;
    private LinearLayout ly_find_history_quan;
    private LinearLayout ly_quan_detail;
    private LinearLayout ly_coupon_acitivity_back;
    private StringBuffer stringBuffer = new StringBuffer();
    ;
    private String pkmuser = "";
    private String amount = "";
    //    private String coupos ="";
    private String coupon = "";
    private View footerview;
    private ListView listView1;


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_coupon);
        pkregister = getFromSharePreference(Config.userConfig.pkregister);
        map = new HashMap<>();
        list = new ArrayList<>();

    }

    @Override
    public void beforeInitView() {
        isSelect = getIntent().getStringExtra("isSelect");
        flag = getIntent().getStringExtra("flag"); //0:选择优惠劵 1：历史 2：我的空间--进入
        pkmuser = getIntent().getStringExtra("pkmuser");
        if (getIntent().hasExtra("couType")) {
            couType = getIntent().getStringExtra("couType");
        }
        amount = getIntent().getStringExtra("amount");
        coupon = getIntent().getStringExtra("coupons");
        Log.e("liyunte", "flag=" + flag + "isSelcet=" + isSelect);
    }

    @Override
    public void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_quan_entry = (TextView) findViewById(R.id.tv_quan_entry);

        ly_quan_detail = (LinearLayout) findViewById(R.id.ly_quan_detail);
        ly_coupon_acitivity_back = (LinearLayout) findViewById(R.id.ly_coupon_acitivity_back);
        iv_default_coupon_pic = (ImageView) findViewById(R.id.iv_default_coupon_pic);
//        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        footerview = LayoutInflater.from(this).inflate(R.layout.layout_coupon_footer, null);
        listView = (PullToRefreshListView) findViewById(R.id.coupon_listvew);
        ly_find_history_quan = (LinearLayout) footerview.findViewById(R.id.ly_find_history_quan);
        listView1 = listView.getRefreshableView();
        listView1.addFooterView(footerview);

        if (isSelect.equals("Y")) {
            listView.setMode(PullToRefreshBase.Mode.DISABLED);
            ly_find_history_quan.setVisibility(View.GONE);
            tv_quan_entry.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) listView.getLayoutParams();
            params.bottomMargin = DensityUtil.dip2px(this, 40);

//            listView.setBottom(DensityUtil.dip2px(this, 50));
        } else if (isSelect.equals("N") && (flag.equals("0") && flag.equals("2"))) {
            tv_quan_entry.setVisibility(View.GONE);
            ly_quan_detail.setVisibility(View.VISIBLE);
            listView.setMode(PullToRefreshBase.Mode.DISABLED);
            ly_find_history_quan.setVisibility(View.VISIBLE);
        } else {
            listView.setMode(PullToRefreshBase.Mode.BOTH);
        }
        if (flag.equals("1")) {
            tv_title.setText("失效券");
            ly_quan_detail.setVisibility(View.GONE);
            tv_quan_entry.setVisibility(View.GONE);
            ly_find_history_quan.setVisibility(View.GONE);
        }

    }

    public void setRequest(int type) {
        if (type == QUERY_REFERSH) {
            if (map != null) {
                map.clear();
            }
            begin = 0;
            isrefresh = 1;
            map.put("pkregister", pkregister);
            map.put("couType", couType);//2： 历史 1：可用
            map.put("begin", begin + "");
            map.put("pageLength", pageLength + "");
            Wethod.httpPost(CouponActivity.this, 46, url, map, this);

        } else {
            if (map != null) {
                map.clear();
            }
            begin += 10;
            isrefresh = 2;
            map.put("pkregister", pkregister);
            map.put("couType", couType);
            map.put("begin", begin + "");
            map.put("pageLength", pageLength + "");
            Wethod.httpPost(CouponActivity.this, 46, url, map, this);
        }

    }

    @Override
    public void afterInitView() {
        if (isSelect.equals("Y")) {
            if (couType.equals("2") && flag.equals("1")) {
                setRequest(QUERY_REFERSH);
            }
            getQuan();
        } else {
            setRequest(QUERY_REFERSH);
        }
        adapter = new CouponAdapter(this, list, handler);
        listView.setAdapter(adapter);

    }

    @Override
    public void bindListener() {
        ly_find_history_quan.setOnClickListener(this);
        tv_quan_entry.setOnClickListener(this);
        ly_quan_detail.setOnClickListener(this);
        ly_coupon_acitivity_back.setOnClickListener(this);
        /****
         * 上拉下拉双向监听事件
         */
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isSelect.equals("Y")) {
                    listView.onRefreshComplete();
                } else {
                    setRequest(QUERY_REFERSH);
                }

            }


            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isSelect.equals("Y")) {
                    listView.onRefreshComplete();
                } else {
                    setRequest(QUERY_MORE);
                }

            }
        });


    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.ly_coupon_acitivity_back:
                Intent mnIntent = new Intent();
//                mnIntent.putExtra("coupons", "");
                setResult(RESULT_CANCELED, mnIntent);
                finish();
                break;
            case R.id.tv_quan_entry://确定
                request_check_quan();
                break;
            case R.id.ly_quan_detail://优惠券说明
                startActivity(new Intent(CouponActivity.this, CouponToastActivity.class));
                break;
            case R.id.ly_find_history_quan://查看历史优惠券
//                ly_find_history_quan.setVisibility(View.GONE);
//                listView.setMode(PullToRefreshBase.Mode.BOTH);
//                flag = "1";
//                isSelect = "N";
//                couType = "2";
//                setRequest(QUERY_REFERSH);
                Intent intent = new Intent(this, CouponActivity.class);
                intent.putExtra("flag", "1");
                intent.putExtra("isSelect", "N");
                intent.putExtra("couType", "2");
                startActivity(intent);

                break;

        }
    }

    private void request_check_quan() {
         /*  1) amount预计支付金额[AES加密]
        2) coupons 用户选择的优惠券主键（多个优惠券以分号隔开）*/

        if (StringUtil.isNotEmpty(stringBuffer.toString())) {
            Map<String, String> map = new HashMap<>();
            map.put("amount", AES.encrypt(amount, AES.key));
            map.put("coupons", AES.encrypt(stringBuffer.toString().substring(0, stringBuffer.toString().lastIndexOf(";")), AES.key));
            Wethod.httpPost(this, 12, Config.web.check_quan, map, this);
        } else if (adapter.selectList.size() == 1) {
            Intent mnIntent = new Intent();
//            mnIntent.putExtra("coupons", "");
            setResult(RESULT_CANCELED, mnIntent);
            finish();
        } else {
            Intent mIntent = new Intent();
            mIntent.putExtra("coupons", "");
            setResult(RESULT_OK, mIntent);
            finish();
        }


    }

    public void getQuan() {
        Map<String, String> map = new HashMap<>();
        map.put("pkmuser", pkmuser);
        map.put("pkregister", pkregister);
        Wethod.httpPost(this, 46, Config.web.get_quan, map, this);

    }

    @Override
    public void onSuccess(int reqcode, Object result) {

        if (reqcode == 46) {
            Log.e("liyunteee", result.toString());
            try {
                CouponRootBean rootBean = getConfiguration().readValue(result.toString(), CouponRootBean.class);
                Log.e("liyunteee", "------------------------");
                if (rootBean.getResultData().size() == 0 && list.size() == 0) {
                    listView.onRefreshComplete();
                   /* ly_find_history_quan.setVisibility(View.GONE);
                    ly_quan_detail.setVisibility(View.GONE);*/
                    iv_default_coupon_pic.setVisibility(View.VISIBLE);
                } else if (rootBean.getResultData().size() > 0) {
                    iv_default_coupon_pic.setVisibility(View.GONE);
                    if (isrefresh == 1) {
                        if (adapter != null) {
                            Log.e("liyunteee", "!null----------------");
                            list.clear();
                        }
                    }
                    if (isSelect.equals("Y")) {
                        Log.e("llll", "9");
                        if (list.size() > 0) {
                            list.clear();
                            Log.e("llll", "10");
                        }
                    }
                    String[] couponIds = null;
                    if (StringUtil.isNotEmpty(coupon)) {
                        couponIds = coupon.split(";");
                    }

                    for (int i = 0; i < rootBean.getResultData().size(); i++) {
                        Log.e("llll", "0");
                        CouponBean bean = new CouponBean();
                        bean.setEnddate(rootBean.getResultData().get(i).getEnddate());
                        bean.setPayamount(rootBean.getResultData().get(i).getPayamount());
                        bean.setRemark(rootBean.getResultData().get(i).getRemark());
                        bean.setStartdate(rootBean.getResultData().get(i).getStartdate());
                        bean.setUsestatus(rootBean.getResultData().get(i).getUsestatus());
                        bean.setValueamount(rootBean.getResultData().get(i).getValueamount());
                        bean.setMuname(rootBean.getResultData().get(i).getMuname());
                        bean.setPaytime(rootBean.getResultData().get(i).getPaytime());
                        bean.setPkmuser(rootBean.getResultData().get(i).getPkmuser());
                        bean.setFlag(flag);
                        bean.setPolicystatus(rootBean.getResultData().get(i).getPolicystatus());
                        bean.setPolicycontent(rootBean.getResultData().get(i).getPolicycontent());
                        bean.setPkcoupon(rootBean.getResultData().get(i).getPkcoupon());
                        bean.setPkorderid(rootBean.getResultData().get(i).getPkorderid());
                        bean.setIsSelect(isSelect);
                        if (couponIds != null && couponIds.length > 0) {
                            for (int n = 0; n < couponIds.length; n++) {
                                if (couponIds[n].equals(bean.getPkorderid())) {
                                    bean.setIscheck(true);
                                }
                            }
                        }

                        list.add(bean);
                    }

                    if (isrefresh == 1) {
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                            listView.onRefreshComplete();
                        } else {
                            adapter = new CouponAdapter(this, list, handler);
                            adapter.notifyDataSetChanged();
                            listView.onRefreshComplete();
                        }
                    } else if (isrefresh == 2) {
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                            listView.onRefreshComplete();
                        } else {
                            adapter = new CouponAdapter(this, list, handler);
                            adapter.notifyDataSetChanged();
                            listView.onRefreshComplete();
                        }
                    } else {
                        if (isSelect.equals("Y")) {

                        } else {
                            adapter.add(list);
                        }

                        adapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    }
                } else {
                    listView.onRefreshComplete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            listView.onRefreshComplete();
        } else if (reqcode == 12) {

            Intent mIntent = new Intent();
            if (stringBuffer.toString().substring(0, stringBuffer.toString().lastIndexOf(";")) == null) {
                mIntent.putExtra("coupons", "");
            } else {
                mIntent.putExtra("coupons", stringBuffer.toString().substring(0, stringBuffer.toString().lastIndexOf(";")));
            }

            setResult(RESULT_OK, mIntent);
            finish();
//            Toast.makeText(CouponActivity.this, "可以使用", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if (reqcode == 12) {
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == 46) {
            ly_find_history_quan.setVisibility(View.GONE);
            ly_quan_detail.setVisibility(View.GONE);
            iv_default_coupon_pic.setVisibility(View.VISIBLE);
            Wethod.ToFailMsg(this, result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        listView.onRefreshComplete();
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
    public void isConntectedAndRefreshData() {
        setRequest(QUERY_REFERSH);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                HashMap<String, String> ad = (HashMap<String, String>) msg.obj;
                stringBuffer = new StringBuffer();
                Iterator iter = ad.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String val = (String) entry.getValue();
                    stringBuffer.append(val.toString());
                    stringBuffer.append(";");
                }
                if (StringUtil.isNotEmpty(stringBuffer.toString())) {
                    coupon = stringBuffer.toString().substring(0, stringBuffer.toString().lastIndexOf(";"));
                }
            }
        }
    };
}
