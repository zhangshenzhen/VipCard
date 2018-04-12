package com.bjypt.vipcard.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;

/**
 * Created by Administrator on 2016/5/9.
 */
public class MyDialog extends Dialog {

    private Context context;
    private String title;
    private String messageOne, MessageTwo;
    private String confirmButtonText;
    private String cacelButtonText;
    private ClickListenerInterface clickListenerInterface;
    private TextView tvTitle;
    private TextView messageView;
    private TextView messageView_;
    private Button btn_backDD, btn_backSY;
    private ImageView mBack;

    public interface ClickListenerInterface {

        public void doButtomOne();

        public void doButtomTwo();
    }

    public MyDialog(Context context, String title, String messageOne, String MessageTwo, String ButtomOneText, String ButtonTwoText,ClickListenerInterface v) {
        super(context, R.style.MyDialogStyle);
        this.context = context;
        this.messageOne = messageOne;
        this.MessageTwo = MessageTwo;
        this.title = title;
        this.confirmButtonText = ButtomOneText;
        this.cacelButtonText = ButtonTwoText;

        setClicklistener(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_mydialog, null);
        setContentView(view);

        tvTitle = (TextView) view.findViewById(R.id.title);
        messageView = (TextView) view.findViewById(R.id.tv_paysuccess);
        messageView_ = (TextView) view.findViewById(R.id.tv_paysuccess_);
        btn_backDD = (Button) view.findViewById(R.id.btn_backDD);
        btn_backSY = (Button) view.findViewById(R.id.btn_backSY);
        mBack= (ImageView) findViewById(R.id.iv_paysuccess_back);

        tvTitle.setText(title);
        messageView.setText(messageOne);
        messageView_.setText(MessageTwo);
        btn_backDD.setText(confirmButtonText);
        btn_backSY.setText(cacelButtonText);

        btn_backSY.setOnClickListener(new clickListener());
        btn_backDD.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);


        //隐藏
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.this.dismiss();
            }
        });
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            switch (id) {
                case R.id.btn_backSY:
                    clickListenerInterface.doButtomTwo();
                    break;
                case R.id.btn_backDD:
                    clickListenerInterface.doButtomOne();
                    break;
            }
        }

    }
}