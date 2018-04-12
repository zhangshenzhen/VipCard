package com.bjypt.vipcard.test;

import android.view.View;
import android.widget.ListView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/9
 * Use by 测试类
 */
public class TestActivity extends BaseActivity{

    private ListView mTestListView;

    public final static String ITEM_TITLE = "title";
    public final static String ITEM_CAPTION = "caption";


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_test);
    }

    @Override
    public void beforeInitView() {

    }

    public Map<String, ?> createItem(String title, String caption) {
        Map<String, String> item = new HashMap<String, String>();
        item.put(ITEM_TITLE, title);
        item.put(ITEM_CAPTION, caption);
        return item;
    }

    @Override
    public void initView() {

        List<Map<String, ?>> security = new LinkedList<Map<String, ?>>();

    }

    @Override
    public void ClickLeft() {
        finish();
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {

    }

    private void geneItems() {

    }


}
