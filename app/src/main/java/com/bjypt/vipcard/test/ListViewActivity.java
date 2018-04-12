package com.bjypt.vipcard.test;

import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;

import java.util.Arrays;
import java.util.LinkedList;

/***
 *
 * pulltoRefreshListview  text
 */
public class ListViewActivity extends BaseActivity{
    private String[] mStrings = { "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler" };
    private LinkedList<String> mListItems;//
    private PullToRefreshListView mPullRefreshListView; //list
    private ArrayAdapter<String> mAdapter;


    @Override
     public void setContentLayout() {
         setContentView(R.layout.activity_list_view);
     }

     @Override
     public void beforeInitView() {

     }

     @Override
     public void initView() {
         mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.listview);

         mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);//设置上下都可以刷新

         /****
          * 单方向的刷新监听
          */
        /* mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
             @Override
             public void onRefresh(PullToRefreshBase<ListView> refreshView) {

             }
         });*/


         // Set a listener to be invoked when the list should be refreshed.
         /****
          * 上拉下拉双向监听事件
          */
         mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
             /***
              * 下拉
              * @param refreshView
              */
             @Override
             public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                 String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                         DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                 // Update the LastUpdatedLabel
                 refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                 // Do work to refresh the list here.
                 new GetDataTask().execute();
             }

             /****
              * 上拉
              * @param refreshView
              */
             @Override
             public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                 String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                         DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                 // Update the LastUpdatedLabel
                 refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                 // Do work to refresh the list here.
                 new GetDataTask().execute();
             }
         });


         mListItems = new LinkedList<String>();
         mListItems.addAll(Arrays.asList(mStrings));

         mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);

         //这两个绑定方法用其一
         // 方法一
//       mPullRefreshListView.setAdapter(mAdapter);
         //方法二
         ListView actualListView = mPullRefreshListView.getRefreshableView();
         actualListView.setAdapter(mAdapter);
     }

    private class GetDataTask extends AsyncTask<Void, Void, String> {

        //后台处理部分
        @Override
        protected String doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            String str="Added after refresh...I add";
            return str;
        }

        //这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中
        //根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值
        @Override
        protected void onPostExecute(String result) {
            //在头部增加新添内容
            mListItems.addFirst(result);

            //通知程序数据集已经改变，如果不做通知，那么将不会刷新mListItems的集合
            mAdapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();

            super.onPostExecute(result);
        }
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
 }
