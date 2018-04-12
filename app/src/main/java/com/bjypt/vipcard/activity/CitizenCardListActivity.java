package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.CitizenCanCardListAdapter;
import com.bjypt.vipcard.adapter.CitizenCardListAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.dialog.ReminderDialog;
import com.bjypt.vipcard.model.CitizenCardListBean;
import com.bjypt.vipcard.model.CitizenCardListCanBindBean;
import com.bjypt.vipcard.model.CommentDetailsClBean;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.MyListViewFormScollView;
import com.bjypt.vipcard.view.ToastUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CitizenCardListActivity extends BaseActivity implements VolleyCallBack<String> {

    private static final String TAG = "CitizenCardListActivity";

    private RelativeLayout citizen_card_back;                //返回
    private LinearLayout ll_more_card;                          //我的卡包
    private LinearLayout ll_more_can_card;                      //领取更多卡
    private ImageView citizen_card_add;                      //右上角添加市民卡
    private LinearLayout ll_card_ok;                         //已经领取的卡列表
    private LinearLayout ll_card_no;                         //未领取的卡列表
    private LinearLayout iv_add_nocard;                      //没有卡列表时点击激活按钮
    private LinearLayout iv_add_card;                        //有卡列表时点击激活按钮
    private RelativeLayout no_card_relative;                 //没有卡列表时显示的布局
    private TextView txt_can_bind, txt_can_bind2;                          // 绑卡提示
    private CitizenCardListCanBindBean successBean;
    private String msg;
    private View view1;

    private MyListViewFormScollView have_card_listview;                    //有卡列表时显示的listview
    private MyListViewFormScollView have_card_listview2;                   //有卡列表时显示的listview
    private CitizenCardListAdapter myAdapter;

    private static final int request_card_list = 1111;
    private static final int request_can_bind = 1110;
    private static final int request_card_manager_code = 2;
    private List<CitizenCardListBean.ResultDataBean.BindlistBean> mBindlist = new ArrayList<>();
    private List<CitizenCardListBean.ResultDataBean.CanBeReceivedListBean> mCanBeReceivedList;
    private CitizenCanCardListAdapter mCitizenCanCardListAdapter;
    private static final int request_receivedCard = 2222;
    private ImageView iv_soso;
    private LinearLayout linear_have_card;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_citizen_card_list);
    }

    @Override
    public void beforeInitView() {
        requestBindCard();
//        requestCardDoubleList();
    }

    @Override
    public void initView() {
        linear_have_card = (LinearLayout) findViewById(R.id.linear_have_card);
        citizen_card_back = (RelativeLayout) findViewById(R.id.citizen_card_back);
        citizen_card_add = (ImageView) findViewById(R.id.citizen_card_add);
        iv_add_nocard = (LinearLayout) findViewById(R.id.iv_add_nocard);
        ll_more_card = (LinearLayout) findViewById(R.id.ll_more_card);
        ll_more_can_card = (LinearLayout) findViewById(R.id.ll_more_can_card);
        ll_card_ok = (LinearLayout) findViewById(R.id.ll_card_ok);
        ll_card_no = (LinearLayout) findViewById(R.id.ll_card_no);
        no_card_relative = (RelativeLayout) findViewById(R.id.no_card_relative);
        have_card_listview = (MyListViewFormScollView) findViewById(R.id.have_card_listview);
        have_card_listview2 = (MyListViewFormScollView) findViewById(R.id.have_card_listview2);

        View footerview = LayoutInflater.from(this).inflate(R.layout.citizen_card_footerview, null);
        View footerview2 = LayoutInflater.from(this).inflate(R.layout.citizen_card_footerview2, null);
        iv_add_card = (LinearLayout) footerview.findViewById(R.id.iv_add_card);
        txt_can_bind = (TextView) footerview.findViewById(R.id.txt_can_bind);
        txt_can_bind2 = (TextView) findViewById(R.id.txt_can_bind2);
        view1 = footerview2.findViewById(R.id.view1);
        iv_soso = (ImageView) footerview2.findViewById(R.id.iv_soso);
        have_card_listview.setDividerHeight(0);
        have_card_listview.addFooterView(footerview);
        have_card_listview2.setDividerHeight(0);
        myAdapter = new CitizenCardListAdapter(this, mBindlist);
        have_card_listview.setAdapter(myAdapter);
//        have_card_listview2.addFooterView(footerview2);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        citizen_card_back.setOnClickListener(this);
        citizen_card_add.setOnClickListener(this);
        iv_add_nocard.setOnClickListener(this);
        iv_add_card.setOnClickListener(this);
        iv_soso.setOnClickListener(this);
        view1.setOnClickListener(this);
        ll_more_card.setOnClickListener(this);
        ll_more_can_card.setOnClickListener(this);

        have_card_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                if (i < mBindlist.size()) {
                    Intent intent = new Intent(CitizenCardListActivity.this, CardManagementActivity.class);
                    intent.putExtra("cardNum", mBindlist.get(i).getCardnum());
                    intent.putExtra("cardName", mBindlist.get(i).getCategory_name());
                    intent.putExtra("cardAmount", mBindlist.get(i).getAmount());
                    intent.putExtra("cardVirtualAmount", mBindlist.get(i).getVirtual_amount());
                    intent.putExtra("status", mBindlist.get(i).getStatus());
                    intent.putExtra("card_pic", mBindlist.get(i).getCard_pic());
                    intent.putExtra("scope_type", mBindlist.get(i).getScope_type());
                    intent.putExtra("showCardNum", mBindlist.get(i).getShowCardNum());
                    intent.putExtra("categoryId",mBindlist.get(i).getCategoryid());
                    Log.e("yang", "onItemClick: "+mBindlist.get(i).getCategoryid());
                    startActivityForResult(intent, request_card_manager_code);
                }
            }
        });

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.citizen_card_back:                   //返回
                finish();
                break;
            case R.id.citizen_card_add:                     //右上角添加市民卡
            case R.id.iv_add_nocard:                        //没有卡列表时点击激活按钮
            case R.id.iv_add_card:                          //有卡列表时点击激活按钮

                if (!msg.equals("Y")) {
                    final ReminderDialog dialog = new ReminderDialog(this);
                    dialog.setTitle(msg);
                    dialog.setOnNegativeListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    Intent intent = new Intent(this, BandCitizenCardActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.iv_soso:
            case R.id.ll_more_can_card:
                Intent intent = new Intent(CitizenCardListActivity.this, CardSearchActivity.class);
                intent.putExtra("flag", "2");
                startActivity(intent);
                break;
            case R.id.view1:
            case R.id.ll_more_card:
                Intent intent2 = new Intent(CitizenCardListActivity.this, CardSearchActivity.class);
                intent2.putExtra("flag", "1");
                startActivity(intent2);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestCardDoubleList();
    }

    @Override
    public void onSuccess(int reqcode, String result) {

        switch (reqcode) {
            case request_card_list:
                loadCardList(result);
                break;
            case request_receivedCard:
                receivedCardSuccess();
                break;
            case request_can_bind:
                loadCanBind(result);
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {
        switch (reqcode) {
            case request_receivedCard:
                Log.e(TAG, "====== 领卡失败： ======" + result);
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                try {
                    CommentDetailsClBean commentDetailsClBean = objectMapper.readValue(result.toString(), CommentDetailsClBean.class);
                    String resultData = commentDetailsClBean.getResultData();
                    ToastUtil.showToast(this, resultData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case request_can_bind:
                try {
                    CitizenCardListCanBindBean failed = getConfiguration().readValue(result.toString(), CitizenCardListCanBindBean.class);
                    txt_can_bind.setText(Html.fromHtml(failed.getResultData().getTip()));
                    msg = failed.getResultData().getMsg();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request_card_manager_code && resultCode == RESULT_OK) {
            finish();
            MainActivity.tab_select_flag = 1;
        }
    }

    /**
     * 是否还可以绑卡n
     */
    private void requestBindCard() {
        //是否还可以绑定市民卡
        Map<String, String> param = new HashMap<>();
        param.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(CitizenCardListActivity.this, request_can_bind, Config.web.can_bind, param, this);
    }

    /**
     * 请求双列表显示网络
     */
    private void requestCardDoubleList() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
//        params.put("pkregister", "03921e20078b4a20b04f7893ccf00bbd");
        params.put("search_type", "");
        Wethod.httpPost(CitizenCardListActivity.this, request_card_list, Config.web.getSearchCardList, params, this);
    }

    /**
     * 加载卡列表数据
     *
     * @param result 数据源
     */
    private void loadCardList(String result) {
        if (mBindlist != null) {
            mBindlist.clear();
        }
        if (mCanBeReceivedList != null) {
            mCanBeReceivedList.clear();
        }
        Log.e(TAG, "loadCardList =====::=====" + result);
        try {
            CitizenCardListBean citizenCardListBean = getConfiguration().readValue(result.toString(), CitizenCardListBean.class);

            mBindlist.addAll(citizenCardListBean.getResultData().getBindlist());                      // 绑定卡列表
            mCanBeReceivedList = citizenCardListBean.getResultData().getCanBeReceivedList();    // 未绑定列表
            int canBeReceivedListSize = citizenCardListBean.getResultData().getCanBeReceivedListSize();

            if ((mBindlist == null || mBindlist.size() == 0)&&(mCanBeReceivedList == null || mCanBeReceivedList.size() == 0)){
                no_card_relative.setVisibility(View.VISIBLE);
                linear_have_card.setVisibility(View.GONE);
            }else {
                no_card_relative.setVisibility(View.GONE);
                linear_have_card.setVisibility(View.VISIBLE);
            }

            if (mBindlist == null || mBindlist.size() == 0) {
//                have_card_listview.setVisibility(View.GONE);
//                ll_card_ok.setVisibility(View.GONE);
                ll_more_card.setVisibility(View.GONE);
            } else {
                have_card_listview.setVisibility(View.VISIBLE);
                ll_card_ok.setVisibility(View.VISIBLE);
                ll_more_card.setVisibility(View.VISIBLE);
                myAdapter.notifyDataSetChanged();
            }

            if (mCanBeReceivedList == null || mCanBeReceivedList.size() == 0) {
                ll_card_no.setVisibility(View.GONE);
                ll_more_can_card.setVisibility(View.GONE);

            } else if (canBeReceivedListSize <= 2 && canBeReceivedListSize > 0) {
                ll_card_no.setVisibility(View.VISIBLE);
                ll_more_can_card.setVisibility(View.GONE);
                mCitizenCanCardListAdapter = new CitizenCanCardListAdapter(this, mCanBeReceivedList);
                have_card_listview2.setAdapter(mCitizenCanCardListAdapter);
                mCitizenCanCardListAdapter.notifyDataSetChanged();
            } else {
                ll_card_no.setVisibility(View.VISIBLE);
                ll_more_can_card.setVisibility(View.VISIBLE);
                mCitizenCanCardListAdapter = new CitizenCanCardListAdapter(this, mCanBeReceivedList);
                have_card_listview2.setAdapter(mCitizenCanCardListAdapter);
                mCitizenCanCardListAdapter.notifyDataSetChanged();
            }
//            myAdapter.notifyDataSetChanged();
//            mCitizenCanCardListAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求成功的操作
     */
    private void receivedCardSuccess() {
        ToastUtil.showToast(this, "领卡成功");
        requestCardDoubleList();
    }

    /**
     * adapter 向 Activity 调用的方法
     *
     * @param i 当前哪个条目被点击
     */
    public void requestReceivedCard(int i) {
        Map<String, String> param = new HashMap<>();
        param.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        param.put("categoryid", mCanBeReceivedList.get(i).getCategoryid() + "");
        Wethod.httpPost(CitizenCardListActivity.this, request_receivedCard, Config.web.receivedCard, param, CitizenCardListActivity.this);
    }

    /**
     * 加载绑卡数据
     *
     * @param result
     */
    private void loadCanBind(String result) {
        try {
            successBean = getConfiguration().readValue(result.toString(), CitizenCardListCanBindBean.class);
            Spanned spanned = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                spanned = Html.fromHtml(successBean.getResultData().getTip(), Html.FROM_HTML_MODE_COMPACT);
            } else {
                spanned = Html.fromHtml(successBean.getResultData().getTip());
            }
            txt_can_bind.setText(spanned);
            txt_can_bind2.setText(spanned);
            msg = successBean.getResultData().getMsg();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
