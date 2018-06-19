package com.bjypt.vipcard.activity.shangfeng.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bjypt.vipcard.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 预约下单Dialog
 */
public class BookingOrderDialog extends Dialog {

    @BindView(R.id.ibtn_close)
    ImageButton ibtn_close;

    @BindView(R.id.btn_place_an_order)
    Button btn_place_an_order;

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
    private BookingOrderListener listener;

    public BookingOrderDialog(Context context, String money, String discount) {
        super(context, R.style.CommonalityDialogStyle);
        this.mContext = context;
        initView(context, money, discount);
    }

    private void initView(Context context, String money, String discount) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_booking_order, null);

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        view.setMinimumWidth(display.getWidth());
        view.setMinimumHeight(display.getHeight());
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        setContentView(view);
        ButterKnife.bind(this);

        tv_money.setText("金额" + money + "元");
        if(discount != null) {
            tv_discount.setText("折扣" + discount);
        }else{
            linear_discount.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 确认下单
     */
    @OnClick(R.id.btn_place_an_order)
    public void bookingOrder() {
        if(listener != null){
            listener.bookingOrder();
        }

        dismiss();
    }

    @OnClick(R.id.ibtn_close)
    public void dismissDialog() {
        dismiss();
    }

    public void toBookingOrder(BookingOrderListener listener){
        this.listener = listener;
    }

    public interface BookingOrderListener{
        void bookingOrder();
    }



}
