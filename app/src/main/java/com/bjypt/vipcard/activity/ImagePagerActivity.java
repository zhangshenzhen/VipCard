package com.bjypt.vipcard.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.SDCardUtil;
import com.bjypt.vipcard.view.ToastUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by yiw on 2016/1/6.
 */
public class ImagePagerActivity extends YWActivity {
    public static final String INTENT_IMGURLS = "imgurls";
    public static final String INTENT_POSITION = "position";
    public static final String INTENT_IMAGESIZE = "imagesize";

    private List<View> guideViewList = new ArrayList<View>();
    private LinearLayout guideGroup;
    private LinearLayout ll_back;
    public ImageSize imageSize;
    private int startPos;
    private ArrayList<String> imgUrls;
    private TextView tv_number;
    private TextView preservation_photo;
    private ImageView iv_load;
    private String mImgurl;
    private ImageView mImageView;
    private AjaxCallBack callBack;


    public static void startImagePagerActivity(Context context, List<String> imgUrls, int position, ImageSize imageSize) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putStringArrayListExtra(INTENT_IMGURLS, new ArrayList<String>(imgUrls));
        intent.putExtra(INTENT_POSITION, position);
        intent.putExtra(INTENT_IMAGESIZE, imageSize);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagepager);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        iv_load = (ImageView) findViewById(R.id.iv_load);
        tv_number = (TextView) findViewById(R.id.tv_number);
        preservation_photo = (TextView) findViewById(R.id.preservation_photo);
        guideGroup = (LinearLayout) findViewById(R.id.guideGroup);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        preservation_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CircleImageUtil.saveImageToGallery(ImagePagerActivity.this, bitmap);
                if (!SDCardUtil.isSDCardEnable()) {
                    //
                    return;
                }
                File appDir = new File(Config.path.FOLDER_PATH, "Download/");
                if (!appDir.exists()) {
                    appDir.mkdirs();
                }
//                String url = "https://www.baidu.com/img/bd_logo1.png";
                String url = imgUrls.get(viewPager.getCurrentItem());
                final File netFile = new File(url);

                final File file = new File(appDir.getPath(), netFile.getName());
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(ImagePagerActivity.this));
                    ImageLoader.getInstance().loadImage("", new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            //
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
                }
                FinalHttp http = new FinalHttp();

                callBack = new AjaxCallBack<File>() {

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {// 下载失败
                        super.onFailure(t, errorNo, strMsg);
                        LogUtil.debugPrint("下载失败" + strMsg);

                        ToastUtil.showToast(ImagePagerActivity.this, "下载失败");
                    }

                    @Override
                    public void onStart() {// 开始下载
                        super.onStart();
                        LogUtil.debugPrint("开始下载");
                    }

                    @Override
                    public void onSuccess(File t) {// 下载成功
                        super.onSuccess(t);
                        LogUtil.debugPrint("下载完成");
                        // 其次把文件插入到系统图库
                        try {
                            MediaStore.Images.Media.insertImage(getContentResolver(),
                                    t.getAbsolutePath(), netFile.getName(), null);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        // 最后通知图库更新
                        MediaScannerConnection.scanFile(ImagePagerActivity.this, new String[]{t.getPath()}, null, null);
                        ToastUtil.showToast(ImagePagerActivity.this, "已经保存在SD卡/hyb/Download/" + t.getName());
                    }

                    @Override
                    public void onLoading(long count, long current) {// 正在下载
                        super.onLoading(count, current);
                        LogUtil.debugPrint("正在下载");
                    }

                };
                http.download(url, String.valueOf(file.getPath()), callBack);

            }
        });
        getIntentData();

        ImageAdapter mAdapter = new ImageAdapter(this);
        mAdapter.setDatas(imgUrls);
        mAdapter.setImageSize(imageSize);
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < guideViewList.size(); i++) {
                    guideViewList.get(i).setSelected(i == position ? true : false);
                }
                tv_number.setText(position + 1 + "/" + imgUrls.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(startPos);

        addGuideView(guideGroup, startPos, imgUrls);

    }

    private void getIntentData() {
        startPos = getIntent().getIntExtra(INTENT_POSITION, 0);
        imgUrls = getIntent().getStringArrayListExtra(INTENT_IMGURLS);
        imageSize = (ImageSize) getIntent().getSerializableExtra(INTENT_IMAGESIZE);
        tv_number.setText(startPos + 1 + "/" + imgUrls.size());
    }

    private void addGuideView(LinearLayout guideGroup, int startPos, ArrayList<String> imgUrls) {
        if (imgUrls != null && imgUrls.size() > 0) {
            guideViewList.clear();
            for (int i = 0; i < imgUrls.size(); i++) {
                View view = new View(this);
                view.setBackgroundResource(R.drawable.selector_guide_bg);
                view.setSelected(i == startPos ? true : false);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.gudieview_width),
                        getResources().getDimensionPixelSize(R.dimen.gudieview_heigh));
                layoutParams.setMargins(10, 0, 0, 0);
                guideGroup.addView(view, layoutParams);
                guideViewList.add(view);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private class ImageAdapter extends PagerAdapter {

        private List<String> datas = new ArrayList<String>();
        private LayoutInflater inflater;
        private Context context;
        private ImageSize imageSize;
        private ImageView smallImageView = null;

        public void setDatas(List<String> datas) {
            if (datas != null)
                this.datas = datas;
        }

        public void setImageSize(ImageSize imageSize) {
            this.imageSize = imageSize;
        }

        public ImageAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (datas == null) return 0;
            return datas.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = inflater.inflate(R.layout.item_pager_image, container, false);
            if (view != null) {
                mImageView = (ImageView) view.findViewById(R.id.image);
                mImageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
//                        ToastUtil.showToast(ImagePagerActivity.this,"长按了"+position);
                        return false;
                    }
                });
                if (imageSize != null) {
                    //预览imageView
                    smallImageView = new ImageView(context);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageSize.getWidth(), imageSize.getHeight());
                    layoutParams.gravity = Gravity.CENTER;
                    smallImageView.setLayoutParams(layoutParams);
                    smallImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ((FrameLayout) view).addView(smallImageView);
                }

                //loading
                final ProgressBar loading = new ProgressBar(context);
                FrameLayout.LayoutParams loadingLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                loadingLayoutParams.gravity = Gravity.CENTER;
                loading.setLayoutParams(loadingLayoutParams);
                ((FrameLayout) view).addView(loading);

                mImgurl = datas.get(position);

                Glide.with(context)
                        .load(mImgurl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存多个尺寸
                        .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                        .error(R.mipmap.circle_photo_download)
                        .into(new GlideDrawableImageViewTarget(mImageView) {
                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                super.onLoadStarted(placeholder);
                               /* if(smallImageView!=null){
                                    smallImageView.setVisibility(View.VISIBLE);
                                    Glide.with(context).load(imgurl).into(smallImageView);
                                }*/
                                loading.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                /*if(smallImageView!=null){
                                    smallImageView.setVisibility(View.GONE);
                                }*/
                                loading.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);
                                loading.setVisibility(View.INVISIBLE);
                                /*if(smallImageView!=null){
                                    smallImageView.setVisibility(View.GONE);
                                }*/
                            }
                        });

                container.addView(view, 0);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }


    }

    @Override
    protected void onDestroy() {
        guideViewList.clear();
        super.onDestroy();
    }

    public static class ImageSize implements Serializable {

        private int width;
        private int height;

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }
}
