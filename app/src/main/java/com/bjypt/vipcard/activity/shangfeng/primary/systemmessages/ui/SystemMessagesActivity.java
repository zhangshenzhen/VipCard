package com.bjypt.vipcard.activity.shangfeng.primary.systemmessages.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.adapter.MessagesRcvAdapter;
import com.bjypt.vipcard.activity.shangfeng.base.BaseActivity;
import com.bjypt.vipcard.activity.shangfeng.common.PageNumberSetting;
import com.bjypt.vipcard.activity.shangfeng.common.enums.ResultCode;
import com.bjypt.vipcard.activity.shangfeng.common.enums.UserInformationFields;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MessageBean;
import com.bjypt.vipcard.activity.shangfeng.primary.systemmessages.contract.SystemMessagesContract;
import com.bjypt.vipcard.activity.shangfeng.primary.systemmessages.contract.impl.SystemMessagesPresenterImpl;
import com.bjypt.vipcard.activity.shangfeng.util.SharedPreferencesUtils;
import com.githang.statusbar.StatusBarCompat;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 消息
 */
public class SystemMessagesActivity extends BaseActivity implements SystemMessagesContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.ll_system_message_back)
    LinearLayout ll_system_message_back;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    /**
     * 消息列表父布局
     */
    @BindView(R.id.ll_message_list)
    LinearLayout ll_message_list;

    @BindView(R.id.rcv_list)
    RecyclerView rcv_list;
    /**
     * 加载失败父布局
     */
    @BindView(R.id.ll_error)
    LinearLayout ll_error;
    /**
     * 错误提示、重新加载
     */
    @BindView(R.id.tv_error_hint)
    TextView tv_error_hint;


    private SystemMessagesPresenterImpl presenter;
    private String userId;
    private MessagesRcvAdapter adapter;

    public static void callActivity(Context context){
        Intent intent = new Intent(context, SystemMessagesActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_system_messages;
    }

    @Override
    protected void initInjector() {
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));
        presenter = new SystemMessagesPresenterImpl();
        presenter.attachView(this);
        superPresenter = presenter;
    }

    @Override
    protected void init() {
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setColorSchemeResources(R.color.app_theme_color);
        swipeRefresh.setDistanceToTriggerSync(500);
        swipeRefresh.setProgressBackgroundColor(R.color.white);
        swipeRefresh.setSize(SwipeRefreshLayout.DEFAULT);

        userId = String.valueOf(SharedPreferencesUtils.get(UserInformationFields.USER_KEY,""));
        presenter.getMessages(userId, "all_notification", 1, PageNumberSetting.PAGE_NUMBER);

    }

    /**
     * 重新加载
     */
    @OnClick(R.id.tv_error_hint)
    public void loadClick(){
        presenter.getMessages(userId, "all_notification", 1, PageNumberSetting.PAGE_NUMBER);
    }


    @Override
    public void showMessageList(int resultState, List<MessageBean> messageList) {
        if (ResultCode.SUCCESS == resultState){
            if(messageList != null && messageList.size() > 0){
                swipeRefresh.setVisibility(View.VISIBLE);
                ll_error.setVisibility(View.GONE);
                if(adapter == null){
                    LinearLayoutManager lManager = new LinearLayoutManager(this);
                    lManager.setOrientation(LinearLayoutManager.VERTICAL);
                    rcv_list.setLayoutManager(lManager);
                    adapter = new MessagesRcvAdapter(messageList);
                    rcv_list.setAdapter(adapter);
                }else{
                    adapter.notifyDataSetChanged();
                }
            }else{
                com.orhanobut.logger.Logger.e("resultState = "+resultState);
                swipeRefresh.setVisibility(View.GONE);
                ll_error.setVisibility(View.VISIBLE);
                tv_error_hint.setClickable(false);
                tv_error_hint.setText("暂无系统消息");
            }
        }else{
            com.orhanobut.logger.Logger.e("resultState = "+resultState);
            swipeRefresh.setVisibility(View.GONE);
            ll_error.setVisibility(View.VISIBLE);
            tv_error_hint.setClickable(true);
            tv_error_hint.setText(getResources().getString(R.string.load_failed));
        }
    }

    @Override
    public void onRefresh() {
        presenter.getMessages(userId, "all_notification", 1, PageNumberSetting.PAGE_NUMBER);
    }

    @Override
    public void showProgress() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        swipeRefresh.setRefreshing(false);
    }

    @OnClick(R.id.ll_system_message_back)
    public void finishActivity(){
        finish();
    }


}
