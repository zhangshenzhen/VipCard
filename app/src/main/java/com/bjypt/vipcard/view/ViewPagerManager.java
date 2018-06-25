package com.bjypt.vipcard.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.config.AppConfig;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * 图片轮播器
 * 使用方法 直接匹配构造函数
 * Created by 李允特 on 2016/7/19.
 * <p>
 * 下面是将轮播和魅族手机应用商店的轮播设置成一样的效果
 * android:clipChildren="false"
 * android:layout_marginLeft="15dp"
 * android:layout_marginRight="15dp"
 * <p>
 * //        viewPager.setPageMargin(20);
 * //        viewPager.setOffscreenPageLimit(3);
 */
public class ViewPagerManager {
    Context context;
    private LinearLayout layout;
    private ViewPager viewPager;
    private ArrayList<View> views;
    private MyPagerAdapter pageradapter;
    private int currentitem = 0;
    private int[] imageids;
    private Runnable pagerRunable;
    ArrayList<String> stringArrayList;
    boolean isLooping = true;
    int pre = 1;//方向

    /*
* stringArrayList ：图片资源
* layout：小圆点所在的布局
* */
    public ViewPagerManager(Context context, ArrayList<String> stringArrayList, LinearLayout layout, ViewPager viewPager) {
        this.context = context;
        this.layout = layout;
        this.viewPager = viewPager;
        views = new ArrayList<>();
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);

        ImageLoader.getInstance().init(configuration);
        this.stringArrayList = stringArrayList;
        ViewPagerScroller scroller = new ViewPagerScroller(context);
        scroller.setScrollDuration(2000);
        scroller.initViewPagerScroll(this.viewPager);
        initView();
        initData();
    }

    private void initView() {
        //  layout = (LinearLayout) view.findViewById(R.id.layout);
        /*
        * 对小圆点的layout布局进行设置
        * */
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        pageradapter = new MyPagerAdapter(views);
        viewPager.setAdapter(pageradapter);
        // viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        startloop();
    }

    public void notifyDataChanger() {
        initData();
    }

    private void initData() {
        /*
        * 判断是否是网络上的图片url并保存在views集合里，且初始化小圆点并添加到layout布局中
        * */
        layout.removeAllViews();
        views.clear();
        currentitem = 0;
        if (stringArrayList == null) {
            for (int i = 0; i < imageids.length; i++) {
                views.add(IdtoViews(imageids[i]));
                if (i == currentitem) {
                    layout.addView(roundview(20));
                } else {
                    layout.addView(roundview(10));
                }
            }
        } else {
            for (int i = 0; i < stringArrayList.size(); i++) {
                views.add(IdtoViews(stringArrayList.get(i), i));
                if (i == currentitem) {
                    layout.addView(roundview(20));
                } else {
                    layout.addView(roundview(10));
                }
            }
        }
        pageradapter.notifyDataSetChanged();
        /*
        * viewpager设置监听
        * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentitem = position;//将当前的位置赋值给current用来无限循环处理
                setRoundviewColor();//更新小圆点颜色

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void startloop() {
          /*
        * 无限循环的线程
        * */
        pagerRunable = new Runnable() {
            @Override
            public void run() {
                if (pre > 0) {
                    if (currentitem == views.size() - 1) {
                        pre = -1;
                    }
                } else {
                    if (currentitem == 0) {
                        pre = 1;
                    }
                }
                currentitem = currentitem + pre;
                viewPager.setCurrentItem(currentitem);
                viewPager.postDelayed(pagerRunable, 6000);
            }
        };
        if (isLooping)
            viewPager.postDelayed(pagerRunable, 6000);
    }

    /*
    * 控制是否无限轮循
    * */
    public void setLooping(boolean isLooping) {

        this.isLooping = isLooping;
    }

    /*
    * 显示图片
    * */
    public View IdtoViews(int id) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(id);
        return imageView;
    }

    /*
    * 根据InamgeLoader显示图片
    * */
    public View IdtoViews(final String tr, final int id) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //        imageView.setBackgroundResource(R.drawable.round_strock);
        ImageLoader.getInstance().displayImage(tr, imageView, AppConfig.DEFAULT_LUNBO);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lunBoItemClickListener != null) {
                    lunBoItemClickListener.onLunBoItemClickListener(id);
                }
            }
        });
        return imageView;
    }

    /* public static Bitmap toRoundCornerImage(Bitmap bitmap, int pixels) {
         Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
         Canvas canvas = new Canvas(output);
         final int color = 0xff424242;
         final Paint paint = new Paint();
         final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
         final RectF rectF = new RectF(rect);
         final float roundPx = pixels;
         // 抗锯齿
         paint.setAntiAlias(true);
         canvas.drawARGB(0, 0, 0, 0);
         paint.setColor(color);
         canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
         paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
         canvas.drawBitmap(bitmap, rect, rect, paint);
         return output;
     }*/
    /*
    * 处理小圆点
    * */
    public View roundview(int height) {
        RoundImageView view = new RoundImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height, height);
        params.setMargins(10, 0, 0, 0);//设置左边margin为10
        view.setLayoutParams(params);
        //        view.setBackgroundColor(color);
        view.setImageResource(R.mipmap.app_ic_launcher);
        return view;
    }

    public void setRoundviewColor() {
        layout.removeAllViews();
        for (int i = 0; i < views.size(); i++) {
            if (i == currentitem) {
                layout.addView(roundview(20));
            } else {
                layout.addView(roundview(10));
            }
        }
    }

    /*
    * adapter
    * */
    class MyPagerAdapter extends PagerAdapter {
        private ArrayList<View> list;

        public MyPagerAdapter(ArrayList<View> views) {
            list = views;
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (position < list.size())
                container.removeView(list.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }
    }

    /*
    * 轮播的页面点击事件监听
    * */
    private LunBoItemClickListener lunBoItemClickListener;

    public void setOnLunBoItemClickListener(LunBoItemClickListener lunBoItemClickListener) {
        this.lunBoItemClickListener = lunBoItemClickListener;
    }

    public interface LunBoItemClickListener {
        public void onLunBoItemClickListener(int position);
    }
}
