package com.hw.common.ui.marquee.library;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 */
@SuppressLint("NewApi")
public class ColorAnimationView extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
	private  final int RED = 0xffFF8080;
	private  final int BLUE = 0xff3fb8d6;
	private  final int LIGHT_BLUE = 0xff3eb4ff;
	private  final int WHITE = 0xffffffff;
	private  final int GREEN = 0xff2ec49b;
	private  final int DURATION = 10000;
	ValueAnimator colorAnim = null;

	private PageChangeListener mPageChangeListener;

	ViewPager.OnPageChangeListener onPageChangeListener;

	public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
		this.onPageChangeListener = onPageChangeListener;
	}

	/**
	 * 这是你唯一需要关心的方法
	 * 
	 * @param mViewPager
	 *            你必须在设置 Viewpager 的 Adapter 这后，才能调用这个方法。
	 * @param obj
	 *            ,这个obj实现了 ColorAnimationView.OnPageChangeListener ，实现回调
	 * @param count
	 *            ,viewpager孩子的数量
	 * @param colors
	 *            int... colors ，你需要设置的颜色变化值~~ 如何你传人 空，那么触发默认设置的颜色动画
	 * */
	public void setmViewPager(ViewPager mViewPager, int count, int... colors) {
		// this.mViewPager = mViewPager;
		if (mViewPager.getAdapter() == null) {
			throw new IllegalStateException("ViewPager does not have adapter instance.");
		}
		mPageChangeListener.setViewPagerChildCount(count);

		mViewPager.setOnPageChangeListener(mPageChangeListener);
		if (colors.length == 0) {
			createDefaultAnimation();
		} else {
			createAnimation(colors);
		}

	}

	public ColorAnimationView(Context context) {
		this(context, null, 0);

	}

	public ColorAnimationView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ColorAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mPageChangeListener = new PageChangeListener();
	}

	private void seek(long seekTime) {
		if (colorAnim == null) {
			createDefaultAnimation();
		}
		colorAnim.setCurrentPlayTime(seekTime);
	}

	private void createAnimation(int... colors) {
		if (colorAnim == null) {
			colorAnim = ObjectAnimator.ofInt(this, "backgroundColor", colors);
			colorAnim.setEvaluator(new ArgbEvaluator());
			colorAnim.setDuration(DURATION);
			colorAnim.addUpdateListener(this);
		}
	}

	private void createDefaultAnimation() {
//		colorAnim = ObjectAnimator.ofInt(this, "backgroundColor", WHITE, RED, BLUE, GREEN, WHITE);
		colorAnim = ObjectAnimator.ofInt(this, "backgroundColor", LIGHT_BLUE, RED, BLUE, GREEN);
		colorAnim.setEvaluator(new ArgbEvaluator());
		colorAnim.setDuration(DURATION);
		colorAnim.addUpdateListener(this);
	}

	@Override
	public void onAnimationStart(Animator animation) {

	}

	@Override
	public void onAnimationEnd(Animator animation) {
	}

	@Override
	public void onAnimationCancel(Animator animation) {

	}

	@Override
	public void onAnimationRepeat(Animator animation) {

	}

	@Override
	public void onAnimationUpdate(ValueAnimator animation) {
		invalidate();
		// long playtime = colorAnim.getCurrentPlayTime();
	}

	private class PageChangeListener implements ViewPager.OnPageChangeListener {

		private int viewPagerChildCount;

		public void setViewPagerChildCount(int viewPagerChildCount) {
			this.viewPagerChildCount = viewPagerChildCount;
		}

		public int getViewPagerChildCount() {
			return viewPagerChildCount;
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			int count = getViewPagerChildCount() - 1;
			if (count != 0) {
				float length = (position + positionOffset) / count;
				int progress = (int) (length * DURATION);
				ColorAnimationView.this.seek(progress);
			}
			// call the method by default
			if (onPageChangeListener != null) {
				onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
			}

		}

		public void onPageSelected(int position) {
			if (onPageChangeListener != null) {
				onPageChangeListener.onPageSelected(position);
			}
		}

		public void onPageScrollStateChanged(int state) {
			if (onPageChangeListener != null) {
				onPageChangeListener.onPageScrollStateChanged(state);
			}
		}
	}
}
