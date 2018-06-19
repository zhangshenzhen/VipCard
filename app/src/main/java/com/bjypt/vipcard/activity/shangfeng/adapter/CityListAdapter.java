package com.bjypt.vipcard.activity.shangfeng.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.common.enums.LocateState;
import com.bjypt.vipcard.activity.shangfeng.data.bean.CityBean;
import com.bjypt.vipcard.activity.shangfeng.util.PinyinUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dell on 2018/3/22.
 */

public class CityListAdapter extends BaseAdapter {

    private static final int VIEW_TYPE_COUNT = 2;
    private LayoutInflater inflater;
    private List<CityBean> cityList;
    private Context context;
    private CityBean cityBean;
    private HashMap<String, Integer> letterIndexes;
    private String[] sections;
    private OnCityClickListener onCityClickListener;
    private int locateState = LocateState.LOCATING;
    private CityBean locationCity;
    ViewHolder viewHolder = null;


    public CityListAdapter(List<CityBean> cityList, Context context) {
        this.cityList = cityList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        if (cityList == null) {
            cityList = new ArrayList<>();
        }
        cityList.add(0, new CityBean("定位中....", "0", null, null, 0.0, 0.0));
        int size = cityList.size();
        letterIndexes = new HashMap<>();
        sections = new String[size];
        for (int index = 0; index < size; index++) {
            //当前城市拼音首字母
            String currentLetter = PinyinUtils.getFirstLetter(cityList.get(index).getPinyin());
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? PinyinUtils.getFirstLetter(cityList.get(index - 1).getPinyin()) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                letterIndexes.put(currentLetter, index);
                sections[index] = currentLetter;
            }
        }
    }

    /**
     * 更新定位状态
     *
     * @return
     */
    public void updateLocateState(int state, CityBean locationCity) {
        this.locateState = state;
        this.locationCity = locationCity;
        notifyDataSetChanged();
    }

    /**
     * 获取字母索引的位置
     *
     * @param list
     * @return
     */
    public int getCityListPosition(String list) {
        Integer integer = letterIndexes.get(list);
        return integer == null ? -1 : integer;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return position < VIEW_TYPE_COUNT - 1 ? position : VIEW_TYPE_COUNT - 1;
    }

    @Override
    public int getCount() {
        return cityList == null ? 0 : cityList.size();
    }

    @Override
    public Object getItem(int position) {
        return cityList == null ? null : cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        int viewType = getItemViewType(position);
        switch (viewType) {
            case 0:     //定位
                view = inflater.inflate(R.layout.select_city_locate, viewGroup, false);
                ViewGroup container = (ViewGroup) view.findViewById(R.id.layout_locate);
                TextView state = (TextView) view.findViewById(R.id.tv_located_city);
                switch (locateState) {
                    case LocateState.LOCATING:
                        state.setText(R.string.locating);
                        break;
                    case LocateState.FAILED:
                        state.setText(R.string.located_failed);
                        break;
                    case LocateState.SUCCESS:
                        state.setText(locationCity.getCityname());
                        break;
                }
                container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (locateState == LocateState.FAILED) {
                            //重新定位
                            if (onCityClickListener != null) {
                                onCityClickListener.onLocateClick();
                            }
                        } else if (locateState == LocateState.SUCCESS) {
                            //返回定位城市
                            if (onCityClickListener != null) {
                                onCityClickListener.onCityClick(locationCity);
                                Logger.d(locationCity);
                            }
                        }
                    }
                });
                break;
            case 1:     //所有
                if (view == null) {
                    view = inflater.inflate(R.layout.select_city_item, viewGroup, false);
                    viewHolder = new ViewHolder();
                    viewHolder.zimu = (TextView) view.findViewById(R.id.zimu);
                    viewHolder.tv_city = (TextView) view.findViewById(R.id.tv_city);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                cityBean = cityList.get(position);
                final String cityName = cityBean.getCityname();
                viewHolder.tv_city.setText(cityName);
                String currentLetter = cityList.get(position).getPinyin();
                String previousLetter = position >= 1 ? cityList.get(position - 1).getPinyin() : "";

                if (!TextUtils.equals(currentLetter, previousLetter)) {
                    viewHolder.zimu.setVisibility(View.VISIBLE);
                    // 截取第一个字母转大写
                    viewHolder.zimu.setText(currentLetter.substring(0, 1).toUpperCase());
                } else {
                    viewHolder.zimu.setVisibility(View.GONE);
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Logger.v(cityList.get(position).getCityname());
                        onCityClickListener.onCityClick(cityList.get(position));
                    }
                });
                break;
        }
        return view;
    }


    class ViewHolder {
        TextView zimu, tv_city;
    }

    public void setOnCityClickListener(OnCityClickListener listener) {
        this.onCityClickListener = listener;
    }

    public interface OnCityClickListener {
        void onCityClick(CityBean cityBean);

        void onLocateClick();
    }
}
