package com.bjypt.vipcard.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.CircleMyActivity;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.dialog.NormalDialog;
import com.bjypt.vipcard.model.AttentionBean;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.MyListView;
import com.bjypt.vipcard.view.RoundImageView;
import com.bjypt.vipcard.view.ToastUtil;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bjypt.vipcard.R.id.attention_status;

@SuppressLint("ValidFragment")
public class FansFragment extends BaseFrament implements VolleyCallBack<String> {

    private MyListView attention_mylistview;
    private LinearLayout attention_ivbg;
    private View view;
    private AttentionAdapter attentionAdapter;
    private static final int Fans_Data_Request = 0010;
    private static final int Cancle_Attention = 0011;
    private static final int Confirm_Attention = 0001;
    private AttentionBean attentionBean = new AttentionBean();
    private TextView more_tv;
    List<AttentionBean.ResultDataBean> adapterList = new ArrayList<>();
    private LinearLayout zhanwei_linear;
    private CircleProgressBar fansProgressBar;

    private int page = 0;                                          //起始条数
    final int QUERY_FANS_MORE = 0x0101;
    final int QUERY_FANS_REFERSH = 0x0110;
    private boolean is_refresh = true;
    private BroadCastReceiverUtils mBroadCastReceiverUtils;
    private String myUid;

    public FansFragment() {
    }

    public FansFragment(String myUid) {
        this.myUid = myUid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attention_and_fans, container, false);
        return view;
    }

    @Override
    public void beforeInitView() {
        mBroadCastReceiverUtils = new BroadCastReceiverUtils();
    }


    @Override
    public void initView() {

        //进度条
        fansProgressBar = (CircleProgressBar) view.findViewById(R.id.progressbar);
        fansProgressBar.setColorSchemeResources(R.color.gallery_black);

        zhanwei_linear = (LinearLayout) view.findViewById(R.id.zhanwei_linear);
        attention_ivbg = (LinearLayout) view.findViewById(R.id.attention_ivbg);
        attention_mylistview = (MyListView) view.findViewById(R.id.attention_mylistview);
        more_tv = (TextView) view.findViewById(R.id.more_tv);
        attentionAdapter = new AttentionAdapter();
        attention_mylistview.setAdapter(attentionAdapter);
    }


    @Override
    public void afterInitView() {
        fansProgressBar.setVisibility(View.VISIBLE);
        getFansData(QUERY_FANS_REFERSH);
    }

    @Override
    public void bindListener() {
        more_tv.setOnClickListener(this);
        attention_mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //点击item跳转他的
                Intent intent = new Intent(getActivity(), CircleMyActivity.class);
                intent.putExtra("uid", attentionBean.getResultData().get(i).getFollowUser().getUid() + "");
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.more_tv:
                getFansData(QUERY_FANS_MORE);
                break;
        }
    }


    @Override
    public void onSuccess(int reqcode, String result) {
        if (isVisible()) {
            CircleMyActivity circleMyActivity = (CircleMyActivity) getActivity();
            circleMyActivity.onRefreshComplete();
        }
        fansProgressBar.setVisibility(View.GONE);
        if (reqcode == Fans_Data_Request) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();

            try {
                attentionBean = objectMapper.readValue(result.toString(), AttentionBean.class);
                if (adapterList.size() != 0) {
                    adapterList.clear();
                    adapterList.addAll(attentionBean.getResultData());
                } else {
                    adapterList.addAll(attentionBean.getResultData());
                }

                if (adapterList.size() == 0) {                //没有数据显示默认的，暂无内容
                    attention_mylistview.setVisibility(View.GONE);
                    attention_ivbg.setVisibility(View.VISIBLE);
                    zhanwei_linear.setVisibility(View.GONE);
                } else {
                    attention_mylistview.setVisibility(View.VISIBLE);
                    attention_ivbg.setVisibility(View.GONE);
                    zhanwei_linear.setVisibility(View.VISIBLE);
                    attentionAdapter.notifyDataSetChanged();
                }

                if (adapterList.size() < 10) {          //数据小于十条，不显示加载更多
                    more_tv.setVisibility(View.GONE);
                } else {
                    more_tv.setVisibility(View.VISIBLE);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == Cancle_Attention) {
            ToastUtil.showToast(getActivity(), "取消关注成功");
            getFansData(QUERY_FANS_REFERSH);
            mBroadCastReceiverUtils.sendBroadCastReceiver(getActivity(), "RESULT_OK");
        } else if (reqcode == Confirm_Attention) {
            ToastUtil.showToast(getActivity(), "关注成功");
            getFansData(QUERY_FANS_REFERSH);
            mBroadCastReceiverUtils.sendBroadCastReceiver(getActivity(), "RESULT_OK");
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {
        if (reqcode == Fans_Data_Request) {
            more_tv.setVisibility(View.GONE);
            attention_mylistview.setVisibility(View.GONE);
            attention_ivbg.setVisibility(View.VISIBLE);
        } else if (reqcode == Cancle_Attention) {
            ToastUtil.showToast(getActivity(), "取消关注失败");
        } else if (reqcode == Confirm_Attention) {
            ToastUtil.showToast(getActivity(), "关注失败");
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    /*mlist的适配器*/
    class AttentionAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return adapterList.size();
        }

        @Override
        public Object getItem(int i) {
            return adapterList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                holder = new ViewHolder();
                view = View.inflate(getActivity(), R.layout.layout_attention_item, null);
                holder.attention_status = (TextView) view.findViewById(attention_status);
                holder.header_name = (TextView) view.findViewById(R.id.header_name);
                holder.header_iv = (RoundImageView) view.findViewById(R.id.header_iv);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            if (adapterList.get(i).getBkname() == null || adapterList.get(i).getBkname().equals("")) {
                holder.header_name.setText(adapterList.get(i).getFollowUser().getUsername());
            } else {
                holder.header_name.setText(adapterList.get(i).getBkname());
            }

            if (getFromSharePreference(Config.userConfig.uid).equals(myUid)) {
                //我的界面
                holder.attention_status.setVisibility(View.VISIBLE);
                if (adapterList.get(i).getMutual() != null && adapterList.get(i).getMutual().equals("1")) {
                    //0:单向 1:已互相关注
                    holder.attention_status.setText("相互关注");
                    holder.attention_status.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDialog("确定取消关注此人？", 1, adapterList.get(i).getFollowUser().getUid());
                        }
                    });
                } else {
                    holder.attention_status.setText("+关注");
                    holder.attention_status.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDialog("确定添加关注？", 0, adapterList.get(i).getFollowUser().getUid());
                        }
                    });
                }
            } else {
                //他的界面
                holder.attention_status.setVisibility(View.GONE);
//                holder.attention_status.setText("+关注");
//                holder.attention_status.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        showDialog("确定添加关注？", 0, adapterList.get(i).getFollowUser().getUid());
//                    }
//                });
            }


            if (adapterList.get(i).getFollowUser().getAvatar() != null) {
                ImageLoader.getInstance().displayImage(adapterList.get(i).getFollowUser().getAvatar(), holder.header_iv, AppConfig.CIRCLE_HEADER);
            } else {
                holder.header_iv.setImageResource(R.drawable.header_yellow);
            }
            return view;
        }

        class ViewHolder {
            TextView header_name;
            TextView attention_status;
            RoundImageView header_iv;
        }
    }

    /* 显示对话框 是否取消关注*/
    private void showDialog(String string, final int s, final int userUid) {
        final NormalDialog mNormalDialog = NormalDialog.getInstance(getActivity());
        mNormalDialog.setDesc(string);
        mNormalDialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s == 1) {
                    mNormalDialog.dismiss();
                    //取消关注
                    cancleAttention(userUid);
                } else {
                    mNormalDialog.dismiss();
                    //关注
                    insertAttention(userUid);
                }
            }
        });
        mNormalDialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNormalDialog.dismiss();
            }
        });
    }

    /* 添加关注接口请求*/
    private void insertAttention(int userUid) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", myUid);                          //uid
        params.put("followUid", userUid + "");
        params.put("system_code", "android");
        Wethod.httpPost(getActivity(), Confirm_Attention, Config.web.confirm_attention, params, this,View.GONE);
    }

    /* 取消关注的 数据请求 */
    private void cancleAttention(int userUid) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", myUid);                          //uid
        params.put("followUid", userUid + "");
        params.put("system_code", "android");
        Wethod.httpPost(getActivity(), Cancle_Attention, Config.web.cancle_attention, params, this,View.GONE);
    }

    //粉丝列表数据请求
    public void getFansData(int refresh_type) {
        if (refresh_type == QUERY_FANS_REFERSH) {
            page = 0;
            is_refresh = true;
        } else {
            page++;
            is_refresh = false;
        }

        Map<String, String> params = new HashMap<>();
        params.put("uid", myUid);                          //uid
        params.put("system_code", "android");
        params.put("page", page + "");
        params.put("size", "10");
        Wethod.httpPost(getActivity(), Fans_Data_Request, Config.web.getFansList, params, this,View.GONE);
    }

}
