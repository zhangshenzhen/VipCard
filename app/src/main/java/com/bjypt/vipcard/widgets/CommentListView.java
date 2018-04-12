package com.bjypt.vipcard.widgets;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.CircleMyActivity;
import com.bjypt.vipcard.model.socialCommentResponseListBean;
import com.bjypt.vipcard.spannable.CircleMovementMethod;
import com.bjypt.vipcard.spannable.SpannableClickable;
import com.bjypt.vipcard.utils.UrlUtils;
import com.sinia.orderlang.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiwei on 16/7/9.
 */
public class CommentListView extends LinearLayout {
    private int itemColor;
    private int itemSelectorColor;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private List<socialCommentResponseListBean> mDatas;
    private LayoutInflater layoutInflater;
    private String mAvatar;
    private Context mContext;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setDatas(List<socialCommentResponseListBean> datas, Context context) {
        if (datas == null) {
            datas = new ArrayList<socialCommentResponseListBean>();
        }
        mDatas = datas;
        mContext = context;
        notifyDataSetChanged();
    }

    public List<socialCommentResponseListBean> getDatas() {
        return mDatas;
    }

    public CommentListView(Context context) {
        super(context);
    }

    public CommentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public CommentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PraiseListView, 0, 0);
        try {
            //textview的默认颜色
            itemColor = typedArray.getColor(R.styleable.PraiseListView_item_color, getResources().getColor(R.color.praise_item_default));
            itemSelectorColor = typedArray.getColor(R.styleable.PraiseListView_item_selector_color, getResources().getColor(R.color.praise_item_selector_default));

        } finally {
            typedArray.recycle();
        }
    }

    public void notifyDataSetChanged() {

        removeAllViews();
        if (mDatas == null || mDatas.size() == 0) {
            return;
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < mDatas.size(); i++) {
            final int index = i;
            View view = getView(index);
            if (view == null) {
                throw new NullPointerException("listview item layout is null, please check getView()...");
            }

            addView(view, index, layoutParams);
        }

    }

    //
    private View getView(final int position) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(getContext());
        }
        View convertView = layoutInflater.inflate(R.layout.item_comment1, null, false);

        TextView commentTv = (TextView) convertView.findViewById(R.id.commentTv);
        final CircleMovementMethod circleMovementMethod = new CircleMovementMethod(itemSelectorColor, itemSelectorColor);

        final socialCommentResponseListBean bean = mDatas.get(position);
        if( bean.getUserInfo() != null) {
            String name = bean.getUserInfo().getUsername();
            String id = bean.getUserInfo().getUid() + "";
            String toReplyName = "";
            if (bean.getRepliedInfo() != null) {
                toReplyName = bean.getRepliedInfo().getUsername();
            }
//        if(bean.getFor_user() != null){
//            toReplyName = bean.getFor_user().getUsername();
//        }

            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(setClickableSpan(name, String.valueOf(bean.getUserInfo().getUid()), bean.getUserInfo().getAvatar()));

            if (!StringUtil.isEmpty(toReplyName)) {
                if (toReplyName.equals(name)) {
                    builder.append(setClickableSpan(name, String.valueOf(bean.getUserInfo().getUid()), bean.getUserInfo().getAvatar()));
                } else {
                    builder.append(" 回复 ");
                    builder.append(setClickableSpan(toReplyName, String.valueOf(bean.getRepliedInfo().getUid()), bean.getRepliedInfo().getAvatar()));
                }
            }
            builder.append(": ");
            //转换表情字符
            String contentBodyStr = bean.getSubject();
            builder.append(UrlUtils.formatUrlString(contentBodyStr));
            commentTv.setText(builder);

            commentTv.setMovementMethod(circleMovementMethod);
            commentTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (circleMovementMethod.isPassToTv()) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
            commentTv.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (circleMovementMethod.isPassToTv()) {
                        if (onItemLongClickListener != null) {
                            onItemLongClickListener.onItemLongClick(position);
                        }
                        return true;
                    }
                    return false;
                }
            });
        }

        return convertView;
    }

    @NonNull
    private SpannableString setClickableSpan(final String textStr, final String id, final String avatar) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new SpannableClickable(itemColor) {
                                    @Override
                                    public void onClick(View widget) {
//                                        Toast.makeText(MyApplication.getContext(), textStr + " &id = " + id + " &avatar = " + avatar, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(mContext, CircleMyActivity.class);
                                        intent.putExtra("uid", id);
                                        intent.putExtra("username", textStr);
                                        intent.putExtra("avatar", avatar);
                                        mContext.startActivity(intent);
                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    public static interface OnItemClickListener {
        public void onItemClick(int position);
    }

    public static interface OnItemLongClickListener {
        public void onItemLongClick(int position);
    }


}
