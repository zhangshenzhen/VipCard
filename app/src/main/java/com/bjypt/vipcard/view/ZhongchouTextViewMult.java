package com.bjypt.vipcard.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.bean.AnnouncementBean;
import com.bjypt.vipcard.bean.ZhongChouBanerBean;

import java.util.List;

/**
 * Created by admin on 2017/7/24.
 */

public class ZhongchouTextViewMult extends LinearLayout {
    List<ZhongChouBanerBean> mTexts; //数据源
    private int flipInterval = 3000; //切换间隔时间
    private int anmiDuration = 2000;
    private ViewFlipper viewFlipper;
    private int pageSize;

    private OnItemClickListener onItemClickListener;

    public ZhongchouTextViewMult(Context context) {
        super(context);
        init(context, null);
    }

    public ZhongchouTextViewMult(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ZhongchouTextViewMult(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ZhongchouTextViewMult(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_adtextview_mult, this);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
    }

    /**
     * 设置多长时间切换
     *
     * @param flipInterval
     */
    public void setFlipInterval(int flipInterval) {
        this.flipInterval = flipInterval;
        viewFlipper.setFlipInterval(flipInterval);
    }

    public void setAnmiDuration(int anmiDuration) {
        this.anmiDuration = anmiDuration;
        Animation inAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.slide_in_from_left);
        inAnimation.setDuration(anmiDuration);
        Animation outAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.slide_out_to_right);
        outAnimation.setDuration(anmiDuration);
        viewFlipper.setInAnimation(inAnimation);
        viewFlipper.setOutAnimation(outAnimation);
    }

    /**
     * 开始切换 自动播放
     */
    private void startFlipping() {
        setAnmiDuration(anmiDuration);
        viewFlipper.startFlipping();
    }

    public void stopFilpping(){
        viewFlipper.stopFlipping();
    }

    /**
     * 设置数据源
     *
     * @param mTexts   数据源
     * @param pageSize 一页显示多少条
     */
    public void setTexts(final List<ZhongChouBanerBean> mTexts, int pageSize) {
        this.mTexts = mTexts;
        int countrecord = mTexts.size();
        int countpage = 1;
        if (pageSize <= 0) {
            pageSize = this.pageSize;
        }
        if (countrecord % pageSize == 0) {
            countpage = countrecord / pageSize;
        } else {
            //补齐整页
            int temp = countrecord % pageSize ;
            for(int i=0;i< pageSize - temp;i++){
                mTexts.add(mTexts.get(i));
            }
            countrecord = mTexts.size();
            countpage = countrecord / pageSize;
        }
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < countpage; i++) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout.setPadding(2, 2, 2, 2);
            linearLayout.setLayoutParams(params);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            int limit = (i + 1) * pageSize - 1;
            if (limit > countrecord) limit = countrecord - 1;
            for (int j = i * pageSize; j <= limit; j++) {
                View view = inflater.inflate(R.layout.layout_adtextview_view_item, null);
                ImageView tv_mFront = (ImageView) view.findViewById(R.id.tv_mFront);
                TextView tv_mBack = (TextView)view.findViewById(R.id.tv_mBack);
                tv_mBack.setText(mTexts.get(j).getApp_name());
                view.setTag(mTexts.get(j));
                    view.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //AnnouncementBean announcementBean = (AnnouncementBean)view.getTag();
                           ZhongChouBanerBean zhongChouBean = (ZhongChouBanerBean) view.getTag();
                            if(onItemClickListener != null)
                            onItemClickListener.onClick(zhongChouBean);
                          }
                    });
                linearLayout.addView(view);
            }

            viewFlipper.addView(linearLayout);
        }

        startFlipping();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(ZhongChouBanerBean zhongChouBean);
    }


}
