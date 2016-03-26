package com.hw.common.ui.risenumber;

import java.text.DecimalFormat;

import com.hw.common.utils.basicUtils.MLogUtil;
import com.nineoldandroids.animation.ValueAnimator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lee on 2014/7/29.
 * Android实现余额宝金额变动动画效果
 */
public class RiseNumberTextView extends TextView implements RiseNumberBase {

	private static final int STOPPED = 0;

	private static final int RUNNING = 1;

	private int mPlayingState = STOPPED;

	private float number;

	private float fromNumber;

	private long duration = 1500;
	/**
	 * 1.int 2.float
	 */
	private int numberType = 2;

	private DecimalFormat fnum;

	private EndListener mEndListener = null;

	final static int[] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE };

	public RiseNumberTextView(Context context) {
		super(context);
	}

	public RiseNumberTextView(Context context, AttributeSet attr) {
		super(context, attr);
	}

	public RiseNumberTextView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
	}

	public interface EndListener {
		public void onEndFinish();
	}

	public boolean isRunning() {
		return (mPlayingState == RUNNING);
	}

	private void runFloat() {
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(fromNumber, number);
		valueAnimator.setDuration(duration);

		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {

				setText(fnum.format(Float.parseFloat(valueAnimator.getAnimatedValue().toString())));
				if (valueAnimator.getAnimatedFraction() >= 1) {
					mPlayingState = STOPPED;
					if (mEndListener != null)
						mEndListener.onEndFinish();
				}
			}

		});
		valueAnimator.start();
	}

	private void runInt() {
		ValueAnimator valueAnimator = ValueAnimator.ofInt((int) fromNumber, (int) number);
		valueAnimator.setDuration(duration);

		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {

				setText(valueAnimator.getAnimatedValue().toString());
				if (valueAnimator.getAnimatedFraction() >= 1) {
					mPlayingState = STOPPED;
					if (mEndListener != null)
						mEndListener.onEndFinish();
				}
			}
		});
		valueAnimator.start();
	}

	static int sizeOfInt(int x) {
		for (int i = 0;; i++)
			if (x <= sizeTable[i])
				return i + 1;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		fnum = new DecimalFormat("##0.00");
	}

	@Override
	public void start() {

		if (!isRunning()) {
			mPlayingState = RUNNING;
			if (numberType == 1)
				runInt();
			else if(numberType == 2)
				runFloat();
		}
	}

	public RiseNumberTextView withNumber(String str) {
		try {
			this.number = Float.valueOf(str);
			numberType = 2;
			if (this.number > 1000) {
				fromNumber = this.number - (float) Math.pow(10, sizeOfInt((int) this.number) - 2);
			} else {
				fromNumber = this.number * 8 / 10;
			}
		} catch (Exception e) {
			setText(str);
		}
		MLogUtil.e("fromNumber  "+fromNumber);
		return this;
	}

	public RiseNumberTextView withNumber(float number) {
		this.number = number;
		numberType = 2;
		if (number > 1000) {
			fromNumber = number - (float) Math.pow(10, sizeOfInt((int) number) - 2);
		} else {
			fromNumber = number / 2;
		}

		return this;
	}

	@Override
	public RiseNumberTextView withNumber(int number) {
		this.number = number;
		numberType = 1;
		if (number > 1000) {
			fromNumber = number - (float) Math.pow(10, sizeOfInt((int) number) - 2);
		} else {
			fromNumber = number / 2;
		}

		return this;

	}

	public RiseNumberTextView setDuration(long duration) {
		this.duration = duration;
		return this;
	}

	public void setOnEnd(EndListener callback) {
		mEndListener = callback;
	}
}
