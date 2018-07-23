package com.hcb.hcbsdk.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * @ClassName: SharedPreferencesHelper
 * @Description: SharedPreferences帮助类
 * @author wangguowei
 * @date 2012-7-6 下午3:29:12
 */
public class SPHelper {

	/**
	 * 插入参数
	 *
	 * @param context
	 * @param fileName
	 * @param mode
	 * @param key
	 * @param value
	 */
	public static void putValue(Context context, String fileName, int mode,
								String key, String value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(
				fileName, mode).edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void putValue(Context context, String key, String value) {
		putValue(context, "config", Context.MODE_PRIVATE, key, value);
	}

	/**
	 * 插入参数
	 *
	 * @param context
	 * @param fileName
	 * @param mode
	 * @param key
	 * @param value
	 */
	public static void putValue(Context context, String fileName, int mode,
								String key, Boolean value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(
				fileName, mode).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 获得参数
	 *
	 * @param context
	 * @param fileName
	 * @param mode
	 * @param key
	 */
	public static String getValue(Context context, String fileName, int mode,
								  String key) {
		return context.getSharedPreferences(fileName, mode).getString(key, "");
	}

	public static String getValue(Context context, String key) {
		return context.getSharedPreferences("config", Context.MODE_PRIVATE).getString(key, "");
	}

	/**
	 * 获得参数
	 *
	 * @param context
	 * @param fileName
	 * @param mode
	 * @param key
	 */
	public static String getValue(Context context, String fileName, int mode,
								  String key, String defaultValue) {
		return context.getSharedPreferences(fileName, mode).getString(key, defaultValue);
	}

	/**
	 * 清空参数
	 *
	 * @param context
	 * @param fileName
	 * @param mode
	 */
	public static void emptyFile(Context context, String fileName, int mode) {
		context.getSharedPreferences(fileName, mode).edit().clear().commit();
	}

	/**
	 * 获得所以参数
	 *
	 * @param context
	 * @param fileName
	 * @param mode
	 * @return
	 */
	public static Map getAllByFileName(Context context, String fileName,
									   int mode) {
		return context.getSharedPreferences(fileName, mode).getAll();
	}
}
