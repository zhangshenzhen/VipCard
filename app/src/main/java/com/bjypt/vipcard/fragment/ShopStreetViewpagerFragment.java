package com.bjypt.vipcard.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.NewSubscribeDishesActivity;
import com.bjypt.vipcard.adapter.ShopStreetGridviewAdapter;
import com.bjypt.vipcard.adapter.cityconnect.ShopStreetRecycleAdapter;
import com.bjypt.vipcard.base.BaseFragment;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.fragment.crowdfunding.decoration.GridSpacingItemDecoration;
import com.bjypt.vipcard.fragment.crowdfunding.decoration.HorizontalSpaceItemDecoration;
import com.bjypt.vipcard.model.NewHomeMerchantListBean;
import com.bjypt.vipcard.model.NewMerchantListBean;
import com.bjypt.vipcard.model.ShopStreetBean;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.NewsViewpager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ShopStreetViewpagerFragment extends BaseFragment implements VolleyCallBack<String> {

    private static final String TAG ="StreetViewpagerFragment" ;
    private View view;
    private RecyclerView my_gridview_shopstreet;
    final int TWO_LEVEL_REQUEST = 110;
    final int MERCHANT_LIST_REQUEST = 111;

    private List<ShopStreetBean.ResultDataBean> listall = new ArrayList<>();
    private List<String> listName = new ArrayList<>();
    private ShopStreetGridviewAdapter myAdapter;
    private int currentIndex;
    private NewsViewpager viewpager;
    private Map<String, String> maps = new HashMap<>();

    private String longitude;//获取经度
    private String latitude;//获取纬度

    private int pageLength = 10;
    private NewHomeMerchantListBean homeMerchantListBean;
    private List<NewMerchantListBean> listmerchant = new ArrayList<NewMerchantListBean>();
    private LinearLayout ll_merchantlist;
    private String mtlevel;
    private String pkmertype;

    LayoutInflater layoutInflater = null;
    private boolean isClick = false;

    private LinearLayout linear_footer;
    private int currentPage;
    private int gridviewPosition = -1;

    private LinearLayout ll_data_null;  //没有数据时的占位图

    final int QUERY_EXERCISE_MORE = 0x0101;
    final int QUERY_EXERCISE_REFERSH = 0x0110;
    private ShopStreetRecycleAdapter adapter;
    private int dposition = -1;


    public static ShopStreetViewpagerFragment getInstance(String mtlevel, String pkmertype, NewsViewpager viewpager, int index) {
        ShopStreetViewpagerFragment fragment = new ShopStreetViewpagerFragment();
        fragment.mtlevel = mtlevel;
        fragment.pkmertype = pkmertype;
        fragment.currentIndex = index;
        fragment.viewpager = viewpager;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop_street_viewpager,  null);
        if (viewpager != null) {
            viewpager.setObjectForPosition(view, currentIndex);
        }
        return view;
    }

    @Override
    public void beforeInitView() {
        latitude = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.CURRENT_LATU);
        longitude = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.CURRENT_LNGU);

        Logger.e("经度 >:"+longitude+" ; 纬度> :"+latitude);

        Log.e("lat", "beforeInitView...>: " + SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.CURRENT_LATU));
        requestTwoLevel();
        requestMerchantList(mtlevel, pkmertype, QUERY_EXERCISE_REFERSH);
    }

    @Override
    public void initView() {
        layoutInflater = LayoutInflater.from(getActivity());
      //  my_gridview_shopstreet = (RecyclerView) view.findViewById(R.id.my_gridview_shopstreet);
        ll_merchantlist = (LinearLayout) view.findViewById(R.id.ll_merchantlist);
        linear_footer = (LinearLayout) view.findViewById(R.id.linear_footer);
        ll_data_null = (LinearLayout) view.findViewById(R.id.ll_data_null);
        my_gridview_shopstreet = view.findViewById(R.id.my_gridview_shopstreet);
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_news_footer_view, null);
        linear_footer.addView(footerView);
    }

    @Override
    public void afterInitView() {
       /* myAdapter = new ShopStreetGridviewAdapter(getActivity(), listName);
        my_gridview_shopstreet.setAdapter(myAdapter);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) my_gridview_shopstreet.getLayoutParams();
        WindowManager wm = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        layoutParams.setMargins(- width*3/4, 0, 0, 0);*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        my_gridview_shopstreet.setLayoutManager(layoutManager);//设置布局管理器
        //设置间距
        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(1, DensityUtil.dip2px(getContext(), 5));
        HorizontalSpaceItemDecoration horizontalSpaceItemDecoration = new HorizontalSpaceItemDecoration(DensityUtil.dip2px(getContext(), 10), DensityUtil.dip2px(getContext(), 0));
        my_gridview_shopstreet.removeItemDecoration(horizontalSpaceItemDecoration);
        my_gridview_shopstreet.addItemDecoration(gridSpacingItemDecoration);


        adapter = new ShopStreetRecycleAdapter(getActivity(),listName,handler);
        my_gridview_shopstreet.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
             dposition = msg.what;
             isClick = true;
             if (dposition != -1){
             requestMerchantList(listall.get(dposition).getMtlevel() + "", listall.get(dposition).getPkmertype(), QUERY_EXERCISE_REFERSH);
             }
        }
    };

    @Override
    public void bindListener() {
        //点击按钮
     /*   my_gridview_shopstreet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                isClick = true;
                gridviewPosition = position;   //记录GridView选中的下标
                myAdapter.setSelection(position);
                myAdapter.notifyDataSetChanged();
                requestMerchantList(listall.get(position).getMtlevel() + "", listall.get(position).getPkmertype(), QUERY_EXERCISE_REFERSH);
            }
        });*/

        linear_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (gridviewPosition != -1) {
                    requestMerchantList(listall.get(gridviewPosition).getMtlevel() + "", listall.get(gridviewPosition).getPkmertype(), QUERY_EXERCISE_MORE);
                } else {
                    requestMerchantList(mtlevel, pkmertype, QUERY_EXERCISE_MORE);
                }*/
                if (dposition != -1) {
                    requestMerchantList(listall.get(dposition).getMtlevel() + "", listall.get(dposition).getPkmertype(), QUERY_EXERCISE_MORE);
                } else {
                    requestMerchantList(mtlevel, pkmertype, QUERY_EXERCISE_MORE);
                }
            }
        });
    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
       Log.i(TAG,"可见了");
        if(adapter !=null){
            adapter.reFresh(listName,handler,-1);
        }
    }

    public void onRefresh() {
        isClick = true;
        //点击tab时GridView恢复初始状态
        gridviewPosition = -1;
      //  myAdapter.setSelection(-1);
      //  myAdapter.notifyDataSetChanged();
        requestTwoLevel();
        requestMerchantList(mtlevel, pkmertype, QUERY_EXERCISE_REFERSH);
    }

    private void notifyDataSetChanged(List<NewMerchantListBean> listmerchant) {
        for (int i = 0; i < listmerchant.size(); i++) {
            ll_merchantlist.addView(getView(listmerchant.get(i)));
        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == TWO_LEVEL_REQUEST) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                ShopStreetBean bean = objectMapper.readValue(result.toString(), ShopStreetBean.class);

                if (listall.size() > 0) {
                    listall.clear();
                }
                listall.addAll(bean.getResultData());

                if (listName.size() > 0) {
                    listName.clear();
                }

                for (int i = 0; i < listall.size(); i++) {
                    listName.add(listall.get(i).getMtname());
                }
                //myAdapter.notifyDataSetChanged();
                adapter.reFresh(listName,handler,-1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == MERCHANT_LIST_REQUEST) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                homeMerchantListBean = objectMapper.readValue(result.toString(), NewHomeMerchantListBean.class);
                Logger.d(homeMerchantListBean);
                if (listmerchant.size()> 0) {
                    listmerchant.clear();
                }

                if (isClick) {
                    ll_merchantlist.removeAllViews();
                    isClick = false;
                }

                if (homeMerchantListBean.getResultData().size() > 0) {
                    for (int i = 0; i < homeMerchantListBean.getResultData().size(); i++) {
                        NewMerchantListBean listBean = new NewMerchantListBean();
                        listBean.setDiscount(homeMerchantListBean.getResultData().get(i).getDiscount());
                        listBean.setDistance(homeMerchantListBean.getResultData().get(i).getDistance());
                        listBean.setLogo(homeMerchantListBean.getResultData().get(i).getLogo());
                        listBean.setMuname(homeMerchantListBean.getResultData().get(i).getMuname());
                        listBean.setPkmuser(homeMerchantListBean.getResultData().get(i).getPkmuser());
                        listBean.setAddress(homeMerchantListBean.getResultData().get(i).getAddress());
                        listBean.setIntegral(homeMerchantListBean.getResultData().get(i).getIntegral());
                        listBean.setMerdesc(homeMerchantListBean.getResultData().get(i).getMerdesc());
                        if (!"".equals(homeMerchantListBean.getResultData().get(i).getLinkage_pkdealer())) {
                            listBean.setLinkage_pkdealer(homeMerchantListBean.getResultData().get(i).getLinkage_pkdealer());
                        }
                        if (!"".equals(homeMerchantListBean.getResultData().get(i).getVirtualActivity())) {
                            listBean.setVirtualActivity(homeMerchantListBean.getResultData().get(i).getVirtualActivity());
                        }
                        if (!"".equals(homeMerchantListBean.getResultData().get(i).getFirstConsume())) {
                            listBean.setFirstConsume(homeMerchantListBean.getResultData().get(i).getFirstConsume());
                        }
                        if (!"".equals(homeMerchantListBean.getResultData().get(i).getConsumeReduction())) {
                            listBean.setConsumeReduction(homeMerchantListBean.getResultData().get(i).getConsumeReduction());
                        }
                        listmerchant.add(listBean);
                    }
                    notifyDataSetChanged(listmerchant);
                }

                if (currentPage == 0) {
                    if (listmerchant.size() == 0) {
                        ll_data_null.setVisibility(View.VISIBLE);
                    } else {
                        ll_data_null.setVisibility(View.GONE);
                    }
                }

                if (listmerchant.size() < 10) {
                    linear_footer.findViewById(R.id.news_more).setVisibility(View.GONE);
                } else {
                    linear_footer.findViewById(R.id.news_more).setVisibility(View.VISIBLE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    private void requestTwoLevel() {
        //获取二级列表的请求
        Map<String, String> maps = new HashMap<>();
        maps.put("mtlevel", "2");
        maps.put("type", "0");
        maps.put("cityCode", Config.web.cityCode);
        maps.put("parentpk", pkmertype);
        Wethod.httpPost(getActivity(), TWO_LEVEL_REQUEST, Config.web.merchantcatetory, maps, this, View.GONE);
    }

    //点击商家列表数据
    private void requestMerchantList(String mtlevel, String pkmertype, int type) {
        if (maps != null) {
            maps.clear();
        }
        if (type == QUERY_EXERCISE_MORE) {
            currentPage += 10;
//            currentPage = ll_merchantlist.getChildCount();
        } else {
            currentPage = 0;
        }
        maps.put("cityCode", Config.web.cityCode);
        maps.put("lng", longitude);
        maps.put("lat", latitude);
        maps.put("begin", currentPage + "");
        maps.put("pageLength", pageLength + "");
        maps.put("mtlevel", mtlevel);
        maps.put("pkmertype", pkmertype);
        Wethod.httpPost(getActivity(), MERCHANT_LIST_REQUEST, Config.web.new_home_merchant_listview, maps, this, View.GONE);
    }

    private View getView(final NewMerchantListBean resultDataBean) {

        ViewHolder viewHolder = null;

        View convertView = layoutInflater.inflate(R.layout.item_mercahntlist, null);
        viewHolder = new ViewHolder();
        viewHolder.photo_merchant_item = (ImageView) convertView.findViewById(R.id.photo_merchant_item);
        viewHolder.name_merchant_item = (TextView) convertView.findViewById(R.id.name_merchant_item);
        viewHolder.address_merchant_item = (TextView) convertView.findViewById(R.id.address_merchant_item);
        viewHolder.distance_merchant_item = (TextView) convertView.findViewById(R.id.distance_merchant_item);
        viewHolder.address_merchant_item = (TextView) convertView.findViewById(R.id.address_merchant_item);
        viewHolder.jian_merchant_item = (TextView) convertView.findViewById(R.id.jian_merchant_item);
        viewHolder.fan_merchant_item = (TextView) convertView.findViewById(R.id.fan_merchant_item);
        viewHolder.linear_fan = (LinearLayout) convertView.findViewById(R.id.linear_fan);
        viewHolder.linear_jian = (LinearLayout) convertView.findViewById(R.id.linear_jian);
        viewHolder.imageView5 = (ImageView) convertView.findViewById(R.id.imageView5);
        convertView.setTag(viewHolder);

        //减字一行，包括两个字段：firstConsume：首单立减 ，  consumeReduction 消费立减
        if ((null == resultDataBean.getFirstConsume() || ("").equals(resultDataBean.getFirstConsume())) && (null == resultDataBean.getConsumeReduction() || ("").equals(resultDataBean.getConsumeReduction()))) {
            viewHolder.linear_jian.setVisibility(View.GONE);
        } else if ((null == resultDataBean.getFirstConsume() || ("").equals(resultDataBean.getFirstConsume())) && (null != resultDataBean.getConsumeReduction() || !("").equals(resultDataBean.getConsumeReduction()))) {
            viewHolder.linear_jian.setVisibility(View.VISIBLE);
            String string = resultDataBean.getConsumeReduction();
            String regEx = "\\d{1,}[.]?\\d{0,}\\%";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(string);
            SpannableString spannableString = new SpannableString(string);
            if (m.find()) {
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#f1706b")),
                        m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            viewHolder.jian_merchant_item.setText(spannableString);
        } else if ((null != resultDataBean.getFirstConsume() || !("").equals(resultDataBean.getFirstConsume())) && (null == resultDataBean.getConsumeReduction() || ("").equals(resultDataBean.getConsumeReduction()))) {
            viewHolder.linear_jian.setVisibility(View.VISIBLE);
            viewHolder.jian_merchant_item.setText(resultDataBean.getFirstConsume());
        } else {
            viewHolder.linear_jian.setVisibility(View.VISIBLE);
            String string = resultDataBean.getFirstConsume() + "," + resultDataBean.getConsumeReduction();
            String regEx = "\\d[.]?\\d{0,}\\%";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(string);
            SpannableString spannableString = new SpannableString(string);
            if (m.find()) {
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#f1706b")),
                        m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            viewHolder.jian_merchant_item.setText(spannableString);
        }

        //消费立返积分
        if (null == resultDataBean.getVirtualActivity() || ("").equals(resultDataBean.getVirtualActivity())) {
            viewHolder.linear_fan.setVisibility(View.GONE);
        } else {
            viewHolder.linear_fan.setVisibility(View.VISIBLE);
            viewHolder.fan_merchant_item.setText(resultDataBean.getVirtualActivity());
        }

        if ("".equals(resultDataBean.getLogo())) {
            viewHolder.photo_merchant_item.setImageResource(R.drawable.default_dianpu);
        } else {
            ImageLoader.getInstance().displayImage(Config.web.picUrl + resultDataBean.getLogo(), viewHolder.photo_merchant_item, AppConfig.DEFAULT_IMG_MERCHANT_LIST_BG);
        }

        viewHolder.distance_merchant_item.setText(resultDataBean.getDistance());
        viewHolder.name_merchant_item.setText(resultDataBean.getMuname());
        viewHolder.address_merchant_item.setText(resultDataBean.getAddress());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewSubscribeDishesActivity.class);
                intent.putExtra("distance", resultDataBean.getDistance());
                intent.putExtra("pkmuser", resultDataBean.getPkmuser());
                startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView photo_merchant_item;      //店铺图片
        TextView name_merchant_item;        //店铺名字
        TextView address_merchant_item;    //地址
        TextView distance_merchant_item;   //距离
        TextView jian_merchant_item;         //减
        LinearLayout linear_jian;             //减
        TextView fan_merchant_item;          //返
        LinearLayout linear_fan;              //返
        ImageView imageView5;
    }
}

