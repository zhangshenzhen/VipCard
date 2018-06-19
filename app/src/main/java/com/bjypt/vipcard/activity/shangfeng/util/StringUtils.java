package com.bjypt.vipcard.activity.shangfeng.util;

import android.content.Context;
import android.text.TextPaint;
import android.text.TextUtils;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dell on 2018/5/4.
 */

public class StringUtils {
    /**
     * 在指定位置添加换行符号
     *
     * @param limitSize 指定位置
     */
    public static String addBreakLineAtIndex(int limitSize, String target) {
        if (TextUtils.isEmpty(target)) {
            return target;
        }
        if (target.length() <= limitSize) {
            return target;
        } else {
            String br = "\n";
            StringBuilder sb = new StringBuilder(target);
            double i = target.length() / (limitSize * 1.0);
            int temp = (int) i;
            if (temp == i) {//表示字符串刚好被limit整除,最后一个不加br
                temp--;
            }
            for (int j = 1; j <= temp; j++) {
                sb.insert(j * limitSize + (br.length() * (j - 1)), br);
            }
            return sb.toString();
        }
    }

    public static int stringConvertInt(String intString){
        int data = 0 ;
        try {
            if(intString!=null&&!intString.equals("")){
                data = Integer.parseInt(intString);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return data;
    }
    /**
     * 获取文本长度
     *
     * @param ctx         有效的上下文环境,可以创建textview
     * @param displayText 文本
     * @param textSize    字体大小
     * @return 文本长度
     */
    public static float getStringWidth(Context ctx, String displayText, int textSize) {
        TextView tv = new TextView(ctx);
        tv.setTextSize(textSize);
        tv.setText(displayText);

        TextPaint mTextPaint = tv.getPaint();
        return mTextPaint.measureText(displayText);
    }


    /**
     * 获取textview中文字的长度
     *
     * @param tv 带文字的textview
     * @return 文本长度
     */
    public static float getStringWidth(TextView tv) {
        TextPaint mTextPaint = tv.getPaint();
        return mTextPaint.measureText(tv.getText().toString());
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.equalsIgnoreCase("null") || str.equals("");
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }



    /**
     * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。
     *
     * @param value
     * @return Sting
     */
    public static String formatFloatNumber(double value) {
        if (value != 0.00) {
            DecimalFormat df = new DecimalFormat("########0.00");
            return df.format(value);
        } else {
            return "0.00";
        }

    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 去除html标签
     * @param htmlStr
     * @return
     */
    public static String delHTMLTag(String htmlStr){
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script= Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(htmlStr);
        htmlStr=m_script.replaceAll(""); //过滤script标签

        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(htmlStr);
        htmlStr=m_style.replaceAll(""); //过滤style标签

        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(htmlStr);
        htmlStr=m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }


    /**
     * 验证手机号是否正确
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^(13[0-9]|14[57]|15[0-35-9]|17[6-8]|18[0-9])[0-9]{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

}
