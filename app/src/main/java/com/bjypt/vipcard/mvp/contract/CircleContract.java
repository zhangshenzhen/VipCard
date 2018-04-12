package com.bjypt.vipcard.mvp.contract;


import com.bjypt.vipcard.bean.CommentConfig;
import com.bjypt.vipcard.model.CircleItemBean;
import com.bjypt.vipcard.model.CircleUserInfo;
import com.bjypt.vipcard.model.LikeUserListBean;
import com.bjypt.vipcard.model.socialCommentResponseListBean;

import java.util.List;

/**
 * Created by suneee on 2016/7/15.
 */
public interface CircleContract {

    interface View extends BaseView {
        void update2DeleteCircle(String circleId);

        void update2AddFavorite(int circlePosition, LikeUserListBean addItem);

        void update2DeleteFavort(int circlePosition, String favortId);

        void update2AddComment(int circlePosition, socialCommentResponseListBean addItem);

        void update2DeleteComment(int circlePosition, String commentId);

        void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig);

        void update2loadData(int loadType, List<CircleItemBean.ResultDataBean.ListBean> datas);

        void update2loadDataUserInfo(List<CircleUserInfo.ResultDataBean> datas);

        void netWorkError(Object result);

        void update2ReplaceInfo(int i, List<socialCommentResponseListBean> commentList, List<LikeUserListBean> likeUserList);
    }

    interface Presenter extends BasePresenter {
        void loadData(int loadType, String name);

        void loadUserInfo();

        void deleteCircle(final String circleId);

        void addFavort(final int circlePosition, String tid);

        void deleteFavort(final int circlePosition, final String favortId);

        void deleteComment(final int circlePosition, final String commentId);

        void requestRefreshOneItem(final String web, final String tid, final int position);

    }
}
