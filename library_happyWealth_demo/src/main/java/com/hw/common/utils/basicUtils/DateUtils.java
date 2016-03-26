package com.hw.common.utils.basicUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	/** 获取两个时间的时间查 如1天2小时30分钟 */
	public static String getDatePoor(Date endDate, Date nowDate) {
		if(endDate == null || nowDate == null){
			return "---";
		}
	    return getDatePoor(nowDate.getTime() - endDate.getTime());
	}
	
	public static String getDatePoor(long diff) {
	    long nd = 1000 * 24 * 60 * 60;
	    long nh = 1000 * 60 * 60;
	    long nm = 1000 * 60;
	    try {
	    	 // long ns = 1000;
		    // 计算差多少天
		    long day = diff / nd;
		    // 计算差多少小时
		    long hour = diff % nd / nh;
		    // 计算差多少分钟
		    long min = diff % nd % nh / nm;
		    // 计算差多少秒//输出结果
		    // long sec = diff % nd % nh % nm / ns;
		    if(day == 0){
		    	return hour + "小时" + min + "分钟";
		    }
		    return day + "天" + hour + "小时" + min + "分钟";
		} catch (Exception e) {
			return "---";
		}
	}
	
	public static String getDate(Date date){
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(date);
		} catch (Exception e) {
			return "";
		}
		
	}

	public static String getNowTime() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		String date = sdf.format(now);
		return date;
	}

	// 2015年6月15日->2015-06-15，format: yyyy年M月dd日，toFormat: yyyy-MM-dd
	public static String stringDateToString(String format, String toFormat, String dateString) {
		ParsePosition position = new ParsePosition(0);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
		SimpleDateFormat StringDateFormat = new SimpleDateFormat(toFormat);
		Date dateValue = null;
		String data = "";
		try {
			dateValue = simpleDateFormat.parse(dateString, position);
			data = StringDateFormat.format(dateValue);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

	public static String stringDateToString(String format, String dateString) {
		ParsePosition position = new ParsePosition(0);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
		SimpleDateFormat StringDateFormat = new SimpleDateFormat(format);
		Date dateValue = null;
		String data = "";
		try {
			dateValue = simpleDateFormat.parse(dateString, position);
			data = StringDateFormat.format(dateValue);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

	public final static String dateToString(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		String date = null;
		try {
			date = sdf.format(d);
		} catch (Exception e) {
			date = "";
		}
		return date;
	}

	// 返回当前日期 DateType:0="YYYY年MM月DD�?"，DateType:1="YYYY-MM-DD"�?
	public static String GetDate(int dateType) {
		String Year, Month, Day;

		final Calendar c = Calendar.getInstance();
		Year = Integer.toString(c.get(Calendar.YEAR));

		if ((c.get(Calendar.MONTH) + 1) < 10)
			Month = "0" + Integer.toString(c.get(Calendar.MONTH) + 1);
		else
			Month = Integer.toString(c.get(Calendar.MONTH) + 1);
		if (c.get(Calendar.DAY_OF_MONTH) < 10)
			Day = "0" + Integer.toString(c.get(Calendar.DAY_OF_MONTH));
		else
			Day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));

		if (dateType == 0)
			return Year + "年" + Month + "月" + Day + "日";
		else
			return Year + "-" + Month + "-" + Day;
	}

	// 返回当前时间
	public static String GetTime(int dateType) {
		String Hour, Minute, Second;
		final Calendar c = Calendar.getInstance();
		Hour = Integer.toString(c.get(Calendar.HOUR));
		Minute = Integer.toString(c.get(Calendar.MINUTE));
		Second = Integer.toString(c.get(Calendar.SECOND));
		if (dateType == 0)
			return Hour + "时" + Minute + "分" + Second + "秒";
		else
			return Hour + ":" + Minute + ":" + Second;
	}

	// 返回年�?�月、日、时、分、秒
	public static String GetDateTime(int dateType) {
		String Hour, Minute, Second, Year, Month, Day;
		final Calendar c = Calendar.getInstance();
		Year = Integer.toString(c.get(Calendar.YEAR));
		Hour = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
		Minute = Integer.toString(c.get(Calendar.MINUTE));
		Second = Integer.toString(c.get(Calendar.SECOND));
		if ((c.get(Calendar.MONTH) + 1) < 10)
			Month = "0" + Integer.toString(c.get(Calendar.MONTH) + 1);
		else
			Month = Integer.toString(c.get(Calendar.MONTH) + 1);
		if (c.get(Calendar.DAY_OF_MONTH) < 10)
			Day = "0" + Integer.toString(c.get(Calendar.DAY_OF_MONTH));
		else
			Day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
		if (dateType == 0)
			return Year + "年" + Month + "月" + Day + "日" + Hour + "时" + Minute + "分" + Second + "秒";
		else
			return Year + "-" + Month + "-" + Day + " " + Hour + ":" + Minute + ":" + Second;
	}

	public final static Date stringToDate(String dateString) {
		ParsePosition position = new ParsePosition(0);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Date dateValue = simpleDateFormat.parse(dateString, position);
		return dateValue;
	}

	// 比较日期是否在有效期�?
	public static Boolean CompareDate(String BeforeDate, String LastDate) {
		Date beforeDate, nowDate, lastDate;

		beforeDate = stringToDate(BeforeDate);
		nowDate = stringToDate(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
		lastDate = stringToDate(LastDate);
		if (beforeDate.getTime() < nowDate.getTime() && lastDate.getTime() > nowDate.getTime()) {
			return true;
		}
		return false;
	}
	
	public static long getCompareDate(Date first, Date last, int DateType){
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(first);
		cal2.setTime(last);

		long milliseconds1 = cal1.getTimeInMillis();
		long milliseconds2 = cal2.getTimeInMillis();
		long diff = milliseconds2 - milliseconds1;
		long diffSeconds = diff / 1000;
		long diffMinutes = diff / (60 * 1000);
		long diffHours = diff / (60 * 60 * 1000);
		long diffDays = diff / (24 * 60 * 60 * 1000);

		switch (DateType) {
		case 0:
			return diffSeconds; // 返回秒
		case 1:
			return diffMinutes; // 返回分
		case 2:
			return diffHours; // 返回小时
		case 3:
			return diffDays; // 返回天数
		default:
			return -1;
		}
	}

	// 比较2个日期相差多少天
	public static long CompareDate(String Date1, String Date2, int DateType) {
		SimpleDateFormat format = new SimpleDateFormat("MM月dd", Locale.getDefault());
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(Date1);
			d2 = format.parse(Date2);

			return getCompareDate(d1,d2,DateType);
		} catch (Exception e) {
			return -1;
		}
	}
}
