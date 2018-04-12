package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.DetailsChooseAdater;
import com.bjypt.vipcard.adapter.TaoCanAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.DishesCarBean;
import com.bjypt.vipcard.model.ProductDetailBean;
import com.bjypt.vipcard.model.TaoCanBean;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaoCanActivity extends BaseActivity implements VolleyCallBack<String> {
    private View view_title;
    private View view_footer;
    private View view;
    private RelativeLayout back;//返回
    private RelativeLayout layout_find_other_dian;//查看更多分店
    private LinearLayout layout_at_once_yuding;//立即预订
    private ListView listView_zhushi;//套餐主食内容
    private TextView tv_taocan_title;//title名
    private TextView tv_yuexiao;//月销量
    private TextView tv_dianzan;//点赞百分比
    private TextView tv_taocan_yuanjia;//原价
    private TextView tv_vip_zhekou;//vip会员折扣
    private TextView tv_taocan_other_server;//更多服务
    private TextView tv_ad_once_yuding;//立即预订
    private ImageView iv_taocan_photo;
    private List<TaoCanBean> list_zhushi;//两个数据合并到一个
    private TaoCanAdapter adapter_zhushi;

    private Map<String, String> params = new HashMap<>();
    private String temStr="";//临时参数
    private String imageUrl,merchantName,dishesName,mouthSell,
            dishesGoodsNum,dishesPrice,discount,dishesIntroduce,payment,pkproduct;
    private String rechargeActivity;//是否是充值活动（1：是  0：否）
    private int num;
    public static int position;
    private TextView mTaocanName;
    private TextView tv_yuyue_taocan;
    private ImageView quanfan;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_tao_can);
        //  view = View.inflate(this,R.layout.activity_tao_can,null);
        view_title = View.inflate(this, R.layout.layout_taocan_title, null);
        view_footer = View.inflate(this, R.layout.layout_taocan_footer, null);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("imageUrl");//菜品图片
        merchantName = intent.getStringExtra("merchantName");//商家名称
        dishesName = intent.getStringExtra("dishesName");//菜品名称
        mouthSell = intent.getStringExtra("mouthSell");//月售
        dishesGoodsNum = intent.getStringExtra("dishesGoodsNum");//好评量
        dishesPrice = intent.getStringExtra("dishesPrice");//菜品单价
        discount = intent.getStringExtra("discount");//折扣率
        dishesIntroduce = intent.getStringExtra("dishesIntroduce");//是否存在分店
        payment = intent.getStringExtra("payment");//定金
        pkproduct = intent.getStringExtra("pkproduct");//商品主键
        position = intent.getIntExtra("position", 0);
        num = intent.getIntExtra("num", 0);
        rechargeActivity = intent.getStringExtra("rechargeActivity");//是否是充值活动（1：是  0：否）
        Log.e("TYZ","dishesPrice:"+dishesPrice);

       request();
    }
    public void request(){
        if (params!=null&&!params.isEmpty()){
            params.clear();
        }
        params.put("pkproduct",pkproduct);
        Wethod.httpPost(TaoCanActivity.this,1, Config.web.product_details, params, this);
    }

    @Override
    public void initView() {
        listView_zhushi = (ListView) findViewById(R.id.listviw1);
        listView_zhushi.addHeaderView(view_title);
        listView_zhushi.addFooterView(view_footer);
        tv_taocan_title = (TextView) findViewById(R.id.tv_taocan_title);
        tv_yuexiao = (TextView) findViewById(R.id.tv_yuexiao);
        tv_dianzan = (TextView) findViewById(R.id.tv_dianzan);
        tv_taocan_yuanjia = (TextView) findViewById(R.id.tv_taocan_yuanjia);
        tv_vip_zhekou = (TextView) findViewById(R.id.tv_vip_zhekou);
        tv_taocan_other_server = (TextView) findViewById(R.id.tv_taocan_other_server);
        back = (RelativeLayout) findViewById(R.id.layout_taocan_back);
        layout_at_once_yuding = (LinearLayout) findViewById(R.id.layout_at_once_yuding);
        layout_find_other_dian = (RelativeLayout) findViewById(R.id.layout_find_other_dian);
        iv_taocan_photo = (ImageView) findViewById(R.id.iv_taocan_photo);
        mTaocanName = (TextView) findViewById(R.id.taocan_dishes_name);
        tv_ad_once_yuding = (TextView) findViewById(R.id.tv_ad_once_yuding);
        tv_yuyue_taocan = (TextView) findViewById(R.id.tv_yuyue_taocan);
        quanfan = (ImageView) findViewById(R.id.quanfan);


        if(num>0){
            tv_yuyue_taocan.setText("已加入购物车");
        }

    }

    @Override
    public void afterInitView() {
        tv_taocan_title.setText(merchantName);
        iv_taocan_photo.setImageResource(R.mipmap.ad_bg);
        if (!imageUrl.equals("")) {

            if (imageUrl.indexOf("@")==-1){          //没有@符号
                ImageLoader.getInstance().displayImage(Config.web.picUrl + imageUrl,iv_taocan_photo, AppConfig.DEFAULT_IMG_OPTIONS_AD);
            }else {
                ImageLoader.getInstance().displayImage(Config.web.picUrl + imageUrl.substring(0,imageUrl.indexOf("@")),iv_taocan_photo, AppConfig.DEFAULT_IMG_OPTIONS_AD);
            }
        }
        mTaocanName.setText(dishesName);
        tv_yuexiao.setText(mouthSell);
        tv_dianzan.setText(dishesGoodsNum);
        tv_taocan_yuanjia.setText(dishesPrice);
        tv_vip_zhekou.setText(discount);
        tv_ad_once_yuding.setText("确定(定金"+payment+"元)");

        if(rechargeActivity.equals("1")){
            quanfan.setVisibility(View.VISIBLE);
        }else{
            quanfan.setVisibility(View.GONE);
        }


        list_zhushi = new ArrayList<TaoCanBean>();

    }

    @Override
    public void bindListener() {
        back.setOnClickListener(this);
        layout_find_other_dian.setOnClickListener(this);
        layout_at_once_yuding.setOnClickListener(this);
        tv_yuyue_taocan.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            /**
             * back
             */
            case R.id.layout_taocan_back:
                finish();
                break;
            /**
             * 立即预订
             */
            case R.id.layout_at_once_yuding:
//                 SkipIntent(DetailOrderActivity.class);


                if (num == 0) {
                    /******************************************************
                     * 在订餐页面没有选择该菜品的数量，直接点击了该菜品 ，所以当选择了菜品规格后，默认选择了该菜品的数量为1.
                     * 即在购物车中创建了一个新的单品，并且在菜品列表中显示该菜品已经选择了1,该产品不存在规格，所以选择默认
                     *****************************************************/
                    DetailsChooseAdater.numMap.put(position, 1);
                    SubscribeDishesActivity.carList.add(new DishesCarBean(pkproduct,dishesName,Integer.parseInt(dishesPrice),Integer.parseInt(payment),"","","",1,"Y"));
                    DetailsChooseAdater.FLAG_DETAILS_ADAPTER = 2;
                    finish();
                }
                break;
            /**
             * 查看更多店家
             */
            case R.id.layout_find_other_dian:
                break;
            case R.id.tv_yuyue_taocan:
                //加入购物车
                if(tv_yuyue_taocan.getText().toString().contains("已")){
                    Toast.makeText(this, "您已加入购物车,请勿重复添加", Toast.LENGTH_LONG).show();
                }else{
                    SubscribeDishesActivity.carList.add(new DishesCarBean(pkproduct, dishesName, Double.parseDouble(dishesPrice), Double.parseDouble(payment), "", "", "", 1, "Y"));
                    DetailsChooseAdater.numMap.put(position, 1);
                    finish();
                    Toast.makeText(this,"您已加入购物车",Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    @Override
    public void onSuccess(int reqcode, String result) {

        try {
            ProductDetailBean homeTest = getConfiguration().readValue(result.toString(), ProductDetailBean.class);
            for (int i = 0; i < homeTest.getResultData().size(); i++) {

                TaoCanBean taoCanBean=null;
                if(!temStr.equals(homeTest.getResultData().get(i).getProducttype())){
                    temStr=homeTest.getResultData().get(i).getProducttype();

                    taoCanBean=new TaoCanBean(null, null, null);
                    i-=1;//返回
                }else {
                    taoCanBean = new TaoCanBean(homeTest.getResultData().get(i).getTcdetailname(),
                            homeTest.getResultData().get(i).getTcdetailnum(),
                            homeTest.getResultData().get(i).getTcdetailprice());
                }
                list_zhushi.add(taoCanBean);
            }
            adapter_zhushi = new TaoCanAdapter(this, list_zhushi);

            listView_zhushi.setAdapter(adapter_zhushi);

        } catch (IOException e) {

            Log.i("aaa", "" + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {
        if(reqcode == 1){
            Wethod.ToFailMsg(this,result);
        }

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

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

    @Override
    public void isConntectedAndRefreshData() {
        request();
    }
}
