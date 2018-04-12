package com.bjypt.vipcard.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016/6/20.
 */
public final class Imeobserver {


    /* @Override
    //调用
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Imeobserver.observer(this);
    }*/

    public Imeobserver() {
    }

    public static void observer(final Activity activity) {
        if (null == activity) {
            return;
        }
        final View root = activity.getWindow().getDecorView();
        if (root instanceof ViewGroup) {
            final ViewGroup decorView = (ViewGroup) root;
            if (decorView.getChildCount() > 0) {
                final View child = decorView.getChildAt(0);
                decorView.removeAllViews();
                ViewGroup.LayoutParams params = child.getLayoutParams();
                ImeObserverLayout observerLayout = new ImeObserverLayout(activity.getApplicationContext());
                observerLayout.addView(child, params);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                decorView.addView(observerLayout, lp);
            }
        }
    }

    private static class ImeObserverLayout extends FrameLayout{

        private List<EditText> mEditTexts;

        public ImeObserverLayout(Context context) {
            super(context);
        }

        public ImeObserverLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ImeObserverLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public ImeObserverLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            load(this);
        }

        @Override
        protected void onDetachedFromWindow() {
            clearEditText();
            super.onDetachedFromWindow();
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            if(ev.getAction()==MotionEvent.ACTION_DOWN&&inThisCircle(ev)){
                hideSoftInput();
            }
            return super.onInterceptTouchEvent(ev);
        }

        public void load(View Child){

            if(mEditTexts==null){
                mEditTexts=new ArrayList<>();
            }

            if(Child instanceof ViewGroup){

                ViewGroup mViewGroup= (ViewGroup) Child;
                int childcount=mViewGroup.getChildCount();
                for(int i=0;i<childcount;i++){

                    View view=mViewGroup.getChildAt(i);
                    load(view);
                }
            }else if(Child instanceof EditText){
                EditText mEditText= (EditText) Child;
                if(!mEditTexts.contains(mEditText)){
                    mEditTexts.add(mEditText);
                }
            }
        }
        private void clearEditText() {
            if (null != mEditTexts) {
                mEditTexts.clear();
                mEditTexts = null;
            }
        }

        private void hideSoftInput() {
            final Context context = getContext().getApplicationContext();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }

        public boolean inThisCircle(MotionEvent ev){

            if(mEditTexts==null||mEditTexts.isEmpty()){
                return false;
            }
            int x=(int)ev.getX();
            int y=(int)ev.getY();

            Rect rect=new Rect();
            for(EditText editText:mEditTexts){
                editText.getGlobalVisibleRect(rect);
                if(rect.contains(x,y)){
                    return false;
                }
            }
            return true;
        }
    }
}