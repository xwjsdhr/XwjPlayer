package com.hw.common.utils.basicUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class MathUtil {
	// 两点距离
	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.abs(x1 - x2) * Math.abs(x1 - x2) + Math.abs(y1 - y2) * Math.abs(y1 - y2));
	}

	public static double pointTotoDegrees(double x, double y) {
		return Math.toDegrees(Math.atan2(x, y));
	}

	public static boolean checkInRound(float sx, float sy, float r, float x, float y) {
		return Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y)) < r;
	}
	
	// 字符串转float
	public static float stringToFloat(String str){
		float num = 0;
		NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
		try {
			Number parsedNumber = nf.parse(str);
			num = parsedNumber.floatValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return num;
	}
	
	public static int stringToInt(String str){
		int num = 0;
		NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
		try {
			Number parsedNumber = nf.parse(str);
			num = parsedNumber.intValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return num;
	}
}