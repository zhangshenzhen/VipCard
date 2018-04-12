package com.bjypt.vipcard.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by 崔龙 on 2018/1/19.
 * <p>
 * 理财利率计算
 */

public class FinancingProfitUtil {
    private static final String PRICE_FRACTION_TWO = "0.00"; // 保留2位小数

    /**
     * rate 年化率
     * dayInt  周期
     * totalMoney 购买金额
     */
    public static BigDecimal getFinanceMoney(BigDecimal rate, Integer dayInt, BigDecimal totalMoney) {

        BigDecimal incomemoney = new BigDecimal("0.0");
        BigDecimal day = new BigDecimal(dayInt);
        incomemoney = rate.multiply(day);
        incomemoney = incomemoney.multiply(totalMoney);
        incomemoney = incomemoney.divide(new BigDecimal("365.0"), 5, BigDecimal.ROUND_HALF_EVEN);
        incomemoney = incomemoney.divide(new BigDecimal("100.0"), 5, BigDecimal.ROUND_HALF_EVEN);
        return incomemoney;
    }

    /**
     * 保留2位小数
     *
     * @param price
     * @return
     */
    public static String getFractionTwo(BigDecimal price) {
        String result = "";
        try {
            DecimalFormat df = new DecimalFormat(PRICE_FRACTION_TWO);
            df.setRoundingMode(RoundingMode.FLOOR);
            result = df.format(price);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
