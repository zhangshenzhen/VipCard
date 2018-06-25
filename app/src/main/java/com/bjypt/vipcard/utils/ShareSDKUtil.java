package com.bjypt.vipcard.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.wxapi.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sinia.orderlang.utils.StringUtil;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;

/**
 * Created by 崔龙 on 2017/11/8.
 * <p>
 * 分享工具类
 */

public class ShareSDKUtil {
    /**
     * 分享到qq
     *
     * @param context    上下文
     * @param mQRCodeURL 打开时链接
     * @param filepath   文件路径
     */
    public static void shareQZone(Context context, String mQRCodeURL, String filepath) {

        QZone.ShareParams sp = new QZone.ShareParams();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        sp.setTitle("繁城都市分享有礼");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        sp.setTitleUrl(mQRCodeURL);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_ic_launcher);
        sp.setImageData(bitmap);
        sp.setComment("点击下载繁城app，加油95折，分享朋友圈，你可得到朋友加油1%的收益");
        sp.setText("点击下载繁城app，加油95折，分享朋友圈，你可得到朋友加油1%的收益");
        // site是分享此内容的网站名称，仅在QQ空间使用
        sp.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        sp.setSiteUrl(mQRCodeURL);
        Platform qq = ShareSDK.getPlatform(QZone.NAME);
        qq.setPlatformActionListener((PlatformActionListener) context); // 设置分享事件回调
        qq.share(sp);
    }

    /**
     * 分享到qq  web
     *
     * @param context 上下文
     * @param url     链接
     * @param title   标题
     * @param content 内容
     */
    public static void shareQZoneWeb(Context context, String picUrl, String url, String title, String content) {
        QZone.ShareParams sp = new QZone.ShareParams();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        sp.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        sp.setTitleUrl(url);
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_ic_launcher);
//        sp.setImageData(bitmap);
        if (StringUtil.isNotEmpty(picUrl)) {
            sp.setImageUrl(picUrl);
        } else {
            sp.setImageUrl("https://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/userposition_aa6a2eb5bfd84a62850c7df573e4f1a2.jpg");
        }
        sp.setComment(content);
        sp.setText(content);
        // site是分享此内容的网站名称，仅在QQ空间使用
        sp.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        sp.setSiteUrl(url);
        Platform qq = ShareSDK.getPlatform(QZone.NAME);
        qq.setPlatformActionListener((PlatformActionListener) context); // 设置分享事件回调
        qq.share(sp);
    }

    /**
     * 分享到qq  web
     *
     * @param context 上下文
     * @param url     链接
     * @param title   标题
     * @param content 内容
     */
    public static void shareQQWeb(Context context, String picUrl, String url, String title, String content) {
        QQ.ShareParams sp = new QQ.ShareParams();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        sp.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        sp.setTitleUrl(url);
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_ic_launcher);
//        sp.setImageData(bitmap);
        if (StringUtil.isNotEmpty(picUrl)) {
            sp.setImageUrl(picUrl);
        } else {
            sp.setImageUrl("https://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/userposition_aa6a2eb5bfd84a62850c7df573e4f1a2.jpg");
        }
        sp.setComment(content);
        sp.setText(content);
        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        platform.setPlatformActionListener((PlatformActionListener) context); // 设置分享事件回调
        platform.share(sp);
    }

    /**
     * 微信、朋友圈分享
     *
     * @param context    上下文
     * @param mQRCodeURL 点击打开的链接
     * @param mWxApi     传入微信api
     * @param type       1：微信好友分享；2：微信朋友全
     */
    public static void shareWechat(Context context, String mQRCodeURL, IWXAPI mWxApi, int type) {
        SharedPreferenceUtils.saveToSharedPreference(context, "shareType", "app");
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            ToastUtil.showToast(context, "您还未安装微信客户端");
            return;
        }
        // 初始化
        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = mQRCodeURL;

        WXMediaMessage wxMediaMessage = new WXMediaMessage(wxWebpageObject);
        wxMediaMessage.mediaObject = wxWebpageObject;
        wxMediaMessage.title = "繁城都市分享有礼";
        wxMediaMessage.description = "点击下载繁城app，加油95折，分享朋友圈，你可得到朋友加油1%的收益";
        wxMediaMessage.thumbData =
                Util.bmpToByteArray(BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_ic_launcher), true);
        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = wxMediaMessage;
        switch (type) {
            case 1:
                req.scene = SendMessageToWX.Req.WXSceneSession;
                break;
            case 2:
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                break;
        }
        mWxApi.sendReq(req);
    }

    /**
     * 微信、朋友圈分享 Web分享
     *
     * @param context 上下文
     * @param url     链接
     * @param mWxApi  api
     * @param type    类型
     * @param title   标题
     * @param content 内容
     */
    public static void shareWechatWeb(final Context context, String picUrl, final String url, final IWXAPI mWxApi, final int type, final String title, final String content) {
        SharedPreferenceUtils.saveToSharedPreference(context, "shareType", "web");
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            ToastUtil.showToast(context, "您还未安装微信客户端");
            return;
        }
        Glide.with(context).load(picUrl).asBitmap().into(new SimpleTarget<Bitmap>(200,200) {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                // 构造一个Req
                // 初始化
                WXWebpageObject wxWebpageObject = new WXWebpageObject();
                wxWebpageObject.webpageUrl = url;

                WXMediaMessage wxMediaMessage = new WXMediaMessage(wxWebpageObject);
                wxMediaMessage.mediaObject = wxWebpageObject;
                wxMediaMessage.title = title;
                wxMediaMessage.description = content;
//                Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 120, 150, true);
                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                wxMediaMessage.thumbData = Util.bmpToByteArray(bitmap, true);

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = wxMediaMessage;
                switch (type) {
                    case 1:
                        req.scene = SendMessageToWX.Req.WXSceneSession;
                        break;
                    case 2:
                        req.scene = SendMessageToWX.Req.WXSceneTimeline;
                        break;
                }
                mWxApi.sendReq(req);
            }
        });

    }

    /**
     * 分享到微博
     *
     * @param context    上下文
     * @param mQRCodeURL 打开时链接
     * @param filepath   文件路径
     */
    public static void shareSinaWeibo(Context context, String mQRCodeURL, String filepath) {
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        sp.setTitle("繁城都市分享有礼");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        sp.setTitleUrl(mQRCodeURL);
        // text是分享文本，所有平台都需要这个字段
//        sp.setText("分享注册成功后即可获得300积分，并且分享人可以获得部分奖励，积分可用于优惠抵扣1:1，商家消费，充值话费均可当钱使用");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        if (filepath != null) {
//            sp.setImagePath(filepath);//确保SDcard下面存在此张图片
//        }
        sp.setImageData(BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_ic_launcher));
//        sp.setImageUrl(mQRCodeURL);
        // url仅在微信（包括好友和朋友圈）中使用
//        sp.setUrl(mQRCodeURL);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        sp.setComment("点击下载繁城app，加油95折，分享朋友圈，你可得到朋友加油1%的收益");
        // site是分享此内容的网站名称，仅在QQ空间使用
        sp.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        sp.setSiteUrl(mQRCodeURL);
        Platform qq = ShareSDK.getPlatform(SinaWeibo.NAME);
        qq.SSOSetting(false);
        qq.setPlatformActionListener((PlatformActionListener) context); // 设置分享事件回调
        qq.authorize();
        qq.share(sp);
    }
}
