package com.bjypt.vipcard.activity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.AssessShowAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.model.AssessShowBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.umeng.UmengCountContext;

import java.util.ArrayList;
import java.util.List;

public class AssessShowActivity extends BaseActivity {
    private View view;
    private RelativeLayout layout_1;
    private RelativeLayout layout_2;
    private RelativeLayout layout_3;
    private ImageView iv_1;
    private ImageView iv_2;
    private ImageView iv_3;
    private PullToRefreshListView listView;
    private LinearLayout layout_back;
    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;
    private TextView tv_fenshu;
    private List<AssessShowBean> list;
    private AssessShowAdapter adapter;


    @Override
    public void setContentLayout() {
        Log.v("TAG", "AssessShowActivity----------------setContentLayout");
        setContentView(R.layout.activity_assess_show);//activity_assess_show


    }

    @Override
    public void beforeInitView() {
        Log.v("TAG", "AssessShowActivity----------------beforeInitView");


    }

    @Override
    public void initView() {
        Log.v("TAG", "AssessShowActivity----------------initView");
        listView = (PullToRefreshListView) findViewById(R.id.lv_pingjia);

        listView.setMode(PullToRefreshBase.Mode.BOTH);
        layout_1 = (RelativeLayout) findViewById(R.id.relative_1);
        layout_2 = (RelativeLayout) findViewById(R.id.relative_2);
        layout_3 = (RelativeLayout) findViewById(R.id.relative_3);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_1.setTextColor(getResources().getColor(R.color.red_text_dishes));
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_fenshu = (TextView) findViewById(R.id.tv_fenshu);
        iv_2 = (ImageView) findViewById(R.id.iv_2);
        iv_3 = (ImageView) findViewById(R.id.iv_3);
        iv_1 = (ImageView) findViewById(R.id.iv_1);
        iv_1.setVisibility(View.VISIBLE);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);

    }

    @Override
    public void afterInitView() {
        Log.v("TAG", "AssessShowActivity----------------afterInitView");
        list = new ArrayList<AssessShowBean>();
        String url1 = "http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg";
        String url2 = "http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg";
        String url3 = "http://pic18.nipic.com/20111215/577405_080531548148_2.jpg";
        String url4 = "http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg";
        list.add(new AssessShowBean(url1, "J******1", "商家服务好，快递一天就到了，好评！", "2016-3-18 14:25", 80, "谢谢"));
        list.add(new AssessShowBean(url2, "J******2", "商家服务好，快递一天就到了，好评！", "2016-3-18 14:25", 50, ""));
        list.add(new AssessShowBean(url3, "J******3", "商家服务好，快递一天就到了，好评！", "2016-3-18 14:25", 35, "谢谢"));
        list.add(new AssessShowBean(url4, "J******4", "商家服务好，快递一天就到了，好评！", "2016-3-18 14:25", 100, ""));
//         adapter = new AssessShowAdapter(AssessShowActivity.this,list);
        listView.setAdapter(adapter);
    }

    @Override
    public void bindListener() {
        layout_back.setOnClickListener(this);
        layout_2.setOnClickListener(this);
        layout_1.setOnClickListener(this);
        layout_3.setOnClickListener(this);

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            /**
             * 全部评价
             */
            case R.id.relative_1:
                qieHuan(1);
                break;
            /**
             * 好评
             */
            case R.id.relative_2:
                qieHuan(2);
                break;
            /**
             * 其他
             */
            case R.id.relative_3:
                qieHuan(3);
                break;
            /**
             * 返回
             */
            case R.id.layout_back:
                finish();
                break;


        }
    }

    public void qieHuan(int flag) {
        if (flag == 1) {
            tv_1.setTextColor(getResources().getColor(R.color.red_text_dishes));
            tv_2.setTextColor(getResources().getColor(R.color.dark_black));
            tv_3.setTextColor(getResources().getColor(R.color.dark_black));
            iv_1.setVisibility(View.VISIBLE);
            iv_2.setVisibility(View.GONE);
            iv_3.setVisibility(View.GONE);
        } else if (flag == 2) {
            tv_1.setTextColor(getResources().getColor(R.color.dark_black));
            tv_2.setTextColor(getResources().getColor(R.color.red_text_dishes));
            tv_3.setTextColor(getResources().getColor(R.color.dark_black));
            iv_1.setVisibility(View.GONE);
            iv_2.setVisibility(View.VISIBLE);
            iv_3.setVisibility(View.GONE);
        } else {
            tv_1.setTextColor(getResources().getColor(R.color.dark_black));
            tv_2.setTextColor(getResources().getColor(R.color.dark_black));
            tv_3.setTextColor(getResources().getColor(R.color.red_text_dishes));
            iv_1.setVisibility(View.GONE);
            iv_2.setVisibility(View.GONE);
            iv_3.setVisibility(View.VISIBLE);
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
}
