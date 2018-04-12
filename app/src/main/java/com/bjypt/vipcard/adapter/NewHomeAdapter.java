package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.NewHomeAdapterBean;
import com.bjypt.vipcard.model.XinWenAdBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by liyunte on 2017/1/5.
 */

public class NewHomeAdapter extends BaseAdapter {
    private Context context;
    private List<XinWenAdBean> list;
    private List<Integer> picList ;

    public NewHomeAdapter(Context context, List<XinWenAdBean> list,List<Integer> picList) {
        this.context = context;
        this.list = list;
        this.picList = picList;
        Wethod.configImageLoader(context);
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

    public void add(List<XinWenAdBean> l){
        if(list == null){
            list = l;
        }else{
            list.addAll(l);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyViewOneHolder oneHolder = null;
        MyViewTwoHolder twoHolder = null;
        MyViewThreeHolder threeHolder = null;
        MyViewFourHolder fourHolder = null;

        if(convertView == null){
            Log.e("xinwenAd","null::"+picList.get(position));
            switch (picList.get(position)){
                case 0:
                    oneHolder = new MyViewOneHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.new_home_xinwen_one,parent,false);
                    oneHolder.xinwen_one_title = (TextView) convertView.findViewById(R.id.xinwen_one_title);
                    oneHolder.xinwen_one_pinglun = (TextView) convertView.findViewById(R.id.xinwen_one_pinglun);
                    oneHolder.xinwen_one_wangzhan = (TextView) convertView.findViewById(R.id.xinwen_one_wangzhan);
                    oneHolder.xinwen_one_time = (TextView) convertView.findViewById(R.id.xinwen_one_time);

                    oneHolder.xinwen_one_title.setText(list.get(position).getTitle());


                    convertView.setTag(R.id.xinwen_one,oneHolder);
                    break;
                case 1:
                    twoHolder = new MyViewTwoHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.new_home_xinwen_two,parent,false);
                    twoHolder.xinwen_two_title = (TextView) convertView.findViewById(R.id.xinwen_two_title);
                    twoHolder.xinwen_two_pinglun = (TextView) convertView.findViewById(R.id.xinwen_two_pinglun);
                    twoHolder.xinwen_two_wangzhan = (TextView) convertView.findViewById(R.id.xinwen_two_wangzhan);
                    twoHolder.xinwen_two_time = (TextView) convertView.findViewById(R.id.xinwen_two_time);
                    twoHolder.xinwen_two_pic = (ImageView) convertView.findViewById(R.id.xinwen_two_pic);

                    twoHolder.xinwen_two_title.setText(list.get(position).getTitle());
                    if ("".equals(list.get(position).getPics().get(0).getUrl())) {
                        twoHolder.xinwen_two_pic.setImageResource(R.mipmap.xinwen_one);
                    } else {
                        ImageLoader.getInstance().displayImage(list.get(position).getPics().get(0).getUrl(), twoHolder.xinwen_two_pic, AppConfig.XINWEN_IMG_OPTIONS_ONE);
                    }

                    convertView.setTag(R.id.xinwen_two,twoHolder);
                    break;
                case 2:
                    threeHolder = new MyViewThreeHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.new_home_xinwen_three,parent,false);
                    threeHolder.xinwen_three_title = (TextView) convertView.findViewById(R.id.xinwen_three_title);
                    threeHolder.xinwen_three_pinglun = (TextView) convertView.findViewById(R.id.xinwen_three_pinglun);
                    threeHolder.xinwen_three_wangzhan = (TextView) convertView.findViewById(R.id.xinwen_three_wangzhan);
                    threeHolder.xinwen_three_time = (TextView) convertView.findViewById(R.id.xinwen_three_time);
                    threeHolder.xinwen_three_pci_one = (ImageView) convertView.findViewById(R.id.xinwen_three_pci_one);
                    threeHolder.xinwen_three_pci_two = (ImageView) convertView.findViewById(R.id.xinwen_three_pci_two);

                    threeHolder.xinwen_three_title.setText(list.get(position).getTitle());
                    if ("".equals(list.get(position).getPics().get(0).getUrl())) {
                        threeHolder.xinwen_three_pci_one.setImageResource(R.mipmap.xinwen_two);
                    } else {
                        ImageLoader.getInstance().displayImage(list.get(position).getPics().get(0).getUrl(), threeHolder.xinwen_three_pci_one, AppConfig.XINWEN_IMG_OPTIONS_TWO);
                    }

                    if ("".equals(list.get(position).getPics().get(1).getUrl())) {
                        threeHolder.xinwen_three_pci_two.setImageResource(R.mipmap.xinwen_two);
                    } else {
                        ImageLoader.getInstance().displayImage(list.get(position).getPics().get(1).getUrl(), threeHolder.xinwen_three_pci_two, AppConfig.XINWEN_IMG_OPTIONS_TWO);
                    }

                    convertView.setTag(R.id.xinwen_three,threeHolder);
                    break;
                case 3:
                    fourHolder = new MyViewFourHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.new_home_xinwen_four,parent,false);
                    fourHolder.xinwen_four_title = (TextView) convertView.findViewById(R.id.xinwen_four_title);
                    fourHolder.xinwen_four_pinglun = (TextView) convertView.findViewById(R.id.xinwen_four_pinglun);
                    fourHolder.xinwen_four_wangzhan = (TextView) convertView.findViewById(R.id.xinwen_four_wangzhan);
                    fourHolder.xinwen_four_time = (TextView) convertView.findViewById(R.id.xinwen_four_time);
                    fourHolder.xinwen_four_pic_one = (ImageView) convertView.findViewById(R.id.xinwen_four_pic_one);
                    fourHolder.xinwen_four_pic_two = (ImageView) convertView.findViewById(R.id.xinwen_four_pic_two);
                    fourHolder.xinwen_four_pic_three = (ImageView) convertView.findViewById(R.id.xinwen_four_pic_three);

                    fourHolder.xinwen_four_title.setText(list.get(position).getTitle());

                    if ("".equals(list.get(position).getPics().get(0).getUrl())) {
                        fourHolder.xinwen_four_pic_one.setImageResource(R.mipmap.xinwen_two);
                    } else {
                        ImageLoader.getInstance().displayImage(list.get(position).getPics().get(0).getUrl(), fourHolder.xinwen_four_pic_one, AppConfig.XINWEN_IMG_OPTIONS_THREE);
                    }

                    if ("".equals(list.get(position).getPics().get(1).getUrl())) {
                        fourHolder.xinwen_four_pic_two.setImageResource(R.mipmap.xinwen_two);
                    } else {
                        ImageLoader.getInstance().displayImage(list.get(position).getPics().get(1).getUrl(), fourHolder.xinwen_four_pic_two, AppConfig.XINWEN_IMG_OPTIONS_THREE);
                    }

                    if ("".equals(list.get(position).getPics().get(2).getUrl())) {
                        fourHolder.xinwen_four_pic_three.setImageResource(R.mipmap.xinwen_three);
                    } else {
                        ImageLoader.getInstance().displayImage(list.get(position).getPics().get(2).getUrl(), fourHolder.xinwen_four_pic_three, AppConfig.XINWEN_IMG_OPTIONS_THREE);
                    }



                    convertView.setTag(R.id.xinwen_four,fourHolder);
                    break;
            }
        }else{
            switch (picList.get(position)){
                case 0:
                    oneHolder = (MyViewOneHolder) convertView.getTag(R.id.xinwen_one);

                    if(oneHolder ==null){
                        oneHolder = new MyViewOneHolder();
                        convertView = LayoutInflater.from(context).inflate(R.layout.new_home_xinwen_one,parent,false);
                        oneHolder.xinwen_one_title = (TextView) convertView.findViewById(R.id.xinwen_one_title);
                        oneHolder.xinwen_one_pinglun = (TextView) convertView.findViewById(R.id.xinwen_one_pinglun);
                        oneHolder.xinwen_one_wangzhan = (TextView) convertView.findViewById(R.id.xinwen_one_wangzhan);
                        oneHolder.xinwen_one_time = (TextView) convertView.findViewById(R.id.xinwen_one_time);
                        oneHolder.xinwen_one_title.setText(list.get(position).getTitle());
                    }



                    break;
                case 1:
                    twoHolder = (MyViewTwoHolder) convertView.getTag(R.id.xinwen_two);
                    if(twoHolder==null){
                        twoHolder = new MyViewTwoHolder();
                        convertView = LayoutInflater.from(context).inflate(R.layout.new_home_xinwen_two,parent,false);
                        twoHolder.xinwen_two_title = (TextView) convertView.findViewById(R.id.xinwen_two_title);
                        twoHolder.xinwen_two_pinglun = (TextView) convertView.findViewById(R.id.xinwen_two_pinglun);
                        twoHolder.xinwen_two_wangzhan = (TextView) convertView.findViewById(R.id.xinwen_two_wangzhan);
                        twoHolder.xinwen_two_time = (TextView) convertView.findViewById(R.id.xinwen_two_time);
                        twoHolder.xinwen_two_pic = (ImageView) convertView.findViewById(R.id.xinwen_two_pic);
                        twoHolder.xinwen_two_title.setText(list.get(position).getTitle());
                        if ("".equals(list.get(position).getPics().get(0).getUrl())) {
                            twoHolder.xinwen_two_pic.setImageResource(R.mipmap.xinwen_one);
                        } else {
                            ImageLoader.getInstance().displayImage( list.get(position).getPics().get(0).getUrl(), twoHolder.xinwen_two_pic, AppConfig.XINWEN_IMG_OPTIONS_ONE);
                        }
                    }

                    break;
                case 2:
                    threeHolder = (MyViewThreeHolder) convertView.getTag(R.id.xinwen_three);
                    if(threeHolder == null){
                        threeHolder = new MyViewThreeHolder();
                        convertView = LayoutInflater.from(context).inflate(R.layout.new_home_xinwen_three,parent,false);
                        threeHolder.xinwen_three_title = (TextView) convertView.findViewById(R.id.xinwen_three_title);
                        threeHolder.xinwen_three_pinglun = (TextView) convertView.findViewById(R.id.xinwen_three_pinglun);
                        threeHolder.xinwen_three_wangzhan = (TextView) convertView.findViewById(R.id.xinwen_three_wangzhan);
                        threeHolder.xinwen_three_time = (TextView) convertView.findViewById(R.id.xinwen_three_time);
                        threeHolder.xinwen_three_pci_one = (ImageView) convertView.findViewById(R.id.xinwen_three_pci_one);
                        threeHolder.xinwen_three_pci_two = (ImageView) convertView.findViewById(R.id.xinwen_three_pci_two);
                        threeHolder.xinwen_three_title.setText(list.get(position).getTitle());
                        if ("".equals(list.get(position).getPics().get(0).getUrl())) {
                            threeHolder.xinwen_three_pci_one.setImageResource(R.mipmap.xinwen_two);
                        } else {
                            ImageLoader.getInstance().displayImage(list.get(position).getPics().get(0).getUrl(), threeHolder.xinwen_three_pci_one, AppConfig.XINWEN_IMG_OPTIONS_TWO);
                        }

                        if ("".equals(list.get(position).getPics().get(1).getUrl())) {
                            threeHolder.xinwen_three_pci_two.setImageResource(R.mipmap.xinwen_two);
                        } else {
                            ImageLoader.getInstance().displayImage( list.get(position).getPics().get(1).getUrl(), threeHolder.xinwen_three_pci_two, AppConfig.XINWEN_IMG_OPTIONS_TWO);
                        }
                    }

                    break;
                case 3:
                    fourHolder = (MyViewFourHolder) convertView.getTag(R.id.xinwen_four);
                    if(fourHolder == null){
                        fourHolder = new MyViewFourHolder();
                        convertView = LayoutInflater.from(context).inflate(R.layout.new_home_xinwen_four,parent,false);
                        fourHolder.xinwen_four_title = (TextView) convertView.findViewById(R.id.xinwen_four_title);
                        fourHolder.xinwen_four_pinglun = (TextView) convertView.findViewById(R.id.xinwen_four_pinglun);
                        fourHolder.xinwen_four_wangzhan = (TextView) convertView.findViewById(R.id.xinwen_four_wangzhan);
                        fourHolder.xinwen_four_time = (TextView) convertView.findViewById(R.id.xinwen_four_time);
                        fourHolder.xinwen_four_pic_one = (ImageView) convertView.findViewById(R.id.xinwen_four_pic_one);
                        fourHolder.xinwen_four_pic_two = (ImageView) convertView.findViewById(R.id.xinwen_four_pic_two);
                        fourHolder.xinwen_four_pic_three = (ImageView) convertView.findViewById(R.id.xinwen_four_pic_three);
                        fourHolder.xinwen_four_title.setText(list.get(position).getTitle());
                        if ("".equals(list.get(position).getPics().get(0).getUrl())) {
                            fourHolder.xinwen_four_pic_one.setImageResource(R.mipmap.xinwen_two);
                        } else {
                            ImageLoader.getInstance().displayImage(list.get(position).getPics().get(0).getUrl(), fourHolder.xinwen_four_pic_one, AppConfig.XINWEN_IMG_OPTIONS_THREE);
                        }

                        if ("".equals(list.get(position).getPics().get(1).getUrl())) {
                            fourHolder.xinwen_four_pic_two.setImageResource(R.mipmap.xinwen_two);
                        } else {
                            ImageLoader.getInstance().displayImage( list.get(position).getPics().get(1).getUrl(), fourHolder.xinwen_four_pic_two, AppConfig.XINWEN_IMG_OPTIONS_THREE);
                        }

                        if ("".equals(list.get(position).getPics().get(2).getUrl())) {
                            fourHolder.xinwen_four_pic_three.setImageResource(R.mipmap.xinwen_three);
                        } else {
                            ImageLoader.getInstance().displayImage( list.get(position).getPics().get(2).getUrl(), fourHolder.xinwen_four_pic_three, AppConfig.XINWEN_IMG_OPTIONS_THREE);
                        }
                    }

                    break;
            }
        }


        return convertView;
    }
    class MyViewOneHolder {
        TextView xinwen_one_title;
        TextView xinwen_one_wangzhan;
        TextView xinwen_one_pinglun;
        TextView xinwen_one_time;
    }

    class MyViewTwoHolder{
        TextView xinwen_two_title;
        TextView xinwen_two_wangzhan;
        TextView xinwen_two_pinglun;
        TextView xinwen_two_time;
        ImageView xinwen_two_pic;
    }

    class MyViewThreeHolder{
        TextView xinwen_three_title;
        TextView xinwen_three_wangzhan;
        TextView xinwen_three_pinglun;
        TextView xinwen_three_time;
        ImageView xinwen_three_pci_one;
        ImageView xinwen_three_pci_two;
    }

    class MyViewFourHolder{
        TextView xinwen_four_title;
        TextView xinwen_four_wangzhan;
        TextView xinwen_four_pinglun;
        TextView xinwen_four_time;
        ImageView xinwen_four_pic_one;
        ImageView xinwen_four_pic_two;
        ImageView xinwen_four_pic_three;
    }
}
