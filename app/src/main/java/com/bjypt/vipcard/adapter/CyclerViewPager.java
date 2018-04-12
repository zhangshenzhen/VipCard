package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 崔龙 on 2017/4/13.
 */

public class CyclerViewPager extends ViewPager {
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int currentItem = getCurrentItem();
            currentItem += 1;
            setCurrentItem(currentItem);
            sendEmptyMessageDelayed(0, 5000);
        }
    };
    public CyclerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CyclerViewPager(Context context) {
        super(context);
    }
    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        InnerOnPageChangeListener innerOnPageChangeListener = new InnerOnPageChangeListener(
                listener);
        super.setOnPageChangeListener(innerOnPageChangeListener);
    }

    class InnerOnPageChangeListener implements OnPageChangeListener {

        private OnPageChangeListener listener;

        public InnerOnPageChangeListener(OnPageChangeListener listener) {
            this.listener = listener;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            if (listener != null) {
                listener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }

        }

        private int position;

        // 页面切换的时候调用的方法
        @Override
        public void onPageSelected(int position) {
            this.position = position;
            if (listener != null) {
                listener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (position == getAdapter().getCount() - 1) {
                    // A元素
                    setCurrentItem(1, false);
                } else if (position == 0) {
                    // D元素
                    setCurrentItem(getAdapter().getCount() - 2, false);
                }
            }

            if (listener != null) {
                listener.onPageScrollStateChanged(state);
            }
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        InnerPagerAdapter innerPagerAdapter = new InnerPagerAdapter(adapter);
        super.setAdapter(innerPagerAdapter);
        setOnPageChangeListener(null);
        setCurrentItem(1);// [ABCD] --->[DABCDA]
        startScroll();
    }

    private void startScroll() {
        // 轮播
        handler.sendEmptyMessageDelayed(0, 5000);
    }

    private void stopScroll() {
        handler.removeMessages(0);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopScroll();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                startScroll();
                break;

            default:
                break;
        }

        return super.onTouchEvent(ev);
    }

    class InnerPagerAdapter extends PagerAdapter {
        private PagerAdapter adapter;

        public InnerPagerAdapter(PagerAdapter adapter) {
            this.adapter = adapter;
        }

        // viewpager页面数量
        @Override
        public int getCount() {
            return adapter.getCount() + 2; // [ABCD] ---> [DABCDA]
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return adapter.isViewFromObject(view, object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // postion [DABCDA] 索引
            if (position == getCount() - 1) {
                // 如果 要获取最后一个A元素 就让他 拿 最前边的A元素
                position = 0; // [ABCD] 中的索引
            } else if (position == 0) {
                // D
                position = adapter.getCount() - 1;
            } else {
                position -= 1;
            }
            // 3. 返回 view对象

            return adapter.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            adapter.destroyItem(container, position, object);
        }

    }
}
