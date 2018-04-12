package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.model.MenuModel;
import com.bjypt.vipcard.model.ProductListBean;

import org.w3c.dom.Text;

import java.util.List;

public class TotalPriceActivity extends BaseActivity {

    private LinearLayout back;     //返回键
    private TextView totalPriceTitle;   //标题title
    private TextView menuPrice;   //总价
    private String price;
    private List<MenuModel> menuModelList;
    private String title;
    private LinearLayout parent;
    private TextView menuListPrice;
    private TextView menuListNum;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_total_price);
        initView();
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        price = intent.getStringExtra("totalPrice");
        menuModelList = (List<MenuModel>) intent.getSerializableExtra("menuModelList");
    }

    @Override
    public void initView() {
        back = (LinearLayout) findViewById(R.id.total_price_back);
        totalPriceTitle = (TextView) findViewById(R.id.total_price_title);
        menuPrice = (TextView) findViewById(R.id.price_menu);
        parent = (LinearLayout) findViewById(R.id.menu_list);
    }

    @Override
    public void afterInitView() {
        menuPrice.setText("¥ "+price);
        totalPriceTitle.setText(title);

        for (int i = 0; i < menuModelList.size(); i++) {

            View view = LayoutInflater.from(this).inflate(R.layout.item_total_price, null);
            TextView menuItemTitle = (TextView) view.findViewById(R.id.menu_type);
            LinearLayout childsView = (LinearLayout)view.findViewById(R.id.childs);
            menuItemTitle.setText(menuModelList.get(i).getTypename());
            parent.addView(view);

            for (int j = 0; j < menuModelList.get(i).getMenuProductions().size(); j++) {
                View view1 = LayoutInflater.from(this).inflate(R.layout.item_menu_all, null);
                TextView menuListName = (TextView) view1.findViewById(R.id.menu_list_name);
                menuListNum = (TextView) view1.findViewById(R.id.menu_list_num);
                menuListPrice = (TextView) view1.findViewById(R.id.menu_list_price);
                menuListName.setText(menuModelList.get(i).getMenuProductions().get(j).getProductName());
                menuListPrice.setText("¥ "+menuModelList.get(i).getMenuProductions().get(j).getProductPrice());
                menuListNum.setText("x"+menuModelList.get(i).getMenuProductions().get(j).getNumber()+"");
                childsView.addView(view1);
            }
        }
    }

    @Override
    public void bindListener() {
        back.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.total_price_back:
                finish();
                break;
        }
    }

}
