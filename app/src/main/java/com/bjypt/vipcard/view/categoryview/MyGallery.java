package com.bjypt.vipcard.view.categoryview;

import android.content.Context;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

public class MyGallery extends Gallery {
    private Camera mCamera;
    private int mWidth;
    private int mPaddingLeft;
    private boolean flag;
    private static int firstChildWidth;
    private static int firstChildPaddingLeft;

    /*
     * 构造方法
     */
    public MyGallery(Context context) {
        super(context);
        mCamera = new Camera();
        this.setStaticTransformationsEnabled(true);
    }

    /*
     * 构造方法
     */
    public MyGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCamera = new Camera();
        this.setStaticTransformationsEnabled(true);
    }

    /*
     * 构造方法
     */
    public MyGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mCamera = new Camera();
        this.setStaticTransformationsEnabled(true);
    }


    protected boolean getChildStaticTransformation(View child, Transformation t) {
        t.clear();
        t.setTransformationType(Transformation.TYPE_MATRIX);
        mCamera.save();
        final Matrix imageMatrix = t.getMatrix();
        if (flag) {
            firstChildWidth = getChildAt(0).getWidth();
            firstChildPaddingLeft = getChildAt(0).getPaddingLeft();
            flag = false;
        }
        mCamera.translate(firstChildWidth / 2 + firstChildPaddingLeft + mPaddingLeft - mWidth / 2, 0f, 0f);
        mCamera.getMatrix(imageMatrix);
        mCamera.restore();
        return true;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (!flag) {
            mWidth = w ;
            getLayoutParams().width = mWidth;
            flag = true;
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return super.onFling(e1, e2, 0, velocityY);//方法一：只去除翻页惯性
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // TODO Auto-generated method stub
        return super.onScroll(e1, e2, distanceX, distanceY);
        // TODO Auto-generated method stub
    }


}
