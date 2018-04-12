package com.bjypt.vipcard.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.CITYS;
import com.bjypt.vipcard.model.CitysBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 崔大爷 on 2017/12/28.
 * <p>
 * SuperCitySelect
 */

public class BankCitySelectUtil {
    private String provider = "";
    private Activity context;
    private boolean isProvider;
    private List<CITYS> lists = new ArrayList<>();
    private List<CitysBean> list_s = new ArrayList<>();
    private MyPopupWindows myPopupWindows;
    private LinearLayout ll_city_two;
    private TextView tv_city_name1;
    private TextView tv_city_name2;
    private ImageView iv_red_line1;
    private ListView mList_city;
    private String cityName = "北京";
    private boolean flag = false;
    private MyAdapter mAdapter;
    private JSONObject mJsonObj;//把全国的省市区的信息以json的格式保存，解析完成后赋值为null

    public BankCitySelectUtil(Activity context, boolean isProvider, String provider) {
        this.context = context;
        this.isProvider = isProvider;
        this.provider = provider;
        initJsonData();
        initDatas();
        init();
        initView();
    }

    public void init() {
        if (isProvider) {
            for (int i = 0; i < lists.size(); i++) {
                CitysBean citysBean = new CitysBean();
                citysBean.setCity(lists.get(i).getProvince());
                citysBean.setCoder("");
                list_s.add(citysBean);
            }
        } else {
            for (int i = 0; i < lists.size(); i++) {
                if (lists.get(i).getProvince().equals(provider)) {
                    list_s.addAll(lists.get(i).getCitys());
                }

            }
        }


    }

    private void initView() {
        myPopupWindows = new MyPopupWindows(context, R.layout.layout_popup_city1);
        myPopupWindows.setMyCallBack(new MyPopupWindows.MyCallBack() {
            @Override
            public void func(View view) {
                mList_city = (ListView) view.findViewById(R.id.list_city);
                View view_popup_close = view.findViewById(R.id.view_popup_close);
                LinearLayout ll_dismiss = (LinearLayout) view.findViewById(R.id.ll_dismiss);
                ll_city_two = (LinearLayout) view.findViewById(R.id.ll_city_two);
                tv_city_name1 = (TextView) view.findViewById(R.id.tv_city_name1);
                tv_city_name2 = (TextView) view.findViewById(R.id.tv_city_name2);
                iv_red_line1 = (ImageView) view.findViewById(R.id.iv_red_line1);

                // 点击退出PopWindow
                click(view_popup_close, ll_dismiss, tv_city_name1);
                // 设置listView的Adapter
                mAdapter = new MyAdapter(list_s, context);
                mList_city.setAdapter(mAdapter);
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        private List<CitysBean> list;
        private Context context;

        MyAdapter(List<CitysBean> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        public void setData(List<CitysBean> datas) {
            this.list = datas;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LogUtil.debugPrint("BankCity getView" + position);
            MyViewHolder holder = null;
            if (convertView == null) {
                holder = new MyViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_bank_city_select, parent, false);
                holder.tv_city = (TextView) convertView.findViewById(R.id.tv_city);
                convertView.setTag(holder);
            } else {
                holder = (MyViewHolder) convertView.getTag();
            }
            holder.tv_city.setText(list.get(position).getCity());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        if (!flag) {
                            tv_city_name1.setText(list.get(position).getCity());
                            cityName = list.get(position).getCity();
                        } else {
                            tv_city_name2.setText(list.get(position).getCity());
                            listener.click(cityName, list.get(position).getCity());
                            myPopupWindows.dismiss();
                        }
                        for (int i = 0; i < lists.size(); i++) {
                            if (lists.get(i).getProvince().equals(list.get(position).getCity())) {
                                list_s.clear();
                                list_s.addAll(lists.get(i).getCitys());
                                notifyDataSetChanged();
                                iv_red_line1.setVisibility(View.INVISIBLE);
                                ll_city_two.setVisibility(View.VISIBLE);
                                tv_city_name2.setText(list.get(0).getCity());
                                flag = true;
                                break;
                            }
                        }
                    }
                }
            });
            return convertView;
        }

    }

    public void show(View view) {
        myPopupWindows.show(view, 0, 0);
    }

    public void diss() {
        myPopupWindows.dismiss();
    }

    private OnItemClickListener listener;

    public void setOnItemClickListner(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {

        void click(String value, String n);

    }

    /**
     * 点击退出popWindow
     *
     * @param view_popup_close 第一个View
     * @param ll_dismiss       第二个View
     * @param tv_city_name1
     */
    private void click(View view_popup_close, LinearLayout ll_dismiss, TextView tv_city_name1) {
        view_popup_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPopupWindows.dismiss();
            }
        });
        ll_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPopupWindows.dismiss();
            }
        });
        tv_city_name1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_red_line1.setVisibility(View.VISIBLE);
                ll_city_two.setVisibility(View.GONE);
                list_s = new ArrayList<>();
                flag = false;
                isProvider = true;
                init();
                mAdapter.setData(list_s);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 从assert文件夹中读取省市区的json文件，然后转化为json对象
     */
    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = context.getAssets().open("city.json");
            int len = -1;
            byte[] buf = new byte[is.available()];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "gb2312"));
            }
            is.close();
            mJsonObj = new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析整个Json对象，完成后释放Json对象的内存
     */
    private void initDatas() {
        try {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
                String province = jsonP.getString("p");// 省名字
                CITYS citys = new CITYS();
                citys.setCitys(new ArrayList<CitysBean>());
                citys.setProvince(province);
                JSONArray jsonCs = null;
                try {
                    /**
                     * Throws JSONException if the mapping doesn't exist or is
                     * not a JSONArray.
                     */
                    jsonCs = jsonP.getJSONArray("c");
                } catch (Exception e1) {
                    continue;
                }
                for (int j = 0; j < jsonCs.length(); j++) {
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    String city = jsonCity.getString("n");// 市名字
                    CitysBean citysBean = new CitysBean();
                    citysBean.setCity(city);
                    citys.getCitys().add(citysBean);
                }
                lists.add(citys);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
    }
}

class MyViewHolder {
    TextView tv_city;
}
