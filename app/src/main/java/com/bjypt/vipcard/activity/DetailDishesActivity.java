package com.bjypt.vipcard.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.DetailsChooseAdater;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.DetailDeshesResultBean;
import com.bjypt.vipcard.model.DetailDishesBean;
import com.bjypt.vipcard.model.DishesCarBean;
import com.bjypt.vipcard.model.SingleDetailDishesResultBean;
import com.bjypt.vipcard.model.SingleDetailDishesRootBean;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.StringUtils;
import com.bjypt.vipcard.view.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 菜品详情
 */
public class DetailDishesActivity extends BaseActivity implements VolleyCallBack {
    private HorizontalListView grid_view;
    private TextView tv_xiaoliang; //月销量
    private TextView tv_haoping;//好评量
    private TextView tv_price;//价格
    private TextView tv_jianjie;//简介
    private TextView tv_dingjin_bt;//定金bt
    private TextView tv_yuyue;//预约
    private TextView tv_vipprice;//会员价
    private TextView tv_pr;//
    private TextView tv_dishes_name_two;//商品名称
    private TextView tv_dishes_name;//商品名称
    private ImageView iv_dishes_photo;//商家菜品图片
    private LinearLayout layout_back;
    private RelativeLayout layout_find_other;
    private List<DetailDeshesResultBean> listDetail;
    SingleDetailDishesResultBean resultBean;//无规格的菜品数据
    private boolean isvip = true;
    private boolean isother = true;//判断是否有分店
    private int flag = 1;//大中小份 的标志

    /**
     *
     */
    private String ifvip = "Y";
    /**
     * 上一个界面给的数据
     */
    private String dishesName = "";//菜品名称
    private String dishesUrl = "";//商品图片
    private String dishesGoodsNum = "";//好评量
    private String mouthSell = "";//月售
    private String dishesPrice = "";//菜品单价
    private String dishesIntroduce = "";//是否存在分店
    private String payment = "";//定金
    private String discount = "";//折扣率
    private String preorderStartPrice = "";//最小起送价格*/
    private String pkproduct = "";
    private int position;//当前点击的position
    private int dishesNum;//当前点击的position选中的菜品数量
    //    private String isExist;//判断是否存在规格  Y --存在   N --不存在
    private String isSpecGoods;
    private ImageView quanfan;
    private String rechargeActivity;//是否是充值活动（1：是  0：否）
//    private LoadingFragDialog mFragDialog;

    @Override
    public void setContentLayout() {
        Log.v("TAG", "DetailDishesActivity---------- setContentLayout");
        setContentView(R.layout.activity_detail_dishes);
        Wethod.configImageLoader(this);
        Intent intent = getIntent();
        isSpecGoods = intent.getStringExtra("isSpecGoods");
        pkproduct = intent.getStringExtra("pkproduct");
        dishesName = intent.getStringExtra("dishesName");
        dishesUrl = intent.getStringExtra("dishesUrl");
        mouthSell = intent.getStringExtra("mouthSell");
        dishesGoodsNum = intent.getStringExtra("dishesGoodsNum");
        dishesPrice = intent.getStringExtra("dishesPrice");
        dishesIntroduce = intent.getStringExtra("dishesIntroduce");
        payment = intent.getStringExtra("payment");
        discount = intent.getStringExtra("discount");
        preorderStartPrice = intent.getStringExtra("preorderStartPrice");
        position = intent.getIntExtra("position", 0);
        rechargeActivity = intent.getStringExtra("rechargeActivity");//是否是充值活动（1：是  0：否）

        Log.e("liyunte", dishesUrl);
//        position = intent.getIntExtra("position", 0);
        dishesNum = intent.getIntExtra("num", 0);
//        isExist = intent.getStringExtra("isSpecGoods");

    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_xiaoliang = (TextView) findViewById(R.id.tv_xiaoliang);
        tv_haoping = (TextView) findViewById(R.id.tv_haoping);
        grid_view = (HorizontalListView) findViewById(R.id.grid_view);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_vipprice = (TextView) findViewById(R.id.tv_vipprice);
        tv_pr = (TextView) findViewById(R.id.tv_pr);
        tv_dishes_name = (TextView) findViewById(R.id.tv_dishes_name);
        tv_dishes_name_two = (TextView) findViewById(R.id.tv_dishes_name_two);
        tv_jianjie = (TextView) findViewById(R.id.tv_jianjie);
        tv_dingjin_bt = (TextView) findViewById(R.id.tv_dingjin_bt);
        tv_yuyue = (TextView) findViewById(R.id.tv_yuyue);
        iv_dishes_photo = (ImageView) findViewById(R.id.iv_dishes_photo);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        /*layout_if_vip = (LinearLayout) findViewById(R.id.layout_if_vip);*/
        layout_find_other = (RelativeLayout) findViewById(R.id.layout_find_other);
        quanfan = (ImageView) findViewById(R.id.quanfan_dish);

        if (dishesNum > 0) {
            tv_yuyue.setText("已加入购物车");
        }

//        mFragDialog = new LoadingFragDialog(this,R.anim.loadingpage,R.style.MyDialog);
    }

    @Override
    public void afterInitView() {
        Map<String, String> params = new HashMap<>();
        Log.e("GGG", "-------" + pkproduct);
        params.put("pkproduct", pkproduct);  //正式的时候用
//           params.put("pkproduct", "48e171e86d3d4474b55c780acbf6b7f3");
        if ("Y".equals(isSpecGoods)) {
            Wethod.httpPost(DetailDishesActivity.this, 1, Config.web.product_spec, params, this);
        } else {
            Wethod.httpPost(DetailDishesActivity.this, 2, Config.web.single_product_details, params, this);
        }
//        mFragDialog.showDialog();

        if (rechargeActivity.equals("1")) {
            quanfan.setVisibility(View.VISIBLE);
        } else {
            quanfan.setVisibility(View.GONE);
        }


        tv_haoping.setText(dishesGoodsNum);
        tv_xiaoliang.setText(mouthSell);
        tv_dishes_name_two.setText(dishesName);
        tv_dishes_name.setText(dishesName);
//        tv_yuyue.setText("最小起送价  "+preorderStartPrice);
        tv_dingjin_bt.setText("定金  " + payment);
        if (dishesUrl != null) {
            if (dishesUrl.contains("@")) {
                ImageLoader.getInstance().displayImage(Config.web.picUrl + dishesUrl.substring(0, dishesUrl.indexOf("@")), iv_dishes_photo, AppConfig.DEFAULT_IMG_OPTIONS_AD);
            } else {
                iv_dishes_photo.setImageResource(R.mipmap.ad_bg);
            }

        } else {
            iv_dishes_photo.setImageResource(R.mipmap.ad_bg);
        }


        /*if ("Y".equals(ifvip)){
            layout_if_vip.setVisibility(View.VISIBLE);
        }*/
        if ("Y".equals(dishesIntroduce)) {
            layout_find_other.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void bindListener() {
        tv_dingjin_bt.setOnClickListener(this);
        layout_back.setOnClickListener(this);
        layout_find_other.setOnClickListener(this);

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {


            /**
             * 返回
             */
            case R.id.layout_back:
                finish();
                break;
            /**
             * 查找其它分店
             */
            case R.id.layout_find_other:
                break;
            /**
             * 确定
             */
            case R.id.tv_yuyue:
                //加入购物车操作
                if (tv_yuyue.getText().toString().contains("已")) {
                    Toast.makeText(this, "您已加入购物车,请勿重复添加", Toast.LENGTH_LONG).show();
                } else {
                    SubscribeDishesActivity.carList.add(new DishesCarBean(pkproduct, dishesName, Double.parseDouble(dishesPrice), Double.parseDouble(payment), "", "2", "", 1, "N"));
                    DetailsChooseAdater.numMap.put(position, 1);
                    finish();
                    Toast.makeText(this, "您已加入购物车", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 1) {
            Log.e("GGG", result.toString());
            try {
                DetailDishesBean detailDishesBean = getConfiguration().readValue(result.toString(), DetailDishesBean.class);
                listDetail = detailDishesBean.getResultData();
                myGridViewAdapter adapter = new myGridViewAdapter(this, listDetail);
                grid_view.setAdapter(adapter);
                /**
                 * 以下三个edittext为默认选中第二个菜品规格时显示
                 */

                tv_yuyue.setOnClickListener(this);
                tv_price.setText(listDetail.get(0).getPrice() + "");
                tv_vipprice.setText(listDetail.get(0).getVipprice() + "");
                tv_jianjie.setText(listDetail.get(0).getRemark());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == 2) {
            Log.e("GGGG", "sult---" + result.toString());
            try {
                SingleDetailDishesRootBean rootBean = getConfiguration().readValue(result.toString(), SingleDetailDishesRootBean.class);
                resultBean = rootBean.getResultData();

                tv_yuyue.setOnClickListener(this);
                tv_pr.setVisibility(View.GONE);
                tv_vipprice.setText(resultBean.getVipprice());
                tv_jianjie.setText(resultBean.getRemark());
            } catch (IOException e) {
                Log.e("GGGG", "eee" + e.toString());
                e.printStackTrace();
            }
        }

//        mFragDialog.cancelDialog();

    }

    @Override
    public void onFailed(int reqcode, Object result) {

//        mFragDialog.cancelDialog();
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    public class myGridViewAdapter extends BaseAdapter {
        int flag = 1;//默认选中的菜品规格
        private List<DetailDeshesResultBean> list;
        private Context context;

        public myGridViewAdapter(Context context, List<DetailDeshesResultBean> list) {
            this.context = context;
            this.list = list;

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.layout_detail_deshes_item, null);
                holder.tv_bt = (TextView) convertView.findViewById(R.id.tv_bt);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            holder.tv_bt.setText(list.get(position).getSpecName());
            if (flag == position) {
                holder.tv_bt.setTextColor(Color.WHITE);
                holder.tv_bt.setBackgroundResource(R.mipmap.round_bt_true);
            } else {
                holder.tv_bt.setTextColor(Color.RED);
                holder.tv_bt.setBackgroundResource(R.mipmap.round_bt_false);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = position;
                    notifyDataSetChanged();
                    Log.e("GGGG", "position" + position);
                    Message msg = handler.obtainMessage(1, list.get(position));
                    handler.sendMessage(msg);
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView tv_bt;
        }
    }

    private DetailDeshesResultBean data = null;//选中菜品规格的返回数据
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Log.e("GGG", "handle       ----------------");
                data = (DetailDeshesResultBean) msg.obj;
                tv_price.setText(data.getPrice() + "");
                tv_vipprice.setText(StringUtils.setScale(data.getVipprice() + ""));
                tv_jianjie.setText(data.getRemark());
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }
}
