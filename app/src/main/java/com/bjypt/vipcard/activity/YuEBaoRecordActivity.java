package com.bjypt.vipcard.activity;


import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.YuEBaoRecordAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.YuEBaoRecordAdapterBean;
import com.bjypt.vipcard.model.YuEBaoRecordRootBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YuEBaoRecordActivity extends BaseActivity implements VolleyCallBack{
private PullToRefreshListView plistview_yu_e_bao_record;
    private LinearLayout ly_yu_e_bao_record;
    private String url = Config.web.yu_e_bao_record;
    private String pkregister ;
    private int begin = 0;
    private int pageLength=10;
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;
    private int isrefresh;
    private Map<String ,String > map;
   private List<YuEBaoRecordAdapterBean> list = new ArrayList<>();
    private YuEBaoRecordAdapter adapter;

    @Override
    public void setContentLayout() {
setContentView(R.layout.activity_yu_ebao_record);

//        pkregister = "00452d1afcf04330b2d5db6f7c1deaa0";
        map = new HashMap<>();
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        ly_yu_e_bao_record = (LinearLayout) findViewById(R.id.ly_yu_e_bao_record);
        plistview_yu_e_bao_record = (PullToRefreshListView) findViewById(R.id.plistview_yu_e_bao_record);
        plistview_yu_e_bao_record.setMode(PullToRefreshBase.Mode.BOTH);
    }

    @Override
    public void afterInitView() {
        setRequest(QUERY_REFERSH);
        adapter = new YuEBaoRecordAdapter(this,list);
        plistview_yu_e_bao_record.setAdapter(adapter);
        plistview_yu_e_bao_record.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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
    public void bindListener() {

        ly_yu_e_bao_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void setRequest(int type){
        if (type==QUERY_REFERSH){
            if (map!=null){
                map.clear();
            }
            begin=0;
            isrefresh=1;
            map.put("pkRegister",getFromSharePreference(Config.userConfig.pkregister));
            map.put("pageIndex",begin+"");
            map.put("pageSize",pageLength+"");
            Wethod.httpPost(YuEBaoRecordActivity.this,46, url, map, this);

        }else {
            if (map!=null){
                map.clear();
            }
            begin+=10;
            isrefresh=2;
            map.put("pkRegister",getFromSharePreference(Config.userConfig.pkregister));
            map.put("pageIndex",begin+"");
            map.put("pageSize",pageLength+"");
            Wethod.httpPost(YuEBaoRecordActivity.this,46, url, map, this);
        }

    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode==46){
            Log.e("liyunteee", result.toString());
            try {
                YuEBaoRecordRootBean rootBean = getConfiguration().readValue(result.toString(), YuEBaoRecordRootBean.class);
                Log.e("liyunteee", "------------------------");
if (rootBean.getResultData()==null){
    plistview_yu_e_bao_record.onRefreshComplete();
} else if (rootBean.getResultData().size()==0&&list.size()==0){
                    plistview_yu_e_bao_record.onRefreshComplete();
//                    iv_default_redpacket_pic.setVisibility(View.VISIBLE);
                }
                else if(rootBean.getResultData().size()>0){
//                    iv_default_redpacket_pic.setVisibility(View.GONE);
                    if(isrefresh==1){
                        if(adapter!=null){
                            Log.e("liyunteee", "!null----------------");
                            list.clear();
                        }
                    }
                    for (int i=0;i<rootBean.getResultData().size();i++){
                        YuEBaoRecordAdapterBean bean= new YuEBaoRecordAdapterBean();
                        bean.setName(rootBean.getResultData().get(i).getProductname());
                        bean.setDays(rootBean.getResultData().get(i).getProductday());
                        if (rootBean.getResultData().get(i).getInvestmoney().contains(".")){
                            bean.setBuyprice(rootBean.getResultData().get(i).getInvestmoney().substring(0,rootBean.getResultData().get(i).getInvestmoney().indexOf(".")));
                        }else {
                            bean.setBuyprice(rootBean.getResultData().get(i).getInvestmoney()+"");
                        }
                        bean.setEndTime(rootBean.getResultData().get(i).getExpiredate());
                        bean.setStartTime(rootBean.getResultData().get(i).getInvestdate());
                        bean.setProgress(Integer.parseInt(rootBean.getResultData().get(i).getProgress()));
                        bean.setIsEnd(rootBean.getResultData().get(i).getInveststatus()+"");
                        bean.setShouyi(rootBean.getResultData().get(i).getIncomemoney()+"");
                        if (rootBean.getResultData().get(i).getProductrate().contains(".")){
                            bean.setYearLv(rootBean.getResultData().get(i).getProductrate().substring(0,rootBean.getResultData().get(i).getProductrate().indexOf(".")+2));
                        }else {
                            bean.setYearLv(rootBean.getResultData().get(i).getProductrate() + "");
                        }
                        list.add(bean);

                    }
                    if(isrefresh==1){
                        if(adapter !=null){
                            adapter.notifyDataSetChanged();
                            plistview_yu_e_bao_record.onRefreshComplete();
                        }else{
                            Log.e("liyunteee","adapter-----------------");
                            adapter = new YuEBaoRecordAdapter(this,list);
                            adapter.notifyDataSetChanged();
                            plistview_yu_e_bao_record.onRefreshComplete();
                        }
                    }else if(isrefresh==2){
                        if(adapter !=null){
                            adapter.notifyDataSetChanged();
                            plistview_yu_e_bao_record.onRefreshComplete();
                        }else{
                            adapter = new YuEBaoRecordAdapter(this,list);
                            adapter.notifyDataSetChanged();
                            plistview_yu_e_bao_record.onRefreshComplete();
                        }
                    }else{
                        adapter.add(list);
                        adapter.notifyDataSetChanged();
                        plistview_yu_e_bao_record.onRefreshComplete();
                    }
                }else{
                    plistview_yu_e_bao_record.onRefreshComplete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            plistview_yu_e_bao_record.onRefreshComplete();
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        plistview_yu_e_bao_record.onRefreshComplete();
    }

    @Override
    public void onError(VolleyError volleyError) {
        plistview_yu_e_bao_record.onRefreshComplete();
    }
   /* public static String getUserDate(String sformat) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(sformat));
        return  sf.format(c.getTime());
    }*/

   /* public static String getUserDate(String sformat) {
        Date currentTime = new Date();
        currentTime.setTime(Long.parseLong(sformat));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    public static int differentDaysByMillisecond(String date1,String date2)
    {
        int days = (int) ((Long.parseLong(date2)-Long.parseLong(date1)) / (1000*3600*24));
        return days;
    }*/
}
