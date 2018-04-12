package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.CardMenusBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/12.
 */

public class CardMenusAdapter extends BaseAdapter {

    private List<CardMenusBean.ResultDataBean> menus;
    private Context mContext;

    public CardMenusAdapter(List<CardMenusBean.ResultDataBean> menus, Context mContext) {
        this.menus = menus;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int i) {
        return menus.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MenusViewHolder menusHolder = null;
        if (view == null) {
            menusHolder = new MenusViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_card_menus, null);
            menusHolder.ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
            menusHolder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            menusHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(menusHolder);
        } else {
            menusHolder = (MenusViewHolder) view.getTag();
        }
//        0, 卡充值,
//        1, “消费记录”,
//        2, “礼品金纪录”,
//        3, “余额转出”,
//        4,”积分兑换”,
//        5,”抵扣金说明”,
//        6,”线上商家”,
//        7,”我提款”;
//        8,"卡挂失"
//        9,"解除挂失"

        switch (menus.get(i).getId()){
            case 0:         //卡充值
                menusHolder.iv_icon.setImageResource(R.drawable.chongzhi_card_manage);
                break;
            case 1:        //“消费记录”
                menusHolder.iv_icon.setImageResource(R.drawable.xiaofeijilu_card_manage);
                break;
            case 2:        //“礼品金纪录”
                menusHolder.iv_icon.setImageResource(R.drawable.gifts);
                break;
            case 3:        //“余额转出”
                menusHolder.iv_icon.setImageResource(R.drawable.tk);
                break;
            case 4:        //”积分兑换”
                menusHolder.iv_icon.setImageResource(R.drawable.card_manage_duihuan);
                break;
            case 5:        //”抵扣金说明”
                menusHolder.iv_icon.setImageResource(R.drawable.dk);
                break;
            case 6:        //”线上商家”
                menusHolder.iv_icon.setImageResource(R.drawable.xianshang_card_manage);
                break;
            case 7:        //”我提款”
                menusHolder.iv_icon.setImageResource(R.drawable.tk);
                break;
            case 8:        //"卡挂失"
                menusHolder.iv_icon.setImageResource(R.drawable.jiegua_card_manage);
                break;
            case 9:        //"解除挂失"
                menusHolder.iv_icon.setImageResource(R.drawable.jiegua_card_manage);
                break;
        }

        menusHolder.tv_name.setText(menus.get(i).getName());

        return view;
    }

    class MenusViewHolder{
        private LinearLayout ll_item;
        private ImageView iv_icon;
        private TextView tv_name;
    }
}
