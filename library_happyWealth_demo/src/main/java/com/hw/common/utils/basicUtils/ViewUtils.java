package com.hw.common.utils.basicUtils;

import java.lang.reflect.Field;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ViewUtils {
	
	@SuppressLint("NewApi")
	public static void setTextColor(Context ctx,TextView view,int id){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			view.setTextColor(ctx.getResources().getColor(id, ctx.getTheme()));
		}else {
			view.setTextColor(ctx.getResources().getColor(id));
		}
	}

	@SuppressLint("NewApi")
	public static void setBackgroundDrawable(View view, Drawable drawable) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackgroundDrawable(drawable);
		} else {
			view.setBackground(drawable);
		}
	}
	
	public static void setBackgroundDrawable(Context context,View view, int id) {
		setBackgroundDrawable(view,context.getResources().getDrawable(id));
	}

	// 获取屏幕高度 不包括状态栏
	public static int getViewHeight(Context ctx) {
		return getScreenPoint(ctx).y - getBarHeight(ctx);
	}

	// 获取屏幕宽度
	public static int getViewWidth(Context ctx) {
		return getScreenPoint(ctx).x;
	}

	// 获得屏幕宽高
	public static Point getScreenPoint(Context ctx) {
		DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
		Point point = new Point(dm.widthPixels, dm.heightPixels);
		return point;
	}

	// 获取状态栏高度
	public static int getStatusBarHeight(Activity activity) {
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		return statusBarHeight;
	}

	// 获取标题栏高度
	public static int getContentHeight(Activity activity) {
		int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
		int titleBarHeight = contentTop - getStatusBarHeight(activity);
		return titleBarHeight;
	}

	// 获取状态栏高度 比较准确
	public static int getBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 38;// 默认为38，貌似大部分是这样的

		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = context.getResources().getDimensionPixelSize(x);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return sbar;
	}

	/**
	 * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
	 * 
	 * @param view
	 * @return
	 */
	public static int getViewMeasuredHeight(View view) {
		calcViewMeasure(view);
		return view.getMeasuredHeight();
	}

	/**
	 * 获取控件的宽度，如果获取的宽度为0，则重新计算尺寸后再返回宽度
	 * 
	 * @param view
	 * @return
	 */
	public static int getViewMeasuredWidth(View view) {
		calcViewMeasure(view);
		return view.getMeasuredWidth();
	}

	/**
	 * 测量控件的尺寸
	 * 
	 * @param view
	 */
	public static void calcViewMeasure(View view) {
		int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
		view.measure(width, expandSpec);
	}

	public static int getAllListViewSectionCounts(ListView lv, List dataSource) {
		if (null == lv || dataSource == null || dataSource.size() <= 0) {
			return 0;
		}
		return dataSource.size() + lv.getHeaderViewsCount() + lv.getFooterViewsCount();
	}

	/**
	 * 使用ColorFilter来改变ImageView的亮度
	 * 
	 * @param imageview
	 * @param brightness
	 */
	public static void changeBrightness(ImageView imageview, float brightness) {
		ColorMatrix matrix = new ColorMatrix();
		matrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });
		imageview.setColorFilter(new ColorMatrixColorFilter(matrix));
	}

	/**
	 * sp转换为px
	 * 
	 * @param context
	 * @param spValue
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * sp转换为px
	 * 
	 * @param context
	 * @param spValue
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int dp2px(Context context, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
	}
}
