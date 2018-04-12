package com.bjypt.vipcard.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.Interface.PetroleumViewCallBackListener;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.BusinessFinancingActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.PetroleumBean;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinia.orderlang.utils.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 崔龙 on 2018/1/17.
 * <p>
 * 石油广告
 */

public class PetroleumView extends LinearLayout implements VolleyCallBack {
    private ImageView mIv_poster;
    private static final int REQUEST_PETROLEUM_ENTRANCE = 201811701;
    private Context mContext;
    private String mPkmuser;
    private String mCardNum;
    private String mCategoryId;
    private PetroleumViewCallBackListener mListener;

    public PetroleumView(Context context) {
        super(context);
        initView(context);
    }

    public PetroleumView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PetroleumView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.petroleum_item, this);
        mIv_poster = (ImageView) findViewById(R.id.iv_poster);
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        switch (reqcode) {
            case REQUEST_PETROLEUM_ENTRANCE:
                loadData(result);
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        mIv_poster.setVisibility(GONE);
    }

    @Override
    public void onError(VolleyError volleyError) {
        mIv_poster.setVisibility(GONE);
    }

    /**
     * 请求购买石油理财的入口
     *
     * @param context 上下文
     * @param pkmuser pkmuser
     */
    public void requestPetroleumEntrance(final Context context, String pkmuser, String categoryId, String cardnum) {
        mContext = context;
        mPkmuser = pkmuser;
        mCategoryId = categoryId;
        mCardNum = cardnum;
        Map<String, String> params = new HashMap<>();
        params.put("pkmuser", pkmuser);
        if (null != categoryId) {
            params.put("categoryid", categoryId);
        }
        Wethod.httpPost(context, REQUEST_PETROLEUM_ENTRANCE, Config.web.request_petroleum, params, this, View.GONE);
    }

    /**
     * 加载解析正确返回数据
     *
     * @param result 数据
     */
    private void loadData(Object result) {
        Log.e("cl123", result + "");
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            PetroleumBean petroleumBean = objectMapper.readValue(result.toString(), PetroleumBean.class);
            if (StringUtil.isEmpty(petroleumBean.getResultData().getFinance_url())) {
                mIv_poster.setVisibility(GONE);
            } else {
                String URL = petroleumBean.getResultData().getFinance_url();
                setUrl(mContext, URL);
                if (mListener != null) {
                    mListener.hideView();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听网络请求成功的回调
     *
     * @param arg listener
     */
    public void setPetroleumViewCallBackListener(PetroleumViewCallBackListener arg) {
        mListener = arg;
    }

    /**
     * 请求网络获取图片
     *
     * @param context 上下文
     * @param URL     图片链接
     */
    public void setUrl(final Context context, String URL) {
        ImageLoader.getInstance().displayImage(URL, mIv_poster, AppConfig.DEFAULT_IMG_CITIZEN_CARD);
        mIv_poster.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusinessFinancingActivity.class);
                intent.putExtra("pkmuser", mPkmuser);
                intent.putExtra("categoryid", mCategoryId);
                intent.putExtra("cardnum", mCardNum);
                Log.e("Petro", "mCardNum: " + mCardNum + "mCategoryId:" + mCategoryId);
                context.startActivity(intent);
            }
        });
    }
}
