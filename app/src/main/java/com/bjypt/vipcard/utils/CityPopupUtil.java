package com.bjypt.vipcard.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.CITYS;
import com.bjypt.vipcard.model.CitysBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyunte on 2017/1/10.
 */

public class CityPopupUtil {
private String provider="";

private Activity context ;
    private boolean isProvider;
    private List<CITYS> lists = new ArrayList<>();
    private List<CitysBean> list_s = new ArrayList<>();
    private MyPopupWindows myPopupWindows;

    public CityPopupUtil(Activity context,boolean isProvider,String provider) {
        this.context = context;
        this.isProvider = isProvider;
        this.provider = provider;
        lists.addAll(ParserAssetsUtil.initJsonData(context));
        init();
        initView();
    }

    private void initView() {
        myPopupWindows = new MyPopupWindows(context, R.layout.layout_popup_city);
        myPopupWindows.setMyCallBack(new MyPopupWindows.MyCallBack() {
            @Override
            public void func(View view) {
                ListView listview_city = (ListView) view.findViewById(R.id.listview_city);
                View view_popup_close = (View) view.findViewById(R.id.view_popup_close);
                view_popup_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myPopupWindows.dismiss();
                    }
                });
                listview_city.setAdapter(new MyAdapter(list_s,context));

            }
        });
    }

    public void init(){
        if (isProvider){
            for (int i= 0;i<lists.size();i++){
                CitysBean citysBean = new CitysBean();
                    citysBean.setCity(lists.get(i).getProvince());
                citysBean.setCoder("");
              list_s.add(citysBean);
            }
        }else {
            for (int i= 0;i<lists.size();i++){
                if (lists.get(i).getProvince().equals(provider)){
                    list_s.addAll(lists.get(i).getCitys());
                }

            }
        }



    }
    class MyAdapter extends BaseAdapter{
private List<CitysBean> list;
        private Context context;

        public MyAdapter(List<CitysBean> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            MyViewHolder holder = null;
            if (convertView==null){
                holder = new MyViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_listview_item_city,parent,false);
                holder.tv = (TextView) convertView.findViewById(R.id.tv_listview);
                convertView.setTag(holder);
            }else {
                holder = (MyViewHolder) convertView.getTag();
            }
            holder.tv.setText(list.get(position).getCity());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myPopupWindows.dismiss();
                    if (listener!=null){
                        listener.click(list.get(position).getCity());

                    }
                }
            });
            return convertView;
        }
    }
    class MyViewHolder{
        TextView tv;
    }
    public void show(View view){
        myPopupWindows.show(view,0,0);
    }
    public void diss(){
        myPopupWindows.dismiss();
    }
    private OnItemColickListener listener;
    public void setOnItemClickListner( OnItemColickListener listener){
        this.listener = listener;
    }
   public interface OnItemColickListener{
        public void click(String value);
    }
}
