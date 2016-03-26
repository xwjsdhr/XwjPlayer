package com.hw.common.utils.basicUtils;

import android.util.Log;

public class MLogUtil {
	private static Boolean isDebug = true;
	
	public static void e(Object msg) {
		if (getIsDebug()) {
			Log.e("========error========", msg + "");
		}

	}

	public static void d(Object msg) {
		if (getIsDebug()) {
			Log.d("========debug========", msg + "");
		}
	}

	public static Boolean getIsDebug() {
		return isDebug;
	}

	public static void setDebug(Boolean isDebug) {
		MLogUtil.isDebug = isDebug;
	}
}
