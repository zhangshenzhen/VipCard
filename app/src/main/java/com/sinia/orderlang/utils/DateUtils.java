package com.sinia.orderlang.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class DateUtils {
	public static String formateTime(String time) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 下午HH:mm:ss");
		try {
			Date date = sdf2.parse(time);
			String t = sdf1.format(date);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	public static String getCurrentDateTime() {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		String now = sdf1.format(new Date());
		return now;
	}

	public static String formateDate(String time) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
		String time2 = time.substring(0, 11);
		try {
			Date date = sdf2.parse(time2);
			String t = sdf1.format(date);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	public static boolean isChooseTimeSmallThanCur(String startTime,
			String curTime) {
		DateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d1 = sdf1.parse(startTime);
			Date d2 = sdf2.parse(curTime);
			long t = d1.getTime() - d2.getTime();
			if (t <= 10 * 60 * 1000) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
