package com.bjypt.vipcard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/8
 * Use by 顶部导航栏
 */
public class TitleView extends RelativeLayout implements View.OnClickListener {
    private View view;
    private ImageView iv_left;
    private RelativeLayout  left_lay,right_lay;
    private ImageView iv_reight;
    private TextView tv_title;

    public TitleImageListener listener;


    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        view = LayoutInflater.from(context).inflate(R.layout.layout_title, null);
        addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        initView();
        initClick();
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        // TODO Auto-generated constructor stub
    }

    public TitleView(Context context) {
        this(context,null);
        // TODO Auto-generated constructor stub
    }
    /*
     * 初始化界面
     */
    private void initView() {
        // TODO Auto-generated method stub
        iv_left = (ImageView) view.findViewById(R.id.iv_left);
        left_lay = (RelativeLayout) view.findViewById(R.id.img_left_lay);
        right_lay=(RelativeLayout) view.findViewById(R.id.img_right_lay);
        iv_reight = (ImageView) view.findViewById(R.id.iv_right);
        if (isInEditMode()) { return; }
        tv_title = (TextView) view.findViewById(R.id.tv_title);
    }
    private void initClick() {
        // TODO Auto-generated method stub
        if (isInEditMode()) { return; }
        left_lay.setOnClickListener(this);
        right_lay.setOnClickListener(this);

    }

    /**
     *
     * @param IvLeft 标题栏：左侧图标
     * @param title  标题
     * @param IvRight 右侧图标
     * @param listener 标题栏，左右图标的监听
     */
    public void setTitle(int IvLeft,String title,int IvRight,TitleImageListener listener){
        tv_title.setText(title);
        iv_left.setImageResource(IvLeft);
        iv_reight.setImageResource(IvRight);
        this.listener = listener;
    }
    public void setTitleAndLeft(int IvLeft,String title){
        tv_title.setText(title);
        iv_left.setImageResource(IvLeft);
    }

    /**
     * 设置标题栏的显示文本
     * @param Titletext
     */
    public void settitleText(String Titletext){
        tv_title.setText(Titletext);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()){
            case R.id.img_left_lay:
                if(listener != null){
                    listener.ClickLeft();
                }
                break;
            case R.id.img_right_lay:
                if(listener != null){
                    listener.ClickRight();
                }
                break;
        }

    }
    /**
     * 标题栏左右图标的点击监听
     */
    public interface TitleImageListener{
        public void ClickLeft();
        public void ClickRight();
    }

}
