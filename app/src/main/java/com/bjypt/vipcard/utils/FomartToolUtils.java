package com.bjypt.vipcard.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
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
    public static  String fomartMoney( BigDecimal big){
        if (big==null){
            return 0+"元";
        }
        DecimalFormat df = new DecimalFormat("#.###");
        BigDecimal bd = new BigDecimal(big+"");
        double db = bd.doubleValue();
        return df.format(db)+"元";
    }

    /*
     * 格式化*/
    public static  String fomartMoneyNoSymbol( BigDecimal big){
        if (big==null){
            return "";
        }
        DecimalFormat df = new DecimalFormat("#.###");
        BigDecimal bd = new BigDecimal(big+"");
        double db = bd.doubleValue();
        return df.format(db)+"";
    }

    /*
     * 格式化数字*/
    public static   String fomartNum( String num){
        DecimalFormat df = new DecimalFormat("#.##");
        BigDecimal bd = new BigDecimal(num+"");
        double db = bd.doubleValue();
        return df.format(db)+"";
    }

    /*
     * 格式化数字2*/
    public static   String fomartPercentNum( String num){
        DecimalFormat df = new DecimalFormat("#.##");
        BigDecimal bd = new BigDecimal(num+"");
        double db = bd.doubleValue();
        String percent = df.format(db)+"";
        if (percent.length()>4){
          return percent.substring(0,3)+"..";
        }
        return percent;
    }


    //判断是否超额
   /*
   * int a = bigdemical.compareTo(bigdemical2)
     a = -1,表示bigdemical小于bigdemical2；
      a = 0,表示bigdemical等于bigdemical2；
      a = 1,表示bigdemical大于bigdemical2；*/
    public static boolean isLimitMoney(BigDecimal cfAmount, BigDecimal processAmount){
        if (processAmount.compareTo( cfAmount ) < 0){
          return false;
         }else {
          return true;
        }
    }

    /*
    * 获取当前网络时间*/

    public static void getNetTime() {
        try {
        URL url=new URL("http://www.baidu.com");
        URLConnection conn=url.openConnection();
        conn.connect();
        long dateL=conn.getDate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
