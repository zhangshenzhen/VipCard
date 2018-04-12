package com.bjypt.vipcard.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.CitizenCardListActivity;
import com.bjypt.vipcard.activity.LifeServireH5Activity;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.dialog.NormalDialog;
import com.bjypt.vipcard.model.CitizenCardListBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */

public class CitizenCanCardListAdapter extends BaseAdapter {

    private CitizenCardListActivity context;
    List<CitizenCardListBean.ResultDataBean.CanBeReceivedListBean> cardInfoList;

    public CitizenCanCardListAdapter(CitizenCardListActivity context, List<CitizenCardListBean.ResultDataBean.CanBeReceivedListBean> cardInfoList) {
        this.context = context;
        this.cardInfoList = cardInfoList;
    }

    @Override
    public int getCount() {
        return cardInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return cardInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.card_list_item, null);
            holder.card_name = (TextView) view.findViewById(R.id.card_name);
            holder.card_num = (TextView) view.findViewById(R.id.card_num);
            holder.tv_explain = (ImageView) view.findViewById(R.id.tv_explain);
            holder.tv_ok_go = (ImageView) view.findViewById(R.id.tv_ok_go);
            holder.go_card_manage = (ImageView) view.findViewById(R.id.go_card_manage);
//            holder.lose = (ImageView) view.findViewById(R.id.lose);
            holder.card_pic = (ImageView) view.findViewById(R.id.card_pic);
            holder.bind_time = (TextView) view.findViewById(R.id.bind_time);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //让Android支持自定义的ttf字体
//        Typeface fontFace = Typeface.createFromAsset(context.getAssets(),
//                "fonts/citizen_card_text.ttf");
//        holder.card_num.setTypeface(fontFace);
        holder.tv_ok_go.setImageResource(R.mipmap.huanying_lingqu);
        holder.card_name.setText(cardInfoList.get(i).getCategory_name());
        holder.bind_time.setText("绑定时间：" + cardInfoList.get(i).getBind_time());
        Picasso.with(context)
                .load(Config.web.picUrl + cardInfoList.get(i).getList_url())
                .error(R.drawable.citizen_card_bg)
                .into(holder.card_pic);
        holder.tv_explain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LifeServireH5Activity.class);
                intent.putExtra("life_url", Config.web.cardUseIllustrate + cardInfoList.get(i).getCategoryid());
                intent.putExtra("isLogin", "N");
                intent.putExtra("serviceName", "卡使用说明");
                context.startActivity(intent);
            }
        });
        holder.card_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showDialog(i);
            }
        });

//        if (cardInfoList.get(i).getStatus() == 1) {
//            //1:正常  2：挂失  3：注销  4：系统锁定
//            holder.lose.setVisibility(View.GONE);
//        } else {
//            holder.lose.setVisibility(View.VISIBLE);
//        }

        return view;
    }

    private void showDialog(final int i) {
//        mNormalDialog = new NormalDialog(context);
        final NormalDialog mNormalDialog = NormalDialog.getInstance(context);
        mNormalDialog.setDesc("确定领卡吗？");
        mNormalDialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.requestReceivedCard(i);
                mNormalDialog.dismiss();
            }
        });
        mNormalDialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNormalDialog.dismiss();
            }
        });


    }

    class ViewHolder {
        TextView card_name;
        TextView card_num;
        ImageView tv_explain;
        ImageView tv_ok_go;
        //        ImageView lose;
        ImageView card_pic;
        ImageView go_card_manage;
        TextView bind_time;
    }
}
