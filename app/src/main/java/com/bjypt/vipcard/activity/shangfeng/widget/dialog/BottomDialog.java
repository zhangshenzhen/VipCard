package com.bjypt.vipcard.activity.shangfeng.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.bjypt.vipcard.R;


/**
 * 底部弹出Dialog
 */
public class BottomDialog extends Dialog {

    private final Context mContext;
    private BtnBottomListener mListener;
    private Button btn_1, btn_2;
    private Button btn_cancel;
    private Display display;

    public BottomDialog(Context context, String buttonText1, String buttonText2) {

        super(context, R.style.BottomAnimDialogStyle);
        this.mContext = context;
        initView(context, buttonText1, buttonText2);

    }


    private void initView(Context context, String buttonText1, String buttonText2) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_bottom_view2, null);

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        view.setMinimumWidth(display.getWidth());
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);

        btn_1 = (Button) view.findViewById(R.id.btn_1);
        btn_1.setText(buttonText1);
        btn_2 = (Button) view.findViewById(R.id.btn_2);
        btn_2.setText(buttonText2);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        btn_1.setOnClickListener(new BottomDialog.clickListener());
        btn_2.setOnClickListener(new BottomDialog.clickListener());
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        setContentView(view);

    }


    public void setClickListener(BtnBottomListener listener) {
        this.mListener = listener;
    }

    public interface BtnBottomListener {
        void onBtn1Click();

        void onBtn2Click();
    }


    private class clickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_1:
                    if (mListener != null) {
                        mListener.onBtn1Click();
                    }
                    break;
                case R.id.btn_2:
                    if (mListener != null) {
                        mListener.onBtn2Click();
                    }
                    break;
            }
            dismiss();
        }
    }
}
