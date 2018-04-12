package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.PointCommonBean;
import com.bjypt.vipcard.model.PointRecordInfo;
import com.bjypt.vipcard.model.PointRecordListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/5/17
 * Use by
 */
public class PointRecordAdapter extends MyBaseAdapter {

    private Context mContext;
    private int flag;
    private List<PointCommonBean> list = new ArrayList<>();


    public PointRecordAdapter(Context mContext,List<PointCommonBean> list,int flag) {

        super(list, mContext);
        this.list = list;
        this.mContext = mContext;
        this.flag = flag;
    }

    @Override
    public View initView(int position, View convertView, ViewGroup parent) {
        PointHolder pointHolder;
        if(convertView == null){
            pointHolder = new PointHolder();
            convertView = inflater.inflate(R.layout.point_record_item, null);
            pointHolder.mPointBg = (ImageView) convertView.findViewById(R.id.point_bg);
            pointHolder.mPointType = (TextView) convertView.findViewById(R.id.point_type);
            pointHolder.mPointTime = (TextView) convertView.findViewById(R.id.point_time);
            pointHolder.mPointNum = (TextView) convertView.findViewById(R.id.point_num);
            convertView.setTag(pointHolder);
        }else{
            pointHolder = (PointHolder) convertView.getTag();
        }

        if(flag == 1){
            //积分记录
            pointHolder.mPointBg.setImageResource(R.mipmap.point_record);
            pointHolder.mPointNum.setTextColor(Color.parseColor("#ED5F51"));
        }else if(flag == 2){
            //消分记录
            pointHolder.mPointBg.setImageResource(R.mipmap.use_point);
            pointHolder.mPointNum.setTextColor(Color.parseColor("#33CB98"));
        }
        if(list.get(position).getPointtype().equals("1")){
            //充值获取
            pointHolder.mPointType.setText("充值获取");
        }else if(list.get(position).getPointtype().equals("2")){
            //消费获取
            pointHolder.mPointType.setText("消费获取");
        }else if(list.get(position).getPointtype().equals("3")){
            //消费扣除
            pointHolder.mPointType.setText("消费扣除");
        }else if(list.get(position).getPointtype().equals("4")){
            //系统发送
            pointHolder.mPointType.setText("系统发送");
        }
        pointHolder.mPointTime.setText(list.get(position).getCreatetime());
        pointHolder.mPointNum.setText(list.get(position).getPoint());

        return convertView;
    }

    class PointHolder{
        private ImageView mPointBg;
        private TextView mPointType;
        private TextView mPointTime;
        private TextView mPointNum;
    }
}
