package com.bjypt.vipcard.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FomartToolUtils {

    /*格式化时间
     *
     * */
    public static  String fomartDate(String time){
        long t = Long.parseLong(time);
        Date date = new Date(t);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date)+"";
    }
    public static  String fomartDateSecond(String time){
        long t = Long.parseLong(time);
        Date date = new Date(t);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date)+"";
    }

    /*
     * 格式化*/
    public  static   String fomartMoney( BigDecimal big){
        DecimalFormat df = new DecimalFormat("#.##");
        BigDecimal bd = new BigDecimal(big+"");
        double db = bd.doubleValue();
        return df.format(db)+"元";
    }

    /*
     * 格式化数字*/
    public static   String fomartNum( double num){
        DecimalFormat df = new DecimalFormat("#.##");
        BigDecimal bd = new BigDecimal(num+"");
        double db = bd.doubleValue();
        return df.format(db)+"";
    }
}
