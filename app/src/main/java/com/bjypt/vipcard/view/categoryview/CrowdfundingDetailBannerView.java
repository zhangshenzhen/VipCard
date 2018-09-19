package com.bjypt.vipcard.view.categoryview;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.crowdfunding.projectdetail.entity.HybAttachmentListBean;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.widget.FlyBanner;
import com.sinia.orderlang.utils.StringUtil;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CrowdfundingDetailBannerView extends LinearLayout {

    private ArrayList<String> adv_list = new ArrayList<>();
    private FlyBanner viewpager_new_home;

    private VideoView videoView;
    private RelativeLayout relate_play;

    String videoUrl = null;
    String videoBg = null;

    public CrowdfundingDetailBannerView(Context context) {
        super(context);
    }

    public CrowdfundingDetailBannerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CrowdfundingDetailBannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_crowdfunding_banner, this);
        viewpager_new_home = (FlyBanner) findViewById(R.id.viewpager_new_home);
        videoView = findViewById(R.id.videoView);
        relate_play = findViewById(R.id.relate_play);
        // 设置指示点位置
        viewpager_new_home.setPoinstPosition(2);

    }

    public void setDataSource(List<HybAttachmentListBean> listBeans) {

        if (listBeans == null) {
            return;
        }

        for (int i = 0; i < listBeans.size(); i++) {
            if (listBeans.get(i).getAttach_type() == 1) {
                videoUrl = listBeans.get(i).getAttachment();
            } else {
                videoBg = listBeans.get(i).getAttachment();
            }
        }
        if (videoUrl == null) {
            relate_play.setVisibility(View.GONE);
            viewpager_new_home.setVisibility(View.VISIBLE);
            if (adv_list.size() > 0) {
                adv_list.clear();
            }
            for (int i = 0; i < listBeans.size(); i++) {
                adv_list.add(listBeans.get(i).getAttachment());
            }
            if (adv_list.size() > 0) {
                viewpager_new_home.setImagesUrl(adv_list);
            } else {
                List<Integer> images = new ArrayList<>();
                images.add(R.mipmap.ad_bg);
                images.add(R.mipmap.ad_bg);
                viewpager_new_home.setImages(images);
            }
        } else {
            playVedio();
        }
    }

    private void playVedio() {
        if (!videoView.isPlaying()) {
            relate_play.setVisibility(View.VISIBLE);
            videoView.setVideoPath(videoUrl);
            MediaController mediaController = new MediaController(getContext(), false);
            videoView.setMediaController(mediaController);
            videoView.setZOrderOnTop(true);
            videoView.setZOrderMediaOverlay(true);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    videoView.seekTo(200);//2000000
                    mediaController.show();
                }
            });
        }
    }

    public void resumePlay() {
        if (StringUtil.isNotEmpty(videoUrl)) {
            playVedio();
        }
    }


}
