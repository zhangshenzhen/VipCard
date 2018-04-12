package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/9
 * Use by Adapter基类
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
    public List<T> list;
    public LayoutInflater inflater;
    public Context mContext;
    public MyBaseAdapter(Context mContext){
        super();
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }
    public MyBaseAdapter(List<T> list,Context mContext){
        super();
        this.list = list;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }
    public void add(List<T> l){
        if(list == null){
            list = l;
        }else{
            list.addAll(l);
        }
    }
    /**
     * 清理
     */
    public void clean(){
        if(list !=null && !list.isEmpty()){
            list.clear();
        }
    }

    @Override
    public int getCount() {
        return list !=null && !list.isEmpty() ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position,convertView,parent);
    }
    public abstract View initView(int position,View convertView,ViewGroup parent);
}
