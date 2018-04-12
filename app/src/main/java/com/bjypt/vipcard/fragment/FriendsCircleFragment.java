package com.bjypt.vipcard.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.CircleAdapter;
import com.bjypt.vipcard.bean.CircleItem;
import com.bjypt.vipcard.bean.CommentConfig;
import com.bjypt.vipcard.bean.CommentItem;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.model.CircleItemBean;
import com.bjypt.vipcard.model.CircleUserInfo;
import com.bjypt.vipcard.model.CommonBeanResult;
import com.bjypt.vipcard.model.LikeUserListBean;
import com.bjypt.vipcard.model.socialCommentResponseListBean;
import com.bjypt.vipcard.mvp.contract.CircleContract;
import com.bjypt.vipcard.mvp.presenter.CirclePresenter;
import com.bjypt.vipcard.utils.CommonUtils;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.widgets.CommentListView;
import com.bjypt.vipcard.widgets.DivItemDecoration;
import com.bumptech.glide.Glide;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static com.bjypt.vipcard.utils.SharedPreferenceUtils.getFromSharedPreference;


/**
 * Created by 崔龙 on 2017/11/20.
 * <p>
 * 朋友圈fragment
 */

public class FriendsCircleFragment extends YMFragment implements CircleContract.View, EasyPermissions.PermissionCallbacks {
    private static final String TAG = "FriendsCircleFragment";
    private String name;
    private View view;
    private ImageView sendIv;
    private CircleAdapter circleAdapter;
    private CommentConfig commentConfig;
    private EditText editText;
    private ImageView iv_default;
    private int currentKeyboardH;
    private int editTextBodyHeight;
    private int selectCircleItemH;
    private int selectCommentItemOffset;
    private int screenHeight;
    private final static int TYPE_PULLREFRESH = 1;
    private final static int TYPE_UPLOADREFRESH = 2;
    private CirclePresenter presenter;
    private RefreshReceiver mRefreshReceiver;
    private SuperRecyclerView recyclerView;
    private LinearLayout edittextbody;
    private RelativeLayout bodyLayout;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private boolean isPause = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends_circle, null);
        return view;
    }

    public static FriendsCircleFragment getInstance(String name) {
        FriendsCircleFragment fragment = new FriendsCircleFragment();
        fragment.name = name;
        return fragment;
    }


    @Override
    public void beforeInitView() {
        presenter = new CirclePresenter(this);
        mRefreshReceiver = new RefreshReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("RESULT_OK");
        getActivity().registerReceiver(mRefreshReceiver, filter);
    }

    @Override
    public void initView() {
        recyclerView = (SuperRecyclerView) view.findViewById(R.id.recyclerView);
        iv_default = (ImageView) view.findViewById(R.id.iv_default);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DivItemDecoration(2, true));
        recyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (edittextbody.getVisibility() == View.VISIBLE) {
                    LogUtil.debugPrint("recyclerView setOnTouchListener");
                    updateEditTextBodyVisible(View.GONE, null);
                    return true;
                }
                return false;
            }
        });

        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadData(TYPE_PULLREFRESH, name);
            }
        };

        recyclerView.setRefreshListener(refreshListener);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Activity activity = getActivity();//activity 回收报错问题
                if (!(activity.isDestroyed() || activity.isFinishing())) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        Glide.with(activity).resumeRequests();
                    } else {
                        Glide.with(activity).pauseRequests();
                    }
                }
            }
        });

        circleAdapter = new CircleAdapter(getActivity());
        circleAdapter.setCirclePresenter(presenter);
        recyclerView.setAdapter(circleAdapter);

        edittextbody = (LinearLayout) view.findViewById(R.id.editTextBodyLl);
        editText = (EditText) view.findViewById(R.id.circleEt);
        sendIv = (ImageView) view.findViewById(R.id.sendIv);

        sendIv.setOnClickListener(new View.OnClickListener() {
            private long mLasttime = 0;

            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    //发布评论
                    if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                        return;
                    mLasttime = System.currentTimeMillis();
                    String content = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        Toast.makeText(getActivity(), "评论内容不能为空...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    presenter.addComment(content, commentConfig);
                }
            }
        });

        setViewTreeObserver();
    }

    @Override
    public void afterInitView() {
        initPermission();
        //实现自动下拉刷新功能
        recyclerView.getSwipeToRefresh().post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setRefreshing(true);//执行下拉刷新的动画
                refreshListener.onRefresh();//执行数据加载操作
            }
        });
    }


    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {

    }

    private void initPermission() {
        String[] perms = {Manifest.permission.CALL_PHONE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "因为功能需要，需要使用相关权限，请允许",
                    100, perms);
        }
    }

    public void setRefresh() {
        recyclerView.setRefreshing(true);//执行下拉刷新的动画
        refreshListener.onRefresh();//执行数据加载操作
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String errorMsg) {

    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
//
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            if (edittextbody != null && edittextbody.getVisibility() == View.VISIBLE) {
//                //edittextbody.setVisibility(View.GONE);
//                LogUtil.debugPrint("onKeyDown");
//                updateEditTextBodyVisible(View.GONE, null);
//                return true;
//            }
//        }
//        return true;
//    }

    @Override
    public void update2DeleteCircle(String circleId) {
        List<CircleItem> circleItems = circleAdapter.getDatas();
        for (int i = 0; i < circleItems.size(); i++) {
            if (circleId.equals(circleItems.get(i).getId())) {
                circleItems.remove(i);
                circleAdapter.notifyDataSetChanged();
                //circleAdapter.notifyItemRemoved(i+1);
                return;
            }
        }
    }

    @Override
    public void update2AddFavorite(int circlePosition, LikeUserListBean addItem) {
        if (addItem != null) {
            CircleItemBean.ResultDataBean.ListBean item = (CircleItemBean.ResultDataBean.ListBean) circleAdapter.getDatas().get(circlePosition);
            item.setIs_like(1);
            item.getThreadLikes().add(addItem);
            circleAdapter.notifyDataSetChanged();
            //circleAdapter.notifyItemChanged(circlePosition+1);
        }
    }

    @Override
    public void update2DeleteFavort(int circlePosition, String favortId) {
        CircleItemBean.ResultDataBean.ListBean item = (CircleItemBean.ResultDataBean.ListBean) circleAdapter.getDatas().get(circlePosition);
        List<LikeUserListBean> items = item.getThreadLikes();
        for (int i = 0; i < items.size(); i++) {
            if (favortId.equals(items.get(i).getUid() + "")) {
                item.setIs_like(0);
                items.remove(i);
                circleAdapter.notifyDataSetChanged();
                //circleAdapter.notifyItemChanged(circlePosition+1);
                return;
            }
        }
    }

    @Override
    public void update2AddComment(int circlePosition, socialCommentResponseListBean addItem) {
        if (addItem != null) {
            CircleItemBean.ResultDataBean.ListBean item = (CircleItemBean.ResultDataBean.ListBean) circleAdapter.getDatas().get(circlePosition);
            item.getForumPostList().add(addItem);
            circleAdapter.notifyDataSetChanged();
            //circleAdapter.notifyItemChanged(circlePosition+1);
        }
        updateEditTextBodyVisible(View.GONE, null);
        //清空评论文本
        editText.setText("");
    }

    @Override
    public void update2DeleteComment(int circlePosition, String commentId) {
        CircleItem item = (CircleItem) circleAdapter.getDatas().get(circlePosition);
        List<CommentItem> items = item.getComments();
        for (int i = 0; i < items.size(); i++) {
            if (commentId.equals(items.get(i).getId())) {
                items.remove(i);
                circleAdapter.notifyDataSetChanged();
                //circleAdapter.notifyItemChanged(circlePosition+1);
                return;
            }
        }
    }

    @Override
    public void update2ReplaceInfo(int position, List<socialCommentResponseListBean> commentList, List<LikeUserListBean> likeUserList) {
        if (commentList != null && likeUserList != null) {
            CircleItemBean.ResultDataBean.ListBean item = (CircleItemBean.ResultDataBean.ListBean) circleAdapter.getDatas().get(position);
            item.setThreadLikes(likeUserList);
            item.setForumPostList(commentList);
            for (int i = 0; i < likeUserList.size(); i++) {
                if (getFromSharedPreference(getActivity(), Config.userConfig.uid).equals((likeUserList.get(i).getUid() + ""))) {
                    item.setIs_like(1);
                }
            }
            circleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig) {
        this.commentConfig = commentConfig;
        edittextbody.setVisibility(visibility);

        measureCircleItemHighAndCommentItemOffset(commentConfig);

        if (View.VISIBLE == visibility) {
            editText.requestFocus();
            //弹出键盘
            CommonUtils.showSoftInput(editText.getContext(), editText);

        } else if (View.GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(editText.getContext(), editText);
        }
    }

    @Override
    public void update2loadData(int loadType, List<CircleItemBean.ResultDataBean.ListBean> datas) {
        if (datas.size() == 0) {
            iv_default.setVisibility(View.VISIBLE);
        } else {
            iv_default.setVisibility(View.GONE);
        }
        if (loadType == TYPE_PULLREFRESH) {
            recyclerView.setRefreshing(false);
            circleAdapter.setDatas(datas);
        } else if (loadType == TYPE_UPLOADREFRESH) {
            circleAdapter.getDatas().addAll(datas);
        }
        circleAdapter.notifyDataSetChanged();
        if (datas.size() >= CirclePresenter.pageLength) {
            recyclerView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            presenter.loadData(TYPE_UPLOADREFRESH, name);
                        }
                    }, 2000);

                }
            }, 1);
        } else {
            recyclerView.removeMoreListener();
            recyclerView.hideMoreProgress();
        }
    }


    @Override
    public void update2loadDataUserInfo(List<CircleUserInfo.ResultDataBean> datas) {

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }


    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(getActivity(), "您拒绝了相关权限，可能会导致相关功能不可用", Toast.LENGTH_LONG).show();
    }

    private void setViewTreeObserver() {
        bodyLayout = (RelativeLayout) view.findViewById(R.id.bodyLayout);
        final ViewTreeObserver swipeRefreshLayoutVTO = bodyLayout.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                bodyLayout.getWindowVisibleDisplayFrame(r);
                int statusBarH = getStatusBarHeight();//状态栏高度
                int screenH = bodyLayout.getRootView().getHeight();
                if (r.top != statusBarH) {
                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                    r.top = statusBarH;
                }
                int keyboardH = screenH - (r.bottom - r.top);
                Log.d(TAG, "screenH＝ " + screenH + " &keyboardH = " + keyboardH + " &r.bottom=" + r.bottom + " &top=" + r.top + " &statusBarH=" + statusBarH);

                if (keyboardH == currentKeyboardH) {//有变化时才处理，否则会陷入死循环
                    return;
                }

                currentKeyboardH = keyboardH;
                screenHeight = screenH;//应用屏幕的高度
                editTextBodyHeight = edittextbody.getHeight();

                if (keyboardH < 150) {//说明是隐藏键盘的情况
                    updateEditTextBodyVisible(View.GONE, null);
                    return;
                }
                //偏移listview
                if (layoutManager != null && commentConfig != null) {
                    layoutManager.scrollToPositionWithOffset(commentConfig.circlePosition + CircleAdapter.HEADVIEW_SIZE, getListviewOffset(commentConfig));
                }
            }
        });
    }

    /**
     * 测量偏移量
     *
     * @param commentConfig
     * @return
     */
    private int getListviewOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return 0;
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        //int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight;
        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight;
        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            listviewOffset = listviewOffset + selectCommentItemOffset;
        }
        Log.i(TAG, "listviewOffset : " + listviewOffset);
        return listviewOffset;
    }

    private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return;

        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = layoutManager.getChildAt(commentConfig.circlePosition + CircleAdapter.HEADVIEW_SIZE - firstPosition);

        if (selectCircleItem != null) {
            selectCircleItemH = selectCircleItem.getHeight();
        }

        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.commentList);
            if (commentLv != null) {
                //找到要回复的评论view,计算出该view距离所属动态底部的距离
                View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
                if (selectCommentItem != null) {
                    //选择的commentItem距选择的CircleItem底部的距离
                    selectCommentItemOffset = 0;
                    View parentView = selectCommentItem;
                    do {
                        int subItemBottom = parentView.getBottom();
                        parentView = (View) parentView.getParent();
                        if (parentView != null) {
                            selectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                        }
                    } while (parentView != null && parentView != selectCircleItem);
                }
            }
        }
    }

    @Override
    public void netWorkError(Object result) {
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        JavaType type = objectMapper.getTypeFactory().constructParametricType(CommonBeanResult.class, String.class);
        try {
            CommonBeanResult<String> commonBeanResult = objectMapper.readValue(result.toString(), type);
            ToastUtil.showToast(getActivity(), commonBeanResult.getResultData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class RefreshReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("RESULT_OK".equals(intent.getAction())) {
                presenter.loadData(TYPE_PULLREFRESH, name);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mRefreshReceiver);
    }

    @Override
    public void onPause() {
        super.onPause();
        isPause = true; //记录页面已经被暂停
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isPause) { //判断是否暂停
            isPause = false;
            Log.e(TAG, "onResume()");
            circleAdapter.refreshOneItem();
        }
    }
}
