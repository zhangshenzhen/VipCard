package com.bjypt.vipcard.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class AmountDisplayUtil {

    /**
     * 大雨1000 显示中国 万
     * @return
     */
    public static final String displayChineseWan(BigDecimal amount){
        if(amount == null){
            return "0元";
        }
        if(amount.compareTo(new BigDecimal(1000))>=0){
           return  amount.divide(new BigDecimal(10000)).stripTrailingZeros().toPlainString()+"万";
        }else{
            return amount.stripTrailingZeros().toPlainString()+"元";
        }
    }

    public static final String displayChineseWan2(BigDecimal amount){

        if (amount.compareTo(new BigDecimal(0))<=0){
            return 0+"元";
          }
        if(amount.compareTo(new BigDecimal(1000))>=0){
            DecimalFormat df = new DecimalFormat("#.##");
            BigDecimal  bd  = new BigDecimal(amount.divide(new BigDecimal(10000))
                                            .stripTrailingZeros().toPlainString());
            double money = bd.doubleValue();
            return  df.format(money) +"万";
           // return amount.divide(new BigDecimal(10000)).stripTrailingZeros().toPlainString()+"万";
        }else{
            return amount.stripTrailingZeros().toPlainString()+"元";
        }
    }
}
