package com.hw.common.ui;

import android.widget.GridView;

public class NoSvGridView extends GridView {
	public NoSvGridView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
		this.setFocusable(false);
	}

	/**
	 * 设置不滚动
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}