package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.CitizenCardListAdapter2;
import com.bjypt.vipcard.adapter.CitizenSearchCardListAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.CommentDetailsClBean;
import com.bjypt.vipcard.model.MyBindlistBean;
import com.bjypt.vipcard.model.MyCanBeReceivedListBean;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.ToastUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 崔龙 on 2017/10/19.
 */

public class CardSearchActivity extends BaseActivity implements VolleyCallBack {

    private static final int REQUEST_SEARCH_LIST = 22222;
    private static final int request_receivedCard = 2222;
    private static final int request_card_manager_code = 2;
    private static final String TAG = "CardSearchActivity";

    private RelativeLayout citizen_card_back;
    private RelativeLayout rl_search_card;
    private LinearLayout ll_hint;
    private ListView lv_card_search;
    private CitizenSearchCardListAdapter adapter;
    private CitizenCardListAdapter2 myAdapter;
    private ImageView iv_sousuo;
    private EditText withdraw_money;
    private TextView tv_hint;
    private TextView tv_go_go;
    private View search_ok;
    private String flag;
    private List<MyBindlistBean.ResultDataBean.BindlistBean> mBindlist;
    private List<MyCanBeReceivedListBean.ResultDataBean.CanBeReceivedListBean> mCanBeReceivedList;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_card_search);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");

        requestCardDoubleList(flag);

    }

    @Override
    public void initView() {
        ll_hint = (LinearLayout) findViewById(R.id.ll_hint);
        lv_card_search = (ListView) findViewById(R.id.lv_card_search);
        lv_card_search.setDividerHeight(0);
        citizen_card_back = (RelativeLayout) findViewById(R.id.citizen_card_back);
        rl_search_card = (RelativeLayout) findViewById(R.id.rl_search_card);
        iv_sousuo = (ImageView) findViewById(R.id.iv_sousuo);
        withdraw_money = (EditText) findViewById(R.id.withdraw_money);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        tv_go_go = (TextView) findViewById(R.id.tv_go_go);
        search_ok = findViewById(R.id.search_ok);
        rl_search_card.setVisibility(View.VISIBLE);
        if (flag.equals("2")) {
            tv_hint.setText("搜索未领取卡");
        } else {
            tv_hint.setText("搜索已领取卡");
        }
//        withdraw_money.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        withdraw_money.setSelection(withdraw_money.getText().length());
        withdraw_money.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    //完成自己的事件
                    requestAgainCardList();
                }
                return false;
            }
        });
        withdraw_money.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //获取焦点
                    ll_hint.setVisibility(View.GONE);
                } else if (!hasFocus && withdraw_money.getText().toString().equals("")) {
                    ll_hint.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void afterInitView() {
        if (flag.equals("1")) {
            lv_card_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(CardSearchActivity.this, CardManagementActivity.class);
                    intent.putExtra("cardNum", mBindlist.get(position).getCardnum());
                    intent.putExtra("cardName", mBindlist.get(position).getCategory_name());
                    intent.putExtra("cardAmount", mBindlist.get(position).getAmount());
                    intent.putExtra("cardVirtualAmount", mBindlist.get(position).getVirtual_amount());
                    intent.putExtra("status", mBindlist.get(position).getStatus());
                    intent.putExtra("card_pic", mBindlist.get(position).getCard_pic());
                    intent.putExtra("scope_type", mBindlist.get(position).getScope_type());
                    startActivityForResult(intent, request_card_manager_code);
                }
            });
        }

    }

    @Override
    public void bindListener() {
        citizen_card_back.setOnClickListener(this);
        tv_go_go.setOnClickListener(this);
        search_ok.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.citizen_card_back:
                finish();
                break;
            case R.id.tv_go_go:
            case R.id.search_ok:
                requestAgainCardList();
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        switch (reqcode) {
            case REQUEST_SEARCH_LIST:
                Log.e(TAG, "result::" + result);
                loadListData(result);
                break;
            case request_receivedCard:
                receivedCardSuccess();
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
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
     * 请求未领取卡片列表
     *
     * @param flag
     */
    private void requestCardDoubleList(String flag) {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
//        params.put("pkregister", "03921e20078b4a20b04f7893ccf00bbd");
        params.put("search_type", flag);
        Wethod.httpPost(CardSearchActivity.this, REQUEST_SEARCH_LIST, Config.web.getSearchCardList, params, this);
    }

    /**
     * 加载网络数据
     *
     * @param result
     */
    private void loadListData(Object result) {
        if (mBindlist != null) {
            mBindlist.clear();
        }
        if (mCanBeReceivedList != null) {
            mCanBeReceivedList.clear();
        }
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        if (flag.equals("1")) {
            try {
                MyBindlistBean myBindlistBean = objectMapper.readValue(result.toString(), MyBindlistBean.class);
                mBindlist = myBindlistBean.getResultData().getBindlist();
                myAdapter = new CitizenCardListAdapter2(this, mBindlist);
                lv_card_search.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                MyCanBeReceivedListBean myCanBeReceivedListBean = objectMapper.readValue(result.toString(), MyCanBeReceivedListBean.class);
                mCanBeReceivedList = myCanBeReceivedListBean.getResultData().getCanBeReceivedList();
                adapter = new CitizenSearchCardListAdapter(this, mCanBeReceivedList);
                lv_card_search.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * adapter 向 Activity 调用的方法
     *
     * @param i 当前哪个条目被点击
     */
    public void requestReceivedCard(int i) {
        Map<String, String> param = new HashMap<>();
        param.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
//        param.put("pkregister", "03921e20078b4a20b04f7893ccf00bbd");
        param.put("categoryid", mCanBeReceivedList.get(i).getCategoryid() + "");
        Wethod.httpPost(CardSearchActivity.this, request_receivedCard, Config.web.receivedCard, param, CardSearchActivity.this);
    }

    /**
     * 请求成功的操作
     */
    private void receivedCardSuccess() {
        ToastUtil.showToast(this, "领卡成功");
        requestCardDoubleList(flag);
    }

    /**
     * 请求搜索的结果
     */
    private void requestAgainCardList() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
//        params.put("pkregister", "03921e20078b4a20b04f7893ccf00bbd");
        params.put("search_type", flag);
        params.put("categoryname", withdraw_money.getText().toString().trim());
        Wethod.httpPost(CardSearchActivity.this, REQUEST_SEARCH_LIST, Config.web.getSearchCardList, params, this);
    }
}
