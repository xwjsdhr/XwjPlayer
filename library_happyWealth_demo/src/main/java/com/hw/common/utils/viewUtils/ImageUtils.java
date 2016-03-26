package com.hw.common.utils.viewUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.view.View;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

/**
 * Some effect of images
 */
public class ImageUtils {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int TOP = 3;
    public static final int BOTTOM = 4;

    public static Bitmap scaleWithWH(Bitmap src, double w, double h) {
		if (w == 0 || h == 0 || src == null) {
			return src;
		} else {
			// 记录src的宽高
			int width = src.getWidth();
			int height = src.getHeight();
			// 创建一个matrix容器
			Matrix matrix = new Matrix();
			// 计算缩放比例
			float scaleWidth = (float) (w / width);
			float scaleHeight = (float) (h / height);
			// 开始缩放
			matrix.postScale(scaleWidth, scaleHeight);
			// 创建缩放后的图片
			Bitmap result = null;
			try {  
				result = Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
		    } catch (OutOfMemoryError e) {  
		        while(result == null) {  
		            System.gc();  
		            System.runFinalization();  
		            result = Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
		        }  
		    }  
			return result;
		}
	}

	public static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text) {
		Resources resources = context.getResources();
		float scale = resources.getDisplayMetrics().density;

		bitmap = scaleWithWH(bitmap, 300 * scale, 300 * scale);

		android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

		// set default bitmap config if none
		if (bitmapConfig == null) {
			bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		}
		// resource bitmaps are imutable,
		// so we need to convert it to mutable one
		bitmap = bitmap.copy(bitmapConfig, true);

		Canvas canvas = new Canvas(bitmap);
		// new antialised Paint
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// text color - #3D3D3D
		paint.setColor(Color.RED);
		paint.setTextSize((int) (18 * scale));
		paint.setDither(true); // 获取跟清晰的图像采样
		paint.setFilterBitmap(true);// 过滤一些
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		int x = 30;
		int y = 30;
		canvas.drawText(text, x * scale, y * scale, paint);
		return bitmap;
	}
    
    /**
     * 图片去色,锟斤拷锟截灰讹拷图片
     *
     * @param bmpOriginal 锟斤拷锟斤拷锟酵计�
     * @return 去色锟斤拷锟酵计�
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }


    /**
     * 锟斤拷图片锟斤拷锟皆诧拷锟�
     *
     * @param bitmap 锟斤拷要锟睫改碉拷图片
     * @param pixels 圆锟角的伙拷锟斤拷
     * @return 圆锟斤拷图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap
                .createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 水印
     *
     * @param
     * @return
     */
    public static Bitmap createBitmapForWatermark(Bitmap src, Bitmap watermark) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 锟斤拷锟斤拷一锟斤拷锟铰的猴拷SRC锟斤拷锟饺匡拷锟揭伙拷锟斤拷位图
        Canvas cv = new Canvas(newb);
        // draw src into
        cv.drawBitmap(src, 0, 0, null);// 锟斤拷 0锟斤拷0锟斤拷昕硷拷锟斤拷锟絪rc
        // draw watermark into
        cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 锟斤拷src锟斤拷锟斤拷锟铰角伙拷锟斤拷水印
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 锟斤拷锟斤拷
        // store
        cv.restore();// 锟芥储
        return newb;
    }

    /**
     * 图片锟较筹拷
     *
     * @return
     */
    public static Bitmap potoMix(int direction, Bitmap... bitmaps) {
        if (bitmaps.length <= 0) {
            return null;
        }
        if (bitmaps.length == 1) {
            return bitmaps[0];
        }
        Bitmap newBitmap = bitmaps[0];
// newBitmap = createBitmapForFotoMix(bitmaps[0],bitmaps[1],direction);
        for (int i = 1; i < bitmaps.length; i++) {
            newBitmap = createBitmapForFotoMix(newBitmap, bitmaps[i], direction);
        }
        return newBitmap;
    }

    private static Bitmap createBitmapForFotoMix(Bitmap first, Bitmap second, int direction) {
        if (first == null) {
            return null;
        }
        if (second == null) {
            return first;
        }
        int fw = first.getWidth();
        int fh = first.getHeight();
        int sw = second.getWidth();
        int sh = second.getHeight();
        Bitmap newBitmap = null;
        if (direction == LEFT) {
            newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, sw, 0, null);
            canvas.drawBitmap(second, 0, 0, null);
        } else if (direction == RIGHT) {
            newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, 0, null);
            canvas.drawBitmap(second, fw, 0, null);
        } else if (direction == TOP) {
            newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, sh, null);
            canvas.drawBitmap(second, 0, 0, null);
        } else if (direction == BOTTOM) {
            newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, 0, null);
            canvas.drawBitmap(second, 0, fh, null);
        }
        return newBitmap;
    }

    // 锟皆分憋拷锟绞较达拷锟酵计拷锟斤拷锟斤拷锟斤拷锟�
    public static Bitmap zoomBitmap(Bitmap bitmap, float width, float height) {

        int w = bitmap.getWidth();

        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();

        float scaleWidth = ((float) width / w);

        float scaleHeight = ((float) height / h);

        matrix.postScale(scaleWidth, scaleHeight);// 锟斤拷锟矫撅拷锟斤拷锟斤拷锟斤拷锟斤拷挪锟斤拷锟斤拷锟斤拷锟节达拷锟斤拷锟�

        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);

        return newbmp;

    }

    /**
     * 锟斤拷锟斤拷图片为PNG
     *
     * @param bitmap
     * @param name
     */
    public static void savePNG_After(Bitmap bitmap, String name) {
        File file = new File(name);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 锟斤拷锟斤拷图片为JPEG
     *
     * @param bitmap
     * @param path
     */
    public static void saveJPGE_After(Bitmap bitmap, int quality, String path) {
        File file = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static Bitmap revitionImageSize(String path, int width) throws IOException {

        return revitionImageSize(path, width, width);
    }

    public static Bitmap revitionImageSize(String path) throws IOException {

        return revitionImageSize(path, 1000, 1000);
    }

    public static Bitmap revitionImageSize(String path, int width, int height) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= width)
                    && (options.outHeight >> i <= height)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    public static Bitmap convertViewToBitmapWithCanvas(View view, int bitmapWidth, int bitmapHeight) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }
}
