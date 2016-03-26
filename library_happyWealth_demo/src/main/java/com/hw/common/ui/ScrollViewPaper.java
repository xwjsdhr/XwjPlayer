package com.hw.common.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ScrollViewPaper extends ViewPager{

	public ScrollViewPaper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	 @Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int height = 0;
			for (int i = 0; i < getChildCount(); i++) {
				View child = getChildAt(i);
				child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				int h = child.getMeasuredHeight();
				if (h > height)
					height = h;
			}

			heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	    

}
