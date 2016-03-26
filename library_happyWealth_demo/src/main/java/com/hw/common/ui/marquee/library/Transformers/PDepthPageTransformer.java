package com.hw.common.ui.marquee.library.Transformers;

import com.nineoldandroids.view.ViewHelper;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

public class PDepthPageTransformer implements PageTransformer  {
	private static final float MIN_SCALE = 0.75f;

	public void transformPage(View view, float position) {
		if (position <= 0f) {
            ViewHelper.setTranslationX(view,0f);
            ViewHelper.setScaleX(view,1f);
            ViewHelper.setScaleY(view,1f);
		} else if (position <= 1f) {
			final float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            ViewHelper.setAlpha(view,1-position);
            ViewHelper.setPivotY(view,0.5f * view.getHeight());
            ViewHelper.setTranslationX(view,view.getWidth() * - position);
            ViewHelper.setScaleX(view,scaleFactor);
            ViewHelper.setScaleY(view,scaleFactor);
		}
	}
}
