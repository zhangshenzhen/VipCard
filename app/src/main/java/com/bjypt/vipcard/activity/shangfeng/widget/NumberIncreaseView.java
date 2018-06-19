package com.bjypt.vipcard.activity.shangfeng.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import com.bjypt.vipcard.R;
import com.even.numbermorphview.TimerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglei on 2018/5/18.
 */

public class NumberIncreaseView extends LinearLayout {

    TimerView numberMorphView0;
    TimerView numberMorphView1;
    Integer currentNum =0;

    public NumberIncreaseView(Context context) {
        super(context);
        init(context, null);
    }

    public NumberIncreaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NumberIncreaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NumberIncreaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.number_increase_view, this);
        numberMorphView0 = (TimerView) findViewById(R.id.numberMorphView0);
        numberMorphView1 = (TimerView)findViewById(R.id.numberMorphView1);
        numberMorphView0.interpolator = new TimerView.SpringInterpolator();
        numberMorphView0.setPeriod(1000);
        numberMorphView1.interpolator = new TimerView.SpringInterpolator();
        numberMorphView1.setPeriod(1500);
    }

    public void startAnim(int end_num, int duration){
        Interpolator interpolator = new LinearInterpolator();//可用
//        if(interpolator instanceof BounceInterpolator){
//            end_num ++;
//        }
        ValueAnimator animator = ValueAnimator.ofInt(0,end_num).setDuration(duration);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                LogUtils.print("onAnimationUpdate:" + valueAnimator.getAnimatedValue());
                int animValue = (Integer) valueAnimator.getAnimatedValue();
                if(animValue != currentNum){
                    currentNum = animValue;
                    updateLable();
                }
            }
        });
        animator.start();
    }

    private Interpolator getRandInterpolator(){
        int num=(int)(Math.random()*3);
        List<Interpolator> interpolatorList = new ArrayList<>();
        interpolatorList.add( new AccelerateDecelerateInterpolator());
        interpolatorList.add( new BounceInterpolator());
        interpolatorList.add(new OvershootInterpolator());
        return interpolatorList.get(num);
    }

    private void updateLable(){
        if(currentNum > 99){
            currentNum =99;
        }
//        LogUtils.print("updateLable:" + currentNum +"  " + (currentNum % 10));
        numberMorphView0.setCurrentNum(currentNum % 10 );
        if(currentNum / 10 > 0 ){
            numberMorphView1.setVisibility(View.VISIBLE);
            numberMorphView1.setCurrentNum(currentNum / 10);
        }else{
            numberMorphView1.setVisibility(View.GONE);
        }


    }
}
