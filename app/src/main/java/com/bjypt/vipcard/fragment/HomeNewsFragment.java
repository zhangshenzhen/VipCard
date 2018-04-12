package com.bjypt.vipcard.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.LifeServireH5Activity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.FuYangNews;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.NewsViewpager;
import com.lidroid.xutils.util.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 崔龙 on 2017/3/10.
 * 首页新闻
 */
@SuppressLint("ValidFragment")
public class HomeNewsFragment extends Fragment implements VolleyCallBack {
    private static final int NEWS_MORE_NEWS = 201776;
    private String mTitle;
    private String catid;
    //    private List<FuYangNews.ResultDataBean> newsList = new ArrayList<>();
    private View view;
    private TextView card_title_tv;
    //    private MyListViewFormScollView lv_news;
    private LinearLayout linear_news_list;
    //    private NewsAdapter newsAdapter;
    FuYangNews fuYangNewsBeans = null;
    final int QUERY_EXERCISE_MORE = 0x0101;
    final int QUERY_EXERCISE_REFERSH = 0x0110;
    private boolean is_refresh = true;
    private int page = 0; //起始条数

    private boolean is_refreshing = false;
    private LinearLayout linear_head;
    private LinearLayout linear_footer;
    View load_head_view;


    private NewsViewpager newsViewpager;
    private int currentIndex;
    LayoutInflater layoutInflater = null;

    public static HomeNewsFragment getInstance(String title, String catid, NewsViewpager newsViewpager, int index) {
        HomeNewsFragment sf = new HomeNewsFragment();
        sf.mTitle = title;
        sf.catid = catid;
        sf.newsViewpager = newsViewpager;
        sf.currentIndex = index;
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fr_home_news, null);
        initView();
        initData();
        Log.i("wanglei", (newsViewpager == null)  + (" " + (view == null) ) +" "  + (currentIndex));
        if(newsViewpager != null)
        newsViewpager.setObjectForPosition(view, currentIndex);
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView() {
//        card_title_tv = (TextView) view.findViewById(R.id.card_title_tv);
        linear_news_list = (LinearLayout) view.findViewById(R.id.linear_news_list);
        linear_head = (LinearLayout) view.findViewById(R.id.linear_head);
        linear_footer = (LinearLayout) view.findViewById(R.id.linear_footer);
        load_head_view = LayoutInflater.from(getActivity()).inflate(R.layout.home_news_list_load_refresh, null);
        layoutInflater = LayoutInflater.from(getActivity());
        isMoreNews(QUERY_EXERCISE_REFERSH);
    }

    public void onRefresh() {
        if (!is_refreshing) {
            is_refreshing = true;
            linear_head.addView(load_head_view);
            isMoreNews(QUERY_EXERCISE_REFERSH);
        }

    }


    /**
     * 初始化数据
     */
    private void initData() {
        Log.i("wanglei", "initData");
//        newsAdapter = new NewsAdapter(getActivity(), newsList);
//        lv_news.setAdapter(newsAdapter);
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_news_footer_view, null);
//        lv_news.setFocusable(false);
        linear_footer.addView(footerView);
//        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (is_refreshing) return;
//                int lastVisiblePosition = lv_news.getLastVisiblePosition();
//                if (lastVisiblePosition == i) {
////                    ToastUtil.showToast(getActivity(), "加载更多");
//                    isMoreNews(QUERY_EXERCISE_MORE);
//                } else {
//                    Intent intent = new Intent(getActivity(), LifeServireH5Activity.class);
//                    intent.putExtra("life_url", newsList.get(i).getMobileurl());//String.format(Config.web.discuz_news_detail,newsList.get(i).getAid())
//                    intent.putExtra("serviceName", "新闻");
//                    intent.putExtra("isLogin", "N");
//                    intent.putExtra("isNews", "Y");
//                    startActivity(intent);
//                }
//            }
//        });
        linear_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMoreNews(QUERY_EXERCISE_MORE);
            }
        });
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        switch (reqcode) {
            case NEWS_MORE_NEWS:
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                onRefreshComplate();
                try {
                    fuYangNewsBeans = objectMapper.readValue(result.toString(), FuYangNews.class);
                    // 判断是下拉刷新还是上拉加载
                    if (is_refresh) { // 下拉刷新
//                        newsList.clear();
                        linear_news_list.removeAllViews();
                    }
                    if (fuYangNewsBeans.getResultData().size() < 20) {
                        linear_footer.findViewById(R.id.news_more).setVisibility(View.GONE);
                    }
//                    Log.i("wanglei", "news result: " + newsList.size());
//                    newsAdapter.notifyDataSetChanged();

                    notifyDataSetChanged(fuYangNewsBeans.getResultData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private void notifyDataSetChanged(List<FuYangNews.ResultDataBean> newsList) {
        for (int i = 0; i < newsList.size(); i++) {
            linear_news_list.addView(getView(newsList.get(i)));
        }
    }


    private View getView(final FuYangNews.ResultDataBean resultDataBean) {
        int type = 0;
        if (resultDataBean.getAttachment() != null) {
            type = resultDataBean.getAttachment().size();
        }
        ItemThreeHolder threeHolder = new ItemThreeHolder();
        View convertView = layoutInflater.inflate(R.layout.item_fuyang_news_1, null);
        threeHolder.news_title0 = (TextView) convertView.findViewById(R.id.news_title0);
        threeHolder.news_type0 = (TextView) convertView.findViewById(R.id.news_type0);
        threeHolder.tv_create_time0 = (TextView) convertView.findViewById(R.id.tv_create_time0);
        threeHolder.tv_viewnum0 = (TextView) convertView.findViewById(R.id.tv_viewnum0);
        threeHolder.tv_commentnum0 = (TextView) convertView.findViewById(R.id.tv_commentnum0);

        threeHolder.news_title1 = (TextView) convertView.findViewById(R.id.news_title1);
        threeHolder.news_type1 = (TextView) convertView.findViewById(R.id.news_type1);
        threeHolder.tv_create_time1 = (TextView) convertView.findViewById(R.id.tv_create_time1);
        threeHolder.tv_viewnum1 = (TextView) convertView.findViewById(R.id.tv_viewnum1);
        threeHolder.tv_commentnum1 = (TextView) convertView.findViewById(R.id.tv_commentnum1);
        threeHolder.home_news_pic_one1 = (ImageView) convertView.findViewById(R.id.home_news_pic_one1);

        threeHolder.news_title2 = (TextView) convertView.findViewById(R.id.news_title2);
        threeHolder.news_type2 = (TextView) convertView.findViewById(R.id.news_type2);
        threeHolder.tv_create_time2 = (TextView) convertView.findViewById(R.id.tv_create_time2);
        threeHolder.tv_viewnum2 = (TextView) convertView.findViewById(R.id.tv_viewnum2);
        threeHolder.tv_commentnum2 = (TextView) convertView.findViewById(R.id.tv_commentnum2);
        threeHolder.home_news_pic_one2 = (ImageView) convertView.findViewById(R.id.home_news_pic_one2);
        threeHolder.home_news_pic_two2 = (ImageView) convertView.findViewById(R.id.home_news_pic_two2);
        threeHolder.home_news_pic_three2 = (ImageView) convertView.findViewById(R.id.home_news_pic_three2);

        threeHolder.linear_news_0 = (LinearLayout) convertView.findViewById(R.id.linear_news_0);
        threeHolder.linear_news_1 = (LinearLayout) convertView.findViewById(R.id.linear_news_1);
        threeHolder.linear_news_2 = (LinearLayout) convertView.findViewById(R.id.linear_news_2);
        convertView.setTag(threeHolder);
        switch (type) {
            case 0:
                threeHolder.linear_news_1.setVisibility(View.GONE);
                threeHolder.linear_news_2.setVisibility(View.GONE);
                threeHolder.linear_news_0.setVisibility(View.VISIBLE);
                threeHolder.news_title0.setText(resultDataBean.getTitle());
                threeHolder.news_type0.setText(resultDataBean.getCatname());
                threeHolder.tv_create_time0.setText(resultDataBean.getCreate_time());
                threeHolder.tv_viewnum0.setText(resultDataBean.getViewnum());
                threeHolder.tv_commentnum0.setText(resultDataBean.getCommentnum());
                break;
            case 1:
            case 2:
                threeHolder.linear_news_1.setVisibility(View.VISIBLE);
                threeHolder.linear_news_0.setVisibility(View.GONE);
                threeHolder.linear_news_2.setVisibility(View.GONE);
                threeHolder.news_title1.setText(resultDataBean.getTitle());
                threeHolder.news_type1.setText(resultDataBean.getCatname());
                threeHolder.tv_create_time1.setText(resultDataBean.getCreate_time());
                threeHolder.tv_viewnum1.setText(resultDataBean.getViewnum());
                threeHolder.tv_commentnum1.setText(resultDataBean.getCommentnum());
                if ("".equals(resultDataBean.getAttachment().get(0).getAttachment())) {
                    threeHolder.home_news_pic_one1.setImageResource(R.mipmap.xinwen_two);
                } else {
                    if (resultDataBean.getAttachment().get(0).getRemote().equals("0")) {
                        ImageLoader.getInstance().displayImage(Config.web.fy_picUrl + resultDataBean.getAttachment().get(0).getAttachment(), threeHolder.home_news_pic_one1, AppConfig.XINWEN_IMG_OPTIONS_THREE);
                    } else {
                        ImageLoader.getInstance().displayImage(resultDataBean.getAttachment().get(0).getAttachment(), threeHolder.home_news_pic_one1, AppConfig.XINWEN_IMG_OPTIONS_THREE);
                    }
                }
                break;
            case 3:
                threeHolder.linear_news_2.setVisibility(View.VISIBLE);
                threeHolder.linear_news_1.setVisibility(View.GONE);
                threeHolder.linear_news_0.setVisibility(View.GONE);
                threeHolder.news_title2.setText(resultDataBean.getTitle());
                threeHolder.news_type2.setText(resultDataBean.getCatname());
                threeHolder.tv_create_time2.setText(resultDataBean.getCreate_time());
                threeHolder.tv_viewnum2.setText(resultDataBean.getViewnum());
                threeHolder.tv_commentnum2.setText(resultDataBean.getCommentnum());
                if ("".equals(resultDataBean.getAttachment().get(0).getAttachment())) {
                    threeHolder.home_news_pic_one2.setImageResource(R.mipmap.xinwen_two);
                } else {
                    if (resultDataBean.getAttachment().get(0).getRemote().equals("0")) {
                        ImageLoader.getInstance().displayImage(Config.web.fy_picUrl + resultDataBean.getAttachment().get(0).getAttachment(), threeHolder.home_news_pic_one2, AppConfig.XINWEN_IMG_OPTIONS_THREE);
                    } else {
                        ImageLoader.getInstance().displayImage(resultDataBean.getAttachment().get(0).getAttachment(), threeHolder.home_news_pic_one2, AppConfig.XINWEN_IMG_OPTIONS_THREE);
                    }
                }
                if (threeHolder.home_news_pic_two2 != null) {
                    if ("".equals(resultDataBean.getAttachment().get(1).getAttachment())) {
                        threeHolder.home_news_pic_two2.setImageResource(R.mipmap.xinwen_two);
                    } else {
                        if (resultDataBean.getAttachment().get(1).getRemote().equals("0")) {
                            ImageLoader.getInstance().displayImage(Config.web.fy_picUrl + resultDataBean.getAttachment().get(1).getAttachment(), threeHolder.home_news_pic_two2, AppConfig.XINWEN_IMG_OPTIONS_THREE);
                        } else {
                            ImageLoader.getInstance().displayImage(resultDataBean.getAttachment().get(1).getAttachment(), threeHolder.home_news_pic_two2, AppConfig.XINWEN_IMG_OPTIONS_THREE);
                        }
                    }
                }
                if (threeHolder.home_news_pic_three2 != null) {
                    if ("".equals(resultDataBean.getAttachment().get(2).getAttachment())) {
                        threeHolder.home_news_pic_three2.setImageResource(R.mipmap.xinwen_three);
                    } else {
                        if (resultDataBean.getAttachment().get(2).getRemote().equals("0")) {
                            ImageLoader.getInstance().displayImage(Config.web.fy_picUrl + resultDataBean.getAttachment().get(2).getAttachment(), threeHolder.home_news_pic_three2, AppConfig.XINWEN_IMG_OPTIONS_THREE);
                        } else {
                            ImageLoader.getInstance().displayImage(resultDataBean.getAttachment().get(2).getAttachment(), threeHolder.home_news_pic_three2, AppConfig.XINWEN_IMG_OPTIONS_THREE);
                        }
                    }
                }
                break;
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LifeServireH5Activity.class);
                intent.putExtra("life_url", resultDataBean.getMobileurl());//String.format(Config.web.discuz_news_detail,newsList.get(i).getAid())
                intent.putExtra("serviceName", "");
                intent.putExtra("isLogin", "N");
                intent.putExtra("isNews", "Y");
                startActivity(intent);
            }
        });
        Log.i("wanglei", resultDataBean.getAid() + " ");
        return convertView;
    }


    class ItemThreeHolder {
        private LinearLayout linear_news_0;
        private LinearLayout linear_news_1;
        private LinearLayout linear_news_2;
        private TextView news_title0;
        private TextView news_type0;
        private TextView tv_create_time0;
        private TextView tv_viewnum0;
        private TextView tv_commentnum0;
        private TextView news_title1;
        private TextView news_type1;
        private TextView tv_create_time1;
        private TextView tv_viewnum1;
        private TextView tv_commentnum1;
        private ImageView home_news_pic_one1;
        private TextView news_title2;
        private TextView news_type2;
        private TextView tv_create_time2;
        private TextView tv_viewnum2;
        private TextView tv_commentnum2;
        private ImageView home_news_pic_one2;
        private ImageView home_news_pic_two2;
        private ImageView home_news_pic_three2;
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    /**
     * 加载更多新闻
     */
    private void isMoreNews(int refresh_type) {
        if(isAdded() && catid != null) {
            if (refresh_type == QUERY_EXERCISE_REFERSH) {
                page = 0;
                is_refresh = true;
            } else {
                page = linear_news_list.getChildCount();
                is_refresh = false;
            }
            Map<String, String> params = new HashMap<>();
            params.put("page", page + "");
            params.put("catid", catid);
            Log.i("wanglei", "NEWS_MORE_NEWS" +  params.get("page") + "  catid=" + params.get("catid"));
            Wethod.httpPost(getActivity(), NEWS_MORE_NEWS, Config.web.fyzx_news_more, params, this, View.GONE);
        }
    }

    private void onRefreshComplate() {
//        new  Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        Animation animation = new TranslateAnimation(0, 0, 0, DensityUtil.dip2px(getContext(), 45) * (-1));
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                linear_head.removeView(load_head_view);
                is_refreshing = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.setAnimation(animation);


//            }
//        }, 2000);
    }

}
