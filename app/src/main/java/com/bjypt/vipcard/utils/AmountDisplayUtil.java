package com.bjypt.vipcard.utils;

import java.math.BigDecimal;

public class AmountDisplayUtil {

    /**
     * 大雨1000 显示中国 万
     * @return
     */
    public static final String displayChineseWan(BigDecimal amount){
        if(amount.compareTo(new BigDecimal(1000))>=0){
           return  amount.divide(new BigDecimal(10000)).stripTrailingZeros().toPlainString()+"万";
        }else{
            return amount.stripTrailingZeros().toPlainString()+"元";
        }
    }
}
