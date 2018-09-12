package com.bjypt.vipcard.config;

import android.graphics.Bitmap;

import com.bjypt.vipcard.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.L;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/29
 * Use by
 */
public class AppConfig {

    /**
     * 引导页
     **/
    static {
        L.writeLogs(false);
    }

    public static final DisplayImageOptions DEFAULT_IMG_OPTIONS_WELCOME = new DisplayImageOptions.Builder()
            .cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheOnDisk(true)
            .bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
//            .showStubImage(R.drawable.welcome02) // 设置图片下载期间显示的图片
//            .showImageForEmptyUri(R.drawable.welcome02) // 设置图片Uri为空或是错误的时候显示的图片
//            .showImageOnFail(R.drawable.welcome02) // 设置图片加载或解码过程中发生错误显示的图片
            .build();

    /**
     * 商家大图背景
     **/
    public static final DisplayImageOptions DEFAULT_IMG_OPTIONS_AD = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.ad_bg) // 设置图片下载期间显示的图片
            .cacheOnDisk(true)
            .cacheInMemory(true)
            .showImageForEmptyUri(R.mipmap.ad_bg) // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.mipmap.ad_bg) // 设置图片加载或解码过程中发生错误显示的图片
            .build();


    /**
     * 新闻一张图时默认
     **/
    public static final DisplayImageOptions XINWEN_IMG_OPTIONS_ONE = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.xinwen_one) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.xinwen_one) // 设置图片Uri为空或是错误的时候显示的图片
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new FadeInBitmapDisplayer(100))
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheInMemory(true)
            .showImageOnFail(R.mipmap.xinwen_one) // 设置图片加载或解码过程中发生错误显示的图片
            .build();

    /**
     * 新闻二张图时默认
     **/
    public static final DisplayImageOptions XINWEN_IMG_OPTIONS_TWO = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.xinwen_two) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.xinwen_two) // 设置图片Uri为空或是错误的时候显示的图片
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new FadeInBitmapDisplayer(100))
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheInMemory(true)
            .showImageOnFail(R.mipmap.xinwen_two) // 设置图片加载或解码过程中发生错误显示的图片
            .build();

    /**
     * 新闻三张图时默认
     **/
    public static final DisplayImageOptions XINWEN_IMG_OPTIONS_THREE = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.xinwen_three) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.xinwen_three) // 设置图片Uri为空或是错误的时候显示的图片
            .cacheInMemory(false)
            .cacheOnDisk(true)
            .displayer(new FadeInBitmapDisplayer(100))
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .showImageOnFail(R.mipmap.xinwen_three) // 设置图片加载或解码过程中发生错误显示的图片
            .build();

    /**
     * 商家大图背景
     **/
    public static final DisplayImageOptions DEFAULT_LUNBO = new DisplayImageOptions.Builder()
            .cacheOnDisk(true)
            .cacheInMemory(true)
            .showImageOnLoading(R.mipmap.ad_bg) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.ad_bg) // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.mipmap.ad_bg) // 设置图片加载或解码过程中发生错误显示的图片
            .build();

    /*头像背景*/
    public static final DisplayImageOptions DEFAULT_IMG_OPTIONS_PERSON = new DisplayImageOptions.Builder()
            .cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheOnDisk(true)
            .bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
            .showStubImage(R.mipmap.app_ic_launcher) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.app_ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.mipmap.ad_bg) // 设置图片加载或解码过程中发生错误显示的图片
            .build();

    public static final DisplayImageOptions DEFAULT_MERCHANT_TITLE = new DisplayImageOptions.Builder()
            .cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheOnDisk(true)
            .bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
            .showStubImage(R.mipmap.system_messages) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.system_messages) // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.mipmap.system_messages) // 设置图片加载或解码过程中发生错误显示的图片
            .build();

    /*分类背景*/
    public static final DisplayImageOptions DEFAULT_IMG_LIFE_TYPE = new DisplayImageOptions.Builder()
            .cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheOnDisk(true)
            .bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
            .showStubImage(R.mipmap.menu_error) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.menu_error) // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.mipmap.menu_error) // 设置图片加载或解码过程中发生错误显示的图片
            .build();

    /*商家相册缩略图*/
    public static final DisplayImageOptions DEFAULT_IMG_MERCHANT_BG = new DisplayImageOptions.Builder()
            .cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheOnDisk(true)
            .bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
            .showStubImage(R.mipmap.merchant_photo_album) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.merchant_photo_album) // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.mipmap.merchant_photo_album) // 设置图片加载或解码过程中发生错误显示的图片
            .build();


    /*商家缩略图*/
    public static final DisplayImageOptions DEFAULT_IMG_MERCHANT_LIST_BG = new DisplayImageOptions.Builder()
            .cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheOnDisk(true)
            .bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
            .showStubImage(R.mipmap.default_merchant_pic) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.default_merchant_pic) // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.mipmap.default_merchant_pic) // 设置图片加载或解码过程中发生错误显示的图片
            .build();

    /*菜品缩略图*/
    public static final DisplayImageOptions DEFAULT_IMG_PRODUCT_BG = new DisplayImageOptions.Builder()
            .cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheOnDisk(true)
            .bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
            .showStubImage(R.mipmap.product_bg_) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.product_bg_) // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.mipmap.product_bg_) // 设置图片加载或解码过程中发生错误显示的图片
            .build();

    /*一卡通缩略图*/
    public static final DisplayImageOptions DEFAULT_IMG_CITIZEN_CARD = new DisplayImageOptions.Builder()
            .cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheOnDisk(true)
            .bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
            .showStubImage(R.mipmap.more) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.more) // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.mipmap.more) // 设置图片加载或解码过程中发生错误显示的图片
            .build();

    /*圈圈默认头像*/
    public static final DisplayImageOptions CIRCLE_HEADER = new DisplayImageOptions.Builder()
            .cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheOnDisk(true)
            .bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
            .showStubImage(R.drawable.header_yellow) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.drawable.header_yellow) // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.header_yellow) // 设置图片加载或解码过程中发生错误显示的图片
            .build();


    /*圈圈默认头像*/
    public static final DisplayImageOptions CIRCLE_HEADER_GRAY = new DisplayImageOptions.Builder()
            .cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheOnDisk(true)
            .bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
            .showStubImage(R.drawable.header_gray) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.drawable.header_gray) // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.header_gray) // 设置图片加载或解码过程中发生错误显示的图片
            .build();

    /*众筹默认头像*/
    public static final DisplayImageOptions CF_HEADER_GRAY = new DisplayImageOptions.Builder()
            .cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheOnDisk(true)
            .bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
            .showStubImage(R.mipmap.my_photo) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.my_photo) // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.mipmap.my_photo) // 设置图片加载或解码过程中发生错误显示的图片
            .build();

    /*圈圈默认背景*/
    public static final DisplayImageOptions CIRCLE_BACKGROUND_IMAGE = new DisplayImageOptions.Builder()
            .cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheOnDisk(true)
            .bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
            .showStubImage(R.mipmap.header) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.header) // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.mipmap.header) // 设置图片加载或解码过程中发生错误显示的图片
            .build();

    /**
     * 朋友圈我的图片默认
     **/
    public static final DisplayImageOptions CIRCLE_IMAGE = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.circle_moren) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.drawable.circle_moren) // 设置图片Uri为空或是错误的时候显示的图片
            .cacheInMemory(false)
            .cacheOnDisk(true)
            .displayer(new FadeInBitmapDisplayer(100))
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .showImageOnFail(R.drawable.circle_moren) // 设置图片加载或解码过程中发生错误显示的图片
            .build();
    /**
     * 朋友圈我的图片默认
     **/
    public static final DisplayImageOptions CIRCLE_HOME_IMAGE = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.header) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.header) // 设置图片Uri为空或是错误的时候显示的图片
            .cacheInMemory(false)
            .cacheOnDisk(true)
            .displayer(new FadeInBitmapDisplayer(100))
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .showImageOnFail(R.mipmap.header) // 设置图片加载或解码过程中发生错误显示的图片
            .build();
}
