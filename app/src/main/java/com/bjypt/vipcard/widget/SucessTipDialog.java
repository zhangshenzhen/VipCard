package com.bjypt.vipcard.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.bjypt.vipcard.R;

/**
 * Created by Dell on 2018/4/11.
 */

public class SucessTipDialog extends Dialog implements View.OnClickListener {

    private LayoutInflater inflater;
    private View view;
    private Button btn1;
    private Button btn2;
    private TextView tv_content;
    private ButtonClickListener buttonClickListener;

    public SucessTipDialog(@NonNull Context context) {
        super(context,R.style.MyDialogStyle);
        inflater = LayoutInflater.from(context);
        initViews();
    }


    private void initViews() {
        view = inflater.inflate(R.layout.dialog_recharge_state,null);
        Window window = this.getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setAttributes(lp);
        }

        tv_content = (TextView) view.findViewById(R.id.tv_content);
        btn1 = (Button) view.findViewById(R.id.btn1);
        btn2 = (Button) view.findViewById(R.id.btn2);
        view.setOnClickListener(this);
        setContentView(view);
    }



    public SucessTipDialog setButtonText(String text, String buttonText, String buttonString2){
        tv_content.setText(text);
        if(null != buttonText){
            btn1.setText(buttonText);
            btn1.setVisibility(View.VISIBLE);
            btn1.setOnClickListener(this);
        }else{
            btn1.setVisibility(View.GONE);
        }
        if(null != buttonString2){
            btn2.setText(buttonString2);
            btn2.setVisibility(View.VISIBLE);
            btn2.setOnClickListener(this);
        }else{
            btn2.setVisibility(View.GONE);
        }
        return this;
    }

    public SucessTipDialog setBtnClick(ButtonClickListener listener){
        this.buttonClickListener = listener;
        return this;
    }

    public interface ButtonClickListener{
        void btn1Click();
        void btn2Click();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn1:
                if(null != buttonClickListener){
                    buttonClickListener.btn1Click();
                }
                break;
            case R.id.btn2:
                if(null != buttonClickListener){
                    buttonClickListener.btn2Click();
                }
                break;
        }
        dismiss();
    }
}
