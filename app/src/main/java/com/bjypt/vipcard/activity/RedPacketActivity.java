package com.bjypt.vipcard.activity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.RedPacketAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.CoupnBean;
import com.bjypt.vipcard.model.CoupnRootBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.umeng.UmengCountContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/1.
 */
public class RedPacketActivity extends BaseActivity implements VolleyCallBack{

    private PullToRefreshListView listView;
    private RelativeLayout layout_back;

    /* pkregister:用户ID
   wctype：类型（1：红包、2：优惠券）
   begin：开始位置
   pageLength：长度*/
    private String url = Config.web.coupon;
    private String pkregister ;
    private String wctype = "1";
    private int begin;
    private int pageLength=10;
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;
    private int isrefresh;
    private Map<String ,String > map;
    private List<CoupnBean> list ;
    private RedPacketAdapter adapter;
    private ImageView iv_default_redpacket_pic;
    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_redpacket);
        pkregister = getFromSharePreference(Config.userConfig.pkregister);
        map = new HashMap<>();
        list = new ArrayList<>();

    }

    @Override
    public void beforeInitView() {

    }
    public void setRequest(int type){
        if (type==QUERY_REFERSH){
            if (map!=null){
                map.clear();
            }
            begin=0;
            isrefresh=1;
            map.put("pkregister",pkregister);
            map.put("wctype",wctype);
            map.put("begin",begin+"");
            map.put("pageLength",pageLength+"");
            Wethod.httpPost(RedPacketActivity.this,46, url, map, this);

        }else {
            if (map!=null){
                map.clear();
            }
            begin+=10;
            isrefresh=2;
            map.put("pkregister",pkregister);
            map.put("wctype",wctype);
            map.put("begin",begin+"");
            map.put("pageLength",pageLength+"");
            Wethod.httpPost(RedPacketActivity.this,46, url, map, this);
        }

    }

    @Override
    public void initView() {
        iv_default_redpacket_pic = (ImageView) findViewById(R.id.iv_default_redpacket_pic);
        listView = (PullToRefreshListView) findViewById(R.id.lv_redpacket);
        layout_back= (RelativeLayout) findViewById(R.id.layout_back);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    @Override
    public void afterInitView() {
        setRequest(QUERY_REFERSH);
        adapter = new RedPacketAdapter(this,list);
        listView.setAdapter(adapter);
    }

    @Override
    public void bindListener() {

        layout_back.setOnClickListener(this);
        /****
         * 上拉下拉双向监听事件
         */
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                setRequest(QUERY_REFERSH);
            }


            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                setRequest(QUERY_MORE);
            }
        });
    }

    @Override
    public void onClickEvent(View v) {

        switch (v.getId()){
            case R.id.layout_back:

                this.finish();
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode==46){
            Log.e("liyunteee", result.toString());
            try {
                CoupnRootBean rootBean = getConfiguration().readValue(result.toString(), CoupnRootBean.class);
                Log.e("liyunteee", "------------------------");

                if (rootBean.getResultData().size()==0&&list.size()==0){
                    listView.onRefreshComplete();
                    iv_default_redpacket_pic.setVisibility(View.VISIBLE);
                }
                else if(rootBean.getResultData().size()>0){
                    iv_default_redpacket_pic.setVisibility(View.GONE);
                    if(isrefresh==1){
                        if(adapter!=null){
                            Log.e("liyunteee", "!null----------------");
                            list.clear();
                        }
                    }
                    for (int i=0;i<rootBean.getResultData().size();i++){
                        CoupnBean bean= new CoupnBean();
                        bean.setEndtime(rootBean.getResultData().get(i).getEndtime());
                        bean.setExpirestatus(rootBean.getResultData().get(i).getExpirestatus());
                        bean.setMuname(rootBean.getResultData().get(i).getMuname());
                        bean.setMzmoney(rootBean.getResultData().get(i).getMzmoney());
                        bean.setStarttime(rootBean.getResultData().get(i).getStarttime());
                        bean.setTitle(rootBean.getResultData().get(i).getTitle());
                        bean.setWealmoney(rootBean.getResultData().get(i).getWealmoney());
                        bean.setWealstatus(rootBean.getResultData().get(i).getWealstatus());
                        list.add(bean);

                    }
                    if(isrefresh==1){
                        if(adapter !=null){
                            adapter.notifyDataSetChanged();
                            listView.onRefreshComplete();
                        }else{
                            Log.e("liyunteee","adapter-----------------");
                            adapter = new RedPacketAdapter(this,list);
                            adapter.notifyDataSetChanged();
                            listView.onRefreshComplete();
                        }
                    }else if(isrefresh==2){
                        if(adapter !=null){
                            adapter.notifyDataSetChanged();
                            listView.onRefreshComplete();
                        }else{
                            adapter = new RedPacketAdapter(this,list);
                            adapter.notifyDataSetChanged();
                            listView.onRefreshComplete();
                        }
                    }else{
                        adapter.add(list);
                        adapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    }
                }else{
                    listView.onRefreshComplete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            listView.onRefreshComplete();
        }


    }

    @Override
    public void onFailed(int reqcode, Object result) {

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
}
