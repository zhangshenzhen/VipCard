package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.DefaultAdressActivity;
import com.bjypt.vipcard.model.AdressBean;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/1
 * Use by 地址的adapter
 */
public class AdressAdapter extends MyBaseAdapter<AdressBean> {

    private int mUpdNum;
    private MyClickListener listener;

    public AdressAdapter(List<AdressBean> list, Context mContext, MyClickListener listener) {
        super(list, mContext);
        this.listener = listener;
    }

    @Override
    public View initView(final int position, View convertView, ViewGroup parent) {
        AdressHolder adressHolder;
        if (convertView == null) {
            adressHolder = new AdressHolder();
            convertView = inflater.inflate(R.layout.adress_item, null);
            adressHolder.mNameTv = (TextView) convertView.findViewById(R.id.adress_name);
            adressHolder.mPhoneTv = (TextView) convertView.findViewById(R.id.adress_phone);
            adressHolder.mAdressTv = (TextView) convertView.findViewById(R.id.default_adress);
            adressHolder.mDel = (TextView) convertView.findViewById(R.id.tv_del);
            adressHolder.mSettingDefaultAdr = (RelativeLayout) convertView.findViewById(R.id.setting_default_adress);
            adressHolder.default_bg = (ImageView) convertView.findViewById(R.id.default_bg);
//            adressHolder.mupdateInfo = (LinearLayout) convertView.findViewById(R.id.linear_updateInfo);
            convertView.setTag(adressHolder);
        } else {
            adressHolder = (AdressHolder) convertView.getTag();
        }

        adressHolder.mNameTv.setText(list.get(position).getRegistername());
        adressHolder.mPhoneTv.setText(list.get(position).getPhoneno());
        adressHolder.mAdressTv.setText(list.get(position).getReceiptaddress());

        if (list.get(position).getDefaultaddress().equals("1")) {
            adressHolder.default_bg.setImageResource(R.mipmap.adress_click);
            DefaultAdressActivity.returnUpdNum(position);
            Log.i("aaa", "111adapter=" + position);
        } else {
            adressHolder.default_bg.setImageResource(R.mipmap.adress_noclick);
        }

        adressHolder.mSettingDefaultAdr.setOnClickListener(listener);
        adressHolder.mDel.setOnClickListener(listener);
        adressHolder.mSettingDefaultAdr.setTag(position);
        adressHolder.mDel.setTag(position);


//        adressHolder.mupdateInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent mIntent=new Intent(mContext, AddAdressActivity.class);
//                mIntent.putExtra("registername",list.get(position).getRegistername());
//                mIntent.putExtra("phoneno",list.get(position).getPhoneno());
//                mIntent.putExtra("defaultadress",list.get(position).getDefaultaddress());
//                mIntent.putExtra("receiptaddress",list.get(position).getReceiptaddress());
//                mContext.startActivity(mIntent);
//            }
//        });

        return convertView;
    }

    public static abstract class MyClickListener implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }

        public abstract void myOnClick(int position, View v);
    }

    class AdressHolder {
        private TextView mNameTv, mPhoneTv, mAdressTv, mDel;
        private RelativeLayout mSettingDefaultAdr;
        private ImageView default_bg;
    }
}
