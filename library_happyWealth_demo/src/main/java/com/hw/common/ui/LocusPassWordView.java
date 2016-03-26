package com.hw.common.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.hw.common.R;
import com.hw.common.utils.basicUtils.MathUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class LocusPassWordView extends View {
	private float width = 0;
	private float height = 0;
	private boolean isCache = false;
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Point[][] mPoints = new Point[3][3];
	private float dotRadius = 0;
	private List<Point> sPoints = new ArrayList<Point>();
	private boolean checking = false;
	private long CLEAR_TIME = 1000;
	private int pwdMinLen = 4; //密码最小长度
	private boolean isTouch = true;
	
	private Paint arrowPaint;
	private Paint linePaint;
	private Paint selectedPaint;
	private Paint errorPaint;
	private Paint normalPaint;
	private int errorColor = 0xffff2020; // 出错时圆点的图片
	private int selectedColor = 0xff0596f6; //圆点点击时的图片
	private int lineColor = 0xff0596f6;
	private int outterSelectedColor = 0xff0596f6;
	private int outterErrorColor = 0xffff2020;
	private int dotColor = 0x90ffffff; // 初始时内圆环颜色
	private int outterDotColor = 0x90ffffff; // 初始时外圆环颜色
	private int dotNormalFillColor,dotSelectedFillColor; // 内圆实心颜色
	private int outterDotNormalFillColor,outterDotSelectedFillColor; // 外圆实心颜色

	public LocusPassWordView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LocusPassWordView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LocusPwd);
		errorColor = a.getColor(R.styleable.LocusPwd_lock_error_color, 0xffff2020);
		selectedColor = a.getColor(R.styleable.LocusPwd_lock_selected_color, 0xff0596f6);
		outterSelectedColor = a.getColor(R.styleable.LocusPwd_lock_out_selected_color, 0xff0596f6);
		outterErrorColor = a.getColor(R.styleable.LocusPwd_lock_out_error_color, 0xffff2020);
		dotColor = a.getColor(R.styleable.LocusPwd_lock_dot_color, 0xffff2020);
		outterDotColor = a.getColor(R.styleable.LocusPwd_lock_out_dot_color, 0x90ffffff);
		lineColor = a.getColor(R.styleable.LocusPwd_lock_line_color, 0xff0596f6);
		dotNormalFillColor = a.getColor(R.styleable.LocusPwd_lock_dot_fill_n_color, -999);
		dotSelectedFillColor = a.getColor(R.styleable.LocusPwd_lock_dot_fill_p_color, -999);
		outterDotNormalFillColor = a.getColor(R.styleable.LocusPwd_lock_out_dot_fill_n_color, -999);
		outterDotSelectedFillColor = a.getColor(R.styleable.LocusPwd_lock_out_dot_fill_p_color, -999);
		a.recycle();
	}

	public LocusPassWordView(Context context) {
		super(context);
	}

	public void onDraw(Canvas canvas) {
		if (!isCache) {
			initCache();
		}
		drawToCanvas(canvas);
	}

	private void initPaints() {
		arrowPaint = new Paint();
 		arrowPaint.setColor(selectedColor);
 		arrowPaint.setStyle(Style.FILL);
 		arrowPaint.setAntiAlias(true);
 		
 		linePaint = new Paint();
 		linePaint.setColor(lineColor);
		linePaint.setStyle(Style.STROKE);
		linePaint.setAntiAlias(true);
		linePaint.setStrokeWidth(dotRadius / 6);
		
		selectedPaint = new Paint(); 
		selectedPaint.setStyle(Style.STROKE);
		selectedPaint.setAntiAlias(true); 
		selectedPaint.setStrokeWidth(2);
//		selectedPaint.setStrokeWidth(dotRadius / 6);
		
		errorPaint = new Paint();
		errorPaint.setStyle(Style.STROKE);
		errorPaint.setAntiAlias(true); 
		errorPaint.setStrokeWidth(2);
//		errorPaint.setStrokeWidth(dotRadius / 6);
		
		normalPaint = new Paint();
		normalPaint.setStyle(Style.STROKE);
		normalPaint.setAntiAlias(true); 
		normalPaint.setStrokeWidth(2); 
//		normalPaint.setStrokeWidth(dotRadius / 9);  
	}
	
	private void drawToCanvas(Canvas canvas) {
		boolean inErrorState = false;
		for (int i = 0; i < mPoints.length; i++) {
			for (int j = 0; j < mPoints[i].length; j++) {
				Point p = mPoints[i][j];
				if (p.state == Point.STATE_CHECK) {
					selectedPaint.setColor(outterSelectedColor);  
					canvas.drawCircle(p.x, p.y, dotRadius, selectedPaint);
					
					if(outterDotSelectedFillColor != -999){
						selectedPaint.setColor(outterDotSelectedFillColor);
						selectedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
						canvas.drawCircle(p.x, p.y, dotRadius - 2, selectedPaint);
					}
					
					selectedPaint.setColor(selectedColor);
					selectedPaint.setStyle(Paint.Style.STROKE);
					canvas.drawCircle(p.x, p.y, dotRadius/4, selectedPaint);
					
					if(dotSelectedFillColor != -999){
						selectedPaint.setColor(dotSelectedFillColor);
						selectedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
						canvas.drawCircle(p.x, p.y, dotRadius/4 - 2, selectedPaint);
					}
				} else if (p.state == Point.STATE_CHECK_ERROR) {
					inErrorState = true;
					errorPaint.setColor(outterErrorColor);
					errorPaint.setStyle(Paint.Style.STROKE);
					canvas.drawCircle(p.x, p.y, dotRadius, errorPaint);
					
					errorPaint.setColor(errorColor);
					errorPaint.setStyle(Paint.Style.STROKE);
					canvas.drawCircle(p.x, p.y, dotRadius/4, errorPaint);
					
					errorPaint.setColor(errorColor);
					errorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
					canvas.drawCircle(p.x, p.y, dotRadius/4 - 2, errorPaint);
					
				} else {
					normalPaint.setColor(outterDotColor);
					normalPaint.setStyle(Paint.Style.STROKE);
					canvas.drawCircle(p.x, p.y, dotRadius, normalPaint);
					if(outterDotNormalFillColor != -999){
						normalPaint.setColor(outterDotNormalFillColor);
						normalPaint.setStyle(Paint.Style.FILL_AND_STROKE);
						canvas.drawCircle(p.x, p.y, dotRadius - 2, normalPaint);
					}
					
					normalPaint.setColor(dotColor);
					normalPaint.setStyle(Paint.Style.STROKE);
					canvas.drawCircle(p.x, p.y, dotRadius/4, normalPaint);
					
					if(dotNormalFillColor != -999){
						normalPaint.setColor(dotNormalFillColor);
						normalPaint.setStyle(Paint.Style.FILL_AND_STROKE);
						canvas.drawCircle(p.x, p.y, dotRadius/4 - 2, normalPaint);
					}
				}
			}
		}
		
		if (inErrorState) {
			arrowPaint.setColor(errorColor);
			linePaint.setColor(errorColor);
		} else {
			arrowPaint.setColor(selectedColor);
			linePaint.setColor(lineColor);
		}

		if (sPoints.size() > 0) {
			int tmpAlpha = mPaint.getAlpha();
			Point tp = sPoints.get(0);
			for (int i = 1; i < sPoints.size(); i++) {
				Point p = sPoints.get(i);
				drawLine(tp, p, canvas, linePaint);
//				drawArrow(canvas, arrowPaint, tp, p, dotRadius/4, 38);
				tp = p;
			}
			if (this.movingNoPoint) {
				drawLine(tp, new Point(moveingX, moveingY, -1), canvas, linePaint);
			}
			mPaint.setAlpha(tmpAlpha);
		}

	}
	
	private void drawLine(Point start, Point end, Canvas canvas, Paint paint) {
		double d = MathUtil.distance(start.x, start.y, end.x, end.y);
		float rx = (float) ((end.x-start.x) * dotRadius / 4 / d);
		float ry = (float) ((end.y-start.y) * dotRadius / 4 / d);
		canvas.drawLine(start.x+rx, start.y+ry, end.x-rx, end.y-ry, paint);
	}
	
	private void drawArrow(Canvas canvas, Paint paint, Point start, Point end, float arrowHeight, int angle) {
		double d = MathUtil.distance(start.x, start.y, end.x, end.y);
		float sin_B = (float) ((end.x - start.x) / d);
		float cos_B = (float) ((end.y - start.y) / d);
		float tan_A = (float) Math.tan(Math.toRadians(angle));
		float h = (float) (d - arrowHeight - dotRadius * 1.1);
		float l = arrowHeight * tan_A;
		float a = l * sin_B;
		float b = l * cos_B;
		float x0 = h * sin_B;
		float y0 = h * cos_B;
		float x1 = start.x + (h + arrowHeight) * sin_B;
		float y1 = start.y + (h + arrowHeight) * cos_B;
		float x2 = start.x + x0 - b;
		float y2 = start.y + y0 + a;
		float x3 = start.x + x0 + b;
		float y3 = start.y + y0 - a;
		Path path = new Path();
		path.moveTo(x1, y1);
		path.lineTo(x2, y2);
		path.lineTo(x3, y3);
		path.close();
		canvas.drawPath(path, paint);
	}

	private void initCache() {
		width = this.getWidth();
		height = this.getHeight();
		float x = 0;
		float y = 0;

		if (width > height) {
			x = (width - height) / 2;
			width = height;
		} else {
			y = (height - width) / 2;
			height = width;
		}
		
		int leftPadding = 15;
		float dotPadding = (float) (width / 3.5 - 0);
		float middleX = width / 2;
		float middleY = height / 2;

		mPoints[0][0] = new Point(x + middleX - dotPadding, y + middleY - dotPadding, 1);
		mPoints[0][1] = new Point(x + middleX, y + middleY - dotPadding, 2);
		mPoints[0][2] = new Point(x + middleX + dotPadding, y + middleY - dotPadding, 3);
		
		mPoints[1][0] = new Point(x + middleX - dotPadding, y + middleY, 4);
		mPoints[1][1] = new Point(x + middleX, y + middleY, 5);
		mPoints[1][2] = new Point(x + middleX + dotPadding, y + middleY, 6);
		
		mPoints[2][0] = new Point(x + middleX - dotPadding, y + middleY + dotPadding, 7);
		mPoints[2][1] = new Point(x + middleX, y + middleY + dotPadding, 8);
		mPoints[2][2] = new Point(x + middleX + dotPadding, y + middleY + dotPadding, 9);
		
		dotRadius = width / 10;
 		isCache = true;
 		
 		initPaints();
	}

	/**
	 *
	 * 
	 * @param index
	 * @return
	 */
	public int[] getArrayIndex(int index) {
		int[] ai = new int[2];
		ai[0] = index / 3;
		ai[1] = index % 3;
		return ai;
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	private Point checkSelectPoint(float x, float y) {
		for (int i = 0; i < mPoints.length; i++) {
			for (int j = 0; j < mPoints[i].length; j++) {
				Point p = mPoints[i][j];
				if (MathUtil.checkInRound(p.x, p.y, dotRadius, (int) x, (int) y)) {
					return p;
				}
			}
		}
		return null;
	}

	/**
	 *
	 */
	private void reset() {
		for (Point p : sPoints) {
			p.state = Point.STATE_NORMAL;
		}
		sPoints.clear();
		this.enableTouch();
	}

	/**
	 *
	 * 
	 * @param p
	 * @return
	 */
	private int crossPoint(Point p) {
		// reset
		if (sPoints.contains(p)) {
			if (sPoints.size() > 2) {
				//
				if (sPoints.get(sPoints.size() - 1).index != p.index) {
					return 2;
				}
			}
			return 1; //
		} else {
			return 0; //
		}
	}

	/**
	 *
	 * 
	 * @param point
	 */
	private void addPoint(Point point) {
		if (sPoints.size() > 0) {
			Point lastPoint = sPoints.get(sPoints.size() - 1);
			int dx = Math.abs(lastPoint.getColNum() - point.getColNum());
			int dy = Math.abs(lastPoint.getRowNum() - point.getRowNum());
			if ((dx > 1 || dy > 1) && (dx == 0 || dy == 0 || dx == dy)) {
//			if ((dx > 1 || dy > 1) && (dx != 2 * dy) && (dy != 2 * dx)) {
				int middleIndex = (point.index + lastPoint.index) / 2 - 1;
				Point middlePoint = mPoints[middleIndex / 3][middleIndex % 3];
				if (middlePoint.state != Point.STATE_CHECK) {
					middlePoint.state = Point.STATE_CHECK;
					sPoints.add(middlePoint);
				}
			}
		}
		this.sPoints.add(point);
	}

	/**
	 *
	 * 
	 * @param points
	 * @return
	 */
	private String toPointString() {
		if (sPoints.size() >= pwdMinLen) {
			StringBuffer sf = new StringBuffer();
			for (Point p : sPoints) {
				sf.append(p.index);
			}
			return sf.toString();
		} else {
			return "";
		}
	}

	boolean movingNoPoint = false;
	float moveingX, moveingY;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isTouch) {
			return false;
		}

		movingNoPoint = false;
		float ex = event.getX();
		float ey = event.getY();
		boolean isFinish = false;
		boolean redraw = false;
		Point p = null;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: //
			if (task != null) {
				task.cancel();
				task = null;
				Log.d("task", "touch cancel()");
			}
			reset();
			p = checkSelectPoint(ex, ey);
			if (p != null) {
				checking = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (checking) {
				p = checkSelectPoint(ex, ey);
				if (p == null) {
					movingNoPoint = true;
					moveingX = ex;
					moveingY = ey;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			p = checkSelectPoint(ex, ey);
			checking = false;
			isFinish = true;
			break;
		}
		if (!isFinish && checking && p != null) {
			int rk = crossPoint(p);
			if (rk == 2) //
			{
				// reset();
				// checking = false;

				movingNoPoint = true;
				moveingX = ex;
				moveingY = ey;

				redraw = true;
			} else if (rk == 0) //
			{
				p.state = Point.STATE_CHECK;
				addPoint(p);
				redraw = true;
			}
			// rk == 1

		}

		//
		if (redraw) {

		}
		if (isFinish) {
			if (this.sPoints.size() == 1) {
				this.reset();
			} else if (sPoints.size() < pwdMinLen ) {
				// mCompleteListener.onPasswordTooMin(sPoints.size());
				error();
				clearPassword();
				Toast.makeText(this.getContext(), "最少连接4个点,请重新输入",
						Toast.LENGTH_SHORT).show();
			} else if (mCompleteListener != null) {
				this.disableTouch();
				mCompleteListener.onComplete(toPointString());
			}
		}
		this.postInvalidate();
		return true;
	}

	/**
	 *
	 */
	private void error() {
		for (Point p : sPoints) {
			p.state = Point.STATE_CHECK_ERROR;
		}
	}

	public void markError() {
		markError(CLEAR_TIME);
	}

	public void markError(final long time) {
		for (Point p : sPoints) {
			p.state = Point.STATE_CHECK_ERROR;
		}
		this.clearPassword(time);
	}

	public void enableTouch() {
		isTouch = true;
	}

	public void disableTouch() {
		isTouch = false;
	}

	private Timer timer = new Timer();
	private TimerTask task = null;


	public void clearPassword() {
		clearPassword(CLEAR_TIME);
	}

	public void clearPassword(final long time) {
		if (time > 1) {
			if (task != null) {
				task.cancel();
			}
			postInvalidate();
			task = new TimerTask() {
				public void run() {
					reset();
					postInvalidate();
				}
			};
			timer.schedule(task, time);
		} else {
			reset();
			postInvalidate();
		}

	}

	//
	private OnCompleteListener mCompleteListener;

	/**
	 * @param mCompleteListener
	 */
	public void setOnCompleteListener(OnCompleteListener mCompleteListener) {
		this.mCompleteListener = mCompleteListener;
	}

	public interface OnCompleteListener {
		
		public void onComplete(String password);
	}
	
	public class Point {
		public static final int STATE_NORMAL = 0;
		public static final int STATE_CHECK = 1; //
		public static final int STATE_CHECK_ERROR = 2; //

		public float x;
		public float y;
		public int state = 0;
		public int index = 0;//

		public Point() {

		}

		public Point(float x, float y, int value) {
	 		this.x = x;
	 		this.y = y;
			index = value;
	 	}
		
		public int getColNum() {
			return (index - 1) % 3;
		}
		
		public int getRowNum() {
			return (index - 1) / 3;
		}

	}
}
