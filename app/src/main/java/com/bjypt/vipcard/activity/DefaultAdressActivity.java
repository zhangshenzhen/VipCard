package com.bjypt.vipcard.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.AdressAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.AdressBean;
import com.bjypt.vipcard.model.DefaultAdressBean;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.sinia.orderlang.bean.AddressListBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/1
 * Use by 存在地址的时候显示该界面
 */
public class DefaultAdressActivity extends BaseActivity implements VolleyCallBack {
    private ListView mAdressList;
    private List<AdressBean> adressBeansList = new ArrayList<>();
    private AdressAdapter adressAdapter;
    private RelativeLayout mBack;
    private TextView mAddAdress;
    private DefaultAdressBean defaultAdressBean;
    private int deleteNum;
    private static int updateNum;
    private ImageView iv_default_adress_pic;
    private int flag = 0;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_default_adress);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        flag = intent.getIntExtra("FLAG", 0);

    }

    @Override
    protected void onResume() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(DefaultAdressActivity.this, 1, Config.web.default_adress, params, this);
        super.onResume();
        UmengCountContext.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }

    @Override
    public void initView() {
        iv_default_adress_pic = (ImageView) findViewById(R.id.iv_default_adress_pic);
        mAdressList = (ListView) findViewById(R.id.adress_list);
        mBack = (RelativeLayout) findViewById(R.id.default_adress_back);
        mAddAdress = (TextView) findViewById(R.id.add_new_adress);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
        mAddAdress.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.default_adress_back:
                finish();
                break;
            case R.id.add_new_adress:
                startActivity(new Intent(this, AddAdressActivity.class));
//                startActivity(new Intent(this,CommentActivity.class));
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 1) {
            Log.e("liyunte", result.toString());
            try {

                defaultAdressBean = getConfiguration().readValue(result.toString(), DefaultAdressBean.class);

                if (defaultAdressBean.getResultData().size() > 0) {
                    iv_default_adress_pic.setVisibility(View.GONE);
                    adressAdapter = new AdressAdapter(defaultAdressBean.getResultData(), this, mListenter);
                    mAdressList.setAdapter(adressAdapter);
                }
//

                if (flag == 1) {
                    mAdressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            Intent it = getIntent();
                            AddressListBean aBean = new AddressListBean();
                            aBean.setAddress(defaultAdressBean.getResultData().get(position).getReceiptaddress());
                            aBean.setTelephone(defaultAdressBean.getResultData().get(position).getPhoneno());
                            aBean.setName(defaultAdressBean.getResultData().get(position).getRegistername());
                            aBean.setAddressId(defaultAdressBean.getResultData().get(position).getAddressid());
                            it.putExtra("AddressListBean", aBean);
                            setResult(RESULT_OK, it);
                            finish();
                        }
                    });
                }

//
//                mAdressList.setDelButtonClickListener(new QQListView.DelButtonClickListener() {
//                    @Override
//                    public void clickHappend(final int position) {
//
//
//                    }
//                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == 2) {
            defaultAdressBean.getResultData().remove(deleteNum);
            adressAdapter.notifyDataSetChanged();
            Toast.makeText(this, "删除成功！", Toast.LENGTH_SHORT).show();
        } else if (reqcode == 3) {
            for (int i = 0; i < defaultAdressBean.getResultData().size(); i++) {
                defaultAdressBean.getResultData().get(i).setDefaultaddress("0");
            }
            defaultAdressBean.getResultData().get(updateNum).setDefaultaddress("1");
            adressAdapter.notifyDataSetChanged();
            Toast.makeText(this, "设置成功！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if (reqcode == 1) {
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == 2) {
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == 3) {
            Wethod.ToFailMsg(this, result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    private AdressAdapter.MyClickListener mListenter = new AdressAdapter.MyClickListener() {
        @Override
        public void myOnClick(final int position, View v) {
            switch (v.getId()) {
                case R.id.tv_del:

                    new AlertDialog.Builder(DefaultAdressActivity.this).setTitle("提示")
                            .setMessage("您确定删除当前地址吗?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                    if (updateNum != position) {
                                        Map<String, String> deleteParams = new HashMap<String, String>();
                                        deleteParams.put("addressid", defaultAdressBean.getResultData().get(position).getAddressid());
                                        Wethod.httpPost(DefaultAdressActivity.this, 2, Config.web.delete_default_adress, deleteParams, DefaultAdressActivity.this);
                                        deleteNum = position;
                                    } else {
                                        Toast.makeText(DefaultAdressActivity.this, "你不能删除默认的收货地址", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override

                        public void onClick(DialogInterface dialog, int which) {//响应事件

                        }

                    }).show();
                    break;

                case R.id.setting_default_adress:
                    new AlertDialog.Builder(DefaultAdressActivity.this).setTitle("提示")
                            .setMessage("您确定设置当前地址为默认地址吗?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    updateNum = position;
                                    Map<String, String> updateParams = new HashMap<String, String>();
                                    updateParams.put("addressid", defaultAdressBean.getResultData().get(position).getAddressid());
                                    updateParams.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
                                    Wethod.httpPost(DefaultAdressActivity.this, 3, Config.web.update_default_adress, updateParams, DefaultAdressActivity.this);
                                }

                            }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override

                        public void onClick(DialogInterface dialog, int which) {//响应事件

                        }

                    }).show();
                    break;
            }
        }
    };

    /**
     * 获得第一次进来是获取的下标 在不能删除时使用
     */
    public static void returnUpdNum(int updateNums) {
        updateNum = updateNums;
        Log.i("aaa", "updateNum=" + updateNum);
    }

    @Override
    public void isConntectedAndRefreshData() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(DefaultAdressActivity.this, 1, Config.web.default_adress, params, this);
    }
}
