package com.bjypt.vipcard.utils;

import android.widget.ImageView;

import com.bjypt.vipcard.R;

/**
 * Created by 崔龙 on 2017/8/4.
 * <p>
 * 显示星星工具类
 */

public class StarUtils {
    /**
     * 显示五个星星（不含半个星星的方法）
     *
     * @param num       需要的星星数值
     * @param iv_star_1 第一颗要显示的星星
     * @param iv_star_2 第二颗要显示的星星
     * @param iv_star_3 第三颗要显示的星星
     * @param iv_star_4 第四颗要显示的星星
     * @param iv_star_5 第五颗要显示的星星
     */
    public static void showStar(float num, ImageView iv_star_1, ImageView iv_star_2, ImageView iv_star_3, ImageView iv_star_4, ImageView iv_star_5) {
        if (num == 0.0) {
            iv_star_1.setBackgroundResource(R.mipmap.pf_wf);
            iv_star_2.setBackgroundResource(R.mipmap.pf_wf);
            iv_star_3.setBackgroundResource(R.mipmap.pf_wf);
            iv_star_4.setBackgroundResource(R.mipmap.pf_wf);
            iv_star_5.setBackgroundResource(R.mipmap.pf_wf);
        } else if (num > 0 && num <= 0.5) {
            iv_star_1.setBackgroundResource(R.mipmap.pf_bf);
            iv_star_2.setBackgroundResource(R.mipmap.pf_wf);
            iv_star_3.setBackgroundResource(R.mipmap.pf_wf);
            iv_star_4.setBackgroundResource(R.mipmap.pf_wf);
            iv_star_5.setBackgroundResource(R.mipmap.pf_wf);
        } else if (num > 0.5 && num <= 1) {
            iv_star_1.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_2.setBackgroundResource(R.mipmap.pf_wf);
            iv_star_3.setBackgroundResource(R.mipmap.pf_wf);
            iv_star_4.setBackgroundResource(R.mipmap.pf_wf);
            iv_star_5.setBackgroundResource(R.mipmap.pf_wf);
        } else if (num > 1 && num <= 1.5) {
            iv_star_1.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_2.setBackgroundResource(R.mipmap.pf_bf);
            iv_star_3.setBackgroundResource(R.mipmap.pf_wf);
            iv_star_4.setBackgroundResource(R.mipmap.pf_wf);
            iv_star_5.setBackgroundResource(R.mipmap.pf_wf);
        } else if (num > 1.5 && num <= 2) {
            iv_star_1.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_2.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_3.setBackgroundResource(R.mipmap.pf_wf);
            iv_star_4.setBackgroundResource(R.mipmap.pf_wf);
            iv_star_5.setBackgroundResource(R.mipmap.pf_wf);
        } else if (num > 2 && num <= 2.5) {
            iv_star_1.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_2.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_3.setBackgroundResource(R.mipmap.pf_bf);
            iv_star_4.setBackgroundResource(R.mipmap.pf_wf);
            iv_star_5.setBackgroundResource(R.mipmap.pf_wf);
        } else if (num > 2.5 && num <= 3) {
            iv_star_1.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_2.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_3.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_4.setBackgroundResource(R.mipmap.pf_wf);
            iv_star_5.setBackgroundResource(R.mipmap.pf_wf);
        } else if (num > 3 && num <= 3.5) {
            iv_star_1.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_2.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_3.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_4.setBackgroundResource(R.mipmap.pf_bf);
            iv_star_5.setBackgroundResource(R.mipmap.pf_wf);
        } else if (num > 3.5 && num <= 4) {
            iv_star_1.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_2.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_3.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_4.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_5.setBackgroundResource(R.mipmap.pf_wf);
        } else if (num > 4 && num <= 4.5) {
            iv_star_1.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_2.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_3.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_4.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_5.setBackgroundResource(R.mipmap.pf_bf);
        } else if (num > 4.5 && num <= 5) {
            iv_star_1.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_2.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_3.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_4.setBackgroundResource(R.mipmap.pf_mf);
            iv_star_5.setBackgroundResource(R.mipmap.pf_mf);
        }
    }

    /**
     * 显示五个半星星的方法
     *
     * @param num      需要的星星数值
     * @param iv_star1 第一颗要显示的星星
     * @param iv_star2 第二颗要显示的星星
     * @param iv_star3 第三颗要显示的星星
     * @param iv_star4 第四颗要显示的星星
     * @param iv_star5 第五颗要显示的星星
     */
    public static void showFiveHalfStar(float num, ImageView iv_star1, ImageView iv_star2, ImageView iv_star3, ImageView iv_star4, ImageView iv_star5) {
        if (num == 0) {
            iv_star1.setBackgroundResource(R.mipmap.pf_wf);
            iv_star2.setBackgroundResource(R.mipmap.pf_wf);
            iv_star3.setBackgroundResource(R.mipmap.pf_wf);
            iv_star4.setBackgroundResource(R.mipmap.pf_wf);
            iv_star5.setBackgroundResource(R.mipmap.pf_wf);
        } else if (num > 0 && num <= 1) {
            iv_star1.setBackgroundResource(R.mipmap.pf_mf);
            iv_star2.setBackgroundResource(R.mipmap.pf_wf);
            iv_star3.setBackgroundResource(R.mipmap.pf_wf);
            iv_star4.setBackgroundResource(R.mipmap.pf_wf);
            iv_star5.setBackgroundResource(R.mipmap.pf_wf);
        } else if (num > 1 && num <= 2) {
            iv_star1.setBackgroundResource(R.mipmap.pf_mf);
            iv_star2.setBackgroundResource(R.mipmap.pf_mf);
            iv_star3.setBackgroundResource(R.mipmap.pf_wf);
            iv_star4.setBackgroundResource(R.mipmap.pf_wf);
            iv_star5.setBackgroundResource(R.mipmap.pf_wf);
        } else if (num > 2 && num <= 3) {
            iv_star1.setBackgroundResource(R.mipmap.pf_mf);
            iv_star2.setBackgroundResource(R.mipmap.pf_mf);
            iv_star3.setBackgroundResource(R.mipmap.pf_mf);
            iv_star4.setBackgroundResource(R.mipmap.pf_wf);
            iv_star5.setBackgroundResource(R.mipmap.pf_wf);
        } else if (num > 3 && num <= 4) {
            iv_star1.setBackgroundResource(R.mipmap.pf_mf);
            iv_star2.setBackgroundResource(R.mipmap.pf_mf);
            iv_star3.setBackgroundResource(R.mipmap.pf_mf);
            iv_star4.setBackgroundResource(R.mipmap.pf_mf);
            iv_star5.setBackgroundResource(R.mipmap.pf_wf);
        } else if (num > 4 && num <= 5) {
            iv_star1.setBackgroundResource(R.mipmap.pf_mf);
            iv_star2.setBackgroundResource(R.mipmap.pf_mf);
            iv_star3.setBackgroundResource(R.mipmap.pf_mf);
            iv_star4.setBackgroundResource(R.mipmap.pf_mf);
            iv_star5.setBackgroundResource(R.mipmap.pf_mf);
        }
    }
}
