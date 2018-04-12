package com.bjypt.vipcard.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.UnPayOrderAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.AllOrderBean;
import com.bjypt.vipcard.model.AllOrderRootBean;
import com.bjypt.vipcard.model.UnPayOrderBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.ToastUtils;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liyunte on 2016/3/31.
 */
public class UnPayOrderActivity extends BaseActivity implements VolleyCallBack {

    private PullToRefreshListView pullList;
    private View mHeadView;
    private List<UnPayOrderBean> list;
    private TextView tv_edit_lv_unpayOrder;
    private UnPayOrderAdapter mAdapter;
    private boolean isShow = false;
    private RelativeLayout layout_back;
    private List<AllOrderBean> resultList;
    public static int FLAG_UP = 1;

    //http://123.57.232.188:8080/hyb/ws/post/getPreOrder
  /* userId:用户主键
    begin：位移(分页)
    pageLength：长度(分页)
    status: (4:已失效  21:待使用 ； 22:待预约 23已消费 无此参数时查询所有订单)"
*/
    private String url = Config.web.find_all_order;
    private int pageLength = 10;
    private int isRefresh = 1;// 1刷新 2.加载更多
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;
    private Map<String, String> map;
    private String userId;
    private int begin = 0;
    private int flag; // 1.全部订单 2.待预约订单，3待使用订单 4 已消费
    public static String UNPAY_FLAG = "Y";
    /**
     * 删除订单
     * 入参
     * userId:用户主键
     * preorderId:订单主键
     */
    private String delurl = Config.web.del_order;
    private Map<String, String> maps;
//    private String preorderId;
    private ImageView iv_default_unpay_pic;
    private TextView mOrderName;


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_unpay_order);
        map = new HashMap<>();
        maps = new HashMap<>();
        list = new ArrayList<UnPayOrderBean>();
        userId = getFromSharePreference(Config.userConfig.pkregister);
        Log.e("liyunte", userId);
    }

    @Override
    public void beforeInitView() {


    }

    public void delRequest(String preorderId) {
        if (maps != null) {
            maps.clear();
        }
        maps.put("userId", userId);
        maps.put("preorderId", preorderId);
        Wethod.httpPost(UnPayOrderActivity.this,45, delurl, maps, this);
    }

    public void setRequest(int type, int falg) {
        if (type == QUERY_REFERSH) {
            if (map != null) {
                map.clear();
            }
            if (falg == 2) {
                map.put("status", "21");
                mOrderName.setText("待使用订单");
            } else if (falg == 3) {
                map.put("status", "22");
                mOrderName.setText("待支付订单");
            } else if (falg == 4) {
                map.put("status", "23");
                mOrderName.setText("待评论订单");
            }
            begin = 0;
            isRefresh = 1;
            map.put("userId", userId);
            map.put("begin", begin + "");
            map.put("pageLength", pageLength + "");
            Wethod.httpPost(UnPayOrderActivity.this,44, url, map, this);

        } else {
            if (map != null) {
                map.clear();
            }
            if (falg == 2) {
                map.put("status", "21");
            } else if (falg == 3) {
                map.put("status", "22");
            } else if (falg == 4) {
                map.put("status", "23");
            }
            isRefresh = 2;
            begin += 10;
            map.put("userId", userId);
            map.put("begin", begin + "");
            map.put("pageLength", pageLength + "");
            Wethod.httpPost(UnPayOrderActivity.this,44, url, map, this);
        }
    }

    @Override
    public void initView() {
        pullList = (PullToRefreshListView) findViewById(R.id.lv_unpay_order);
        iv_default_unpay_pic= (ImageView) findViewById(R.id.iv_default_unpay_pic);
        layout_back = (RelativeLayout) findViewById(R.id.layout_back);
        tv_edit_lv_unpayOrder = (TextView) findViewById(R.id.tv_edit_lv_unpayOrder);
        mOrderName= (TextView) findViewById(R.id.mOrderName);
        pullList.setMode(PullToRefreshBase.Mode.BOTH);

        //假如为未支付订单
        if (flag == 2) {
            tv_edit_lv_unpayOrder.setVisibility(View.GONE);
        }
        /****
         * 上拉下拉双向监听事件
         */
        pullList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            /***
             * 下拉
             * @param refreshView
             */
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                setRequest(QUERY_REFERSH, flag);
            }

            /****
             * 上拉
             * @param refreshView
             */
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                setRequest(QUERY_MORE, flag);

            }
        });

    }

    @Override
    public void afterInitView() {


        //添加适配
        refreshAdapter();
    }

    @Override
    public void bindListener() {
        layout_back.setOnClickListener(this);
        tv_edit_lv_unpayOrder.setOnClickListener(this);
    }


    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.tv_edit_lv_unpayOrder:

                if (isShow) {
                    tv_edit_lv_unpayOrder.setText("编辑");
                    isShow = !isShow;
//                    pullList.setEnabled(false);
                } else {
                    tv_edit_lv_unpayOrder.setText("完成");
                    isShow = !isShow;
//                    pullList.setEnabled(true);
                }
                refreshAdapter();
                break;
            case R.id.layout_back:

                this.finish();
                break;
        }
    }

    public void refreshAdapter() {
        mAdapter = new UnPayOrderAdapter(list, this, isShow, mListener);
        pullList.setAdapter(mAdapter);
    }



    /**
     * 实现类，响应按钮点击事件
     */
    private UnPayOrderAdapter.MyClickListener mListener = new UnPayOrderAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            switch (v.getId()) {
                case R.id.btn_affirmOrder:
                    //代预约
                    if (list.get(position).getStatus() == 23) {
                        Intent intent = new Intent(UnPayOrderActivity.this, IssueCommentActivity.class);
                        intent.putExtra("preorderId", list.get(position).getPreorderId());
                        intent.putExtra("pkmuser", list.get(position).getPkmuser());
                        startActivity(intent);
                    } else if (list.get(position).getStatus() == 22) {
                        Intent intent_queren = new Intent(UnPayOrderActivity.this, PayOneActivity.class);
                        intent_queren.putExtra("orderId", list.get(position).getOrderNo());
                        intent_queren.putExtra("pkmuser", list.get(position).getPkmuser());
                        intent_queren.putExtra("preorderId", list.get(position).getPreorderId());

                        intent_queren.putExtra("primaryk",list.get(position).getPksubscbptn());
                        intent_queren.putExtra("FLAG", "Y");
                        startActivity(intent_queren);
                        finish();
                    } else if (list.get(position).getStatus() == 21) {
                        Intent intent_queren = new Intent(UnPayOrderActivity.this, PayOneActivity.class);
                        intent_queren.putExtra("orderId", list.get(position).getOrderNo());
                        intent_queren.putExtra("pkmuser", list.get(position).getPkmuser());
                        intent_queren.putExtra("preorderId", list.get(position).getPreorderId());
                        intent_queren.putExtra("FLAG", "N");
                        startActivity(intent_queren);
                        finish();

                    }
                  /*  Toast.makeText(
                            UnPayOrderActivity.this,
                            "预约订单被点击了" + position, Toast.LENGTH_SHORT)
                            .show();*/
                    break;
                case R.id.btn_unpay_order_del:
                    //删除订单

                    dialogShow(position);
                    //refreshAdapter();
                    break;
            }
        }
    };

    protected void dialogShow(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认删除此订单？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (list.get(position).getStatus() != 21) {
                    delRequest(list.get(position).getPreorderId());
                    list.remove(position);
                    mAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showMessage(UnPayOrderActivity.this, "不可删除");
                }
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
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 44) {
            Log.e("liyunteee", result.toString());
            try {
                AllOrderRootBean bean = getConfiguration().readValue(result.toString(), AllOrderRootBean.class);
                if (bean.getResultData().size()==0&&list.size()==0){
                    iv_default_unpay_pic.setVisibility(View.VISIBLE);
                }else if (bean.getResultData().size() > 0) {
                    iv_default_unpay_pic.setVisibility(View.GONE);
                    if (isRefresh == 1) {
                        if (mAdapter != null) {
                            Log.e("liyunteee", "!null----------------");
                            list.clear();
                        }
                    }
                    //  resultList = bean.getResultData();
                    for (int i = 0; i < bean.getResultData().size(); i++) {

                        Log.e("liyunteee", "for--------------------");
                        UnPayOrderBean orderBean = new UnPayOrderBean();
                        orderBean.setUserRemark(bean.getResultData().get(i).getUserRemark());
                        orderBean.setGoodsImg(bean.getResultData().get(i).getLogo());
                        orderBean.setGoodsName(bean.getResultData().get(i).getMuname());
                        orderBean.setGoodsNum(bean.getResultData().get(i).getOrderCount());
                        orderBean.setPreorderId(bean.getResultData().get(i).getPreorderId());
                        orderBean.setGoodsPrice(bean.getResultData().get(i).getTotalPrice());
                        orderBean.setStatus(bean.getResultData().get(i).getStatus());
                        orderBean.setPkmuser(bean.getResultData().get(i).getPkmuser());
                        orderBean.setOrderNo(bean.getResultData().get(i).getOrderNo());
                        orderBean.setTime(bean.getResultData().get(i).getDisableTime());
                        orderBean.setCreatetime(bean.getResultData().get(i).getCreatetime());
                        orderBean.setPksubscbptn(bean.getResultData().get(i).getPksubscbptn());
                        orderBean.setEarnestmoney(bean.getResultData().get(i).getEarnestmoney());
                        list.add(orderBean);
                    }


                    if (isRefresh == 1) {
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                            pullList.onRefreshComplete();
                        } else {
                            Log.e("liyunteee", "adapter-----------------");
                            mAdapter = new UnPayOrderAdapter(list, this, isShow, mListener);
                            mAdapter.notifyDataSetChanged();
                            pullList.onRefreshComplete();
                        }
                    } else if (isRefresh == 2) {
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                            pullList.onRefreshComplete();
                        } else {
                            mAdapter = new UnPayOrderAdapter(list, this, isShow, mListener);
                            mAdapter.notifyDataSetChanged();
                            pullList.onRefreshComplete();
                        }
                    } else {
                        mAdapter.add(list);
                        mAdapter.notifyDataSetChanged();
                        pullList.onRefreshComplete();
                    }
                } else {
                    pullList.onRefreshComplete();
                }
            } catch (IOException e) {
                Log.e("liyunte", "eee" + e);
                e.printStackTrace();
            }
            pullList.onRefreshComplete();
        }
        if (reqcode == 45) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            Log.e("liyunte", result.toString());
        }

//        mFragDialog.cancelDialog();
    }

    @Override
    public void onFailed(int reqcode, Object result) {
//        mFragDialog.cancelDialog();
    }

    @Override
    public void onError(VolleyError volleyError) {
        pullList.onRefreshComplete();

    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);
        flag = getIntent().getIntExtra("flag", 0);
        userId = getFromSharePreference(Config.userConfig.pkregister);
        setRequest(QUERY_REFERSH, flag);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }

    @Override
    public void isConntectedAndRefreshData() {
        setRequest(QUERY_REFERSH, flag);
    }
}
