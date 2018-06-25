package com.bjypt.vipcard.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.HomeTypeBean;
import com.bjypt.vipcard.model.HomeTypeBeanList;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/11/17.
 */

public class ViewPagerGridView {
    private ViewPager viewpager_gridview;
    List<HomeTypeBean> list;//HomeTypeBeangen根据项目的需要可自己写
    List<HomeTypeBeanList> listbeans;//HomeTypeBeanList根据项目的需要可自己写
    private LinearLayout layout;
    private Context context;
    private int currentitem=0;
    private int size  = 0;

    /**
     *
     * @param context
     * @param viewpager_gridview  布局文件中的viewpager
     * @param layout  小圆点所在的layout
     * @param list  所有的item集合
     * @param size  每页显示多少个（一般10个）
     */
    public ViewPagerGridView(Context context, ViewPager viewpager_gridview, LinearLayout layout, List<HomeTypeBean> list , int size) {
       this.context = context;
        this.viewpager_gridview = viewpager_gridview;
        this.list = list;
        this.layout = layout;
        this.size = size;
        initView();
        initData();
    }
    private void initView() {
        listbeans = new ArrayList<>();

    }
    private void initData() {
        fengzhuang(list,size);
        setRoundviewColor();
        viewpager_gridview.setAdapter(new MyPagerAdpater(listbeans,context));
        viewpager_gridview.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentitem = position;
                setRoundviewColor();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     *
     * @param list 所有的item的集合
     * @param size 每页显示多少个item
     */
    public void fengzhuang(List<HomeTypeBean> list, int size){
        int n = list.size()%size;
        int num;
        if (n!=0){
            num = (int) list.size()/size+1;
        }else {
            num = (int) list.size()/size;
        }
        for (int i = 1;i<=num;i++){
            List<HomeTypeBean> stringList =new ArrayList();
            for (int j = 0;j<list.size();j++){
                if (j>=(i-1)*size&&j<i*size){
                    stringList.add(list.get(j));
                }
            }
            HomeTypeBeanList bean = new HomeTypeBeanList();
            bean.setTypeBeanList(stringList);
            listbeans.add(bean);
        }
    }



    public View roundview(int height){
        RoundImageView view = new RoundImageView(context);
        view.setImageResource(R.mipmap.app_ic_launcher);
//        View view = new View(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height,height);
        params.setMargins(10, 0, 0, 0);//设置左边margin为10
        view.setLayoutParams(params);
        return view;
    }
    public void setRoundviewColor(){
        layout.removeAllViews();
        for (int i = 0;i<listbeans.size();i++){
            if (i == currentitem){
                layout.addView(roundview(20));
            }else {
                layout.addView(roundview( 10));
            }
        }
    }
    class MyPagerAdpater extends PagerAdapter {
        private List<HomeTypeBeanList> listbeans;
        private Context context;


        public MyPagerAdpater(List<HomeTypeBeanList> listbeans, Context context) {
            this.context = context;
            this.listbeans = listbeans;
        }
        @Override
        public int getCount() {
            return listbeans.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            GridView gridView = new GridView(context);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(viewpager_gridview.getMeasuredWidth(),viewpager_gridview.getMeasuredHeight());
            gridView.setLayoutParams(layoutParams);
            gridView.setNumColumns(size/2);
            gridView.setAdapter(new MyGridViewAdapter(context, listbeans,position));
            container.addView(gridView);
            return gridView;
        }
    }
    private int ceshi = 0;
    class MyGridViewAdapter extends BaseAdapter {
        private Context context;
        private List<HomeTypeBeanList> list;
        private int flag;

        public MyGridViewAdapter(Context context, List<HomeTypeBeanList> list, int flag) {
            this.context = context;
            this.list = list;
            this.flag = flag;
            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
            ImageLoader.getInstance().init(configuration);
        }

        @Override
        public int getCount() {
            return list.get(flag).getTypeBeanList().size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(flag).getTypeBeanList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder viewHolder=null;
            if (convertView==null){
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_viewpager_gridview,parent,false);
                viewHolder.iv_menu = (ImageView) convertView.findViewById(R.id.iv_menu);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_viewpager_gridview);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.textView.setText(list.get(flag).getTypeBeanList().get(position).getMtname());
            //AppConfig.DEFAULT_IMG_MERCHANT_BG 配置iamgeloader的显示参数
            ImageLoader.getInstance().displayImage(Config.web.picUrl+list.get(flag).getTypeBeanList().get(position).getLogourl(), viewHolder.iv_menu, AppConfig.DEFAULT_IMG_MERCHANT_BG);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener!=null) {
                        clickListener.onCridViewClickListener(v, list.get(flag).getTypeBeanList().get(position).getPkmertype(), list.get(flag).getTypeBeanList().get(position).getMtname());
                    }
                }
            });
            return convertView;
        }
    }
    class ViewHolder {
        ImageView iv_menu;//处理圆图
        TextView textView;//显示名字
    }
    public void setOnGridViewClickListener(GridViewClickListener clickListener){//设置事件监听
        this.clickListener = clickListener;
    }
    private GridViewClickListener clickListener;//处理gridview的item点击事件的接口回调
    public interface GridViewClickListener {
        public void onCridViewClickListener(View v,String pkmertype,String name);
    }
}
