package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.AppCategoryBean;
import com.bjypt.vipcard.model.HomeASCategoryBean;
import com.bjypt.vipcard.model.HomeASCategoryBeanList;
import com.bjypt.vipcard.view.ViewPagerCYGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */

public class InnGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<AppCategoryBean> list;


    public InnGridViewAdapter(Context context, List<AppCategoryBean> list) {
        this.context = context;
        this.list = list;
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(configuration);
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_gridview_item_cy,parent,false);
            viewHolder.roundImageView = (ImageView) convertView.findViewById(R.id.iv_cy_gradview_item);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name_cy_gradview_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_name.setText(list.get(position).getApp_name());

        //AppConfig.DEFAULT_IMG_MERCHANT_BG 配置iamgeloader的显示参数
        ImageLoader.getInstance().displayImage(list.get(position).getApp_icon(), viewHolder.roundImageView, AppConfig.DEFAULT_IMG_MERCHANT_BG);

        return convertView;
    }
class ViewHolder {
    ImageView roundImageView;//处理圆图
    TextView tv_name;//显示名字

}
}
