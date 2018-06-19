package com.bjypt.vipcard.activity.shangfeng.primary.systemmessages.contract;


import com.bjypt.vipcard.activity.shangfeng.base.BaseContract;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MessageBean;

import java.util.List;

/**
 * Created by Dell on 2018/5/14.
 */

public interface SystemMessagesContract {

    interface View extends BaseContract.View {
        /**
         * 展示消息列表
         * @param resultState
         * @param messageList
         */
        void showMessageList(int resultState, List<MessageBean> messageList);

    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 获取消息
         * @param userid
         * @param business_code  消息类别(user_notification：用户消息 server_notification：系统消息 , all_notification : 所有消息)
         * @param pageNum
         * @param pageSize
         */
        void getMessages(String userid, String business_code, int pageNum, int pageSize);

    }

}
