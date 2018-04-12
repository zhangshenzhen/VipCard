package com.bjypt.vipcard.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/6/26.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    int mSpace;


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = mSpace;

        outRect.right = mSpace;

        if (parent.getChildAdapterPosition(view) == 0) {

            outRect.left = mSpace+outRect.left;

        }
        if (parent.getChildAdapterPosition(view) == parent.getChildCount()-1) {

            outRect.right = mSpace+outRect.right;

        }

    }

    public SpaceItemDecoration(int space) {

        this.mSpace = space;

    }

}
