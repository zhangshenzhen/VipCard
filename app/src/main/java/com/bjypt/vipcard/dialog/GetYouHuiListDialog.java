package com.bjypt.vipcard.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.cityconnect.YouHuiDialogAdapter;
import com.bjypt.vipcard.bean.YouHuiQuanBean;
import com.bjypt.vipcard.fragment.crowdfunding.decoration.GridSpacingItemDecoration;
import com.bjypt.vipcard.fragment.crowdfunding.decoration.HorizontalSpaceItemDecoration;
import com.bjypt.vipcard.utils.DensityUtil;
import com.sinia.orderlang.utils.AppInfoUtil;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;


public class GetYouHuiListDialog {
    private ScheduledThreadPoolExecutor executor;
    private ProgressBar pb_progess;
    private Context context;
    private String url;
    private Dialog dialog;
    int second = 0;
    private TextView tv_pencent;
    private String content;
    private String[] stringArr;
    private RecyclerView youhui_recv;
    private final int width;
    private List<YouHuiQuanBean.YouHuiQuanDataBean> youHuiQuanDataBeanlist;


    public GetYouHuiListDialog(Context context,List<YouHuiQuanBean.YouHuiQuanDataBean> youHuiQuanDataBeanlist) {

         this.context = context;
         this.youHuiQuanDataBeanlist = youHuiQuanDataBeanlist;
         width = AppInfoUtil.getScreenWidth(context);
    }


    public  void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.getyouhui_dialog, null);
        LinearLayout linyout = v.findViewById(R.id.linyout);
        youhui_recv = v.findViewById(R.id.youhui_recv);
        int size = youHuiQuanDataBeanlist.size();//条目个数

        int dianog_contentHeght = size>2? DensityUtil.dip2px(context, 330):DensityUtil.dip2px(context, 250);
       //自适应图标距离顶部的位置
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(DensityUtil.dip2px(context, 400), dianog_contentHeght);
        if(width<1280){
         params2.topMargin = DensityUtil.dip2px(context, 75);
        }else {
         params2.topMargin = DensityUtil.dip2px(context, 80);
        }
        linyout.setLayoutParams(params2);


        ImageView dialog_close = v.findViewById(R.id.dialog_close);
        dialog = builder.create();
        //背景透明
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setContentView(v);
        //dialog设置宽高
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = width - width / 5;//设置宽
        int add_width = size > 2 ? width /4+DensityUtil.dip2px(context, 5):width /15;
        params.height = width +add_width;//dialog_close置底部，这里可以调节高度
       // params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
        //设置间距
        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(1, DensityUtil.dip2px(context, 5));
        HorizontalSpaceItemDecoration horizontalSpaceItemDecoration = new HorizontalSpaceItemDecoration(DensityUtil.dip2px(context, 10), DensityUtil.dip2px(context, 0));
        youhui_recv.removeItemDecoration(horizontalSpaceItemDecoration);
        youhui_recv.addItemDecoration(gridSpacingItemDecoration);


        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        youhui_recv.setLayoutManager(layoutManager);//设置布局管理器

        YouHuiDialogAdapter adapter = new YouHuiDialogAdapter(context,youHuiQuanDataBeanlist,dialog);
        youhui_recv.setAdapter(adapter);


        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
               // executor.shutdown();
            }
        });

      }

      public void closeDialog(){
         if(dialog != null && dialog.isShowing()){

             dialog.dismiss();
         }
      }

}