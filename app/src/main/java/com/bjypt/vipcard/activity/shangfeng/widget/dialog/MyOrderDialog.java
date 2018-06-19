package com.bjypt.vipcard.activity.shangfeng.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.data.bean.OrderBean;
import com.bjypt.vipcard.activity.shangfeng.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单提示Dialog
 */
public class MyOrderDialog extends Dialog {

    @BindView(R.id.ibtn_close)
    ImageButton ibtn_close;
    /**
     * 商家名称
     */
    @BindView(R.id.tv_merchant_name)
    TextView tv_merchant_name;
    /**
     * 进入商家
     */
    @BindView(R.id.tv_to_merchant)
    TextView tv_to_merchant;
    /**
     * 订单时间
     */
    @BindView(R.id.tv_time)
    TextView tv_time;
    /**
     * 取消订单
     */
    @BindView(R.id.tv_cancel_order)
    TextView tv_cancel_order;
    /**
     * 订单金额
     */
    @BindView(R.id.tv_money)
    TextView tv_money;
    /**
     * 订单折扣
     */
    @BindView(R.id.tv_discount)
    TextView tv_discount;
    @BindView(R.id.linear_discount)
    LinearLayout linear_discount;

    private Context mContext;
    private OrderStateListener listener;
    private OrderBean orderBean;

    public MyOrderDialog(Context context, OrderBean orderBean) {
        super(context, R.style.CommonalityDialogStyle);
        this.mContext = context;
        this.orderBean = orderBean;
        initView(context, orderBean);
    }

    private void initView(Context context, OrderBean orderBean) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_my_order, null);

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        view.setMinimumWidth(display.getWidth());
        view.setMinimumHeight(display.getHeight());
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        setContentView(view);
        ButterKnife.bind(this);

        initData(orderBean);
    }

    private void initData(OrderBean orderBean) {
        tv_merchant_name.setText(orderBean.getMuname());
        tv_time.setText(orderBean.getCreate_at());
        tv_money.setText("金额" + orderBean.getConsume_amount() + "元");
        if(StringUtils.isNotEmpty(orderBean.getDiscount_rate_desc())){
            tv_discount.setText("折扣" + orderBean.getDiscount_rate_desc());
        }else{
            linear_discount.setVisibility(View.INVISIBLE);
        }

    }

    @OnClick({R.id.tv_to_merchant, R.id.tv_cancel_order})
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_to_merchant: // 进入商家
                if (listener != null) {
                    listener.toMerchant(orderBean);
                }
                dismiss();

                break;
            case R.id.tv_cancel_order: // 取消订单
                if (listener != null) {
                    listener.cancleOrder(orderBean);
                }

                dismiss();
                break;
        }
    }

    @OnClick(R.id.ibtn_close)
    public void dismissDialog() {
        dismiss();
    }

    public void setOrderStateListener(OrderStateListener orderStateListener) {
        this.listener = orderStateListener;
    }

    public interface OrderStateListener {
        void cancleOrder(OrderBean orderBean);

        void toMerchant(OrderBean orderBean);
    }

}
