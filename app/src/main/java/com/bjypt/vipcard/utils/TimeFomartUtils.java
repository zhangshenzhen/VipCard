package com.bjypt.vipcard.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFomartUtils {

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
}
