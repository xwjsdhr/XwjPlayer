package com.hw.common.utils.basicUtils;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class CommonUtil {
	// context转activity
	public static Activity contextToActivity(Context cont) {
		if (cont == null)
			return null;
		else if (cont instanceof Activity)
			return (Activity) cont;
		else if (cont instanceof ContextWrapper)
			return contextToActivity(((ContextWrapper) cont).getBaseContext());

		return null;
	}
	
	public static String replaceString(String str){
		if(!StringUtils.isEmpty(str)&&str.contains("T")){
			str = str.replace("T", " ");
		}
		if(!StringUtils.isEmpty(str)&&str.contains(".")){
			str = str.substring(0, str.lastIndexOf(":"));
		}
		
		return str;
	}
	// 2015年6月15日->2015-06-15，format: yyyy年M月dd日，toFormat: yyyy-MM-dd
	public static String stringDateToString(String format,String toFormat,String dateString) {
		ParsePosition position = new ParsePosition(0);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
		SimpleDateFormat StringDateFormat = new SimpleDateFormat(toFormat);
		Date dateValue = null;
		String data = "";
		 try{    
			 dateValue = simpleDateFormat.parse(dateString, position);     
			 data = StringDateFormat.format(dateValue);
	        }catch(Exception e){            
	            e.printStackTrace() ;       
	        }    
		
		return data;
	}
	
	public static String stringDateToString(String format,String dateString) {
		ParsePosition position = new ParsePosition(0);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
		SimpleDateFormat StringDateFormat = new SimpleDateFormat(format);
		Date dateValue = null;
		String data = "";
		 try{    
			 dateValue = simpleDateFormat.parse(dateString, position);     
			 data = StringDateFormat.format(dateValue);
	        }catch(Exception e){            
	            e.printStackTrace() ;       
	        }    
		
		return data;
	}
	
	public static String GetSeparateString(String str,String separate){
		if(!StringUtils.isEmpty(str)&&str.contains(separate)){
			str = str.substring(0, str.lastIndexOf(separate));
		}
		return str;
	}
	
	public static String GetSeparateString(String str,String index1,String index2){
		if(!StringUtils.isEmpty(str)&&str.contains(index1)&&str.contains(index2)){
			str = str.substring(str.indexOf("index1"), str.lastIndexOf(index2));
		}
		return str;
	}
	
	public static String replaceNull(String str){
		if(StringUtils.isEmpty(str)){
			str = " ";
		}
		return str;
	}
	
	 public static Object evaluate(float fraction, Object startValue,
	            Object endValue) {
	        int startInt = (Integer) startValue;
	        int startA = (startInt >> 24) & 0xff;
	        int startR = (startInt >> 16) & 0xff;
	        int startG = (startInt >> 8) & 0xff;
	        int startB = startInt & 0xff;
	        int endInt = (Integer) endValue;
	        int endA = (endInt >> 24) & 0xff;
	        int endR = (endInt >> 16) & 0xff;
	        int endG = (endInt >> 8) & 0xff;
	        int endB = endInt & 0xff;
	        return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
	                | (int) ((startR + (int) (fraction * (endR - startR))) << 16)
	                | (int) ((startG + (int) (fraction * (endG - startG))) << 8)
	                | (int) ((startB + (int) (fraction * (endB - startB))));
	    }

	// 获取设备�?
	public static String getDviceID(Context context){
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
		return tm.getDeviceId();
	}
	
	//sdcard是否可读�? 
	public static boolean IsCanUseSdCard() { 
	    try { 
	        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); 
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	    } 
	    return false; 
	}
	
	// get请求url转成map
	public static Map<String, String> url2Map(String url) {
		url = url.substring(url.lastIndexOf("?")+1);
		String[] params = url.split("&");
		Map<String, String> map = new HashMap<String, String>();
		for (String param : params) {
			map.put(param.split("=")[0], param.split("=")[1]);
		}
		return map;
	}

	
	//获取随机�?
	public static String getRand(int num) {
		String sRand = "";
		Random random = new Random();
		// 取随机产生的认证�?(4位数�?)
		for (int i = 0; i < num; i++) {
			String rand = "";
			// 随机生成数字或�?�字�?
			if (random.nextInt(10) > 5) {
				rand = String.valueOf((char) (random.nextInt(10) + 48));
			} else {
				rand = String.valueOf((char) (random.nextInt(26) + 65));
			}
			sRand += rand;
		}
		return sRand;
	}

	public final static String dateToString(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
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
			return Year + "�?" + Month + "�?" + Day + "�?";
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
			return Hour + "�?" + Minute + "�?" + Second + "�?";
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
			return Year + "�?" + Month + "�?" + Day + "�?" + Hour + "�?" + Minute + "�?" + Second + "�?";
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

	// 比较2个日期相差多少天
	public static long CompareDate(String Date1, String Date2, int DateType) {
		SimpleDateFormat format = new SimpleDateFormat("MM月dd",Locale.getDefault());
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(Date1);
			d2 = format.parse(Date2);

			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal1.setTime(d1);
			cal2.setTime(d2);

			long milliseconds1 = cal1.getTimeInMillis();
			long milliseconds2 = cal2.getTimeInMillis();
			long diff = milliseconds2 - milliseconds1;
			long diffSeconds = diff / 1000;
			long diffMinutes = diff / (60 * 1000);
			long diffHours = diff / (60 * 60 * 1000);
			long diffDays = diff / (24 * 60 * 60 * 1000);

			switch (DateType) {
			case 0:
				return diffSeconds; // 返回�?
			case 1:
				return diffMinutes; // 返回�?
			case 2:
				return diffHours; // 返回小时
			case 3:
				return diffDays; // 返回天数
			default:
				return -1;
			}
		} catch (Exception e) {
			return -1;
		}
	}

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public final static int getVerCode(Context context, String packageName) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
		} catch (NameNotFoundException e) {
		}
		return verCode;
	}

	public static String getNowTime() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
		String date = sdf.format(now);
		return date;
	}

	/**
	 * 获得版本名称
	 */
	public final static String getVerName(Context context, String packageName) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e("版本名称获取异常", e.getMessage());
		}
		return verName;
	}

	public final static boolean isEmpty(String msg) {
		if (msg == null || msg.trim().length() == 0 || msg.trim().equals("")) {
			return true;
		}
		return false;
	}

	public final static String convertFristToUpperCase(String temp) {
		String frist = temp.substring(0, 1);
		String other = temp.substring(1);
		return frist.toUpperCase(Locale.getDefault()) + other;
	}

	public static String toUTF8(String str) {
		String u = str;
		try {
			u = java.net.URLEncoder.encode(u, "UTF-8");
		} catch (Exception e) {

		}
		return u;
	}

	public static Map<String, String> convertBean(Object bean) {
		if (bean == null)
			return null;
		try {
			Class<? extends Object> type = bean.getClass();
			Map<String, String> returnMap = new HashMap<String, String>();
			Field[] fields = type.getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().equals("serialVersionUID")) {
					continue;
				}
				Object result = type.getMethod("get" + convertFristToUpperCase(field.getName())).invoke(bean);
				if (result != null) {
					if(field.getType().getCanonicalName().equals(Date.class)){
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
						result = dateFormat.format(result);
					}else if(field.getType().getCanonicalName().equals(boolean.class)){
						result = type.getMethod("is" + convertFristToUpperCase(field.getName())).invoke(bean);
					}
					returnMap.put(field.getName(), String.valueOf(result));
				}
			}
			return returnMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static boolean equalsType(Field type, Class<?>... cls) {
		for (int i = 0; i < cls.length; i++) {
			Class<?> mClass = cls[i];
			if (type.getType().getCanonicalName().equals(mClass.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public static LinkedHashMap<String, String> convertBeans(Object bean) {
		if (bean == null)
			return null;
		try {
			Class<? extends Object> type = bean.getClass();
			LinkedHashMap<String, String> returnMap = new LinkedHashMap<String, String>();
			Field[] fields = type.getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().equals("serialVersionUID")) {
					continue;
				}
				Object result = type.getMethod("get" + convertFristToUpperCase(field.getName())).invoke(bean);
				if (result != null) {
					if(equalsType(field,Date.class)){
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
						result = dateFormat.format(result);
					}else if(equalsType(field,boolean.class)){
						result = type.getMethod("is" + convertFristToUpperCase(field.getName())).invoke(bean);
					}
					returnMap.put(field.getName(), String.valueOf(result));
				}
			}
			return returnMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 根据随机数来获取不同的布�?
	public static int random(int s) {
		Random r = new Random();
		int i = r.nextInt(s);
		return i;
	}

	// 判断邮箱的函�?
	public static boolean checkEmail(String email) {
		if (email.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) {
			return true;
		} else {
			return false;
		}
	}

	// 判断手机号码
	public static boolean checkphone(String phonenumber) {
		String phone = "\\d{11}";
		Pattern p = Pattern.compile(phone);
		Matcher m = p.matcher(phonenumber);
		return m.matches();
	}

	public static String obj2Str(Object obj) {
		if (obj != null) {
			return String.valueOf(obj);
		} else {
			return null;
		}
	}

	public static int getSDKVersionNumber() {
		int sdkVersion;
		try {
			sdkVersion = android.os.Build.VERSION.SDK_INT;
		} catch (NumberFormatException e) {
			sdkVersion = 0;
		}
		return sdkVersion;
	}
}
