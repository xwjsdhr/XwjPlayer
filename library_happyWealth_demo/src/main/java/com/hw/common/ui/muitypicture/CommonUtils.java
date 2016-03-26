package com.hw.common.ui.muitypicture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.DisplayMetrics;

public class CommonUtils {
	public static String SDPATH = Environment.getExternalStorageDirectory() + "/EWealth/";
	/**
	 * ����activity
	 */
	public static void launchActivity(Context context, Class<?> activity) {
		Intent intent = new Intent(context, activity);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		context.startActivity(intent);
	}

	/**
	 * ����activity(�����)
	 */
	public static void launchActivity(Context context, Class<?> activity, Bundle bundle) {
		Intent intent = new Intent(context, activity);
		intent.putExtras(bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		context.startActivity(intent);
	}

	/**
	 * ����activity(�����)
	 */
	public static void launchActivity(Context context, Class<?> activity, String key, int value) {
		Bundle bundle = new Bundle();
		bundle.putInt(key, value);
		launchActivity(context, activity, bundle);
	}

	public static void launchActivity(Context context, Class<?> activity, String key, String value) {
		Bundle bundle = new Bundle();
		bundle.putString(key, value);
		launchActivity(context, activity, bundle);
	}

	public static void launchActivityForResult(Activity context, Class<?> activity, int requestCode) {
		Intent intent = new Intent(context, activity);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		context.startActivityForResult(intent, requestCode);
	}
	
	public static void launchActivityForResult(Activity context, Class<?> activity, int requestCode, int maxImage) {
		Intent intent = new Intent(context, activity);
		intent.putExtra(PhotoSelectorActivity.KEY_MAX, maxImage);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		context.startActivityForResult(intent, requestCode);
	}

	public static void launchActivityForResult(Activity activity, Intent intent, int requestCode) {
		activity.startActivityForResult(intent, requestCode);
	}

	/** ����һ������ */
	public static void launchService(Context context, Class<?> service) {
		Intent intent = new Intent(context, service);
		context.startService(intent);
	}

	public static void stopService(Context context, Class<?> service) {
		Intent intent = new Intent(context, service);
		context.stopService(intent);
	}

	/**
	 * �ж��ַ��Ƿ�Ϊ��
	 * @param text
	 * @return true null false !null
	 */
	public static boolean isNull(CharSequence text) {
		if (text == null || "".equals(text.toString().trim()) || "null".equals(text))
			return true;
		return false;
	}

	/** ��ȡ��Ļ��� */
	public static int getWidthPixels(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/** ��ȡ��Ļ�߶� */
	public static int getHeightPixels(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/** ͨ��Uri��ȡͼƬ·�� */
	public static String query(Context context, Uri uri) {
		Cursor cursor = context.getContentResolver().query(uri, new String[] { ImageColumns.DATA }, null, null, null);
		cursor.moveToNext();
		return cursor.getString(cursor.getColumnIndex(ImageColumns.DATA));
	}
	
	public static void saveBitmap(Bitmap bm, String picName) {
		try {
			if (!isFileExist("")) {
				createSDDir("");
			}
			File f = new File(SDPATH, picName);
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}

	public static void delFile(String fileName) {
		File file = new File(SDPATH + fileName);
		if (file.isFile()) {
			file.delete();
		}
		file.exists();
	}

	public static void deleteDir() {
		File dir = new File(SDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete();
			else if (file.isDirectory())
				deleteDir();
		}
		dir.delete();
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

}
