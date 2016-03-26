package com.hw.common.utils.basicUtils;

import com.alibaba.fastjson.JSON;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {
	// SharedPreference文件的文件名
	public final static String XML_Settings = "settings";
	private static SharedPreferences settings;

	/**
	 * @category 保存String键�?�对
	 * 
	 * @param context
	 *            上下�?
	 * @param key
	 *            �?
	 * @param value
	 *            �?
	 */
	public static void saveSharedPreString(Context context, String key, String value) {
		settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
		settings.edit().putString(key, value).commit();
	}

	/**
	 * @category 获取String键�?�对
	 * 
	 * @param context
	 *            上下�?
	 * @param key
	 *            �?
	 * @param value
	 *            �?
	 */
	public static String getSharedPreString(Context context, String key) {
		settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
		return settings.getString(key, "");
	}

	/**
	 * @category 获取boolean�?
	 * 
	 * @param context
	 *            上下�?
	 * @param key
	 *            �?
	 * @return
	 */
	public static boolean getSharedPreBoolean(Context context, String key) {
		settings = context.getSharedPreferences(XML_Settings, 0);
		return settings.getBoolean(key, false);
	}

	/**
	 * @category 保存boolean键�?�对
	 * 
	 * @param context
	 *            上下�?
	 * @param key
	 *            �?
	 * @param value
	 *            �?
	 */
	public static void saveSharedPreBoolean(Context context, String key, boolean value) {
		settings = context.getSharedPreferences(XML_Settings, 0);
		settings.edit().putBoolean(key, value).commit();
	}

	/**
	 * @category 保存int类型的�?�到SharedPreference配置文件�?
	 * @param context
	 *            上下�?
	 * @param key
	 *            �?
	 * @param value
	 *            �?
	 */
	public static void saveSharedPreInteger(Context context, String key, int value) {
		settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
		settings.edit().putInt(key, value).commit();
	}

	public static void saveSharedPreObject(Context context, String key, Object value) {
		settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
		try {
			saveSharedPreString(context, key, JSON.toJSONString(value));
		} catch (Exception e) {
			saveSharedPreString(context, key, "");
		}

	}

	public static <T> T getSharedPreObject(Context context, String key, Class<T> t) {
		settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
		try {
			return JSON.parseObject(getSharedPreString(context, key), t);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * @category 从SharedPreference配置文件中获取int类型的�??
	 * @param context
	 *            上下�?
	 * @param key
	 *            �?
	 * @return 返回int类型的value�?
	 */
	public static int getSharedPreInteger(Context context, String key) {
		settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
		return settings.getInt(key, 0);
	}

	/**
	 * @category 清空SharedPreference中的�?有String类型的数�?
	 * 
	 * @param context
	 *            上下�?
	 */
	public static void clearSave(Context context) {
		settings = context.getSharedPreferences(XML_Settings, 0);
		for (String name : settings.getAll().keySet()) {
			saveSharedPreString(context, name, "");
		}
	}

}
