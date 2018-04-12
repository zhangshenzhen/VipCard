package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bjypt.vipcard.base.BaseActivity;

/**
 * Created by Dell on 2018/3/29.
 */

public class MyRemainingSumGridViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private int[] imageIds = {};

    public MyRemainingSumGridViewAdapter(Context context){
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
