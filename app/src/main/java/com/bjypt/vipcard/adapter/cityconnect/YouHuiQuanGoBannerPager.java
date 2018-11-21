package com.bjypt.vipcard.adapter.cityconnect;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.cityconnect.YouHuiQuanListActivity;
import com.bjypt.vipcard.bean.YiuHuiCountGoBean;
import com.bjypt.vipcard.utils.DensityUtil;

public class YouHuiQuanGoBannerPager extends PagerAdapter {

    private Context mContext;
    private int[] mImageIds;
    private YiuHuiCountGoBean.ResultDataBean resultDataBean;

    public YouHuiQuanGoBannerPager(Context mContext, int[] mImageIds, YiuHuiCountGoBean.ResultDataBean resultDataBean) {
        this.mContext = mContext;
        this.mImageIds = mImageIds;
        this.resultDataBean = resultDataBean;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int currentposition = (position + 0) % mImageIds.length;
        Log.d("TAG", "当前位置是 ：" + currentposition);
        //添加自定义布局
        View view = View.inflate(mContext,
                R.layout.go_banner_info, null);// 最后一个传了null
        RelativeLayout relyout = view.findViewById(R.id.relyout);
        LinearLayout line_count = view.findViewById(R.id.line_count);//显示优惠券的布局
        LinearLayout line_binder = view.findViewById(R.id.line_binder);//显示优惠券的布局
        TextView tv_num = view.findViewById(R.id.tv_num);
        ImageView img_go = view.findViewById(R.id.img_go);
        relyout.setBackgroundResource(mImageIds[currentposition]); //根据位置变换图片
        if (currentposition == 0) {
            img_go.setBackgroundResource(R.mipmap.go2);
            line_count.setVisibility(View.VISIBLE);
            line_binder.setVisibility(View.INVISIBLE);
            tv_num.setText(resultDataBean.getCanUseCoupon()+"");// 可使用数量
        } else {
           /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = DensityUtil.dip2px(mContext, 10);
            img_go.setLayoutParams(params);*/
            img_go.setBackgroundResource(R.mipmap.go1);
            line_binder.setVisibility(View.VISIBLE);
            line_count.setVisibility(View.INVISIBLE);
        }
         img_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "当前位置是 ：" + currentposition);
               if (currentposition==0){//到优惠券列表
                mContext.startActivity(new Intent(mContext, YouHuiQuanListActivity.class));

               }else {//线下绑定

               }
            }
        });
         ((ViewPager) container).addView(view);
        return view;
    }

    /**
     * 销毁一个Item
     * @param container ViewPager
     * @param position  要销毁item的位置
     * @param object    instantiateItem方法的返回值
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    static class  Holder {



    }

}
