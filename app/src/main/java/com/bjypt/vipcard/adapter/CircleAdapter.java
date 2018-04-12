package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.CircleMyActivity;
import com.bjypt.vipcard.activity.FriendsCircleWeb;
import com.bjypt.vipcard.activity.ImagePagerActivity;
import com.bjypt.vipcard.adapter.viewholder.CircleViewHolder;
import com.bjypt.vipcard.adapter.viewholder.ImageViewHolder;
import com.bjypt.vipcard.adapter.viewholder.URLViewHolder;
import com.bjypt.vipcard.adapter.viewholder.VideoViewHolder;
import com.bjypt.vipcard.bean.ActionItem;
import com.bjypt.vipcard.bean.CommentConfig;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.model.CircleItemBean;
import com.bjypt.vipcard.model.CircleUserInfo;
import com.bjypt.vipcard.model.LikeUserListBean;
import com.bjypt.vipcard.model.socialCommentResponseListBean;
import com.bjypt.vipcard.mvp.presenter.CirclePresenter;
import com.bjypt.vipcard.utils.GlideCircleTransform;
import com.bjypt.vipcard.utils.UrlUtils;
import com.bjypt.vipcard.widgets.CommentListView;
import com.bjypt.vipcard.widgets.ExpandTextView;
import com.bjypt.vipcard.widgets.MultiImageView;
import com.bjypt.vipcard.widgets.PraiseListView;
import com.bjypt.vipcard.widgets.SnsPopupWindow;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import static com.bjypt.vipcard.utils.SharedPreferenceUtils.getFromSharedPreference;

/**
 * Created by yiwei on 16/5/17.
 */
public class CircleAdapter extends BaseRecycleViewAdapter {

    public final static int TYPE_HEAD = 0;

    private static final int STATE_IDLE = 0;
    private static final int STATE_ACTIVED = 1;
    private static final int STATE_DEACTIVED = 2;
    private int videoState = STATE_IDLE;
    public static final int HEADVIEW_SIZE = 1;

    int curPlayIndex = -1;

    private CirclePresenter presenter;
    private Context context;
    private boolean hasComment;
    private boolean hasFavort;
    private String mTid = null;
    private int mPosition;
    CircleUserInfo.ResultDataBean headDataBean;

    public void setCirclePresenter(CirclePresenter presenter) {
        this.presenter = presenter;
    }

    public CircleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        }

        int itemType = 0;
        CircleItemBean.ResultDataBean.ListBean item = (CircleItemBean.ResultDataBean.ListBean) datas.get(position - 1);
//        if (CircleItem.TYPE_URL.equals(item.getType())) {
//            itemType = CircleViewHolder.TYPE_URL;
//        } else if (CircleItem.TYPE_IMG.equals(item.getType())) {
//            itemType = CircleViewHolder.TYPE_IMAGE;
//        } else if(CircleItem.TYPE_VIDEO.equals(item.getType())){
//            itemType = CircleViewHolder.TYPE_VIDEO;
//        }
        return 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == TYPE_HEAD) {
            View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.head_circle, parent, false);
//            ImageView iv_icon = (ImageView) headView.findViewById(R.id.iv_icon);
//            TextView userIconName = (TextView) headView.findViewById(R.id.userIconName);
            viewHolder = new HeaderViewHolder(headView);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_circle_item, parent, false);

            if (viewType == CircleViewHolder.TYPE_URL) {
                viewHolder = new URLViewHolder(view);
            } else if (viewType == CircleViewHolder.TYPE_IMAGE) {
                viewHolder = new ImageViewHolder(view);
            } else if (viewType == CircleViewHolder.TYPE_VIDEO) {
                viewHolder = new VideoViewHolder(view);
            }
        }

        return viewHolder;
    }

    public void setHeadImageAndNickname(CircleUserInfo.ResultDataBean resultBean) {
        headDataBean = resultBean;
        notifyDataSetChanged();
    }

    public void refreshOneItem() {
        if (mTid != null) {
            presenter.requestRefreshOneItem("WEB", mTid, mPosition);
            mTid = null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if (getItemViewType(position) == TYPE_HEAD) {
            if (headDataBean != null) {
                HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
                ImageView iv_icon = (ImageView) holder.itemView.findViewById(R.id.iv_icon);
                TextView userIconName = (TextView) holder.itemView.findViewById(R.id.userIconName);
                userIconName.setText(headDataBean.getUsername());
                Glide.with(context).load(headDataBean.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.bg_no_photo).transform(new GlideCircleTransform(context)).into(iv_icon);
                iv_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        } else {

            final int circlePosition = position - HEADVIEW_SIZE;
            final CircleViewHolder holder = (CircleViewHolder) viewHolder;
            final CircleItemBean.ResultDataBean.ListBean circleItem = (CircleItemBean.ResultDataBean.ListBean) datas.get(circlePosition);
            final String circleId = String.valueOf(circleItem.getUserResponse().getUid());
            final String name = circleItem.getUserResponse().getUsername();
            final String headImg = circleItem.getUserResponse().getAvatar();               // 用户头像
            final String content = circleItem.getSubject();                            // 标题
            final String createTime = circleItem.getDateline();                                // 发帖时间
            final List<LikeUserListBean> favortDatas = circleItem.getThreadLikes(); // 点赞的人
            final List<socialCommentResponseListBean> commentsDatas = circleItem.getForumPostList();
            int hasFavort1 = circleItem.getIs_like(); // 点赞否
            if (commentsDatas.size() > 0) {
                hasComment = true;
            } else {
                hasComment = false;
            }
            if (favortDatas.size() > 0) {
                hasFavort = true;
            } else {
                hasFavort = false;
            }
//            boolean hasFavort = circleItem.hasFavort();
//            boolean hasComment = circleItem.hasComment();

            Glide.with(context).load(headImg).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.bg_no_photo).transform(new GlideCircleTransform(context)).into(holder.headIv);

            holder.nameTv.setText(name);
            holder.timeTv.setText(createTime);

            if (!TextUtils.isEmpty(content)) {
//                holder.contentTv.setExpand(circleItem.isExpand());
                holder.contentTv.setExpandStatusListener(new ExpandTextView.ExpandStatusListener() {
                    @Override
                    public void statusChange(boolean isExpand) {
//                        circleItem.setExpand(isExpand);
                    }
                });

                holder.contentTv.setText(UrlUtils.formatUrlString(content));
            }
            holder.contentTv.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
            holder.headIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CircleMyActivity.class);
                    intent.putExtra("uid", circleId);
                    intent.putExtra("username", name);
                    intent.putExtra("avatar", headImg);
                    context.startActivity(intent);
                }
            });
            holder.nameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CircleMyActivity.class);
                    intent.putExtra("uid", circleId);
                    intent.putExtra("username", name);
                    intent.putExtra("avatar", headImg);
                    context.startActivity(intent);
                }
            });
            holder.ll_jump.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPosition = circlePosition;
                    mTid = String.valueOf(circleItem.getTid());
                    Intent intent = new Intent(context, FriendsCircleWeb.class);
                    String url = Config.web.circle_web_activity + mTid;
                    intent.putExtra("url", url);
                    context.startActivity(intent);
                }
            });
            holder.contentTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPosition = circlePosition;
                    mTid = String.valueOf(circleItem.getTid());
                    Intent intent = new Intent(context, FriendsCircleWeb.class);
                    String url = Config.web.circle_web_activity + mTid;
                    intent.putExtra("url", url);
                    context.startActivity(intent);
                }
            });
//            if(DatasUtil.curUser.getId().equals(circleItem.getUser().getId())){
//                holder.deleteBtn.setVisibility(View.VISIBLE);
//            }else{
//                holder.deleteBtn.setVisibility(View.GONE);
//            }
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除
                    if (presenter != null) {
                        presenter.deleteCircle(circleId);
                    }
                }
            });
            if (hasFavort || hasComment) {
                if (hasFavort) {//处理点赞列表
                    holder.praiseListView.setOnItemClickListener(new PraiseListView.OnItemClickListener() {
                        @Override
                        public void onClick(int position) {
//                            String userName = favortDatas.get(position).getUsername();
//                            String userId = String.valueOf(favortDatas.get(position).getUid());
//                            Toast.makeText(MyApplication.getContext(), userName + " &id = " + userId, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, CircleMyActivity.class);
                            intent.putExtra("uid", String.valueOf(favortDatas.get(position).getUid()));
                            context.startActivity(intent);
                        }
                    });
                    holder.praiseListView.setDatas(favortDatas);
                    holder.praiseListView.setVisibility(View.VISIBLE);
                } else {
                    holder.praiseListView.setVisibility(View.GONE);
                }

                if (hasComment) {//处理评论列表
                    holder.commentList.setOnItemClickListener(new CommentListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(int commentPosition) {
                            socialCommentResponseListBean commentItem = commentsDatas.get(commentPosition);
                            if (commentItem.getUserInfo().getUid() == Integer.parseInt(getFromSharedPreference(context, Config.userConfig.uid))) {//复制或者删除自己的评论
//
//                                CommentDialog dialog = new CommentDialog(context, presenter, commentItem, circlePosition);
//                                dialog.show();
                            } else {//回复别人的评论
                                if (presenter != null) {
                                    CommentConfig config = new CommentConfig();
                                    config.circlePosition = circlePosition;
                                    config.commentPosition = commentPosition;
                                    config.commentType = CommentConfig.Type.REPLY;
                                    config.pid = String.valueOf(commentItem.getPid());
                                    config.tid = String.valueOf(circleItem.getTid());
                                    config.replyUser = commentItem.getRepliedInfo();
                                    presenter.showEditTextBody(config);
                                }
                            }
                        }
                    });
//                    holder.commentList.setOnItemLongClickListener(new CommentListView.OnItemLongClickListener() {
//                        @Override
//                        public void onItemLongClick(int commentPosition) {
                    //长按进行复制或者删除
//                            CommentItem commentItem = commentsDatas.get(commentPosition);
//                            CommentDialog dialog = new CommentDialog(context, presenter, commentItem, circlePosition);
//                            dialog.show();
//                        }
//                    });
                    holder.commentList.setDatas(commentsDatas, context);
                    holder.commentList.setVisibility(View.VISIBLE);

                } else {
                    holder.commentList.setVisibility(View.GONE);
                }
                holder.digCommentBody.setVisibility(View.VISIBLE);
            } else {
                holder.digCommentBody.setVisibility(View.GONE);
            }

            holder.digLine.setVisibility(hasFavort && hasComment ? View.VISIBLE : View.GONE);

            final SnsPopupWindow snsPopupWindow = holder.snsPopupWindow;
            //判断是否已点赞
            if (hasFavort1 == 1) {
                snsPopupWindow.getmActionItems().get(0).mTitle = "取消";
            } else {
                snsPopupWindow.getmActionItems().get(0).mTitle = "赞";
            }
            snsPopupWindow.update();
            String tid = String.valueOf(circleItem.getTid());
            snsPopupWindow.setmItemClickListener(new PopupItemClickListener(circlePosition, circleItem, tid));
            holder.snsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //弹出popupwindow
                    snsPopupWindow.showPopupWindow(view);
                }
            });

            holder.urlTipTv.setVisibility(View.GONE);
            switch (holder.viewType) {
                case CircleViewHolder.TYPE_URL:// 处理链接动态的链接内容和和图片
                    if (holder instanceof URLViewHolder) {
//                        String linkImg = circleItem.getLinkImg();
//                        String linkTitle = circleItem.getLinkTitle();
//                        Glide.with(context).load(linkImg).into(((URLViewHolder)holder).urlImageIv);
//                        ((URLViewHolder)holder).urlContentTv.setText(linkTitle);
                        ((URLViewHolder) holder).urlBody.setVisibility(View.VISIBLE);
                        ((URLViewHolder) holder).urlTipTv.setVisibility(View.VISIBLE);
                    }

                    break;
                case CircleViewHolder.TYPE_IMAGE:// 处理图片
                    if (holder instanceof ImageViewHolder) {
                        final List<CircleItemBean.ResultDataBean.ListBean.AttachmentsBean> attachmentResponseListBean = circleItem.getAttachments();
                        if (attachmentResponseListBean != null && attachmentResponseListBean.size() > 0) {
                            ((ImageViewHolder) holder).multiImageView.setVisibility(View.VISIBLE);
                            ((ImageViewHolder) holder).multiImageView.setList(attachmentResponseListBean);
                            ((ImageViewHolder) holder).multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    //imagesize是作为loading时的图片size
                                    ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());

                                    List<String> photoUrls = new ArrayList<>();
                                    for (int i = 0; i < attachmentResponseListBean.size(); i++) {
                                        photoUrls.add(attachmentResponseListBean.get(i).getAttachment());
                                    }
//                                    List<String> photoUrls = new ArrayList<String>();
//                                    for(PhotoInfo photoInfo : photos){
//                                        photoUrls.add(photoInfo.url);
//                                    }
                                    ImagePagerActivity.startImagePagerActivity((context), photoUrls, position, imageSize);


                                }
                            });
                        } else {
                            ((ImageViewHolder) holder).multiImageView.setVisibility(View.GONE);
                        }
                    }

                    break;
                case CircleViewHolder.TYPE_VIDEO:
//                    if(holder instanceof VideoViewHolder){
//                        ((VideoViewHolder)holder).videoView.setVideoUrl(circleItem.getVideoUrl());
//                        ((VideoViewHolder)holder).videoView.setVideoImgUrl(circleItem.getVideoImgUrl());//视频封面图片
//                        ((VideoViewHolder)holder).videoView.setPostion(position);
//                        ((VideoViewHolder)holder).videoView.setOnPlayClickListener(new CircleVideoView.OnPlayClickListener() {
//                            @Override
//                            public void onPlayClick(int pos) {
//                                curPlayIndex = pos;
//                            }
//                        });
//                    }

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;//有head需要加1
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener {
        private String tid;
        //动态在列表中的位置
        private int mCirclePosition;
        private long mLasttime = 0;
        private CircleItemBean.ResultDataBean.ListBean mCircleItem;

        public PopupItemClickListener(int circlePosition, CircleItemBean.ResultDataBean.ListBean circleItem, String tid) {
            this.mCirclePosition = circlePosition;
            this.tid = tid;
            this.mCircleItem = circleItem;
        }

        @Override
        public void onItemClick(ActionItem actionitem, int position) {
            switch (position) {
                case 0://点赞、取消点赞
                    if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                        return;
                    mLasttime = System.currentTimeMillis();
                    if (presenter != null) {
                        if ("赞".equals(actionitem.mTitle.toString())) {
                            presenter.addFavort(mCirclePosition, tid);
                        } else {//取消点赞
                            presenter.deleteFavort(mCirclePosition, tid);
                        }
                    }
                    break;
                case 1://发布评论
                    if (presenter != null) {
                        CommentConfig config = new CommentConfig();
                        config.circlePosition = mCirclePosition;
                        config.tid = tid;
                        config.commentType = CommentConfig.Type.PUBLIC;
                        presenter.showEditTextBody(config);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
