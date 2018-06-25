package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.CommentData;
import com.bjypt.vipcard.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/21
 * Use by
 */
public class CommentShowAdapter extends MyBaseAdapter<CommentData>{

    private Context context;

    public CommentShowAdapter(Context mContext) {
        super(mContext);
    }


    @Override
    public View initView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.layout_assess_show_item,null);
            holder.name = (TextView)convertView.findViewById(R.id.tv_username);
            holder.star_1 = (ImageView)convertView.findViewById(R.id.star_1);
            holder.star_2 = (ImageView)convertView.findViewById(R.id.star_2);
            holder.star_3 = (ImageView)convertView.findViewById(R.id.star_3);
            holder.star_4 = (ImageView)convertView.findViewById(R.id.star_4);
            holder.star_5 = (ImageView)convertView.findViewById(R.id.star_5);
            holder.time = (TextView)convertView.findViewById(R.id.tv_time_1);
            holder.layout_merchant = (LinearLayout)convertView.findViewById(R.id.layout_merchant);
            holder.content = (TextView)convertView.findViewById(R.id.tv_content);
            holder.merchantcontent = (TextView)convertView.findViewById(R.id.tv_merchantcontent);
            holder.imageView = (RoundImageView)convertView.findViewById(R.id.photo);
            convertView.setTag(holder);
        }

        holder = (ViewHolder)convertView.getTag();
//        if("".equals(list.get(position).getPosition())){
//            holder.imageView.setImageResource(R.drawable.app_ic_launcher);
//        }
//        ImageLoader.getInstance().displayImage(list.get(position).getPosition(), holder.imageView);





//        if (!"".equals(list.get(position).getPosition())){
//            ImageLoader.getInstance(). displayImage(Config.web.picUrl + list.get(position).getPosition(), holder.imageView, AppConfig.DEFAULT_IMG_OPTIONS_PERSON);
//        }

        Log.e("ggggg","&&&&&&&&&&&"+list.get(position).getNickname().length());
        if(list.get(position).getNickname().length()==1){
            Log.e("TYZ","1");
            holder.name.setText(list.get(position).getNickname());
        }else if(list.get(position).getNickname().length()==2){
            Log.e("TYZ","2");
            holder.name.setText(list.get(position).getNickname().substring(0,1)+"*");
        }else if(list.get(position).getNickname().length()==3){
            Log.e("TYZ","3");
            holder.name.setText(list.get(position).getNickname().substring(0,1)+"*"+list.get(position).getNickname().substring(2,3));
        }else{
            Log.e("TYZ","4");
            holder.name.setText(list.get(position).getNickname().substring(0,1)+"****"+list.get(position).getNickname().substring(list.get(position).getNickname().length()-1,list.get(position).getNickname().length()));
        }


        holder.content.setText(list.get(position).getContent());
        holder.time.setText(list.get(position).getReplytime());
        if("".equals(list.get(position).getReplycontent())||list.get(position).getReplytime()==null){
            holder.layout_merchant.setVisibility(View.GONE);
        }
        holder.merchantcontent.setText(list.get(position).getReplycontent());
        if(Integer.parseInt(list.get(position).getScore())<1){
            holder.star_1.setImageResource(R.drawable.stars_true);
            holder.star_2.setImageResource(R.drawable.stars_flase);
            holder.star_3.setImageResource(R.drawable.stars_flase);
            holder.star_4.setImageResource(R.drawable.stars_flase);
            holder.star_5.setImageResource(R.drawable.stars_flase);
        }else if(Integer.parseInt(list.get(position).getScore())<3 &&Integer.parseInt(list.get(position).getScore())>=2){
            holder.star_1.setImageResource(R.drawable.stars_true);
            holder.star_2.setImageResource(R.drawable.stars_true);
            holder.star_3.setImageResource(R.drawable.stars_flase);
            holder.star_4.setImageResource(R.drawable.stars_flase);
            holder.star_5.setImageResource(R.drawable.stars_flase);
        }else if(Integer.parseInt(list.get(position).getScore())<4&&Integer.parseInt(list.get(position).getScore())>=3){
            holder.star_1.setImageResource(R.drawable.stars_true);
            holder.star_2.setImageResource(R.drawable.stars_true);
            holder.star_3.setImageResource(R.drawable.stars_true);
            holder.star_4.setImageResource(R.drawable.stars_flase);
            holder.star_5.setImageResource(R.drawable.stars_flase);
        }else if(Integer.parseInt(list.get(position).getScore())<5&&Integer.parseInt(list.get(position).getScore())>=4){
            holder.star_1.setImageResource(R.drawable.stars_true);
            holder.star_2.setImageResource(R.drawable.stars_true);
            holder.star_3.setImageResource(R.drawable.stars_true);
            holder.star_4.setImageResource(R.drawable.stars_true);
            holder.star_5.setImageResource(R.drawable.stars_flase);
        }else if(Integer.parseInt(list.get(position).getScore())>=5){
            holder.star_1.setImageResource(R.drawable.stars_true);
            holder.star_2.setImageResource(R.drawable.stars_true);
            holder.star_3.setImageResource(R.drawable.stars_true);
            holder.star_4.setImageResource(R.drawable.stars_true);
            holder.star_5.setImageResource(R.drawable.stars_true);
        }

        Log.e("TYZ", "list.get(position).getPosition():" + list.get(position).getPosition());


        holder.imageView.setImageResource(R.drawable.ic_launcher);
        if(!list.get(position).getPosition().equals("")){
            ImageLoader.getInstance(). displayImage(Config.web.picUrl + list.get(position).getPosition(), holder.imageView, AppConfig.DEFAULT_IMG_OPTIONS_PERSON);
        }

        return convertView;
    }

    class ViewHolder{
        TextView name;
        TextView content;
        TextView time;
        TextView merchantcontent;
        ImageView star_1;
        ImageView star_2;
        ImageView star_3;
        ImageView star_4;
        ImageView star_5;
        LinearLayout layout_merchant;
        RoundImageView imageView;

    }
}
