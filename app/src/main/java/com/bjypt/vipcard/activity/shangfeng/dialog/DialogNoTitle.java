package com.bjypt.vipcard.activity.shangfeng.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bjypt.vipcard.R;


/**
 * Created by Dell on 2018/5/4.
 */
public class DialogNoTitle extends Dialog {
    public interface OnCloseListener {
        public boolean onClose(DialogNoTitle dialog);
    }

    protected OnCloseListener onCloseListener;
    protected View closeButton;
    private View content;
    protected TextView contentsView;

    public void setContent(String text) {
        contentsView.setText(text);
    }

    public DialogNoTitle(Context context, int layoutId, float margin,
                         String content) {
        super(context);
        this.setNotTitle(margin);
        init(context, layoutId, content);
    }

    public DialogNoTitle(Context context, int theme, int layoutId,
                         float margin, String content) {
        super(context, theme);
        this.setNotTitle(margin);
        init(context, layoutId, content);
    }

    public DialogNoTitle(Context context, int layoutId, String content) {
        super(context);
        this.setNotTitle();
        init(context, layoutId, content);
    }

    public DialogNoTitle(Context context, int theme, int layoutId,
                         String content) {
        super(context, theme);
        this.setNotTitle();
        init(context, layoutId, content);
    }

    private void setNotTitle() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCanceledOnTouchOutside(false);
//		this.setOnKeyListener(keylistener);
    }

    private void setNotTitle(float x) {
        this.setNotTitle();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.verticalMargin = x;
        getWindow().setAttributes(lp);
    }

    private void init(Context context, int layoutId, String contents) {
        try {
            content = View.inflate(context, layoutId, null);
            setContentView(content);
            contentsView = (TextView) (content.findViewById(R.id.dialog_content));
            contentsView.setText(contents);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };

    public void setBackGroudColor(int color) {
        content.setBackgroundColor(color);
    }

    public void setBackgroundResource(int res) {
        content.setBackgroundResource(res);
    }
}
