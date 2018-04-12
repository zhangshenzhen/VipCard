package com.bjypt.vipcard.test;

import android.view.View;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;

public class TextLayoutActivity extends BaseActivity {

   /* private View view;
    private Button bt_my_myself;//我的
    private Button bt_my_lingdang;//右上角的铃铛图标
    private RoundImageView roundIV_my_photo;
    private Button bt_my_login;//登录按钮
    private TextView telphone;//电话号码  默认隐藏
    private RelativeLayout my_order;//我的订单
    private RelativeLayout order_to_be_used;//待使用
    private RelativeLayout order_to_be_paid;//待支付
    private RelativeLayout order_to_be_evaluated;//待评价
    private RelativeLayout coupon_button;//优惠券
    private RelativeLayout hongbao;//红包
    private RelativeLayout integral_button;//积分
    private RelativeLayout my_zichan;//我的资产
    private RelativeLayout vip_center;//会员中心
    private RelativeLayout zd_detail;//账单明细
    private String imageUrl;//图片url
    public FragmentManager manager ;
    public SelectHandPopupWindow menuWindow;*/
     @Override
     public void setContentLayout() {
         setContentView(R.layout.fra_my);
     }

     @Override
     public void beforeInitView() {

     }

     @Override
     public void initView() {
        /* bt_my_myself = (Button) view.findViewById(R.id.bt_my_myself);
         bt_my_lingdang = (Button) view.findViewById(R.id.bt_my_lingdang);
         bt_my_login = (Button) view.findViewById(R.id.bt_my_login);
         roundIV_my_photo = (RoundImageView) view.findViewById(R.id.roundIV_my_photo);
         telphone = (TextView) view.findViewById(R.id.telphone);
         my_order = (RelativeLayout) view.findViewById(R.id.my_order);
         order_to_be_used = (RelativeLayout) view.findViewById(R.id.order_to_be_used);
         order_to_be_evaluated = (RelativeLayout) view.findViewById(R.id.order_to_be_evaluated);
         order_to_be_paid = (RelativeLayout) view.findViewById(R.id.order_to_be_paid);
         coupon_button = (RelativeLayout) view.findViewById(R.id.coupon_button);
         hongbao = (RelativeLayout) view.findViewById(R.id.hongbao);
         integral_button = (RelativeLayout) view.findViewById(R.id.integral_button);
         my_zichan = (RelativeLayout) view.findViewById(R.id.my_zichan);
         vip_center = (RelativeLayout) view.findViewById(R.id.vip_center);
         zd_detail = (RelativeLayout) view.findViewById(R.id.zd_detail);*/
     }

     @Override
     public void afterInitView() {

     }

     @Override
     public void bindListener() {
       /*  bt_my_myself.setOnClickListener(this);
         bt_my_lingdang.setOnClickListener(this);
         bt_my_login.setOnClickListener(this);
         roundIV_my_photo.setOnClickListener(this);
         my_order.setOnClickListener(this);
         order_to_be_used.setOnClickListener(this);
         order_to_be_evaluated.setOnClickListener(this);
         order_to_be_paid.setOnClickListener(this);
         coupon_button.setOnClickListener(this);
         hongbao.setOnClickListener(this);
         integral_button.setOnClickListener(this);
         my_zichan.setOnClickListener(this);
         vip_center.setOnClickListener(this);
         zd_detail.setOnClickListener(this);*//*  bt_my_myself.setOnClickListener(this);
         bt_my_lingdang.setOnClickListener(this);
         bt_my_login.setOnClickListener(this);
         roundIV_my_photo.setOnClickListener(this);
         my_order.setOnClickListener(this);
         order_to_be_used.setOnClickListener(this);
         order_to_be_evaluated.setOnClickListener(this);
         order_to_be_paid.setOnClickListener(this);
         coupon_button.setOnClickListener(this);
         hongbao.setOnClickListener(this);
         integral_button.setOnClickListener(this);
         my_zichan.setOnClickListener(this);
         vip_center.setOnClickListener(this);
         zd_detail.setOnClickListener(this);*/
     }

     @Override
     public void onClickEvent(View v) {
         switch (v.getId()){
           /*  *//****
              * 我的点击事件
              *//*
             case R.id.bt_my_myself :
                 startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                 break;
             *//****
              *右上角铃铛点击事件
              *//*
             case R.id.bt_my_lingdang :
                 //  startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                 break;
             *//****
              *登录点击事件
              *//*
             case R.id.bt_my_login :
                 //  startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                 break;
             *//****
              *头像点击事件
              *//*
             case R.id.roundIV_my_photo :
                 //获取网络图片并保存在手机上
                 imageUrl="";
                 menuWindow = new SelectHandPopupWindow(this,manager,roundIV_my_photo,1);
                 menuWindow.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                 menuWindow.update();
              *//*  Bitmap bitmap =ImageUtils.getBitmapURL(imageUrl);//从网络上获取图片
                ImageUtils.scaleBitmap(ImageUtils.getBitmapURL(imageUrl),168,168);//图片缩放
                ImageUtils.toRoundCorner(ImageUtils.scaleBitmap(ImageUtils.getBitmapURL(imageUrl),168,168),84);//图片剪成圆角*//*

               *//* ImageUtils.savePhotoSDcard("图片地址",
                        ImageUtils.getImageName(),//用当前时间命名图片
                        ImageUtils.toRoundCorner( //将图片圆角处理
                                ImageUtils.scaleBitmap(//图片缩放
                                        ImageUtils.getBitmapURL(imageUrl), 168, 168), 84));//获取网络图片
*//*
                 break;
             *//****
              *我的订单点击事件
              *//*
             case R.id.my_order :
                 //  startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                 break;
             *//****
              *待使用 点击事件
              *//*
             case R.id.order_to_be_used :
                 //  startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                 break;
             *//****
              *待评论点击事件
              *//*
             case R.id.order_to_be_evaluated :
                 //  startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                 break;
             *//****
              *待支付点击事件
              *//*
             case R.id.order_to_be_paid :
                 //  startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                 break;
             *//****
              *优惠券点击事件
              *//*
             case R.id.coupon_button :
                 //  startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                 break;
             *//****
              *红包点击事件
              *//*
             case R.id.hongbao :
                 //  startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                 break;
             *//****
              *积分点击事件
              *//*
             case R.id.integral_button :
                 //  startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                 break;
             *//****
              *我的资产点击事件
              *//*
             case R.id.my_zichan :
                 // startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                 break;
             *//****
              *会员中心点击事件
              *//*
             case R.id.vip_center :
                 //startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                 break;
             *//****
              *账单明细点击事件
              *//*
             case R.id.zd_detail :
                 // startActivity(new Intent(MyApplication.getInstance(), LangMainActivity.class));
                 break;
*/
     }










    }
 }
