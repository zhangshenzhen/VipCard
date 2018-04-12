package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.PayOneAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.DishesCarBean;
import com.bjypt.vipcard.model.OrderInfoBean;
import com.bjypt.vipcard.model.PackageGoods;
import com.bjypt.vipcard.model.PayOneBean;
import com.bjypt.vipcard.model.PayOrderBean;
import com.bjypt.vipcard.model.SingleGoods;
import com.bjypt.vipcard.model.saveMoneyListGoBean;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.StringUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YuYueActivity extends BaseActivity implements VolleyCallBack<String> {

    //private List<> list;
    //时间标准
    private int timeStandard=36000000;

    private ListView listview_yuyue;
    private TextView tv_title;//title 名称
    private TextView tv_order_price;//订金金额
    private TextView tv_price;//支付金额
    private TextView tv_sheng;//到店可省金额
    private TextView tv_should_yuding;//提示 因该预订
    private TextView tv_qixian;//期限
    private TextView tv_time;//使用时间
    private TextView tv_ruler;//使用规则
    private TextView tv_more_sever;//更多服务
    private TextView tv_merchant_name;//菜名
    private LinearLayout layout_back;//返回键
    private ImageView bt_yuyue;//确认预约按钮
    private View view_one, view_two;
    private List<PayOneBean> list = null;
    private PayOneAdapter adapter = null;

    private List<DishesCarBean> dishesCarList;
    private String saveMoney,merchantName;//省钱  //商家名称
    private PayOrderBean payOrderBean;
    private String pkmuser;
    private String orderId;//商品流水号  例如:20160414120040
    private String primaryk;//商品订单主键  例如:fb3d0e64263745a3a0d699ca5999bdb3
    private String createtime;//订单生成时间
    private String preorderId;

    private EditText use_remark;

    private saveMoneyListGoBean mBean;

    private String tn;
    private Map<String, String> strParams;
    private String totalEarnet;

//    private LoadingFragDialog mFragDialog;



    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_yu_yue);

    }

    @Override
    public void beforeInitView() {
        view_one = View.inflate(this, R.layout.layout_pay_one, null);
        view_two = View.inflate(this, R.layout.layout_yuyue_footer, null);


//        intent.putExtra("save_money",saveMoneyData.getResultData());
//        intent.putExtra("carlist", (Serializable)carList);
//        intent.putExtra("pkmuser",merchantDetailsTest.getResultData().getPkmuser());
//        intent.putExtra("totalPrice",totalMoney+"");
//        intent.putExtra("orderPhone",merchantDetailsTest.getResultData().getPhone());
//        intent.putExtra("totalEarnet",totalPayment+"");

        Intent intent = getIntent();
        mBean= (saveMoneyListGoBean) intent.getSerializableExtra("saveMoenyModle");

        merchantName=intent.getStringExtra("merchantName");//店家名称
        dishesCarList = (List<DishesCarBean>) intent.getSerializableExtra("carlist");//购物车列表
        pkmuser = intent.getStringExtra("pkmuser");//商户主键
        String totalPrice = intent.getStringExtra("totalPrice");//总价格
        String orderPhone = intent.getStringExtra("orderPhone");//商家电话
        totalEarnet = intent.getStringExtra("totalEarnet");//总定金

        Log.i("aaa","接收到的价格==="+totalPrice+">>>>"+totalEarnet);
        ArrayList<SingleGoods> singleGoods = new ArrayList<>();
        List<PackageGoods> packageGoods = new ArrayList<>();

        for (int i = 0; i < dishesCarList.size(); i++) {
            if (!dishesCarList.get(i).getIsPackage().equals("Y")) {
                Log.e("tyz","single");
                SingleGoods single = new SingleGoods();
                single.setProductId(dishesCarList.get(i).getPkId());
                single.setCount(dishesCarList.get(i).getDishesNum() + "");
                single.setSpeciType(dishesCarList.get(i).getSpeciType());
                single.setProductSpeciId(dishesCarList.get(i).getProductSpeciId());
                singleGoods.add(single);
            } else {
                Log.e("cxy","pack"+dishesCarList.get(i).getPkId());
                PackageGoods pack = new PackageGoods();
                pack.setProductId(dishesCarList.get(i).getPkId());
                pack.setCount(dishesCarList.get(i).getDishesNum() + "");
                packageGoods.add(pack);
            }
        }

        payOrderBean = new PayOrderBean();
        payOrderBean.setUserId(getFromSharePreference(Config.userConfig.pkregister));
        payOrderBean.setPkmuser(pkmuser);
        payOrderBean.setTotalPrice(totalPrice);
        payOrderBean.setTotalEarnet(totalEarnet);
        payOrderBean.setOrderPhone(orderPhone);
        payOrderBean.setSingleGoods(singleGoods);
        payOrderBean.setPackageGoods(packageGoods);



    }

    private void addData() {
        list = new ArrayList<PayOneBean>();
        for (int i = 0; i < dishesCarList.size(); i++) {

            list.add(new PayOneBean(dishesCarList.get(i).getDishesName(),
                    dishesCarList.get(i).getDishesNum() + "",
                    dishesCarList.get(i).getDishesPrice() + ""));
        }
    }

    @Override
    public void initView() {
        listview_yuyue = (ListView) findViewById(R.id.lv_yuyue);
        listview_yuyue.addHeaderView(view_one);
        listview_yuyue.addFooterView(view_two);
        tv_merchant_name = (TextView) findViewById(R.id.tv_merchant_name);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        tv_order_price = (TextView) findViewById(R.id.tv_order_price);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_sheng = (TextView) findViewById(R.id.tv_sheng);
        tv_should_yuding = (TextView) findViewById(R.id.tv_should_yuding);
        tv_qixian = (TextView) findViewById(R.id.tv_qixian);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_ruler = (TextView) findViewById(R.id.tv_ruler);
        tv_more_sever = (TextView) findViewById(R.id.tv_more_sever);
        bt_yuyue = (ImageView) findViewById(R.id.bt_yuyue);
        use_remark = (EditText) findViewById(R.id.use_remark);

//        mFragDialog=new LoadingFragDialog(this,R.anim.loadingpage,R.style.MyDialogStyle);
//        mFragDialog.showDialog();
    }


    /**
     *
     * */
    public void setData(){

        tv_merchant_name.setText("" + merchantName);//店名
        tv_sheng.setText("到店最多可省"+mBean.getSaveMoney()+"元");//省钱
        tv_price.setText(payOrderBean.getTotalPrice()+"元");//小计
        tv_order_price.setText(payOrderBean.getTotalEarnet()+"元");//定金


        tv_qixian.setText(StringUtils.setTimeFormat(mBean.getEffectiveTime()+""));
        tv_time.setText(StringUtils.setTimeFormatDay(mBean.getBegintime())+"-"+StringUtils.setTimeFormatDay(mBean.getEndtime()));
        tv_ruler.setText(mBean.getPurchaserules()+"");
        tv_more_sever.setText(mBean.getMoreservices()+"");

    }

    @Override
    public void afterInitView() {

        addData();
        adapter = new PayOneAdapter(YuYueActivity.this, list);
        listview_yuyue.setAdapter(adapter);

        setData();
    }

    @Override
    public void bindListener() {

        layout_back.setOnClickListener(this);
        bt_yuyue.setOnClickListener(this);
//        mFragDialog.cancelDialog();

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.bt_yuyue:

                if(count==0) {
                    bt_yuyue.setEnabled(false);
                    if (SubscribeDishesActivity.carList != null) {
                        SubscribeDishesActivity.carList.clear();
                    }
                    SubscribeDishesActivity.SKIP_ACTIVITY = 1;

                    payOrderBean.setUserRemark(use_remark.getText().toString());

                    String strJson = new Gson().toJson(payOrderBean);
                    strParams = new HashMap<>();
                    strParams.put("strJson", strJson);

                    Wethod.httpPost(YuYueActivity.this,1, Config.web.subscribe_order, strParams, this);
//                    mFragDialog.showDialog();

                    Message msg = mHandler.obtainMessage();
                    msg.obj = count;
                    mHandler.sendMessageDelayed(msg, 1000);
                }else {
                    Log.i("zc","目前不可以点击");
                }
                //获取TN
//                1.userId(String):用户id
//                2.rechargeCode(String)://支付类型  1 支付宝 2 财付通 3 百度钱包 4 网银 5余额支付 6 微信支付 7现金支付  8 其他支付
//                3.queryType（String）:// 1 前台充值 2 后台充值 3 前台消费     4定金支付
//                4.phoneno(String):用户手机号
//                5.timeStamp(String):时间戳
//                6.cpuid(String):设备唯一标示
//                7.balance(String):金额 单位为元
//                8.primaryk(String)

                break;
            case R.id.layout_back:
                finish();
                break;
        }

    }

    @Override
    public void onSuccess(int reqcode, String result) {
        Log.i("aaa", "success");
        if(reqcode == 1){

            try {
                OrderInfoBean orderInfoBean = getConfiguration().readValue(result.toString(), OrderInfoBean.class);
                orderId = orderInfoBean.getResultData().getOrderId();
                primaryk = orderInfoBean.getResultData().getPrimaryk();
                createtime = orderInfoBean.getResultData().getCreatetime();
                preorderId = orderInfoBean.getResultData().getPreorderId();
                if(Double.parseDouble(totalEarnet)==0||totalEarnet.equals("")||totalEarnet==null){
                    //结束当前页面，跳转到订单列表页面
                    Intent intent = new Intent(this,UnPayOrderActivity.class);
                    intent.putExtra("flag",2);
                    startActivity(intent);
                    finish();
                }else{
                        Intent intent = new Intent(YuYueActivity.this,PayOneActivity.class);
                        Log.e("yj","orderId:"+orderId+"-----primaryk:"+primaryk+"-----createtime:"+createtime+"");
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("primaryk",primaryk);
                        intent.putExtra("pkmuser",pkmuser);
                        intent.putExtra("createtime",createtime);
                        intent.putExtra("preorderId",preorderId);
                        intent.putExtra("FLAG","Y");

                        startActivity(intent);
                    finish();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(reqcode == 2){
            //更新支付后订单状态

            Log.i("aaa",""+result.toString());
        }else if(reqcode == 3){


        }
//        mFragDialog.cancelDialog();
    }

    @Override
    public void onFailed(int reqcode, String result) {
//        mFragDialog.cancelDialog();
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
        if(PayOneActivity.FLAG_SKIP_PAY == 2){
            finish();
            PayOneActivity.FLAG_SKIP_PAY = 1;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }

    private int count=0;
    android.os.Handler mHandler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(count>2) {
                Log.i("zc","目前可点击");
                count=0;
                bt_yuyue.setEnabled(true);
            }else {
                count++;
                Message msgs=mHandler.obtainMessage();
                msgs.obj=count;
                mHandler.sendMessageDelayed(msgs, 1000);
            }
        }
    };
}
