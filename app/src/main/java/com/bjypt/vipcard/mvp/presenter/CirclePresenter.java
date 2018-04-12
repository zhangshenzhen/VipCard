package com.bjypt.vipcard.mvp.presenter;

import android.util.Log;
import android.view.View;

import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.bean.CommentConfig;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.dialog.IntegralToast;
import com.bjypt.vipcard.listener.IDataRequestListener;
import com.bjypt.vipcard.model.CircleItemBean;
import com.bjypt.vipcard.model.CircleUserInfo;
import com.bjypt.vipcard.model.CommonBeanResult;
import com.bjypt.vipcard.model.LikeUserListBean;
import com.bjypt.vipcard.model.RefreshOneItemBean;
import com.bjypt.vipcard.model.socialCommentResponseListBean;
import com.bjypt.vipcard.mvp.contract.CircleContract;
import com.bjypt.vipcard.mvp.modle.CircleModel;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.lidroid.xutils.util.LogUtils;
import com.sinia.orderlang.utils.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yiw
 * @ClassName: CirclePresenter
 * @Description: 通知model请求服务器和通知view更新
 * @date 2015-12-28 下午4:06:03
 */
public class CirclePresenter implements CircleContract.Presenter {
    private final static String TAG = "CirclePresenter";
    private final static int TYPE_PULLREFRESH = 1;
    private final static int TYPE_UPLOADREFRESH = 2;
    private CircleModel circleModel;
    private CircleContract.View view;
    private String uid;
    private int begin = 1;
    public static int pageLength = 10;
    private String URL;

    public CirclePresenter(CircleContract.View view) {
        circleModel = new CircleModel();
        this.view = view;
        uid = SharedPreferenceUtils.getFromSharedPreference(MyApplication.getInstance().getContext(), Config.userConfig.uid);
    }

    /**
     * 请求朋友全首页头像以及昵称
     */
    public void loadUserInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);

        circleModel.loadUserInfo(new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {
                Log.e("clcl", "object=" + object + "");
                List<CircleUserInfo.ResultDataBean> list = new ArrayList<>();
                if (view != null) {
                    if (object != null) {
                        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                        try {
                            CircleUserInfo circleUserInfo = objectMapper.readValue(object.toString(), CircleUserInfo.class);
                            list.add(circleUserInfo.getResultData());
                            view.update2loadDataUserInfo(list);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void netWorkError(Object result) {
                view.netWorkError(result);
            }
        }, Config.web.request_user_info, params);
    }

    /**
     * 页面列表显示以及刷新状态
     *
     * @param loadType 刷新还是加载
     */
    public void loadData(final int loadType, String name) {

        if (loadType == TYPE_PULLREFRESH) {
            begin = 0;
        } else {
            begin += 1;
        }
        Map<String, String> params = new HashMap<>();
        params.put("fid", "2");                // 板块
        params.put("system_code", "Android");  // 系统编码
        params.put("page", begin + "");        // 页码
        params.put("size", pageLength + "");   // 一页记录条数
        params.put("uid", uid);               // 登录用户
        if (name.equals("推荐")) {
            URL = Config.web.request_social_list;
        } else {
            URL = Config.web.request_social_list2;
        }
//        Log.e("---------->", "begin=" + begin + "");
        circleModel.loadData(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                Log.e(TAG, "getAllThreadList======" + object.toString());
                if (view != null) {
                    if (object != null) {
                        List<CircleItemBean.ResultDataBean.ListBean> datas = new ArrayList<>();
                        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                        try {
                            CircleItemBean circleItemBean = objectMapper.readValue(object.toString(), CircleItemBean.class);
                            List<CircleItemBean.ResultDataBean.ListBean> list = circleItemBean.getResultData().getList(); // 朋友圈记录
                            datas.addAll(list);
//						}
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        view.update2loadData(loadType, datas);
                    } else {
                        LogUtils.e("network return null");
                    }
                }
            }

            @Override
            public void netWorkError(Object result) {
                view.netWorkError(result);
            }
        }, URL, params);
    }

    /**
     * @param circleId
     * @return void   返回类型
     * @Description: 删除动态
     */
    public void deleteCircle(final String circleId) {
        Map<String, String> params = new HashMap<>();
        circleModel.deleteCircle(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                if (view != null) {
                    view.update2DeleteCircle(circleId);
                }
            }

            @Override
            public void netWorkError(Object result) {
                view.netWorkError(result);
            }
        }, "", params);
    }

    /**
     * @return void    返回类型
     * @Description: 点赞
     */
    public void addFavort(final int circlePosition, String tid) {
        Map<String, String> params = new HashMap<>();
        params.put("tid", tid); // 话题主键
        params.put("system_code", "Android"); // 话题主键
//        params.put("uid", "21"); // 用户主键
        params.put("uid", SharedPreferenceUtils.getFromSharedPreference(MyApplication.getInstance().getContext(), Config.userConfig.uid)); // 用户主键
        circleModel.addFavort(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
//                Log.e("clzan", "=====" + object.toString());
                if (object != null) {
                    ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(CommonBeanResult.class, LikeUserListBean.class);
                    try {
                        CommonBeanResult<LikeUserListBean> likeUserListBean = objectMapper.readValue(object.toString(), type);
                        view.update2AddFavorite(circlePosition, likeUserListBean.getResultData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void netWorkError(Object result) {
                view.netWorkError(result);
            }
        }, Config.web.request_topiclike, params);
    }

    /**
     * @return void    返回类型
     * @Title: deleteFavort
     * @Description: 取消点赞
     */
    public void deleteFavort(final int circlePosition, final String tid) {
        Map<String, String> params = new HashMap<>();
        params.put("tid", tid); // 话题主键
//        params.put("uid", "21"); // 用户主键
        params.put("uid", SharedPreferenceUtils.getFromSharedPreference(MyApplication.getInstance().getContext(), Config.userConfig.uid)); // 用户主键
        circleModel.deleteFavort(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
//                Log.e("clzan", "cancel_topiclike" + object);
                if (view != null && object != null) {
                    view.update2DeleteFavort(circlePosition, uid);
                }
            }

            @Override
            public void netWorkError(Object result) {
                view.netWorkError(result);
            }
        }, Config.web.request_cancel_topiclike, params);
    }


    /**
     * @param config CommentConfig
     * @return void    返回类型
     * @Description: 增加评论
     */
    public void addComment(final String content, final CommentConfig config) {
        if (config == null) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("tid", config.tid); // 话题主键
//        params.put("uid", "21"); // 用户主键
        params.put("uid", SharedPreferenceUtils.getFromSharedPreference(MyApplication.getInstance().getContext(), Config.userConfig.uid)); // 用户主键
        params.put("message", content); // 用户主键
        if (StringUtil.isNotEmpty(config.pid)) {
            params.put("pid", config.pid); // 评论id
        }
        circleModel.addComment(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                if (object != null) {
                    Log.e("cl_comment", "=======================" + object.toString());
                    ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(CommonBeanResult.class, socialCommentResponseListBean.class);
                    try {
                        CommonBeanResult<socialCommentResponseListBean> socialCommentResponseListBean = objectMapper.readValue(object.toString(), type);
                        if (view != null) {
                            view.update2AddComment(config.circlePosition, socialCommentResponseListBean.getResultData());
                            if (StringUtil.isNotEmpty(socialCommentResponseListBean.getResultData().getVirtualBalance())) {
                                String virtualBalance = socialCommentResponseListBean.getResultData().getVirtualBalance();
                                if (!virtualBalance.equals("0")) {
                                    IntegralToast.getIntegralToast().ToastShow(MyApplication.getInstance().getContext(), null, "已评论，加" + virtualBalance + "积分");
                                }
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void netWorkError(Object result) {
                view.netWorkError(result);
            }

        }, Config.web.request_topiccomment, params);
    }

    /**
     * @return void    返回类型
     * @Title: deleteComment
     * @Description: 删除评论
     */
    public void deleteComment(final int circlePosition, final String commentId) {
        Map<String, String> params = new HashMap<>();
        circleModel.deleteComment(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                if (view != null) {
                    view.update2DeleteComment(circlePosition, commentId);
                }
            }

            @Override
            public void netWorkError(Object result) {
                view.netWorkError(result);
            }

        }, "", params);
    }

    /**
     * 当详情页返回时刷新单条数据
     *
     * @param web 请求类型
     * @param tid 帖子id
     */
    public void requestRefreshOneItem(String web, String tid, final int position) {
        Map<String, String> params = new HashMap<>();
        params.put("system_code", web); // 类型
        params.put("tid", tid); // 话题主键
        params.put("uid", SharedPreferenceUtils.getFromSharedPreference(MyApplication.getInstance().getContext(), Config.userConfig.uid)); // 用户主键

        circleModel.requestRefreshOneItem(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                Log.e(TAG, "=========================" + object.toString());
                if (object != null) {
//                    List<CircleItemBean.ResultDataBean.ListBean> datas = new ArrayList<>();
                    ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                    try {
                        RefreshOneItemBean refreshOneItemBean = objectMapper.readValue(object.toString(), RefreshOneItemBean.class);
                        List<socialCommentResponseListBean> commentList = refreshOneItemBean.getResultData().getCommentList();
                        List<LikeUserListBean> likeUserList = refreshOneItemBean.getResultData().getLikeUserList();
                        view.update2ReplaceInfo(position, commentList, likeUserList);
//                        datas.addAll(list);
//						}
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    LogUtils.e("network return null");
                }
            }

            @Override
            public void netWorkError(Object result) {
                view.netWorkError(result);
            }

        }, Config.web.request_refreshOne_item, params);
    }

    /**
     */
    public void showEditTextBody(CommentConfig commentConfig) {
        if (view != null) {
            view.updateEditTextBodyVisible(View.VISIBLE, commentConfig);
        }
    }

    /**
     * 清除对外部对象的引用，反正内存泄露。
     */
    public void recycle() {
        this.view = null;
    }


}
