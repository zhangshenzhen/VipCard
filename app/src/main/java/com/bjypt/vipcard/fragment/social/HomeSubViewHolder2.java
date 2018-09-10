package com.bjypt.vipcard.fragment.social;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bjypt.vipcard.R;

/**
 * Created by afon on 2017/2/5.
 */

public class HomeSubViewHolder2 extends RecyclerView.ViewHolder {

    protected Context mContext;

    TextView title;

    public HomeSubViewHolder2(View itemView) {
        super(itemView);

        mContext = itemView.getContext();

        title = (TextView)itemView.findViewById(R.id.title);

    }
}
