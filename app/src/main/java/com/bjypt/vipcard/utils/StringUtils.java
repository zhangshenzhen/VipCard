package com.bjypt.vipcard.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/18
 * Use by 字符串工具类
 */
public class StringUtils {
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 保留两位小数
     */
    public static String setScale(String str) {

        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(Float.parseFloat(str));
    }

    /**
     * 值作乘法
     * 用于小数间计算
     * subtract=减 add=加 divide=除 multiply=乘
     * */
    public static String doubleAdd(int s1,Double s2) {
        BigDecimal d1 = new BigDecimal(s1);
        BigDecimal d2 = new BigDecimal(s2);

        return setScale(d1.multiply(d2).toString());
    }

    /**
     * 时间格式转换
     * yyyy-MM-dd HH:mm
     */
    public static String setTimeFormat(String str) {

        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return mformat.format(Double.parseDouble(str));
    }

    /**
     * 时间格式转换
     * yyyy-MM-dd HH:mm
     */
    public static String setTimeFormatDay(String str) {

        SimpleDateFormat mformat = new SimpleDateFormat("HH:mm");

        return mformat.format(Double.parseDouble(str));
    }

    /***
     * 判断密码强度
     * 判断是否全为数字
     */
    private static int strengthGrade = 0;

    public static int judgePsdAllNum(String psd) {

        boolean isDigit = false;
        boolean isLetter = false;
        int numStrength = 0;//密码强度
        int conNum = 0;

        if (psd.length() <= 6) {
            numStrength += 1;
        } else if (psd.length() > 6 && psd.length() < 9) {
            numStrength += 2;
        } else if (psd.length() >= 8 && psd.length() < 12) {
            numStrength += 3;
        } else {
            numStrength += 4;
        }

        for (int i = 0; i < psd.length(); i++) {
            if (Character.isDigit(psd.charAt(i))) {
                // 用char包装类中的判断数字的方法判断每一个字符
                if (isDigit) {
                    conNum++;
                } else {
                    Log.i("aaa", "数字长度=" + conNum);
                    if (conNum >= 2) {
                        numStrength += 3;
                    } else {
                        numStrength += 1;
                    }
                    conNum = 0;
                }
                isDigit = true;
                isLetter = false;
            }
            if (Character.isLetter(psd.charAt(i))) {
                //用char包装类中的判断字母的方法判断每一个字符
                if (isLetter) {
                    conNum++;
                } else {
                    Log.i("aaa", "字母长度=" + conNum);
                    if (conNum >= 2) {
                        numStrength += 3;
                    } else {
                        numStrength += 1;
                    }
                    conNum = 0;
                }
                isLetter = true;
                isDigit = false;
            }
        }
        Log.i("aaa", "numStrength=" + numStrength);
        //numStrength==7为一等
        //numStrength==9为二等
        //numStrength==11为三等
        //numStrength==12以上为四等
        if (numStrength <= 4) strengthGrade = 7;
        else if (numStrength > 4 && numStrength <= 8) strengthGrade = 9;
        else if (numStrength > 8 && numStrength <= 12) strengthGrade = 11;
        else strengthGrade=13;
        return strengthGrade;
    }
}
