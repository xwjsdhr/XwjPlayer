package com.hw.common.utils.basicUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * To get the HEX,SHA1,Unicode of String
 *
 */
public class StringUtils {
	public static String NullToStr(String str,String rtn){
		if(StringUtils.isEmpty(str)){
			return rtn;
		}
		return str;
	}
	
	public static int getStringUnicodeLength(String value) {
		int valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		// Judge the unicodelength is 1 or 2
		for (int i = 0; i < value.length(); i++) {

			String temp = value.substring(i, i + 1);

			if (temp.matches(chinese)) {

				valueLength += 2;
			} else {

				valueLength += 1;
			}
		}

		return valueLength;
	}

	public static String alignString(String initString, int requireLength) {
		int stringLength = getStringUnicodeLength(initString);
		String transString = initString;
		try {
			if (requireLength > stringLength)
				for (int i = 0; i < (requireLength - stringLength); i++) {
					transString = transString + "  ";
				}
			// if ((requireLength < stringLength)&&requireLength>=2) {
			// transString = initString.substring(0, requireLength/2 - 1) +
			// "..";
			// }
			// transString=String.format("%-12s",initString);

		} catch (Exception e) {
			MLogUtil.d(initString + "    " + stringLength + "   " + requireLength + "    " + transString);
			e.printStackTrace();
			MLogUtil.e(e);
		}
		return transString;
	}

	public static String alignStrings(String initString, int requireLength) {
		int stringLength = getStringUnicodeLength(initString);
		String transString = initString;
		try {
			if (requireLength > stringLength)
				for (int i = 0; i < (requireLength - stringLength); i++) {
					transString = transString + "  ";
				}
			if ((requireLength < stringLength) && requireLength >= 2) {
				transString = initString.substring(0, requireLength - 1) + "..";
			}
			// transString=String.format("%-12s",initString);

		} catch (Exception e) {
			MLogUtil.d(initString + "    " + stringLength + "   " + requireLength + "    " + transString);
			e.printStackTrace();
			MLogUtil.e(e);
		}
		return transString;
	}

	public static String inputStreamToString(InputStream inputStream) {
		StringBuilder total = new StringBuilder();
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));

			String line;
			while ((line = r.readLine()) != null) {
				total.append(line);
			}
		} catch (IOException e) {
			MLogUtil.e(e);
		}
		return total.toString();
	}

	public static String HtmlToText(String s) {
		if (!isEmpty(s)) {
			String str = s.replaceAll("</?[a-zA-Z]+[^><]*>", "");
			str = str.replaceAll("&nbsp;", " "); // 将&nbsp;替换为一个空格
			str = str.replaceAll("&mdash;", "－");// 将&mdash;替换为-
			str = str.replaceAll("&rdquo;", "”");
			str = str.replaceAll("&ldquo;", "“");
			str = str.replaceAll("&le;", "<=");
			str = str.replaceAll("&ne;", "!=");
			str = str.replaceAll("&ge;", ">=");

			return str;
		} else {
			return s;
		}
	}

	public static String decodeHtml(String str) {
		String rst = str;
		rst = rst.replaceAll("&lt;", "<");
		rst = rst.replaceAll("&gt;", ">");
		rst = rst.replaceAll("&quot;", "\"");
		rst = rst.replaceAll("&amp;", "&");
		return rst;
	}

	public static String SHA1(String s) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();
			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String toHexString(byte[] keyData) {
		if (keyData == null) {
			return null;
		}
		int expectedStringLen = keyData.length * 2;
		StringBuilder sb = new StringBuilder(expectedStringLen);
		for (int i = 0; i < keyData.length; i++) {
			String hexStr = Integer.toString(keyData[i] & 0x00FF, 16);
			if (hexStr.length() == 1) {
				hexStr = "0" + hexStr;
			}
			sb.append(hexStr);
		}
		return sb.toString();
	}

	/**
	 * @param str
	 * @return if string is null or its size is 0 or it is made by space, return
	 *         true, else return false.
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.trim().length() == 0 || str.equals("null") || str.trim().equals(""));
	}

	public static boolean ifStringInList(String s, ArrayList<String> arrayList) {
		if (BasicUtils.judgeNotNull(s) && BasicUtils.judgeNotNull(arrayList)) {
			for (String str : arrayList) {
				if (str.trim().contains(s))
					return true;
			}
		}

		return false;
	}

	public static boolean ifStringExactlyInList(String s, ArrayList<String> arrayList) {
		if (BasicUtils.judgeNotNull(s) && BasicUtils.judgeNotNull(arrayList)) {
			for (String str : arrayList) {
				if (str.equals(s))
					return true;
			}
		}

		return false;
	}
}
