package com.bjypt.vipcard.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bjypt.vipcard.R;


/**
 * 分享 Dialog
 */
public class ShareBottomDialog extends Dialog implements View.OnClickListener {

    private final Context mContext;
    private ShareBtnListener mListener;
    private Display display;
    private ImageButton ibtn_weixin_friend;
    private ImageButton ibtn_winxin_friend_circle;
    private TextView tv_cancel;

    public ShareBottomDialog(Context context) {
        super(context, R.style.BottomAnimDialogStyle);
        this.mContext = context;
        initView(context);

    }

    private void initView(Context context) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_share_view, null);

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        view.setMinimumWidth(display.getWidth());
        view.setMinimumHeight(display.getHeight());
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);

        ibtn_weixin_friend = (ImageButton) view.findViewById(R.id.ibtn_weixin_friend);
        ibtn_winxin_friend_circle = (ImageButton) view.findViewById(R.id.ibtn_winxin_friend_circle);

        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);

        ibtn_weixin_friend.setOnClickListener(this);
        ibtn_winxin_friend_circle.setOnClickListener(this);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        setContentView(view);

    }


    public void setClickListener(ShareBtnListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_weixin_friend: // 微信好友
                if (mListener != null) {
                    mListener.onWeixinFriendClick();
                }
                break;
            case R.id.ibtn_winxin_friend_circle:  // 微信朋友圈
                if (mListener != null) {
                    mListener.onWinxinFriendCircleClick();
                }
                break;
        }
        dismiss();
    }

    public interface ShareBtnListener {
        void onWeixinFriendClick();

        void onWinxinFriendCircleClick();
    }

}
