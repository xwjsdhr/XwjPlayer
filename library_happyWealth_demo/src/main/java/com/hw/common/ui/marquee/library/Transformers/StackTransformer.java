package com.hw.common.ui.marquee.library.Transformers;

import com.nineoldandroids.view.ViewHelper;

import android.view.View;

public class StackTransformer extends BaseTransformer {

	@Override
	protected void onTransform(View view, float position) {
		ViewHelper.setTranslationX(view,position < 0 ? 0f : -view.getWidth() * position);
	}

}
