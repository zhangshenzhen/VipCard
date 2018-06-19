package com.bjypt.vipcard.activity.shangfeng.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MessageBean;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * 消息列表
 */
public class MessagesRcvAdapter extends RecyclerView.Adapter<MessagesRcvAdapter.ViewHolder>{

    private List<MessageBean> messageBeans;
    private MessageBean messageBean;

    public MessagesRcvAdapter(List<MessageBean> messageBeans) {
        this.messageBeans = messageBeans;
    }

    @Override
    public int getItemCount() {
        return messageBeans.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        messageBean = messageBeans.get(position);
        Logger.d(messageBean);
        holder.tv_title.setText(messageBean.getTitle());
        holder.tv_content.setText(messageBean.getContent());
        holder.tv_date.setText(messageBean.getCreate_at());
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shangfeng_message, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title,tv_content,tv_date;

        public ViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_date = (TextView) view.findViewById(R.id.tv_date);

        }

    }


}
