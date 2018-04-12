package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.ShopItemAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.LikeProduct;
import com.bjypt.vipcard.model.OrderContentBean;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/30
 * Use by 发布评论
 */
public class IssueCommentActivity extends BaseActivity implements VolleyCallBack {

    private RadioGroup mStarGroup;
    private ImageView mRadioOne, mRadioTwo, mRadioThree, mRadioFour, mRadioFive;
    private RelativeLayout mRelaOne, mRelaTwo, mRelaThree, mRelaFour, mRelaFive;
    private ListView mShopList;
    private TextView mTrue;
    private String preorderId;
    private String pkmuser;
    private EditText mCommentDetails;
    private int starNum = 0;
    private OrderContentBean orderContentBean;
    private RelativeLayout mBack;
    private List<LikeProduct.likeProduct> mlikeproduct = new ArrayList<>();

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_issue_comment);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        preorderId = intent.getStringExtra("preorderId");
        pkmuser = intent.getStringExtra("pkmuser");

        Log.i("aaa", "preorderId=" + preorderId + "         pkmuser=" + pkmuser);
        Map<String, String> params = new HashMap<>();
        params.put("preorderId", preorderId);
        Wethod.httpPost(IssueCommentActivity.this, 1, Config.web.order_details, params, this);
    }

    @Override
    public void initView() {
//        mStarGroup = (RadioGroup) findViewById(R.id.star_group);
        mRadioOne = (ImageView) findViewById(R.id.radio_one);
        mRadioTwo = (ImageView) findViewById(R.id.radio_two);
        mRadioThree = (ImageView) findViewById(R.id.radio_three);
        mRadioFour = (ImageView) findViewById(R.id.radio_four);
        mRadioFive = (ImageView) findViewById(R.id.radio_five);

        mRelaOne = (RelativeLayout) findViewById(R.id.rela_one);
        mRelaTwo = (RelativeLayout) findViewById(R.id.rela_two);
        mRelaThree = (RelativeLayout) findViewById(R.id.rela_three);
        mRelaFour = (RelativeLayout) findViewById(R.id.rela_four);
        mRelaFive = (RelativeLayout) findViewById(R.id.rela_five);

        mShopList = (ListView) findViewById(R.id.shopList);
        mCommentDetails = (EditText) findViewById(R.id.comment_details_ed);
        mTrue = (TextView) findViewById(R.id.issue_comment);
        mBack = (RelativeLayout) findViewById(R.id.issue_comment_back);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        mRelaOne.setOnClickListener(this);
        mRelaTwo.setOnClickListener(this);
        mRelaThree.setOnClickListener(this);
        mRelaFour.setOnClickListener(this);
        mRelaFive.setOnClickListener(this);
        mTrue.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.issue_comment_back:
                finish();
                break;

            case R.id.rela_one:
                mRadioOne.setBackgroundResource(R.mipmap.comment_star_click);
                mRadioTwo.setBackgroundResource(R.mipmap.comment_star);
                mRadioThree.setBackgroundResource(R.mipmap.comment_star);
                mRadioFour.setBackgroundResource(R.mipmap.comment_star);
                mRadioFive.setBackgroundResource(R.mipmap.comment_star);
                starNum = 1;
                break;
            case R.id.rela_two:
                mRadioOne.setBackgroundResource(R.mipmap.comment_star_click);
                mRadioTwo.setBackgroundResource(R.mipmap.comment_star_click);
                mRadioThree.setBackgroundResource(R.mipmap.comment_star);
                mRadioFour.setBackgroundResource(R.mipmap.comment_star);
                mRadioFive.setBackgroundResource(R.mipmap.comment_star);
                starNum = 2;
                break;
            case R.id.rela_three:
                mRadioOne.setBackgroundResource(R.mipmap.comment_star_click);
                mRadioTwo.setBackgroundResource(R.mipmap.comment_star_click);
                mRadioThree.setBackgroundResource(R.mipmap.comment_star_click);
                mRadioFour.setBackgroundResource(R.mipmap.comment_star);
                mRadioFive.setBackgroundResource(R.mipmap.comment_star);
                starNum = 3;
                break;
            case R.id.rela_four:
                mRadioOne.setBackgroundResource(R.mipmap.comment_star_click);
                mRadioTwo.setBackgroundResource(R.mipmap.comment_star_click);
                mRadioThree.setBackgroundResource(R.mipmap.comment_star_click);
                mRadioFour.setBackgroundResource(R.mipmap.comment_star_click);
                mRadioFive.setBackgroundResource(R.mipmap.comment_star);
                starNum = 4;
                break;
            case R.id.rela_five:
                mRadioOne.setBackgroundResource(R.mipmap.comment_star_click);
                mRadioTwo.setBackgroundResource(R.mipmap.comment_star_click);
                mRadioThree.setBackgroundResource(R.mipmap.comment_star_click);
                mRadioFour.setBackgroundResource(R.mipmap.comment_star_click);
                mRadioFive.setBackgroundResource(R.mipmap.comment_star_click);
                starNum = 5;
                break;
            case R.id.issue_comment:
                if (starNum < 1) {
                    Toast.makeText(this, "请选择星级", Toast.LENGTH_LONG).show();
                } else if (mCommentDetails.getText().toString().equals("") || mCommentDetails.getText().toString() == null) {
                    Toast.makeText(this, "请留下您宝贵的意见", Toast.LENGTH_LONG).show();
                } else {
                    if (mCommentDetails.getText().toString().length() < 4) {
                        Toast.makeText(this, "评论至少5个字哦亲", Toast.LENGTH_LONG).show();
                    } else {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("preorderId", preorderId);//订单主键
                        params.put("pkmuser", pkmuser);//商家主键
                        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));//用户主键
                        params.put("score", starNum + "");//评分
                        params.put("content", mCommentDetails.getText().toString());//评论内容

                        mlikeproduct = new ArrayList<>();
                        LikeProduct mLikeProduct = new LikeProduct();
                        for (int i = 0; i < orderContentBean.getResultData().size(); i++) {
                            if (ShopItemAdapter.params.get(i) == true) {

                                LikeProduct.likeProduct m = mLikeProduct.new likeProduct(orderContentBean.getResultData().get(i).getProductId(),
                                        orderContentBean.getResultData().get(i).getDetailType(),
                                        orderContentBean.getResultData().get(i).getOrderCount());

                                mlikeproduct.add(m);
                            }
                        }
                        mLikeProduct.setLikeProduct(mlikeproduct);
                        String likeProduct = new Gson().toJson(mLikeProduct);
                        Log.i("aaa", likeProduct);
                        params.put("likeProduct", likeProduct);//点赞商品
                        Wethod.httpPost(IssueCommentActivity.this, 2, Config.web.add_comment, params, this);
                    }
                }

                //发布评论
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        Log.i("aaa", ">>>" + result.toString());
        if (reqcode == 1) {
            try {
                orderContentBean = getConfiguration().readValue(result.toString(), OrderContentBean.class);
                ShopItemAdapter shopItemAdapter = new ShopItemAdapter(orderContentBean.getResultData(), this);
                mShopList.setAdapter(shopItemAdapter);
                setListViewHeightBasedOnChildren(mShopList);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (reqcode == 2) {
            Toast.makeText(this, "评论成功", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        Log.i("aaa", "on Failed");
        if (reqcode == 1) {
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == 2) {
            Wethod.ToFailMsg(this, result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    /*
 * 动态设置ListView组建的高度
 *
 * */
    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {

            return;

        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0);

            totalHeight += listItem.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight

                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);

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
}
