package com.sinia.orderlang.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class MyViewPagerAdapter extends PagerAdapter {
	public List<View> data;

	public MyViewPagerAdapter(List<View> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data != null ? data.size() : 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}
	@Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        
        ((ViewPager) container).addView(data.get(position), 0);
        
        return data.get(position);
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        ((ViewPager) container).removeView(data.get(position));
    }
}
